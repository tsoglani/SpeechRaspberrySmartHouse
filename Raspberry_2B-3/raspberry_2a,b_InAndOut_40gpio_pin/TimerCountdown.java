
import java.util.ArrayList;
public class TimerCountdown extends Thread
{
    static ArrayList<TimerCountdown> timers= new ArrayList<TimerCountdown>();
    int seconds=0;
    private SH sh;
    String commandText;
    int id;
    boolean isRunning=true;
    private long timestamp;
    public TimerCountdown(SH sh,String commandText,String seconds,String timestamp) {
        this.seconds=Integer.parseInt(seconds);
        this.sh=sh;
        System.out.println("new TimerCountdown:"+commandText);
        this.commandText=commandText;
        this.timestamp=Long.parseLong(timestamp);
        id=generateID();
        isRunning=true;
        timers.add(this);
    }

    @Override
    public void run(){

        while(seconds>0&&isRunning){
            try{
                Thread.sleep(1000);
                seconds--;
                     //    System.out.println("TimerCountdowns"+timers.size());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

        }
        if(timers.contains(this)){
           //
            System.out.println("commandText+ "+commandText);
        sh.processCommandString(commandText);
        timers.remove(this);
        sh.sendTheUpdates(commandText);
           sh.sendToAll("switch "+commandText);
        sh.sendToAll("removeTime:DeviceID:"+SH.DeviceID+toString());
        if(!sh.fr.isSwitchModeSelected){
        new Thread(){public void run(){
        try{
            sleep(1500);
         sh.fr.manualSelected();
        }catch(Exception d){}
        
        }}.start();
        }
        
    }
}

    public static boolean containsTimestamp(long timestamp){
        for(TimerCountdown timer:timers){
            if(timer.timestamp==timestamp){
                return true;
            }
        }
        return false;
    }

     private static TimerCountdown getTimer(long timestamp){
        for(TimerCountdown timer:timers){
            if(timer.timestamp==timestamp){
                return timer;
            }
        }
        return null;
    }
      protected static void removeFromList(long timestamp){
      TimerCountdown timer=getTimer( timestamp);
      if(timer!=null){
        timers.remove(timer);
        }
    }
    public static String getAllTimers(){
        String out="";
        for(int i=0;i<timers.size();i++){
            TimerCountdown timer=timers.get(i);

            if(i!=timers.size()-1){
                out+=timer.toString()+DB.SHEDULE_SPLIT_STRING;
            }else{
                out+=timer.toString();
            }}

       
        return out;
    }

    public static boolean containsID(int id){
        for(TimerCountdown timer:timers){
            if(timer.id==id){
                return true;
            }
        }
        return false;
    }

    private int generateID(){
        int maxID=0;
        for(TimerCountdown timer:timers){
            if(timer.id>=maxID){
                maxID=timer.id+1;
            }
        }
        return maxID;
    }

    @Override
    public String toString(){
        return "CommandID:"+id+DB.COMMAND_SPLIT_STRING+DB.TIME_STAMP+timestamp+DB.COMMAND_SPLIT_STRING+DB.COMMAND_TEXT_STRING+commandText+
        DB.COMMAND_SPLIT_STRING+DB.SENDING_TIME+seconds;
    }

}
