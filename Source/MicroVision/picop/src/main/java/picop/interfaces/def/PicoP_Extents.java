/** \file PicoP_Extents.java
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

public class PicoP_Extents 
{
	private short width;			/**< horizontal x extents */
	private short height;		/**< vertical y extents */

	public PicoP_Extents(){
		this.width = 0;
		this.height = 0;
	}

	public void setPicoP_ExtentsValues(short l_width, short l_height) 
	{
		this.width 	= l_width;
		this.height = l_height;
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
