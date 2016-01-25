package ch.globaz.vulpecula.domain.models.caissemaladie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.specifications.caissemaladie.CMCaisseMaladieSaisieSpecification;
import ch.globaz.vulpecula.domain.specifications.caissemaladie.CMPeriodeCaisseMaladieValideSpecification;
import ch.globaz.vulpecula.domain.specifications.caissemaladie.CMPeriodePosteTravailValideSpecification;
import ch.globaz.vulpecula.domain.specifications.caissemaladie.CMPeriodeValideSpecification;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class AffiliationCaisseMaladie implements DomainEntity {

    private String id;
    private Travailleur travailleur;
    private Administration caisseMaladie;
    private Date moisDebut;
    private Date moisFin;
    private Date dateAnnonceDebut;
    private Date dateAnnonceFin;
    private String spy;
    private List<SuiviCaisseMaladie> suiviDocument = new ArrayList<SuiviCaisseMaladie>();
    private String idPosteTravail;
    private String descriptionEmployeur;
    private PosteTravail posteTravail;

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public List<SuiviCaisseMaladie> getSuiviDocument() {
        return suiviDocument;
    }

    public void setSuiviDocument(List<SuiviCaisseMaladie> suiviDocument) {
        this.suiviDocument = suiviDocument;
    }

    public Administration getCaisseMaladie() {
        return caisseMaladie;
    }

    public void setCaisseMaladie(Administration caisseMaladie) {
        this.caisseMaladie = caisseMaladie;
    }

    public Date getMoisDebut() {
        return moisDebut;
    }

    public void setMoisDebut(Date moisDebut) {
        this.moisDebut = moisDebut;
    }

    public Date getMoisFin() {
        return moisFin;
    }

    public void setMoisFin(Date moisFin) {
        this.moisFin = moisFin;
    }

    public Date getDateAnnonceDebut() {
        return dateAnnonceDebut;
    }

    public void setDateAnnonceDebut(Date dateAnnonceDebut) {
        this.dateAnnonceDebut = dateAnnonceDebut;
    }

    public Date getDateAnnonceFin() {
        return dateAnnonceFin;
    }

    public void setDateAnnonceFin(Date dateAnnonceFin) {
        this.dateAnnonceFin = dateAnnonceFin;
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdTravailleur() {
        return travailleur.getId();
    }

    public String getMoisAnneeDebut() {
        if (moisDebut != null) {
            return moisDebut.getAnneeMois();
        }
        return "";
    }

    public String getMoisAnneeDebutFormatte() {
        if (moisDebut != null) {
            return moisDebut.getMoisAnneeFormatte();
        }
        return null;
    }

    public String getMoisAnneeFinFormatte() {
        if (moisFin != null) {
            return moisFin.getMoisAnneeFormatte();
        }
        return null;
    }

    public String getMoisAnneeFin() {
        if (moisFin != null) {
            return moisFin.getAnneeMois();
        }
        return "";
    }

    public String getNoAVSTravailleur() {
        return travailleur.getNumAvsActuel();
    }

    public String getNomTravailleur() {
        return travailleur.getDesignation1();
    }

    public String getPrenomTravailleur() {
        return travailleur.getDesignation2();
    }

    public String getLibelleCaisseMaladie() {
        if (caisseMaladie.getDesignation2().length() > 0) {
            return caisseMaladie.getDesignation1() + " " + caisseMaladie.getDesignation2();
        }
        return caisseMaladie.getDesignation1();
    }

    public String getIdCaisseMaladie() {
        if (caisseMaladie == null) {
            return null;
        }
        return caisseMaladie.getIdTiers();
    }

    public void validate(List<AffiliationCaisseMaladie> caissesExistantes) throws UnsatisfiedSpecificationException {
        CMCaisseMaladieSaisieSpecification caisseMaladieSaisieSpecification = new CMCaisseMaladieSaisieSpecification();
        CMPeriodeValideSpecification periodeValideSpecification = new CMPeriodeValideSpecification();

        caisseMaladieSaisieSpecification.isSatisfiedBy(this);
        periodeValideSpecification.isSatisfiedBy(this);

        CMPeriodeCaisseMaladieValideSpecification periodeCaisseValide = new CMPeriodeCaisseMaladieValideSpecification(
                caissesExistantes);
        periodeCaisseValide.isSatisfiedBy(this);

        CMPeriodePosteTravailValideSpecification periodePosteValide = new CMPeriodePosteTravailValideSpecification(
                getPosteTravail());

        periodePosteValide.isSatisfiedBy(this);

    }

    /**
     * Détermine si une affiliation à une caisse maladie peut être supprimée.
     * Une affiliation annoncé (admission ou démission) ne peut plus être supprimée ni modifiée
     * 
     * @return true si possibilité de supprimer
     */
    public boolean isSupprimable() {
        return dateAnnonceDebut == null && dateAnnonceFin == null;
    }

    /**
     * Détermine si une affiliation à une caisse maladie peut être modifiée.
     * Une affiliation annoncé uniquement en admission/démission peut être modifiée.
     * 
     * @return true si possibilité de modifier
     */
    public boolean isModifiable() {
        return dateAnnonceDebut == null || dateAnnonceFin == null;
    }

    public static Map<Administration, Collection<AffiliationCaisseMaladie>> groupByCaisseMaladie(
            List<AffiliationCaisseMaladie> affiliations) {
        return Multimaps.index(affiliations, new Function<AffiliationCaisseMaladie, Administration>() {
            @Override
            public Administration apply(AffiliationCaisseMaladie affiliationCaisseMaladie) {
                return affiliationCaisseMaladie.caisseMaladie;
            }
        }).asMap();
    }

    public String getDescriptionEmployeur() {
        return descriptionEmployeur;
    }

    public void setDescriptionEmployeur(String descriptionEmployeur) {
        this.descriptionEmployeur = descriptionEmployeur;
    }

    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    /**
     * Retourne si l'affiliation est active à la date du jour.
     * 
     * @return true si actif, false si inactif
     */
    public boolean isActif() {
        return new Periode(moisDebut, moisFin).isActifMois(Date.now());
    }

}
