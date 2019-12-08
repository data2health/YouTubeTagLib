package edu.uiowa.slis.YouTubeTagLib.playlist;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class PlaylistTitle extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			if (!thePlaylist.commitNeeded) {
				pageContext.getOut().print(thePlaylist.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for title tag ");
		}
		return SKIP_BODY;
	}

	public String getTitle() throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			return thePlaylist.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for title tag ");
		}
	}

	public void setTitle(String title) throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			thePlaylist.setTitle(title);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for title tag ");
		}
	}

}
