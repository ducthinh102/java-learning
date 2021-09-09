
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.main.model;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user", schema="public" )
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
    private Integer    id           ;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Column(name="idcalendar")
    private Integer    idcalendar   ;

    @Column(name="username", length=100)
    private String     username     ;

    @Column(name="password", length=300)
    private String     password     ;

    @Column(name="enabled")
    private Boolean    enabled      ;

    @Column(name="displayname", length=200)
    private String     displayname  ;

    @Column(name="email", length=200)
    private String     email        ;

    @Column(name="firstname", length=100)
    private String     firstname    ;

    @Column(name="lastname", length=100)
    private String     lastname     ;

    @Column(name="thumbnail", length=2147483647)
    private String     thumbnail    ;

    @Column(name="address", length=500)
    private String     address      ;

    @Column(name="mobile", length=50)
    private String     mobile       ;

    @Column(name="telephone", length=50)
    private String     telephone    ;

    @Column(name="scope", length=100)
    private String     scope        ;

    @Column(name="status")
    private Integer    status       ;

	@JsonIgnore
    @Column(name="idowner")
    private Integer    idowner      ;

	@JsonIgnore
    @Column(name="idcreate")
    private Integer    idcreate     ;

	@JsonIgnore
    @Column(name="idupdate")
    private Integer    idupdate     ;

	@JsonIgnore
    @Column(name="iddelete")
    private Integer    iddelete     ;

	@JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date       createdate   ;

	@JsonIgnore
    @Column(name="idlock")
    private Integer    idlock       ;

	@JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updatedate")
    private Date       updatedate   ;

	@JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deletedate")
    private Date       deletedate   ;

	@JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lockdate")
    private Date       lockdate     ;

    @Column(name="version")
    private Integer    version      ;


    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public User()
    {
		super();
    }
    
    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE KEY FIELD
    //----------------------------------------------------------------------
    public void setId( Integer id )
    {
        this.id = id ;
    }
    public Integer getId()
    {
        return this.id;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : idcalendar ( int4 ) 
    public void setIdcalendar( Integer idcalendar )
    {
        this.idcalendar = idcalendar;
    }
    public Integer getIdcalendar()
    {
        return this.idcalendar;
    }

    //--- DATABASE MAPPING : username ( varchar ) 
    public void setUsername( String username )
    {
        this.username = username;
    }
    public String getUsername()
    {
        return this.username;
    }

    //--- DATABASE MAPPING : password ( varchar ) 
    public void setPassword( String password )
    {
        this.password = password;
    }
    public String getPassword()
    {
        return this.password;
    }

    //--- DATABASE MAPPING : enabled ( bool ) 
    public void setEnabled( Boolean enabled )
    {
        this.enabled = enabled;
    }
    public Boolean getEnabled()
    {
        return this.enabled;
    }

    //--- DATABASE MAPPING : displayname ( varchar ) 
    public void setDisplayname( String displayname )
    {
        this.displayname = displayname;
    }
    public String getDisplayname()
    {
        return this.displayname;
    }

    //--- DATABASE MAPPING : firstname ( varchar ) 
    public void setFirstname( String firstname )
    {
        this.firstname = firstname;
    }
    public String getFirstname()
    {
        return this.firstname;
    }

    //--- DATABASE MAPPING : lastname ( varchar ) 
    public void setLastname( String lastname )
    {
        this.lastname = lastname;
    }
    public String getLastname()
    {
        return this.lastname;
    }

    //--- DATABASE MAPPING : email ( varchar ) 
    public void setEmail( String email )
    {
        this.email = email;
    }
    public String getEmail()
    {
        return this.email;
    }

    //--- DATABASE MAPPING : thumbnail ( text ) 
    public void setThumbnail( String thumbnail )
    {
        this.thumbnail = thumbnail;
    }
    public String getThumbnail()
    {
        return this.thumbnail;
    }

    //--- DATABASE MAPPING : address ( varchar ) 
    public void setAddress( String address )
    {
        this.address = address;
    }
    public String getAddress()
    {
        return this.address;
    }

    //--- DATABASE MAPPING : mobile ( varchar ) 
    public void setMobile( String mobile )
    {
        this.mobile = mobile;
    }
    public String getMobile()
    {
        return this.mobile;
    }

    //--- DATABASE MAPPING : telephone ( varchar ) 
    public void setTelephone( String telephone )
    {
        this.telephone = telephone;
    }
    public String getTelephone()
    {
        return this.telephone;
    }

    //--- DATABASE MAPPING : scope ( varchar ) 
    public void setScope( String scope )
    {
        this.scope = scope;
    }
    public String getScope()
    {
        return this.scope;
    }

    //--- DATABASE MAPPING : status ( int4 ) 
    public void setStatus( Integer status )
    {
        this.status = status;
    }
    public Integer getStatus()
    {
        return this.status;
    }

    //--- DATABASE MAPPING : idowner ( int4 ) 
    public void setIdowner( Integer idowner )
    {
        this.idowner = idowner;
    }
    public Integer getIdowner()
    {
        return this.idowner;
    }

    //--- DATABASE MAPPING : idcreate ( int4 ) 
    public void setIdcreate( Integer idcreate )
    {
        this.idcreate = idcreate;
    }
    public Integer getIdcreate()
    {
        return this.idcreate;
    }

    //--- DATABASE MAPPING : idupdate ( int4 ) 
    public void setIdupdate( Integer idupdate )
    {
        this.idupdate = idupdate;
    }
    public Integer getIdupdate()
    {
        return this.idupdate;
    }

    //--- DATABASE MAPPING : iddelete ( int4 ) 
    public void setIddelete( Integer iddelete )
    {
        this.iddelete = iddelete;
    }
    public Integer getIddelete()
    {
        return this.iddelete;
    }

    //--- DATABASE MAPPING : createdate ( timestamptz ) 
    public void setCreatedate( Date createdate )
    {
        this.createdate = createdate;
    }
    public Date getCreatedate()
    {
        return this.createdate;
    }

    //--- DATABASE MAPPING : idlock ( int4 ) 
    public void setIdlock( Integer idlock )
    {
        this.idlock = idlock;
    }
    public Integer getIdlock()
    {
        return this.idlock;
    }

    //--- DATABASE MAPPING : updatedate ( timestamptz ) 
    public void setUpdatedate( Date updatedate )
    {
        this.updatedate = updatedate;
    }
    public Date getUpdatedate()
    {
        return this.updatedate;
    }

    //--- DATABASE MAPPING : deletedate ( timestamptz ) 
    public void setDeletedate( Date deletedate )
    {
        this.deletedate = deletedate;
    }
    public Date getDeletedate()
    {
        return this.deletedate;
    }

    //--- DATABASE MAPPING : lockdate ( timestamptz ) 
    public void setLockdate( Date lockdate )
    {
        this.lockdate = lockdate;
    }
    public Date getLockdate()
    {
        return this.lockdate;
    }

    //--- DATABASE MAPPING : version ( int4 ) 
    public void setVersion( Integer version )
    {
        this.version = version;
    }
    public Integer getVersion()
    {
        return this.version;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR LINKS
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
	public String toString() {
    	String value = "";
        StringBuffer sb = new StringBuffer(); 
        sb.append("{");
        sb.append("\"id\":");
        sb.append(id);
    	sb.append(",\"idcalendar\":");
        value = idcalendar == null ? "null" : "\"" + idcalendar + "\"";
        sb.append(value);
    	sb.append(",\"username\":");
        value = username == null ? "null" : "\"" + username + "\"";
        sb.append(value);
    	sb.append(",\"password\":");
        value = password == null ? "null" : "\"" + password + "\"";
        sb.append(value);
    	sb.append(",\"enabled\":");
        value = enabled == null ? "null" : "\"" + enabled + "\"";
        sb.append(value);
    	sb.append(",\"displayname\":");
        value = displayname == null ? "null" : "\"" + displayname + "\"";
        sb.append(value);
    	sb.append(",\"firstname\":");
        value = firstname == null ? "null" : "\"" + firstname + "\"";
        sb.append(value);
    	sb.append(",\"lastname\":");
        value = lastname == null ? "null" : "\"" + lastname + "\"";
        sb.append(value);
    	sb.append(",\"email\":");
        value = email == null ? "null" : "\"" + email + "\"";
        sb.append(value);
    	sb.append(",\"thumbnail\":");
        value = thumbnail == null ? "null" : "\"" + thumbnail + "\"";
        sb.append(value);
    	sb.append(",\"address\":");
        value = address == null ? "null" : "\"" + address + "\"";
        sb.append(value);
    	sb.append(",\"mobile\":");
        value = mobile == null ? "null" : "\"" + mobile + "\"";
        sb.append(value);
    	sb.append(",\"telephone\":");
        value = telephone == null ? "null" : "\"" + telephone + "\"";
        sb.append(value);
    	sb.append(",\"scope\":");
        value = scope == null ? "null" : "\"" + scope + "\"";
        sb.append(value);
    	sb.append(",\"status\":");
        value = status == null ? "null" : "\"" + status + "\"";
        sb.append(value);
    	sb.append(",\"idowner\":");
        value = idowner == null ? "null" : "\"" + idowner + "\"";
        sb.append(value);
    	sb.append(",\"idcreate\":");
        value = idcreate == null ? "null" : "\"" + idcreate + "\"";
        sb.append(value);
    	sb.append(",\"idupdate\":");
        value = idupdate == null ? "null" : "\"" + idupdate + "\"";
        sb.append(value);
    	sb.append(",\"iddelete\":");
        value = iddelete == null ? "null" : "\"" + iddelete + "\"";
        sb.append(value);
    	sb.append(",\"createdate\":");
        value = createdate == null ? "null" : "\"" + createdate + "\"";
        sb.append(value);
    	sb.append(",\"idlock\":");
        value = idlock == null ? "null" : "\"" + idlock + "\"";
        sb.append(value);
    	sb.append(",\"updatedate\":");
        value = updatedate == null ? "null" : "\"" + updatedate + "\"";
        sb.append(value);
    	sb.append(",\"deletedate\":");
        value = deletedate == null ? "null" : "\"" + deletedate + "\"";
        sb.append(value);
    	sb.append(",\"lockdate\":");
        value = lockdate == null ? "null" : "\"" + lockdate + "\"";
        sb.append(value);
    	sb.append(",\"version\":");
        value = version == null ? "null" : "\"" + version + "\"";
        sb.append(value);
        sb.append("}");
        return sb.toString(); 
    }


    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------

}