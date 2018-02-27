/** \file PicoP_ConnectionInfoEx .java
* Copyright	: (c)2011 Microvision
*
* Description:  connection information
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_ConnectionInfoEx 
{
	private PicoP_ConnectionTypeE connectionType;  /**< Defines whether the struct contains info for an RS232, USB, or Bluetooth connection */
	
	public PicoP_ConnectionInfoEx(PicoP_ConnectionTypeE connType) 
	{
		setConnectionType(connType);
		switch(connType)
		{
		case eBTH:
			break;
		case eUSB:
			//not implemented
		case eRS232:
			//not implemented
		default:
			break;
		}
	}


	public PicoP_ConnectionTypeE getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(PicoP_ConnectionTypeE connectionType) {
		this.connectionType = connectionType;
	}

}
