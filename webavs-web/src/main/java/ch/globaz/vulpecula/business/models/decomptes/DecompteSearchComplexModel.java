package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;

public class DecompteSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -6641472392212694779L;

    public static final String ORDER_BY_RAISON_SOCIAL_ASC = "raisonSocialeAsc";
    public static final String ORDER_BY_RAISON_SOCIAL_PERIODE_DEBUT_ASC = "raisonSocialePeriodeDebutAsc";
    public static final String ORDER_BY_ID_ASC = "idAsc";
    public static final String ORDER_BY_PERIODE_DESC = "periodeDebutDesc";

    private Collection<String> forIds;
    private String forId;
    private String forIdGreater;
    private String forNoDecompte;
    private String likeNoDecompte;
    private String forIdEmployeur;
    private String forIdConvention;
    private String likeNoAffilie;
    private String forDateDe;
    private String forDateAu;
    private String fromDateDebut;
    private String toDateFin;
    private String likeRaisonSociale;
    private String likeRaisonSocialeUpper;
    private String forType;
    private String forNotType;
    private String forEtat;
    private String forNotEtat;
    private Collection<String> inEtats;
    private Collection<String> inTypes;
    private String forIdPassage;
    private Boolean wantRectifie;
    private String beforeDate;
    private String forDateReception;
    private String beforeDateRappel;
    private String dateRappelIsNotZero;
    private String forTypeProvenance;

    private String forAnneeDebut;
    private String forAnneeFin;

    public String getForAnneeDebut() {
        return forAnneeDebut;
    }

    public void setForAnneeDebut(String forAnneeDebut) {
        this.forAnneeDebut = forAnneeDebut;
    }

    public String getForAnneeFin() {
        return forAnneeFin;
    }

    public void setForAnneeFin(String forAnneeFin) {
        this.forAnneeFin = forAnneeFin;
    }

    public String getForDateReception() {
        return forDateReception;
    }

    public void setForDateReception(String forDateReception) {
        this.forDateReception = forDateReception;
    }

    public Boolean getWantRectifie() {
        return wantRectifie;
    }

    public void setWantRectifie(Boolean wantRectifie) {
        this.wantRectifie = wantRectifie;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(final String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public DecompteSearchComplexModel() {
        inEtats = new ArrayList<String>();
        inTypes = new ArrayList<String>();
    }

    public String getForDateAu() {
        return forDateAu;
    }

    public String getForDateDe() {
        return forDateDe;
    }

    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return the forNotEtat
     */
    public String getForNotEtat() {
        return forNotEtat;
    }

    /**
     * @param forNotEtat the forNotEtat to set
     */
    public void setForNotEtat(String forNotEtat) {
        this.forNotEtat = forNotEtat;
    }

    public String getForId() {
        return forId;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public String getForNoDecompte() {
        return forNoDecompte;
    }

    public String getForType() {
        return forType;
    }

    public Collection<String> getInEtats() {
        return inEtats;
    }

    public Collection<String> getInTypes() {
        return inTypes;
    }

    public String getLikeNoAffilie() {
        return likeNoAffilie;
    }

    public String getLikeRaisonSociale() {
        return likeRaisonSociale;
    }

    public String getLikeRaisonSocialeUpper() {
        return likeRaisonSocialeUpper;
    }

    public void setLikeRaisonSocialeUpper(String likeRaisonSocialeUpper) {
        this.likeRaisonSocialeUpper = JadeStringUtil.toUpperCase(likeRaisonSocialeUpper);
    }

    public void setForDateAu(final String forDateAu) {
        this.forDateAu = forDateAu;
    }

    public void setForDateDe(final String forDateDe) {
        this.forDateDe = forDateDe;
    }

    public void setForDateDe(Date forDateDe) {
        this.forDateDe = forDateDe.getAnneeMois();
    }

    public void setForDateAu(Date forDateAu) {
        this.forDateAu = forDateAu.getAnneeMois();
    }

    public void setFromDateDebut(Date fromDateDebut) {
        this.fromDateDebut = fromDateDebut.getAnneeMois();
    }

    public void setToDateFin(Date toDateFin) {
        this.toDateFin = toDateFin.getAnneeMois();
    }

    /**
     * @return the fromDateDebut
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * @return the toDateFin
     */
    public String getToDateFin() {
        return toDateFin;
    }

    /**
     * @param fromDateDebut the fromDateDebut to set
     */
    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    /**
     * @param toDateFin the toDateFin to set
     */
    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
    }

    public void setForPeriode(final PeriodeMensuelle periodeMensuelle) {
        forDateDe = periodeMensuelle.getPeriodeDebutAsValue();
        forDateAu = periodeMensuelle.getPeriodeFinAsValue();
    }

    public void setForEtat(final String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForEtat(final EtatDecompte etat) {
        if (etat != null) {
            forEtat = etat.getValue();
        }
    }

    public void setForNotEtat(final EtatDecompte etat) {
        if (etat != null) {
            forNotEtat = etat.getValue();
        }
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    public void setForIdConvention(final String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForIdEmployeur(final String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public void setForNoDecompte(final String forNoDecompte) {
        this.forNoDecompte = forNoDecompte;
    }

    public void setForType(final TypeDecompte type) {
        forType = type.getValue();
    }

    public void setForType(final String forType) {
        this.forType = forType;
    }

    /**
     * @param etats
     *            the EtatDecompte to set. if none are provided, then this search criteria is discarded.
     */
    public void setInEtats(final EtatDecompte... etats) {
        if (etats.length == 0) {
            inEtats = null;
        } else {
            inEtats = new ArrayList<String>();
            for (EtatDecompte etatDecompte : etats) {
                inEtats.add(etatDecompte.getValue());
            }
        }
    }

    /**
     * @param etats
     *            a String representing one or more (comma-separated) EtatDecompte to set. if none are provided, then
     *            this search criteria is discarded.
     */
    public void setInEtats(final String etats) {
        if (etats.length() == 0) {
            inEtats = null;
            return;
        }
        List<EtatDecompte> etatsDecompte = new ArrayList<EtatDecompte>();
        if (etats.contains(",")) {
            for (String etat : etats.split(",")) {
                etatsDecompte.add(EtatDecompte.fromValue(etat));
            }
        } else {
            etatsDecompte.add(EtatDecompte.fromValue(etats));
        }
        EtatDecompte[] etatsDecompteArray = new EtatDecompte[etatsDecompte.size()];
        setInEtats(etatsDecompte.toArray(etatsDecompteArray));
    }

    /**
     * Rajoute un crit?re de recherche sur le type de d?compte, qui doit ?tre contenu dans la liste pass?e en param?tre.
     * 
     * @param types les types de d?comptes que l'on recherche
     */
    public void setInTypes(TypeDecompte... types) {
        if (types.length == 0) {
            inTypes = null;
        } else {
            inTypes = new ArrayList<String>();
            for (TypeDecompte typeDecompte : types) {
                inTypes.add(typeDecompte.getValue());
            }
        }
    }

    public void setLikeNoAffilie(final String likeNoAffilie) {
        this.likeNoAffilie = likeNoAffilie;
    }

    public void setLikeRaisonSociale(final String likeRaisonSociale) {
        this.likeRaisonSociale = likeRaisonSociale;
    }

    public String getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }

    public void setBeforeDate(Date beforeDate) {
        this.beforeDate = beforeDate.getValue();
    }

    public void setBeforeDateRappel(Date date) {
        setBeforeDateRappel(date.getSwissValue());
    }

    public void setBeforeDateRappel(String date) {
        beforeDateRappel = date;
    }

    public String getBeforeDateRappel() {
        return beforeDateRappel;
    }

    public String getDateRappelIsNotZero() {
        return dateRappelIsNotZero;
    }

    public void setDateRappelIsNotZero() {
        dateRappelIsNotZero = "0";
    }

    public Collection<String> getForIds() {
        return forIds;
    }

    public void setForIds(Collection<String> forIds) {
        this.forIds = forIds;
    }

    public final String getForIdGreater() {
        return forIdGreater;
    }

    public final void setForIdGreater(String forIdGreater) {
        this.forIdGreater = forIdGreater;
    }

    public final String getLikeNoDecompte() {
        return likeNoDecompte;
    }

    public final void setLikeNoDecompte(String likeNoDecompte) {
        if (!JadeStringUtil.isEmpty(likeNoDecompte)) {
            this.likeNoDecompte = "%" + likeNoDecompte;
        }
    }

    /**
     * @return the forTypeProvenance
     */
    public String getForTypeProvenance() {
        return forTypeProvenance;
    }

    public void setForTypeProvenance(final TypeProvenance typeProvenance) {
        forTypeProvenance = typeProvenance.getValue();
    }

    /**
     * @param forTypeProvenance the forTypeProvenance to set
     */
    public void setForTypeProvenance(String forTypeProvenance) {
        this.forTypeProvenance = forTypeProvenance;
    }

    /**
     * @return the forNotType
     */
    public String getForNotType() {
        return forNotType;
    }

    /**
     * @param forNotType the forNotType to set
     */
    public void setForNotType(String forNotType) {
        this.forNotType = forNotType;
    }

    @Override
    public Class<DecompteComplexModel> whichModelClass() {
        return DecompteComplexModel.class;
    }
}
