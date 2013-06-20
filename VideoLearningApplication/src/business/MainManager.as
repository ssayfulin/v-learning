package business
{
import com.event.MainManagerEvent;
import com.model.User;

import flash.events.IEventDispatcher;
import flash.net.NetConnection;

import mx.collections.ArrayCollection;
import mx.controls.Alert;


public class MainManager
{
	// properties
	
	[Bindable]
	public var connectedUsers : ArrayCollection;
	
	[Bindable]
	public var netConnection:NetConnection;

	private var dispatcher:IEventDispatcher;
    
    private var nameLoggedUser:String;
    private var idClient:String;
    private var roleUser:String;

    // constructor
	public function MainManager(dispatcher:IEventDispatcher)
	{
		this.dispatcher = dispatcher;
	}
	// methods
	
	
	/**
 	 * new user connected to the Plateforme
	 * @param
	 * arg - connected user name
 	 */
	public function onLoggedUserJoinDeck(name:String, id:String, role:String) : void
	{
		nameLoggedUser=name;
		idClient=id;
		roleUser=role;
    }
	
	public function netConnectionSuccess(event : Object) : void
	{
		var mainManagerEvent:MainManagerEvent = new MainManagerEvent(MainManagerEvent.SET_LOGGED_USER);
		mainManagerEvent.nameLoggedUser = nameLoggedUser;
		mainManagerEvent.idClient = idClient;
		mainManagerEvent.roleUser = roleUser;
		mainManagerEvent.netConnection = this.netConnection;
		dispatcher.dispatchEvent(mainManagerEvent);
	}
	
	public function onJoinUser(name:String, id:String, role:String, status:String = "passive") : void
	{
		var mainManagerEvent:MainManagerEvent = new MainManagerEvent(MainManagerEvent.JOUN_USER);
		mainManagerEvent.nameLoggedUser = name;
		mainManagerEvent.idClient = id;
		mainManagerEvent.roleUser = role;
		mainManagerEvent.netConnection = this.netConnection;
		dispatcher.dispatchEvent(mainManagerEvent);
	}
	
	/**
	 * user discconnected, walk out from the plateforme
	 * @param
	 * arg - disconnected user
	 */ 
	public function onLeaveUser(arg : Object) : void
	{
	   var outDeckEvent:MainManagerEvent = new MainManagerEvent(MainManagerEvent.LEAVE_USER);
       outDeckEvent.idClient = arg.toString();
       this.dispatcher.dispatchEvent(outDeckEvent);
	}

	public function onCheckConnectedClients(obj:Object):void
	{
		var checkConnectedUsersEvent:MainManagerEvent = new MainManagerEvent(MainManagerEvent.SET_CONNECTED_USERS);
		var arr:ArrayCollection = new ArrayCollection();
		for each(var elm:Object in obj as Array)
		{
			var user:User = new User();
			user.nameUser = elm[0];
			user.idUser = elm[1];
			user.roleUser = elm[2];
			user.statusUser = elm[3];
			arr.addItem(user);
		}
		checkConnectedUsersEvent.arrList = arr;
       	dispatcher.dispatchEvent(checkConnectedUsersEvent);
	}
	
	public function onChangeStatusUser(userId:String, status:String):void
	{
		var mainManagerEvent:MainManagerEvent = new MainManagerEvent(MainManagerEvent.CHANGE_STATUS_USER);
		mainManagerEvent.idClient = userId;
		mainManagerEvent.statusUser = status;
       	this.dispatcher.dispatchEvent(mainManagerEvent);
	}
	
	public function onCheckChatMessageUser(userId:String, message:String):void
	{
		var checkMessageUser:MainManagerEvent = new MainManagerEvent(MainManagerEvent.CHECK_CHAT_MESSAGE_USER);
		checkMessageUser.idClient = userId;
		checkMessageUser.message = message;
		this.dispatcher.dispatchEvent(checkMessageUser);
	}


	
}
}
