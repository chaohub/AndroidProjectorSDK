/** \file PicoP_TestPatternInfoE .java
*
* Description:  Defines enum for Test Patterns
*
* Copyright: (C) 2017 MicroVision
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public enum PicoP_TestPatternInfoE
{
    eCHECKER_BOARD_PATTERN,  /**< 0 Checker board pattern */
    eSPLASH_SCREEN,          /**< 2 Splash screen */
    eGRID_PATTERN_16x12,     /**< 3 Grid Pattern */
    eCROSS_HAIR_PATTERN,     /**< 4 Cross hair in a square */
    eALL_ON,                 /**< 5 Full color specified */
    eALL_OFF,                /**< 6 Blank */
    eNINE_POINT_PATTERN;     /**< 8 Nine rectangles to be 27% of the screen */

    public int enumtoInt()
    {
        int res = 0;
        switch (this)
        {
        case eCHECKER_BOARD_PATTERN:
            res = 0;
            break;
        case eSPLASH_SCREEN:
            res = 2;
            break;
        case eGRID_PATTERN_16x12:
            res = 3;
            break;
        case eCROSS_HAIR_PATTERN:
            res = 4;
            break;
        case eALL_ON:
            res = 5;
            break;
        case eALL_OFF:
            res = 6;
            break;
        case eNINE_POINT_PATTERN:
            res = 8;
            break;
        default:
            break;
        }
        return res;
    }

    public PicoP_TestPatternInfoE inttoEnum(int value)
    {
        PicoP_TestPatternInfoE pattern = eCHECKER_BOARD_PATTERN;
        switch (value)
        {
        case 0:
            pattern = eCHECKER_BOARD_PATTERN;
            break;
        case 2:
            pattern = eSPLASH_SCREEN;
            break;
        case 3:
            pattern = eGRID_PATTERN_16x12;
            break;
        case 4:
            pattern = eCROSS_HAIR_PATTERN;
            break;
        case 5:
            pattern = eALL_ON;
            break;
        case 6:
            pattern = eALL_OFF;
            break;
        case 8:
            pattern = eNINE_POINT_PATTERN;
            break;
        default:
            break;
        }
        return pattern;
    }

    public static boolean invalidTestPattern(PicoP_TestPatternInfoE pattern){
        return (pattern != eCHECKER_BOARD_PATTERN
             && pattern != eSPLASH_SCREEN
             && pattern != eGRID_PATTERN_16x12
             && pattern != eCROSS_HAIR_PATTERN
             && pattern != eALL_ON
             && pattern != eALL_OFF
             && pattern != eNINE_POINT_PATTERN) ? true : false;
    }
}
