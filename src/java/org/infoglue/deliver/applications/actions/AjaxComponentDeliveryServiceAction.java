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


package org.infoglue.deliver.applications.actions;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.tag.common.core.CatchTag;
import org.exolab.castor.jdo.Database;
import org.infoglue.cms.applications.common.actions.InfoGlueAbstractAction;
import org.infoglue.cms.controllers.kernel.impl.simple.CastorDatabaseService;
import org.infoglue.cms.controllers.kernel.impl.simple.ComponentController;
import org.infoglue.cms.controllers.kernel.impl.simple.LanguageController;
import org.infoglue.cms.controllers.kernel.impl.simple.ServerNodeController;
import org.infoglue.cms.controllers.kernel.impl.simple.UserControllerProxy;
import org.infoglue.cms.security.InfoGluePrincipal;
import org.infoglue.cms.util.CmsPropertyHandler;
import org.infoglue.deliver.applications.databeans.CacheEvictionBean;
import org.infoglue.deliver.controllers.kernel.impl.simple.ExtranetController;
import org.infoglue.deliver.controllers.kernel.impl.simple.PageEditorHelper;
import org.infoglue.deliver.util.CacheController;
import org.infoglue.deliver.util.RequestAnalyser;
import org.infoglue.deliver.util.ThreadMonitor;


/**
 * This is the action supplying all ajax calls for component aspects for the delivery engine.
 *
 * @author Mattias Bogeblad
 */

public class AjaxComponentDeliveryServiceAction extends InfoGlueAbstractAction 
{
    private final static Logger logger = Logger.getLogger(AjaxComponentDeliveryServiceAction.class.getName());
  
    /**
     * This method will return all properties for a component. 
     */
         
    public String doGetComponentPropertyDiv() throws Exception
    {
    	StringBuffer propertiesDiv = new StringBuffer();

    	String repositoryIdString 		= this.getRequest().getParameter("repositoryId");
    	String siteNodeIdString 		= this.getRequest().getParameter("siteNodeId");
    	String languageIdString 		= this.getRequest().getParameter("languageId");
    	String componentIdString 		= this.getRequest().getParameter("componentId");
    	String contentIdString 			= this.getRequest().getParameter("contentId");
    	String componentContentIdString = this.getRequest().getParameter("componentContentId");
    	String slotName 				= this.getRequest().getParameter("slotName");
    	String showSimple 				= this.getRequest().getParameter("showSimple");
    	String showLegend 				= this.getRequest().getParameter("showLegend");
    	String targetDiv 				= this.getRequest().getParameter("targetDivId");

    	Integer repositoryId = new Integer(repositoryIdString);
    	Integer siteNodeId = new Integer(siteNodeIdString);
    	Integer languageId = new Integer(languageIdString);
    	Integer componentId = new Integer(componentIdString);
    	Integer contentId = new Integer(contentIdString);
    	Integer componentContentId = new Integer(componentContentIdString);
    		    
    	Database db = CastorDatabaseService.getDatabase();
    	
    	try
    	{
    		beginTransaction(db);
    	
        	InfoGluePrincipal principal = this.getInfoGluePrincipal();
        	String cmsUserName = (String)this.getHttpSession().getAttribute("cmsUserName");
       	 	System.out.println("cmsUserName:" + cmsUserName);
        	if(cmsUserName != null)
       	 		principal = UserControllerProxy.getController(db).getUser(cmsUserName);
        	else
        		principal = (InfoGluePrincipal)this.getAnonymousPrincipal();
        	
       	 	System.out.println("cmsUserName:" + cmsUserName);
        	Locale locale = this.getLocale();
        	if(languageId != null)
        		locale = LanguageController.getController().getLocaleWithId(languageId);
        	
        	if(slotName == null)
        		slotName = "";
        	
    		PageEditorHelper peh = new PageEditorHelper();
	    	String componentPropertiesDiv = peh.getComponentPropertiesDiv(db, principal, this.getRequest(), locale, repositoryId, siteNodeId, languageId, contentId, componentId, componentContentId, slotName, showSimple, this.getOriginalFullURL(), showLegend, targetDiv);
	    	propertiesDiv.append(componentPropertiesDiv);
    	
	    	commitTransaction(db);
    	}
    	catch (Exception e) 
    	{
    		rollbackTransaction(db);
    		e.printStackTrace();
		}
     	    	
        this.getResponse().setContentType("text/plain");
        this.getResponse().getWriter().println(propertiesDiv.toString());

        System.out.println("Returning:" + propertiesDiv.toString());
        return NONE;
    }
    
    /**
     * This method will return all tasks available for a component. 
     */
         
    public String doGetComponentTasksDiv() throws Exception
    {
    	StringBuffer tasksDiv = new StringBuffer();

    	String repositoryIdString 		= this.getRequest().getParameter("repositoryId");
    	String siteNodeIdString 		= this.getRequest().getParameter("siteNodeId");
    	String languageIdString 		= this.getRequest().getParameter("languageId");
    	String componentIdString 		= this.getRequest().getParameter("componentId");
    	String contentIdString 			= this.getRequest().getParameter("contentId");
    	String componentContentIdString = this.getRequest().getParameter("componentContentId");
    	String slotName 				= this.getRequest().getParameter("slotName");
    	String showSimple 				= this.getRequest().getParameter("showSimple");
    	String showLegend 				= this.getRequest().getParameter("showLegend");
    	String targetDiv 				= this.getRequest().getParameter("targetDivId");

    	Integer repositoryId = new Integer(repositoryIdString);
    	Integer siteNodeId = new Integer(siteNodeIdString);
    	Integer languageId = new Integer(languageIdString);
    	Integer componentId = new Integer(componentIdString);
    	Integer contentId = new Integer(contentIdString);
    	Integer componentContentId = new Integer(componentContentIdString);
    		    
    	Database db = CastorDatabaseService.getDatabase();
    	
    	try
    	{
    		beginTransaction(db);
    	
        	InfoGluePrincipal principal = this.getInfoGluePrincipal();
        	String cmsUserName = (String)this.getHttpSession().getAttribute("cmsUserName");
       	 	System.out.println("cmsUserName:" + cmsUserName);
        	if(cmsUserName != null)
       	 		principal = UserControllerProxy.getController(db).getUser(cmsUserName);
        	else
        		principal = (InfoGluePrincipal)this.getAnonymousPrincipal();
        	
       	 	System.out.println("cmsUserName:" + cmsUserName);
        	Locale locale = this.getLocale();
        	if(languageId != null)
        		locale = LanguageController.getController().getLocaleWithId(languageId);
        	
        	if(slotName == null)
        		slotName = "";
        	
    		PageEditorHelper peh = new PageEditorHelper();
	    	String componentTasksDiv = peh.getComponentTasksDiv(db, principal, this.getRequest(), locale, repositoryId, siteNodeId, languageId, contentId, componentId, componentContentId, slotName, showSimple, this.getOriginalFullURL(), showLegend, targetDiv);
	    	tasksDiv.append(componentTasksDiv);
    	
	    	commitTransaction(db);
    	}
    	catch (Exception e) 
    	{
    		rollbackTransaction(db);
    		e.printStackTrace();
		}
    	
    	//tasksDiv = new StringBuffer("<div id='componentMenu' class='skin0'>" + System.currentTimeMillis() + "</div>");
     	    	
        this.getResponse().setContentType("text/plain");
        this.getResponse().getWriter().println(tasksDiv.toString());

        System.out.println("Returning:" + tasksDiv.toString());
        return NONE;
    }

    /**
     * This method will return all properties for a component. 
     */
         
    public String doGetAvailableComponentsDiv() throws Exception
    {
    	String availableComponentDiv = "";

    	String repositoryIdString 		= this.getRequest().getParameter("repositoryId");
    	String languageIdString 		= this.getRequest().getParameter("languageId");
    	String componentContentIdString = this.getRequest().getParameter("componentContentId");
    	String slotName 				= this.getRequest().getParameter("slotName");
    	String showLegend 				= this.getRequest().getParameter("showLegend");
    	String targetDiv 				= this.getRequest().getParameter("targetDivId");

    	Integer repositoryId = null;
    	if(repositoryIdString != null)
    		repositoryId = new Integer(repositoryIdString);
    	
    	Integer languageId = null;
    	if(languageIdString != null)
    		languageId = new Integer(languageIdString);
    	
    	Integer componentContentId = null;
    	if(componentContentIdString != null)
    		componentContentId = new Integer(componentContentIdString);
    		    
    	Database db = CastorDatabaseService.getDatabase();
    	
    	try
    	{
    		beginTransaction(db);
    	
    		InfoGluePrincipal principal = this.getInfoGluePrincipal();
        	String cmsUserName = (String)this.getHttpSession().getAttribute("cmsUserName");
       	 	System.out.println("cmsUserName:" + cmsUserName);
        	if(cmsUserName != null)
       	 		principal = UserControllerProxy.getController(db).getUser(cmsUserName);
        	else
        		principal = (InfoGluePrincipal)this.getAnonymousPrincipal();
        	
       	 	System.out.println("cmsUserName:" + cmsUserName);
        	Locale locale = this.getLocale();
        	if(languageId != null)
        		locale = LanguageController.getController().getLocaleWithId(languageId);
        	
        	if(slotName == null)
        		slotName = "";
        	
    		PageEditorHelper peh = new PageEditorHelper();
	    	availableComponentDiv = peh.getAvailableComponentsDiv(db, principal, locale, repositoryId, languageId, componentContentId, slotName, showLegend, targetDiv);
	    	
	    	commitTransaction(db);
    	}
    	catch (Exception e) 
    	{
    		rollbackTransaction(db);
    		e.printStackTrace();
		}
     	    	
        this.getResponse().setContentType("text/plain");
        this.getResponse().getWriter().println(availableComponentDiv);

        System.out.println("Returning:" + availableComponentDiv);
        return NONE;
    }
    
 
    /**
     * This method is the application entry-point. The parameters has been set through the setters
     * and now we just have to render the appropriate output. 
     */
         
    public String doExecute() throws Exception
    {
        return SUCCESS;
    }
    
}
