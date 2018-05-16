import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class EditSheduleView  extends JPanel
{
    private  Color []colors = {Color.white,Color.green,Color.red};
    private String activeDays, time, weeklyString, activeString,commandString;
    protected  int id;
    protected JPanel firstRow,secondRow;
    private JButton cancel,save;
    private JCheckBox isWeekly,isActive; 
    private JLabel [] days= new JLabel[7];
//    private Shedule shedule;
    private JPanel centerPanel,header;
    protected JLabel timeLabel;
    private JSlider hourSlider,minSlider,secSlider;
    private JPanel hourPanel= new JPanel();
    private JPanel minPanel= new JPanel();
    private JPanel secPanel= new JPanel();

    private  Fr fr;
    private JLabel hourLabel,minLabel,secLabel;
    private  int min=0,hours=0, sec;
    private  JButton back;
    JComboBox commandCombo ;
    private ImageIcon cancelIcon;
    public EditSheduleView(Fr fr,Shedule shedule)
    {
        setLayout(new BorderLayout());
        activeDays= shedule.getActiveDays();
        weeklyString=  shedule.getIsWeekly();
        activeDays=  shedule.getActiveDays();
        activeString=  shedule.getIsActive();
        commandString=  shedule.getCommandText();

        ArrayList<String>[] usingList;

        usingList=fr.sh.outputPowerCommands;
        String [] comboCommandsString=new String[usingList.length];
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            comboCommandsString[i]=list.get(0);
        }

        commandCombo= new JComboBox(comboCommandsString);
        commandCombo.setSelectedItem(commandString);
        final String fontName = commandCombo.getSelectedItem().toString();
        commandCombo.setFont(new Font(fontName, Font.BOLD, 20));
        commandCombo.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        final String fontName = commandCombo.getSelectedItem().toString();
                        commandCombo.setFont(new Font(fontName, Font.BOLD, 20));
                    }
                }
            });
        ((JLabel)commandCombo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel= new JPanel();
        header= new JPanel();
        header.setLayout(new BorderLayout());
        ImageIcon backTime=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/back.png");
        backTime=new ImageIcon(fr.getScaledImage(backTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        back = new JButton(backTime);
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new SheduleView(fr);
                }
            });
        header.add(fr.home,BorderLayout.LINE_END);
        header.add(back,BorderLayout.LINE_START);
        header.add(commandCombo);
        id=shedule.getId();
        time=shedule.getTime();

        setBorder(BorderFactory.createLineBorder(Color.blue));

        hourSlider= new JSlider(0,23);

        hours=Integer.parseInt(time.split(":")[0]);
        hourSlider.setValue(hours);

        hourSlider.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent changeEvent) {
                    Object source = changeEvent.getSource();
                    JSlider theJSlider = (JSlider) source;
                    hours=theJSlider.getValue();
                    String hoursString=null;
                    String minString=null;
                    String secString=null;
                    if(hours<10){
                        hoursString=  "0"+Integer.toString(hours);
                    }else{
                        hoursString=  Integer.toString(hours);
                    }

                    if(min<10){
                        minString=  "0"+Integer.toString(min);
                    }else{
                        minString=  Integer.toString(min);
                    }
                    if(sec<10){
                        secString=  "0"+Integer.toString(sec);
                    }else{
                        secString=  Integer.toString(sec);
                    }
                    timeLabel.setText(hoursString+":"+minString+":"+secString);
                }
            });

        minSlider= new JSlider(0,59);
        min=Integer.parseInt(time.split(":")[1]);
        minSlider.setValue(min);
        minSlider.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent changeEvent) {
                    Object source = changeEvent.getSource();
                    JSlider theJSlider = (JSlider) source;
                    min=theJSlider.getValue();
                    String hoursString=null;
                    String minString=null;
                    String secString=null;
                    if(hours<10){
                        hoursString=  "0"+Integer.toString(hours);
                    }else{
                        hoursString=  Integer.toString(hours);
                    }
                    if(min<10){
                        minString=  "0"+Integer.toString(min);
                    }else{
                        minString=  Integer.toString(min);
                    }
                    if(sec<10){
                        secString=  "0"+Integer.toString(sec);
                    }else{
                        secString=  Integer.toString(sec);
                    }
                    timeLabel.setText(hoursString+":"+minString+":"+secString);

                }
            });


        secSlider= new JSlider(0,59);
        sec=Integer.parseInt(time.split(":")[2]);
        secSlider.setValue(sec);
        secSlider.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent changeEvent) {
                Object source = changeEvent.getSource();
                JSlider theJSlider = (JSlider) source;
                sec=theJSlider.getValue();
                String hoursString=null;
                String minString=null;
                String secString=null;
                if(hours<10){
                    hoursString=  "0"+Integer.toString(hours);
                }else{
                    hoursString=  Integer.toString(hours);
                }
                if(min<10){
                    minString=  "0"+Integer.toString(min);
                }else{
                    minString=  Integer.toString(min);
                }
                if(sec<10){
                    secString=  "0"+Integer.toString(sec);
                }else{
                    secString=  Integer.toString(sec);
                }
                timeLabel.setText(hoursString+":"+minString+":"+secString);

            }
        });

        hourLabel= new JLabel("Hours:            ");
         minLabel= new JLabel( "Minutes:         ");
         secLabel= new JLabel( "Seconds:         ");
        secLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
        hourLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
minLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
        firstRow= new JPanel();
        firstRow.setLayout (new GridLayout(1,7));
        secondRow= new JPanel();

        cancelIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/cancel3.png");
        cancelIcon=new ImageIcon(fr.getScaledImage(cancelIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));

        cancel= new JButton(cancelIcon);

        ImageIcon saveIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/save.png");
        saveIcon=new ImageIcon(fr.getScaledImage(saveIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        save = new JButton(saveIcon);

        isWeekly= new JCheckBox("is Weekly");
        isActive= new JCheckBox("is Active");

        centerPanel.setLayout(new GridLayout(2,1));
        centerPanel.add(firstRow);
        centerPanel.add(secondRow);
        timeLabel=new JLabel(time);

        add(centerPanel);

        for(int i=0;i<7;i++){
            JLabel dayButton=null;
            switch(i){
                case 0:
                dayButton= new JLabel("Su",SwingConstants.CENTER);
                break;
                case 1:
                dayButton= new JLabel("Mo",SwingConstants.CENTER);
                break;
                case 2:
                dayButton= new JLabel("Tu",SwingConstants.CENTER);
                break;
                case 3:
                dayButton= new JLabel("We",SwingConstants.CENTER);
                break;
                case 4:
                dayButton= new JLabel("Th",SwingConstants.CENTER);
                break;
                case 5:
                dayButton= new JLabel("Fr",SwingConstants.CENTER);
                break;
                case 6:
                dayButton= new JLabel("Sa",SwingConstants.CENTER);
                break;    
                default: 
                dayButton= new JLabel("Uknown",SwingConstants.CENTER);

            }
            dayButton.setOpaque(true);

            dayButton.setBorder(BorderFactory.createLineBorder(Color.gray));
            days[i]=dayButton;
            dayButton.addMouseListener(new MouseAdapter() { 
                    public void mousePressed(MouseEvent me) {
                        JLabel dayLab=(JLabel)me.getSource();
                        if (dayLab.getBackground() == colors[0]) {
                            dayLab.setBackground(colors[1]);
                        } else   if (dayLab.getBackground() ==colors[1]){
                            dayLab.setBackground(colors[2]);
                        } else   if (dayLab.getBackground() == colors[2]){
                            dayLab.setBackground(colors[0]);
                        }

                    }
                });
            firstRow.setBorder(BorderFactory.createLineBorder(Color.gray));
            firstRow.add(dayButton);

            dayButton.setBackground(colors[0]);

        }
        JPanel timerPanel= new JPanel();
        JPanel centerInsideSecondPanel= new JPanel();
        centerInsideSecondPanel.setLayout(new BorderLayout());
        //  secondRow.add(cancel,SwingConstants.CENTER);
        //    secondRow.add(new JLabel("    "),SwingConstants.CENTER);
        //   secondRow.add(new JLabel("    "),SwingConstants.CENTER);

        centerInsideSecondPanel.add(isActive,BorderLayout.LINE_END);
        centerInsideSecondPanel.add(isWeekly,BorderLayout.LINE_START);


        centerInsideSecondPanel.add(timerPanel);
        timerPanel.setLayout(new GridLayout(2,1));
        hourPanel.setLayout(new BorderLayout());
        hourPanel.add(hourLabel,BorderLayout.LINE_START);
        hourPanel.add(hourSlider);
        minPanel.setLayout(new BorderLayout());
        minPanel.add(minLabel,BorderLayout.LINE_START);
        minPanel.add(minSlider);

        secPanel.setLayout(new BorderLayout());
        secPanel.add(secLabel,BorderLayout.LINE_START);
        secPanel.add(secSlider);


        JPanel changeHourPanel=new JPanel();
        timerPanel.add(timeLabel);
        changeHourPanel.setLayout(new BoxLayout(changeHourPanel,BoxLayout.Y_AXIS));
        changeHourPanel.add(hourPanel);
        changeHourPanel.add(minPanel);
        changeHourPanel.add(secPanel);

        timerPanel.add(changeHourPanel);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font f = timeLabel.getFont();
        f=f.deriveFont ((float)(fr.width/15));
        timeLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        timeLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        timerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        timeLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        changeHourPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        secondRow.setLayout(new BorderLayout());
        secondRow.add(centerInsideSecondPanel,SwingConstants.CENTER);
        // secondRow.add(new JLabel("    "),SwingConstants.CENTER);
        //             secondRow.add(new JLabel("    "),SwingConstants.CENTER);
        // secondRow.add(edit,SwingConstants.CENTER);

        JPanel bottomPanel= new JPanel();
        bottomPanel.setLayout(new GridLayout(1,2));
        bottomPanel.add(cancel);
        bottomPanel.add(save);
        add(bottomPanel,BorderLayout.PAGE_END);

        save.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    String activeDays = "";
                    if (isDaySelected(days[0])) {
                        String extgraString=(isDayOn(days[0]))?" on":" off";
                        activeDays += Calendar.SUNDAY+extgraString;
                    }
                    if (isDaySelected(days[1])) {
                        String extgraString=(isDayOn(days[1]))?" on":" off";
                        activeDays += Calendar.MONDAY+extgraString;
                    }
                    if (isDaySelected(days[2])) {
                        String extgraString=(isDayOn(days[2]))?" on":" off";
                        activeDays += Calendar.TUESDAY+extgraString;
                    }
                    if (isDaySelected(days[3])) {
                        String extgraString=(isDayOn(days[3]))?" on":" off";
                        activeDays += Calendar.WEDNESDAY+extgraString;
                    }
                    if (isDaySelected(days[4])) {
                        String extgraString=(isDayOn(days[4]))?" on":" off";
                        activeDays += Calendar.THURSDAY+extgraString;
                    }
                    if (isDaySelected(days[5])) {
                        String extgraString=(isDayOn(days[5]))?" on":" off";
                        activeDays += Calendar.FRIDAY+extgraString;
                    }
                    if (isDaySelected(days[6])) {
                        String extgraString=(isDayOn(days[6]))?" on":" off";
                        activeDays += Calendar.SATURDAY+extgraString;
                    }

                    String commandForUpdate="CommandText:"+commandCombo.getSelectedItem().toString()+fr.sh.db.COMMAND_SPLIT_STRING+fr.sh.db.DAYS_STRING
                        + activeDays+fr.sh.db.COMMAND_SPLIT_STRING+fr.sh.db.TIME_STRING+timeLabel.getText()
                        +fr.sh.db.COMMAND_SPLIT_STRING+fr.sh.db.IS_WEEKLY+isWeekly.isSelected() +fr.sh.db.COMMAND_SPLIT_STRING+
                        fr.sh.db.IS_ACTIVE+isActive.isSelected();
                    // command:CommandText:kitchen lights##ActiveDays:2 on3 on4 on
                    //##ActiveTime:00:02##IsWeekly:true##IsActive:true
                    //       commandID=3          

 
                    fr.sh.db.updateShedule(commandForUpdate,Integer.toString(shedule.getId()));
                    new SheduleView(fr);
                }
            });
        cancel.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new SheduleView(fr);

                }
            });

        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);
        add(header,BorderLayout.PAGE_START);
        updateAll(shedule);

    }

    
    private boolean isDaySelected(Component v) {
        Color color = Color.gray;
        color = v.getBackground();

        if (color.equals( Color.green)||color.equals(Color.red)) {
            return true;
        }


        return false;
    }

    private boolean isDayOn(Component v) {

        Color background = v.getBackground();

        if (background.equals( Color.green)) {
            return true;
        }


        return false;
    }

    private void updateAll(Shedule shedule){
        updateAll(shedule.getActiveDays(), shedule.getIsWeekly(), shedule.getIsActive(), shedule.getTime());
        repaint();
        revalidate();
        centerPanel.repaint();
        centerPanel.revalidate();
    }

    private void updateAll(String activeDays,String weeklyString,String activeString,String time){
        updateDaysEnable(activeDays);
        updateWeekly(weeklyString);
        updatActive(activeString);
        updateTime(time);
    }

    private void updateAll(){
        updateDaysEnable();
        updateWeekly();
        updatActive();
        updateTime();
    }

    protected void updateDaysEnable(String activeDays){
        this.activeDays=activeDays;
        updateDaysEnable();
    }

    protected void updateDaysEnable(){

        if(activeDays.contains(Integer.toString( Calendar.SUNDAY)+" on")){
            days[0].setBackground(colors[1]);

        } else if(activeDays.contains(Integer.toString( Calendar.SUNDAY)+" off")){
            days[0].setBackground(colors[2]);

        } else {
            days[0].setBackground(colors[0]);

        }

        if(activeDays.contains(Integer.toString( Calendar.MONDAY)+" on")){
            days[1].setBackground(colors[1]);

        } else if(activeDays.contains(Integer.toString( Calendar.MONDAY)+" off")){
            days[1].setBackground(colors[2]);

        } else {
            days[1].setBackground(colors[0]);

        }

        if(activeDays.contains(Integer.toString( Calendar.TUESDAY)+" on")){
            days[2].setBackground(colors[1]);

        } else if(activeDays.contains(Integer.toString( Calendar.TUESDAY)+" off")){
            days[2].setBackground(colors[2]);

        } else {
            days[2].setBackground(colors[0]);

        }

        if(activeDays.contains(Integer.toString( Calendar.WEDNESDAY)+" on")){
            days[3].setBackground(colors[1]);

        } else if(activeDays.contains(Integer.toString( Calendar.WEDNESDAY)+" off")){
            days[3].setBackground(colors[2]);

        } else {
            days[3].setBackground(colors[0]);

        }

        if(activeDays.contains(Integer.toString( Calendar.THURSDAY)+" on")){
            days[4].setBackground(colors[1]);

        } else if(activeDays.contains(Integer.toString( Calendar.THURSDAY)+" off")){
            days[4].setBackground(colors[2]);

        } else {
            days[4].setBackground(colors[0]);

        }

        if(activeDays.contains(Integer.toString( Calendar.FRIDAY)+" on")){
            days[5].setBackground(colors[1]);

        } else if(activeDays.contains(Integer.toString( Calendar.FRIDAY)+" off")){
            days[5].setBackground(colors[2]);

        } else {
            days[5].setBackground(colors[0]);

        }
        if(activeDays.contains(Integer.toString( Calendar.SATURDAY)+" on")){
            days[6].setBackground(colors[1]);

        } else if(activeDays.contains(Integer.toString( Calendar.SATURDAY)+" off")){
            days[6].setBackground(colors[2]);

        } else {
            days[6].setBackground(colors[0]);

        }

        System.out.println(weeklyString);
        //setBackground(usingColor);
        //centerPanel.setBackground(usingColor);
        //firstRow.setBackground(usingColor);
        //secondRow.setBackground(usingColor);

    }

    protected void updateTime(String time){
        this.time=time;
        updateWeekly();
    }

    protected void updateTime(){
        timeLabel.setText(time);
    }

    protected void updateWeekly(String weeklyString){

        this.weeklyString=weeklyString;
        updateWeekly();
    }

    protected void updateWeekly(){
        if(weeklyString.equalsIgnoreCase("true")|| weeklyString.equalsIgnoreCase("enable")){
            isWeekly.setSelected(true);
        }else  if(weeklyString.equalsIgnoreCase("false")|| weeklyString.equalsIgnoreCase("disable")){
            isWeekly.setSelected(false);
        }
    }

    protected void updatActive(String activeString){

        this.activeString=activeString;
        updatActive();
    }

    protected void updatActive(){

        if(activeString.equalsIgnoreCase("true")|| activeString.equalsIgnoreCase("enable")){
            isActive.setSelected(true);
        }else  if(activeString.equalsIgnoreCase("false")|| activeString.equalsIgnoreCase("disable")){
            isActive.setSelected(false);
        }
    }}