/** \file PicoP_InputVideoStateE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for Input Video States
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_InputVideoStateE
{
    eINPUT_VIDEO_DISABLED,		/**< Input video is Disabled */
    eINPUT_VIDEO_ENABLED;		/**< Input video is Enabled */
    
	public int enumtoInt()
	{
		int res =0;
		switch (this)
		{
		case eINPUT_VIDEO_DISABLED:
			res =0;
			break;
		case eINPUT_VIDEO_ENABLED:
			res =1;
			break;
					
		default:
			break;
		}
		return res;
	}
	
    public PicoP_InputVideoStateE intToEnum(int value)
    {
    	PicoP_InputVideoStateE input_video = eINPUT_VIDEO_DISABLED;
		switch (value) 
		{
		case 0:
			input_video =  eINPUT_VIDEO_DISABLED;
		break;
			
		case 1:
			input_video =  eINPUT_VIDEO_ENABLED;
			break;
			
		
		default:
			break;
		}
		return input_video;
    }
}
