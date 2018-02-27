package picop.interfaces.def;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import static picop.interfaces.def.PicoP_Operator.responseMsgmsg;

/**
 * Created by louis.yang on 2017/5/5.
 */

public class UsbPortOperator implements UsbSerialDriver{
    private static final String TAG = "UsbPortOperator ";

    private static int VendorID = 0x148A;
    private static int ProductID = 0x0004;
    private static final int DEFAULT_BAUD_RATE = 9600;
    private static final int DEFAULT_READ_BUFFER_SIZE = 4 * 1024;
    private static final int DEFAULT_WRITE_BUFFER_SIZE = 4 * 1024;

    private static UsbDevice mDevice = null;
    private UsbSerialPort mPort;
    public static Context mContext;
    private static PendingIntent mPendingIntent;
    private static UsbDeviceConnection mConnection;

    private static UsbEndpoint mReadEndpoint;
    private static UsbEndpoint mWriteEndpoint;

    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    protected static final Object mReadBufferLock = new Object();
    protected static final Object mWriteBufferLock = new Object();
    protected static byte[] mReadBuffer = new byte[DEFAULT_READ_BUFFER_SIZE];
    protected static byte[] mWriteBuffer = new byte[DEFAULT_WRITE_BUFFER_SIZE];
    private static final int READ_WAIT_MILLIS = 0;
    private static final int MAX_PACKET_SIZE = 64;

    private static boolean isInit = false;
    private static final boolean mEnableAsyncReads = true;
    public static ReadThread mUsbReadThread;

    public UsbPortOperator(UsbDevice device){
        mDevice = device;
    }

    @Override
    public UsbDevice getDevice() {
        return mDevice;
    }

    @Override
    public List<UsbSerialPort> getPorts() {
        return Collections.singletonList(mPort);
    }

    public static PicoP_RC open(PicoP_ConnectionInfo connectionInfo){
        PicoP_RC ret = PicoP_RC.eSUCCESS;

        if (isInit){
            // we add this, to avoid open the port repeatly.
            ret = PicoP_RC.eALREADY_OPENED;
        } else {
            mContext = connectionInfo.PicoP_USB.getContext();
            final UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            if (!deviceList.isEmpty()) {
                for (UsbDevice device : deviceList.values()) {
                    PicoP_Ulog.i(TAG, "device.VendorID = " + device.getVendorId() + " device.ProductID = " + device.getProductId());
                    if(device.getVendorId() == VendorID && device.getProductId()==ProductID) {
                        mDevice = device;

                        if (!usbManager.hasPermission(mDevice)) {
                            mPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                                    new Intent(ACTION_USB_PERMISSION), 0);
                            usbManager.requestPermission(mDevice, mPendingIntent);

                            return PicoP_RC.eINCOMPLETE;
                        } else {
                            PicoP_Ulog.i(TAG, "We have the USB permission! ");
                        }
                    }
                }
                if(mDevice == null){
                    PicoP_Ulog.e(TAG, "Not find the PicoP usb devices! ");
                    return PicoP_RC.eUNINITIALIZED;
                }
            } else {
                PicoP_Ulog.e(TAG, "Not find the correct usb devices! ");
                return PicoP_RC.eUNINITIALIZED;
            }

            mConnection = usbManager.openDevice(mDevice);

            try {
                for (int i = 0; i < mDevice.getInterfaceCount(); i++) {
                    UsbInterface usbIface = mDevice.getInterface(i);
                    if (mConnection.claimInterface(usbIface, true)) {
                        PicoP_Ulog.i(TAG, "claimInterface " + i + " SUCCESS");
                    } else {
                        PicoP_Ulog.e(TAG, "claimInterface " + i + " FAIL");
                    }
                }

                UsbInterface dataIface = mDevice.getInterface(mDevice.getInterfaceCount() - 1);
                for (int i = 0; i < dataIface.getEndpointCount(); i++) {
                    UsbEndpoint ep = dataIface.getEndpoint(i);
                    if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
                        if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                            mReadEndpoint = ep;
                        } else if(ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                            mWriteEndpoint = ep;
                        }
                    }
                }
                isInit = true;
                mUsbReadThread = new ReadThread();
            } finally {
                if (!isInit) {
                    try {
                        close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return ret;
    }

    public static PicoP_RC close() throws IOException {
        isInit = false;
        mDevice = null;
        if (mUsbReadThread != null) {
            mUsbReadThread.interrupt();
        }

        if (mConnection == null) {
            throw new IOException("Already closed");
        }
        try {
            mConnection.close();
        } finally {
            mConnection = null;
        }
        return PicoP_RC.eSUCCESS;
    }

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

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static int getIntFromHex(char hex){
        return "0123456789abcdef".indexOf(hex);
    }

    public static int getLenthFromBuffer(String msg){
        char[] value = msg.toCharArray();
        int len = value.length;
        int ret = 0;
        ret = getIntFromHex(value[7])*256 + getIntFromHex(value[8])*16 + getIntFromHex(value[9]);
        return ret;
    }

    public static int read(byte[] dest, int timeoutMillis) throws IOException {
        if (mEnableAsyncReads) {
            PicoP_Ulog.d(TAG, "Going to read bytes");
            final UsbRequest request = new UsbRequest();
            PicoP_Ulog.d(TAG, "Instantiated UsbRequest");
            try {
                request.initialize(mConnection, mReadEndpoint);
                PicoP_Ulog.d(TAG, "Innitialized UsbRequest");;
                final ByteBuffer buf = ByteBuffer.wrap(dest);
                PicoP_Ulog.d(TAG, "Wrapped Destination in ByteBuffer");
                if (!request.queue(buf, dest.length)) {
                    throw new IOException("Error queueing request.");
                }
                PicoP_Ulog.d(TAG, "Queued request");

                Timer t = new Timer();

                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        request.cancel();
                        PicoP_Ulog.d(TAG, "Timer canceled USB request");
                    }
                }, 500);

                PicoP_Ulog.d(TAG, "Timer scheduled");

                final UsbRequest response = mConnection.requestWait();

                t.cancel();
                PicoP_Ulog.d(TAG, "Timer canceled");

                PicoP_Ulog.d(TAG, "Finished requestWait");

                if (response == null) {
                    throw new IOException("Null response");
                }

                final int nread = buf.position();
                if (nread > 0) {
                    return nread;
                } else {
                    return 0;
                }
            }
            finally {
                if(request != null){
                    request.close();
                }
            }
        }

        final int numBytesRead;
        synchronized (mReadBufferLock) {
            int readAmt = Math.min(dest.length, mReadBuffer.length);
            numBytesRead = mConnection.bulkTransfer(mReadEndpoint, mReadBuffer, readAmt,
                    timeoutMillis);
            if (numBytesRead < 0) {
                if (timeoutMillis == Integer.MAX_VALUE) {
                    return -1;
                }
                return 0;
            }
            System.arraycopy(mReadBuffer, 0, dest, 0, numBytesRead);
        }

        PicoP_Ulog.i(TAG, "read function numBytesRead = " + numBytesRead);
        return numBytesRead;
    }

    public static int readRequest(byte[] dest, int timeoutMillis)throws IOException {
        int inMax = mReadEndpoint.getMaxPacketSize();

        ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
        UsbRequest usbRequest = new UsbRequest();
        usbRequest.initialize(mConnection, mReadEndpoint);
        usbRequest.queue(byteBuffer, inMax);
        if(mConnection.requestWait() == usbRequest){
            PicoP_Ulog.i(TAG, "byteBuffer.position() = " + byteBuffer.position());
            byte[] retData = byteBuffer.array();
            System.arraycopy(retData, 0, dest, 0, byteBuffer.array().length);
            PicoP_Ulog.e(TAG, "retData length = " + retData.length);
            PicoP_Ulog.e(TAG, "dest = " + bytesToHexString(dest, inMax));
            }
        usbRequest.close();

        return 0;
    }

    public static int write(byte[] src, int timeoutMillis) throws IOException {
        int offset = 0;

        while (offset < src.length) {
            final int writeLength;
            final int amtWritten;

            synchronized (mWriteBufferLock) {
                final byte[] writeBuffer;

                writeLength = Math.min(src.length - offset, mWriteBuffer.length);
                PicoP_Ulog.i(TAG, "writeLength = " + writeLength);
                if (offset == 0) {
                    writeBuffer = src;
                } else {
                    PicoP_Ulog.d(TAG, "Writing with offset by making a copy");
                    // bulkTransfer does not support offsets, make a copy.
                    System.arraycopy(src, offset, mWriteBuffer, 0, writeLength);
                    writeBuffer = mWriteBuffer;
                }

                String msg = String.format("Going to send %d %d", writeLength, timeoutMillis);
                PicoP_Ulog.i(TAG, msg);
                amtWritten = mConnection.bulkTransfer(mWriteEndpoint, writeBuffer, writeLength,
                        timeoutMillis);
            }
            if (amtWritten <= 0) {
                throw new IOException("Error writing to endpoint " + mWriteEndpoint.getAddress() + " " + writeLength
                        + " bytes at offset " + offset + " length=" + src.length + " fd=" + mConnection.getFileDescriptor()
                        + " bulkTransfer returned " + amtWritten);
            }

            PicoP_Ulog.d(TAG, "Wrote to endpoint " + mWriteEndpoint.getAddress() + " amt=" + amtWritten + " attempted=" + writeLength);

            offset += amtWritten;
        }

        return offset;
    }

    public static boolean sendCmds(String cmd){
        boolean result = true;

        if( !isInit ){
            PicoP_Ulog.e(TAG, "init not finised.");
            return false;
        }
        PicoP_Ulog.d(TAG, "cmd = " + cmd);
        byte[] mBuffer = hexStringToBytes(cmd);

        try {

            if(write(mBuffer, 100) > 0){
                result = true;
            } else {
                result = false;
            }
        } catch (IOException e){
            e.printStackTrace();
            PicoP_Ulog.e(TAG, "" + e.toString());
        }

        return result;
    }

    private static int tryCount = 0;
    private static boolean stop = false;
    private static Object readLock = new Object();
    private static final int MAX_TRY_COUNT=1;
    public static class ReadThread extends Thread {
        private static String response = "";
        private int lenth_header_and_data      = 5;
        private int lenth_chksum = 1;
        private int lenth_data      = 0;
        private boolean fitstPacket = true;
        private int readTimes = 0;

        @Override
        public void run() {
            PicoP_Ulog.i(TAG, "Running USB read code");
            tryCount = 0;
            fitstPacket = true;
            stop =false;
            synchronized (readLock) {
                PicoP_Ulog.i(TAG, "Acquired lock");
                while (isInit && !isInterrupted() && (tryCount < MAX_TRY_COUNT) && !stop) {
                    int size;
                    PicoP_Ulog.i(TAG, "Trying to read USB");

                    try {
                        byte[] readBuf = new byte[MAX_PACKET_SIZE];
                        size = read(readBuf, READ_WAIT_MILLIS);
                        PicoP_Ulog.i(TAG, "size = " + size);
                        if (size > 0) {
                            String str = bytesToHexString(readBuf, MAX_PACKET_SIZE);
                            PicoP_Ulog.i(TAG, "str = " + str);
                            if (str.startsWith("c0") && fitstPacket) {
                                lenth_data = getLenthFromBuffer(str);
                                PicoP_Ulog.i(TAG,"lenth_data = " +lenth_data);
                                if((lenth_data + lenth_header_and_data + lenth_chksum) > MAX_PACKET_SIZE){
                                    response += str;
                                    readTimes = (int)lenth_data/MAX_PACKET_SIZE;
                                    PicoP_Ulog.i(TAG,"readTimes = " +readTimes);
                                    tryCount = 0;
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //continue;
                                } else {
                                    response += bytesToHexString(readBuf, lenth_data + lenth_header_and_data + lenth_chksum);
                                    responseMsgmsg.setMsg(response);
                                    PicoP_Ulog.i(TAG,"response 1 = " +response);
                                    stop = true;
                                }
                                fitstPacket = false;
                            } else {
                                if(readTimes > 1){
                                    response += bytesToHexString(readBuf, MAX_PACKET_SIZE);
                                    readTimes--;
                                } else {
                                    int leftData = (lenth_data+lenth_header_and_data + lenth_chksum)%MAX_PACKET_SIZE;
                                    PicoP_Ulog.i(TAG,"leftData = " +leftData);
                                    response += bytesToHexString(readBuf, leftData);
                                    responseMsgmsg.setMsg(response);
                                    PicoP_Ulog.i(TAG,"response 2 = " +response);
                                    stop = true;
                                }

                            }
                        } else {
                            tryCount++;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e){
                        PicoP_Ulog.e(TAG, e.getLocalizedMessage());
                    }
                }
				
                response = "";
                readTimes = 0;
            }
        }
    }
}
