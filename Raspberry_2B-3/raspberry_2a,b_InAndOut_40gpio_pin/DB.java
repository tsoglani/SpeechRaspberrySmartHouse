
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.Serializable;

public class DB implements Serializable{
    public final static String DAYS_STRING = "ActiveDays:", TIME_STRING = "ActiveTime:",TIME_STAMP = "TimeStamp:",SENDING_TIME="Time:",USER_ID_SPLIT="!!!!!",
    COMMAND_TEXT_STRING = "CommandText:",COMMAND_ID = "CommandID:",DEVICE_ID = "DeviceID:",    IS_WEEKLY = "IsWeekly:", IS_ACTIVE = "IsActive:", COMMAND_SPLIT_STRING = "##", SHEDULE_SPLIT_STRING = "@@!@@";

    private static ArrayList<Shedule> sheduleList = new ArrayList<Shedule>(){

            @Override
            public boolean add(Shedule shedule){

                System.out.println(size()+"    add to list     "+shedule.getCommands()  );
                for(int i=0;i<size();i++){

                    if(get(i).getId()==shedule.getId()){
                        return false;
                    }

                    System.out.println(get(i).getCommands()+"    vs    "+shedule.getCommands()  );
                    if(get(i).getCommands().equals(shedule.getCommands())){
                        System.out.println("false");
                        return false;
                    }

                }
                return super.add(shedule);    
            }};
    private Connection conn_costumer;
    private Connection conn_flower;
    private final String shedule_table = "MY_FOURTY_PINS_INANDOUT_SHEDULE_TABLE";
    private SH sh;
    public DB(SH sh) {
        this.sh=sh;
        try {
            initShedule();
            // updateShedule();
        } catch (Exception var1_1) {
            // empty catch block
        }
        sheduleList = getShedules();

    }

    protected boolean conainsCommandInDevice(String command){
        if(command.endsWith(" on")){
            command=command.substring(0,command.length()-" on".length());
        }else if(command.endsWith("on")){
            command=command.substring(0,command.length()-"on".length());
        }else  if(command.endsWith(" off")){
            command=command.substring(0,command.length()-" off".length());
        }else if(command.endsWith("off")){
            command=command.substring(0,command.length()-"off".length());
        }

        for (int i = 0; i < sh.outputPowerCommands.length; i++) {
            //  System.out.println(command+"   ns  "+sh.outputPowerCommands[i].get(0));
            if(command.equals(sh.outputPowerCommands[i].get(0))){
                System.out.println("sucess found");
                return true;}
        }return false;
    }

    private void initShedule() {
        Statement stmt = null;
        try {
            try {
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                String connectionURL = "jdbc:derby:"+shedule_table+";create=true";
                String createString = "CREATE TABLE " + shedule_table + " (NAME LONG VARCHAR FOR BIT DATA NOT NULL)";

                Class.forName(driver);
                conn_costumer = DriverManager.getConnection(connectionURL);
                stmt = conn_costumer.createStatement();
                try {
                    stmt.executeUpdate(createString);
                } catch (SQLException var4_5) {
                }
            } catch (Exception ex) {
                System.out.println("error in class DB at initCostumer function "+ex.getMessage());
                System.exit(0);
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateShedule() {
        deleteSheduleTable();
        initShedule();
        addAllShedules();
    }

    private void addAllShedules() {
        for (Shedule costumer : sheduleList) {
            try {
                PreparedStatement psInsert = conn_costumer.prepareStatement("insert into " + shedule_table + " values (?)");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(costumer);
                oos.close();
                psInsert.setBytes(1, bos.toByteArray());
                psInsert.executeUpdate();
                continue;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private synchronized void deleteSheduleTable() {
        try {
            PreparedStatement psInsert = conn_costumer.prepareStatement("DROP TABLE " + shedule_table);
            psInsert.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized String add(String command) {
        
        System.out.println("add:::"+command);
        Shedule shedule = new Shedule();
        shedule.setId(getnewID());
        shedule.setActiveDays(getDays(command));
        shedule.setTime(getTime(command));
        shedule.setIsWeekly(getIsWeekly(command));
        shedule.setIsActive(getIsActive(command));
        shedule.setCommandText(getCommandText(command));
        if(!conainsCommandInDevice(shedule.getCommandText())){
            return null;}
        boolean isGointToADd=true;
        for(int i=0;i<sheduleList.size();i++){

            if(sheduleList.get(i).getId()==shedule.getId()){
                isGointToADd= false;
            }

            if(sheduleList.get(i).getCommands().equals(shedule.getCommands())){
                System.out.println("false");
                isGointToADd= false;
            }

        }
        if(isGointToADd){
            if(sheduleList.add(shedule)){
                addShedule(shedule);
                return"DeviceID:"+SH.DeviceID+COMMAND_SPLIT_STRING+shedule.toString();

            }
        }return"addedNotOk";
    }

    public synchronized void updateSingleShedule(String command,String commandID,String commantModeText) {

        int cmdID=Integer.parseInt(commandID);
        if(!conainsCommandInDevice(command)){
            return ;
        }

        System.out.println("found command in device for signle update   #ncommand:"+command+"#ncommandID="+commandID+" commantModeText:"+commantModeText);

        System.out.println("commandID= "+commandID);
        // int position = -1;
        Shedule shedule=null;
        for (int i = 0; i < sheduleList.size(); i++) {
            if (sheduleList.get(i).getId() == cmdID) {
                shedule=sheduleList.get(i);
                System.out.println("found update id");
                if(commantModeText.startsWith(IS_WEEKLY)){
                    System.out.println(commantModeText.substring(IS_WEEKLY.length(),commantModeText.length()));
                    // boolean isActive=Boolean.parseBoolean(commantModeText.substring(IS_WEEKLY.length(),commantModeText.length()));
                    shedule.setIsWeekly(commantModeText.substring(IS_WEEKLY.length(),commantModeText.length()));
                    updateShedule();
                }else if(commantModeText.startsWith(IS_ACTIVE)){
                    // boolean isActive=Boolean.parseBoolean(commantModeText.substring(IS_ACTIVE.length()));
                    System.out.println(commantModeText.substring(IS_ACTIVE.length(),commantModeText.length()));
                    shedule.setIsActive(commantModeText.substring(IS_ACTIVE.length(),commantModeText.length()));
                    updateShedule();
                }
                //   position = i;

            }
        }

        //   if (position != -1) {
        //    sheduleList.remove(position);
        //   sheduleList.add(position,shedule);
        //  }

        if(shedule==null){
            System.out.println("shedule did not found");
            return ;

        }
        sh.sendToAll("updatedOk:DeviceID:"+SH.DeviceID+COMMAND_SPLIT_STRING+shedule.toString());
    }

    public synchronized void updateShedule(String command,String commandID) {

        String sendingToAllCommand="";
        //  Shedule shedule = new Shedule();

        // System.out.println("command = "+command);
        if(!conainsCommandInDevice(getCommandText(command))){
            return ;
        }
        System.out.println("found command in device for update   command:"+command+"#ncommandID="+commandID);
        //   int position = -1;
        Shedule shedule=null;
        int cmdID=Integer.parseInt(commandID);
        for (int i = 0; i < sheduleList.size(); i++) {
            if (sheduleList.get(i).getId() == cmdID) {
                shedule=sheduleList.get(i);
                shedule.setId(cmdID); ///////// iDD
                shedule.setActiveDays(getDays(command));
                shedule.setTime(getTime(command));
                shedule.setIsWeekly(getIsWeekly(command));
                shedule.setIsActive(getIsActive(command));
                shedule.setCommandText(getCommandText(command));
                updateShedule();
                //   position = i;
            }

        }

        // if (position != -1) {
        //    sheduleList.remove(position);
        //     sheduleList.add(position,shedule);
        //  }

        if(shedule==null){
            return ;}
        sh.sendToAll("updatedOk:DeviceID:"+SH.DeviceID+COMMAND_SPLIT_STRING+shedule.toString());

        //   return"updatedOk:DeviceID:"+SH.DeviceID+COMMAND_SPLIT_STRING+shedule.toString();
    }

    public synchronized String updateShedule(Shedule shedule) {

        int position = -1;
        for (int i = 0; i < sheduleList.size(); i++) {
            if (sheduleList.get(i).getId() == shedule.getId()) {
                position = i;
            }
        }

        if (position != -1) {
            sheduleList.remove(position);
            sheduleList.add(position,shedule);
        }

        updateShedule();
        return"UpdatedOk:DeviceID:"+SH.DeviceID+COMMAND_SPLIT_STRING+shedule.toString();
    }

    public void removeShedule(String command,String textCommand) {//// px "updateShedule:ID:1"
        System.out.println("removeShedule::::"+command+"  "+textCommand);

        if(!conainsCommandInDevice(textCommand)){
            return ;}
        int remId=Integer.parseInt(command);
        String output=null;
        int position = -1;
        for (int i = 0; i < sheduleList.size(); i++) {
            if (sheduleList.get(i).getId() ==remId) {
                position = i;
            }
        }

        if (position != -1) {
            sheduleList.remove(position);
            updateShedule();
        }
        output="removeShedule:DeviceID:"+SH.DeviceID+COMMAND_SPLIT_STRING+COMMAND_ID+command;
        sh.sendToAll(output);
        //  return output;
    }

    private int getnewID(){
        int id=0;
        for (int i = 0; i < sheduleList.size(); i++) {
            if (sheduleList.get(i).getId() >=id) {
                id=sheduleList.get(i).getId()+1 ;
            }
        }
        return id;
    }

    private String getDays(String command) {
        String[] list = command.split(COMMAND_SPLIT_STRING);
        String output=list[1];
        return output.substring(DAYS_STRING.length(),output.length());
    } 

    private String getCommandText(String command) {
        String[] list = command.split(COMMAND_SPLIT_STRING);
        String output=list[0];
        return output.substring(COMMAND_TEXT_STRING.length(),output.length());
    }

    private String getTime(String command) {
        String[] list = command.split(COMMAND_SPLIT_STRING);
        String output=list[2];
        return output.substring(TIME_STRING.length(),output.length());

    }

    private String getIsWeekly(String command) {
        String[] list = command.split(COMMAND_SPLIT_STRING);
        String output=list[3];
        return output.substring(IS_WEEKLY.length(),output.length());
    }

    private String getIsActive(String command) {
        String[] list = command.split(COMMAND_SPLIT_STRING);
        String output=list[4];
        return output.substring(IS_ACTIVE.length(),output.length());
    }

    private void addShedule(Shedule shedule) {
        try {
            PreparedStatement psInsert = conn_costumer.prepareStatement("insert into " + shedule_table + " values (?)");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(shedule);
            oos.close();
            psInsert.setBytes(1, bos.toByteArray());
            psInsert.executeUpdate();
            
            sh.sendToAll("Shedules:"+DEVICE_ID+sh.DeviceID+COMMAND_SPLIT_STRING+getAllSheduleText());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized ArrayList<Shedule> getShedules() {
        ArrayList<Shedule> array =  new ArrayList<Shedule>();
        try {
            Statement stmt2 = conn_costumer.createStatement();
            ResultSet rs = stmt2.executeQuery("select * from " + shedule_table);
            boolean num = false;
            while (rs.next()) {
                ObjectInputStream obj = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes(1)));
                array.add((Shedule) obj.readObject());
            }
            rs.close();
        } catch (Exception ex) {
            // ex.printStackTrace();
        } catch (Error ex) {
            //  ex.printStackTrace();
        }
        return array;
    }

    public String getAllSheduleText(){
        String sendedText="";
        for(int i=0;i<sheduleList.size();i++){
            if(i<sheduleList.size()-1)
                sendedText+=sheduleList.get(i).toString()+SHEDULE_SPLIT_STRING;
            else
                sendedText+=sheduleList.get(i).toString();
        }
        System.out.println(sendedText);
        return sendedText;
    }

}
