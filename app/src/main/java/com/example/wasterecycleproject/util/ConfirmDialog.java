package com.example.wasterecycleproject.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.wasterecycleproject.R;

public class ConfirmDialog extends Dialog implements View.OnClickListener {

    TextView tv_msg;
    Button btn_ok;

    public ConfirmDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setContentView(R.layout.dialog_confirm);     //다이얼로그에서 사용할 레이아웃입니다.

        initView();
    }

    public void initView() {
        tv_msg = (TextView) findViewById(R.id.tv_message_dialog);

        btn_ok = (Button) findViewById(R.id.btn_ok_dialog);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_ok_dialog:
                dismiss();
                break;
        }
    }

    public void setMessageAndShow(String message) {
        setMessage(message);
        show();
    }

    public Button getOkBtn() {
        return btn_ok;
    }

    public void setMessage(String message) {
        tv_msg.setText(message);
    }

    public void setOkBtnDismiss() {
        btn_ok.setOnClickListener(this);
    }
}