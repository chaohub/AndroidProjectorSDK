/** \file PicoP_OutputVideoStateE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for Output Video States
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_OutputVideoStateE
{
	 	eOUTPUT_VIDEO_DISABLED,	/**< Output video is Disabled */
	 	eOUTPUT_VIDEO_ENABLED;		/**< Output video is Enabled */

		public int enumtoInt()
		{
			int res =0;
			switch (this)
			{
			case eOUTPUT_VIDEO_DISABLED:
				res =0;
				break;
			case eOUTPUT_VIDEO_ENABLED:
				res =1;
				break;
			
				
			default:
				break;
			}
			return res;
		}
		
	    public PicoP_OutputVideoStateE intToEnum(int value)
	    {
	    	PicoP_OutputVideoStateE oVideo_state = eOUTPUT_VIDEO_DISABLED;
			switch (value) 
			{
			case 0:
				oVideo_state =  eOUTPUT_VIDEO_DISABLED;
			break;
				
			case 1:
				oVideo_state =  eOUTPUT_VIDEO_ENABLED;
				break;
			
				
			default:
				break;
			}
			return oVideo_state;
	    }
}
