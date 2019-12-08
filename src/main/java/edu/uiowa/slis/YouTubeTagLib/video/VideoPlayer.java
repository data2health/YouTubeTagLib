package edu.uiowa.slis.YouTubeTagLib.video;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class VideoPlayer extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			if (!theVideo.commitNeeded) {
				pageContext.getOut().print(theVideo.getPlayer());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for player tag ");
		}
		return SKIP_BODY;
	}

	public String getPlayer() throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			return theVideo.getPlayer();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for player tag ");
		}
	}

	public void setPlayer(String player) throws JspTagException {
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			theVideo.setPlayer(player);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Video for player tag ");
		}
	}

}
