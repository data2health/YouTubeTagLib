package edu.uiowa.slis.YouTubeTagLib.playlistItem;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class PlaylistItemVideoId extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			PlaylistItem thePlaylistItem = (PlaylistItem)findAncestorWithClass(this, PlaylistItem.class);
			if (!thePlaylistItem.commitNeeded) {
				pageContext.getOut().print(thePlaylistItem.getVideoId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PlaylistItem for videoId tag ");
		}
		return SKIP_BODY;
	}

	public String getVideoId() throws JspTagException {
		try {
			PlaylistItem thePlaylistItem = (PlaylistItem)findAncestorWithClass(this, PlaylistItem.class);
			return thePlaylistItem.getVideoId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PlaylistItem for videoId tag ");
		}
	}

	public void setVideoId(String videoId) throws JspTagException {
		try {
			PlaylistItem thePlaylistItem = (PlaylistItem)findAncestorWithClass(this, PlaylistItem.class);
			thePlaylistItem.setVideoId(videoId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PlaylistItem for videoId tag ");
		}
	}

}
