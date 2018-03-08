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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.goertek.microvision.R;

import static com.goertek.microvision.MessageCenter.intToColor;
import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_GAMMAVAL;
import static com.goertek.microvision.Utils.MSG_SET_GAMMAVAL;

/**
 * Created on 2017/4/18.
 */

public class GammaVal extends Fragment  {
    private static final String TAG = "GammaVal";
    private SeekBar mSeekBarGammaval = null;
    private static TextView mTvMaxValue,mTvSetGammavalCurrent, mTvGetGammavalCurrent,mTvGetGammavalStartup, mTvGetGammavalFactory;
    private Button mBtnSetCommit, mBtnSetNoCommit, mBtnGetCurrent, mBtnGetStartup, mBtnGetFactory;
    private int mSetGammaval = 0;
    private View rootView;

    private LinearLayout mLayout_Red, mLayout_Green, mLayout_Blue, mLayout_All;
    private TextView mTv_Red, mTv_Blue, mTv_Green, mTv_All ;
    private int color = 0;

    public static final int MSG_GAMMAVAL_RESPONSE_GET = 90001;
    public static Handler gammavalHandler =  new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GAMMAVAL_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    int type = b.getInt("type");
                    if(result==0) {
                        int color = b.getInt("color");
                        Log.i(TAG,"type = " + type + " color " + color );
                        if(type ==0){
                            mTvGetGammavalCurrent.setText(" " + intToColor(color)+", gammaval : " + resultData.getGammaval());
                        } else if(type ==1){
                            mTvGetGammavalStartup.setText(" " + intToColor(color)+", gammaval : " + resultData.getGammaval());
                        } else if (type ==2){
                            mTvGetGammavalFactory.setText("  " + intToColor(color)+", gammaval : " + resultData.getGammaval());
                        }
                    } else{
                        String STR = b.getString("STR");
                        if(type ==0 ){
                            mTvGetGammavalCurrent.setText("fun run failed, errno = " + STR);
                        } else if (type ==1 ){
                            mTvGetGammavalStartup.setText("fun run failed, errno = " + STR);
                        } else if(type ==2 ) {
                            mTvGetGammavalFactory.setText("fun run failed, errno = " + STR);
                        }
                    }
                    break;
            }
        }
    };

    public GammaVal(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_gammaval, container, false);
        initView();
        return rootView;
    }
    private void initView() {
        /* Set Gamma values */
        //Textview should init before seekbar, otherwise seekbar changelistener will crash.
        mTvSetGammavalCurrent = (TextView) rootView.findViewById(R.id.fragment_gammaval_tv_set_curr_value);
        mTvSetGammavalCurrent.setText("50");

        mSeekBarGammaval = (SeekBar)rootView.findViewById(R.id.fragment_gammaval_seekbar_gammaval);
        mSeekBarGammaval.setOnSeekBarChangeListener(new GammaVal.OnSeekBarChangeListenerImp());
        mSeekBarGammaval.setEnabled(true);
        mSeekBarGammaval.setMax(100);
        mSeekBarGammaval.setProgress(50);

        mTvMaxValue = (TextView)rootView.findViewById(R.id.fragment_gammaval_tv_max);
        mTvMaxValue.setText("100");
        mBtnSetCommit = (Button)rootView.findViewById(R.id.fragment_gammaval_btn_set_commit);
        mBtnSetCommit.setOnClickListener(btnClickListener);
        mBtnSetNoCommit = (Button) rootView.findViewById(R.id.fragment_gammaval_btn_set_no_commit);
        mBtnSetNoCommit.setOnClickListener(btnClickListener);
        /* Set Gamma values */

        /* Get Gamma values */
        mBtnGetCurrent = (Button)rootView.findViewById(R.id.fragment_gammaval_btn_get_current);
        mBtnGetCurrent.setOnClickListener(btnClickListener);
        mBtnGetStartup = (Button)rootView.findViewById(R.id.fragment_gammaval_btn_get_startup);
        mBtnGetStartup.setOnClickListener(btnClickListener);
        mBtnGetFactory = (Button)rootView.findViewById(R.id.fragment_gammaval_btn_get_factory);
        mBtnGetFactory.setOnClickListener(btnClickListener);

        mTvGetGammavalCurrent = (TextView)rootView.findViewById(R.id.fragment_gammaval_tv_get_current);
        mTvGetGammavalCurrent.setText("");
        mTvGetGammavalStartup = (TextView)rootView.findViewById(R.id.fragment_gammaval_tv_get_startup);
        mTvGetGammavalStartup.setText("");
        mTvGetGammavalFactory = (TextView)rootView.findViewById(R.id.fragment_gammaval_tv_get_factory);
        mTvGetGammavalFactory.setText("");

        mLayout_Red = (LinearLayout) rootView.findViewById(R.id.fragment_gammaval_layout_red);
        mLayout_Red.setOnClickListener(layoutClickListener);
        mLayout_Green = (LinearLayout) rootView.findViewById(R.id.fragment_gammaval_layout_green);
        mLayout_Green.setOnClickListener(layoutClickListener);
        mLayout_Blue = (LinearLayout) rootView.findViewById(R.id.fragment_gammaval_layout_blue);
        mLayout_Blue.setOnClickListener(layoutClickListener);
        mLayout_All = (LinearLayout) rootView.findViewById(R.id.fragment_gammaval_layout_all);
        mLayout_All.setOnClickListener(layoutClickListener);


        mTv_Red = (TextView) rootView.findViewById(R.id.fragment_gammaval_layout_tv_red);
        mTv_Blue = (TextView) rootView.findViewById(R.id.fragment_gammaval_layout_tv_blue);
        mTv_Green = (TextView) rootView.findViewById(R.id.fragment_gammaval_layout_tv_green);
        mTv_All = (TextView) rootView.findViewById(R.id.fragment_gammaval_layout_tv_all);


        mLayout_Red.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_Green.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_Blue.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_All.setBackgroundResource(R.drawable.shape_cart3);

        mTv_Red.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_Blue.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_Green.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_All.setTextColor(this.getResources().getColor(R.color.tab_dark));

        color = 1;
        /* Get Brightnes */
    }


    private class OnSeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener {
        // progress
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //setMoveTextLayout();
            mSetGammaval = (int)(progress );
            mTvSetGammavalCurrent.setText(""+mSetGammaval);
        }
        // start
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // stop
        public void onStopTrackingTouch(SeekBar seekBar) {

            mSetGammaval = (int)(seekBar.getProgress() );
        }
    }

    /* { button code */
    private View.OnClickListener layoutClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_gammaval_layout_red: {
                    color = 0;
                    Log.i("ll","red pressed");
                    mLayout_Red.setBackgroundResource(R.drawable.shape_cart);
                    mLayout_Green.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Blue.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_All.setBackgroundResource(R.drawable.shape_cart3);

                    mTv_Red.setTextColor(v.getResources().getColor(R.color.tab_light));
                    mTv_Blue.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Green.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_All.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    break;
                }
                case R.id.fragment_gammaval_layout_green: {
                    color = 1;
                    mLayout_Red.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Green.setBackgroundResource(R.drawable.shape_cart);
                    mLayout_Blue.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_All.setBackgroundResource(R.drawable.shape_cart3);

                    mTv_Red.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Blue.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Green.setTextColor(v.getResources().getColor(R.color.tab_light));
                    mTv_All.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    break;
                }
                case R.id.fragment_gammaval_layout_blue: {
                    color=2;
                    mLayout_Red.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Green.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Blue.setBackgroundResource(R.drawable.shape_cart);
                    mLayout_All.setBackgroundResource(R.drawable.shape_cart3);

                    mTv_Red.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Blue.setTextColor(v.getResources().getColor(R.color.tab_light));
                    mTv_Green.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_All.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    break;
                }
                case R.id.fragment_gammaval_layout_all: {
                    color = 3;
                    mLayout_Red.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Green.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Blue.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_All.setBackgroundResource(R.drawable.shape_cart);

                    mTv_Red.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Blue.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Green.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_All.setTextColor(v.getResources().getColor(R.color.tab_light));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    /* { button code */
    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_gammaval_btn_set_commit:{
                    Log.i("ll","commit click");
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_GAMMAVAL);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", true);
                    b.putFloat("gammaval", mSetGammaval);
                    b.putInt("color",color);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_gammaval_btn_set_no_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_GAMMAVAL);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", false);
                    b.putFloat("gammaval", mSetGammaval);
                    b.putInt("color",color);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_gammaval_btn_get_current:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_GAMMAVAL);
                    Bundle b = new Bundle();
                    b.putInt("type", 0);
                    b.putInt("color",color);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_gammaval_btn_get_startup:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_GAMMAVAL);
                    Bundle b = new Bundle();
                    b.putInt("type", 1);
                    b.putInt("color",color);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_gammaval_btn_get_factory:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_GAMMAVAL);
                    Bundle b = new Bundle();
                    b.putInt("type", 2);
                    b.putInt("color",color);
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