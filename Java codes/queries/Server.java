import javax.print.attribute.standard.MediaSize;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Server {

    public static final String DATABASE_URL="jdbc:mysql://localhost:3306/sanchitdatabase";
    public static final String JDBC_DRIVER="com.mysql.jdbc.Driver";

    public static final String USERNAME="root";
    public static final String PASS="root";

    public static void main(String args[]){
        Connection connection;
        Statement statement;
        try {
            //Registering jdbc driver
            Class.forName(JDBC_DRIVER);
            // open a connection
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASS);
            // create database;
            statement = connection.createStatement();
        
        int port=8085;
        
            Scanner scn=new Scanner(System.in);
            ServerSocket serverSocket=new ServerSocket(port);
//            serverSocket.setSoTimeout(10000);
            // This stops

            while (true){

                System.out.println("Server Says: Client Connected to Server at port "+serverSocket.getLocalPort());
                Socket socket=serverSocket.accept();
                System.out.println("Server Says: Client Connected "+socket.getLocalAddress());
                 
                ClientHandlingThread clientThread=new ClientHandlingThread(socket,statement);
                clientThread.start();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static class ClientHandlingThread extends Thread{
        Socket socket;
        Statement statement;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        String clientText="",key="";
        ClientHandlingThread(Socket socket,Statement statement){
            this.socket=socket;
            this.statement=statement;
            try {
                dataInputStream=new DataInputStream(socket.getInputStream());
                dataOutputStream=new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();

            String sendData=""; boolean done;
            ArrayList<RowData> arrayList=new ArrayList<>();
            while(true){
                try {
                    if(dataInputStream.available()>0) {
                        clientText = dataInputStream.readUTF();
                        StringTokenizer stringTokenizer= new StringTokenizer(clientText);
                        String input=stringTokenizer.nextToken();
                        while (stringTokenizer.hasMoreTokens()){
                            if(input.equals("SELECTID")){
                                String id=stringTokenizer.nextToken();
                                arrayList=getDatabyID(statement,id);
                                sendData=getRowDataToString(arrayList);
                                if(sendData.isEmpty())
                                    dataOutputStream.writeUTF("EMPTY OUTPUT");
                                else
                                    dataOutputStream.writeUTF(sendData);
                            }
                            else if(input.equals("SELECT")){
                                String wherecolumnName=stringTokenizer.nextToken();
                                String wherecolumnValue=stringTokenizer.nextToken();
                                arrayList= getDatabyColumn(statement,wherecolumnName,wherecolumnValue);
                                sendData=getRowDataToString(arrayList);
                                if(sendData.isEmpty())
                                    dataOutputStream.writeUTF("EMPTY OUTPUT");
                                else
                                    dataOutputStream.writeUTF(sendData);
                            }
                            else if(input.equals("SELECTALL")){
                                arrayList=getData(statement);
                                sendData=getRowDataToString(arrayList);
                                if(sendData.isEmpty())
                                    dataOutputStream.writeUTF("EMPTY OUTPUT");
                                else
                                dataOutputStream.writeUTF(sendData);
                            }
                            else if(input.equals("UPDATE")){
    
                                String columnName=stringTokenizer.nextToken();
                                String columnValue=stringTokenizer.nextToken();
    
                                String wherecolumnName=stringTokenizer.nextToken();
                                String wherecolumnValue=stringTokenizer.nextToken();
                                done= updateDatabyColumn(statement,columnName,columnValue,wherecolumnName,wherecolumnValue);
                                if(done)
                                    dataOutputStream.writeUTF("UPDATE SUCCESSFUL");
                                else
                                    dataOutputStream.writeUTF("OOPS SOMETHING WAS WRONG");
                            }
                            else if(input.equals("DELETEID")){
                                String id=stringTokenizer.nextToken();
                                done=deleteDatabyID(statement,id);
                                if(done)
                                    dataOutputStream.writeUTF("DELETE SUCCESSFUL");
                                else
                                    dataOutputStream.writeUTF("OOPS SOMETHING WAS WRONG");


                            }
                            else if(input.equals("DELETE")){
    
                                String wherecolumnName=stringTokenizer.nextToken();
                                String wherecolumnValue=stringTokenizer.nextToken();
                                done=deleteData(statement,wherecolumnName,wherecolumnValue);
                                if(done)
                                    dataOutputStream.writeUTF("DELETE SUCCESSFUL");
                                else
                                    dataOutputStream.writeUTF("OOPS SOMETHING WAS WRONG");

                            }
                            else if(input.equals("INSERT")){
                                String id= (stringTokenizer.nextToken());
                                String Name=stringTokenizer.nextToken();
                                String email=stringTokenizer.nextToken();
                                String sex=stringTokenizer.nextToken();
                                int age= Integer.parseInt(stringTokenizer.nextToken());
                                done=insertData(statement,id, Name,email,sex,age);
                                if(done)
                                    dataOutputStream.writeUTF("INSERT SUCCESSFUL");
                                else
                                    dataOutputStream.writeUTF("OOPS SOMETHING WAS WRONG");
                            }
    
                        }
                    }
                    else{
    //                    System.out.println("Server:Client Didn't Write anything");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public static boolean insertData(Statement statement, String id,String Name,String email,String sex,int age){
        try {
            ResultSet rs= statement.executeQuery("SELECT COUNT(*) AS rowcount from employee where id="+id+";");
            id=modifyValue(id);
            if(rs!=null&&rs.next()&&rs.getInt("rowcount")==0){
                String query="Insert into employee values("+id+","+"'"+Name+"',"
                        +"'"+email+"',"
                        +"'"+sex+"',"
                        +age+");";
                System.out.println(query);
                statement.executeUpdate(query);


            }
            else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateData(Statement statement, int id,String Name,String email,String sex,int age){
        try {
            ResultSet rs= statement.executeQuery("SELECT COUNT(*) AS rowcount from employee where id="+id+";");
            if(rs!=null&&rs.next()&&rs.getInt("rowcount")>0){
                String query="Update employee set name='"+Name+"'"
                        +",email='"+email+"'"
                        +",sex='"+sex+"'"
                        +",age="+age+" where id="+id+";";
                System.out.println(query);
                statement.executeUpdate(query);


            }
            else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean updateDatabyColumn(Statement statement,String columnToChange,String columnValue,String whereColumn,String whereColumnValue){
        try {

            whereColumnValue=modifyValue(whereColumnValue);
            columnValue=modifyValue(columnValue);
            String query="Update employee set "+columnToChange+"="+columnValue+" where "+whereColumn+"="+whereColumnValue+";";
            System.out.println(query);
            statement.executeUpdate(query);



        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean deleteDatabyID(Statement statement,String id){
        try {
            ResultSet rs= statement.executeQuery("SELECT COUNT(*) AS rowcount from employee where id="+id+";");
            if(rs!=null&&rs.next()&&rs.getInt("rowcount")>0){
                String query="delete from employee where id="+id+";";
                System.out.println(query);
                statement.executeUpdate(query);


            }
            else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean deleteData(Statement statement,String whereColumn,String whereValue){
        try {

            whereValue=modifyValue(whereValue);
            String query="delete from employee where "+whereColumn+"="+whereValue+";";
            System.out.println(query);
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String getRowDataToString(ArrayList<RowData> arrayList){
        StringBuilder message=new StringBuilder();
        for (int i = 0; i <arrayList.size() ; i++) {
            message.append("ID: "+arrayList.get(i).ID+"\n");
            message.append("NAME: "+arrayList.get(i).NAME+"\n");
            message.append("EMAIL: "+arrayList.get(i).EMAIL+"\n");
            message.append("SEX: "+arrayList.get(i).SEX+"\n");
            message.append("AGE: "+arrayList.get(i).AGE+"\n");
        }

        return message.toString();

    }
    public static ArrayList<RowData> getDatabyID(Statement statement, String id){
        ArrayList<RowData> data=new ArrayList<>();
        try {
            ResultSet resultSet= statement.executeQuery("SELECT * from employee where id="+id);
            while (resultSet!=null&&resultSet.next()){
                int age=resultSet.getInt("age");
                int idid=resultSet.getInt("id");
                String sex=resultSet.getString("sex");
                String name=resultSet.getString("name");
                String email=resultSet.getString("email");
                data.add(new RowData(idid,name,email,sex,age));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static ArrayList<RowData> getDatabyColumn(Statement statement, String columnName, String value){
        ArrayList<RowData> data=new ArrayList<>();
        try {
            ResultSet resultSet=null;
            if(isInteger(value)){

                resultSet= statement.executeQuery("SELECT * from employee where "+columnName+"="+value);
            }
            else{
                resultSet= statement.executeQuery("SELECT * from employee where "+columnName+"="+"'"+value+"'");
            }
            while (resultSet!=null&&resultSet.next()){
                int age=resultSet.getInt("age");
                int id=resultSet.getInt("id");
                String sex=resultSet.getString("sex");
                String name=resultSet.getString("name");
                String email=resultSet.getString("email");
                data.add(new RowData(id,name,email,sex,age));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static ArrayList<RowData> getData(Statement statement){
        ArrayList<RowData> data=new ArrayList<>();
        try {
            ResultSet resultSet= statement.executeQuery("SELECT * from employee;");
            while (resultSet!=null&&resultSet.next()){
                int age=resultSet.getInt("age");
                int id=resultSet.getInt("id");
                String sex=resultSet.getString("sex");
                String name=resultSet.getString("name");
                String email=resultSet.getString("email");
                data.add(new RowData(id,name,email,sex,age));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    static class RowData{
        int ID;
        String NAME;
        String EMAIL;
        String SEX;
        int AGE;

        public RowData(int ID, String NAME, String EMAIL, String SEX, int AGE) {
            this.ID = ID;
            this.NAME = NAME;
            this.EMAIL = EMAIL;
            this.SEX = SEX;
            this.AGE = AGE;
        }
    }
    public static boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
    public static String modifyValue(String value){
        if(isInteger(value)){
            return value;
        }
        else{
            value="'"+value+"'";
        }
        return value;
    }
}



