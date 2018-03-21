package picop.interfaces.def;

import java.util.ArrayList;


/**
 * Created on 2017/3/30.
 */

public class PicoP_PpcpHostMsg {
    private static final String TAG = "PicoP_PpcpHostMsg ";

// ************************** Classic PPCP Packet Structure ***************************
//
//  +---------+--------+-------------+-----------+------------+--------------+-------------+
//  | Dest(8) | Src(8) | Priority(2) : protID(2) : Length(12) | Payload(n*8) | Checksum(8) |
//  +---------+--------+-------------+-----------+------------+--------------+-------------+
//
// ****************************************************************************

    String PICOP_PPCPMSG_START_CODE;                                                                // length=2(*4);
    String PICOP_PPCPMSG_DESTINATION_ID;                                                            // length=2(*4);
    String PICOP_PPCPMSG_SOURCE_ID;                                                                 // length=2(*4);
    String PICOP_PPCPMSG_PRIORITY_PROTOCOLID_PAYLOAD_LEN;                                           // length=4(*4); priority + protocol id + payload len

    String PICOP_PPCPMSG_PAYLOAD;                                                                   // length=2n(*4)
    String PICOP_PPCPMSG_PRIORITY_PROTOCOLID;                                                       // length=1(*4)
    String PICOP_PPCPMSG_DESTINATION_FID;                                                           // length=2(*4)
    String PICOP_PPCPMSG_SOURCE_FID;                                                                // length=2(*4)
    String PICOP_PPCPMSG_FLAG_SEQUENCE;                                                             // length=2(*4)  flag[7:6], sequence number [5:0]
    String PICOP_PPCPMSG_COMMAND;                                                                   // length=2(*4)
    String PICOP_PPCMMSG_PAYLOAD_DATA;                                                              // length=2(n-4)(*4) payload data
    public static ArrayList<String> payloadList = new ArrayList<String>();                          // length=2(n-4)(*4) payload data

    String PICOP_PPCPMSG_CHECKSUM;                                                                  // length=2(*4)
    public static int PICOP_PPCPMSG_PARAMS      = 0;                                                // params count

    public void init(){
        /* Package Header
         * ************************** Classic PPCP Packet Current Part Structure ***************************
         *  +---------+---------+--------+
         *  | Head(8) | Dest(8) | Src(8) |
         *  +---------+---------+--------+
         *  For all the messages, it shouled be c0 00 02
         *
         * */
        PICOP_PPCPMSG_START_CODE = PicoP_PpcpData.getStartCodeByString(PicoP_PpcpData.PICOP_PPCP_MSG_START_CODE, 8);
        PICOP_PPCPMSG_DESTINATION_ID = PicoP_PpcpData.getDeviceIdByString(PicoP_PpcpData.ePDE_DEVICE, 8);
        PICOP_PPCPMSG_SOURCE_ID = PicoP_PpcpData.getSourceIdByString(PicoP_PpcpData.eHOST_DEVICE, 8);

        PICOP_PPCPMSG_DESTINATION_FID = PicoP_PpcpData.getFunctionIdByString(PicoP_PpcpData.eHOST_COMM_MANAGER_FID);
        PICOP_PPCPMSG_SOURCE_FID = PicoP_PpcpData.getFunctionIdByString(PicoP_PpcpData.eHOST_COMM_MANAGER_FID);
        PICOP_PPCPMSG_PRIORITY_PROTOCOLID = "0001";
        PICOP_PPCPMSG_PAYLOAD = "";
        PICOP_PPCPMSG_PARAMS = 0;
        payloadList.clear();

        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_START_CODE = " + PICOP_PPCPMSG_START_CODE);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_DESTINATION_ID = " + PICOP_PPCPMSG_DESTINATION_ID);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_SOURCE_ID = " + PICOP_PPCPMSG_SOURCE_ID);

        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_PAYLOAD = " + PICOP_PPCPMSG_PAYLOAD);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_DESTINATION_FID = " + PICOP_PPCPMSG_DESTINATION_FID);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_SOURCE_FID = " + PICOP_PPCPMSG_SOURCE_FID);

    }

    public void setPpcpMsgDestinationId(int destID){
        PICOP_PPCPMSG_DESTINATION_ID = PicoP_PpcpData.getDeviceIdByString(destID, 8);
    }

    /*
    *   public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    *   src -- the source array.
    *   srcPos -- the start position in source array
    *   dest -- the destination array
    *   destPos -- the start position in destination array.
    *   length -- the length to copy.
    * */

    /* prepare the message payload data */
    public void setPpcpMsgFlagAndSequence(int flag, int sequence){
        init();
        String sflag = Integer.toBinaryString(flag);
        while(sflag.length()<2){
            sflag = "0" + sflag;
        }
        String sSequence = Integer.toBinaryString(sequence);
        while(sSequence.length() < 6){
            sSequence = "0"+sSequence;
        }
        PICOP_PPCPMSG_FLAG_SEQUENCE = sflag + sSequence;
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_FLAG_SEQUENCE = " + PICOP_PPCPMSG_FLAG_SEQUENCE);
    }

    public void setPpcpMsgPriorityProtocolPayloadLen(String priority_protocolid_payloadlen){
        PICOP_PPCPMSG_PRIORITY_PROTOCOLID_PAYLOAD_LEN = priority_protocolid_payloadlen;
    }

    public void updatePayload(){
        PICOP_PPCPMSG_PAYLOAD = PICOP_PPCPMSG_DESTINATION_FID + PICOP_PPCPMSG_SOURCE_FID + PICOP_PPCPMSG_FLAG_SEQUENCE
                + PICOP_PPCPMSG_COMMAND;
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_DESTINATION_FID = " + PICOP_PPCPMSG_DESTINATION_FID);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_SOURCE_FID = " + PICOP_PPCPMSG_SOURCE_FID);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_FLAG_SEQUENCE = " + PICOP_PPCPMSG_FLAG_SEQUENCE);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_COMMAND = " + PICOP_PPCPMSG_COMMAND);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"=> PICOP_PPCPMSG_PAYLOAD = " + PICOP_PPCPMSG_PAYLOAD);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_PARAMS = " + PICOP_PPCPMSG_PARAMS);

        for(int i = 0; i< PICOP_PPCPMSG_PARAMS ; i++){
            PICOP_PPCPMSG_PAYLOAD = PICOP_PPCPMSG_PAYLOAD + payloadList.get(i);
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"  PICOP_PPCPMSG_PAYLOAD[" + i + "] = " + PICOP_PPCPMSG_PAYLOAD);
        }

        int payload_length = (PICOP_PPCPMSG_PAYLOAD.length()/PicoP_PpcpUtils.DATA_LENGTH_BINARY_STRING);
        String sLength = Integer.toBinaryString(payload_length);
        while(sLength.length()<12){
            sLength = "0"+sLength;
        }

        PICOP_PPCPMSG_PRIORITY_PROTOCOLID_PAYLOAD_LEN = PICOP_PPCPMSG_PRIORITY_PROTOCOLID + sLength;
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_PRIORITY_PROTOCOLID_PAYLOAD_LEN = " + PICOP_PPCPMSG_PRIORITY_PROTOCOLID_PAYLOAD_LEN);
    }

    public String getPpcpMsgNoneCheckSum(){
        return PICOP_PPCPMSG_START_CODE + PICOP_PPCPMSG_DESTINATION_ID + PICOP_PPCPMSG_SOURCE_ID
                + PICOP_PPCPMSG_PRIORITY_PROTOCOLID_PAYLOAD_LEN + PICOP_PPCPMSG_PAYLOAD;
    }

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
        PicoP_Ulog.v(TAG, "at last, checksum is " + result);
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

    public void setPpcpMsgPayload(String payload){
        PICOP_PPCPMSG_PAYLOAD = payload;
    }

    public String getINT8StringByBool(boolean b){
        String ret = "";
        if(b){
            ret = "1";
        }else{
            ret = "0";
        }
        ret = String.format("%8s",ret);
        ret = ret.replaceAll(" ", "0");
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + ret);
        return  ret;
    }

    public String getINT16StringByBool(boolean b){
        String ret = "";
        if(b){
            ret = "1";
        }else{
            ret = "0";
        }
        ret = String.format("%8s",ret);
        ret = ret.replaceAll(" ", "0");
        while(ret.length()<16){
            ret = ret +"0";
        }
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + ret);
        return  ret;
    }

    public String getINT32StringByBool(boolean b){
        String ret = "";
        if(b){
            ret = "1";
        }else{
            ret = "0";
        }
        ret = String.format("%8s",ret);
        ret = ret.replaceAll(" ", "0");
        while(ret.length()<32){
            ret = ret +"0";
        }
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + ret);
        return  ret;
    }

    public String getINT8StringByInt(int i){
        String ret = Integer.toBinaryString(i);
        ret = String.format("%8s",ret);
        ret = ret.replaceAll(" ", "0");
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + ret);
        return ret;
    }
    public String getINT8StringByByte(byte text){
        String ret = Integer.toBinaryString(text & 0xFF);
        ret = String.format("%8s",ret);
        ret = ret.replaceAll(" ", "0");
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + ret);
        return ret;
    }
    public String getINT16StringByInt(int i){
        String ret = Integer.toBinaryString(i);
        ret = String.format("%16s",ret);
        ret = ret.replaceAll(" ", "0");
        String tmp1 = "";
        String tmp2 = "";
        if(i>=0) {
            tmp1 = ret.substring(8, 16);
            tmp2 = ret.substring(0, 8);
        }else{
            tmp1 = ret.substring(24);
            tmp2 = ret.substring(16,24);
        }
        ret = "" + tmp1 + tmp2;
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + ret);
        return ret;
    }

    public String getINT32StringByInt(int i){
        String ret = Integer.toBinaryString(i);
        ret = String.format("%32s",ret);
        ret = ret.replaceAll(" ", "0");
        String tmp1 = ret.substring(0,8);
        String tmp2 = ret.substring(8,16);
        String tmp3 = ret.substring(16,24);
        String tmp4 = ret.substring(24);
        ret = "" + tmp4 + tmp3 + tmp2 + tmp1;
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + ret);
        return ret;
    }


    private static String floatToIEEE754(float BrightnessValue){
        String binValue = "";
        String sBrightness =Integer.toBinaryString(Float.floatToRawIntBits(BrightnessValue));
        while(sBrightness.length()<32){
            sBrightness = "0" + sBrightness;
        }
        byte Big_Endian[] = sBrightness.getBytes();
        byte Little_Endian[] = bigToLittle(Big_Endian);

        for (int i = 0 ; i<Little_Endian.length; i++ ){
            binValue = binValue + (Little_Endian[i] - 48);
        }
        PicoP_Ulog.i(TAG,PicoP_Ulog._FUNC_()+" " + binValue);
        return binValue;
    }
    private static byte[] bigToLittle(byte[] in){
        byte out[] = new byte[in.length];
        for(int i = 0; i < in.length ; i++){
            if(i>=0 && i<8){
                out[i] = in[24+i];
            }else if(i>=8 && i<16){
                out[i] = in[8+i];
            }else if(i>=16&& i<24){
                out[i] = in[i-8];
            }else if(i>=24&& i<32){
                out[i] = in[i-24];
            }
        }
        return out;
    }
    public void DECLARE_ALC_PARAMS(int _param){
        PICOP_PPCPMSG_PARAMS = _param;
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_PARAMS = " + PICOP_PPCPMSG_PARAMS);
    }

    public void AddParam_UINT8(Object param){
        if(param instanceof Boolean){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a boolean ");
            boolean b = ((Boolean) param).booleanValue();
            payloadList.add(getINT8StringByBool(b));
        } else if(param instanceof Integer){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a int ");
            int value = ((Integer) param).intValue();
            payloadList.add(getINT8StringByInt(value));
        } else if (param instanceof Byte){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a byte ");
            byte text = ((Byte) param).byteValue();
            payloadList.add(getINT8StringByByte(text));
        }else {
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a what? ");
        }
    }

    public void AddParam_UINT16(Object param){
        if(param instanceof Boolean){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a boolean ");
            boolean b = ((Boolean) param).booleanValue();
            payloadList.add(getINT16StringByBool(b));
        } else if(param instanceof Integer){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a int ");
            int value = ((Integer) param).intValue();
            payloadList.add(getINT16StringByInt(value));
        } else if(param instanceof Short){
            short value = ((Short) param).shortValue();
            payloadList.add(getINT16StringByInt(value));
        } else{
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a what? ");
        }
    }

    public void AddParam_UINT32(Object param){
        if(param instanceof Boolean){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a boolean ");
            boolean b = ((Boolean) param).booleanValue();
            payloadList.add(getINT32StringByBool(b));
        } else if(param instanceof Integer){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a int ");
            int value = ((Integer) param).intValue();
            payloadList.add(getINT32StringByInt(value));
        } else {
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a what? ");
        }
    }

    public void AddParam_INT8(Object param){
        if(param instanceof Boolean){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a boolean ");
            boolean b = ((Boolean) param).booleanValue();
            payloadList.add(getINT8StringByBool(b));
        } else if(param instanceof Integer){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a int ");
            int value = ((Integer) param).intValue();
            payloadList.add(getINT8StringByInt(value));
        } else {
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a what? ");
        }
    }

    public void AddParam_INT16(Object param){
        if(param instanceof Boolean){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a boolean ");
            boolean b = ((Boolean) param).booleanValue();
            payloadList.add(getINT16StringByBool(b));
        } else if(param instanceof Integer){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a int ");
            int value = ((Integer) param).intValue();
            payloadList.add(getINT16StringByInt(value));
        } else if(param instanceof Short){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a short ");
            short value = ((Short) param).shortValue();
            payloadList.add(getINT16StringByInt(value));
        } else{
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a what? ");
        }
    }
    public void AddParam_INT32(Object param){
        if(param instanceof Boolean){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a boolean ");
            boolean b = ((Boolean) param).booleanValue();
            payloadList.add(getINT32StringByBool(b));
        } else if(param instanceof Integer){
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a int ");
            int value = ((Integer) param).intValue();
            payloadList.add(getINT32StringByInt(value));
        } else {
            PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+" is a what? ");
        }
    }

    public void AddParam_FP32(Object param){
        if(param instanceof Float){
            float f = ((Float) param).floatValue();
            payloadList.add(floatToIEEE754(f));
        }
    }

    public static int bytesToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static short bytesToShort(byte[] b) {
        return (short) (((b[0] << 8) | b[1] & 0xff));
    }


    public void ALC_SEND_CMD_PARAMS(int cmd){
        PICOP_PPCPMSG_COMMAND = PicoP_PpcpData.getCommandIdByString(cmd);
        PicoP_Ulog.i(TAG, PicoP_Ulog._FUNC_()+"PICOP_PPCPMSG_COMMAND = " + PICOP_PPCPMSG_COMMAND);
        updatePayload();
    }


}
