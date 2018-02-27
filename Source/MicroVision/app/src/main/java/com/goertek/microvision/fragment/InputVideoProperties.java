package com.goertek.microvision.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goertek.microvision.R;

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_INPUTVIDEOPROPERTIES;

/**
 * Created by carmindy.li on 2017/4/18.
 */

public class InputVideoProperties extends Fragment {
    protected static final String TAG = "InputVideoProperties";
    private View rootView;
    private Button button1;
    private int framerate = 0;
    private int lines;
    private static TextView mTvGetPropertiesFrameRate, mTvGetPropertiesLines;
    public static final int MSG_GET_INPUTVIDEOPROPERTIES_RESPONSE_GET = 90001;
    public static Handler inputvideopropertiesHandler = new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_INPUTVIDEOPROPERTIES_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    if (result == 0) {
                        mTvGetPropertiesFrameRate.setText("FrameRate:" + resultData.getFrameRate());
                        mTvGetPropertiesLines.setText("Lines:" + resultData.getLines());
                    } else {
                        String STR = b.getString("STR");
                        mTvGetPropertiesFrameRate.setText("Run failed with error = " + STR);

                    }
                    break;
            }
        }
    };

    public InputVideoProperties() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_inputvideoproperties, container, false);
        button1 = (Button) rootView.findViewById(R.id.fragment_properties);
        button1.setOnClickListener(btnClickListener);

        mTvGetPropertiesFrameRate = (TextView) rootView.findViewById(R.id.fragment_properties_tv_framerate);
        mTvGetPropertiesFrameRate.setText("");
        mTvGetPropertiesLines = (TextView) rootView.findViewById(R.id.fragment_properties_tv_lines);
        mTvGetPropertiesLines.setText("");
        return rootView;
    }

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_properties: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_INPUTVIDEOPROPERTIES);
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
