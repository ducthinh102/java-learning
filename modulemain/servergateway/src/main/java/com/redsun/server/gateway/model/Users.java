
// This Bean has a basic Primary Key (not composite) 

package com.redsun.server.gateway.model;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;


import javax.persistence.*;

@Entity
@Table(name="users", schema="public" )
public class Users implements Serializable
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
    @Column(name="username", length=100)
    private String     username       ;

    @Column(name="password", length=300)
    private String     password        ;

    @Column(name="displayname", length=200)
    private String     displayname     ;

    @Column(name="email", length=200)
    private String     email     ;

    @Column(name="firstname", length=100)
    private String     firstname    ;

    @Column(name="lastname", length=100)
    private String     lastname     ;

	@Column(name="thumbnail", length=2147483647)
    private String     thumbnail    ;

    @Column(name="enabled")
    private Boolean     enabled        ;

    @Column(name="version")
    private Integer    version      ;



    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public Users()
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
    //--- DATABASE MAPPING : username ( varchar ) 
    public void setUsername( String username )
    {
        this.username = username;
    }
    public String getUsername()
    {
        return this.username;
    }

    //--- DATABASE MAPPING : password ( text ) 
    public void setPassword( String password )
    {
        this.password = password;
    }
    public String getPassword()
    {
        return this.password;
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

    //--- DATABASE MAPPING : email ( varchar ) 
	public void setEmail(String email) {
		this.email = email;
	}
    public String getEmail() {
		return email;
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

    //--- DATABASE MAPPING : thumbnail ( text ) 
    public void setThumbnail( String thumbnail )
    {
        this.thumbnail = thumbnail;
    }
    public String getThumbnail()
    {
        return this.thumbnail;
    }

    //--- DATABASE MAPPING : enabled ( bit ) 
    public void setEnabled( Boolean enabled )
    {
        this.enabled = enabled;
    }
    public Boolean getEnabled()
    {
        return this.enabled;
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
        StringBuffer sb = new StringBuffer(); 
        sb.append("{\"id\":"); 
        sb.append(id);
        sb.append(",\"username\":"); 
        sb.append(username);
        sb.append(",\"password\":");
        sb.append(password);
        sb.append(",\"displayname\":");
        sb.append(displayname);
        sb.append(",\"email\":");
        sb.append(email);
        sb.append(",\"firstname\":");
        sb.append(firstname);
        sb.append(",\"lastname\":");
        sb.append(lastname);
        sb.append(",\"enabled\":");
        sb.append(enabled);
        sb.append(",\"version\":");
        sb.append(version);
        sb.append("}"); 
        return sb.toString(); 
    } 

}