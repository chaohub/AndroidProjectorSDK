/** \file PicoP_ColorModeE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for Color Modes
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

/**
 * @enum PicoP_ColorModeE 
 *
 * @brief Color Modes
 */

public enum PicoP_ColorModeE 
{
    eCOLOR_MODE_BRILLIANT,      /**< Brilliant color mode */                
    eCOLOR_MODE_STANDARD,		/**< Standard Color mode */
    eCOLOR_MODE_INVERTED;		/**< Inverted Color Mode */
  
	public int enumtoInt()
	{
		int res =0;
		switch (this)
		{
		case eCOLOR_MODE_BRILLIANT:
			res =0;
			break;
		case eCOLOR_MODE_STANDARD:
			res =1;
			break;
		case eCOLOR_MODE_INVERTED:
			res =2;
			break;
					
		default:
			break;
		}
		return res;
	}
	
    public PicoP_ColorModeE intToEnum(int value)
    {
    	PicoP_ColorModeE color_mode = eCOLOR_MODE_BRILLIANT;
		switch (value) 
		{
		case 0:
			color_mode =  eCOLOR_MODE_BRILLIANT;
		break;
			
		case 1:
			color_mode =  eCOLOR_MODE_STANDARD;
			break;
			
		case 2:
			color_mode =  eCOLOR_MODE_INVERTED;
			break;
			
		default:
			break;
		}
		return color_mode;
    }
}
