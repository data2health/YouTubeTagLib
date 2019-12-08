package edu.uiowa.slis.YouTubeTagLib.playlist;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class PlaylistPublishedToNow extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			thePlaylist.setPublishedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for published tag ");
		}
		return SKIP_BODY;
	}

	public Date getPublished() throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			return thePlaylist.getPublished();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for published tag ");
		}
	}

	public void setPublished( ) throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			thePlaylist.setPublishedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for published tag ");
		}
	}

}
