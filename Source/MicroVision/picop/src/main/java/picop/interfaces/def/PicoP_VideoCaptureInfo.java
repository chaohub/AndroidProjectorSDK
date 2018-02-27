/** \file PicoP_VideoCaptureInfo.java
* Copyright	: (c)2011 Microvision
*
* Description:  Video information
*
* $Author:     $ Amala P K
* $Date:       $ 21 sep 2012
* $Revision:   $ 0.1.0
* $Archive:    $
*/
/*---------------------------------------------------------------------------*/
package picop.interfaces.def;

public class PicoP_VideoCaptureInfo
{
	private PicoP_Point                 videoStartPosition; // video capture start line and pixel.           
	private PicoP_PolarityE             hSyncPolarity;      // polarity of HYSNC signal                       
	private PicoP_PolarityE             vSyncPolarity;      // polarity of VSYNC signal                        
	private PicoP_PolarityE             pixelClockEdge;     // active edge of pixel clock                      
	private PicoP_Extents               resolution;         // x & y resolution of video mode                 
	private float               		pixelAspectRatio;   // Pixel aspect ratio                             
	private PicoP_VideoColorSpaceE      colorSpace;         // color space of the input video mode            
	private PicoP_InterlaceE            interlaceField;     //interlace field to capture

    public PicoP_VideoCaptureInfo(){
		this.videoStartPosition = new PicoP_Point();
		this.hSyncPolarity = PicoP_PolarityE.ePOLARITY_NEGATIVE;
		this.vSyncPolarity = PicoP_PolarityE.ePOLARITY_NEGATIVE;
		this.pixelClockEdge = PicoP_PolarityE.ePOLARITY_NEGATIVE;
		this.resolution = new PicoP_Extents();
		this.pixelAspectRatio = 0;
		this.colorSpace = PicoP_VideoColorSpaceE.eCOLOR_SPACE_RGB;
		this.interlaceField = PicoP_InterlaceE.eINTERLACE_NONE;
	}
	public void setPicoP_VideoCaptureInfoValues(PicoP_Point l_videoStartPosition,PicoP_PolarityE l_hSyncPolarity,PicoP_PolarityE l_vSyncPolarity,PicoP_PolarityE l_pixelClockEdge,PicoP_Extents l_resolution,float l_pixelAspectRatio,PicoP_VideoColorSpaceE l_colorSpace,PicoP_InterlaceE l_interlaceField)
	{
		this.videoStartPosition = l_videoStartPosition;
		this.hSyncPolarity 		= l_hSyncPolarity;
		this.vSyncPolarity 		= l_vSyncPolarity;
		this.pixelClockEdge 	= l_pixelClockEdge;
		this.resolution 		= l_resolution;
		this.pixelAspectRatio 	= l_pixelAspectRatio;
		this.colorSpace 		= l_colorSpace;
		this.interlaceField 	= l_interlaceField;
	}
	public void setVideoStartPosition(int x, int y)
	{
		this.videoStartPosition.setPicoP_Point((short)x, (short)y);
	}
	public void setHSyncPolarity(int hSync)
	{
		this.hSyncPolarity = hSyncPolarity.intToEnum(hSync);
	}
	public void setVSyncPolarity(int vSync)
	{
		this.vSyncPolarity = vSyncPolarity.intToEnum(vSync);
	}
	public void setPixelClockEdge(int pixel)
	{
		this.pixelClockEdge = pixelClockEdge.intToEnum(pixel);
	}
	public void setResolution(int x, int y)
	{
		this.resolution.setPicoP_ExtentsValues((short)x, (short)y);
	}
	public void setPixelAspectRatio(float l_pixelAspectRatio)
	{
		this.pixelAspectRatio = l_pixelAspectRatio;
	}
	public void setColorSpace(int colorspace)
	{
		this.colorSpace = colorSpace.intToEnum(colorspace);
	}
	public void setInterlaceField(int interlaceF)
	{
		this.interlaceField = interlaceField.intToEnum(interlaceF);
	}

	public PicoP_Point getVideoStartPosition()
	{
		return videoStartPosition;
	}
	public PicoP_PolarityE getHSyncPolarity()
	{
		return hSyncPolarity;
	}
	public PicoP_PolarityE getVSyncPolarity()
	{
		return vSyncPolarity;
	}
	public PicoP_PolarityE getPixelClockEdge()
	{
		return pixelClockEdge;
	}
	public PicoP_Extents getResolution()
	{
		return resolution;
	}
	public float getPixelAspectRatio()
	{
		return pixelAspectRatio;
	}
	public PicoP_VideoColorSpaceE getColorSpace()
	{
		return colorSpace;
	}
	public PicoP_InterlaceE getInterlaceField()
	{
		return interlaceField;
	}
}
