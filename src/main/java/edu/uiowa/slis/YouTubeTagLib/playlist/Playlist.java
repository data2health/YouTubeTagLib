package edu.uiowa.slis.YouTubeTagLib.playlist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.YouTubeTagLib.channel.Channel;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")

public class Playlist extends YouTubeTagLibTagSupport {

	static Playlist currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Playlist.class);

	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	String playlistId = null;
	String channelId = null;
	String etag = null;
	Date published = null;
	String title = null;
	String description = null;
	boolean relevant = false;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			if (theChannel!= null)
				parentEntities.addElement(theChannel);

			if (theChannel == null) {
			} else {
				channelId = theChannel.getChannelId();
			}

			PlaylistIterator thePlaylistIterator = (PlaylistIterator)findAncestorWithClass(this, PlaylistIterator.class);

			if (thePlaylistIterator != null) {
				playlistId = thePlaylistIterator.getPlaylistId();
			}

			if (thePlaylistIterator == null && theChannel == null && playlistId == null) {
				// no playlistId was provided - the default is to assume that it is a new Playlist and to generate a new playlistId
				playlistId = null;
				log.debug("generating new Playlist " + playlistId);
				insertEntity();
			} else {
				// an iterator or playlistId was provided as an attribute - we need to load a Playlist from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select channel_id,etag,published,title,description,relevant from youtube.playlist where playlist_id = ?");
				stmt.setString(1,playlistId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (channelId == null)
						channelId = rs.getString(1);
					if (etag == null)
						etag = rs.getString(2);
					if (published == null)
						published = rs.getTimestamp(3);
					if (title == null)
						title = rs.getString(4);
					if (description == null)
						description = rs.getString(5);
					if (relevant == false)
						relevant = rs.getBoolean(6);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving playlistId " + playlistId);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update youtube.playlist set channel_id = ?, etag = ?, published = ?, title = ?, description = ?, relevant = ? where playlist_id = ?");
				stmt.setString(1,channelId);
				stmt.setString(2,etag);
				stmt.setTimestamp(3,published == null ? null : new java.sql.Timestamp(published.getTime()));
				stmt.setString(4,title);
				stmt.setString(5,description);
				stmt.setBoolean(6,relevant);
				stmt.setString(7,playlistId);
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
			if (channelId == null)
				channelId = "";
			if (etag == null)
				etag = "";
			if (title == null)
				title = "";
			if (description == null)
				description = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into youtube.playlist(playlist_id,channel_id,etag,published,title,description,relevant) values (?,?,?,?,?,?,?)");
			stmt.setString(1,playlistId);
			if (channelId == null) {
				stmt.setNull(2, java.sql.Types.INTEGER);
			} else {
				stmt.setString(2,channelId);
			}
			stmt.setString(3,etag);
			stmt.setTimestamp(4,published == null ? null : new java.sql.Timestamp(published.getTime()));
			stmt.setString(5,title);
			stmt.setString(6,description);
			stmt.setBoolean(7,relevant);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public String getPlaylistId () {
		if (commitNeeded)
			return "";
		else
			return playlistId;
	}

	public void setPlaylistId (String playlistId) {
		this.playlistId = playlistId;
	}

	public String getActualPlaylistId () {
		return playlistId;
	}

	public String getChannelId () {
		if (commitNeeded)
			return "";
		else
			return channelId;
	}

	public void setChannelId (String channelId) {
		this.channelId = channelId;
		commitNeeded = true;
	}

	public String getActualChannelId () {
		return channelId;
	}

	public String getEtag () {
		if (commitNeeded)
			return "";
		else
			return etag;
	}

	public void setEtag (String etag) {
		this.etag = etag;
		commitNeeded = true;
	}

	public String getActualEtag () {
		return etag;
	}

	public Date getPublished () {
		return published;
	}

	public void setPublished (Date published) {
		this.published = published;
		commitNeeded = true;
	}

	public Date getActualPublished () {
		return published;
	}

	public void setPublishedToNow ( ) {
		this.published = new java.util.Date();
		commitNeeded = true;
	}

	public String getTitle () {
		if (commitNeeded)
			return "";
		else
			return title;
	}

	public void setTitle (String title) {
		this.title = title;
		commitNeeded = true;
	}

	public String getActualTitle () {
		return title;
	}

	public String getDescription () {
		if (commitNeeded)
			return "";
		else
			return description;
	}

	public void setDescription (String description) {
		this.description = description;
		commitNeeded = true;
	}

	public String getActualDescription () {
		return description;
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

	public static String playlistIdValue() throws JspException {
		try {
			return currentInstance.getPlaylistId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function playlistIdValue()");
		}
	}

	public static String channelIdValue() throws JspException {
		try {
			return currentInstance.getChannelId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function channelIdValue()");
		}
	}

	public static String etagValue() throws JspException {
		try {
			return currentInstance.getEtag();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function etagValue()");
		}
	}

	public static Date publishedValue() throws JspException {
		try {
			return currentInstance.getPublished();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publishedValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	public static String descriptionValue() throws JspException {
		try {
			return currentInstance.getDescription();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function descriptionValue()");
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
		playlistId = null;
		channelId = null;
		etag = null;
		published = null;
		title = null;
		description = null;
		relevant = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<YouTubeTagLibTagSupport>();

	}

}
