package edu.uiowa.slis.YouTubeTagLib.channel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class ChannelRelevant extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			if (!theChannel.commitNeeded) {
				pageContext.getOut().print(theChannel.getRelevant());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for relevant tag ");
		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspTagException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			return theChannel.getRelevant();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for relevant tag ");
		}
	}

	public void setRelevant(boolean relevant) throws JspTagException {
		try {
			Channel theChannel = (Channel)findAncestorWithClass(this, Channel.class);
			theChannel.setRelevant(relevant);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Channel for relevant tag ");
		}
	}

}
