package edu.uiowa.slis.YouTubeTagLib.video;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoLikeCount extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			if (!theVideo.commitNeeded) {
				pageContext.getOut().print(theVideo.getLikeCount());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for likeCount tag ");
		}
		return SKIP_BODY;
	}

	public int getLikeCount() throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			return theVideo.getLikeCount();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for likeCount tag ");
		}
	}

	public void setLikeCount(int likeCount) throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			theVideo.setLikeCount(likeCount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for likeCount tag ");
		}
	}

}
