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
        int i = 0;
        if(function1.type.equals("funcDef")){
            for(String curOrd: Parser.parsedFunction.get(function1.name).results) {
                this.plotPoint(Double.parseDouble(Parser.parsedFunction.get(function1.name).abscissa.get(i)), Double.parseDouble(curOrd), series);
                i++;
            }
        }else if(function1.type.equals("funcEval")){
            if(Parser.parsedResult.containsKey(function1.name)){
                System.out.println(Parser.parsedResult.get(function1.name).results.get(0));
            }
        }else if(function1.type.equals("calc")){
            System.out.println(Parser.parsedResult.get(function1.name).results.get(0));
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