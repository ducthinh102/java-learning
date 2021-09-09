
-- ----------------------------
-- Note
-- status: 1=new, 2=update, 3=delete, 4=notuse.
-- workflow status: 10=executeworkflow, 11=send (to multi receivers), 12=receive, 13=getback, 14=sendback, 15=cancel, 16=approve.
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
-- Table structure for calendar.
-- content as "{dayofweek: { 1: {iswork: false}, 2: {iswork: true}, 3: {iswork: true}, 4: {iswork: true}, 5: {iswork: true}, 6: {iswork: true}, 7: {iswork: false} } }".
-- ----------------------------
DROP TABLE IF EXISTS public.calendar;
CREATE TABLE public.calendar (
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
-- Table structure for calendarextend
-- ----------------------------
DROP TABLE IF EXISTS public.calendarextend;
CREATE TABLE public.calendarextend (
	id SERIAL PRIMARY KEY,
	idcalendar integer DEFAULT '1',
	code varchar(50) DEFAULT NULL,
	name varchar(300) DEFAULT NULL,
	iswork boolean DEFAULT FALSE,
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
