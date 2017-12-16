package JavaSqlRemoteConnection;

import java.sql.*;
import java.util.ArrayList;

public class SqlQueriesDemo {

public static final String DATABASE_URL="jdbc:mysql://localhost:3306/sanchitdatabase";
public static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
public static final String DATABASE_NAME="sanchitdatabase";

public static final String USERNAME="root";
public static final String PASS="root";

public static void main(String args[]){
    Connection connection;
    Statement statement;
    try {
        //Registering jdbc driver
        Class.forName(JDBC_DRIVER);
        // open a connection
        connection= DriverManager.getConnection(DATABASE_URL,USERNAME,PASS);
        // create database;
        statement=connection.createStatement();



//        String query="CREATE DATABASE "+DATABASE_NAME+";";
//        String query="CREATE TABLE EMP(NAME VARCHAR(10),ROLL INTEGER(10));";

//        statement.executeUpdate("Insert Into EMP values('sanchit',12);");
//       ResultSet resultSet= statement.executeQuery("Select * from emp");
//
//        while (resultSet!=null&&resultSet.next()){
//            int roll=resultSet.getInt("ROLL");
//            String name=resultSet.getString("NAME");
//            System.out.println("ROll: " + roll);
//            System.out.println("NAme: " + name);
//        }
      boolean done=  insertData(statement,1,"Sanchit",1000000);
      if(done)
          System.out.println("Data Inserted");
      else
          System.out.println("Data Insertion Failed");
        done= insertData(statement,1,"Sanchit",1000000);
        if(done)
            System.out.println("Data Inserted");
        else
            System.out.println("Data Insertion Failed");
        done= insertData(statement,2,"Muskan",1000000);
        if(done)
            System.out.println("Data Inserted");
        else
            System.out.println("Data Insertion Failed");

        done= insertData(statement,3,"Nish",1000000);
        if(done)
            System.out.println("Data Inserted");
        else
            System.out.println("Data Insertion Failed");

        ArrayList<RowData> arrayList=new ArrayList<>();
        arrayList=getDataby(statement);
        for(RowData rowData:arrayList){
            System.out.println("ID: "+rowData.ID);
            System.out.println("NAME:  "+rowData.NAME);
            System.out.println("SALARY: "+rowData.SALARY);
            System.out.println("-----------------------------------------------");
        }
        System.out.println("----------------GET DATA BY ID-----------------");
        arrayList=getDatabyID(statement,2);
        for(RowData rowData:arrayList){
            System.out.println("ID: "+rowData.ID);
            System.out.println("NAME:  "+rowData.NAME);
            System.out.println("SALARY: "+rowData.SALARY);
            System.out.println("-----------------------------------------------");
        }
         done=updateData(statement,1,"sanchit",10000001);
        if(done)
            System.out.println("Data Updated");
        else
            System.out.println("Data updation Failed");
        done=deleteData(statement,1);
        if(done)
            System.out.println("Data DELETED");
        else
            System.out.println("Data deletion Failed");
        if(statement!=null&&connection!=null)
            connection.close();


    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }

}
public static boolean insertData(Statement statement, int id,String Name,int salary){
    try {
       ResultSet rs= statement.executeQuery("SELECT COUNT(*) AS rowcount from emp where id="+id+";");

       if(rs!=null&&rs.next()&&rs.getInt("rowcount")==0){
           String query="Insert into emp values("+id+","+"'"+Name+"',"+salary+");";
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
public static boolean updateData(Statement statement,int id,String Name, int salary){
    try {
        ResultSet rs= statement.executeQuery("SELECT COUNT(*) AS rowcount from emp where id="+id+";");
        if(rs!=null&&rs.next()&&rs.getInt("rowcount")>0){
            String query="Update emp set name='"+Name+"',salary="+salary+" where id="+id+";";
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
    public static boolean deleteData(Statement statement,int id){
        try {
            ResultSet rs= statement.executeQuery("SELECT COUNT(*) AS rowcount from emp where id="+id+";");
            if(rs!=null&&rs.next()&&rs.getInt("rowcount")>0){
                String query="delete from emp where id="+id+";";
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

    public static ArrayList<RowData> getDatabyID(Statement statement, int id){
        ArrayList<RowData> data=new ArrayList<>();
        try {
            ResultSet resultSet= statement.executeQuery("SELECT * from emp where id="+id);
            while (resultSet!=null&&resultSet.next()){
                int salary=resultSet.getInt("salary");
                String name=resultSet.getString("name");
                data.add(new RowData(id,name,salary));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static ArrayList<RowData> getDataby(Statement statement){
        ArrayList<RowData> data=new ArrayList<>();
        try {
            ResultSet resultSet= statement.executeQuery("SELECT * from emp;");
            while (resultSet!=null&&resultSet.next()){
                int salary=resultSet.getInt("salary");
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                data.add(new RowData(id,name,salary));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
static class RowData{
        int ID;
        String NAME;
        int SALARY;

    public RowData(int ID, String NAME, int SALARY) {
        this.ID = ID;
        this.NAME = NAME;
        this.SALARY = SALARY;
    }
}

}
