package com.goertek.microvision;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import picop.interfaces.def.ALC_Api;
import picop.interfaces.def.ALC_Api.ALC_Callback;
import picop.interfaces.def.PicoP_AspectRatioModeE;
import picop.interfaces.def.PicoP_Color;
import picop.interfaces.def.PicoP_ColorConvertE;
import picop.interfaces.def.PicoP_ColorE;
import picop.interfaces.def.PicoP_ColorModeE;
import picop.interfaces.def.PicoP_ConnectionTypeE;
import picop.interfaces.def.PicoP_DirectionE;
import picop.interfaces.def.PicoP_FlipStateE;
import picop.interfaces.def.PicoP_InputVideoStateE;
import picop.interfaces.def.PicoP_Event;
import picop.interfaces.def.PicoP_OSDStateE;
import picop.interfaces.def.PicoP_OutputVideoStateE;
import picop.interfaces.def.PicoP_Point;
import picop.interfaces.def.PicoP_RC;
import picop.interfaces.def.PicoP_RectSize;
import picop.interfaces.def.PicoP_RenderTargetE;
import picop.interfaces.def.PicoP_SystemInfo;
import picop.interfaces.def.PicoP_SystemStatus;
import picop.interfaces.def.PicoP_TestPatternInfoE;
import picop.interfaces.def.PicoP_Ulog;
import picop.interfaces.def.PicoP_ValueStorageTypeE;
import picop.interfaces.def.PicoP_VideoCaptureInfo;
import picop.interfaces.def.PicoP_VideoModeHandleE;

import static com.goertek.microvision.MainActivity.SERIAL_PORT_OPENED;
import static com.goertek.microvision.MainActivity.connectionInfo;
import static com.goertek.microvision.MainActivity.mContext;
import static com.goertek.microvision.MainActivity.mPicoPHandler;
import static com.goertek.microvision.Utils.MSG_CLOSE_CONNECTION;
import static com.goertek.microvision.Utils.MSG_FLIP_IMAGE;
import static com.goertek.microvision.Utils.MSG_GET_ACTIVECAPTUREMODE;
import static com.goertek.microvision.Utils.MSG_GET_ACTIVEOSD;
import static com.goertek.microvision.Utils.MSG_GET_ASPECTRATIOMODE;
import static com.goertek.microvision.Utils.MSG_GET_BRIGHTNESS;
import static com.goertek.microvision.Utils.MSG_GET_CLEARTARGER;
import static com.goertek.microvision.Utils.MSG_GET_COLORALIGNMENT;
import static com.goertek.microvision.Utils.MSG_GET_COLORCONVERTER;
import static com.goertek.microvision.Utils.MSG_GET_COLOR_MODE;
import static com.goertek.microvision.Utils.MSG_GET_COMMITINPUTCAPTUREMODE;
import static com.goertek.microvision.Utils.MSG_GET_DISPLAYINFO;
import static com.goertek.microvision.Utils.MSG_GET_DRAWLINE;
import static com.goertek.microvision.Utils.MSG_GET_DRAWPOINT;
import static com.goertek.microvision.Utils.MSG_GET_DRAWRECTANGLE;
import static com.goertek.microvision.Utils.MSG_GET_DRAWTESTPATTERN;
import static com.goertek.microvision.Utils.MSG_GET_DRAWTEXT;
import static com.goertek.microvision.Utils.MSG_GET_DRAWTRIANGLE;
//import static com.goertek.microvision.Utils.MSG_GET_ENUMERATEDEVICES;
import static com.goertek.microvision.Utils.MSG_GET_EVENTLOG;
import static com.goertek.microvision.Utils.MSG_GET_FLIPSTATE;
import static com.goertek.microvision.Utils.MSG_GET_GAMMAVAL;
import static com.goertek.microvision.Utils.MSG_GET_INPUTCAPTUREMODEINFO;
import static com.goertek.microvision.Utils.MSG_GET_INPUTVIDEOPROPERTIES;
import static com.goertek.microvision.Utils.MSG_GET_INPUTVIDEOSTATE;
import static com.goertek.microvision.Utils.MSG_GET_KEYSTONE;
import static com.goertek.microvision.Utils.MSG_GET_LOADBITMAPIMAGE;
import static com.goertek.microvision.Utils.MSG_GET_OSDINFO;
import static com.goertek.microvision.Utils.MSG_GET_OSDSTATE;
import static com.goertek.microvision.Utils.MSG_GET_OUTPUTVIDEOSTATE;
import static com.goertek.microvision.Utils.MSG_GET_PHASE;
import static com.goertek.microvision.Utils.MSG_GET_RENDER;
import static com.goertek.microvision.Utils.MSG_RESTOREFACTORYCONFIG;
import static com.goertek.microvision.Utils.MSG_GET_SPLASHSCREENTIMEOUT;
import static com.goertek.microvision.Utils.MSG_GET_SYSTEMINFO;
import static com.goertek.microvision.Utils.MSG_GET_SYSTEMSTATUS;
import static com.goertek.microvision.Utils.MSG_GET_TEXTBOXINFO;
//import static com.goertek.microvision.Utils.MSG_GET_UPGRADESOFTWARE;
import static com.goertek.microvision.Utils.MSG_GET_VIEWPORTDISTORTION;
import static com.goertek.microvision.Utils.MSG_MODIFY_INPUTCAPTUREMODEINFO;
import static com.goertek.microvision.Utils.MSG_SET_ACTIVECAPTUREMODE;
import static com.goertek.microvision.Utils.MSG_SET_ACTIVEOSD;
import static com.goertek.microvision.Utils.MSG_SET_ASPECTRATIOMODE;
import static com.goertek.microvision.Utils.MSG_SET_BRIGHTNESS;
import static com.goertek.microvision.Utils.MSG_SET_COLORALIGNMENT;
import static com.goertek.microvision.Utils.MSG_SET_COLORCONVERTER;
import static com.goertek.microvision.Utils.MSG_SET_COLOR_MODE;
import static com.goertek.microvision.Utils.MSG_SET_COMMITINPUTCAPTUREMODE;
import static com.goertek.microvision.Utils.MSG_SET_FLIPSTATE;
import static com.goertek.microvision.Utils.MSG_SET_GAMMAVAL;
import static com.goertek.microvision.Utils.MSG_SET_INPUTCAPTUREMODEINFO;
import static com.goertek.microvision.Utils.MSG_SET_INPUTVIDEOSTATE;
import static com.goertek.microvision.Utils.MSG_SET_KEYSTONE;
import static com.goertek.microvision.Utils.MSG_SET_OSDINFO;
import static com.goertek.microvision.Utils.MSG_SET_OSDSTATE;
import static com.goertek.microvision.Utils.MSG_SET_OUTPUTVIDEOSTATE;
import static com.goertek.microvision.Utils.MSG_SET_PHASE;

import static com.goertek.microvision.Utils.MSG_SET_SPLASHSCREENTIMEOUT;
import static com.goertek.microvision.Utils.MSG_SET_VIEWPORTDISTORTION;
import static com.goertek.microvision.fragment.ActiveCaptureMode.MSG_ACTIVEACTIVEMODE_RESPONSE_GET;
import static com.goertek.microvision.fragment.ActiveCaptureMode.activeCapModeHandler;
import static com.goertek.microvision.fragment.Brightness.MSG_BRIGHTNESS_RESPONSE_GET;
import static com.goertek.microvision.fragment.Brightness.brightnessHandler;
import static com.goertek.microvision.fragment.ColorAlignment.MSG_COLORALIGNMENT_RESPONSE_GET;
import static com.goertek.microvision.fragment.ColorAlignment.coloralignmentHandler;
import static com.goertek.microvision.fragment.ColorMode.MSG_COLORMODE_RESPONSE_GET;
import static com.goertek.microvision.fragment.ColorMode.colorModeHandler;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_ACTIVE_OSD_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_ASPECT_RATIO_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_COLOR_CONVERTER_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_COMMIT_INPUT_CAPTURE_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_DISPLAY_INFO_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_FLIP_STATE_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_OSD_INFO_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_OSD_STATE_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_SPLASH_SCREEN_TIMEOUT_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_TEMPERATURE_LOOP_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_TEXTBOX_INFO_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.MSG_FUNCTIONS_VIEWPORT_RESPONSE_GET;
import static com.goertek.microvision.fragment.Functions.functionsHandler;
import static com.goertek.microvision.fragment.GammaVal.MSG_GAMMAVAL_RESPONSE_GET;
import static com.goertek.microvision.fragment.GammaVal.gammavalHandler;
import static com.goertek.microvision.fragment.GetEventLog.MSG_EVENTLOG_RESPONSE_GET;
import static com.goertek.microvision.fragment.GetEventLog.eventlogHandler;
import static com.goertek.microvision.fragment.InputCaptureMode.MSG_INPUTCAPTURE_RESPONSE_GET;
import static com.goertek.microvision.fragment.InputCaptureMode.inputCaptureHandler;
import static com.goertek.microvision.fragment.InputVideoProperties.MSG_GET_INPUTVIDEOPROPERTIES_RESPONSE_GET;
import static com.goertek.microvision.fragment.InputVideoProperties.inputvideopropertiesHandler;
import static com.goertek.microvision.fragment.InputVideoState.MSG_INPUTVIDEOSTATE_RESPONSE_GET;
import static com.goertek.microvision.fragment.InputVideoState.inputvideostateHandler;
import static com.goertek.microvision.fragment.KeyStone.MSG_KEYSTONE_RESPONSE_GET;
import static com.goertek.microvision.fragment.KeyStone.keystoneHandler;
import static com.goertek.microvision.fragment.OutputVideoState.MSG_OUTPUTVIDEOSTATE_RESPONSE_GET;
import static com.goertek.microvision.fragment.OutputVideoState.outputvideostateHandler;
import static com.goertek.microvision.fragment.Phase.MSG_PHASE_RESPONSE_GET;
import static com.goertek.microvision.fragment.Phase.phaseHandler;
import static com.goertek.microvision.fragment.SystemInfo.MSG_GET_SYSTEMINFO_RESPONSE_GET;
import static com.goertek.microvision.fragment.SystemInfo.systeminfoHandler;
import static com.goertek.microvision.fragment.SystemStatus.MSG_GET_SYSTEMSTATUS_RESPONSE_GET;
import static com.goertek.microvision.fragment.SystemStatus.systemstatusHandler;
import static picop.interfaces.def.PicoP_ColorModeE.eCOLOR_MODE_BRILLIANT;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_EVENT_LOG;

/**
 * Created on 2017/3/28.
 */

public class MessageCenter {




    private static final String TAG = "MessageCenter";
    public static PicoP_VideoModeHandleE videoMode  = PicoP_VideoModeHandleE.eVideoModeHandle_640x480;
    public static PicoP_ColorModeE colorMode = PicoP_ColorModeE.eCOLOR_MODE_BRILLIANT;
    public static PicoP_BasicDataResult resultData = new PicoP_BasicDataResult();
    public static PicoP_Event[] pEvent = new PicoP_Event[MAX_EVENT_LOG];
    public static PicoP_SystemStatus systemStatus = new PicoP_SystemStatus();
    public static PicoP_SystemInfo systemInfo = new PicoP_SystemInfo();
    public static PicoP_VideoCaptureInfo inputCaptureModeInfo = new PicoP_VideoCaptureInfo();
    public static PicoP_RC RC_FOR_SEND = PicoP_RC.eSUCCESS;
    public static PicoP_RC RC_FOR_GET = PicoP_RC.eSUCCESS;

    /**
     * Callback function to get response from JAR lib.
     */
    static ALC_Callback callback = new ALC_Callback(){
        public void upgradeSoftwareCallback(int currentPacket, int numPackets, int destID){
        }
        public void brightnessCallback(float brightness){
            PicoP_BasicDataResult.setBrightness(brightness);
        }
        public void colorModeCallback(int colorMode){
            PicoP_BasicDataResult.setColormode(colorMode);
        }
        public void keyStoneConnectionCallback(int keyStoneCorrectionValue){
            PicoP_BasicDataResult.setKeyStoneCorrectionValue(keyStoneCorrectionValue);
        }
        public void colorAligmentCallback(int offset){
            PicoP_BasicDataResult.setOffset(offset);
        }
        public void getPhaseCallback(int phaseValue){
            PicoP_BasicDataResult.setPhaseValue(phaseValue);
        }
        public void gammavalCallback(float gammaVal){
            PicoP_BasicDataResult.setGammaval(gammaVal);
        }
        public void inputStateCallback(int state){
            PicoP_BasicDataResult.setInputstate(state);
        }
        public void outputStateCallback(int state){
            PicoP_BasicDataResult.setOutputstate(state);
        }
        public void activeCaptureModeCallback(int videoMode){
            PicoP_BasicDataResult.setVideoMode(videoMode);
        }
        public void inputVideoProCallback(float frameRate, int lines){
            PicoP_BasicDataResult.setFrameRate(frameRate);
            PicoP_BasicDataResult.setLines(lines);
        }
        public void distortViewPortCallback(float topLeft, float topRight, float lowerLeft, float lowerRight){
            PicoP_BasicDataResult.setDistortViewPort_offsetTopLeft(topLeft);
            PicoP_BasicDataResult.setDistortViewPort_offsetTopRight(topRight);
            PicoP_BasicDataResult.setDistortViewPort_offsetLowerLeft(lowerLeft);
            PicoP_BasicDataResult.setDistortViewPort_offsetLowerRight(lowerRight);
        }
        public void aspectRatioCallback(int aspectRatio){
            PicoP_BasicDataResult.setAspectRatio(aspectRatio);
        }
        public void flipStateCallback(int flipState){
            PicoP_BasicDataResult.setFlipState(flipState);
        }
        public void colorConverterCallback(int coefficient){
            PicoP_BasicDataResult.setCoefficient(coefficient);
        }
        public void commitedInputCaptureModeCallback(int videoMode){
            PicoP_BasicDataResult.setVideoMode(videoMode);
        }
        public void activeOSDCallback(int renderTarget){
            PicoP_BasicDataResult.setRenderTarget(renderTarget);
        }
        public void OSDStateCallback(int osdState){
            PicoP_BasicDataResult.setOSDState(osdState);
        }
        public void splashScreenTimeoutCallback(int splashScreenTimeout){
            PicoP_BasicDataResult.setSplashScreenTimeout(splashScreenTimeout);
        }
    };

    private static HandlerThread handlerThread = new HandlerThread("MessageHandlerThread");

    static {
        handlerThread.start();
    }

    public static Handler messageHandler = new Handler( handlerThread.getLooper(),
            new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            PicoP_Ulog.d(TAG, "Handler is handerling a message");
            //super.handleMessage(msg);
            switch(msg.what) {
                case MSG_SET_BRIGHTNESS:{
                    Bundle b = msg.getData();
                    float brightness = b.getFloat("brightness", 1.0f);
                    boolean commit = b.getBoolean("commit");
                    Log.i(TAG, "received message to set brightness, brightness = " + brightness + ", commit = " + commit);
                    doSetBrightness(brightness, commit);
                    break;
                }
                case MSG_GET_BRIGHTNESS:{
                    Bundle b = msg.getData();
                    int type = b.getInt("type");
                    Log.i(TAG, "received message to get brightness, type = " + type);
                    doGetBrightness(type);
                    break;
                }
                case MSG_SET_COLOR_MODE:{
                    Bundle b = msg.getData();
                    int mode = b.getInt("mode");
                    boolean commit = b.getBoolean("commit");
                    Log.i(TAG, "received message to set color mode, mode = " + mode +", commit = " + commit);
                    doSetColorMode(mode, commit);
                    break;
                }
                case MSG_GET_COLOR_MODE:{
                    Bundle b = msg.getData();
                    int type = b.getInt("type");
                    Log.i(TAG, "received message to get color mode, type = " + type);
                    doGetColorMode(type);
                    break;
                }
                case MSG_SET_KEYSTONE:{
                    Bundle b = msg.getData();
                    int keystone = b.getInt("keystone");
                    boolean commit = b.getBoolean("commit");
                    Log.i(TAG, "received message to set keystone, keystone = " + keystone + ", commit = " + commit);
                    doSetkeyStone(keystone, commit);
                    break;
                }
                case MSG_GET_KEYSTONE:{
                    Bundle b = msg.getData();
                    int type = b.getInt("type");
                    Log.i(TAG, "received message to get keystone, type = " + type);
                    doGetkeyStone(type);
                    break;
                }
                case MSG_SET_COLORALIGNMENT:{
                    Bundle b = msg.getData();
                    int coloralignment = b.getInt("coloralignment");
                    boolean commit = b.getBoolean("commit");
                    int color = b.getInt("color");
                    int direction = b.getInt("direction");
                    Log.i(TAG, "received message to set color alignment, coloralignment = " + coloralignment
                            + ", color = " + color + ", direction = " + direction + ", commit = "+ commit);
                    doSetcoloralign(coloralignment,direction, color, commit);
                    break;
                }
                case MSG_GET_COLORALIGNMENT:{
                    Bundle b = msg.getData();
                    int type = b.getInt("type");
                    int color = b.getInt("color");
                    int direction = b.getInt("direction");
                    Log.i(TAG, "received message to set color alignment color = " + color + ", direction = " + direction + ", type = "+ type);
                    doGetcoloralign(direction, color, type);
                    break;
                }
                case MSG_SET_PHASE:{
                    Bundle b = msg.getData();
                    int phase = b.getInt("phase");
                    boolean commit = b.getBoolean("commit");
                    Log.i(TAG, "received message to set phase, phase = " + phase + ", commit = " + commit);
                    doSetphase(phase, commit);
                    break;
                }
                case MSG_GET_PHASE:{
                    Bundle b = msg.getData();
                    int type = b.getInt("type");
                    Log.i(TAG, "received message to get keystone, type = " + type);
                    doGetphase(type);
                    break;
                }
                case MSG_SET_ACTIVECAPTUREMODE:{
                    Bundle b = msg.getData();
                    int mode = b.getInt("mode");
                    Log.i(TAG, "received message to set activecapturemode, mode = " + mode );
                    doSetactivecapturemode(mode);
                    break;
                }
                case MSG_GET_ACTIVECAPTUREMODE:{
                    Log.i(TAG, "received message to get activecapturemode");
                    doGetactivecapturemode();
                    break;
                }
                case MSG_SET_GAMMAVAL: {
                    Bundle b = msg.getData();
                    boolean commit = b.getBoolean("commit");
                    float gammaval = b.getFloat("gammaval");
                    int color = b.getInt("color");
                    Log.i(TAG, "received message to set gammaval, gammaval = " + gammaval + ", commit = " + commit);
                    doSetGammaval(gammaval, color, commit);
                    break;
                }
                case MSG_GET_GAMMAVAL:{
                    Bundle b = msg.getData();
                    int type = b.getInt("type");
                    int color = b.getInt("color");
                    Log.i(TAG, "received message to get gammaval, type = " + type + ", color = " + color);
                    doGetGammaval(type, color);
                    break;
                }
                case MSG_SET_OUTPUTVIDEOSTATE:{
                    Bundle b = msg.getData();
                    int state = b.getInt("state");
                    boolean commit = b.getBoolean("commit");
                    Log.i(TAG, "received message to set output video state, state = " + state + ", commit = " + commit);
                    doSetOutputVideoState(state, commit);
                    break;
                }
                case MSG_GET_OUTPUTVIDEOSTATE:{
                    Bundle b = msg.getData();
                    int state = b.getInt("state");
                    int storageType = b.getInt("type");
                    Log.i(TAG, "received message to get output video state, state = " + state + " Type = " + storageType);
                    doGetOutputVideoState(state, storageType);
                    break;
                }
                case MSG_SET_INPUTVIDEOSTATE:{
                    Bundle b = msg.getData();
                    int state = b.getInt("state");
                    Log.i(TAG, "received message to set output video state, state = " + state);
                    doSetInputVideoState(state);
                    break;
                }
                case MSG_GET_INPUTVIDEOSTATE:{
                    Bundle b = msg.getData();
                    int state = b.getInt("state");
                    Log.i(TAG, "received message to get output video state, state = " + state);
                    doGetInputVideoState(state);
                    break;
                }
                case MSG_FLIP_IMAGE: {
                    Bundle b = msg.getData();
                    int direction = b.getInt("direction");
                    Log.i(TAG, "received message to filp image, cmd = " + direction);
                    doFlipImage(direction);
                    break;
                }
                case MSG_SET_INPUTCAPTUREMODEINFO:{
                    Bundle b = msg.getData();
                    short x = (short) b.getInt("x");
                    short y = (short)b.getInt("y");
                    inputCaptureModeInfo.setVideoStartPosition(x, y);
                    short width = (short)b.getInt("w");
                    short height = (short)b.getInt("h");
                    inputCaptureModeInfo.setResolution(width,height);
                    inputCaptureModeInfo.setHSyncPolarity(b.getInt("hSyncPolarity"));
                    inputCaptureModeInfo.setVSyncPolarity(b.getInt("vSyncPolarity"));
                    inputCaptureModeInfo.setPixelClockEdge(b.getInt("pixelClockEdge"));
                    inputCaptureModeInfo.setColorSpace(b.getInt("colorspace"));
                    inputCaptureModeInfo.setInterlaceField(b.getInt("interlace"));
                    inputCaptureModeInfo.setPixelAspectRatio(b.getFloat("aspectRatio"));
                    doSetInputCaptureModeInfo();
                    Log.i(TAG, "received message to set inputCaptureMode");
                    break;
                }
                case MSG_MODIFY_INPUTCAPTUREMODEINFO:{
                    doModifyInputCaptureModeInfo();
                    Log.i(TAG, "received message to modify inputCaptureMode");
                    break;
                }
                case MSG_GET_INPUTCAPTUREMODEINFO:{
                    Bundle b = msg.getData();
                    int videomode = b.getInt("videomode");
                    doGetInputCaptureModeInfo(videomode);
                    Log.i(TAG, "received message to get inputCaptureMode");
                    break;
                }
                case MSG_GET_INPUTVIDEOPROPERTIES:{
                    doGetInputVideoProperties();
                    Log.i(TAG, "received message to get inputVideoProperties");
                    break;
                }
                case MSG_GET_SYSTEMSTATUS:{
                    doGetSystemStatus();
                    Log.i(TAG, "received message to get systemStatus");
                    break;
                }
                case MSG_GET_EVENTLOG:{
                    Bundle b = msg.getData();
                    short number = b.getShort("eventlognumber");
                    Log.i(TAG, "received message to get event log, number = " + number);
                    doGetEventLog(number);
                    break;
                }
                case MSG_GET_SYSTEMINFO:{
                    doGetSystemInfo();
                    Log.i(TAG, "received message to get systemInfo");
                    break;
                }
                case MSG_SET_VIEWPORTDISTORTION: {
                    doSetViewportDistortion();
                    Log.i(TAG, "received message to set viewportDistortion");
                    break;
                }
                case MSG_GET_VIEWPORTDISTORTION: {
                    doGetViewportDistortion();
                    Log.i(TAG, "received message to get viewportDistortion");
                    break;
                }
                case MSG_SET_ASPECTRATIOMODE: {
                    doSetAspectRatioMode();
                    Log.i(TAG, "received message to set aspectratiomode");
                    break;
                }
                case MSG_GET_ASPECTRATIOMODE: {
                    doGetAspectRatioMode();
                    Log.i(TAG, "received message to get aspectratiomode");
                    break;
                }
                case MSG_SET_FLIPSTATE: {
                    doSetFlipState();
                    Log.i(TAG, "received message to set flipState");
                    break;
                }
                case MSG_GET_FLIPSTATE: {
                    doGetFlipState();
                    Log.i(TAG, "received message to get flipState");
                    break;
                }
                case MSG_SET_COLORCONVERTER: {
                    doSetColorConverter();
                    Log.i(TAG, "received message to set colorConverter");
                    break;
                }
                case MSG_GET_COLORCONVERTER: {
                    doGetColorConverter();
                    Log.i(TAG, "received message to get colorConverter");
                    break;
                }
                case MSG_SET_COMMITINPUTCAPTUREMODE: {
                    doSetCommitInputCaptureMode();
                    Log.i(TAG, "received message to set commitInputCaptureMode");
                    break;
                }
                case MSG_GET_COMMITINPUTCAPTUREMODE: {
                    doGetCommitInputCaptureMode();
                    Log.i(TAG, "received message to get commitInputCaptureMode");
                    break;
                }
                case MSG_SET_ACTIVEOSD: {
                    doSetActiveOSD();
                    Log.i(TAG, "received message to set activeOSD");
                    break;
                }
                case MSG_GET_ACTIVEOSD: {
                    doGetActiveOSD();
                    Log.i(TAG, "received message to get activeOSD");
                    break;
                }
                case MSG_SET_OSDINFO: {
                    doSetOSDInfo();
                    Log.i(TAG, "received message to set OSDInfo");
                    break;
                }
                case MSG_GET_OSDINFO: {
                    doGetOSDInfo();
                    Log.i(TAG, "received message to get OSDInfo");
                    break;
                }
                case MSG_SET_OSDSTATE: {
                    doSetOSDState();
                    Log.i(TAG, "received message to set OSDState");
                    break;
                }
                case MSG_GET_OSDSTATE: {
                    doGetOSDState();
                    Log.i(TAG, "received message to get OSDState");
                    break;
                }
                case MSG_SET_SPLASHSCREENTIMEOUT: {
                    doSetSplashScreenTimeout();
                    Log.i(TAG, "received message to set splashScreenTimeout");
                    break;
                }
                case MSG_GET_SPLASHSCREENTIMEOUT: {
                    doGetSplashScreenTimeout();
                    Log.i(TAG, "received message to get splashScreenTimeout");
                    break;
                }
//                case MSG_GET_ENUMERATEDEVICES: {
//                    doGetEnumerateDevices();
//                    Log.i(TAG, "received message to get enumerateDevices");
//                    break;
//                }
                case MSG_GET_DISPLAYINFO: {
                    doGetDisplayInfo();
                    Log.i(TAG, "received message to get displayInfo");
                    break;
                }
                case MSG_GET_TEXTBOXINFO: {
                    doGetTextBoxInfo();
                    Log.i(TAG, "received message to get textBoxInfo");
                    break;
                }
                case MSG_GET_CLEARTARGER: {
                    doGetClearTarger();
                    Log.i(TAG, "received message to get clearTarger");
                    break;
                }
                case MSG_GET_LOADBITMAPIMAGE: {
                    doGetLoadBitmapImage();
                    Log.i(TAG, "received message to get loadBitmapImage");
                    break;
                }
                case MSG_GET_RENDER: {
                    doGetRender();
                    Log.i(TAG, "received message to get render");
                    break;
                }
                case MSG_GET_DRAWTESTPATTERN: {
                    int patternInt = msg.getData().getInt("pattern");
                    PicoP_TestPatternInfoE pattern = PicoP_TestPatternInfoE.values()[patternInt];

                    int foregroundColor = msg.getData().getInt("color");
                    PicoP_Color color = new PicoP_Color();
                    color.R = (byte) Color.red(foregroundColor);
                    color.G = (byte)Color.green(foregroundColor);
                    color.B = (byte)Color.blue(foregroundColor);
                    color.A = (byte)0xff;

                    doGetDrawTestPattern(pattern, color);
                    Log.i(TAG, "Finished handling message to drawTestPattern");
                    break;
                }
                case MSG_GET_DRAWTEXT: {
                    doGetDrawText();
                    Log.i(TAG, "received message to get drawText");
                    break;
                }
                case MSG_GET_DRAWPOINT: {
                    doGetDrawPoint();
                    Log.i(TAG, "received message to get drawPoint");
                    break;
                }
                case MSG_GET_DRAWLINE: {
                    doGetDrawLine();
                    Log.i(TAG, "received message to get drawLine");
                    break;
                }
                case MSG_GET_DRAWTRIANGLE: {
                    doGetDrawTriangle();
                    Log.i(TAG, "received message to get drawTriangle");
                    break;
                }
                case MSG_GET_DRAWRECTANGLE: {
                    doGetDrawRectangle();
                    Log.i(TAG, "received message to get drawRectangle");
                    break;
                }
                case MSG_RESTOREFACTORYCONFIG: {
                    doRestoreFactoryConfig();
                    Log.i(TAG, "received message to get restoreFactoryConfig");
                    break;
                }
//                case MSG_GET_UPGRADESOFTWARE: {
//                    doGetUpgradeSoftware();
//                    Log.i(TAG, "received message to get upgradeSoftware");
//                    break;
//                }
                case MSG_FUNCTIONS_TEMPERATURE_LOOP_GET: {
                    doGetTemperature();
                    Log.i(TAG, "received message to get temperatureLoop");
                    break;
                }
                case MSG_CLOSE_CONNECTION:{
                    doClose();
                    Log.i(TAG, "received message to close connection");
                    break;
                }
            }

            return true;
        }
    });


    private static boolean doInit() {
        PicoP_RC ret = ALC_Api.PicoP_ALC_OpenLibrary(mPicoPHandler);
        if(PicoP_RC.eSUCCESS!=ret){
            Log.e(TAG,"open library failed, it should not happened " + ret);
            return false;
        }
        // For serial port
//        ret = ALC_Api.PicoP_ALC_OpenConnection(mPicoPHandler,PicoP_ConnectionTypeE.eRS232, connectionInfo);


        // For USB port
        ret = ALC_Api.PicoP_ALC_OpenConnection(mPicoPHandler,PicoP_ConnectionTypeE.eUSB, connectionInfo);

        if(PicoP_RC.eSUCCESS!=ret){
            SERIAL_PORT_OPENED = false;
            Log.e(TAG,"open conn failed, it should not happened " + ret);
            return false;
        }

        SERIAL_PORT_OPENED = true;
        return true;
    }

    private static boolean doClose() {
//        PicoP_RC ret = ALC_Api.PicoP_ALC_CloseConnection(mPicoPHandler, PicoP_ConnectionTypeE.eRS232);
        PicoP_RC ret = ALC_Api.PicoP_ALC_CloseConnection(mPicoPHandler, PicoP_ConnectionTypeE.eUSB);

        SERIAL_PORT_OPENED = false;
        return true;
    }

    private static void doSetBrightness(float brightness, boolean commit){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetBrightnessVal(mPicoPHandler, brightness, commit);
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetBrightnessVal(mPicoPHandler, brightness, commit);
            }
        }
    }

    private static void doGetBrightness(int type){
        RC_FOR_GET = PicoP_RC.eFAILURE;

        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetBrightnessVal(mPicoPHandler, callback, intToStorageTye(type));
            Message msg = brightnessHandler.obtainMessage(MSG_BRIGHTNESS_RESPONSE_GET);
            Bundle b = new Bundle();
            b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
            b.putString("STR",RC_FOR_GET.toString());
            b.putInt("type", type);
            msg.setData(b);
            brightnessHandler.sendMessage(msg);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetBrightnessVal(mPicoPHandler, callback, intToStorageTye(type));
                Message msg = brightnessHandler.obtainMessage(MSG_BRIGHTNESS_RESPONSE_GET);
                Bundle b = new Bundle();
                b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
                b.putString("STR",RC_FOR_GET.toString());
                b.putInt("type", type);
                msg.setData(b);
                brightnessHandler.sendMessage(msg);
            }
        }
    }

    private static void doSetColorMode(int mode, boolean commit){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            if( 0 == mode){
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorMode(mPicoPHandler, PicoP_ColorModeE.eCOLOR_MODE_BRILLIANT, commit);
            } else if (1 == mode) {
                RC_FOR_SEND =  ALC_Api.PicoP_ALC_SetColorMode(mPicoPHandler, PicoP_ColorModeE.eCOLOR_MODE_STANDARD, commit);
            } else if(2 == mode){
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorMode(mPicoPHandler, PicoP_ColorModeE.eCOLOR_MODE_INVERTED, commit);
            }

        } else{
            if(doInit()) {
                if (0 == mode) {
                    RC_FOR_SEND =  ALC_Api.PicoP_ALC_SetColorMode(mPicoPHandler, eCOLOR_MODE_BRILLIANT, commit);
                } else if (1 == mode) {
                    RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorMode(mPicoPHandler, PicoP_ColorModeE.eCOLOR_MODE_STANDARD, commit);
                } else if (2 == mode) {
                    RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorMode(mPicoPHandler, PicoP_ColorModeE.eCOLOR_MODE_INVERTED, commit);
                }
            }
        }
    }

    private static void doGetColorMode(int type){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetColorMode(mPicoPHandler, callback, intToStorageTye(type));
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetColorMode(mPicoPHandler, callback, intToStorageTye(type));
            }
        }
        Message msg = colorModeHandler.obtainMessage(MSG_COLORMODE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("type", type);
        msg.setData(b);
        colorModeHandler.sendMessage(msg);

    }

    private static void doSetkeyStone(int keystone, boolean commit){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_CorrectKeystone(mPicoPHandler, keystone, commit);
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_CorrectKeystone(mPicoPHandler, keystone, commit);
            }
        }
    }

    private static void doGetkeyStone(int type){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetKeystoneCorrection(mPicoPHandler, callback, intToStorageTye(type));
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetKeystoneCorrection(mPicoPHandler, callback, intToStorageTye(type));
            }
        }
        Message msg = keystoneHandler.obtainMessage(MSG_KEYSTONE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("type", type);
        msg.setData(b);
        keystoneHandler.sendMessage(msg);
    }

    private static void doSetcoloralign(int coloralignment, int direction, int color, boolean commit){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorAlignment(mPicoPHandler,intToDirection(direction), intToColor(color), (short)coloralignment, commit);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorAlignment(mPicoPHandler, intToDirection(direction), intToColor(color), (short) coloralignment, commit);
            }
        }
    }

    private static void doGetcoloralign(int direction, int color, int type){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetColorAlignment(mPicoPHandler,intToDirection(direction),intToColor(color), callback, intToStorageTye(type));
        } else{
            if(doInit()) {
                RC_FOR_GET =  ALC_Api.PicoP_ALC_GetColorAlignment(mPicoPHandler, intToDirection(direction), intToColor(color), callback, intToStorageTye(type));
            }
        }
        Message msg = coloralignmentHandler.obtainMessage(MSG_COLORALIGNMENT_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("type", type);
        b.putInt("direction", direction);
        b.putInt("color",color);
        msg.setData(b);
        coloralignmentHandler.sendMessage(msg);
    }

    private static void doSetphase(int phase, boolean commit){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND =  ALC_Api.PicoP_ALC_SetPhase(mPicoPHandler, (short)phase, commit);
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetPhase(mPicoPHandler, (short)phase, commit);
            }
        }
    }

    private static void doGetphase(int type){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetPhase(mPicoPHandler, callback, intToStorageTye(type));
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetPhase(mPicoPHandler, callback, intToStorageTye(type));
            }
        }
        Message msg = phaseHandler.obtainMessage(MSG_PHASE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("type", type);
        msg.setData(b);
        phaseHandler.sendMessage(msg);
    }

    private static void doSetactivecapturemode(int mode){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetActiveCaptureMode(mPicoPHandler, videoMode.inttoEnum(mode));
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetActiveCaptureMode(mPicoPHandler, videoMode.inttoEnum(mode));
            }
        }
    }

    private static void doGetactivecapturemode(){
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetActiveCaptureMode(mPicoPHandler, callback);
            Message msg = activeCapModeHandler.obtainMessage(MSG_ACTIVEACTIVEMODE_RESPONSE_GET);
            Bundle b = new Bundle();
            b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
            b.putString("STR",RC_FOR_GET.toString());
            msg.setData(b);
            activeCapModeHandler.sendMessage(msg);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetActiveCaptureMode(mPicoPHandler, callback);
                Message msg = activeCapModeHandler.obtainMessage(MSG_ACTIVEACTIVEMODE_RESPONSE_GET);
                Bundle b = new Bundle();
                b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
                b.putString("STR",RC_FOR_GET.toString());
                msg.setData(b);
                activeCapModeHandler.sendMessage(msg);
            }
        }
    }

    private static void doFlipImage(int direction){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_FlipImage(mPicoPHandler, intToDirection(direction));
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_FlipImage(mPicoPHandler, intToDirection(direction));
            }
        }
    }

    private static void doSetInputCaptureModeInfo(){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetInputCaptureModeInfo(mPicoPHandler, videoMode, inputCaptureModeInfo);
        } else {
            if(doInit()){
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetInputCaptureModeInfo(mPicoPHandler, videoMode, inputCaptureModeInfo);
            }
        }

    }

    private static void doModifyInputCaptureModeInfo(){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_ModifyInputCaptureModeInfo(mPicoPHandler, videoMode, inputCaptureModeInfo);
        } else {
            if(doInit()){
                RC_FOR_SEND = ALC_Api.PicoP_ALC_ModifyInputCaptureModeInfo(mPicoPHandler, videoMode, inputCaptureModeInfo);
            }
        }

    }

    private static void doGetInputCaptureModeInfo(int videomode){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        videoMode = videoMode.inttoEnum(videomode);
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_GetInputCaptureModeInfo(mPicoPHandler, videoMode, inputCaptureModeInfo);
        } else {
            if(doInit()){
                RC_FOR_SEND = ALC_Api.PicoP_ALC_GetInputCaptureModeInfo(mPicoPHandler, videoMode, inputCaptureModeInfo);
            }
        }

        Message msg = inputCaptureHandler.obtainMessage(MSG_INPUTCAPTURE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        inputCaptureHandler.sendMessage(msg);
    }

    private static void doGetInputVideoProperties(){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetInputVideoProperties(mPicoPHandler,callback);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetInputVideoProperties(mPicoPHandler, callback);
            }
        }
        Message msg = inputvideopropertiesHandler.obtainMessage(MSG_GET_INPUTVIDEOPROPERTIES_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        inputvideopropertiesHandler.sendMessage(msg);
    }
    private static void doGetSystemStatus(){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetSystemStatus(mPicoPHandler,systemStatus);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetSystemStatus(mPicoPHandler, systemStatus);
            }
        }
        Message msg = systemstatusHandler.obtainMessage(MSG_GET_SYSTEMSTATUS_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        systemstatusHandler.sendMessage(msg);
    }

    private static void doGetSystemInfo(){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetSystemInfo(mPicoPHandler, systemInfo);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetSystemInfo(mPicoPHandler, systemInfo);
            }
        }
        Message msg = systeminfoHandler.obtainMessage(MSG_GET_SYSTEMINFO_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        systeminfoHandler.sendMessage(msg);
    }

    private static void doSetGammaval(float gammaVal, int color, boolean commit){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetGammaVal(mPicoPHandler, intToColor(color), gammaVal,commit);
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetGammaVal(mPicoPHandler, intToColor(color),gammaVal, commit);
            }
        }
    }

    private static void doGetGammaval(int type, int color){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetGammaVal(mPicoPHandler, intToColor(color), callback, intToStorageTye(type));
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetGammaVal(mPicoPHandler, intToColor(color), callback, intToStorageTye(type));
            }
        }
        Message msg = gammavalHandler.obtainMessage(MSG_GAMMAVAL_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("type", type);
        b.putInt("color", color);
        msg.setData(b);
        gammavalHandler.sendMessage(msg);
    }
    private static void doSetOutputVideoState(int state, boolean commit){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetOutputVideoState(mPicoPHandler, intToOutputVideoState(state), commit);
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetOutputVideoState(mPicoPHandler, intToOutputVideoState(state), commit);
            }
        }
    }
    private static void doGetOutputVideoState(int state, int storageType){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetOutputVideoState(mPicoPHandler, callback, intToStorageTye(storageType));
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetOutputVideoState(mPicoPHandler, callback, intToStorageTye(storageType));
            }
        }
        Message msg = outputvideostateHandler.obtainMessage(MSG_OUTPUTVIDEOSTATE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        outputvideostateHandler.sendMessage(msg);
    }
    private static void doSetInputVideoState(int state){
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetInputVideoState(mPicoPHandler, intToInputVideoState(state));
        } else{
            if(doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetInputVideoState(mPicoPHandler, intToInputVideoState(state));
            }
        }
    }
    private static void doGetInputVideoState(int state){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetInputVideoState(mPicoPHandler, callback);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetInputVideoState(mPicoPHandler, callback);
            }
        }
        Message msg = inputvideostateHandler.obtainMessage(MSG_INPUTVIDEOSTATE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("state", state);
        msg.setData(b);
        inputvideostateHandler.sendMessage(msg);
    }

    private static void doGetEventLog(short eventLogNumber){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetEventLog(mPicoPHandler, eventLogNumber, pEvent);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetEventLog(mPicoPHandler, eventLogNumber, pEvent);
            }
        }
        Message msg = eventlogHandler.obtainMessage(MSG_EVENTLOG_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("eventLogNumber", eventLogNumber);
        msg.setData(b);
        eventlogHandler.sendMessage(msg);
    }

    //ling
    private static void doSetViewportDistortion() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;

        float offsetTopLeft = 10, fOffsetTopRight = 50, fOffsetLowerLeft = 50, fOffsetLowerRight = 100;
        boolean bCommit = false;

        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetViewportDistortion(mPicoPHandler, offsetTopLeft, fOffsetTopRight, fOffsetLowerLeft, fOffsetLowerRight, bCommit);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetViewportDistortion(mPicoPHandler, offsetTopLeft, fOffsetTopRight, fOffsetLowerLeft, fOffsetLowerRight, bCommit);
            }
        }
    }

    private static void doGetViewportDistortion() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetViewportDistortion(mPicoPHandler, callback, intToStorageTye(0));
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetViewportDistortion(mPicoPHandler, callback, intToStorageTye(0));
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_VIEWPORT_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }

    private static void doSetAspectRatioMode() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;

        PicoP_AspectRatioModeE aspectRatio = PicoP_AspectRatioModeE.eASPECT_RATIO_ZOOM_HORIZONTAL;
        boolean bCommit = false;

        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetAspectRatioMode(mPicoPHandler, aspectRatio, bCommit);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetAspectRatioMode(mPicoPHandler, aspectRatio, bCommit);
            }
        }
    }

    private static void doGetAspectRatioMode() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetAspectRatioMode(mPicoPHandler, callback, intToStorageTye(0));
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetAspectRatioMode(mPicoPHandler, callback, intToStorageTye(0));
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_ASPECT_RATIO_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }

    private static void doSetFlipState() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        PicoP_FlipStateE flipState = PicoP_FlipStateE.eFLIP_BOTH;
        boolean bCommit = false;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetFlipState(mPicoPHandler, flipState, bCommit);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetFlipState(mPicoPHandler, flipState, bCommit);
            }
        }
    }

    private static void doGetFlipState() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetFlipState(mPicoPHandler, callback, intToStorageTye(0));
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetFlipState(mPicoPHandler, callback, intToStorageTye(0));
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_FLIP_STATE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }

    private static void doSetColorConverter() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        PicoP_ColorConvertE color = PicoP_ColorConvertE.eRED_TO_GREEN;   // offset = 0
        int offset = 0;
        //PicoP_ColorConvertE color = PicoP_ColorConvertE.eGREEN_TO_GREEN; // offset = 0x8000
        //int offset = 0x8000;
        boolean commit = false;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorConverter(mPicoPHandler, color, offset, commit);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetColorConverter(mPicoPHandler, color, offset, commit);
            }
        }
    }

    private static void doGetColorConverter() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_ColorConvertE color = PicoP_ColorConvertE.eRED_TO_GREEN;   // offset = 0
        //PicoP_ColorConvertE color = PicoP_ColorConvertE.eGREEN_TO_GREEN; // offset = 0x8000
        int type = 0; // PicoP_ValueStorageTypeE.eCURRENT_VALUE
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetColorConverter(mPicoPHandler, callback, color, intToStorageTye(type));
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetColorConverter(mPicoPHandler, callback, color, intToStorageTye(type));
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_COLOR_CONVERTER_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putInt("type", type);
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }
    private static void doSetCommitInputCaptureMode() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        PicoP_VideoModeHandleE mVideoMode  = PicoP_VideoModeHandleE.eVideoModeHandle_720p;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_CommitInputCaptureMode(mPicoPHandler, mVideoMode);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_CommitInputCaptureMode(mPicoPHandler, mVideoMode);
            }
        }
    }

    private static void doGetCommitInputCaptureMode() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetCommitedInputCaptureMode(mPicoPHandler, callback);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetCommitedInputCaptureMode(mPicoPHandler, callback);
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_COMMIT_INPUT_CAPTURE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }
    private static void doSetActiveOSD() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eOSD_0;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetActiveOSD(mPicoPHandler, target);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetActiveOSD(mPicoPHandler, target);
            }
        }
    }

    private static void doGetActiveOSD() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetActiveOSD(mPicoPHandler, callback);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetActiveOSD(mPicoPHandler, callback);
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_ACTIVE_OSD_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }

    private static void doSetOSDInfo() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        PicoP_Point startPoint = new PicoP_Point();
        startPoint.setPicoP_Point((short)0, (short)0);
        PicoP_RectSize size = new PicoP_RectSize();
        size.setPicoP_RectSize((short)273, (short)160);

        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetOSDInfo(mPicoPHandler, startPoint, size);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetOSDInfo(mPicoPHandler, startPoint, size);
            }
        }
    }

    private static void doGetOSDInfo() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_Point startPoint = new PicoP_Point();
        PicoP_RectSize size = new PicoP_RectSize();
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetOSDInfo(mPicoPHandler, startPoint, size);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetOSDInfo(mPicoPHandler, startPoint, size);
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_OSD_INFO_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putShort("startPointX", startPoint.getX_value());
        b.putShort("startPointY", startPoint.getY_value());
        b.putShort("sizeWidth", size.getWidth());
        b.putShort("sizeHeight", size.getHeight());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }

    private static void doSetOSDState() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        PicoP_OSDStateE osdState = PicoP_OSDStateE.eOSD_ENABLED;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetOSDState(mPicoPHandler, osdState);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetOSDState(mPicoPHandler, osdState);
            }
        }
    }

    private static void doGetOSDState() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetOSDState(mPicoPHandler, callback);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetOSDState(mPicoPHandler, callback);
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_OSD_STATE_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }

    private static void doSetSplashScreenTimeout() {
        RC_FOR_SEND = PicoP_RC.eFAILURE;
        int nTimeout = 2000;
        boolean bCommit = false;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_SEND = ALC_Api.PicoP_ALC_SetSplashScreenTimeout(mPicoPHandler, nTimeout, bCommit);
        } else {
            if (doInit()) {
                RC_FOR_SEND = ALC_Api.PicoP_ALC_SetSplashScreenTimeout(mPicoPHandler, nTimeout, bCommit);
            }
        }
    }

    private static void doGetSplashScreenTimeout() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetSplashScreenTimeout(mPicoPHandler, callback, intToStorageTye(0));
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetSplashScreenTimeout(mPicoPHandler, callback, intToStorageTye(0));
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_SPLASH_SCREEN_TIMEOUT_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }
//    private static void doGetEnumerateDevices() {
//        RC_FOR_GET = PicoP_RC.eFAILURE;
//        if (SERIAL_PORT_OPENED) {
//            //RC_FOR_GET = ALC_Api.PicoP_ALC_GetEnumerateDevices(mPicoPHandler);
//        } else {
//            if (doInit()) {
//                //RC_FOR_GET = ALC_Api.PicoP_ALC_GetEnumerateDevices(mPicoPHandler);
//            }
//        }
//    }
    private static void doGetDisplayInfo() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eOSD_0;
        PicoP_RectSize size = new PicoP_RectSize();
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetDisplayInfo(mPicoPHandler, target, size);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetDisplayInfo(mPicoPHandler, target, size);
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_DISPLAY_INFO_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putShort("sizeW", size.getWidth());
        b.putShort("sizeH", size.getHeight());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }
    private static void doGetTextBoxInfo() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        String str = "This is test for TextBoxInfo.";
        byte[] text = str.getBytes();
        PicoP_RectSize size = new PicoP_RectSize();
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetTextBoxInfo(mPicoPHandler, text, (short)text.length, size);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetTextBoxInfo(mPicoPHandler, text, (short)text.length, size);
            }
        }
        Message msg = functionsHandler.obtainMessage(MSG_FUNCTIONS_TEXTBOX_INFO_RESPONSE_GET);
        Bundle b = new Bundle();
        b.putInt("result", RC_FOR_GET.enumtoInt(RC_FOR_GET));
        b.putString("STR",RC_FOR_GET.toString());
        b.putShort("sizeW", size.getWidth());
        b.putShort("sizeH", size.getHeight());
        msg.setData(b);
        functionsHandler.sendMessage(msg);
    }
    private static void doGetClearTarger() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eOSD_0;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_ClearTarget(mPicoPHandler, target);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_ClearTarget(mPicoPHandler, target);
            }
        }
    }
    private static void doGetLoadBitmapImage() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eOSD_0;
        PicoP_Point startPoint = new PicoP_Point();
        startPoint.setPicoP_Point((short)100, (short)100);
        PicoP_RectSize size = new PicoP_RectSize();
        size.setPicoP_RectSize((short)10, (short)10);
        byte[] image = new byte[400];
        for(int i=0; i<image.length; i++){
            image[i] = 'F';
        }
        int nSize = image.length;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_LoadBitmapImage(mPicoPHandler, target, startPoint, size, image, nSize);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_LoadBitmapImage(mPicoPHandler, target, startPoint, size, image, nSize);
            }
        }
    }
    private static void doGetRender() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_Render(mPicoPHandler);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_Render(mPicoPHandler);
            }
        }
    }

    private static void doGetDrawTestPattern(PicoP_TestPatternInfoE pattern, PicoP_Color foregroundColor) {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eFRAME_BUFFER_0;
        PicoP_Point startPoint = new PicoP_Point();
        startPoint.setPicoP_Point((short)0, (short)0);
        PicoP_RectSize size = new PicoP_RectSize();
        size.setPicoP_RectSize((short)1024, (short)720);
        PicoP_Color backgroundColor = new PicoP_Color();
        backgroundColor.R = (byte)0x00;
        backgroundColor.G = (byte)0x00;
        backgroundColor.B = (byte)0x00;
        backgroundColor.A = (byte)0x00;
//        PicoP_OutputVideoStateE state = PicoP_OutputVideoStateE.eOUTPUT_VIDEO_DISABLED;

        if (!SERIAL_PORT_OPENED) {
            if(!doInit()){
                return;
            }
        }

        RC_FOR_GET = ALC_Api.PicoP_ALC_DrawTestPattern(mPicoPHandler, target, startPoint, size, foregroundColor, backgroundColor, pattern);

    }
    private static void doGetDrawText() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eOSD_0;
        String str = "This is test for draw text.";
        byte[] text = str.getBytes();
        PicoP_Point startPoint = new PicoP_Point();
        startPoint.setPicoP_Point((short)100, (short)100);
        PicoP_Color textColor = new PicoP_Color();
        textColor.R = 100;
        textColor.G = 0;
        textColor.B = 0;
        textColor.A = 0;
        PicoP_Color backgroundColor  = new PicoP_Color();
        backgroundColor.R = 0;
        backgroundColor.G = 0;
        backgroundColor.B = 0;
        backgroundColor.A = 0;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_DrawText(mPicoPHandler, target, text, (short)text.length, startPoint, textColor, backgroundColor);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_DrawText(mPicoPHandler, target, text, (short)text.length, startPoint, textColor, backgroundColor);
            }
        }
    }
    private static void doGetDrawPoint() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eOSD_0;
        PicoP_Point pixel = new PicoP_Point();
        pixel.setPicoP_Point((short)100, (short)100);
        PicoP_Color color = new PicoP_Color();
        color.R = 100;
        color.G = 0;
        color.B = 0;
        color.A = 0;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_DrawPoint(mPicoPHandler, target, pixel, color);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_DrawPoint(mPicoPHandler, target, pixel, color);
            }
        }
    }
    private static void doGetDrawLine() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eFRAME_BUFFER_0;
        PicoP_Point pointA = new PicoP_Point();
        pointA.setPicoP_Point((short)100, (short)100);
        PicoP_Point pointB = new PicoP_Point();
        pointB.setPicoP_Point((short)400, (short)400);
        PicoP_Color color = new PicoP_Color();
        color.R = 100;
        color.G = 0;
        color.B = 0;
        color.A = 0;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_DrawLine(mPicoPHandler, target, pointA, pointB, color);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_DrawLine(mPicoPHandler, target, pointA, pointB, color);
            }
        }
    }
    private static void doGetDrawTriangle() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eFRAME_BUFFER_0;
        PicoP_Point pointA = new PicoP_Point();
        pointA.setPicoP_Point((short)100, (short)100);
        PicoP_Point pointB = new PicoP_Point();
        pointB.setPicoP_Point((short)100, (short)400);
        PicoP_Point pointC = new PicoP_Point();
        pointC.setPicoP_Point((short)400, (short)400);
        PicoP_Color fillColor = new PicoP_Color();
        fillColor.R = 100;
        fillColor.G = 0;
        fillColor.B = 0;
        fillColor.A = 0;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_DrawTriangle(mPicoPHandler, target, pointA, pointB, pointC, fillColor);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_DrawTriangle(mPicoPHandler, target, pointA, pointB, pointC, fillColor);
            }
        }
    }
    private static void doGetDrawRectangle() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        PicoP_RenderTargetE target = PicoP_RenderTargetE.eFRAME_BUFFER_0;
        PicoP_Point point = new PicoP_Point();
        point.setPicoP_Point((short)100, (short)100);
        PicoP_RectSize size = new PicoP_RectSize();
        size.setPicoP_RectSize((short)120, (short)130);
        PicoP_Color fillColor = new PicoP_Color();
        fillColor.R = 100;
        fillColor.G = 0;
        fillColor.B = 0;
        fillColor.A = 0;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_DrawRectangle(mPicoPHandler, target, point, size, fillColor);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_DrawRectangle(mPicoPHandler, target, point, size, fillColor);
            }
        }
    }
    // TODO: commit
    private static void doRestoreFactoryConfig() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        boolean bCommit = false;
        if (SERIAL_PORT_OPENED) {
            RC_FOR_GET = ALC_Api.PicoP_ALC_RestoreFactoryConfig(mPicoPHandler, bCommit);
        } else {
            if (doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_RestoreFactoryConfig(mPicoPHandler, bCommit);
            }
        }
    }
    private static void doGetUpgradeSoftware() {
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if (SERIAL_PORT_OPENED) {
            //RC_FOR_GET = ALC_Api.PicoP_ALC_UpgradeSoftware(mPicoPHandler, image, nSize, callback);
        } else {
            if (doInit()) {
                //RC_FOR_GET = ALC_Api.PicoP_ALC_UpgradeSoftware(mPicoPHandler, image, nSize, callback);
            }
        }
    }
    private static void doGetTemperature(){
        RC_FOR_GET = PicoP_RC.eFAILURE;
        if(SERIAL_PORT_OPENED){
            RC_FOR_GET = ALC_Api.PicoP_ALC_GetSystemStatus(mPicoPHandler,systemStatus);
        } else{
            if(doInit()) {
                RC_FOR_GET = ALC_Api.PicoP_ALC_GetSystemStatus(mPicoPHandler, systemStatus);
                Log.i("MessageCenter", "temperature = " + systemStatus.getTemperature());
            }
        }
    }

    private static int PicoP_RC_To_Int(PicoP_RC ret) {
        int value = 0;
        return value;
    }
    public static PicoP_ValueStorageTypeE intToStorageTye(int type){
        switch(type){
            case 0:
                return PicoP_ValueStorageTypeE.eCURRENT_VALUE;
            case 1:
                return  PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP;
            case 2:
                return PicoP_ValueStorageTypeE.eFACTORY_VALUE;
            default:
                return PicoP_ValueStorageTypeE.eFACTORY_VALUE;
        }
    }

    public static PicoP_DirectionE intToDirection(int direction){
        switch (direction){
            case 0:
                return PicoP_DirectionE.eHORIZONTAL;
            case 1:
                return PicoP_DirectionE.eVERTICAL;
            case 2:
                return PicoP_DirectionE.eBOTH;
            default:
                return PicoP_DirectionE.eHORIZONTAL;
        }
    }

    public static PicoP_ColorE intToColor(int color){
        switch(color){
            case 0:
                return PicoP_ColorE.eRED;
            case 1:
                return PicoP_ColorE.eGREEN;
            case 2:
                return PicoP_ColorE.eBLUE;
            case 3:
                return PicoP_ColorE.eALL_COLORS;
            default:
                return PicoP_ColorE.eRED;
        }
    }
    public static PicoP_OutputVideoStateE intToOutputVideoState(int state){
        switch (state){
            case 0:
                return PicoP_OutputVideoStateE.eOUTPUT_VIDEO_DISABLED;
            case 1:
                return PicoP_OutputVideoStateE.eOUTPUT_VIDEO_ENABLED;
            default:
                return PicoP_OutputVideoStateE.eOUTPUT_VIDEO_DISABLED;
        }

    }
    public static PicoP_InputVideoStateE intToInputVideoState(int state){
        switch (state){
            case 0:
                return PicoP_InputVideoStateE.eINPUT_VIDEO_DISABLED;
            case 1:
                return PicoP_InputVideoStateE.eINPUT_VIDEO_ENABLED;
            default:
                return PicoP_InputVideoStateE.eINPUT_VIDEO_DISABLED;
        }

    }
}
