package picop.interfaces.def;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static picop.interfaces.def.PicoP_Operator.responseMsgmsg;

/**
 * Created on 2017/3/23.
 */

public class SerialPortOperator {
    private static final String TAG = "SerialPortOperator ";

    private  static SerialPort mSerialPort;

    private static OutputStream mOutputStream;

    private static InputStream mInputStream;

    private static boolean isInit = false;

    public static ReadThread mSerialReadThread;

    public static PicoP_RC open(String path, int baudrate, int flags){
        PicoP_RC ret = PicoP_RC.eSUCCESS;
        if (isInit){
            // we add this, to avoid open the port repeatedly.
            ret = PicoP_RC.eALREADY_OPENED;
        } else {
            try {
                mSerialPort = new SerialPort(new File(path), baudrate, flags);
            } catch (Exception e) {
                PicoP_Ulog.e(TAG, "" + e.toString());
                return PicoP_RC.eUNINITIALIZED;
            }

            if(mSerialPort ==null){
                ret = PicoP_RC.eDEVICE_ERROR;
            } else {
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();
                mSerialReadThread = new ReadThread();
                isInit = true;
            }
        }
        return  ret;
    }

    public static boolean sendCmds(String cmd){

        boolean result = true;
        if( !isInit ){
            PicoP_Ulog.e(TAG, "init not finised.");
            return false;
        }
        // because PSE receive the cmd in Hex byte, we need change the formate.
        byte[] mBuffer = hexStringToBytes(cmd);
        try{
            if(mOutputStream !=null){
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (Exception e) {
            PicoP_Ulog.e(TAG, "" + e.toString());
        }

        return result;
    }

    public static boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        byte[] mBufferTemp = new byte[mBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBufferTemp);

            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /*
    *  Note @ 2017.03.29
    *  For response message, the formate always like below.
    *  Header       (1      byte) --> 0xC0.
    *  Info         (4      byte) --> 0xXX 0xXX 0xXX 0xXX <--the last one means the length of the data.
    *  Data         (Len    byte) --> 0xXX 0xXX 0xXX 0xXX 0xXX 0xXX
    *  CheckSum     (1      byte) --> checksum.
    *  And we need verify the message with the checksum flag,
    *  if it's matched            --> this is a correct message, share it.
    *  if it's not matched        --> this is a bad  message, drop it.
    */

    //This should change to a statemachine
    private static int tryCount = 0;
    private static boolean stop = false;
    private static final Object readLock = new Object();
    private static final int CHECK_HEADER = 0;
    private static final int CHECK_INFO   = 1;
    private static final int CHECK_DATA   = 2;

    private static final int THREAD_SLEEP_TIME = 20;
    private static final int MAX_TRY_COUNT     = 400;

    public static class ReadThread extends Thread {
        private static String response = "";
        private int status        = CHECK_HEADER;
        private int length_header = 1;
        private int length_info   = 4;
        private int length_data   = 0;

        @Override
        public void run() {
            response = "";
            status = CHECK_HEADER;
            tryCount = 0;
            stop = false;

            synchronized (readLock) {
                PicoP_Ulog.i(TAG,"READLOCK tryCount="+tryCount+" stop="+stop);
                while (tryCount < MAX_TRY_COUNT && !stop &&
                       isInit && !isInterrupted()) {
                    int size = -1;
                    try {
                        if (mInputStream == null)
                        {
                            PicoP_Ulog.e(TAG,"No input stream");
                            return;
                        }

                        if (status == CHECK_HEADER) {
                            Thread.sleep(100);
                            byte[] buffer = new byte[length_header];
                            size = mInputStream.read(buffer);
                            if (size > 0) {
                                String str = bytesToHexString(buffer, size);
                                if (str.startsWith("c0")) {
                                    response += str;
                                    status = CHECK_INFO;
                                }
                            }
                        } else if (status == CHECK_INFO) {
                            byte[] buffer = new byte[length_info];
                            size = mInputStream.read(buffer);
                            if (size > 0) {
                                response += bytesToHexString(buffer, size);
                                length_data = getLengthFromBuffer(response);
                                status = CHECK_DATA;
                            }
                        } else if (status == CHECK_DATA) {
                            byte[] buffer = new byte[length_data + 1];
                            size = mInputStream.read(buffer);
                            response += bytesToHexString(buffer, size);
                            if (size == length_data + 1){

                                responseMsgmsg.setMsg(response);

                            } else {
                                PicoP_Ulog.i(TAG, "  DATA read size "+size+" != "+(length_data+1)+" resp='"+response+"'");
                            }
                            stop = true;
                        }
                        Thread.sleep(THREAD_SLEEP_TIME);
                        tryCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    public static PicoP_RC closeSerialPort() {
        isInit = false;
        if (mSerialReadThread != null) {
            mSerialReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.closeconn();
        }
        return PicoP_RC.eSUCCESS;
    }

    private static int getIntFromHex(char hex){
        return "0123456789abcdef".indexOf(hex);
    }

    public static int getLengthFromBuffer(String msg){
        int ret = 0;
        char[] value = msg.toCharArray();
        int len = value.length;
        if (len > 2) {
            ret = getIntFromHex(value[len - 3]) * 256 + getIntFromHex(value[len - 2]) * 16 + getIntFromHex(value[len - 1]);
        }
        return ret;
    }

    /**
     * Convert byte[] to hex string.
     *  1. byte -> int
     *  2. hexStr = Integer.toHexString(int)
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src, int len){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv+"");
        }
        return stringBuilder.toString();
    }
    public static String bytesToBinaryString(byte[] src, int len){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toBinaryString(v);
            int length = hv.length();
            while (length <4) {
                stringBuilder.append(0);
                length++;
            }
            stringBuilder.append(hv+"");
        }
        PicoP_Ulog.i(TAG,"len " + len + ", buffer " + stringBuilder.toString());
        return stringBuilder.toString();
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
