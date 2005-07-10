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

package org.infoglue.cms.controllers.kernel.impl.simple;

import org.infoglue.cms.entities.kernel.BaseEntityVO;
import org.infoglue.cms.entities.management.AccessRight;
import org.infoglue.cms.entities.management.AccessRightGroup;
import org.infoglue.cms.entities.management.AccessRightGroupVO;
import org.infoglue.cms.entities.management.AccessRightRole;
import org.infoglue.cms.entities.management.AccessRightRoleVO;
import org.infoglue.cms.entities.management.AccessRightVO;
import org.infoglue.cms.entities.management.AvailableServiceBinding;
import org.infoglue.cms.entities.management.InterceptionPoint;
import org.infoglue.cms.entities.management.InterceptionPointVO;
import org.infoglue.cms.entities.management.impl.simple.AccessRightGroupImpl;
import org.infoglue.cms.entities.management.impl.simple.AccessRightImpl;
import org.infoglue.cms.entities.management.impl.simple.AccessRightRoleImpl;
import org.infoglue.cms.entities.management.impl.simple.AvailableServiceBindingImpl;
import org.infoglue.cms.exception.*;
import org.infoglue.cms.security.InfoGlueGroup;
import org.infoglue.cms.security.InfoGluePrincipal;
import org.infoglue.cms.security.InfoGlueRole;

import org.infoglue.deliver.util.CacheController;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is a helper class for the use case handle Accesss
 *
 * @author Mattias Bogeblad
 */

public class AccessRightController extends BaseController
{
	
	/**
	 * Factory method
	 */

	public static AccessRightController getController()
	{
		return new AccessRightController();
	}
	
	public AccessRight getAccessRightWithId(Integer accessRightId, Database db) throws SystemException, Bug
	{
		return (AccessRight) getObjectWithId(AccessRightImpl.class, accessRightId, db);
	}
    
	public AccessRightVO getAccessRightVOWithId(Integer accessRightId) throws SystemException, Bug
	{
		return (AccessRightVO) getVOWithId(AccessRightImpl.class, accessRightId);
	}
  
	public List getAccessRightVOList() throws SystemException, Bug
	{
		return getAllVOObjects(AccessRightImpl.class, "accessRightId");
	}

	public List getAccessRightVOList(Database db) throws SystemException, Bug
	{
		return this.getAllVOObjects(AccessRightImpl.class, "accessRightId", db);
	}

	public List getAccessRightGroupVOList(Integer accessRightId) throws SystemException, Bug
	{
		List accessRightGroupVOList = new ArrayList();
		
		Database db = CastorDatabaseService.getDatabase();

		try 
		{
			beginTransaction(db);
			
			AccessRight accessRight = this.getAccessRightWithId(accessRightId, db);
			if(accessRight != null)
			    accessRightGroupVOList = toVOList(accessRight.getGroups());
			
	        getLogger().info("accessRightGroupVOList:" + accessRightGroupVOList.size());
			
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
		    e.printStackTrace();
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
		
		return accessRightGroupVOList;	
	}
	
	public List getAccessRightVOList(Integer interceptionPointId, String parameters, String roleName) throws SystemException, Bug
	{
		List accessRightVOList = null;
		
		Database db = CastorDatabaseService.getDatabase();

		try 
		{
			beginTransaction(db);
			
			accessRightVOList = getAccessRightVOList(db, interceptionPointId, parameters, roleName);

			getLogger().info("accessRightVOList:" + accessRightVOList.size());
			
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
		    e.printStackTrace();
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
		
		return accessRightVOList;	
	}

	
	public List getAccessRightVOList(Database db, Integer interceptionPointId, String parameters, String roleName) throws SystemException, Bug
	{
		List accessRightVOList = null;
		
		InterceptionPointVO interceptionPointVO = InterceptionPointController.getController().getInterceptionPointVOWithId(interceptionPointId);
		if(interceptionPointVO.getUsesExtraDataForAccessControl().booleanValue())
			accessRightVOList = toVOList(getAccessRightList(interceptionPointId, parameters, roleName, db));
		else
			accessRightVOList = toVOList(getAccessRightList(interceptionPointId, roleName, db));

		getLogger().info("accessRightVOList:" + accessRightVOList.size());
		
		return accessRightVOList;	
	}

	public List getAccessRightVOListOnly(Integer interceptionPointId, String parameters) throws SystemException, Bug
	{
		List accessRightVOList = null;
		
		Database db = CastorDatabaseService.getDatabase();

		try 
		{
			beginTransaction(db);
			
			accessRightVOList = getAccessRightVOListOnly(db, interceptionPointId, parameters);

			getLogger().info("accessRightVOList:" + accessRightVOList.size());
			
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
		    e.printStackTrace();
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
		
		return accessRightVOList;	
	}

	public List getAccessRightVOListOnly(Database db, Integer interceptionPointId, String parameters) throws SystemException, Bug
	{
		List accessRightVOList = null;
		
		InterceptionPointVO interceptionPointVO = InterceptionPointController.getController().getInterceptionPointVOWithId(interceptionPointId);
		if(interceptionPointVO.getUsesExtraDataForAccessControl().booleanValue())
			accessRightVOList = toVOList(getAccessRightListOnly(interceptionPointId, parameters, db));
		else
			accessRightVOList = toVOList(getAccessRightList(interceptionPointId, db));

		getLogger().info("accessRightVOList:" + accessRightVOList.size());
		
		return accessRightVOList;	
	}

	public List getAccessRightList(String interceptionPointName, String parameters, String roleName, Database db) throws SystemException, Bug
	{
		List accessRightList = getAccessRightList(InterceptionPointController.getController().getInterceptionPointVOWithName(interceptionPointName).getId(), parameters, roleName, db);
		
		return accessRightList;		
	}

	public List getAccessRightList(Integer interceptionPointId, String parameters, String roleName, Database db) throws SystemException, Bug
	{
		List accessRightList = new ArrayList();
		
		try
		{
			OQLQuery oql = null;
			
			if(parameters == null || parameters.length() == 0)
			{
				oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1 AND (is_undefined(f.parameters) OR f.parameters = $2) AND f.roles.roleName = $3");
				oql.bind(interceptionPointId);
				oql.bind(parameters);
				oql.bind(roleName);
			}
			else
			{
		    	oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1 AND f.parameters = $2 AND f.roles.roleName = $3");
				oql.bind(interceptionPointId);
				oql.bind(parameters);
				oql.bind(roleName);
			}
			
			QueryResults results = oql.execute();
			
			while (results.hasMore()) 
			{
				AccessRight accessRight = (AccessRight)results.next();
				accessRightList.add(accessRight);
			}
		}
		catch(Exception e)
		{
		    e.printStackTrace();
			throw new SystemException("An error occurred when we tried to fetch a list of Access rights. Reason:" + e.getMessage(), e);    
		}
		
		return accessRightList;		
	}

	public List getAccessRightListOnly(Integer interceptionPointId, String parameters, Database db) throws SystemException, Bug
	{
		List accessRightList = new ArrayList();
		
		try
		{
			OQLQuery oql = null;
			
			if(parameters == null || parameters.length() == 0)
			{
				oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1 AND (is_undefined(f.parameters) OR f.parameters = $2)");
				oql.bind(interceptionPointId);
				oql.bind(parameters);
			}
			else
			{
		    	oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1 AND f.parameters = $2");
				oql.bind(interceptionPointId);
				oql.bind(parameters);
			}
			
			QueryResults results = oql.execute();
			
			while (results.hasMore()) 
			{
				AccessRight accessRight = (AccessRight)results.next();
				accessRightList.add(accessRight);
			}
		}
		catch(Exception e)
		{
		    e.printStackTrace();
			throw new SystemException("An error occurred when we tried to fetch a list of Access rights. Reason:" + e.getMessage(), e);    
		}
		
		return accessRightList;		
	}

	public List getAccessRightListForEntity(Integer interceptionPointId, String parameters, Database db)  throws SystemException, Bug
	{
		List accessRightList = new ArrayList();
		
		try
		{
		    //getLogger().info("getAccessRightListForEntity(Integer interceptionPointId, String parameters, Database db)");
		    //getLogger().info("interceptionPointId:" + interceptionPointId);
		    //getLogger().info("parameters:" + parameters);
			
			OQLQuery oql = null;
			
			if(parameters == null || parameters.length() == 0)
			{
				oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1 AND (is_undefined(f.parameters) OR f.parameters = $2)");
				oql.bind(interceptionPointId);
				oql.bind(parameters);
			}
			else
			{
		    	oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1 AND f.parameters = $2");
				oql.bind(interceptionPointId);
				oql.bind(parameters);
			}
						
			QueryResults results = oql.execute();
			while (results.hasMore()) 
			{
				AccessRight accessRight = (AccessRight)results.next();
				//getLogger().info("accessRight:" + accessRight.getAccessRightId());
				accessRightList.add(accessRight);
			}
		}
		catch(Exception e)
		{
			throw new SystemException("An error occurred when we tried to fetch a list of Function. Reason:" + e.getMessage(), e);    
		}
		
		return accessRightList;		
	}
	
	
	public List getAccessRightList(Integer interceptionPointId, Database db)  throws SystemException, Bug
	{
		List accessRightList = new ArrayList();
		
		try
		{
			getLogger().info("getAccessRightList(Integer interceptionPointId, Database db)");
			getLogger().info("interceptionPointId: " + interceptionPointId);
			
			OQLQuery oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1");
			oql.bind(interceptionPointId);
			
			QueryResults results = oql.execute();
			while (results.hasMore()) 
			{
				AccessRight accessRight = (AccessRight)results.next();
				getLogger().info("accessRight:" + accessRight.getAccessRightId());
				accessRightList.add(accessRight);
			}
		}
		catch(Exception e)
		{
			throw new SystemException("An error occurred when we tried to fetch a list of Function. Reason:" + e.getMessage(), e);    
		}
		
		return accessRightList;		
	}

	public List getAccessRightList(String roleName, Database db)  throws SystemException, Bug
	{
		List accessRightList = new ArrayList();
		
		try
		{
			getLogger().info("getAccessRightList(String roleName, Database db)");
			getLogger().info("roleName: " + roleName);
			
			OQLQuery oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.roles.roleName = $1");
			oql.bind(roleName);
			
			QueryResults results = oql.execute();
			while (results.hasMore()) 
			{
				AccessRight accessRight = (AccessRight)results.next();
				getLogger().info("accessRight:" + accessRight.getAccessRightId());
				accessRightList.add(accessRight);
			}
		}
		catch(Exception e)
		{
			throw new SystemException("An error occurred when we tried to fetch a list of Function. Reason:" + e.getMessage(), e);    
		}
		
		return accessRightList;		
	}
	

	public List getAccessRightList(Integer interceptionPointId, String roleName, Database db)  throws SystemException, Bug
	{
		List accessRightList = new ArrayList();
		
		try
		{
		    getLogger().info("getAccessRightList(Integer interceptionPointId, String roleName, Database db)");
			getLogger().info("interceptionPointId: " + interceptionPointId);
			getLogger().info("roleName: " + roleName);
			
			OQLQuery oql = db.getOQLQuery("SELECT f FROM org.infoglue.cms.entities.management.impl.simple.AccessRightImpl f WHERE f.interceptionPoint = $1 AND f.roles.roleName = $2");
			oql.bind(interceptionPointId);
			oql.bind(roleName);
			
			QueryResults results = oql.execute();
			while (results.hasMore()) 
			{
				AccessRight accessRight = (AccessRight)results.next();
				getLogger().info("accessRight:" + accessRight.getAccessRightId());
				accessRightList.add(accessRight);
			}
		}
		catch(Exception e)
		{
			throw new SystemException("An error occurred when we tried to fetch a list of Function. Reason:" + e.getMessage(), e);    
		}
		
		return accessRightList;		
	}
	
	/**
	 * This method creates an access note.
	 * 
	 * @param accessRightVO
	 * @param db
	 * @return
	 * @throws SystemException
	 * @throws Exception
	 */
	
	public AccessRight create(AccessRightVO accessRightVO, InterceptionPoint interceptionPoint, Database db) throws SystemException, Exception
	{
		AccessRight accessRight = new AccessRightImpl();
		accessRight.setValueObject(accessRightVO);
		
		accessRight.setInterceptionPoint(interceptionPoint);
		
		db.create(accessRight);
					
		return accessRight;
	}     

	
	public AccessRightVO update(AccessRightVO AccessRightVO) throws ConstraintException, SystemException
	{
		return (AccessRightVO) updateEntity(AccessRightImpl.class, AccessRightVO);
	}        

	
	public void update(String parameters, HttpServletRequest request) throws ConstraintException, SystemException
	{
		Database db = CastorDatabaseService.getDatabase();
		
		getLogger().info("parameters:" + parameters);
		
		try 
		{
			beginTransaction(db);

			int interceptionPointIndex = 0;
			String interceptionPointIdString = request.getParameter(interceptionPointIndex + "_InterceptionPointId");
			while(interceptionPointIdString != null)
			{
				delete(new Integer(interceptionPointIdString), parameters, db);

				AccessRightVO accessRightVO = new AccessRightVO();
				accessRightVO.setParameters(parameters);

				AccessRight accessRight = null;
				
				int roleIndex = 0;
				String roleName = request.getParameter(interceptionPointIdString + "_" + roleIndex + "_roleName");
				while(roleName != null)
				{
				    String hasAccess = request.getParameter(interceptionPointIdString + "_" + roleName + "_hasAccess");
					
					if(hasAccess != null)
					{
					    if(accessRight == null)
					    {
						    InterceptionPoint interceptionPoint = InterceptionPointController.getController().getInterceptionPointWithId(new Integer(interceptionPointIdString), db);
							accessRight = create(accessRightVO, interceptionPoint, db);
					    }
					    
					    AccessRightRoleVO accessRightRoleVO = new AccessRightRoleVO();
					    accessRightRoleVO.setRoleName(roleName);
					    AccessRightRole accessRightRole = createAccessRightRole(db, accessRightRoleVO, accessRight);
					    accessRight.getRoles().add(accessRightRole);
					}
					
					roleIndex++;
					roleName = request.getParameter(interceptionPointIdString + "_" + roleIndex + "_roleName");
				}

				int groupIndex = 0;
				String groupName = request.getParameter(interceptionPointIdString + "_" + groupIndex + "_groupName");

				while(groupName != null)
				{
				    if(accessRight == null)
				    {
					    InterceptionPoint interceptionPoint = InterceptionPointController.getController().getInterceptionPointWithId(new Integer(interceptionPointIdString), db);
					    //getLogger().info("Creating access for:" + interceptionPoint.getName() + "_" + parameters);
						accessRight = create(accessRightVO, interceptionPoint, db);
				    }
					
				    AccessRightGroupVO accessRightGroupVO = new AccessRightGroupVO();
				    accessRightGroupVO.setGroupName(groupName);
				    AccessRightGroup accessRightGroup = createAccessRightGroup(db, accessRightGroupVO, accessRight);
				    accessRight.getGroups().add(accessRightGroup);
					
				    groupIndex++;
				    groupName = request.getParameter(interceptionPointIdString + "_" + groupIndex + "_groupName");
				    //getLogger().info("groupName:" + groupName);
				}

				interceptionPointIndex++;
				interceptionPointIdString = request.getParameter(interceptionPointIndex + "_InterceptionPointId");
			}
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
		    e.printStackTrace();
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
	}			
	
	
	public void updateGroups(Integer accessRightId, String parameters, String[] groupNames) throws ConstraintException, SystemException
	{
		Database db = CastorDatabaseService.getDatabase();
		
		getLogger().info("parameters:" + parameters);
		
		try 
		{
			beginTransaction(db);
			
			AccessRight accessRight = this.getAccessRightWithId(accessRightId, db);

			Iterator groupsIterator = accessRight.getGroups().iterator();
			while(groupsIterator.hasNext())
			{
			    AccessRightGroup accessRightGroup = (AccessRightGroup)groupsIterator.next();
			    groupsIterator.remove();
			    db.remove(accessRightGroup);
			}
			
			if(groupNames != null)
			{
				for(int i=0; i < groupNames.length; i++)
				{
				    String groupName = groupNames[i];
				    AccessRightGroupVO accessRightGroupVO = new AccessRightGroupVO();
				    accessRightGroupVO.setGroupName(groupName);
				    AccessRightGroup accessRightGroup = createAccessRightGroup(db, accessRightGroupVO, accessRight);
				    accessRight.getGroups().add(accessRightGroup);
				}
			}
			
		    commitTransaction(db);
		} 
		catch (Exception e) 
		{
		    e.printStackTrace();
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
	}
	
	/**
	 * This method creates a AccessRightRole-object in the database.
	 * @param db
	 * @param accessRightRoleVO
	 * @return
	 * @throws SystemException
	 */
	
	public AccessRightRole createAccessRightRole(Database db, AccessRightRoleVO accessRightRoleVO, AccessRight accessRight) throws SystemException, Exception
	{
	    AccessRightRole accessRightRole = new AccessRightRoleImpl();
	    accessRightRole.setValueObject(accessRightRoleVO);
	    accessRightRole.setAccessRight(accessRight);
	    
	    db.create(accessRightRole);
        
	    return accessRightRole;
	}

	/**
	 * This method creates a AccessRightGroup-object in the database.
	 * @param db
	 * @param accessRightRoleVO
	 * @return
	 * @throws SystemException
	 */
	
	public AccessRightGroup createAccessRightGroup(Database db, AccessRightGroupVO accessRightGroupVO, AccessRight accessRight) throws SystemException, Exception
	{
	    AccessRightGroup accessRightGroup = new AccessRightGroupImpl();
	    accessRightGroup.setValueObject(accessRightGroupVO);
	    accessRightGroup.setAccessRight(accessRight);
	    
	    db.create(accessRightGroup);
	    
        return accessRightGroup;
	}

	
	/*
	 	public void update(String parameters, HttpServletRequest request) throws ConstraintException, SystemException
	{
		Database db = CastorDatabaseService.getDatabase();
		
		getLogger().info("parameters:" + parameters);
		
		try 
		{
			beginTransaction(db);

			int roleIndex = 0;
			String roleName = request.getParameter(roleIndex + "_roleName");
			while(roleName != null)
			{
				getLogger().info("roleName:" + roleName);
				
				int interceptionPointIndex = 0;
				String interceptionPointIdString = request.getParameter(roleName + "_" + interceptionPointIndex + "_InterceptionPointId");
				while(interceptionPointIdString != null)
				{
					getLogger().info("interceptionPointIdString:" + interceptionPointIdString);
					
					delete(new Integer(interceptionPointIdString), parameters, roleName, db);

					String hasAccess = request.getParameter(roleName + "_" + interceptionPointIdString + "_hasAccess");
					
					if(hasAccess != null)
					{
						AccessRightVO accessRightVO = new AccessRightVO();
						accessRightVO.setRoleName(roleName);
						accessRightVO.setParameters(parameters);

						InterceptionPoint interceptionPoint = InterceptionPointController.getController().getInterceptionPointWithId(new Integer(interceptionPointIdString), db);
						getLogger().info("Creating access for roleName:" + roleName + ":" + parameters + "_" + interceptionPoint.getName());
						
						create(accessRightVO, interceptionPoint, db);
					}
					
					interceptionPointIndex++;
					interceptionPointIdString = request.getParameter(roleName + "_" + interceptionPointIndex + "_InterceptionPointId");
				}
								
				roleIndex++;
				roleName = request.getParameter(roleIndex + "_roleName");
			}
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
	}			
	 */
	
	/**
	 * This method deletes all occurrencies of AccessRight which has the interceptionPointId.
	 * 
	 * @param roleName
	 * @throws ConstraintException
	 * @throws SystemException
	 */

	public void delete(String roleName) throws SystemException, Exception
	{
		Database db = CastorDatabaseService.getDatabase();
		
		getLogger().info("roleName:" + roleName);
		
		try 
		{
			beginTransaction(db);

			List accessRightList = getAccessRightList(roleName, db);
			Iterator i = accessRightList.iterator();
			while(i.hasNext())
			{
				AccessRight accessRight = (AccessRight)i.next();
				db.remove(accessRight);
			}
			
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
	}        
	
	/**
	 * This method deletes all occurrencies of AccessRight which has the interceptionPointId.
	 * 
	 * @param roleName
	 * @throws ConstraintException
	 * @throws SystemException
	 */

	public void delete(Integer interceptionPointId, String parameters, Database db) throws SystemException, Exception
	{
		List accessRightList = getAccessRightListOnly(interceptionPointId, parameters, db);
		Iterator i = accessRightList.iterator();
		while(i.hasNext())
		{
			AccessRight accessRight = (AccessRight)i.next();
			
			Iterator rolesIterator = accessRight.getRoles().iterator();
			while(rolesIterator.hasNext())
			{
			    AccessRightRole accessRightRole = (AccessRightRole)rolesIterator.next();
			    rolesIterator.remove();
			    db.remove(accessRightRole);
			}
			
			Iterator groupsIterator = accessRight.getGroups().iterator();
			while(groupsIterator.hasNext())
			{
			    AccessRightGroup accessRightGroup = (AccessRightGroup)groupsIterator.next();
			    groupsIterator.remove();
			    db.remove(accessRightGroup);
			}
			
			db.remove(accessRight);
		}
		
	}        

	
	
	/**
	 * This method checks if a role has access to an entity. It takes name and id of the entity. 
	 */
	public boolean getIsPrincipalAuthorized(InfoGluePrincipal infoGluePrincipal, String interceptionPointName, String parameters) throws SystemException
	{
	    if(infoGluePrincipal == null)
	        return false;
	        
		if(infoGluePrincipal != null && infoGluePrincipal.getIsAdministrator())
			return true;
			
		boolean isPrincipalAuthorized = false;
		
		Database db = CastorDatabaseService.getDatabase();

		try 
		{
			beginTransaction(db);
			
			isPrincipalAuthorized = getIsPrincipalAuthorized(db, infoGluePrincipal, interceptionPointName, parameters);
		
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
					
		return isPrincipalAuthorized;
	}
	
	
	/**
	 * This method checks if a role has access to an entity. It takes name and id of the entity. 
	 */
	public boolean getIsPrincipalAuthorized(Database db, InfoGluePrincipal infoGluePrincipal, String interceptionPointName, String extraParameters) throws SystemException
	{		
		if(infoGluePrincipal != null && infoGluePrincipal.getIsAdministrator())
			return true;
		
		boolean isPrincipalAuthorized = false;
		boolean limitOnGroups = false;
		boolean principalHasRole = false;
		boolean principalHasGroup = false;
		   
		Collection roles = infoGluePrincipal.getRoles();
		Collection groups = infoGluePrincipal.getGroups();
		getLogger().info("roles:" + roles.size());
		getLogger().info("groups:" + groups.size());
		
		InterceptionPoint interceptionPoint = InterceptionPointController.getController().getInterceptionPointWithName(interceptionPointName, db);
		List accessRightList = this.getAccessRightListOnly(interceptionPoint.getId(), extraParameters, db);

		Iterator accessRightListIterator = accessRightList.iterator();
		while(accessRightListIterator.hasNext())
		{
		    AccessRight accessRight = (AccessRight)accessRightListIterator.next();
		    Collection approvedRoles = accessRight.getRoles();
		    Collection approvedGroups = accessRight.getGroups();
		    
		    
		    Iterator rolesIterator = roles.iterator();
			outer:while(rolesIterator.hasNext())
			{
				InfoGlueRole role = (InfoGlueRole)rolesIterator.next();
				getLogger().info("role:" + role.getName());
				
				Iterator approvedRolesIterator = approvedRoles.iterator();
				while(approvedRolesIterator.hasNext())
				{
				    AccessRightRole accessRightRole = (AccessRightRole)approvedRolesIterator.next();
				    if(accessRightRole.getRoleName().equals(role.getName()))
				    {
				        principalHasRole = true;
				        break outer;
				    }
				}
			}
 
			Iterator approvedGroupsIterator = approvedGroups.iterator();
			outer:while(approvedGroupsIterator.hasNext())
			{
			    AccessRightGroup accessRightGroup = (AccessRightGroup)approvedGroupsIterator.next();
			    getLogger().info("accessRightGroup:" + accessRightGroup.getGroupName());

			    limitOnGroups = true;

			    Iterator groupsIterator = groups.iterator();
				while(groupsIterator.hasNext())
				{
				    InfoGlueGroup group = (InfoGlueGroup)groupsIterator.next();
				    if(accessRightGroup.getGroupName().equals(group.getName()))
				    {
				        principalHasGroup = true;
				        break outer;
				    }
				}
			}

		}
		
	    if((principalHasRole && principalHasGroup) || (principalHasRole && !limitOnGroups))
		    isPrincipalAuthorized = true;
			    
		return isPrincipalAuthorized;
	}
	
	/**
	 * This method checks if a role has access to an entity. It takes name and id of the entity. 
	 */
	public boolean getIsPrincipalAuthorized(InfoGluePrincipal infoGluePrincipal, String interceptionPointName) throws SystemException
	{
		if(infoGluePrincipal.getIsAdministrator())
			return true;
			
		String key = "" + infoGluePrincipal.getName() + "_" + interceptionPointName;
		getLogger().info("key:" + key);
		Boolean cachedIsPrincipalAuthorized = (Boolean)CacheController.getCachedObject("authorizationCache", key);
		if(cachedIsPrincipalAuthorized != null)
		{
			getLogger().info("There was an cached authorization:" + cachedIsPrincipalAuthorized);
			return cachedIsPrincipalAuthorized.booleanValue();
		}
		
		boolean isPrincipalAuthorized = false;
		
		Database db = CastorDatabaseService.getDatabase();

		try 
		{
			beginTransaction(db);
			
			isPrincipalAuthorized = getIsPrincipalAuthorized(db, infoGluePrincipal, interceptionPointName);
			
			CacheController.cacheObject("authorizationCache", key, new Boolean(isPrincipalAuthorized));
			
			commitTransaction(db);
		} 
		catch (Exception e) 
		{
			getLogger().info("An error occurred so we should not complete the transaction:" + e);
			rollbackTransaction(db);
			throw new SystemException(e.getMessage());
		}
					
		return isPrincipalAuthorized;
	}

	/**
	 * This method checks if a role has access to an entity. It takes name and id of the entity. 
	 */
	
	public boolean getIsPrincipalAuthorized(Database db, InfoGluePrincipal infoGluePrincipal, String interceptionPointName) throws SystemException
	{		
	    if(infoGluePrincipal.getIsAdministrator())
			return true;

		/*
		String key = "cachedAccessRightsVOList";
		getLogger().info("key:" + key);
		List cachedAccessRightsVOList = (List)CacheController.getCachedObject("authorizationCache", key);
		if(cachedAccessRightsVOList == null)
		{
		    cachedAccessRightsVOList = this.getAccessRightVOList();
		    CacheController.cacheObject("authorizationCache", key, cachedAccessRightsVOList);
		}
		*/
		boolean isPrincipalAuthorized = false;
		boolean limitOnGroups = false;
		boolean principalHasRole = false;
		boolean principalHasGroup = false;
		   
		Collection roles = infoGluePrincipal.getRoles();
		Collection groups = infoGluePrincipal.getGroups();
		InterceptionPoint interceptionPoint = InterceptionPointController.getController().getInterceptionPointWithName(interceptionPointName, db);
		List accessRightList = this.getAccessRightList(interceptionPoint.getId(), db);

		Iterator accessRightListIterator = accessRightList.iterator();
		while(accessRightListIterator.hasNext())
		{
		    AccessRight accessRight = (AccessRight)accessRightListIterator.next();
		    Collection approvedRoles = accessRight.getRoles();
		    Collection approvedGroups = accessRight.getGroups();
		    
		    
		    Iterator rolesIterator = roles.iterator();
			outer:while(rolesIterator.hasNext())
			{
				InfoGlueRole role = (InfoGlueRole)rolesIterator.next();
				getLogger().info("role:" + role.getName());
				
				Iterator approvedRolesIterator = approvedRoles.iterator();
				while(approvedRolesIterator.hasNext())
				{
				    AccessRightRole accessRightRole = (AccessRightRole)approvedRolesIterator.next();
				    if(accessRightRole.getRoleName().equals(role.getName()))
				    {
				        principalHasRole = true;
				        break outer;
				    }
				}
			}
 
		    Iterator approvedGroupsIterator = approvedGroups.iterator();
			outer:while(approvedGroupsIterator.hasNext())
			{
			    AccessRightGroup accessRightGroup = (AccessRightGroup)approvedGroupsIterator.next();
			    getLogger().info("accessRightGroup:" + accessRightGroup.getGroupName());

			    limitOnGroups = true;

			    Iterator groupsIterator = groups.iterator();
				while(groupsIterator.hasNext())
				{
				    InfoGlueGroup group = (InfoGlueGroup)groupsIterator.next();
				    if(accessRightGroup.getGroupName().equals(group.getName()))
				    {
				        principalHasGroup = true;
				        break outer;
				    }
				}
			}

		}
		
	    if((principalHasRole && principalHasGroup) || (principalHasRole && !limitOnGroups))
		    isPrincipalAuthorized = true;
		
	    getLogger().info("isPrincipalAuthorized:" + isPrincipalAuthorized);
	    
		return isPrincipalAuthorized;
	}
	
	/**
	 * This is a method that gives the user back an newly initialized ValueObject for this entity that the controller
	 * is handling.
	 */

	public BaseEntityVO getNewVO()
	{
		return new AccessRightVO();
	}

}
 