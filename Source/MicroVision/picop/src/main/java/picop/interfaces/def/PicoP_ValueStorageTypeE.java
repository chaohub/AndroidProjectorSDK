/** \file PicoP_ValueStorageTypeE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enums for types of storage for the value queried on the system
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_ValueStorageTypeE
{
    eCURRENT_VALUE ,			/**< Current value */
    eVALUE_ON_STARTUP,			/**< Value on system startup */
    eFACTORY_VALUE	;			/**< Value set at the Factory */
    
    private int m_Value = 0;
	
    PicoP_ValueStorageTypeE(int value) 
	{
		this.m_Value = value;
	}
	private PicoP_ValueStorageTypeE()
	{
		this.m_Value = 0;
	}
	public int enumtoInt()
	{
		int res = -1;
		switch (this) {
		case eCURRENT_VALUE:
			res = 0;
			break;
		case eVALUE_ON_STARTUP:
			res = 1;
			break;
		case eFACTORY_VALUE:
			res = 2;
			break;
		}
		return res;
	}
	
    public PicoP_ValueStorageTypeE intToEnum(int value)
    {
    	PicoP_ValueStorageTypeE vStorage = eCURRENT_VALUE;
		switch (value) 
		{
		case 0:
			vStorage =  eCURRENT_VALUE;
		break;
			
		case 1:
			vStorage =  eVALUE_ON_STARTUP;
			break;
			
		case 2:
			vStorage =  eFACTORY_VALUE;
			break;
			
		default:
			break;
		}
		return vStorage;
    }
}
