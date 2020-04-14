package sample;

import functionManager.Parser;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TextArea;

public class Controller {
    @FXML
    private TextArea functionArea;
    @FXML
    private LineChart<Double, Double> plot;

    public Controller() {
    }

    public void plotLine(String functionToGraph) {
        this.plot.setCreateSymbols(false);
        Series<Double, Double> series = new Series();
        new ArrayList();
        Parser function1 = new Parser(functionToGraph) {};
        function1.parsedData.get();
        double x = (function1.parsedData.get().min;
        System.out.println(x);

        for(Iterator var7 = function1.results.iterator(); var7.hasNext(); x += ((Function)function1).step) {
            String y = (String)var7.next();
            this.plotPoint(x, Double.parseDouble(y), series);
        }

        this.plot.getData().add(series);
    }

    private void plotPoint(double x, double y, Series<Double, Double> series) {
        series.getData().add(new Data(x, y));
    }

    @FXML
    public void evaluate(ActionEvent actionEvent) {
        String textFromArea = this.functionArea.getText();
        this.plotLine(textFromArea);
    }
}