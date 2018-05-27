import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.util.Calendar;
import javax.swing.border.Border;
public class SheduleView extends JPanel
{ private JButton back;
    private Fr fr;
    private  Color []colors = {Color.white,Color.green,Color.red};
    private ArrayList   <MyJPanel> myPanels;
    private JButton addNewSchedule;
    private ImageIcon addSchedule;
  private  Thread thread;
   private  ImageIcon backTime;
     final int sleepingTime=600;
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
            setLayout(new GridLayout(4,3));
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
           int tempDay=day;
          
             final int day2=tempDay+1;
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
    
    
    
    

    
     ArrayList <Shedule> containingElements=new ArrayList<Shedule>();
    private int usingDay=-1;
    private void createByDayGUI(int day){

 byDayCommandUsed.removeAll(byDayCommandUsed);
       
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
                  System.out.println(commandCombo.getSelectedIndex()+1);
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
        
     
        
        
        
                            ArrayList<Shedule> shedules2=fr.sh.db.getShedules();
     for(Shedule d:shedules2){
                         System.out.println(" day"+d.getActiveDays()+"::vs::"+day);
             if(d.getActiveDays().contains(day+" off")||d.getActiveDays().contains(day+" on")){
                System.out.println("Same day"+d.getActiveDays()+"::vs::"+day);
                
                
           if(!byDayCommandUsed.contains(d.getCommandText())){

     String cmd=d.getCommandText();
           MyJPanel button = new MyJPanel(cmd,1);////1
            byDayCommandUsed.add(cmd);
           myPanels.add(button);
         center.add(button);
       }
    
    
    }
       }
     
        
        int row=(int)(Math.sqrt(byDayCommandUsed.size())),colum=(int)(Math.sqrt(byDayCommandUsed.size()));
        if(row==0&&colum==0){
        row=1;
        colum=1;
    }
        center.setLayout(new  GridLayout(row,colum));
       
    // scrollSpecific = new JScrollPane(center);
                add(center);
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

                          if(s.getActiveDays(). contains(Integer.toString(usingDay)))
                           activeShedules.add(s);
                        }
  if(shedules!=null&&fr.isSheduleModeSelected){

        
                update(activeShedules);
       
      
    }
                              
                            Thread.sleep(sleepingTime);
                        }catch(Exception e){}}
                }};thread.start();}
    }
    ArrayList <String>byDayCommandUsed= new ArrayList<String>();
    
    
    
    
    
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
        JPanel center= new JPanel( );
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
     //   center.setPreferredSize(new Dimension(600,606));
        
JPanel c2= new JPanel();
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            if(!list.get(0).equalsIgnoreCase(dev)){
                continue;
            }
            
            System.out.println("--------------list.get(0)"+list.get(0));
            MyJPanel button = new MyJPanel(list.get(0),0);
            myPanels.add(button);
            center.add(button);


        }


c2.setLayout(new BorderLayout());

          scrollSpecific = new JScrollPane(center);
scrollSpecific.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
scrollSpecific.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
c2.add(scrollSpecific);
//c2.add(center);


        add(c2);
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
                             System.out.println(shedules);
                            if(shedules!=null&&fr.isSheduleModeSelected)
 {                            //   update(shedules);
               

    
                update(shedules);
  
      
      

}
                            Thread.sleep(sleepingTime);
                        }catch(Exception e){

                        }}
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
        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length),(int)Math.sqrt(usingList.length)));

        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            myPanels.add(button);
            center.add(button);

        }

        add(center);

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
  if(shedules!=null&&fr.isSheduleModeSelected){
      
     
                update(shedules);
  

    
    }
  
                            Thread.sleep(sleepingTime);
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
                 JScrollPane      scrollSpecific = new JScrollPane(centerPanel);
scrollSpecific.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
scrollSpecific.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
           
            add(scrollSpecific,BorderLayout.CENTER);
Dimension pSize = new Dimension(450, 140);
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

               JScrollPane      scrollSpecific = new JScrollPane(centerPanel);
           scrollSpecific.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollSpecific.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                add(centerPanel);
               
               // centerPanel.setLayout(new GridLayout(0,(int)(fr.width/450)));
                centerPanel.setLayout(new WrapLayout());
                //  add(centerPanel);
                //  add(scrollSpecific,BorderLayout.CENTER);
                //  centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
                // example of use
                //   for(int j=0;j<10;j++){
                //     SingleSheduleView ssv=new SingleSheduleView();
                // centerPanel.add(ssv);
                // }
                //## end of example
             Dimension pSize = new Dimension(450, 150);
Dimension maxSize = new Dimension(450, 150);
        Dimension mSize = new Dimension(100, 100);
           setPreferredSize(pSize);
        setMinimumSize(mSize);
        setMaximumSize(maxSize);
            }
            
              
           else if(id==1){
                this.title=title;

                      setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            titleLabel.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            setBackground(colors[color_id]);
            titlePanel.setBackground(colors[color_id]);
           // add(titlePanel,BorderLayout.PAGE_START);
            //  add(centerPanel);           
            JPanel extraPanel= new JPanel();
            extraPanel.setLayout(new BorderLayout());
            extraPanel.add(titlePanel,BorderLayout.PAGE_START);
                centerPanel.setLayout(new WrapLayout());
                extraPanel.add(centerPanel);
                        //centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
                 JScrollPane      scrollSpecific = new JScrollPane(extraPanel);
scrollSpecific.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
scrollSpecific.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
           
            add(scrollSpecific,BorderLayout.CENTER);
Dimension pSize = new Dimension(450, 140);
        Dimension mSize = new Dimension(100, 100);

      setPreferredSize(pSize);
        setMinimumSize(mSize);
            }
          


   
        }

        protected  void update(Shedule shedule,ArrayList<Shedule> sheduleList){
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
                                    sleep(sleepingTime+1000);
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


public class WrapLayout extends FlowLayout
{
    private Dimension preferredLayoutSize;

    /**
    * Constructs a new <code>WrapLayout</code> with a left
    * alignment and a default 5-unit horizontal and vertical gap.
    */
    public WrapLayout()
    {
        super();
    }

    /**
    * Constructs a new <code>FlowLayout</code> with the specified
    * alignment and a default 5-unit horizontal and vertical gap.
    * The value of the alignment argument must be one of
    * <code>WrapLayout</code>, <code>WrapLayout</code>,
    * or <code>WrapLayout</code>.
    * @param align the alignment value
    */
    public WrapLayout(int align)
    {
        super(align);
    }

    /**
    * Creates a new flow layout manager with the indicated alignment
    * and the indicated horizontal and vertical gaps.
    * <p>
    * The value of the alignment argument must be one of
    * <code>WrapLayout</code>, <code>WrapLayout</code>,
    * or <code>WrapLayout</code>.
    * @param align the alignment value
    * @param hgap the horizontal gap between components
    * @param vgap the vertical gap between components
    */
    public WrapLayout(int align, int hgap, int vgap)
    {
        super(align, hgap, vgap);
    }

    /**
    * Returns the preferred dimensions for this layout given the
    * <i>visible</i> components in the specified target container.
    * @param target the component which needs to be laid out
    * @return the preferred dimensions to lay out the
    * subcomponents of the specified container
    */
    @Override
    public Dimension preferredLayoutSize(Container target)
    {
        return layoutSize(target, true);
    }

    /**
    * Returns the minimum dimensions needed to layout the <i>visible</i>
    * components contained in the specified target container.
    * @param target the component which needs to be laid out
    * @return the minimum dimensions to lay out the
    * subcomponents of the specified container
    */
    @Override
    public Dimension minimumLayoutSize(Container target)
    {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }

    /**
    * Returns the minimum or preferred dimension needed to layout the target
    * container.
    *
    * @param target target to get layout size for
    * @param preferred should preferred size be calculated
    * @return the dimension to layout the target container
    */
    private Dimension layoutSize(Container target, boolean preferred)
    {
    synchronized (target.getTreeLock())
    {
        //  Each row must fit with the width allocated to the containter.
        //  When the container width = 0, the preferred width of the container
        //  has not yet been calculated so lets ask for the maximum.

        int targetWidth = target.getSize().width;
        Container container = target;

        while (container.getSize().width == 0 && container.getParent() != null)
        {
            container = container.getParent();
        }

        targetWidth = container.getSize().width;

        if (targetWidth == 0)
            targetWidth = Integer.MAX_VALUE;

        int hgap = getHgap();
        int vgap = getVgap();
        Insets insets = target.getInsets();
        int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
        int maxWidth = targetWidth - horizontalInsetsAndGap;

        //  Fit components into the allowed width

        Dimension dim = new Dimension(0, 0);
        int rowWidth = 0;
        int rowHeight = 0;

        int nmembers = target.getComponentCount();

        for (int i = 0; i < nmembers; i++)
        {
            Component m = target.getComponent(i);

            if (m.isVisible())
            {
                Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

                //  Can't add the component to current row. Start a new row.

                if (rowWidth + d.width > maxWidth)
                {
                    addRow(dim, rowWidth, rowHeight);
                    rowWidth = 0;
                    rowHeight = 0;
                }

                //  Add a horizontal gap for all components after the first

                if (rowWidth != 0)
                {
                    rowWidth += hgap;
                }

                rowWidth += d.width;
                rowHeight = Math.max(rowHeight, d.height);
            }
        }

        addRow(dim, rowWidth, rowHeight);

        dim.width += horizontalInsetsAndGap;
        dim.height += insets.top + insets.bottom + vgap * 2;

        //  When using a scroll pane or the DecoratedLookAndFeel we need to
        //  make sure the preferred size is less than the size of the
        //  target containter so shrinking the container size works
        //  correctly. Removing the horizontal gap is an easy way to do this.

        Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);

        if (scrollPane != null && target.isValid())
        {
            dim.width -= (hgap + 1);
        }

        return dim;
    }
    }

    /*
     *  A new row has been completed. Use the dimensions of this row
     *  to update the preferred size for the container.
     *
     *  @param dim update the width and height when appropriate
     *  @param rowWidth the width of the row to add
     *  @param rowHeight the height of the row to add
     */
    private void addRow(Dimension dim, int rowWidth, int rowHeight)
    {
        dim.width = Math.max(dim.width, rowWidth);

        if (dim.height > 0)
        {
            dim.height += getVgap();
        }

        dim.height += rowHeight;
    }
}

public class VerticalFlowLayout implements LayoutManager, java.io.Serializable
{
    /**
     * This value indicates that each row of components
     * should be left-justified.
     */
    public static final int TOP     = 0;

    /**
     * This value indicates that each row of components
     * should be centered.
     */
    public static final int CENTER    = 1;

    /**
     * This value indicates that each row of components
     * should be right-justified.
     */
    public static final int BOTTOM     = 2;

    /**
     * <code>align</code> is the property that determines
     * how each column distributes empty space.
     * It can be one of the following three values:
     * <ul>
     * <code>TOP</code>
     * <code>BOTTOM</code>
     * <code>CENTER</code>
     * </ul>
     *
     * @see #getAlignment
     * @see #setAlignment
     */
    int align;     // This is the one we actually use

    /**
     * The flow layout manager allows a seperation of
     * components with gaps.  The horizontal gap will
     * specify the space between components and between
     * the components and the borders of the
     * <code>Container</code>.
     *
     * @see #getHgap()
     * @see #setHgap(int)
     */
    int hgap;

    /**
     * The flow layout manager allows a seperation of
     * components with gaps.  The vertical gap will
     * specify the space between rows and between the
     * the rows and the borders of the <code>Container</code>.
     *
     * @see #getHgap()
     * @see #setHgap(int)
     */
    int vgap;

    /**
     * Constructs a new <code>VerticalFlowLayout</code> with a centered alignment and a
     * default 5-unit horizontal and vertical gap.
     */
    public VerticalFlowLayout()
    {
        this(CENTER, 5, 5);
    }

    /**
     * Constructs a new <code>VerticalFlowLayout</code> with the specified
     * alignment and a default 5-unit horizontal and vertical gap.
     * The value of the alignment argument must be one of
     * <code>VerticalFlowLayout.TOP</code>, <code>VerticalFlowLayout.BOTTOM</code>,
     * or <code>VerticalFlowLayout.CENTER</code>
     * @param align the alignment value
     */
    public VerticalFlowLayout(int align)
    {
        this(align, 5, 5);
    }

    /**
     * Creates a new flow layout manager with the indicated alignment
     * and the indicated horizontal and vertical gaps.
     * <p>
     * The value of the alignment argument must be one of
     * <code>VerticalFlowLayout.TOP</code>, <code>VerticalFlowLayout.BOTTOM</code>,
     * or <code>VerticalFlowLayout.CENTER</code>.
     * @param     align   the alignment value
     * @param     hgap  the horizontal gap between components
     *                   and between the components and the
     *                   borders of the <code>Container</code>
     * @param     vgap  the vertical gap between components
     *                   and between the components and the
     *                   borders of the <code>Container</code>
     */
    public VerticalFlowLayout(int align, int hgap, int vgap)
    {
        this.hgap = hgap;
        this.vgap = vgap;
        setAlignment(align);
    }

    /**
     * Gets the alignment for this layout.
     * Possible values are <code>VerticalFlowLayout.TOP</code>,
     * <code>VerticalFlowLayout.BOTTOM</code> or <code>VerticalFlowLayout.CENTER</code>,
     * @return   the alignment value for this layout
     * @see     java.awt.VerticalFlowLayout#setAlignment
     * @since     JDK1.1
     */
    public int getAlignment()
    {
        return align;
    }

    /**
     * Sets the alignment for this layout. Possible values are
     * <ul>
     * <li><code>VerticalFlowLayout.TOP</code>
     * <li><code>VerticalFlowLayout.BOTTOM</code>
     * <li><code>VerticalFlowLayout.CENTER</code>
     * </ul>
     * @param     align one of the alignment values shown above
     * @see     #getAlignment()
     * @since     JDK1.1
     */
    public void setAlignment(int align)
    {
        this.align = align;
    }

    /**
     * Gets the horizontal gap between components
     * and between the components and the borders
     * of the <code>Container</code>
     *
     * @return   the horizontal gap between components
     *           and between the components and the borders
     *           of the <code>Container</code>
     * @see     java.awt.VerticalFlowLayout#setHgap
     * @since     JDK1.1
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * Sets the horizontal gap between components and
     * between the components and the borders of the
     * <code>Container</code>.
     *
     * @param hgap the horizontal gap between components
     *           and between the components and the borders
     *           of the <code>Container</code>
     * @see     java.awt.VerticalFlowLayout#getHgap
     * @since     JDK1.1
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * Gets the vertical gap between components and
     * between the components and the borders of the
     * <code>Container</code>.
     *
     * @return   the vertical gap between components
     *           and between the components and the borders
     *           of the <code>Container</code>
     * @see     java.awt.VerticalFlowLayout#setVgap
     * @since     JDK1.1
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * Sets the vertical gap between components and between
     * the components and the borders of the <code>Container</code>.
     *
     * @param vgap the vertical gap between components
     *           and between the components and the borders
     *           of the <code>Container</code>
     * @see     java.awt.VerticalFlowLayout#getVgap
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    /**
     * Adds the specified component to the layout.
     * Not used by this class.
     * @param name the name of the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout.
     * Not used by this class.
     * @param comp the component to remove
     * @see    java.awt.Container#removeAll
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Returns the preferred dimensions for this layout given the
     * <i>visible</i> components in the specified target container.
     *
     * @param target the container that needs to be laid out
     * @return  the preferred dimensions to lay out the
     *          subcomponents of the specified container
     * @see Container
     * @see #minimumLayoutSize
     * @see    java.awt.Container#getPreferredSize
     */
    public Dimension preferredLayoutSize(Container target)
    {
    synchronized (target.getTreeLock())
    {
        Dimension dim = new Dimension(0, 0);
        int nmembers = target.getComponentCount();
        boolean firstVisibleComponent = true;

        for (int i = 0 ; i < nmembers ; i++)
        {
            Component m = target.getComponent(i);

            if (m.isVisible())
            {
                Dimension d = m.getPreferredSize();
                dim.width = Math.max(dim.width, d.width);

                if (firstVisibleComponent)
                {
                    firstVisibleComponent = false;
                }
                else
                {
                    dim.height += vgap;
                }

                dim.height += d.height;
            }
        }

        Insets insets = target.getInsets();
        dim.width += insets.left + insets.right + hgap*2;
        dim.height += insets.top + insets.bottom + vgap*2;
        return dim;
    }
    }

    /**
     * Returns the minimum dimensions needed to layout the <i>visible</i>
     * components contained in the specified target container.
     * @param target the container that needs to be laid out
     * @return  the minimum dimensions to lay out the
     *          subcomponents of the specified container
     * @see #preferredLayoutSize
     * @see    java.awt.Container
     * @see    java.awt.Container#doLayout
     */
    public Dimension minimumLayoutSize(Container target)
    {
    synchronized (target.getTreeLock())
    {
        Dimension dim = new Dimension(0, 0);
        int nmembers = target.getComponentCount();
        boolean firstVisibleComponent = true;

        for (int i = 0 ; i < nmembers ; i++)
        {
            Component m = target.getComponent(i);
            if (m.isVisible())
            {
                Dimension d = m.getMinimumSize();
                dim.width = Math.max(dim.width, d.width);

                if (firstVisibleComponent)
                {
                    firstVisibleComponent = false;
                }
                else
                {
                    dim.height += vgap;
                }

                dim.height += d.height;
            }
        }


        Insets insets = target.getInsets();
        dim.width += insets.left + insets.right + hgap*2;
        dim.height += insets.top + insets.bottom + vgap*2;
        return dim;
    }
    }

    /**
     * Lays out the container. This method lets each
     * <i>visible</i> component take
     * its preferred size by reshaping the components in the
     * target container in order to satisfy the alignment of
     * this <code>VerticalFlowLayout</code> object.
     *
     * @param target the specified component being laid out
     * @see Container
     * @see    java.awt.Container#doLayout
     */
    public void layoutContainer(Container target)
    {
    synchronized (target.getTreeLock())
    {
        Insets insets = target.getInsets();
        int maxHeight = target.getSize().height - (insets.top + insets.bottom + vgap*2);
        int nmembers = target.getComponentCount();
        int x = insets.left + hgap;
        int y = 0;
        int columnWidth = 0;
        int start = 0;

        boolean ttb = target.getComponentOrientation().isLeftToRight();

        for (int i = 0 ; i < nmembers ; i++)
        {
            Component m = target.getComponent(i);

            if (m.isVisible())
            {
                Dimension d = m.getPreferredSize();
                m.setSize(d.width, d.height);

                if ((y == 0) || ((y + d.height) <= maxHeight))
                {
                    if (y > 0)
                    {
                        y += vgap;
                    }

                    y += d.height;
                    columnWidth = Math.max(columnWidth, d.width);
                }
                else
                {
                    moveComponents(target, x, insets.top + vgap, columnWidth, maxHeight - y, start, i, ttb);
                    y = d.height;
                    x += hgap + columnWidth;
                    columnWidth = d.width;
                    start = i;
                }
            }
        }

        moveComponents(target, x, insets.top + vgap, columnWidth, maxHeight - y, start, nmembers, ttb);
    }
    }

    /**
     * Centers the elements in the specified row, if there is any slack.
     * @param target the component which needs to be moved
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width dimensions
     * @param height the height dimensions
     * @param columnStart the beginning of the column
     * @param columnEnd the the ending of the column
     */
    private void moveComponents(
        Container target, int x, int y, int width, int height, int columnStart, int columnEnd, boolean ttb)
    {
        switch (align)
        {
            case TOP:
                y += ttb ? 0 : height;
                break;
            case CENTER:
                y += height / 2;
                break;
            case BOTTOM:
                y += ttb ? height : 0;
                break;
        }

        for (int i = columnStart ; i < columnEnd ; i++)
        {
            Component m = target.getComponent(i);

            if (m.isVisible())
            {
                int cx;
                cx = x + (width - m.getSize().width) / 2;

                if (ttb)
                {
                    m.setLocation(cx, y);
                }
                else
                {
                    m.setLocation(cx, target.getSize().height - y - m.getSize().height);
                }

                y += m.getSize().height + vgap;
            }
        }
    }

    /**
     * Returns a string representation of this <code>VerticalFlowLayout</code>
     * object and its values.
     * @return   a string representation of this layout
     */
    public String toString()
    {
        String str = "";

        switch (align)
        {
            case TOP:    str = ",align=top"; break;
            case CENTER: str = ",align=center"; break;
            case BOTTOM: str = ",align=bottom"; break;
        }

        return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + str + "]";
    }


}

}


