/** \file PicoP_RenderTargetE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Target rendering buffers for draw functions
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_RenderTargetE 
{
	eFRAME_BUFFER_0,		/**< FrameBuffer 0 */
	eFRAME_BUFFER_1,		/**< FrameBuffer 1 */
	eFRAME_BUFFER_2,		/**< FrameBuffer 2 */
	eOSD_0,					/**< On Screen Display 0 */
	eOSD_1;					/**< On Screen Display 1*/
	private int m_Value = 0;
	
	PicoP_RenderTargetE(int value) 
	{
		this.m_Value = value;
	}
	private PicoP_RenderTargetE() 
	{
		this.m_Value = 0;
	}
	public void setvalue(int value)
	{
		this.m_Value = value;
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
		case eFRAME_BUFFER_0:
			res =0;
			break;
		case eFRAME_BUFFER_1:
			res =1;
			break;
		case eFRAME_BUFFER_2:
			res =2;
			break;
		case eOSD_0:
			res =3;
			break;
		case eOSD_1:
			res =4;
			break;
			
		default:
			break;
		}
		return res;
	}
	
    public PicoP_RenderTargetE intToEnum(int value)
    {
    	PicoP_RenderTargetE rTarget = eFRAME_BUFFER_0;
		switch (value) 
		{
		case 0:
			rTarget =  eFRAME_BUFFER_0;
		break;
			
		case 1:
			rTarget =  eFRAME_BUFFER_1;
			break;
			
		case 2:
			rTarget =  eFRAME_BUFFER_2;
			break;
			
		case 3:
			rTarget =  eOSD_0;
			break;
		case 4:
			rTarget =  eOSD_1;
			break;
				
			
		default:
			break;
		}
		return rTarget;
    }

	public static boolean invalidRenderTarget(PicoP_RenderTargetE renderTarget){
		return (renderTarget != eFRAME_BUFFER_0
				&& renderTarget != eFRAME_BUFFER_1
				&& renderTarget != eFRAME_BUFFER_2
				&& renderTarget != eOSD_0
				&& renderTarget != eOSD_1)?true:false;
	}
}
