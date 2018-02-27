/** \file PicoP_ConnectionInfo.java
* Copyright	: (c)2011 Microvision
*
* Description:  Connection information base class
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

import android.content.Context;

import static picop.interfaces.def.PicoP_ConnectionTypeE.eRS232;
import static picop.interfaces.def.PicoP_ConnectionTypeE.eUSB;
import static picop.interfaces.def.PicoP_ConnectionTypeE.eBTH;

public class PicoP_ConnectionInfo
{
    private boolean connected = false;
    private PicoP_ConnectionTypeE mConnectType;

    PicoP_RS232INFO PicoP_RS232;

    PicoP_USBINFO PicoP_USB;

    public PicoP_ConnectionInfo(){

    }

    public PicoP_ConnectionInfo(PicoP_ConnectionTypeE connectType) {

        //PicoP_ConnectionTypeE connectType = eRS232;
        this.connected = false;
        this.mConnectType = connectType;
        switch (connectType){
            case eRS232:
                this.PicoP_RS232 = new PicoP_RS232INFO();
                break;
            case eUSB:
                this.PicoP_USB = new PicoP_USBINFO();
                break;
            case eBTH:
                break;
            default:
                break;
        }
    }

    public void setConnectionStatus(boolean conn){
        this.connected = conn;
    }

    public boolean getConnectionStatus(){
        return connected;
    }

    public void setRS232Info(String port, int baudrate){
        this.PicoP_RS232.setPort(port);
        this.PicoP_RS232.setBaudrate(baudrate);
    }

    public void setUsbInfo(Context mContext){
        this.PicoP_USB.setContext(mContext);
    }

    public static class PicoP_RS232INFO{
        private String _port;
        private int _baudrate;

        public void PicoP_RS232INFO(){
        }

        public void PicoP_RS232INFO(String port, int baudrate){
            this._port = port;
            this._baudrate = baudrate;
        }

        public String getPort(){
            return this._port;
        }

        public int getBaudrate(){
            return this._baudrate;
        }

        public void setPort(String port){
            this._port = port;
        }

        public void setBaudrate(int baudrate){
            this._baudrate = baudrate;
        }
    }

    public static class PicoP_USBINFO{
        private static Context _mContext;

        public void PicoP_USBINFO(){
        }

        public void PicoP_USBINFO(Context context){
            this._mContext = context;
        }

        public void setContext(Context context){
            this._mContext = context;
        }

        public Context getContext(){
            return this._mContext;
        }
    }

};
