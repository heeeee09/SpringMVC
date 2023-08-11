package kr.co.coupang.member.service;

import kr.co.coupang.member.domain.Member;

public interface MemberService {
	/**
	 * ��� ��� service
	 * @param member
	 * @return
	 */
	public int registerMember(Member member);

	/**
	 * ȸ�� ���� service
	 * @param memberId
	 * @return
	 */
	public int removeMember(String memberId);

	/**
	 * ��� �α��� Service
	 * @param member
	 * @return
	 */
	public Member memberLoginCheck(Member member);

	/**
	 * ȸ�� ���������� service
	 * @param mOne
	 * @return
	 */
	public Member showOneById(String memberId);

	/**
	 * ȸ�� ���� ����
	 * @param member
	 * @return
	 */
	public int modifyMember(Member member);

	
}
