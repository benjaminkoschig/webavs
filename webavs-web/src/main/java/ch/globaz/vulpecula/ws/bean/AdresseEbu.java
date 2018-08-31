package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adresse")
public class AdresseEbu {
    private String titre;
    private String aLAttention;

    private String description1;
    private String description2;
    // private String description3;
    // private String description4;

    private String casePostale;
    private String rue;
    private String rueNumero;
    private String npa;
    private String localite;
    private String idLocalite;

    private String pays;

    // private String canton;

    public AdresseEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getCasePostale() {
        return casePostale;
    }

    public void setCasePostale(String casePostale) {
        this.casePostale = casePostale;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getRueNumero() {
        return rueNumero;
    }

    public void setRueNumero(String rueNumero) {
        this.rueNumero = rueNumero;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getAdresseFormatte() {
        StringBuilder adressAsString = new StringBuilder();
        adressAsString.append(description1);
        adressAsString.append(" ");
        adressAsString.append(description2);

        adressAsString.append("\n");
        adressAsString.append(rue);
        adressAsString.append(" ");
        adressAsString.append(rueNumero);

        adressAsString.append("\n");
        adressAsString.append(npa);
        adressAsString.append(" ");
        adressAsString.append(localite);

        return adressAsString.toString();
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    public String getALAttention() {
        return aLAttention;
    }

    public void setALAttention(String attention) {
        aLAttention = attention;
    }

}
