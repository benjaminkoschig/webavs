package ch.globaz.common.domaine.Echeance;

import java.util.Date;
import ch.globaz.pyxis.domaine.Tiers;

public class Echeance {

    private String id;
    private String idExterne;
    private Tiers tiers;
    private EcheanceDomaine domaine;
    private EcheanceEtat etat;
    private EcheanceType type;
    private Date dateTraitement;
    private Date dateEcheance;
    private String libelle;
    private String description;
    private String spy;

    public String getSpy() {
        return spy;
    }

    public void setSpy(String spy) {
        this.spy = spy;
    }

    public Echeance(String id, String idExterne, Tiers tiers, Date dateEcheance, EcheanceDomaine domaine,
            EcheanceEtat etat, EcheanceType type, String libelle, String description, Date dateTraitement) {
        super();
        this.id = id;
        this.idExterne = idExterne;
        this.tiers = tiers;
        this.domaine = domaine;
        this.etat = etat;
        this.type = type;
        this.dateTraitement = dateTraitement;
        this.dateEcheance = dateEcheance;
        this.libelle = libelle;
        this.description = description;
    }

    public Echeance(String id, String idExterne, Tiers tiers, EcheanceDomaine domaine, EcheanceType type,
            Date dateEcheance, String libelle, String remarque) {
        this(id, idExterne, tiers, dateEcheance, domaine, EcheanceEtat.PLANIFIEE, type, libelle, remarque, null);
    }

    public String getId() {
        return id;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public EcheanceDomaine getDomaine() {
        return domaine;
    }

    public EcheanceEtat getEtat() {
        return etat;
    }

    public EcheanceType getType() {
        return type;
    }

    public Date getDateTraitement() {
        return dateTraitement;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setEtat(EcheanceEtat etat) {
        this.etat = etat;
    }

    public void setDateTraitement(Date dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    @Override
    public String toString() {
        return "Echeance [id=" + id + ", idExterne=" + idExterne + ", tiers=" + tiers + ", domaine=" + domaine
                + ", etat=" + etat + ", type=" + type + ", dateTraitement=" + dateTraitement + ", dateEcheance="
                + dateEcheance + ", libelle=" + libelle + ", description=" + description + "]";
    }

}
