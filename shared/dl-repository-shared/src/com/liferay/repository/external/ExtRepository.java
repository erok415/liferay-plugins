/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.repository.external;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.repository.external.search.ExtRepositoryQueryMapper;

import java.io.InputStream;

import java.util.List;

/**
 * Represents the external repository for Liferay's Document Library, providing
 * utility methods for adding, deleting, retrieving, searching, and updating
 * external repository file entries and folders.
 *
 * <p>
 * Most data transferred in this class is in the external repository's domain
 * format. For example, external repository data such as object keys and user
 * names are strings expressed in the native external repository format.
 * </p>
 *
 * <p>
 * One exception is the {@link #search(SearchContext, Query,
 * ExtRepositoryQueryMapper)} method where the {@link Query} contains Liferay
 * identifiers and user names. To help with this, the {@link
 * ExtRepositoryQueryMapper} instance can be used to translate such IDs to the
 * native external repository format.
 * </p>
 *
 * @author Iván Zaera
 * @author Sergio González
 */
public interface ExtRepository {

	/**
	 * Adds an external repository file entry and associated metadata based on
	 * the {@link InputStream} object.
	 *
	 * @param  extRepositoryParentFolderKey the primary key of the repository
	 *         file entry's parent folder
	 * @param  mimeType the repository file entry's MIME type
	 * @param  title the repository file entry's name
	 * @param  description the repository file entry's description
	 * @param  changeLog the repository file entry's version change log
	 * @param  inputStream the repository file entry's data (optionally
	 *         <code>null</code>)
	 * @return a repository file entry and associated metadata
	 * @throws PortalException if the repository parent folder could not be
	 *         found or if the repository file entry's information was invalid
	 * @throws SystemException if a system exception occurred
	 */
	public ExtRepositoryFileEntry addExtRepositoryFileEntry(
			String extRepositoryParentFolderKey, String mimeType, String title,
			String description, String changeLog, InputStream inputStream)
		throws PortalException, SystemException;

	/**
	 * Adds an external repository folder.
	 *
	 * @param  extRepositoryParentFolderKey the primary key of the repository
	 *         folder's parent folder
	 * @param  name the repository folder's name
	 * @param  description the repository folder's description
	 * @return a repository folder
	 * @throws PortalException if the repository parent folder could not be
	 *         found or if the repository folder's information was invalid
	 * @throws SystemException if a system exception occurred
	 */
	public ExtRepositoryFolder addExtRepositoryFolder(
			String extRepositoryParentFolderKey, String name,
			String description)
		throws PortalException, SystemException;

	/**
	 * Cancels the check out of the external repository file. If a user has not
	 * checked out the external repository file entry, invoking this method
	 * results in no changes.
	 *
	 * @param  extRepositoryFileEntryKey the primary key of the repository file
	 *         entry
	 * @return the discarded repository file version (optionally
	 *         <code>null</code>)
	 * @throws PortalException if the repository file entry's information was
	 *         invalid
	 * @throws SystemException if a system exception occurred
	 */
	public ExtRepositoryFileVersion cancelCheckOut(
			String extRepositoryFileEntryKey)
		throws PortalException, SystemException;

	/**
	 * Checks in the external repository file entry. If a user has not checked
	 * out the external repository file entry, invoking this method results in
	 * no changes.
	 *
	 * @param  extRepositoryFileEntryKey the primary key of the repository file
	 *         entry
	 * @param  createMajorVersion whether to increase the major or minor version
	 *         number
	 * @param  changeLog the description of the changes being checked in
	 * @throws PortalException if the repository file entry's information was
	 *         invalid
	 * @throws SystemException if a system exception occurred
	 */
	public void checkInExtRepositoryFileEntry(
			String extRepositoryFileEntryKey, boolean createMajorVersion,
			String changeLog)
		throws PortalException, SystemException;

	/**
	 * Checks out the external repository file entry.
	 *
	 * @param  extRepositoryFileEntryKey the primary key of the repository file
	 *         entry
	 * @return the checked out repository file entry
	 * @throws PortalException if the repository file entry's information was
	 *         invalid
	 * @throws SystemException if a system exception occurred
	 */
	public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(
			String extRepositoryFileEntryKey)
		throws PortalException, SystemException;

	/**
	 * Copies the external repository object to a different parent folder.
	 *
	 * @param  extRepositoryObjectType the repository object's type (file or
	 *         folder)
	 * @param  extRepositoryFileEntryKey the primary key of the repository
	 *         object
	 * @param  newExtRepositoryFolderKey the primary key of the repository
	 *         destination folder
	 * @param  newTitle the new name of the repository object in the destination
	 *         folder
	 * @return the repository object
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public <T extends ExtRepositoryObject> T copyExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFileEntryKey, String newExtRepositoryFolderKey,
			String newTitle)
		throws PortalException, SystemException;

	/**
	 * Deletes the external repository object.
	 *
	 * @param  extRepositoryObjectType the repository object's type (file or
	 *         folder)
	 * @param  extRepositoryObjectKey the primary key of the repository object
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public void deleteExtRepositoryObject(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws PortalException, SystemException;

	public String getAuthType();

	/**
	 * Returns the content stream of the external repository file entry.
	 *
	 * @param  extRepositoryFileEntry the primary key of the repository file
	 *         entry
	 * @return the content stream of the repository file entry
	 * @throws PortalException if the repository file entry's information was
	 *         invalid
	 * @throws SystemException if a system exception occurred
	 */
	public InputStream getContentStream(
			ExtRepositoryFileEntry extRepositoryFileEntry)
		throws PortalException, SystemException;

	/**
	 * Returns the content stream of the external repository file version.
	 *
	 * @param  extRepositoryFileVersion the primary key of the repository file
	 *         version
	 * @return the content stream of the repository file version
	 * @throws PortalException if the repository file version's information was
	 *         invalid
	 * @throws SystemException if a system exception occurred
	 */
	public InputStream getContentStream(
			ExtRepositoryFileVersion extRepositoryFileVersion)
		throws PortalException, SystemException;

	/**
	 * Returns the external repository file version of the file entry,
	 * identified by its version name (e.g. <code>1.0</code>).
	 *
	 * @param  extRepositoryFileEntry the primary key of the repository file
	 *         entry
	 * @param  version the repository version name
	 * @return the repository file version of the file entry
	 * @throws PortalException if the repository file entry or version
	 *         information was invalid
	 * @throws SystemException if a system exception occurred
	 */
	public ExtRepositoryFileVersion getExtRepositoryFileVersion(
			ExtRepositoryFileEntry extRepositoryFileEntry, String version)
		throws PortalException, SystemException;

	/**
	 * Translates the external repository file version key into an {@link
	 * ExtRepositoryFileVersionDescriptor}, describing the external repository
	 * file entry key and version name.
	 *
	 * @param  extRepositoryFileVersionKey the repository file version's key
	 * @return the translated {@link ExtRepositoryFileVersionDescriptor}
	 */
	public ExtRepositoryFileVersionDescriptor
		getExtRepositoryFileVersionDescriptor(
			String extRepositoryFileVersionKey);

	/**
	 * Returns the external repository file versions of the external repository
	 * file entry. The list is ordered by version age, starting with the newest
	 * version at index <code>0</code>.
	 *
	 * @param  extRepositoryFileEntry the primary key of the repository file
	 *         entry
	 * @return the repository file versions of the repository file entry
	 * @throws PortalException if the repository file entry was invalid
	 * @throws SystemException if a system exception occurred
	 */
	public List<ExtRepositoryFileVersion> getExtRepositoryFileVersions(
			ExtRepositoryFileEntry extRepositoryFileEntry)
		throws PortalException, SystemException;

	/**
	 * Returns the external repository object matching the key.
	 *
	 * @param  extRepositoryObjectType the repository object's type (file or
	 *         folder)
	 * @param  extRepositoryObjectKey the primary key of the repository object
	 * @return the repository object matching the key
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws PortalException, SystemException;

	/**
	 * Returns the external repository object matching the name and parent
	 * folder.
	 *
	 * @param  extRepositoryObjectType the repository object's type (file or
	 *         folder)
	 * @param  extRepositoryFolderKey the primary key of the repository object's
	 *         parent folder
	 * @param  title the repository object's name
	 * @return the repository object matching the name and parent folder
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey, String title)
		throws PortalException, SystemException;

	/**
	 * Returns the external repository objects in the folder. Depending on the
	 * type of repository object requested, this method may return only
	 * repository files, only repository folders, or both repository files and
	 * folders.
	 *
	 * @param  extRepositoryObjectType the type of repository objects to return
	 * @param  extRepositoryFolderKey the primary key of the repository folder
	 *         to search
	 * @return the repository objects contained in the folder
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws PortalException, SystemException;

	/**
	 * Returns the number of elements in the external repository folder matching
	 * the object type. Depending on the repository object type requested, this
	 * method may only return the number of repository files, the number of
	 * repository folders, or the number of both repository files and folders.
	 *
	 * @param  extRepositoryObjectType the repository object type to count
	 * @param  extRepositoryFolderKey the primary key of the repository folder
	 *         to search
	 * @return the number of elements in the repository folder matching the
	 *         object type
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public int getExtRepositoryObjectsCount(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws PortalException, SystemException;

	/**
	 * Returns the external repository parent folder of the external repository
	 * object.
	 *
	 * @param  extRepositoryObject the repository object (file or folder)
	 * @return the repository parent folder of the repository object
	 * @throws PortalException if the repository object's information was
	 *         invalid
	 * @throws SystemException if a system exception occurred
	 */
	public ExtRepositoryFolder getExtRepositoryParentFolder(
			ExtRepositoryObject extRepositoryObject)
		throws PortalException, SystemException;

	/**
	 * Returns the Liferay login value, which is mapped from the user's external
	 * repository login value. A Liferay login value can be a user ID, screen
	 * name, e-mail, etc, depending on the value returned by the {@link
	 * #getAuthType()} method. The format of the repository login is specific
	 * for each repository implementation.
	 *
	 * @param  extRepositoryLogin the user's repository login value
	 * @return the Liferay login value, which is mapped from the user's
	 *         repository login value
	 */
	public String getLiferayLogin(String extRepositoryLogin);

	/**
	 * Returns the primary key of the external repository root folder.
	 *
	 * @return the primary key of the repository root folder
	 * @throws PortalException if the repository root folder could not be
	 *         accessed
	 * @throws SystemException if a system exception occurred
	 */
	public String getRootFolderKey() throws PortalException, SystemException;

	/**
	 * Returns the keys of the external repository subfolders stored inside the
	 * external repository folder.
	 *
	 * @param  extRepositoryFolderKey the primary key of the repository folder
	 * @param  recurse whether to recurse through each repository subfolder
	 * @return the keys of the repository subfolders stored inside the Ext
	 *         repository folder
	 * @throws PortalException if the repository folder's information was
	 *         invalid
	 * @throws SystemException if a system exception occurred
	 */
	public List<String> getSubfolderKeys(
			String extRepositoryFolderKey, boolean recurse)
		throws PortalException, SystemException;

	/**
	 * Returns the supported configurations for the external repository, which
	 * may each have different supported parameters.
	 *
	 * @return the supported configurations for the repository
	 * @see    #getSupportedParameters()
	 */
	public String[] getSupportedConfigurations();

	/**
	 * Returns the supported external repository configuration parameters
	 * indexed by configuration type. These parameters are stored in the
	 * database when the repository is configured and made available using the
	 * {@link #initRepository(UnicodeProperties, CredentialsProvider)} method.
	 *
	 * @return the supported repository configuration parameters indexed by
	 *         configuration type
	 * @see    #getSupportedConfigurations()
	 */
	public String[][] getSupportedParameters();

	/**
	 * Initializes the external repository and checks the connectivity between
	 * the external repository and Liferay Portal.
	 *
	 * <p>
	 * This method uses the credentials provided by the {@link
	 * CredentialsProvider} object to authenticate to the repository. In
	 * addition, the method acquires the repository-specific configuration
	 * parameters from the type settings properties parameter. The parameters
	 * contained in this variable are indexed by the key names returned by the
	 * {@link #getSupportedParameters()} method. This method is called only
	 * once, the first time the repository is used.
	 * </p>
	 *
	 * @param  typeSettingsProperties the type settings properties
	 * @param  credentialsProvider the Liferay Portal username and password
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public void initRepository(
			UnicodeProperties typeSettingsProperties,
			CredentialsProvider credentialsProvider)
		throws PortalException, SystemException;

	/**
	 * Moves the external repository object to a different location. This method
	 * can also be used to rename repository objects.
	 *
	 * @param  extRepositoryObjectType the repository object's type (file or
	 *         folder)
	 * @param  extRepositoryObjectKey the primary key of the repository object
	 * @param  newExtRepositoryFolderKey the primary key of the repository
	 *         destination folder
	 * @param  newTitle the new name of the repository object (may not change)
	 * @return the repository object
	 * @throws PortalException if the repository object could not be moved or if
	 *         a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public <T extends ExtRepositoryObject> T moveExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey, String newExtRepositoryFolderKey,
			String newTitle)
		throws PortalException, SystemException;

	/**
	 * Returns the external repository objects fulfilling the query. There may
	 * be some limitations due to back-end repository constraints.
	 *
	 * @param  searchContext the search context to be applied. The folder ID,
	 *         start bound, and end bound attributes must be set in this search
	 *         context.
	 * @param  query the logical expression describing the query to be
	 *         performed. This may need to be translated to an equivalent native
	 *         repository query.
	 * @param  extRepositoryQueryMapper the repository query mapper to translate
	 *         Liferay IDs to the native repository format
	 * @return the repository objects fulfilling the query
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public List<ExtRepositorySearchResult<?>> search(
			SearchContext searchContext, Query query,
			ExtRepositoryQueryMapper extRepositoryQueryMapper)
		throws PortalException, SystemException;

	/**
	 * Updates the external repository file entry's content.
	 *
	 * @param  extRepositoryFileEntryKey the primary key of the repository file
	 *         entry
	 * @param  mimeType the repository file entry's MIME type
	 * @param  inputStream the new repository file entry's content
	 * @return the updated file entry
	 * @throws PortalException if a portal exception occurred
	 * @throws SystemException if a system exception occurred
	 */
	public ExtRepositoryFileEntry updateExtRepositoryFileEntry(
			String extRepositoryFileEntryKey, String mimeType,
			InputStream inputStream)
		throws PortalException, SystemException;

}