package functionManager;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Parser {
    public HashMap<String, Calculation> parsedData = new HashMap<>();
    Parser(String data) {
        String functionStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*,)*(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*)+\\)\\s*=[a-zA-Z0-9+\\-*/^\\s()]+)";

        String functionEvalStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z0-9()+\\-*/^]+\\s*,)*(\\s*[a-zA-Z0-9()+\\-*/^]+\\s*)+\\)\\s*)";

        String calculusStr = "([a-zA-Z0-9()+\\-*/^\\s]+)";




        if (Pattern.matches(functionStr, data)) {
            FunctionDefinition newFunc = new FunctionDefinition(data);
            parsedData.put(newFunc.name, newFunc);
            newFunc.compute(1,0,1);
        }
        if(Pattern.matches(functionEvalStr, data)){
            FunctionEvaluator newFunc = new FunctionEvaluator(data);
            if(parsedData.containsKey(newFunc.name)){
                if(parsedData.get(newFunc.name).type.equals("functionDef")){
                    FunctionDefinition funcDef = (FunctionDefinition)parsedData.get(newFunc.name);
                    newFunc.evalute(funcDef);
                    parsedData.put(newFunc.name, newFunc);
                }
            }
        }
        if(Pattern.matches(calculusStr, data)){
            Calculus newCalc = new Calculus(data);
            parsedData.put(null, newCalc);
        }

    }
}
