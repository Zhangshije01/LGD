package com.lgd.lgdthesis.adapter.chatAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lgd.lgdthesis.R;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 同意添加好友的agree类型
 */
public class AgreeHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener {

  protected TextView tv_time;
  protected TextView tv_message;

  public AgreeHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
    super(context, root, R.layout.item_chat_agree, listener);
  }

  @Override
  public void bindData(Object o) {
    tv_message = (TextView) view_holder.findViewById(R.id.tv_message);
    tv_time = (TextView) view_holder.findViewById(R.id.tv_time);
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    String content = message.getContent();
    tv_message.setText(content);
    tv_time.setText(time);
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}
