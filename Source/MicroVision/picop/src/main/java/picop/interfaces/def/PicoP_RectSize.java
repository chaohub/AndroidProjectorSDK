/** \file PicoP_RectSize  .java
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

public class PicoP_RectSize 
{
	public short width;			/**< horizontal width */
	public short height;		/**< vertical height*/

	public PicoP_RectSize(){
		this.width = 0;
		this.height = 0;
	}
	
	public void setPicoP_RectSize(short l_width,short l_height)
	{
		width  = l_width;
		height = l_height;
	}
	
	public short getWidth()
	{
		return width;
	}
	public short getHeight()
	{
		return height;
	}
}
