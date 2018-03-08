package com.mvis.apps.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mvis.apps.R;

import static com.mvis.apps.MessageCenter.messageHandler;
import static com.mvis.apps.MessageCenter.resultData;
import static com.mvis.apps.Utils.MSG_GET_ACTIVECAPTUREMODE;
import static com.mvis.apps.Utils.MSG_SET_ACTIVECAPTUREMODE;


/**
 * Created on 2017/4/8.
 */

public class ActiveCaptureMode extends Fragment {

    private static final String TAG = "ActiveCaptureMode";

    private View rootView;
    private Button mBtnSet, mBtnMode, mBtnGet;
    private static TextView mTvMode;

    private int capturemode = 1;
    public ActiveCaptureMode(){
    }

    public static final int MSG_ACTIVEACTIVEMODE_RESPONSE_GET = 90001;
    public static Handler activeCapModeHandler =  new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ACTIVEACTIVEMODE_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    if(result==0) {
                        mTvMode.setText("" + resultData.getVideoMode());
                    } else{
                        String STR = b.getString("STR");
                        mTvMode.setText("fun run failed, errno = " + STR);
                    }
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_activecapturemode, container, false);
        initView();
        return rootView;
    }
    private void initView(){
        mBtnSet = (Button) rootView.findViewById(R.id.fragment_activecapturemode_btn_set);
        mBtnSet.setOnClickListener(btnClickListener);
        mBtnMode = (Button) rootView.findViewById(R.id.fragment_activecapturemode_btn_mode);
        mBtnMode.setOnClickListener(btnClickListener);
        mBtnGet = (Button) rootView.findViewById(R.id.fragment_activecapturemode_btn_get);
        mBtnGet.setOnClickListener(btnClickListener);
        mTvMode = (TextView) rootView.findViewById(R.id.fragment_activecapturemode_tv_mode);
        mTvMode.setText("");
    }
    /* { button code */
    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_activecapturemode_btn_set:
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_ACTIVECAPTUREMODE);
                    Bundle b = new Bundle();
                    b.putInt("mode", capturemode);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                case R.id.fragment_activecapturemode_btn_mode:
                    popUp(v);
                    break;
                case R.id.fragment_activecapturemode_btn_get:
                    Message msg1 = messageHandler.obtainMessage(MSG_GET_ACTIVECAPTUREMODE);
                    messageHandler.sendMessage(msg1);
                    break;
                default:
                    Log.i(TAG,"why you reach here?");
                    break;
            }
        }
    };

    private String modeToString(int mode){
        String ret = "";
        switch(mode){
            case 3:
                ret = "640 x 480";
                break;
            case 8:
                ret = "800 x 480";
                break;
            case 15:
                ret = "848 x 480";
                break;
            case 13:
                ret = "invalid";
                break;
            case 40:
                ret = "720p";
                break;
            default:
                Log.i(TAG,"why you reach here?");
                break;
        }
        return ret;
    }
    public void popUp(View v){
        PopupMenu pupupmenu = new PopupMenu(getContext(), v);
        pupupmenu.getMenuInflater().inflate(R.menu.activecapturemode, pupupmenu.getMenu());
        pupupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch(item.getItemId()){
                    case R.id.videomode_1:
                        capturemode = 3;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_2:
                        capturemode = 8;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_3:
                        capturemode = 15;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_4:
                        capturemode = 13;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_5:
                        capturemode = 40;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        Log.i(TAG,"5");
                        break;
                    default:
                        Log.i(TAG,"why you reach here?");
                        break;
                }
                setButton();
                return false;
            }
        });
        // PopupMenu关闭事件
        pupupmenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        pupupmenu.show();
    }

    private void setButton(){
        mBtnMode.setText(modeToString(capturemode));
    }
}
