package picop.interfaces.def;

/**
 * Created by carey.wang on 2017/3/30.
 */

public class PicoP_PpcpUtils {

    // The major version number of ALC library
    public static final byte ALJ_LIB_VERSION_MAJOR                                                  = 1;
    // The minor version number of ALC library
    public static final byte ALJ_LIB_VERSION_MINOR                                                  = 0;
    // The patch version number of ALC library
    public static final byte ALJ_LIB_VERSION_PATCH                                                  = 0;
    /* ALC specific contants */
    public static final int ALJ_CAPABILITY_FLAGS                                                    = 0;     // Library capability flags


    // The min value for set brightneess
    public static final float MIN_BRIGHTNESS_VAL                                                    = 0.0f;
    // The max value for set brightness
    public static final float MAX_BRIGHTNESS_VAL                                                    = 1.0f;

    /* Values for keystone */
    public static final int MIN_KEYSTONE_VALUE                                                      = -100;
    public static final int MAX_KEYSTONE_VALUE                                                      = 100;
    public static final int DEFAULT_KEYSTONE_VALUE                                                  = 0;


    public static final int MIN_PHASE_VAL                                                           = -50;
    public static final int MAX_PHASE_VAL                                                           = 50;

    public static final int MIN_GAMMA_VAL                                                           = 0;
    public static final int MAX_GAMMA_VAL                                                           = 100;

    public static final int MIN_COLOR_ALIGN_OFFSET                                                  = -32;
    public static final int MAX_COLOR_ALIGN_OFFSET                                                  = 32;

    public static final int MIN_HOR_RESOLUTION                                                      =0;
    public static final int MAX_HOR_RESOLUTION                                                      =1280;

    public static final int MIN_VER_RESOLUTION                                                      =0;
    public static final int MAX_VER_RESOLUTION                                                      =720;

    public static final int MAX_SERIAL_NUMBER_LEN                                                      =17;
    public static final int MAX_EVENT_LOG                                                      =255;
    public static final int MIN_COLOR_CONVERT_OFFSET                                            = -65536;
    public static final int MAX_COLOR_CONVERT_OFFSET                                            = 65535;
    public static final int MAX_PAYLOAD_SIZE                                            =4095;
    public static final int MAX_DRAW_TEXT_LEN                                            =1008;
    public static final int PAYLOAD_DATA_START_IDX                                            =3;
    public static final int MAX_REPLY_PAYLOAD_LEN                                            =4096;
    public static final int MAX_BITMAP_PAYLOAD_SIZE                                            =2548;
    public static final int MAX_UPGRADE_PAYLOAD_SIZE                                            =2548;
    public static final int MCU_FW_HEADER                                            =0x4655434D;



    /*
    * Data Length Info
    * Ex.
    * String data = 0x000000
    * Data length = 6
    * */

    public static final int DATA_LENGTH_BINARY_STRING                                                = 8;
    public static final int DATA_LENGTH_KEYSTONE                                                     = 8;
    public static final int DATA_LENGTH_BRIGHTNESS                                                   = 8;


}
