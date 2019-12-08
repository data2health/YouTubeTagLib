package edu.uiowa.slis.YouTubeTagLib.playlistItem;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class PlaylistItemPlaylistId extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			PlaylistItem thePlaylistItem = (PlaylistItem)findAncestorWithClass(this, PlaylistItem.class);
			if (!thePlaylistItem.commitNeeded) {
				pageContext.getOut().print(thePlaylistItem.getPlaylistId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PlaylistItem for playlistId tag ");
		}
		return SKIP_BODY;
	}

	public String getPlaylistId() throws JspTagException {
		try {
			PlaylistItem thePlaylistItem = (PlaylistItem)findAncestorWithClass(this, PlaylistItem.class);
			return thePlaylistItem.getPlaylistId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PlaylistItem for playlistId tag ");
		}
	}

	public void setPlaylistId(String playlistId) throws JspTagException {
		try {
			PlaylistItem thePlaylistItem = (PlaylistItem)findAncestorWithClass(this, PlaylistItem.class);
			thePlaylistItem.setPlaylistId(playlistId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PlaylistItem for playlistId tag ");
		}
	}

}
