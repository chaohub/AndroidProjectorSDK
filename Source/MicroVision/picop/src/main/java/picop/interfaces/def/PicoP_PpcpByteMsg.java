package picop.interfaces.def;

/**
 * Created on 2017/3/30.
 */

public class PicoP_PpcpByteMsg {

    private static final String TAG = "PicoP_PpcpByteMsg ";

// ************************** Classic PPCP Packet Structure ***************************
//
//  +---------+--------+-------------+-----------+-----------+--------------+-------------+
//  | Dest(8) | Src(8) | Priority(2) | protID(2) | Length(12)| Payload(n*8) | Checksum(8) |
//  +---------+--------+-------------+-----------+-----------+--------------+-------------+
//
// ****************************************************************************

    byte[] PICOP_PPCPMSG_START_CODE;
    byte[] PICOP_PPCPMSG_DEVICE_ID;
    byte[] PICOP_PPCPMSG_SOURCE_ID;
    byte[] PICOP_PPCPMSG_PRIORITY;
    byte[] PICOP_PPCPMSG_PROTOCOLID;
    byte[] PICOP_PPCPMSG_PAYLOADLEN;
    byte[] PICOP_PPCPMSG_PAYLOAD;
    byte[] PICOP_PPCPMSG_CHECKSUM;

    public void PicoP_PpcpByteMsg(){
        PICOP_PPCPMSG_START_CODE                                                                    = new byte[8];
        PICOP_PPCPMSG_DEVICE_ID                                                                     = new byte[8];
        PICOP_PPCPMSG_SOURCE_ID                                                                     = new byte[8];
        PICOP_PPCPMSG_PRIORITY                                                                      = new byte[2];
        PICOP_PPCPMSG_PROTOCOLID                                                                    = new byte[2];
        PICOP_PPCPMSG_PAYLOADLEN                                                                    = new byte[12];
        PICOP_PPCPMSG_CHECKSUM                                                                      = new byte[8];
    }

    /*
    *   public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    *   src -- the source array.
    *   srcPos -- the start position in source array
    *   dest -- the destination array
    *   destPos -- the start position in destination array.
    *   length -- the length to copy.
    * */

    public boolean setPpcpMsgStart(byte[] start){
        if(start.length != PICOP_PPCPMSG_START_CODE.length){
            PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " length not matched.");
            return false;
        } else {
            System.arraycopy(start, 0, PICOP_PPCPMSG_START_CODE, 0, start.length);
            return true;
        }
    }

    public boolean setPpcpMsgDeviceId(byte[] device_id){
        if(device_id.length != PICOP_PPCPMSG_DEVICE_ID.length){
            PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " length not matched.");
            return false;
        } else {
            System.arraycopy(device_id, 0, PICOP_PPCPMSG_DEVICE_ID, 0, device_id.length);
            return true;
        }
    }

    public boolean setPpcpMsgSourceId(byte[] source_id){
        if(source_id.length != PICOP_PPCPMSG_SOURCE_ID.length){
            PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " length not matched.");
            return false;
        } else {
            System.arraycopy(source_id, 0, PICOP_PPCPMSG_SOURCE_ID, 0, source_id.length);
            return true;
        }
    }

    public boolean setPpcpMsgPriority(byte[] priority){
        if(priority.length != PICOP_PPCPMSG_PRIORITY.length){
            PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " length not matched.");
            return false;
        } else {
            System.arraycopy(priority, 0, PICOP_PPCPMSG_PRIORITY, 0, priority.length);
            return true;
        }
    }

    public boolean setPpcpMsgProtocolId(byte[] protocol_id){
        if(protocol_id.length != PICOP_PPCPMSG_PROTOCOLID.length){
            PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " length not matched.");
            return false;
        } else {
            System.arraycopy(protocol_id, 0, PICOP_PPCPMSG_PROTOCOLID, 0, protocol_id.length);
            return true;
        }
    }

    public boolean setPpcpMsgPayloadLen(byte[] payloadlen){
        if(payloadlen.length != PICOP_PPCPMSG_PAYLOADLEN.length){
            PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " length not matched.");
            return false;
        } else {
            System.arraycopy(payloadlen, 0, PICOP_PPCPMSG_PAYLOADLEN, 0, payloadlen.length);
            return true;
        }
    }

    public void setPpcpMsgPayload(int length, byte[] payload){
        PICOP_PPCPMSG_PAYLOAD = new byte[length];
        System.arraycopy(payload, 0,PICOP_PPCPMSG_PAYLOAD, 0, payload.length);
    }

    public boolean setPpcpMsgCheckSum(byte[] checksum){
        if(checksum.length != PICOP_PPCPMSG_CHECKSUM.length){
            PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " length not matched.");
            return false;
        } else{
            System.arraycopy(checksum, 0, PICOP_PPCPMSG_CHECKSUM, 0, checksum.length);
            return true;
        }
    }

    public byte[] getPpcpMsgNonCheckSum(){
        byte[] msg = new byte[PICOP_PPCPMSG_START_CODE.length + PICOP_PPCPMSG_DEVICE_ID.length + PICOP_PPCPMSG_SOURCE_ID.length
                + PICOP_PPCPMSG_PRIORITY.length + PICOP_PPCPMSG_PROTOCOLID.length + PICOP_PPCPMSG_PAYLOADLEN.length
                + PICOP_PPCPMSG_PAYLOAD.length ];
        int pos = 0;
        /* set start code*/
        System.arraycopy(PICOP_PPCPMSG_START_CODE, 0, msg, pos, PICOP_PPCPMSG_START_CODE.length);
        pos += PICOP_PPCPMSG_START_CODE.length;
        /* set destination device id*/
        System.arraycopy(PICOP_PPCPMSG_DEVICE_ID, 0, msg, pos, PICOP_PPCPMSG_DEVICE_ID.length);
        pos += PICOP_PPCPMSG_DEVICE_ID.length;
        /* set source device id*/
        System.arraycopy(PICOP_PPCPMSG_SOURCE_ID, 0, msg, pos, PICOP_PPCPMSG_SOURCE_ID.length);
        pos += PICOP_PPCPMSG_SOURCE_ID.length;
        /* set priority */
        System.arraycopy(PICOP_PPCPMSG_PRIORITY, 0, msg, pos, PICOP_PPCPMSG_PRIORITY.length);
        pos += PICOP_PPCPMSG_PRIORITY.length;
        /* set protocol id */
        System.arraycopy(PICOP_PPCPMSG_PROTOCOLID, 0, msg, pos, PICOP_PPCPMSG_PROTOCOLID.length);
        pos += PICOP_PPCPMSG_PROTOCOLID.length;
        /* set payload length */
        System.arraycopy(PICOP_PPCPMSG_PAYLOADLEN, 0, msg, pos, PICOP_PPCPMSG_PAYLOADLEN.length);
        pos += PICOP_PPCPMSG_PAYLOADLEN.length;
        /* set payload */
        System.arraycopy(PICOP_PPCPMSG_PAYLOAD, 0, msg, pos, PICOP_PPCPMSG_PAYLOAD.length);
        pos += PICOP_PPCPMSG_PAYLOAD.length;

        return msg;
    }

    public byte[] getPpcpMsgBody(){
        byte[] msg = new byte[PICOP_PPCPMSG_START_CODE.length + PICOP_PPCPMSG_DEVICE_ID.length + PICOP_PPCPMSG_SOURCE_ID.length
                + PICOP_PPCPMSG_PRIORITY.length + PICOP_PPCPMSG_PROTOCOLID.length + PICOP_PPCPMSG_PAYLOADLEN.length
                + PICOP_PPCPMSG_PAYLOAD.length + PICOP_PPCPMSG_CHECKSUM.length];
        int pos = 0;
        /* set start code*/
        System.arraycopy(PICOP_PPCPMSG_START_CODE, 0, msg, pos, PICOP_PPCPMSG_START_CODE.length);
        pos += PICOP_PPCPMSG_START_CODE.length;
        /* set destination device id*/
        System.arraycopy(PICOP_PPCPMSG_DEVICE_ID, 0, msg, pos, PICOP_PPCPMSG_DEVICE_ID.length);
        pos += PICOP_PPCPMSG_DEVICE_ID.length;
        /* set source device id*/
        System.arraycopy(PICOP_PPCPMSG_SOURCE_ID, 0, msg, pos, PICOP_PPCPMSG_SOURCE_ID.length);
        pos += PICOP_PPCPMSG_SOURCE_ID.length;
        /* set priority */
        System.arraycopy(PICOP_PPCPMSG_PRIORITY, 0, msg, pos, PICOP_PPCPMSG_PRIORITY.length);
        pos += PICOP_PPCPMSG_PRIORITY.length;
        /* set protocol id */
        System.arraycopy(PICOP_PPCPMSG_PROTOCOLID, 0, msg, pos, PICOP_PPCPMSG_PROTOCOLID.length);
        pos += PICOP_PPCPMSG_PROTOCOLID.length;
        /* set payload length */
        System.arraycopy(PICOP_PPCPMSG_PAYLOADLEN, 0, msg, pos, PICOP_PPCPMSG_PAYLOADLEN.length);
        pos += PICOP_PPCPMSG_PAYLOADLEN.length;
        /* set payload */
        System.arraycopy(PICOP_PPCPMSG_PAYLOAD, 0, msg, pos, PICOP_PPCPMSG_PAYLOAD.length);
        pos += PICOP_PPCPMSG_PAYLOAD.length;
        /* set checksum */
        System.arraycopy(PICOP_PPCPMSG_CHECKSUM, 0, msg, pos, PICOP_PPCPMSG_CHECKSUM.length);

        return msg;
    }
}
