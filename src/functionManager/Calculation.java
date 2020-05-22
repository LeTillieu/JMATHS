package functionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Calculation extends Parser{
    /**
     * The step between each computed value
     */
    public double step = 0.1;
    /**
     * The minimum computed value
     */
    public double min = -100;
    /**
     * The maximum computed value
     */
    public double max = 100;

    String existingFuncStr;
    Pattern existingFuncPattern = null;
    Matcher existingFuncMatcher = null;

    protected String nameStr = "([a-zA-Z]+[a-zA-Z0-9]*)";
    protected Pattern namePattern = Pattern.compile(nameStr);
    protected Matcher nameMatcher = null;
    /**
     * parsed calculus
     */
    public ArrayList<String> dataArray = new ArrayList<>();
    /**
     * result of the calculus
     */
    public ArrayList<String> results = new ArrayList<>();
    /**
     * List of x points that will be displayed
     */
    public ArrayList<String> abscissa  = new ArrayList<>();
    /**
     * List of pre-existing function (cos, sin, exp,...)
    */
    public ArrayList<String> possibleFun = new ArrayList<>();

    /**
     *get all existing functions
     */
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

    /**
     * @param exp String that represent a calculus
     */
    public void parseCalculus(String exp){
        existingFuncMatcher = existingFuncPattern.matcher(exp);
        existingFuncMatcher.reset();
        while(existingFuncMatcher.find()){
            dataArray.add(existingFuncMatcher.group());
        }
        int curIdx = 0;
        ArrayList<String> elemToAdd = null;
        boolean somethingFound;
        ArrayList<String> dataArrayCpy = new ArrayList<>(dataArray);
        do{
            somethingFound = false;
            for(String curFunc: parsedFunction.keySet()){
                if(dataArrayCpy.indexOf(curFunc) != -1){
                    curIdx = dataArrayCpy.indexOf(curFunc);
                    if(dataArrayCpy.get(curIdx+1).equals("(")){
                        elemToAdd = new ArrayList<>(parsedFunction.get(curFunc).dataArray);
                        //get the input parameters
                        int closeBracket = getMatchingBracket(dataArrayCpy, curIdx+1);
                        List<String> variableList = dataArrayCpy.subList(curIdx+2, closeBracket+1);
                        StringBuilder variables = new StringBuilder();
                        for(String s: variableList){
                            variables.append(s);
                        }

                        ArrayList<String> inputParam = new ArrayList<>();
                        for (String curVar : variables.toString().split(",")) {
                            inputParam.add(curVar.trim());
                        }

                        //compare input parameters with given parameters and check lengths and replace variables
                        if(inputParam.size() == ((FunctionDefinition)this).parameters.size()){
                            if(parsedFunction.get(curFunc).parameters.size() == inputParam.size()){
                                int curVarIdx = 0;
                                for(String s: inputParam){
                                    if(((FunctionDefinition)this).parameters.indexOf(s) != -1){
                                        String curVar = parsedFunction.get(curFunc).parameters.get(curVarIdx);
                                        for(String curElem: elemToAdd){
                                            if(curElem.equals(curVar)){
                                                elemToAdd.set(elemToAdd.indexOf(curVar), s);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        for(int i = closeBracket; i >= curIdx; i--){
                            dataArrayCpy.remove(i);
                        }
                        elemToAdd.add(0, "(");
                        elemToAdd.add( ")");
                        dataArrayCpy.addAll(curIdx, elemToAdd);
                    }
                }
            }
        }while(somethingFound);
        dataArray = new ArrayList<>(dataArrayCpy);
    }

    /**
     *
     * @param start first point to be calculated (included)
     * @param end last point to be calculated (included)
     */
    protected void calculate(int start, int end) {
        calculate(start, end, false, dataArray);
    }

    /**
     *
     * @param start id of the starting element in curArray that we want to compute (included)
     * @param end id of the ending element in curArray that we want to compute (included)
     * @param isRec true if there are parenthesis
     * @param curArr Array of data that has to be computed
     * @return An array with one Element: the result of the calculus
     */
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
                            idx = dataArrayCpy.indexOf("(")+1;
                            int endIdx = getMatchingBracket(dataArrayCpy, idx-1);
                            tmpCopy = new ArrayList<>(dataArrayCpy);
                            if (endIdx >= idx-1) {
                                dataArrayCpy.subList(idx-1, endIdx + 1).clear();
                            }
                            dataArrayCpy.addAll(idx-1, calculate(idx, endIdx, true, tmpCopy));
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
                            idx = dataArrayCpy.indexOf(curFunc);
                            if(idx != 0){
                                firstNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) - 1));
                            }else{
                                firstNumber = 0d;
                            }
                            secondNumber = Double.parseDouble(dataArrayCpy.get(dataArrayCpy.indexOf(curFunc) + 1));
                            res = Double.toString(firstNumber - secondNumber);
                            if(idx != 0){
                                dataArrayCpy.set(idx - 1, res);
                                dataArrayCpy.remove(idx + 1);
                                dataArrayCpy.remove(idx);
                            }else{
                                dataArrayCpy.set(idx, res);
                                dataArrayCpy.remove(idx + 1);
                            }
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

    /**
     *
     * @param data Array of element that are computed
     * @param openBracketPos Index in the array of the opening bracjet we're trying to match
     * @return Id in the array of the closing bracket matching the opening one
     */
    private int getMatchingBracket(ArrayList<String> data, int openBracketPos){
        int toReturn = -1;
        int nbOpen = 1;
        int nbClose = 0;
        openBracketPos++;
        while(nbOpen > nbClose && openBracketPos < data.size()){
            if(data.get(openBracketPos).equals(")")){
                nbClose++;
                toReturn = openBracketPos;
            }else if(data.get(openBracketPos).equals("(")){
                nbOpen++;
            }
            openBracketPos++;
        }

        if(nbOpen == nbClose){
            return toReturn;
        }else{
            return -1;
        }
    }
}
