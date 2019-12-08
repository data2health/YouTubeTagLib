package edu.uiowa.slis.YouTubeTagLib.videoThumbnail;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibBodyTagSupport;
import edu.uiowa.slis.YouTubeTagLib.video.Video;

@SuppressWarnings("serial")

public class VideoThumbnailDeleter extends YouTubeTagLibBodyTagSupport {
    String videoId = null;
    String size = null;
    String url = null;
    int width = 0;
    int height = 0;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Video theVideo = (Video)findAncestorWithClass(this, Video.class);
		if (theVideo!= null)
			parentEntities.addElement(theVideo);

		if (theVideo == null) {
		} else {
			videoId = theVideo.getVideoId();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from youtube.video_thumbnail where 1=1"
                                                        + (videoId == null ? "" : " and video_id = ?")
                                                        + (size == null ? "" : " and size = ?")
                                                        );
            if (videoId != null) stat.setString(webapp_keySeq++, videoId);
            if (size != null) stat.setString(webapp_keySeq++, size);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating VideoThumbnail deleter");
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
        size = null;
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

	public String getSize () {
		return size;
	}

	public void setSize (String size) {
		this.size = size;
	}

	public String getActualSize () {
		return size;
	}
}
