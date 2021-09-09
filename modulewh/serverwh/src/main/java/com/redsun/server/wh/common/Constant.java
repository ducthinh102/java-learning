
package com.redsun.server.wh.common;

import java.util.HashMap;

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

	public final static int SERVERDB_STATUS_WAITAPPROVE = 10;
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
	public final static int SERVERCODE_EXITMATERIAL = 105;
	public final static int SERVERCODE_EXISTMATERIALCODE = 108;
	public final static int SERVERCODE_EXISTCODEANDMATERIALCODE = 106;
	public final static int SERVERCODE_EXISTNAMEANDMATERIALCODE = 107;
	public final static int SERVERCODE_OUT_OF_STOCK = 109;
	
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
	public final static int MATERIALPRE = 7;
	public final static int MATERIALTECH = 8;
	public final static int REQUESTEXTRA = 9;
	
	/***********************************************************
	 * Reftype map
	 ***********************************************************/
	public final static HashMap<String, Integer> REFTYPE = new HashMap<>();
	static {
		REFTYPE.put("quotation", 1);
		REFTYPE.put("purchase", 2);
		REFTYPE.put("request", 3);
		REFTYPE.put("materialimport", 4);
		REFTYPE.put("materialexport", 5);
		REFTYPE.put("materialbaseline", 6);
		REFTYPE.put("materialpre", 7);
		REFTYPE.put("materialtech", 8);
		REFTYPE.put("requestextra", 9);
    }

	/***********************************************************
	 * Workflow map
	 ***********************************************************/
	public final static HashMap<Integer, String> WORKFLOW = new HashMap<>();
	static {
		WORKFLOW.put(1, "quotation");
		WORKFLOW.put(2, "purchase");
		WORKFLOW.put(3, "request");
		WORKFLOW.put(4, "materialimport");
		WORKFLOW.put(5, "materialexport");
		WORKFLOW.put(6, "materialbaseline");
		WORKFLOW.put(7, "materialpre");
		WORKFLOW.put(8, "materialtech");
		WORKFLOW.put(9, "requestextra");
    }
	
	/***********************************************************
	 * Workflow tabs
	 ***********************************************************/
	public final static String TAB_CREATED = "created";
	public final static String TAB_RECEIVED = "received";
	public final static String TAB_ASSIGNED = "assigned";
	public final static String TAB_DELETED = "deleted";
	
}