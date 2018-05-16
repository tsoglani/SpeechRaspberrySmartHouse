
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NewTimerView extends JPanel{
    private Fr fr;
    private JButton back,save;
    private JScrollPane scrollPane;
    private  Color []colors = {Color.gray,Color.green,Color.red};
    public NewTimerView(Fr fr) 
    {
        setLayout(new BorderLayout());
        this.fr=fr;
        ImageIcon backTime=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/back.png");
        backTime=new ImageIcon(fr.getScaledImage(backTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        back = new JButton(backTime);
        ImageIcon saveIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/save.png");
        saveIcon=new ImageIcon(fr.getScaledImage(saveIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        back = new JButton(backTime);
        save = new JButton(saveIcon);
        save.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    saveFunction();
                    new TimerView(fr); 
                }
            });
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new TimerView(fr);
                }
            });
        createGUI();

    }
    ArrayList<String>[] usingList;
    ArrayList   <MyJPanel> myPanels;
    private void createGUI(){
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);
        usingList=null;
        JPanel header=new JPanel();



        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        header.add(save);
        header.add(back,BorderLayout.LINE_START);

        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);

        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));

        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            myPanels.add(button);
            center.add(button);

        }
        scrollPane = new JScrollPane(center);
        add(scrollPane);
        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();
    }

    private class MyJPanel extends JPanel{
        int color_id=0;
        String title ;        int sleepingTime=300;
        int sec=0,min=0,hours=0;
        private JPanel centerPanel= new JPanel();
        private JPanel hourPanel= new JPanel();
        private JPanel minPanel= new JPanel();
        private JPanel secPanel= new JPanel();
        //  private JSlider hourSlider,minSlider,secSlider;
        JButton hourup,hourdown,
        minup,mindown,
        secup,secdown;

        ImageIcon upIcon,downIcon;
        private JLabel hourLabel,minLabel,secLabel;

        private JLabel hourValues,minValues,secValues;
        public MyJPanel(String title){
            this.title=title;
            double numberOfViews=Math.sqrt(usingList.length);

            upIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/up.png");
            upIcon=new ImageIcon(fr.getScaledImage(upIcon.getImage(),(int)(fr.height/(11*numberOfViews)), (int)(fr.height/(11*numberOfViews))));

            downIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/down.png");
            downIcon=new ImageIcon(fr.getScaledImage(downIcon.getImage(),(int)(fr.height/(11*numberOfViews)), (int)(fr.height/(11*numberOfViews))));

            hourup= new JButton(upIcon);hourdown= new JButton(downIcon);
            minup= new JButton(upIcon);mindown= new JButton(downIcon);
            secup= new JButton(upIcon);secdown= new JButton(downIcon);  
            hourup.addMouseListener(new MouseAdapter() {
                    boolean isPressed=false;
                    @Override
                    public void mousePressed(MouseEvent e) {
                        isPressed=true;
                        sleepingTime=300;
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
                                        Thread.sleep(sleepingTime);
                                        if(sleepingTime>100){
                                            sleepingTime=sleepingTime-20;
                                        }
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
                        sleepingTime=300;
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
                                        Thread.sleep(sleepingTime);
                                        if(sleepingTime>100){
                                            sleepingTime=sleepingTime-20;
                                        }
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
                        sleepingTime=300;
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
                                        Thread.sleep(sleepingTime);
                                        if(sleepingTime>100){
                                            sleepingTime=sleepingTime-20;
                                        }
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
                        sleepingTime=300;
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
                                        Thread.sleep(sleepingTime);
                                        if(sleepingTime>100){
                                            sleepingTime=sleepingTime-20;
                                        }
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
                        sleepingTime=300;
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
                                        Thread.sleep(sleepingTime);
                                        if(sleepingTime>100){
                                            sleepingTime=sleepingTime-20;
                                        }
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
                        isPressed=true;sleepingTime=300;
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
                                        Thread.sleep(sleepingTime);
                                        if(sleepingTime>100){
                                            sleepingTime=sleepingTime-20;
                                        }
                                    }catch(Exception ee){} 
                                } 
                            }}.start();
                    }

                    public void mouseReleased(MouseEvent e) {
                        isPressed=false; 

                    }});

            //             hourSlider= new JSlider(0,23);
            //             hourSlider.setValue(0);
            //             hourSlider.addChangeListener(new ChangeListener(){
            //                     public void stateChanged(ChangeEvent changeEvent) {
            //                         Object source = changeEvent.getSource();
            //                         JSlider theJSlider = (JSlider) source;
            //                         hours=theJSlider.getValue();
            //                         hourValues.setText(Integer.toString(hours));
            // 
            //                     }
            //                 });
            // 
            //             minSlider= new JSlider(0,59);
            //             minSlider.setValue(0);
            //             minSlider.addChangeListener(new ChangeListener(){
            //                     public void stateChanged(ChangeEvent changeEvent) {
            //                         Object source = changeEvent.getSource();
            //                         JSlider theJSlider = (JSlider) source;
            //                         min=theJSlider.getValue();
            //                         minValues.setText(Integer.toString(min));
            // 
            //                     }
            //                 });
            //             secSlider= new JSlider(0,59);
            //             secSlider.setValue(0);
            //             secSlider.addChangeListener(new ChangeListener(){
            //                     public void stateChanged(ChangeEvent changeEvent) {
            //                         Object source = changeEvent.getSource();
            //                         JSlider theJSlider = (JSlider) source;
            //                         sec=theJSlider.getValue();
            //                         secValues.setText(Integer.toString(sec));
            // 
            //                     }
            //                 });
            hourLabel= new JLabel("Hour:");
            minLabel= new JLabel("Mins:");
            secLabel= new JLabel("Secs:");

            hourValues= new JLabel("00");
            minValues= new JLabel("00");
            secValues= new JLabel("00");
            hourPanel.add(hourdown); 
            hourPanel.add(hourLabel); 
            hourPanel.add(hourValues);
            hourPanel.add(hourup); 
            minPanel.add(mindown);
            minPanel.add(minLabel);

            minPanel.add(minValues);
            minPanel.add(minup);
            secPanel.add(secdown);
            secPanel.add(secLabel);

            secPanel.add(secValues);
            secPanel.add(secup);
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            JLabel titleLabel= new JLabel(title);
            Font f = titleLabel.getFont();
            titleLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

            titleLabel.setForeground(Color.CYAN)  ;  
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);        

            centerPanel.add(hourPanel);
            centerPanel.add(minPanel);
            centerPanel.add(secPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));

            // JPanel titlePanel= new JPanel();
            //titlePanel.add(titleLabel);
            setBackground(colors[color_id]);

            hourPanel.setBackground(colors[color_id]);
            minPanel.setBackground(colors[color_id]);
            secPanel.setBackground(colors[color_id]);
            titleLabel.setBackground(colors[color_id]);

            hourLabel.setBackground(Color.white);
            minLabel.setBackground(Color.white);
            secLabel.setBackground(Color.white);
            hourValues.setBackground(Color.white);
            minValues.setBackground(Color.white);
            secValues.setBackground(Color.white);

            add(titleLabel,BorderLayout.PAGE_START);
            add(centerPanel);
            addMouseListener(new MouseAdapter() { 
                    public void mousePressed(MouseEvent me) { 
                        color_id++;
                        if(color_id>2){
                            color_id=0;
                        }
                        setBackground(colors[color_id]);
                        //centerPanel.setBackground(colors[color_id]);
                        hourPanel.setBackground(colors[color_id]);
                        minPanel.setBackground(colors[color_id]);
                        secPanel.setBackground(colors[color_id]);
                        titleLabel.setBackground(colors[color_id]);
                    } 
                }); 

            centerPanel. addMouseListener(new MouseAdapter() { 
                    public void mousePressed(MouseEvent me) { 
                        color_id++;
                        if(color_id>2){
                            color_id=0;
                        }
                        setBackground(colors[color_id]);
                        //centerPanel.setBackground(colors[color_id]);
                        hourPanel.setBackground(colors[color_id]);
                        minPanel.setBackground(colors[color_id]);
                        secPanel.setBackground(colors[color_id]);
                        titleLabel.setBackground(colors[color_id]);
                    } 
                }); 
        }

        private int getRemainingTimeToSeconds(){
            return hours*60*60+min*60+sec;
        }
    }

    private void saveFunction(){
        for (int i=0;i<myPanels.size();i++){
            MyJPanel mp=myPanels.get(i);
            if(mp.getBackground()!=colors[0]){
                String mode=" off";
                if(mp.getBackground()==colors[1]){
                    mode=" on";
                }
                TimerCountdown timerCountdown=  new TimerCountdown(fr.sh,mp.title+mode,Integer.toString(mp.getRemainingTimeToSeconds()),Long.toString(getTimeStamps()));
                timerCountdown.start();

                new Thread(){
                    public void run(){
                        try{
                            sleep(1000);
                            fr.sh.sendToAll("Timers:DeviceID:"+ fr.sh.DeviceID+ DB.COMMAND_SPLIT_STRING+TimerCountdown.getAllTimers());
                        }catch (Exception e){}
                    }

                }.start();

                System.out.println("Timers:DeviceID:"+ fr.sh.DeviceID+ DB.COMMAND_SPLIT_STRING+TimerCountdown.getAllTimers() );
            }
        }
    }

    private long getTimeStamps(){
        return  System.currentTimeMillis();
    }

}