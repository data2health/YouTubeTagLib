package edu.uiowa.slis.YouTubeTagLib.video;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibBodyTagSupport;

@SuppressWarnings("serial")

public class VideoIterator extends YouTubeTagLibBodyTagSupport {
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

	private static final Log log =LogFactory.getLog(Video.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String filterCriteria = null;
    String var = null;
    int rsCount = 0;

	public static String videoCount() throws JspTagException {
		int count = 0;
		VideoIterator theIterator = new VideoIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.video"
						);

			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Video iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean videoExists (String videoId) throws JspTagException {
		int count = 0;
		VideoIterator theIterator = new VideoIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.video where 1=1"
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
			throw new JspTagException("Error: JDBC error generating Video iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {



      try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT youtube.video.video_id from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + generateFilterCriteria()
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            rs = stat.executeQuery();

            if (rs.next()) {
                videoId = rs.getString(1);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating Video iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("youtube.video");
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
            return "video_id";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    private String generateFilterCriteria() {
        if (filterCriteria != null) {
            return " and " + filterCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                videoId = rs.getString(1);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across Video");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending Video iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        videoId = null;
        parentEntities = new Vector<YouTubeTagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.filterCriteria = null;
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

    public String getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(String filterCriteria) {
        this.filterCriteria = filterCriteria;
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
