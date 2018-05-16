
/**
 * Write a description of class ChatterBotSecondQuestionRespond here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import com.google.code.chatterbotapi.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatterBotExample {

    public ChatterBotExample() {

      
    }
 String ask(String str){
String output=null;
  try {
            ChatterBotFactory factory = new ChatterBotFactory();

            ChatterBot bot1 = factory.create(ChatterBotType.CLEVERBOT);
            ChatterBotSession bot1session = bot1.createSession();

            ChatterBot bot2 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            ChatterBotSession bot2session = bot2.createSession();

            String s = str;
//        while (true) {

            System.out.println("que> " + s);

            output = bot2session.think(s);
            System.out.println("ans- " + output);
//
//            s = bot1session.think(s);
//        }
        } catch (Exception ex) {
ex.printStackTrace();
        }
        return output;
}
    

}
