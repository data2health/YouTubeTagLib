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
import edu.uiowa.slis.YouTubeTagLib.Sequence;

@SuppressWarnings("serial")

public class Video extends YouTubeTagLibTagSupport {

	static Video currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Video.class);

	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	String videoId = null;
	Date published = null;
	String duration = null;
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

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			VideoIterator theVideoIterator = (VideoIterator)findAncestorWithClass(this, VideoIterator.class);

			if (theVideoIterator != null) {
				videoId = theVideoIterator.getVideoId();
			}

			if (theVideoIterator == null && videoId == null) {
				// no videoId was provided - the default is to assume that it is a new Video and to generate a new videoId
				videoId = null;
				log.debug("generating new Video " + videoId);
				insertEntity();
			} else {
				// an iterator or videoId was provided as an attribute - we need to load a Video from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select published,duration::text,caption,definition,title,description,view_count,like_count,dislike_count,favorite_count,comment_count,player,relevant from youtube.video where video_id = ?");
				stmt.setString(1,videoId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (published == null)
						published = rs.getTimestamp(1);
					if (duration == null)
						duration = rs.getString(2);
					if (caption == false)
						caption = rs.getBoolean(3);
					if (definition == null)
						definition = rs.getString(4);
					if (title == null)
						title = rs.getString(5);
					if (description == null)
						description = rs.getString(6);
					if (viewCount == 0)
						viewCount = rs.getLong(7);
					if (likeCount == 0)
						likeCount = rs.getInt(8);
					if (dislikeCount == 0)
						dislikeCount = rs.getInt(9);
					if (favoriteCount == 0)
						favoriteCount = rs.getInt(10);
					if (commentCount == 0)
						commentCount = rs.getInt(11);
					if (player == null)
						player = rs.getString(12);
					if (relevant == false)
						relevant = rs.getBoolean(13);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving videoId " + videoId);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update youtube.video set published = ?, duration = ?, caption = ?, definition = ?, title = ?, description = ?, view_count = ?, like_count = ?, dislike_count = ?, favorite_count = ?, comment_count = ?, player = ?, relevant = ? where video_id = ?");
				stmt.setTimestamp(1,published == null ? null : new java.sql.Timestamp(published.getTime()));
				stmt.setString(2,duration);
				stmt.setBoolean(3,caption);
				stmt.setString(4,definition);
				stmt.setString(5,title);
				stmt.setString(6,description);
				stmt.setLong(7,viewCount);
				stmt.setInt(8,likeCount);
				stmt.setInt(9,dislikeCount);
				stmt.setInt(10,favoriteCount);
				stmt.setInt(11,commentCount);
				stmt.setString(12,player);
				stmt.setBoolean(13,relevant);
				stmt.setString(14,videoId);
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
			if (definition == null)
				definition = "";
			if (title == null)
				title = "";
			if (description == null)
				description = "";
			if (player == null)
				player = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into youtube.video(video_id,published,duration,caption,definition,title,description,view_count,like_count,dislike_count,favorite_count,comment_count,player,relevant) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1,videoId);
			stmt.setTimestamp(2,published == null ? null : new java.sql.Timestamp(published.getTime()));
			stmt.setString(3,duration);
			stmt.setBoolean(4,caption);
			stmt.setString(5,definition);
			stmt.setString(6,title);
			stmt.setString(7,description);
			stmt.setLong(8,viewCount);
			stmt.setInt(9,likeCount);
			stmt.setInt(10,dislikeCount);
			stmt.setInt(11,favoriteCount);
			stmt.setInt(12,commentCount);
			stmt.setString(13,player);
			stmt.setBoolean(14,relevant);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public String getDuration () {
		return duration;
	}

	public void setDuration (String duration) {
		this.duration = duration;
		commitNeeded = true;
	}

	public Object getActualDuration () {
		return duration;
	}

	public boolean getCaption () {
		return caption;
	}

	public void setCaption (boolean caption) {
		this.caption = caption;
		commitNeeded = true;
	}

	public boolean getActualCaption () {
		return caption;
	}

	public String getDefinition () {
		if (commitNeeded)
			return "";
		else
			return definition;
	}

	public void setDefinition (String definition) {
		this.definition = definition;
		commitNeeded = true;
	}

	public String getActualDefinition () {
		return definition;
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

	public long getViewCount () {
		return viewCount;
	}

	public void setViewCount (long viewCount) {
		this.viewCount = viewCount;
		commitNeeded = true;
	}

	public long getActualViewCount () {
		return viewCount;
	}

	public int getLikeCount () {
		return likeCount;
	}

	public void setLikeCount (int likeCount) {
		this.likeCount = likeCount;
		commitNeeded = true;
	}

	public int getActualLikeCount () {
		return likeCount;
	}

	public int getDislikeCount () {
		return dislikeCount;
	}

	public void setDislikeCount (int dislikeCount) {
		this.dislikeCount = dislikeCount;
		commitNeeded = true;
	}

	public int getActualDislikeCount () {
		return dislikeCount;
	}

	public int getFavoriteCount () {
		return favoriteCount;
	}

	public void setFavoriteCount (int favoriteCount) {
		this.favoriteCount = favoriteCount;
		commitNeeded = true;
	}

	public int getActualFavoriteCount () {
		return favoriteCount;
	}

	public int getCommentCount () {
		return commentCount;
	}

	public void setCommentCount (int commentCount) {
		this.commentCount = commentCount;
		commitNeeded = true;
	}

	public int getActualCommentCount () {
		return commentCount;
	}

	public String getPlayer () {
		if (commitNeeded)
			return "";
		else
			return player;
	}

	public void setPlayer (String player) {
		this.player = player;
		commitNeeded = true;
	}

	public String getActualPlayer () {
		return player;
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

	public static String videoIdValue() throws JspException {
		try {
			return currentInstance.getVideoId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function videoIdValue()");
		}
	}

	public static Date publishedValue() throws JspException {
		try {
			return currentInstance.getPublished();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publishedValue()");
		}
	}

	public static Object durationValue() throws JspException {
		try {
			return currentInstance.getDuration();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function durationValue()");
		}
	}

	public static boolean captionValue() throws JspException {
		try {
			return currentInstance.getCaption();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function captionValue()");
		}
	}

	public static String definitionValue() throws JspException {
		try {
			return currentInstance.getDefinition();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function definitionValue()");
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

	public static long viewCountValue() throws JspException {
		try {
			return currentInstance.getViewCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function viewCountValue()");
		}
	}

	public static int likeCountValue() throws JspException {
		try {
			return currentInstance.getLikeCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function likeCountValue()");
		}
	}

	public static int dislikeCountValue() throws JspException {
		try {
			return currentInstance.getDislikeCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function dislikeCountValue()");
		}
	}

	public static int favoriteCountValue() throws JspException {
		try {
			return currentInstance.getFavoriteCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function favoriteCountValue()");
		}
	}

	public static int commentCountValue() throws JspException {
		try {
			return currentInstance.getCommentCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function commentCountValue()");
		}
	}

	public static String playerValue() throws JspException {
		try {
			return currentInstance.getPlayer();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function playerValue()");
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
		videoId = null;
		published = null;
		duration = null;
		caption = false;
		definition = null;
		title = null;
		description = null;
		viewCount = 0;
		likeCount = 0;
		dislikeCount = 0;
		favoriteCount = 0;
		commentCount = 0;
		player = null;
		relevant = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<YouTubeTagLibTagSupport>();

	}

}
