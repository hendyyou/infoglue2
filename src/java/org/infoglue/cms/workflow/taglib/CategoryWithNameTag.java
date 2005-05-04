/* ===============================================================================
*
* Part of the InfoGlue Content Management Platform (www.infoglue.org)
*
* ===============================================================================
*
*  Copyright (C)
* 
* This program is free software; you can redistribute it and/or modify it under
* the terms of the GNU General Public License version 2, as published by the
* Free Software Foundation. See the file LICENSE.html for more information.
* 
* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY, including the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License along with
* this program; if not, write to the Free Software Foundation, Inc. / 59 Temple
* Place, Suite 330 / Boston, MA 02111-1307 / USA.
*
* ===============================================================================
*/
package org.infoglue.cms.workflow.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.infoglue.cms.controllers.kernel.impl.simple.CategoryController;
import org.infoglue.cms.entities.management.CategoryVO;

public class CategoryWithNameTag extends TagSupport {
	private String id;
	private String name;
	
	/**
	 * 
	 */
    public int doEndTag() throws JspException {
		processCategory();
        return EVAL_PAGE;
    }

	/**
	 * 
	 */
	private void processCategory() throws JspTagException {
		try {
			CategoryVO categoryVO = CategoryController.getController().findByPath(name);
			CategoryVO categoryVOWithChildren = CategoryController.getController().findWithChildren(categoryVO.getId());
			setResultAttribute(categoryVOWithChildren);
		} catch(Exception e) {
			e.printStackTrace();
			throw new JspTagException("CategoryWithNameTag.processCategory() : " + e);
		}
	}
	
	/**
	 * 
	 */
	protected void setResultAttribute(CategoryVO categoryVO) {
		pageContext.setAttribute(id, categoryVO);
	}

	/**
	 * 
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}
}
