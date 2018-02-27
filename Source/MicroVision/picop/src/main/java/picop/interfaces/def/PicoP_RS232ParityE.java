/** \file PicoP_RS232ParityE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enums for RS232 parity selection
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_RS232ParityE
{
	eRS232ParityNone,			/**< No parity required */
	eRS232ParityEven,			/**< Even parity required */
	eRS232ParityOdd	;			/**< Odd parity required */

	public int enumtoInt()
	{
		int res =0;
		switch (this) 
		{
		case eRS232ParityNone:
			res =0;
			break;
		case eRS232ParityEven:
			res =1;
			break;
		case eRS232ParityOdd:
			res =2;
			break;
		
		default:
			break;
		}
		return res;
	}
	
    public PicoP_RS232ParityE intToEnum(int value)
    {
    	PicoP_RS232ParityE rs232_parity = eRS232ParityNone;
		switch (value) 
		{
		case 0:
			rs232_parity =  eRS232ParityNone;
		break;
			
		case 1:
			rs232_parity =  eRS232ParityEven;
			break;
			
		case 2:
			rs232_parity =  eRS232ParityOdd;
			break;
			
		default:
			break;
		}
		return rs232_parity;
    }
}
