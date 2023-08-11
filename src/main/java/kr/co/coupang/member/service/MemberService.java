package kr.co.coupang.member.service;

import kr.co.coupang.member.domain.Member;

public interface MemberService {
	/**
	 * 멤버 등록 service
	 * @param member
	 * @return
	 */
	public int registerMember(Member member);

	/**
	 * 회원 삭제 service
	 * @param memberId
	 * @return
	 */
	public int removeMember(String memberId);

	/**
	 * 멤버 로그인 Service
	 * @param member
	 * @return
	 */
	public Member memberLoginCheck(Member member);

	/**
	 * 회원 마이페이지 service
	 * @param mOne
	 * @return
	 */
	public Member showOneById(String memberId);

	/**
	 * 회원 정보 수정
	 * @param member
	 * @return
	 */
	public int modifyMember(Member member);

	
}
