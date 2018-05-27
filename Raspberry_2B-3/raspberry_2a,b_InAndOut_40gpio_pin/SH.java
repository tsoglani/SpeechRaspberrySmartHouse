
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.GpioPinOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.Properties;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author tsoglani
 */
public class SH {

    private DatagramSocket serverSocket;
    protected DB db;
    boolean hasExterlanGPIO=false;

    //// user editable part
    // Pay attention on **
    private int NumberOfBindingCommands = 13;// ** Number of commands you want to bind with one or more outputs.

    private  static int port = 2222; // default port can change it, but you have to change it also in android device,
    //not recomented to change it

    private  static String deviceName = "tsoglani";// ** is used for global connection for safety, must put it on android device name field in global connection option.
    ///** every startingDeviceID must be unique in every raspberry device contected in local network.
     static int DeviceID = 0; // Example: if we have 4 raspberry devices connected in local network, each one MUST have a unique ID :
    // the first Ruspberry device DeviceID will be 0, the second device's DeviceID will be 1
    // the third will be 2 the fourth will be 3 ...    (it is very important)

    ///**
    // these are the commannds that each device can receive and react,
    // so every outputPowerCommand must be unique in every device contected in local network.
    // on addCommandsAndPorts function RECOMENDER LOWER CASE TEXT

    protected ArrayList<String>[] outputPowerCommands ;
    private ArrayList<Integer>[] activatePortOnCommand ;
    private static  int raspberryOutputs = 20;// 0 - 12 plus external 0-7
    private static  int raspberryInputs = 20;//13-16,21-29  plus external 8-15
    protected ArrayList<String>[] outputCommands = new ArrayList[raspberryOutputs];
    private ArrayList<GpioPinDigitalInput>[] inputButtons = new ArrayList[raspberryInputs];
    private GpioPinOutput pins[] = new GpioPinOutput[raspberryOutputs];
    private ArrayList<String> ON, OFF;// = "on", OFF = "off";// word you have to use at the end of the command to activate or deactivate
    private ArrayList<String> ONAtTheStartOfSentence, OFFAtTheStartOfSentence;
    private  ArrayList<String> exclusiveOutputs= new ArrayList<String>();;
private boolean isGoingToBeOn_OnSwitchOn=false;
    
     public  void isGoingToBeOn_OnSwitchOn(String fileName){

        String line;
        BufferedReader br;
        try {
            InputStream fis = null;
            fis = new FileInputStream(fileName);

            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            int counter =0;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("isGoingToBeOn_OnSwitchOn:")){
                    line=  line.substring("isGoingToBeOn_OnSwitchOn:".length(),line.length()).replaceAll(" ","");
                    isGoingToBeOn_OnSwitchOn=Boolean.parseBoolean(line);
                }
            }
            System.out.println("isGoingToBeOn_OnSwitchOn"+isGoingToBeOn_OnSwitchOn);

        } catch (Exception e) {
            e.printStackTrace();
        isGoingToBeOn_OnSwitchOn=false;
        }
    }
    
    
     public  void readDeviceID(String fileName){

        String line;
        BufferedReader br;
        try {
            InputStream fis = null;
            fis = new FileInputStream(fileName);

            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            int counter =0;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("DeviceID:")){
                    line=  line.substring("DeviceID:".length(),line.length()).replaceAll(" ","");
                    DeviceID=Integer.parseInt(line);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            DeviceID=0;
        }
    }

    
    private void sendEmailOnBoot(String fileName){
    String line=null,output=null;
        BufferedReader br;
         boolean enable_send_mail_on_boot=false;
         String email_from=null,email_password=null;
         ArrayList <String>emails_to=new ArrayList <String>();
        try {
            InputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
           while ((line = br.readLine()) != null) {
                // Deal with the line

                if (line.startsWith("enable_send_mail_on_boot:")){
                    output=line.substring("enable_send_mail_on_boot:".length(),line.length());
                   enable_send_mail_on_boot=Boolean.parseBoolean(output.replaceAll(" ",""));
             System.out.println("enable_send_mail_on_boot"+enable_send_mail_on_boot);
                }else if (line.startsWith("email_from:")){
                    output=line.substring("email_from:".length(),line.length());
                   email_from=output;
             System.out.println("email_from"+email_from);
                }else if (line.startsWith("email_to:")){
                    output=line.substring("email_to:".length(),line.length());
                   emails_to.add(output);
             System.out.println("email_to"+output);
                }
else if (line.startsWith("email_password:")){
                    output=line.substring("email_password:".length(),line.length());
                   email_password=output;
             System.out.println("email_password"+"****");
                }
                }
if(!enable_send_mail_on_boot||email_from==null|| emails_to.isEmpty()|| email_password==null){
return;
}
final String emailFrom=email_from,emailPassword=email_password;
final ArrayList<String> emailsTo=emails_to;
new Thread(){
            @Override
            public void run() {
                String externalIP=getIP();
                while(externalIP==null){
                    try {
                        sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    externalIP=getIP();

                }
                try {

                new EmailJava(emailFrom,emailsTo,emailPassword,externalIP,deviceName);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    

//                System.out.println();
            

        }catch (Exception e){
            e.printStackTrace();
        }



    }
    
    
    
  

private String getIP() {

        String systemipaddress = "";
        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");
            BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
            systemipaddress = sc.readLine().trim();
            if (!(systemipaddress.length() > 0)) {
                try {
                    InetAddress localhost = InetAddress.getLocalHost();
                    System.out.println((localhost.getHostAddress()).trim());
                    systemipaddress = (localhost.getHostAddress()).trim();
                } catch (Exception e1) {
                    systemipaddress = "Cannot Execute Properly";
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            systemipaddress= getIP2();

        }
        return systemipaddress;

    }


    private  String getIP2() { URL ipAdress;
        String ip=null;
        try {
            ipAdress = new URL("http://myexternalip.com/raw");

            BufferedReader in = new BufferedReader(new InputStreamReader(ipAdress.openStream()));

             ip = in.readLine();

        } catch (Exception e) {
            e.printStackTrace();
        }

return ip;

    }

    
    
    
    
    
    public static String readUserName(String fileName){


        String line=null,output=null;
        BufferedReader br;
        try {
            InputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                // Deal with the line

                if (line.startsWith("username:")){
                    output=line.substring("username:".length(),line.length());
                }else{
                    continue;

                }




//                System.out.println();
            }

        }catch (Exception e){
            e.printStackTrace();
            output="home";
        }

        return output;

    }

    public void readExclusiveOutputs(String fileName){
        String line;
        BufferedReader br;
        try {
            InputStream fis = null;
            fis = new FileInputStream(fileName);

            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            int counter =0;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("exclusiveOutputs:")){
                    line=line.substring("exclusiveOutputs:".length(),line.length()).replaceAll(" ","");
                    exclusiveOutputs.add(line);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public  void readPort(String fileName){

        String line;
        BufferedReader br;
        try {
            InputStream fis = null;
            fis = new FileInputStream(fileName);

            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            int counter =0;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("port:")){
                    line=  line.substring("port:".length(),line.length()).replaceAll(" ","");
                    port=Integer.parseInt(line);
                    System.out.println(port);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            port=2222;
        }
    }


    private void initializePowerCommands2(String fileName) {
        String line;
        BufferedReader br;
        try {
            InputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            int counter =0;
            while ((line = br.readLine()) != null) {
                // Deal with the line
                NumberOfBindingCommands=counter;
                if (line.replaceAll(" ","").equals("")){
                    continue;
                }

                System.out.println("line = "+line);
                String commandString=line.split("@@")[0];
                String[] commands=commandString.split(",,");////////////////
                for (String s:commands){

                    System.out.print(s+",");

                }

                String numberListString=line.split("@@")[1];
                String numbersString[] =numberListString.split(",");
                Integer[] numbers=new Integer[numbersString.length];////////////////

                for (  int i=0;i<numbersString.length;i++){
                    numbers[i]= Integer.parseInt(numbersString[i].replaceAll(" ",""));
                    System.out.println("numbers"+  numbersString[i].replaceAll(" ","")+" ");
                }


                addCommandsAndPorts(counter ,// number of command
                        commands,numbers
                );

                counter ++;


//                System.out.println();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }




    private int powerCommandFileLength(String fileName){


        String line;
        int NumberOfBindingCommands=20;

        BufferedReader br;
        try {
            InputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            int counter =0;
            while ((line = br.readLine()) != null) {
                // Deal with the line
                if (line.replaceAll(" ","").equals("")){
                    break;
                }
                counter++;
                NumberOfBindingCommands=counter;


//                System.out.println();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return (NumberOfBindingCommands);

    }

    private void initializePowerCommands() {

        for (int i = 0; i < NumberOfBindingCommands; i++) {
            switch (i) {
                case 0:
                    //Number of command you can put in one Device:outputPowerCommands[0]... outputPowerCommands[RelayNumberOfChanels-1] NO MORE THAN 'RelayNumberOfChanels-1'
                    // else you will have an error mesasge
                    //ALL commands WITH LATIN LETERS
                    addCommandsAndPorts(i // number of command
                            , new String[]{"Kitchen lights", "kitchen light", "koyzina fos", "koyzina fota", "koyzinas fos", "koyzinas fota", "fos koyzina", "fota koyzina", "fos koyzinas", "fota koyzinas"},// command text for reaction, the first one ("kitchen lights") is sending to client as command for switch button, the others commands can be used (send) by client with voice-speech
                            new Integer[]{0} // on (receiving) command "kitchen lights","kitchen light", "koyzina fos" .....  these outputs will open or close at once
                    );
                    break;

                case 1:
                    addCommandsAndPorts(i // command no 1
                            , new String[]{"living room light 1", "living room lights 1"},
                            new Integer[]{1});// on command 1 these outputs will open or close at once when the previous commands received
                    break;

                case 2:
                    //Number of command you can put in one Device:outputPowerCommands[0]... outputPowerCommands[RelayNumberOfChanels-1] NO MORE THAN 'RelayNumberOfChanels-1'
                    // else you will have an error mesasge
                    //ALL commands WITH LATIN LETERS
                    addCommandsAndPorts(i // number of command 2
                            , new String[]{"living room light 2", "living room lights 2"},// command text for reaction
                            new Integer[]{2} // when command "office lights" or "office lights" received, these outputs will open or close at once .
                    );
                    break;

                case 3:
                    addCommandsAndPorts(i // command no 3
                            , new String[]{"hall 1"},
                            new Integer[]{3});// on command 3 these outputs will open or close at once when the previous commands received
                    break;
                case 4:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"hall 2"},
                            new Integer[]{4});// on command 4 these outputs will open or close at once when the previous commands received
                    break;
                case 5:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"room light 1", "room lights 1", "bedroom light 1", "bedroom lights 1", "domatio fos 1",// command text for reaction
                                    "domatio fota 1", "fos domatio 1", "fota domatio 1"},
                            new Integer[]{5});// on command 4 these outputs will open or close at once when the previous commands received
                    break;
                //                    case 5:
                //                     addCommandsAndPorts(i // command no 5
                //                     ,new String[]{"air condition","cooler" },
                //                   new Integer[]{16});// on command 5 these outputs will open or close at once when the previous commands received
                //                     break;
                //                   case 6:
                //                     addCommandsAndPorts(i // command no 6
                //                     ,new String[]{"garage" },
                //                    new Integer[]{17,18,19});// on command 6 these outputs will open or close at once when the previous commands received
                //                    break;
                //                    case 7:
                //                     addCommandsAndPorts(i // command no 7
                //                     ,new String[]{"toilet light","toilet lights" },
                //                    new Integer[]{20,0});// on command 7 these outputs will open or close at once when the previous commands received
                //      break;
                case 6:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"room light 2", "room lights 2", "bedroom light 2", "bedroom lights 2", "domatio fos 2",// command text for reaction
                                    "domatio fota 2", "fos domatio 2", "fota domatio 2"},
                            new Integer[]{6});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 7:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"Door"},
                            new Integer[]{7});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 8:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"Hidden light 1", "Hidden lights 1", "Hidden light one", "Hidden lights one"},
                            new Integer[]{8});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 9:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"Hidden light 2", "Hidden lights 2", "Hidden light two", "Hidden lights two"},
                            new Integer[]{9});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 10:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"Hidden light 3", "Hidden lights 3", "Hidden light three", "Hidden lights three"},
                            new Integer[]{10});// on command 4 these outputs will open or close at once when the previous commands received
                    break;


                case 11:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"balcony 1 light 1", "balcony 1 lights 1", "balcony one light one", "balcony one lights one"},
                            new Integer[]{11});// on command 4 these outputs will open or close at once when the previous commands received
                    break;
                case 12:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"balcony 1 light 2", "balcony 1 lights 2", "balcony one light two", "balcony one lights two"},
                            new Integer[]{12});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 13:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"balcony 2 light", "balcony 2 lights", "balcony two light", "balcony two lights"},
                            new Integer[]{13});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 14:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"electric shutters bedroom on"},
                            new Integer[]{14});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 15:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"electric shutters bedroom off"},
                            new Integer[]{15});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 16:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"electric shutters kitchen on"},
                            new Integer[]{16});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 17:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"electric shutters kitchen off"},
                            new Integer[]{17});// on command 4 these outputs will open or close at once when the previous commands received
                    break;

                case 18:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"extra light 1", "extra lights 1", "extra light one", "extra lights one"},
                            new Integer[]{18});// on command 4 these outputs will open or close at once when the previous commands received
                    break;


                case 19:
                    addCommandsAndPorts(i // command no 4
                            , new String[]{"extra light 2", "extra lights 2", "extra light two", "extra lights two"},
                            new Integer[]{19});// on command 4 these outputs will open or close at once when the previous commands received
                    break;


            }
        }

    }

    //// end of user editable part

    //     private ArrayList<InetAddress> addresses = new ArrayList<InetAddress>() {
    //
    //             @Override
    //             public boolean add(InetAddress e) {
    //                 if (!contains(e)) {
    //                     return super.add(e);
    //                 }
    //                 return false;
    //             }
    //         };
    //
    //     private ArrayList<Integer> allPorts = new ArrayList<Integer>() {
    //
    //             @Override
    //             public boolean add(Integer e) {
    //                 if (!contains(e)) {
    //                     return super.add(e);
    //                 }
    //                 return false;
    //             }
    //         };
    PCA9685GpioProvider provider;


    String fileCommandPath="/home/pi/Desktop/SpeechRaspberrySmartHouse/commands.txt";
    public SH() {
        //   allPorts.add(port);
        readDeviceID("/home/pi/Desktop/SpeechRaspberrySmartHouse/DeviceID.txt");
        declareInputsAndOutputs("/home/pi/Desktop/SpeechRaspberrySmartHouse/gpio_input_output.txt");
        readPort("/home/pi/Desktop/SpeechRaspberrySmartHouse/port.txt");
        readExclusiveOutputs("/home/pi/Desktop/SpeechRaspberrySmartHouse/exclusiveOutputs.txt");
isGoingToBeOn_OnSwitchOn("/home/pi/Desktop/SpeechRaspberrySmartHouse/isGoingToBeOn_OnSwitchOn.txt");
        
        if(hasExterlanGPIO) {

    BigDecimal frequency = new BigDecimal("48.828");
    // Correction factor: actualFreq / targetFreq
    // e.g. measured actual frequency is: 51.69 Hz
    // Calculate correction factor: 51.65 / 48.828 = 1.0578
    // --> To measure actual frequency set frequency without correction factor(or set to 1)
    BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
    // Create custom PCA9685 GPIO provider
    I2CBus bus;
    try {
        bus = I2CFactory.getInstance(I2CBus.BUS_1);

        provider = new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor);

    } catch (I2CFactory.UnsupportedBusNumberException ex) {
        Logger.getLogger(SH.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(SH.class.getName()).log(Level.SEVERE, null, ex);
    }
}
        NumberOfBindingCommands= powerCommandFileLength(fileCommandPath);
        outputPowerCommands = new ArrayList[NumberOfBindingCommands];
        activatePortOnCommand = new ArrayList[NumberOfBindingCommands];

        initArrays();
        initStates();

        initializeOutputCommands();
        initializePowerCommands2(fileCommandPath);
        String backupdeviceName=readUserName("/home/pi/Desktop/SpeechRaspberrySmartHouse/deviceName.txt");


        if (backupdeviceName!=null){
            deviceName=backupdeviceName;
        }
        initGpioPinDigitalOutputs2();
        initInputListeners2(); // remove comment if you have input plug in
        db = new DB(this);
        new SheduleThread().start();
        sendEmailOnBoot("/home/pi/Desktop/SpeechRaspberrySmartHouse/email.txt");
    }

    private void initArrays() {
        //outputPowerCommands = new ArrayList[NumberOfBindingCommands];
        for (int i = 0; i < NumberOfBindingCommands; i++) {
            outputPowerCommands[i] = new ArrayList<String>();

            activatePortOnCommand[i] = new ArrayList<Integer>();
        }
    }

    // add commands text for reaction and the ports that want to react
    private void addCommandsAndPorts(int number, String[] reactOnCommands, Integer[] ports) {
        for (int i = 0; i < reactOnCommands.length; i++) {
            outputPowerCommands[number].add(reactOnCommands[i]);
        }
        for (int i = 0; i < ports.length; i++) {
            activatePortOnCommand[number].add(ports[i]);
        }
    }

    private void addCommands(int number, String... reactOnCommands) {
        for (int i = 0; i < reactOnCommands.length; i++) {
            outputPowerCommands[number].add(reactOnCommands[i]);
        }
    }

    private void addPortsOnCommand(int number, Integer... ports) {
        for (int i = 0; i < ports.length; i++) {
            activatePortOnCommand[number].add(ports[i]);
        }
    }

    // greek letters match ( must be latin characters )
    //α=a,β=v,γ=g,δ=d,ε=e,ζ=z,  η=i,ι=i,θ=th,κ=k,
    //λ=l,μ =m, ν=n, ξ=ks, o=ο,ω=o,π=p,ρ=r,
    //ς=s,σ=s,τ=t,υ=y,φ=f,χ=x,ψ=ps
    //in this function you add multi command for each output.
    // these EXACT commands you must send from the Android device (speech or with Switch buttons ) to activate or deactivate the device output
    // Example send command "kitchen light" and "on" or "off" to activate or deactivate the device in output 0.
    //You can modify your commands.
    private void initializeOutputCommands() {
        for (int i = 0; i < outputCommands.length; i++) {
            outputCommands[i] = new ArrayList<String>();
            outputCommands[i].add(DeviceID + " output " + (i));
        }

    }

    ArrayList<Integer> inputsReadFromFile;
    ArrayList<Integer> outputsReadFromFile;
    private void declareInputsAndOutputs(String fileName){
        String line;
        BufferedReader br;
        try {
            InputStream fis = null;
            fis = new FileInputStream(fileName);

            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            int counter =0;
            System.out.println("declareInputsAndOutputs ");
            while ((line = br.readLine()) != null) {

                System.out.println(line);
                if (line.startsWith("Input")){
                    line= line.replace("Input:","").replaceAll(" ","");
                    inputsReadFromFile=new ArrayList<Integer>();
                    for (String str :line.replaceAll(" ","").split(",") ) {
                        inputsReadFromFile.add(Integer.parseInt(str));
                    }

                    raspberryInputs = inputsReadFromFile.size();
                    inputButtons = new ArrayList[raspberryInputs];
                }else if (line.startsWith("Output:")){
                    line= line.replace("Output:","").replaceAll(" ","");


                    outputsReadFromFile=new ArrayList<Integer>();
                    for (String str :line.replaceAll(" ","").split(",") ) {
                        outputsReadFromFile.add(Integer.parseInt(str));
                    }

                    raspberryOutputs = outputsReadFromFile.size();// 0 - 12 plus external 0-7
                    outputCommands = new ArrayList[raspberryOutputs];
                    pins= new GpioPinOutput[raspberryOutputs];
                }
                else if (line.startsWith("External")){
                    line= line.replace("External","").replaceAll(" ","");
                    System.out.println("External text found");
                    if (line.contains("true")){
                        hasExterlanGPIO=true;
                        raspberryOutputs = raspberryOutputs+16;
                        pins= new GpioPinOutput[raspberryOutputs];
                        outputCommands = new ArrayList[raspberryOutputs];
                        System.out.println("External added");
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initGpioPinDigitalOutputs2() {
        int counter=0;
        GpioController gpio = GpioFactory.getInstance();

        while(!outputsReadFromFile.isEmpty()){

            int giopOutInt=outputsReadFromFile.remove(0);
            GpioPinOutput pin = null;
            switch (giopOutInt){
                case 0:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);
                    break;
                case 1:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);
                    break;
                case 2:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 3:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 4:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 5:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 6:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 7:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 8:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 9:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 10:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 11:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 12:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 13:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 14:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 15:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 16:

                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 17:

                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 18:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 19:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 20:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 21:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 22:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 23:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 24:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 25:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 26:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 27:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 28:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 29:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);
                    break;
                case 30:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_30, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;
                case 31:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_31, "PinLED" + counter, com.pi4j.io.gpio.PinState.LOW);

                    break;




            }
            pins[counter] = pin;
            if (pin.getName().startsWith("PinLED")) {


                System.out.println("GpioPinDigitalOutput installed   No " + counter + "  " + pin.getName());
                GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
                ppp.low();

            }

            counter++;

        }
        if (hasExterlanGPIO){

            for (int i = 0; i <= 15; i++) {

                GpioPinOutput pin = null;
                switch (i) {
                    case 0:

                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_00, "Pulse 00");
                        break;
                    case 1:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_01, "Pulse 00");
                        break;
                    case 2:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_02, "Pulse 00");
                        break;
                    case 3:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_03, "Pulse 00");
                        break;
                    case 4:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_04, "Pulse 00");
                        break;
                    case 5:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_05, "Pulse 00");
                        break;
                    case 6:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_06, "Pulse 00");
                        break;
                    case 7:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_07, "Pulse 00");
                        break;
                    case 8:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_08, "Pulse 00");
                        break;
                    case 9:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_09, "Pulse 00");
                        break;
                    case 10:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_10, "Pulse 00");
                        break;
                    case 11:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_11, "Pulse 00");
                        break;
                    case 12:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_12, "Pulse 00");
                        break;
                    case 13:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_13, "Pulse 01");

                        break;
                    case 14:
                        // pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "PinLED" + i, com.pi4j.io.gpio.PinState.LOW);
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_14, "Pulse 02");

                        break;
                    case 15:
                        pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_15, "Pulse 03");

                        break;


                }
                pins[counter+i] = pin;

                if (pin.getName().startsWith("Pulse")) {
                    GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
                    System.out.println("GpioPinPwmOutput installed   No " + i);

                    provider.setAlwaysOff(ppp.getPin());

                }

            }
        }

    }

    private void initInputListeners2() {

        GpioController gpio = GpioFactory.getInstance();
        int counter=0;
        while(!inputsReadFromFile.isEmpty()){

            int giopInInt=inputsReadFromFile.remove(0);
            GpioPinDigitalInput myButton = null;

            switch (giopInInt){
                case 0:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 1:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 2:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 3:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 4:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 5:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 6:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 7:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 8:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_08, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 9:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_09, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 10:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_10, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 11:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 12:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_12, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 13:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 14:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_14, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 15:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 16:

                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_16, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 17:

                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 18:

                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_18, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 19:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_19, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 20:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_20, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 21:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 22:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 23:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 24:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 25:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 26:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 27:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 28:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 29:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 30:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_30, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;
                case 31:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_31, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);

                    break;




            }

            if(myButton==null){
                continue;
            }
            myButton.addListener(new GpioUsageExampleListener(myButton, counter));
            myButton.setShutdownOptions(true, com.pi4j.io.gpio.PinState.LOW);


            counter++;



        }





        //    gpio.shutdown();
    }
    private void initGpioPinDigitalOutputs() {
        GpioController gpio = GpioFactory.getInstance();
        for (int i = 0; i < pins.length; i++) {

            GpioPinOutput pin = null;
            switch (i) {
                case 0:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_00, "Pulse 00");
                    break;
                case 1:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_01, "Pulse 00");
                    break;
                case 2:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_02, "Pulse 00");
                    break;
                case 3:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_03, "Pulse 00");
                    break;
                case 4:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_04, "Pulse 00");
                    break;
                case 5:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_05, "Pulse 00");
                    break;
                case 6:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_06, "Pulse 00");
                    break;
                case 7:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_07, "Pulse 00");
                    break;
                case 8:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_08, "Pulse 00");
                    break;
                case 9:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_09, "Pulse 00");
                    break;
                case 10:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_10, "Pulse 00");
                    break;
                case 11:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_11, "Pulse 00");
                    break;
                case 12:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_12, "Pulse 00");
                    break;
                case 13:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_13, "Pulse 01");

                    break;
                case 14:
                    // pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "PinLED" + i, com.pi4j.io.gpio.PinState.LOW);
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_14, "Pulse 02");

                    break;
                case 15:
                    pin = gpio.provisionPwmOutputPin(provider, PCA9685Pin.PWM_15, "Pulse 03");

                    break;
                case 16:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "PinLED" + i, com.pi4j.io.gpio.PinState.LOW);
                    break;
                case 17:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PinLED" + i, com.pi4j.io.gpio.PinState.LOW);
                    break;
                case 18:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "PinLED" + i, com.pi4j.io.gpio.PinState.LOW);
                    break;
                case 19:
                    pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "PinLED" + i, com.pi4j.io.gpio.PinState.LOW);
                    break;

            }
            pins[i] = pin;
            if (pin.getName().startsWith("PinLED")) {


                System.out.println("GpioPinDigitalOutput installed   No " + i + "  " + pin.getName());
                GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
                ppp.low();

            } else if (pin.getName().startsWith("Pulse")) {
                GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
                System.out.println("GpioPinPwmOutput installed   No " + i);

                provider.setAlwaysOff(ppp.getPin());

            }

        }
        //  gpio.shutdown();
    }

    private void initInputListeners() {

        GpioController gpio = GpioFactory.getInstance();
        for (int i = 0; i < raspberryInputs; i++) {
            GpioPinDigitalInput myButton = null;
            switch (i) {
                case 0:
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 1:// commands for input no 1
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 2://commands for input no 2
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 3:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 4:// commands for input no 3
                    //myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_08, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_10, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 5:// commands for input no 3
                    //myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_09, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 6:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_12, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 7:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 8:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_14, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 9:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 10:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_16, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 11:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 12:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 13:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 14:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 15:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 16:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 17:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 18:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;
                case 19:// commands for input no 3
                    myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, com.pi4j.io.gpio.PinPullResistance.PULL_DOWN);
                    break;

            }
            myButton.addListener(new GpioUsageExampleListener(myButton, i));
            myButton.setShutdownOptions(true, com.pi4j.io.gpio.PinState.LOW);
        }
        //    gpio.shutdown();
    }

    private void initStates() {
        ON = new ArrayList<String>();
        OFF = new ArrayList<String>();
        ONAtTheStartOfSentence = new ArrayList<String>();
        OFFAtTheStartOfSentence = new ArrayList<String>();
        ON.add("on");
        ON.add("start");
        ON.add("open");
        OFF.add("off");
        OFF.add("stop");
        OFF.add("close");

        ONAtTheStartOfSentence.add("open");
        ONAtTheStartOfSentence.add("anoikse");
        OFFAtTheStartOfSentence.add("close");
        OFFAtTheStartOfSentence.add("kleise");

    }

    public void start() {
        new Thread() {

            @Override
            public void run() {
                try {
                    startReceivingData(); //To change body of generated methods, choose Tools | Templates.
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private void startReceivingData() throws IOException {
        boolean isOnSwitchView = false;
        try{
        serverSocket = new DatagramSocket(port);
    }catch(Exception e){}
        System.out.println("Waiting for data..");
        while (true) {
            boolean existAsLed;
            isOnSwitchView = false;
            byte[] receiveData = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            //  System.out.println(sentence);
            //             if(!addresses.contains(receivePacket.getAddress()))
            //                 addresses.add(receivePacket.getAddress());
            //             if(!allPorts.contains(receivePacket.getPort()))
            //                 allPorts.add(receivePacket.getPort());
            System.out.println("-------------->sentence::"+sentence);
            
            String uniqueUserID = "1";
            if (sentence.startsWith("userUniqueID:")) {
                uniqueUserID = sentence.split(DB.USER_ID_SPLIT)[0];
                sentence = sentence.substring((uniqueUserID + DB.USER_ID_SPLIT).length());
            } else {
                //  System.out.println("No Unique user id");
            }

            if (sentence.startsWith("speech@@@")) {
                sentence = sentence.substring("speech@@@".length(), sentence.length());
                if (jarvis != null) {
                    String posResp = jarvis.processRespond(sentence, true);
                    if (posResp != null) {
                        sendData("SpeechCommandOK " + posResp, receivePacket.getAddress(), receivePacket.getPort());
                        System.out.println("SpeechCommandOK " + posResp);
                    } else {
                        sendData("SpeechCommandNotOK " + sentence, receivePacket.getAddress(), receivePacket.getPort());
                        System.out.println("SpeechCommandNotOK " + sentence);
                    }

                    continue;
                }
            }

            if (sentence.startsWith("globalReturning")) {// used when connect for first time and send ok back, when the android receive the ok open to next view
                String sentence2 = sentence.substring("globalReturning".length());
                if (sentence2.replace(" ", "").equalsIgnoreCase(deviceName.replace(" ", ""))) {
                    addForSending(receivePacket.getAddress(), receivePacket.getPort(), uniqueUserID);
                }
            } else {
                addForSending(receivePacket.getAddress(), receivePacket.getPort(), uniqueUserID);
            }
            if (sentence.startsWith("switch ")) {

                sentence = sentence.substring("switch ".length(), sentence.length());
                //   sendData(sentence, receivePacket.getAddress(), receivePacket.getPort());
                sendToAll(sentence);
                sendTheUpdates(sentence);
                //                 for (int i = 0; i < addresses.size(); i++) {
                //                     for (int k = 0; k < allPorts.size(); k++) {
                //                         try {
                //
                //                             sendData(sentence, addresses.get(i), allPorts.get(k));
                //                             //System.out.println( addresses.get(i)+" "+allPorts.get(k)+"     "+ receivePacket.getAddress()+ " "+receivePacket.getPort()   );
                //                         } catch (IOException ex) {
                //                             ex.printStackTrace();
                //                         }
                //                     }
                //
                //                }
                if (!fr.isSwitchModeSelected) {
                    fr.manualSelected();
                    isOnSwitchView = true;
                }
            }

            //            if (isOnSwitchView) { // on switch mode return data to say that the info is here and the light will toght so the toggle button to change status
            //                // this data sends here only on Auto switch view mode
            //                sendData(sentence, receivePacket.getAddress(), receivePacket.getPort());
            ////                System.out.println("send " + sentence);
            //            }
            if (sentence.equalsIgnoreCase("chooseSpeechFunction") || sentence.equalsIgnoreCase("chooseSwitchFunction")
                    || sentence.equalsIgnoreCase("chooseSheduleFunction") || sentence.equalsIgnoreCase("chooseAutomationFunction")
                    || sentence.equalsIgnoreCase("chooseTimerFunction")) {// used when connect for first time and send ok back, when the android receive the ok open to next view
                sendData(sentence, receivePacket.getAddress(), receivePacket.getPort());
                final String sent = sentence;
                new Thread() {
                    public void run() {
                        try {

                            sendData(sent, receivePacket.getAddress(), receivePacket.getPort());
                        } catch (Exception e) {
                        }
                    }
                }.start();

            } else if (sentence.startsWith("globalReturning")) {// used when connect for first time and send ok back, when the android receive the ok open to next view
                sentence = sentence.substring("globalReturning".length());

                if (sentence.replace(" ", "").equalsIgnoreCase(deviceName.replace(" ", ""))) {
                    sendData("ok", receivePacket.getAddress(), receivePacket.getPort());

                } else {
                    sendData("wrong", receivePacket.getAddress(), receivePacket.getPort());
                    continue;
                }
                //     addresses.add(receivePacket.getAddress());
            } else if (sentence.startsWith("returning")) {// used when connect for first time and send ok back, when the android receive the ok open to next view
                sendData("ok", receivePacket.getAddress(), receivePacket.getPort());

                //     addresses.add(receivePacket.getAddress());
            } //            if (sentence.startsWith("manual_switch_view")) {
            //                String msg = "works";// getAllOutput();
            //                sendData(msg, receivePacket.getAddress(), receivePacket.getPort());
            //                System.out.println("send data");
            //            }
            else if (sentence.startsWith("getAllOutput")) { // I say than I need all the outputs
                String msg = getAllOutput();//"getput on@@@getoutt2 off";//

                if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
                    sendData("respondGetAllOutput" + msg, receivePacket.getAddress(), receivePacket.getPort());
                }
            } else if (sentence.startsWith("getAllCommandsOutput")) { // I say than I need all the commands that open ports with each one state ( Example : "kitchen light on" kitchen light is the commands and on or of are the states  )

                String msg = getAllCommandOutput();//"getput on@@@getoutt2 off";//
                if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
                    sendData("respondGetAllCommandsOutput" + msg, receivePacket.getAddress(), receivePacket.getPort());
                }

            } else if (sentence.startsWith("saveShedule")) { // I say than I need all the commands that open ports with each one state ( Example : "kitchen light on" kitchen light is the commands and on or of are the states  )
                //prepei na stelnw device id

                ///saveShedule:DeviceID:0##CommandText:kitchen lights##ActiveDays:2 on3 on5 off##ActiveTime:00:00##IsWeekly:true##IsActive:true
                String usingCommand = sentence.substring("saveShedule:DevideID:".length(), sentence.length());
                String[] list = usingCommand.split(DB.COMMAND_SPLIT_STRING);
                String wantedDeviceIDString = list[0];
                if (Integer.parseInt(wantedDeviceIDString) == DeviceID) {

                    usingCommand = usingCommand.substring((wantedDeviceIDString + DB.COMMAND_SPLIT_STRING).length(), usingCommand.length());
                    String out = db.add(usingCommand);
                    String cmd=usingCommand.substring("CommandText:".length(),usingCommand.length()).split(DB.COMMAND_SPLIT_STRING)[0];

                    if (out != null) {
                        if (!out.equals("addedNotOk")) {
                            sendToAllExcept("Shedules:" + out, receivePacket.getAddress(), receivePacket.getPort());
                            sendData("addedOk", receivePacket.getAddress(), receivePacket.getPort());
                        } else {
                            sendData("addedNotOk", receivePacket.getAddress(), receivePacket.getPort());
                        }
                    }


if(fr==null||SheduleView.selectedOption==null||fr.isSheduleModeSelected&&(!SheduleView.selectedOption.startsWith("all")))
                        new SheduleView(fr).createByDeviceGUI(cmd);
                    

                    
                }
            } else if (sentence.startsWith("updateShedule")) { // I say than I need all the commands that open ports with each one state ( Example : "kitchen light on" kitchen light is the commands and on or of are the states  )
                //prepei na stelnw device id+
                String usingCommand = sentence.substring(("updateShedule:" + DB.DEVICE_ID).length(), sentence.length());
                String[] list = usingCommand.split(DB.COMMAND_SPLIT_STRING);
                String wantedDeviceIDString = list[0];
                if (Integer.parseInt(wantedDeviceIDString) == DeviceID) {

                    usingCommand = usingCommand.substring((wantedDeviceIDString + DB.COMMAND_SPLIT_STRING + DB.COMMAND_ID).length(), usingCommand.length());

                    String wantedCommandID = usingCommand.split(DB.COMMAND_SPLIT_STRING)[0];

                    usingCommand = usingCommand.substring((wantedCommandID + DB.COMMAND_SPLIT_STRING).length(), usingCommand.length());

                    db.updateShedule(usingCommand, wantedCommandID);//thelw command id edw
                                        String cmd=usingCommand.substring("CommandText:".length(),usingCommand.length()).split(DB.COMMAND_SPLIT_STRING)[0];

if(fr==null||SheduleView.selectedOption==null||fr.isSheduleModeSelected&&(!SheduleView.selectedOption.startsWith("all")))
                        new SheduleView(fr).createByDeviceGUI(cmd);
                    
                }
            } else if (sentence.startsWith("updateSingleShedule")) { // I say than I need all the commands that open ports with each one state ( Example : "kitchen light on" kitchen light is the commands and on or of are the states  )
                //prepei na stelnw device id+

                //updateSingleShedule:DeviceID:0##CommandID:0##CommandText:kitchen lights on##IsActive:false
                String usingCommand = sentence.substring(("updateSingleShedule:" + DB.DEVICE_ID).length(), sentence.length());
                String[] list = usingCommand.split(DB.COMMAND_SPLIT_STRING);
                String wantedDeviceIDString = list[0];
                if (Integer.parseInt(wantedDeviceIDString) == DeviceID) {
                    
                    usingCommand = usingCommand.substring((wantedDeviceIDString + DB.COMMAND_SPLIT_STRING + DB.COMMAND_ID).length(), usingCommand.length());

                    String[] list2 = usingCommand.split(DB.COMMAND_SPLIT_STRING);
                    String wantedCommandID = list2[0];

                    usingCommand = list2[1].substring(DB.COMMAND_TEXT_STRING.length(), list2[1].length());
                    String stringModeModify = list2[2];
                    db.updateSingleShedule(usingCommand, wantedCommandID, stringModeModify);//thelw command id edw


                                        String cmd=usingCommand.substring("CommandText:".length(),usingCommand.length()).split(DB.COMMAND_SPLIT_STRING)[0];
if(fr==null||SheduleView.selectedOption==null||fr.isSheduleModeSelected&&(!SheduleView.selectedOption.startsWith("all")))
                        new SheduleView(fr).createByDeviceGUI(cmd);
                    
                }
            } else if (sentence.startsWith("removeShedule")) { // I say than I need all the commands that open ports with each one state ( Example : "kitchen light on" kitchen light is the commands and on or of are the states  )

                //
                //// px removeShedule(1)" prepei na stelnw device id
                String usingCommand = sentence.substring(("removeShedule:" + DB.DEVICE_ID).length(), sentence.length());
                String[] list = usingCommand.split(DB.COMMAND_SPLIT_STRING);
                String wantedDeviceIDString = list[0];

                if (Integer.parseInt(wantedDeviceIDString) == DeviceID) {
                   
                    usingCommand = list[1].substring((DB.COMMAND_TEXT_STRING).length(), list[1].length());

                    db.removeShedule(usingCommand, list[2]);//thelw command id edw

if(fr==null||SheduleView.selectedOption==null||fr.isSheduleModeSelected&&(!SheduleView.selectedOption.startsWith("all")))
                        new SheduleView(fr).createByDeviceGUI( list[2]);
                    
                }
            } else if (sentence.equals("getShedules")) {

                sendData("Shedules:DeviceID:" + DeviceID + DB.COMMAND_SPLIT_STRING + db.getAllSheduleText(), receivePacket.getAddress(), receivePacket.getPort());
            } else if (sentence.equals("getIDS")) {

                sendData("getIDS" + Integer.toString(DeviceID), receivePacket.getAddress(), receivePacket.getPort());
            } else if (sentence.startsWith("getCommandID")) {
                String idCommandWanted = sentence.substring("getCommandID".length(), sentence.length());

                if (idCommandWanted.equalsIgnoreCase(Integer.toString(DeviceID))) {
                    String msg = getAllCommandOutput();//"getput on@@@getoutt2 off";//
                    if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
                        sendData("getComandID" + msg, receivePacket.getAddress(), receivePacket.getPort());
                    }
                }

            } else if (sentence.startsWith("newTimer:")) {
                // create new Timer
                String UsingCommand = sentence.substring("newTimer:".length(), sentence.length());
                String[] list = UsingCommand.split(DB.COMMAND_SPLIT_STRING);
                String device_id = list[0].substring(DB.DEVICE_ID.length());
                if (Integer.parseInt(device_id) != DeviceID) {
                    return;
                }
                String timeStamp = list[1].substring(DB.TIME_STAMP.length());
                String command_text = list[2].substring(DB.COMMAND_TEXT_STRING.length());
                if (!db.conainsCommandInDevice(command_text)) {
                    return;
                }
                String timeInSeconds = list[3].substring(DB.SENDING_TIME.length());
                System.out.println("TIMER : device_id = " + device_id + " , timeStamp= " + timeStamp + " , command_text= "
                        + command_text + " , timeInSeconds= " + timeInSeconds);
                sendData("newTimerOK", receivePacket.getAddress(), receivePacket.getPort());
                if (!TimerCountdown.containsTimestamp(Long.parseLong(timeStamp))) {
                    if (!fr.isTimerModeSelected) {
                        new TimerView(fr);
                    }
                    TimerCountdown timer = new TimerCountdown(this, command_text, timeInSeconds, timeStamp);
                    timer.start();
                    sendToAllExcept("Timers:DeviceID:" + DeviceID + DB.COMMAND_SPLIT_STRING + timer.toString(), receivePacket.getAddress(), receivePacket.getPort());
                }
            } else if (sentence.startsWith("getTimers")) {

                sendData("Timers:DeviceID:" + DeviceID + DB.COMMAND_SPLIT_STRING + TimerCountdown.getAllTimers(), receivePacket.getAddress(), receivePacket.getPort());

            } else if (sentence.startsWith("removeTimer:")) {
                // removeTimer:DeviceID:0CommandID:0##TimeStamp:1457625771345##CommandText:kitchen lights on
                String UsingCommand = sentence.substring("removeTimer:".length(), sentence.length());

                String[] list = UsingCommand.split(DB.COMMAND_SPLIT_STRING);
                String device_id = list[0].substring(DB.DEVICE_ID.length());
                if (Integer.parseInt(device_id) != DeviceID) {
                    return;
                }
                String commandID = list[1].substring(DB.COMMAND_ID.length());
                String timeStamp = list[2].substring(DB.TIME_STAMP.length());
                String command_text = list[3].substring(DB.COMMAND_TEXT_STRING.length());

                if (!db.conainsCommandInDevice(command_text)) {
                    return;
                }
                if (!fr.isTimerModeSelected) {
                    new TimerView(fr);
                }
                TimerCountdown.removeFromList(Long.parseLong(timeStamp));
                sendToAll(sentence);

            }
            //
            //             if (sentence.startsWith("update_manual_mode")) { // I say than I need all the commands that open ports with each one state ( Example : "kitchen light on" kitchen light is the commands and on or of are the states  )
            //
            //                 String msg = "kouzina fwta on@@@domatio fos on";//getAllOutput();
            //                 if (msg != null && !msg.replaceAll(" ", "").equalsIgnoreCase("")) {
            //                     sendData("update_manual_mode" + msg, receivePacket.getAddress(), receivePacket.getPort());
            //                 }
            //
            //             }
            existAsLed = processLedString(sentence);
            if (!existAsLed) {

                processCommandString(sentence);

            }
        }

    }

    protected String getAllOutput() {

        String output = new String();
        try {
            //GpioController  gpio = GpioFactory.getInstance();
            GpioPinOutput pin = null;
            String isDoing = new String();
            for (int i = 0; i < outputCommands.length; i++) {

                pin = pins[i];
                if (pin == null) {
                    continue;
                }
                boolean isHight = isHight(pin);


                if (isHight) {
                    isDoing = ON.get(0);
                } else {
                    isDoing = OFF.get(0);
                }

                //for (int j = 0; j < outputCommands[i].size(); j++) {
                if (i != 0) {
                    output += "@@@";

                }
                output += outputCommands[i].get(0) + " " + isDoing;

                //  }
            }
            //  gpio.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    boolean isHightfromSwitch(boolean curentStateIsHight){


        return curentStateIsHight;

    }
    boolean isHight(GpioPinOutput pin) {
        boolean isHight = false;

        if (pin.getName().startsWith("PinLED")) {


            GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
            if (ppp.getState().isHigh()) {
                isHight = true;
            }
        } else if (pin.getName().startsWith("Pulse")) {
            GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
            int[] iii = provider.getPwmOnOffValues(ppp.getPin());

            if (iii[1] == 4096) {
                isHight = false;
            }

            if (iii[0] == 4096) {
                isHight = true;
            }


        }


        return isHight;
    }

    protected String getAllCommandOutput() {
        String output = new String();
        try {
            //GpioController  gpio = GpioFactory.getInstance();
            GpioPinOutput pin = null;

            for (int i = 0; i < outputPowerCommands.length; i++) {
                ArrayList<String> isOpenList = new ArrayList<String>();
                String finalIsDoing = ON.get(0);
                for (int j = 0; j < activatePortOnCommand[i].size(); j++) {

                    String isDoing = new String();

                    int pinID = getRealOutLed(activatePortOnCommand[i].get(j));

                    if (pinID != -1) {
                        pin = pins[pinID];
                    }

                    if (pin == null) {
                        initGpioPinDigitalOutputs();
                        pin = pins[activatePortOnCommand[i].get(j)];
                    }
                    if (pin == null) {
                        continue;
                    }


                    if (isHight(pin)) {
                        isDoing = ON.get(0);
                    } else {
                        isDoing = OFF.get(0);

                    }
                    isOpenList.add(isDoing);

                }
                if (i != 0) {
                    output += "@@@";

                }
                if (isOpenList.contains(OFF.get(0))) {
                    finalIsDoing = OFF.get(0);
                } else {
                    finalIsDoing = ON.get(0);
                }
                if (!outputPowerCommands[i].isEmpty()) {
                    output += outputPowerCommands[i].get(0) + " " + finalIsDoing;
                }

            }
            //  gpio.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    private int getRealOutLed(int port) {
        int pinID = port;
        //         switch(port){
        //             case 0:
        //             pinID=0;
        //             break;
        //             case 2:
        //             pinID=1;
        //             break;
        //             case 3:
        //             pinID=2;
        //             break;
        //             case 7:
        //             pinID=3;
        //             break;
        //             case 8:
        //             pinID=4;
        //             break;
        //             case 9:
        //             pinID=5;
        //             break; case 12:
        //             pinID=6;
        //             break;
        //             case 13:
        //             pinID=7;
        //             break;
        //         }

        return pinID;
    }

    private void sendData(String msg, InetAddress IPAddress, int port) throws IOException {
        byte[] sendData;
        sendData = msg.getBytes();
        DatagramPacket sendPacket
                = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        try {
            msg = StringUtils.stripAccents(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverSocket.send(sendPacket);
    }

    protected void processCommandString(String input) {
        String isDoing = "off";

        boolean found = false;

        for (int i = 0; i < outputPowerCommands.length; i++) {

            for (int j = 0; j < outputPowerCommands[i].size(); j++) {

                if (input.startsWith(outputPowerCommands[i].get(j))) {
                    isDoing = input.replace(outputPowerCommands[i].get(j), "").replaceAll(" ", "");
                } else {

                    String firstWord = input.split(" ")[0];
                    if (OFFAtTheStartOfSentence.contains(firstWord)) {
                        isDoing = "off";
                        input = input.substring((firstWord + " ").length() + 1, input.length());
                    } else if (ONAtTheStartOfSentence.contains(firstWord)) {
                        isDoing = "on";
                        input = input.substring((firstWord + " ").length() + 1, input.length());

                    }
                }
                if (input.startsWith(outputPowerCommands[i].get(j))) {

                    if (ON.contains(isDoing)) {

                        for (int k = 0; k < activatePortOnCommand[i].size(); k++) {
                            ToggleLedNo(activatePortOnCommand[i].get(k), ON.get(0));
                        }
                    } else if (OFF.contains(isDoing)) {

                        for (int k = 0; k < activatePortOnCommand[i].size(); k++) {
                            ToggleLedNo(activatePortOnCommand[i].get(k), OFF.get(0));
                        }

                    }

                }

            }

        }
    }

    private void ToggleLedNo(int number, String state) {
        ToggleLedNo(number, state, true);
    }

    private void ToggleLedNo(int number, String state, boolean isFromInput) {
        // GpioController  gpio = GpioFactory.getInstance();
        GpioPinOutput pin = null;

        int portOut = number;
        if (isFromInput) {
            portOut = getRealOutLed(number);
        }
        pin = pins[portOut];

        if (pin == null) {
            initGpioPinDigitalOutputs();
            pin = pins[number];
        }
        if (pin == null) {
            return;
        }


        if (state.equalsIgnoreCase(ON.get(0))) {
            checkRelativePinsForExclusiveOutput(portOut,true);
            if (pin.getName().startsWith("PinLED")) {
                GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
                ppp.high();

            } else if (pin.getName().startsWith("Pulse")) {
                GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
                provider.setAlwaysOn(ppp.getPin());
            }

        } else if (state.equalsIgnoreCase(OFF.get(0))) {

            if (pin.getName().startsWith("PinLED")) {
                GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
                ppp.low();

            } else if (pin.getName().startsWith("Pulse")) {
                GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
                provider.setAlwaysOff(ppp.getPin());
            }
        }

        //           String usingString=outputPowerCommands[i].get(j)+""+isDoing;
        //
        //         if(fr!=null)
        //         fr.changeState(usingString);
        if (fr.isSwitchModeSelected) {
            fr.updateManual();
        }

    }


    public void checkRelativePinsForExclusiveOutput(int id,boolean isGoingHigh){
        if (isGoingHigh){
        for (int i=0;i<exclusiveOutputs.size();i++){
            int[] list=  strArrayToIntArray(exclusiveOutputs.get(i).split(","));

            for (int ii=0;ii<list.length;ii++){

                if (list[ii]==id){
                    list[ii]=-1;
                    switchExclusivePins(list,isGoingHigh);
                    break;
                }
            }


        }}

    }


    public void switchExclusivePins(int[] idList,boolean isGoingHigh){


        for (int id:idList ){
            if (id<0){
                continue;
            }
        GpioPinOutput pin = null;
        pin=pins[id];
        if (isGoingHigh&&isHight(pin)) {

            if (pin.getName().startsWith("PinLED")) {
                GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
                ppp.low();

            } else if (pin.getName().startsWith("Pulse")) {
                GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
                provider.setAlwaysOff(ppp.getPin());
            }
        sendToAll("switch "+DeviceID+" output "+id+" off");
       // sendToAll("switch "+outputPowerCommands[id].get(0)+" off");
       
       for(int i=0;i<activatePortOnCommand.length;i++){
        ArrayList<Integer> prt=activatePortOnCommand[i];
        if(prt.contains(id)){
            sendToAll("switch "+outputPowerCommands[i].get(0)+" off");

        }
        }
       
        }}

    }





    public  int[] strArrayToIntArray(String[] a){
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Integer.parseInt(a[i].replaceAll(" ",""));
        }

        return b;
    }

    protected boolean processLedString(String input) {
        // get a handle to the GPIO controller
        // GpioController  gpio = GpioFactory.getInstance();
        boolean found = false;
        GpioPinOutput pin;
        String isDoing = "off"; // creating the pin with parameter PinState hIGH
        // will instantly power up the pin
        for (int i = 0; i < outputCommands.length; i++) {

            for (int j = 0; j < outputCommands[i].size(); j++) {
                if (input.startsWith(outputCommands[i].get(j))) {
                    isDoing = input.replace(outputCommands[i].get(j), "").replaceAll(" ", "");
                } else {
                    try {
                        String firstWord = input.split(" ")[0];
                        if (OFFAtTheStartOfSentence.contains(firstWord)) {
                            isDoing = "off";
                            input = input.substring((firstWord + " ").length() + 1, input.length());
                        } else if (ONAtTheStartOfSentence.contains(firstWord)) {
                            isDoing = "on";
                            input = input.substring((firstWord + " ").length() + 1, input.length());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (input.startsWith(outputCommands[i].get(j))) {
                    found = true;

                    pin = pins[i];
                    isDoing = input.replace(outputCommands[i].get(j), "").replaceAll(" ", "");
                    if (ON.contains(isDoing)) {
                        checkRelativePinsForExclusiveOutput(i,true);

                        if (pin.getName().startsWith("PinLED")) {
                            GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
                            ppp.high();

                        } else if (pin.getName().startsWith("Pulse")) {
                            GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
                            provider.setAlwaysOn(ppp.getPin());
                        }
                    } else if (OFF.contains(isDoing)) {
                        if (pin.getName().startsWith("PinLED")) {
                            GpioPinDigitalOutput ppp = (GpioPinDigitalOutput) pin;
                            ppp.low();

                        } else if (pin.getName().startsWith("Pulse")) {
                            GpioPinPwmOutput ppp = (GpioPinPwmOutput) pin;
                            provider.setAlwaysOff(ppp.getPin());
                        }
                    }
                    if (fr.isSwitchModeSelected) {
                        fr.updateManual();
                    }
                    break;

                }

            }
        }

        return found;
    }

    static Fr fr;
    static Jarvis jarvis;

    public static void main(String args[]) throws Exception {
        SH shs = new SH();
        fr = new Fr(shs);
        shs.start();
        try {
            jarvis = new Jarvis(shs);
            // jarvis.run();

        } catch (Exception e) {
        }
    }

//    boolean isRunning = false;

    public class GpioUsageExampleListener implements GpioPinListenerDigital {

        String state = null;
        String command = null;
        int id = -1;
        String curentState = null;
        GpioPinDigitalInput in;
        boolean isHightPrevious =false;
        public GpioUsageExampleListener(GpioPinDigitalInput in, int id) {
            this.in = in;

            this.id = id;
            isHightPrevious=in.isHigh();
            this.command = outputCommands[id].get(0);
        }


        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//            if (isRunning) {
//                return;
//            }
//            isRunning = true;

//            boolean startState = in.getState().isHigh();
//           String tempState = "" + in.getState().isHigh();
//            if ((curentState != null && curentState == tempState)) {
//                System.out.println("same state as input");
//                return;
//            } else if (isHightPrevious ==in.isHigh()) {
//                System.out.println("might false input");
//                return;
//            } else if (startState != in.getState().isHigh()) {
//                System.out.println("startState!=in.getState().isHigh()");
//                return;
//            }
//            curentState = tempState;
//            // display pin state on console
//            state = null;

//            boolean isHigh = isHightfromSwitch(isHightPrevious !=in.isHigh());


boolean isHigh=false;
          
if(!isGoingToBeOn_OnSwitchOn){
             isHigh = isHight(pins[id]);
             if (isHigh) {
                state = OFF.get(0);
            } else {
                state = ON.get(0);
            }
            }else{
                         isHigh =in.isHigh();
                         if (!isHigh) {
                state = OFF.get(0);
            } else {
                state = ON.get(0);
            }
            }
                       System.out.println("isHigh"+isHigh);
                             System.out.println("isHightPrevious"+isHightPrevious);
       if((isHigh==isHightPrevious)&&isGoingToBeOn_OnSwitchOn){
           
           System.out.println("returning input, same input");
           return;}
            
            //else {
            //  System.out.println("problem state = " + state);
            //return;
            //}
            

            ToggleLedNo(id, state, false);
            new Thread() {
                public void run() {
                    try {

                        sendToAll(command + " " + state);
                        System.out.println(command + " GpioPinListenerDigital " + state);
                        sendTheUpdates(command + " " + state);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();



//            isRunning = false;
            //System.out.println( addresses.get(i)+" "+port+"     "+ receivePacket.getAddress()+ " "+receivePacket.getPort()   );
            //
            //             for (int i = 0; i < addresses.size(); i++) {
            //                 try {
            //                     sendData(command + " " + state, addresses.get(i), port);
            //                                 System.out.println(command + " " + state);
            //                 } catch (IOException ex) {
            //                     ex.printStackTrace();
            //                 }
            //             }
                        isHightPrevious =in.isHigh();
        }
    }

    public void sendToAll(final String message) {

        new Thread() {
            public void run() {
                try {

                    for (int i = 0; i < sendingTo.size(); i++) {
                        Object[] obj = sendingTo.get(i);
                        InetAddress ia = (InetAddress) obj[0];
                        int prt = (Integer) obj[1];
                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        sendData(message, ia, prt);

                    }
                    //   }
                    //                     for (int i = 0; i < addresses.size(); i++) {
                    //                         for (int k = 0; k < allPorts.size(); k++) {
                    //                             try{
                    //                                 Thread.sleep(50);
                    //                             }catch(Exception e){}
                    //                             sendData(message, addresses.get(i), allPorts.get(k));
                    //
                    //                         }
                    //
                    //     }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public void sendToAllExcept(final String message, InetAddress ia, int port) {

        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < sendingTo.size(); i++) {
                        Object[] obj = sendingTo.get(i);
                        InetAddress ia = (InetAddress) obj[0];
                        int prt = (Integer) obj[1];
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                        }
                        if (!ia.equals(ia) && prt != port) {
                            sendData(message, ia, prt);
                        }

                    }
                    //                     for (int i = 0; i < addresses.size(); i++) {
                    //                         for (int k = 0; k < allPorts.size(); k++) {
                    //                             if(!addresses.get(i).equals(ia)&&allPorts.get(k)!=port)
                    //                                 sendData(message, addresses.get(i), allPorts.get(k));
                    //
                    //                         }
                    //
                    //                     }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private Calendar calendar;

    private String getTime(Calendar calendar) {

        Date curentDate = calendar.getTime();
        String hour = Integer.toString(curentDate.getHours());
        String min = Integer.toString(curentDate.getMinutes());
        String sec = Integer.toString(curentDate.getSeconds());
        String output;

        if (hour.length() != 2) {
            hour = "0" + hour;
        }

        if (min.length() != 2) {
            min = "0" + min;
        }
        if (sec.length() != 2) {
            sec = "0" + sec;
        }

        return hour + ":" + min+":"+sec;
    }

    private String getDay(Calendar calendar) {

        Date curentDate = calendar.getTime();
        return Integer.toString((curentDate.getDay() + Calendar.SUNDAY));
    }

    private boolean isSearchingForShedules = true;
    private int secondsSheduleDelay = 1;
    private String prevTime;

    private class SheduleThread extends Thread {

        public void run() {
            new Thread() {
                public void run() {

                }
            }.start();
            while (isSearchingForShedules) {
                try {

                    Thread.sleep(secondsSheduleDelay * 1000);
                    Calendar calendar = Calendar.getInstance();

                    if (fr.shv != null) {
                        if (prevTime == null || !prevTime.equals(getTime(calendar))) {
                            prevTime = getTime(calendar);
                        } else {
                            continue;
                        }
                    }
                    for (int i = 0; i < db.getShedules().size(); i++) {
                        Shedule shedule = db.getShedules().get(i);
                        if (Boolean.parseBoolean(shedule.getIsActive())) {

                            if (shedule.getActiveDays().contains(getDay(calendar))) {

                               
                                if (shedule.getTime().equals(getTime(calendar))) {

                                    // excecute command
                                    String extraString = null;
                                    if (shedule.getActiveDays().contains(getDay(calendar) + " on")) {
                                        extraString = " on";
                                    }
                                    if (shedule.getActiveDays().contains(getDay(calendar) + " off")) {
                                        extraString = " off";
                                    }

                                    processCommandString(shedule.getCommandText() + extraString);

                                    if (!Boolean.parseBoolean(shedule.getIsWeekly())) {
                                        shedule.setActiveDays((shedule.getActiveDays().replace(getDay(calendar), "")));
                                    }
                                    //  sendToAll("UpdatedOk:DeviceID:"+SH.DeviceID+DB.COMMAND_SPLIT_STRING+shedule.toString());
                                    String out = db.updateShedule(shedule);
                                    for (int j = 0; j < outputPowerCommands.length; j++) {
                                        String mode = null;
                                        String shCm = null;
                                        if (shedule.getCommandText().endsWith(" on")) {
                                            shCm = shedule.getCommandText().substring(0, shedule.getCommandText().length() - " on".length());
                                            mode = "on";
                                        } else if (shedule.getCommandText().endsWith("on")) {
                                            shCm = shedule.getCommandText().substring(0, shedule.getCommandText().length() - "on".length());
                                            mode = "on";
                                        } else if (shedule.getCommandText().endsWith(" off")) {
                                            shCm = shedule.getCommandText().substring(0, shedule.getCommandText().length() - " off".length());
                                            mode = "off";
                                        } else if (shedule.getCommandText().endsWith("off")) {
                                            shCm = shedule.getCommandText().substring(0, shedule.getCommandText().length() - "off".length());
                                            mode = "off";
                                        }

                                        if (outputPowerCommands[j].get(0).equals(shCm)) {

                                            for (int h = 0; h < activatePortOnCommand[j].size(); h++) {
                                                //
                                                for (int p = 0; p < pins.length; p++) {

                                                    if (pins[p].getPin().getAddress() == activatePortOnCommand[j].get(h)) {

                                                        // System.out.println(DeviceID+ " switch "+p+" "+mode);
                                                        sendToAll("switch " + DeviceID + " output " + p + " " + mode);
                                                    }
                                                }
                                             
                                            }

                                        }
                                    }
                                    sendToAll("switch " + shedule.getCommandText() + extraString);
                                    sendTheUpdates(shedule.getCommandText() + extraString);
                                    sendToAll(out);

                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    protected void sendTheUpdates(String command) {

        String mode = null;
        String shCm = null;
        if (command.endsWith(" on")) {
            shCm = command.substring(0, command.length() - " on".length());
            mode = "on";
        } else if (command.endsWith("on")) {
            shCm = command.substring(0, command.length() - "on".length());
            mode = "on";
        } else if (command.endsWith(" off")) {
            shCm = command.substring(0, command.length() - " off".length());
            mode = "off";
        } else if (command.endsWith("off")) {
            shCm = command.substring(0, command.length() - "off".length());
            mode = "off";
        }
        //if(outputPowerCommands.contains(shCm)){

        for (int j = 0; j < outputPowerCommands.length; j++) {

            if (outputPowerCommands[j].get(0).equals(shCm)) {
                for (int h = 0; h < activatePortOnCommand[j].size(); h++) {
                    //

                    for (int p = 0; p < pins.length; p++) {

                        if (pins[p].getPin().getAddress() == activatePortOnCommand[j].get(h)) {

                            int sendid = getRealOutLed(p);
                         //   System.out.println("switch " + outputCommands[p].get(0) + " " + mode);
                          
                            sendToAll("switch " + outputCommands[p].get(0) + " " + mode);
                        }
                    }

                   
                }

                for (int k = 0; k < activatePortOnCommand.length; k++) {
                    if (k != j) {
                        if (activatePortOnCommand[j].containsAll(activatePortOnCommand[k])) {

                            sendToAll("switch " + outputPowerCommands[k].get(0) + " " + mode);

                        }
                        if (activatePortOnCommand[k].containsAll(activatePortOnCommand[j])) {
                            if (mode.equals(OFF.get(0))) {
                                sendToAll("switch " + outputPowerCommands[k].get(0) + " " + mode);
                            } else if (mode.equals(ON.get(0))) {
                                boolean isActive = true;
                                for (int kk = 0; kk < activatePortOnCommand[k].size(); kk++) {
                                    //  System.out.println("j= "+j+"  k= "+k+" kk= "+kk+"  "+activatePortOnCommand[k].get(kk)+"   update 2 switch "+getPinFromOutput(activatePortOnCommand[k].get(kk)).isHigh());
                                    if (!activatePortOnCommand[j].contains(activatePortOnCommand[k].get(kk))) {
                                        if (!isHight(getPinFromOutput(activatePortOnCommand[k].get(kk)))) {
                                            isActive = false;
                                        }
                                    }
                                }
                                if (isActive) {
                                    sendToAll("switch " + outputPowerCommands[k].get(0) + " " + mode);

                                }
                            }
                            // System.out.println("update 2 switch "+outputPowerCommands[k].get(0)+" "+mode);
                        }

                    }
                }

            }

        }
        for (int i = 0; i < outputCommands.length; i++) {
            //System.out.println(outputCommands[i]+"  "+(shCm) );
            if (outputCommands[i].get(0).equals(shCm)) {
                //   System.out.println("outputCommands[i].equals(shCm) ");

                boolean isHight;
                if (mode.equals(ON.get(0))) {
                    isHight = true;
                } else {
                    isHight = false;
                }

                boolean isActive = true;
                int pinAddress = pins[i].getPin().getAddress();
                for (int j = 0; j < activatePortOnCommand.length; j++) {

                    if (activatePortOnCommand[j].contains(pinAddress)) {
                        for (int k = 0; k < activatePortOnCommand[j].size(); k++) {

                            //   System.out.println(activatePortOnCommand[j]+" k= "+k+"  j= "+j+" i= "+i+"  "+activatePortOnCommand[j].get(k)+"  "+
                            //   Boolean.toString(getPinFromOutput(activatePortOnCommand[j].get(k)).isHigh()==isHight));
                            if (pinAddress != activatePortOnCommand[j].get(k)) {
                                if (isHight(getPinFromOutput(activatePortOnCommand[j].get(k))) != isHight) {
                                    isActive = false;
                                    break;
                                }
                            }
                        }

                        if (!isActive) {
                            isActive = true;
                            for (int k = 0; k < activatePortOnCommand[j].size(); k++) {

                                //   System.out.println(activatePortOnCommand[j]+" k= "+k+"  j= "+j+" i= "+i+"  "+activatePortOnCommand[j].get(k)+"  "+
                                //   Boolean.toString(getPinFromOutput(activatePortOnCommand[j].get(k)).isHigh()==isHight));
                                if (isHight) {
                                    if (pinAddress != activatePortOnCommand[j].get(k)) {
                                        if (isHight(getPinFromOutput(activatePortOnCommand[j].get(k))) != true) {
                                            isActive = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        //                         if(!isActive){
                        //                             isActive=true;
                        //                         for(int k=0;k<activatePortOnCommand[j].size();k++){
                        //
                        //                             //   System.out.println(activatePortOnCommand[j]+" k= "+k+"  j= "+j+" i= "+i+"  "+activatePortOnCommand[j].get(k)+"  "+
                        //                             //   Boolean.toString(getPinFromOutput(activatePortOnCommand[j].get(k)).isHigh()==isHight));
                        //
                        //                             if(pinAddress!=activatePortOnCommand[j].get(k))
                        //                                 if(getPinFromOutput(activatePortOnCommand[j].get(k)).isHigh()==isHight){
                        //                                     isActive=false;
                        //                                     break;
                        //                                 }
                        //                         }
                        //                         }
                        if (isActive) {
                           // System.out.println("switch " + outputPowerCommands[j].get(0) + " " + mode);
                            sendToAll("switch " + outputPowerCommands[j].get(0) + " " + mode);

                        }
                    }
                }

            }
        }

        //             for(int j=0;j<outputPowerCommands.length;j++){
        //
        //                 for(int h=0;h<activatePortOnCommand[j].size();h++){
        //
        //                     boolean isActive=true;
        //                     for(int p=0;p<pins.length;p++){
        //
        //                         if(pins[p].getPin().getAddress()== activatePortOnCommand[j].get(h)){
        //                             if(!pins[p].isHigh()){
        //                                 isActive=false;
        //                             }
        //
        //                     }
        //                     if(isActive&&pins.length>=1){sendToAll("switch "+outputPowerCommands[j].get(0)+" "+mode);
        //                     }
        //
        //
        //             }
        //         }}
        if (fr.isSwitchModeSelected) {
            fr.updateManual();
        }
    }

    private GpioPinOutput getPinFromOutput(int output) {
        GpioPinOutput p = null;
        for (int i = 0; i < pins.length; i++) {

            if (pins[i].getPin().getAddress() == output) {

                p = pins[i];
            }
        }
        return p;
    }

    ArrayList<Object[]> sendingTo = new ArrayList<Object[]>();
    private ArrayList<ResetThread> resetSendingTo = new ArrayList<ResetThread>();

    private void addForSending(InetAddress address, int port, String userID) {
        boolean contains = false;
        if (sendingTo.size() >= 20) {
            for (int i = 0; i < resetSendingTo.size() / 2; i++) {
                resetSendingTo.get(i).isRunning = false;
            }
        }

        for (int i = 0; i < sendingTo.size(); i++) {
            Object[] obj = sendingTo.get(i);
            InetAddress ia = (InetAddress) obj[0];
            int prt = (Integer) obj[1];
            String id = (String) obj[2];
            if (ia.equals(address) && prt == port) {
                contains = true;
                for (int j = 0; j < resetSendingTo.size(); j++) {
                    ResetThread rt = resetSendingTo.get(j);
                    if (rt.obj == obj || rt.obj[0].equals(obj[0]) && rt.obj[1].equals(obj[1])) {
                        rt.remaining = rt.maxTime;
                        System.out.println("renew time address =" + ia.toString() + "  port=" + prt);
                    }
                    // resetSendingTo.get(i).remaining= resetSendingTo.get(i).maxTime;
                }
            } else if (userID.equals(id)) {
                contains = true;
                for (int j = 0; j < resetSendingTo.size(); j++) {
                    ResetThread rt = resetSendingTo.get(j);
                    if (rt.userID.equals(userID)) {
                        rt.remaining = rt.maxTime;
                        obj[0] = address;
                        obj[1] = port;
                        System.out.println("renew time address =" + ia.toString() + "  port=" + prt + "  USER ID=" + userID);
                    }
                    // resetSendingTo.get(i).remaining= resetSendingTo.get(i).maxTime;
                }
            }

        }
        if (!contains) {
            final Object[] objects = {address, port, userID};
            sendingTo.add(objects);

            ResetThread thread = new ResetThread(objects) {

                public void run() {
                    System.out.println("added =" + objects[0].toString() + "  port=" + objects[1] + "  USER ID=" + objects[2]);
                    while (remaining > 0 && isRunning) {
                        try {
                            Thread.sleep(sleepingtime);
                        } catch (Exception e) {
                        }
                        remaining -= sleepingtime;

                    }
                    sendingTo.remove(objects);
                    resetSendingTo.remove(this);
                    System.out.println("removed =" + objects[0].toString() + "  port=" + objects[1]);
                }
            };
            thread.start();
            resetSendingTo.add(thread);
        }
       // System.out.println("sendingTo.size() =" + sendingTo.size());

    }

    private class ResetThread extends Thread {

        final int maxTime = 20 * 60 * 1000;
        int remaining = maxTime, sleepingtime = 0 * 1000;
        //   ArrayList <Object[]> list;
        boolean isRunning = true;
        Object[] obj;
        String userID;

        public ResetThread(Object[] obj) {
            this.obj = obj;
            this.userID = (String) obj[2];
        }
        //public void run(){}

    }
}