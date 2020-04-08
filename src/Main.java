import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Parser> test = new ArrayList<Parser>();
        Parser function1 = new Function("     dg(x,y) = (x+2)*(3+y)") {
        };
//        test.add(function1);
        function1.compute(1,0,1);
        for(String curRes: function1.results){
            System.out.println(curRes);
        }
    }
}
