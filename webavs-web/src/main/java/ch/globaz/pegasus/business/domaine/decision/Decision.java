package ch.globaz.pegasus.business.domaine.decision;

import ch.globaz.common.domaine.Date;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class Decision {

    private String id;
    private EtatDecision etat;
    private TypeDecision type;
    private MotifDecision motif;
    private String numero;
    private String preparationPar; // personne qui a prepare
    private String validationPar; // personne qui a valide
    private Date dateDebut;
    private Date dateDecision;
    private Date dateFin;
    private Date datePreparation;
    private Date dateValidation;
    private PersonneAVS tiersBeneficiaire;
    private PersonneAVS tiersCourrier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EtatDecision getEtat() {
        return etat;
    }

    public void setEtat(EtatDecision etat) {
        this.etat = etat;
    }

    public MotifDecision getMotif() {
        return motif;
    }

    public void setMotif(MotifDecision motif) {
        this.motif = motif;
    }

    public TypeDecision getType() {
        return type;
    }

    public void setType(TypeDecision type) {
        this.type = type;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPreparationPar() {
        return preparationPar;
    }

    public void setPreparationPar(String preparationPar) {
        this.preparationPar = preparationPar;
    }

    public String getValidationPar() {
        return validationPar;
    }

    public void setValidationPar(String validationPar) {
        this.validationPar = validationPar;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(Date dateDecision) {
        this.dateDecision = dateDecision;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDatePreparation() {
        return datePreparation;
    }

    public void setDatePreparation(Date datePreparation) {
        this.datePreparation = datePreparation;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public PersonneAVS getTiersBeneficiaire() {
        return tiersBeneficiaire;
    }

    public void setTiersBeneficiaire(PersonneAVS tiersBeneficiaire) {
        this.tiersBeneficiaire = tiersBeneficiaire;
    }

    public PersonneAVS getTiersCourrier() {
        return tiersCourrier;
    }

    public void setTiersCourrier(PersonneAVS tiersCourrier) {
        this.tiersCourrier = tiersCourrier;
    }

}
