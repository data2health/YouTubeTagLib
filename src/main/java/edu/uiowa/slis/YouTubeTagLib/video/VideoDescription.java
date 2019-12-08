package edu.uiowa.slis.YouTubeTagLib.video;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoDescription extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			if (!theVideo.commitNeeded) {
				pageContext.getOut().print(theVideo.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for description tag ");
		}
		return SKIP_BODY;
	}

	public String getDescription() throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			return theVideo.getDescription();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for description tag ");
		}
	}

	public void setDescription(String description) throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			theVideo.setDescription(description);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for description tag ");
		}
	}

}
