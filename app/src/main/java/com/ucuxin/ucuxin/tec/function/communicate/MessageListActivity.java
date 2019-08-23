package com.ucuxin.ucuxin.tec.function.communicate;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.partner.PartnerFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 消息好友列表
 * @author Administrator
 *
 */
public class MessageListActivity extends  BaseActivity implements OnCheckedChangeListener {

    private CommunicateFragment chatFragment;

    private PartnerFragment friendFragment;

    private FragmentManager fm;

    private RadioGroup radioGroup;

    private RadioButton radio_message;

    private RadioButton radio_friend;

    private int index = 0;

    private RelativeLayout back_layout;
    private RelativeLayout layout_next_step;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.message_list_activity);
        initView();
        initListener();
        onCheckedChanged(radioGroup, radio_message.getId());

    }

    @Override
    public void initView() {
        super.initView();
        fm = getSupportFragmentManager();
        back_layout = (RelativeLayout)findViewById(R.id.back_layout);
        layout_next_step = (RelativeLayout)this.findViewById(R.id.next_setp_layout);
        this.radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radio_message = (RadioButton)findViewById(R.id.radio_message);
        radio_friend = (RadioButton)findViewById(R.id.radio_friend);
    }

    @Override
    public void initListener() {
        super.initListener();
        radioGroup.setOnCheckedChangeListener(this);
        back_layout.setOnClickListener(this);
        layout_next_step.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_layout:// 返回
                finish();
                break;
            case R.id.next_setp_layout:
            Intent addIntent = new Intent(this, AddContactsActivity.class);
            startActivity(addIntent);
            break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        index = radio_message.getId() == checkedId ? 0 : 1;
        setTabSelection(index);
    }

    public void setTabSelection(int index) {
        // 每次先清除上次的选中状态
        clearSelection();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:// 消息tab
                radio_message.setBackgroundResource(R.drawable.xian_baline);
                radio_friend.setBackgroundColor(Color.parseColor("#00000000"));
                radio_message.setTextColor(getResources().getColor(R.color.welearn_theme_center_txt_color));
                radio_friend.setTextColor(Color.parseColor("#737373"));
                if (chatFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    chatFragment = new CommunicateFragment();
                    transaction.add(R.id.layout_container, chatFragment);
                } else {
                    transaction.show(chatFragment);
                }
                break;
            case 1:// 好友tab
            default:
                radio_friend.setBackgroundResource(R.drawable.xian_baline);
                radio_message.setBackgroundColor(Color.parseColor("#00000000"));
                radio_friend.setTextColor(getResources().getColor(R.color.welearn_theme_center_txt_color));
                radio_message.setTextColor(Color.parseColor("#737373"));
                if (friendFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    friendFragment = new PartnerFragment();
                    transaction.add(R.id.layout_container, friendFragment);
                } else {
                    transaction.show(friendFragment);
                }
                break;
        }
        transaction.commit();

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        radio_message.setBackgroundColor(Color.parseColor("#00000000"));
        radio_friend.setBackgroundColor(Color.parseColor("#00000000"));
        radio_message.setTextColor(Color.parseColor("#828282"));
        radio_friend.setTextColor(Color.parseColor("#828282"));

    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     * 
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (chatFragment != null) {
            transaction.hide(chatFragment);
        }

        if (friendFragment != null) {
            transaction.hide(friendFragment);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
        // //将这一行注释掉，阻止activity保存fragment的状态
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (chatFragment != null && fragment instanceof CommunicateFragment) {
            chatFragment=(CommunicateFragment)fragment;
        }else if (friendFragment != null && fragment instanceof PartnerFragment) {
            friendFragment=(PartnerFragment)fragment;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
