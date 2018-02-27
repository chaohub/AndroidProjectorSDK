/** \file PicoP_Event .java
* Copyright	: (c)2011 Microvision
*
* Description:  System Event 
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_Event 
{
	private short session;		/**< session number when event occureed */
	private short eventId;		/**< Event ID */
	private int time;			/**< Event time stamp */
	private int data;			/**< Event data */
	private short cid;			/**< Event component ID */
	private short line;			/**< Event line number */

	public PicoP_Event(){
		this.session = 0;
		this.eventId = 0;
		this.time    = 0;
		this.data    = 0;
		this.cid     = 0;
		this.line    = 0;
	}
	
	public void setPicoP_EventValues(short l_session,short l_eventId,int l_time,int l_data,short l_cid,short l_line)
	{
		this.session = l_session;
		this.eventId = l_eventId;
		this.time    = l_time;
		this.data    = l_data;
		this.cid     = l_cid;
		this.line    = l_line;
	}
	
	public short getSession()
	{
		return session;	
	}
	
	public short getEventId()
	{
		return eventId;	
	}
	public int getTime()
	{
		return time;
	}
	public int getData()
	{
		return data;
	}
	public short getCid()
	{
		return cid;
	}
	public short getLine()
	{
		return line;
	}
}
