package picop.interfaces.def;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by carey.wang on 2017/3/23.
 */

public class SerialPort {
    private static final String TAG = "SerialPort";

    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutoutStream;

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException{
        /* open native library file */
        try{
            System.loadLibrary("serial_port");
        } catch ( Exception e) {
            PicoP_Ulog.e(TAG, "" + e.toString());
        }
        /* Check access permission */
        PicoP_Ulog.i(TAG,"Serial port path = " + device.getAbsolutePath());
        if(!device.canRead()){
            PicoP_Ulog.i(TAG," path can't be read.");
        } else if (!device.canWrite()){
            PicoP_Ulog.i(TAG, "path can't be write");
        }
        if( !device.canRead() || !device.canWrite()) {
            PicoP_Ulog.d(TAG,"Serial port can't be read or write, try to chmod.");
        }
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if( mFd == null ){
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutoutStream = new FileOutputStream(mFd);

        PicoP_Ulog.i(TAG, "Serial port open Successfully.");
    }
    public static void closeconn(){
        close();
        PicoP_Ulog.i(TAG,"Serial port open Successfully.");
    }

    public InputStream getInputStream(){
        return mFileInputStream;
    }

    public OutputStream getOutputStream(){
        return mFileOutoutStream;
    }

    /* JNI */
    private native static FileDescriptor open(String path, int baudrate, int flags);
    private native static void close();
    static {
        System.loadLibrary("serial_port");
    }
}
