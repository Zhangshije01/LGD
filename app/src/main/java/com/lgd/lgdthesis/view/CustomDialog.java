package com.lgd.lgdthesis.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgd.lgdthesis.R;

import java.lang.reflect.Field;


/**
 * 自定义dailog用于显示网络提示
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context) {
        super(context);
    }

    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private String neutralbuttonText;
        private View contentView;
        private boolean mCancelable = true;

        private OnClickListener positiveButtonClickListener,
                negativeButtonClickListener, neutralbuttonButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(int neutralbuttonText,
                                        OnClickListener listener) {
            this.neutralbuttonText = (String) context
                    .getText(neutralbuttonText);
            this.neutralbuttonButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(String neutralbuttonText,
                                        OnClickListener listener) {
            this.neutralbuttonText = neutralbuttonText;
            this.neutralbuttonButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 设置dialog点击确定后是否关闭窗口
         *
         * @param dialog
         * @param isShow
         */
        public void setDialogCanCancel(DialogInterface dialog, boolean isShow) {
            try {
                Field field = dialog.getClass().getSuperclass()
                        .getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(dialog, isShow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.custom_dialog_style);
            View layout = inflater.inflate(R.layout.custom_dialog_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
            LinearLayout dialog_ll = (LinearLayout) layout
                    .findViewById(R.id.dialog_ll);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.dialog_positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.dialog_positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
                dialog_ll.setVisibility(View.VISIBLE);
            } else {
                layout.findViewById(R.id.dialog_positiveButton).setVisibility(
                        View.GONE);
            }
            if (neutralbuttonText != null) {
                ((Button) layout.findViewById(R.id.dialog_NeutralButton))
                        .setText(neutralbuttonText);
                if (neutralbuttonButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.dialog_NeutralButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    neutralbuttonButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_NEUTRAL);
                                }
                            });
                }
                dialog_ll.setVisibility(View.VISIBLE);
            } else {
                layout.findViewById(R.id.dialog_NeutralButton).setVisibility(
                        View.GONE);
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.dialog_negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.dialog_negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
                dialog_ll.setVisibility(View.VISIBLE);
            } else {
                layout.findViewById(R.id.dialog_negativeButton).setVisibility(
                        View.GONE);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.dialog_message))
                        .setText(message);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.dialog_content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.dialog_content))
                        .addView(contentView, new LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));
            }
            dialog.setCancelable(mCancelable);
            if (mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }

        public CustomDialog show() {
            CustomDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

}