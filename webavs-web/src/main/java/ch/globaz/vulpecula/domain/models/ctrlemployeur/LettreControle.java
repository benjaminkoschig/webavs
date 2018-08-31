package ch.globaz.vulpecula.domain.models.ctrlemployeur;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

/***
 * objet POJO qui contient les élément d'une lettre de contrôle employeur
 * 
 * @author CEL
 */
public class LettreControle implements Serializable {
    private static final long serialVersionUID = -2034970070663249733L;

    private String email;
    private Date date;
    private Date dateReference;
    private String reviseur;
    private String heure;
    private int anneeDebut;
    private int anneeFin;
    private String idEmployeur;
    private Employeur employeur;

    private boolean isAVS;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public int getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(int anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public int getAnneeFin() {
        return anneeFin;
    }

    public void setAnneeFin(int anneeFin) {
        this.anneeFin = anneeFin;
    }

    public String getReviseur() {
        return reviseur;
    }

    public void setReviseur(String reviseur) {
        this.reviseur = reviseur;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public boolean isAVS() {
        return isAVS;
    }

    public void setAVS(boolean isAVS) {
        this.isAVS = isAVS;
    }

    public void setDateReference(Date date2) {
        dateReference = date2;
    }

    public Date getDateReference() {
        return dateReference;
    }

}
