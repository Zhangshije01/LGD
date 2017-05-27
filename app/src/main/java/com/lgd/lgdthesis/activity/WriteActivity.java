package com.lgd.lgdthesis.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.bean.MainFindBean;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.databinding.ActivityWriteBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import ren.qinc.edit.PerformEdit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WriteActivity extends BasesActivity {

    ActivityWriteBinding mBinding;
    private String photoPath;//拍照后图片保存的路径
    private Bitmap res;
    private String article_type;
    private UserBean userBean;
    private PerformEdit performEdit;
    private static final int CAMERA_SUCCESS = 100;
    private static final int PHOTO_SUCCESS = 101;

    private View popup_view_map;//地图
    private View popup_view_setting;//设置
    private View popup_view_size;//字体设置
    private PopupWindow popupWindow;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private List<String> list_map = new ArrayList<>();
    private ListView listView_map;

    public static void start(Context context) {
        Intent intent = new Intent(context, WriteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_write);
        initView();
        initTitlesize();
        initMapDate();


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

        mBinding.ivWriteMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupWindow(popup_view_map);
                mBinding.popuWriteZhezhao.setVisibility(View.VISIBLE);
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 100);
            }
        });

        mBinding.ivWriteSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupWindow(popup_view_setting);
                mBinding.popuWriteZhezhao.setVisibility(View.VISIBLE);
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.RIGHT, 0, 220);
            }
        });

        mBinding.ivWriteSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupWindow(popup_view_size);
                mBinding.popuWriteZhezhao.setVisibility(View.VISIBLE);
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER, 0, 220);
            }
        });

        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void initView() {
        popup_view_map = getLayoutInflater().inflate(R.layout.popup_layout_map_location, null);
        popup_view_setting = getLayoutInflater().inflate(R.layout.popup_write_setting, null);
        popup_view_size = getLayoutInflater().inflate(R.layout.popup_write_size, null);
        listView_map = (ListView) popup_view_map.findViewById(R.id.listview_popup_map);
    }

    public void initTitlesize() {
        performEdit = new PerformEdit(mBinding.editWriteBody);
        performEdit.setDefaultText("");
        mBinding.editWriteBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLength = mBinding.editWriteBody.getText().toString().length();
                if (textLength >= 0) {
                    mBinding.tvWriteNum.setText(textLength + " 字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //rxJava
    public Observable<String> writeEditLength() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                mBinding.editWriteBody.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int textLength = mBinding.editWriteBody.getText().toString().length();
                        if (textLength >= 0) {
                            subscriber.onNext(textLength + " 字");
                            subscriber.onCompleted();
                        } else {
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

    public void takePhoto() {
        final CharSequence[] items = {"手机相册", "相机拍摄"};
        AlertDialog dlg = new AlertDialog.Builder(WriteActivity.this).setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //这里item是根据选择的方式,
                        //在items数组里面定义了两种方式, 拍照的下标为1所以就调用拍照方法
                        if (item == 1) {
                            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                        } else {
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
                    if (bitmap != null) {
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(WriteActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]" + 1 + "[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = mBinding.editWriteBody.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = mBinding.editWriteBody.getEditableText();
                        if (index < 0 || index >= edit_text.length()) {
                            edit_text.append(spannableString);
                        } else {
                            edit_text.insert(index, spannableString);
                        }

                    } else {
                        ToastUtils.show("获取图片失败");
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if (originalBitmap1 != null) {
                        bitmap = resizeImage(originalBitmap1, 650, 500);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(WriteActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        SpannableString spannableString = new SpannableString("[local]" + 1 + "[/local]");
                        //  用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在位置
                        int index = mBinding.editWriteBody.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = mBinding.editWriteBody.getEditableText();
                        if (index < 0 || index >= edit_text.length()) {
                            edit_text.append(spannableString);
                        } else {
                            edit_text.insert(index, spannableString);
                        }
                    } else {
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
     *
     * @param originalBitmap 原始的Bitmap
     * @param newWidth       自定义宽度
     * @return 缩放后的Bitmap
     */
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义欲转换成的宽、高
//            int newWidth = 200;
//            int newHeight = 200;
        //计算宽、高缩放率
        float scanleWidth = (float) newWidth / width;
        float scanleHeight = (float) newHeight / height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth, scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    public void sendArticle() {
        final String article_title = mBinding.editWriteTitle.getText().toString();
        final String article_body = mBinding.editWriteBody.getText().toString();
        userBean = LGDApplication.getInstance().getUserBean();
        LogUtils.d("write userbean" + userBean.toString());
        final CharSequence[] items = {"论文", "感悟", "教育", "学术", "闲谈"};
        AlertDialog dlg = new AlertDialog.Builder(WriteActivity.this).setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 1) {
                            article_type = "论文";
                        } else if (item == 2) {
                            article_type = "感悟";
                        } else if (item == 3) {
                            article_type = "教育";
                        } else if (item == 4) {
                            article_type = "学术";
                        } else if (item == 5) {
                            article_type = "闲谈";
                        }

                        FindCircleBean findCircleBean = new FindCircleBean();
                        if (userBean != null) {
                            findCircleBean.setUserBean(userBean);
                        }
                        findCircleBean.setUser_name(userBean.getUserName());
                        findCircleBean.setArticle_name(article_title);
                        findCircleBean.setArticle_type(article_type);
                        findCircleBean.setDetails(article_body);
                        findCircleBean.setTime(Util.Time(new Date()));
                        findCircleBean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    ToastUtils.show("发表成功");
                                    setMainFind(userBean, article_title);
                                    pushMessage(userBean);
                                } else {
                                    ToastUtils.show(e.getMessage());
                                }
                            }
                        });

                    }
                }).create();
        dlg.show();

    }

    public void pushMessage(UserBean userBean) {
        try {
            BmobPushManager<BmobInstallation> manager = new BmobPushManager<BmobInstallation>();
            JSONObject json = new JSONObject();
            json.put("tag", "new");
            json.put("user", userBean.getUserName());
            json.put("time", userBean.getCreatedAt());
            manager.pushMessageAll(json,new PushListener() {

                @Override
                public void done(BmobException e) {
                    if(e ==null){
                        LogUtils.d("push成功");
                    }else{
                        LogUtils.d("push 失败");
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setMainFind(UserBean user, String article_title) {
        MainFindBean mainFindBean = new MainFindBean();
        if (user != null) {
            LogUtils.d("---------");
            mainFindBean.setUserBean(user);
        }
        LogUtils.d("userbean" + userBean.getUserName());
        mainFindBean.setUser_name(userBean.getUserName());
        mainFindBean.setUser_avator(user.getUserAnvator());
        mainFindBean.setArticle_name(article_title);
        mainFindBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtils.d("mainfindBean 成功");
                } else {
                    LogUtils.d("mainfindBean " + e.getMessage());
                }
            }
        });
    }

    public void initMapDate() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initLocation();

        final MapAdapter adapter = new MapAdapter(this, list_map);
        listView_map.setAdapter(adapter);
        listView_map.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = adapter.getItem(position);
                mBinding.popuWriteZhezhao.setVisibility(View.GONE);
                mBinding.tvWriteLocation.setText(location);
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }


    public void setPopupWindow(View view_popup_window) {
        popupWindow = new PopupWindow(view_popup_window, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mBinding.popuWriteZhezhao.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBinding.popuWriteZhezhao.setVisibility(View.GONE);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
//        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        LogUtils.d("mLocationClient.start()" + (mLocationClient.isStarted()));
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            int code = location.getLocType();
            if (code == 61 || code == 66 || code == 161) {
                String addr = location.getAddrStr();
                String locationdescribe = location.getLocationDescribe();
                list_map.add(addr);
                list_map.add(locationdescribe);
                List<Poi> list = location.getPoiList();    // POI数据
                if (list != null) {
                    for (Poi p : list) {
                        list_map.add(p.getName());
                    }
                }
            } else {
                ToastUtils.show("定位失败");
            }
            if (mLocationClient.isStarted()) {
                mLocationClient.unRegisterLocationListener(myListener);
                mLocationClient.stop();
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    class MapAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context mContext;
        private List<String> mList;

        public MapAdapter(Context mContext, List<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String s = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.item_write_map, null);
            TextView nameText = (TextView) view.findViewById(R.id.tv_write_map);
            nameText.setText(s);
            return view;
        }
    }

}
