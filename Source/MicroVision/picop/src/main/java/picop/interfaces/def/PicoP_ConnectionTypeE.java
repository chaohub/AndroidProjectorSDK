/** \file PicoP_ConnectionTypeE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Selects the RS232 or USB interface for communication
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/

package picop.interfaces.def;

/**
 * @enum PicoP_ConnectionTypeE
 *
 * @brief Selects the RS232 or USB interface for communication
 */

public enum PicoP_ConnectionTypeE 
{
	eRS232,				/**< RS232 interface */
	eUSB,				/**< USB interface */
	eBTH;				/**< BTH interface */

	public int enumtoInt()
	{
		int res =0;
		switch (this) 
		{
		case eRS232:
			res =0;
			break;
		case eUSB:
			res =1;
			break;
		case eBTH:
			res =2;
			break;
		
		default:
			break;
		}
		return res;
	}
	
    public PicoP_ConnectionTypeE intToEnum(int value)
    {
    	PicoP_ConnectionTypeE connection_type = eRS232;
		switch (value) 
		{
		case 0:
			connection_type =  eRS232;
			break;
			
		case 1:
			connection_type =  eUSB;
			break;
			
		case 2:
			connection_type =  eBTH;
			break;
			
		default:
			break;
		}
		return connection_type;
    }
}
