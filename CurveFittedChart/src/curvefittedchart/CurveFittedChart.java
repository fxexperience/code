package curvefittedchart;

import javafx.application.Application;
import javafx.scene.SceneBuilder;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * @author Jasper Potts
 */
public class CurveFittedChart extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override public void start(Stage primaryStage) {
        // create chart and set as center content
        CurvedFittedAreaChart chart = new CurvedFittedAreaChart(
                new NumberAxis(0,10000,2500), new NumberAxis(0,1000,200));
        chart.setLegendVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setAlternativeRowFillVisible(false);
        final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        series.getData().addAll(
            new XYChart.Data<Number,Number>(0       ,950),
            new XYChart.Data<Number,Number>(2000    ,100),
            new XYChart.Data<Number,Number>(5000    ,200),
            new XYChart.Data<Number,Number>(7500    ,180),
            new XYChart.Data<Number,Number>(10000   ,100)
        );
        chart.getData().add(series);
        primaryStage.setScene(SceneBuilder.create()
                .root(chart)
                .width(500).height(400)
                .stylesheets(CurveFittedChart.class.getResource("CurveFittedChart.css").toExternalForm())
                .build()
        );
        primaryStage.show();
    }
}
