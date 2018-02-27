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
import static com.goertek.microvision.MessageCenter.intToDirection;
import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_COLORALIGNMENT;
import static com.goertek.microvision.Utils.MSG_SET_COLORALIGNMENT;

public class ColorAlignment extends Fragment {

    private static final String TAG = "ColorAlignment";
    private SeekBar mSeekBarColoralignment = null;
    private static TextView mTvMaxValue,mTvSetColoralignmentCurrent, mTvGetColoralignmentCurrent,mTvGetColoralignmentStartup, mTvGetColoralignmentFactory;
    private Button mBtnSetCommit, mBtnSetNoCommit, mBtnGetCurrent, mBtnGetStartup, mBtnGetFactory;
    private int mSetColoralignment = 0;
    private View rootView;

    private LinearLayout mLayout_Red, mLayout_Green, mLayout_Blue, mLayout_All, mLayout_Horizontal, mLayout_Vertical;
    private TextView mTv_Red, mTv_Blue, mTv_Green, mTv_All, mTv_Horizontal, mTv_Vertical;
    private int color = 0;
    private int direction = 0;

    public static final int MSG_COLORALIGNMENT_RESPONSE_GET = 90001;
    public static Handler coloralignmentHandler =  new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COLORALIGNMENT_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    int type = b.getInt("type");
                    if(result==0) {
                        int direction = b.getInt("direction");
                        int color = b.getInt("color");
                        Log.i(TAG,"type = " + type + "color " + color + " direction " + direction);
                        if(type ==0){
                            mTvGetColoralignmentCurrent.setText("" +intToDirection(direction)+", " + intToColor(color)+", offset : " + resultData.getOffset());
                        } else if(type ==1){
                            mTvGetColoralignmentStartup.setText("" +intToDirection(direction)+", " + intToColor(color)+", offset : " + resultData.getOffset());
                        } else if (type ==2){
                            mTvGetColoralignmentFactory.setText("" +intToDirection(direction)+", " + intToColor(color)+", offset : " + resultData.getOffset());
                        }
                    } else{
                        String STR = b.getString("STR");
                        if(type ==0 ){
                            mTvGetColoralignmentCurrent.setText("fun run failed, errno = " + STR);
                        } else if (type ==1 ){
                            mTvGetColoralignmentStartup.setText("fun run failed, errno = " + STR);
                        } else if(type ==2 ) {
                            mTvGetColoralignmentFactory.setText("fun run failed, errno = " + STR);
                        }
                    }
                    break;
            }
        }
    };

    public ColorAlignment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_coloralignment, container, false);
        initView();
        return rootView;
    }
    private void initView() {
        /* Set Brightnes */
        //Textview should init before seekbar, otherwise seekbar changelistener will crash.
        mTvSetColoralignmentCurrent = (TextView) rootView.findViewById(R.id.fragment_coloralignment_tv_set_curr_value);
        mTvSetColoralignmentCurrent.setText("0");

        mSeekBarColoralignment = (SeekBar)rootView.findViewById(R.id.fragment_coloralignment_seekbar_coloralignment);
        mSeekBarColoralignment.setOnSeekBarChangeListener(new ColorAlignment.OnSeekBarChangeListenerImp());
        mSeekBarColoralignment.setEnabled(true);
        mSeekBarColoralignment.setMax(100);
        mSeekBarColoralignment.setProgress(50);

        mTvMaxValue = (TextView)rootView.findViewById(R.id.fragment_coloralignment_tv_max);
        mTvMaxValue.setText("32");
        mBtnSetCommit = (Button)rootView.findViewById(R.id.fragment_coloralignment_btn_set_commit);
        mBtnSetCommit.setOnClickListener(btnClickListener);
        mBtnSetNoCommit = (Button) rootView.findViewById(R.id.fragment_coloralignment_btn_set_no_commit);
        mBtnSetNoCommit.setOnClickListener(btnClickListener);
        /* Set Brightnes */

        /* Get Brightnes */
        mBtnGetCurrent = (Button)rootView.findViewById(R.id.fragment_coloralignment_btn_get_current);
        mBtnGetCurrent.setOnClickListener(btnClickListener);
        mBtnGetStartup = (Button)rootView.findViewById(R.id.fragment_coloralignment_btn_get_startup);
        mBtnGetStartup.setOnClickListener(btnClickListener);
        mBtnGetFactory = (Button)rootView.findViewById(R.id.fragment_coloralignment_btn_get_factory);
        mBtnGetFactory.setOnClickListener(btnClickListener);

        mTvGetColoralignmentCurrent = (TextView)rootView.findViewById(R.id.fragment_coloralignment_tv_get_current);
        mTvGetColoralignmentCurrent.setText("");
        mTvGetColoralignmentStartup = (TextView)rootView.findViewById(R.id.fragment_coloralignment_tv_get_startup);
        mTvGetColoralignmentStartup.setText("");
        mTvGetColoralignmentFactory = (TextView)rootView.findViewById(R.id.fragment_coloralignment_tv_get_factory);
        mTvGetColoralignmentFactory.setText("");

        mLayout_Red = (LinearLayout) rootView.findViewById(R.id.fragment_coloralignment_layout_red);
        mLayout_Red.setOnClickListener(layoutClickListener);
        mLayout_Green = (LinearLayout) rootView.findViewById(R.id.fragment_coloralignment_layout_green);
        mLayout_Green.setOnClickListener(layoutClickListener);
        mLayout_Blue = (LinearLayout) rootView.findViewById(R.id.fragment_coloralignment_layout_blue);
        mLayout_Blue.setOnClickListener(layoutClickListener);
        mLayout_All = (LinearLayout) rootView.findViewById(R.id.fragment_coloralignment_layout_all);
        mLayout_All.setOnClickListener(layoutClickListener);

        mLayout_Horizontal = (LinearLayout) rootView.findViewById(R.id.fragment_coloralignment_layout_horizontal);
        mLayout_Horizontal.setOnClickListener(layoutClickListener);
        mLayout_Vertical = (LinearLayout) rootView.findViewById(R.id.fragment_coloralignment_layout_vertical);
        mLayout_Vertical.setOnClickListener(layoutClickListener);

        mTv_Red = (TextView) rootView.findViewById(R.id.fragment_coloralignment_layout_tv_red);
        mTv_Blue = (TextView) rootView.findViewById(R.id.fragment_coloralignment_layout_tv_blue);
        mTv_Green = (TextView) rootView.findViewById(R.id.fragment_coloralignment_layout_tv_green);
        mTv_All = (TextView) rootView.findViewById(R.id.fragment_coloralignment_layout_tv_all);
        mTv_Horizontal = (TextView) rootView.findViewById(R.id.fragment_coloralignment_layout_tv_horizontal);
        mTv_Vertical = (TextView) rootView.findViewById(R.id.fragment_coloralignment_layout_tv_vertical);

        mLayout_Red.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_Green.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_Blue.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_All.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_Horizontal.setBackgroundResource(R.drawable.shape_cart3);
        mLayout_Vertical.setBackgroundResource(R.drawable.shape_cart3);


        mTv_Red.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_Blue.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_Green.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_All.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_Horizontal.setTextColor(this.getResources().getColor(R.color.tab_dark));
        mTv_Vertical.setTextColor(this.getResources().getColor(R.color.tab_dark));
        color = 1;
        direction = 1;
        /* Get Brightnes */
    }


    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {
        // progress
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //setMoveTextLayout();
            mSetColoralignment = (int)((progress * 0.64) - 32);
            mTvSetColoralignmentCurrent.setText(""+mSetColoralignment);
        }
        // start
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // stop
        public void onStopTrackingTouch(SeekBar seekBar) {

            mSetColoralignment = (int)((seekBar.getProgress() * 0.64) - 32);
        }
    }

    /* { button code */
    private View.OnClickListener layoutClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_coloralignment_layout_red: {
                    color = 0;
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
                case R.id.fragment_coloralignment_layout_green: {
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
                case R.id.fragment_coloralignment_layout_blue: {
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
                case R.id.fragment_coloralignment_layout_all: {
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
                case R.id.fragment_coloralignment_layout_horizontal: {
                    direction = 0;
                    mLayout_Horizontal.setBackgroundResource(R.drawable.shape_cart);
                    mLayout_Vertical.setBackgroundResource(R.drawable.shape_cart3);

                    mTv_Horizontal.setTextColor(v.getResources().getColor(R.color.tab_light));
                    mTv_Vertical.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    break;
                }
                case R.id.fragment_coloralignment_layout_vertical: {
                    direction =1;
                    mLayout_Horizontal.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Vertical.setBackgroundResource(R.drawable.shape_cart);

                    mTv_Horizontal.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Vertical.setTextColor(v.getResources().getColor(R.color.tab_light));
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
                case R.id.fragment_coloralignment_btn_set_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_COLORALIGNMENT);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", true);
                    b.putInt("coloralignment", mSetColoralignment);
                    b.putInt("color",color);
                    b.putInt("direction",direction);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_coloralignment_btn_set_no_commit:{
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_COLORALIGNMENT);
                    Bundle b = new Bundle();
                    b.putBoolean("commit", false);
                    b.putInt("coloralignment", mSetColoralignment);
                    b.putInt("color",color);
                    b.putInt("direction",direction);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_coloralignment_btn_get_current:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_COLORALIGNMENT);
                    Bundle b = new Bundle();
                    b.putInt("type", 0);
                    b.putInt("color",color);
                    b.putInt("direction",direction);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_coloralignment_btn_get_startup:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_COLORALIGNMENT);
                    Bundle b = new Bundle();
                    b.putInt("type", 1);
                    b.putInt("color",color);
                    b.putInt("direction",direction);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_coloralignment_btn_get_factory:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_COLORALIGNMENT);
                    Bundle b = new Bundle();
                    b.putInt("type", 2);
                    b.putInt("color",color);
                    b.putInt("direction",direction);
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