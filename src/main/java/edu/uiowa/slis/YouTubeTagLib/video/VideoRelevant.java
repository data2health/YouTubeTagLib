package edu.uiowa.slis.YouTubeTagLib.video;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoRelevant extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			if (!theVideo.commitNeeded) {
				pageContext.getOut().print(theVideo.getRelevant());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for relevant tag ");
		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			return theVideo.getRelevant();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for relevant tag ");
		}
	}

	public void setRelevant(boolean relevant) throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			theVideo.setRelevant(relevant);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for relevant tag ");
		}
	}

}
