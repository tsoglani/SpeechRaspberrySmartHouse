import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.Date;


public class Fr extends JFrame
{
    // instance variables - replace the example below with your own
    private JButton manual;
    private JButton timer;
    private JButton auto;
    protected boolean isSwitchModeSelected=false;
    protected boolean isTimerModeSelected=false;
    protected boolean isSheduleModeSelected=false;
    protected boolean isOnMainMenu=true;
    private String state="off";
    protected JButton home;
    protected SH sh;
    JButton[] switcButtons;
    protected  SheduleView shv;
    ImageIcon speechIcon;
    ImageIcon stop_speechIcon;
    JToggleButton toggle;
    JButton speechButton;
    JLabel speechTextLabel;
    JButton alarmButton;
    JButton findMobileButton;
    ImageIcon alarmIcon;
    JPanel extraInfoPanel;
    ImageIcon findMobileIcon,cancelOkIcon,cancelNot_okIcon,upIcon,downIcon;
    private ImageIcon findMobileAbcIcon;
    JPanel addAlarmPanel;
    /**
     * Constructor for objects of class Fr
     */
    double width,height;
    private String usinAlarmText;
    boolean isReadyToShowTime=true;
    public Fr(SH sh)
    {
        this.sh=sh;
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        width=screenSize.getWidth();
        height=screenSize.getHeight();
        speechTextLabel= new JLabel();
System.out.println("Fr is running");

        //      StyleContext context = new StyleContext();
        //     StyledDocument document = new DefaultStyledDocument(context);
        // 
        //     Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        // 
        //     StyleConstants.setBackground(style, Color.blue);
        // 
        //     try {
        //       document.insertString(document.getLength(), "java2s.com", style);
        //                 speechTextLabel= new JTextPane(document);
        //     } catch (Exception e) {
        //       // TODO Auto-generated catch block
        //           speechTextLabel= new JTextPane();
        //       e.printStackTrace();
        //     }

        //      SimpleAttributeSet attribs = new SimpleAttributeSet();
        //StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        //speechTextLabel.setParagraphAttributes(attribs, true);

        ImageIcon homeIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/home.png");
        homeIcon=new ImageIcon(getScaledImage(homeIcon.getImage(),(int)(height/10), (int)(height/10)));

        alarmIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/alarm.png");
        alarmIcon=new ImageIcon(getScaledImage(alarmIcon.getImage(),(int)(height/10), (int)(height/10)));

        upIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/up.png");
        upIcon=new ImageIcon(getScaledImage(upIcon.getImage(),(int)(height/10), (int)(height/10)));

        downIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/down.png");
        downIcon=new ImageIcon(getScaledImage(downIcon.getImage(),(int)(height/10), (int)(height/10)));

        cancelOkIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/ok.png");
        cancelOkIcon=new ImageIcon(getScaledImage(cancelOkIcon.getImage(),(int)(height/10), (int)(height/10)));
        cancelNot_okIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/not_ok.png");
        cancelNot_okIcon=new ImageIcon(getScaledImage(cancelNot_okIcon.getImage(),(int)(height/10), (int)(height/10)));

        findMobileIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/find_mobile.png");
        findMobileIcon=new ImageIcon(getScaledImage(findMobileIcon.getImage(),(int)(height/10), (int)(height/10)));
        findMobileAbcIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/find_mobile_abc.png");
        findMobileAbcIcon=new ImageIcon(getScaledImage(findMobileAbcIcon.getImage(),(int)(height/10), (int)(height/10)));

        ImageIcon manualIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/manual.png");
        manualIcon=new ImageIcon(getScaledImage(manualIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        speechIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/speech.png");
        speechIcon=new ImageIcon(getScaledImage(speechIcon.getImage(),(int)(height/10), (int)(height/10)));
        stop_speechIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/stop_speech.jpg");
        stop_speechIcon=new ImageIcon(getScaledImage(stop_speechIcon.getImage(),(int)(height/10), (int)(height/10)));
        speechButton= new JButton();
        alarmButton= new JButton();
        manual= new JButton(manualIcon);
        toggle=new JToggleButton("Commands Mode");
        ImageIcon timelIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/time.png");
        timelIcon=new ImageIcon(getScaledImage(timelIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        timer= new JButton(timelIcon);
        findMobileButton= new JButton();
        addAlarmPanel= new JPanel();
        addAlarmPanel.setLayout(new BorderLayout());
        addAlarmPanel.add(new AlarmJPanel());
        ImageIcon automationIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/automation.png");
        automationIcon=new ImageIcon(getScaledImage(automationIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        auto= new JButton(automationIcon);
        home= new JButton(homeIcon);
        home.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                goHome();
                isOnMainMenu=true;
                SheduleView.selectedOption=null;

            }
        });

        timer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new TimerView(Fr.this);
            }
        });

        auto.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                new SheduleView(Fr.this);
            }
        });
        alarmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                if(alarmButton.getIcon()==null){ // is already activated
                    isReadyToShowTime=false;
                    speechTextLabel.setText("Cancel alarm ?");
                    findMobileButton.setIcon(cancelOkIcon) ;
                    alarmButton.setIcon(cancelNot_okIcon);
                    usinAlarmText=alarmButton.getText();
                    alarmButton.setText("");
                }else if(alarmButton.getIcon()==cancelNot_okIcon){
                    findMobileButton.setIcon(findMobileIcon) ;
                    speechTextLabel.setText("");
                    isReadyToShowTime=true;

                    alarmButton.setText(usinAlarmText);
                    if(  sh.jarvis.hasAlarm) {
                        alarmButton.setIcon(null);
                        isReadyToShowTime = true;

                    }else{
                        alarmButton.setIcon(alarmIcon);
                        isReadyToShowTime=true;

                    }
                }else if(alarmButton.getIcon()==alarmIcon){
                    if(sh.jarvis.alarmProcess!=null&&sh.jarvis.alarmProcess.isAlive()){
                        sh.jarvis.alarmProcess.destroy();
                        isReadyToShowTime=true;

                    }else
                        showOrHideNewAlarm();
                }

            }
        });
        toggle.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(toggle!=null)
                    toggle.setEnabled(false);
                if(!toggle.isSelected()){

                    toggle.setText("Commands Mode");
                    manualSelected();
                }else{
                    toggle.setText("Output Mode");
                    manualSelected();

                }

                if(toggle!=null)
                    toggle.setEnabled(true);

            }
        });
        findMobileButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                if(  findMobileButton.getIcon()==findMobileIcon ){
                    new Thread(){public void run(){
                        try{
                            findMobileButton.setEnabled(false);
                           sh.jarvis.findMobile();
                            findMobileButton.setIcon(findMobileAbcIcon) ;
                            sleep(4000);
                            findMobileButton.setEnabled(true);
                            findMobileButton.setIcon(findMobileIcon) ;

                        }catch(Exception e){
                        }
                    }}.start();}else if(findMobileButton.getIcon()==cancelOkIcon){

                    alarmButton.setIcon(alarmIcon);

                    speechTextLabel.setText("Alarm canceled.");
                    isReadyToShowTime=true;
                    findMobileButton.setIcon(findMobileIcon) ;

                    sh.jarvis.hasAlarm=false;
                }

            }
        });
        this.setSize((int)width,(int)height);
        mainMenu();
        setLocation(0,0);

        manual.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                manualSelected();
            }
        });
        speechButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(speechButton.getIcon()==speechIcon){
                    sh.jarvis.activate();

                }else if(speechButton.getIcon()==stop_speechIcon){
                    sh.jarvis.deActivate();
                }
            }
        });

        showTime();
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void  showOrHideNewAlarm(){
        int child=extraInfoPanel.getComponentCount();
        boolean contains=false;
        for(int i=0;i<child;i++){
            Component cmp=extraInfoPanel.getComponent(i);
            if(cmp==addAlarmPanel){
                contains=true;
                break;

            }
        }
        if(contains){
            extraInfoPanel.remove(addAlarmPanel);
        }else{
            extraInfoPanel.add(addAlarmPanel,BorderLayout.PAGE_END);
        }

        extraInfoPanel.repaint();
        extraInfoPanel.revalidate();
    }

    boolean dotBoolean=true;

    void showTime(){
        dotBoolean=true;
        new Thread(){

            @Override
            public void run() {
                while(true){

                    try {
                        sleep(1000);

                        if (!isOnMainMenu||speechButton==null||speechButton.getIcon()==null
                                ||speechButton.getIcon()!=speechIcon||!isReadyToShowTime){
                            continue;

                        }
                        if(speechTextLabel==null){
                            continue;
                        }
                        speechTextLabel.setText(getStringTimeDate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    JPanel mainMenuPanel;
    void mainMenu(){
        shv=null;
        mainMenuPanel= new JPanel();
        mainMenuPanel.setLayout(new BorderLayout());
        isSwitchModeSelected=false;
        isTimerModeSelected=false;
        isSheduleModeSelected=false;
        isOnMainMenu=true;
        JPanel menuPanel= new JPanel();
        extraInfoPanel= new JPanel();

        extraInfoPanel.setLayout(new BorderLayout());
        if(!sh.jarvis.isSpeechActivate){
            speechButton.setIcon(speechIcon);
        }else{

            speechButton.setIcon(stop_speechIcon);
        }


        JPanel lineBeginExtraPanel= new JPanel();
        lineBeginExtraPanel.setLayout(new GridLayout(1,2));

        lineBeginExtraPanel.add(alarmButton);
        alarmButton.setIcon(alarmIcon);

        speechTextLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        speechTextLabel.setBackground(Color.GRAY);
        speechTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        speechTextLabel.setVerticalAlignment(SwingConstants.CENTER);
        speechTextLabel.setAutoscrolls(true);
        lineBeginExtraPanel.add(findMobileButton);
        speechTextLabel.setText( sh.jarvis.lastUsedResponse);
        extraInfoPanel.add(speechTextLabel);

        extraInfoPanel.add(speechButton,BorderLayout.LINE_END);
        findMobileButton.setIcon(findMobileIcon) ;
        extraInfoPanel.add(lineBeginExtraPanel,BorderLayout.LINE_START);
        setLayout(new BorderLayout());

        getContentPane().removeAll();
        menuPanel.setLayout(new GridLayout(1,3));
        menuPanel.add(manual);
        menuPanel.add(timer);
        menuPanel.add(auto);
        mainMenuPanel.add(menuPanel);
        mainMenuPanel.add(extraInfoPanel,BorderLayout.PAGE_END);

        add(mainMenuPanel);
        mainMenuPanel.repaint();
        mainMenuPanel.revalidate();
        repaint();
        revalidate();
    }

    void goHome(){
        shv=null;
        isSwitchModeSelected=false;
        isTimerModeSelected=false;
        isSheduleModeSelected=false;
        isOnMainMenu=true;
        getContentPane().removeAll();
        add(mainMenuPanel);
        mainMenuPanel.repaint();
        mainMenuPanel.revalidate();
        repaint();
        revalidate();
    }

    JPanel header=new JPanel();
    JPanel center=new JPanel();
    void manualSelected(){

        isTimerModeSelected=false;
        isSheduleModeSelected=false;
        isOnMainMenu=false;
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        header.removeAll();
        center.removeAll();

        isSwitchModeSelected=true;
        header.setLayout(new BorderLayout());
        header.add(home,BorderLayout.LINE_START);
        header.add(toggle,BorderLayout.LINE_END);
        add(header,BorderLayout.PAGE_START);

        ArrayList<String>[] usingList;
        String neededOutputs;
        if(toggle.getText().equals("Output Mode")){
            usingList=sh.outputCommands;
            neededOutputs=sh.getAllOutput();

        }else{
            usingList=sh.outputPowerCommands;
            neededOutputs=sh.getAllCommandOutput();

        }
        switcButtons=new JButton[usingList.length];
        String [] outputs=neededOutputs.split("@@@");

        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length),(int)Math.sqrt(usingList.length)));

        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            JButton button = new JButton(list.get(0));
            switcButtons[i]=(button);
            center.add(button);

            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try {
                    if(button.getBackground()==Color.GREEN){state="off";}else{
                        state="on";
                    }

                    String command =button.getText();


                        if(toggle.getText().equals("Output Mode")){
                            sh.processLedString(command + " " + state);
                            sh.sendToAll("switch "+command + " " + state);
                            System.out.println("pressed:"+command + " " + state);
                            sh.sendTheUpdates(command + " " + state);
                        }else{
                            sh.processCommandString(command + " " + state);
                            sh.sendToAll("switch "+command + " " + state);
                            System.out.println("pressed:"+command + " " + state);
                            sh.sendTheUpdates(command + " " + state);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });

            for(int j=0;j<outputs.length;j++){
                boolean isOn=false;
                String textNeededFromOutput=null;
                if(outputs[j].endsWith("on")){
                    textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"on".length());
                    isOn=true;

                }else if(outputs[j].endsWith("off")){
                    textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"off".length());
                    isOn=false;
                }


                if(textNeededFromOutput!=null&&textNeededFromOutput.equals(button.getText())){
                    if(isOn){
                        button.setBackground(Color.GREEN);
                    }else{
                        button.setBackground(Color.GRAY);
                    }
                }
            }
        }

        add(center);
        System.gc();
        center.repaint();
        center.revalidate();
        header.repaint();
        header.revalidate();
        getContentPane().repaint();
        getContentPane().revalidate();
        Fr.this.repaint();
        Fr.this.revalidate();

    }

    protected void updateManual(){
        ArrayList<String>[] usingList;
        String neededOutputs;
        if(toggle.getText().equals("Output Mode")){
            usingList=sh.outputCommands;
            neededOutputs=sh.getAllOutput();
        }else{
            usingList=sh.outputPowerCommands;
            neededOutputs=sh.getAllCommandOutput();
        }
        String [] outputs=neededOutputs.split("@@@");

        for(int j=0;j<outputs.length;j++){
            System.out.println(outputs[j]);
            boolean isOn=false;
            String textNeededFromOutput=null;
            if(outputs[j].replaceAll(" ","").endsWith("on")){
                textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"on".length());
                isOn=true;

            }else if(outputs[j].endsWith("off")){

                textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"off".length());
                isOn=false;
            }
            JButton button=null;
            if(switcButtons!=null){
                for(int i=0;i<switcButtons.length;i++){

                    if(switcButtons[i]!=null&&switcButtons[i].getText().equals(textNeededFromOutput)){
                        button=switcButtons[i];

                    }
                }

                if(button!=null)
                    if(textNeededFromOutput!=null&&textNeededFromOutput.equals(button.getText())){

                        if(isOn){

                            button.setBackground(Color.GREEN);
                        }else{
                            button.setBackground(Color.GRAY);
                        }
                    }
            }
        }
    }

    protected void changeState(String message){
        boolean isOn=false;
        String textNeededFromOutput=null;
        if(message.endsWith("on")){
            textNeededFromOutput=message.substring(0,message.length()-1-"on".length());
            isOn=true;

        }else if(message.endsWith("off")){
            textNeededFromOutput=message.substring(0,message.length()-1-"off".length());
            isOn=false;
        }

        if(switcButtons!=null){
            for(int i=0;i<switcButtons.length;i++){
                System.out.println(switcButtons[i].getText()+":changeState:"+textNeededFromOutput);
                if(switcButtons[i].getText().equals(textNeededFromOutput)){
                    if(!isOn){
                        switcButtons[i].setBackground(Color.GRAY);
                    }else{
                        switcButtons[i].setBackground(Color.GREEN);
                    }

                }
            }}

    }

    protected Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private class AlarmJPanel extends JPanel{
        int color_id=0;
        int sec=0,min=0,hours=0;
        private JPanel centerPanel= new JPanel();
        private JPanel hourPanel= new JPanel();
        private JPanel minPanel= new JPanel();
        private JPanel secPanel= new JPanel();
        // private JSlider minSlider,secSlider;
        private JLabel hourLabel,minLabel,secLabel;
        private JLabel hourValues,minValues,secValues;
        public AlarmJPanel(){
            hourPanel.setLayout(new GridLayout(4,1));
            minPanel.setLayout(new GridLayout(4,1));
            secPanel.setLayout(new GridLayout(4,1));
            JButton hourup= new JButton(upIcon),hourdown= new JButton(downIcon),
                    minup= new JButton(upIcon),mindown= new JButton(downIcon),
                    secup= new JButton(upIcon),secdown= new JButton(downIcon);

            hourup.addMouseListener(new MouseAdapter() {
                boolean isPressed=false;
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed=true;
                    new Thread(){public void run(){
                        while(isPressed){
                            hours=Integer.parseInt(hourValues.getText());
                            if(hours>=23){
                                hours=0;
                            }else{
                                hours++;}
                            String hoursString="";
                            if(hours>9){
                                hoursString=Integer.toString(hours);
                            }else{
                                hoursString="0"+Integer.toString(hours);
                            }
                            hourValues.setText(hoursString);

                            try{
                                Thread.sleep(300);
                            }catch(Exception ee){}
                        }
                    }}.start();
                }

                public void mouseReleased(MouseEvent e) {
                    isPressed=false;

                }

            });

            hourdown.addMouseListener(new MouseAdapter() {

                boolean isPressed=false;
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed=true;
                    new Thread(){public void run(){
                        while(isPressed){
                            hours=Integer.parseInt(hourValues.getText());
                            if(hours<=0){
                                hours=23;
                            }else{
                                hours--;}
                            String hoursString="";
                            if(hours>9){
                                hoursString=Integer.toString(hours);
                            }else{
                                hoursString="0"+Integer.toString(hours);
                            }
                            hourValues.setText(hoursString);

                            try{
                                Thread.sleep(300);
                            }catch(Exception ee){}
                        }
                    }}.start();
                }

                public void mouseReleased(MouseEvent e) {
                    isPressed=false;

                }});


            // hourSlider= new JSlider(0,23);
            //hourSlider.setValue(0);
            //hourSlider.addChangeListener(new ChangeListener(){
            //  public void stateChanged(ChangeEvent changeEvent) {
            //                         Object source = changeEvent.getSource();
            //                         JSlider theJSlider = (JSlider) source;
            //                         hours=theJSlider.getValue();
            // 
            //                         String hoursString="";
            //                         if(hours>9){
            //                             hoursString=Integer.toString(hours);
            //                         }else{
            //                             hoursString="0"+Integer.toString(hours);
            //                         }
            //                         hourValues.setText(hoursString);

            //    }
            //  });

            //  minSlider= new JSlider(0,59);
            //   minSlider.setValue(0);
            //  minSlider.addChangeListener(new ChangeListener(){

            minup.addMouseListener(new MouseAdapter() {
                boolean isPressed=false;
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed=true;
                    new Thread(){public void run(){
                        while(isPressed){
                            min=Integer.parseInt(minValues.getText());
                            if(min>=59){
                                min=0;
                            }else{
                                min++;}
                            String minString="";
                            if(min>9){
                                minString=Integer.toString(min);
                            }else{
                                minString="0"+Integer.toString(min);
                            }
                            minValues.setText(minString);

                            try{
                                Thread.sleep(200);
                            }catch(Exception ee){}
                        }
                    }}.start();
                }

                public void mouseReleased(MouseEvent e) {
                    isPressed=false;

                }

            });

            mindown.addMouseListener(new MouseAdapter() {

                boolean isPressed=false;
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed=true;
                    new Thread(){public void run(){
                        while(isPressed){
                            min=Integer.parseInt(minValues.getText());
                            if(min<=0){
                                min=59;
                            }else{
                                min--;}
                            String minString="";
                            if(min>9){
                                minString=Integer.toString(min);
                            }else{
                                minString="0"+Integer.toString(min);
                            }
                            minValues.setText(minString);

                            try{
                                Thread.sleep(200);
                            }catch(Exception ee){}
                        }
                    }}.start();
                }

                public void mouseReleased(MouseEvent e) {
                    isPressed=false;

                }});

            secup.addMouseListener(new MouseAdapter() {
                boolean isPressed=false;
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed=true;
                    new Thread(){public void run(){
                        while(isPressed){
                            sec=Integer.parseInt(secValues.getText());
                            if(sec>=59){
                                sec=0;
                            }else{
                                sec++;}
                            String secString="";
                            if(sec>9){
                                secString=Integer.toString(sec);
                            }else{
                                secString="0"+Integer.toString(sec);
                            }
                            secValues.setText(secString);

                            try{
                                Thread.sleep(200);
                            }catch(Exception ee){}
                        }
                    }}.start();
                }

                public void mouseReleased(MouseEvent e) {
                    isPressed=false;

                }

            });

            secdown.addMouseListener(new MouseAdapter() {

                boolean isPressed=false;
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed=true;
                    new Thread(){public void run(){
                        while(isPressed){
                            sec=Integer.parseInt(secValues.getText());
                            if(sec<=0){
                                sec=59;
                            }else{
                                sec--;}
                            String secString="";
                            if(sec>9){
                                secString=Integer.toString(sec);
                            }else{
                                secString="0"+Integer.toString(sec);
                            }
                            secValues.setText(secString);

                            try{
                                Thread.sleep(300);
                            }catch(Exception ee){}
                        }
                    }}.start();
                }

                public void mouseReleased(MouseEvent e) {
                    isPressed=false;

                }});

            hourLabel= new JLabel("Hours:");
            minLabel= new JLabel("Minutes:");
            secLabel= new JLabel("Seconds:");

            hourValues= new JLabel("00");
            minValues= new JLabel("00");
            secValues= new JLabel("00");
            hourLabel.setHorizontalAlignment(SwingConstants.CENTER);
            hourValues.setHorizontalAlignment(SwingConstants.CENTER);

            hourPanel.add(hourLabel);
            hourPanel.add(hourup);
            Font f = hourLabel.getFont();
            // bold
            hourLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            hourLabel.setForeground(Color.BLUE)  ;

            minLabel.setHorizontalAlignment(SwingConstants.CENTER);
            minValues.setHorizontalAlignment(SwingConstants.CENTER);

            // bold
            minLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

            secLabel.setHorizontalAlignment(SwingConstants.CENTER);
            secValues.setHorizontalAlignment(SwingConstants.CENTER);

            // bold
            secLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            secPanel.add(secLabel);
            secPanel.add(secup);
            minLabel.setForeground(Color.BLUE)  ;
            secLabel.setForeground(Color.BLUE)  ;
            minPanel.add(minLabel);
            minPanel.add(minup);

            hourPanel.add(hourValues);
            hourPanel.add(hourdown);
            // minPanel.add(minLabel);
            //   minPanel.add(minSlider);
            minPanel.add(minValues);
            minPanel.add(mindown);
            // secPanel.add(secLabel);

            //  secPanel.add(secSlider);
            secPanel.add(secValues);
            secPanel.add(secdown);
            // centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            centerPanel.setLayout(new GridLayout(1,3));
            centerPanel.add(hourPanel);
            centerPanel.add(minPanel);
            centerPanel.add(secPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));

            JButton save= new JButton(),cancel=new JButton();
            save.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try{
                        int hour=Integer.parseInt(hourValues.getText())
                                ,min=Integer.parseInt(minValues.getText())
                                ,sec=Integer.parseInt(secValues.getText());
                        sh.jarvis.alarmAtTime(hour, min,sec);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    extraInfoPanel.remove(addAlarmPanel);
                    extraInfoPanel.repaint();
                    extraInfoPanel.revalidate();
                }
            });
            cancel.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    extraInfoPanel.remove(addAlarmPanel);
                    extraInfoPanel.repaint();
                    extraInfoPanel.revalidate();
                }
            });
            save.setIcon(cancelOkIcon);
            cancel.setIcon(cancelNot_okIcon);

            add(centerPanel,BorderLayout.CENTER);
            add(save,BorderLayout.LINE_END);
            add(cancel,BorderLayout.LINE_START);

        }

        private int getRemainingTimeToSeconds(){
            return hours*60*60+min*60+sec;
        }
    }

    private String getStringTimeDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return (dateFormat.format(date));

    }

    class MyTextPane extends JTextPane {
        public MyTextPane() {
            super();

            setOpaque(false);

            // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
            setBackground(new Color(0,0,0,0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            // set background green - but can draw image here too
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            // uncomment the following to draw an image
            // Image img = ...;
            // g.drawImage(img, 0, 0, this);

            super.paintComponent(g);
        }
    }
}