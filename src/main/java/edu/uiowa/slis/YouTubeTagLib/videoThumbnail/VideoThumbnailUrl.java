package edu.uiowa.slis.YouTubeTagLib.videoThumbnail;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoThumbnailUrl extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			if (!theVideoThumbnail.commitNeeded) {
				pageContext.getOut().print(theVideoThumbnail.getUrl());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for url tag ");
		}
		return SKIP_BODY;
	}

	public String getUrl() throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			return theVideoThumbnail.getUrl();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for url tag ");
		}
	}

	public void setUrl(String url) throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			theVideoThumbnail.setUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for url tag ");
		}
	}

}
