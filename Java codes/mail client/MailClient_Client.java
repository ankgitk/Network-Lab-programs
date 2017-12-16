import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MailClient_Client {
    public static final int PORT=8085;

    public static void main(String args[]){
        String loginName;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1", PORT);
            System.out.println("BaseClient Connected to " + socket.getRemoteSocketAddress());
            System.out.println("Enter Your Login: ");
            dataInputStream=new DataInputStream(socket.getInputStream());
            dataOutputStream=new DataOutputStream(socket.getOutputStream());
            loginName=scanner.next();
            StringTokenizer stringTokenizer;
            String reciever,data,sender,receivedData;
            dataOutputStream.writeUTF(loginName);
            while (true){
                System.out.println("\nCHOOSE ANY OF THE OPTIONS:\n" +
                        "1.SEND\n" +
                        "2.GET MY EMAILS\n" +
                        "3. VIEW MY MAIL\n"+
                        "4. GET MY SENT MAILS\n" +
                        "5. VIEW MY SENT MAIL\n"
                );
                int option=scanner.nextInt();
                switch (option){
                    case 1:
                        System.out.println("Enter the receiver");
                        reciever=scanner.next();
                        System.out.println("Enter Data");
                        scanner.nextLine();
                        data=scanner.nextLine();
                        dataOutputStream.writeUTF("SEND "+reciever+" "+data);
                        break;
                    case 2:
                        dataOutputStream.writeUTF("GET");
                        break;
                    case 3:
                        System.out.println("Enter the sender");
                        sender=scanner.next();
                        dataOutputStream.writeUTF("VIEW "+sender);
                        break;
                    case 4:
                        dataOutputStream.writeUTF("GETSENT");
                        break;
                    case 5:
                        System.out.println("Enter the reciever");
                        reciever=scanner.next();
                        dataOutputStream.writeUTF("VIEWSENT "+reciever);
                        break;
                }
                if(option>=1&&option<=5){
                    while (dataInputStream.available()<=0);
                     receivedData=dataInputStream.readUTF();
                     stringTokenizer=new StringTokenizer(receivedData);
                    String messageType = stringTokenizer.nextToken();
                    if (messageType.equals("SEND")){
                        String mess=stringTokenizer.nextToken();
                        if(mess.equals("DONE"))
                            System.out.println("MAIL SENT SUCCESSFULLY");
                        else
                            System.out.println("RECIEVER DOESNT EXIST");
                    }
                    else if(messageType.equals("GET")){
                        System.out.println("========List of Emails========");
                        while (stringTokenizer.hasMoreTokens()){
                            System.out.println(stringTokenizer.nextToken());
                        }
                    }
                    else if(messageType.equals("GETSENT")){
                        System.out.println("========List of Sent Emails========");
                        while (stringTokenizer.hasMoreTokens()){
                            System.out.println(stringTokenizer.nextToken());
                        }
                    }
                    else if(messageType.equals("VIEW")){
                        while (stringTokenizer.hasMoreTokens()){
                            String d="",printdata="";
                            while (stringTokenizer.hasMoreTokens()){
                                d=stringTokenizer.nextToken();
                                if(d.equals("END"))
                                    break;
                                printdata=printdata+d+" ";
                            }
                            System.out.println(printdata+"\n=======================");
                        }
                    }
                    else if(messageType.equals("VIEWSENT")){
                        while (stringTokenizer.hasMoreTokens()){
                            String d="",printdata="";
                            while (stringTokenizer.hasMoreTokens()){
                                d=stringTokenizer.nextToken();
                                if(d.equals("END"))
                                    break;
                                printdata=printdata+d+" ";
                            }
                            System.out.println(printdata+"\n=======================");
                        }
                    }
                }

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
