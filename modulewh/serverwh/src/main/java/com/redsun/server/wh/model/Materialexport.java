
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.wh.model;

import java.io.Serializable;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="materialexport", schema="public" )
public class Materialexport implements Serializable {
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
    @Column(name="idstore")
    private Integer    idstore      ;

    @Column(name="idexporter")
    private Integer    idexporter   ;

	@Column(name = "code", length = 50)
	private String code;

    @Column(name="name", length=300)
    private String     name         ;

    @Column(name="idref")
    private Integer    idref        ;

    @Column(name="reftype", length=50)
    private String     reftype      ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="exportdate")
    private Date       exportdate   ;

    @Column(name="note", length=1000)
    private String     note         ;

    @Column(name="status")
    private Integer    status       ;

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
	public Materialexport() {
		super();
    }
    
    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE KEY FIELD
    //----------------------------------------------------------------------
	public void setId(Integer id) {
        this.id = id ;
    }

	public Integer getId() {
        return this.id;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : idstore ( int4 ) 
	public void setIdstore(Integer idstore) {
        this.idstore = idstore;
    }

	public Integer getIdstore() {
        return this.idstore;
    }

	// ----------------------------------------------------------------------
	// GETTERS & SETTERS FOR FIELDS
	// ----------------------------------------------------------------------
	// --- DATABASE MAPPING : idexporter ( int4 )
	public Integer getIdexporter() {
		return idexporter;
    }

	public void setIdexporter(Integer idexporter) {
        this.idexporter = idexporter;
    }

	// --- DATABASE MAPPING : code ( varchar )
	public void setCode(String code) {
		this.code = code;
    }

	public String getCode() {
		return this.code;
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

    //--- DATABASE MAPPING : name ( varchar ) 
	public void setName(String name) {
        this.name = name;
    }

	public String getName() {
        return this.name;
    }

    //--- DATABASE MAPPING : exportdate ( date ) 
	public void setExportdate(Date exportdate) {
        this.exportdate = exportdate;
    }

	public Date getExportdate() {
        return this.exportdate;
    }

    //--- DATABASE MAPPING : note ( varchar ) 
	public void setNote(String note) {
        this.note = note;
    }

	public String getNote() {
        return this.note;
    }

    //--- DATABASE MAPPING : status ( int4 ) 
	public void setStatus(Integer status) {
        this.status = status;
    }

	public Integer getStatus() {
        return this.status;
    }

    //--- DATABASE MAPPING : idowner ( int4 ) 
	public void setIdowner(Integer idowner) {
        this.idowner = idowner;
    }

	public Integer getIdowner() {
        return this.idowner;
    }

    //--- DATABASE MAPPING : idcreate ( int4 ) 
	public void setIdcreate(Integer idcreate) {
        this.idcreate = idcreate;
    }

	public Integer getIdcreate() {
        return this.idcreate;
    }

    //--- DATABASE MAPPING : idupdate ( int4 ) 
	public void setIdupdate(Integer idupdate) {
        this.idupdate = idupdate;
    }

	public Integer getIdupdate() {
        return this.idupdate;
    }

    //--- DATABASE MAPPING : iddelete ( int4 ) 
	public void setIddelete(Integer iddelete) {
        this.iddelete = iddelete;
    }

	public Integer getIddelete() {
        return this.iddelete;
    }

    //--- DATABASE MAPPING : idlock ( int4 ) 
	public void setIdlock(Integer idlock) {
        this.idlock = idlock;
    }

	public Integer getIdlock() {
        return this.idlock;
    }

    //--- DATABASE MAPPING : createdate ( timestamptz ) 
	public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

	public Date getCreatedate() {
        return this.createdate;
    }

    //--- DATABASE MAPPING : updatedate ( timestamptz ) 
	public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

	public Date getUpdatedate() {
        return this.updatedate;
    }

    //--- DATABASE MAPPING : deletedate ( timestamptz ) 
	public void setDeletedate(Date deletedate) {
        this.deletedate = deletedate;
    }

	public Date getDeletedate() {
        return this.deletedate;
    }

    //--- DATABASE MAPPING : lockdate ( timestamptz ) 
	public void setLockdate(Date lockdate) {
        this.lockdate = lockdate;
    }

	public Date getLockdate() {
        return this.lockdate;
    }

    //--- DATABASE MAPPING : version ( int4 ) 
	public void setVersion(Integer version) {
        this.version = version;
    }

	public Integer getVersion() {
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
    	sb.append(",\"idstore\":");
        value = idstore == null ? "null" : "\"" + idstore + "\"";
        sb.append(value);
    	sb.append(",\"code\":");
        value = code == null ? "null" : "\"" + code + "\"";
        sb.append(value);
    	sb.append(",\"idexporter\":");
        value = idexporter == null ? "null" : "\"" + idexporter + "\"";
        sb.append(value);
    	sb.append(",\"idref\":");
        value = idref == null ? "null" : "\"" + idref + "\"";
        sb.append(value);
    	sb.append(",\"name\":");
        value = name == null ? "null" : "\"" + name + "\"";
        sb.append(value);
    	sb.append(",\"reftype\":");
        value = reftype == null ? "null" : "\"" + reftype + "\"";
        sb.append(value);
    	sb.append(",\"exportdate\":");
        value = exportdate == null ? "null" : "\"" + exportdate + "\"";
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

	// materialexportdetails.
	@OneToMany(mappedBy = "materialexport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Materialexportdetail> materialexportdetails;

	// material.
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstore", nullable = true, insertable = false, updatable = false)
	@JsonIgnore
	private Store store;

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	// material.
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idexporter", nullable = true, insertable = false, updatable = false)
	@JsonIgnore
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}