/** \file PicoP_ColorConvertE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for Color Converter
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_ColorConvertE 
{
	eRED_TO_RED,		/**< Red to Red*/      
	eGREEN_TO_RED,		/**< Green to Red */      
	eBLUE_TO_RED,		/**< Blue to Red */
	eRED_TO_GREEN,		/**< Red to Green */
	eGREEN_TO_GREEN,	/**< Green to Green */
	eBLUE_TO_GREEN,		/**< Blue to Green */
	eRED_TO_BLUE,		/**< Red to Blue */
	eGREEN_TO_BLUE,		/**< Green to Blue */
	eBLUE_TO_BLUE;		/**< Blue to Blue */

	public int enumtoInt()
	{

		int res =0;
		switch (this) {
		case eRED_TO_RED:
			res =0;
			break;
		case eGREEN_TO_RED:
			res =1;
			break;
		case eBLUE_TO_RED:
			res =2;
			break;
		case eRED_TO_GREEN:
			res =3;
			break;
		case eGREEN_TO_GREEN:
			res =4;
			break;
		case eBLUE_TO_GREEN:
			res =5;
			break;
		case eRED_TO_BLUE:
			res =6;
			break;
		case eGREEN_TO_BLUE:
			res =7;
			break;
		case eBLUE_TO_BLUE:
			res =8;
			break;
			
		default:
			break;
		}
		return res;
	}
	
    public PicoP_ColorConvertE intToEnum(int value)
    {
    	PicoP_ColorConvertE color_convert = eRED_TO_RED;
		switch (value) 
		{
		case 0:
			color_convert =  eRED_TO_RED;
		break;
			
		case 1:
			color_convert =  eGREEN_TO_RED;
			break;
			
		case 2:
			color_convert =  eBLUE_TO_RED;
		break;
			
		case 3:
			color_convert =  eRED_TO_GREEN;
			break;
		case 4:
			color_convert =  eGREEN_TO_GREEN;
		break;
			
		case 5:
			color_convert =  eBLUE_TO_GREEN;
			break;
			
		case 6:
			color_convert =  eRED_TO_BLUE;
		break;
			
		case 7:
			color_convert =  eGREEN_TO_BLUE;
			break;
		case 8:
			color_convert =  eBLUE_TO_BLUE;
			break;
			
		default:
			break;
		}
		return color_convert;
    }
	
}
