package com.lgd.lgdthesis.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.databinding.ActivityWriteBinding;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import ren.qinc.edit.PerformEdit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WriteActivity extends AppCompatActivity {

    ActivityWriteBinding mBinding;
    private String photoPath;//拍照后图片保存的路径
    private Bitmap res;
    private String article_type;
    private UserBean userBean;
    private PerformEdit performEdit;
    private static final int CAMERA_SUCCESS = 100;
    private static final int PHOTO_SUCCESS = 101;

    public static void start(Context context){
        Intent intent = new Intent(context,WriteActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_write);
        performEdit = new PerformEdit(mBinding.editWriteBody);
        performEdit.setDefaultText("");
        mBinding.editWriteBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLength = mBinding.editWriteBody.getText().toString().length();
                if(textLength>=0){
                    mBinding.tvWriteNum.setText(textLength+" 字");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.ivWritePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        mBinding.tvWriteSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendArticle();
            }
        });

        mBinding.ivWriteLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performEdit.redo();
            }
        });
        mBinding.ivWriteRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performEdit.undo();
            }
        });
        //清除历史    mPerformEdit.clearHistory();

    }
    //rxJava
    public Observable<String> writeEditLength(){
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                mBinding.editWriteBody.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int textLength = mBinding.editWriteBody.getText().toString().length();
                        if(textLength>=0){
                            subscriber.onNext(textLength+" 字");
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(new Throwable("字数有误"));
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public void takePhoto(){
        final CharSequence[] items = { "手机相册", "相机拍摄" };
        AlertDialog dlg = new AlertDialog.Builder(WriteActivity.this).setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int item) {
                        //这里item是根据选择的方式,
                        //在items数组里面定义了两种方式, 拍照的下标为1所以就调用拍照方法
                        if(item==1){
                            Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                        }else{
                            Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                            getImage.addCategory(Intent.CATEGORY_OPENABLE);
                            getImage.setType("image/*");
                            startActivityForResult(getImage, PHOTO_SUCCESS);
                        }
                    }
                }).create();
        dlg.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intent.getData();
                    Bitmap bitmap = null;
                    try {
                        Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        bitmap = resizeImage(originalBitmap, 650, 500);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(bitmap != null){
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(WriteActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = mBinding.editWriteBody.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = mBinding.editWriteBody.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }

                    }else{
                        ToastUtils.show("获取图片失败");
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if(originalBitmap1 != null){
                        bitmap = resizeImage(originalBitmap1, 650, 500);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(WriteActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = mBinding.editWriteBody.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = mBinding.editWriteBody.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        ToastUtils.show("获取图片失败");
                    }
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 图片缩放
     * @param originalBitmap 原始的Bitmap
     * @param newWidth 自定义宽度
     * @return 缩放后的Bitmap
     */
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义欲转换成的宽、高
//            int newWidth = 200;
//            int newHeight = 200;
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/width;
        float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
        return resizedBitmap;
    }

    public void sendArticle(){
        final String article_title=mBinding.editWriteTitle.getText().toString();
        final String article_body = mBinding.editWriteBody.getText().toString();
        userBean = LGDApplication.getInstance().getUserBean();
        final CharSequence[] items = { "论文", "感悟" , "教育" , "学术" , "闲谈" };
        AlertDialog dlg = new AlertDialog.Builder(WriteActivity.this).setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int item) {
                        if(item==1){
                            article_type = "论文";
                        }else if(item == 2){
                            article_type = "感悟";
                        }else if(item == 3){
                            article_type = "教育";
                        }else if(item == 4){
                            article_type = "学术";
                        }else if(item == 5){
                            article_type = "闲谈";
                        }

                        FindCircleBean findCircleBean = new FindCircleBean();
                        if(userBean != null){
                            findCircleBean.setUserBean(userBean);
                        }
                        findCircleBean.setArticle_name(article_title);
                        findCircleBean.setArticle_type(article_type);
                        findCircleBean.setDetails(article_body);
                        findCircleBean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    ToastUtils.show("发表成功");
                                }else{
                                    ToastUtils.show(e.getMessage());
                                }
                            }
                        });

                    }
                }).create();
        dlg.show();

    }

}
