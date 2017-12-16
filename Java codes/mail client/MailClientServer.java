import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MailClientServer {
    public static final int PORT=8085;
    public static HashMap<String,ArrayList<Email>>emailMap;


    public static void main(String args[]){

        try {
            emailMap=new HashMap<String,ArrayList<Email>>();
            ServerSocket serverSocket=new ServerSocket(PORT);
            while (true){
                Socket socket=serverSocket.accept();
                ClientHandlingThread clientThread=new ClientHandlingThread(socket);
                clientThread.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }




static class Email{
    String sender;
    String receiver;
    String data;

    public Email(String sender, String receiver, String data) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
    }
}
public static class ClientHandlingThread extends Thread{
    Socket socket;
    String loginName;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    ClientHandlingThread(Socket socket){
        this.socket=socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            loginName=dataInputStream.readUTF().trim();
            if(!emailMap.containsKey(loginName)){
                emailMap.put(loginName,new ArrayList<Email>());
            }
            System.out.println(loginName+" Logged In");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        super.run();
        ArrayList<Email> arrayList;
        Email email;
        while (true) {
            String data=new String();
            try {
                if (dataInputStream.available() > 0) {
                    String messagefromClient = dataInputStream.readUTF();
                    StringTokenizer stringTokenizer = new StringTokenizer(messagefromClient);
                    String messageType = stringTokenizer.nextToken();
                    if (messageType.equals("SEND")){
                        String to=stringTokenizer.nextToken();
                        if(emailMap.containsKey(to)) {
                            while (stringTokenizer.hasMoreTokens()) {
                                data += stringTokenizer.nextToken();
                            }
                            arrayList = emailMap.get(loginName);
                            arrayList.add(new Email(loginName, to, data));
                            arrayList = emailMap.get(to);
                            arrayList.add(new Email(loginName, to, data));
                            dataOutputStream.writeUTF("SEND DONE");
                        }
                        else{
                            dataOutputStream.writeUTF("SEND ERROR");
                        }
                    }
                    else if(messageType.equals("GET")){
                        System.out.println("GET CALLED ");
                        String sendEmailNames=new String();
                        arrayList=emailMap.get(loginName);
                        for (int i = 0; i <arrayList.size() ; i++) {
                            email=arrayList.get(i);
                            if(email.receiver.equals(loginName)){
                                sendEmailNames+=email.sender+" ";
                            }
                        }
                        dataOutputStream.writeUTF("GET "+sendEmailNames);
                    }
                    else if(messageType.equals("GETSENT")){
                        String receiverEmailNames=new String();
                        arrayList=emailMap.get(loginName);
                        for (int i = 0; i <arrayList.size() ; i++) {
                            email=arrayList.get(i);
                            if(email.sender.equals(loginName)){
                                receiverEmailNames+=email.receiver+" ";
                            }
                        }
                        dataOutputStream.writeUTF("GETSENT "+receiverEmailNames);
                    }

                    else if(messageType.equals("VIEW")){
                        String sender=stringTokenizer.nextToken();
                        String sendData=new String();
                        arrayList=emailMap.get(loginName);
                        if(emailMap.containsKey(sender)) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                email = arrayList.get(i);
                                if (email.receiver.equals(loginName) && email.sender.equals(sender)) {
                                    sendData += email.data + " END ";
                                }
                            }
                            dataOutputStream.writeUTF("VIEW " + sendData);
                        }
                        else{
                            dataOutputStream.writeUTF("VIEW " + "NOTHING");
                        }
                    }
                    else if(messageType.equals("VIEWSENT")){
                        String receiver=stringTokenizer.nextToken();
                        String sendData=new String();
                        arrayList=emailMap.get(loginName);
                        if(emailMap.containsKey(receiver)) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                email = arrayList.get(i);
                                if (email.sender.equals(loginName) && email.receiver.equals(receiver)) {
                                    sendData += email.data + " END ";
                                }
                            }
                            dataOutputStream.writeUTF("VIEWSENT " + sendData);
                        }
                        else{
                            dataOutputStream.writeUTF("VIEWSENT " + "NOTHING");
                        }
                    }
                }
            }
            catch(IOException e){
            e.printStackTrace();
        }
    }
    }
}


}


