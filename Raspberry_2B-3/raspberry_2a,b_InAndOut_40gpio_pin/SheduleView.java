import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
public class SheduleView extends JPanel
{ private JButton back;
    private Fr fr;
    private  Color []colors = {Color.white,Color.green,Color.red};
    private ArrayList   <MyJPanel> myPanels;
    private JButton addNewSchedule;
    ImageIcon addSchedule;
    Thread thread;
     ImageIcon backTime;
    static  String selectedOption=null;
    public SheduleView(Fr fr)
    {
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);
        fr.isSwitchModeSelected=false;     
        fr.isSheduleModeSelected=true;
        fr.isTimerModeSelected=false;
        fr.isOnMainMenu=false;
        
         backTime=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/back.png");
        backTime=new ImageIcon(fr.getScaledImage(backTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
       
        this.fr=fr;
        setLayout(new BorderLayout());

        fr.shv=this;
        addSchedule=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/add_calentar.png");
        addSchedule=new ImageIcon(fr.getScaledImage(addSchedule.getImage(),(int)(fr.height/15), (int)(fr.height/15)));

        addNewSchedule = new JButton(addSchedule);

        addNewSchedule.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    fr.isSheduleModeSelected=false;
                    fr.shv=null;
                    new SelectNewShedule(fr);
                }
            });
        if(selectedOption!=null){
            if(selectedOption.equalsIgnoreCase("all")){
                allView();
                return;
            }else   if(selectedOption.equalsIgnoreCase("device")){
                byDeviceView();
                return;
            }else   if(selectedOption.equalsIgnoreCase("day")){
                byDayView();
                return;
            }else   if(selectedOption.startsWith("createByDayGUI")){
                               createByDayGUI(Integer.parseInt(selectedOption.substring("createByDayGUI".length(),selectedOption.length())));
                return;
            }
            
            
            else   if(selectedOption.startsWith("createByDeviceGUI")){
                createByDeviceGUI(selectedOption.substring("createByDeviceGUI".length(),selectedOption.length()));
                return;
            }
        }

        JPanel header=new JPanel();

        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.EAST);
        JPanel menu= new JPanel();

        menu.setLayout(new GridLayout(3,1));

        JButton byAll= new JButton("All");
        JButton byDay= new JButton("By day");
        JButton byDevice= new JButton("By device");
        menu.add(byAll);
        menu.add(byDay);
        menu.add(byDevice);
        byAll.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    allView();
                    selectedOption="all";
                }
            });

        byDevice.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    byDeviceView();
                    selectedOption="device";
                }
            });
        byDay.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    byDayView();
                    selectedOption="day";
                }
            });

        add(menu);
        add(header,BorderLayout.NORTH);
        fr.repaint();
        fr.revalidate();
        repaint();
        revalidate();

    }
    private void allView(){
        createGUI();

    }
    
    private void byDayView(){
      fr.getContentPane().removeAll();
        removeAll();
        
         JPanel header=new JPanel();
        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        JButton  back = new JButton(backTime);
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    selectedOption=null;
                    new SheduleView(fr);

                }
            });
            setLayout(new GridLayout(3,3));
            JButton dayButton=null;
             for(int day=0;day<7;day++){
            switch(day){
            case 0:dayButton= new JButton("Sunday");
            break;
            case 1:dayButton= new JButton("Monday");
            break;
            case 2:dayButton= new JButton("Tuesday");
            break;
            case 3:dayButton= new JButton("Wednesday");
            break;
            case 4:dayButton= new JButton("Thursday");
            break;
            case 5:dayButton= new JButton("Friday");
            break;
            case 6:dayButton= new JButton("Saturday");
            break;
            }
            add(dayButton);
            final int day2=day+1;
            dayButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
            createByDayGUI(day2);
            }});
            }
            
            
        header.add(back,BorderLayout.LINE_START);
        fr.add(header,BorderLayout.PAGE_START); 
        fr.add(this);
        repaint();
        revalidate();

        fr.repaint();
        fr.revalidate();
        
    }
    private void byDeviceView(){
        fr.getContentPane().removeAll();
        removeAll();
        fr.isSwitchModeSelected=false;
        fr.getContentPane().add(this);
        ArrayList<String>[] usingList;

        usingList=fr.sh.outputPowerCommands;

        ArrayList <String> devices= new ArrayList();
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            devices.add(list.get(0));

        }


        setLayout(new GridLayout((int)Math.sqrt(devices.size()),(int)Math.sqrt(devices.size())));
        for (int i=0;i<devices.size();i++){
            String dv=devices.get(i);
            JButton devButton= new JButton(dv);
            devButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        createByDeviceGUI(dv);

                        selectedOption="createByDeviceGUI"+dv;
                    }
                });

            add(devButton);
        }
      
        JPanel header=new JPanel();
        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        JButton  back = new JButton(backTime);
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    selectedOption=null;
                    new SheduleView(fr);
        fr.isSheduleModeSelected=false;
                }
            });
        header.add(back,BorderLayout.LINE_START);
        fr.add(header,BorderLayout.PAGE_START); 

        repaint();
        revalidate();

        fr.repaint();
        fr.revalidate();
        //    createByDeviceGUI();
    }

    ArrayList<SingleSheduleView> deletedSingleSheduleView = new ArrayList<SingleSheduleView>();

    protected void update(ArrayList<Shedule> sheduleList){
        if(sheduleList!=null){
            for(int i=0;i<sheduleList.size();i++){
                Shedule shedule=sheduleList.get(i);
                boolean containsInMyPanels=false;
                boolean isGoingToOpen;
                String timerFullCommand=shedule.getCommandText();

                if(myPanels!=null)
                    for(int j=0;j<myPanels.size();j++){
                        MyJPanel mp=myPanels.get(j);

                        if(mp.title.equals(timerFullCommand)){

                            /////for 
                            mp.update(shedule,sheduleList);

                        }
                    }
            }

            deletedSingleSheduleView.removeAll(deletedSingleSheduleView);
            for(int j=0;j<myPanels.size();j++){
                MyJPanel mp=myPanels.get(j);
                JPanel centerPanel=mp.centerPanel;
                for(int i=0;i<centerPanel.getComponentCount();i++){

                    boolean containsInSheduleList=false;
                    SingleSheduleView ssv=(SingleSheduleView)centerPanel.getComponent(i);
      
                    for(int k=0;k<sheduleList.size();k++){
                        Shedule shedule2=sheduleList.get(k);

                        if(shedule2.getId()==ssv.id&&shedule2.getCommandText().equals(ssv.commandString)){
                            containsInSheduleList=true;

                            break;
                        }

                    }
                    if(!containsInSheduleList){

                        deletedSingleSheduleView.add(ssv);
                    }
                }

                for(int i=0;i<deletedSingleSheduleView.size();i++){
                    centerPanel.remove(deletedSingleSheduleView.get(i));}
                centerPanel.repaint();
                centerPanel.revalidate();
            }

        }
    }
    
    private int usingDay=-1;
    private void createByDayGUI(int day){
        usingDay=day;
                        selectedOption="createByDayGUI"+day;
  fr.isSwitchModeSelected=false;     
        fr.isSheduleModeSelected=true;
        fr.isTimerModeSelected=false;
        fr.isOnMainMenu=false;
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);
        removeAll();
        setLayout(new BorderLayout());
        JPanel header=new JPanel();
        JPanel bottom=new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(addNewSchedule);
        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        //  header.add(back,BorderLayout.LINE_START);
         JButton  back = new JButton(backTime);
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    selectedOption=null;
    selectedOption=null;
                  byDayView();
        fr.isSheduleModeSelected=false;

                }
            });
            
               JComboBox commandCombo ;

        
        String [] comboCommandsString={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
      

        commandCombo= new JComboBox(comboCommandsString);
        commandCombo.setSelectedIndex(day-1);
        
        final String fontName = commandCombo.getSelectedItem().toString();
        commandCombo.setFont(new Font(fontName, Font.BOLD, 20));
    
        commandCombo.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                  createByDayGUI(commandCombo.getSelectedIndex()+1);
                    }
                }
            });
        ((JLabel)commandCombo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        
            
            

        header.add(back,BorderLayout.LINE_START);
        header.add(commandCombo);
        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);
        add(bottom,BorderLayout.PAGE_END);
        // add(bottom,BorderLayout.PAGE_END);
        ArrayList<String>[] usingList;
        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
        center.setLayout(new GridLayout(0,(int)(fr.width/350)));

        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0),1);
            myPanels.add(button);
            center.add(button);

        }
     scrollSpecific = new JScrollPane(center);
                add(scrollSpecific);
        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();
  if(thread==null||!thread.isAlive()||!fr.isSheduleModeSelected){
            thread=  new Thread(){
                public void run(){
                    while(fr.isSheduleModeSelected)   {
                        try{

                            ArrayList<Shedule> shedules=fr.sh.db.getShedules();
                                                        ArrayList<Shedule> activeShedules=new ArrayList<Shedule>();
                            for(Shedule s:shedules){
                                System.out.println(s.getActiveDays());
                            if(s.getActiveDays(). contains(Integer.toString(usingDay)))
                            activeShedules.add(s);
                        }
  if(shedules!=null&&fr.isSheduleModeSelected)
                                update(activeShedules);
                            Thread.sleep(2000);
                        }catch(Exception e){}}
                }};thread.start();}
    }
    
     JScrollPane   scrollSpecific;
    public void createByDeviceGUI(String dev){
        System.out.println("createByDeviceGUI:"+dev);
         fr.isSwitchModeSelected=false;     
        fr.isSheduleModeSelected=true;
        fr.isTimerModeSelected=false;
        fr.isOnMainMenu=false;
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);
        JComboBox commandCombo ;

        ArrayList<String>[] usingList2;

        usingList2=fr.sh.outputPowerCommands;
        String [] comboCommandsString=new String[usingList2.length];
        for(int i=0;i<usingList2.length;i++){
            ArrayList <String> list=usingList2[i];
            comboCommandsString[i]=list.get(0);
        }

        commandCombo= new JComboBox(comboCommandsString);
        commandCombo.setSelectedItem(dev);

        commandCombo.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
        
        commandCombo.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        createByDeviceGUI((String)commandCombo.getSelectedItem().toString());
                        selectedOption="createByDeviceGUI"+commandCombo.getSelectedItem().toString();
                        commandCombo.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
                    }
                }
            });
        ((JLabel)commandCombo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        removeAll();
        setLayout(new BorderLayout());

        JPanel bottom=new JPanel();
        bottom.setLayout(new BorderLayout());
        JButton   addNewSchedule = new JButton(addSchedule);

        addNewSchedule.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    fr.isSheduleModeSelected=false;
                    fr.shv=null;

                    new AddNewShedule(fr,dev);

                }
            });
        bottom.add(addNewSchedule);
        JPanel header=new JPanel();
        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        header.add(commandCombo);
        //  header.add(back,BorderLayout.LINE_START);
        JButton  back = new JButton(backTime);
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    byDeviceView();
        fr.isSheduleModeSelected=false;
                }
            });
        header.add(back,BorderLayout.LINE_START);
        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);
        add(bottom,BorderLayout.PAGE_END);
        // add(bottom,BorderLayout.PAGE_END);
        ArrayList<String>[] usingList;
        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
        center.setLayout(new BorderLayout());
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            if(!list.get(0).equalsIgnoreCase(dev)){
                continue;
            }
            MyJPanel button = new MyJPanel(list.get(0),0);
            myPanels.add(button);
            center.add(button);

        }

           scrollSpecific = new JScrollPane(center);
        add(scrollSpecific);
        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();
        if(thread==null||!thread.isAlive()||!fr.isSheduleModeSelected){
            thread=  new Thread(){
                public void run(){
                    while(fr.isSheduleModeSelected)   {
                        try{

                            ArrayList<Shedule> shedules=fr.sh.db.getShedules();
                            if(shedules!=null&&fr.isSheduleModeSelected)
                                update(shedules);
                                System.out.println(shedules);
                            Thread.sleep(2000);
                        }catch(Exception e){}}
                }};thread.start();}

    }

    private void createGUI(){
         fr.isSwitchModeSelected=false;     
        fr.isSheduleModeSelected=true;
        fr.isTimerModeSelected=false;
        fr.isOnMainMenu=false;
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);
        removeAll();
        setLayout(new BorderLayout());
        JPanel header=new JPanel();
        JPanel bottom=new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(addNewSchedule);
        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        //  header.add(back,BorderLayout.LINE_START);
         JButton  back = new JButton(backTime);
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    selectedOption=null;
                    new SheduleView(fr);

                }
            });
        header.add(back,BorderLayout.LINE_START);
        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);
        add(bottom,BorderLayout.PAGE_END);
        // add(bottom,BorderLayout.PAGE_END);
        ArrayList<String>[] usingList;
        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
        center.setLayout(new GridLayout(0,(int)(fr.width/350)));

        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            myPanels.add(button);
            center.add(button);

        }
    scrollSpecific = new JScrollPane(center,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollSpecific);

        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();
  if(thread==null||!thread.isAlive()||!fr.isSheduleModeSelected){
            thread=  new Thread(){
                public void run(){
                    while(fr.isSheduleModeSelected)   {
                        try{

                            ArrayList<Shedule> shedules=fr.sh.db.getShedules();
  if(shedules!=null&&fr.isSheduleModeSelected)
                                update(shedules);
                            Thread.sleep(2000);
                        }catch(Exception e){}}
                }};thread.start();}

    }
    private class MyJPanel extends JPanel{
        int color_id=0;
        String title ;
        protected JPanel centerPanel= new JPanel();

        public MyJPanel(String title){
            this.title=title;

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            titleLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            setBackground(colors[color_id]);
            titlePanel.setBackground(colors[color_id]);
            add(titlePanel,BorderLayout.PAGE_START);
            //  add(centerPanel);           
                        centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
                 JScrollPane      scrollSpecific = new JScrollPane(centerPanel,
                  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

           
            add(scrollSpecific,BorderLayout.CENTER);
Dimension pSize = new Dimension(340, 140);
        Dimension mSize = new Dimension(100, 100);

      setPreferredSize(pSize);
        setMinimumSize(mSize);
            // example of use
            //   for(int j=0;j<10;j++){
            //     SingleSheduleView ssv=new SingleSheduleView();
            // centerPanel.add(ssv);
            // }
            //## end of example

        }

        public MyJPanel(String title, int id){
            if(id==0){
                this.title=title;

                setLayout(new BorderLayout());
                setBorder(BorderFactory.createLineBorder(Color.black));
               
                setBackground(colors[color_id]);

                 JScrollPane      scrollSpecific = new JScrollPane(centerPanel,
                  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                add(scrollSpecific);
               
                centerPanel.setLayout(new GridLayout(0,(int)(fr.width/350)));
                //  add(centerPanel);
                //  add(scrollSpecific,BorderLayout.CENTER);
                //  centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
                // example of use
                //   for(int j=0;j<10;j++){
                //     SingleSheduleView ssv=new SingleSheduleView();
                // centerPanel.add(ssv);
                // }
                //## end of example
                Dimension pSize = new Dimension(350, 150);
Dimension maxSize = new Dimension(700, 150);
        Dimension mSize = new Dimension(100, 100);
           setPreferredSize(pSize);
        setMinimumSize(mSize);
        setMaximumSize(maxSize);
            }
            
            if(id==1){
                this.title=title;

                setLayout(new BorderLayout());
                setBorder(BorderFactory.createLineBorder(Color.black));
                JLabel titleLabel= new JLabel(title);
                titleLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
                JPanel titlePanel= new JPanel();
                titlePanel.add(titleLabel);
                 add(titlePanel,BorderLayout.PAGE_START);
                setBackground(colors[color_id]);
                titlePanel.setBackground(colors[color_id]);
                   JScrollPane      scrollSpecific = new JScrollPane(centerPanel,
                  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));;
                add(scrollSpecific);
             //   centerPanel.setLayout(new GridLayout(0,2));
                //  add(centerPanel);
                //  add(scrollSpecific,BorderLayout.CENTER);

                // example of use
                //   for(int j=0;j<10;j++){
                //     SingleSheduleView ssv=new SingleSheduleView();
                // centerPanel.add(ssv);
                // }
                //## end of example
                Dimension pSize = new Dimension(350, 150);
Dimension maxSize = new Dimension(700, 150);
        Dimension mSize = new Dimension(100, 100);
           setPreferredSize(pSize);
        setMinimumSize(mSize);
        setMaximumSize(maxSize);
            }
          


   
        }

        protected void update(Shedule shedule,ArrayList<Shedule> sheduleList){
            boolean contains=false;
            SingleSheduleView usingSsv=null;

            for(int i=0;i<centerPanel.getComponentCount();i++){
                SingleSheduleView ssv=(SingleSheduleView)centerPanel.getComponent(i);

                if(ssv.id==shedule.getId()){
                    contains=true;
                    usingSsv=ssv;
                }
            }
            if(!contains){
                centerPanel.add(new SingleSheduleView(shedule));
                centerPanel.repaint();
                centerPanel.revalidate();
                if(scrollSpecific!=null){
                
                scrollSpecific.repaint();
                scrollSpecific.revalidate();
                }
                 centerPanel.getParent().repaint();
                centerPanel.getParent().revalidate();
            }else{
                if(usingSsv!=null){
                    usingSsv.updateAll(shedule);
                }

            }

            /// check for shedule in centerPanel id if not exist create
            /// if exist update text

        }

    } 
    class SingleSheduleView extends JPanel{

        private String activeDays, time, weeklyString, activeString,commandString;
        protected  int id;
        protected JPanel firstRow,secondRow;
        private JButton delete,edit;
        private JCheckBox isWeekly,isActive; 
        private JLabel [] days= new JLabel[7];
        private Shedule shedule;
        private JPanel centerPanel,header;
        protected JLabel timeLabel;
        private   ImageIcon deleteAmbIcon , deleteIcon;
        public SingleSheduleView(Shedule shedule){
            setLayout(new BorderLayout());
            activeDays= shedule.getActiveDays();
            weeklyString=  shedule.getIsWeekly();
            activeDays=  shedule.getActiveDays();
            activeString=  shedule.getIsActive();
            commandString=  shedule.getCommandText();
            centerPanel= new JPanel();
            header= new JPanel();
            id=shedule.getId();
            time=shedule.getTime();

            setBorder(BorderFactory.createLineBorder(Color.blue));
            firstRow= new JPanel();
            firstRow.setLayout (new GridLayout(1,7));
            secondRow= new JPanel();

            deleteAmbIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/delete_amb.png");
            deleteAmbIcon=new ImageIcon(fr.getScaledImage(deleteAmbIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));

            deleteIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/delete.png");
            deleteIcon=new ImageIcon(fr.getScaledImage(deleteIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
            delete = new JButton(deleteIcon);

            ImageIcon editIcon=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/edit.png");
            editIcon=new ImageIcon(fr.getScaledImage(editIcon.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
            edit = new JButton(editIcon);

            isWeekly= new JCheckBox("is Weekly");
            isActive= new JCheckBox("is Active");
            isWeekly.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getStateChange() == ItemEvent.SELECTED){
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_WEEKLY+"true");
                        }else{
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_WEEKLY+"false");
                        }

                    }
                });
            isActive.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getStateChange() == ItemEvent.SELECTED){
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_ACTIVE+"true");
                        }else{
                            fr.sh.db.updateSingleShedule(commandString,Integer.toString(id),DB.IS_ACTIVE+"false");
                        }

                    }
                });
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            centerPanel.add(firstRow);
            centerPanel.add(secondRow);
            timeLabel=new JLabel(time);

            Font f = timeLabel.getFont();
            f=f.deriveFont (30f);
            timeLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            header.add(timeLabel);
            add(centerPanel);
            add(header,BorderLayout.PAGE_START);
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
                firstRow.setBorder(BorderFactory.createLineBorder(Color.gray));
                firstRow.add(dayButton);

                dayButton.setBackground(colors[0]);

            }
            //   secondRow.setLayout(new BorderLayout());
            JPanel centerInsideSecondPanel= new JPanel();
            secondRow.add(delete,SwingConstants.CENTER);
            //    secondRow.add(new JLabel("    "),SwingConstants.CENTER);
            //   secondRow.add(new JLabel("    "),SwingConstants.CENTER);

            centerInsideSecondPanel.add(isActive,SwingConstants.CENTER);
            centerInsideSecondPanel.add(isWeekly,SwingConstants.CENTER);
            secondRow.add(centerInsideSecondPanel,SwingConstants.CENTER);
            // secondRow.add(new JLabel("    "),SwingConstants.CENTER);
            //             secondRow.add(new JLabel("    "),SwingConstants.CENTER);
            // secondRow.add(edit,SwingConstants.CENTER);
            add(edit,BorderLayout.LINE_START);
            add(delete,BorderLayout.LINE_END);
            edit.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        fr.isSheduleModeSelected=false;

                        fr.shv=null;
                        new EditSheduleView(fr,shedule);
                    }
                });
            delete.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        new Thread(){
                            public void run(){
                                try{
                                    if(delete!=null){

                                        delete.setIcon(deleteAmbIcon);
                                        delete.setEnabled(false);
                                    }
                                    sleep(3000);
                                    if(delete!=null){
                                        delete.setIcon(deleteIcon);
                                        delete.setEnabled(true);
                                    }}catch(Exception e){}
                            }

                        }.start();

                        fr.sh.db.removeShedule(Integer.toString(id),commandString);
                        repaint();
                        revalidate();
                        centerPanel.repaint();
                        centerPanel.revalidate();
                    }
                });

            updateAll(shedule);

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
        }

    }

}
