package perfConfig;

import io.restassured.response.ValidatableResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiLoadThreads extends Thread {
    public TestPerformance testPerformance;
    private int threadQnt;
    public static Map<Integer, Long> times;
    public static int failures;

    static {
        times = Collections.synchronizedMap(new HashMap<>());
    }

    public ApiLoadThreads(int numb){
        threadQnt = numb;
    }

    public void runTheadsForAreas(){
        System.out.print("started thread for areas " + threadQnt);
        ValidatableResponse response = given()
                .spec(testPerformance.setupForRestApi())
                .when().get(Endpoints.ALL_AREAS).then();
        if(response.extract().statusCode() != 200)
            failures++;
        times.put(threadQnt, response.extract().time());
    }

    public void runThreadsForTeams(){
        System.out.print("started a thread for teams " + threadQnt);
        ValidatableResponse response = given().spec(testPerformance.setupForRestApi())
                .when().get(Endpoints.ALL_TEAMS).then();
        if(response.extract().statusCode() != 200)
            failures++;
        times.put(threadQnt, response.extract().time());
    }

    public void runThreadsForCompetitions(){
        System.out.print("started a thread for competitions " + threadQnt);
        ValidatableResponse response = given().spec(testPerformance.setupForRestApi())
                .when().get(Endpoints.ALL_COMPETITIONS).then();
        if(response.extract().statusCode() != 200)
            failures++;
        times.put(threadQnt, response.extract().time());
    }
}
