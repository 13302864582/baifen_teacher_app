
package com.ucuxin.ucuxin.tec.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.model.PayAnswerGoldGson;
import com.ucuxin.ucuxin.tec.model.SubjectId;

public class GoldToStringUtil {
    public static String GoldToString(float gold) {
        gold += 0.0005;
        DecimalFormat df = new DecimalFormat("#.0");
        String goldStr = df.format(gold);
        if (goldStr.startsWith(".")) {
            goldStr = "0" + goldStr;
        }
        return goldStr;
    }

    public static String GoldToString(double gold) {
        gold += 0.0005;
        DecimalFormat df = new DecimalFormat("#.0");
        String goldStr = df.format(gold);
        if (goldStr.startsWith(".")) {
            goldStr = "0" + goldStr;
        }
        return goldStr;
    }

    public static void updataPayGoldInfo(final Context mActivity, JSONObject subjects) {
        PayAnswerGoldGson chu_1 = new PayAnswerGoldGson();
        PayAnswerGoldGson chu_2 = new PayAnswerGoldGson();
        PayAnswerGoldGson chu_3 = new PayAnswerGoldGson();
        PayAnswerGoldGson gao_1 = new PayAnswerGoldGson();
        PayAnswerGoldGson gao_2 = new PayAnswerGoldGson();
        PayAnswerGoldGson gao_3 = new PayAnswerGoldGson();

        List<SubjectId> chu1subs = new ArrayList<SubjectId>();
        List<SubjectId> chu2subs = new ArrayList<SubjectId>();
        List<SubjectId> chu3subs = new ArrayList<SubjectId>();
        List<SubjectId> gao1subs = new ArrayList<SubjectId>();
        List<SubjectId> gao2subs = new ArrayList<SubjectId>();
        List<SubjectId> gao3subs = new ArrayList<SubjectId>();

        // 英语
        JSONObject english = JsonUtils.getJSONObject(subjects, "english", null);
        chu1subs.add(
                new Gson().fromJson(JsonUtils.getString(english, "chu_1", null), SubjectId.class));
        chu2subs.add(
                new Gson().fromJson(JsonUtils.getString(english, "chu_2", null), SubjectId.class));
        chu3subs.add(
                new Gson().fromJson(JsonUtils.getString(english, "chu_3", null), SubjectId.class));
        gao1subs.add(
                new Gson().fromJson(JsonUtils.getString(english, "gao_1", null), SubjectId.class));
        gao2subs.add(
                new Gson().fromJson(JsonUtils.getString(english, "gao_2", null), SubjectId.class));
        gao3subs.add(
                new Gson().fromJson(JsonUtils.getString(english, "gao_3", null), SubjectId.class));

        // 数学
        JSONObject math = JsonUtils.getJSONObject(subjects, "math", null);
        chu1subs.add(
                new Gson().fromJson(JsonUtils.getString(math, "chu_1", null), SubjectId.class));
        chu2subs.add(
                new Gson().fromJson(JsonUtils.getString(math, "chu_2", null), SubjectId.class));
        chu3subs.add(
                new Gson().fromJson(JsonUtils.getString(math, "chu_3", null), SubjectId.class));
        gao1subs.add(
                new Gson().fromJson(JsonUtils.getString(math, "gao_1", null), SubjectId.class));
        gao2subs.add(
                new Gson().fromJson(JsonUtils.getString(math, "gao_2", null), SubjectId.class));
        gao3subs.add(
                new Gson().fromJson(JsonUtils.getString(math, "gao_3", null), SubjectId.class));

        // 物理
        JSONObject physics = JsonUtils.getJSONObject(subjects, "physics", null);
        chu1subs.add(
                new Gson().fromJson(JsonUtils.getString(physics, "chu_1", null), SubjectId.class));
        chu2subs.add(
                new Gson().fromJson(JsonUtils.getString(physics, "chu_2", null), SubjectId.class));
        chu3subs.add(
                new Gson().fromJson(JsonUtils.getString(physics, "chu_3", null), SubjectId.class));
        gao1subs.add(
                new Gson().fromJson(JsonUtils.getString(physics, "gao_1", null), SubjectId.class));
        gao2subs.add(
                new Gson().fromJson(JsonUtils.getString(physics, "gao_2", null), SubjectId.class));
        gao3subs.add(
                new Gson().fromJson(JsonUtils.getString(physics, "gao_3", null), SubjectId.class));

        // 化学
        JSONObject chemistry = JsonUtils.getJSONObject(subjects, "chemistry", null);
        chu1subs.add(new Gson().fromJson(JsonUtils.getString(chemistry, "chu_1", null),
                SubjectId.class));
        chu2subs.add(new Gson().fromJson(JsonUtils.getString(chemistry, "chu_2", null),
                SubjectId.class));
        chu3subs.add(new Gson().fromJson(JsonUtils.getString(chemistry, "chu_3", null),
                SubjectId.class));
        gao1subs.add(new Gson().fromJson(JsonUtils.getString(chemistry, "gao_1", null),
                SubjectId.class));
        gao2subs.add(new Gson().fromJson(JsonUtils.getString(chemistry, "gao_2", null),
                SubjectId.class));
        gao3subs.add(new Gson().fromJson(JsonUtils.getString(chemistry, "gao_3", null),
                SubjectId.class));

        // 生物
        JSONObject biology = JsonUtils.getJSONObject(subjects, "biology", null);
        chu1subs.add(
                new Gson().fromJson(JsonUtils.getString(biology, "chu_1", null), SubjectId.class));
        chu2subs.add(
                new Gson().fromJson(JsonUtils.getString(biology, "chu_2", null), SubjectId.class));
        chu3subs.add(
                new Gson().fromJson(JsonUtils.getString(biology, "chu_3", null), SubjectId.class));
        gao1subs.add(
                new Gson().fromJson(JsonUtils.getString(biology, "gao_1", null), SubjectId.class));
        gao2subs.add(
                new Gson().fromJson(JsonUtils.getString(biology, "gao_2", null), SubjectId.class));
        gao3subs.add(
                new Gson().fromJson(JsonUtils.getString(biology, "gao_3", null), SubjectId.class));

        LogUtils.e("subject:", new Gson()
                .fromJson(JsonUtils.getString(biology, "gao_3", null), SubjectId.class).toString());
        chu_1.setSubjects(chu1subs);
        chu_2.setSubjects(chu2subs);
        chu_3.setSubjects(chu3subs);
        gao_1.setSubjects(gao1subs);
        gao_2.setSubjects(gao2subs);
        gao_3.setSubjects(gao3subs);

        final List<PayAnswerGoldGson> gsonlist = new ArrayList<PayAnswerGoldGson>();
        gsonlist.add(chu_1);
        gsonlist.add(chu_2);
        gsonlist.add(chu_3);
        gsonlist.add(gao_1);
        gsonlist.add(gao_2);
        gsonlist.add(gao_3);

        ThreadPoolUtil.execute(new Runnable() {

            @Override
            public void run() {
                GlobalVariable.doingGoldDB = true;
                /*
                 * 更改数据调用方式 modified by yh 2015-01-07 Start
                 * ---------------------- OLD CODE ---------------------- new
                 * PayAnswerGoldDBHelper(mActivity).insert(gsonlist);
                 */
                WLDBHelper.getInstance().getWeLearnDB().insertGold(gsonlist);
                // 更改数据调用方式 modified by yh 2015-01-07 End
            }
        });
    }
}
