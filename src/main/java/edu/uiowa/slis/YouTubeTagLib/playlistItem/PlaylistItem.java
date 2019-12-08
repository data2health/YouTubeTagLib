package edu.uiowa.slis.YouTubeTagLib.playlistItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.YouTubeTagLib.playlist.Playlist;
import edu.uiowa.slis.YouTubeTagLib.video.Video;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")

public class PlaylistItem extends YouTubeTagLibTagSupport {

	static PlaylistItem currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(PlaylistItem.class);

	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	String playlistId = null;
	String videoId = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
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

			PlaylistItemIterator thePlaylistItemIterator = (PlaylistItemIterator)findAncestorWithClass(this, PlaylistItemIterator.class);

			if (thePlaylistItemIterator != null) {
				playlistId = thePlaylistItemIterator.getPlaylistId();
				videoId = thePlaylistItemIterator.getVideoId();
			}

			if (thePlaylistItemIterator == null && thePlaylist == null && theVideo == null && playlistId == null) {
				// no playlistId was provided - the default is to assume that it is a new PlaylistItem and to generate a new playlistId
				playlistId = null;
				log.debug("generating new PlaylistItem " + playlistId);
				insertEntity();
			} else if (thePlaylistItemIterator == null && thePlaylist != null && theVideo == null) {
				// an playlistId was provided as an attribute - we need to load a PlaylistItem from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select video_id from youtube.playlist_item where playlist_id = ?");
				stmt.setString(1,playlistId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (videoId == null)
						videoId = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (thePlaylistItemIterator == null && thePlaylist == null && theVideo != null) {
				// an playlistId was provided as an attribute - we need to load a PlaylistItem from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select playlist_id from youtube.playlist_item where video_id = ?");
				stmt.setString(1,videoId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (playlistId == null)
						playlistId = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or playlistId was provided as an attribute - we need to load a PlaylistItem from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from youtube.playlist_item where playlist_id = ? and video_id = ?");
				stmt.setString(1,playlistId);
				stmt.setString(2,videoId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
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
				PreparedStatement stmt = getConnection().prepareStatement("update youtube.playlist_item set where playlist_id = ? and video_id = ?");
				stmt.setString(1,playlistId);
				stmt.setString(2,videoId);
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
			PreparedStatement stmt = getConnection().prepareStatement("insert into youtube.playlist_item(playlist_id,video_id) values (?,?)");
			stmt.setString(1,playlistId);
			stmt.setString(2,videoId);
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

	public String getVideoId () {
		if (commitNeeded)
			return "";
		else
			return videoId;
	}

	public void setVideoId (String videoId) {
		this.videoId = videoId;
	}

	public String getActualVideoId () {
		return videoId;
	}

	public static String playlistIdValue() throws JspException {
		try {
			return currentInstance.getPlaylistId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function playlistIdValue()");
		}
	}

	public static String videoIdValue() throws JspException {
		try {
			return currentInstance.getVideoId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function videoIdValue()");
		}
	}

	private void clearServiceState () {
		playlistId = null;
		videoId = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<YouTubeTagLibTagSupport>();

	}

}
