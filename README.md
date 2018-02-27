# Getting Started

## Add the JAR to Gradle.build:

```groovy
dependencies {
	// [...]
    compile files('libs/PicoP.jar')
}
```

## Init connection:

```java
Context appContext = InstrumentationRegistry.getTargetContext();

PicoP_LibraryInfo libraryInfo = new PicoP_LibraryInfo();
PicoP_Handle connectionHandle = new PicoP_Handle(eUSB);
PicoP_ConnectionInfo connectionInfo = connectionHandle.getConnectInfo();
connectionInfo.setConnectionContext(appContext);

PicoP_RC ret = ALC_Api.PicoP_ALC_OpenLibrary(connectionHandle);
assertEquals(PicoP_RC.eSUCCESS, ret);

ret = ALC_Api.PicoP_ALC_OpenConnection(connectionHandle, PicoP_ConnectionTypeE.eUSB, connectionInfo);
assertEquals(PicoP_RC.eSUCCESS, ret);
```

## Construct parameters:

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

## Send command:

```java
 PicoP_RC result = ALC_Api.PicoP_ALC_DrawTestPattern(connectionHandle, target, startPoint, size, foregroundColor, backgroundColor, pattern);
 ```