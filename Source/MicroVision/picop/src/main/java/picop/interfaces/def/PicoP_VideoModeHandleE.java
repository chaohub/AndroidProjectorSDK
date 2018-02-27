/** \file PicoP_VideoModeHandleE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for video mode handle
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_VideoModeHandleE 
{
	  eVideoModeHandle_640x480 (3),
	  eVideoModeHandle_800x480 (8),
	  eVideoModeHandle_848x480 (15),
	  eVideoModeHandle_INVALID (13),
	  eVideoModeHandle_720p    (40),
	  eVideoModeHandle_USER    (41);

		private int m_Value = 0;	
		
		PicoP_VideoModeHandleE(int value) 
		{
			this.m_Value = value;
		}
		
		private PicoP_VideoModeHandleE()
		{
			this.m_Value = 0;
		}
		public void setvalue(int value)
		{
			m_Value = value;
		}
		public int getvalue()
		{
			return m_Value;
		}
		public int enumtoInt()
		{
			int res =0;
			switch (this) 
			{
			case eVideoModeHandle_640x480:
				res =3;
				break;
			case eVideoModeHandle_800x480:
				res =8;
				break;
			case eVideoModeHandle_848x480:
				res =15;
				break;
			case eVideoModeHandle_INVALID:
				res =13;
				break;
			case eVideoModeHandle_720p:
				res =40;
				break;
			case eVideoModeHandle_USER:
				res =41;
				break;
			default:
				break;
			}
			return res;
		}
		
		public PicoP_VideoModeHandleE inttoEnum(int value)
		{
			PicoP_VideoModeHandleE video_mode = eVideoModeHandle_640x480;
			switch (value) 
			{
			case 3:
				video_mode = eVideoModeHandle_640x480;
				break;
			case 8:
				video_mode = eVideoModeHandle_800x480;
				break;
			case 15:
				video_mode = eVideoModeHandle_848x480;
				break;
			case 13:
				video_mode = eVideoModeHandle_INVALID;
				break;
			case 40:
				video_mode = eVideoModeHandle_720p;
				break;
			case 41:
				video_mode = eVideoModeHandle_USER;
				break;
			default:
				break;
			}
				return video_mode;
		}
}
