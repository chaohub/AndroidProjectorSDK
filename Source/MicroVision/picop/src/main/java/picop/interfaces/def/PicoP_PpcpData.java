package picop.interfaces.def;

/**
 * Created by carey.wang on 2017/3/30.
 */

public class PicoP_PpcpData {

    /* { sequence number start */
    /* Max sequence number is 0x3f */
    public static final int MAX_SEQNUM = 63;
    /* sequence number end } */

    /* { picop ppcp msg start code start */
    /* All the msg start with the same header 0xc0*/
    public static final int PICOP_PPCP_MSG_START_CODE = 192;

    /* Return the header with a byte[]. */
    public static byte[] getStartCodeByByte(int start_code){
        String hex = Integer.toBinaryString(start_code);
        if(hex.length()<8){
            hex = "0000" + hex;
        }
        byte[] startcode = hex.getBytes();
        return startcode;
    }

    /* Return the header with a binary string. */
    public static String getStartCodeByString(int start_code, int lenth){
        String bin = Integer.toBinaryString(start_code);
        while(bin.length()<lenth){
            bin = "0" + bin;
        }
        return bin;
    }
    /* picop ppcp msg start code end } */

    /* { Valid device identifiers for source and destination addresses start */
    public static final int ePDE_DEVICE          = 0;    // device id = 0
    public static final int eINVALID_DEVICE      = 1;    // device id = 1
    public static final int eHOST_DEVICE         = 2;    // device id = 2
    public static final int eMCU_DEVICE          = 3;    // device id = 3
    public static final int eVERDE_CONSOLE       = 4;    // device id = 4
    public static final int eDEBUG_OUTPUT        = 5;    // device id = 5
    public static final int eTEST                = 6;    // device id = 6
    public static final int eBROADCAST_DEVICE_ID = 0x3f; // devuce id = 3f

    /* Return the device id with a byte[]. */
    public static byte[] getDeviceIdByByte(int device_id){
        String hex = Integer.toBinaryString(device_id);
        if(hex.length()<8){
            hex = "0000"+hex;
        }
        byte[] deviceid = hex.getBytes();
        return deviceid;
    }
    /* Return the device id with a binary string. */
    public static String getDeviceIdByString(int device_id, int lenth){
        String bin = Integer.toHexString(device_id);
        while(bin.length()<lenth){
            bin = "0"+bin;
        }
        return bin;
    }
    /* Return the source id with a byte[]. */
    public static byte[] getSourceIdByByte(int source_id){
        String hex = Integer.toBinaryString(source_id);
        while(hex.length()<8){
            hex = "0"+hex;
        }
        byte[] sourceid = hex.getBytes();
        return sourceid;
    }
    /* Return the source id with a binary string. */
    public static String getSourceIdByString(int source_id, int lenth){
        String bin = Integer.toBinaryString(source_id);
        while(bin.length()<lenth){
            bin = "0"+bin;
        }
        return bin;
    }
    /* Valid device identifiers for source and destination addresses end } */

    /* { protocol ids start */
    public static final int ePROTOCOL_CONNECTIONLESS      = 0;
    public static final int ePROTOCOL_CONNECTION_ORIENTED = 1;
    public static final int ePROTOCOL_CONNECTION_EXPANDED = 2;
    public static final int ePROTOCOL_RESERVED_3          = 3;
    public static final int ePROTOCOL_INVALID             = 4;

    /* Return the protocol id with a byte[]. */
    public static byte[] getProtocolIdByByte(int protocol_id){
        String hex = Integer.toBinaryString(protocol_id);
        byte[] protocolid = hex.getBytes();
        return protocolid;
    }
    /* Return the protocol id with a binary string. */
    public static String getProtocolIdByString(int protocol_id){
        String hex = Integer.toHexString(protocol_id);
        return hex;
    }
    /* protocol ids end }*/

    /* { function ids start */
    public static final int eHOST_COMM_MANAGER_FID = 29; //0x1d

    /* Return the function id with a byte[]. */
    public static byte[] getFunctionIdByByte(int function_id){
        String hex = Integer.toBinaryString(function_id);
        byte[] functionid = hex.getBytes();
        return functionid;
    }
    /* Return the function id with a binary string. */
    public static String getFunctionIdByString(int function_id){
        String bin = Integer.toBinaryString(function_id);
        while (bin.length()<8){
            bin = "0"+bin;
        }
        return bin;
    }
    /* function ids end } */

    /* { payload flag start */
    public static final int eDATA_MESSAGE = 0;
    public static final int eACT          = 1;
    public static final int eNACK         = 2;
    public static final int eRESET        = 3;

    /* Return the payload flag with a byte[]. */
    public static byte[] getPayloadFlagByByte(int flag){
        String hex = Integer.toBinaryString(flag);
        byte[] flags = hex.getBytes();
        return flags;
    }
    /* Return the payload flag with a binary string. */
    public static String getPayloadFlagByString(int flag){
        String hex = Integer.toHexString(flag);
        return hex;
    }
    /* payload flag end }*/

    /* { Command identifiers start */
    public static final int PICOP_PPCPDATA_BASE_COMMAND_ID                      = 8;
    public static final int eHCM_SET_BRIGHTNESS_VAL                             = PICOP_PPCPDATA_BASE_COMMAND_ID + 1;       // 9
    public static final int eHCM_GET_BRIGHTNESS_VAL                             = PICOP_PPCPDATA_BASE_COMMAND_ID + 2;       // 10
    public static final int eHCM_SET_ASPECT_RATIO_MODE                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 3;       // 11
    public static final int eHCM_GET_ASPECT_RATIO_MODE                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 4;       // 12
    public static final int eHCM_SET_COLOR_MODE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 5;       // 13
    public static final int eHCM_GET_COLOR_MODE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 6;       // 14
    public static final int eHCM_SET_GAMMA_VAL                                  = PICOP_PPCPDATA_BASE_COMMAND_ID + 7;       // 15
    public static final int eHCM_GET_GAMMA_VAL                                  = PICOP_PPCPDATA_BASE_COMMAND_ID + 8;       // 16
    public static final int eHCM_SET_PHASE                                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 9;       // 17
    public static final int eHCM_GET_PHASE                                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 10;      // 18
    public static final int eHCM_FLIP_IMAGE                                     = PICOP_PPCPDATA_BASE_COMMAND_ID + 11;      // 19
    public static final int eHCM_WARP_IMAGE                                     = PICOP_PPCPDATA_BASE_COMMAND_ID + 12;      // 20
    public static final int eHCM_SET_OUTPUT_VIDEO_STATE                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 13;      // 21
    public static final int eHCM_GET_OUTPUT_VIDEO_STATE                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 14;      // 22
    public static final int eHCM_CORRECT_KEYSTONE                               = PICOP_PPCPDATA_BASE_COMMAND_ID + 15;      // 23
    public static final int eHCM_CORRECT_VERTICAL_DISTORTION                    = PICOP_PPCPDATA_BASE_COMMAND_ID + 16;      // 24
    public static final int eHCM_SET_AUTO_BRIGHTNESS_STATE                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 17;      // 25
    public static final int eHCM_GET_AUTO_BRIGHTNESS_STATE                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 18;      // 26
    public static final int eHCM_SET_MAX_AUTO_BRIGHTNESS                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 19;      // 27
    public static final int eHCM_GET_MAX_AUTO_BRIGHTNESS                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 20;      // 28
    public static final int eHCM_SET_MIN_AUTO_BRIGHTNESS                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 21;      // 29
    public static final int eHCM_GET_MIN_AUTO_BRIGHTNESS                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 22;      // 30
    public static final int eHCM_SET_ABC_UP_SLEWRATE                            = PICOP_PPCPDATA_BASE_COMMAND_ID + 23;      // 31
    public static final int eHCM_GET_ABC_UP_SLEWRATE                            = PICOP_PPCPDATA_BASE_COMMAND_ID + 24;      // 32
    public static final int eHCM_SET_ABC_DOWN_SLEWRATE                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 25;      // 33
    public static final int eHCM_GET_ABC_DOWN_SLEWRATE                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 26;      // 34
    public static final int eHCM_SET_INPUT_CAPTURE_MODE_INFO                    = PICOP_PPCPDATA_BASE_COMMAND_ID + 27;      // 35
    public static final int eHCM_GET_INPUT_CAPTURE_MODE_INFO                    = PICOP_PPCPDATA_BASE_COMMAND_ID + 28;      // 36
    public static final int eHCM_SET_ACTIVE_CAPTURE_MODE                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 29;      // 37
    public static final int eHCM_GET_ACTIVE_CAPTURE_MODE                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 30;      // 38
    public static final int eHCM_GET_INPUT_VIDEO_PROPERTIES                     = PICOP_PPCPDATA_BASE_COMMAND_ID + 31;      // 39
    public static final int eHCM_SET_INPUT_VIDEO_STATE                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 32;      // 40
    public static final int eHCM_GET_INPUT_VIDEO_STATE                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 33;      // 41
    public static final int eHCM_SET_ACTIVE_FRAME_BUFFER                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 34;      // 42
    public static final int eHCM_GET_ACTIVE_FRAME_BUFFER                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 35;      // 43
    public static final int eHCM_DRAW_TEST_PATTERN                              = PICOP_PPCPDATA_BASE_COMMAND_ID + 36;      // 44
    public static final int eHCM_DRAW_TEXT                                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 37;      // 45
    public static final int eHCM_GET_TEXT_BOX_INFO                              = PICOP_PPCPDATA_BASE_COMMAND_ID + 38;      // 46
    public static final int eHCM_DRAW_POINT                                     = PICOP_PPCPDATA_BASE_COMMAND_ID + 39;      // 47
    public static final int eHCM_DRAW_LINE                                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 40;      // 48
    public static final int eHCM_DRAW_TRIANGLE                                  = PICOP_PPCPDATA_BASE_COMMAND_ID + 41;      // 49
    public static final int eHCM_DRAW_RECTANGLE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 42;      // 50
    public static final int eHCM_RENDER                                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 43;      // 51
    public static final int eHCM_SET_OSD_STATE                                  = PICOP_PPCPDATA_BASE_COMMAND_ID + 44;      // 52
    public static final int eHCM_GET_OSD_STATE                                  = PICOP_PPCPDATA_BASE_COMMAND_ID + 45;      // 53
    public static final int eHCM_SET_OSD_INFO                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 46;      // 54
    public static final int eHCM_GET_OSD_INFO                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 47;      // 55
    public static final int eHCM_GET_DISPLAY_INFO                               = PICOP_PPCPDATA_BASE_COMMAND_ID + 48;      // 56
    public static final int eHCM_LOAD_BITMAP_IMAGE                              = PICOP_PPCPDATA_BASE_COMMAND_ID + 49;      // 57
    public static final int eHCM_GET_SYSTEM_STATUS                              = PICOP_PPCPDATA_BASE_COMMAND_ID + 50;      // 58
    public static final int eHCM_GET_SYSTEM_INFO                                = PICOP_PPCPDATA_BASE_COMMAND_ID + 51;      // 59
    public static final int eHCM_GET_EVENT_LOG                                  = PICOP_PPCPDATA_BASE_COMMAND_ID + 52;      // 60
    public static final int eHCM_SW_UPGRADE                                     = PICOP_PPCPDATA_BASE_COMMAND_ID + 53;      // 61
    public static final int eHCM_FPGA_UPGRADE                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 54;      // 62
    public static final int eHCM_SAVE_AS_SPLASH                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 55;      // 63
    public static final int eHCM_RESTORE_FACTORY_CONFIG                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 56;      // 64
    public static final int eHCM_SET_SYSTEM_POWER_STATE                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 57;      // 65
    public static final int eHCM_GET_SYSTEM_POWER_STATE                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 58;      // 66
    public static final int eHCM_GET_BATTERY_STATUS                             = PICOP_PPCPDATA_BASE_COMMAND_ID + 59;      // 67     // do not delete, it could mess up Poogle
    public static final int eHCM_KEY_PRESS                                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 60;      // 68
    public static final int eHCM_AUTO_SET_GREEN_MAG_BAL                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 61;      // 69
    public static final int eHCM_MANUAL_SET_GREEN_MAG_BAL                       = PICOP_PPCPDATA_BASE_COMMAND_ID + 62;      // 70
    public static final int eHCM_MANUAL_GET_GREEN_MAG_BAL                       = PICOP_PPCPDATA_BASE_COMMAND_ID + 63;      // 71
    public static final int eHCM_SET_COLOR_ALIGNMENT                            = PICOP_PPCPDATA_BASE_COMMAND_ID + 64;      // 72
    public static final int eHCM_GET_COLOR_ALIGNMENT                            = PICOP_PPCPDATA_BASE_COMMAND_ID + 65;      // 73
    public static final int eHCM_GET_INPUT_DELTA                                = PICOP_PPCPDATA_BASE_COMMAND_ID + 66;      // 74
    public static final int eHCM_SET_INPUT_DELTA_THRESHOLD                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 67;      // 75
    public static final int eHCM_SET_ACTIVE_OSD                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 68;      // 76
    public static final int eHCM_GET_ACTIVE_OSD                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 69;      // 77
    public static final int eHCM_SET_SERRATION_MODE                             = PICOP_PPCPDATA_BASE_COMMAND_ID + 70;      // 78
    public static final int eHCM_CLEAR_TARGET                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 71;      // 79
    public static final int eHCM_GET_AUTO_GREEN_MAG_BAL_STATUS                  = PICOP_PPCPDATA_BASE_COMMAND_ID + 72;      // 80
    public static final int eHCM_SETUP_WARP                                     = PICOP_PPCPDATA_BASE_COMMAND_ID + 73;      // 81
    public static final int eHCM_SET_COLOR_CONVERTER                            = PICOP_PPCPDATA_BASE_COMMAND_ID + 74;      // 82
    public static final int eHCM_GET_COLOR_CONVERTER                            = PICOP_PPCPDATA_BASE_COMMAND_ID + 75;      // 83
    public static final int eHCM_SET_SCAN_ANGLE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 76;      // 84
    public static final int eHCM_GET_SCAN_ANGLE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 77;      // 85
    public static final int eHCM_SET_WARP_STATE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 78;      // 86
    public static final int eHCM_GET_WARP_STATE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 79;      // 87
    public static final int eHCM_MODIFY_INPUT_CAPTURE_MODE_INFO                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 80;      // 88
    public static final int eHCM_COMMIT_CAPTURE_MODE_INFO                       = PICOP_PPCPDATA_BASE_COMMAND_ID + 81;      // 89
    public static final int eHCM_GET_COMMITED_CAPTURE_MODE                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 82;      // 90
    public static final int eHCM_SET_FLIP_STATE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 83;      // 91
    public static final int eHCM_GET_FLIP_STATE                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 84;      // 92
    public static final int eHCM_SET_RETROTOUCH_STATE                           = PICOP_PPCPDATA_BASE_COMMAND_ID + 85;      // 93
    public static final int eHCM_GET_RETROTOUCH_STATE                           = PICOP_PPCPDATA_BASE_COMMAND_ID + 86;      // 94
    public static final int eHCM_SET_RETROTOUCH_CONFIG                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 87;      // 95
    public static final int eHCM_GET_RETROTOUCH_CONFIG                          = PICOP_PPCPDATA_BASE_COMMAND_ID + 88;      // 96
    public static final int eHCM_SET_3D_POLARIZATION_ROTATOR_STATE              = PICOP_PPCPDATA_BASE_COMMAND_ID + 89;      // 97
    public static final int eHCM_GET_3D_POLARIZATION_ROTATOR_STATE              = PICOP_PPCPDATA_BASE_COMMAND_ID + 90;      // 98
    public static final int eHCM_SET_3D_PHASE                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 91;      // 99
    public static final int eHCM_GET_3D_PHASE                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 92;      // 100
    public static final int eHCM_SET_3D_DEPTH                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 93;      // 101
    public static final int eHCM_GET_3D_DEPTH                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 94;      // 102
    public static final int eHCM_SET_OUTPUT_VIDEO_STATE_EX                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 95;      // 103
    public static final int eHCM_GET_OUTPUT_VIDEO_STATE_EX                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 96;      // 104
    public static final int eHCM_GET_WARP_PARAMETER                             = PICOP_PPCPDATA_BASE_COMMAND_ID + 97;      // 105
    public static final int eHCM_SET_VERTICAL_PROJECTION_ANGLE_OFFSET           = PICOP_PPCPDATA_BASE_COMMAND_ID + 98;      // 106
    public static final int eHCM_GET_VERTICAL_PROJECTION_ANGLE_OFFSET           = PICOP_PPCPDATA_BASE_COMMAND_ID + 99;      // 107
    public static final int eHCM_SET_SPLASH_SCREEN_TIMEOUT                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 100;     // 108
    public static final int eHCM_GET_SPLASH_SCREEN_TIMEOUT                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 101;     // 109
    public static final int eHCM_GET_KEYSTONE_CORRECTION                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 102;     // 110
    public static final int eHCM_GET_VERTICAL_DISTORTION_CORRECTION             = PICOP_PPCPDATA_BASE_COMMAND_ID + 103;     // 111
    public static final int eHCM_GET_WARP_SETUP                                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 104;     // 112
    public static final int eHCM_SIMULATE_RETROTOUCH                            = PICOP_PPCPDATA_BASE_COMMAND_ID + 105;     // 113
    public static final int eHCM_SET_FRAMEBUFFER_CONFIG                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 106;     // 114
    public static final int eHCM_GET_FRAMEBUFFER_CONFIG                         = PICOP_PPCPDATA_BASE_COMMAND_ID + 107;     // 115
    public static final int eHCM_DISTORT_VIEWPORT                               = PICOP_PPCPDATA_BASE_COMMAND_ID + 108;     // 116
    public static final int eHCM_GET_VIEWPORT_DISTORTION                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 109;     // 117
    public static final int eHCM_SET_OPERATING_TEMPERATURE_RANGE                = PICOP_PPCPDATA_BASE_COMMAND_ID + 110;     // 118
    public static final int eHCM_GET_OPERATING_TEMPERATURE_RANGE                = PICOP_PPCPDATA_BASE_COMMAND_ID + 111;     // 119
    public static final int eHCM_SET_INPUT_VIDEO_STATE_EX                       = PICOP_PPCPDATA_BASE_COMMAND_ID + 112;     // 120
    public static final int eHCM_GET_INPUT_VIDEO_STATE_EX                       = PICOP_PPCPDATA_BASE_COMMAND_ID + 113;     // 121
    public static final int eHCM_SET_VERTICAL_SCAN_ANGLE                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 114;     // 122      //MEK
    public static final int eHCM_GET_VERTICAL_SCAN_ANGLE                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 115;     // 123      //MEK
    public static final int eHCM_SET_HORIZONTAL_SCAN_ANGLE                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 116;     // 124      //MEK
    public static final int eHCM_GET_HORIZONTAL_SCAN_ANGLE                      = PICOP_PPCPDATA_BASE_COMMAND_ID + 117;     // 125      //MEK
    public static final int eHCM_GET_SERNO_SAA_LPAS_INFO                        = PICOP_PPCPDATA_BASE_COMMAND_ID + 118;     // 126      //Sharp
    public static final int eHCM_SET_GAMMA_BOOST_MODE                           = PICOP_PPCPDATA_BASE_COMMAND_ID + 119;     // 127
    public static final int eHCM_GET_GAMMA_BOOST_MODE                           = PICOP_PPCPDATA_BASE_COMMAND_ID + 120;     // 128
    public static final int eHCM_AUTO_SET_GREEN_MAGENTA_BALANCE                 = PICOP_PPCPDATA_BASE_COMMAND_ID + 121;     // 129      // TEST ONLY, will be removed shortly
    public static final int eHCM_GET_SN_SAA_LPAS_INFO                           = PICOP_PPCPDATA_BASE_COMMAND_ID + 122;     // 130      // TEST ONLY, will be removed shortly
    public static final int eNUM_HCM_COMMANDS                                   = PICOP_PPCPDATA_BASE_COMMAND_ID + 123;     // 131
    public static final int eHCM_EXTENDED_MCU_COMMAND_SET_ID                    = 0xF0;
//  public static final int eHCM_EXTENDED_XXX_COMMAND_SET_ID                    = eHCM_EXTENDED_MCU_COMMAND_SET_ID + 1;     //

    /* { Command identifiers Extended Mcu start */
    public static final int eHCM_MCU_UPGRADE                = 0x00;
    public static final int eHCM_MCU_UPGRADE_RDY            = 0x01;
    public static final int eHCM_SET_AUDIO_VOLUME           = 0x02;
    public static final int eHCM_GET_AUDIO_VOLUME           = 0x03;
    public static final int eHCM_SET_AUDIO_STATE            = 0x04;
    public static final int eHCM_GET_AUDIO_STATE            = 0x05;
    public static final int eHCM_SET_AUDIO_PROPERTY         = 0x06;
    public static final int eHCM_GET_AUDIO_PROPERTY         = 0x07;
    public static final int eHCM_SET_BATTERY_POWER_MODE     = 0x08; // do not delete, it could mess up Poogle
    public static final int eHCM_SET_MOTIONSENSE_STATE      = 0x09;
    public static final int eHCM_GET_MOTIONSENSE_STATE      = 0x0A;
    public static final int eHCM_SET_MOTIONSENSE_CONFIG     = 0x0B;
    public static final int eHCM_GET_MOTIONSENSE_CONFIG     = 0x0C;
    public static final int eHCM_SIMULATE_MOTIONSENSE       = 0x0D;
    public static final int eHCM_SET_BUTTONSENSE_STATE      = 0x0E;
    public static final int eHCM_GET_BUTTONSENSE_STATE      = 0x0F;
    public static final int eHCM_GET_BUTTON_STATUS          = 0x10;
    public static final int eHCM_SET_SLIDESHOW_STATE        = 0x11;
    public static final int eHCM_GET_SLIDESHOW_STATE        = 0x12;
    public static final int eHCM_SET_SLIDESHOW_CONFIG       = 0x13;
    public static final int eHCM_GET_SLIDESHOW_CONFIG       = 0x14;
    public static final int eHCM_CONTROL_SLIDESHOW          = 0x15;
    public static final int eHCM_SET_VEE_STATE              = 0x16;
    public static final int eHCM_GET_VEE_STATE              = 0x17;
    public static final int eHCM_SET_VEE_STRENGTH           = 0x18;
    public static final int eHCM_GET_VEE_STRENGTH           = 0x19;
    public static final int eHCM_GET_DB_RANGE               = 0x1A;
    public static final int eHCM_MCU_SET_RETROTOUCH_CONFIG  = 0x1B;
    public static final int eHCM_MCU_GET_RETROTOUCH_CONFIG  = 0x1C;
    public static final int eHCM_LAST_EXTENDED_MCU_COMMAND  = 0x1D;
    /* Command identifiers Extended Mcu end } */

    /* Return the command id with a byte[]. */
    public static byte[] getCommandIdByByte(int command_id){
        String hex = Integer.toBinaryString(command_id);
        if(hex.length()<8){
            hex = "0000"+hex;
        }
        byte[] commandid = hex.getBytes();
        return commandid;
    }

    /* Return the command id with a binary string. */
    public static String getCommandIdByString(int command_id){
        String bin = Integer.toBinaryString(command_id);
        while(bin.length()<8){
            bin = "0"+bin;
        }
        return bin;
    }
    /* Command identifiers start }*/

    private static final String PICOP_SERIAL_CMD_COMMIT     = "00000001"; //0x01
    private static final String PICOP_SERIAL_CMD_NOT_COMMIT = "00000000"; //0x00
    public static String getCommitStatus(boolean commit){
        if(commit){
            return PICOP_SERIAL_CMD_COMMIT;
        } else{
            return PICOP_SERIAL_CMD_NOT_COMMIT;
        }
    }
}
