package com.goertek.microvision.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import com.goertek.microvision.R;

import static com.goertek.microvision.MessageCenter.inputCaptureModeInfo;
import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.Utils.MSG_GET_INPUTCAPTUREMODEINFO;
import static com.goertek.microvision.Utils.MSG_SET_INPUTCAPTUREMODEINFO;

/**
 * Created by carmindy.li on 2017/4/22.
 */

public class InputCaptureMode extends Fragment {
    private static final String TAG = "InputCaptureMode";
    private SeekBar mSeekBarColormode = null;
    private View rootView;
    private Button mBtnMode1, mBtnMode2,mBtnMode3,mBtnMode4,mBtnMode5, mBtnSetCommit, mBtnGet,mBtnMode;
    private static TextView mTvMaxValue,mTvMinValue, mTvAspectRatioValue, mTvGetCaptureInfoResult;
    private int hSyncPolarity = 1;
    private int vSyncPolarity = 1;
    private int pixelClockEdge = 1;
    private int colorspace=0;
    private int interlace=0;
    private  float mAspectRatioValue ;
    private int capturemode = 1;
    private int videomode = 3;
   private String str1,str2,str3,str4;
    public InputCaptureMode() {
    }

    public static final int MSG_INPUTCAPTURE_RESPONSE_GET = 90001;
    public static Handler inputCaptureHandler = new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_INPUTCAPTURE_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    String captureInfo = "";
                    if (result == 0) {
                        captureInfo = captureInfo + "   Start position is: X=" + inputCaptureModeInfo.getVideoStartPosition().getX_value()
                                + "   Y=" + inputCaptureModeInfo.getVideoStartPosition().getY_value() + "\n"
                                + "   hSyncPolarity is: " + inputCaptureModeInfo.getHSyncPolarity() + "\n"
                                + "   vSyncPolarity is: " + inputCaptureModeInfo.getVSyncPolarity() + "\n"
                                + "   pixelClockEdge is: " + inputCaptureModeInfo.getPixelClockEdge() + "\n"
                                + "   resolution is: Width=" + inputCaptureModeInfo.getResolution().getWidth()
                                + "   Height=" + inputCaptureModeInfo.getResolution().getHeight() + "\n"
                                + "   pixelAspectRatio is: " + inputCaptureModeInfo.getPixelAspectRatio() + "\n"
                                + "   colorSpace is: " + inputCaptureModeInfo.getColorSpace() + "\n"
                                + "   interlaceField is: " + inputCaptureModeInfo.getInterlaceField() + "\n";
                        mTvGetCaptureInfoResult.setText(captureInfo);
                    } else {
                        String STR = b.getString("STR");
                        mTvGetCaptureInfoResult.setText("fun run failed, errno = " + STR);
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inputcapturemode, container, false);
        initView();
        return rootView;
    }

    private void initView() {

        mBtnMode1 = (Button) rootView.findViewById(R.id.fragment_hSyncPolarity_btn);
        mBtnMode1.setOnClickListener(btnClickListener);
        mBtnMode2 = (Button) rootView.findViewById(R.id.fragment_vSyncPolarity_btn);
        mBtnMode2.setOnClickListener(btnClickListener);
        mBtnMode3 = (Button) rootView.findViewById(R.id.fragment_pixelClockEdge_btn);
        mBtnMode3.setOnClickListener(btnClickListener);
        mBtnMode4 = (Button) rootView.findViewById(R.id.fragment_colorSpace_btn);
        mBtnMode4.setOnClickListener(btnClickListener);
        mBtnMode5 = (Button) rootView.findViewById(R.id.fragment_interlace_btn);
        mBtnMode5.setOnClickListener(btnClickListener);
        mBtnMode = (Button) rootView.findViewById(R.id.fragment_capturemode_btn);
        mBtnMode.setOnClickListener(btnClickListener);

        mTvAspectRatioValue = (TextView) rootView.findViewById(R.id.fragment_aspectratio_tv_value);
        mTvAspectRatioValue.setText("0.8f");
        mSeekBarColormode = (SeekBar)rootView.findViewById(R.id.fragment_par_seekbar);
        mSeekBarColormode.setOnSeekBarChangeListener(new InputCaptureMode.OnSeekBarChangeListenerImp());
        mSeekBarColormode.setEnabled(true);
        mSeekBarColormode.setMax(40);
        mSeekBarColormode.setProgress(0);
        mTvMaxValue = (TextView)rootView.findViewById(R.id.fragment_tv_max);
        mTvMaxValue.setText("1.2f");
        mTvGetCaptureInfoResult = (TextView) rootView.findViewById(R.id.fragment_getcaptureinfo_tv_result);
        mTvGetCaptureInfoResult.setText("");

        mBtnSetCommit = (Button)rootView.findViewById(R.id.fragment_setcaptureinfo_btn);
        mBtnSetCommit.setOnClickListener(btnClickListener);
        mBtnGet = (Button)rootView.findViewById(R.id.fragment_getcaptureinfo_btn);
        mBtnGet.setOnClickListener(btnClickListener);

        setButton1();
        setButton2();
        setButton3();
        setButton4();
        setButton5();
        setButton();
    }
    private class OnSeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener {
        // progress
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //setMoveTextLayout();
            float value = (float)progress/100 + (float) 0.8;
            mTvAspectRatioValue.setText("" + value);
        }
        // start
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // stop
        public void onStopTrackingTouch(SeekBar seekBar) {

            mAspectRatioValue = (float)(seekBar.getProgress()/100)+ (float)0.8;
        }
    }
    /* { button code */
    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_hSyncPolarity_btn:
                    popUp1(v);
                    break;
                case R.id.fragment_vSyncPolarity_btn:
                    popUp2(v);
                    break;
                case R.id.fragment_pixelClockEdge_btn:
                    popUp3(v);
                    break;
                case R.id.fragment_colorSpace_btn:
                    popUp4(v);
                    break;
                case R.id.fragment_interlace_btn:
                    popUp5(v);
                    break;
                case R.id.fragment_capturemode_btn:
                    popUp(v);
                    break;
                case R.id.fragment_setcaptureinfo_btn:{
                    EditText edit1 = (EditText)rootView.findViewById(R.id.etx);
                    String str1 = edit1.getText().toString();
                    EditText edit2 = (EditText)rootView.findViewById(R.id.ety);
                    String str2 = edit2.getText().toString();
                    EditText edit3 = (EditText)rootView.findViewById(R.id.etw);
                    String str3 = edit3.getText().toString();
                    EditText edit4 = (EditText)rootView.findViewById(R.id.eth);
                    String str4 = edit4.getText().toString();
                    edit1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    edit2.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    edit3.setImeOptions(EditorInfo.IME_ACTION_DONE);

                    Message msg0 = messageHandler.obtainMessage(MSG_SET_INPUTCAPTUREMODEINFO);
                    Bundle b = new Bundle();

                    b.putInt("x",Integer.parseInt(str1));
                    b.putInt("y",Integer.parseInt(str2));
                    b.putInt("w",Integer.parseInt(str3));
                    b.putInt("h",Integer.parseInt(str4));
                    b.putInt("hSyncPolarity",hSyncPolarity);
                    b.putInt("vSyncPolarity",vSyncPolarity);
                    b.putInt("pixelClockEdge",pixelClockEdge);
                    b.putInt("colorspace",colorspace);
                    b.putInt("interlace",interlace);
                    b.putFloat("aspectRatio", mAspectRatioValue);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.fragment_getcaptureinfo_btn:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_INPUTCAPTUREMODEINFO);
                    Bundle b = new Bundle();
                    b.putInt("videomode",videomode);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                }
                    break;
                default:
                    Log.i(TAG, "1 why you reach here?");
                    break;
            }
        }
    };

    private String modeToString1(int mode) {
        String ret = "";
        switch (mode) {
            case 0:
                ret = "NEGATIVE";
                break;
            case 1:
                ret = "POSITIVE";
                break;
            default:
                Log.i(TAG, "2 why you reach here?");
                break;
        }
        return ret;
    }
    public String modeToString2 (int enumto)    {
        String res ="";
        switch (enumto)        {
            case 0:
                res ="eCOLOR_SPACE_RGB";
                break;
            case 1:
                res ="eCOLOR_SPACE_YCBCR";
                break;
            case 2:
                res ="eCOLOR_SPACE_YPBPR";
                break;
            case 3:
                res ="eCOLOR_SPACE_RGB_DIRECT";
                break;
            case 4:
                res ="eCOLOR_SPACE_REC601_YCBCR";
                break;
            case 5:
                res ="eCOLOR_SPACE_REC601_BT656";
                break;
            case 6:
                res ="eCOLOR_SPACE_REC709_YCBCR";
                break;

            default:
                break;
        }
        return res;
    }

    private String modeToString3(int mode) {
        String ret = "";
        switch (mode) {
            case 0:
                ret = "eINTERLACE_NONE";
                break;
            case 1:
                ret = "eINTERLACE_EVEN_FIELD";
                break;
            case 2:
                ret = "eINTERLACE_ODD_FIELD";
                break;
            default:
                Log.i(TAG, "3 why you reach here?");
                break;
        }
        return ret;
    }
    private String modeToString(int mode){
        String ret = "";
        switch(mode){
            case 3:
                ret = "640 x 480";
                break;
            case 8:
                ret = "800 x 480";
                break;
            case 15:
                ret = "848 x 480";
                break;
            case 13:
                ret = "invalid";
                break;
            case 40:
                ret = "720p";
                break;
            default:
                Log.i(TAG,"4 why you reach here?");
                break;
        }
        return ret;
    }
    public void popUp(View v){
        PopupMenu pupupmenu = new PopupMenu(getContext(), v);
        pupupmenu.getMenuInflater().inflate(R.menu.activecapturemode, pupupmenu.getMenu());
        pupupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch(item.getItemId()){
                    case R.id.videomode_1:
                        capturemode = 3;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_2:
                        capturemode = 8;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_3:
                        capturemode = 15;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_4:
                        capturemode = 13;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        break;
                    case R.id.videomode_5:
                        capturemode = 40;
                        Log.i(TAG,"choose capture mode is " +capturemode);
                        Log.i(TAG,"5");
                        break;
                    default:
                        Log.i(TAG,"5 why you reach here?");
                        break;
                }
                setButton();
                return false;
            }
        });
        // PopupMenu关闭事件
        pupupmenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        pupupmenu.show();
    }

    private void setButton(){
        mBtnMode.setText(modeToString(videomode));
    }
    public void popUp1(View v) {
        PopupMenu pupupmenu1 = new PopupMenu(getContext(), v);
        pupupmenu1.getMenuInflater().inflate(R.menu.hsyncpolarity, pupupmenu1.getMenu());
        pupupmenu1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.hsyncpolarity_1:
                        hSyncPolarity = 0;
                        Log.i(TAG, "choose hSyncPolarity mode is " + hSyncPolarity);
                        break;
                    case R.id.hsyncpolarity_2:
                        hSyncPolarity = 1;
                        Log.i(TAG, "choose hSyncPolarity mode is " + hSyncPolarity);
                        break;
                    default:
                        Log.i(TAG, "6 why you reach here?");
                        break;
                }
                setButton1();
                return false;
            }
        });
        pupupmenu1.setOnDismissListener(new PopupMenu.OnDismissListener()

        {
            @Override
            public void onDismiss (PopupMenu menu){
                //Toast.makeText(getContext(), "�ر�PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        pupupmenu1.show();
    }

    public void popUp2(View v) {
        PopupMenu pupupmenu2 = new PopupMenu(getContext(), v);
        pupupmenu2.getMenuInflater().inflate(R.menu.vsyncpolarity, pupupmenu2.getMenu());
        pupupmenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.vsyncpolarity_1:
                        vSyncPolarity = 0;
                        Log.i(TAG, "choose vSyncPolarity mode is " + vSyncPolarity);
                        break;
                    case R.id.vsyncpolarity_2:
                        vSyncPolarity = 1;
                        Log.i(TAG, "choose vSyncPolarity mode is " + vSyncPolarity);
                        break;
                    default:
                        Log.i(TAG, "7 why you reach here?");
                        break;
                }
                setButton2();
                return false;
            }
        });

    // PopupMenu�ر��¼�
        pupupmenu2.setOnDismissListener(new PopupMenu.OnDismissListener()

    {
        @Override
        public void onDismiss (PopupMenu menu){
        //Toast.makeText(getContext(), "�ر�PopupMenu", Toast.LENGTH_SHORT).show();
    }
    });
        pupupmenu2.show();
}
    public void popUp3(View v) {
        PopupMenu pupupmenu3 = new PopupMenu(getContext(), v);
        pupupmenu3.getMenuInflater().inflate(R.menu.pixelclockedge, pupupmenu3.getMenu());
        pupupmenu3.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.pixelclockedge_1:
                        pixelClockEdge = 0;
                        Log.i(TAG, "choose pixelClockEdge mode is " + pixelClockEdge);
                        break;
                    case R.id.pixelclockedge_2:
                        pixelClockEdge = 1;
                        Log.i(TAG, "choose pixelClockEdge mode is " + pixelClockEdge);
                        break;
                    default:
                        Log.i(TAG, "8 why you reach here?");
                        break;
                }
                setButton3();
                return false;
            }
        });

        // PopupMenu�ر��¼�
        pupupmenu3.setOnDismissListener(new PopupMenu.OnDismissListener()

        {
            @Override
            public void onDismiss (PopupMenu menu){
                //Toast.makeText(getContext(), "�ر�PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        pupupmenu3.show();
    }
    public void popUp4(View v) {
        PopupMenu pupupmenu4 = new PopupMenu(getContext(), v);
        pupupmenu4.getMenuInflater().inflate(R.menu.colorspace, pupupmenu4.getMenu());
        pupupmenu4.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.colorSpace_1:
                        colorspace = 0;
                        Log.i(TAG, "choose colorspace mode is " + colorspace);
                        break;
                    case R.id.colorSpace_2:
                        colorspace = 1;
                        Log.i(TAG, "choose colorspace mode is " + colorspace);
                        break;
                    case R.id.colorSpace_3:
                        colorspace = 2;
                        Log.i(TAG, "choose colorspace mode is " + colorspace);
                        break;
                    case R.id.colorSpace_4:
                        colorspace = 3;
                        Log.i(TAG, "choose colorspace mode is " + colorspace);
                        break;
                    case R.id.colorSpace_5:
                        colorspace = 4;
                        Log.i(TAG, "choose colorspace mode is " + colorspace);
                        break;
                    case R.id.colorSpace_6:
                        colorspace = 5;
                        Log.i(TAG, "choose colorspace mode is " + colorspace);
                        break;
                    case R.id.colorSpace_7:
                        colorspace = 6;
                        Log.i(TAG, "choose colorspace mode is " + colorspace);
                        break;
                    default:
                        Log.i(TAG, "9 why you reach here?");
                        break;
                }
                setButton4();
                return false;
            }
        });

        // PopupMenu�ر��¼�
        pupupmenu4.setOnDismissListener(new PopupMenu.OnDismissListener()

        {
            @Override
            public void onDismiss (PopupMenu menu){
                //Toast.makeText(getContext(), "�ر�PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        pupupmenu4.show();
    }

    public void popUp5(View v) {
        PopupMenu pupupmenu5= new PopupMenu(getContext(), v);
        pupupmenu5.getMenuInflater().inflate(R.menu.interlace, pupupmenu5.getMenu());
        pupupmenu5.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.interlace_1:
                        interlace = 0;
                        Log.i(TAG, "choose interlace mode is " + interlace);
                        break;
                    case R.id.interlace_2:
                        interlace = 1;
                        Log.i(TAG, "choose interlace mode is " + interlace);
                        break;
                    case R.id.interlace_3:
                        interlace = 2;
                        Log.i(TAG, "choose interlace mode is " + interlace);
                        break;
                    default:
                        Log.i(TAG, "10 why you reach here?");
                        break;
                }
                setButton5();
                return false;
            }
        });

        // PopupMenu�ر��¼�
        pupupmenu5.setOnDismissListener(new PopupMenu.OnDismissListener()

        {
            @Override
            public void onDismiss (PopupMenu menu){
                //Toast.makeText(getContext(), "�ر�PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        pupupmenu5.show();
    }

    private void setButton1(){
        mBtnMode1.setText(modeToString1(hSyncPolarity));
    }
    private void setButton2(){
        mBtnMode2.setText(modeToString1(vSyncPolarity));
    }
    private void setButton3(){
        mBtnMode3.setText(modeToString1(pixelClockEdge));
    }
    private void setButton4(){
        mBtnMode4.setText(modeToString2(colorspace));
    }
    private void setButton5(){
        mBtnMode5.setText(modeToString3(interlace));
    }
}

