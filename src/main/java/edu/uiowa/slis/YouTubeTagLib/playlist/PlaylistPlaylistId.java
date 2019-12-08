package edu.uiowa.slis.YouTubeTagLib.playlist;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class PlaylistPlaylistId extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			if (!thePlaylist.commitNeeded) {
				pageContext.getOut().print(thePlaylist.getPlaylistId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for playlistId tag ");
		}
		return SKIP_BODY;
	}

	public String getPlaylistId() throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			return thePlaylist.getPlaylistId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for playlistId tag ");
		}
	}

	public void setPlaylistId(String playlistId) throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			thePlaylist.setPlaylistId(playlistId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for playlistId tag ");
		}
	}

}
