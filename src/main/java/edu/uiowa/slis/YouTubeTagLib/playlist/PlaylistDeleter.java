package edu.uiowa.slis.YouTubeTagLib.playlist;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibBodyTagSupport;
import edu.uiowa.slis.YouTubeTagLib.channel.Channel;

@SuppressWarnings("serial")

public class PlaylistDeleter extends YouTubeTagLibBodyTagSupport {
    String playlistId = null;
    String channelId = null;
    String etag = null;
    Date published = null;
    String title = null;
    String description = null;
    boolean relevant = false;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
		if (theChannel!= null)
			parentEntities.addElement(theChannel);

		if (theChannel == null) {
		} else {
			channelId = theChannel.getChannelId();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from youtube.playlist where 1=1"
                                                        + (playlistId == null ? "" : " and playlist_id = ?")
                                                        );
            if (playlistId != null) stat.setString(webapp_keySeq++, playlistId);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Playlist deleter");
        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {
		clearServiceState();
		return super.doEndTag();
	}

    private void clearServiceState() {
        playlistId = null;
        parentEntities = new Vector<YouTubeTagLibTagSupport>();

        this.rs = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }



	public String getPlaylistId () {
		return playlistId;
	}

	public void setPlaylistId (String playlistId) {
		this.playlistId = playlistId;
	}

	public String getActualPlaylistId () {
		return playlistId;
	}
}
