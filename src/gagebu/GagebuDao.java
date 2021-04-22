package gagebu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GagebuDao {
	public Connection conn = null;
	public PreparedStatement pstmt = null;
	public ResultSet rs = null;

	String sql = "";
	GagebuVo vo = null;
	
	// 생성자를 통한 데이터베이스 연결
	public GagebuDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/works";
			String user = "green";
			String password = "1234";
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("드라이버 검색 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 검색 실패!!!");
		} catch (Exception e) {
			System.out.println("데이터베이스 연동실패!!");
		}
	}
	
	// 데이터베이스 Close(Connection객체 Close)
	public void dbClose() {
		if(conn != null)
			try {
				conn.close();
			} catch (Exception e) {}
	}
	
	// PreparedStatement객체 Close
	public void pstmtClose() {
		if(pstmt != null)
			try {
				pstmt.close();
			} catch (Exception e) {}
	}
	
	// ResultSet객체 Close
	public void rsClose() {
		if(rs != null)
			try {
				rs.close();
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
	}

	//가계부 입력처리
	public void gInput(GagebuVo vo) {
		int balance;
		try {
			// 기존의 잔고를 읽어온다.
			sql = "select balance from gagebu order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) balance = rs.getInt("balance");
			else balance = 0;
			if(pstmt != null) pstmt.close();
			
			// '수입/지출'인지를 판별하여 잔액을 계산한다.
			if(vo.getgCode().equals("+")) balance += vo.getPrice();
			else balance -= vo.getPrice();
			
			// 입력된 자료를 가계부테이블에 등록한다.
			sql = "insert into gagebu values (default,default,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getgCode());
			pstmt.setInt(2, vo.getPrice());
			pstmt.setString(3, vo.getContent());
			pstmt.setInt(4, balance);
			pstmt.executeUpdate();
			System.out.println("자료가 입력되었습니다.");
		} catch (Exception e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		
	}

	//전체조회
	public ArrayList<GagebuVo> gList() {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();		
		try {
			sql = "select * from gagebu order by idx desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				vo = new GagebuVo();
				
				vo.setIdx(rs.getInt("idx"));
				vo.setWdate(rs.getString("wdate"));
				vo.setgCode(rs.getString("gCode"));
				vo.setPrice(rs.getInt("price"));
				vo.setContent(rs.getString("content"));
				vo.setBalance(rs.getInt("balance"));
				
				vos.add(vo);
			}
		} catch (Exception e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		}finally {
			rsClose();
		}
		
		return vos;
	}

	//날짜조회
	public ArrayList<GagebuVo> gSearch(String wdate) {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		try {
			//전체조회
			if(wdate.equals("list")) {
				sql = "select * from gagebu order by idx desc";
				pstmt = conn.prepareStatement(sql);
			} else {
			//sql의 substr함수(변수, 시작위치, 꺼낼갯수)
				sql = "select * from gagebu where replace(substr(wdate,1,10),'-','')=? order by idx desc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, wdate);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				vo = new GagebuVo();
				
				vo.setIdx(rs.getInt("idx"));
				vo.setWdate(rs.getString("wdate"));
				vo.setgCode(rs.getString("gCode"));
				vo.setPrice(rs.getInt("price"));
				vo.setContent(rs.getString("content"));
				vo.setBalance(rs.getInt("balance"));
				
				vos.add(vo);
			}
		} catch(Exception e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		}finally {
			rsClose();
		}
		return vos;
	}

	//삭제메소드
	public void gDelete(int idx) {
//		System.out.println("vo : "+vo);
		int balance=0, price=0;
		String gCode="";
		try {
			//고유번호(idx)에 해당하는 gCode와 price를 구해온다.
			sql = "select gCode, price from gagebu where idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				gCode = rs.getString("gCode");
				price = rs.getInt("price");	
			}
			if(pstmt != null) pstmt.close();
			
			// 기존의 잔고를 읽어온다.
			sql = "select idx,balance from gagebu order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				balance = rs.getInt("balance");	
			}
			if(pstmt != null) pstmt.close();
			
			if(gCode.equals("+")) balance -= price;
			else balance += price;
			
			//삭제
			sql = "delete from gagebu where idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.executeUpdate();
			pstmtClose();
			
			//가장 최근 열 가져오기
			sql = "select idx from gagebu order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int imshiIdx=0;
			if(rs.next()) {
				imshiIdx = rs.getInt("idx");
			}
			if(pstmt != null) pstmt.close();
			
			
			sql = "update gagebu set balance=? where idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,balance);
			pstmt.setInt(2, imshiIdx);
			pstmt.executeUpdate();
			pstmtClose();
			
			System.out.println("자료가 삭제 처리 되었습니다.");
		} catch (Exception e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		}finally {
			pstmtClose();
		}
	}
	
}
