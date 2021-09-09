
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.wh.model;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="requestdetail", schema="public" )
public class Requestdetail implements Serializable
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
    @Column(name="idrequest")
    private Integer    idrequest    ;

    @Column(name="idmaterial")
    private Integer    idmaterial   ;

    @Column(name="softquantity")
    private Integer    softquantity ;

    @Column(name="quantity")
    private Integer    quantity     ;

    @Column(name="workitem", length=300)
    private String     workitem     ;

    @Temporal(TemporalType.DATE)
    @Column(name="deliverydate")
    private Date       deliverydate ;

    @Column(name="drawingname", length=300)
    private String     drawingname  ;

    @Column(name="teamname", length=300)
    private String     teamname     ;

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
    public Requestdetail()
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
    //--- DATABASE MAPPING : idrequest ( int4 ) 
    public void setIdrequest( Integer idrequest )
    {
        this.idrequest = idrequest;
    }
    public Integer getIdrequest()
    {
        return this.idrequest;
    }

    //--- DATABASE MAPPING : idmaterial ( int4 ) 
    public void setIdmaterial( Integer idmaterial )
    {
        this.idmaterial = idmaterial;
    }
    public Integer getIdmaterial()
    {
        return this.idmaterial;
    }

    //--- DATABASE MAPPING : softquantity ( int4 ) 
    public void setSoftquantity( Integer softquantity )
    {
        this.softquantity = softquantity;
    }
    public Integer getSoftquantity()
    {
        return this.softquantity;
    }

    //--- DATABASE MAPPING : quantity ( int4 ) 
    public void setQuantity( Integer quantity )
    {
        this.quantity = quantity;
    }
    public Integer getQuantity()
    {
        return this.quantity;
    }

    //--- DATABASE MAPPING : workitem ( varchar ) 
    public void setWorkitem( String workitem )
    {
        this.workitem = workitem;
    }
    public String getWorkitem()
    {
        return this.workitem;
    }

    //--- DATABASE MAPPING : deliverydate ( date ) 
    public void setDeliverydate( Date deliverydate )
    {
        this.deliverydate = deliverydate;
    }
    public Date getDeliverydate()
    {
        return this.deliverydate;
    }

    //--- DATABASE MAPPING : drawingname ( varchar ) 
    public void setDrawingname( String drawingname )
    {
        this.drawingname = drawingname;
    }
    public String getDrawingname()
    {
        return this.drawingname;
    }

    //--- DATABASE MAPPING : teamname ( varchar ) 
    public void setTeamname( String teamname )
    {
        this.teamname = teamname;
    }
    public String getTeamname()
    {
        return this.teamname;
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
    	sb.append(",\"idrequest\":");
        value = idrequest == null ? "null" : "\"" + idrequest + "\"";
        sb.append(value);
    	sb.append(",\"idmaterial\":");
        value = idmaterial == null ? "null" : "\"" + idmaterial + "\"";
        sb.append(value);
    	sb.append(",\"softquantity\":");
        value = softquantity == null ? "null" : "\"" + softquantity + "\"";
        sb.append(value);
    	sb.append(",\"quantity\":");
        value = quantity == null ? "null" : "\"" + quantity + "\"";
        sb.append(value);
    	sb.append(",\"workitem\":");
        value = workitem == null ? "null" : "\"" + workitem + "\"";
        sb.append(value);
    	sb.append(",\"deliverydate\":");
        value = deliverydate == null ? "null" : "\"" + deliverydate + "\"";
        sb.append(value);
    	sb.append(",\"drawingname\":");
        value = drawingname == null ? "null" : "\"" + drawingname + "\"";
        sb.append(value);
    	sb.append(",\"teamname\":");
        value = teamname == null ? "null" : "\"" + teamname + "\"";
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
	
	
	// request.
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idrequest", nullable=true, insertable=false, updatable=false)
	@JsonIgnore
	private Request request;
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}

	// material.
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idmaterial", nullable=true, insertable=false, updatable=false)
	@JsonIgnore
	private Material material;
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}

}