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

package org.infoglue.deliver.controllers.kernel.impl.simple;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Node;
import org.exolab.castor.jdo.Database;
import org.infoglue.cms.applications.common.VisualFormatter;

import org.infoglue.cms.controllers.kernel.impl.simple.CastorDatabaseService;
import org.infoglue.cms.controllers.kernel.impl.simple.GroupPropertiesController;
import org.infoglue.cms.controllers.kernel.impl.simple.RolePropertiesController;
import org.infoglue.cms.controllers.kernel.impl.simple.SiteNodeController;
import org.infoglue.cms.controllers.kernel.impl.simple.UserControllerProxy;
import org.infoglue.cms.controllers.kernel.impl.simple.UserPropertiesController;

import org.infoglue.cms.entities.content.DigitalAsset;
import org.infoglue.cms.entities.management.GroupProperties;
import org.infoglue.cms.entities.management.LanguageVO;
import org.infoglue.cms.entities.management.RoleProperties;
import org.infoglue.cms.entities.management.UserProperties;
import org.infoglue.cms.entities.structure.SiteNode;
import org.infoglue.cms.exception.SystemException;
import org.infoglue.cms.security.AuthenticationModule;
import org.infoglue.cms.security.InfoGlueAuthenticationFilter;
import org.infoglue.cms.security.InfoGlueGroup;
import org.infoglue.cms.security.InfoGluePrincipal;
import org.infoglue.cms.security.InfoGlueRole;

import org.infoglue.cms.util.CmsPropertyHandler;
import org.infoglue.cms.util.ConstraintExceptionBuffer;
import org.infoglue.cms.util.dom.DOMBuilder;

/**
 * @author Mattias Bogeblad
 *
 * This class is the controller for extranet functionality. Authentication, authorization and stuff like that
 * is what it does best!
 */

public class ExtranetController extends BaseDeliveryController
{
	/**
	 * Private constructor to enforce factory-use
	 */

	private ExtranetController()
	{
	}

	/**
	 * Factory method
	 */
	
	public static ExtranetController getController()
	{
		return new ExtranetController();
	}
	
	/**
	 * This method autenticates a user. It takes a username and checks first that it is defined as a
	 * infoglue extranet user. 
	 */
	
	public Principal getAuthenticatedPrincipal(Database db, Map request) throws Exception
	{		
		Principal principal = null;
		
		try
		{
		    String authenticatorClass 	= InfoGlueAuthenticationFilter.authenticatorClass;
		    String authorizerClass 	  	= InfoGlueAuthenticationFilter.authorizerClass;
		    String invalidLoginUrl 		= InfoGlueAuthenticationFilter.invalidLoginUrl;
		    String loginUrl 			= InfoGlueAuthenticationFilter.loginUrl;
		    String serverName 			= InfoGlueAuthenticationFilter.serverName;
		    Properties extraProperties 	= InfoGlueAuthenticationFilter.extraProperties;
		    String casRenew 			= InfoGlueAuthenticationFilter.casRenew;
		    String casServiceUrl 		= InfoGlueAuthenticationFilter.casServiceUrl;
		    String casValidateUrl 		= InfoGlueAuthenticationFilter.casValidateUrl;
		    
		    AuthenticationModule authenticationModule = (AuthenticationModule)Class.forName(authenticatorClass).newInstance();
			authenticationModule.setAuthenticatorClass(authenticatorClass);
			authenticationModule.setAuthorizerClass(authorizerClass);
			authenticationModule.setInvalidLoginUrl(invalidLoginUrl);
			authenticationModule.setLoginUrl(loginUrl);
			authenticationModule.setServerName(serverName);
			authenticationModule.setExtraProperties(extraProperties);
			authenticationModule.setCasRenew(casRenew);
			authenticationModule.setCasServiceUrl(casServiceUrl);
			authenticationModule.setCasValidateUrl(casValidateUrl);
			authenticationModule.setTransactionObject(db);
			
			String authenticatedUserName = authenticationModule.authenticateUser(request);
			getLogger().info("authenticatedUserName:" + authenticatedUserName);
			principal = UserControllerProxy.getController(db).getUser(authenticatedUserName);
			getLogger().info("principal:" + principal);
		}
		catch(Exception e)
		{
			getLogger().error("An error occurred so we should not complete the transaction:" + e, e);
			throw new SystemException("The login process failed: " + e.getMessage(), e);
		}
		
		return principal;
	}
	
	
	/**
	 * This method autenticates a user. It takes a username and checks first that it is defined as a
	 * infoglue extranet user. 
	 */
	
	public Principal getAuthenticatedPrincipal(Map request) throws Exception
	{		
		Principal principal = null;
		
		try
		{
		    String authenticatorClass 	= InfoGlueAuthenticationFilter.authenticatorClass;
		    String authorizerClass 	  	= InfoGlueAuthenticationFilter.authorizerClass;
		    String invalidLoginUrl 		= InfoGlueAuthenticationFilter.invalidLoginUrl;
		    String loginUrl 			= InfoGlueAuthenticationFilter.loginUrl;
		    String serverName 			= InfoGlueAuthenticationFilter.serverName;
		    Properties extraProperties 	= InfoGlueAuthenticationFilter.extraProperties;
		    String casRenew 			= InfoGlueAuthenticationFilter.casRenew;
		    String casServiceUrl 		= InfoGlueAuthenticationFilter.casServiceUrl;
		    String casValidateUrl 		= InfoGlueAuthenticationFilter.casValidateUrl;
		    
		    AuthenticationModule authenticationModule = (AuthenticationModule)Class.forName(authenticatorClass).newInstance();
			authenticationModule.setAuthenticatorClass(authenticatorClass);
			authenticationModule.setAuthorizerClass(authorizerClass);
			authenticationModule.setInvalidLoginUrl(invalidLoginUrl);
			authenticationModule.setLoginUrl(loginUrl);
			authenticationModule.setServerName(serverName);
			authenticationModule.setExtraProperties(extraProperties);
			authenticationModule.setCasRenew(casRenew);
			authenticationModule.setCasServiceUrl(casServiceUrl);
			authenticationModule.setCasValidateUrl(casValidateUrl);
			authenticationModule.setTransactionObject(null);
			
			String authenticatedUserName = authenticationModule.authenticateUser(request);
			getLogger().info("authenticatedUserName:" + authenticatedUserName);
			principal = UserControllerProxy.getController().getUser(authenticatedUserName);
			getLogger().info("principal:" + principal);
		}
		catch(Exception e)
		{
			getLogger().error("An error occurred so we should not complete the transaction:" + e, e);
			throw new SystemException("The login process failed: " + e.getMessage(), e);
		}
		
		return principal;
	}
	/**
	 * This method checks if a user has access to an entity. It takes name and id of the entity. 
	 */
	/*
	public boolean getIsPrincipalAuthorized(Principal principal, String name, String value, NodeDeliveryController nodeDeliveryController) throws Exception
	{		
		boolean isPrincipalAuthorized = false;
		
		//UserVO userVO = UserController.getController().getUserVO(principal.getName());
		
		List extranetAccessVOList = ExtranetAccessController.getController().getExtranetAccessVOList(name, value);
		if(extranetAccessVOList != null && extranetAccessVOList.size() > 0)
		{	
			Iterator extranetAccessVOListIterator = extranetAccessVOList.iterator();
			outer: while(extranetAccessVOListIterator.hasNext())
			{
				ExtranetAccessVO extranetAccessVO = (ExtranetAccessVO)extranetAccessVOListIterator.next();
				List roles = ((InfoGluePrincipal)principal).getRoles();
				getLogger().info("ExtranetAccessVO:" + extranetAccessVO.getRoleId());
				getLogger().info("name:" + extranetAccessVO.getName());
				getLogger().info("value:" + extranetAccessVO.getValue());
				Iterator rolesIterator = roles.iterator();
				while(rolesIterator.hasNext())
				{
					RoleVO roleVO = (RoleVO)rolesIterator.next();
					getLogger().info("roleVO:" + roleVO.getRoleId());
					if(roleVO.getRoleId().intValue() == extranetAccessVO.getRoleId().intValue() && extranetAccessVO.getHasReadAccess().booleanValue())
					{
						isPrincipalAuthorized = true;
						break outer;
					}
				}
			}	
		}
		else
		{
			if(name.equals("SiteNode"))
			{
				SiteNodeVO parentSiteNodeVO = nodeDeliveryController.getParentSiteNode(new Integer(value));
				if(parentSiteNodeVO != null)
					isPrincipalAuthorized = getIsPrincipalAuthorized(principal, name, parentSiteNodeVO.getSiteNodeId().toString(), nodeDeliveryController);
			}
		}
		
		getLogger().info("isPrincipalAuthorized:" + isPrincipalAuthorized);
		
		return isPrincipalAuthorized;
	}
	*/
	
	/**
	 * This method checks if a user has write access to an entity. It takes name and id of the entity. 
	 */
	/*
	public boolean getIsPrincipalAuthorizedForWriteAccess(Principal principal, String name, String value, NodeDeliveryController nodeDeliveryController) throws Exception
	{		
		boolean isPrincipalAuthorized = false;
		
		//UserVO userVO = UserController.getController().getUserVO(principal.getName());
		
		List extranetAccessVOList = ExtranetAccessController.getController().getExtranetAccessVOList(name, value);
		if(extranetAccessVOList != null && extranetAccessVOList.size() > 0)
		{	
			Iterator extranetAccessVOListIterator = extranetAccessVOList.iterator();
			outer: while(extranetAccessVOListIterator.hasNext())
			{
				ExtranetAccessVO extranetAccessVO = (ExtranetAccessVO)extranetAccessVOListIterator.next();
				List roles = ((InfoGluePrincipal)principal).getRoles();
				getLogger().info("ExtranetAccessVO:" + extranetAccessVO.getRoleId());
				getLogger().info("name:" + extranetAccessVO.getName());
				getLogger().info("value:" + extranetAccessVO.getValue());
				Iterator rolesIterator = roles.iterator();
				while(rolesIterator.hasNext())
				{
					RoleVO roleVO = (RoleVO)rolesIterator.next();
					getLogger().info("roleVO:" + roleVO.getRoleId());
					if(roleVO.getRoleId().intValue() == extranetAccessVO.getRoleId().intValue() && extranetAccessVO.getHasWriteAccess().booleanValue())
					{
						isPrincipalAuthorized = true;
						break outer;
					}
				}
			}	
		}
		else
		{
			if(name.equals("SiteNode"))
			{
				SiteNodeVO parentSiteNodeVO = nodeDeliveryController.getParentSiteNode(new Integer(value));
				if(parentSiteNodeVO != null)
					isPrincipalAuthorized = getIsPrincipalAuthorizedForWriteAccess(principal, name, parentSiteNodeVO.getSiteNodeId().toString(), nodeDeliveryController);
			}
		}
		
		getLogger().info("isPrincipalAuthorized:" + isPrincipalAuthorized);
		
		return isPrincipalAuthorized;
	}
	*/

	/**
	 * Getting a property for a Principal - used for personalisation. 
	 * This method starts with getting the property on the user and if it does not exist we check out the
	 * group-properties as well.
	 */
	/*
	public String getPrincipalPropertyValue(Database db, InfoGluePrincipal infoGluePrincipal, String propertyName, Integer languageId, Integer siteNodeId, boolean useLanguageFallback, boolean escapeSpecialCharacters) throws Exception
	{
		String value = "";
		
		if(infoGluePrincipal == null || propertyName == null)
			return null;
		
		Collection userPropertiesList = UserPropertiesController.getController().getUserPropertiesList(infoGluePrincipal.getName(), languageId, db);
		Iterator userPropertiesListIterator = userPropertiesList.iterator();
		while(userPropertiesListIterator.hasNext())
		{
			UserProperties userProperties = (UserProperties)userPropertiesListIterator.next();
	
			if(userProperties != null && userProperties.getLanguage().getLanguageId().equals(languageId) && userProperties.getValue() != null && propertyName != null)
			{
				String propertyXML = userProperties.getValue();
				DOMBuilder domBuilder = new DOMBuilder();
				Document document = domBuilder.getDocument(propertyXML);
	
				Node node = document.getRootElement().selectSingleNode("attributes/" + propertyName);
				if(node != null)
				{
					value = node.getStringValue();
					getLogger().info("Getting value: " + value);
					if(value != null && escapeSpecialCharacters)
						value = new VisualFormatter().escapeHTML(value);
					break;
				}
			}
		}
		
		if(value.equals(""))
		{	
			List roles = infoGluePrincipal.getRoles();
			Iterator rolesIterator = roles.iterator();
			while(rolesIterator.hasNext())
			{
				InfoGlueRole role = (InfoGlueRole)rolesIterator.next();
				
				Collection rolePropertiesList = RolePropertiesController.getController().getRolePropertiesList(role.getName(), languageId, db);
				Iterator rolePropertiesListIterator = rolePropertiesList.iterator();
				while(rolePropertiesListIterator.hasNext())
				{
					RoleProperties roleProperties = (RoleProperties)rolePropertiesListIterator.next();
					
					if(roleProperties != null && roleProperties.getLanguage().getLanguageId().equals(languageId) && roleProperties.getValue() != null && propertyName != null)
					{
						String propertyXML = roleProperties.getValue();
						DOMBuilder domBuilder = new DOMBuilder();
						Document document = domBuilder.getDocument(propertyXML);
						
						Node node = document.getRootElement().selectSingleNode("attributes/" + propertyName);
						if(node != null)
						{
							value = node.getStringValue();
							getLogger().info("Getting value: " + value);
							if(value != null && escapeSpecialCharacters)
								value = new VisualFormatter().escapeHTML(value);
							break;
						}
					}
				}
									
			}
			
			if(value.equals("") && useLanguageFallback)
			{
				LanguageVO masterLanguageVO = LanguageDeliveryController.getLanguageDeliveryController().getMasterLanguageForSiteNode(db, siteNodeId);
				if(!masterLanguageVO.getLanguageId().equals(languageId))
					return getPrincipalPropertyValue(db, infoGluePrincipal, propertyName, masterLanguageVO.getLanguageId(), siteNodeId, useLanguageFallback, escapeSpecialCharacters);
			}
		}

		if(value.equals(""))
		{	
			List groups = infoGluePrincipal.getGroups();
			Iterator groupsIterator = groups.iterator();
			while(groupsIterator.hasNext())
			{
				InfoGlueGroup group = (InfoGlueGroup)groupsIterator.next();
				
				Collection groupPropertiesList = GroupPropertiesController.getController().getGroupPropertiesList(group.getName(), languageId, db);
				Iterator groupPropertiesListIterator = groupPropertiesList.iterator();
				while(groupPropertiesListIterator.hasNext())
				{
					GroupProperties groupProperties = (GroupProperties)groupPropertiesListIterator.next();
					
					if(groupProperties != null && groupProperties.getLanguage().getLanguageId().equals(languageId) && groupProperties.getValue() != null && propertyName != null)
					{
						String propertyXML = groupProperties.getValue();
						DOMBuilder domBuilder = new DOMBuilder();
						Document document = domBuilder.getDocument(propertyXML);
						
						Node node = document.getRootElement().selectSingleNode("attributes/" + propertyName);
						if(node != null)
						{
							value = node.getStringValue();
							getLogger().info("Getting value: " + value);
							if(value != null && escapeSpecialCharacters)
								value = new VisualFormatter().escapeHTML(value);
							break;
						}
					}
				}
									
			}
			
			if(value.equals("") && useLanguageFallback)
			{
				LanguageVO masterLanguageVO = LanguageDeliveryController.getLanguageDeliveryController().getMasterLanguageForSiteNode(db, siteNodeId);
				if(!masterLanguageVO.getLanguageId().equals(languageId))
					return getPrincipalPropertyValue(db, infoGluePrincipal, propertyName, masterLanguageVO.getLanguageId(), siteNodeId, useLanguageFallback, escapeSpecialCharacters);
			}
		}

		return value;
	}	
	*/
	
	
	/**
	 * Getting a digitalAsset for a Principal - used for personalisation. 
	 * This method starts with getting the asset on the user and if it does not exist we check out the
	 * group-properties as well.
	 */
	
	public String getPrincipalAssetUrl(Database db, InfoGluePrincipal infoGluePrincipal, String assetKey, Integer languageId, Integer siteNodeId, boolean useLanguageFallback) throws Exception
	{
		String assetUrl = "";
		
		if(infoGluePrincipal == null || assetKey == null)
			return null;

		Collection userPropertiesList = UserPropertiesController.getController().getUserPropertiesList(infoGluePrincipal.getName(), languageId, db);
		getLogger().info("userProperties:" + userPropertiesList.size());
		Iterator userPropertiesListIterator = userPropertiesList.iterator();
		while(userPropertiesListIterator.hasNext())
		{
			UserProperties userProperties = (UserProperties)userPropertiesListIterator.next();
			//getLogger().info("userProperties:" + userProperties.getValue());
			//getLogger().info("propertyName:" + propertyName);
			getLogger().info("userProperties:" + userProperties.getValue());
			getLogger().info("assetKey:" + assetKey);

			if(userProperties != null && userProperties.getLanguage().getLanguageId().equals(languageId))
			{
			    Collection assets = userProperties.getDigitalAssets();
			    getLogger().info("assets:" + assets.size());
			    Iterator assetsIterator = assets.iterator();
			    while(assetsIterator.hasNext())
			    {
			        DigitalAsset digitalAsset = (DigitalAsset)assetsIterator.next();
			        getLogger().info("digitalAsset:" + digitalAsset.getAssetKey());
			        if(digitalAsset.getAssetKey().equalsIgnoreCase(assetKey))
			        {
			            SiteNode siteNode = SiteNodeController.getController().getSiteNodeWithId(siteNodeId, db);
			            assetUrl = DigitalAssetDeliveryController.getDigitalAssetDeliveryController().getAssetUrl(digitalAsset, siteNode.getRepository());
			            getLogger().info("assetUrl:" + assetUrl);
			            break;
			        }
			    }
			}
		}
		
		if(assetUrl.equals(""))
		{	
			List roles = infoGluePrincipal.getRoles();
			Iterator rolesIterator = roles.iterator();
			while(rolesIterator.hasNext())
			{
				InfoGlueRole role = (InfoGlueRole)rolesIterator.next();
				
				Collection rolePropertiesList = RolePropertiesController.getController().getRolePropertiesList(role.getName(), languageId, db);
				Iterator rolePropertiesListIterator = rolePropertiesList.iterator();
				while(rolePropertiesListIterator.hasNext())
				{
					RoleProperties roleProperties = (RoleProperties)rolePropertiesListIterator.next();
					
					if(roleProperties != null && roleProperties.getLanguage().getLanguageId().equals(languageId))
					{
					    Collection assets = roleProperties.getDigitalAssets();
					    Iterator assetsIterator = assets.iterator();
					    while(assetsIterator.hasNext())
					    {
					        DigitalAsset digitalAsset = (DigitalAsset)assetsIterator.next();
					        if(digitalAsset.getAssetKey().equalsIgnoreCase(assetKey))
					        {
					            SiteNode siteNode = SiteNodeController.getController().getSiteNodeWithId(siteNodeId, db);
					            assetUrl = DigitalAssetDeliveryController.getDigitalAssetDeliveryController().getAssetUrl(digitalAsset, siteNode.getRepository());
					            break;
					        }
					    }
					}
				}
									
			}
			
			if(assetUrl.equals("") && useLanguageFallback)
			{
				LanguageVO masterLanguageVO = LanguageDeliveryController.getLanguageDeliveryController().getMasterLanguageForSiteNode(db, siteNodeId);
				if(!masterLanguageVO.getLanguageId().equals(languageId))
					return getPrincipalAssetUrl(db, infoGluePrincipal, assetKey, masterLanguageVO.getLanguageId(), siteNodeId, useLanguageFallback);
			}
		}

		if(assetUrl.equals(""))
		{	
			List groups = infoGluePrincipal.getGroups();
			Iterator groupsIterator = groups.iterator();
			while(groupsIterator.hasNext())
			{
				InfoGlueGroup group = (InfoGlueGroup)groupsIterator.next();
				
				Collection groupPropertiesList = GroupPropertiesController.getController().getGroupPropertiesList(group.getName(), languageId, db);
				Iterator groupPropertiesListIterator = groupPropertiesList.iterator();
				while(groupPropertiesListIterator.hasNext())
				{
					GroupProperties groupProperties = (GroupProperties)groupPropertiesListIterator.next();
					
					if(groupProperties != null && groupProperties.getLanguage().getLanguageId().equals(languageId))
					{
					    Collection assets = groupProperties.getDigitalAssets();
					    Iterator assetsIterator = assets.iterator();
					    while(assetsIterator.hasNext())
					    {
					        DigitalAsset digitalAsset = (DigitalAsset)assetsIterator.next();
					        if(digitalAsset.getAssetKey().equalsIgnoreCase(assetKey))
					        {
					            SiteNode siteNode = SiteNodeController.getController().getSiteNodeWithId(siteNodeId, db);
					            assetUrl = DigitalAssetDeliveryController.getDigitalAssetDeliveryController().getAssetUrl(digitalAsset, siteNode.getRepository());
					            break;
					        }
					    }
					}
				}
									
			}
			
			if(assetUrl.equals("") && useLanguageFallback)
			{
				LanguageVO masterLanguageVO = LanguageDeliveryController.getLanguageDeliveryController().getMasterLanguageForSiteNode(db, siteNodeId);
				if(!masterLanguageVO.getLanguageId().equals(languageId))
					return getPrincipalAssetUrl(db, infoGluePrincipal, assetKey, masterLanguageVO.getLanguageId(), siteNodeId, useLanguageFallback);
			}
		}

		return assetUrl;
	}	
	
	
	/**
	 * Getting a digitalAsset for a Principal - used for personalisation. 
	 * This method starts with getting the asset on the user and if it does not exist we check out the
	 * group-properties as well.
	 */
	
	public String getPrincipalThumbnailAssetUrl(Database db, InfoGluePrincipal infoGluePrincipal, String assetKey, Integer languageId, Integer siteNodeId, boolean useLanguageFallback, int width, int height) throws Exception
	{
		String assetUrl = "";
		
		if(infoGluePrincipal == null || assetKey == null)
			return null;
		
		Collection userPropertiesList = UserPropertiesController.getController().getUserPropertiesList(infoGluePrincipal.getName(), languageId, db);
		Iterator userPropertiesListIterator = userPropertiesList.iterator();
		while(userPropertiesListIterator.hasNext())
		{
			UserProperties userProperties = (UserProperties)userPropertiesListIterator.next();
	
			if(userProperties != null && userProperties.getLanguage().getLanguageId().equals(languageId))
			{
			    Collection assets = userProperties.getDigitalAssets();
			    Iterator assetsIterator = assets.iterator();
			    while(assetsIterator.hasNext())
			    {
			        DigitalAsset digitalAsset = (DigitalAsset)assetsIterator.next();
			        if(digitalAsset.getAssetKey().equalsIgnoreCase(assetKey))
			        {
			            SiteNode siteNode = SiteNodeController.getController().getSiteNodeWithId(siteNodeId, db);
			            assetUrl = DigitalAssetDeliveryController.getDigitalAssetDeliveryController().getAssetThumbnailUrl(digitalAsset, siteNode.getRepository(), width, height);
			            break;
			        }
			    }
			}
		}
		
		if(assetUrl.equals(""))
		{	
			List roles = infoGluePrincipal.getRoles();
			Iterator rolesIterator = roles.iterator();
			while(rolesIterator.hasNext())
			{
				InfoGlueRole role = (InfoGlueRole)rolesIterator.next();
				
				Collection rolePropertiesList = RolePropertiesController.getController().getRolePropertiesList(role.getName(), languageId, db);
				Iterator rolePropertiesListIterator = rolePropertiesList.iterator();
				while(rolePropertiesListIterator.hasNext())
				{
					RoleProperties roleProperties = (RoleProperties)rolePropertiesListIterator.next();
					
					if(roleProperties != null && roleProperties.getLanguage().getLanguageId().equals(languageId))
					{
					    Collection assets = roleProperties.getDigitalAssets();
					    Iterator assetsIterator = assets.iterator();
					    while(assetsIterator.hasNext())
					    {
					        DigitalAsset digitalAsset = (DigitalAsset)assetsIterator.next();
					        if(digitalAsset.getAssetKey().equalsIgnoreCase(assetKey))
					        {
					            SiteNode siteNode = SiteNodeController.getController().getSiteNodeWithId(siteNodeId, db);
					            assetUrl = DigitalAssetDeliveryController.getDigitalAssetDeliveryController().getAssetThumbnailUrl(digitalAsset, siteNode.getRepository(), width, height);
					            break;
					        }
					    }
					}
				}
									
			}
			
			if(assetUrl.equals("") && useLanguageFallback)
			{
				LanguageVO masterLanguageVO = LanguageDeliveryController.getLanguageDeliveryController().getMasterLanguageForSiteNode(db, siteNodeId);
				if(!masterLanguageVO.getLanguageId().equals(languageId))
					return getPrincipalThumbnailAssetUrl(db, infoGluePrincipal, assetKey, masterLanguageVO.getLanguageId(), siteNodeId, useLanguageFallback, width, height);
			}
		}
	
		if(assetUrl.equals(""))
		{	
			List groups = infoGluePrincipal.getGroups();
			Iterator groupsIterator = groups.iterator();
			while(groupsIterator.hasNext())
			{
				InfoGlueGroup group = (InfoGlueGroup)groupsIterator.next();
				
				Collection groupPropertiesList = GroupPropertiesController.getController().getGroupPropertiesList(group.getName(), languageId, db);
				Iterator groupPropertiesListIterator = groupPropertiesList.iterator();
				while(groupPropertiesListIterator.hasNext())
				{
					GroupProperties groupProperties = (GroupProperties)groupPropertiesListIterator.next();
					
					if(groupProperties != null && groupProperties.getLanguage().getLanguageId().equals(languageId))
					{
					    Collection assets = groupProperties.getDigitalAssets();
					    Iterator assetsIterator = assets.iterator();
					    while(assetsIterator.hasNext())
					    {
					        DigitalAsset digitalAsset = (DigitalAsset)assetsIterator.next();
					        if(digitalAsset.getAssetKey().equalsIgnoreCase(assetKey))
					        {
					            SiteNode siteNode = SiteNodeController.getController().getSiteNodeWithId(siteNodeId, db);
					            assetUrl = DigitalAssetDeliveryController.getDigitalAssetDeliveryController().getAssetThumbnailUrl(digitalAsset, siteNode.getRepository(), width, height);
					            break;
					        }
					    }
					}
				}
									
			}
			
			if(assetUrl.equals("") && useLanguageFallback)
			{
				LanguageVO masterLanguageVO = LanguageDeliveryController.getLanguageDeliveryController().getMasterLanguageForSiteNode(db, siteNodeId);
				if(!masterLanguageVO.getLanguageId().equals(languageId))
					return getPrincipalThumbnailAssetUrl(db, infoGluePrincipal, assetKey, masterLanguageVO.getLanguageId(), siteNodeId, useLanguageFallback, width, height);
			}
		}
	
		return assetUrl;
	}	
	
	
	/**
	 * Getting a property for a Principal - used for personalisation. 
	 * This method starts with getting the property on the user and if it does not exist we check out the
	 * group-properties as well. The value in question is a map - name-value.
	 */
	/*
	public Map getPrincipalPropertyHashValues(Database db, InfoGluePrincipal infoGluePrincipal, String propertyName, Integer languageId, Integer siteNodeId, boolean useLanguageFallback, boolean escapeSpecialCharacters) throws Exception
	{
		Properties properties = new Properties();
		
		String attributeValue = getPrincipalPropertyValue(db, infoGluePrincipal, propertyName, languageId, siteNodeId, useLanguageFallback, escapeSpecialCharacters);
		
		ByteArrayInputStream is = new ByteArrayInputStream(attributeValue.getBytes("UTF-8"));

		properties.load(is);
		
		return properties;
	}	
	*/

}
