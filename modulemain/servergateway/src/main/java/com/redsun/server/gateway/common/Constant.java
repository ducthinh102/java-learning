
package com.redsun.server.gateway.common;

public class Constant {
	
	public final static String COMPOSITE_KEY_SEPARATOR = "|";
	
	/***********************************************************
	 * App config
	 ***********************************************************/
	public final static String APPCONFIG_SCOPE_MAILSERVER = "mailserver";
	
	/***********************************************************
	 * DB config
	 ***********************************************************/

	public final static int SERVERDB_lOCKTIMEOUT = 3600; // 1 hour.
	
	public final static int SERVERDB_STATUS_NEW = 1;
	public final static int SERVERDB_STATUS_UPDATE = 2;
	public final static int SERVERDB_STATUS_DELETE = 3;
	public final static int SERVERDB_STATUS_NOTUSE = 4;
	
	public final static int SERVERDB_STATUS_EXECUTEWORKFLOW = 10;
	public final static int SERVERDB_STATUS_SEND = 11;
	public final static int SERVERDB_STATUS_RECEIVE = 12;
	public final static int SERVERDB_STATUS_GETBACK = 13;
	public final static int SERVERDB_STATUS_SENDBACK = 14;
	public final static int SERVERDB_STATUS_CANCEL = 15;
	public final static int SERVERDB_STATUS_APPROVE = 16;
	public final static int SERVERDB_STATUS_RECEIVESENDBACK = 17;

	
	/***********************************************************
	 * Server code
	 ***********************************************************/
	public final static int SERVERCODE_NOTEXISTID = 100;
	public final static int SERVERCODE_EXISTCODE = 101;
	public final static int SERVERCODE_EXISTNAME = 102;
	public final static int SERVERCODE_EXISTALL = 103;
	public final static int SERVERCODE_DIFFPRICE = 104;
	
	public final static int SERVERCODE_EXISTSCOPE = 110;
	public final static int SERVERCODE_EXISTUSERNAME = 111;
	public final static int SERVERCODE_EXISTFILENAME = 115;
	
	public final static int SERVERCODE_VERSIONDIFFERENCE = 500;
	
	public final static int SERVERCODE_LOCKED = 510;
	
	public final static int SERVERCODE_ERROR = 1000;
	
	/***********************************************************
	 * Authorize
	 ***********************************************************/
	public final static String USSER_INFO = "userinfo";
	
	/***********************************************************
	 * Roles
	 ***********************************************************/
/*	public final static int ROLE_ADMIN = 1;// ADMIN
	public final static int ROLE_USER = 2;// USER
	public final static int ROLE_EXTERNAL = 3;// EXTERNAL
	public final static int ROLE_OWNER = 4;// OWNER
	public final static int ROLE_DIRECTOR = 5;// DIRECTOR
	public final static int ROLE_SUPPLIER = 11;// SUPPLIER
	public final static int ROLE_MATERIAL = 14;// MATERIAL
	public final static int ROLE_MATERIAL_CODE = 15;// MANAGER MATERIAL CODE 
	public final static int ROLE_STORE = 16;// STORE
*/	
	/***********************************************************
	 * ServiceTransaction
	 ***********************************************************/
	public final static int SERVICETRANSACTION_SUCCESS = 1;
	public final static int SERVICETRANSACTION_ABORT = 2;
	
	/***********************************************************
	 * Workflow
	 ***********************************************************/
	public final static int QUOTATION = 1;
	public final static int PURCHASE = 2;
	public final static int REQUEST = 3;
	public final static int MATERIALIMPORT = 4;
	public final static int MATERIALEXPORT = 5;
	public final static int MATERIALBASELINE = 6;

}