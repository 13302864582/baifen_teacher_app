package com.ucuxin.ucuxin.tec.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import android.text.TextUtils;

import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.model.GradeModel;
import com.ucuxin.ucuxin.tec.model.SubjectModel;
import com.ucuxin.ucuxin.tec.utils.LogUtils;

public class WelearnDBUtil {

	public static final String TAG = WelearnDBUtil.class.getSimpleName();
	
	/**
	 * 加载默认年级静态数据
	 */
	public static void loadDefaultGradeDB() {
		
		WeLearnDB db = WLDBHelper.getInstance().getWeLearnDB();
		
		InputStream in = null;
		BufferedReader br = null;
		
		String encoding = "utf-8";
		try {
			in = TecApplication.getContext().getAssets().open("welearn_grade.txt");
			br = new BufferedReader(new java.io.InputStreamReader(in, encoding));

			int count = 0;
			String line = null;
			while ((line = br.readLine()) != null) {
				if (count++ == 0 || TextUtils.isEmpty(line)){
					continue;
				}

				String elements[] = line.split("\t");
				if(elements == null || elements.length < 3){
					continue;
				}
				
				GradeModel gm = new GradeModel();
				gm.setId(Integer.parseInt(elements[0]));
				gm.setName(elements[1]);
				gm.setGradeId(Integer.parseInt(elements[2]));
				if (elements.length == 4) {
					gm.setSubjects(elements[3]);
				} else {
					gm.setSubjects("");
				}
				
				db.insertGrade(gm);
			}
			LogUtils.d(TAG, "loadDefaultSubjectsDB total: " + count);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.i(TAG, e.getMessage());
		} finally {
			try {
				if (br != null)
					br.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 加载默认科目静态数据
	 */
	public static void loadDefaultSubjectDB() {
		
		WeLearnDB db = WLDBHelper.getInstance().getWeLearnDB();
		
		InputStream in = null;
		BufferedReader br = null;
		
		String encoding = "utf-8";
		try {
			in = TecApplication.getContext().getAssets().open("welearn_subjects.txt");
			br = new BufferedReader(new java.io.InputStreamReader(in, encoding));

			int count = 0;
			String line = null;
			while ((line = br.readLine()) != null) {
				if (count++ == 0 || TextUtils.isEmpty(line)){
					continue;
				}

				String elements[] = line.split("\t");
				if(elements == null || elements.length < 2){
					continue;
				}
				
				SubjectModel sm = new SubjectModel();
				sm.setId(Integer.parseInt(elements[0]));
				sm.setName(elements[1]);
				
				db.insertSubject(sm);
			}
			LogUtils.d(TAG, "loadDefaultSubjectsDB total: " + count);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.i(TAG, e.getMessage());
		} finally {
			try {
				if (br != null)
					br.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
