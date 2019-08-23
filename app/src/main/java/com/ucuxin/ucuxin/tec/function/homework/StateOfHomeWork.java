package com.ucuxin.ucuxin.tec.function.homework;

public class StateOfHomeWork {
	/** 抢答中 */
	public static final int ASKING = 0;
	/** 答题中 */
	public static final int ANSWERING = 1;
	/** 已提交答案 */
	public static final int ANSWERED = 2;
	/** 追问中 */
	public static final int APPENDASK = 3;
	/** 已采纳 */
	public static final int ADOPTED = 4;
	/** 已拒绝 */
	public static final int REFUSE = 5;
	/** 仲裁中 */
	public static final int ARBITRATE = 6;
	/** 仲裁结束 */
	public static final int ARBITRATED = 7;
	/** 被举报 */
	public static final int REPORT = 8;
	/** 已删除 */
	public static final int DELETE = 9;
}
