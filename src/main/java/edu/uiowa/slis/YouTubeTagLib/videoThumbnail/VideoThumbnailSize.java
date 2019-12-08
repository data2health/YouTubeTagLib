package edu.uiowa.slis.YouTubeTagLib.videoThumbnail;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoThumbnailSize extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			if (!theVideoThumbnail.commitNeeded) {
				pageContext.getOut().print(theVideoThumbnail.getSize());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for size tag ");
		}
		return SKIP_BODY;
	}

	public String getSize() throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			return theVideoThumbnail.getSize();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for size tag ");
		}
	}

	public void setSize(String size) throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			theVideoThumbnail.setSize(size);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for size tag ");
		}
	}

}
