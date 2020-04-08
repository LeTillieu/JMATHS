package functionManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculus extends Calculation {
    Calculus(String data){
        getPossibleFun();
        type = "functionEval";
        //get the function's name

        parseCalculus(data);
        calculate(0, dataArray.size());
        dataArray.clear();


        dataArray = new ArrayList<>(results);
        results.clear();
        calculate(0,dataArray.size());
    }
}
