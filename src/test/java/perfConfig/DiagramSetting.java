package perfConfig;

import io.qameta.allure.Attachment;
import jdk.tools.jlink.internal.Platform;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DiagramSetting {
    @Attachment
    public static byte[] toLineChartPict(String title, String seriesName, Map<Integer, Long> data) throws  InterruptedException {
        int upperBound = (data.values().stream().max(Long::compare).get().intValue() / 1000 + 1) * 1000;
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        AtomicBoolean completed = new AtomicBoolean(false);
        AtomicBoolean success = new AtomicBoolean(false);

        SwingUtilities.invokeLater(() ->{
            new JFXPanel();
            Platform.runLater(() -> {
                final NumberAxis xAxis = new NumberAxis(1, data.size(), 1);
                final NumberAxis yAxis = new NumberAxis("Miliseconds", 0, upperBound, 500);
                yAxis.setTickMarkVisible(true);
                final AreaChart<Number, Number> lineChart = new AreaChart<Number, Number>(xAxis, yAxis);
                lineChart.setTiltle(title);
                lineChart.setAnimated(false);
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(seriesName);
                List<Integer> keys = data.keySet().stream().sorted().collect(Collectors.toList());
                for (Integer key : keys)
                    series.getData().add(new XYChart.Data<>(key + 1, data.get(key)));

                lineChart.getData().add(series);
                Scene scene = new Scene (lineChart, 1000, 600);

                WritableImage image = scene.snapshot(null);

                try {
                    ImageIO.write(SwingFXUtils.fromFXimage(image, null), "png", bas);
                    success.set(true);
                    completed.set(true);

                } catch (Exception e){
                    completed.set(true);
                }
            });
        });
       while (!completed.get()) Thread.sleep(500);
       return (success.get()) ? bais.toByteArray() : null;
    }
}
