package com.mapuni.shangluo.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.loginAc.LoginActivity;
import com.mapuni.shangluo.activity.wdAc.AboutActivity;
import com.mapuni.shangluo.activity.wdAc.ContactMethodActivity;
import com.mapuni.shangluo.activity.wdAc.KnowledgeBaseActivity;
import com.mapuni.shangluo.activity.wdAc.MessageActivity;
import com.mapuni.shangluo.activity.wdAc.ModifyPasswordActivity;
import com.mapuni.shangluo.activity.wdAc.NotSignTaskActivity;
import com.mapuni.shangluo.activity.wdAc.QueryRecordActivity;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MyFragment extends Fragment {


    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.my_msg)
    TextView mMyMsg;
    @BindView(R.id.query_history)
    TextView mQueryHistory;
    @BindView(R.id.contact_method)
    TextView mContactMethod;
    @BindView(R.id.study_store)
    TextView mStudyStore;
    Unbinder unbinder;
    @BindView(R.id.my_notSignTask)
    TextView mMyNotSignTask;
    @BindView(R.id.exit_account)
    TextView mExitAccount;
    @BindView(R.id.tv_modifyPassword)
    TextView tvModifyPassword;
    @BindView(R.id.my_about)
    TextView mMyAbout;
    private Context mContext;

    public static MyFragment newInstance(String s) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wd, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.mContext = getActivity();
        initData();
        return view;
    }

    private void initData() {
        //获取用户名
        String mUserName = (String) SPUtils.getSp(mContext, "userName", "");
        mTvUserName.setText(mUserName);
        showOrHindDQS();
    }

    private void showOrHindDQS() {
        String roleId=(String) SPUtils.getSp(getActivity(),"roleId","");
        int gridLevel=(int)SPUtils.getSp(getActivity(),"gridLevel",-1);
        if(3==gridLevel||4==gridLevel){//假如是34级用户，直接显示代签收
            mMyNotSignTask.setVisibility(View.VISIBLE);
        }else {
            if(roleId.contains("3")){//审核员
                mMyNotSignTask.setVisibility(View.VISIBLE);
            }else {
                mMyNotSignTask.setVisibility(View.GONE);
            }
        }


    }

    @OnClick({R.id.my_msg, R.id.query_history,R.id.my_about, R.id.contact_method, R.id.study_store, R.id.my_notSignTask, R.id.exit_account,R.id.tv_modifyPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_notSignTask:
                startActivity(NotSignTaskActivity.class);
                break;
            case R.id.my_msg:
                startActivity(MessageActivity.class);
                break;
            case R.id.query_history:
                startActivity(QueryRecordActivity.class);
                break;
            case R.id.contact_method:
                startActivity(ContactMethodActivity.class);
                break;
            case R.id.study_store:
                startActivity(KnowledgeBaseActivity.class);
                break;
            case R.id.tv_modifyPassword:
                startActivity(ModifyPasswordActivity.class);
                break;
            case R.id.my_about://关于
                startActivity(AboutActivity.class);
                break;
            case R.id.exit_account://注销账号
                showExitDialog();
                break;
        }
    }

    private void showExitDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("确认要注销登录?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        deleteAccount();
                    }
                });
        normalDialog.setNegativeButton("关闭",null);
        // 显示
        normalDialog.show();
    }
    private void deleteAccount() {
                String sessionId = (String) SPUtils.getSp(getActivity(), "sessionId", "");
                NetManager.loginOut(sessionId, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        clearLoginMsg();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        clearLoginMsg();
                    }
                });
    }

    private void clearLoginMsg() {

        boolean clearSuccess = SPUtils.clearLoginMsg(mContext);
        if (clearSuccess) {
            mHandler.sendEmptyMessage(0);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(LoginActivity.class);
            ((Activity)mContext).finish();
        }
    };
    /**
     *  @author Tianfy
     *  @time 2017/8/31  10:13
     *  @describe 跳转到其他Activity的方法
     */
    private void startActivity(Class<? extends AppCompatActivity> clazz){
        FragmentActivity activity = getActivity();
        activity.startActivity(new Intent(activity, clazz));
        activity.overridePendingTransition(R.anim.activity_enter_anim,R.anim.activity_exit_anim);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
