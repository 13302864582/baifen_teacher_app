package com.ucuxin.ucuxin.tec.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.ResponseCmdDef;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.PhoneLoginModel;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.DebugActvity;
import com.ucuxin.ucuxin.tec.utils.MD5Util;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.AndroidBug5497Workaround;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneLoginActivity extends BaseActivity implements OnClickListener, HttpListener {

    public static final String TAG = PhoneLoginActivity.class.getSimpleName();

    private TextView nonNum, tv_wangzhi, tv_service_tel;
    private EditText phone_et;
    private EditText psw_et;
    private Button login_btn;
    private ImageView deletePhoneIV, deletePswIV;

    private String num, psw;

    private PhoneLoginModel plm;

    private String address;

    private RelativeLayout bottom_info_layout;

    private Button debug_bt;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContant.CLOSEDIALOG:
                    if (isShowDialog) {
                        isShowDialog = false;
                        closeDialog();
                        ToastUtils.show(R.string.text_login_timeout);
                    }
                    break;

                // case GlobalContant.TOAST_IN_THREAD:
                // break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_phone_login);
        initView();
        initListener();

    }

    @Override
    public void initView() {
        super.initView();
        AndroidBug5497Workaround.assistActivity(this);
        setWelearnTitle(R.string.text_login_by_phone);
        deletePhoneIV = (ImageView) findViewById(R.id.delete_phone_iv);
        deletePswIV = (ImageView) findViewById(R.id.delete_psw_iv);
        phone_et = (EditText) findViewById(R.id.phone_num_et_phonelogin);
        psw_et = (EditText) findViewById(R.id.phone_psw_et_phonelogin);
        login_btn = (Button) findViewById(R.id.phone_login_btn_phonelogin);
        nonNum = (TextView) findViewById(R.id.non_num_tv_phonelogin);
        bottom_info_layout = (RelativeLayout) findViewById(R.id.bottom_info_layout);

        TextView losPsw = (TextView) findViewById(R.id.los_psw_tv_phonelogin);
        losPsw.setOnClickListener(this);
       /*
        SpannableString spStr = new SpannableString("忘记密码了？");
        spStr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // 设置下划线
            }

            @Override
            public void onClick(View widget) {
                Intent i = new Intent(PhoneLoginActivity.this, PhoneRegisterActivity.class);
                i.putExtra(PhoneRegisterActivity.DO_TAG, PhoneRegisterActivity.DO_RESET);
                startActivity(i);
            }
        }, 0, "忘记密码了？".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

        tv_wangzhi = (TextView) findViewById(R.id.tv_wangzhi);
        address = TecApplication.getContext().getResources().getString(R.string.login_guide_info_address);
        //String info = TecApplication.getContext().getResources().getString(R.string.login_guide_info1, address);
        SpannableStringBuilder builder = new SpannableStringBuilder(address);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(TecApplication.getContext().getResources()
                .getColor(R.color.welearn_blue));
        builder.setSpan(redSpan, 0, address.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_wangzhi.setText("网址:" + builder);

        //losPsw.setHighlightColor(Color.TRANSPARENT); // 设置点击后的颜色为透明，否则会一直出现高亮
        //losPsw.append(spStr);
        //losPsw.setMovementMethod(LinkMovementMethod.getInstance());// 开始响应点击事件

        debug_bt = (Button) this.findViewById(R.id.ip_change_debug_bt);

        if (AppConfig.IS_DEBUG) {
            debug_bt.setVisibility(View.VISIBLE);
            debug_bt.setOnClickListener(this);
        } else {
            debug_bt.setVisibility(View.GONE);
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        login_btn.setOnClickListener(this);
        nonNum.setOnClickListener(this);
        deletePhoneIV.setOnClickListener(this);
        deletePswIV.setOnClickListener(this);
        //tv_wangzhi.setOnClickListener(this);
        phone_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                bottom_info_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                bottom_info_layout.setVisibility(View.VISIBLE);
                if (s.toString().length() > 0) {
                    deletePhoneIV.setVisibility(View.VISIBLE);
                } else {
                    deletePhoneIV.setVisibility(View.GONE);
                }
            }
        });

        psw_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    deletePswIV.setVisibility(View.VISIBLE);
                } else {
                    deletePswIV.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        String phoneNum = SharePerfenceUtil.getInstance().getPhoneNum();
        phone_et.setText(phoneNum);
    }


    @Override
    public void onPause() {
        SharePerfenceUtil.getInstance().setPhoneNum(phone_et.getText().toString().trim());
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.phone_login_btn_phonelogin:
                if (System.currentTimeMillis() - clickTime < 500) {
                    return;
                }
                clickTime = System.currentTimeMillis();
                num = phone_et.getText().toString().trim();
                psw = psw_et.getText().toString().trim();
                if (TextUtils.isEmpty(num)/* || !num.matches("1[3|5|7|8|][0-9]{9}") */) {
                    ToastUtils.show(R.string.text_input_right_phone_num);
                    return;
                }
                SharePerfenceUtil.getInstance().setPhoneNum(num);
                if (TextUtils.isEmpty(psw)) {
                    ToastUtils.show(R.string.text_input_password);
                    return;
                }
                showDialog(getString(R.string.text_logining));
                isShowDialog = true;
                mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 35000);

                loginNewServer();

                break;
            case R.id.non_num_tv_phonelogin:
                IntentManager.goToPhoneRegActivity(this, null, false);
                break;
            case R.id.delete_phone_iv:
                if (null != phone_et) {
                    phone_et.setText("");
                }
                break;
            case R.id.delete_psw_iv:
                if (null != psw_et) {
                    psw_et.setText("");
                }
                break;
//            case R.id.tv_wangzhi:
//                try {
//                    Uri uri = Uri.parse("http://" + address);
//                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(it);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                break;
            case R.id.ip_change_debug_bt:// 换ip
                startActivity(new Intent(this, DebugActvity.class));
                break;
            case R.id.los_psw_tv_phonelogin:
                Intent i = new Intent(PhoneLoginActivity.this, PhoneRegisterActivity.class);
                i.putExtra(PhoneRegisterActivity.DO_TAG, PhoneRegisterActivity.DO_RESET);
                startActivity(i);
                break;
        }
    }


    /**
     * 登陆新服务器
     */
    private void loginNewServer() {
        plm = new PhoneLoginModel();
        plm.setCount(num);
        plm.setPassword(MD5Util.getMD5String(psw));
        // plm.setProvice(province);
        // plm.setCity(city);

        try {
            OkHttpHelper.post(this, "user", "tellogin", new JSONObject(new Gson().toJson(plm)), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        // ToastUtils.show(R.string.text_connection_timeout);
        if (code == ResponseCmdDef.CODE_RETURN_OK) {
            try {
                JSONObject jobj = new JSONObject(dataJson);
                String tokenId = jobj.getString("tokenid");
                int roleid = jobj.getInt("roleid");
                if (roleid != GlobalContant.ROLE_ID_COLLEAGE) {
                    closeDialogHelp();
                    ToastUtils.show(R.string.you_are_not_a_teacher);
                } else {

                    UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
                    WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                    SharePerfenceUtil.getInstance().savePhoneLoginInfo(plm);
                    SharePerfenceUtil.getInstance().setUserRoleId(uInfo.getRoleid());

                    SharePerfenceUtil.getInstance().setWelearnTokenId(tokenId);

                    if (GlobalVariable.loginActivity != null) {
                        GlobalVariable.loginActivity.finish();
                        GlobalVariable.loginActivity = null;
                    }

                    // 打开websocket连接
                    IntentManager.startWService(TecApplication.getContext());

                    IntentManager.goToMainView(this);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            closeDialog();
            if (!TextUtils.isEmpty(errMsg)) {
                ToastUtils.show(errMsg);
            } else {
                ToastUtils.show(R.string.text_connection_timeout);
            }
        }
    }

    @Override
    public void onFail(int HttpCode, String errMsg) {
        closeDialog();
        if (!TextUtils.isEmpty(errMsg)) {
            ToastUtils.show(errMsg);
        } else {
            ToastUtils.show(R.string.text_connection_timeout);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isShowDialog = false;
        GlobalVariable.loginActivity = null;
        closeDialog();
        if (mHandler != null) {
            mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
        }
    }


}
