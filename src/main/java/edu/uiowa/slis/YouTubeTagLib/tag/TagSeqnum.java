package edu.uiowa.slis.YouTubeTagLib.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class TagSeqnum extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			if (!theTag.commitNeeded) {
				pageContext.getOut().print(theTag.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public long getSeqnum() throws JspTagException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			return theTag.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for seqnum tag ");
		}
	}

	public void setSeqnum(long seqnum) throws JspTagException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			theTag.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for seqnum tag ");
		}
	}

}
