import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Customer {
    /*public Customer(String nameText, String ageText, String phoneText, String birthDayText, String genderText, String noteText) {
    }*/

   /* public static void main(String[] args) {
        createTable();
        createCustomer("danny","01039227048","male","23","note...");
        ArrayList<String> list = getCustomers();
        for(String item:list){
            System.out.println(item);
        }
        createCustomer("peter","01039227228","Female","26","note!!!!!!!!!!!!!!!!");
        list = getCustomers();
        for(String item:list){
            System.out.println(item);
        }
    }*/

    public static String[][] getCustomers(){
        try{
            Connection con = getConnection();
            //select 찾을것, from 테이블이름
            PreparedStatement statement = con.prepareStatement("SELECT id, name, phone, gender, age, note FROM customer");
            ResultSet results = statement.executeQuery();
            ArrayList<String[]> list = new ArrayList<String[]>();
            while(results.next()) {
                //1차배열의 string값
                list.add(new String[]{
                        results.getString("id"),
                        results.getString("name"),
                        results.getString("phone"),
                        results.getString("gender"),
                        results.getString("age"),
                        results.getString("note")
                });
            }
            System.out.println("The data has been fetched");
            //리스트를 2차어레이로 변환해야하는데 얼마나많은데이터인지 모르기때문에 행은 list.size() / 열은 name phone gender age note  5개이므로 5
            String[][] arr = new String[list.size()][6];
            return list.toArray(arr);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static void createCustomer(String name,String phone,String gender,String age,String note){
        try{
            Connection con = getConnection();
            PreparedStatement insert = con.prepareStatement(""
                    + "INSERT INTO customer"
                    + "(name, phone, gender, age, note)"
                    + "VALUE "
                    + "('"+name+"','"+phone+"','"+gender+"','"+age+"','"+note+"')"
            );
            insert.executeUpdate();
            System.out.println("The data has been saved!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void createTable(){
        try{
            Connection con = getConnection();
            PreparedStatement createTable = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS "+
                        "customer(id int NOT NULL AUTO_INCREMENT,"+
                        "name varChar(255),"+
                        "phone varChar(255),"+
                        "gender varChar(255),"+
                        "age varChar(255),"+
                        "note varChar(255),"+
                        "PRIMARY KEY(id))"
            );
            createTable.execute();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            System.out.println("Table successfully created");
        }
    }
    public static void deleteCustomer(){
        try {
            Connection con = getConnection();
            PreparedStatement deleteCustomer = con.prepareStatement(
                    "DELETE FROM TABLE WHERE " +
                            "customer(1) = '1'"
            );
            deleteCustomer.execute();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally{
            System.out.println("The data has been deleted");
        }
    }

    public static Connection getConnection(){
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12329367";
            String user = "sql12329367";
            String pass = "DY6wUJICry";
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url,user,pass);
            System.out.println("The Connection Successful");
            return con;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
