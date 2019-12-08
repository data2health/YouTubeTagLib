package edu.uiowa.slis.YouTubeTagLib.playlist;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class PlaylistEtag extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			if (!thePlaylist.commitNeeded) {
				pageContext.getOut().print(thePlaylist.getEtag());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for etag tag ");
		}
		return SKIP_BODY;
	}

	public String getEtag() throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			return thePlaylist.getEtag();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for etag tag ");
		}
	}

	public void setEtag(String etag) throws JspTagException {
		try {
			Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
			thePlaylist.setEtag(etag);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Playlist for etag tag ");
		}
	}

}
