/** \file PicoP_LibraryInfo.java
* Copyright	: (c)2011 Microvision
*
* Description:  Library information 
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_LibraryInfo
{
	public 	byte	majorVersion;		/**< Contains the major version of the library */
	public 	byte	minorVersion;		/**< Contains the minor version of the library */
	public 	byte	patchVersion;		/**< Contains the patch version of the library */
	public 	int	capabilityFlags;	/**< Flags that describe the capbility of this version of the ALC library */
}
