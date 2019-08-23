package com.ucuxin.ucuxin.tec.utils;

public class SubjectUtil {
	public static String getSubjectString(int subjectid) {
		String subject = "";
		switch (subjectid) {
		case 1:
			 subject = "英语";
			break;
		case 2:
			subject = "数学";
			break;
		case 3:
			subject = "物理";
			break;
		case 4:
			subject = "化学";
			break;
		case 5:
			subject = "生物";
			break;
		case 6:
		    subject="语文";
		    break;

		default:
			break;
		}
		return subject;
	}
}
