package com.goertek.microvision.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goertek.microvision.R;

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.systemInfo;
import static com.goertek.microvision.Utils.MSG_GET_SYSTEMINFO;

/**
 * Created on 2017/4/18.
 */

public class SystemInfo extends Fragment  {
    protected static final String TAG = "SystemInfo";
    private View rootView;
    private Button button1;
    private static String serialNumber;
    private static int softwareVersion=0;
    private static int chipVersion=0;
    private static int electronicsVersion=0;
    private static int runTime=0;
    private static TextView mTvGetSeriesNumber, mTvGetSoftwareVersion, mTvGetFpgaVersion;
    public static final int MSG_GET_SYSTEMINFO_RESPONSE_GET = 90001;
    public static Handler systeminfoHandler = new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_SYSTEMINFO_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    if (result == 0) {
                        mTvGetSeriesNumber.setText("Serial Number : " + systemInfo.getSerialNumber());
                        mTvGetSoftwareVersion.setText("Software Version : " + Integer.toString(systemInfo.getSoftwareVersion()));
                        mTvGetFpgaVersion.setText("FPGA Version : " + Integer.toString(systemInfo.getFpgaVersion()));
                    } else {
                        String STR = b.getString("STR");
                        mTvGetSeriesNumber.setText("fun run failed, errno = " + STR);
                    }
                    break;
            }
        }
    };

    public SystemInfo() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_systeminfo, container, false);
        button1 = (Button) rootView.findViewById(R.id.fragment_systemsinfo_info);
        button1.setOnClickListener(btnClickListener);
        mTvGetSeriesNumber=(TextView)rootView.findViewById(R.id.fragment_tv_systemsinfo_serialNumber);
        mTvGetSeriesNumber.setText("");
        mTvGetSoftwareVersion=(TextView)rootView.findViewById(R.id.fragment_tv_systemsinfo_softwareVersion);
        mTvGetSoftwareVersion.setText("");
        mTvGetFpgaVersion =(TextView)rootView.findViewById(R.id.fragment_tv_systemsinfo_fpgaVersion);
        mTvGetFpgaVersion.setText("");
        return rootView;
    }

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_systemsinfo_info:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_SYSTEMINFO);
                    Bundle b = new Bundle();
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

