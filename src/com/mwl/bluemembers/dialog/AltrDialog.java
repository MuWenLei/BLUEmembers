package com.mwl.bluemembers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mwl.bluemembers.R;

/**
 * @author Mu
 * @date 2015-3-6
 * @description 普通提示对话框
 */
public class AltrDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private Handler handler;
	private TextView ok;
	private TextView text;
	private String msg;
	private int flag;
	public AltrDialog(Context context, Handler handler, String msg,int flag) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.handler = handler;
		this.context = context;
		this.flag=flag;
		this.msg = msg;
		setContentView(R.layout.alert_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		text = (TextView) findViewById(R.id.customer_text);
		ok = (TextView) findViewById(R.id.customer_ok);
		text.setText(msg);

		ok.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.customer_ok:
			Message message = new Message();
			message.what = 40;
			message.arg1=flag;
			handler.sendMessage(message);
			dismiss();
			break;
		default:
			break;
		}

	}

}
