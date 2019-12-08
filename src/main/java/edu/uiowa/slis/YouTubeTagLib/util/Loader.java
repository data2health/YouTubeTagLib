package edu.uiowa.slis.YouTubeTagLib.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Loader {
    static Logger logger = Logger.getLogger(Loader.class);
    static Properties prop_file = PropertyLoader.loadProperties("youtube");
    static Connection conn = null;

    // GET https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=UCHN1NLDCeq3IO1nQPZqddzQ&key=[YOUR_API_KEY] HTTP/1.1


    // GET https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLqHAuecefulQUdT28h7VqfajqtwfuCLRI&key=[YOUR_API_KEY] HTTP/1.1


    // GET https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2CtopicDetails%2Cstatistics&id=D-_lsO9dsgY&key=[YOUR_API_KEY] HTTP/1.1


    public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException, MalformedURLException, IOException, InterruptedException {
	PropertyConfigurator.configure(args[0]);
	conn = getConnection();
	
//	search();
//	channels();
//	playlists();
//	playlist_items();
	videos();
    }
    
    public static void search() throws JSONException, MalformedURLException, IOException, SQLException, InterruptedException {
	String api_key = prop_file.getProperty("key");
	String continuation = null;
	int maxResults = 50;
	int year = 2019;
	String query = "i2b2";

	do {
	    JSONObject object = new JSONObject(new JSONTokener(
		    new InputStreamReader((new URL("https://www.googleapis.com/youtube/v3/search?part=snippet"
							+ "&key=" + api_key
		    					+ "&q=" + query.replace(' ', '+')
		    					+ "&maxResults=" + maxResults
			    				+ (continuation == null ? "" : "&pageToken="+continuation)
		    					+ (year == 0 ? "" : "&publishedAfter=" + year + "-01-01T00:00:00Z")
		    					+ (year == 0 ? "" : "&publishedBefore=" + (year+1) + "-01-01T00:00:00Z")
			    		)).openConnection().getInputStream())));
	    logger.info("result: " + object.toString(3));
	    continuation = object.optString("nextPageToken");
	    logger.info("next page: " + continuation);
	    JSONArray theArray = object.getJSONArray("items");
	    for (int i = 0; i < theArray.length(); i++) {
		JSONObject item = theArray.getJSONObject(i);
		String videoID = item.getJSONObject("id").optString("videoId");
		logger.info("item: " + item.toString(3));

		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw values (?,?,?::jsonb)");
		citeStmt.setInt(1, year);
		citeStmt.setString(2, videoID);
		citeStmt.setString(3, item.toString());
		citeStmt.executeUpdate();
		citeStmt.close();
	    }
	    if (continuation != null && !continuation.equals(""))
		Thread.sleep(60*1000);
	} while (continuation != null && !continuation.equals(""));
    }

    public static void channels() throws SQLException {
	PreparedStatement stmt = conn.prepareStatement("insert into youtube.channel"
							+ " select distinct channel_id, channel_title"
							+ " from youtube.search_result"
							+ " where channel_id not in (select channel_id from youtube.channel)");
	int count = stmt.executeUpdate();
	stmt.close();
	logger.info("new channels: " + count);
    }


    public static void playlists() throws JSONException, MalformedURLException, IOException, SQLException, InterruptedException {
	PreparedStatement stmt = conn.prepareStatement("select channel_id,channel_title from youtube.channel"
							+ " where relevant"
							+ " and channel_id not in (select channel_id from youtube.raw_playlist_empty)"
							+ " and channel_id not in (select channel_id from youtube.raw_playlist)");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String id = rs.getString(1);
	    playlist(id);
	    Thread.sleep(2*1000);
	}
	stmt.close();

	stmt = conn.prepareStatement("insert into youtube.playlist"
					+ " select playlist_id,channel_id,etag,published,title,description"
					+ " from youtube.playlist_staging"
					+ " where playlist_id not in (select playlist_id from youtube.playlist natural join youtube.channel where channel.relevant)");
	int count = stmt.executeUpdate();
	stmt.close();
	logger.info("new channels: " + count);
    }

    public static void playlist(String id) throws JSONException, MalformedURLException, IOException, SQLException, InterruptedException {
	String api_key = prop_file.getProperty("key");
	String continuation = null;
	int maxResults = 50;

	do {
	    JSONObject object = null;;
	    try {
		object = new JSONObject(new JSONTokener(
		    new InputStreamReader((new URL("https://www.googleapis.com/youtube/v3/playlists?part=snippet"
							+ "&key=" + api_key
		    					+ "&channelId=" + id
		    					+ "&maxResults=" + maxResults
			    				+ (continuation == null ? "" : "&pageToken="+continuation)
			    		)).openConnection().getInputStream())));
	    } catch (java.io.FileNotFoundException e) {
		logger.info("** not found: " + id);
		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw_playlist_empty values (?)");
		citeStmt.setString(1, id);
		citeStmt.executeUpdate();
		citeStmt.close();
		return;
	    }
	    logger.info("result: " + object.toString(3));
	    continuation = object.optString("nextPageToken");
	    logger.info("next page: " + continuation);
	    JSONArray theArray = object.getJSONArray("items");
	    for (int i = 0; i < theArray.length(); i++) {
		JSONObject item = theArray.getJSONObject(i);
		logger.info("item: " + item.toString(3));

		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw_playlist values (?,?::jsonb)");
		citeStmt.setString(1, id);
		citeStmt.setString(2, item.toString());
		citeStmt.executeUpdate();
		citeStmt.close();
	    }
	    if (theArray.length() == 0) {
		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw_playlist_empty values (?)");
		citeStmt.setString(1, id);
		citeStmt.executeUpdate();
		citeStmt.close();
	    }
	    if (continuation != null && !continuation.equals(""))
		Thread.sleep(10*1000);
	} while (continuation != null && !continuation.equals(""));
    }
    
    public static void playlist_items() throws JSONException, MalformedURLException, IOException, SQLException, InterruptedException {
	PreparedStatement stmt = conn.prepareStatement("select playlist_id,title from youtube.playlist"
							+ " where channel_id in (select channel_id from youtube.channel where relevant)"
							+ " and playlist_id not in (select playlist_id from youtube.raw_playlist_item_empty)"
							+ " and playlist_id not in (select playlist_id from youtube.raw_playlist_item)"
							+ " order by 1");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String id = rs.getString(1);
	    String title = rs.getString(2);
	    logger.info("*** play list: " + title);
	    playlist_item(id);
	    Thread.sleep(2*1000);
	}
	stmt.close();
    }

    public static void playlist_item(String id) throws JSONException, MalformedURLException, IOException, SQLException, InterruptedException {
	String api_key = prop_file.getProperty("key");
	String continuation = null;
	int maxResults = 50;

	do {
	    JSONObject object = new JSONObject(new JSONTokener(
		    new InputStreamReader((new URL("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet"
							+ "&key=" + api_key
		    					+ "&playlistId=" + id
		    					+ "&maxResults=" + maxResults
			    				+ (continuation == null ? "" : "&pageToken="+continuation)
			    		)).openConnection().getInputStream())));
	    logger.info("result: " + object.toString(3));
	    continuation = object.optString("nextPageToken");
	    logger.info("next page: " + continuation);
	    JSONArray theArray = object.getJSONArray("items");
	    for (int i = 0; i < theArray.length(); i++) {
		JSONObject item = theArray.getJSONObject(i);
		logger.info("item: " + item.toString(3));

		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw_playlist_item values (?,?::jsonb)");
		citeStmt.setString(1, id);
		citeStmt.setString(2, item.toString());
		citeStmt.executeUpdate();
		citeStmt.close();
	    }
	    if (theArray.length() == 0) {
		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw_playlist_item_empty values (?)");
		citeStmt.setString(1, id);
		citeStmt.executeUpdate();
		citeStmt.close();
	    }
	    if (continuation != null && !continuation.equals(""))
		Thread.sleep(2*1000);
	} while (continuation != null && !continuation.equals(""));
    }

    public static void videos() throws JSONException, MalformedURLException, IOException, SQLException, InterruptedException {
	int count = 0;
	StringBuffer list = new StringBuffer();
	PreparedStatement stmt = conn.prepareStatement("select video_id from youtube.playlist_item"
							+ " where video_id not in (select video_id from youtube.raw_video_empty)"
							+ " and video_id not in (select video_id from youtube.raw_video)"
							+ " and playlist_id in (select playlist_id from youtube.playlist,youtube.channel where playlist.channel_id=channel.channel_id and channel.relevant) order by 1");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String id = rs.getString(1);
	    logger.debug("id: " + id);
	    list.append((list.length() == 0 ? "" : ",")+id);
	    if ((++count % 50) == 0) {
		logger.debug("ids: " + list.toString());
		video(list.toString());
		list = new StringBuffer();
	    }
	}
	stmt.close();

	if (list.length() > 0) {
	    logger.debug("ids: " + list.toString());
	    list = new StringBuffer();
	}

	stmt = conn.prepareStatement("insert into youtube.video"
		+ " select distinct video_id,published,duration,caption,definition,title,description,view_count,like_count,dislike_count,favorite_count,comment_count,player"
		+ " from youtube.video_staging"
		+ " where not exists (select video_id from youtube.video where video.video_id = video_staging.video_id)"
		+ "   and exists (select video_id from youtube.playlist_item natural join youtube.playlist where playlist.relevant and playlist_item.video_id = video_staging.video_id)");
	count = stmt.executeUpdate();
	stmt.close();
	logger.info("new videos: " + count);

	stmt = conn.prepareStatement("insert into youtube.video_thumbnail"
		+ " select video_id,size,url,width,height"
		+ " from youtube.video_thumbnail_staging"
		+ " where url is not null"
		+ "   and not exists (select video_id from youtube.video_thumbnail where video_thumbnail.video_id = video_thumbnail_staging.video_id)"
		+ "   and exists (select video_id from youtube.video where video.video_id = video_thumbnail_staging.video_id)");
	count = stmt.executeUpdate();
	stmt.close();
	logger.info("new thumbnails: " + count);

	stmt = conn.prepareStatement("insert into youtube.tag"
		+ " select video_id,seqnum,tag"
		+ " from youtube.tag_staging"
		+ " where not exists (select video_id from youtube.tag where tag.video_id = tag_staging.video_id)"
		+ "   and exists (select video_id from youtube.video where video.video_id = tag_staging.video_id)");
	count = stmt.executeUpdate();
	stmt.close();
	logger.info("new tags: " + count);
    }

    public static void video(String list) throws JSONException, MalformedURLException, IOException, SQLException, InterruptedException {
	String api_key = prop_file.getProperty("key");
	String continuation = null;

	do {
	    JSONObject object = new JSONObject(new JSONTokener(
		    new InputStreamReader((new URL("https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2CtopicDetails%2Cstatistics%2Cplayer"
							+ "&key=" + api_key
		    					+ "&id=" + list
//		    					+ "&maxResults=" + maxResults
			    				+ (continuation == null ? "" : "&pageToken="+continuation)
			    		)).openConnection().getInputStream())));
	    logger.info("result: " + object.toString(3));
	    continuation = object.optString("nextPageToken");
	    logger.info("next page: " + continuation);
	    JSONArray theArray = object.getJSONArray("items");
	    String id = null;
	    for (int i = 0; i < theArray.length(); i++) {
		JSONObject item = theArray.getJSONObject(i);
		id = item.optString("id");
		logger.info("item: " + item.toString(3));

		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw_video values (?,?::jsonb)");
		citeStmt.setString(1, id);
		citeStmt.setString(2, item.toString());
		citeStmt.executeUpdate();
		citeStmt.close();
	    }
	    if (theArray.length() == 0 && id != null) {
		PreparedStatement citeStmt = conn.prepareStatement("insert into youtube.raw_video_empty values (?)");
		citeStmt.setString(1, id);
		citeStmt.executeUpdate();
		citeStmt.close();
	    }
	} while (continuation != null && !continuation.equals(""));
    }

    static Connection getConnection() throws ClassNotFoundException, SQLException {
	Connection local = null;
	Properties props = new Properties();
	props.setProperty("user", prop_file.getProperty("jdbc.user"));
	props.setProperty("password", prop_file.getProperty("jdbc.password"));
	Class.forName("org.postgresql.Driver");
	local = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);
	return local;
    }
}
