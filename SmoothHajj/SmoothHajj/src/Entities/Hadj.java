/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Amine
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Hadj.FindById", query="select e from Hadj e WHERE e.id = :id"),
    @NamedQuery(name="Hadj.FindByName", query="select e from Hadj e WHERE e.name= :name"),
    @NamedQuery(name="Hadj.getLastId", query="select COUNT(e.name) from Hadj e"),
    @NamedQuery(name="Hadj.FindAll", query="select m from Hadj m"),
})
public class Hadj implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String name;
    private String photo_path;
    private String fingerprint;
    private String pincode;

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

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        return "Hadj{" + "id=" + id + ", name=" + name + ", photo_path=" + photo_path + ", fingerprint=" + fingerprint + ", pincode=" + pincode + '}';
    }
    
}
