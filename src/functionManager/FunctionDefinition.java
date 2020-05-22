package functionManager;

import java.util.ArrayList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionDefinition extends Calculation {

    /**
     * List of parameters names
     */
    public ArrayList<String> parameters = new ArrayList<>();
    String functionStr = "(\\s*[a-zA-Z]+\\(([a-zA-Z\\s]+,)*[a-zA-Z\\s]+\\)\\s*=[a-zA-Z0-9+\\-*/^\\s()]+)";
    Pattern functionPattern = Pattern.compile(functionStr);
    Matcher functionMatcher = null;

    private int curComputedVar = 0;

    FunctionDefinition() {
    }

    /**
     * @param data String representing a function
     */
    FunctionDefinition(String data) {
        getPossibleFun();
        type = "functionDef";
        //get the function's name
        nameMatcher = namePattern.matcher(data);
        if (nameMatcher.find()) {
            name = nameMatcher.group(1);
        }

        String paramStr = "(\\((\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*,)*(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*)+\\))";
        Pattern paramPattern = Pattern.compile(paramStr);
        Matcher paramMatcher;

        //get the function's variable
        paramMatcher = paramPattern.matcher(data);
        String allVariables = null;
        if (paramMatcher.find()) {
            allVariables = paramMatcher.group(1);
        }
        assert allVariables != null;
        allVariables = allVariables.substring(1, allVariables.length() - 1);
        for (String curVar : allVariables.split(",")) {
            parameters.add(curVar.trim());
        }
        if (parameters.size() == 1) {
            //expression
            functionMatcher = functionPattern.matcher(data);
            String expression = null;
            if (functionMatcher.find()) {
                expression = functionMatcher.group(1);
            }
            assert expression != null;
            expression = expression.split("\\s*[a-zA-Z]+\\(([a-zA-Z\\s]+,)*[a-zA-Z\\s]+\\)\\s*=\\s*")[1].trim();
            parseCalculus(expression);
            System.out.println("\n");
            for (String s : dataArray) {
                System.out.println(s);
            }
        }

    }

    void compute() {
        compute(this.step, this.min, this.max);
    }

    /**
     * @param step the step between each computed point
     */
    void compute(double step) {
        compute(step, this.min, this.max);
    }

    /**
     * @param min the minimum value to compute (included)
     * @param max the maximum value to compute (included)
     */
    void compute(double min, double max) {
        compute(this.step, min, max);
    }

    /**
     * @param step the step between each computed point
     * @param min  the minimum value to compute (included)
     * @param max  the maximum value to compute (included)
     */
    public void compute(double step, double min, double max) {
        String curVar = parameters.get(curComputedVar);
        ArrayList<Integer> idxList = new ArrayList<>();
        int idx;
        if (curComputedVar < parameters.size() - 1) {
            curComputedVar++;
            for (double i = min; i <= max; i += step) {
                idxList.clear();
                while (dataArray.indexOf(curVar) != -1) {
                    idx = dataArray.indexOf(curVar);
                    dataArray.set(idx, Double.toString(i));
                    idxList.add(idx);
                }
                compute(step, min, max);
                for (int curIdx : idxList) {
                    dataArray.set(curIdx, curVar);
                }
            }
        } else {
            for (double i = min; i <= max; i += step) {
                idxList.clear();
                while (dataArray.indexOf(curVar) != -1) {
                    idx = dataArray.indexOf(curVar);
                    dataArray.set(idx, Double.toString(i));
                    idxList.add(idx);
                }
                calculate(0, dataArray.size());
                for (int curIdx : idxList) {
                    dataArray.set(curIdx, curVar);
                }
            }
        }

        for (double i = min; i <= max; i += step) {
            abscissa.add(Double.toString(i));
        }
    }


}
