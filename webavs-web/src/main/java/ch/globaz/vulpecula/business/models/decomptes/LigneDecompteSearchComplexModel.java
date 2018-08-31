package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class LigneDecompteSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -4117607392449294593L;

    public static final String SEQUENCE_DESC = "sequenceDesc";
    public static final String SEQUENCE_ASC = "sequenceAsc";
    public static final String ORDER_BY_EMPLOYEUR = "employeur";
    public static final String ORDER_BY_TRAVAILLEUR = "travailleur";

    public static final String DATE_FIN_DECOMPTE_DESC = "dateFinDecompteDesc";
    public static final String DATE_FIN_DECOMPTE_ASC = "dateFinDecompteAsc";

    public static final String ID_DECOMPTE_ASC = "idDecompteAsc";

    private String forId;
    private String forIdConvention;
    private String forIdDecompte;
    private String forIdDecompteGreater;
    private String forIdPosteTravail;
    private String forIdTravailleur;
    private String forIdEmployeur;
    private String forSequence;
    private String fromSequence;
    private String toSequence;
    private String beforeDateFinDecompte;
    private String forEtatDecompte;
    private String forPeriodeDebut;
    private String forPeriodeFin;
    private Collection<String> forIdsDecomptesIn;
    private Collection<String> forIdsPostesTravailIn;
    private String forDateNonAnnoncee;
    private String forDesignationUpper1Like;
    private String forDesignationUpper2Like;
    private String forRaisonSocialeUpperLike;
    private String likeNumeroDecompte;
    private String forAnneeDecompte;
    private String forNumeroDecompte;
    private String forIdTravailleurLess;
    private String forIdTravailleurGreater;
    private String forPeriodeFinAfter;
    private Collection<String> forEtatDecompteIn;
    private List<String> forTypeDecompteIn;
    private String forAnneeCotisationsGreaterOrEquals;
    private String forAnneeCotisationsLessOrEquals;
    private Boolean forToTreat;

    public String getForDateNonAnnoncee() {
        return forDateNonAnnoncee;
    }

    public void setForDateNonAnnoncee(String forDateNonAnnoncee) {
        this.forDateNonAnnoncee = forDateNonAnnoncee;
    }

    public String getForId() {
        return forId;
    }

    public String getForIdDecompte() {
        return forIdDecompte;
    }

    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    /**
     * @return the forSequence
     */
    public String getForSequence() {
        return forSequence;
    }

    /**
     * @return the fromSequence
     */
    public String getFromSequence() {
        return fromSequence;
    }

    /**
     * @return the toSequence
     */
    public String getToSequence() {
        return toSequence;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    public void setForIdDecompte(final String forIdDecompte) {
        this.forIdDecompte = forIdDecompte;
    }

    public void setForIdPosteTravail(final String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    public void setForIdTravailleur(final String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    /**
     * @param forSequence
     *            the forSequence to set
     */
    public void setForSequence(final String forSequence) {
        this.forSequence = forSequence;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    /**
     * @param fromSequence
     *            the fromSequence to set
     */
    public void setFromSequence(final String fromSequence) {
        this.fromSequence = fromSequence;
    }

    /**
     * @param toSequence
     *            the toSequence to set
     */
    public void setToSequence(final String toSequence) {
        this.toSequence = toSequence;
    }

    public Collection<String> getForIdsDecomptesIn() {
        return forIdsDecomptesIn;
    }

    public void setForIdsDecomptesIn(final Collection<String> forIdsDecomptesIn) {
        this.forIdsDecomptesIn = forIdsDecomptesIn;
    }

    public String getBeforeDateFinDecompte() {
        return beforeDateFinDecompte;
    }

    public void setBeforeDateFinDecompte(Date beforeDateFinDecompte) {
        this.beforeDateFinDecompte = beforeDateFinDecompte.getAnnee() + beforeDateFinDecompte.getMois();
    }

    public void setBeforeDateFinDecompte(String beforeDateFinDecompte) {
        this.beforeDateFinDecompte = beforeDateFinDecompte;
    }

    public void setForEtatDecompte(EtatDecompte etatDecompte) {
        forEtatDecompte = etatDecompte.getValue();
    }

    public String getForEtatDecompte() {
        return forEtatDecompte;
    }

    public String getForPeriodeDebut() {
        return forPeriodeDebut;
    }

    public void setForPeriodeDebut(Date dateDebut) {
        forPeriodeDebut = dateDebut.getSwissValue();
    }

    public void setForPeriodeDebut(String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    public String getForPeriodeFin() {
        return forPeriodeFin;
    }

    public void setForPeriodeFin(String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    public void setForPeriodeFin(Date periodeFin) {
        forPeriodeFin = periodeFin.getSwissValue();
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public List<String> getForTypeDecompteIn() {
        return forTypeDecompteIn;
    }

    public void setForTypeDecompteIn(List<String> forTypeDecompteIn) {
        this.forTypeDecompteIn = forTypeDecompteIn;
    }

    public void setForTypeDecompteIn(TypeDecompte... types) {
        List<String> collection = new ArrayList<String>();
        for (TypeDecompte type : types) {
            collection.add(type.getValue());
        }
        forTypeDecompteIn = collection;
    }

    public Collection<String> getForIdsPostesTravailIn() {
        return forIdsPostesTravailIn;
    }

    public void setForIdsPostesTravailIn(Collection<String> forIdsPostesTravailIn) {
        this.forIdsPostesTravailIn = forIdsPostesTravailIn;
    }

    public final String getForDesignationUpper1Like() {
        return forDesignationUpper1Like;
    }

    public final void setForDesignationUpper1Like(String forDesignationUpper1Like) {
        if (!JadeStringUtil.isEmpty(forDesignationUpper1Like)) {
            this.forDesignationUpper1Like = "%" + JadeStringUtil.toUpperCase(forDesignationUpper1Like);
        }
    }

    public final String getForDesignationUpper2Like() {
        return forDesignationUpper2Like;
    }

    public final void setForDesignationUpper2Like(String forDesignationUpper2Like) {
        if (!JadeStringUtil.isEmpty(forDesignationUpper2Like)) {
            this.forDesignationUpper2Like = "%" + JadeStringUtil.toUpperCase(forDesignationUpper2Like);
        }
    }

    public void setForDesignationUpperLike(String nom) {
        setForDesignationUpper1Like(nom);
        setForDesignationUpper2Like(nom);
    }

    public final String getForRaisonSocialeUpperLike() {
        return forRaisonSocialeUpperLike;
    }

    public final void setForRaisonSocialeUpperLike(String forRaisonSocialeUpperLike) {
        if (!JadeStringUtil.isEmpty(forRaisonSocialeUpperLike)) {
            this.forRaisonSocialeUpperLike = "%" + JadeStringUtil.toUpperCase(forRaisonSocialeUpperLike);
        }
    }

    public final String getForIdDecompteGreater() {
        return forIdDecompteGreater;
    }

    public final void setForIdDecompteGreater(String forIdDecompteGreater) {
        this.forIdDecompteGreater = forIdDecompteGreater;
    }

    public final String getLikeNumeroDecompte() {
        return likeNumeroDecompte;
    }

    public final void setLikeNumeroDecompte(String likeNumeroDecompte) {
        this.likeNumeroDecompte = "%" + likeNumeroDecompte;
    }

    public String getForAnneeDecompteDebut() {
        AnneeComptable anneeComptable = new AnneeComptable(forAnneeDecompte);
        return anneeComptable.getDateDebutAsSwissValue();
    }

    public String getForAnneeDecompteFin() {
        AnneeComptable anneeComptable = new AnneeComptable(forAnneeDecompte);
        return anneeComptable.getDateFinAsSwissValue();
    }

    public void setForAnneeDecompte(String forAnneeDecompte) {
        this.forAnneeDecompte = forAnneeDecompte;
    }

    public String getForNumeroDecompte() {
        return forNumeroDecompte;
    }

    public void setForNumeroDecompte(String forNumeroDecompte) {
        this.forNumeroDecompte = forNumeroDecompte;
    }

    public String getForIdTravailleurLess() {
        return forIdTravailleurLess;
    }

    public void setForIdTravailleurLess(String forIdTravailleurLess) {
        this.forIdTravailleurLess = forIdTravailleurLess;
    }

    public String getForIdTravailleurGreater() {
        return forIdTravailleurGreater;
    }

    public void setForIdTravailleurGreater(String forIdTravailleurGreater) {
        this.forIdTravailleurGreater = forIdTravailleurGreater;
    }

    /**
     * @return the forEtatDecompteIn
     */
    public Collection<String> getForEtatDecompteIn() {
        return forEtatDecompteIn;
    }

    /**
     * @param forEtatDecompteIn the forEtatDecompteIn to set
     */
    public void setForEtatDecompteIn(Collection<String> forEtatDecompteIn) {
        this.forEtatDecompteIn = forEtatDecompteIn;
    }

    public String getForPeriodeFinAfter() {
        return forPeriodeFinAfter;
    }

    public void setForPeriodeFinAfter(Date dateFin) {
        forPeriodeFinAfter = dateFin.getSwissValue();
    }

    public String getForAnneeCotisationsGreaterOrEquals() {
        return forAnneeCotisationsGreaterOrEquals;
    }

    public void setForAnneeCotisationsGreaterOrEquals(String forAnneeCotisationsGreaterOrEquals) {
        this.forAnneeCotisationsGreaterOrEquals = forAnneeCotisationsGreaterOrEquals;
    }

    public String getForAnneeCotisationsLessOrEquals() {
        return forAnneeCotisationsLessOrEquals;
    }

    public void setForAnneeCotisationsLessOrEquals(String forAnneeCotisationsLessOrEquals) {
        this.forAnneeCotisationsLessOrEquals = forAnneeCotisationsLessOrEquals;
    }

    public void setForAnneeCotisationsGreaterOrEquals(Annee anneeDebut) {
        setForAnneeCotisationsGreaterOrEquals(String.valueOf(anneeDebut.getValue()));
    }

    public void setForAnneeCotisationsLessOrEquals(Annee anneeFin) {
        if (anneeFin != null) {
            setForAnneeCotisationsLessOrEquals(String.valueOf(anneeFin.getValue()));
        }
    }

    public Boolean getForToTreat() {
        return forToTreat;
    }

    public void setForToTreat(Boolean forToTreat) {
        this.forToTreat = forToTreat;
    }

    @Override
    public Class<? extends LigneDecompteComplexModel> whichModelClass() {
        return LigneDecompteComplexModel.class;
    }
}
