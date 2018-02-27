/** \file PicoP_FlipStateE .java
* Copyright	: (c)2011 Microvision
*
* Description:  Defines enum for FlipState
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_FlipStateE 
{
	eFLIP_NEITHER,
	eFLIP_HORIZONTAL ,			/**< Horizontal Flip */      
	eFLIP_VERTICAL,				/**< Vertical Flip */      
	eFLIP_BOTH	;				/**< Both directions */
	private int m_Value = 0;
	
	PicoP_FlipStateE(int value) 
	{
		this.m_Value = value;
	}
	private PicoP_FlipStateE() 
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
		case eFLIP_NEITHER:
			res =0;
			break;
		case eFLIP_HORIZONTAL:
			res =1;
			break;
		case eFLIP_VERTICAL:
			res =2;
			break;
		case eFLIP_BOTH:
			res =3;
			break;
		
		default:
			break;
		}
		return res;
	}
	
    public PicoP_FlipStateE intToEnum(int value)
    {
    	PicoP_FlipStateE flip_state = eFLIP_NEITHER;
		switch (value) 
		{
		case 0:
			flip_state =  eFLIP_NEITHER;
		break;
			
		case 1:
			flip_state =  eFLIP_HORIZONTAL;
			break;
		case 2:
			flip_state =  eFLIP_VERTICAL;
		break;
			
		case 3:
			flip_state =  eFLIP_BOTH;
			break;
			
			
		default:
			break;
		}
		return flip_state;
    }
}
