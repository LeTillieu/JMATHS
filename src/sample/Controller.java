package sample;

import functionManager.Parser;

import java.math.BigDecimal;
import java.net.FileNameMap;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

public class Controller<BarChart> implements Initializable {

    public Button varButt;
    public Text varResult;
    public Button medButt;
    public Text medResult;
    public PieChart effectiveChart;
    public CategoryAxis valueAxis;
    public NumberAxis effectiveAxis;

    @FXML
    private TableView<ColData> statTable;

    @FXML
    private TableColumn<?, ?> col0;

    @FXML
    private TableColumn<Object, String> col1;

    @FXML
    private TableColumn<Object, String> col2;

    @FXML
    private Text moyResult;

    @FXML
    private TextArea functionArea;
    @FXML
    private LineChart<Double, Double> plot;

    @FXML
    private ListView<String> resList;


    public void plotLine(String functionToGraph) {
        this.plot.setCreateSymbols(false);
        Series<Double, Double> series = new Series<>();
        Parser function1 = new Parser(functionToGraph) {
        };
        int i = 0;
        if (function1.type.equals("funcDef")) {
            for (String curOrd : Parser.parsedFunction.get(function1.name).results) {
                this.plotPoint(Double.parseDouble(Parser.parsedFunction.get(function1.name).abscissa.get(i)), Double.parseDouble(curOrd), series);
                i++;
            }
        } else if (function1.type.equals("funcEval")) {
            if (Parser.parsedResult.containsKey(function1.name)) {
                System.out.println(Parser.parsedResult.get(function1.name).results.get(0));
            }

        } else if (function1.type.equals("calc")) {
            System.out.println(Parser.parsedResult.get(function1.name).results.get(0));
        }


        this.plot.getData().add(series);
    }

    private void plotPoint(double x, double y, Series<Double, Double> series) {
        series.getData().add(new Data(x, y));
    }

    @FXML
    public void evaluate(ActionEvent actionEvent) {
        Parser function1;
        String textFromArea = this.functionArea.getText();
        this.plotLine(textFromArea);
        function1 = new Parser(textFromArea);
        if (function1.type.equals("calc")) {
            resList.getItems().add(textFromArea + " = " + Parser.parsedResult.get(function1.name).results.get(0));
        } else {
            resList.getItems().add(textFromArea);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col0.setCellValueFactory(new PropertyValueFactory<>("rowNum"));
        col1.setCellValueFactory(new PropertyValueFactory<>("coll1Data"));
        col2.setCellValueFactory(new PropertyValueFactory<>("coll2Data"));
        for (int i = 0; i < 1000; i++) {
            ColData data = new ColData(i + 1, null, null);
            statTable.getItems().add(data);
        }
        col1.setCellFactory(TextFieldTableCell.forTableColumn());
        col2.setCellFactory(TextFieldTableCell.forTableColumn());
        System.out.println("done initializing");
    }

    public double calculateMoy() {
        double numerator = 0;
        double denominator = 0;
        double result = 0;
        for (int i = 0; i < 1000; i++) {
            if (col1.getCellData(i) != null
                    && !col1.getCellData(i).equals("")
                    && col2.getCellData(i) != null
                    && !col2.getCellData(i).equals("")) {
                numerator += Double.parseDouble(col1.getCellData(i)) * Double.parseDouble(col2.getCellData(i));
                denominator += Double.parseDouble(col2.getCellData(i));
            }
        }
        result = numerator / denominator;

        moyResult.setText(String.format("%.2f", result));
        return result;
    }

    public void calculateVar() {
        double result = 0;
        double moy = calculateMoy();
        double numerator = 0;
        double nbVal = 0;
        for (int i = 0; i < 1000; i++) {
            if (col1.getCellData(i) != null
                    && !col1.getCellData(i).equals("")
                    && col2.getCellData(i) != null
                    && !col2.getCellData(i).equals("")) {
                nbVal += Double.parseDouble(col2.getCellData(i));
                numerator += Math.pow(((Double.parseDouble(col1.getCellData(i)) - moy)), 2) * Double.parseDouble(col2.getCellData(i));
            }
        }
        result = numerator / nbVal;

        varResult.setText(String.format("%.2f", result));
    }


    public void calculateMed() {
        double nbVal = 0;
        double medValue;
        ArrayList<Double> valueList = new ArrayList<Double>();
        for (int i = 0; i < 1000; i++) {
            if (col1.getCellData(i) != null
                    && !col1.getCellData(i).equals("")
                    && col2.getCellData(i) != null
                    && !col2.getCellData(i).equals("")) {
                nbVal += Double.parseDouble(col2.getCellData(i));
                for (int j = 0; j < Double.parseDouble(col2.getCellData(i)); j++) {
                    valueList.add(Double.parseDouble(col1.getCellData(i)));
                }
            }
        }
        Collections.sort(valueList);
        medValue = (nbVal + 1) / 2;
        medResult.setText(valueList.get((int) Math.round(medValue)).toString());
    }

    public void commitValue(TableColumn.CellEditEvent<Object, String> objectStringCellEditEvent) {
        ColData valueSelected = statTable.getSelectionModel().getSelectedItem();
        valueSelected.setColl1Data(objectStringCellEditEvent.getNewValue().toString());

        updateChart();
    }

    public void commitCoefficient(TableColumn.CellEditEvent<Object, String> objectStringCellEditEvent) {
        ColData coefficientSelected = statTable.getSelectionModel().getSelectedItem();
        coefficientSelected.setColl2Data(objectStringCellEditEvent.getNewValue().toString());

        updateChart();
    }

    public void deleteValue(TableColumn.CellEditEvent<Object, String> objectStringCellEditEvent) {
        ColData valueSelected = statTable.getSelectionModel().getSelectedItem();
        valueSelected.setColl1Data(null);
    }

    public void deleteCoefficient(TableColumn.CellEditEvent<Object, String> objectStringCellEditEvent) {
        ColData coefficientSelected = statTable.getSelectionModel().getSelectedItem();
        coefficientSelected.setColl2Data(null);
    }

    public void updateChart() {
        effectiveChart.getData().clear();
        effectiveChart.getScene().getStylesheets().add("sample/colors.css");

        double nbVal = 0;
        ArrayList<Double> valueList = new ArrayList<Double>();
        for (int i = 0; i < 1000; i++) {
            if (col1.getCellData(i) != null
                    && !col1.getCellData(i).equals("")
                    && col2.getCellData(i) != null
                    && !col2.getCellData(i).equals("")) {
                boolean dataExist = false;
                for (int j = 0; j < effectiveChart.getData().size(); j++) {
                    PieChart.Data curData = effectiveChart.getData().get(j);
                    System.out.println("curData: "+curData.getPieValue());
                    if (Double.parseDouble(curData.getName()) == Double.parseDouble(col1.getCellData(i))) {
                        dataExist = true;
                        System.out.println("wesh ca devrait pas");
                        Double oldValue = curData.getPieValue();
                        Double newValue = Double.parseDouble(col2.getCellData(i));
                        curData.setPieValue(oldValue+newValue);
                    }
                }
                if (!dataExist) {
                    System.out.println("value: "+col2.getCellData(i));
                    effectiveChart.getData().add(new PieChart.Data(col1.getCellData(i), Double.parseDouble(col2.getCellData(i))));
                }
            }
        }


        for(int i = 0; i < effectiveChart.getData().size(); i++){
            System.out.println(effectiveChart.getData().get(i));
        }


    }


}