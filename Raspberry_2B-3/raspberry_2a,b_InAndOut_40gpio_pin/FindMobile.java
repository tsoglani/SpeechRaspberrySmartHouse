
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikos
 */
public final class FindMobile {

    public static final int PORT = 2429;
public FindMobile(SH sh){

     new Thread(){
        
        public void run(){
         try {
             sendToAllIpInNetwork(sh);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        }
        }.start();
         
}
    
    public  void sendToAllIpInNetwork(SH sh) throws UnknownHostException, IOException {
ArrayList <String>stAdd=new ArrayList<String>();
                try {

                    for (int i = 0; i < sh.sendingTo.size(); i++) {
                        Object[] obj =  sh.sendingTo.get(i);
                        InetAddress ia = (InetAddress) obj[0];

                        
stAdd.add(ia.toString());
 Socket s = new Socket(ia.toString(), PORT);

                            System.out.println("success   " + ia.toString());

                            s.close();
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
         
        
        
        ArrayList<String> ipList = getLocal();

        for (String ip : ipList) {
            for (int i = 1; i < 255; i++) {
                final String checkIp = ip + i;
                if(stAdd.contains(checkIp)){
                continue;
                }
                new Thread() {
                    public void run() {
                        try {
                            //      System.out.println(checkIp + "  :  " + InetAddress.getByName(checkIp).isReachable(2000));

                            Socket s = new Socket(checkIp, PORT);

                            System.out.println("success   " + checkIp);

                            s.close();

                        } catch (IOException ex) {
                            //   System.out.println(checkIp + " is not available");
                        }

                    }
                }.start();
            }
        }
        
    }

    private  ArrayList<String> getLocal() throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        ArrayList<String> list = new ArrayList<String>();
        while (e.hasMoreElements()) {

            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {

                InetAddress inet = (InetAddress) ee.nextElement();
                if (!inet.isLinkLocalAddress()) {

                    String hostAdd = inet.getHostAddress();
                    // System.out.println(hostAdd);
                    String str = "";
                    String[] ars = hostAdd.split("\\.");
                    //    System.out.println("ars.length = " + ars.length);
                    for (int j = 0; j < ars.length - 1; j++) {
                        //    System.out.println(ars[j]);
                        str += ars[j] + ".";
                    }
                    //  System.out.println("str = " + str);
                    list.add(str);
                }
            }
        }
        return list;
    }
}