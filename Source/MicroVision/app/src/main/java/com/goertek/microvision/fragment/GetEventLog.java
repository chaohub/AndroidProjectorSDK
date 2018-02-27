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
import android.widget.SeekBar;
import android.widget.TextView;

import com.goertek.microvision.R;

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.MessageCenter.pEvent;
import static com.goertek.microvision.MessageCenter.resultData;
import static com.goertek.microvision.Utils.MSG_GET_EVENTLOG;

/**
 * Created by carmindy.li on 2017/4/18.
 */

public class GetEventLog extends Fragment  {
    protected static final String TAG = "GetEventLog";
    private View rootView;
    private Button button;
    private int eventNumber = 0;
    private SeekBar mSeekBarEventlogNumber = null;
    private static TextView mTvEventLogNumber, mTvEventLog;
    private short mProgress = 255;

    public static final int MSG_EVENTLOG_RESPONSE_GET = 90001;
    public static Handler eventlogHandler =  new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_EVENTLOG_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    int number = b.getInt("eventLogNumber");
                    String eventLog = "";
                    if(result==0) {
                        for(int i=0; i<number; i++){
                            eventLog = eventLog + "Event session is: " + pEvent[i].getSession()
                                    + "   id is: " + pEvent[i].getEventId()
                                    + "   time is: " + pEvent[i].getTime()
                                    + "   data is: " + pEvent[i].getData()
                                    + "   cid is: " + pEvent[i].getCid()
                                    + "   line is: " + pEvent[i].getLine() + "\n";
                        }
                        mTvEventLog.setText(eventLog);
                    }else{
                        String STR = b.getString("STR");
                        mTvEventLog.setText("" + resultData.getBrightness());
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_geteventlog, container, false);
        button = (Button) rootView.findViewById(R.id.fragment_getsystemlog);
        button.setOnClickListener(btnClickListener);

        mTvEventLogNumber = (TextView)rootView.findViewById(R.id.fragment_eventlog_tv_number);
        mTvEventLogNumber.setText("");
        mTvEventLog = (TextView)rootView.findViewById(R.id.fragment_getsystemlog_tv);
        mTvEventLog.setText("");

        mSeekBarEventlogNumber = (SeekBar)rootView.findViewById(R.id.fragment_eventlog_seekbar_number);
        mSeekBarEventlogNumber.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        mSeekBarEventlogNumber.setEnabled(true);
        mSeekBarEventlogNumber.setMax(255);
        mSeekBarEventlogNumber.setProgress(255);

        return rootView;
    }

    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {
        // progress
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //setMoveTextLayout();
            //float value = (float)progress/255;
            //Log.i(TAG,"value = " + value +", progress "+ progress);
            mTvEventLogNumber.setText(""+progress);
        }
        // start
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // stop
        public void onStopTrackingTouch(SeekBar seekBar) {

            mProgress = (short)seekBar.getProgress();
        }
    }

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_getsystemlog:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_EVENTLOG);
                    Bundle b = new Bundle();
                    b.putShort("eventlognumber", mProgress);
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
