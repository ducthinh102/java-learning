
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.wh.model;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="material", schema="public" )
public class Material implements Serializable
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
    @Column(name="idcategory")
    private Integer    idcategory   ;

    @Column(name="idsystem")
    private Integer    idsystem     ;

    @Column(name="idtype")
    private Integer    idtype       ;

    @Column(name="idbrand")
    private Integer    idbrand      ;

    @Column(name="idspec")
    private Integer    idspec       ;

    @Column(name="idorigin")
    private Integer    idorigin     ;

    @Column(name="idunit")
    private Integer    idunit       ;

    @Column(name="idref")
    private Integer    idref        ;

    @Column(name="code", length=50)
    private String     code         ;

    @Column(name="reftype", length=50)
    private String     reftype      ;

    @Column(name="name", length=300)
    private String     name         ;

    @Column(name="materialcode", length=50)
    private String     materialcode ;

    @Column(name="description", length=5000)
    private String     description  ;

    @Column(name="quantity")
    private Integer    quantity     ;

    @Column(name="thumbnail", length=2147483647)
    private String     thumbnail    ;

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
    public Material()
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
    //--- DATABASE MAPPING : idcategory ( int4 ) 
    public void setIdcategory( Integer idcategory )
    {
        this.idcategory = idcategory;
    }
    public Integer getIdcategory()
    {
        return this.idcategory;
    }

    //--- DATABASE MAPPING : idsystem ( int4 ) 
    public void setIdsystem( Integer idsystem )
    {
        this.idsystem = idsystem;
    }
    public Integer getIdsystem()
    {
        return this.idsystem;
    }

    //--- DATABASE MAPPING : idtype ( int4 ) 
    public void setIdtype( Integer idtype )
    {
        this.idtype = idtype;
    }
    public Integer getIdtype()
    {
        return this.idtype;
    }

    //--- DATABASE MAPPING : idbrand ( int4 ) 
    public void setIdbrand( Integer idbrand )
    {
        this.idbrand = idbrand;
    }
    public Integer getIdbrand()
    {
        return this.idbrand;
    }

    //--- DATABASE MAPPING : idspec ( int4 ) 
    public void setIdspec( Integer idspec )
    {
        this.idspec = idspec;
    }
    public Integer getIdspec()
    {
        return this.idspec;
    }

    //--- DATABASE MAPPING : idorigin ( int4 ) 
    public void setIdorigin( Integer idorigin )
    {
        this.idorigin = idorigin;
    }
    public Integer getIdorigin()
    {
        return this.idorigin;
    }

    //--- DATABASE MAPPING : idunit ( int4 ) 
    public void setIdunit( Integer idunit )
    {
        this.idunit = idunit;
    }
    public Integer getIdunit()
    {
        return this.idunit;
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

    //--- DATABASE MAPPING : code ( varchar ) 
    public void setCode( String code )
    {
        this.code = code;
    }
    public String getCode()
    {
        return this.code;
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

    //--- DATABASE MAPPING : name ( varchar ) 
    public void setName( String name )
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }

    //--- DATABASE MAPPING : materialcode ( varchar ) 
    public void setMaterialcode( String materialcode )
    {
        this.materialcode = materialcode;
    }
    public String getMaterialcode()
    {
        return this.materialcode;
    }

    //--- DATABASE MAPPING : description ( varchar ) 
    public void setDescription( String description )
    {
        this.description = description;
    }
    public String getDescription()
    {
        return this.description;
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

    //--- DATABASE MAPPING : thumbnail ( text ) 
    public void setThumbnail( String thumbnail )
    {
        this.thumbnail = thumbnail;
    }
    public String getThumbnail()
    {
        return this.thumbnail;
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
    	sb.append(",\"idcategory\":");
        value = idcategory == null ? "null" : "\"" + idcategory + "\"";
        sb.append(value);
    	sb.append(",\"idsystem\":");
        value = idsystem == null ? "null" : "\"" + idsystem + "\"";
        sb.append(value);
    	sb.append(",\"idtype\":");
        value = idtype == null ? "null" : "\"" + idtype + "\"";
        sb.append(value);
    	sb.append(",\"idbrand\":");
        value = idbrand == null ? "null" : "\"" + idbrand + "\"";
        sb.append(value);
    	sb.append(",\"idspec\":");
        value = idspec == null ? "null" : "\"" + idspec + "\"";
        sb.append(value);
    	sb.append(",\"idorigin\":");
        value = idorigin == null ? "null" : "\"" + idorigin + "\"";
        sb.append(value);
    	sb.append(",\"idunit\":");
        value = idunit == null ? "null" : "\"" + idunit + "\"";
        sb.append(value);
    	sb.append(",\"idref\":");
        value = idref == null ? "null" : "\"" + idref + "\"";
        sb.append(value);
    	sb.append(",\"code\":");
        value = code == null ? "null" : "\"" + code + "\"";
        sb.append(value);
    	sb.append(",\"reftype\":");
        value = reftype == null ? "null" : "\"" + reftype + "\"";
        sb.append(value);
    	sb.append(",\"name\":");
        value = name == null ? "null" : "\"" + name + "\"";
        sb.append(value);
    	sb.append(",\"materialcode\":");
        value = materialcode == null ? "null" : "\"" + materialcode + "\"";
        sb.append(value);
    	sb.append(",\"description\":");
        value = description == null ? "null" : "\"" + description + "\"";
        sb.append(value);
    	sb.append(",\"quantity\":");
        value = quantity == null ? "null" : "\"" + quantity + "\"";
        sb.append(value);
    	sb.append(",\"thumbnail\":");
        value = thumbnail == null ? "null" : "\"" + thumbnail + "\"";
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

	// parent.
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idref", nullable=true, insertable=false, updatable=false)
	@Where(clause="reftype='material'")
	@JsonIgnore
	private Material parent;
	public Material getParent() {
		return parent;
	}
	public void setParent(Material parent) {
		this.parent = parent;
	}


	// children.
	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonIgnore
	private Set<Material> children;
	public Set<Material> getChildren() {
		return children;
	}
	public void setChildren(Set<Material> children) {
		this.children = children;
	}
	
	
	// attachments.
	@OneToMany(mappedBy="material", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonIgnore
	private Set<Attachment> attachments;
	public Set<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	// unit.
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idunit", nullable=true, insertable=false, updatable=false)
	@JsonIgnore
	private Catalog unit;
	public Catalog getUnit() {
		return unit;
	}
	public void setUnit(Catalog unit) {
		this.unit = unit;
	}
	
	// type.
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idtype", nullable=true, insertable=false, updatable=false)
	@JsonIgnore
	private Catalog type;
	public Catalog getType() {
		return type;
	}
	public void setType(Catalog type) {
		this.type = type;
	}
		
		// spec.
		@OneToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="idspec", nullable=true, insertable=false, updatable=false)
		@JsonIgnore
		private Catalog spec;
		public Catalog getSpec() {
			return spec;
		}
		public void setSpec(Catalog spec) {
			this.spec = spec;
		}
		
		// brand.
		@OneToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="idbrand", nullable=true, insertable=false, updatable=false)
		@JsonIgnore
		private Catalog brand;
		public Catalog getBrand() {
			return brand;
		}
		public void setBrand(Catalog brand) {
			this.brand = brand;
		}
		
		// origin.
		@OneToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="idorigin", nullable=true, insertable=false, updatable=false)
		@JsonIgnore
		private Catalog origin;
		public Catalog getOrigin() {
			return origin;
		}
		public void setOrigin(Catalog origin) {
			this.origin = origin;
		}
		
		//category
		@OneToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="idcategory", nullable=true, insertable=false, updatable=false)
		@JsonIgnore
		private Catalog category;
		public Catalog getCategory() {
			return category;
		}
		public void setCategory(Catalog category) {
			this.category = category;
		}
		
		//system
		@OneToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="idsystem", nullable=true, insertable=false, updatable=false)
		@JsonIgnore
		private Catalog system;
		public Catalog getSystem() {
			return system;
		}
		public void setSystem(Catalog system) {
			this.system = system;
		}

	
}