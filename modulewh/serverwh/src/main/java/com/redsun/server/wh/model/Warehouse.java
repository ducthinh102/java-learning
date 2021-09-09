
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.wh.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "warehouse", schema = "public")
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------------------
	// ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
	// ----------------------------------------------------------------------
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	// ----------------------------------------------------------------------
	// ENTITY DATA FIELDS
	// ----------------------------------------------------------------------
	@Column(name = "code", length = 100)
	private String code;

	@Column(name = "name", length = 2147483647)
	private String name;

	@Column(name = "address", length = 2147483647)
	private String address;

	@Column(name = "description", length = 2147483647)
	private String description;

	@Column(name = "projectid")
	private Integer projectid;

	// ----------------------------------------------------------------------
	// ENTITY LINKS ( RELATIONSHIP )
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// CONSTRUCTOR(S)
	// ----------------------------------------------------------------------
	public Warehouse() {
		super();
	}

	// ----------------------------------------------------------------------
	// GETTER & SETTER FOR THE KEY FIELD
	// ----------------------------------------------------------------------
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	// ----------------------------------------------------------------------
	// GETTERS & SETTERS FOR FIELDS
	// ----------------------------------------------------------------------
	// --- DATABASE MAPPING : Code ( text )
	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	// --- DATABASE MAPPING : Name ( text )
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	// --- DATABASE MAPPING : Address ( text )
	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	// --- DATABASE MAPPING : Description ( text )
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	// --- DATABASE MAPPING : Project ID ( text )
	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	public Integer getProjectid() {
		return this.projectid;
	}

	// ----------------------------------------------------------------------
	// GETTERS & SETTERS FOR LINKS
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// toString METHOD
	// ----------------------------------------------------------------------
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(id);
		sb.append("]:");
		sb.append(code);
		sb.append("|");
		sb.append(name);
		sb.append("|");
		sb.append("address");
		sb.append("|");
		sb.append("description");
		sb.append("|");
		sb.append("projectid");
		return sb.toString();

	}

}