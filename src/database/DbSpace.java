/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pepe
 */
@Entity
@Table(name = "db_space")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DbSpace.findAll", query = "SELECT d FROM DbSpace d")
    , @NamedQuery(name = "DbSpace.findById", query = "SELECT d FROM DbSpace d WHERE d.id = :id")
    , @NamedQuery(name = "DbSpace.findByName", query = "SELECT d FROM DbSpace d WHERE d.name = :name")})
public class DbSpace implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSpace")
    private Collection<DbBall> dbBallCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSpace")
    private Collection<DbObstacle> dbObstacleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSpace")
    private Collection<DbStopitem> dbStopitemCollection;

    public DbSpace() {
    }

    public DbSpace(Integer id) {
        this.id = id;
    }

    public DbSpace(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<DbBall> getDbBallCollection() {
        return dbBallCollection;
    }

    public void setDbBallCollection(Collection<DbBall> dbBallCollection) {
        this.dbBallCollection = dbBallCollection;
    }

    @XmlTransient
    public Collection<DbObstacle> getDbObstacleCollection() {
        return dbObstacleCollection;
    }

    public void setDbObstacleCollection(Collection<DbObstacle> dbObstacleCollection) {
        this.dbObstacleCollection = dbObstacleCollection;
    }

    @XmlTransient
    public Collection<DbStopitem> getDbStopitemCollection() {
        return dbStopitemCollection;
    }

    public void setDbStopitemCollection(Collection<DbStopitem> dbStopitemCollection) {
        this.dbStopitemCollection = dbStopitemCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbSpace)) {
            return false;
        }
        DbSpace other = (DbSpace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitydb.DbSpace[ id=" + id + " ]";
    }
    
}
