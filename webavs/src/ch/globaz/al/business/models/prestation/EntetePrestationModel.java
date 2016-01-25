package ch.globaz.al.business.models.prestation;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle simple de l'en-tête d'une prestation
 * 
 * @author PTA
 * 
 */
public class EntetePrestationModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Type de bonification
     */
    private String bonification = null;
    /**
     * Canton de l'affilié
     */
    private String cantonAffilie = null;
    /**
     * Date du versement compensatoire
     */
    private String dateVersComp = null;
    /**
     * Etat de la prestation
     */
    private String etatPrestation = null;
    /**
     * Id de la cotisation lié à l'affilié pour la période de la prestation
     */
    private String idCotisation = null;
    /**
     * identifiant du dossier
     */
    private String idDossier = null;
    /**
     * Identifiant de l'entête de la prestation
     */
    private String idEntete = null;
    /**
     * Identifiant du journal
     */
    private String idJournal = null;
    /**
     * Identifiant du passage
     */
    private String idPassage = null;
    /**
     * Identifiant de la récapitulation
     */
    private String idRecap = null;
    /**
     * Identifiant du tiers de la caisse AF
     */
    private String idTiersCaisseAF = null;
    /**
     * Jour du début de la mutation
     */
    private String jourDebutMut = null;
    /**
     * Jour de fin de la mutation
     */
    private String jourFinMut = null;
    /**
     * Mois du début de la mutation
     */
    private String moisDebutMut = null;
    /**
     * Mois de fin de la mutation
     */
    private String moisFinMut = null;
    /**
     * Montant total
     */
    private String montantTotal = null;
    /**
     * Nombre d'enfants
     */
    private String nombreEnfants = null;
    /**
     * Nombre d'unités
     */
    private String nombreUnite = null;
    /**
     * Numéro du passage génération
     */
    private String numPsgGeneration = null;
    /**
     * Fin de la période de validité de la prestation
     */
    private String periodeA = null;
    /**
     * Début de la période de validité de la prestation
     */
    private String periodeDe = null;
    /**
     * Statut de la prestation. Indique s'il s'agit d'une ADI, ADC ou Suisse
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_STATUT
     */
    private String statut = null;
    /**
     * Taux de versement du dossier au moment de la génération de la prestation
     */
    private String tauxVersement = null;
    /**
     * Type de génération (globale, affilié ou dossier)
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_GENERATION_TYPE
     */
    private String typeGeneration = null;
    /**
     * Unité du dossier au moment de la génération
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL
     */
    private String unite = null;

    /**
     * @return the bonification
     */
    public String getBonification() {
        return bonification;
    }

    /**
     * @return the cantonAffilie
     */
    public String getCantonAffilie() {
        return cantonAffilie;
    }

    /**
     * @return the dateVersComp
     */
    public String getDateVersComp() {
        return dateVersComp;
    }

    /**
     * @return the etatPrestation
     */
    public String getEtatPrestation() {
        return etatPrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idEntete;
    }

    /**
     * @return Id de la cotisation lié à l'affilié pour la période de la prestation
     */
    public String getIdCotisation() {
        return idCotisation;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idEntete
     */
    public String getIdEntete() {
        return idEntete;
    }

    /**
     * @return the idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @return the idPassage
     */
    public String getIdPassage() {
        return idPassage;
    }

    /**
     * @return the idRecap
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return the idTiersCaisseAF
     */
    public String getIdTiersCaisseAF() {
        return idTiersCaisseAF;
    }

    /**
     * @return the jourDebutMut
     */
    public String getJourDebutMut() {
        return jourDebutMut;
    }

    /**
     * @return the jourFinMut
     */
    public String getJourFinMut() {
        return jourFinMut;
    }

    /**
     * @return the moisDebutMut
     */
    public String getMoisDebutMut() {
        return moisDebutMut;
    }

    /**
     * @return the moisFinMut
     */
    public String getMoisFinMut() {
        return moisFinMut;
    }

    /**
     * Montant total des détails prestations liés à l'en-tête
     * 
     * @return the montantTotal
     */
    public String getMontantTotal() {
        return montantTotal;
    }

    /**
     * Montant total des détails prestations liés à l'en-tête formatté Par exemple : 1000.00 --> 1'000.00
     */
    public String getMontantTotalFormatte() {
        return JANumberFormatter.fmt(montantTotal, true, true, false, 2);
    }

    /**
     * @return the nombreEnfants
     */
    public String getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * @return the nombreUnite
     */
    public String getNombreUnite() {
        return nombreUnite;
    }

    /**
     * @return the numPsgGeneration
     */
    public String getNumPsgGeneration() {
        return numPsgGeneration;
    }

    /**
     * @return the periodeA
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * @return the periodeDe
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /**
     * @return the statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @return the tauxVersement
     */
    public String getTauxVersement() {
        return tauxVersement;
    }

    /**
     * @return the tauxVersement formatte
     */
    public String getTauxVersementFormatte() {
        if (tauxVersement != null) {
            return JANumberFormatter.fmt(tauxVersement, true, true, false, 2);
        }
        return null;
    }

    /**
     * @return the typeGeneration
     */
    public String getTypeGeneration() {
        return typeGeneration;
    }

    /**
     * @return the unite
     */
    public String getUnite() {
        return unite;
    }

    /**
     * @param bonification
     *            the bonification to set
     */
    public void setBonification(String bonification) {
        this.bonification = bonification;
    }

    /**
     * @param cantonAffilie
     *            the cantonAffilie to set
     */
    public void setCantonAffilie(String cantonAffilie) {
        this.cantonAffilie = cantonAffilie;
    }

    /**
     * @param dateVersComp
     *            the dateVersComp to set
     */
    public void setDateVersComp(String dateVersComp) {
        this.dateVersComp = dateVersComp;
    }

    /**
     * @param etatPrestation
     *            the etatPrestation to set
     */
    public void setEtatPrestation(String etatPrestation) {
        this.etatPrestation = etatPrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idEntete = id;
    }

    /**
     * @param idCotisation
     *            id de la cotisation lié à l'affilié pour la période de la prestation
     */
    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idEntete
     *            the idEntete to set
     */
    public void setIdEntete(String idEntete) {
        this.idEntete = idEntete;
    }

    /**
     * @param idJournal
     *            the idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * @param idPassage
     *            the idPassage to set
     */
    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    /**
     * @param idRecap
     *            the idRecap to set
     */
    public void setIdRecap(String idRecap) {
        this.idRecap = idRecap;
    }

    /**
     * @param idTiersCaisseAF
     *            the idTiersCaisseAF to set
     */
    public void setIdTiersCaisseAF(String idTiersCaisseAF) {
        this.idTiersCaisseAF = idTiersCaisseAF;
    }

    /**
     * @param jourDebutMut
     *            the jourDebutMut to set
     */
    public void setJourDebutMut(String jourDebutMut) {
        this.jourDebutMut = jourDebutMut;
    }

    /**
     * @param jourFinMut
     *            the jourFinMut to set
     */
    public void setJourFinMut(String jourFinMut) {
        this.jourFinMut = jourFinMut;
    }

    /**
     * @param moisDebutMut
     *            the moisDebutMut to set
     */
    public void setMoisDebutMut(String moisDebutMut) {
        this.moisDebutMut = moisDebutMut;
    }

    /**
     * @param moisFinMut
     *            the moisFinMut to set
     */
    public void setMoisFinMut(String moisFinMut) {
        this.moisFinMut = moisFinMut;
    }

    /**
     * @param montantTotal
     *            the montantTotal to set
     */
    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    /**
     * @param nombreEnfants
     *            the nombreEnfants to set
     */
    public void setNombreEnfants(String nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    /**
     * @param nombreUnite
     *            the nombreUnite to set
     */
    public void setNombreUnite(String nombreUnite) {
        this.nombreUnite = nombreUnite;
    }

    /**
     * @param numPsgGeneration
     *            the numPsgGeneration to set
     */
    public void setNumPsgGeneration(String numPsgGeneration) {
        this.numPsgGeneration = numPsgGeneration;
    }

    /**
     * @param periodeA
     *            the periodeA to set
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * @param periodeDe
     *            the periodeDe to set
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

    /**
     * Définit le statut de la prestation.
     * 
     * Indique s'il s'agit d'une ADI, ADC ou Suisse
     * 
     * @param statut
     *            the statut to set
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_STATUT
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @param tauxVersement
     *            the tauxVersement to set
     */
    public void setTauxVersement(String tauxVersement) {
        this.tauxVersement = tauxVersement;
    }

    /**
     * @param typeGeneration
     *            the typeGeneration to set
     */
    public void setTypeGeneration(String typeGeneration) {
        this.typeGeneration = typeGeneration;
    }

    /**
     * @param unite
     *            the unite to set
     */
    public void setUnite(String unite) {
        this.unite = unite;
    }
}
