package com.lgd.lgdthesis.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.bean.FriendBean;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.cache.LGDSharedprefrence;
import com.lgd.lgdthesis.databinding.ActivityUserEditBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserEditActivity extends AppCompatActivity {

    ActivityUserEditBinding mBinding;

    private String photoPath;//拍照后图片保存的路径
    private String anvatorUrl;//图片上传成功后保存地址
    private ProgressDialog pd;//放图片时给用户的提示
    private String et_username;
    private UserBean userBean;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserEditActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_edit);
        userBean = LGDApplication.getInstance().getUserBean();
        // 利用ImageLoader将用户选取的图片放到ivAvatar中显示
        ImageLoader.getInstance().displayImage(userBean.getUserAnvator(), mBinding.ivUserDetailAnvator);
        if(!TextUtils.isEmpty(userBean.getUserName())){
            mBinding.etUsername.setText(userBean.getUserName());
        }
        mBinding.llUserEditAnvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initIntentPhoto();
            }
        });
        //完成
        mBinding.tvUserEditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userBean== null){
                    ToastUtils.show("请登录");
                }
                userBean.setUserAnvator(anvatorUrl);
                et_username = mBinding.etUsername.getText().toString();
                if(TextUtils.isEmpty(et_username)){
                    mBinding.etUsername.setText("匿名");
                }
                userBean.setUserName(et_username);
                userBean.update(userBean.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            ToastUtils.show("保存成功");
                            LGDSharedprefrence.setUserName(et_username);
                            LGDSharedprefrence.setUserAvator(anvatorUrl);
                            addFriend(userBean);
                        }else{
                            ToastUtils.show(e.getMessage());
                        }
                    }
                });
            }
        });
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addFriend(UserBean userBean){
        FriendBean friendBean = new FriendBean();
        friendBean.setAnvator(userBean.getUserAnvator());
        friendBean.setName(userBean.getUserName());
        friendBean.setPhone(userBean.getUserAccount());
        friendBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    LogUtils.d(" friend ok");
                }
            }
        });
    }

    public void initIntentPhoto(){
        //利用Intent选择器(IntentChooser)实现弹出对话框
        //让用户进行拍照或者选图来作为头像

        //创建打开图库的intent
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        //创建打开系统拍照程序的intnet
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //告诉系统拍照程序，拍照完毕后照片保存的位置
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".jpg");
        photoPath = file.getAbsolutePath();
        Uri value = Uri.fromFile(file);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, value);

        //创建Intent选择器(IntentChooser)
        Intent intent = Intent.createChooser(intent1, "选择头像...");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent2});

        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==101){
            //从data中找到作为头像图片的本地地址
            String filePath;
            if(data!=null){
                //头像图片是用户从图库选择的图片
                //uri是用户选取的图片在MeidaStroe数据表的位置
                Uri uri = data.getData();
                //根据uri找到该图片在SD上的真实位置
                Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                cursor.moveToNext();
                filePath = cursor.getString(0);
                cursor.close();
            }else{
                //头像图片是用户从相机拍摄回来的
                filePath = photoPath;
            }
            //显示一个提示框，告诉用户正在上传
            pd = ProgressDialog.show(this, "", "上传中...");

            //利用BmobSDK提供的BmobFile类上传图片
            final BmobFile bf = new BmobFile(new File(filePath));
            bf.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(null == e){
                        // 上传文件（图片）成功
                        // 获取该文件（图片）在服务器上的地址
                        anvatorUrl = bf.getFileUrl();
                        Log.d("TAG" , anvatorUrl);
                        // 利用ImageLoader将用户选取的图片放到ivAvatar中显示
                        ImageLoader.getInstance().displayImage(anvatorUrl, mBinding.ivUserDetailAnvator);
                        // 让提示框消失
                        pd.dismiss();
                    }else{
                        ToastUtils.show("上传失败 \n" +e.getMessage());
                    }
                }
            });
        }
    }
}
