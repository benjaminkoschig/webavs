package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "posteTravail")
public class PosteTravailEbu {

    private String idTravailleur;
    private String idPosteTravail;
    private String correlationId;
    private String posteCorrelationId;

    // Travailleur
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String nss;
    private String sexe;
    private PermisTravailEbu permisTravail;

    // Poste de travail
    private EmployeurEbu employeur;
    private String dateDebutActivite;
    private String dateFinActivite;
    private TypeSalaire genreSalaire;
    private Qualification qualification;

    public PosteTravailEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public PosteTravailEbu(PosteTravail posteTravail) {
        idPosteTravail = posteTravail.getId();
        nom = posteTravail.getTravailleur().getDesignation1();
        prenom = posteTravail.getTravailleur().getDesignation2();
        dateNaissance = posteTravail.getDateNaissanceTravailleur();
        nss = posteTravail.getTravailleurNss();
        sexe = posteTravail.getTravailleur().getSexe();
        // permisTravail = new PermisTravailEbu(posteTravail.getTravailleur().getPermisTravail().getValue(),
        // posteTravail.getTravailleur().getReferencePermis());
        idTravailleur = posteTravail.getTravailleur().getId();

        dateDebutActivite = posteTravail.getDebutActiviteAsSwissValue();
        dateFinActivite = posteTravail.getFinActiviteAsSwissValue();
        genreSalaire = posteTravail.getTypeSalaire();
        qualification = posteTravail.getQualification();
        correlationId = posteTravail.getTravailleur().getCorrelationId();
        posteCorrelationId = posteTravail.getPosteCorrelationId();
        employeur = new EmployeurEbu(posteTravail.getEmployeur());
    }

    /**
     * @return the idPosteTravail
     */
    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    /**
     * @return the correlationId
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId the correlationId to set
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getDescriptionPoste() {
        return nss + " " + nom + " " + prenom + " (" + dateNaissance + ")";
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

}
