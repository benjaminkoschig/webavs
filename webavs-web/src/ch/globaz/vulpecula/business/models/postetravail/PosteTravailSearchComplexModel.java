package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

/**
 * Modèle de recherche pour {@link PosteTravailComplexModel}
 * 
 */
public class PosteTravailSearchComplexModel extends JadeSearchComplexModel {
    public static final String WHERE_AANNONCER = "aAnnoncer";

    private String forPeriodeDebutLess;
    private String forGenrePrestation;
    private String forPeriodeDebut;
    private String forPeriodeFin;
    private String forIdAffilie;
    private String forIdEmployeur;
    private String forIdPosteTravail;
    private String forIdTiers;
    private String forIdTravailleur;
    private String forIdConvention;
    private Collection<String> forQualificationIn;
    private String likeNomTravailleur;
    private String likePrenomTravailleur;
    private Boolean annonceMeroba;

    public String getForPeriodeDebut() {
        return forPeriodeDebut;
    }

    public void setForPeriodeDebut(final String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdAffilie(final String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }

    public void setForIdEmployeur(final String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public void setForIdPosteTravail(final String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    public void setForIdTiers(final String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTravailleur(final String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getLikeNomTravailleur() {
        return likeNomTravailleur;
    }

    public void setLikeNomTravailleur(final String likeNomTravailleur) {
        this.likeNomTravailleur = likeNomTravailleur.toUpperCase();
    }

    public String getLikePrenomTravailleur() {
        return likePrenomTravailleur;
    }

    public void setLikePrenomTravailleur(final String likePrenomTravailleur) {
        this.likePrenomTravailleur = likePrenomTravailleur.toUpperCase();
    }

    public Boolean getAnnonceMeroba() {
        return annonceMeroba;
    }

    public void setAnnonceMeroba(Boolean annonceMeroba) {
        this.annonceMeroba = annonceMeroba;
    }

    @Override
    public Class<PosteTravailComplexModel> whichModelClass() {
        return PosteTravailComplexModel.class;
    }

    public String getForPeriodeFin() {
        return forPeriodeFin;
    }

    public void setForPeriodeFin(final String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    public String getForGenrePrestation() {
        return forGenrePrestation;
    }

    public void setForGenrePrestation(String forGenrePrestation) {
        this.forGenrePrestation = forGenrePrestation;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public Collection<String> getForQualificationIn() {
        return forQualificationIn;
    }

    public void setForQualificationIn(Collection<String> forQualificationIn) {
        this.forQualificationIn = forQualificationIn;
    }

    public String getForPeriodeDebutLess() {
        return forPeriodeDebutLess;
    }

    public void setForPeriodeDebutLess(String forPeriodeDebutLess) {
        this.forPeriodeDebutLess = forPeriodeDebutLess;
    }

    /**
     * @param qualifications
     */
    public void setForQualficationIn(List<Qualification> qualifications) {
        List<String> qualifs = new ArrayList<String>();
        for (Qualification qualification : qualifications) {
            qualifs.add(qualification.getValue());
        }
        setForQualificationIn(qualifs);

    }
}
