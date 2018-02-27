/** \file PicoP_VideoColorSpaceE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enums for color space property selection
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_VideoColorSpaceE
{
	    eCOLOR_SPACE_RGB,				/**< Deprecated */
	    eCOLOR_SPACE_YCBCR,				/**< Deprecated */
	    eCOLOR_SPACE_YPBPR,				/**< Deprecated */
	    eCOLOR_SPACE_RGB_DIRECT,
	    eCOLOR_SPACE_REC601_YCBCR,    
	    eCOLOR_SPACE_REC601_BT656,    
	    eCOLOR_SPACE_REC709_YCBCR;		/**< Reserved for Future use */

		public int enumtoInt()
		{
			int res =0;
			switch (this)
			{
			case eCOLOR_SPACE_RGB:
				res =0;
				break;
			case eCOLOR_SPACE_YCBCR:
				res =1;
				break;
			case eCOLOR_SPACE_YPBPR:
				res =2;
				break;
			case eCOLOR_SPACE_RGB_DIRECT:
				res =3;
				break;
			case eCOLOR_SPACE_REC601_YCBCR:
				res =4;
				break;
			case eCOLOR_SPACE_REC601_BT656:
				res =5;
				break;
			case eCOLOR_SPACE_REC709_YCBCR:
				res =6;
				break;
		
			default:
				break;
			}
			return res;
		}
		
	    public PicoP_VideoColorSpaceE intToEnum(int value)
	    {
	    	PicoP_VideoColorSpaceE vColorSpace = eCOLOR_SPACE_RGB;
			switch (value) 
			{
			case 0:
				vColorSpace =  eCOLOR_SPACE_RGB;
			break;
				
			case 1:
				vColorSpace =  eCOLOR_SPACE_YCBCR;
				break;
			case 2:
				vColorSpace =  eCOLOR_SPACE_YPBPR;
			break;
				
			case 3:
				vColorSpace =  eCOLOR_SPACE_RGB_DIRECT;
				break;
			case 4:
				vColorSpace =  eCOLOR_SPACE_REC601_YCBCR;
				break;
			case 5:
				vColorSpace =  eCOLOR_SPACE_REC601_BT656;
			break;
				
			case 6:
				vColorSpace =  eCOLOR_SPACE_REC709_YCBCR;
				break;
				
				
			default:
				break;
			}
			return vColorSpace;
	    }

		public static boolean invalidColorSpace(PicoP_VideoColorSpaceE colorSpace){
			return (colorSpace != eCOLOR_SPACE_RGB
					&& colorSpace != eCOLOR_SPACE_YCBCR
					&& colorSpace != eCOLOR_SPACE_YPBPR
					&& colorSpace != eCOLOR_SPACE_RGB_DIRECT
					&& colorSpace != eCOLOR_SPACE_REC601_YCBCR
					&& colorSpace != eCOLOR_SPACE_REC601_BT656
					&& colorSpace != eCOLOR_SPACE_REC709_YCBCR)?true:false;
		}
}
