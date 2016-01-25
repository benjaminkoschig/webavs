package ch.globaz.corvus.process.attestationsfiscales;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RETiersPourAttestationsFiscales implements Comparable<RETiersPourAttestationsFiscales>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseCourrierFormatee;
    private String codeIsoLangue;
    private String csLangue;
    private String csSexe;
    private String dateDeces;
    private String dateNaissance;
    private String idTiers;
    private String nom;
    private String numeroAvs;
    private String prenom;
    private Map<String, RERentePourAttestationsFiscales> rentes;
    private String titreTiers;

    public RETiersPourAttestationsFiscales() {
        super();

        adresseCourrierFormatee = "";
        codeIsoLangue = "";
        csLangue = "";
        csSexe = "";
        dateDeces = "";
        dateNaissance = "";
        idTiers = "";
        nom = "";
        numeroAvs = "";
        prenom = "";
        rentes = new HashMap<String, RERentePourAttestationsFiscales>();
        titreTiers = "";
    }

    @Override
    public int compareTo(RETiersPourAttestationsFiscales unTiers) {
        int comparaisonNom = nom.compareTo(unTiers.getNom());
        if (comparaisonNom != 0) {
            return comparaisonNom;
        }
        int comparaisonPrenom = prenom.compareTo(unTiers.getPrenom());
        if (comparaisonPrenom != 0) {
            return comparaisonPrenom;
        }

        return idTiers.compareTo(unTiers.getIdTiers());
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof RETiersPourAttestationsFiscales)) {
            return false;
        }

        return getIdTiers().equals(((RETiersPourAttestationsFiscales) obj).getIdTiers());
    }

    public String getAdresseCourrierFormatee() {
        return adresseCourrierFormatee;
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getCsLangue() {
        return csLangue;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdTiers() {
        return idTiers;
    }

    Map<String, RERentePourAttestationsFiscales> getMapRentes() {
        return rentes;
    }

    public String getNom() {
        return nom;
    }

    public String getNumeroAvs() {
        return numeroAvs;
    }

    public String getPrenom() {
        return prenom;
    }

    public Collection<RERentePourAttestationsFiscales> getRentes() {
        return rentes.values();
    }

    public String getTitreTiers() {
        return titreTiers;
    }

    @Override
    public int hashCode() {
        return (this.getClass().getName() + idTiers).hashCode();
    }

    void setAdresseCourrierFormatee(String adresseCourrierFormatee) {
        this.adresseCourrierFormatee = adresseCourrierFormatee;
    }

    void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    void setCsLangue(String csLangue) {
        this.csLangue = csLangue;
    }

    void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    void setNom(String nom) {
        this.nom = nom;
    }

    void setNumeroAvs(String numeroAvs) {
        this.numeroAvs = numeroAvs;
    }

    void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    void setTitreTiers(String titreTiers) {
        this.titreTiers = titreTiers;
    }
}
