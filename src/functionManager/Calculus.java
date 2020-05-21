package functionManager;

import java.util.ArrayList;

public class Calculus extends Calculation {
    /**
     *
     * @param data String that represents a calculus
     */
    Calculus(String data){
        getPossibleFun();
        type = "calc";

        //get the function's name
        parseCalculus(data);


        calculate(0, dataArray.size());

        dataArray = new ArrayList<>(results);
    }
}
