package t3_CRUD;

import java.util.Scanner;

public class Menu {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		DbConnDao dbConnDao = new DbConnDao();
		DbConnService service = new DbConnService();
		
		int choise = 0;
		boolean flag = true;
		while(flag) {
			String mid, pwd, name;
			int age = 0;
			System.out.print("선택>1.입력 2.개별조회 3.전체조회a 4.전체조회ArrayList 5.수정 6.삭제 0.종료");
			choise = sc.nextInt();
			switch (choise) {
				case 1: {			
					service.dbInput();//자료등록
					break;
				}
				case 2: {			
					service.dbSearch();//자료조회
					break;
				}
				case 3: {			
					service.dbList(); //전체조회
					break;
				}
				case 4: {			
					service.dbList2(); //전체조회
					break;
				}
				case 5: {			
					service.dbUpdate(); //자료수정
					break;
				}
				case 6: {			
					service.dbDelete();//자료삭제
					break;
				}
				default:
					flag = false;
			}
		}
		System.out.println("======================================");
		System.out.println("작업 끝");
		
		dbConnDao.dbClose();
		sc.close();
	}
	
	
	
}
