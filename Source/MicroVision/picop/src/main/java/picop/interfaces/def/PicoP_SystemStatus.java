/** \file PicoP_SystemStatus.java
* Copyright	: (c)2011 Microvision
*
* Description:  System Status
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_SystemStatus
{
	private int  status;             /**< 32 bit value, 0 is system OK and non-zero value indicates system not OK */
	private int  state;              /**< System State. Bit pattern describing system state.  E.g. ready accept video */
	private float temperature;	     /**< PDE Temperature */
	private int  data0;              /**< Data 0 for future expansion */
	private int  data1;              /**< Data 1 for future expansion */
	private int  data2;              /**< Data 2 for future expansion */
	private int  data3;              /**< Data 3 for future expansion */
	private int  data4;              /**< Data 4 for future expansion */
	
	public void setPicoP_SystemStatus(int l_status,int l_state,float l_temperature,int l_data0,int l_data1,int l_data2,int l_data3,int l_data4)
	{
		this.status      = l_status;
		this.state       = l_state;
		this.temperature = l_temperature;
		this.data0 		= l_data0;
		this.data1	    = l_data1;
		this.data2       = l_data2;
		this.data3       = l_data3;
		this.data4       = l_data4;
	}
	public int getStatus()
	{
		return status;
	}
	public int getState()
	{
		return state;
	}
	public float getTemperature()
	{
		return temperature;
	}
	public int getData0()
	{
		return data0;
	}
	public int getData1()
	{
		return data1;
	}
	public int getData2()
	{
		return data2;
	}
	public int getData3()
	{
		return data3;
	}
	public int getData4()
	{
		return data4;
	}
}
