
-- ----------------------------
-- Note
-- status: 1=new, 2=update, 3=delete, 4=notuse.
-- workflow status: 10=waitforapprove, 11=send (to multi receivers), 12=receive, 13=getback, 14=sendback, 15=cancel, 16=approve, 17=receivesendback.
-- notify status: 51=send, 52=view, 53=cannotsend, 55=cancel.
-- ----------------------------

-- ----------------------------
-- Table structure for user with scope as 'user', 'customer', 'contact'.
-- ----------------------------
DROP TABLE IF EXISTS public.user;
CREATE TABLE public.user (
	id SERIAL PRIMARY KEY,
	idcalendar integer DEFAULT NULL,
	username varchar(100) DEFAULT NULL,
	password varchar(300) DEFAULT NULL,
	displayname varchar(200) DEFAULT NULL,
	email varchar(200) DEFAULT NULL,
	thumbnail text DEFAULT NULL,
	enabled boolean DEFAULT TRUE,
	firstname varchar(100) DEFAULT NULL,
	lastname varchar(100) DEFAULT NULL,
	address varchar(500) DEFAULT NULL,
	mobile varchar(50) DEFAULT NULL,
	telephone varchar(50) DEFAULT NULL,
	scope varchar(100) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS public.role;
CREATE TABLE public.role (
	id SERIAL PRIMARY KEY,
	code varchar(50) DEFAULT NULL,
	name varchar(100) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for userrole
-- ----------------------------
DROP TABLE IF EXISTS public.userrole;
CREATE TABLE public.userrole (
	id SERIAL PRIMARY KEY,
	iduser integer DEFAULT NULL,
	idrole integer DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS public.permission;
CREATE TABLE public.permission (
	id SERIAL PRIMARY KEY,
	target varchar(100) DEFAULT NULL,
	rules text DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS public.store;
CREATE TABLE public.store (
	id SERIAL PRIMARY KEY,
	idparent integer DEFAULT NULL,
	idmanager integer DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
); 


-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS public.supplier;
CREATE TABLE public.supplier (
	id SERIAL PRIMARY KEY,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	email varchar(200) DEFAULT NULL,
	address varchar(500) DEFAULT NULL,
	mobile varchar(50) DEFAULT NULL,
	telephone varchar(50) DEFAULT NULL,
	fax varchar(50) DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for catalog with scope as 'category' (material or device), 'system', 'type', 'brand', 'origin', 'unit'.
-- ----------------------------
DROP TABLE IF EXISTS public.catalog;
CREATE TABLE public.catalog (
	id SERIAL PRIMARY KEY,
	idparent integer DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	scope varchar(100) DEFAULT NULL,
	scopeparent varchar(100) DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for unitcoefficient
-- ----------------------------
DROP TABLE IF EXISTS public.unitcoefficient;
CREATE TABLE public.unitcoefficient (
	id SERIAL PRIMARY KEY,
	idfromunit integer DEFAULT NULL,
	idtounit integer DEFAULT NULL,
	coefficient integer DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for material with reftype as 'confirm', 'parent'.
-- ----------------------------
DROP TABLE IF EXISTS public.material;
CREATE TABLE public.material (
	id SERIAL PRIMARY KEY,
	idcategory integer DEFAULT NULL,
	idsystem integer DEFAULT NULL,
	idtype integer DEFAULT NULL,
	idbrand integer DEFAULT NULL,
	idspec integer DEFAULT NULL,
	idorigin integer DEFAULT NULL,
	idunit integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	materialcode varchar(50) DEFAULT NULL,
	description varchar(5000) DEFAULT NULL,
	quantity integer DEFAULT NULL,
	thumbnail text DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialstore
-- ----------------------------
DROP TABLE IF EXISTS public.materialstore;
CREATE TABLE public.materialstore (
	id SERIAL PRIMARY KEY,
	idstore integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	quantity integer DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for quotation
-- ----------------------------
DROP TABLE IF EXISTS public.quotation;
CREATE TABLE public.quotation (
	id SERIAL PRIMARY KEY,
	idsupplier integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	startdate date DEFAULT NULL,
	enddate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for quotationdetail
-- ----------------------------
DROP TABLE IF EXISTS public.quotationdetail;
CREATE TABLE public.quotationdetail (
	id SERIAL PRIMARY KEY,
	idquotation integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	idunit integer DEFAULT NULL,
	materialcode varchar(50) DEFAULT NULL,
	price numeric DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialbaseline
-- ----------------------------
DROP TABLE IF EXISTS public.materialbaseline;
CREATE TABLE public.materialbaseline (
	id SERIAL PRIMARY KEY,
	idstore integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	scope varchar(100) DEFAULT NULL,
	totalamount numeric DEFAULT NULL,
	baselinedate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialbaselinedetail
-- ----------------------------
DROP TABLE IF EXISTS public.materialbaselinedetail;
CREATE TABLE public.materialbaselinedetail (
	id SERIAL PRIMARY KEY,
	idmaterialbaseline integer DEFAULT NULL,
	idsupplier integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	idunit integer DEFAULT NULL,
	price numeric DEFAULT NULL,
	quantity integer DEFAULT NULL,
	amount numeric DEFAULT NULL,
	startdate date DEFAULT NULL,
	enddate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for purchase
-- ----------------------------
DROP TABLE IF EXISTS public.purchase;
CREATE TABLE public.purchase (
	id SERIAL PRIMARY KEY,
	idreceiver integer DEFAULT NULL,
	idcontact integer DEFAULT NULL,
	idsupplier integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	idstore integer DEFAULT NULL,
	contactphonenumber varchar(50) DEFAULT NULL,
	contactfaxnumber varchar(50) DEFAULT NULL,
	deliveryaddress varchar(500) DEFAULT NULL,
	deliverydate date DEFAULT NULL,
	vat integer  DEFAULT NULL,
	totalamount numeric  DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for purchasedetail
-- ----------------------------
DROP TABLE IF EXISTS public.purchasedetail;
CREATE TABLE public.purchasedetail (
	id SERIAL PRIMARY KEY,
	idpurchase integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	idunit integer DEFAULT NULL,
	materialcode varchar(50) DEFAULT NULL,
	price numeric DEFAULT NULL,
	quantity integer DEFAULT NULL,
	amount numeric DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for request
-- ----------------------------
DROP TABLE IF EXISTS public.request;
CREATE TABLE public.request (
	id SERIAL PRIMARY KEY,
	idstore integer DEFAULT NULL,
	idwriter integer DEFAULT NULL,
	idreceiver integer DEFAULT NULL,
	idresponsible integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,	
	scope varchar(100) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	receiverphonenumber varchar(50) DEFAULT NULL,
	writerphonenumber varchar(50) DEFAULT NULL,
	address varchar(500) DEFAULT NULL,
	times integer DEFAULT NULL,
	requestdate date DEFAULT NULL,
	receivedate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for requestdetail
-- ----------------------------
DROP TABLE IF EXISTS public.requestdetail;
CREATE TABLE public.requestdetail (
	id SERIAL PRIMARY KEY,
	idrequest integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	softquantity integer DEFAULT NULL,
	quantity integer DEFAULT NULL,
	workitem varchar(300) DEFAULT NULL,
	deliverydate date DEFAULT NULL,
	drawingname varchar(300) DEFAULT NULL,
	teamname varchar(300) DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialimport
-- ----------------------------
DROP TABLE IF EXISTS public.materialimport;
CREATE TABLE public.materialimport (
	id SERIAL PRIMARY KEY,
	idstore integer DEFAULT NULL,
	idimporter integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	importdate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialimportdetail
-- ----------------------------
DROP TABLE IF EXISTS public.materialimportdetail;
CREATE TABLE public.materialimportdetail (
	id SERIAL PRIMARY KEY,
	idmaterialimport integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	idunit integer DEFAULT NULL,
	price numeric DEFAULT NULL,
	quantity integer DEFAULT NULL,
	amount numeric DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialexport
-- ----------------------------
DROP TABLE IF EXISTS public.materialexport;
CREATE TABLE public.materialexport (
	id SERIAL PRIMARY KEY,
	idstore integer DEFAULT NULL,
	idexporter integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	exportdate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialexportdetail
-- ----------------------------
DROP TABLE IF EXISTS public.materialexportdetail;
CREATE TABLE public.materialexportdetail (
	id SERIAL PRIMARY KEY,
	idmaterialexport integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	idunit integer DEFAULT NULL,
	price numeric DEFAULT NULL,
	quantity integer DEFAULT NULL,
	amount numeric DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialform
-- ----------------------------
DROP TABLE IF EXISTS public.materialform;
CREATE TABLE public.materialform (
	id SERIAL PRIMARY KEY,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	scope varchar(100) DEFAULT NULL,
	totalamount numeric DEFAULT NULL,
	formdate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialformdetail
-- ----------------------------
DROP TABLE IF EXISTS public.materialformdetail;
CREATE TABLE public.materialformdetail (
	id SERIAL PRIMARY KEY,
	idmaterialform integer DEFAULT NULL,
	idmaterial integer DEFAULT NULL,
	idunit integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	price numeric DEFAULT NULL,
	quantity integer DEFAULT NULL,
	amount numeric DEFAULT NULL,
	startdate date DEFAULT NULL,
	enddate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);


-- ----------------------------
-- Table structure for materialquantity:
-- ----------------------------
DROP TABLE IF EXISTS public.materialquantity;
CREATE TABLE public.materialquantity (
	id SERIAL PRIMARY KEY,
	idref integer DEFAULT NULL,
	reftype varchar(100) DEFAULT NULL,
	scope varchar(100) DEFAULT NULL,
	quantity numeric DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for materialamount: reftype as materialimportdetail; scope as materialprice = baseprice + accessoryprice + ncprice, unitprice = materialprice * adminfeepercent + materialprice.
-- ----------------------------
DROP TABLE IF EXISTS public.materialamount;
CREATE TABLE public.materialamount (
	id SERIAL PRIMARY KEY,
	idref integer DEFAULT NULL,
	reftype varchar(100) DEFAULT NULL,
	scope varchar(100) DEFAULT NULL,
	amount numeric DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for attachment
-- ----------------------------
DROP TABLE IF EXISTS public.attachment;
CREATE TABLE public.attachment (
	id SERIAL PRIMARY KEY,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	filename varchar(300) DEFAULT NULL,
	filesize integer DEFAULT NULL,
	mimetype varchar(150) DEFAULT NULL,
	description varchar(5000) DEFAULT NULL,
	filepath varchar(300) DEFAULT NULL,
	filetype varchar(150) DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS public.comment;
CREATE TABLE public.comment (
	id SERIAL PRIMARY KEY,
	idwriter integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	content text DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);


-- ----------------------------
-- Table structure for notify: status as 51=send, 52=view, 53=cannotsend, 55=cancel; method as push message (hàng chục), email (hàng trăm). Ex for method: 10 gởi message, 100 gởi email, 110 gởi message và  email, 20 nhận message, 200 nhận email, 220 nhận message và email.
-- ----------------------------
DROP TABLE IF EXISTS public.notify;
CREATE TABLE public.notify (
	id SERIAL PRIMARY KEY,
	idsender integer DEFAULT NULL,
	idreceiver integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	content text DEFAULT NULL,
	method integer DEFAULT NULL,
	priority integer DEFAULT NULL,
	isactive boolean DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);
-- ----------------------------
-- Table structure for history
-- ----------------------------
DROP TABLE IF EXISTS public.history;
CREATE TABLE public.history (
	id BIGSERIAL PRIMARY KEY,
	idref integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	content text DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for assignment
-- ----------------------------
DROP TABLE IF EXISTS public.assignment;
CREATE TABLE public.assignment (
	id SERIAL PRIMARY KEY,
	idworkflowexecute integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	idassignee integer DEFAULT NULL,
	reftype varchar(100) DEFAULT NULL,
	sortorder integer DEFAULT NULL,
	startdate date DEFAULT NULL,
	enddate date DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for sign
-- ----------------------------
DROP TABLE IF EXISTS public.sign;
CREATE TABLE public.sign (
	id SERIAL PRIMARY KEY,
	idsigner integer DEFAULT NULL,
	idassignment integer DEFAULT NULL,
	signdate date DEFAULT NULL,
	signature text DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for workflow: scope as quotation, purchase, request, materialimport, materialexport. Multi workflow with the same scope.
-- ----------------------------
DROP TABLE IF EXISTS public.workflow;
CREATE TABLE public.workflow (
	id SERIAL PRIMARY KEY,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	scope varchar(100) DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for workflowdefine
-- ----------------------------
DROP TABLE IF EXISTS public.workflowdefine;
CREATE TABLE public.workflowdefine (
	id SERIAL PRIMARY KEY,
	idworkflow integer DEFAULT NULL,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	step integer DEFAULT NULL,
	transmit text DEFAULT NULL,
	duration integer DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for workflowexecute: can have comment and attachment.
-- ----------------------------
DROP TABLE IF EXISTS public.workflowexecute;
CREATE TABLE public.workflowexecute (
	id SERIAL PRIMARY KEY,
	idworkflow integer DEFAULT NULL,
	idref integer DEFAULT NULL,
	idsender integer DEFAULT NULL,
	idreceiver integer DEFAULT NULL,
	reftype varchar(50) DEFAULT NULL,
	step integer DEFAULT NULL,
	deadline date DEFAULT NULL,
	iscurrent boolean DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for calendardefine
-- ----------------------------
DROP TABLE IF EXISTS public.calendardefine;
CREATE TABLE public.calendardefine (
	id SERIAL PRIMARY KEY,
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	content varchar(500) DEFAULT NULL,
	sortorder integer DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for calendar
-- ----------------------------
DROP TABLE IF EXISTS public.calendar;
CREATE TABLE public.calendar (
	id SERIAL PRIMARY KEY,
	idcalendardefine integer DEFAULT '1',
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	isoffday integer DEFAULT '0',
	calendardate date DEFAULT NULL,
	day varchar(8) DEFAULT NULL,
	week varchar(6) DEFAULT NULL,
	month varchar(6) DEFAULT NULL,
	year varchar(4) DEFAULT NULL,
	note varchar(1000) DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

-- ----------------------------
-- Table structure for appconfig.
-- ----------------------------
DROP TABLE IF EXISTS public.appconfig;
CREATE TABLE public.appconfig (
	id SERIAL PRIMARY KEY,
	scope varchar(100) DEFAULT NULL,
	content text DEFAULT NULL,
	status integer DEFAULT NULL,
	idowner integer DEFAULT NULL,
	idcreate integer DEFAULT NULL,
	idupdate integer DEFAULT NULL,
	iddelete integer DEFAULT NULL,
	idlock integer DEFAULT NULL,
	createdate timestamp with time zone DEFAULT NULL,
	updatedate timestamp with time zone DEFAULT NULL,
	deletedate timestamp with time zone DEFAULT NULL,
	lockdate timestamp with time zone DEFAULT NULL,
	version integer DEFAULT NULL
);

