package com.lgd.lgdthesis.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.ArticleCommentBean;
import com.lgd.lgdthesis.utils.SoftKeyBoardListener;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.utils.Util;
import com.umeng.socialize.UMShareAPI;

import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/8/10.
 */
public class PingLunDialog {

    private static Activity mContext;

    // 控制输入键盘弹出
    private static Handler mControlHandler = new Handler();


    private static int screenWidth;//屏幕宽度
    private static int screenHeight;//屏幕高度

    private static int screenKeyBoard_height;


    private static String curr_user_name;

    private static PriorityListener listener;

    // 提示请登录的Dialog
    private static AlertDialog toLoginDialog;

    public static UMShareAPI mShareAPI;


    private static String strComment;
    private static EditText et_text;
    public static TextView btn_change;
    public static TextView btn_cancel;

    private static String username;
    private static String userarticleName;

    public PingLunDialog(Activity activity, String curr_user_name, PriorityListener listener, String username, String userarticleName) {
        this.mContext = activity;
        this.curr_user_name = curr_user_name;

        this.username=username;
        this.userarticleName=userarticleName;

        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        mShareAPI = UMShareAPI.get(mContext);
        this.listener = listener;

    }

    /**
     *  自定义Dialog监听器 
     */
    public interface PriorityListener {
        public void refreshPriority();
    }

    ;

    /**
     * 异步弹出输入法
     */
    public static void popupInputMethodWindow() {
        mControlHandler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 自定义提交评论dialog
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void showChangeDialog() {
        // 获取dialog
        final CustomDialog dialog = new CustomDialog(mContext,
                R.style.custom_dialog_style);


        // 获取布局
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_contact_changegroup, null);
        // 确定
        btn_change = (TextView) view.findViewById(R.id.btn_change);
        // 取消
        btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        // 输入评论
        et_text = (EditText) view.findViewById(R.id.et_text);
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String str = et_text.getText().toString();
                if ("".equals(str)) {
                    btn_change.setTextColor(mContext.getResources().getColor(R.color.color_view_gray));
                    btn_change.setBackground(mContext.getResources().getDrawable(R.drawable.pinglun_null));

                } else {
                    btn_change.setTextColor(mContext.getResources().getColor(R.color.common_white));
                    btn_change.setBackground(mContext.getResources().getDrawable(R.drawable.pinglun_full));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int lines = et_text.getLineCount();
                int size = et_text.length();
                // 限制最大输入行数
                if (lines > 8 || size > 250) {
                    String str = s.toString();
                    int cursorStart = et_text.getSelectionStart();
                    int cursorEnd = et_text.getSelectionEnd();
                    if (cursorStart == cursorEnd && cursorStart < str.length()
                            && cursorStart >= 1) {
                        str = str.substring(0, cursorStart - 1)
                                + str.substring(cursorStart);
                    } else {
                        str = str.substring(0, s.length() - 1);
                    }
                    // setText会触发afterTextChanged的递归
                    et_text.setText(str);
                    // setSelection用的索引不能使用str.length()否则会越界
                    et_text.setSelection(et_text.getText().length());
                }
            }
        });
        // 点击操作
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String uid = SharePreferences.getUserId(mContext, "").toString();
//                if (uid.isEmpty() || "".equals(uid)) {
//                    showStartDialog();
//                } else {
                    dialog.dismiss();
                    strComment = et_text.getText().toString().trim();
                    if (TextUtils.isEmpty(strComment)) {
                        ToastUtils.show( "提交评论不能为空");
                        return;
                    }
                    commitComment(strComment,et_text);
                }
//            }
        });

        // 取消操作
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_text.setText("");
                dialog.dismiss();
            }
        });

//        WindowManager wm = mContext.getWindowManager();
//
//        final int width = wm.getDefaultDisplay().getWidth();
//        int height1 = wm.getDefaultDisplay().getHeight();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                screenWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 120);

        // 显示
//        dialog.setView(view, 0, 0, 0, 0);
        dialog.addContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
         /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */

        lp.x = 0; // 新位置X坐标
        lp.y = screenKeyBoard_height - 23; // 新位置Y坐标
        lp.width = screenWidth; // 宽度
//                lp.height = 300; // 高度
        lp.alpha = 1.0f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);

        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

                screenKeyBoard_height = height;
            }

            @Override
            public void keyBoardHide(int height) {
                screenKeyBoard_height = 0;
            }
        });
//        setDialogAttributes(writeDialog,true,100,1000);
    }

    public static void setDialogAttributes(Dialog dlg, boolean right, int xpos, int ypos) {
        try {
            Window win = dlg.getWindow();
            WindowManager.LayoutParams wl = win.getAttributes();
            if (right) {
                wl.gravity = android.view.Gravity.RIGHT | android.view.Gravity.TOP;
            } else {
                wl.gravity = android.view.Gravity.LEFT | android.view.Gravity.TOP;
            }
            wl.x = xpos;
            wl.y = ypos;
            wl.height = mContext.getWindowManager().getDefaultDisplay().getHeight();
            wl.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            wl.alpha = 1.0f;
            wl.dimAmount = 0.0f;
            win.setAttributes(wl);
        } catch (Exception e) {
//            Log.e(TAG, "setWinAttributes", e);
        }
    }

    /**
     * 监听软键盘的状态
     */

    public static void softKeyBoardState(EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)) {
            imm.showSoftInput(editText, 0);
            //软键盘已弹出
        } else {
            //软键盘未弹出
        }
    }


    /**
     * 提交评论
     */
    public static void commitComment(final String content, final EditText editText) {
        ToastUtils.show("提交评论"+content);
        ArticleCommentBean articleCommentBean = new ArticleCommentBean();
        articleCommentBean.setArticle_name(userarticleName);
        articleCommentBean.setComment(content);
        articleCommentBean.setCurr_user_name(curr_user_name);
        articleCommentBean.setFri_user_name(username);
        articleCommentBean.setTime(Util.Time(new Date()));
        articleCommentBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    ToastUtils.show("提交成功");
                }else{
                    ToastUtils.show("提交评论失败"+e.getMessage());
                }
            }
        });
    }

//    public static void showStartDialog(){
//        if (toLoginDialog == null) {
//
//            toLoginDialog = new AlertDialog.Builder(mContext).create();
//            if(toLoginDialog.isShowing()){
//                toLoginDialog.dismiss();
//            }
//            toLoginDialog.show();
//            Window view = toLoginDialog.getWindow();
//
//                view.setContentView(R.layout.dialog_login);
//
//
//            TextView weichat_login= (TextView) view.findViewById(R.id.weichat_login);
//            TextView phone_login= (TextView) view.findViewById(R.id.phone_login);
//            TextView qq_login= (TextView) view.findViewById(R.id.qq_login);
//            TextView more_login= (TextView) view.findViewById(R.id.more_login);
//
//            phone_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toLoginDialog.dismiss();
//                    toLoginDialog=null;
//                    Intent intent = new Intent(mContext, UserLoginActivity.class);
//                    mContext.startActivity(intent);
//                }
//            });
//            more_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toLoginDialog.dismiss();
//                    toLoginDialog=null;
//                    Intent intent = new Intent(mContext, UserLoginActivity.class);
//                    mContext.startActivity(intent);
//                }
//            });
//            weichat_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toLoginDialog.dismiss();
//                    toLoginDialog=null;
//                    if (Utils.isWeixinAvilible(mContext)){
////                        mShareAPI.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, authListener);
//                        mShareAPI.getPlatformInfo(mContext, SHARE_MEDIA.WEIXIN, infoListener);
//                    }else {
//                        ToastFactory.getToast(mContext, "你还没有未安装微信应用或者你的微信版本过低，请安装最新正式版微信！").show();
//                    }
//
//                }
//            });
//            qq_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toLoginDialog.dismiss();
//                    toLoginDialog=null;
//                    if (Utils.isQQClientAvailable(mContext)){
////                        mShareAPI.doOauthVerify(mContext, SHARE_MEDIA.QQ, authListener);
//                        mShareAPI.getPlatformInfo(mContext, SHARE_MEDIA.QQ, infoListener);
//                    }else {
//                        ToastFactory.getToast(mContext, "你还没有未安装QQ应用或者你的QQ版本过低，请安装最新正式版QQ！").show();
//                    }
////                    mShareAPI.doOauthVerify(mContext, SHARE_MEDIA.QQ, authListener);
//                }
//            });
//            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toLoginDialog.dismiss();
//                    toLoginDialog=null;
//                }
//            });
//        }else {
//            if(toLoginDialog!=null){
//                toLoginDialog=null;
//                showStartDialog();
//            }
//        }
//
//    }
}
