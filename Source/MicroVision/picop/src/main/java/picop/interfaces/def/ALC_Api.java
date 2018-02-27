//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!
//! \file
//!
//! \brief Application Level Java (ALJ) API
//!
//!        Application Level Java API implementation for communication to the
//!        MVIS PicoP Display Engine.
//!
//! Copyright (C) 2017 MicroVision, Inc.
//!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

package picop.interfaces.def;

import static picop.interfaces.def.PicoP_PpcpUtils.MAX_BRIGHTNESS_VAL;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_COLOR_ALIGN_OFFSET;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_COLOR_CONVERT_OFFSET;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_EVENT_LOG;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_HOR_RESOLUTION;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_KEYSTONE_VALUE;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_PHASE_VAL;
import static picop.interfaces.def.PicoP_PpcpUtils.MAX_VER_RESOLUTION;
import static picop.interfaces.def.PicoP_PpcpUtils.MIN_BRIGHTNESS_VAL;
import static picop.interfaces.def.PicoP_PpcpUtils.MIN_COLOR_ALIGN_OFFSET;
import static picop.interfaces.def.PicoP_PpcpUtils.MIN_COLOR_CONVERT_OFFSET;
import static picop.interfaces.def.PicoP_PpcpUtils.MIN_KEYSTONE_VALUE;
import static picop.interfaces.def.PicoP_PpcpUtils.MIN_PHASE_VAL;


public class ALC_Api {

	private static final String TAG = "ALC_Api ";
	public static Object mPicoP_Ppcp_lock = null;

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function must be the first call into the ALC library. It opens the library and allocates
	/// resources necessary for operation. It returns a handle to the library that must be used
	/// in subsequent calls.
	/// </summary>
	///
	/// <param name = "libraryHandle">[OUT] A pointer to a PicoP_HANDLE that is set to an open handle
	///	of the library  </param>
	/// <returns> eSUCCESS- If the library is opened successfully .<br>
	/// eFAILURE- If library open failed. <br>
	/// eALREADY_OPENED- If ALC library opened already. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_OpenLibrary(PicoP_Handle libraryHandle) {
		if( null == libraryHandle ) {
			PicoP_Ulog.e(TAG, "error allocating memory for library context.");
			return PicoP_RC.eFAILURE;
		}

		// new a object for synchronized work.
		mPicoP_Ppcp_lock = new Object();
		if( null == mPicoP_Ppcp_lock) {
			PicoP_Ulog.e(TAG, "error creating sync object.");
		}

		// instantiate g_pLibCtx.
		synchronized (mPicoP_Ppcp_lock){
			libraryHandle.setPicoP_HANDLE_Values(0);
			libraryHandle.setSequenceNumber(1);
		}
		return PicoP_RC.eSUCCESS;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function closes the ALC API library and releases all resources.
	/// It also closes all the open connections
	/// </summary>
	///
	/// <param name = "libraryHandle">[IN] A valid library handle previously obtained by a call
	/// to PicoP_ALC_OpenLibrary() </param>
	/// <returns> eSUCCESS- If the library is closed successfully .<br>
	/// eFAILURE- If library close failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_CloseLibrary(PicoP_Handle libraryHandle){
		if( null == libraryHandle ) {
			return PicoP_RC.eUNINITIALIZED;
		}
		// close all open connections
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		synchronized (mPicoP_Ppcp_lock){
			if(libraryHandle.connectInfo.getConnectionStatus()){
				ret = PicoP_Operator.CloseConnection(libraryHandle.connectionInfoEx.getConnectionType());
			}
			libraryHandle = null;
			System.gc();
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function returns the version and capability information of the ALC API library.
	/// </summary>
	///
	/// <param name = "libraryHandle">[IN] A valid library handle previously obtained by a call
	/// to PicoP_ALC_OpenLibrary()</param>
	/// <param name = "libraryInfo">[OUT] A pointer to an PicoP_LibraryInfo struct that will be filled with the information
	/// </param>
	/// <returns> eSUCCESS- If the library information is retrieved successfully .<br>
	/// eFAILURE- If library information retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// </returns>
	///-----
	public static PicoP_RC PicoP_ALC_GetLibraryInfo(PicoP_Handle libraryHandle, PicoP_LibraryInfo libraryInfo){
		// Assign the library information details to OUT parameter
		if(null == libraryHandle || null == libraryInfo){
			return PicoP_RC.eUNINITIALIZED;
		} else {
			synchronized (mPicoP_Ppcp_lock) {
				libraryInfo.majorVersion = PicoP_PpcpUtils.ALJ_LIB_VERSION_MAJOR;
				libraryInfo.minorVersion = PicoP_PpcpUtils.ALJ_LIB_VERSION_MINOR;
				libraryInfo.patchVersion = PicoP_PpcpUtils.ALJ_LIB_VERSION_PATCH;
				libraryInfo.capabilityFlags = PicoP_PpcpUtils.ALJ_CAPABILITY_FLAGS;
			}
			return PicoP_RC.eSUCCESS;
		}
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function opens a connection to the Projector Display Engine using either USB or RS232
	/// </summary>
	///
	/// <param name = "libraryHandle">[IN] A valid library handle previously obtained by a call
	/// to PicoP_ALC_OpenLibrary()</param>
	/// <param name = "connectionType">[IN] Type of connection to be opened.</param>
	/// <param name = "connectionInfo">[IN] Information about the connection, includes items such
	/// as type, port#, baud rate, etc.</param>
	/// <param name = "connectionHandle">[OUT] A pointer to an PicoP_HANDLE that is set if the connection is successful.
	/// </param>
	/// <returns> eSUCCESS- If the connection is opened successfully .<br>
	/// eFAILURE- If connection open failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eALREADY_OPENED- If connection opened already. <br>
	/// eNOT_CONNECTED- If no valid device connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_OpenConnection(PicoP_Handle libraryHandle,
													PicoP_ConnectionTypeE connectionType, PicoP_ConnectionInfo connectionInfo /*, PicoP_Handle connectionHandle*/){   // remove the last parameter, I don't know why we need it
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		PicoP_ConnectionInfoEx connectioninfoex = new PicoP_ConnectionInfoEx(connectionType);
		if( null == libraryHandle
				||(!PicoP_ConnectionTypeE.eRS232.equals(connectionType)
				&&!PicoP_ConnectionTypeE.eBTH.equals(connectionType)
				&&!PicoP_ConnectionTypeE.eUSB.equals(connectionType))){
			PicoP_Ulog.e(TAG, " Invalid argument : "+ connectionType );
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock) {
			PicoP_Ulog.i(TAG," try to open conn now.");
			ret = PicoP_Operator.OpenConnection(connectionType, connectionInfo);

			if(ret == PicoP_RC.eSUCCESS){
				libraryHandle.connectInfo.setConnectionStatus(true);
				libraryHandle.connectionInfoEx = connectioninfoex;
				libraryHandle.connectInfo = connectionInfo;
			}
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function Closes a previously opened connection to the Projector Display Engine
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open handle to the connection to be closed.
	/// </param>
	/// <returns> eSUCCESS- If the connection is closed successfully .<br>
	/// eFAILURE- If connection close failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_CloseConnection(PicoP_Handle libraryHandle,PicoP_ConnectionTypeE connectionType/*, PicoP_Handle connectionHandle*/){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if( null == libraryHandle
				||(!PicoP_ConnectionTypeE.eRS232.equals(connectionType)
				&&!PicoP_ConnectionTypeE.eBTH.equals(connectionType)
				&&!PicoP_ConnectionTypeE.eUSB.equals(connectionType))){
			PicoP_Ulog.e(TAG, "Invalid argument : " + ", " + connectionType + ", " + libraryHandle);
			return PicoP_RC.eINVALID_ARG;
		}

		// Watch for race condition of mPicoP_Ppcp_lock being null when the phone is rotated
		if (null == mPicoP_Ppcp_lock) {
			PicoP_Ulog.i(TAG, " close conn anyway.");
			ret = PicoP_Operator.CloseConnection(connectionType);
		} else {
			synchronized (mPicoP_Ppcp_lock) {
				PicoP_Ulog.i(TAG, " try to close conn now.");
				ret = PicoP_Operator.CloseConnection(connectionType);
			}
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function Enumerates Bth devices..
	/// </summary>
	///
	/// <param name = "devType">[IN] Device Type to Enumerate (BT, USB, or UART).</param>
	/// <param name = "numDevicesToEnumerate">[IN]  Maximum number of devices to enumarate.</param>
	/// <param name = "pNumDevices">[OUT]  Pointer to hold number of devices found</param>
	/// <param name = "pDeviceInfo">[OUT] Memory allocated by called, will return device information
	///									structures for enumerated devices. In case of BT,
	///									size = numDevicesToEnumerate * sizeof(PicoP_BTHDiscoveryInfo)</param>
	/// <returns> eSUCCESS- If the device enumeration is done successfully.<br>
	/// eFAILURE- If the device enumeration failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	/*
	public static PicoP_RC PicoP_ALC_EnumerateDevices(PicoP_ConnectionTypeE devType,PInt numDevicesToEnumerate,
			PInt pNumDevices, PicoP_BTHDiscoveryInfo[] pDeviceInfo){
		return PicoP_RC.eSUCCESS;
	}
	*/

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets brightness for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "brightnessValue">[IN] Floating point value of brightness (0.0 to 1.0).</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit. </param>
	/// <returns> eSUCCESS- If the brightness is set successfully .<br>
	/// eFAILURE- If brightness set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetBrightnessVal(PicoP_Handle libraryHandle,
													  float fBrightnessValue, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if( null == libraryHandle
				|| (fBrightnessValue <MIN_BRIGHTNESS_VAL ||  fBrightnessValue > MAX_BRIGHTNESS_VAL)){
			PicoP_Ulog.e(TAG,"invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			PicoP_Ulog.i(TAG," try to set brightness now.");
			ret = PicoP_Operator.setBrightness(libraryHandle, fBrightnessValue, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets brightness for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store floating value of get brightness.</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the brightness is got successfully .<br>
	/// eFAILURE- If brightness retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetBrightnessVal(PicoP_Handle libraryHandle,
													  ALC_Api.ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			PicoP_Ulog.i(TAG," try to get brightness now.");
			ret = PicoP_Operator.getBrightness(libraryHandle, callback, storageType);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets aspect ratio for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "aspectRatio">[IN] Accepted values are eASPECT_RATIO_NORMAL, eASPECT_RATIO_WIDESCREEN and eASPECT_RATIO_ZOOM.</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit. </param>
	/// <returns> eSUCCESS- If the aspect ratio is set successfully .<br>
	/// eFAILURE- If aspect ratio set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetAspectRatioMode( PicoP_Handle connectionHandle,
														 PicoP_AspectRatioModeE aspectRatio, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				||(PicoP_AspectRatioModeE.eASPECT_RATIO_NORMAL != aspectRatio
				&& PicoP_AspectRatioModeE.eASPECT_RATIO_WIDESCREEN != aspectRatio
				&& PicoP_AspectRatioModeE.eASPECT_RATIO_ZOOM_HORIZONTAL != aspectRatio
				&& PicoP_AspectRatioModeE.eASPECT_RATIO_ZOOM_VERTICAL != aspectRatio
				&& PicoP_AspectRatioModeE.eASPECT_RATIO_ZOOM_ANAMORPHIC != aspectRatio)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setAspectRatioMode(connectionHandle, aspectRatio, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets aspect ratio for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "aspectRatio">[OUT] Pointer to hold aspect ratio.
	/// Returned values can be eASPECT_RATIO_NORMAL, eASPECT_RATIO_WIDESCREEN and eASPECT_RATIO_ZOOM</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the aspect ratio is got successfully .<br>
	/// eFAILURE- If aspect ratio retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetAspectRatioMode( PicoP_Handle connectionHandle,
														 ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == connectionHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			PicoP_Ulog.i(TAG," try to get aspect ratio mode now.");
			ret = PicoP_Operator.getAspectRatioMode(connectionHandle, callback, storageType);
		}

		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets color mode for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "aspectRatio">[IN] Accepted values are eCOLOR_MODE_BRILLIANT, eCOLOR_MODE_STANDARD and eCOLOR_MODE_INVERTED.</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit. </param>
	/// <returns> eSUCCESS- If the color mode is set successfully .<br>
	/// eFAILURE- If color mode set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetColorMode( PicoP_Handle libraryHandle,
												   PicoP_ColorModeE colorMode, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libraryHandle
				||(PicoP_ColorModeE.eCOLOR_MODE_BRILLIANT != colorMode
				&& PicoP_ColorModeE.eCOLOR_MODE_INVERTED != colorMode
				&& PicoP_ColorModeE.eCOLOR_MODE_STANDARD != colorMode)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setColorMode(libraryHandle, colorMode, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets color mode for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store floating value of hold color mode.
	///  0 = Brilliant, 1 = Standard, 2 = Inverted.</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the color mode is got successfully .<br>
	/// eFAILURE- If color mode retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetColorMode( PicoP_Handle libraryHandle,
												   ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getColorMode(libraryHandle, callback, storageType);
		}
		return PicoP_RC.eSUCCESS;
	}

	///------------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets gamma value for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "color">[IN] Enumerated color value. Accepted values are eRED, eGREEN, eBLUE or eALL_COLORS.</param>
	/// <param name = "gammaValue">[IN]Any floating point value. </param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit. </param>
	/// <returns> eSUCCESS- If the gamma value is set successfully .<br>
	/// eFAILURE- If gamma value set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetGammaVal( PicoP_Handle libraryHandle,
												  PicoP_ColorE color, float gammaValue, boolean bCommit) {
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| (PicoP_ColorE.eRED != color && PicoP_ColorE.eGREEN != color && PicoP_ColorE.eBLUE != color && PicoP_ColorE.eALL_COLORS != color)
				|| (gammaValue > PicoP_PpcpUtils.MAX_GAMMA_VAL || gammaValue < PicoP_PpcpUtils.MIN_GAMMA_VAL)){
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setGammaval(libraryHandle,color ,  gammaValue , bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets gamma value for the output display.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "color">[IN] Enumerated color value. Accepted values are eRED, eGREEN, eBLUE or eALL.</param>
	/// <param name = "callback">[OUT] To store floating value of get gamma value for the chosen color.</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the gamma value is got successfully .<br>
	/// eFAILURE- If gamma value retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetGammaVal( PicoP_Handle libraryHandle,
												  PicoP_ColorE color, ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libraryHandle
				|| (PicoP_ColorE.eRED != color && PicoP_ColorE.eGREEN != color && PicoP_ColorE.eBLUE != color && PicoP_ColorE.eALL_COLORS != color)
				|| (PicoP_ValueStorageTypeE.eCURRENT_VALUE!=storageType && PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType && PicoP_ValueStorageTypeE.eFACTORY_VALUE !=storageType)){
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getGammaval(libraryHandle, color, callback, storageType);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function Flip the image horizontally or vertically.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "direction">[IN] Direction.Accepted values are eHORIZONTAL or eVERTICAL.</param>
	/// <returns> eSUCCESS- If the image flipped successfully .<br>
	/// eFAILURE- If image flip failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_FlipImage(PicoP_Handle connectionHandle,PicoP_DirectionE direction){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				||(PicoP_DirectionE.eVERTICAL!=direction && PicoP_DirectionE.eHORIZONTAL !=direction && PicoP_DirectionE.eBOTH !=direction)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_()+ " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.doFlipImage(connectionHandle, direction);;
		}
		return ret;
	}

	//--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function Sets the Flip State.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "flipState">[IN] flipState.Accepted values are eFLIP_NEITHER or HORIZONTAL or eFLIP_VERTICAL or eFLIP_BOTH.</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit.</param>
	/// <returns> eSUCCESS- If flip state is set successfully .<br>
	/// eFAILURE- If flip state setting failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetFlipState(PicoP_Handle connectionHandle,
												  PicoP_FlipStateE flipState, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				||(PicoP_FlipStateE.eFLIP_NEITHER != flipState
				&& PicoP_FlipStateE.eFLIP_HORIZONTAL != flipState
				&& PicoP_FlipStateE.eFLIP_VERTICAL != flipState
				&& PicoP_FlipStateE.eFLIP_BOTH != flipState)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setFlipState(connectionHandle, flipState, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function returns the current Flip state.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "flipState">[OUT] Current flip State. eFLIP_NEITHER or HORIZONTAL or eFLIP_VERTICAL or eFLIP_BOTH.</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the flip state is retrieved successfully .<br>
	/// eFAILURE- If flip state retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetFlipState(PicoP_Handle libraryHandle,
												  ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getFlipState(libraryHandle, callback, storageType);
		}
		return PicoP_RC.eSUCCESS;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function performs keystone correction.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "keyStoneCorrectionValue">[IN] This value ranges from +100 to -100.</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit.</param>
	/// <returns> eSUCCESS- If the keystone correction is done successfully .<br>
	/// eFAILURE- If keystone correction failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_CorrectKeystone( PicoP_Handle libraryHandle,
													  int keyStoneCorrectionValue, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libraryHandle
				|| MIN_KEYSTONE_VALUE > keyStoneCorrectionValue
				|| MAX_KEYSTONE_VALUE < keyStoneCorrectionValue){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.correctKeystone(libraryHandle, keyStoneCorrectionValue, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function retrieves the Keystone Correction Value.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store returned Keystone Correction Value.</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the Keystone Correction Value is got successfully .<br>
	/// eFAILURE- If Keystone Correction Value retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetKeystoneCorrection(PicoP_Handle libraryHandle,
														   ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getKeyStoneConnection(libraryHandle, callback, storageType);
		}
		return PicoP_RC.eSUCCESS;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the Viewport Distortion Parameters.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "offsetTopLeft">[IN] % of viewport to push top left corner inward.</param>
	/// <param name = "offsetTopRight">[IN] % of viewport to push top right corner inward.</param>
	/// <param name = "offsetLowerLeft">[IN] % of viewport to push lower left corner inward.</param>
	/// <param name = "offsetLowerRight">[IN] % of viewport to push lower right corner inward.</param>
	/// <param name = "commit">[IN] "commit">[IN] 0 = No commit, 1 = Commit</param
	/// <returns> eSUCCESS- If the Viewport Distortion Parameters are set successfully .<br>
	/// eFAILURE- If Viewport Distortion Parameters set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetViewportDistortion(PicoP_Handle connectionHandle, float offsetTopLeft, float fOffsetTopRight,
														   float fOffsetLowerLeft, float fOffsetLowerRight, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setViewportDistortion(connectionHandle, offsetTopLeft, fOffsetTopRight,
					fOffsetLowerLeft, fOffsetLowerRight, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function retrieves the Viewport Distortion Parameters.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store value of the corner distortion offset .</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the Viewport Distortion Parameters are retrieved successfully .<br>
	/// eFAILURE- If Viewport Distortion Parameters retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetViewportDistortion(PicoP_Handle libraryHandle,
														   ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		
		synchronized (mPicoP_Ppcp_lock){
			PicoP_Ulog.i(TAG," try to get viewport distortion now.");

			ret = PicoP_Operator.getViewportDistortion(libraryHandle, callback, storageType);

		}

		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function performs vertical or horizontal color alignment for the selected color.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "direction">[IN] Valid values are eHORIZONTAL and eVERTICAL.</param>
	/// <param name = "color">[IN] Valid values are eRED, eGREEN or eBLUE.</param>
	/// <param name = "offset">[IN] This value ranges from +32 to -32.</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit.</param>
	/// <returns> eSUCCESS- If the color alignment is done successfully .<br>
	/// eFAILURE- If the color alignment failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetColorAlignment(PicoP_Handle libraryHandle,
													   PicoP_DirectionE direction, PicoP_ColorE color, short sOffset, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| (PicoP_DirectionE.eHORIZONTAL != direction && PicoP_DirectionE.eVERTICAL != direction)
				|| (PicoP_ColorE.eRED!=color && PicoP_ColorE.eGREEN != color && PicoP_ColorE.eBLUE !=color && PicoP_ColorE.eALL_COLORS !=color)
				|| (MIN_COLOR_ALIGN_OFFSET > sOffset || MAX_COLOR_ALIGN_OFFSET<sOffset)) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setColorAlignment(libraryHandle, direction, color,sOffset, bCommit);
		}

		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the vertical or horizontal color alignment offset value for the selected color.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "direction">[IN] Valid values are eHORIZONTAL and eVERTICAL.</param>
	/// <param name = "color">[IN] Valid values are eRED, eGREEN or eBLUE.</param>
	/// <param name = "callback">[OUT] To store the offset value, this value ranges from +32 to -32.</param>
	/// <returns> eSUCCESS- If the get color alignment call is done successfully .<br>
	/// eFAILURE- If the get color alignment call failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetColorAlignment(PicoP_Handle libraryHandle,
													   PicoP_DirectionE direction, PicoP_ColorE color, ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| (PicoP_DirectionE.eHORIZONTAL != direction && PicoP_DirectionE.eVERTICAL != direction)
				|| (PicoP_ColorE.eRED!=color && PicoP_ColorE.eGREEN != color && PicoP_ColorE.eBLUE !=color && PicoP_ColorE.eALL_COLORS !=color)
				|| (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType
				&& PicoP_ValueStorageTypeE.eFACTORY_VALUE !=storageType && PicoP_ValueStorageTypeE.eCURRENT_VALUE !=storageType)) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getColorAlignment(libraryHandle, direction, color,callback, storageType);
		}

		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the color converter values.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "color">[IN] Valid values are eRED_TO_RED,eGREEN_TO_RED,eBLUE_TO_RED,eRED_TO_GREEN,eGREEN_TO_GREEN,eBLUE_TO_GREEN,eRED_TO_BLUE,eGREEN_TO_BLUE and eBLUE_TO_BLUE	</param>
	/// <param name = "coefficient">[IN] This value ranges from -65536 to +65535.</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit. </param>
	/// <returns> eSUCCESS- If the color converter value is set successfully .<br>
	/// eFAILURE- If the converter value failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetColorConverter(PicoP_Handle connectionHandle,
													   PicoP_ColorConvertE color,
                                                       int nCoefficient,
                                                       boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| color.enumtoInt() < PicoP_ColorConvertE.eRED_TO_RED.enumtoInt()
				|| color.enumtoInt() > PicoP_ColorConvertE.eBLUE_TO_BLUE.enumtoInt()
				|| nCoefficient <MIN_COLOR_CONVERT_OFFSET
				|| nCoefficient > MAX_COLOR_CONVERT_OFFSET){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			PicoP_Ulog.i(TAG," try to set color converter now.");
			ret = PicoP_Operator.setColorConverter(connectionHandle, color, nCoefficient, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the color converter values.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store the value, which ranges from -65536 to +65535.</param>
	/// <param name = "color">[IN] Valid values are eRED_TO_RED,eGREEN_TO_RED,eBLUE_TO_RED,eRED_TO_GREEN,eGREEN_TO_GREEN,eBLUE_TO_GREEN,eRED_TO_BLUE,eGREEN_TO_BLUE and eBLUE_TO_BLUE	</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the get color alignment call is done successfully .<br>
	/// eFAILURE- If the get color alignment call failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetColorConverter(PicoP_Handle connectionHandle,
                                                       ALC_Callback callback,
													   PicoP_ColorConvertE color,
                                                       PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))
				|| color.enumtoInt() < PicoP_ColorConvertE.eRED_TO_RED.enumtoInt()
				|| color.enumtoInt() > PicoP_ColorConvertE.eBLUE_TO_BLUE.enumtoInt()){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			PicoP_Ulog.i(TAG," try to get color converter now.");
			ret = PicoP_Operator.getColorConverter(connectionHandle, callback, color, storageType);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the scan line phase delay to align the forward and reverse scan video.
	/// This command will affect all three color channels.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "phaseValue">[IN] Phase Value(-50 to 50).</param>
	/// <param name = "commit">[IN] 0 = No Commit, 1 = Commit.</param>
	/// <returns> eSUCCESS- If the phase delay value is set successfully .<br>
	/// eFAILURE- If phase delay value set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetPhase( PicoP_Handle libraryHandle, short sPhaseValue, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libraryHandle
				|| MAX_PHASE_VAL <sPhaseValue
				|| MIN_PHASE_VAL > sPhaseValue){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setPhase(libraryHandle, sPhaseValue, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the scan line phase delay.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store of get scan line phase delay value.</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the phase delay value is got successfully .<br>
	/// eFAILURE- If phase delay retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetPhase( PicoP_Handle libraryHandle,
											   ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if (null == libraryHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getPhase(libraryHandle, callback, storageType);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the video output state to enabled or disabled.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "state">[IN] Accepted values are eVIDEO_OUTPUT_DISABLED or eVIDEO_OUTPUT_ENABLED.</param>
	/// <param name = "bCommit">[IN] false = No Commit, true = Commit.</param>
	/// <returns> eSUCCESS- If the output video state is set successfully .<br>
	/// eFAILURE- If output video state set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetOutputVideoState( PicoP_Handle libHandle, PicoP_OutputVideoStateE state, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libHandle
		        || ((PicoP_OutputVideoStateE.eOUTPUT_VIDEO_DISABLED != state
		        && PicoP_OutputVideoStateE.eOUTPUT_VIDEO_ENABLED != state))
		        || ((true != bCommit)
        		&& (false != bCommit))){
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setOutputvideostate(libHandle, state, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function returns the current video output state.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store Current Video Output State. The value can be eVIDEO_OUTPUT_DISABLED or eVIDEO_OUTPUT_ENABLED.</param>
	/// <param name = "storageType">[IN] 0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the output video state is retrieved successfully .<br>
	/// eFAILURE- If output video state retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetOutputVideoState(PicoP_Handle connectionHandle, ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
			|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
			&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
			&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))){
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getOutputvideostate(connectionHandle, callback, storageType);
		}
		return ret;
	}

	///------------------------------------------------------------------------------------------------
	/// <summary>
	/// This function configures the information for custom video capture mode.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "handle">[OUT] Handle to the custom video capture mode.</param>
	/// <param name = "captureInfo">[IN] VideocaptureInfo structure populated with the Video Capture properties. </param>
	/// <returns> eSUCCESS- If the video modes are set successfully .<br>
	/// eFAILURE- If video mode set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetInputCaptureModeInfo(PicoP_Handle connectionHandle,
															 PicoP_VideoModeHandleE modeHandle, PicoP_VideoCaptureInfo captureInfo){
		PicoP_RC ret = PicoP_RC.eSUCCESS;

		if(null == connectionHandle
				|| null == captureInfo
				|| captureInfo.getVideoStartPosition().getX_value() >= MAX_HOR_RESOLUTION
				|| captureInfo.getVideoStartPosition().getY_value() >= MAX_VER_RESOLUTION
				|| (captureInfo.getHSyncPolarity() != PicoP_PolarityE.ePOLARITY_NEGATIVE
				&& captureInfo.getHSyncPolarity() != PicoP_PolarityE.ePOLARITY_POSITIVE)
				|| (captureInfo.getVSyncPolarity() != PicoP_PolarityE.ePOLARITY_NEGATIVE
				&& captureInfo.getVSyncPolarity() != PicoP_PolarityE.ePOLARITY_POSITIVE)
				|| (captureInfo.getPixelClockEdge() != PicoP_PolarityE.ePOLARITY_NEGATIVE
				&& captureInfo.getPixelClockEdge() != PicoP_PolarityE.ePOLARITY_POSITIVE)
				|| captureInfo.getResolution().getWidth() > MAX_HOR_RESOLUTION
				|| captureInfo.getResolution().getHeight() > MAX_VER_RESOLUTION
				|| PicoP_VideoColorSpaceE.invalidColorSpace(captureInfo.getColorSpace())
				|| (captureInfo.getInterlaceField() != PicoP_InterlaceE.eINTERLACE_EVEN_FIELD
				&& captureInfo.getInterlaceField() != PicoP_InterlaceE.eINTERLACE_NONE
				&& captureInfo.getInterlaceField() != PicoP_InterlaceE.eINTERLACE_ODD_FIELD)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setInputCaptureModeInfo(connectionHandle, modeHandle, captureInfo);
		}
		return ret;
	}

	///------------------------------------------------------------------------------------------------
	/// <summary>
	/// This function modifies an existing custom input video mode in the PicoP.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "modeHandle">[IN] Video mode handle returned by SetInputCaptureModeInfo</param>
	/// <param name = "captureInfo">[IN] VideocaptureInfo structure populated with the Video Capture properties. </param>
	/// <returns> eSUCCESS- If the video modes are modified successfully .<br>
	/// eFAILURE- If video mode modification failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_ModifyInputCaptureModeInfo(PicoP_Handle connectionHandle,
																PicoP_VideoModeHandleE modeHandle, PicoP_VideoCaptureInfo captureInfo){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| null == modeHandle
				|| null == captureInfo
				|| captureInfo.getVideoStartPosition().getX_value() >= MAX_HOR_RESOLUTION
				|| captureInfo.getVideoStartPosition().getY_value() >= MAX_VER_RESOLUTION
				|| (captureInfo.getHSyncPolarity() != PicoP_PolarityE.ePOLARITY_NEGATIVE
				&& captureInfo.getHSyncPolarity() != PicoP_PolarityE.ePOLARITY_POSITIVE)
				|| (captureInfo.getVSyncPolarity() != PicoP_PolarityE.ePOLARITY_NEGATIVE
				&& captureInfo.getVSyncPolarity() != PicoP_PolarityE.ePOLARITY_POSITIVE)
				|| (captureInfo.getPixelClockEdge() != PicoP_PolarityE.ePOLARITY_NEGATIVE
				&& captureInfo.getPixelClockEdge() != PicoP_PolarityE.ePOLARITY_POSITIVE)
				|| captureInfo.getResolution().getWidth() > MAX_HOR_RESOLUTION
				|| captureInfo.getResolution().getHeight() > MAX_VER_RESOLUTION
				|| PicoP_VideoColorSpaceE.invalidColorSpace(captureInfo.getColorSpace())
				|| (captureInfo.getInterlaceField() != PicoP_InterlaceE.eINTERLACE_EVEN_FIELD
				&& captureInfo.getInterlaceField() != PicoP_InterlaceE.eINTERLACE_NONE
				&& captureInfo.getInterlaceField() != PicoP_InterlaceE.eINTERLACE_ODD_FIELD)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.modifyInputCaptureModeInfo(connectionHandle, modeHandle, captureInfo);
		}
		return ret;
	}

	///------------------------------------------------------------------------------------------------
	/// <summary>
	/// This function commits the given video capture mode info to NVRAM.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "modeHandle">[IN] Video mode handle returned by SetInputCaptureModeInfo</param>
	/// <returns> eSUCCESS- If the video modes is commited successfully .<br>
	/// eFAILURE- If video mode handle commit failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_CommitInputCaptureMode(PicoP_Handle connectionHandle, PicoP_VideoModeHandleE modeHandle){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if( null == connectionHandle){
			PicoP_Ulog.e(TAG,"invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			PicoP_Ulog.i(TAG," try to commit input capture mode now.");
			ret = PicoP_Operator.commitInputCaptureMode(connectionHandle, modeHandle);
		}
		return ret;
	}

	///------------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the commited custom video capture mode.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "modeHandle">[OUT] Handle to the custom video capture mode.
	///                 if no videomode is committed then it is filled with eVideoModeHandle_INVALID</param>
	/// <returns> eSUCCESS- If the video modes are set successfully .<br>
	/// eFAILURE- If video mode set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetCommitedInputCaptureMode(PicoP_Handle connectionHandle,
																 ALC_Callback callback){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getCommitedInputCaptureMode(connectionHandle, callback);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function returns information on input video capture mode
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "handle">[OUT] Handle to the video capture mode to be queried.</param>
	/// <param name = "captureInfo">[OUT] VideoCaptureInfo struct that gets filled in with the current video capture mode properties.</param>
	/// <returns> eSUCCESS- If the input video capture mode is got successfully .<br>
	/// eFAILURE- If input video capture mode retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetInputCaptureModeInfo(PicoP_Handle connectionHandle,
															 PicoP_VideoModeHandleE modeHandle, PicoP_VideoCaptureInfo captureInfo){
		PicoP_RC ret = PicoP_RC.eSUCCESS;

		if (null == connectionHandle|| null == modeHandle) {
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getInputCaptureModeInfo(connectionHandle, modeHandle, captureInfo);
		}
		return ret;
	}


	///------------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the active input video capture mode.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "modeHandle">[IN] Handle to the requested video capture mode.</param>
	/// <returns> eSUCCESS- If the given video mode is set successfully .<br>
	/// eFAILURE- If video capture mode set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetActiveCaptureMode(PicoP_Handle libraryHandle,
														  PicoP_VideoModeHandleE modeHandle){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libraryHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setActiveCaptureMode(libraryHandle,modeHandle);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the current input video capture mode
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "videoMode">[OUT] VideoCaptureInfo struct that gets filled in with the current video mode features. </param>
	/// <returns> eSUCCESS- If the input video capture mode is got successfully .<br>
	/// eFAILURE- If input video capture mode retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetActiveCaptureMode(PicoP_Handle libraryHandle, ALC_Callback callback){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libraryHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invalid argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getActiveCaptureMode(libraryHandle, callback);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the video input state to enabled or disabled.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "state">[IN] Accepted values are eINPUT_VIDEO_DISABLED or eINPUT_VIDEO_ENABLED.</param>
	/// <returns> eSUCCESS- If the input video state is set successfully .<br>
	/// eFAILURE- If input video state set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetInputVideoState( PicoP_Handle libHandle, PicoP_InputVideoStateE state){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == libHandle
				|| (PicoP_InputVideoStateE.eINPUT_VIDEO_ENABLED != state && PicoP_InputVideoStateE.eINPUT_VIDEO_DISABLED != state)){
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setInputvideostate(libHandle, state);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function returns the current video input state.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store Current Video Input State. The value can be eINPUT_VIDEO_DISABLED or eINPUT_VIDEO_ENABLED.</param>
	/// <returns> eSUCCESS- If the input video state is retrived successfully .<br>
	/// eFAILURE- If input video state retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetInputVideoState( PicoP_Handle connectionHandle, ALC_Callback callback){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getInputvideostate(connectionHandle,callback );
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets detected input video Frame Rate and Lines per Frame.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "callback">[OUT] To store the value of frameRate--Detected Frames per second
	///                                                       lines--Detected number of vertical lines per frames</param>
	/// <returns> eSUCCESS- If the input video properties is retrieved successfully .<br>
	/// eFAILURE- If input video properties retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetInputVideoProperties( PicoP_Handle connectionHandle, ALC_Callback callback){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getInputvideoproperties(connectionHandle, callback );
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the maximum size of the Frame Buffer or OSD that can be used for rendering.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "renderTarget">[IN] FrameBuffer Render target to query.</param>
	/// <param name = "size">[OUT] Size of the render target.</param>
	/// <returns> eSUCCESS- If the display information is retrieved successfully .<br>
	/// eFAILURE- If display information retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetDisplayInfo( PicoP_Handle connectionHandle,
													 PicoP_RenderTargetE renderTarget, PicoP_RectSize size){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(renderTarget)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getDisplayInfo(connectionHandle, renderTarget, size);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the active OSD to be used for video output.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "renderTarget">[IN] OSD to be used for video output.</param>
	/// <returns> eSUCCESS- If the active frame buffer is set successfully .<br>
	/// eFAILURE- If active frame buffer set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetActiveOSD( PicoP_Handle connectionHandle, PicoP_RenderTargetE renderTarget){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(renderTarget)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setActiveOSD(connectionHandle, renderTarget);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the active OSD used for video output.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "renderTarget">[OUT] OSD currently used for video output.</param>
	/// <returns> eSUCCESS- If the active frame buffer is retrieved successfully .<br>
	/// eFAILURE- If active frame buffer retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetActiveOSD( PicoP_Handle connectionHandle, ALC_Callback callback){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getActiveOSD(connectionHandle, callback);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function clears the selected render target.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Target that needs to be cleared.</param>
	/// <returns> eSUCCESS- If the active frame buffer is set successfully .<br>
	/// eFAILURE- If active frame buffer set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_ClearTarget( PicoP_Handle connectionHandle, PicoP_RenderTargetE renderTarget){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(renderTarget)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.clearTarget(connectionHandle, renderTarget);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the current Position and Size of On-Screen Display (OSD) within the output video area.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "startPoint">[OUT] Upper left corner location of OSD</param>
	/// <param name = "size">[OUT] Size of OSD</param>
	/// <returns> eSUCCESS- If the OSD information is retrieved successfully .<br>
	/// eFAILURE- If OSD information retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetOSDInfo( PicoP_Handle connectionHandle,PicoP_Point startPoint, PicoP_RectSize size){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getOSDInfo(connectionHandle, startPoint, size);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the Position and Size of On-Screen Display (OSD) within the output video area.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "startPoint">[IN] Upper left corner location of OSD</param>
	/// <param name = "size">[IN] Size of OSD</param>
	/// <returns> eSUCCESS- If the OSD information is set successfully .<br>
	/// eFAILURE- If OSD information set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetOSDInfo( PicoP_Handle connectionHandle, PicoP_Point startPoint, PicoP_RectSize size){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| startPoint.getX_value() >= MAX_HOR_RESOLUTION
				|| startPoint.getY_value() >= MAX_VER_RESOLUTION
				|| size.getWidth() > MAX_HOR_RESOLUTION
				|| size.getHeight() > MAX_VER_RESOLUTION){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setOSDInfo(connectionHandle, startPoint, size);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the OSD state to enabled or disabled.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "state">[IN] Accepted values are eOSD_DISABLED or eOSD_ENABLED.</param>
	/// <returns> eSUCCESS- If the OSD state is set successfully .<br>
	/// eFAILURE- If OSD state set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetOSDState( PicoP_Handle connectionHandle, PicoP_OSDStateE state){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| (state != PicoP_OSDStateE.eOSD_DISABLED
				&& state != PicoP_OSDStateE.eOSD_ENABLED)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setOSDState(connectionHandle, state);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function returns the current OSD state.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "state">[OUT] Current OSD State. Returned value can be eOSD_DISABLED or eOSD_ENABLED.</param>
	/// <returns> eSUCCESS- If the OSD state is retrieved successfully .<br>
	/// eFAILURE- If OSD state retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetOSDState( PicoP_Handle connectionHandle, ALC_Callback callback){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getOSDState(connectionHandle, callback);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function displays the test pattern selected.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Target Buffer to draw to (FrameBuffer or OSD).</param>
	/// <param name = "startPoint">[IN] Upper left corner coordinates of the test pattern.</param>
	/// <param name = "dimensions">[IN] Dimensions of the pattern; height and width to be rendered in the selected target.</param>
	/// <param name = "patternColor">[IN] Pattern Color - 24-Bit RGB pattern color value.</param>
	/// <param name = "backgroundColor">[IN] Background Color - 24-Bit RGB background color value.</param>
	/// <param name = "pattern">[IN] Test pattern enumeration definition. </param>
	/// <returns> eSUCCESS- If the test pattern is displayed successfully .<br>
	/// eFAILURE- If test pattern display failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_DrawTestPattern( PicoP_Handle connectionHandle, PicoP_RenderTargetE target,
													  PicoP_Point startPoint, PicoP_RectSize dimensions, PicoP_Color patternColor,
													  PicoP_Color backgroundColor, PicoP_TestPatternInfoE pattern){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)
				|| startPoint.getX_value() >= MAX_HOR_RESOLUTION
				|| startPoint.getY_value() >= MAX_VER_RESOLUTION
				|| dimensions.getWidth() > MAX_HOR_RESOLUTION
				|| dimensions.getHeight() > MAX_VER_RESOLUTION
				|| PicoP_TestPatternInfoE.invalidTestPattern(pattern)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.drawTestPattern(connectionHandle, target, startPoint, dimensions, patternColor, backgroundColor, pattern);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function loads raw binary RGB565 encoded bitmap data from the buffer on to the specified frame buffer.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Buffer to copy the image to.</param>
	/// <param name = "startPoint">[IN] Upper left corner coordinates of the image.</param>
	/// <param name = "dimensions">[IN] Dimensions of the bitmap; height and width to be rendered in the selected target.</param>
	/// <param name = "image">[IN] Data of bitmap image; 16 bit RGB565.</param>
	/// <param name = "size">[IN] Size of bitmap image.</param>
	/// <returns> eSUCCESS- If the bitmap image is loaded successfully .<br>
	/// eFAILURE- If bitmap image loading failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_LoadBitmapImage( PicoP_Handle connectionHandle,
													  PicoP_RenderTargetE target, PicoP_Point startPoint,
													  PicoP_RectSize dimensions, byte[] image, int nSize){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)
				|| startPoint.getX_value() >= MAX_HOR_RESOLUTION
				|| startPoint.getY_value() >= MAX_VER_RESOLUTION
				|| dimensions.getWidth() >= MAX_HOR_RESOLUTION
				|| dimensions.getWidth() == 0
				|| dimensions.getHeight() >= MAX_VER_RESOLUTION
				|| dimensions.getHeight() == 0
				|| (nSize < (dimensions.getWidth()*dimensions.getHeight()*2))
				|| nSize == 0){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}

		int u32Size = ((int)(dimensions.height * dimensions.width * 2) < nSize) ? (dimensions.height * dimensions.width * 2) : nSize;
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.loadBitmapImage(connectionHandle, target, startPoint, dimensions, image, nSize);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function queues a command to display text in the OSD or FrameBuffer.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Target Buffer to draw to (FrameBuffer or OSD).</param>
	/// <param name = "text">[IN] Text to Draw.</param>
	/// <param name = "length">[IN] Length of the Text (in characters).</param>
	/// <param name = "startPoint">[IN] Starting position for the text (lower-left corner).</param>
	/// <param name = "textColor">[IN] Text color to use.</param>
	/// <param name = "backgroundColor">[IN] Background color to use.</param>
	/// <returns> eSUCCESS- If the text is displayed successfully .<br>
	/// eFAILURE- If text display failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_DrawText(PicoP_Handle connectionHandle, PicoP_RenderTargetE target,
											  byte[] text, short length, PicoP_Point startPoint, PicoP_Color textColor, PicoP_Color backgroundColor){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)
				|| startPoint.getX_value() >= MAX_HOR_RESOLUTION
				|| startPoint.getY_value() >= MAX_VER_RESOLUTION){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.drawText(connectionHandle, target, text, length, startPoint, textColor, backgroundColor);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// Gets the dimensions of the bounding rectangle of the text as it will appear if drawn in the target.
	/// This will provide the API user with feedback to determine where and how to draw text, how to determine
	/// line-breaks, how to dimension menu items etc.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "text">[IN] Text to Draw.</param>
	/// <param name = "length">[IN] Length of the Text (in characters).</param>
	/// <param name = "textBoxRegion">[OUT] Region that would be filled with the given text if rendered.</param>
	/// <returns> eSUCCESS- If the text box information is retrieved successfully .<br>
	/// eFAILURE- If text box information retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetTextBoxInfo( PicoP_Handle connectionHandle, byte[] text,
													 short sLength, PicoP_RectSize textBoxRegion){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getTextBoxInfo(connectionHandle, text, sLength, textBoxRegion);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function queues a command to set a single pixel in the OSD or a FrameBuffer.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Target Buffer to draw to (FrameBuffer or OSD).</param>
	/// <param name = "pixel">[IN] Pixel to set to.</param>
	/// <param name = "color">[IN] Color to use for drawing.</param>
	/// <returns> eSUCCESS- If the point is displayed successfully .<br>
	/// eFAILURE- If point display failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_DrawPoint( PicoP_Handle connectionHandle, PicoP_RenderTargetE target,
												PicoP_Point pixel, PicoP_Color color){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)
				|| pixel.getX_value() >= MAX_HOR_RESOLUTION
				|| pixel.getY_value() >= MAX_VER_RESOLUTION){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.drawPoint(connectionHandle, target, pixel, color);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function queues a command to draw a line segment between two points.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Target Buffer to draw to (FrameBuffer or OSD).</param>
	/// <param name = "pointA">[IN] Starting point of the line.</param>
	/// <param name = "pointB">[IN] End point of the line.</param>
	/// <param name = "color">[IN] Color to use for drawing.</param>
	/// <returns> eSUCCESS- If the line is displayed successfully .<br>
	/// eFAILURE- If line display failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_DrawLine( PicoP_Handle connectionHandle, PicoP_RenderTargetE target,
											   PicoP_Point pointA, PicoP_Point pointB, PicoP_Color color){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)
				|| pointA.getX_value() >= MAX_HOR_RESOLUTION
				|| pointA.getY_value() >= MAX_VER_RESOLUTION
				|| pointB.getX_value() >= MAX_HOR_RESOLUTION
				|| pointB.getY_value() >= MAX_VER_RESOLUTION){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.drawLine(connectionHandle, target, pointA, pointB, color);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function queues a command to draw a Triangle into the OSD or a FrameBuffer.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Target Buffer to draw to (FrameBuffer or OSD).</param>
	/// <param name = "pointA">[IN] Corner A of Triangle.</param>
	/// <param name = "pointB">[IN] Corner B of Triangle.</param>
	/// <param name = "pointC">[IN] Corner C of Triangle.</param>
	/// <param name = "fillColor">[IN] Color to use for filling the triangle.</param>
	/// <returns> eSUCCESS- If the triangle is displayed successfully .<br>
	/// eFAILURE- If triangle display failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_DrawTriangle( PicoP_Handle connectionHandle, PicoP_RenderTargetE target,
												   PicoP_Point pointA, PicoP_Point pointB, PicoP_Point pointC, PicoP_Color fillColor){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)
				|| pointA.getX_value() >= MAX_HOR_RESOLUTION
				|| pointA.getY_value() >= MAX_VER_RESOLUTION
				|| pointB.getX_value() >= MAX_HOR_RESOLUTION
				|| pointB.getY_value() >= MAX_VER_RESOLUTION
				|| pointC.getX_value() >= MAX_HOR_RESOLUTION
				|| pointC.getY_value() >= MAX_VER_RESOLUTION){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.drawTriangle(connectionHandle, target, pointA, pointB, pointC, fillColor);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function queues a command to draw a Rectangle into the OSD or a FrameBuffer.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] Target Buffer to draw to (FrameBuffer or OSD).</param>
	/// <param name = "startPoint">[IN] Upper left corner of the rectangle.</param>
	/// <param name = "size">[IN] Size of the rectangle.</param>
	/// <param name = "fillColor">[IN] Color to use for filling the rectangle.</param>
	/// <returns> eSUCCESS- If the rectangle is displayed successfully .<br>
	/// eFAILURE- If rectangle display failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_DrawRectangle( PicoP_Handle connectionHandle, PicoP_RenderTargetE target,
													PicoP_Point startPoint, PicoP_RectSize size, PicoP_Color fillColor){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)
				|| startPoint.getX_value() >= MAX_HOR_RESOLUTION
				|| startPoint.getY_value() >= MAX_VER_RESOLUTION
				|| size.getWidth() >= MAX_HOR_RESOLUTION
				|| size.getHeight() >= MAX_VER_RESOLUTION){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.drawRectangle(connectionHandle, target, startPoint, size, fillColor);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// Renders queued draw commands into the render target.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <returns> eSUCCESS- If the draw commands are rendered successfully .<br>
	/// eFAILURE- If draw commands rendering failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_Render( PicoP_Handle connectionHandle){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.render(connectionHandle);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the system status
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "status">[OUT] Pointer to hold system status .32 bit value, 0 is system Ok and
	/// non-zero value indicates system not OK.</param>
	/// <returns> eSUCCESS- If the system status is got successfully .<br>
	/// eFAILURE- If system status retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetSystemStatus( PicoP_Handle connectionHandle, PicoP_SystemStatus systemStatus){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle || null == systemStatus){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getSystemStatus(connectionHandle, systemStatus);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets system information.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "systemInfo">[OUT] Pointer to hold system information structure .</param>
	/// <returns> eSUCCESS- If the system information is got successfully .<br>
	/// eFAILURE- If system information retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetSystemInfo( PicoP_Handle connectionHandle, PicoP_SystemInfo systemInfo){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle || null == systemInfo){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getSystemInfo(connectionHandle, systemInfo);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function gets the system event log.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "numEvents">[IN] Number of event log to be retrieved.</param>
	/// <param name = "pEvent">[OUT] Pointer to hold event log .</param>
	/// <returns> eSUCCESS- If the event log is got successfully .<br>
	/// eFAILURE- If event log retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetEventLog( PicoP_Handle connectionHandle, short sNumEvents, PicoP_Event[] pEvent){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| null == pEvent
				|| sNumEvents > MAX_EVENT_LOG){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getEventLog(connectionHandle, sNumEvents, pEvent);
		}
		return ret;
	}


	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function takes a snapshot of the specified frame buffer
	/// and saves the content as the Splash Screen.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "target">[IN] The frame buffer to take snapshot of. </param>
	/// <returns> eSUCCESS- If the EP splash screen is set successfully.<br>
	/// eFAILURE- If splash screen saving failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SaveAsSplash( PicoP_Handle connectionHandle, PicoP_RenderTargetE target){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| PicoP_RenderTargetE.invalidRenderTarget(target)){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.saveAsSplash(connectionHandle, target);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function restores system configuration to factory configuration.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "commit">[IN] 0 = No commit, 1 = Commit All.</param>
	/// <returns> eSUCCESS- If the system configuration is restored to factory configuration successfully .<br>
	/// eFAILURE- If restore factory configuration failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_RestoreFactoryConfig( PicoP_Handle connectionHandle, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.restoreFactoryConfig(connectionHandle, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function loads the EP software image for firmware upgrade.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "image">[IN] Data of SW image.</param>
	/// <param name = "size">[IN] Size of SW image.</param>
	/// <returns> eSUCCESS- If the EP software image is loaded successfully .<br>
	/// eFAILURE- If EP software image loading failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_UpgradeSoftware( PicoP_Handle connectionHandle, byte[] image, int nSize, ALC_Callback callback){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle || nSize == 0){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + "Invaild argument.");
			return PicoP_RC.eINVALID_ARG;
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.upgradeSoftware(connectionHandle, PicoP_PpcpData.eHCM_SW_UPGRADE, image, nSize, callback);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function sets the timeout for how long Splash Screen will be displayed.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "timeout">[IN] Length of SplashScreen Display in milliseconds, Range TBD .</param>
	/// <param name = "commit">[IN] "commit">[IN] 0 = No commit, 1 = Commit</param
	/// <returns> eSUCCESS- If the Splash Screen timeout is set successfully .<br>
	/// eFAILURE- If Splash Screen timeout set failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_SetSplashScreenTimeout(PicoP_Handle connectionHandle, int nTimeout, boolean bCommit){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.setSplashScreenTimeout(connectionHandle, nTimeout, bCommit);
		}
		return ret;
	}

	///--------------------------------------------------------------------------------------------
	/// <summary>
	/// This function retrieves the Splash Screen timeout.
	/// </summary>
	///
	/// <param name = "connectionHandle">[IN] An open connection handle to the projector engine.</param>
	/// <param name = "state">[OUT] Current Video Output State. Returned value can be eVIDEO_OUTPUT_DISABLED or eVIDEO_OUTPUT_ENABLED.</param>
	/// <param name = "storageType">[IN]  0 = Current Value, 1 = Value on system startup, 2 = Factory value. </param>
	/// <returns> eSUCCESS- If the Splash Screen timeout is retrieved successfully .<br>
	/// eFAILURE- If Splash Screen timeout retrieval failed. <br>
	/// eNOT_INITIALIZED- If ALC library is not initialized. <br>
	/// eINVALID_ARG- If any one of the arguments is invalid. <br>
	/// eNOT_CONNECTED- If no valid device is connected. <br>
	/// </returns>
	///------------------------------------------------------------------------------------------------
	public static PicoP_RC PicoP_ALC_GetSplashScreenTimeout(PicoP_Handle connectionHandle,
															ALC_Callback callback, PicoP_ValueStorageTypeE storageType){
		PicoP_RC ret = PicoP_RC.eSUCCESS;
		if(null == connectionHandle
				|| ((PicoP_ValueStorageTypeE.eCURRENT_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eFACTORY_VALUE != storageType)
				&& (PicoP_ValueStorageTypeE.eVALUE_ON_STARTUP != storageType))){
			PicoP_Ulog.e(TAG, PicoP_Ulog._FUNC_() + " Invaild argument.");
		}
		synchronized (mPicoP_Ppcp_lock){
			ret = PicoP_Operator.getSplashScreenTimeout(connectionHandle, callback, storageType);
		}
		return ret;
	}

	public interface ALC_Callback{
		public void upgradeSoftwareCallback(int currentPacket, int numPackets, int destID);
		public void brightnessCallback(float brightness);
		public void colorModeCallback(int colorMode);
		public void keyStoneConnectionCallback(int keyStoneCorrectionValue);
		public void colorAligmentCallback(int offset);
		public void getPhaseCallback(int phaseValue);
		public void gammavalCallback(float gammaVal);
		public void inputStateCallback(int state);
		public void outputStateCallback(int state);
		public void activeCaptureModeCallback(int videoMode);
		public void inputVideoProCallback(float frameRate, int lines);
		public void distortViewPortCallback(float topLeft, float topRight, float lowerLeft, float lowerRight);
		public void aspectRatioCallback(int aspectRatio);
		public void flipStateCallback(int flipState);
		public void colorConverterCallback(int coefficient);
		public void commitedInputCaptureModeCallback(int videoMode);
		public void activeOSDCallback(int renderTarget);
		public void OSDStateCallback(int osdState);
		public void splashScreenTimeoutCallback(int splashScreenTimeout);
	}
}
