package edu.uiowa.slis.YouTubeTagLib.tag;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibBodyTagSupport;
import edu.uiowa.slis.YouTubeTagLib.video.Video;

@SuppressWarnings("serial")

public class TagIterator extends YouTubeTagLibBodyTagSupport {
    String videoId = null;
    long seqnum = 0;
    String tag = null;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	private static final Log log =LogFactory.getLog(Tag.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

	public static String tagCountByVideo(String videoId) throws JspTagException {
		int count = 0;
		TagIterator theIterator = new TagIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.tag where 1=1"
						+ " and video_id = ?"
						);

			stat.setString(1,videoId);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Tag iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean videoHasTag(String videoId) throws JspTagException {
		return ! tagCountByVideo(videoId).equals("0");
	}

	public static Boolean tagExists (String videoId, String seqnum) throws JspTagException {
		int count = 0;
		TagIterator theIterator = new TagIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.tag where 1=1"
						+ " and video_id = ?"
						+ " and seqnum = ?"
						);

			stat.setString(1,videoId);
			stat.setLong(2,Long.parseLong(seqnum));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Tag iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Video theVideo = (Video)findAncestorWithClass(this, Video.class);
		if (theVideo!= null)
			parentEntities.addElement(theVideo);

		if (theVideo == null) {
		} else {
			videoId = theVideo.getVideoId();
		}


      try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT youtube.tag.video_id, youtube.tag.seqnum from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (videoId == null ? "" : " and video_id = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (videoId != null) stat.setString(webapp_keySeq++, videoId);
            rs = stat.executeQuery();

            if (rs.next()) {
                videoId = rs.getString(1);
                seqnum = rs.getLong(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating Tag iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("youtube.tag");
      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "video_id,seqnum";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                videoId = rs.getString(1);
                seqnum = rs.getLong(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across Tag");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending Tag iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        videoId = null;
        seqnum = 0;
        parentEntities = new Vector<YouTubeTagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getLimitCriteria() {
        return limitCriteria;
    }

    public void setLimitCriteria(int limitCriteria) {
        this.limitCriteria = limitCriteria;
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
