# Getting Started

## Add JAR dependency to Gradle.build:

```groovy
dependencies {
	// [...]
    compile files('libs/PicoP.jar')
}
```

## Open Connection:

```java
Context appContext = InstrumentationRegistry.getTargetContext();

PicoP_LibraryInfo libraryInfo = new PicoP_LibraryInfo();
PicoP_Handle connectionHandle = new PicoP_Handle(eUSB);
PicoP_ConnectionInfo connectionInfo = connectionHandle.getConnectInfo();
connectionInfo.setConnectionContext(appContext);

PicoP_RC result = ALC_Api.PicoP_ALC_OpenLibrary(connectionHandle);

ret = ALC_Api.PicoP_ALC_OpenConnection(connectionHandle, PicoP_ConnectionTypeE.eUSB, connectionInfo);
```

## Construct Parameters:

```java
PicoP_RenderTargetE target = PicoP_RenderTargetE.eFRAME_BUFFER_0;
PicoP_Point startPoint = new PicoP_Point();
startPoint.setPicoP_Point((short)0, (short)0);
PicoP_RectSize size = new PicoP_RectSize();
size.setPicoP_RectSize((short)1024, (short)720);

PicoP_Color foregroundColor = new PicoP_Color();
foregroundColor.R = (byte)0x00;
foregroundColor.G = (byte)0x99;
foregroundColor.B = (byte)0x99;
foregroundColor.A = (byte)0x00;

PicoP_Color backgroundColor = new PicoP_Color();
backgroundColor.R = (byte)0x00;
backgroundColor.G = (byte)0x00;
backgroundColor.B = (byte)0x00;
backgroundColor.A = (byte)0x00;

PicoP_TestPatternInfoE pattern = PicoP_TestPatternInfoE.eCHECKER_BOARD_PATTERN;
```

## Send Command:

```java
 PicoP_RC result = ALC_Api.PicoP_ALC_DrawTestPattern(connectionHandle, target, startPoint, size, foregroundColor, backgroundColor, pattern);
 ```

## Close Connection:

```java
 PicoP_RC result = PicoP_Operator.CloseConnection(libraryHandle.connectionInfoEx.getConnectionType());
 ```


# Compatibility:
- USB OTG enabled devices running Android 7.1+
- Firefly-RK3399 board running Android 7.1.1
