package com.mvis.apps.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mvis.apps.R;

import static com.mvis.apps.MessageCenter.messageHandler;
import static com.mvis.apps.MessageCenter.resultData;
import static com.mvis.apps.Utils.MSG_GET_INPUTVIDEOSTATE;
import static com.mvis.apps.Utils.MSG_SET_INPUTVIDEOSTATE;

/**
 * Created on 2017/4/18.
 */

public class InputVideoState extends Fragment  {
    protected static final String TAG = "InputVideoState";
    private View rootView;
    public static TextView mTvGet;
    public static Button mBtnEnable, mBtnDisable, mBtnGet;
    private int state = 0;

    public static final int MSG_INPUTVIDEOSTATE_RESPONSE_GET = 90001;
    public static Handler inputvideostateHandler = new Handler() {
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_INPUTVIDEOSTATE_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    if (result == 0) {
                        mTvGet.setText("state is: " + resultData.getInputstate());
                    } else {
                        String STR = b.getString("STR");
                        mTvGet.setText("fun run failed, errno = " + STR);
                    }
                    break;
            }
        }
    };
    public InputVideoState(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_inputvideostate, container, false);
        mTvGet = (TextView) rootView.findViewById(R.id.fragment_inputvideostate_tv_get_state);
        mTvGet.setText("click button to get input video state");
        mBtnEnable = (Button) rootView.findViewById(R.id.evideo_input_enabled);
        mBtnEnable.setOnClickListener(btnClickListener);
        mBtnDisable =(Button) rootView.findViewById(R.id.evideo_input_disabled);
        mBtnDisable.setOnClickListener(btnClickListener);
        mBtnGet = (Button) rootView.findViewById(R.id.fragment_inputvideostate_btn_get_state);
        mBtnGet.setOnClickListener(btnClickListener);
        return rootView;
    }

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.evideo_input_disabled: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_INPUTVIDEOSTATE);
                    Bundle b = new Bundle();
                    b.putInt("state", 0);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.evideo_input_enabled: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_INPUTVIDEOSTATE);
                    Bundle b = new Bundle();
                    b.putInt("state", 1);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_inputvideostate_btn_get_state: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_INPUTVIDEOSTATE);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

}
