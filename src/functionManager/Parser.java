package functionManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static void main(String data) {
        String functionStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*,)*(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*)+\\)\\s*=[a-zA-Z0-9+\\-*/^\\s()]+)";

        String functionEvalStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z0-9()+\\-*/^]+\\s*,)*(\\s*[a-zA-Z0-9()+\\-*/^]+\\s*)+\\)\\s*)";

        String calculusStr = "([a-zA-Z0-9()+\\-*/^\\s]+)";


        ArrayList<Calculation> parsedData = new ArrayList<>();

        if (Pattern.matches(functionStr, data)) {
            FunctionDefinition newFunc = new FunctionDefinition(data);
            parsedData.add(newFunc);
            newFunc.compute(1,0,1);
        }
        if(Pattern.matches(functionEvalStr, data)){
            FunctionEvaluator newFunc = new FunctionEvaluator(data);
            for(Calculation curCal: parsedData){
                if(curCal.type.equals("functionDef")){
                    FunctionDefinition funcDef = (FunctionDefinition)curCal;
                    if(funcDef.parameters.size() == newFunc.results.size() && funcDef.name.equals(newFunc.name)){
                        newFunc.evalute(funcDef);
                        parsedData.add(newFunc);
                    }
                }
            }
        }
        if(Pattern.matches(calculusStr, data)){
            Calculus newCalc = new Calculus(data);
            parsedData.add(newCalc);
        }

    }
}
