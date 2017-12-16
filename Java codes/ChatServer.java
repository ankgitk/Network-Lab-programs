
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ChatServer {
    public static final int PORT=8085;
  static  HashMap<String,Socket> map=new HashMap<String,Socket>();
    public static void main(String args[]){

        try {
            ServerSocket serverSocket=new ServerSocket(PORT);
            while (true){
                Socket socket=serverSocket.accept();
                ClientThread clientThread=new ClientThread(socket);
                clientThread.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    static class ClientThread extends Thread {

        Socket socket;
        String loginName;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        ClientThread(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                loginName=dataInputStream.readUTF();
                map.put(loginName,socket);
                System.out.println(loginName+" joined the ChatRoom ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("Thread Server started running");
            try {
                while(true){
                if (dataInputStream.available() > 0) {

                    String messagefromClient = dataInputStream.readUTF();
                    StringTokenizer stringTokenizer = new StringTokenizer(messagefromClient);
                    String messageType = stringTokenizer.nextToken();
                    String displayMessage = "";
                    if (messageType.equals("BROADCAST")) {
                        while (stringTokenizer.hasMoreTokens()) {
                            displayMessage += stringTokenizer.nextToken();
                        }
                        for (String key : map.keySet()) {
                            if (map.get(key) != null) {
                                DataOutputStream dos = new DataOutputStream(map.get(key).getOutputStream());
                                dos.writeUTF(loginName + " says: " + displayMessage);
                            }
                        }
                    } else {

                        String sendTo = stringTokenizer.nextToken();
                        while (stringTokenizer.hasMoreTokens()) {
                            displayMessage += stringTokenizer.nextToken();
                        }
                        if (map.containsKey(sendTo.trim())) {
                            Socket sendToSocket = map.get(sendTo.trim());
                            DataOutputStream sendToDataOutputStream = new DataOutputStream(sendToSocket.getOutputStream());
                            sendToDataOutputStream.writeUTF(loginName + " says: " + displayMessage);
                        }
                    }
                }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
