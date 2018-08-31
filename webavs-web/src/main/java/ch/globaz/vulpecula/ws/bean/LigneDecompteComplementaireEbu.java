package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ligneDecompteComplementaire")
public class LigneDecompteComplementaireEbu {
    @XmlElement(nillable = false, required = true)
    private StatusLine status = StatusLine.UNCHANGED;

    private String idDecompteLine;
    private String lineCorrelationId;
    private PosteTravailEbu posteTravail;
    private String periodeDebut;
    private String periodeFin;

    private String vacances;
    private String gratification;
    private String absencesJustifiees;
    private String totalSalaire;
    private String tauxContribution;
    private String apgComplementaireSM;
    private String taux;
    private boolean cotisationAVSActive;
    private String remarque = "";

    public LigneDecompteComplementaireEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public LigneDecompteComplementaireEbu(DecompteSalaire ligne) {
        if (ligne.getStatus() != null) {
            update(ligne, ligne.getStatus());
        } else {
            update(ligne, StatusLine.UNCHANGED);
        }

    }

    @Override
    public LigneDecompteComplementaireEbu clone() {
        LigneDecompteComplementaireEbu newLine = new LigneDecompteComplementaireEbu();
        newLine.setIdDecompteLine(idDecompteLine);
        newLine.setLineCorrelationId(lineCorrelationId);
        newLine.setPosteTravail(posteTravail);
        newLine.setPeriodeDebut(periodeDebut);
        newLine.setPeriodeFin(periodeFin);
        newLine.setVacances(vacances);
        newLine.setGratification(gratification);
        newLine.setAbsencesJustifiees(absencesJustifiees);
        newLine.setTotalSalaire(totalSalaire);
        newLine.setTauxContribution(tauxContribution);
        newLine.setApgComplementaireSM(apgComplementaireSM);
        newLine.setTaux(taux);
        newLine.setCotisationAVSActive(cotisationAVSActive);
        newLine.setRemarque(remarque);
        return newLine;
    }

    public DecompteSalaire getDecompteSalaire() {
        DecompteSalaire decompteSalaire = new DecompteSalaire();
        decompteSalaire.setSalaireTotal(new Montant(totalSalaire));
        decompteSalaire.setPeriode(new Periode(periodeDebut, periodeFin));
        return decompteSalaire;
    }

    public void update(DecompteSalaire ligne, StatusLine status) {
        idDecompteLine = ligne.getId();
        lineCorrelationId = ligne.getLineCorrelationId();
        totalSalaire = ligne.getSalaireTotalAsValue();
        tauxContribution = ligne.getTauxContribuableForCaissesSocialesAsValue(true);
        this.status = status;
        posteTravail = new PosteTravailEbu(ligne.getPosteTravail());
        periodeDebut = ligne.getPeriodeDebutAsSwissValue();
        periodeFin = ligne.getPeriodeFinAsSwissValue();
        vacances = ligne.getVacancesFeriesAsValue();
        gratification = ligne.getGratificationsAsValue();
        absencesJustifiees = ligne.getAbsencesJustifieesAsValue();
        apgComplementaireSM = ligne.getApgComplementaireSMAsValue();
        ligne.getPosteTravail().setAdhesionsCotisations(
                VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository().findByIdPosteTravailAndPeriode(
                        ligne.getPosteTravail().getId(),
                        new Periode(ligne.getDecompte().getPeriodeDebut(), ligne.getDecompte().getPeriodeFin())));
        cotisationAVSActive = ligne.getPosteTravail().cotiseAVS(new Date());
        remarque = ligne.getRemarque();
    }

    /**
     * @return the status
     */
    public StatusLine getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusLine status) {
        this.status = status;
    }

    /**
     * @return the idDecompteLine
     */
    public String getIdDecompteLine() {
        return idDecompteLine;
    }

    /**
     * @param idDecompteLine the idDecompteLine to set
     */
    public void setIdDecompteLine(String idDecompteLine) {
        this.idDecompteLine = idDecompteLine;
    }

    /**
     * @return the posteTravail
     */
    public PosteTravailEbu getPosteTravail() {
        return posteTravail;
    }

    /**
     * @param posteTravail the posteTravail to set
     */
    public void setPosteTravail(PosteTravailEbu posteTravail) {
        this.posteTravail = posteTravail;
    }

    /**
     * @return the periodeDebut
     */
    public String getPeriodeDebut() {
        return periodeDebut;
    }

    /**
     * @param periodeDebut the periodeDebut to set
     */
    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    /**
     * @return the periodeFin
     */
    public String getPeriodeFin() {
        return periodeFin;
    }

    /**
     * @param periodeFin the periodeFin to set
     */
    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    /**
     * @return the vacances
     */
    public String getVacances() {
        return vacances;
    }

    /**
     * @param vacances the vacances to set
     */
    public void setVacances(String vacances) {
        this.vacances = vacances;
    }

    /**
     * @return the gratification
     */
    public String getGratification() {
        return gratification;
    }

    /**
     * @param gratification the gratification to set
     */
    public void setGratification(String gratification) {
        this.gratification = gratification;
    }

    /**
     * @return the absencesJustifiees
     */
    public String getAbsencesJustifiees() {
        return absencesJustifiees;
    }

    /**
     * @param absencesJustifiees the absencesJustifiees to set
     */
    public void setAbsencesJustifiees(String absencesJustifiees) {
        this.absencesJustifiees = absencesJustifiees;
    }

    /**
     * @return the totalSalaire
     */
    public String getTotalSalaire() {
        return totalSalaire;
    }

    /**
     * @param totalSalaire the totalSalaire to set
     */
    public void setTotalSalaire(String totalSalaire) {
        this.totalSalaire = totalSalaire;
    }

    /**
     * @return the tauxContribution
     */
    public String getTauxContribution() {
        return tauxContribution;
    }

    /**
     * @param tauxContribution the tauxContribution to set
     */
    public void setTauxContribution(String tauxContribution) {
        this.tauxContribution = tauxContribution;
    }

    /**
     * @return the apgComplementaireSM
     */
    public String getApgComplementaireSM() {
        return apgComplementaireSM;
    }

    /**
     * @param apgComplementaireSM the apgComplementaireSM to set
     */
    public void setApgComplementaireSM(String apgComplementaireSM) {
        this.apgComplementaireSM = apgComplementaireSM;
    }

    /**
     * @return the taux
     */
    public String getTaux() {
        return taux;
    }

    /**
     * @param taux the taux to set
     */
    public void setTaux(String taux) {
        this.taux = taux;
    }

    public boolean isCotisationAVSActive() {
        return cotisationAVSActive;
    }

    public void setCotisationAVSActive(boolean cotisationAVSActive) {
        this.cotisationAVSActive = cotisationAVSActive;
    }

    public String getLineCorrelationId() {
        return lineCorrelationId;
    }

    public void setLineCorrelationId(String lineCorrelationId) {
        this.lineCorrelationId = lineCorrelationId;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
}
