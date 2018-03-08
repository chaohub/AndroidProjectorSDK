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
import static com.mvis.apps.MessageCenter.systemStatus;
import static com.mvis.apps.Utils.MSG_GET_SYSTEMSTATUS;

/**
 * Created on 2017/4/18.
 */

public class SystemStatus extends Fragment {
    protected static final String TAG = "SystemStatus";
    private View rootView;
    private Button button1;
    private static TextView mTvGetSystemStatus, mTvGetSystemState, mTvGetTemperature;
    public static final int MSG_GET_SYSTEMSTATUS_RESPONSE_GET = 90001;
    public static Handler systemstatusHandler = new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_SYSTEMSTATUS_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    if (mTvGetSystemStatus != null) {
                        if (result == 0) {
                            mTvGetSystemStatus.setText("System status : " + Integer.toString(systemStatus.getStatus()));
                            mTvGetSystemState.setText("System state : " + Integer.toString(systemStatus.getState()));
                            mTvGetTemperature.setText("Temperature : " + Float.toString(systemStatus.getTemperature()));
                        } else {
                            String STR = b.getString("STR");
                            mTvGetSystemStatus.setText("fun run failed, errno = " + STR);
                            mTvGetSystemState.setText("fun run failed, errno = " + STR);
                            mTvGetTemperature.setText("fun run failed, errno = " + STR);
                        }
                    }
                    break;
            }
        }
    };

    public SystemStatus() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_systemstatus, container, false);
        button1 = (Button) rootView.findViewById(R.id.fragment_systemstatus1);
        button1.setOnClickListener(btnClickListener);
        mTvGetSystemStatus = (TextView) rootView.findViewById(R.id.fragment_tv_systemstatus);
        mTvGetSystemStatus.setText("");
        mTvGetSystemState = (TextView) rootView.findViewById(R.id.fragment_tv_systemstate);
        mTvGetSystemState.setText("");
        mTvGetTemperature = (TextView) rootView.findViewById(R.id.fragment_tv_temperature);
        mTvGetTemperature.setText("");
        return rootView;
    }

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_systemstatus1: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_SYSTEMSTATUS);
                    Bundle b = new Bundle();
                    msg0.setData(b);
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


