package functionManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String functionStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*,)*(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*)+\\)\\s*=[a-zA-Z0-9+\\-*/^\\s()]+)";
        Pattern functionPattern = Pattern.compile(functionStr);
        Matcher functionMatcher = null;

        String functionEvalStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z0-9()+\\-*/^]+\\s*,)*(\\s*[a-zA-Z0-9()+\\-*/^]+\\s*)+\\)\\s*)";
        Pattern functionEvalPattern = Pattern.compile(functionStr);
        Matcher functionEvalMatcher = null;

        String calculusStr = "([a-zA-Z0-9()+\\-*/^\\s]+)";
        Pattern calculusPattern = Pattern.compile(calculusStr);
        Matcher calculusMatcher = null;


        ArrayList<Calculation> parsedData = new ArrayList<>();
        String data1 = "     dg(x) = 2*x";
        String data2 = "dg(2)";
        String data3 = "2^(2+5)";

        if (Pattern.matches(functionStr, data1)) {
            FunctionDefinition newFunc = new FunctionDefinition(data1);
            parsedData.add(newFunc);
            newFunc.compute(1,0,1);
        }
        if(Pattern.matches(functionEvalStr, data2)){
            FunctionEvaluator newFunc = new FunctionEvaluator(data2);
            for(Calculation curCal: parsedData){
                if(curCal.type.equals("functionDef")){
                    FunctionDefinition funcDef = (FunctionDefinition)curCal;
                    if(funcDef.parameters.size() == newFunc.results.size() && funcDef.name.equals(newFunc.name)){
                        newFunc.evalute(funcDef);
                        for(String test: newFunc.results){
                            System.out.println(test);
                        }
                    }
                }
            }
        }
        if(Pattern.matches(calculusStr, data3)){
            Calculus newCalc = new Calculus(data3);
            for(String test: newCalc.results){
                System.out.println(test);
            }
        }

    }
}
