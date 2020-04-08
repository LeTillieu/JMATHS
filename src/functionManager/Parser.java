package functionManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Parser {
    public String type = null;
    public ArrayList<String> dataArray = new ArrayList<String>();
    public ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> possibleFun = new ArrayList<String>();

    String existingFuncStr;
    Pattern existingFuncPattern = null;
    Matcher existingFuncMatcher = null;

    String nameStr = "([a-zA-Z]+)";
    Pattern namePattern = Pattern.compile(nameStr);
    Matcher nameMatcher = null;

    String paramStr = "(\\(([a-zA-Z\\s]+,)*[a-zA-Z\\s]+\\))";
    Pattern paramPattern = Pattern.compile(paramStr);
    Matcher paramMatcher = null;

    public void getPossibleFun() {
        possibleFun.add("\\(");
        possibleFun.add("\\)");
        possibleFun.add("acos");
        possibleFun.add("asin");
        possibleFun.add("atan");
        possibleFun.add("cos");
        possibleFun.add("sin");
        possibleFun.add("tan");
        possibleFun.add("exp");
        possibleFun.add("sqrt");
        possibleFun.add("\\^");
        possibleFun.add("\\*");
        possibleFun.add("/");
        possibleFun.add("\\+");
        possibleFun.add("-");
        possibleFun.add("[a-zA-Z]+[a-zA-Z0-9]*");
        possibleFun.add("[0-9]+");

        StringBuilder regexExpressionBuilder = new StringBuilder();
        for (String curFunc : possibleFun) {
            regexExpressionBuilder.append("|(").append(curFunc).append(")");
        }
        existingFuncStr = regexExpressionBuilder.toString().replaceFirst("\\|", "");
        existingFuncPattern = Pattern.compile(existingFuncStr);


    }

    abstract void parseFunction(String exp);

    abstract void compute();

    abstract void compute(double step);

    abstract void compute(double min, double max);

    abstract void compute(double step, double min, double max);

    protected ArrayList<String> calculate(int start, int end) {
        return calculate(start, end, false, dataArray);
    }

    private ArrayList<String> calculate(int start, int end, boolean isRec,ArrayList<String> curArr) {
        ArrayList<String> dataArrayCpy = new ArrayList<String>(curArr.subList(start, end));
        ArrayList<String> tmpCopy = new ArrayList<String>();
        Double firstNumber;
        Double secondNumber;
        String res;
        int idx;
        boolean somethingFound = false;
        do {
            somethingFound = false;
            outer: for (String curFunc : possibleFun) {
                if (curFunc.startsWith("\\")) {
                    curFunc = curFunc.substring(1);
                }
                if (dataArrayCpy.indexOf(curFunc) != -1) {
                    somethingFound = true;
                    switch (curFunc) {
                        case "(":
                            idx = dataArrayCpy.indexOf("(") + 1;
                            int endIdx = dataArrayCpy.indexOf(")");
                            tmpCopy = new ArrayList<>(dataArrayCpy);
                            for (int i = endIdx; i >= idx - 1; i--) {
                                dataArrayCpy.remove(i);
                            }
                            dataArrayCpy.addAll(idx - 1, calculate(idx, endIdx, true, tmpCopy));
                            tmpCopy.clear();
                            break outer;
                        case "cos":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.cos(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break;
                        case "*":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber * secondNumber);
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break;
                        case "/":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber / secondNumber);
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break;
                        case "+":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber + secondNumber);
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break;
                        case "-":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber - secondNumber);
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break outer;
                    }
                }
            }
        } while (somethingFound);

        if (!isRec) {
            results.add(dataArrayCpy.get(0));
        }
        return dataArrayCpy;
    }
}
