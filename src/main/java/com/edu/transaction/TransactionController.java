package com.edu.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import transaction.TicketDAO;
import transaction.TicketDTO;

@Controller
public class TransactionController {
	
	//트랜잭션 처리를 위한 DAO객체를 멤버변수로 선언
	private TicketDAO dao;
	
	//servlet-context.xml에서 미리 생성한 빈을 자동주입 받는다.
	@Autowired
	public void setDao(TicketDAO dao) {
		this.dao = dao;
		System.out.println("@Autowired=>TicketDAO 주입성공");
	}
	
	//티켓 구매 페이지에 대한 매핑
	@RequestMapping("/transaction/buyTicketMain.do")
	public String buyTicketMain() {
		return "08Transaction/buyTicketMain";
	}
	
	//티켓 구매 처리
	@RequestMapping("/transaction/buyTicketAction.do")
	public String buyTicketAction(TicketDTO ticketDTO, Model model) {
		/*
		구매 페이지에서 전송한 폼값은 커맨드객체를 통해 한번에 받아서
		DTO객체에 저장한다. 해당 객체를 DB처리하기위해 DAO로 전달한다.
		*/
		dao.buyTicket(ticketDTO);
		model.addAttribute("ticketInfo", ticketDTO);
		return "08Transaction/buyTicketAction";
	}
	
}
