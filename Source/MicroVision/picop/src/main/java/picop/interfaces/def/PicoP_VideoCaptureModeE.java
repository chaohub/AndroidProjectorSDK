/** \file PicoP_VideoCaptureModeE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Video Capture Modes
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_VideoCaptureModeE 
{
	   eCAPTURE_MODE_848x480,						/**< Built-in Capture Mode for 848x480 */
	   eCAPTURE_MODE_800x480,						/**< Built-in Capture Mode for 800x480 */
	   eCAPTURE_MODE_640x480;						/**< Built-in Capture Mode for 640x480 */
	   
		public int enumtoInt()
		{
			int res =0;
			switch (this)
			{
			case eCAPTURE_MODE_848x480:
				res =0;
				break;
			case eCAPTURE_MODE_800x480:
				res =1;
				break;
			case eCAPTURE_MODE_640x480:
				res =2;
				break;
		
			default:
				break;
			}
			return res;
		}
		
	    public PicoP_VideoCaptureModeE intToEnum(int value)
	    {
	    	PicoP_VideoCaptureModeE vCapture_mode = eCAPTURE_MODE_848x480;
			switch (value) 
			{
			case 0:
				vCapture_mode =  eCAPTURE_MODE_848x480;
			break;
				
			case 1:
				vCapture_mode =  eCAPTURE_MODE_800x480;
				break;
				
			case 2:
				vCapture_mode =  eCAPTURE_MODE_640x480;
				break;
				
			default:
				break;
			}
			return vCapture_mode;
	    }
}
