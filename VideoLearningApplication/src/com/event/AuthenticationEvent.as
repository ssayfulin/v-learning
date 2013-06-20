package com.event
{
	
	import flash.events.Event;
	
	public class AuthenticationEvent extends Event
	{
		public static const CONNECT:String = "connect";
		public static const DISCONNECT:String = "disconnect";
		public static const AUTHENTICATION_ERROR:String = "authentication_error";
		public static const GET_ID_TUTER:String = "getIdTuteur";
		public static const SET_ID_TUTER:String = "setIdTuteur";
		

		public var params : Object;
		public var rtmpSever : String;
		public var nameTuteur : String;
		public var idClientTuteur : String;
		
		public function AuthenticationEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		override public  function toString() : String
		{ return "AuthenticationEvent " + params ; }
	}
}