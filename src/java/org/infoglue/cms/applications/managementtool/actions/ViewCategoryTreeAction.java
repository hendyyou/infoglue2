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
 * $Id: ViewCategoryTreeAction.java,v 1.1 2004/12/01 23:41:45 frank Exp $
 */
package org.infoglue.cms.applications.managementtool.actions;

import java.util.List;

import org.infoglue.cms.applications.common.actions.TreeViewAbstractAction;
import org.infoglue.cms.controllers.kernel.impl.simple.ContentTypeDefinitionController;
import org.infoglue.cms.controllers.kernel.impl.simple.RepositoryLanguageController;
import org.infoglue.cms.treeservice.ss.ContentNodeSupplier;
import org.infoglue.cms.treeservice.ss.ContentNodeVersionSupplier;
import org.infoglue.cms.treeservice.ss.CategoryNodeSupplier;
import org.infoglue.cms.exception.SystemException;

import com.frovi.ss.Tree.INodeSupplier;

public class ViewCategoryTreeAction extends TreeViewAbstractAction
{
	public static final String BINDING = "bindingView";

	private Integer select;

	public String doBindingView() throws Exception
	{
		super.doExecute();

		return BINDING;
	}

	/**
	 * @see org.infoglue.cms.applications.common.actions.TreeViewAbstractAction#getNodeSupplier()
	 */
	protected INodeSupplier getNodeSupplier() throws SystemException
	{
		return new CategoryNodeSupplier();
	}

	/**
	 * Returns the select.
	 * @return Integer
	 */
	public Integer getSelect()
	{
		return select;
	}

	/**
	 * Sets the select.
	 * @param select The select to set
	 */
	public void setSelect(Integer select)
	{
		this.select = select;
	}

}
