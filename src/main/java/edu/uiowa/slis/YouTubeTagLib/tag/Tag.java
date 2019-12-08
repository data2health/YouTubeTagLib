package edu.uiowa.slis.YouTubeTagLib.tag;

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

@SuppressWarnings("serial")

public class Tag extends YouTubeTagLibTagSupport {

	static Tag currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Tag.class);

	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	String videoId = null;
	long seqnum = 0;
	String tag = null;

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

			TagIterator theTagIterator = (TagIterator)findAncestorWithClass(this, TagIterator.class);

			if (theTagIterator != null) {
				videoId = theTagIterator.getVideoId();
				seqnum = theTagIterator.getSeqnum();
			}

			if (theTagIterator == null && theVideo == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Tag and to generate a new seqnum
				seqnum = 0;
				log.debug("generating new Tag " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Tag from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select tag from youtube.tag where video_id = ? and seqnum = ?");
				stmt.setString(1,videoId);
				stmt.setLong(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (tag == null)
						tag = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving seqnum " + seqnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update youtube.tag set tag = ? where video_id = ? and seqnum = ?");
				stmt.setString(1,tag);
				stmt.setString(2,videoId);
				stmt.setLong(3,seqnum);
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
			if (tag == null)
				tag = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into youtube.tag(video_id,seqnum,tag) values (?,?,?)");
			stmt.setString(1,videoId);
			stmt.setLong(2,seqnum);
			stmt.setString(3,tag);
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

	public long getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (long seqnum) {
		this.seqnum = seqnum;
	}

	public long getActualSeqnum () {
		return seqnum;
	}

	public String getTag () {
		if (commitNeeded)
			return "";
		else
			return tag;
	}

	public void setTag (String tag) {
		this.tag = tag;
		commitNeeded = true;
	}

	public String getActualTag () {
		return tag;
	}

	public static String videoIdValue() throws JspException {
		try {
			return currentInstance.getVideoId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function videoIdValue()");
		}
	}

	public static long seqnumValue() throws JspException {
		try {
			return currentInstance.getSeqnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnumValue()");
		}
	}

	public static String tagValue() throws JspException {
		try {
			return currentInstance.getTag();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function tagValue()");
		}
	}

	private void clearServiceState () {
		videoId = null;
		seqnum = 0;
		tag = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<YouTubeTagLibTagSupport>();

	}

}
