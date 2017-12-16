

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static final int PORT=8085;
    static DataOutputStream dataOutputStream;
    static DataInputStream dataInputStream;
    public static void main(String args[]){
        Scanner scn=new Scanner(System.in);
        try {
            Socket socket = new Socket("127.0.0.1", PORT);
            dataInputStream=new DataInputStream(socket.getInputStream());
            dataOutputStream=new DataOutputStream(socket.getOutputStream());
            System.out.println("BaseClient Connected to " + socket.getRemoteSocketAddress());
            MainThread mainThread=new MainThread(socket);
            ClientGetMessagesThread clientGetMessagesThread=new ClientGetMessagesThread(socket);

            int type;String message,login;
            System.out.println("Enter YOUR LOGIN ID");
            login=scn.next();
            dataOutputStream.writeUTF(login);
            mainThread.start();
            clientGetMessagesThread.start();


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class MainThread extends Thread{
        Socket socket;int type;String message,login;
        DataOutputStream dataOutputStream;
        MainThread(Socket socket){
            this.socket=socket;
            try {
                dataOutputStream=new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            while(true){
                Scanner scn=new Scanner(System.in);
                System.out.println("TYPE OF MESSAGE:\n1.BROADCAST\n2.TO CLIENT");
                type=Integer.parseInt(scn.nextLine());
                if(type==1){
                    System.out.println("ENTER YOUR MESSAGE");
                    message=scn.nextLine();
                    try {
                        dataOutputStream.writeUTF("BROADCAST "+message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(type==2){
                    System.out.println("ENTER RECEIVER LOGIN ID");
                    login=scn.nextLine();
                    System.out.println("ENTER YOUR MESSAGE");
                    message=scn.nextLine();
                    try {
                        dataOutputStream.writeUTF("CLIENT "+login+" "+message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
            }
        }
    }

   static class ClientGetMessagesThread extends Thread{
        Socket clientsocket;
        DataInputStream dataInputStream;
        ClientGetMessagesThread(Socket socket) throws IOException {
            this.clientsocket=socket;
            dataInputStream =new DataInputStream(clientsocket.getInputStream());
        }

        @Override
        public void run() {
            System.out.println("Thread Client started running");
            while (true){
                try {
                    if(dataInputStream!=null&&dataInputStream.available()>0){
                        String displayMessage=dataInputStream.readUTF();
                        System.out.println(displayMessage);
                    }
//                    System.out.println("Sleep Starts");
                    Thread.sleep(4000);
//                    System.out.println("Sleep Ends");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
            }
        }
    }
}
