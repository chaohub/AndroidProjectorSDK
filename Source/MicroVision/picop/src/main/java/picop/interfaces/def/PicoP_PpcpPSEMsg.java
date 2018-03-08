package picop.interfaces.def;

/**
 * Created on 2017/3/31.
 */

public class PicoP_PpcpPSEMsg {

    private static final String TAG = "PicoP_PpcpPSEMsg ";

    public static String PICOP_PPCP_RESP_MSG_START_CODE;     // Ex. 11000000
    public static String PICOP_PPCP_RESP_MSG_DES_ID;         // Ex. 00000000
    public static String PICOP_PPCP_RESP_MSG_SOU_ID;         // Ex. 00000010
    public static String PICOP_PPCP_RESP_MSG_PRIORITY;       // Ex. 01
    public static String PICOP_PPCP_RESP_MSG_PROTOCOL_ID;    // Ex. 00
    public static String PICOP_PPCP_RESP_MSG_PAYLOAD_LEN;    // Ex. 001001
    public static String PICOP_PPCP_RESP_MSG_PAYLOAD;        // Ex. (00000000)*N
    public static String PICOP_PPCP_RESP_MSG_PAYLOAD_DES_ID; // Ex. 00000000
    public static String PICOP_PPCP_RESP_MSG_PAYLOAD_SOU_ID; // Ex. 00000000
    public static String PICOP_PPCP_RESP_MSG_PAYLOAD_FLAG;   // Ex. 01
    public static String PICOP_PPCP_RESP_MSG_PAYLOAD_SEQ;    // Ex. 000000
    public static String PICOP_PPCP_RESP_MSG_CHECKSUM;       // Ex. 00000000

    public static int sSeqNum;
    public static boolean sChecksum;
    public static String sPayload;

    /* response msg from PSE side. */
    private static String mResponseMsg;
    private static int mIndex;

    public void PicoP_PpcpResponseMsg(){
    }

    /* After below method called, we will parse the msg automatically. */
    public static void setMsg(String _msg){
        PicoP_Ulog.i(TAG, "set msg '" + _msg + "'");
        mResponseMsg = _msg;
        doParse(mResponseMsg);
    }

    public void clearMsg(){
        PicoP_Ulog.i(TAG, "clear msg");
        mResponseMsg = "";
        mIndex = 0;
    }

    public String getMsg(){
        return mResponseMsg;
    }

    public static void doParse(String msg){
        char[] msgline = msg.toCharArray();
        sChecksum = checksum(msgline);
        PicoP_Ulog.i(TAG,"checksum = " + sChecksum);
        if(sChecksum){
            getPayload(msgline);
            PicoP_Ulog.i(TAG,"current payload info is '" + sPayload + "'");
        }
    }

    public static boolean checksum(char[] cmd){
        boolean ret = false;
        int len = cmd.length;
        int checksum = getIntFromHex(cmd[len-2]) * 16 + getIntFromHex(cmd[len-1]);
        int ender = 0;
        int cur = 0;
        for(int i = 2; i< len-2; i++){
            cur = getIntFromHex(cmd[i]) * 16 + getIntFromHex(cmd[i+1]);
            if(ender < cur){
                ender = ender + 256 - cur;
            } else {
                ender = ender - cur;
            }
            i++;
        }
        if(ender == checksum){
            int temp = getIntFromHex(cmd[14]) * 16 + getIntFromHex(cmd[15]);
            sSeqNum = getSequenceNum(Integer.toBinaryString(temp));
            PicoP_Ulog.i(TAG,"chksum seqnum = " + sSeqNum);
            ret = true;
        } else{
            ret = false;
        }
        return ret;
    }

    private static int getIntFromHex(char hex){
        return "0123456789abcdef".indexOf(hex);
    }

    private static int getSequenceNum(String cmd){
        while(cmd.length()<8){
            cmd = "0"+cmd;
        }
        int ret = 0;
        byte[] bytes = cmd.getBytes();
        byte[] temp = new byte[6];
        System.arraycopy(bytes, 2, temp, 0, 6);
        String value = "";
        for(int i = 0; i<temp.length ;i++){
            value += temp[i]-48;
        }
        ret = Integer.parseInt(value, 2);
        return ret;
    }

    private static void getPayload(char[] cmd){
        int len = cmd.length;
        sPayload = "";
        for(int i = 16; i< len-2;i++){
            sPayload = sPayload + cmd[i];
        }
        PicoP_Ulog.i(TAG,"payload len="+len+" '"+sPayload+"'");
        mIndex = 0;
    }

    public static int getL2BINT8(){
        char[] cmd = sPayload.toCharArray();
        int len = cmd.length;
        String ret = "" + cmd[mIndex++] + cmd[mIndex++];
        int val = Integer.valueOf(ret,16);
        if(ret.startsWith("f")||ret.startsWith("F")){
            val = val - 256;
        }
        PicoP_Ulog.i(TAG,"getL2BINT8: len="+len+" '"+cmd+"' => "+val);
        return val;
    }

    public static int getL2BINT16(){
        String ret = "";
        char[] cmd = sPayload.toCharArray();
        int len = cmd.length;
        for(int i =0; i<2; i++){
            ret = "" + cmd[mIndex++] + cmd[mIndex++] + ret;
        }
        int val = Integer.valueOf(ret,16);
        if(ret.startsWith("f")||ret.startsWith("F")){
            val = val - 65536;
        }
        PicoP_Ulog.i(TAG,"getL2BINT16: len="+len+" '"+cmd+"' => "+val);
        return val;
    }

    public static int getL2BINT32(){
        String ret = "";
        char[] cmd = sPayload.toCharArray();
        int len = cmd.length;
        for(int i =0; i<4; i++){
            ret = "" + cmd[mIndex++] + cmd[mIndex++] + ret;
        }
        int val = Integer.valueOf(ret,16);
        if(ret.startsWith("f")||ret.startsWith("F")){
            val = val - 65536;
        }
        PicoP_Ulog.i(TAG,"getL2BINT32: len="+len+" '"+cmd+"' => "+val);
        return val;
    }

    public static String getL2BFP32(){
        String ret = "";
        char[] cmd = sPayload.toCharArray();
        int len = cmd.length;
        for(int i =0; i<4; i++){
            ret = "" + cmd[mIndex++] + cmd[mIndex++] + ret;
        }
        PicoP_Ulog.i(TAG,"getL2BFP32: len="+len+" '"+cmd+"' => "+ret);
        return  ret;
    }

    public static int GetReply_UINT8(){
        return getL2BINT8();
    }

    public static int GetReply_UINT16(){
        return getL2BINT16();
    }

    public static int GetReply_UINT32(){
        return getL2BINT32();
    }

    public static int GetReply_INT8(){
        return getL2BINT8();
    }

    public static int GetReply_INT16(){
        return getL2BINT16();
    }

    public static int GetReply_INT32(){
        return getL2BINT32();
    }

    public static float GetReply_FP32(){
        String hex = getL2BFP32();
        return Float.intBitsToFloat(Integer.valueOf(hex,16));
    }

    public static int GetReply_Enum(){
        return getL2BINT8();
    }

    public static int GetReply_Enum32(){
        return getL2BINT32();
    }

    public static String GetReply_String(int len){
        StringBuilder sb = new StringBuilder();
        char[] cmd = sPayload.toCharArray();
        int cmdlen = cmd.length;
        for(int i =0; i<len; i++){
            String ret = "" + cmd[mIndex++] + cmd[mIndex++];
            int val = Integer.valueOf(ret,16);
            if (val > 0)
                sb.append((char)val);
        }
        PicoP_Ulog.i(TAG,"GetReply_String: len="+cmdlen+" '"+cmd+"' => "+sb.toString());
        return sb.toString();
    }

    // Return a PicoP_RC value
    public static PicoP_RC GetReplyStatus(){
        PicoP_RC ret = PicoP_RC.eCOMMUNICATION_ERROR;
        ret = ret.inttoEnum(getL2BINT16());
        mIndex += 4;
        return ret;
    }
}
