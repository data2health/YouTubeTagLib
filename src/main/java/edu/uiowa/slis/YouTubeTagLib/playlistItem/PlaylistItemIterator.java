package edu.uiowa.slis.YouTubeTagLib.playlistItem;


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
import edu.uiowa.slis.YouTubeTagLib.playlist.Playlist;
import edu.uiowa.slis.YouTubeTagLib.video.Video;

@SuppressWarnings("serial")

public class PlaylistItemIterator extends YouTubeTagLibBodyTagSupport {
    String playlistId = null;
    String videoId = null;
	Vector<YouTubeTagLibTagSupport> parentEntities = new Vector<YouTubeTagLibTagSupport>();

	private static final Log log =LogFactory.getLog(PlaylistItem.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String filterCriteria = null;
    String var = null;
    int rsCount = 0;

   boolean usePlaylist = false;
   boolean useVideo = false;

	public static String playlistItemCountByPlaylist(String playlistId) throws JspTagException {
		int count = 0;
		PlaylistItemIterator theIterator = new PlaylistItemIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.playlist_item where 1=1"
						+ " and playlist_id = ?"
						);

			stat.setString(1,playlistId);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating PlaylistItem iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean playlistHasPlaylistItem(String playlistId) throws JspTagException {
		return ! playlistItemCountByPlaylist(playlistId).equals("0");
	}

	public static String playlistItemCountByVideo(String videoId) throws JspTagException {
		int count = 0;
		PlaylistItemIterator theIterator = new PlaylistItemIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.playlist_item where 1=1"
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
			throw new JspTagException("Error: JDBC error generating PlaylistItem iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean videoHasPlaylistItem(String videoId) throws JspTagException {
		return ! playlistItemCountByVideo(videoId).equals("0");
	}

	public static Boolean playlistItemExists (String playlistId, String videoId) throws JspTagException {
		int count = 0;
		PlaylistItemIterator theIterator = new PlaylistItemIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.playlist_item where 1=1"
						+ " and playlist_id = ?"
						+ " and video_id = ?"
						);

			stat.setString(1,playlistId);
			stat.setString(2,videoId);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating PlaylistItem iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean playlistVideoExists (String playlistId, String videoId) throws JspTagException {
		int count = 0;
		PlaylistItemIterator theIterator = new PlaylistItemIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from youtube.playlist_item where 1=1"
						+ " and playlist_id = ?"
						+ " and video_id = ?"
						);

			stat.setString(1,playlistId);
			stat.setString(2,videoId);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating PlaylistItem iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
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


      try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT youtube.playlist_item.playlist_id, youtube.playlist_item.video_id from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + generateFilterCriteria()
                                                        + (playlistId == null ? "" : " and playlist_id = ?")
                                                        + (videoId == null ? "" : " and video_id = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (playlistId != null) stat.setString(webapp_keySeq++, playlistId);
            if (videoId != null) stat.setString(webapp_keySeq++, videoId);
            rs = stat.executeQuery();

            if (rs.next()) {
                playlistId = rs.getString(1);
                videoId = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating PlaylistItem iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("youtube.playlist_item");
       if (usePlaylist)
          theBuffer.append(", youtube.playlist");
       if (useVideo)
          theBuffer.append(", youtube.video");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (usePlaylist)
          theBuffer.append(" and playlist.playlist_id = playlist_item.playlist_id");
       if (useVideo)
          theBuffer.append(" and video.video_id = playlist_item.video_id");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "playlist_id,video_id";
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
                playlistId = rs.getString(1);
                videoId = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across PlaylistItem");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending PlaylistItem iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        playlistId = null;
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


   public boolean getUsePlaylist() {
        return usePlaylist;
    }

    public void setUsePlaylist(boolean usePlaylist) {
        this.usePlaylist = usePlaylist;
    }

   public boolean getUseVideo() {
        return useVideo;
    }

    public void setUseVideo(boolean useVideo) {
        this.useVideo = useVideo;
    }



	public String getPlaylistId () {
		return playlistId;
	}

	public void setPlaylistId (String playlistId) {
		this.playlistId = playlistId;
	}

	public String getActualPlaylistId () {
		return playlistId;
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
