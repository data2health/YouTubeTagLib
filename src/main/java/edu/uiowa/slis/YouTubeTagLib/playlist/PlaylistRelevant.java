package edu.uiowa.slis.YouTubeTagLib.playlist;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class PlaylistRelevant extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			if (!thePlaylist.commitNeeded) {
				pageContext.getOut().print(thePlaylist.getRelevant());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for relevant tag ");
		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			return thePlaylist.getRelevant();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for relevant tag ");
		}
	}

	public void setRelevant(boolean relevant) throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			thePlaylist.setRelevant(relevant);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for relevant tag ");
		}
	}

}
