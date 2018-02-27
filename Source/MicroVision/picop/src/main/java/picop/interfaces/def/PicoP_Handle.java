/** \file PicoP_Handle.java
* Copyright	: (c)2011 Microvision
*
* Description:  All API function require a valid PicoP_HANDLE except MvXXX_OpenLibrary
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

import android.content.Context;

/**
 * @class PicoP_Handle
 *
 * @brief All API function require a valid PicoP_HANDLE except MvXXX_OpenLibrary
 */
public class PicoP_Handle 
{
	private static final String TAG = "PicoP_Handle ";
	PicoP_ConnectionInfoEx connectionInfoEx;  /**< Defines whether the struct contains info for an RS232, USB, or Bluetooth connection */
    PicoP_ConnectionInfo connectInfo;
	int currSequenceNumber;     // this is use to match the command and response

	private int nHandle;
	
	public PicoP_Handle(PicoP_ConnectionTypeE connectType) {
		this.connectInfo = new PicoP_ConnectionInfo(connectType);
		this.nHandle = -1;
		this.currSequenceNumber = 1;
	}
	
	public void setPicoP_HANDLE_Values(int nPicoP_Handle)
	{
		this.nHandle = nPicoP_Handle;
	}
	
	public int getPicoP_HANDLE()
	{
		return nHandle;
	}

	public PicoP_ConnectionInfo getConnectInfo(){ return connectInfo;}

	public void setSequenceNumber (int sequenceNumber){
		currSequenceNumber = sequenceNumber;
		PicoP_Ulog.d(TAG,"set seq "+ currSequenceNumber);
	}

	public int getSequenceNumber(){
		PicoP_Ulog.d(TAG,"get seq "+ currSequenceNumber);
		return currSequenceNumber;
	}
}
