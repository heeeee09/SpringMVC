package kr.co.coupang.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import kr.co.coupang.member.domain.Member;
import kr.co.coupang.member.service.MemberService;

@Controller
@SessionAttributes({"memberId", "memberName"})
public class MemberController {
	// ������ ����
	@Autowired
	private MemberService service;
	
	//doGet : ������ �̵���
	@RequestMapping(value="/member/register.do", method=RequestMethod.GET)
	public String showRegisterForm() {
		return "member/register";
	}
	
	// doPost : ������ �����
	@RequestMapping(value="/member/register.do", method=RequestMethod.POST)	// name��, ���� ���� ������ ()���� ����
	public String registorMember(HttpServletRequest request, HttpServletResponse response
			, @RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
			, @RequestParam("memberName") String memberName
			, @RequestParam("memberAge") int memberAge
			, @RequestParam("memberGender") String memberGender
			, @RequestParam("memberEmail") String memberEmail
			, @RequestParam("memberPhone") String memberPhone
			, @RequestParam("memberAddress") String memberAddress
			, @RequestParam("memberHobby") String memberHobby
			, Model model) {
		/* 							�� HttpServletRequest request �� ���� �ִ�!
		 * request.setAttribute("", "");
		 * request.getRequestDispatcher("/WEB-INF/views/member/register.jsp");
		 * �� �� ������ ���� �� ���� Model
		 */
//		String memberId = request.getParameter("memberID");
//		request.setCharacterEncoding("UTF-8");
		Member member = new Member(memberId, memberPw, memberName, memberAge, memberGender, memberEmail, memberPhone, memberAddress, memberHobby);
		// ���� �޽��� ���� Ȯ���ϱ� ���� �Ϻη� try�� ����Ͽ���.
		try {
			int result = service.registerMember(member);
			if(result > 0) {
				// ����
				// viewReserver�� Ÿ�� �ʰ� sendRedirectó�� �����Ѵ�.
				return "redirect:/index.jsp"; 
				//.forward�� ����
			}else {
				// ����
				model.addAttribute("msg", "ȸ�������� �Ϸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(); // �ܼ� â�� ������ �޽��� ���
			model.addAttribute("msg", e.getMessage()); // �ܼ� â�� �ߴ� �޽����� �� �������� ���
			return "common/errorPage";
		}
	}

	@RequestMapping(value = "/member/update.do", method = RequestMethod.GET)
	public String modifyMember(
			  @RequestParam("memberId") String memberId
			, Model model
			) {
		Member mOne = service.showOneById(memberId);
		if(mOne != null) {
			model.addAttribute("member", mOne);
			return "member/modify";
		} else {
			return "redirect:/mypage.jsp";
		}
		
	}
	
	@RequestMapping(value="/member/update.do", method = RequestMethod.POST)
	public String modifyMember(
			  @RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
		    , @RequestParam("memberEmail") String memberEmail
		    , @RequestParam("memberPhone") String memberPhone
		    , @RequestParam("memberAddress") String memberAddress
		    , @RequestParam("memberHobby") String memberHobby
			, Model model
			) {
		Member member = new Member(memberId, memberPw, memberEmail, memberPhone, memberAddress, memberHobby);
		int result = service.modifyMember(member);
		if(result > 0) {
			model.addAttribute("member", member);
			return "member/myPage";
		}else {
			model.addAttribute("msg", "ȸ������ ������ �Ϸ���� �ʾҽ��ϴ�.");
			return "common/errorPage";
		}
	}
	
	

	@RequestMapping(value="/member/delete.do", method = RequestMethod.GET)
	public String removeMember(@RequestParam("memberId") String memberId, Model model) {
		try {
			int result = service.removeMember(memberId);
			if(result > 0) {
				return "redirect:/member/logout.do";
			}else {
				model.addAttribute("msg", "ȸ�� ������ �Ϸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/myPage.do", method= RequestMethod.GET)
		public String showDetailMember(
				 @RequestParam("memberId") String memberId
				, Model model) {
			try {
				Member mOne = service.showOneById(memberId);
	//		System.out.println(mOne.toString());
				if(mOne != null) {
					model.addAttribute("member", mOne);
					return "member/myPage";
				}else {
					model.addAttribute("msg", "������ ��ȸ�� �����߽��ϴ�.");
					return "common/errorPage";
				}
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("msg", e.getMessage());
				return "common/errorPage";
			}
		}

	@RequestMapping(value="/member/login.do" ,method = RequestMethod.POST)
	public String memberLogin(HttpServletRequest request
			, @RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
			, Model model) {
		// SELECT * FROM MEMBER_TBL WHERE MEMBER_ID=? AND MEMBER_PW=?
		try {
			Member member = new Member();
			member.setMemberId(memberId);
			member.setMemberPw(memberPw);
			Member mOne = service.memberLoginCheck(member);
			if(mOne != null) {
				// �����ϸ� �α��� �������� �̵�
//				model.addAttribute("member", mOne);
//				HttpSession session = request.getSession();
//				session.setAttribute("memberId", mOne.getMemberId());
//				session.setAttribute("memberName", mOne.getMemberName());
				// redirect�� model�� ���� �ȵȴ�
				model.addAttribute("memberId", mOne.getMemberId());
				model.addAttribute("memberName", mOne.getMemberName());
				return "redirect:/index.jsp";
			} else {
				// �����ϸ� ���� �޽��� ���
				model.addAttribute("msg", "�α����� �����߽��ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			// TODO: handle exception
			// ���� �߻��� ���� �޽��� ���
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/logout.do", method =RequestMethod.GET)
	public String memberLogout(HttpSession sessionPrev
			/*
			* SessionStatus�� �������� ������̼�(SessionAtributes)�� �����Ǵ� ������ �����Ų��.
			* ���� �޼ҵ�� setComplete();
			*/
			, SessionStatus session
			, Model model) {
		if(session != null) {
//			session.invalidate();
			session.setComplete();
			if(session.isComplete()) {
				
			}// ���� ���� ��ȿ�� üũ
			return "redirect:/index.jsp";
			
		}else {
			model.addAttribute("msg", "�α׾ƿ��� �Ϸ����� ���߽��ϴ�.");
			return "common/errorPage";
		}
	}
}


