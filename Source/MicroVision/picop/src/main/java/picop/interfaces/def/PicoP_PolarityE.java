/** \file PicoP_PolarityE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enums for vsync, hsync and clock polarities
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_PolarityE
{
	ePOLARITY_NEGATIVE,         /**< Negative polarity */                
    ePOLARITY_POSITIVE;			/**< Positive polarity */
 
	public int enumtoInt()
	{
		int res =0;
		switch (this) 
		{
		case ePOLARITY_NEGATIVE:
			res =0;
			break;
		case ePOLARITY_POSITIVE:
			res =1;
					
		default:
			break;
		}
		return res;
	}
	
    public PicoP_PolarityE intToEnum(int value)
    {
    	PicoP_PolarityE pPolarity = ePOLARITY_NEGATIVE;
		switch (value) 
		{
		case 0:
			pPolarity =  ePOLARITY_NEGATIVE;
		break;
			
		case 1:
			pPolarity =  ePOLARITY_POSITIVE;
			break;
	
		default:
			break;
		}
		return pPolarity;
    }
}
