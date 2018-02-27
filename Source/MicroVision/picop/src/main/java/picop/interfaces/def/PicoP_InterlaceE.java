/** \file PicoP_InterlaceE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enums for interlace property selection
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_InterlaceE
{
	eINTERLACE_NONE,
    eINTERLACE_EVEN_FIELD,
    eINTERLACE_ODD_FIELD;
	
	public int enumtoInt()
	{
		int res =0;
		switch (this)
		{
		case eINTERLACE_NONE:
			res =0;
			break;
		case eINTERLACE_EVEN_FIELD:
			res =1;
			break;
		case eINTERLACE_ODD_FIELD:
			res =2;
			break;
					
		default:
			break;
		}
		return res;
	}
	
    public PicoP_InterlaceE intToEnum(int value)
    {
    	PicoP_InterlaceE p_interface = eINTERLACE_NONE;
		switch (value) 
		{
		case 0:
			p_interface =  eINTERLACE_NONE;
		break;
			
		case 1:
			p_interface =  eINTERLACE_EVEN_FIELD;
			break;
			
		case 2:
			p_interface =  eINTERLACE_ODD_FIELD;
			break;
			
		default:
			break;
		}
		return p_interface;
    }
}
