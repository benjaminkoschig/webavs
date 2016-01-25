package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.decompte.Absence;

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
    private String sequence;
    private boolean first;
    private boolean last;
    private boolean isMensuel;
    private List<AbsenceGSON> absencesGSON;
    private String spy;
    private String periodeDebut;
    private String periodeFin;

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
}
