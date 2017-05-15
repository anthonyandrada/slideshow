import java.awt.Point;

public class CaptionedImage 
{
	private int imgID;
	private String imgPath;
	private String imgCaption;
	private Point captionLoc;
	public CaptionedImage next;
	public CaptionedImage previous;

	public CaptionedImage(int ID, String caption, int x, int y, String path) 
	{
		imgID = ID;
		if (caption.isEmpty()) imgCaption = "CAPTION";
		else imgCaption = caption;
		captionLoc = new Point(x, y);
		imgPath = path;
		next = null;
		previous = null;
	}

	public int getImgID() 
	{
		return imgID;
	}

	public void setImgID(int imgID) 
	{
		this.imgID = imgID;
	}
	
	public String getImagePath() {
		return imgPath;
	}

	public String getImageCaption() {
		return imgCaption;
	}

	public Point getCaptionLocation() {
		return captionLoc;
	}

	public String toString() {
		return imgCaption;
	}

}