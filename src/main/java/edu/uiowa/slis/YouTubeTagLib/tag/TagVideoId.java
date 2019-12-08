package edu.uiowa.slis.YouTubeTagLib.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class TagVideoId extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			if (!theTag.commitNeeded) {
				pageContext.getOut().print(theTag.getVideoId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for videoId tag ");
		}
		return SKIP_BODY;
	}

	public String getVideoId() throws JspTagException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			return theTag.getVideoId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for videoId tag ");
		}
	}

	public void setVideoId(String videoId) throws JspTagException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			theTag.setVideoId(videoId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for videoId tag ");
		}
	}

}
