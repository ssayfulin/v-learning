/**
 * Copyright Université Lyon 1 / Université Lyon 2 (2009,2010)
 * 
 * <ithaca@liris.cnrs.fr>
 * 
 * This file is part of Visu.
 * 
 * This software is a computer program whose purpose is to provide an
 * enriched videoconference application.
 * 
 * Visu is a free software subjected to a double license.
 * You can redistribute it and/or modify since you respect the terms of either 
 * (at least one of the both license) :
 * - the GNU Lesser General Public License as published by the Free Software Foundation; 
 *   either version 3 of the License, or any later version. 
 * - the CeCILL-C as published by CeCILL; either version 2 of the License, or any later version.
 * 
 * -- GNU LGPL license
 * 
 * Visu is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Visu is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Visu.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * -- CeCILL-C license
 * 
 * This software is governed by the CeCILL-C license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-C
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 * 
 * -- End of licenses
 */
package com.lyon2.visu;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 *
 * Copyright (c) 2006-2008 by respective authors (see below). All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.service.ServiceUtils;
import org.red5.server.api.stream.IBroadcastStream;
import org.slf4j.Logger;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends MultiThreadedApplicationAdapter {

	private String logShowMan = "ivis4mac";
	private String logInvite = "invite";
	
	private String SHOW_MAN = "SM";
	private String OPERATOR = "Op";
	private String INVITE = "In";
	private String VIEWER = "Vw";
	
	
	private static Logger log = Red5LoggerFactory.getLogger(Application.class,
			"visu2");

	public Application() {
		super();

		log.info("======== Instanciated {} ==========", Application.class
				.toString());
	}

	public boolean appStart(IScope app) {

		log.info("=== VisuServer start ===");
		log.debug("=== DEBUG test message ===");
		log.info("=== INFO test message ===");
		log.warn("=== WARN. test message ===");
		log.error("=== ERROR. test message ===");
		return super.appStart(app);
	}
	
	@SuppressWarnings("unchecked")
	public boolean appConnect(IConnection conn, Object[] params) {
		if (!super.appConnect(conn, params)) {
			return false;
		}
		IClient client = conn.getClient();
		HashMap<String, String> tempUserMap = (HashMap<String, String>) params[0];
		String nameUser = tempUserMap.get("nameUser");
		client.setAttribute("nameUser", nameUser);
		
		log.warn("======appConnect nameUser = {} ",	nameUser);
		
		// set role user
		String roleUser = VIEWER;
		if(nameUser.equalsIgnoreCase(getLogShowMan()))
		{
			roleUser = SHOW_MAN;
			// check if has other SHOW MAN
		}else if(nameUser.equalsIgnoreCase(getLogInvite()))
		{
			roleUser = INVITE;
			// add id user to end nameUser
			client.setAttribute("nameUser", nameUser + "_"+client.getId());
		}
		client.setAttribute("role", roleUser);
		client.setAttribute("status", "passive");
		return true;
	}

	public void appDisconnect(IConnection conn) {
		super.appDisconnect(conn);
		log.warn("====appDisconnect====");
		log.warn("*** User " + "nameUser" + " - " + conn.getClient().getId()
				+ " out from plateforme" + conn.getScope().getName());
		// Get the Client Scope
		IScope scope = conn.getScope();
		Object[] args = { conn.getClient().getId() };
		// notify all the client in the scope that one is disconnect 
		invokeOnScopeClients(scope, "leaveUser", args);
		// TODO : change status In if SM leave
		
		
		log.warn("====appDisconnect==== end");
	}
	
	
	public boolean roomConnect(IConnection conn, Object[] params) {
		if (!super.roomConnect(conn, params)) {
			return false;
		}
		IClient client = conn.getClient();
		Object[] args = { client.getAttribute("nameUser") , client.getId(), client.getAttribute("role")};
		if (conn instanceof IServiceCapableConnection) {
			IServiceCapableConnection sc = (IServiceCapableConnection) conn;
			sc.invoke("loggedUserJoinDeck", args);
		}
		return true;
	}

	public void notificationJoinUser(IConnection conn)
	{
		IClient client = conn.getClient();
		log.warn("===== notificationJoinUser ");
		Object[] args = { client.getAttribute("nameUser"), client.getId(), client.getAttribute("role"), client.getAttribute("status")};
		// Get the Client Scope
		IScope scope = conn.getScope();
		// notify ALL the client in the scope that new user is join
		invokeOnScopeClients(scope, "joinUser", args);
		
		// send list connected users to LOGGED user
		List<List<String>> userlist = new Vector<List<String>>();
		for (IClient connectedClient : this.getClients()) {
			List<String> info = new Vector<String>();
			info.add((String) connectedClient.getAttribute("nameUser"));
			info.add(connectedClient.getId());
			info.add((String) connectedClient.getAttribute("role"));
			info.add((String) connectedClient.getAttribute("status"));
			userlist.add(info);
		}

		Object[] obj = { userlist };
		if (conn instanceof IServiceCapableConnection) {
			IServiceCapableConnection sc = (IServiceCapableConnection) conn;
			sc.invoke("checkConnectedClients", obj);
		}	
	}
	
	public void notificationChangeStatusUser(IConnection conn, String userId, String status)
	{
		log.warn("===== notificationChangeStatusUser ");
		log.warn(" userId " + userId + "status" + status);
		
		for (IClient client : this.getClients()) {
			String clientId = client.getId();
			if(clientId.equalsIgnoreCase(userId))
			{
				client.setAttribute("status", status);
				log.warn(" set status " + status + "for usesr  " + client.getAttribute("nameUser"));
			}
		}
		Object[] obj = { userId, status };
		IScope scope = conn.getScope();
		// notify ALL the client in the scope that new user is join
		invokeOnScopeClients(scope, "changeStatusUser", obj);
	}
	
	public void sendChatMessageUser(IConnection conn, String message)
	{
		log.warn("===== sendChatMessageUser ");
		
		Object[] obj = { conn.getClient().getId(), message };
		IScope scope = conn.getScope();
		// notify ALL the client in the scope that new user is join
		invokeOnScopeClients(scope, "checkChatMessageUser", obj);
	}
	
	

	

	

//	/**
//	 *  Get id client tuteur
//	 */
//	public void getIdTuteur(IConnection conn, String nameTuteur) {
//		
//		log.warn("=== getIdTuteur nameTuteur = {}", nameTuteur);
//		String idClientTuteur = "void";
//		for (IClient client : this.getClients()) {
//			String nameUser = (String) client.getAttribute("nameUser");
//			if(nameUser.equals(nameTuteur))
//			{
//				idClientTuteur = client.getId();
//			}
//		}
//		log.warn("=== idClientTuteur = {}", idClientTuteur);
//		Object[] obj = { idClientTuteur };
//		if (conn instanceof IServiceCapableConnection) {
//			IServiceCapableConnection sc = (IServiceCapableConnection) conn;
//			sc.invoke("checkIdClientTuteur", obj);
//		}
//	}
	
	
	
//	/**
//	 *  Set id client tuteur
//	 */
//	public void setIdTuteur(IConnection conn, String idClientTuteur) {
//		
//		log.warn("=== setIdTuteur idClientTuteur = {}", idClientTuteur);
//		Object[] args = { idClientTuteur };
//		// Get the Client Scope
//		IScope scope = conn.getScope();
//		invokeOnScopeClients(scope, "checkIdClientTuteur", args);
//	}
	
	public void invokeOnScopeClients(IScope scope, String method, Object[] arg) {
		Collection<Set<IConnection>> conCollection = scope.getConnections();
		for (Set<IConnection> cons : conCollection) {
			for (IConnection client_cnx : cons) {
				if (client_cnx != null) {
					if (client_cnx instanceof IServiceCapableConnection) {
						log.warn("invoke {}->{}", client_cnx.getClient(),
								method);
						ServiceUtils
								.invokeOnConnection(client_cnx, method, arg);
					}
				}
			}
		}
	}
	
	public void setLogShowMan(String value) {
		this.logShowMan = value;
	}
	public String getLogShowMan() {
		return this.logShowMan;
	}
	public void setLogInvite(String value) {
		this.logInvite = value;
	}	
	public String getLogInvite() {
		return this.logInvite;
	}
	
	//////////////////////////
	// unused functions
	/////////////////////////
	private void startPub(String clientIdOfStreamString) 
	{
		log.warn("========== startPub");
	}

	public void streamPublishStart(IBroadcastStream stream) {
		super.streamPublishStart(stream);
		log.warn("stream publish start: " + stream.getPublishedName());
		startPub(stream.getPublishedName());
	}

	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		super.streamBroadcastStart(stream);
		log.warn("stream broadcast start: " + stream.getPublishedName());
	}
}
