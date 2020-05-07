import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Board_list {
	// Board에 대한 SQL 구문 작성! 
	public static String[][] getBoards(){
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT num,title,content,writer,hits,regdate FROM board");
			ResultSet results =statement.executeQuery();
			ArrayList<String[]> list = new ArrayList<String[]>();
            while(results.next()) {
                //1차배열의 string값
                list.add(new String[]{
                        results.getString("num"),
                        results.getString("title"),
                        results.getString("content"),
                        results.getString("writer"),
                        results.getString("hits"),
                        results.getString("regdate")
                });
            }
            String[][] arr = new String[list.size()][6];
            return list.toArray(arr);
		}catch(Exception e ) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static void createBoardTable() {
		
		try {
			Connection con =getConnection();
			PreparedStatement createBoardTable =con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS "+
						"board(num int UNSIGNED NOT NULL AUTO_INCREMENT,"+
						"title varChar(50),"+
						"content varChar(5000),"+
						"writer varChar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,"+
						"hits int UNSIGNED NOT NULL DEFAULT 0,"+
						"regdate DATETIME DEFAULT CURRENT_TIMESTAMP,"+
						"PRIMARY KEY(num))"
					);
			createBoardTable.execute();
		}catch(Exception e ) {
			System.out.println(e.getMessage());
		}
	}
	
	 public static void deleteBoardTable() {
	       try {
	          Connection con = getConnection();
	          PreparedStatement deleteBoardTable = con.prepareStatement(
	                "DROP TABLE board "
	                );
	          deleteBoardTable.executeUpdate();

	       }catch(Exception e) {
	          System.out.println(e.getMessage());
	       }finally {
	          System.out.println("Table successfully deleted");
	       }
	    }
	
	 public static void createBoard(String title, String txtArea,String username) {
		 try {
			 Connection con =getConnection();
			 
			 PreparedStatement insert = con.prepareStatement(""
					 +"INSERT INTO board"
					 + "(title,content,writer)"
					 + " VALUE "
					 + "('"+title+"','"+txtArea+"','"+username+"')"
					 );
			 insert.executeUpdate();
		 }catch(Exception e ) {
			 System.out.println(e.getMessage());
		 }
	 }
	
	public static Connection getConnection() {
		 try{
	            String driver = "com.mysql.cj.jdbc.Driver";
//	            String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12332275";
//	            String user = "sql12332275";
	            String url = "jdbc:mysql://localhost:3306/client_app?serverTimezone=UTC";
	            String user = "root";
	            String pass = "1234qwer";
	            Class.forName(driver);
	            Connection con = DriverManager.getConnection(url,user,pass);
	            System.out.println("The Connection Successful");
	            return con;
	        }catch (Exception e){
	            System.out.println(e.getMessage());
	            return null;
	        }
	}

	public static void deleteBoard(Object Boardid, Object Boardwriter) {
		try {
			Connection con = getConnection();
			PreparedStatement deleteBoard = con.prepareStatement(
					"DELETE FROM board WHERE num=? AND writer=?"
					);
			deleteBoard.setString(1,(String)Boardid);
			deleteBoard.setString(2, (String)Boardwriter);
			deleteBoard.executeUpdate();
			
		}catch(Exception e ) {
			System.out.println(e.getMessage());
		}finally {
			System.out.println("게시글 삭제 완료 ");
		}
	}



	public static void hitsBoard(Object num, Object writer) {
		try {
			Connection con =getConnection();
			PreparedStatement hitBoard= con.prepareStatement(
					"UPDATE board SET "
					+ "hits =hits + 1 "
					+ "WHERE num=? AND writer=?"
					);
			hitBoard.setString(1, (String)num);
			hitBoard.setString(2, (String)writer);
			hitBoard.executeUpdate();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	public static void updateBoard(Object id, String titles, String txtarea, Object writer) {
		try {
			Connection con =getConnection();
			PreparedStatement update =con.prepareStatement(
					"UPDATE board SET "
					+ "title =? ,"
					+ "content =? "
					+ "WHERE num=? AND writer=?"
					
					);
			update.setString(1, titles);
			update.setString(2, txtarea);
			update.setString(3, (String)id);
			update.setString(4, (String)writer);
			update.executeUpdate();
			
		}catch(Exception e ) {
			System.out.println(e.getMessage());
		}finally {
			System.out.println("게시글 수정 완료!!");
		}
		
	}

	
}
