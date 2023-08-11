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
	// 의존성 주입
	@Autowired
	private MemberService service;
	
	//doGet : 페이지 이동용
	@RequestMapping(value="/member/register.do", method=RequestMethod.GET)
	public String showRegisterForm() {
		return "member/register";
	}
	
	// doPost : 데이터 저장용
	@RequestMapping(value="/member/register.do", method=RequestMethod.POST)	// name값, 변수 값이 같으면 ()생략 가능
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
		/* 							ㄴ HttpServletRequest request 쓸 수는 있다!
		 * request.setAttribute("", "");
		 * request.getRequestDispatcher("/WEB-INF/views/member/register.jsp");
		 * 쓸 수 있지만 이제 안 쓰고 Model
		 */
//		String memberId = request.getParameter("memberID");
//		request.setCharacterEncoding("UTF-8");
		Member member = new Member(memberId, memberPw, memberName, memberAge, memberGender, memberEmail, memberPhone, memberAddress, memberHobby);
		// 에러 메시지 별로 확인하기 위해 일부러 try를 사용하였다.
		try {
			int result = service.registerMember(member);
			if(result > 0) {
				// 성공
				// viewReserver를 타지 않고 sendRedirect처럼 동작한다.
				return "redirect:/index.jsp"; 
				//.forward의 역할
			}else {
				// 실패
				model.addAttribute("msg", "회원가입이 완료되지 않았습니다.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(); // 콘솔 창에 빨간색 메시지 출력
			model.addAttribute("msg", e.getMessage()); // 콘솔 창에 뜨는 메시지를 웹 페이지에 출력
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
			model.addAttribute("msg", "회원정보 수정이 완료되지 않았습니다.");
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
				model.addAttribute("msg", "회원 삭제가 완료되지 않았습니다.");
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
					model.addAttribute("msg", "데이터 조회에 실패했습니다.");
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
				// 성공하면 로그인 페이지로 이동
//				model.addAttribute("member", mOne);
//				HttpSession session = request.getSession();
//				session.setAttribute("memberId", mOne.getMemberId());
//				session.setAttribute("memberName", mOne.getMemberName());
				// redirect는 model을 쓰면 안된다
				model.addAttribute("memberId", mOne.getMemberId());
				model.addAttribute("memberName", mOne.getMemberName());
				return "redirect:/index.jsp";
			} else {
				// 실패하면 실패 메시지 출력
				model.addAttribute("msg", "로그인이 실패했습니다.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			// TODO: handle exception
			// 예외 발생기 예외 메시지 출력
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/logout.do", method =RequestMethod.GET)
	public String memberLogout(HttpSession sessionPrev
			/*
			* SessionStatus는 스프링의 어노테이션(SessionAtributes)로 지원되는 세션을 만료시킨다.
			* 사용된 메소드는 setComplete();
			*/
			, SessionStatus session
			, Model model) {
		if(session != null) {
//			session.invalidate();
			session.setComplete();
			if(session.isComplete()) {
				
			}// 세션 만료 유효성 체크
			return "redirect:/index.jsp";
			
		}else {
			model.addAttribute("msg", "로그아웃을 완료하지 못했습니다.");
			return "common/errorPage";
		}
	}
}


