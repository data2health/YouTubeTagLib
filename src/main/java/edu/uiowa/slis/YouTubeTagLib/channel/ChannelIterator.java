package edu.uiowa.slis.YouTubeTagLib.channel;


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

@SuppressWarnings("serial")

public class ChannelIterator extends YouTubeTagLibBodyTagSupport {
    String channelId = null;
    String channelTitle = null;
    boolean relevant = false;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	private static final Log log =LogFactory.getLog(Channel.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String filterCriteria = null;
    String var = null;
    int rsCount = 0;

	public static String channelCount() throws JspTagException {
		int count = 0;
		ChannelIterator theIterator = new ChannelIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.channel"
						);

			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Channel iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean channelExists (String channelId) throws JspTagException {
		int count = 0;
		ChannelIterator theIterator = new ChannelIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.channel where 1=1"
						+ " and channel_id = ?"
						);

			stat.setString(1,channelId);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Channel iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {



      try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT youtube.channel.channel_id from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + generateFilterCriteria()
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            rs = stat.executeQuery();

            if (rs.next()) {
                channelId = rs.getString(1);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating Channel iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("youtube.channel");
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
            return "channel_id";
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
                channelId = rs.getString(1);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across Channel");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending Channel iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        channelId = null;
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



	public String getChannelId () {
		return channelId;
	}

	public void setChannelId (String channelId) {
		this.channelId = channelId;
	}

	public String getActualChannelId () {
		return channelId;
	}
}
