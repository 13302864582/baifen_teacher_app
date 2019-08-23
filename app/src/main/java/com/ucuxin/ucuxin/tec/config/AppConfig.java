package com.ucuxin.ucuxin.tec.config;
import com.ucuxin.ucuxin.tec.utils.SpUtil;
/**
 * app url配置
 *
 * @author: sky
 */
public class AppConfig {
    //private static String WORKURL = "api.huoyanzuoye.com"; // 正式坏境
    private static String WORKURL = /*"zyapi.ucuxin.com"*/"api.baifen.lantel.net"; // 有笔作业环境 zyapi.ucuxin.com
    /**
     * 聊天
     */
    public static String PYTHON_URL;
    // 用于活动分享的url
    public static String WELEARN_URL = "https://web.baifen.lantel.net/";
    /**
     * 错误邮件
     */
    public static String MAIL_URL;
    // 应用自动更新 //
    public static final String UPDATEURL;
    // 分享, Url跳转 //
    public static final String SHARE_URL = "https://web.baifen.lantel.net/?app=1&userid=";
    // 欢迎页面 拉取闪屏 //
    public static final String WELCOME_IMAGE_CHECK_URL = "http://web.baifen.lantel.net/app_guide.php";
    // 除聊天以外(如: 登陆) //
    public static String GO_URL;
    // 是否是调试模式
    public static final boolean IS_DEBUG = false;
    // 外网测试环境
    public static final boolean IS_ONELINE = false;
    public static void loadIP() {
        GO_URL = "https://" + SpUtil.getInstance().getGOTP() + "/api/";
        PYTHON_URL = "ws://" + SpUtil.getInstance().getPYTHONTP() + "/ws";
        // 后台个人
        // execUrl();
    }



    static {
        if (IS_DEBUG) {
            loadIP();
            // MAIL_URL = "http://218.244.151.195:9001/http/mail";
            // MAIL_URL = "http://218.244.151.195:9001/api/mail/errormail";
            WELEARN_URL = "https://api.baifen.lantel.net/";
            UPDATEURL = "https://api.baifen.lantel.net/app_version.php?os=2&roleid=2";// 2是老师

        } else {
            WELEARN_URL = "http://web.baifen.lantel.net/";
            GO_URL = "https://" + WORKURL + "/api/";// homework/";
            PYTHON_URL = "ws://" + WORKURL + "/ws";
            MAIL_URL = "https://" + WORKURL + "/api/mail/errormail";
            UPDATEURL = "http://web.baifen.lantel.net/app_version.php?os=2&roleid=2&isyun=0";// 2是老师
        }
    }

    private static void execUrl() {
        GO_URL = "https://api.baifen.lantel.net/api/";
        PYTHON_URL = "ws://zyws.ucuxin.com:6000/ws";
    }
}