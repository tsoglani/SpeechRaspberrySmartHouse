
/**
 * Write a description of class Shedule here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.Serializable;
 public class Shedule implements Serializable
   {

        private String activeDays, time, isWeekly, isActive,commandString;
        int id;
private static final long serialVersionUID =1;
        public Shedule() {
        }

        public String getActiveDays() {
            return activeDays;
        }

        public void setActiveDays(String activeDays) {
            this.activeDays = activeDays;
        }
        
         public String getCommandText() {
            return commandString;
        }

        public void setCommandText(String commandString) {
            this.commandString = commandString;
        }


        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIsWeekly() {
            return isWeekly;
        }

        public void setIsWeekly(String isWeekly) {
            this.isWeekly = isWeekly;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        
       @Override
        public String toString(){
        return "CommandID:"+id+DB.COMMAND_SPLIT_STRING+DB.COMMAND_TEXT_STRING+getCommandText()+DB.COMMAND_SPLIT_STRING+DB.DAYS_STRING+getActiveDays()+DB.COMMAND_SPLIT_STRING+DB.TIME_STRING+getTime()
        +DB.COMMAND_SPLIT_STRING+DB.IS_WEEKLY+getIsWeekly() +DB.COMMAND_SPLIT_STRING+DB.IS_ACTIVE+getIsActive();
        }
        
       
        public String getCommands(){
        return DB.COMMAND_TEXT_STRING+getCommandText()+DB.DAYS_STRING+getActiveDays()+DB.COMMAND_SPLIT_STRING+DB.TIME_STRING+getTime()
        +DB.COMMAND_SPLIT_STRING+DB.IS_WEEKLY+getIsWeekly() +DB.COMMAND_SPLIT_STRING+DB.IS_ACTIVE+getIsActive();
        }
       //  public final static String DAYS_STRING = "ActiveDays:", TIME_STRING = "ActiveTime:",
         //   IS_WEEKLY = "IsWeekly:", IS_ACTIVE = "IsActive:", COMMAND_SPLIT_STRING = "##", SHEDULE_SPLIT_STRING = "@@!@@";


    }
