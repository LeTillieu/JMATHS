package functionManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Calculation extends Parser{
    public double step = 0.1;
    public double min = -100;
    public double max = 100;

    String existingFuncStr;
    Pattern existingFuncPattern = null;
    Matcher existingFuncMatcher = null;

    protected String nameStr = "([a-zA-Z]+[a-zA-Z0-9]*)";
    protected Pattern namePattern = Pattern.compile(nameStr);
    protected Matcher nameMatcher = null;

    public ArrayList<String> dataArray = new ArrayList<>();
    public ArrayList<String> results = new ArrayList<>();
    public ArrayList<String> abscissa  = new ArrayList<>();
    public ArrayList<String> possibleFun = new ArrayList<>();

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
        possibleFun.add("ln");
        possibleFun.add("log");
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

    public void parseCalculus(String exp){
        existingFuncMatcher = existingFuncPattern.matcher(exp);
        existingFuncMatcher.reset();
        while(existingFuncMatcher.find()){
            dataArray.add(existingFuncMatcher.group());
        }
    }
    
    protected ArrayList<String> calculate(int start, int end) {
        return calculate(start, end, false, dataArray);
    }

    private ArrayList<String> calculate(int start, int end, boolean isRec,ArrayList<String> curArr) {
        ArrayList<String> dataArrayCpy = new ArrayList<>(curArr.subList(start, end));
        ArrayList<String> tmpCopy;
        Double firstNumber;
        Double secondNumber;
        String res;
        int idx;
        boolean somethingFound;
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
                        case "sin":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.sin(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "tan":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.tan(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "acos":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.acos(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "asin":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.asin(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "atan":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.atan(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "exp":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.exp(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "log":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.log10(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "ln":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.log(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "sqrt":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.sqrt(firstNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx, res);
                            dataArrayCpy.remove(idx + 1);
                            break outer;
                        case "^":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(Math.pow(firstNumber, secondNumber));
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break outer;
                        case "*":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber * secondNumber);
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break outer;
                        case "/":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber / secondNumber);
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break outer;
                        case "+":
                            firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber + secondNumber);
                            idx = dataArrayCpy.indexOf(curFunc);
                            dataArrayCpy.set(idx - 1, res);
                            dataArrayCpy.remove(idx + 1);
                            dataArrayCpy.remove(idx);
                            break outer;
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
