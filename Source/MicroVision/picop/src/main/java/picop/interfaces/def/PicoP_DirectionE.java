/** \file PicoP_DirectionE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for Directions
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_DirectionE 
{
	eHORIZONTAL,			/**< Horizontal direction */      
	eVERTICAL,				/**< Vertical direction */      
	eBOTH;					/**< Both directions */

	public int enumtoInt()
	{
		int res =0;
		switch (this) 
		{
		case eHORIZONTAL:
			res =0;
			break;
		case eVERTICAL:
			res =1;
			break;
		case eBOTH:
			res =2;
			break;
		
			
		default:
			break;
		}
		return res;
	}
	
    public PicoP_DirectionE intToEnum(int value)
    {
    	PicoP_DirectionE direction = eHORIZONTAL;
		switch (value) 
		{
		case 0:
			direction =  eHORIZONTAL;
		break;
			
		case 1:
			direction =  eVERTICAL;
			break;
		case 2:
			direction =  eBOTH;
		break;
		
			
		default:
			break;
		}
		return direction;
    }
    
}
