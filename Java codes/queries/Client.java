import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {


    public static void main(String args[]){
        int port=8085;
        try {
            Scanner scn=new Scanner(System.in);
            Socket socket=new Socket("127.0.0.1",port);
            System.out.println("BaseClient Connected to "+socket.getRemoteSocketAddress());
            String key="",clientText="";

            InputStream ios= socket.getInputStream();
            DataInputStream dataInputStream=new DataInputStream(ios);

            DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
            MainThread mainThread=new MainThread(socket);
            ReceiveDisplayMessages receiveDisplayMessages=new ReceiveDisplayMessages(socket);
            mainThread.start();
            receiveDisplayMessages.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static class MainThread extends Thread{
        Scanner scanner;
        Socket socket;
        DataOutputStream dataOutputStream;
        MainThread(Socket socket){
            scanner=new Scanner(System.in);
            this.socket=socket;
            try {
                dataOutputStream=new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            super.run();int x;
            String id,wherecolumnName,wherecolumnValue,columnName,columnValue,query;
            while (true){
                System.out.println("Choose Among the following options:");
                System.out.println("1. SELECT BY ID");
                System.out.println("2. SELECT BY COLUMN NAME AND VALUE");
                System.out.println("3. UPDATE");
                System.out.println("4. DELETE BY ID");
                System.out.println("5. DELETE");
                System.out.println("6. INSERT");
                System.out.println("7. SELECT ALL");
                x=scanner.nextInt();
                try {
                    switch (x) {
                        case 1:
                            System.out.println("ENTER ID");
                            id = scanner.next();
                            query = "SELECTID" + " " + id;
                            dataOutputStream.writeUTF(query);
                            break;

                        case 2:
                            System.out.println("ENTER WHERE COLUMN");
                            wherecolumnName = scanner.next();
                            System.out.println("ENTER WHERE COLUMN VALUE");
                            wherecolumnValue = scanner.next();
                            query = "SELECT" + " " + wherecolumnName + " " + wherecolumnValue;
                            dataOutputStream.writeUTF(query);
                            break;
                        case 3:
                            System.out.println("ENTER COLUMN NAME");
                            columnName = scanner.next();
                            System.out.println("ENTER COLUMN VALUE");
                            columnValue = scanner.next();
                            System.out.println("ENTER WHERE COLUMN NAME");
                            wherecolumnName = scanner.next();
                            System.out.println("ENTER WHERE COLUMN VALUE");
                            wherecolumnValue = scanner.next();
                            query = "UPDATE" + " " + columnName + " " + columnValue + " " + wherecolumnName + " " + wherecolumnValue;
                            dataOutputStream.writeUTF(query);
                            break;
                        case 4:
                            System.out.println("ENTER ID");
                            id = scanner.next();
                            query = "DELETEID" + " " + id;
                            dataOutputStream.writeUTF(query);
                            break;
                        case 5:
                            System.out.println("ENTER WHERE COLUMN");
                            wherecolumnName = scanner.next();
                            System.out.println("ENTER WHERE COLUMN VALUE");
                            wherecolumnValue = scanner.next();
                            query = "DELETE" + " " + wherecolumnName + " " + wherecolumnValue;
                            dataOutputStream.writeUTF(query);
                            break;
                        case 6:

                            System.out.println("ENTER ID");
                            id = scanner.next();

                            System.out.println("ENTER Name");
                            String name = scanner.next();

                            System.out.println("ENTER EMAIL");
                            String email = scanner.next();

                            System.out.println("ENTER SEX");
                            String sex = scanner.next();
                            System.out.println("ENTER AGE");
                            String age = scanner.next();
                            query="INSERT"+" "+id+" "+name+" "+email+" "+sex+" "+age;
                            dataOutputStream.writeUTF(query);
                            break;
                        case 7:
                            query="SELECTALL";
                            dataOutputStream.writeUTF(query);
                            break;

                    }
                }
                catch (IOException e){
                    System.out.println(e.getMessage());
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class ReceiveDisplayMessages extends Thread{

        Socket socket;
        DataInputStream dataInputStream;
        ReceiveDisplayMessages(Socket socket){
            this.socket=socket;
            try {
                dataInputStream=new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    if(dataInputStream!=null&&dataInputStream.available()>0){
                        String displayMessage=dataInputStream.readUTF();
                        System.out.println(displayMessage);
                    }
//                    System.out.println("Sleep Starts");
                    Thread.sleep(2000);
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
