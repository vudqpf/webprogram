package gagebu;

import java.util.ArrayList;
import java.util.Scanner;

public class GagebuService {
	Scanner sc = new Scanner(System.in);
	GagebuVo vo = new GagebuVo();;
	GagebuDao dao = new GagebuDao();
	
	//가계부 입력처리
	public void gInput() {
		while(true) {
			System.out.print("작업선택: 1.수입 2.지출 0.종료 ==>");
			String sel = sc.next();
			
			if(sel.equals("0"))	break;
			
			if(sel.equals("1")) vo.setgCode("+");
			else vo.setgCode("-");
			
			System.out.print("금액 >"); vo.setPrice(sc.nextInt());
			System.out.print("내역 >"); vo.setContent(sc.next());
			dao.gInput(vo);
			
		}
	}

	//조회
	public void gList(int su) {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		String wdate = "";
		
		if(su == 3) {		//전체조회
//			vos = dao.gList();
			vos = dao.gSearch("list");
		}else if(su == 2) {	//날짜조회
			System.out.println("검색할 날짜를 입력(예:20210410) >");
			wdate = sc.next();
			vos = dao.gSearch(wdate);
		}
		
		String gCode = "";
		int balance = 0, sw=0, suip=0, jichul=0;	
		System.out.println("=================================================");
		System.out.println("거래날짜\t\t비고\t  금액\t  적요");
		System.out.println("-------------------------------------------------");
		
		for(GagebuVo vo : vos) {
			if(sw == 0) {
				balance = vo.getBalance();
				sw=1;
			}
			if(vo.getgCode().equals("+")) {
				gCode = "수입";
				suip += vo.getPrice();
			} else 	{
				gCode = "지출";
				jichul += vo.getPrice();
			}
			
			System.out.println(vo.getWdate().substring(0,10)+"\t"+gCode+"\t"+String.format("%,7d", vo.getPrice())+"\t"+vo.getContent());				
		}
		
		System.out.println("=================================================");	
		if(su == 2) System.out.println("검색일자 : "+wdate+", 수입 : "+ suip + ", 지출 : "+jichul);
		System.out.println("현재 총 잔액 : "+String.format("%,7d", balance));
		System.out.println("=================================================");	
	}

	//일련번호 삭제....
	public void gDelete() {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		String wdate = "";
		
		System.out.println("삭제할 날짜를 입력(예:20210410) >");
		wdate = sc.next();
		vos = dao.gSearch(wdate);
	
		String gCode = "";
		System.out.println("=================================================");
		System.out.println("고유번호\t거래날짜\t\t비고\t  금액\t  적요");
		System.out.println("-------------------------------------------------");
		
		for(GagebuVo vo : vos) {

			if(vo.getgCode().equals("+")) {
				gCode = "수입";
			} else 	{
				gCode = "지출";
			}
			System.out.println(vo.getIdx()+"\t"+vo.getWdate().substring(0,10)+"\t"+gCode+"\t"+String.format("%,7d", vo.getPrice())+"\t"+vo.getContent());				
		}
		System.out.println("=================================================");	
		System.out.println("삭제할 고유번호를 선택(종료:0) >");
		int idx = sc.nextInt();
		if(idx == 0) return;
		dao.gDelete(idx);
	}
	
}
