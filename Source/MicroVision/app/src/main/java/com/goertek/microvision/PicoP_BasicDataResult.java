package com.goertek.microvision;

/**
 * Created by louis.yang on 2017/4/28.
 */

public class PicoP_BasicDataResult {
    public static float brightness;
    public static int colormode;
    public static int keyStoneCorrectionValue;
    public static int offset;
    public static int phaseValue;
    public static float gammaval;
    public static int inputstate;
    public static int outputstate;
    public static float frameRate;
    public static short lines;
    public static float distortViewPort_offsetTopLeft;
    public static float distortViewPort_offsetTopRight;
    public static float distortViewPort_offsetLowerLeft;
    public static float distortViewPort_offsetLowerRight;
    public static int aspectRatio;
    public static int flipState;
    public static int coefficient;
    public static int videoMode;
    public static int renderTarget;
    public static int osdState;
    public static int splashScreenTimeout;

    public  PicoP_BasicDataResult(){
        brightness = 0;
        colormode = 0;
        keyStoneCorrectionValue = 0;
        offset = 0;
        phaseValue = 0;
        gammaval = 0;
        inputstate = 0;
        outputstate = 0;
        frameRate = 0;
        lines = 0;
        distortViewPort_offsetTopLeft = 0;
        distortViewPort_offsetTopRight = 0;
        distortViewPort_offsetLowerLeft = 0;
        distortViewPort_offsetLowerRight = 0;
        aspectRatio = 0;
        flipState = 0;
        coefficient = 0;
        videoMode = 3;
        renderTarget = 0;
        splashScreenTimeout = 0;
    }

    public static void setBrightness(float _brightness){
        brightness = _brightness;
    }
    public static void setColormode(int _colormode){ colormode = _colormode; }
    public static void setKeyStoneCorrectionValue(int _keyStoneCorrectionValue) { keyStoneCorrectionValue = _keyStoneCorrectionValue; }
    public static void setOffset(int _offset){ offset = _offset; }
    public static void setPhaseValue(int _phaseValue) { phaseValue = _phaseValue;}
    public static void setGammaval(float _gammaval){ gammaval = _gammaval;}
    public static void setInputstate(int _state){ inputstate = _state; }
    public static void setOutputstate(int _state){ outputstate = _state; }
    public static void setFrameRate(float _frameRate){ frameRate = _frameRate;}
    public static void setLines(int _lines){ lines = (short)_lines;}
    public static void setDistortViewPort_offsetTopLeft(float _distortViewPort_offsetTopLeft){
        distortViewPort_offsetTopLeft = _distortViewPort_offsetTopLeft;
    }
    public static void setDistortViewPort_offsetTopRight(float _distortViewPort_offsetTopRight){
        distortViewPort_offsetTopRight = _distortViewPort_offsetTopRight;
    }
    public static void setDistortViewPort_offsetLowerLeft(float _distortViewPort_offsetLowerLeft){
        distortViewPort_offsetLowerLeft = _distortViewPort_offsetLowerLeft;
    }
    public static void setDistortViewPort_offsetLowerRight(float _distortViewPort_offsetLowerRight){
        distortViewPort_offsetLowerRight = _distortViewPort_offsetLowerRight;
    }
    public static void setAspectRatio(int _aspectRatio){aspectRatio = _aspectRatio;}
    public static int getAspectRatio(){return aspectRatio;}
    public static void setFlipState(int _flipState){flipState = _flipState;}
    public static int getFlipState(){return flipState;}
    public static void setCoefficient(int _coefficient){coefficient = _coefficient;}
    public static int getCoefficient(){return coefficient;}
    public static void setVideoMode(int _videoMode){videoMode = _videoMode;}
    public static int getVideoMode(){return videoMode;}
    public static void setRenderTarget(int _renderTarget){renderTarget = _renderTarget;}
    public static int getRenderTarget(){return renderTarget;}
    public static void setOSDState(int _osdState){osdState = _osdState;}
    public static int getOSDState(){return osdState;}
    public static void setSplashScreenTimeout(int _splashScreenTimeout){splashScreenTimeout = _splashScreenTimeout;}
    public static int getSplashScreenTimeout(){return splashScreenTimeout;}

    public static float getBrightness(){
        return brightness;
    }
    public static int getColormode(){return colormode;}
    public static int getKeyStoneCorrectionValue() {return keyStoneCorrectionValue; }
    public static int getOffset(){ return offset;}
    public static int getPhaseValue() {return phaseValue;}
    public static float getGammaval(){ return gammaval;}
    public static int getInputstate(){ return inputstate; }
    public static int getOutputstate() { return outputstate;}
    public static float getFrameRate() {return frameRate;}
    public static short getLines() {return lines;}
    public static float getDistortViewPort_offsetTopLeft(){return distortViewPort_offsetTopLeft;}
    public static float getDistortViewPort_offsetTopRight(){return distortViewPort_offsetTopRight;}
    public static float getDistortViewPort_offsetLowerLeft(){return distortViewPort_offsetLowerLeft;}
    public static float getDistortViewPort_offsetLowerRight(){return distortViewPort_offsetLowerRight;}
}
