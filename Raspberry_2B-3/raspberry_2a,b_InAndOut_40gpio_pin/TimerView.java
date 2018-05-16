
/**
 * Write a description of class TimerView here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
public class TimerView extends JPanel
{
    private Fr fr;
    private Thread thread;
    private JPanel bottomPanel= new JPanel();
    private JButton addTimeButton,deleteButton,cancelButton;
    private JLabel selectedButtonText;
    private  TimerCountdown selectedTimerForDelete;
    public TimerView(Fr fr)
    {
        this.fr=fr;
         fr.isSwitchModeSelected=false;
                    fr.isSheduleModeSelected=false;
                    fr.isOnMainMenu=false;
        fr.isTimerModeSelected=true;
        ImageIcon addTime=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/add_timer.png");
        addTime=new ImageIcon(fr.getScaledImage(addTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        addTimeButton = new JButton(addTime);

        ImageIcon deleteIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/trash.png");
        deleteIcon=new ImageIcon(fr.getScaledImage(deleteIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        deleteButton = new JButton(deleteIcon);
        bottomPanel.setLayout(new GridLayout(1,3));

        ImageIcon cancelIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/cancel3.png");
        cancelIcon=new ImageIcon(fr.getScaledImage(cancelIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        cancelButton = new JButton(cancelIcon);

        addTimeButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new NewTimerView(fr);
                    selectedButtonText=null;
                    selectedTimerForDelete=null;  
                    fr.isTimerModeSelected=false;

                }
            });

        cancelButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    chooseAddTime();
                }
            });

        deleteButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    ArrayList<TimerCountdown> timers =TimerCountdown.timers;
                    if(selectedTimerForDelete!=null){
                        if(timers.contains(selectedTimerForDelete)){
                            timers.remove(selectedTimerForDelete);
                            fr.sh.sendToAll("Timers:DeviceID:"+ fr.sh.DeviceID+ DB.COMMAND_SPLIT_STRING+TimerCountdown.getAllTimers()
                            );   

                        }
                    }

                    chooseAddTime();
                }
            });
        showList();
        chooseAddTime();

        thread=  new Thread(){public void run(){
                while(fr.isTimerModeSelected){
                    try{ 
                        sleep(1000);

                        updateTimers();
                    }catch(Exception e){
                        e.printStackTrace();
                    }}


            }};
        thread.start();
    }

    ArrayList <MyJPanel> switcButtons;

    private void showList(){
        fr.getContentPane().removeAll();
        setLayout(new BorderLayout());
        JPanel header=new JPanel();
        JPanel center=new JPanel();
        switcButtons=new <MyJPanel> ArrayList();

        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        add(header,BorderLayout.PAGE_START);
        ArrayList<String>[] usingList;
        String neededOutputs;

        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();

        String [] outputs=neededOutputs.split("@@@");
            add(center);
        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
    
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            switcButtons.add(button);
            center.add(button);

        }
        updateTimers();

        fr.add(this);
        add(bottomPanel,BorderLayout.PAGE_END);
        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();
    }

    private void chooseAddTime(){
        bottomPanel.removeAll();
        bottomPanel.add(new JLabel());
        bottomPanel.add(addTimeButton);
        bottomPanel.add(new JLabel());
        selectedButtonText=null;
        selectedTimerForDelete=null;   
        bottomPanel.repaint();
        bottomPanel.revalidate();
    }

    private void chooseForDelete(String text){
        bottomPanel.removeAll();
        selectedButtonText=new JLabel(text);
        selectedButtonText.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(cancelButton);
        bottomPanel.add(selectedButtonText);
        bottomPanel.add(deleteButton);

        bottomPanel.repaint();
        bottomPanel.revalidate();

    }

    static ArrayList<TimerCountdown> timerVisible= TimerCountdown.timers;

    private void updateTimers(){
        ArrayList<TimerCountdown> timers= TimerCountdown.timers;
        for(int i=0;i<timers.size();i++){

            //   if(timerVisible.contains(timers.get(i))){
            //   continue;
            //    }
            TimerCountdown timer=timers.get(i);
            String timerFullCommand=timer.commandText;
            int timerSecondsRemain=timer.seconds;
            boolean isGoingToOpen=false;
            String timerCommand=null;

            if(timerFullCommand.endsWith("on")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"on".length());
                isGoingToOpen=true;

            }else if(timerFullCommand.endsWith("off")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"off".length());
                isGoingToOpen=false;
            }

            for(int j=0;j<switcButtons.size();j++){
                MyJPanel myPanel=switcButtons.get(j);
                if(myPanel.title.equals(timerCommand)){
                    myPanel.timers.add(timer);
                    //        String textToButton ;
                    //      if(isGoingToOpen){
                    //      textToButton="open";}else{
                    //            textToButton="close";
                    //     }
                    //  JButton button=new JButton(textToButton+" in "+timerSecondsRemain+" seconds");

                    //   myPanel.addCenter(button);
                }
            }

        }

        for(int j=0;j<switcButtons.size();j++){
            MyJPanel myPanel=switcButtons.get(j);
            myPanel.update();
        }

        timerVisible= TimerCountdown.timers;
    }

    private class MyJPanel extends JPanel{
        String title ;
        ArrayList <TimerCountdown> timers=new ArrayList  <TimerCountdown>();
        private JPanel centerPanel= new JPanel();
        JScrollPane scrollSpecific;
        private  ArrayList<CostumeButton> buttons = new  ArrayList<CostumeButton>();
        ArrayList <CostumeButton> removingButtons= new ArrayList<CostumeButton>();
        ArrayList <TimerCountdown> removingTimers= new ArrayList<TimerCountdown>();
        public MyJPanel(String title){
            this.title=title;
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            scrollSpecific = new JScrollPane(centerPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            add(titlePanel,BorderLayout.PAGE_START);
            add(scrollSpecific,BorderLayout.CENTER);

        }

        public void update(){
            removingButtons.removeAll(removingButtons);
            removingTimers.removeAll(removingTimers);
            // System.out.println();

            for(int i=0;i<timers.size();i++){
                TimerCountdown timer=timers.get(i);
                boolean contains=false;

                if(!TimerCountdown.timers.contains(timer)){
                    // remove button
                    for(int j=0;j<buttons.size();j++){
                        CostumeButton cb=buttons.get(j);
                        if(cb.id==timer.id){

                            removingButtons.add(cb);
                            removingTimers.add(timer);

                            if(selectedTimerForDelete!=null&&selectedTimerForDelete.id==timer.id&&selectedButtonText!=null){
                                chooseAddTime();

                            }                                

                        }
                    }

                    continue;
                } 
                for(int j=0;j<buttons.size();j++){
                    if(buttons.get(j).id==timer.id){
                        contains=true;
                    }
                }

                if(contains){
                    // update text

                    for(int j=0;j<buttons.size();j++){
                        CostumeButton cb=buttons.get(j);
                        if(cb.id==timer.id){

                            cb.setText(getTextToButton(timer));

                            if(selectedTimerForDelete!=null&&selectedTimerForDelete.id==timer.id&&selectedButtonText!=null){
                                selectedButtonText.setText("Delete: "+title+" "+getTextToButton(timer));
                            }
                        }
                    }

                }else{
                    // create button and add it 
                    CostumeButton button = new CostumeButton(timer);
                    buttons.add(button);
                    timers.add(timer);
                    button.setText(getTextToButton(timer));
                    addCenter(button);

                }

            }

            if(!removingButtons.isEmpty()){

                for(int i=0;i<removingButtons.size();i++){
                    CostumeButton cb=removingButtons.get(i);
                    centerPanel.remove(cb);
                    buttons.remove(cb);
                    centerPanel.repaint();
                    centerPanel.revalidate();
                    System.out.println("remove "+cb.getText());
                }

            }

            if(!removingTimers.isEmpty()){

                for(int i=0;i<removingTimers.size();i++){
                    TimerCountdown tm=removingTimers.get(i);
                    timers.remove(tm);

                }

            }
        }

        private String getTextToButton(TimerCountdown timer){
            String textToButton ;
            String timerFullCommand=timer.commandText;
            int timerSecondsRemain=timer.seconds;
            boolean isGoingToOpen=false;
            String timerCommand=null;

            if(timerFullCommand.endsWith("on")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"on".length());
                isGoingToOpen=true;

            }else if(timerFullCommand.endsWith("off")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"off".length());
                isGoingToOpen=false;
            }

            if(isGoingToOpen){
                textToButton="open";}else{
                textToButton="close";
            }
            return textToButton+" in "+timerSecondsRemain+" seconds";
        }

        private void addCenter(Component c){

            centerPanel.add(c);
            centerPanel.repaint();
            centerPanel.revalidate();

        }

        private class CostumeButton extends JButton{
            int id;
            TimerCountdown timer;
            public CostumeButton(TimerCountdown timer){

                this.timer=timer;
                this.id=timer.id;
                setAlignmentX(Component.CENTER_ALIGNMENT);
                addActionListener(new ActionListener(){

                        @Override
                        public void actionPerformed(ActionEvent e){

                            chooseForDelete("Delete: "+title+" "+getTextToButton(timer));
                            selectedTimerForDelete=timer;
                        }
                    });
            }

        }
    }

}