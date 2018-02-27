/** \file PicoP_AspectRatioModeE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Aspect Ratio Modes - Display Output Scaling.
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_AspectRatioModeE 
{
	eASPECT_RATIO_NORMAL,			/**< Normal Mode */                
    eASPECT_RATIO_WIDESCREEN,		/**< Widescreen Mode, 16:9 aspect ratio. */
    eASPECT_RATIO_ZOOM_HORIZONTAL,	/**< Zoom To Fit Horizontally, retaining aspect ratio */
    eASPECT_RATIO_ZOOM_VERTICAL,	/**< Zoom To Fit Vertically, retaining aspect ratio */
    eASPECT_RATIO_ZOOM_ANAMORPHIC;	/**< Zoom To Fit Horizonally and Vertically, regardless of original aspect ratio */
	
	public int enumtoInt()
	{
		int res =0;
		switch (this) {
		case eASPECT_RATIO_NORMAL:
			res =0;
			break;
		case eASPECT_RATIO_WIDESCREEN:
			res =1;
			break;
		case eASPECT_RATIO_ZOOM_HORIZONTAL:
			res =2;
			break;
		case eASPECT_RATIO_ZOOM_VERTICAL:
			res =3;
			break;
		case eASPECT_RATIO_ZOOM_ANAMORPHIC:
			res =4;
			break;
			
		default:
			break;
		}
		return res;
	}
	
    public PicoP_AspectRatioModeE intToEnum(int value)
    {
    	PicoP_AspectRatioModeE asp_ratio = eASPECT_RATIO_NORMAL;
		switch (value) 
		{
		case 0:
			asp_ratio =  eASPECT_RATIO_NORMAL;
		break;
			
		case 1:
			asp_ratio =  eASPECT_RATIO_WIDESCREEN;
			break;
			
		case 2:
			asp_ratio =  eASPECT_RATIO_ZOOM_HORIZONTAL;
			break;
			
		case 3:
			asp_ratio =  eASPECT_RATIO_ZOOM_VERTICAL;
			break;
			
		case 4:
			asp_ratio =  eASPECT_RATIO_ZOOM_ANAMORPHIC;
			break;
					
		default:
			break;
		}
		return asp_ratio;
    }
}
