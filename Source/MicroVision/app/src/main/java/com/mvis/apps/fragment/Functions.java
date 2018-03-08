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
import static com.mvis.apps.MessageCenter.resultData;
import static com.mvis.apps.Utils.MSG_GET_ACTIVEOSD;
import static com.mvis.apps.Utils.MSG_GET_ASPECTRATIOMODE;
import static com.mvis.apps.Utils.MSG_GET_CLEARTARGER;
import static com.mvis.apps.Utils.MSG_GET_COLORCONVERTER;
import static com.mvis.apps.Utils.MSG_GET_COMMITINPUTCAPTUREMODE;
import static com.mvis.apps.Utils.MSG_GET_DISPLAYINFO;
import static com.mvis.apps.Utils.MSG_GET_DRAWLINE;
import static com.mvis.apps.Utils.MSG_GET_DRAWPOINT;
import static com.mvis.apps.Utils.MSG_GET_DRAWRECTANGLE;
import static com.mvis.apps.Utils.MSG_GET_DRAWTESTPATTERN;
import static com.mvis.apps.Utils.MSG_GET_DRAWTEXT;
import static com.mvis.apps.Utils.MSG_GET_DRAWTRIANGLE;
//import static com.mvis.apps.Utils.MSG_GET_ENUMERATEDEVICES;
import static com.mvis.apps.Utils.MSG_GET_FLIPSTATE;
import static com.mvis.apps.Utils.MSG_GET_LOADBITMAPIMAGE;
import static com.mvis.apps.Utils.MSG_GET_OSDINFO;
import static com.mvis.apps.Utils.MSG_GET_OSDSTATE;
import static com.mvis.apps.Utils.MSG_GET_RENDER;
import static com.mvis.apps.Utils.MSG_RESTOREFACTORYCONFIG;
import static com.mvis.apps.Utils.MSG_GET_SPLASHSCREENTIMEOUT;
import static com.mvis.apps.Utils.MSG_GET_TEXTBOXINFO;
//import static com.mvis.apps.Utils.MSG_GET_UPGRADESOFTWARE;
import static com.mvis.apps.Utils.MSG_GET_VIEWPORTDISTORTION;
import static com.mvis.apps.Utils.MSG_SET_ACTIVEOSD;
import static com.mvis.apps.Utils.MSG_SET_ASPECTRATIOMODE;
import static com.mvis.apps.Utils.MSG_SET_COLORCONVERTER;
import static com.mvis.apps.Utils.MSG_SET_COMMITINPUTCAPTUREMODE;
import static com.mvis.apps.Utils.MSG_SET_FLIPSTATE;
import static com.mvis.apps.Utils.MSG_SET_OSDINFO;
import static com.mvis.apps.Utils.MSG_SET_OSDSTATE;
import static com.mvis.apps.Utils.MSG_SET_SPLASHSCREENTIMEOUT;
import static com.mvis.apps.Utils.MSG_SET_VIEWPORTDISTORTION;

/**
 * Created on 2017/4/24.
 */

public class Functions extends Fragment {
    private static final String TAG = "Functions";

    private View rootView;
    private Button mButtonSetViewport, mButtonGetViewport, mButtonSetAspectratio, mButtonGetAspectratio, mButtonSetFlipState, mButtonGatFlipState,
            mButtonSetColorConverter, mButtonGatColorConverter, mButtonSetCommitICM, mButtonGetCommitICM, mButtonSetActiveOSD, mButtonGetActiveOSD, mButtonSetOSDInfo,
            mButtonGetOSDInfo,mButtonSetOSDState,mButtonGetOSDState,mButtonSetSplash,mButtonGetSplash,mButtonGetDisplayInfo,
            mButtonGetTextBoxInfo,mButtonGetClearTarget,mButtonGetLoadBitmapImage,mButtonGetRender,mButtonGetDrawTestPattern,mButtonGetDrawText,
            mButtonGetDrawPoint,mButtonGetDrawLine,mButtonGetDrawTriangle,mButtonGetDrawRectangle,mButtonGetRestore,mButtonGetTemperatureLoop;
//    private Button mButtonGetEnumerateDevices, mButtonGetDrawUpgrade;
    private static TextView mTvViewPort, mTvAspectRatio, mTvFlipState, mTvColorConverter, mTvCommitInputCapture, mTvActiveOSD, mTvOSDInfo, mTvOSDState,
            mTvSplashScreenTimeout, mTvDisplayInfo, mTvTextBoxInfo, mTvClearTarget, mTvLoadBitmap, mTvRender,
            mTvDrawTestPattern, mTvDrawText, mTvDrawPoint, mTvFrawLine, mTvDrawTriangle, mTvDrawRectangle, mTvRestoreFactory,
            mTvTemperatureLoop;
//    private static mTvEnumerateDevices, mTvUpgradeSoftware;
    public static final int MSG_FUNCTIONS_RESPONSE_GET = 90001;
    public static final int MSG_FUNCTIONS_VIEWPORT_RESPONSE_GET = 90002;
    public static final int MSG_FUNCTIONS_ASPECT_RATIO_RESPONSE_GET = 90003;
    public static final int MSG_FUNCTIONS_FLIP_STATE_RESPONSE_GET = 90004;
    public static final int MSG_FUNCTIONS_COLOR_CONVERTER_RESPONSE_GET = 90005;
    public static final int MSG_FUNCTIONS_COMMIT_INPUT_CAPTURE_RESPONSE_GET = 90006;
    public static final int MSG_FUNCTIONS_ACTIVE_OSD_RESPONSE_GET = 90007;
    public static final int MSG_FUNCTIONS_OSD_INFO_RESPONSE_GET = 90008;
    public static final int MSG_FUNCTIONS_OSD_STATE_RESPONSE_GET = 90009;
    public static final int MSG_FUNCTIONS_SPLASH_SCREEN_TIMEOUT_RESPONSE_GET = 90010;
    public static final int MSG_FUNCTIONS_ENUMERATE_DEVICES_RESPONSE_GET = 90011;
    public static final int MSG_FUNCTIONS_DISPLAY_INFO_RESPONSE_GET = 90012;
    public static final int MSG_FUNCTIONS_TEXTBOX_INFO_RESPONSE_GET = 90013;
    public static final int MSG_FUNCTIONS_CLEAR_TARGET_RESPONSE_GET = 90014;
    public static final int MSG_FUNCTIONS_LOAD_BITMAP_RESPONSE_GET = 90015;
    public static final int MSG_FUNCTIONS_RENDER_RESPONSE_GET = 90016;
    public static final int MSG_FUNCTIONS_DRAW_TEST_PATTERN_RESPONSE_GET = 90017;
    public static final int MSG_FUNCTIONS_DRAW_TEXT_RESPONSE_GET = 90018;
    public static final int MSG_FUNCTIONS_DRAW_POINT_RESPONSE_GET = 90019;
    public static final int MSG_FUNCTIONS_DRAW_LINE_RESPONSE_GET = 90020;
    public static final int MSG_FUNCTIONS_DRAW_TRIANGLE_RESPONSE_GET = 90021;
    public static final int MSG_FUNCTIONS_DRAW_RECTANGLE_RESPONSE_GET = 90022;
    public static final int MSG_FUNCTIONS_RESTORE_FACTORY_RESPONSE_GET = 90023;
    public static final int MSG_FUNCTIONS_UPGRADE_SOFTWARE_RESPONSE_GET = 90024;
    public static final int MSG_FUNCTIONS_TEMPERATURE_LOOP_GET = 90025;

    public static Handler functionsHandler = new Handler() {
        Bundle b = null;
        int result = 0;
        int type = 0;
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FUNCTIONS_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    if (result == 0) {
                        int direction = b.getInt("direction");
                    }
                    break;
                case MSG_FUNCTIONS_VIEWPORT_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    type = b.getInt("type");
                    if(result==0) {
                        mTvViewPort.setText("Viewport offset: " + resultData.getDistortViewPort_offsetTopLeft()
                                + ", " + resultData.getDistortViewPort_offsetTopRight()
                                + ", " + resultData.getDistortViewPort_offsetLowerLeft()
                                + ", " + resultData.getDistortViewPort_offsetLowerRight());

                    } else{
                        String STR = b.getString("STR");
                        mTvViewPort.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_ASPECT_RATIO_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    type = b.getInt("type");
                    if(result==0) {
                        mTvAspectRatio.setText("Aspect Ratio: " + resultData.getAspectRatio());

                    } else{
                        String STR = b.getString("STR");
                        mTvAspectRatio.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_FLIP_STATE_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    type = b.getInt("type");
                    if(result==0) {
                        mTvFlipState.setText("Flip State: " + resultData.getFlipState());

                    } else{
                        String STR = b.getString("STR");
                        mTvFlipState.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_COLOR_CONVERTER_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    type = b.getInt("type");
                    if(result==0) {
                        mTvColorConverter.setText("Color Converter Coefficient: " + resultData.getCoefficient());

                    } else{
                        String STR = b.getString("STR");
                        mTvColorConverter.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_COMMIT_INPUT_CAPTURE_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    type = b.getInt("type");
                    if(result==0) {
                        mTvCommitInputCapture.setText("Video Mode: " + resultData.getVideoMode());

                    } else{
                        String STR = b.getString("STR");
                        mTvCommitInputCapture.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_ACTIVE_OSD_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    type = b.getInt("type");
                    if(result==0) {
                        mTvActiveOSD.setText("Render target: " + resultData.getRenderTarget());

                    } else{
                        String STR = b.getString("STR");
                        mTvActiveOSD.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_OSD_INFO_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    if(result==0) {
                        mTvOSDInfo.setText("OSD Info: X: " + b.getShort("startPointX")
                                + "  Y: " + b.getShort("startPointY")
                                + "  W: " + b.getShort("sizeWidth")
                                + "  H: " + b.getShort("sizeHeight"));
                    } else{
                        String STR = b.getString("STR");
                        mTvOSDInfo.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_OSD_STATE_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    if(result==0) {
                        mTvOSDState.setText("OSD State: " + resultData.getOSDState());

                    } else{
                        String STR = b.getString("STR");
                        mTvOSDState.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_SPLASH_SCREEN_TIMEOUT_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    if(result==0) {
                        mTvSplashScreenTimeout.setText("Splash Screen Timeout: " + resultData.getSplashScreenTimeout());

                    } else{
                        String STR = b.getString("STR");
                        mTvSplashScreenTimeout.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_ENUMERATE_DEVICES_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_DISPLAY_INFO_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    if(result==0) {
                        mTvDisplayInfo.setText("Display Info: W: " + b.getShort("sizeW")
                                + "  H: " + b.getShort("sizeH"));

                    } else{
                        String STR = b.getString("STR");
                        mTvDisplayInfo.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_TEXTBOX_INFO_RESPONSE_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    if(result==0) {
                        mTvTextBoxInfo.setText("TextBox Info: W: " + b.getShort("sizeW")
                                + "  H: " + b.getShort("sizeH"));

                    } else{
                        String STR = b.getString("STR");
                        mTvTextBoxInfo.setText("fun run failed, error = " + STR);
                    }
                    break;
                case MSG_FUNCTIONS_CLEAR_TARGET_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_LOAD_BITMAP_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_RENDER_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_DRAW_TEST_PATTERN_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_DRAW_TEXT_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_DRAW_POINT_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_DRAW_LINE_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_DRAW_TRIANGLE_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_DRAW_RECTANGLE_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_RESTORE_FACTORY_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_UPGRADE_SOFTWARE_RESPONSE_GET:
                    break;
                case MSG_FUNCTIONS_TEMPERATURE_LOOP_GET:
                    b = msg.getData();
                    result = b.getInt("result");
                    if(result==0) {
                        mTvTextBoxInfo.setText("TextBox Info: W: " + b.getShort("sizeW")
                                + "  H: " + b.getShort("sizeH"));

                    } else{
                        String STR = b.getString("STR");
                        mTvTextBoxInfo.setText("fun run failed, error = " + STR);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public Functions() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_functions, container, false);
        mButtonSetViewport = (Button) rootView.findViewById(R.id.btn_set_ViewportDistortion);
        mButtonSetViewport.setOnClickListener(btnClickListener);
        mButtonGetViewport = (Button) rootView.findViewById(R.id.btn_get_ViewportDistortion);
        mButtonGetViewport.setOnClickListener(btnClickListener);
        mButtonSetAspectratio = (Button) rootView.findViewById(R.id.btn_set_Aspectratiomode);
        mButtonSetAspectratio.setOnClickListener(btnClickListener);
        mButtonGetAspectratio = (Button) rootView.findViewById(R.id.btn_get_Aspectratiomode);
        mButtonGetAspectratio.setOnClickListener(btnClickListener);
        mButtonSetFlipState = (Button) rootView.findViewById(R.id.btn_set_FlipState);
        mButtonSetFlipState.setOnClickListener(btnClickListener);
        mButtonGatFlipState = (Button) rootView.findViewById(R.id.btn_get_FlipState);
        mButtonGatFlipState.setOnClickListener(btnClickListener);
        mButtonSetColorConverter = (Button) rootView.findViewById(R.id.btn_set_ColorConverter);
        mButtonSetColorConverter.setOnClickListener(btnClickListener);
        mButtonGatColorConverter = (Button) rootView.findViewById(R.id.btn_get_ColorConverter);
        mButtonGatColorConverter.setOnClickListener(btnClickListener);
        mButtonSetCommitICM = (Button) rootView.findViewById(R.id.btn_set_CommitInputCaptureMode);
        mButtonSetCommitICM.setOnClickListener(btnClickListener);
        mButtonGetCommitICM = (Button) rootView.findViewById(R.id.btn_get_CommitInputCaptureMode);
        mButtonGetCommitICM.setOnClickListener(btnClickListener);
        mButtonSetActiveOSD = (Button) rootView.findViewById(R.id.btn_set_ActiveOSD);
        mButtonSetActiveOSD.setOnClickListener(btnClickListener);
        mButtonGetActiveOSD = (Button) rootView.findViewById(R.id.btn_get_ActiveOSD);
        mButtonGetActiveOSD.setOnClickListener(btnClickListener);
        mButtonSetOSDInfo = (Button) rootView.findViewById(R.id.btn_set_OSDInfo);
        mButtonSetOSDInfo.setOnClickListener(btnClickListener);
        mButtonGetOSDInfo = (Button) rootView.findViewById(R.id.btn_get_OSDInfo);
        mButtonGetOSDInfo.setOnClickListener(btnClickListener);
        mButtonSetOSDState = (Button) rootView.findViewById(R.id.btn_set_OSDState);
        mButtonSetOSDState.setOnClickListener(btnClickListener);
        mButtonGetOSDState = (Button) rootView.findViewById(R.id.btn_get_OSDState);
        mButtonGetOSDState.setOnClickListener(btnClickListener);
        mButtonSetSplash = (Button) rootView.findViewById(R.id.btn_set_SplashScreenTimeout);
        mButtonSetSplash.setOnClickListener(btnClickListener);
        mButtonGetSplash = (Button) rootView.findViewById(R.id.btn_get_SplashScreenTimeout);
        mButtonGetSplash.setOnClickListener(btnClickListener);
//        mButtonGetEnumerateDevices = (Button) rootView.findViewById(R.id.btn_get_EnumerateDevices);
//        mButtonGetEnumerateDevices.setOnClickListener(btnClickListener);
        mButtonGetDisplayInfo = (Button) rootView.findViewById(R.id.btn_get_DisplayInfo);
        mButtonGetDisplayInfo.setOnClickListener(btnClickListener);
        mButtonGetTextBoxInfo = (Button) rootView.findViewById(R.id.btn_get_TextBoxInfo);
        mButtonGetTextBoxInfo.setOnClickListener(btnClickListener);
        mButtonGetClearTarget = (Button) rootView.findViewById(R.id.btn_get_ClearTarger);
        mButtonGetClearTarget.setOnClickListener(btnClickListener);
        mButtonGetLoadBitmapImage = (Button) rootView.findViewById(R.id.btn_get_LoadBitmapImage);
        mButtonGetLoadBitmapImage.setOnClickListener(btnClickListener);
        mButtonGetRender = (Button) rootView.findViewById(R.id.btn_get_Render);
        mButtonGetRender.setOnClickListener(btnClickListener);
        mButtonGetDrawTestPattern = (Button) rootView.findViewById(R.id.btn_get_DrawTestPattern);
        mButtonGetDrawTestPattern.setOnClickListener(btnClickListener);
        mButtonGetDrawText = (Button) rootView.findViewById(R.id.btn_get_DrawText);
        mButtonGetDrawText.setOnClickListener(btnClickListener);
        mButtonGetDrawPoint = (Button) rootView.findViewById(R.id.btn_get_DrawPoint);
        mButtonGetDrawPoint.setOnClickListener(btnClickListener);
        mButtonGetDrawLine = (Button) rootView.findViewById(R.id.btn_get_DrawLine);
        mButtonGetDrawLine.setOnClickListener(btnClickListener);
        mButtonGetDrawTriangle = (Button) rootView.findViewById(R.id.btn_get_DrawTriangle);
        mButtonGetDrawTriangle.setOnClickListener(btnClickListener);
        mButtonGetDrawRectangle = (Button) rootView.findViewById(R.id.btn_get_DrawRectangle);
        mButtonGetDrawRectangle.setOnClickListener(btnClickListener);
        mButtonGetRestore = (Button) rootView.findViewById(R.id.btn_get_RestoreFactoryConfig);
        mButtonGetRestore.setOnClickListener(btnClickListener);
//        mButtonGetDrawUpgrade = (Button) rootView.findViewById(R.id.btn_get_UpgradeSoftware);
//        mButtonGetDrawUpgrade.setOnClickListener(btnClickListener);
        mButtonGetTemperatureLoop = (Button) rootView.findViewById(R.id.btn_get_temperature_loop);
        mButtonGetTemperatureLoop.setOnClickListener(btnClickListener);

        mTvViewPort = (TextView)rootView.findViewById(R.id.fragment_function_viewport_tv_get);
        mTvViewPort.setText("");
        mTvAspectRatio = (TextView)rootView.findViewById(R.id.fragment_function_aspectratio_tv_get);
        mTvAspectRatio.setText("");
        mTvFlipState = (TextView)rootView.findViewById(R.id.fragment_function_flipstate_tv_get);
        mTvFlipState.setText("");
        mTvColorConverter = (TextView)rootView.findViewById(R.id.fragment_function_colorconverter_tv_get);
        mTvColorConverter.setText("");
        mTvCommitInputCapture = (TextView)rootView.findViewById(R.id.fragment_function_commitinputcapture_tv_get);
        mTvCommitInputCapture.setText("");
        mTvActiveOSD = (TextView)rootView.findViewById(R.id.fragment_function_activeOSD_tv_get);
        mTvActiveOSD.setText("");
        mTvOSDInfo = (TextView)rootView.findViewById(R.id.fragment_function_OSDinfo_tv_get);
        mTvOSDInfo.setText("");
        mTvOSDState = (TextView)rootView.findViewById(R.id.fragment_function_OSDstate_tv_get);
        mTvOSDState.setText("");
        mTvSplashScreenTimeout = (TextView)rootView.findViewById(R.id.fragment_function_splashscreentimeout_tv_get);
        mTvSplashScreenTimeout.setText("");
//        mTvEnumerateDevices = (TextView)rootView.findViewById(R.id.fragment_function_enumeratedevices_tv_get);
//        mTvEnumerateDevices.setText("");
        mTvDisplayInfo = (TextView)rootView.findViewById(R.id.fragment_function_displayinfo_tv_get);
        mTvDisplayInfo.setText("");
        mTvTextBoxInfo = (TextView)rootView.findViewById(R.id.fragment_function_textboxinfo_tv_get);
        mTvTextBoxInfo.setText("");
        mTvClearTarget = (TextView)rootView.findViewById(R.id.fragment_function_cleartarget_tv_get);
        mTvClearTarget.setText("");
        mTvLoadBitmap = (TextView)rootView.findViewById(R.id.fragment_function_loadbitmap_tv_get);
        mTvLoadBitmap.setText("");
        mTvRender = (TextView)rootView.findViewById(R.id.fragment_function_render_tv_get);
        mTvRender.setText("");
        mTvDrawTestPattern = (TextView)rootView.findViewById(R.id.fragment_function_drawtestpattern_tv_get);
        mTvDrawTestPattern.setText("");
        mTvDrawText = (TextView)rootView.findViewById(R.id.fragment_function_drawtext_tv_get);
        mTvDrawText.setText("");
        mTvDrawPoint = (TextView)rootView.findViewById(R.id.fragment_function_drawpoint_tv_get);
        mTvDrawPoint.setText("");
        mTvFrawLine = (TextView)rootView.findViewById(R.id.fragment_function_drawline_tv_get);
        mTvFrawLine.setText("");
        mTvDrawTriangle = (TextView)rootView.findViewById(R.id.fragment_function_drawtriangle_tv_get);
        mTvDrawTriangle.setText("");
        mTvDrawRectangle = (TextView)rootView.findViewById(R.id.fragment_function_drawrectangle_tv_get);
        mTvDrawRectangle.setText("");
        mTvRestoreFactory = (TextView)rootView.findViewById(R.id.fragment_function_restorefactory_tv_get);
        mTvRestoreFactory.setText("");
//        mTvUpgradeSoftware = (TextView)rootView.findViewById(R.id.fragment_function_upgradesoftware_tv_get);
//        mTvUpgradeSoftware.setText("");
        mTvTemperatureLoop = (TextView)rootView.findViewById(R.id.fragment_function_temperature_loop_tv_get);
        mTvTemperatureLoop.setText("");

        return rootView;
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_set_ViewportDistortion: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_VIEWPORTDISTORTION);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_ViewportDistortion: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_VIEWPORTDISTORTION);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_Aspectratiomode: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_ASPECTRATIOMODE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_Aspectratiomode: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_ASPECTRATIOMODE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_FlipState: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_FLIPSTATE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_FlipState: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_FLIPSTATE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_ColorConverter: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_COLORCONVERTER);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_ColorConverter: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_COLORCONVERTER);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_CommitInputCaptureMode: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_COMMITINPUTCAPTUREMODE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_CommitInputCaptureMode: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_COMMITINPUTCAPTUREMODE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_ActiveOSD: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_ACTIVEOSD);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_ActiveOSD: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_ACTIVEOSD);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_OSDInfo: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_OSDINFO);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_OSDInfo: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_OSDINFO);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_OSDState: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_OSDSTATE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_OSDState: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_OSDSTATE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_set_SplashScreenTimeout: {
                    Message msg0 = messageHandler.obtainMessage(MSG_SET_SPLASHSCREENTIMEOUT);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_SplashScreenTimeout: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_SPLASHSCREENTIMEOUT);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
//                case R.id.btn_get_EnumerateDevices: {
//                    Message msg0 = messageHandler.obtainMessage(MSG_GET_ENUMERATEDEVICES);
//                    Bundle b = new Bundle();
//                    msg0.setData(b);
//                    messageHandler.sendMessage(msg0);
//                    break;
//                }
                case R.id.btn_get_DisplayInfo: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_DISPLAYINFO);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_TextBoxInfo: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_TEXTBOXINFO);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_ClearTarger: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_CLEARTARGER);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_LoadBitmapImage: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_LOADBITMAPIMAGE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_Render: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_RENDER);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_DrawTestPattern: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_DRAWTESTPATTERN);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_DrawText: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_DRAWTEXT);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_DrawPoint: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_DRAWPOINT);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_DrawLine: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_DRAWLINE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_DrawTriangle: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_DRAWTRIANGLE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_DrawRectangle: {
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_DRAWRECTANGLE);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                case R.id.btn_get_RestoreFactoryConfig: {
                    Message msg0 = messageHandler.obtainMessage(MSG_RESTOREFACTORYCONFIG);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
//                case R.id.btn_get_UpgradeSoftware: {
//                    Message msg0 = messageHandler.obtainMessage(MSG_GET_UPGRADESOFTWARE);
//                    Bundle b = new Bundle();
//                    msg0.setData(b);
//                    messageHandler.sendMessage(msg0);
//                    break;
//                }
                case R.id.btn_get_temperature_loop:{
                    Message msg0 = messageHandler.obtainMessage(MSG_FUNCTIONS_TEMPERATURE_LOOP_GET);
                    Bundle b = new Bundle();
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                default:
                    break;
            }
        }
    };
}
