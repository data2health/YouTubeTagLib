package edu.uiowa.slis.YouTubeTagLib.channel;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibBodyTagSupport;

@SuppressWarnings("serial")

public class ChannelDeleter extends YouTubeTagLibBodyTagSupport {
    String channelId = null;
    String channelTitle = null;
    boolean relevant = false;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from youtube.channel where 1=1"
                                                        + (channelId == null ? "" : " and channel_id = ?")
                                                        );
            if (channelId != null) stat.setString(webapp_keySeq++, channelId);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Channel deleter");
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
        channelId = null;
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
