package edu.uiowa.slis.YouTubeTagLib.videoThumbnail;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoThumbnailVideoId extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			if (!theVideoThumbnail.commitNeeded) {
				pageContext.getOut().print(theVideoThumbnail.getVideoId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for videoId tag ");
		}
		return SKIP_BODY;
	}

	public String getVideoId() throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			return theVideoThumbnail.getVideoId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for videoId tag ");
		}
	}

	public void setVideoId(String videoId) throws JspTagException {
		try {
			VideoThumbnail theVideoThumbnail = (VideoThumbnail)findAncestorWithClass(this, VideoThumbnail.class);
			theVideoThumbnail.setVideoId(videoId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing VideoThumbnail for videoId tag ");
		}
	}

}
