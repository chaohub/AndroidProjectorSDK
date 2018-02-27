package com.goertek.microvision.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.goertek.microvision.R;

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_BRIGHTNESS;
import static com.goertek.microvision.Utils.MSG_SET_BRIGHTNESS;

/**
 * Created by carey.wang on 2017/4/7.
 */

public class Brightness extends Fragment {

    private static final String TAG = "Brightness";

    private SeekBar mSeekBarBrightness = null;
    private View rootView;
    private static TextView mTvMaxValue,mTvSetBrightnessCurrent, mTvGetBrightnessCurrent,mTvGetBrightnessStartup, mTvGetBrightnessFactory;
    private Button mBtnSetCommit, mBtnSetNoCommit, mBtnGetCurrent, mBtnGetStartup, mBtnGetFactory;
    private int mProgress = 100;
    private float brightness = 0;
	public Brightness(){
    }

    public static final int MSG_BRIGHTNESS_RESPONSE_GET = 90001;
    public static Handler brightnessHandler =  new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_BRIGHTNESS_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    int type = b.getInt("type");
                    if(result==0) {
                        if(type ==0){
                            mTvGetBrightnessCurrent.setText("" + resultData.getBrightness());
                        } else if(type ==1){
                            mTvGetBrightnessStartup.setText(""+ resultData.getBrightness());
                        } else if (type ==2){
                            mTvGetBrightnessFactory.setText(""+ resultData.getBrightness());
                        }
                    } else{
                        String STR = b.getString("STR");
                        if(type ==0 ){
                            mTvGetBrightnessCurrent.setText("fun run failed, errno = " + STR);
                        } else if (type ==1 ){
                            mTvGetBrightnessStartup.setText("fun run failed, errno = " + STR);
                        } else if(type ==2 ) {
                            mTvGetBrightnessFactory.setText("fun run failed, errno = " + STR);
                        }
                    }
                    break;
            }
        }
    };
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_brightness, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        /* Set Brightnes */
        //Textview should init before seekbar, otherwise seekbar changelistener will crash.
        mTvSetBrightnessCurrent = (TextView) rootView.findViewById(R.id.fragment_brightness_tv_set_curr_value);
        mTvSetBrightnessCurrent.setText("1.0");

        mSeekBarBrightness = (SeekBar)rootView.findViewById(R.id.fragment_brightness_seekbar_brightness);
        mSeekBarBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        mSeekBarBrightness.setEnabled(true);
        mSeekBarBrightness.setMax(100);
        mSeekBarBrightness.setProgress(100);

        mTvMaxValue = (TextView)rootView.findViewById(R.id.fragment_brightness_tv_max);
        mTvMaxValue.setText("1.0");
        mBtnSetCommit = (Button)rootView.findViewById(R.id.fragment_brightness_btn_set_commit);
        mBtnSetCommit.setOnClickListener(btnClickListener);
        mBtnSetNoCommit = (Button) rootView.findViewById(R.id.fragment_brightness_btn_set_no_commit);
        mBtnSetNoCommit.setOnClickListener(btnClickListener);
        /* Set Brightnes */

        /* Get Brightnes */
        mBtnGetCurrent = (Button)rootView.findViewById(R.id.fragment_brightness_btn_get_current);
        mBtnGetCurrent.setOnClickListener(btnClickListener);
        mBtnGetStartup = (Button)rootView.findViewById(R.id.fragment_brightness_btn_get_startup);
        mBtnGetStartup.setOnClickListener(btnClickListener);
        mBtnGetFactory = (Button)rootView.findViewById(R.id.fragment_brightness_btn_get_factory);
        mBtnGetFactory.setOnClickListener(btnClickListener);

        mTvGetBrightnessCurrent = (TextView)rootView.findViewById(R.id.fragment_brightness_tv_get_current);
        mTvGetBrightnessCurrent.setText("");
        mTvGetBrightnessStartup = (TextView)rootView.findViewById(R.id.fragment_brightness_tv_get_startup);
        mTvGetBrightnessStartup.setText("");
        mTvGetBrightnessFactory = (TextView)rootView.findViewById(R.id.fragment_brightness_tv_get_factory);
        mTvGetBrightnessFactory.setText("");
        /* Get Brightnes */
    }


    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {
        // progress
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //setMoveTextLayout();
            float value = (float)progress/100;
            Log.i(TAG,"value = " + value +", progress "+ progress);
            mTvSetBrightnessCurrent.setText(""+value);
        }
        // start
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // stop
        public void onStopTrackingTouch(SeekBar seekBar) {

            mProgress = seekBar.getProgress();
        }
    }

    /* { button code */
    public OnClickListener btnClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_brightness_btn_set_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_BRIGHTNESS);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", true);
                    b.putFloat("brightness", (float)mProgress/100);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_brightness_btn_set_no_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_BRIGHTNESS);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", false);
                    b.putFloat("brightness", (float)mProgress/100);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_brightness_btn_get_current:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_BRIGHTNESS);
                    Bundle b = new Bundle();
                    b.putInt("type", 0);
                    b.putFloat("brightness",brightness);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_brightness_btn_get_startup:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_BRIGHTNESS);
                    Bundle b = new Bundle();
                    b.putInt("type", 1);
                    b.putFloat("brightness",brightness);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_brightness_btn_get_factory:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_BRIGHTNESS);
                    Bundle b = new Bundle();
                    b.putInt("type", 2);
                    b.putFloat("brightness",brightness);
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
