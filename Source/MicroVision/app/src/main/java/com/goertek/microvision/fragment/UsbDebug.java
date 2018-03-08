package com.goertek.microvision.fragment;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.goertek.microvision.R;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.Utils.MSG_GET_USBLIST;

/**
 * Created on 2017/5/5.
 */

public class UsbDebug extends Fragment {
    private static final String TAG = "UsbDebug";

    /*Louis_debug USB start*/
    private UsbManager mUsbManager;
    /*Louis_debug USB end*/
    private View rootView;
    private static TextView mTvUSBList;
    private Button mBtnGetUSB;
    private ListView mListView;
    //private ArrayAdapter<UsbSerialPort> mAdapter;

    public UsbDevice mUsbDevice;
    public UsbInterface mUsbInterface;
    private UsbEndpoint mUsbEndpoint;
    public UsbDeviceConnection connection;
    private ByteBuffer buffer;
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static PendingIntent mPendingIntent;
    //public static PermissionIntent mPermissionIntent;
    //public MyThread usbThread;

    public static Context mContext;
    //public static Application application;

    private  int VendorID = 0x148A;
    private int ProductID = 0x0004;

    public UsbDebug(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_usb_debug, container, false);
        initView();
        return rootView;
    }

    private void initView(){
        mTvUSBList = (TextView) rootView.findViewById(R.id.fragment_usb_list);
        mTvUSBList.setText("");
        mBtnGetUSB = (Button)rootView.findViewById(R.id.btn_usb_get_list);
        mBtnGetUSB.setOnClickListener(btnClickListener);
        //application = new Application();
        //mContext = application.getApplicationContext();
        Context mContext2 = mContext;
        mUsbManager = (UsbManager) mContext2.getSystemService(Context.USB_SERVICE);
        if(mUsbManager == null){
            Log.i(TAG,"Louis_debug: mUsbManager is null! ");
        }
        mListView = (ListView) rootView.findViewById(R.id.fragment_usb_deviceList);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        if (!deviceList.isEmpty()) {
            for (UsbDevice device : deviceList.values()) {
                if(device.getVendorId() == VendorID && device.getProductId()==ProductID) {
                    mUsbDevice = device;
					
                    Log.i(TAG,"Louis_debug: find the correct usb devices! ");
                    Log.i(TAG, "Louis_debug: VendorID = " + device.getVendorId() + " ProductID = " + device.getProductId());

                    if (!mUsbManager.hasPermission(mUsbDevice)) {
                        mPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                                new Intent(ACTION_USB_PERMISSION), 0);
                        mUsbManager.requestPermission(mUsbDevice, mPendingIntent);
                        Log.i(TAG,"Louis_debug: Request the USB permission! ");
                    } else {
                        Log.i(TAG,"Louis_debug: We have the USB permission! ");
                    }
                } else {
                    //Toast.makeText(context, "NotFind VID and PID",Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"Louis_debug: Not find the PicoP usb devices! ");
                    Log.i(TAG, "Louis_debug: VendorID = " + device.getVendorId() + " ProductID = " + device.getProductId());
                }
            }
        } else {
            Log.i(TAG,"Louis_debug: Not find the correct usb devices! ");
            /*new AlertDialog.Builder(mContext).setTitle("未枚举到设备！")
                    .setMessage("请先连接设备，再重启程序。。")
                    .setCancelable(false)
                    .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                        public voidonClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    }).show();*/
        }

        /*mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        usbThread = new MyThread();
        if(getTheTargetDevice!=null){
            commWithUsbDevice();
        }*/
/*
        mAdapter = new ArrayAdapter<UsbSerialPort>(this,
                android.R.layout.simple_expandable_list_item_2, mEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final TwoLineListItem row;
                if (convertView == null){
                    final LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = (TwoLineListItem) convertView;
                }

                final UsbSerialPort port = mEntries.get(position);
                final UsbSerialDriver driver = port.getDriver();
                final UsbDevice device = driver.getDevice();

                final String title = String.format("Vendor %s Product %s",
                        HexDump.toHexString((short) device.getVendorId()),
                        HexDump.toHexString((short) device.getProductId()));
                row.getText1().setText(title);

                final String subtitle = driver.getClass().getSimpleName();
                row.getText2().setText(subtitle);

                return row;
            }

        };
        */

    }

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_usb_get_list:{
                    Message msg0 = messageHandler.obtainMessage(MSG_GET_USBLIST);
                    Bundle b = new Bundle();
                    //b.putBoolean("commit", true);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

    public UsbDevice getTheTargetDevice() {
        HashMap<String, UsbDevice> usbDeviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> usbDeviceIterator = usbDeviceList.values()
                .iterator();
        while (usbDeviceIterator.hasNext()) {
            UsbDevice device = usbDeviceIterator.next();
            if ("1234".equals(device.getProductId() + "")) {
                mUsbDevice = device;
                if (!mUsbManager.hasPermission(mUsbDevice)) {
                    mPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                            new Intent(ACTION_USB_PERMISSION), 0);
                    mUsbManager.requestPermission(mUsbDevice, mPendingIntent);
                }
                return mUsbDevice;
            }
        }
        return mUsbDevice;
    }

    public void commWithUsbDevice() {
        if (mUsbDevice.getInterfaceCount() > 0) {
            mUsbInterface = mUsbDevice.getInterface(0);
        }
        if (mUsbInterface != null && mUsbInterface.getEndpointCount() > 0) {
            mUsbEndpoint = mUsbInterface.getEndpoint(0);
        }

        connection = mUsbManager.openDevice(mUsbDevice);
        if (connection != null) {
            boolean claim = connection.claimInterface(mUsbInterface, true);

            if(claim){
                //usbThread.start();
            }
        }
    }

    /*public class MyThread extends Thread {
        boolean claim = false;
        public void run() {
            super.run();
            if (mUsbEndpoint != null) {
                UsbRequest request = new UsbRequest();
                boolean isOpenRequest = request.initialize(connection,
                        mUsbEndpoint);
                if (isOpenRequest) {

                    boolean isQueueOK = request.queue(buffer, 128);

                    while (true) {

                        if (isQueueOK && connection.requestWait() == request) {
                        }
                        connection.controlTransfer(0x21, 0x09, 0x300, 0x00,
                                resultOkBuffer2, resultOkBuffer2.length, 100);
                    }

                }

            }

        }
    }*/
}
