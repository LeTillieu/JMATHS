package functionManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionEvaluator extends FunctionDefinition {
    String paramStr = "(\\((\\s*-*[a-zA-Z0-9)(+\\-*/]+\\s*,)*(\\s*[a-zA-Z0-9)(+\\-*/]+\\s*)+\\))";
    Pattern paramPattern = Pattern.compile(paramStr);
    Matcher paramMatcher;

    /**
     *
     * @param data String representing a function to evaluate
     */
    FunctionEvaluator(String data){
        getPossibleFun();
        type = "funcEval";
        //get the function's name
        nameMatcher = namePattern.matcher(data);
        if(nameMatcher.find()){
            name = nameMatcher.group(1);
        }

        paramMatcher = paramPattern.matcher(data);
        String allVariables = null;
        if(paramMatcher.find()){
            allVariables = paramMatcher.group(1);
        }
        allVariables = allVariables.substring(1, allVariables.length() - 1);
        for (String curVar : allVariables.split(",")) {
            System.out.println("dataArray:");
            parseCalculus(curVar);
            for(String curStr: dataArray){
                System.out.println(curStr);
            }
            calculate(0, dataArray.size());
            dataArray.clear();
        }
    }

    /**
     *
     * @param func the function that has to be evaluated
     */
    public void evalute(FunctionDefinition func){
        ArrayList<String> funcDataArrayCpy = new ArrayList<>(func.dataArray);
        int curVarId = 0;
        for(String curVar: func.parameters){
            while(funcDataArrayCpy.indexOf(curVar) != -1){
                funcDataArrayCpy.set(funcDataArrayCpy.indexOf(curVar), results.get(curVarId));
            }
            curVarId++;
        }
        dataArray = funcDataArrayCpy;
        results.clear();
        calculate(0,dataArray.size());
    }
}
