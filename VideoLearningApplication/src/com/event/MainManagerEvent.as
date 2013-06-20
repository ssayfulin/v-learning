package com.event
{
	import flash.events.Event;
	import flash.net.NetConnection;
	
	import mx.collections.IList;
	
	public class MainManagerEvent extends Event
	{
		// constants
		public static const SET_LOGGED_USER:String="setLoggedUser";
		public static const SET_CONNECTED_USERS:String="setConnectedUsers";
		public static const LEAVE_USER:String="leaveUser";
		public static const SEND_NOTIFICATON_JOIN_USER:String = "sendNotificationJoinUser";
		public static const JOUN_USER:String="joinUser";
		public static const SEND_NOTOFICATION_CHANGE_STATUS_USER:String = "sendNotificationChangeStatusUser";
		public static const CHANGE_STATUS_USER:String="changeStatusUser";
		public static const SEND_CHAT_MESSAGE_USER:String="sendChatMessageUser";
		public static const CHECK_CHAT_MESSAGE_USER:String="checkChatMessageUser";
		
		// properties
		// TODO : rename property
		public var nameLoggedUser:String;
		public var idClient:String;
        public var netConnection:NetConnection;
        public var roleUser:String;
        public var statusUser:String;
		public var arrList:IList;
		public var message:String;
		
		
		public function MainManagerEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}