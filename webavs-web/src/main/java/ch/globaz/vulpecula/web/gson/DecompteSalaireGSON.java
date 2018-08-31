package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.ws.bean.StatusLine;

/**
 * Données serialisables et nécessaire pour l'écran decompte salaire
 * 
 * @since Web@BMS 0.01.02
 */
public class DecompteSalaireGSON implements Serializable {
    private static final long serialVersionUID = 746981505913490621L;
    private String idPosteTravail;
    private String idDecompteSalaire;
    private String idTiersTravailleur;
    private String heures;
    private String salaireHoraire;
    private String salaireTotal;
    private String masseAC2;
    private String masseFranchise;
    private String tauxContribuable;
    private String tauxAAfficher;
    private String sequence;
    private boolean first;
    private boolean last;
    private boolean isMensuel;
    private List<AbsenceGSON> absencesGSON;
    private String[] codeErreur;
    private String spy;
    private String periodeDebut;
    private String periodeFin;
    private String remarque;
    private boolean quittancer;
    private String correlationId;
    private boolean nouveauTravailleur;
    private int anneeCotisations;
    private boolean ligneSupprimee;
    private boolean majFinPoste;
    private String tauxSaisieEbu;
    private StatusLine status;
    private String vacances;
    private String gratifications;
    private String absencesJustifiees;
    private String apgComplSm;
    private boolean forcerFranchise0;
    private boolean isEnErreur;

    public boolean isMajFinPoste() {
        return majFinPoste;
    }

    public void setMajFinPoste(boolean majFinPoste) {
        this.majFinPoste = majFinPoste;
    }

    public boolean isLigneSupprimee() {
        return ligneSupprimee;
    }

    public void setLigneSupprimee(boolean ligneSupprimee) {
        this.ligneSupprimee = ligneSupprimee;
    }

    /**
     * @return the heures
     */
    public String getHeures() {
        return heures;
    }

    /**
     * @return the salaireHoraire
     */
    public String getSalaireHoraire() {
        return salaireHoraire;
    }

    /**
     * @return the salaireTotal
     */
    public String getSalaireTotal() {
        return salaireTotal;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @return the tauxContribuable
     */
    public String getTauxContribuable() {
        return tauxContribuable;
    }

    /**
     * @param heures
     *            the heures to set
     */
    public void setHeures(final String heures) {
        this.heures = heures;
    }

    /**
     * @param salaireHoraire
     *            the salaireHoraire to set
     */
    public void setSalaireHoraire(final String salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    /**
     * @param salaireTotal
     *            the salaireTotal to set
     */
    public void setSalaireTotal(final String salaireTotal) {
        this.salaireTotal = salaireTotal;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(final String sequence) {
        this.sequence = sequence;
    }

    /**
     * @param tauxContribuable
     *            the tauxContribuable to set
     */
    public void setTauxContribuable(final String tauxContribuable) {
        this.tauxContribuable = tauxContribuable;
    }

    /**
     * @return the idDecompteSalaire
     */
    public String getIdDecompteSalaire() {
        return idDecompteSalaire;
    }

    /**
     * @param idDecompteSalaire the idDecompteSalaire to set
     */
    public void setIdDecompteSalaire(final String idDecompteSalaire) {
        this.idDecompteSalaire = idDecompteSalaire;
    }

    /**
     * @return the first
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(final boolean first) {
        this.first = first;
    }

    /**
     * @return the last
     */
    public boolean isLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(final boolean last) {
        this.last = last;
    }

    public void setAbsences(final List<Absence> absences) {
        absencesGSON = new ArrayList<AbsenceGSON>();
        for (Absence absence : absences) {
            AbsenceGSON absenceGSON = new AbsenceGSON();
            absenceGSON.typeAbsence = absence.getTypeAsValue();
            absencesGSON.add(absenceGSON);
        }
    }

    public void setAbsencesGSON(final List<AbsenceGSON> absences) {
        absencesGSON = absences;
    }

    public List<AbsenceGSON> getAbsencesGSON() {
        return absencesGSON;
    }

    public boolean isMensuel() {
        return isMensuel;
    }

    public void setMensuel(boolean isMensuel) {
        this.isMensuel = isMensuel;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    /**
     * @return the spy
     */
    public String getSpy() {
        return spy;
    }

    /**
     * @param spy the spy to set
     */
    public void setSpy(final String spy) {
        this.spy = spy;
    }

    public String getMasseAC2() {
        return masseAC2;
    }

    public void setMasseAC2(String masseAC2) {
        this.masseAC2 = masseAC2;
    }

    public final String getMasseFranchise() {
        return masseFranchise;
    }

    public final void setMasseFranchise(String masseFranchise) {
        this.masseFranchise = masseFranchise;
    }

    public final String getIdTiersTravailleur() {
        return idTiersTravailleur;
    }

    public final void setIdTiersTravailleur(String idTiersTravailleur) {
        this.idTiersTravailleur = idTiersTravailleur;
    }

    /**
     * @return the remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * @param remarque the remarque to set
     */
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setCodeErreur(final List<CodeErreurDecompteSalaire> codeErreurDS) {
        codeErreur = new String[codeErreurDS.size()];
        int i = 0;
        for (CodeErreurDecompteSalaire code : codeErreurDS) {
            codeErreur[i++] = code.getCodeErreurAsValue();
        }
    }

    /**
     * @return the codeErreur
     */
    public String[] getCodeErreur() {
        return codeErreur;
    }

    /**
     * @param codeErreur the codeErreur to set
     */
    public void setCodeErreur(String[] codeErreur) {
        this.codeErreur = codeErreur;
    }

    /**
     * @return the aTraiter
     */
    public boolean isQuittancer() {
        return quittancer;
    }

    /**
     * @param aTraiter the aTraiter to set
     */
    public void setQuittancer(boolean aTraiter) {
        quittancer = aTraiter;
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

    /**
     * @return the nouveauTravailleur
     */
    public boolean isNouveauTravailleur() {
        return nouveauTravailleur;
    }

    /**
     * @param nouveauTravailleur the nouveauTravailleur to set
     */
    public void setNouveauTravailleur(boolean nouveauTravailleur) {
        this.nouveauTravailleur = nouveauTravailleur;
    }

    public int getAnneeCotisations() {
        return anneeCotisations;
    }

    public void setAnneeCotisations(int anneeCotisations) {
        this.anneeCotisations = anneeCotisations;
    }

    public String getTauxAAfficher() {
        return tauxAAfficher;
    }

    public void setTauxAAfficher(String tauxAAfficher) {
        this.tauxAAfficher = tauxAAfficher;
    }

    public String getTauxSaisieEbu() {
        return tauxSaisieEbu;
    }

    public void setTauxSaisieEbu(String tauxSaisieEbu) {
        this.tauxSaisieEbu = tauxSaisieEbu;
    }

    public StatusLine getStatus() {
        return status;
    }

    public void setStatus(StatusLine status) {
        this.status = status;
    }

    public String getVacances() {
        return vacances;
    }

    public void setVacances(String vacances) {
        this.vacances = vacances;
    }

    public String getGratifications() {
        return gratifications;
    }

    public void setGratifications(String gratifications) {
        this.gratifications = gratifications;
    }

    public String getAbsencesJustifiees() {
        return absencesJustifiees;
    }

    public void setAbsencesJustifiees(String absencesJustifiees) {
        this.absencesJustifiees = absencesJustifiees;
    }

    public String getApgComplSm() {
        return apgComplSm;
    }

    public void setApgComplSm(String apgComplSm) {
        this.apgComplSm = apgComplSm;
    }

    public boolean isForcerFranchise0() {
        return forcerFranchise0;
    }

    public void setForcerFranchise0(boolean forcerFranchise0) {
        this.forcerFranchise0 = forcerFranchise0;
    }

    public boolean isEnErreur() {
        return isEnErreur;
    }

    public void setEnErreur(boolean isEnErreur) {
        this.isEnErreur = isEnErreur;
    }
}
