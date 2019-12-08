package edu.uiowa.slis.YouTubeTagLib.channel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class ChannelChannelTitle extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			if (!theChannel.commitNeeded) {
				pageContext.getOut().print(theChannel.getChannelTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for channelTitle tag ");
		}
		return SKIP_BODY;
	}

	public String getChannelTitle() throws JspTagException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			return theChannel.getChannelTitle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for channelTitle tag ");
		}
	}

	public void setChannelTitle(String channelTitle) throws JspTagException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			theChannel.setChannelTitle(channelTitle);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for channelTitle tag ");
		}
	}

}
