package functionManager;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 *
 */
public class Parser {
    /**
     * Key to find data in ParsedResults and ParsedFunction
     */
    public String name = null;

    /**
     * Type of element: funcDef, funcEval or calc
     */
    public String type = null;

    /**
     * List of functions created by user
     */
    public static HashMap<String, FunctionDefinition> parsedFunction = new HashMap<>();
    /**
     * List of results of calculus
     */
    public static HashMap<String, Calculation> parsedResult = new HashMap<>();
    protected String functionEvalStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z0-9()+\\-*/^]+\\s*,)*(\\s*[a-zA-Z0-9()+\\-*/^]+\\s*)+\\)\\s*)";
    public Parser(){}

    /**
     *
     * @param data A string representing a new function,  a function to evaluate or a calculus
     */
    public Parser(String data) {
        String functionStr = "(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\((\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*,)*(\\s*[a-zA-Z]+[a-zA-Z0-9]*\\s*)+\\)\\s*=[a-zA-Z0-9+\\-*/^\\s()]+)";

        String calculusStr = "([a-zA-Z0-9()+\\-*/^\\s]+)";


        if (Pattern.matches(functionStr, data)) {
            System.out.println("defFunc");
            FunctionDefinition newFunc = new FunctionDefinition(data);
            this.name = newFunc.name;
            this.type = "funcDef";
            parsedFunction.put(newFunc.name, newFunc);
            newFunc.compute(0.1, -20, 20);
        }else if(Pattern.matches(functionEvalStr, data)) {
            System.out.println("evalFunc");
            this.type = "funcEval";
            FunctionEvaluator newFunc = new FunctionEvaluator(data);
            this.name = data;
            if (parsedFunction.containsKey(newFunc.name)) {
                if (parsedFunction.get(newFunc.name).type.equals("functionDef")) {
                    FunctionDefinition funcDef = (FunctionDefinition) parsedFunction.get(newFunc.name);
                    newFunc.evalute(funcDef);
                    parsedResult.put(data, newFunc);
                }
            }
        }else if(Pattern.matches(calculusStr, data)){
            System.out.println("calc");
            this.type = "calc";
            this.name = data;
            Calculus newCalc = new Calculus(data);
            parsedResult.put(data, newCalc);
        }

    }
}
