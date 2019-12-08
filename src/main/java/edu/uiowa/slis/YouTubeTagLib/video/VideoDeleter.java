package edu.uiowa.slis.YouTubeTagLib.video;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibBodyTagSupport;

@SuppressWarnings("serial")

public class VideoDeleter extends YouTubeTagLibBodyTagSupport {
    String videoId = null;
    Date published = null;
    Object duration = null;
    boolean caption = false;
    String definition = null;
    String title = null;
    String description = null;
    long viewCount = 0;
    int likeCount = 0;
    int dislikeCount = 0;
    int favoriteCount = 0;
    int commentCount = 0;
    String player = null;
    boolean relevant = false;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from youtube.video where 1=1"
                                                        + (videoId == null ? "" : " and video_id = ?")
                                                        );
            if (videoId != null) stat.setString(webapp_keySeq++, videoId);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Video deleter");
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
