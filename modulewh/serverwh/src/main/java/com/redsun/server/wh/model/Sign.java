
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.wh.model;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="sign", schema="public" )
public class Sign implements Serializable
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
    @Column(name="idsigner")
    private Integer    idsigner     ;

    @Column(name="idassignment")
    private Integer    idassignment ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="signdate")
    private Date       signdate     ;

    @Column(name="signature", length=2147483647)
    private String     signature    ;

    @Column(name="note", length=1000)
    private String     note         ;

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
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public Sign()
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
    //--- DATABASE MAPPING : idsigner ( int4 ) 
    public void setIdsigner( Integer idsigner )
    {
        this.idsigner = idsigner;
    }
    public Integer getIdsigner()
    {
        return this.idsigner;
    }

    //--- DATABASE MAPPING : idassignment ( int4 ) 
    public void setIdassignment( Integer idassignment )
    {
        this.idassignment = idassignment;
    }
    public Integer getIdassignment()
    {
        return this.idassignment;
    }

    //--- DATABASE MAPPING : signdate ( date ) 
    public void setSigndate( Date signdate )
    {
        this.signdate = signdate;
    }
    public Date getSigndate()
    {
        return this.signdate;
    }

    //--- DATABASE MAPPING : signature ( text ) 
    public void setSignature( String signature )
    {
        this.signature = signature;
    }
    public String getSignature()
    {
        return this.signature;
    }

    //--- DATABASE MAPPING : note ( varchar ) 
    public void setNote( String note )
    {
        this.note = note;
    }
    public String getNote()
    {
        return this.note;
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
    	sb.append(",\"idsigner\":");
        value = idsigner == null ? "null" : "\"" + idsigner + "\"";
        sb.append(value);
    	sb.append(",\"idassignment\":");
        value = idassignment == null ? "null" : "\"" + idassignment + "\"";
        sb.append(value);
    	sb.append(",\"signdate\":");
        value = signdate == null ? "null" : "\"" + signdate + "\"";
        sb.append(value);
    	sb.append(",\"signature\":");
        value = signature == null ? "null" : "\"" + signature + "\"";
        sb.append(value);
    	sb.append(",\"note\":");
        value = note == null ? "null" : "\"" + note + "\"";
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
}