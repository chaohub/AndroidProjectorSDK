/** \file PicoP_SystemInfo.java
*
* Description: System information
*
* Copyright: (C) 2017 MicroVision
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_SystemInfo
{
    public String serialNumber;     /**< System serial number */
    public int    softwareVersion;  /**< Software version */
    public int    fpgaVersion;      /**< FPGA version */

    public void setPicoP_SystemInfoValues(String serialNumber, int softwareVersion, int fpgaVersion)
    {
        this.serialNumber    = serialNumber;
        this.softwareVersion = softwareVersion;
        this.fpgaVersion     = fpgaVersion;
    }
    public String getSerialNumber()
    {
        return serialNumber;
    }
    public int getSoftwareVersion()
    {
        return softwareVersion;
    }
    public int getFpgaVersion()
    {
        return fpgaVersion;
    }
}
