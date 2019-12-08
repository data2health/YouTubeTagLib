package edu.uiowa.slis.YouTubeTagLib.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.YouTubeTagLib.YouTubeTagLibTagSupport;

@SuppressWarnings("serial")
public class TagTag extends YouTubeTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			if (!theTag.commitNeeded) {
				pageContext.getOut().print(theTag.getTag());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for tag tag ");
		}
		return SKIP_BODY;
	}

	public String getTag() throws JspTagException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			return theTag.getTag();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for tag tag ");
		}
	}

	public void setTag(String tag) throws JspTagException {
		try {
			Tag theTag = (Tag)findAncestorWithClass(this, Tag.class);
			theTag.setTag(tag);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Tag for tag tag ");
		}
	}

}
