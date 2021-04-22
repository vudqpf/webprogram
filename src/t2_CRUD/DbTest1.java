package t2_CRUD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbTest1 {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	String sql = "";
	
	public DbTest1() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/works";
			String user = "green";
			String password = "1234";
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 검색 실패!!");
		} catch (Exception e) {
			System.out.println("데이터베이스 연동 실패!!");			
		}
	}
	
	public void dbClose() {
		try {
			if(conn != null) conn.close();
		} catch (Exception e) {}
	}
	
	//개별자료 검색
	public void dbSearch() {
		try {
			stmt = conn.createStatement();
//			stmt.executeQuery("select * from jusorok where name='홍길동'");
			sql = "select * from test1 where name='홍길동'";
			rs = stmt.executeQuery(sql);
			System.out.println("SQL문 성공");
			System.out.println("=================");
			
			//검색한 자료 조회유무
			if(rs.next()) {	//'홍길동'을 찾음
				//1건의 자료라도 있다면 수행
				int idx = rs.getInt("idx");
				String mid = rs.getString("mid");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				
				//검색자료 출력
				System.out.println("일련번호 : " + idx);
				System.out.println("아이디 : " + mid);
				System.out.println("비번 : " + pwd);
				System.out.println("성명 : " + name);
				System.out.println("나이 : " + age);
			} else {
				System.out.println("검색한 자료가 없습니다.");
			}
		} catch (SQLException e) {
			System.out.println("SQL 오류" + e.getMessage());
//			e.printStackTrace();
		}
	}
}
