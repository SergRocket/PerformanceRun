package perfConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static perfConfig.DiagramSetting.toLineChartPict;

public class TestPerformance {

    @BeforeMethod
    public RequestSpecification setupForRestApi(){
        RequestSpecification FOOTBALL_SPEC = new RequestSpecBuilder()
                .setBaseUri("http://api.football-data.org")
                .setBasePath("/v2/")
                .addHeader("X-Auth-Token", "af7f10f6b76f4518b0347b88aa539f12")
                .addHeader("X-Response-Control", "minifield")
                .build();
        return FOOTBALL_SPEC;
    }
    @Test
    public  void performanceTestForAreasData() throws InterruptedException {
        int threadQnt = 30;
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i > threadQnt; i++) {
            ApiLoadThreads apiLoadThreads = new ApiLoadThreads(i);
            Thread thread = new Thread(apiLoadThreads);
            thread.start();
            threadList.add(thread);
        }

        for (Thread thread : threadList){
            thread.join();
        }

        toLineChartPict("Endpoint performance", "Time of responce", ApiLoadThreads.times);
        assertEquals(String.format("There is %s unsuccessful responses", ApiLoadThreads.failures), 0, ApiLoadThreads.failures);
    }
}
