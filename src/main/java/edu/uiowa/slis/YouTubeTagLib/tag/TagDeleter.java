package edu.uiowa.slis.YouTubeTagLib.tag;


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

public class TagDeleter extends YouTubeTagLibBodyTagSupport {
    String videoId = null;
    long seqnum = 0;
    String tag = null;
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
            stat = getConnection().prepareStatement("DELETE from youtube.tag where 1=1"
                                                        + (videoId == null ? "" : " and video_id = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        );
            if (videoId != null) stat.setString(webapp_keySeq++, videoId);
            if (seqnum != 0) stat.setLong(webapp_keySeq++, seqnum);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Tag deleter");
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
        seqnum = 0;
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

	public long getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (long seqnum) {
		this.seqnum = seqnum;
	}

	public long getActualSeqnum () {
		return seqnum;
	}
}
