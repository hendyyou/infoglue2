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

package org.infoglue.cms.applications.managementtool.actions;

import org.infoglue.cms.applications.common.actions.InfoGlueAbstractAction;
import org.infoglue.cms.controllers.kernel.impl.simple.ServiceDefinitionController;

import java.util.List;

/**
 * 	Action class for usecase ViewListServiceDefinitionUCC 
 *
 *  @author Mattias Bogeblad
 */

public class ViewListServiceDefinitionAction extends InfoGlueAbstractAction 
{

	private List serviceDefinitionVOList;
	

	protected String doExecute() throws Exception 
	{
		this.serviceDefinitionVOList = ServiceDefinitionController.getController().getServiceDefinitionVOList();
	    return "success";
	}
	

	public List getServiceDefinitions()
	{
		return this.serviceDefinitionVOList;		
	}
	
}
