package edu.uiowa.slis.YouTubeTagLib.videoThumbnail;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoThumbnailWidth extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			if (!theVideoThumbnail.commitNeeded) {
				pageContext.getOut().print(theVideoThumbnail.getWidth());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for width tag ");
		}
		return SKIP_BODY;
	}

	public int getWidth() throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			return theVideoThumbnail.getWidth();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for width tag ");
		}
	}

	public void setWidth(int width) throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			theVideoThumbnail.setWidth(width);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for width tag ");
		}
	}

}
