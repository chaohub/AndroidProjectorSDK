/** \file PicoP_Point.java
* Copyright	: (c)2011 Microvision
*
* Description:  A point represents a location in (x,y) display space.
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_Point
{
	public short x;			/**< horizontal x coordinate */
	public short y;			/**< vertical y coordinate */

	public PicoP_Point(){
		this.x = 0;
		this.y = 0;
	}
	public void setPicoP_Point(short l_x,short l_y)
	{
		this.x = l_x;
		this.y = l_y;	
	}
	
	public short getX_value()
	{
		return x;
		
	}
	public short getY_value()
	{
		return y;
		
	}
}
