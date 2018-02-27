/** \file PicoP_RS232Info.java
* Copyright	: (c)2011 Microvision
*
* Description:  RS232 connection information class
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_RS232Info extends PicoP_ConnectionInfo
{
	private PicoP_RS232ParityE parity;			//  The parity type
	private String deviceName;					//  Device name
	private String portName; 					//  port name
	private int baudRate;						//  Baud rate
	private int stopBitCount;					//  Stop bit count

	public PicoP_RS232Info()
	{

	}
	public PicoP_RS232Info(PicoP_ConnectionTypeE connectType)
	{
		
	}
	public void setPicoP_RS232InfoValues(PicoP_RS232ParityE l_parity,String l_deviceName,String l_portName,int l_baudRate,int l_stopBitCount)
	{
		this.parity       = l_parity;
		this.deviceName   = l_deviceName;
		this.portName     = l_portName;
		this.baudRate     = l_baudRate;
		this.stopBitCount = l_stopBitCount;
	}
	public PicoP_RS232ParityE getParity()
	{
		return parity;
	}
	public String getDeviceName()
	{
		return deviceName;
		
	}
	public String getPortName()
	{
		return portName;
		
	}
	public int getBaudrate()
	{
		return baudRate;
		
	}
	public int getStopBitCount()
	{
		return stopBitCount;
		
	}
}
