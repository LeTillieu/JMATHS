import java.util.ArrayList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function extends Parser {
    private double step = 0.1;
    private double min = -100;
    private double max = 100;

    public ArrayList<String> parameters = new ArrayList<String>();
    String functionStr = "(\\s*[a-zA-Z]+\\(([a-zA-Z\\s]+,)*[a-zA-Z\\s]+\\)\\s*\\=[a-zA-Z0-9\\+\\-*/\\s\\(\\)]+)";
    Pattern functionPattern = Pattern.compile(functionStr);
    Matcher functionMatcher = null;
    public Queue<Double> results = null;

    private int curComputedVar = 0;
    Function(String data){
        getPossibleFun();
        if(Pattern.matches(functionStr, data)){
            type = "function";
            //get the function's name
            nameMatcher = namePattern.matcher(data);
            nameMatcher.find();
            String functionName = nameMatcher.group(0);

            //get the function's variable
            paramMatcher = paramPattern.matcher(data);
            paramMatcher.find();
            String allVariables = paramMatcher.group(1);
            allVariables = allVariables.substring(1,allVariables.length()-1);
            for(String curVar: allVariables.split(",")){
                parameters.add(curVar.trim());
            }

            //expression
            functionMatcher = functionPattern.matcher(data);
            functionMatcher.find();
            String expression = functionMatcher.group(0);
            expression = expression.split("\\s*[a-zA-Z]+\\(([a-zA-Z\\s]+,)*[a-zA-Z\\s]+\\)\\s*\\=\\s*")[1].trim();
            parseFunction(expression);

        }
    }


    public void parseFunction(String exp){
        existingFuncMatcher = existingFuncPattern.matcher(exp);
        existingFuncMatcher.reset();
        while(existingFuncMatcher.find()){
            dataArray.add(existingFuncMatcher.group());
        }
    }

    @Override
    void compute() {
        compute(this.step, this.min, this.max);
    }

    @Override
    void compute(double step) {
        compute(step, this.min, this.max);
    }

    @Override
    void compute(double min, double max) {
        compute(this.step, min, max);
    }

    public void compute(double step, double min, double max){
        String curVar = parameters.get(curComputedVar);
        ArrayList<Integer> idxList = new ArrayList<Integer>();
        int idx = -1;
        if(curComputedVar < parameters.size()-1){
            curComputedVar++;
            for(double i = min; i <= max; i+=step){
                idxList.clear();
                while(dataArray.indexOf(curVar) != -1){
                    idx = dataArray.indexOf(curVar);
                    dataArray.set(idx, Double.toString(i));
                    idxList.add(idx);
                }
                compute(step, min, max);
                for(int curIdx: idxList){
                    dataArray.set(curIdx, curVar);
                }
            }
        }else{
            for(double i = min; i <= max; i+=step){
                idxList.clear();
                while(dataArray.indexOf(curVar) != -1){
                    idx = dataArray.indexOf(curVar);
                    dataArray.set(idx, Double.toString(i));
                    idxList.add(idx);
                }

                calculate(0, dataArray.size());
                for(int curIdx: idxList){
                    dataArray.set(curIdx, curVar);
                }
            }
        }
    }

}
