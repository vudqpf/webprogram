package t3_CRUD;

import java.util.ArrayList;
import java.util.Scanner;

public class DbConnService {
	Scanner sc = new Scanner(System.in);
	
	DbConnDao dao = new DbConnDao();
	DbConnVo vo = new DbConnVo();
	
	//자료입력 메소드
	public void dbInput() {
		System.out.println("==========================");
		System.out.print("id : "); vo.setMid(sc.next());
		System.out.print("pw : "); vo.setPwd(sc.next());
		System.out.print("name : "); vo.setName(sc.next());
		System.out.print("age : "); vo.setAge(sc.nextInt());
		dao.dbInput(vo);
	}

	//개별 자료조회 메소드
	public void dbSearch() {
		System.out.println("==========================");
		System.out.print("아이디를 입력 >");
		String mid = sc.next();
		dao.getSearch(mid);
	}

	//전체자료조회 메소드
	public void dbList() {
		System.out.println("==========================");
		dao.dbList();
	}

	//자료삭제 메소드
	public void dbDelete() {
		String mid = "";
		System.out.println("삭제할 아이디를 입력하세요?");
		mid = sc.next();
		//삭제할 자료가 있는지 검색
		String res = dao.getDeleteSearch(mid);
		if(res.equals("")) {
			System.out.println("삭제할 아이디가 존재하지 않습니다.");
		}else {
			System.out.println(res+"자료를 삭제하시겠습니까?(y/n)");
			res = sc.next();
			if(res.toUpperCase().equals("Y")) {
				dao.dbDelete(mid);
			}else {
				System.out.println("삭제가 취소되었습니다.");
			}
		}
	}

	//자료수정 메소드
	public void dbUpdate() {
		String mid = "";
		System.out.println("수정할 아이디를 입력하세요?");
		mid = sc.next();
		//삭제할 자료가 있는지 검색
		dao.dbUpdate(mid);
	}

	//자료수정처리를 위한 개별자료 입력받은 후 수정
	public DbConnVo updateList(DbConnVo vo) {
		boolean flag = true;
		System.out.println("검색한 자료");
		System.out.println("아이디 :"+vo.getMid()+", 성명:"+vo.getName()+", 비번:"+vo.getPwd()+",나이:"+vo.getAge());
		while(flag) {
			System.out.print("수정할 항목을 선택하세요?1.비밀번호 2:성명 3:나이 4.종료 >");
			int no = sc.nextInt();
			switch (no) {
				case 1: {
					System.out.println("비밀번호를 입력 >");
					vo.setPwd(sc.next());
					break;
				}
				case 2: {
					System.out.println("이름를 입력 >");
					vo.setName(sc.next());
					break;
				}
				case 3: {
					System.out.println("나이를 입력 >");
					vo.setAge(sc.nextInt());
					break;
				}
				default:
					flag = false;
			}
		}
		return vo;
	}

	//ArrayList를 사용한 전체조회
	public void dbList2() {
		ArrayList<DbConnVo> vos = dao.dbList2();
		System.out.println("=========================================================");
		System.out.println("번호\t아이디\t비밀번호\t성명\t나이");
		System.out.println("---------------------------------------------------------");
		for(DbConnVo vo: vos) {
			System.out.println(vo.getIdx()+"\t"+vo.getMid()+"\t"+vo.getPwd()+"\t"+vo.getName()+"\t"+vo.getAge());
		}
		System.out.println("=========================================================");
	}
}
