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

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_KEYSTONE;
import static com.goertek.microvision.Utils.MSG_SET_KEYSTONE;

/**
 * Created by carey.wang on 2017/4/7.
 */

public class KeyStone extends Fragment {

    private SeekBar mSeekBarKeystone = null;
    private static TextView mTvMaxValue,mTvSetKeystoneCurrent, mTvGetKeystoneCurrent,mTvGetKeystoneStartup, mTvGetKeystoneFactory;
    private Button mBtnSetCommit, mBtnSetNoCommit, mBtnGetCurrent, mBtnGetStartup, mBtnGetFactory;
    private int mSetKeystone = 0;
    private View rootView;

    public static final int MSG_KEYSTONE_RESPONSE_GET = 90001;
    public static Handler keystoneHandler =  new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_KEYSTONE_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    int type = b.getInt("type");
                    if(result==0) {
                        if(type ==0){
                            mTvGetKeystoneCurrent.setText("" + resultData.getKeyStoneCorrectionValue());
                        } else if(type ==1){
                            mTvGetKeystoneStartup.setText(""+ resultData.getKeyStoneCorrectionValue());
                        } else if (type ==2){
                            mTvGetKeystoneFactory.setText(""+ resultData.getKeyStoneCorrectionValue());
                        }
                    } else{
                        String STR = b.getString("STR");
                        if(type ==0 ){
                            mTvGetKeystoneCurrent.setText("fun run failed, errno = " + STR);
                        } else if (type ==1 ){
                            mTvGetKeystoneStartup.setText("fun run failed, errno = " + STR);
                        } else if(type ==2 ) {
                            mTvGetKeystoneFactory.setText("fun run failed, errno = " + STR);
                        }
                    }
                    break;
            }
        }
    };

    public KeyStone(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_keystone, container, false);
        initView();
        return rootView;
    }
    private void initView() {
        /* Set Brightnes */
        //Textview should init before seekbar, otherwise seekbar changelistener will crash.
        mTvSetKeystoneCurrent = (TextView) rootView.findViewById(R.id.fragment_keystone_tv_set_curr_value);
        mTvSetKeystoneCurrent.setText("50");

        mSeekBarKeystone = (SeekBar)rootView.findViewById(R.id.fragment_keystone_seekbar_keystone);
        mSeekBarKeystone.setOnSeekBarChangeListener(new KeyStone.OnSeekBarChangeListenerImp());
        mSeekBarKeystone.setEnabled(true);
        mSeekBarKeystone.setMax(100);
        mSeekBarKeystone.setProgress(50);

        mTvMaxValue = (TextView)rootView.findViewById(R.id.fragment_keystone_tv_max);
        mTvMaxValue.setText("100");
        mBtnSetCommit = (Button)rootView.findViewById(R.id.fragment_keystone_btn_set_commit);
        mBtnSetCommit.setOnClickListener(btnClickListener);
        mBtnSetNoCommit = (Button) rootView.findViewById(R.id.fragment_keystone_btn_set_no_commit);
        mBtnSetNoCommit.setOnClickListener(btnClickListener);
        /* Set Brightnes */

        /* Get Brightnes */
        mBtnGetCurrent = (Button)rootView.findViewById(R.id.fragment_keystone_btn_get_current);
        mBtnGetCurrent.setOnClickListener(btnClickListener);
        mBtnGetStartup = (Button)rootView.findViewById(R.id.fragment_keystone_btn_get_startup);
        mBtnGetStartup.setOnClickListener(btnClickListener);
        mBtnGetFactory = (Button)rootView.findViewById(R.id.fragment_keystone_btn_get_factory);
        mBtnGetFactory.setOnClickListener(btnClickListener);

        mTvGetKeystoneCurrent = (TextView)rootView.findViewById(R.id.fragment_keystone_tv_get_current);
        mTvGetKeystoneCurrent.setText("");
        mTvGetKeystoneStartup = (TextView)rootView.findViewById(R.id.fragment_keystone_tv_get_startup);
        mTvGetKeystoneStartup.setText("");
        mTvGetKeystoneFactory = (TextView)rootView.findViewById(R.id.fragment_keystone_tv_get_factory);
        mTvGetKeystoneFactory.setText("");
        /* Get Brightnes */
    }


    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {
        // progress
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //setMoveTextLayout();
            mTvSetKeystoneCurrent.setText(""+((progress-50)*2));
        }
        // start
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // stop
        public void onStopTrackingTouch(SeekBar seekBar) {

            mSetKeystone = ((seekBar.getProgress() - 50)*2);
        }
    }

    /* { button code */
    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_keystone_btn_set_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_KEYSTONE);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", true);
                    b.putInt("keystone", mSetKeystone);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_keystone_btn_set_no_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_KEYSTONE);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", false);
                    b.putInt("keystone", mSetKeystone);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_keystone_btn_get_current:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_KEYSTONE);
                    Bundle b = new Bundle();
                    b.putInt("type", 0);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_keystone_btn_get_startup:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_KEYSTONE);
                    Bundle b = new Bundle();
                    b.putInt("type", 1);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_keystone_btn_get_factory:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_KEYSTONE);
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