
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class SelectNewShedule extends JPanel
{
    private Fr fr;
    private JPanel centerPanel= new JPanel();
    private JPanel header= new JPanel();
    public SelectNewShedule(Fr fr)
    {
        this.fr =fr;
        ArrayList<String>[] usingList;

        usingList=fr.sh.outputPowerCommands;
        String [] comboCommandsString=new String[usingList.length];
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            comboCommandsString[i]=list.get(0);
        }
header.setLayout(new BorderLayout());
        greateGUIForSelection(comboCommandsString);
    }

    private void greateGUIForSelection( String [] comboCommandsString){
        fr.getContentPane().removeAll();
        if(comboCommandsString!=null&&comboCommandsString.length>0){
            setLayout(new BorderLayout());
            centerPanel.setLayout(new GridLayout((int)(Math.sqrt(comboCommandsString.length)+1),(int)(Math.sqrt(comboCommandsString.length)+1)));
            for(String s:comboCommandsString){
                JButton commandButton= new JButton(s);
                commandButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e ){
                new AddNewShedule(fr,commandButton.getText());
                }
                });
                centerPanel.add(commandButton);
            }
        }
        ImageIcon backTime=new ImageIcon("/home/pi/Desktop/SpeechRaspberrySmartHouse/Raspberry_2B-3/raspberry_2a,b_InAndOut_40gpio_pin/back.png");
        backTime=new ImageIcon(fr.getScaledImage(backTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        JButton     back = new JButton(backTime);
        back.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new SheduleView(fr);
                }
            });

        header.add(fr.home,BorderLayout.LINE_END);
        header.add(back,BorderLayout.LINE_START);

        add(centerPanel);
        add(header,BorderLayout.PAGE_START);

        fr.add(this);
        repaint();
        revalidate();

    }
    
   
}
