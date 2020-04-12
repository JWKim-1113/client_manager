import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Customer {
    /*public Customer(String nameText, String ageText, String phoneText, String birthDayText, String genderText, String noteText) {
    }*/

//    public static void main(String[] args) {
//        createTable();
//        createCustomer("danny","01039227048","male","23","note...");
//        ArrayList<String> list = getCustomers();
//        for(String item:list){
//            System.out.println(item);
//        }
//        createCustomer("peter","01039227228","Female","26","note!!!!!!!!!!!!!!!!");
//        list = getCustomers();
//        for(String item:list){
//            System.out.println(item);
//        }
//    }

    public static String[][] getCustomers(){
        try{
            Connection con = getConnection();
            //select 찾을것, from 테이블이름
            PreparedStatement statement = con.prepareStatement("SELECT id,username,password, name, phone, gender, age, note FROM customer");
            ResultSet results = statement.executeQuery();            
            ArrayList<String[]> list = new ArrayList<String[]>();
            while(results.next()) {
                //1차배열의 string값
                list.add(new String[]{
                        results.getString("id"),
                        results.getString("username"),
                        results.getString("password"),
                        results.getString("name"),
                        results.getString("phone"),
                        results.getString("gender"),
                        results.getString("age"),
                        results.getString("note")
                });
            }
            System.out.println("The data has been fetched");
            //리스트를 2차어레이로 변환해야하는데 얼마나많은데이터인지 모르기때문에 행은 list.size() / 열은 name phone gender age note  5개이므로 5
            String[][] arr = new String[list.size()][8];
            return list.toArray(arr);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static Integer LoginCustomer(String Username, String password,int loginResult) {
    	try {
    		
    		Connection con = getConnection();
    		PreparedStatement find = con.prepareStatement(""
    				+"SELECT * FROM customer WHERE username =?"
    				);
    		find.setString(1, Username);
    		//결과를 담을 results 생성
    		ResultSet results = find.executeQuery();
    		System.out.println(Username+ "비밀번호 :  "+password);
    		if(results.next()==false || Username.isEmpty()==true) {
    			System.out.println("실패");
    			   		loginResult= 2;//id 존재 x 
    		}else {
    			find.getConnection().prepareStatement(""
    					+"SELECT * FROM (SELECT * FROM customer WHERE username =? )"
    					);
    			find.setString(1, Username);
    			results=find.executeQuery();
    			while(results.next() == true) {
    				if(results.getString("password").equals(password) && results.getString("isfirst").equals("0")) {
    					//로그인 성공
    					System.out.println("최초 로그인 성공");
    					loginResult=0;
    				}else if(results.getString("password").equals(password) && results.getString("isfirst").equals("1")) {
    					//로그인 실패 
    					System.out.println("로그인 성공");
    					loginResult=1;
    				}else {
    					System.out.println("로그인 실패");
    					loginResult=2;
    				}
    			}
    				
    		}
    		
    	}catch(Exception e ) {
    		System.out.println(e.getMessage());
    
    	}
		
    	return loginResult;
    }
    
    public static Boolean createCustomer(String Username,String password  ){
        try{
            Connection con = getConnection();
            PreparedStatement select = con.prepareStatement(""
            		+ " SELECT COUNT(username)"
            		+ " FROM customer "
            		+ " WHERE"
            		+ " username = '"+Username+"'"
            		);
            
            PreparedStatement insert = con.prepareStatement(""
                    + "INSERT INTO customer"
            		+ "(username,password)"
//                    + "(username,password,name, phone, gender, age, note)"
                    + "VALUE "
                    + "('"+Username+"','"+password+"')"
//                    + "('"+username+"','"+password+"','"+name+"','"+phone+"','"+gender+"','"+age+"','"+note+"')"
            );
            ResultSet rs =select.executeQuery();
            if(rs.next()) {
            	int num =rs.getInt(1);
            	if(num==0) {
            insert.executeUpdate();
            System.out.println("The data has been saved!");
            JOptionPane.showMessageDialog(null,"Your data has been saved successfully");
            return true;
            }else {
            	JOptionPane.showMessageDialog(null, "중복입니다.","알림",JOptionPane.ERROR_MESSAGE);
            	
            	return false;
            	}
            }
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage());
            return false;
        }
        return false;
    }

    //테이블생성 QUERRY
/*    public static void createTable(){
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
    }*/
    //테이블생성 QUERRY(UNIQUE INDEX 추가)
    public static void createTable(){
        try{
            Connection con = getConnection();
            PreparedStatement createTable = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS "+
                        "customer(id int NOT NULL AUTO_INCREMENT,"+
                		"username varChar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,"+    //대소문자 구분
                        "password varChar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,"+		//대소문자 구분
                        "name varChar(255),"+
                        "phone varChar(255),"+
                        "gender varChar(255),"+
                        "age varChar(255),"+
                        "note varChar(255),"+
                        "isfirst tinyint(1) DEFAULT 0 NOT NULL,"+  // flag 변수로 최초 로그인 검사 
                        "PRIMARY KEY(id),"+
                        "UNIQUE INDEX(username),"+
                        "UNIQUE INDEX(phone))"
            );
            createTable.execute();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            System.out.println("Table successfully created");
        }
    }

    //테이블제거함수
    
    public static void deleteTable() {
       try {
          Connection con = getConnection();
          PreparedStatement deleteTable = con.prepareStatement(
                "DROP TABLE customer "
                );
          deleteTable.executeUpdate();

       }catch(Exception e) {
          System.out.println(e.getMessage());
       }finally {
          System.out.println("Table successfully deleted");
       }
    }

    public static void deleteCustomer(Object deleteId,Object deleteUsername){
        try {
            Connection con = getConnection();
            PreparedStatement deleteCustomer = con.prepareStatement(
                    "DELETE FROM customer WHERE username=? AND id=?"
            );
            deleteCustomer.setString(1,(String) deleteUsername);
            deleteCustomer.setString(2,(String) deleteId);
            deleteCustomer.executeUpdate();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally{
            System.out.println("The data has been deleted");
        }
    }
    	//고객 Update 함수 
    public Boolean updateCustomer(Object userName, String updateName, String updatePhone, String updateGender, String updateAge, String updateNote,String firstCheck) {
    	try {
    		Connection con = getConnection();
    		PreparedStatement updateCustomer =con.prepareStatement(
    				"UPDATE customer SET "
    				+ "name =? ,"
    				+ "phone =? ,"
    				+ "gender =? ,"
    				+ "age =? ,"
    				+ "note =? ,"
    				+ "isfirst =? "
    				+ "WHERE username=?"
    				);
    		updateCustomer.setString(1, updateName);
    		updateCustomer.setString(2, updatePhone);
    		updateCustomer.setString(3, updateGender);
    		updateCustomer.setString(4, updateAge);
    		updateCustomer.setString(5, updateNote);
    		updateCustomer.setString(6, firstCheck);
    		updateCustomer.setString(7, (String)userName);
    		updateCustomer.executeUpdate();
    		System.out.println("The data has been updated!");
            JOptionPane.showMessageDialog(null,"Your data has been updated successfully");
            return true;
    	}catch(Exception e ) {
    		System.out.println(e.getMessage());
    		JOptionPane.showMessageDialog(null,e.getMessage());
    		return false;
    	}finally {
    		
    	}
		
	}

    public static Connection getConnection(){
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
//            String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12332275";
//            String user = "sql12332275";
            String url = "jdbc:mysql://localhost:3306/client_app?serverTimezone=UTC";
            String user = "root";
            String pass = "qwer1234";
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