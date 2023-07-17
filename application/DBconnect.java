package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnect {
	
	public Connection conn;
	
	public Connection getConnection() {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "cafe3"; // DB 만들때 설정한 id
		String pw = "1234"; // DB 만들때 설정한 pw
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
			System.out.println("디비 접속 성공 - 20230516");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("디비 접속 실패");
		}
		
		return conn;
		
	} // DB 접속하기
	
}
