/** \file PicoP_USBInfo .java
* Copyright	: (c)2011 Microvision
*
* Description:  USB connection information class. 
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

/**
 * @class PicoP_USBInfo 
 *
 * @brief USB connection information class. 
 */

public class PicoP_USBInfo extends PicoP_ConnectionInfo
{
	private int productID;			/**< Product ID */
	private String  serialNumber;	/**< Serial Number */

    public PicoP_USBInfo(PicoP_ConnectionTypeE connectType){
    }
	public void setPicoP_USBInfoValues(int l_productID,String  l_serialNumber)
	{
		this.productID 	 = l_productID;
		this.serialNumber = l_serialNumber;
	}
	
	public int getProductID()
	{
		return productID;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
}
