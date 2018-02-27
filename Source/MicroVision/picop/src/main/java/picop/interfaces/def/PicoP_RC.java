/**
* \file
*
* \brief Defines enum for picop reply.
*
* Copyright (C) 2017 MicroVision, Inc.
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

import android.R.string;

public enum PicoP_RC
{
    eRECONNECT_FAILED   (-13),  /**< Reconnecting to MCU reboot failed */
    eCONNECT_FAILED     (-12),  /**< Connecting to MCU reboot failed */
    ePAYLOAD_TOO_LARGE  (-11),  /**< Payload too large */
    eNOT_CONNECTED      (-10),  /**< Not connected */
    eALREADY_OPENED     (-9),   /**< Connection already open */
    eINCOMPLETE         (-8),   /**< operation could not be completed */
    eINIT_FAILURE       (-7),   /**< Failed to initialize*/
    eINVALID_ARG        (-6),   /**< One or more arguements are invalid */
    eUNINITIALIZED      (-5),   /**< Object is not initialized */
    eTIMEOUT            (-4),   /**< Operation Timed out */
    eBUSY               (-3),   /**< Resource Busy */
    eNOT_SUPPORTED      (-2),   /**< Operation not supported */
    eFAILURE            (-1),   /**< Call Failed */
    eSUCCESS             (0),   /**< Call Successful */
    eCOMMUNICATION_ERROR (1),   /**< Error while trying to communicate */
    eBROKEN_CONNECTION   (2),   /**< A previously opened connection is no longer valid */
    eNOT_INITIALIZED     (3),   /**< API or library not initialized */
    eINTERNAL_ERROR      (4),   /**< Internal unspecified error */
    eDEVICE_ERROR        (5),   /**< Error reading or writing to an I/O device */
    eRESOURCE_BUSY       (6),   /**< Resource is not available */
    eRETRY               (7),   /**< Retry if PDE uart overflows */
    eBTH_DISABLED        (8);

    private int m_Value = 0;

    PicoP_RC(int value)
    {
        this.m_Value = value;
    }

    private PicoP_RC()
    {
        this.m_Value = 0;
    }

    public int getvalue()
    {
        return m_Value;
    }

    public int enumtoInt(PicoP_RC value)
    {
        int res =0;
        switch (value)
        {
        case eRECONNECT_FAILED:
            res = -13;
            break;
        case eCONNECT_FAILED:
            res = -12;
            break;
        case ePAYLOAD_TOO_LARGE:
            res = -11;
            break;
        case eNOT_CONNECTED:
            res = -10;
            break;
        case eALREADY_OPENED:
            res = -9;
            break;
        case eINCOMPLETE:
            res = -8;
            break;
        case eINIT_FAILURE:
            res = -7;
            break;
        case eINVALID_ARG:
            res = -6;
            break;
        case eUNINITIALIZED:
            res = -5;
            break;
        case eTIMEOUT:
            res = -4;
            break;
        case eBUSY:
            res = -3;
            break;
        case eNOT_SUPPORTED:
            res = -2;
            break;
        case eFAILURE:
            res = -1;
            break;
        case eSUCCESS:
            res = 0;
            break;
        case eCOMMUNICATION_ERROR:
            res = 1;
            break;
        case eBROKEN_CONNECTION:
            res = 2;
            break;
        case eNOT_INITIALIZED:
            res = 3;
            break;
        case eINTERNAL_ERROR:
            res = 4;
            break;
        case eDEVICE_ERROR:
            res = 5;
            break;
        case eRESOURCE_BUSY:
            res = 6;
            break;
        case eRETRY:
            res = 7;
            break;
        case eBTH_DISABLED:
            res = 8;
            break;
        default:
            break;
        }
        return res;
    }

    public PicoP_RC inttoEnum(int value)
    {
        PicoP_RC rc = eFAILURE;
        switch (value)
        {
        case -13:
            rc = eRECONNECT_FAILED;
            break;
        case -12:
            rc = eCONNECT_FAILED;
            break;
        case -11:
            rc = ePAYLOAD_TOO_LARGE;
            break;
        case -10:
            rc = eNOT_CONNECTED;
            break;
        case -9:
            rc = eALREADY_OPENED;
            break;
        case -8:
            rc = eINCOMPLETE;
            break;
        case -7:
            rc = eINIT_FAILURE;
            break;
        case -6:
            rc = eINVALID_ARG;
            break;
        case -5:
            rc = eUNINITIALIZED;
            break;
        case -4:
            rc = eTIMEOUT;
            break;
        case -3:
            rc = eBUSY;
            break;
        case -2:
            rc = eNOT_SUPPORTED;
            break;
        case -1:
            rc = eFAILURE;
            break;
        case 0:
            rc = eSUCCESS;
            break;
        case 1:
            rc = eCOMMUNICATION_ERROR;
            break;
        case 2:
            rc = eBROKEN_CONNECTION;
            break;
        case 3:
            rc = eNOT_INITIALIZED;
            break;
        case 4:
            rc = eINTERNAL_ERROR;
            break;
        case 5:
            rc = eDEVICE_ERROR;
            break;
        case 6:
            rc = eRESOURCE_BUSY;
            break;
        case 7:
            rc = eRETRY;
            break;
        case 8:
            rc = eBTH_DISABLED;
            break;
        default:
            break;
        }
        return rc;
    }

    public String PicoP_RC_To_String(PicoP_RC rc)
    {
        String ret = "";
        switch (rc)
        {
        case eRECONNECT_FAILED:
            ret = "eRECONNECT_FAILED";
            break;
        case eCONNECT_FAILED:
            ret = "eCONNECT_FAILED";
            break;
        case ePAYLOAD_TOO_LARGE:
            ret = "ePAYLOAD_TOO_LARGE";
            break;
        case eNOT_CONNECTED:
            ret = "eNOT_CONNECTED";
            break;
        case eALREADY_OPENED:
            ret = "eALREADY_OPENED";
            break;
        case eINCOMPLETE:
            ret = "eINCOMPLETE";
            break;
        case eINIT_FAILURE:
            ret = "eINIT_FAILURE";
            break;
        case eINVALID_ARG:
            ret = "eINVALID_ARG";
            break;
        case eUNINITIALIZED:
            ret = "eUNINITIALIZED";
            break;
        case eTIMEOUT:
            ret = "eTIMEOUT";
            break;
        case eBUSY:
            ret = "eBUSY";
            break;
        case eNOT_SUPPORTED:
            ret = "eNOT_SUPPORTED";
            break;
        case eSUCCESS:
            ret = "eSUCCESS";
            break;
        case eFAILURE:
            ret = "eFAILURE";
            break;
        case eCOMMUNICATION_ERROR:
            ret = "eCOMMUNICATION_ERROR";
            break;
        case eBROKEN_CONNECTION:
            ret = "eBROKEN_CONNECTION";
            break;
        case eNOT_INITIALIZED:
            ret = "eNOT_INITIALIZED";
            break;
        case eINTERNAL_ERROR:
            ret = "eINTERNAL_ERROR";
            break;
        case eDEVICE_ERROR:
            ret = "eDEVICE_ERROR";
            break;
        case eRESOURCE_BUSY:
            ret = "eRESOURCE_BUSY";
            break;
        case eRETRY:
            ret = "eRETRY";
            break;
        case eBTH_DISABLED:
            ret = "eBTH_DISABLED";
            break;

        default:
            break;
        }
        return ret;
    }
}
