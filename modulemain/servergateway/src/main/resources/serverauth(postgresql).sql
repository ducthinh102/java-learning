-- used in tests that use HSQL
DROP TABLE IF EXISTS oauth_client_details;
CREATE TABLE IF NOT EXISTS oauth_client_details (
	client_id VARCHAR(256) PRIMARY KEY,
	resource_ids VARCHAR(256),
	client_secret VARCHAR(256),
	scope VARCHAR(256),
	authorized_grant_types VARCHAR(256),
	web_server_redirect_uri VARCHAR(256),
	authorities VARCHAR(256),
	access_token_validity INTEGER,
	refresh_token_validity INTEGER,
	additional_information VARCHAR(4096),
	autoapprove VARCHAR(256)
);

DROP TABLE IF EXISTS oauth_client_token;
CREATE TABLE IF NOT EXISTS oauth_client_token (
	token_id VARCHAR(256),
	token BYTEA,
	authentication_id VARCHAR(256) PRIMARY KEY,
	user_name VARCHAR(256),
	client_id VARCHAR(256)
);

DROP TABLE IF EXISTS oauth_access_token;
CREATE TABLE IF NOT EXISTS oauth_access_token (
	token_id VARCHAR(256),
	token BYTEA,
	authentication_id VARCHAR(256) PRIMARY KEY,
	user_name VARCHAR(256),
	client_id VARCHAR(256),
	authentication BYTEA,
	refresh_token VARCHAR(256)
);

DROP TABLE IF EXISTS oauth_refresh_token;
CREATE TABLE IF NOT EXISTS oauth_refresh_token (
	token_id VARCHAR(256),
	token BYTEA,
	authentication BYTEA
);

DROP TABLE IF EXISTS oauth_code;
CREATE TABLE IF NOT EXISTS oauth_code (
	code VARCHAR(256), authentication BYTEA
);

DROP TABLE IF EXISTS oauth_approvals;
CREATE TABLE IF NOT EXISTS oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
);


-- customized oauth_client_details table
DROP TABLE IF EXISTS ClientDetails;
CREATE TABLE IF NOT EXISTS ClientDetails (
	appId VARCHAR(256) PRIMARY KEY,
	resourceIds VARCHAR(256),
	appSecret VARCHAR(256),
	scope VARCHAR(256),
	grantTypes VARCHAR(256),
	redirectUrl VARCHAR(256),
	authorities VARCHAR(256),
	access_token_validity INTEGER,
	refresh_token_validity INTEGER,
	additionalInformation VARCHAR(4096),
	autoApproveScopes VARCHAR(256)
);


DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	username varchar(100) DEFAULT NULL,
	password varchar(300) DEFAULT NULL,
	displayname varchar(300) DEFAULT NULL,
	email varchar(200) DEFAULT NULL,
	thumbnail text DEFAULT NULL,
	enabled boolean DEFAULT TRUE,
	firstname varchar(100) DEFAULT NULL,
	lastname varchar(100) DEFAULT NULL,
	version integer DEFAULT NULL
);

DROP TABLE IF EXISTS user_roles;
CREATE TABLE IF NOT EXISTS user_roles (
	id SERIAL PRIMARY KEY,
	username varchar(100) DEFAULT NULL,
	role varchar(300) DEFAULT NULL
);
