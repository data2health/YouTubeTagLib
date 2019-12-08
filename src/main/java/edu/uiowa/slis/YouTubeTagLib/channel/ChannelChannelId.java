package edu.uiowa.slis.YouTubeTagLib.channel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class ChannelChannelId extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			if (!theChannel.commitNeeded) {
				pageContext.getOut().print(theChannel.getChannelId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for channelId tag ");
		}
		return SKIP_BODY;
	}

	public String getChannelId() throws JspTagException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			return theChannel.getChannelId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for channelId tag ");
		}
	}

	public void setChannelId(String channelId) throws JspTagException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			theChannel.setChannelId(channelId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for channelId tag ");
		}
	}

}
