
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.wh.model;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="notify", schema="public" )
public class Notify implements Serializable
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
    @Column(name="idsender")
    private Integer    idsender     ;

    @Column(name="idreceiver")
    private Integer    idreceiver   ;

    @Column(name="idref")
    private Integer    idref        ;

    @Column(name="reftype", length=50)
    private String     reftype      ;

    @Column(name="content", length=2147483647)
    private String     content      ;

    @Column(name="method")
    private Integer    method       ;

    @Column(name="priority")
    private Integer    priority     ;

    @Column(name="isactive")
    private Boolean    isactive     ;

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
    @Column(name="idlock")
    private Integer    idlock       ;

	@JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate")
    private Date       createdate   ;

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
    public Notify()
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
    //--- DATABASE MAPPING : idsender ( int4 ) 
    public void setIdsender( Integer idsender )
    {
        this.idsender = idsender;
    }
    public Integer getIdsender()
    {
        return this.idsender;
    }

    //--- DATABASE MAPPING : idreceiver ( int4 ) 
    public void setIdreceiver( Integer idreceiver )
    {
        this.idreceiver = idreceiver;
    }
    public Integer getIdreceiver()
    {
        return this.idreceiver;
    }

    //--- DATABASE MAPPING : idref ( int4 ) 
    public void setIdref( Integer idref )
    {
        this.idref = idref;
    }
    public Integer getIdref()
    {
        return this.idref;
    }

    //--- DATABASE MAPPING : reftype ( varchar ) 
    public void setReftype( String reftype )
    {
        this.reftype = reftype;
    }
    public String getReftype()
    {
        return this.reftype;
    }

    //--- DATABASE MAPPING : content ( text ) 
    public void setContent( String content )
    {
        this.content = content;
    }
    public String getContent()
    {
        return this.content;
    }

    //--- DATABASE MAPPING : method ( int4 ) 
    public void setMethod( Integer method )
    {
        this.method = method;
    }
    public Integer getMethod()
    {
        return this.method;
    }

    //--- DATABASE MAPPING : priority ( int4 ) 
    public void setPriority( Integer priority )
    {
        this.priority = priority;
    }
    public Integer getPriority()
    {
        return this.priority;
    }

    //--- DATABASE MAPPING : isactive ( bool ) 
    public void setIsactive( Boolean isactive )
    {
        this.isactive = isactive;
    }
    public Boolean getIsactive()
    {
        return this.isactive;
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

    //--- DATABASE MAPPING : idlock ( int4 ) 
    public void setIdlock( Integer idlock )
    {
        this.idlock = idlock;
    }
    public Integer getIdlock()
    {
        return this.idlock;
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
    	sb.append(",\"idsender\":");
        value = idsender == null ? "null" : "\"" + idsender + "\"";
        sb.append(value);
    	sb.append(",\"idreceiver\":");
        value = idreceiver == null ? "null" : "\"" + idreceiver + "\"";
        sb.append(value);
    	sb.append(",\"idref\":");
        value = idref == null ? "null" : "\"" + idref + "\"";
        sb.append(value);
    	sb.append(",\"reftype\":");
        value = reftype == null ? "null" : "\"" + reftype + "\"";
        sb.append(value);
    	sb.append(",\"content\":");
        value = content == null ? "null" : "\"" + content + "\"";
        sb.append(value);
    	sb.append(",\"method\":");
        value = method == null ? "null" : "\"" + method + "\"";
        sb.append(value);
    	sb.append(",\"priority\":");
        value = priority == null ? "null" : "\"" + priority + "\"";
        sb.append(value);
    	sb.append(",\"isactive\":");
        value = isactive == null ? "null" : "\"" + isactive + "\"";
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
    	sb.append(",\"idlock\":");
        value = idlock == null ? "null" : "\"" + idlock + "\"";
        sb.append(value);
    	sb.append(",\"createdate\":");
        value = createdate == null ? "null" : "\"" + createdate + "\"";
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