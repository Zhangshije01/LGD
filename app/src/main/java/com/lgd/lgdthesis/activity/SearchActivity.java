package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.FriendBean;
import com.lgd.lgdthesis.databinding.ActivitySearchBinding;
import com.lgd.lgdthesis.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding mBinding;
    public static void start(Context context){
        Intent intent = new Intent(context,SearchActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        mBinding.editSearch.setOnEditorActionListener(myEditerActionListener);
//        cet_search.setOnEditorActionListener(myEditerActionListener);
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    TextView.OnEditorActionListener myEditerActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                String content = mBinding.editSearch.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ToastUtils.show("请输入内容");
                }else{
                    search(content);
                }
                return true;
            }
            return false;
        }
    };

    public void search(String content){
        BmobQuery<FriendBean> query = new BmobQuery<>();
        query.findObjects(new FindListener<FriendBean>() {
                    @Override
                    public void done(List<FriendBean> list, BmobException e) {
                        if(e == null){
                            ToastUtils.show("list"+list.size());
                        }
                    }
                });
    }
}
