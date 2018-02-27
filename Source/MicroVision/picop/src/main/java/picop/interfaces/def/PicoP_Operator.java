package picop.interfaces.def;

import java.io.IOException;
import java.util.ArrayList;

import static picop.interfaces.def.PicoP_PpcpData.eMCU_DEVICE;
import static picop.interfaces.def.PicoP_PpcpData.ePDE_DEVICE;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_SERIAL_NUMBER_LEN;
import static picop.interfaces.def.PicoP_PpcpUtils.MCU_FW_HEADER;
import static picop.interfaces.def.PicoP_RenderTargetE.eOSD_0;
import static picop.interfaces.def.SerialPortOperator.mSerialReadThread;
import static picop.interfaces.def.UsbPortOperator.mUsbReadThread;

/**
 * Created by carey.wang on 2017/3/30.
 */

public class PicoP_Operator {

    private static final String TAG = "PicoP_Operator ";
    public static final PicoP_PpcpPSEMsg responseMsgmsg = new PicoP_PpcpPSEMsg();
    static PicoP_PpcpHostMsg PpcpHostMsg = new PicoP_PpcpHostMsg();
    public static Thread mReadThreadt;

    /* { Connection port operation start */
    public static PicoP_RC CloseConnection(PicoP_ConnectionTypeE type){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        switch(type){
            case eBTH:
                //ret = CloseBTHSocket();
                break;
            case eUSB:
                try {
                    ret = UsbPortOperator.close();
                } catch (IOException e){
                    ret = PicoP_RC.eFAILURE;
                }
            case eRS232:
                PicoP_Ulog.d(TAG," Try to close Serial Port now.");
                ret = SerialPortOperator.closeSerialPort();
            default:
                break;
        }
        return ret;
    }

    public static PicoP_RC OpenConnection(PicoP_ConnectionTypeE connectionType,
                                          PicoP_ConnectionInfo connectionInfo){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        switch(connectionType){
            case eBTH:
                //ret = OpenBTHSocket();
                break;
            case eUSB:
                PicoP_Ulog.d(TAG," Try to open Usb Port now.");
                ret = UsbPortOperator.open(connectionInfo);
                if(ret == PicoP_RC.eSUCCESS){
                    mReadThreadt = mUsbReadThread;
                }
                break;
            case eRS232:
                PicoP_Ulog.d(TAG," Try to open Serial Port now.");
                ret = SerialPortOperator.open(connectionInfo.PicoP_RS232.getPort(),connectionInfo.PicoP_RS232.getBaudrate(), 0);
                if(ret == PicoP_RC.eSUCCESS){
                    mReadThreadt = mSerialReadThread;
                }
                break;
            default:
                break;
        }
        return ret;
    }

    private static PicoP_RC sendCmds(PicoP_ConnectionTypeE currConnTye, String binaryCMD){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        switch(currConnTye){
            case eBTH:
                break;
            case eUSB:
                ret = UsbPortOperator.sendCmds(binaryCMD)?PicoP_RC.eSUCCESS:PicoP_RC.eFAILURE;
                break;
            case eRS232:
                ret = SerialPortOperator.sendCmds(binaryCMD)?PicoP_RC.eSUCCESS:PicoP_RC.eFAILURE;
                break;
            default:
                break;
        }
        return ret;
    }
    /* Connection port operation end } */

    private static String doCheckSum(String cmd){
        String result ="";
        byte orig[] = cmd.getBytes();
        String tmp ="";
        int starter = 0;
        for(int i=0; i<orig.length;i++){
            tmp = tmp + (orig[i]-48);
            if((i+1)%8 ==0) {
                String hex = binaryString2hexString(tmp);
                if(i==7){
                    result +=hex;
                    tmp = "";
                    continue;
                }
                int a = Integer.parseInt(tmp, 2);
                if(starter < a){
                    starter = starter +256 -a;
                } else {
                    starter = starter - a;
                }
                if(i != (orig.length-1)){
                    result += hex;
                } else{
                    result += hex + Integer.toHexString(starter);
                }
                tmp = "";
            }
        }
        PicoP_Ulog.i(TAG, "at last, checksum is " + result);
        return result;
    }

    public static String binaryString2hexString(String bString)
    {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4)
        {
            iTmp = 0;
            for (int j = 0; j < 4; j++)
            {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    public static PicoP_RC sendCMDS(PicoP_Handle libraryHandle){
        String msgNoneCheckSum = PpcpHostMsg.getPpcpMsgNoneCheckSum();
        /* do checksum */
        String msgWithCheckSum = doCheckSum(msgNoneCheckSum);
        return sendCmds(libraryHandle.connectionInfoEx.getConnectionType(), msgWithCheckSum);
    }

    public static PicoP_RC waitResult(int sequence){
        PicoP_RC ret = PicoP_RC.eTIMEOUT;
        int waitCount = 0;

        while (waitCount++ < 3) {

            PicoP_Ulog.i(TAG, "waitResult... "+waitCount);
            mReadThreadt.run();

            if (responseMsgmsg.getMsg() != null &&
                responseMsgmsg.getMsg().length() > 0) {
                PicoP_Ulog.i(TAG, "seq="+responseMsgmsg.sSeqNum+" exp="+sequence);
                if (responseMsgmsg.sChecksum &&
                    sequence == responseMsgmsg.sSeqNum) {
                    PicoP_Ulog.i(TAG, "Got response");
                    ret = PicoP_RC.eSUCCESS;
                    break;
                } else {
                    PicoP_Ulog.i(TAG, "Bad response - try again");
                    responseMsgmsg.clearMsg();
                }
            } else {
                PicoP_Ulog.i(TAG, "No response");
                ret = PicoP_RC.eTIMEOUT;
                break;
            }
        }
        return ret;
    }

    /* Update sequence number */
    private static void setSequenceNumber(PicoP_Handle libraryHandle, int sequence){
        if(++sequence > PicoP_PpcpData.MAX_SEQNUM){
            sequence = 1;
        }
        libraryHandle.setSequenceNumber(sequence);
        responseMsgmsg.clearMsg();
    }

    /* { PicoP PPCP set function list start */

    public static PicoP_RC setBrightness(PicoP_Handle libraryHandle,
                                         float fBrightnessValue,
                                         boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_FP32(fBrightnessValue);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_BRIGHTNESS_VAL);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getBrightness(PicoP_Handle libraryHandle,
                                         ALC_Api.ALC_Callback callback,
                                         PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_BRIGHTNESS_VAL);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS && callback != null){
            callback.brightnessCallback(responseMsgmsg.GetReply_FP32());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setColorMode(PicoP_Handle libraryHandle,
                                        PicoP_ColorModeE colorMode,
                                        boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_UINT8(colorMode.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_COLOR_MODE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getColorMode(PicoP_Handle libraryHandle,
                                        ALC_Api.ALC_Callback callback,
                                        PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_COLOR_MODE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS && callback != null){
            callback.colorModeCallback(responseMsgmsg.GetReply_UINT8());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC correctKeystone(PicoP_Handle libraryHandle,
                                           int keyStoneCorrectionValue,
                                           boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_INT32(keyStoneCorrectionValue);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_CORRECT_KEYSTONE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getKeyStoneConnection(PicoP_Handle libraryHandle,
                                                 ALC_Api.ALC_Callback callback,
                                                 PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_KEYSTONE_CORRECTION);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.keyStoneConnectionCallback(responseMsgmsg.GetReply_INT32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setColorAlignment(PicoP_Handle libraryHandle,
                                             PicoP_DirectionE direction,
                                             PicoP_ColorE color, short Offset,
                                             boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(4);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_UINT8(color.enumtoInt());
        PpcpHostMsg.AddParam_UINT8(direction.enumtoInt());
        PpcpHostMsg.AddParam_INT16(Offset);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_COLOR_ALIGNMENT);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getColorAlignment(PicoP_Handle libraryHandle,
                                             PicoP_DirectionE direction,
                                             PicoP_ColorE color,
                                             ALC_Api.ALC_Callback callback,
                                             PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(3);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.AddParam_UINT8(color.enumtoInt());
        PpcpHostMsg.AddParam_UINT8(direction.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_COLOR_ALIGNMENT);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS && callback != null){
            callback.colorAligmentCallback(responseMsgmsg.GetReply_INT16());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setPhase(PicoP_Handle libraryHandle,
                                    short sPhaseValue, boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_INT16(sPhaseValue);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_PHASE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getPhase(PicoP_Handle libraryHandle,
                                    ALC_Api.ALC_Callback callback,
                                    PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_PHASE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS && callback != null){
            callback.getPhaseCallback(responseMsgmsg.GetReply_INT16());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setActiveCaptureMode(PicoP_Handle libraryHandle,
                                                PicoP_VideoModeHandleE modeHandle){

        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT32(modeHandle.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_ACTIVE_CAPTURE_MODE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getActiveCaptureMode(PicoP_Handle libraryHandle,
                                                ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_ACTIVE_CAPTURE_MODE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.activeCaptureModeCallback(responseMsgmsg.GetReply_UINT32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setInputCaptureModeInfo(PicoP_Handle libraryHandle,
                                            PicoP_VideoModeHandleE modeHandle,
                                            PicoP_VideoCaptureInfo captureInfo){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(10);
        PpcpHostMsg.AddParam_UINT16(captureInfo.getVideoStartPosition().getX_value());
        PpcpHostMsg.AddParam_UINT16(captureInfo.getVideoStartPosition().getY_value());
        PpcpHostMsg.AddParam_INT32(captureInfo.getHSyncPolarity().enumtoInt());
        PpcpHostMsg.AddParam_INT32(captureInfo.getVSyncPolarity().enumtoInt());
        PpcpHostMsg.AddParam_INT32(captureInfo.getPixelClockEdge().enumtoInt());
        PpcpHostMsg.AddParam_UINT16(captureInfo.getResolution().getWidth());
        PpcpHostMsg.AddParam_UINT16(captureInfo.getResolution().getHeight());
        PpcpHostMsg.AddParam_FP32(captureInfo.getPixelAspectRatio());
        PpcpHostMsg.AddParam_INT32(captureInfo.getColorSpace().enumtoInt());
        PpcpHostMsg.AddParam_INT32(captureInfo.getInterlaceField().enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_INPUT_CAPTURE_MODE_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS){
                modeHandle = modeHandle.inttoEnum(responseMsgmsg.GetReply_UINT32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC modifyInputCaptureModeInfo(PicoP_Handle libraryHandle,
                                            PicoP_VideoModeHandleE modeHandle,
                                            PicoP_VideoCaptureInfo captureInfo){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(11);
        PpcpHostMsg.AddParam_UINT32(modeHandle.enumtoInt());
        PpcpHostMsg.AddParam_UINT16(captureInfo.getVideoStartPosition().getX_value());
        PpcpHostMsg.AddParam_UINT16(captureInfo.getVideoStartPosition().getY_value());
        PpcpHostMsg.AddParam_INT32(captureInfo.getHSyncPolarity().enumtoInt());
        PpcpHostMsg.AddParam_INT32(captureInfo.getVSyncPolarity().enumtoInt());
        PpcpHostMsg.AddParam_INT32(captureInfo.getPixelClockEdge().enumtoInt());
        PpcpHostMsg.AddParam_UINT16(captureInfo.getResolution().getWidth());
        PpcpHostMsg.AddParam_UINT16(captureInfo.getResolution().getHeight());
        PpcpHostMsg.AddParam_FP32(captureInfo.getPixelAspectRatio());
        PpcpHostMsg.AddParam_INT32(captureInfo.getColorSpace().enumtoInt());
        PpcpHostMsg.AddParam_INT32(captureInfo.getInterlaceField().enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_MODIFY_INPUT_CAPTURE_MODE_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getInputCaptureModeInfo(PicoP_Handle libraryHandle,
                                            PicoP_VideoModeHandleE modeHandle,
                                            PicoP_VideoCaptureInfo captureInfo){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT32(modeHandle.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_INPUT_CAPTURE_MODE_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS){
                captureInfo.setVideoStartPosition(responseMsgmsg.GetReply_UINT16(), responseMsgmsg.GetReply_UINT16());
                captureInfo.setHSyncPolarity(responseMsgmsg.GetReply_Enum32());
                captureInfo.setVSyncPolarity(responseMsgmsg.GetReply_Enum32());
                captureInfo.setPixelClockEdge(responseMsgmsg.GetReply_Enum32());
                captureInfo.setResolution(responseMsgmsg.GetReply_UINT16(), responseMsgmsg.GetReply_UINT16());
                captureInfo.setPixelAspectRatio(responseMsgmsg.GetReply_FP32());
                captureInfo.setColorSpace(responseMsgmsg.GetReply_Enum32());
                captureInfo.setInterlaceField(responseMsgmsg.GetReply_Enum32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setGammaval(PicoP_Handle libraryHandle,
                                       PicoP_ColorE color, float gammaValue,
                                       boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(3);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_UINT8(color.enumtoInt());
        PpcpHostMsg.AddParam_FP32(gammaValue);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_GAMMA_VAL);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getGammaval( PicoP_Handle libraryHandle,
                                        PicoP_ColorE color,
                                        ALC_Api.ALC_Callback callback,
                                        PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.AddParam_UINT8(color.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_GAMMA_VAL);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS && callback != null){
            callback.gammavalCallback(responseMsgmsg.GetReply_FP32());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setOutputvideostate(PicoP_Handle libraryHandle,
                                               PicoP_OutputVideoStateE state,
                                               boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_UINT8(state.enumtoInt());

        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_OUTPUT_VIDEO_STATE_EX);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getOutputvideostate(PicoP_Handle libraryHandle,
                                               ALC_Api.ALC_Callback callback,
                                               PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_OUTPUT_VIDEO_STATE_EX);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.outputStateCallback(responseMsgmsg.GetReply_UINT8());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setInputvideostate(PicoP_Handle libraryHandle,
                                              PicoP_InputVideoStateE state){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(state.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_INPUT_VIDEO_STATE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getInputvideostate(PicoP_Handle libraryHandle,
                                              ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_INPUT_VIDEO_STATE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.inputStateCallback(responseMsgmsg.GetReply_UINT8());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getInputvideoproperties(PicoP_Handle libraryHandle,
                                                   ALC_Api.ALC_Callback callback){

        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_INPUT_VIDEO_PROPERTIES);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.inputVideoProCallback(responseMsgmsg.GetReply_FP32(), responseMsgmsg.GetReply_UINT16());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getSystemStatus(PicoP_Handle libraryHandle,
                                           PicoP_SystemStatus systemStatus){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_SYSTEM_STATUS);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            systemStatus.setPicoP_SystemStatus(
                    responseMsgmsg.GetReply_UINT32(),
                    responseMsgmsg.GetReply_UINT32(),
                    responseMsgmsg.GetReply_FP32(),
                    responseMsgmsg.GetReply_UINT32(),
                    responseMsgmsg.GetReply_UINT32(),
                    responseMsgmsg.GetReply_UINT32(),
                    responseMsgmsg.GetReply_UINT32(),
                    responseMsgmsg.GetReply_UINT32());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getSystemInfo(PicoP_Handle libraryHandle,
                                         PicoP_SystemInfo systemInfo){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_SYSTEM_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            systemInfo.setPicoP_SystemInfoValues(
                    responseMsgmsg.GetReply_String(MAX_SERIAL_NUMBER_LEN),
                    responseMsgmsg.GetReply_UINT32(),
                    responseMsgmsg.GetReply_UINT32());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC doFlipImage(PicoP_Handle libraryHandle,
                                       PicoP_DirectionE direction){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(direction.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_FLIP_IMAGE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC saveAsSplash(PicoP_Handle libraryHandle,
                                        PicoP_RenderTargetE target){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SAVE_AS_SPLASH);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getEventLog(PicoP_Handle libraryHandle,
                                       short sNumEvents, PicoP_Event[] pEvent){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT16(sNumEvents);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_EVENT_LOG);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        int nEvents = 0;
        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS){
                nEvents = responseMsgmsg.GetReply_INT16();
                nEvents = (nEvents >= sNumEvents) ? sNumEvents : nEvents;
                for(int i=0; i<nEvents; i++){
                    pEvent[i] = new PicoP_Event();
                    pEvent[i].setPicoP_EventValues((short)responseMsgmsg.GetReply_UINT16(),
                            (short)responseMsgmsg.GetReply_UINT16(),
                            responseMsgmsg.GetReply_UINT32(),
                            responseMsgmsg.GetReply_UINT32(),
                            (short)responseMsgmsg.GetReply_UINT16(),
                            (short)responseMsgmsg.GetReply_UINT16());
                }
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setViewportDistortion(PicoP_Handle libraryHandle,
                                                 float offsetTopLeft,
                                                 float fOffsetTopRight,
                                                 float fOffsetLowerLeft,
                                                 float fOffsetLowerRight,
                                                 boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(5);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_FP32(offsetTopLeft);
        PpcpHostMsg.AddParam_FP32(fOffsetTopRight);
        PpcpHostMsg.AddParam_FP32(fOffsetLowerLeft);
        PpcpHostMsg.AddParam_FP32(fOffsetLowerRight);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_DISTORT_VIEWPORT);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getViewportDistortion(PicoP_Handle libraryHandle,
                                                 ALC_Api.ALC_Callback callback,
                                                 PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_VIEWPORT_DISTORTION);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.distortViewPortCallback(responseMsgmsg.GetReply_FP32(), responseMsgmsg.GetReply_FP32(),
                        responseMsgmsg.GetReply_FP32(), responseMsgmsg.GetReply_FP32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setAspectRatioMode(PicoP_Handle libraryHandle,
                                            PicoP_AspectRatioModeE aspectRatio,
                                            boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_UINT8(aspectRatio.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_ASPECT_RATIO_MODE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getAspectRatioMode(PicoP_Handle libraryHandle,
                                              ALC_Api.ALC_Callback callback,
                                              PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_ASPECT_RATIO_MODE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS && callback != null){
            callback.aspectRatioCallback(responseMsgmsg.GetReply_UINT8());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setFlipState(PicoP_Handle libraryHandle,
                                        PicoP_FlipStateE flipState,
                                        boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT32(flipState.enumtoInt());
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_FLIP_STATE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getFlipState(PicoP_Handle libraryHandle,
                                        ALC_Api.ALC_Callback callback,
                                        PicoP_ValueStorageTypeE storageType) {
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_FLIP_STATE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if (ret == PicoP_RC.eSUCCESS && callback != null) {
            callback.flipStateCallback(responseMsgmsg.GetReply_UINT8());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setColorConverter(PicoP_Handle libraryHandle,
                                             PicoP_ColorConvertE color,
                                             int nCoefficient, boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(3);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_UINT8(color.enumtoInt());
        PpcpHostMsg.AddParam_INT32(nCoefficient);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_COLOR_CONVERTER);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getColorConverter(PicoP_Handle libraryHandle,
                                             ALC_Api.ALC_Callback callback,
                                             PicoP_ColorConvertE color,
                                             PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.AddParam_UINT8(color.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_COLOR_CONVERTER);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if (ret == PicoP_RC.eSUCCESS ) {
            ret = responseMsgmsg.GetReplyStatus();
            if (ret == PicoP_RC.eSUCCESS && callback != null) {
                callback.colorConverterCallback(responseMsgmsg.GetReply_INT32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC commitInputCaptureMode(PicoP_Handle libraryHandle,
                                                  PicoP_VideoModeHandleE modeHandle){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT32(modeHandle.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_COMMIT_CAPTURE_MODE_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getCommitedInputCaptureMode(
                                                PicoP_Handle libraryHandle,
                                                ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_COMMITED_CAPTURE_MODE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.commitedInputCaptureModeCallback(responseMsgmsg.GetReply_UINT32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setActiveOSD(PicoP_Handle libraryHandle,
                                        PicoP_RenderTargetE renderTarget){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(renderTarget.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_ACTIVE_OSD);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getActiveOSD(PicoP_Handle libraryHandle,
                                        ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_ACTIVE_OSD);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.activeOSDCallback(responseMsgmsg.GetReply_UINT8());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setOSDInfo(PicoP_Handle libraryHandle,
                                      PicoP_Point startPoint,
                                      PicoP_RectSize size){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(4);
        PpcpHostMsg.AddParam_UINT16(startPoint.getX_value());
        PpcpHostMsg.AddParam_UINT16(startPoint.getY_value());
        PpcpHostMsg.AddParam_UINT16(size.getWidth());
        PpcpHostMsg.AddParam_UINT16(size.getHeight());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_OSD_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getOSDInfo(PicoP_Handle libraryHandle,
                                      PicoP_Point startPoint,
                                      PicoP_RectSize size){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_OSD_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS){
                startPoint.setPicoP_Point((short) responseMsgmsg.GetReply_UINT16(), (short)responseMsgmsg.GetReply_UINT16());
                size.setPicoP_RectSize((short) responseMsgmsg.GetReply_UINT16(), (short)responseMsgmsg.GetReply_UINT16());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setOSDState(PicoP_Handle libraryHandle,
                                       PicoP_OSDStateE state){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT16(state.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_OSD_STATE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getOSDState(PicoP_Handle libraryHandle,
                                       ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_OSD_STATE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.OSDStateCallback(responseMsgmsg.GetReply_UINT8());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC setSplashScreenTimeout(PicoP_Handle libraryHandle,
                                                  int nTimeout, boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(2);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.AddParam_UINT32(nTimeout);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_SET_SPLASH_SCREEN_TIMEOUT);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getSplashScreenTimeout(PicoP_Handle libraryHandle,
                                                  ALC_Api.ALC_Callback callback,
                                                  PicoP_ValueStorageTypeE storageType){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(storageType.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_SPLASH_SCREEN_TIMEOUT);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if (ret == PicoP_RC.eSUCCESS) {
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS && callback != null){
                callback.splashScreenTimeoutCallback(responseMsgmsg.GetReply_UINT32());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getDisplayInfo(PicoP_Handle libraryHandle,
                                          PicoP_RenderTargetE renderTarget,
                                          PicoP_RectSize size){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(renderTarget.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_DISPLAY_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS){
                size.setPicoP_RectSize((short) responseMsgmsg.GetReply_UINT16(), (short)responseMsgmsg.GetReply_UINT16());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC getTextBoxInfo(PicoP_Handle libraryHandle,
                                          byte[] text, short sLength,
                                          PicoP_RectSize textBoxRegion){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        int len = text.length;
        len = (len < sLength)?len:sLength;
        len = (len < PicoP_PpcpUtils.MAX_DRAW_TEXT_LEN)?len:PicoP_PpcpUtils.MAX_DRAW_TEXT_LEN;
        PpcpHostMsg.DECLARE_ALC_PARAMS(1+len);
        //PpcpHostMsg.AddParam_UINT8(PicoP_PpcpData.eHCM_GET_TEXT_BOX_INFO);
        PpcpHostMsg.AddParam_UINT16(len);
        for(int i=0; i<len; i++){
            PpcpHostMsg.AddParam_UINT8(text[i]);
        }
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_GET_TEXT_BOX_INFO);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
            if(ret == PicoP_RC.eSUCCESS){
                textBoxRegion.setPicoP_RectSize((short) responseMsgmsg.GetReply_UINT16(), (short)responseMsgmsg.GetReply_UINT16());
            }
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC clearTarget(PicoP_Handle libraryHandle,
                                       PicoP_RenderTargetE renderTarget){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(renderTarget.enumtoInt());
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_CLEAR_TARGET);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC loadBitmapImage(PicoP_Handle libraryHandle,
                                           PicoP_RenderTargetE target,
                                           PicoP_Point startPoint,
                                           PicoP_RectSize dimensions,
                                           byte[] image, int size){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        ret = AlcSendImageCommandGeneric(libraryHandle, PicoP_PpcpData.eHCM_LOAD_BITMAP_IMAGE,
                image, 0, size, target, startPoint, dimensions, 0, null);
        return ret;
    }

    public static PicoP_RC render(PicoP_Handle libraryHandle){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_RENDER);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC drawTestPattern(PicoP_Handle libraryHandle,
                                           PicoP_RenderTargetE target,
                                           PicoP_Point startPoint,
                                           PicoP_RectSize dimensions,
                                           PicoP_Color patternColor,
                                           PicoP_Color backgroundColor,
                                           PicoP_TestPatternInfoE pattern){
        int patt = pattern.enumtoInt();
        PicoP_Ulog.i(TAG,"drawTestPattern["+patt+"] t("+target.enumtoInt()+") p("+startPoint.getX_value()+","+startPoint.getY_value()+") d("+dimensions.getWidth()+","+dimensions.getHeight()+") f("+patternColor.R+","+patternColor.G+","+patternColor.B+") b("+backgroundColor.R+","+backgroundColor.G+","+backgroundColor.B+")");

        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(14);
        PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
        PpcpHostMsg.AddParam_UINT16(startPoint.getX_value());
        PpcpHostMsg.AddParam_UINT16(startPoint.getY_value());
        PpcpHostMsg.AddParam_UINT16(dimensions.getWidth());
        PpcpHostMsg.AddParam_UINT16(dimensions.getHeight());
        PpcpHostMsg.AddParam_UINT8(patternColor.R);
        PpcpHostMsg.AddParam_UINT8(patternColor.G);
        PpcpHostMsg.AddParam_UINT8(patternColor.B);
        PpcpHostMsg.AddParam_UINT8(patternColor.A);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.R);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.G);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.B);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.A);
        PpcpHostMsg.AddParam_UINT8(patt);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_DRAW_TEST_PATTERN);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC drawText(PicoP_Handle libraryHandle,
                                    PicoP_RenderTargetE target,
                                    byte[] text, int length,
                                    PicoP_Point startPoint,
                                    PicoP_Color textColor,
                                    PicoP_Color backgroundColor){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        int len = text.length;
        len = (len < length)?len:length;
        len = (len < PicoP_PpcpUtils.MAX_DRAW_TEXT_LEN)?len:PicoP_PpcpUtils.MAX_DRAW_TEXT_LEN;
        PpcpHostMsg.DECLARE_ALC_PARAMS(1+len);
        //PpcpHostMsg.AddParam_UINT8(PicoP_PpcpData.eHCM_DRAW_TEXT);
        PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
        PpcpHostMsg.AddParam_UINT16(len);
        PpcpHostMsg.AddParam_UINT16(startPoint.getX_value());
        PpcpHostMsg.AddParam_UINT16(startPoint.getY_value());
        PpcpHostMsg.AddParam_UINT8(textColor.R);
        PpcpHostMsg.AddParam_UINT8(textColor.G);
        PpcpHostMsg.AddParam_UINT8(textColor.B);
        PpcpHostMsg.AddParam_UINT8(textColor.A);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.R);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.G);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.B);
        PpcpHostMsg.AddParam_UINT8(backgroundColor.A);
        for(int i=0; i<len; i++){
            PpcpHostMsg.AddParam_UINT8(text[i]);
        }
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_DRAW_TEXT);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC drawPoint(PicoP_Handle libraryHandle,
                                     PicoP_RenderTargetE target,
                                     PicoP_Point pixel, PicoP_Color color){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(7);
        PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
        PpcpHostMsg.AddParam_UINT16(pixel.getX_value());
        PpcpHostMsg.AddParam_UINT16(pixel.getY_value());
        PpcpHostMsg.AddParam_UINT8(color.R);
        PpcpHostMsg.AddParam_UINT8(color.G);
        PpcpHostMsg.AddParam_UINT8(color.B);
        PpcpHostMsg.AddParam_UINT8(color.A);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_DRAW_POINT);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC drawLine(PicoP_Handle libraryHandle,
                                    PicoP_RenderTargetE target,
                                    PicoP_Point pointA, PicoP_Point pointB,
                                    PicoP_Color color){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(9);
        PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
        PpcpHostMsg.AddParam_UINT16(pointA.getX_value());
        PpcpHostMsg.AddParam_UINT16(pointA.getY_value());
        PpcpHostMsg.AddParam_UINT16(pointB.getX_value());
        PpcpHostMsg.AddParam_UINT16(pointB.getY_value());
        PpcpHostMsg.AddParam_UINT8(color.R);
        PpcpHostMsg.AddParam_UINT8(color.G);
        PpcpHostMsg.AddParam_UINT8(color.B);
        PpcpHostMsg.AddParam_UINT8(color.A);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_DRAW_LINE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC drawTriangle(PicoP_Handle libraryHandle,
                                        PicoP_RenderTargetE target,
                                        PicoP_Point pointA, PicoP_Point pointB,
                                        PicoP_Point pointC,
                                        PicoP_Color fillColor){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(11);
        PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
        PpcpHostMsg.AddParam_UINT16(pointA.getX_value());
        PpcpHostMsg.AddParam_UINT16(pointA.getY_value());
        PpcpHostMsg.AddParam_UINT16(pointB.getX_value());
        PpcpHostMsg.AddParam_UINT16(pointB.getY_value());
        PpcpHostMsg.AddParam_UINT16(pointC.getX_value());
        PpcpHostMsg.AddParam_UINT16(pointC.getY_value());
        PpcpHostMsg.AddParam_UINT8(fillColor.R);
        PpcpHostMsg.AddParam_UINT8(fillColor.G);
        PpcpHostMsg.AddParam_UINT8(fillColor.B);
        PpcpHostMsg.AddParam_UINT8(fillColor.A);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_DRAW_TRIANGLE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC drawRectangle(PicoP_Handle libraryHandle,
                                         PicoP_RenderTargetE target,
                                         PicoP_Point startPoint,
                                         PicoP_RectSize size,
                                         PicoP_Color fillColor){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(9);
        PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
        PpcpHostMsg.AddParam_UINT16(startPoint.getX_value());
        PpcpHostMsg.AddParam_UINT16(startPoint.getY_value());
        PpcpHostMsg.AddParam_UINT16(size.getWidth());
        PpcpHostMsg.AddParam_UINT16(size.getHeight());
        PpcpHostMsg.AddParam_UINT8(fillColor.R);
        PpcpHostMsg.AddParam_UINT8(fillColor.G);
        PpcpHostMsg.AddParam_UINT8(fillColor.B);
        PpcpHostMsg.AddParam_UINT8(fillColor.A);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_DRAW_RECTANGLE);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        if(ret == PicoP_RC.eSUCCESS){
            ret = responseMsgmsg.GetReplyStatus();
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC restoreFactoryConfig(PicoP_Handle libraryHandle,
                                                boolean bCommit){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(bCommit);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_RESTORE_FACTORY_CONFIG);

        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());
        return ret;
    }

    public static PicoP_RC upgradeSoftware(PicoP_Handle libraryHandle,
                                           int commandId, byte[] image,
                                           int nSize, ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        PicoP_Point dummyPoint = new PicoP_Point();
        PicoP_RectSize dummySie = new PicoP_RectSize();

        if(commandId == PicoP_PpcpData.eHCM_FPGA_UPGRADE){
            ret = AlcSendImageCommandGeneric(libraryHandle, commandId, image, 0, nSize, eOSD_0, dummyPoint, dummySie, ePDE_DEVICE, callback);
        } else {
            ret = AlcPicoPUpgradeProc(libraryHandle, image, nSize, callback);
        }

        return ret;
    }

    static int curIndexFlag;
    static int maxIndexFlag;
    static int UpdateType;

    public static PicoP_RC AlcSendImageCommandGeneric(PicoP_Handle libraryHandle,
                                                    int commandId, byte[]image,
                                                    int imgIndex, int size,
                                                    PicoP_RenderTargetE target,
                                                    PicoP_Point startPoint,
                                                    PicoP_RectSize dimensions,
                                                    int destID,
                                                    ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        int u32SentImgSize = 0;
        int u16PayloadImgSize = 0;
        int curIndex = 0;
        int retryCount = 10;

        int maxImgSize = (PicoP_PpcpData.eHCM_LOAD_BITMAP_IMAGE == commandId) ? PicoP_PpcpUtils.MAX_BITMAP_PAYLOAD_SIZE : PicoP_PpcpUtils.MAX_UPGRADE_PAYLOAD_SIZE;
        int maxIndex = ((size - 1) / maxImgSize) - 1;

        if(((size - 1) % maxImgSize) != 0){
            maxIndex++;
        }

        maxIndex++;
        maxIndexFlag = maxIndex;

        while(size > 0 && ret == PicoP_RC.eSUCCESS){
            u16PayloadImgSize = (0 == curIndex) ? 4 : ((size > maxImgSize) ? maxImgSize : size);
            size -= u16PayloadImgSize;

            PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());

            if (PicoP_PpcpData.eHCM_LOAD_BITMAP_IMAGE == commandId)
            {
                PpcpHostMsg.DECLARE_ALC_PARAMS(5 + 3 +u16PayloadImgSize);
                PpcpHostMsg.AddParam_UINT8(target.enumtoInt());
                PpcpHostMsg.AddParam_UINT16(startPoint.getX_value());
                PpcpHostMsg.AddParam_UINT16(startPoint.getY_value());
                PpcpHostMsg.AddParam_UINT16(dimensions.getWidth());
                PpcpHostMsg.AddParam_UINT16(dimensions.getHeight());
            } else {
                PpcpHostMsg.DECLARE_ALC_PARAMS(3 + u16PayloadImgSize);
            }

            PpcpHostMsg.AddParam_UINT16(curIndex);
            PpcpHostMsg.AddParam_UINT16(maxIndex);
            PpcpHostMsg.AddParam_UINT16(u16PayloadImgSize);

            for(int i=0; i<u16PayloadImgSize; i++){
                PpcpHostMsg.AddParam_UINT8(image[i+imgIndex]);
            }
            PpcpHostMsg.ALC_SEND_CMD_PARAMS(commandId);

            int retry = 0;
            do{
                ret = sendCMDS(libraryHandle);

                if(ret != PicoP_RC.eSUCCESS){
                    PicoP_Ulog.d(TAG," Error sending command.");
                    return ret;
                }

                if (PicoP_PpcpData.eHCM_LOAD_BITMAP_IMAGE != commandId &&
                        curIndex == maxIndex && destID == ePDE_DEVICE)
                {
                    try {
                        Thread.sleep(20000);  // wait 20 seconds for the PicoP to finish upgrading
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                ret = waitResult(libraryHandle.getSequenceNumber());

                /* at last, increase the sequence number */
                setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());

                if(ret == PicoP_RC.eSUCCESS){
                    break;
                } else if(ret == PicoP_RC.eINCOMPLETE){
                    try {
                        Thread.sleep(300);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                retry++;
            }while (retry < retryCount);

            u32SentImgSize += u16PayloadImgSize;
            curIndex++;

            curIndexFlag = curIndex;

            if(callback != null){
                callback.upgradeSoftwareCallback(curIndexFlag, maxIndexFlag, destID);
            }
        }

        return ret;
    }

    public static PicoP_RC AlcPicoPUpgradeProc(PicoP_Handle libraryHandle,
                                               byte[] image, int size,
                                               ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        boolean bUpdateOver = false;
        int u32FileSz = 0;
        int u32RemSz = size;
        int eDestID = 0;
        int imgIndex = 0;
        ArrayList<Byte> imageList = new ArrayList<Byte>();
        for(int i=0; i<size; i++){
            imageList.add(image[i]);
        }
        imageList.toArray();

        do{
            if(size < 5){
                ret = PicoP_RC.eINVALID_ARG;
                bUpdateOver = true;
            } else {
                if ((eMCU_DEVICE != image[imgIndex]) && (ePDE_DEVICE != image[imgIndex])){
                    ret = PicoP_RC.eINVALID_ARG;
                    bUpdateOver = true;
                } else {
                    byte[] subImg = new byte[4];
                    System.arraycopy(image, imgIndex+1, subImg, 0, 4);
                    u32FileSz = PicoP_PpcpHostMsg.bytesToInt(subImg);
                    u32FileSz += 5;
                    u32RemSz -= u32FileSz;

                    if (u32RemSz > 5) {
                        // Update is not over.
                        bUpdateOver = false;
                    } else {
                        bUpdateOver = true;
                    }
                    u32FileSz -= 5;

                    if (eMCU_DEVICE == image[imgIndex]){
                        eDestID = eMCU_DEVICE;
                        int temp=0, temp1=0xFFFFFFFF;

                        imgIndex += 5;

                        byte[] temp2 = new byte[4];
                        System.arraycopy(image, imgIndex, temp2, 0, 4);
                        int tempIndex = 0;
                        int tempVal = PicoP_PpcpHostMsg.bytesToInt(temp2);
                        if(tempVal == MCU_FW_HEADER){
                            tempIndex += 4;
                            System.arraycopy(image, imgIndex+tempIndex, temp2, 0, 4);
                            tempVal = PicoP_PpcpHostMsg.bytesToInt(temp2);
                            for(temp=1; temp<((u32FileSz-4)/4);temp++){
                                System.arraycopy(image, imgIndex+tempIndex+temp*4, temp2, 0, 4);
                                tempVal = PicoP_PpcpHostMsg.bytesToInt(temp2);
                                temp1 = temp1 + tempVal;
                            }
                        }
                        if(temp1 != 0){
                            ret = PicoP_RC.eINVALID_ARG;
                            break;
                        }

                        UpdateType = 1;
                        ret = AlcPicoPUpgradeMCU(libraryHandle, image, imgIndex, u32FileSz, callback);
                        imgIndex += u32FileSz;
                    } else if (ePDE_DEVICE == image[imgIndex]){
                        eDestID = ePDE_DEVICE;
                        PicoP_Point dummyPoint = new PicoP_Point();
                        PicoP_RectSize dummySize = new PicoP_RectSize();

                        imgIndex += 5;
                        UpdateType = 2;
                        ret = AlcSendImageCommandGeneric(libraryHandle, PicoP_PpcpData.eHCM_SW_UPGRADE,
                                image, imgIndex, u32FileSz, eOSD_0,
                                dummyPoint, dummySize,
                                eDestID, callback);
                        imgIndex += u32FileSz;
                    }

                    if(ret == PicoP_RC.eCONNECT_FAILED){
                        imgIndex += u32FileSz;
                    }
                    curIndexFlag = 0;
                    maxIndexFlag = 0;
                    if(callback != null){
                        callback.upgradeSoftwareCallback(curIndexFlag, maxIndexFlag, eDestID);
                    }
                }
            }
        }while (!bUpdateOver);
        return ret;
    }

    public static PicoP_RC AlcPicoPUpgradeMCU(PicoP_Handle libraryHandle,
                                              byte[] image, int imgIndex,
                                              int size, ALC_Api.ALC_Callback callback){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
        PpcpHostMsg.DECLARE_ALC_PARAMS(1);
        PpcpHostMsg.AddParam_UINT8(PicoP_PpcpData.eHCM_EXTENDED_MCU_COMMAND_SET_ID);
        PpcpHostMsg.setPpcpMsgDestinationId(eMCU_DEVICE);
        PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_MCU_UPGRADE);
        ret = sendCMDS(libraryHandle);

        if(ret == PicoP_RC.eSUCCESS){
            ret = waitResult(libraryHandle.getSequenceNumber());
        }

        /* at last, increase the sequence number */
        setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());

        //closes the USB Port
        //MvCloseUsbPort();

        // wait for the Aplication to switch to bootloader and bring up the USB
        //MvSleep(5000);

        // Reopen connection and library once MCU boots up
        //ret = MvOpenUsbPort();

        if(ret != PicoP_RC.eSUCCESS){
            ret = PicoP_RC.eRECONNECT_FAILED;
        } else if(ret == PicoP_RC.eSUCCESS){
            PpcpHostMsg.setPpcpMsgFlagAndSequence(0, libraryHandle.getSequenceNumber());
            PpcpHostMsg.DECLARE_ALC_PARAMS(1);
            PpcpHostMsg.AddParam_UINT8(PicoP_PpcpData.eHCM_EXTENDED_MCU_COMMAND_SET_ID);
            PpcpHostMsg.setPpcpMsgDestinationId(eMCU_DEVICE);
            PpcpHostMsg.ALC_SEND_CMD_PARAMS(PicoP_PpcpData.eHCM_MCU_UPGRADE_RDY);
            ret = sendCMDS(libraryHandle);

            if(ret == PicoP_RC.eSUCCESS){
                ret = waitResult(libraryHandle.getSequenceNumber());
            }

            /* at last, increase the sequence number */
            setSequenceNumber(libraryHandle, libraryHandle.getSequenceNumber());

            if(ret == PicoP_RC.eSUCCESS){
                PicoP_Point dummyPoint = new PicoP_Point();
                PicoP_RectSize dummySize = new PicoP_RectSize();

                ret = AlcSendImageCommandGeneric(libraryHandle, PicoP_PpcpData.eHCM_SW_UPGRADE,
                        image, imgIndex, size, eOSD_0,
                        dummyPoint, dummySize,
                        eMCU_DEVICE, callback);

                if(ret == PicoP_RC.eSUCCESS){
                    PicoP_Ulog.d(TAG," Upgrade MCU Software Success.");
                } else {
                    PicoP_Ulog.d(TAG," Upgrade MCU Software Failed.");
                }
            } else {
                PicoP_Ulog.d(TAG," Failed to Load Bootloader.");
                ret = PicoP_RC.eRECONNECT_FAILED;
            }
        }
        return ret;
    }
}
