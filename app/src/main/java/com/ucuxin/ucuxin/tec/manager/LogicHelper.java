package com.ucuxin.ucuxin.tec.manager;

/**
 * Created by Sky on 2016/6/16 0016.
 * 项目业务逻辑辅助类
 */

public class LogicHelper {


    /**
     * 获取举报理由文本获取id
     * @param str
     * @return
     */
    public static int getReportIdFromTxt(String str) {
         int index=1;
        if ("图片看不清或不完整".equals(str)) {
            index=1;
        } else if ("图片与作业无关".equals(str)) {
            index=2;
        } else if ("年级、科目有误或科目混合".equals(str)) {
            index=3;
        } else if ("空白题超过2题".equals(str)) {
            index=4;
        } else if ("超纲发题".equals(str)) {
            index=5;
        } else if ("一次提问超过一题".equals(str)) {
            index=6;
        }else if("作文不在难题范围内".equals(str)){
            index=7;
        }
        return index;
    }


}
