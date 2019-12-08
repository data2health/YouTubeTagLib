package edu.uiowa.slis.YouTubeTagLib.videoThumbnail;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoThumbnailHeight extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			if (!theVideoThumbnail.commitNeeded) {
				pageContext.getOut().print(theVideoThumbnail.getHeight());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for height tag ");
		}
		return SKIP_BODY;
	}

	public int getHeight() throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			return theVideoThumbnail.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for height tag ");
		}
	}

	public void setHeight(int height) throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			theVideoThumbnail.setHeight(height);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for height tag ");
		}
	}

}
