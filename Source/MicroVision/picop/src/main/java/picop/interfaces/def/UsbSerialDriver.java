package picop.interfaces.def;

import android.hardware.usb.UsbDevice;

import java.util.List;

/**
 * Created by louis.yang on 2017/5/9.
 */

public interface UsbSerialDriver {
    public UsbDevice getDevice();

    public List<UsbSerialPort> getPorts();
}
