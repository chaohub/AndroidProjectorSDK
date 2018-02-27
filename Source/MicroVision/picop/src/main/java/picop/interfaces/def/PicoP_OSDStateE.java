/** \file PicoP_OSDStateE.java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for On-Screen Display (OSD) States
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_OSDStateE
{
	   eOSD_DISABLED,			/**< OSD is Disabled */
	   eOSD_ENABLED	;			/**< OSD is Enabled */
	
		public int enumtoInt()
		{

			int res =0;
			switch (this) 
			{
			case eOSD_DISABLED:
				res =0;
				break;
			case eOSD_ENABLED:
				res =1;
				break;
							
			default:
				break;
			}
			return res;
		}
		
	    public PicoP_OSDStateE intToEnum(int value)
	    {
	    	PicoP_OSDStateE osd_state = eOSD_DISABLED;
			switch (value) 
			{
			case 0:
				osd_state =  eOSD_DISABLED;
			break;
				
			case 1:
				osd_state =  eOSD_ENABLED;
				break;
			
			default:
				break;
			}
			return osd_state;
	    }
}
