package com.goertek.microvision.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import com.goertek.microvision.R;

import picop.interfaces.def.PicoP_ColorModeE;

import static com.goertek.microvision.MessageCenter.colorMode;
import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_COLOR_MODE;
import static com.goertek.microvision.Utils.MSG_SET_COLOR_MODE;

/**
 * Created by carey.wang on 2017/4/7.
 */

public class ColorMode extends Fragment {

	private SeekBar mSeekBarColormode = null;
	private static TextView mTvMaxValue,mTvSetColormodeCurrent, mTvGetColormodeCurrent,mTvGetColormodeStartup, mTvGetColormodeFactory;
	private Button mBtnSetCommit, mBtnSetNoCommit, mBtnGetCurrent, mBtnGetStartup, mBtnGetFactory;
	private int mSetMode = 0;
	private View rootView;

	public static final int MSG_COLORMODE_RESPONSE_GET   = 90001;
	public static Handler colorModeHandler =  new Handler() {
		// when handler.message() called, below code will be triggered.
		public void handleMessage(Message msg) {
			//super.handleMessage(msg);
			switch (msg.what) {
				case MSG_COLORMODE_RESPONSE_GET:
					Bundle b = msg.getData();
					int result = b.getInt("result");
					int type = b.getInt("type");
					if(result==0) {
						if(type ==0){
							mTvGetColormodeCurrent.setText("" + colorMode.intToEnum(resultData.getColormode()));
						} else if(type ==1){
							mTvGetColormodeStartup.setText(""+ colorMode.intToEnum(resultData.getColormode()));
						} else if (type ==2){
							mTvGetColormodeFactory.setText(""+ colorMode.intToEnum(resultData.getColormode()));
						}
					} else{
						String STR = b.getString("STR");
						if(type ==0 ){
							mTvGetColormodeCurrent.setText("fun run failed, errno = " + STR);
						} else if (type ==1 ){
							mTvGetColormodeStartup.setText("fun run failed, errno = " + STR);
						} else if(type ==2 ) {
							mTvGetColormodeFactory.setText("fun run failed, errno = " + STR);
						}
					}
					break;
			}
		}
	};

	public ColorMode(){}
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_colormode, container, false);
		initView();
		return rootView;
	}
	private void initView() {
        /* Set Brightnes */
		//Textview should init before seekbar, otherwise seekbar changelistener will crash.
		mTvSetColormodeCurrent = (TextView) rootView.findViewById(R.id.fragment_colormode_tv_set_curr_value);
		mTvSetColormodeCurrent.setText("brilliant");

		mSeekBarColormode = (SeekBar)rootView.findViewById(R.id.fragment_colormode_seekbar_colormode);
		mSeekBarColormode.setOnSeekBarChangeListener(new ColorMode.OnSeekBarChangeListenerImp());
		mSeekBarColormode.setEnabled(true);
		mSeekBarColormode.setMax(100);
		mSeekBarColormode.setProgress(0);

		mTvMaxValue = (TextView)rootView.findViewById(R.id.fragment_colormode_tv_max);
		mTvMaxValue.setText("inverted");
		mBtnSetCommit = (Button)rootView.findViewById(R.id.fragment_colormode_btn_set_commit);
		mBtnSetCommit.setOnClickListener(btnClickListener);
		mBtnSetNoCommit = (Button) rootView.findViewById(R.id.fragment_colormode_btn_set_no_commit);
		mBtnSetNoCommit.setOnClickListener(btnClickListener);
        /* Set Brightnes */

        /* Get Brightnes */
		mBtnGetCurrent = (Button)rootView.findViewById(R.id.fragment_colormode_btn_get_current);
		mBtnGetCurrent.setOnClickListener(btnClickListener);
		mBtnGetStartup = (Button)rootView.findViewById(R.id.fragment_colormode_btn_get_startup);
		mBtnGetStartup.setOnClickListener(btnClickListener);
		mBtnGetFactory = (Button)rootView.findViewById(R.id.fragment_colormode_btn_get_factory);
		mBtnGetFactory.setOnClickListener(btnClickListener);

		mTvGetColormodeCurrent = (TextView)rootView.findViewById(R.id.fragment_colormode_tv_get_current);
		mTvGetColormodeCurrent.setText("");
		mTvGetColormodeStartup = (TextView)rootView.findViewById(R.id.fragment_colormode_tv_get_startup);
		mTvGetColormodeStartup.setText("");
		mTvGetColormodeFactory = (TextView)rootView.findViewById(R.id.fragment_colormode_tv_get_factory);
		mTvGetColormodeFactory.setText("");
        /* Get Brightnes */
	}


	private class OnSeekBarChangeListenerImp implements
			SeekBar.OnSeekBarChangeListener {
		// progress
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			//setMoveTextLayout();
			mTvSetColormodeCurrent.setText(progressToMode(progress));
		}
		// start
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		// stop
		public void onStopTrackingTouch(SeekBar seekBar) {

			mSetMode = progressToType(seekBar.getProgress());
			if(seekBar.getProgress()<33){
				seekBar.setProgress(0);
			} else if(seekBar.getProgress()<67){
				seekBar.setProgress(50);
			} else {
				seekBar.setProgress(100);
			}
		}
	}

	private static String progressToMode(int progress){
		String str = "";
		if(progress < 33){
			str = "brilliant";
		} else if (progress<67){
			str = "standard";
		} else{
			str = "inverted";
		}
		return str;
	}
	private static int progressToType(int progress){
		int ret = 0;
		if(progress < 33){
			ret = 0;
		} else if (progress<67){
			ret = 1;
		} else{
			ret = 2;
		}
		return ret;
	}
	/* { button code */
	public View.OnClickListener btnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.fragment_colormode_btn_set_commit:{
					Message msg0 = messageHandler.obtainMessage(MSG_SET_COLOR_MODE);
					Bundle b = new Bundle();
					b.putBoolean("commit", true);
					b.putInt("mode", mSetMode);
					msg0.setData(b);
					messageHandler.sendMessage(msg0);
					break;
				}
				case R.id.fragment_colormode_btn_set_no_commit:{
					Message msg0 = messageHandler.obtainMessage(MSG_SET_COLOR_MODE);
					Bundle b = new Bundle();
					b.putBoolean("commit", false);
					b.putInt("mode", mSetMode);
					msg0.setData(b);
					messageHandler.sendMessage(msg0);
					break;
				}
				case R.id.fragment_colormode_btn_get_current:{
					Message msg0 = messageHandler.obtainMessage(MSG_GET_COLOR_MODE);
					Bundle b = new Bundle();
					b.putInt("type", 0);
					msg0.setData(b);
					messageHandler.sendMessage(msg0);
					break;
				}
				case R.id.fragment_colormode_btn_get_startup:{
					Message msg0 = messageHandler.obtainMessage(MSG_GET_COLOR_MODE);
					Bundle b = new Bundle();
					b.putInt("type", 1);
					msg0.setData(b);
					messageHandler.sendMessage(msg0);
					break;
				}
				case R.id.fragment_colormode_btn_get_factory:{
					Message msg0 = messageHandler.obtainMessage(MSG_GET_COLOR_MODE);
					Bundle b = new Bundle();
					b.putInt("type", 2);
					msg0.setData(b);
					messageHandler.sendMessage(msg0);
					break;
				}
				default:{
					break;
				}
			}
		}
	};
}
