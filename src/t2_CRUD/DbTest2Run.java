package t2_CRUD;

public class DbTest2Run {
	public static void main(String[] args) {
		//DB연결(드라이버검색성공 + DB연동성공)
		DbTest2 dbTest1 = new DbTest2();	
		
		//DB에서의 Table작업(ex: test1테이블에서 '홍길동'검색)
		dbTest1.dbSearch();
		
		//DB Close;
		dbTest1.dbClose();
	}
}
