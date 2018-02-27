package com.goertek.microvision.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goertek.microvision.R;

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_OUTPUTVIDEOSTATE;
import static com.goertek.microvision.Utils.MSG_SET_OUTPUTVIDEOSTATE;

/**
 * Created by carmindy.li on 2017/4/18.
 */

public class OutputVideoState extends Fragment {
    protected static final String TAG = "OutputVideoState";
    private View rootView;
    private static TextView mTv_commit, mTv_not_commit, mTv_GetOutputStateCurrent, mTv_GetOutputStateStartup, mTv_GetoutputStateFactory;
    private Button mBtnOutputVideoCommit, mBtnOutputVideoNotCommit, mBtnOutputVideoGet;

    private LinearLayout mLayout_enable, mLayout_disable;
    private TextView mTv_enable, mTv_disable;
    private int state = 0;

    public static final int MSG_OUTPUTVIDEOSTATE_RESPONSE_GET = 90001;
    public static Handler outputvideostateHandler = new Handler() {
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_OUTPUTVIDEOSTATE_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    if (result == 0) {
                        mTv_GetOutputStateCurrent.setText("currect state is : " + resultData.getOutputstate());
                    } else {
                        String STR = b.getString("STR");
                        mTv_GetOutputStateCurrent.setText("fun run failed, errno = " + STR);
                    }
                    break;
            }
        }
    };
    public OutputVideoState(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_outputvideostate, container, false);
        initView();
        return rootView;
    }
    private void initView() {
        mTv_GetOutputStateCurrent = (TextView) rootView.findViewById(R.id.fragment_outputvideostate_tv_get_state);
        mTv_GetOutputStateCurrent.setText("");
        mBtnOutputVideoCommit = (Button) rootView.findViewById(R.id.fragment_outputvideostate_btn_set_commit);
        mBtnOutputVideoCommit.setOnClickListener(btnClickListener);
        mBtnOutputVideoNotCommit = (Button) rootView.findViewById(R.id.fragment_outputvideostate_btn_set_no_commit);
        mBtnOutputVideoNotCommit.setOnClickListener(btnClickListener);

        mBtnOutputVideoGet = (Button) rootView.findViewById(R.id.fragment_outputvideostate_btn_get_state);
        mBtnOutputVideoGet.setOnClickListener(btnClickListener);

        mLayout_enable = (LinearLayout) rootView.findViewById(R.id.fragment_outputvideostate_layout_enable);
        mLayout_enable.setOnClickListener(layoutClickListener);
        mLayout_enable.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_disable = (LinearLayout) rootView.findViewById(R.id.fragment_outputvideostate_layout_disable);
        mLayout_disable.setOnClickListener(layoutClickListener);
        mLayout_disable.setBackgroundResource(R.drawable.shape_cart3);

        mTv_enable = (TextView) rootView.findViewById(R.id.fragment_outputvideostate_layout_tv_enable);
        mTv_enable.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_disable = (TextView) rootView.findViewById(R.id.fragment_outputvideostate_layout_tv_disable);
        mTv_disable.setTextColor(this.getResources().getColor(R.color.tab_dark));

    }

    private View.OnClickListener layoutClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_outputvideostate_layout_disable:
                    mLayout_disable.setBackgroundResource(R.drawable.shape_cart);
                    mLayout_enable.setBackgroundResource(R.drawable.shape_cart3);

                    mTv_disable.setTextColor(v.getResources().getColor(R.color.tab_light));
                    mTv_enable.setTextColor(v.getResources().getColor(R.color.tab_dark));

                    state = 0;
                    break;
                case R.id.fragment_outputvideostate_layout_enable:
                    mLayout_disable.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_enable.setBackgroundResource(R.drawable.shape_cart);

                    mTv_disable.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_enable.setTextColor(v.getResources().getColor(R.color.tab_light));

                    state = 1;
                    break;
                default:
                    break;
            }
        }
    };

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_outputvideostate_btn_set_commit: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_OUTPUTVIDEOSTATE);
                    Bundle b = new Bundle();
                    b.putInt("state", state);
                    b.putBoolean("commit", true);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_outputvideostate_btn_set_no_commit: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_OUTPUTVIDEOSTATE);
                    Bundle b = new Bundle();
                    b.putInt("state", state);
                    b.putBoolean("commit", false);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_outputvideostate_btn_get_state: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_OUTPUTVIDEOSTATE);
                    Bundle b = new Bundle();
                    b.putInt("state", state);
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
