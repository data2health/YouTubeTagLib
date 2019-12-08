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

@SuppressWarnings("serial")

public class Channel extends YouTubeTagLibTagSupport {

	static Channel currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Channel.class);

	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	String channelId = null;
	String channelTitle = null;
	boolean relevant = false;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			ChannelIterator theChannelIterator = (ChannelIterator)findAncestorWithClass(this, ChannelIterator.class);

			if (theChannelIterator != null) {
				channelId = theChannelIterator.getChannelId();
			}

			if (theChannelIterator == null && channelId == null) {
				// no channelId was provided - the default is to assume that it is a new Channel and to generate a new channelId
				channelId = null;
				log.debug("generating new Channel " + channelId);
				insertEntity();
			} else {
				// an iterator or channelId was provided as an attribute - we need to load a Channel from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select channel_title,relevant from youtube.channel where channel_id = ?");
				stmt.setString(1,channelId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (channelTitle == null)
						channelTitle = rs.getString(1);
					if (relevant == false)
						relevant = rs.getBoolean(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving channelId " + channelId);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update youtube.channel set channel_title = ?, relevant = ? where channel_id = ?");
				stmt.setString(1,channelTitle);
				stmt.setBoolean(2,relevant);
				stmt.setString(3,channelId);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (channelTitle == null)
				channelTitle = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into youtube.channel(channel_id,channel_title,relevant) values (?,?,?)");
			stmt.setString(1,channelId);
			stmt.setString(2,channelTitle);
			stmt.setBoolean(3,relevant);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public String getChannelId () {
		if (commitNeeded)
			return "";
		else
			return channelId;
	}

	public void setChannelId (String channelId) {
		this.channelId = channelId;
	}

	public String getActualChannelId () {
		return channelId;
	}

	public String getChannelTitle () {
		if (commitNeeded)
			return "";
		else
			return channelTitle;
	}

	public void setChannelTitle (String channelTitle) {
		this.channelTitle = channelTitle;
		commitNeeded = true;
	}

	public String getActualChannelTitle () {
		return channelTitle;
	}

	public boolean getRelevant () {
		return relevant;
	}

	public void setRelevant (boolean relevant) {
		this.relevant = relevant;
		commitNeeded = true;
	}

	public boolean getActualRelevant () {
		return relevant;
	}

	public static String channelIdValue() throws JspException {
		try {
			return currentInstance.getChannelId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function channelIdValue()");
		}
	}

	public static String channelTitleValue() throws JspException {
		try {
			return currentInstance.getChannelTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function channelTitleValue()");
		}
	}

	public static boolean relevantValue() throws JspException {
		try {
			return currentInstance.getRelevant();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function relevantValue()");
		}
	}

	private void clearServiceState () {
		channelId = null;
		channelTitle = null;
		relevant = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<YouTubeTagLibTagSupport>();

	}

}
