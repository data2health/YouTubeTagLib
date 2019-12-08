package edu.uiowa.slis.YouTubeTagLib.playlistItem;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibBodyTagSupport;
import edu.uiowa.slis.YouTubeTagLib.playlist.Playlist;
import edu.uiowa.slis.YouTubeTagLib.video.Video;

@SuppressWarnings("serial")

public class PlaylistItemDeleter extends YouTubeTagLibBodyTagSupport {
    String playlistId = null;
    String videoId = null;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Playlist thePlaylist = (Playlist)findAncestorWithClass(this, Playlist.class);
		if (thePlaylist!= null)
			parentEntities.addElement(thePlaylist);
		Video theVideo = (Video)findAncestorWithClass(this, Video.class);
		if (theVideo!= null)
			parentEntities.addElement(theVideo);

		if (thePlaylist == null) {
		} else {
			playlistId = thePlaylist.getPlaylistId();
		}
		if (theVideo == null) {
		} else {
			videoId = theVideo.getVideoId();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from youtube.playlist_item where 1=1"
                                                        + (playlistId == null ? "" : " and playlist_id = ?")
                                                        + (videoId == null ? "" : " and video_id = ?")
                                                        );
            if (playlistId != null) stat.setString(webapp_keySeq++, playlistId);
            if (videoId != null) stat.setString(webapp_keySeq++, videoId);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating PlaylistItem deleter");
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
        videoId = null;
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

	public String getVideoId () {
		return videoId;
	}

	public void setVideoId (String videoId) {
		this.videoId = videoId;
	}

	public String getActualVideoId () {
		return videoId;
	}
}
