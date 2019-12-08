package edu.uiowa.slis.YouTubeTagLib.videoThumbnail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.YouTubeTagLib.video.Video;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;
import edu.uiowa.slis.YouTubeTagLib.Sequence;

@SuppressWarnings("serial")

public class VideoThumbnail extends YouTubeTagLibTagSupport {

	static VideoThumbnail currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(VideoThumbnail.class);

	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	String videoId = null;
	String size = null;
	String url = null;
	int width = 0;
	int height = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Video theVideo = (Video)findAncestorWithClass(this, Video.class);
			if (theVideo!= null)
				parentEntities.addElement(theVideo);

			if (theVideo == null) {
			} else {
				videoId = theVideo.getVideoId();
			}

			VideoThumbnailIterator theVideoThumbnailIterator = (VideoThumbnailIterator)findAncestorWithClass(this, VideoThumbnailIterator.class);

			if (theVideoThumbnailIterator != null) {
				videoId = theVideoThumbnailIterator.getVideoId();
				size = theVideoThumbnailIterator.getSize();
			}

			if (theVideoThumbnailIterator == null && theVideo == null && size == null) {
				// no size was provided - the default is to assume that it is a new VideoThumbnail and to generate a new size
				size = null;
				log.debug("generating new VideoThumbnail " + size);
				insertEntity();
			} else {
				// an iterator or size was provided as an attribute - we need to load a VideoThumbnail from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select url,width,height from youtube.video_thumbnail where video_id = ? and size = ?");
				stmt.setString(1,videoId);
				stmt.setString(2,size);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (url == null)
						url = rs.getString(1);
					if (width == 0)
						width = rs.getInt(2);
					if (height == 0)
						height = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving size " + size);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update youtube.video_thumbnail set url = ?, width = ?, height = ? where video_id = ? and size = ?");
				stmt.setString(1,url);
				stmt.setInt(2,width);
				stmt.setInt(3,height);
				stmt.setString(4,videoId);
				stmt.setString(5,size);
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
			if (url == null)
				url = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into youtube.video_thumbnail(video_id,size,url,width,height) values (?,?,?,?,?)");
			stmt.setString(1,videoId);
			stmt.setString(2,size);
			stmt.setString(3,url);
			stmt.setInt(4,width);
			stmt.setInt(5,height);
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

	public String getSize () {
		if (commitNeeded)
			return "";
		else
			return size;
	}

	public void setSize (String size) {
		this.size = size;
	}

	public String getActualSize () {
		return size;
	}

	public String getUrl () {
		if (commitNeeded)
			return "";
		else
			return url;
	}

	public void setUrl (String url) {
		this.url = url;
		commitNeeded = true;
	}

	public String getActualUrl () {
		return url;
	}

	public int getWidth () {
		return width;
	}

	public void setWidth (int width) {
		this.width = width;
		commitNeeded = true;
	}

	public int getActualWidth () {
		return width;
	}

	public int getHeight () {
		return height;
	}

	public void setHeight (int height) {
		this.height = height;
		commitNeeded = true;
	}

	public int getActualHeight () {
		return height;
	}

	public static String videoIdValue() throws JspException {
		try {
			return currentInstance.getVideoId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function videoIdValue()");
		}
	}

	public static String sizeValue() throws JspException {
		try {
			return currentInstance.getSize();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sizeValue()");
		}
	}

	public static String urlValue() throws JspException {
		try {
			return currentInstance.getUrl();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function urlValue()");
		}
	}

	public static int widthValue() throws JspException {
		try {
			return currentInstance.getWidth();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function widthValue()");
		}
	}

	public static int heightValue() throws JspException {
		try {
			return currentInstance.getHeight();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function heightValue()");
		}
	}

	private void clearServiceState () {
		videoId = null;
		size = null;
		url = null;
		width = 0;
		height = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<YouTubeTagLibTagSupport>();

	}

}
