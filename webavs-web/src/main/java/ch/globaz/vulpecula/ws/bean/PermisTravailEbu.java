package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="permisTravail")
public class PermisTravailEbu {
    String categoriePermis;
    String numeroPermis;

    public PermisTravailEbu() {
    	// Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public PermisTravailEbu(String categoriePermis, String numeroPermis) {
        this.categoriePermis = categoriePermis;
        this.numeroPermis = numeroPermis;
    }

    public String getCategoriePermis() {
        return categoriePermis;
    }

    public void setCategoriePermis(String categoriePermis) {
        this.categoriePermis = categoriePermis;
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }
}
