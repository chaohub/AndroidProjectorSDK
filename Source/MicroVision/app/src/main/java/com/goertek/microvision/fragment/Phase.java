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
import static com.goertek.microvision.Utils.MSG_GET_PHASE;
import static com.goertek.microvision.Utils.MSG_SET_PHASE;

/**
 * Created on 2017/4/7.
 */

public class Phase extends Fragment {

    private SeekBar mSeekBarPhase = null;
    private static TextView mTvMaxValue,mTvSetPhaseCurrent, mTvGetPhaseCurrent,mTvGetPhaseStartup, mTvGetPhaseFactory;
    private Button mBtnSetCommit, mBtnSetNoCommit, mBtnGetCurrent, mBtnGetStartup, mBtnGetFactory;
    private int mSetPhase = 0;

    private View rootView;

    public static final int MSG_PHASE_RESPONSE_GET = 90001;
    public static Handler phaseHandler =  new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PHASE_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    int type = b.getInt("type");
                    if(result==0) {
                        if(type ==0){
                            mTvGetPhaseCurrent.setText("" + resultData.getPhaseValue());
                        } else if(type ==1){
                            mTvGetPhaseStartup.setText(""+ resultData.getPhaseValue());
                        } else if (type ==2){
                            mTvGetPhaseFactory.setText(""+ resultData.getPhaseValue());
                        }
                    } else{
                        String STR = b.getString("STR");
                        if(type ==0 ){
                            mTvGetPhaseCurrent.setText("fun run failed, errno = " + STR);
                        } else if (type ==1 ){
                            mTvGetPhaseStartup.setText("fun run failed, errno = " + STR);
                        } else if(type ==2 ) {
                            mTvGetPhaseFactory.setText("fun run failed, errno = " + STR);
                        }
                    }
                    break;
            }
        }
    };

    public Phase(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_phase, container, false);
        initView();
        return rootView;
    }
    private void initView() {
        /* Set Brightnes */
        //Textview should init before seekbar, otherwise seekbar changelistener will crash.
        mTvSetPhaseCurrent = (TextView) rootView.findViewById(R.id.fragment_phase_tv_set_curr_value);
        mTvSetPhaseCurrent.setText("0");

        mSeekBarPhase = (SeekBar)rootView.findViewById(R.id.fragment_phase_seekbar_phase);
        mSeekBarPhase.setOnSeekBarChangeListener(new Phase.OnSeekBarChangeListenerImp());
        mSeekBarPhase.setEnabled(true);
        mSeekBarPhase.setMax(100);
        mSeekBarPhase.setProgress(50);

        mTvMaxValue = (TextView)rootView.findViewById(R.id.fragment_phase_tv_max);
        mTvMaxValue.setText("50");
        mBtnSetCommit = (Button)rootView.findViewById(R.id.fragment_phase_btn_set_commit);
        mBtnSetCommit.setOnClickListener(btnClickListener);
        mBtnSetNoCommit = (Button) rootView.findViewById(R.id.fragment_phase_btn_set_no_commit);
        mBtnSetNoCommit.setOnClickListener(btnClickListener);
        /* Set Brightnes */

        /* Get Brightnes */
        mBtnGetCurrent = (Button)rootView.findViewById(R.id.fragment_phase_btn_get_current);
        mBtnGetCurrent.setOnClickListener(btnClickListener);
        mBtnGetStartup = (Button)rootView.findViewById(R.id.fragment_phase_btn_get_startup);
        mBtnGetStartup.setOnClickListener(btnClickListener);
        mBtnGetFactory = (Button)rootView.findViewById(R.id.fragment_phase_btn_get_factory);
        mBtnGetFactory.setOnClickListener(btnClickListener);

        mTvGetPhaseCurrent = (TextView)rootView.findViewById(R.id.fragment_phase_tv_get_current);
        mTvGetPhaseCurrent.setText("");
        mTvGetPhaseStartup = (TextView)rootView.findViewById(R.id.fragment_phase_tv_get_startup);
        mTvGetPhaseStartup.setText("");
        mTvGetPhaseFactory = (TextView)rootView.findViewById(R.id.fragment_phase_tv_get_factory);
        mTvGetPhaseFactory.setText("");
        /* Get Brightnes */
    }


    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {
        // progress
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //setMoveTextLayout();
            mTvSetPhaseCurrent.setText(""+(progress-50));
        }
        // start
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // stop
        public void onStopTrackingTouch(SeekBar seekBar) {

            mSetPhase = (seekBar.getProgress() - 50);
        }
    }

    /* { button code */
    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_phase_btn_set_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_PHASE);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", true);
                    b.putInt("phase", mSetPhase);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_phase_btn_set_no_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_PHASE);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", false);
                    b.putInt("phase", mSetPhase);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_phase_btn_get_current:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_PHASE);
                    Bundle b = new Bundle();
                    b.putInt("type", 0);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_phase_btn_get_startup:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_PHASE);
                    Bundle b = new Bundle();
                    b.putInt("type", 1);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_phase_btn_get_factory:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_PHASE);
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