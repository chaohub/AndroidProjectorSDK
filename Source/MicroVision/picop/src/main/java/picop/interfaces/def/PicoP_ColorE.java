/** \file PicoP_ColorE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enums for Color selection
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_ColorE 
{
    eRED ,					/**< Enumeration definition for Color Red */
    eGREEN,					/**< Enumeration definition for Color Green */
    eBLUE,					/**< Enumeration definition for Color Blue */
    eALL_COLORS	;			/**< Enumeration definition for All 3 colors */
   
	public int enumtoInt()
	{
		int res =0;
		switch (this)
		{
		case eRED:
			res =0;
			break;
		case eGREEN:
			res =1;
			break;
		case eBLUE:
			res =2;
			break;
		case eALL_COLORS:
			res =3;
			break;
					
		default:
			break;
		}
		return res;
	}
	
    public PicoP_ColorE intToEnum(int value)
    {
    	PicoP_ColorE pColor = eRED;
		switch (value) 
		{
		case 0:
			pColor =  eRED;
		break;
		
		case 1:
			pColor =  eGREEN;
		break;
			
		case 2:
			pColor =  eBLUE;
			break;
			
		case 3:
			pColor =  eALL_COLORS;
			break;
			
		default:
			break;
		}
		return pColor;
    }
}
