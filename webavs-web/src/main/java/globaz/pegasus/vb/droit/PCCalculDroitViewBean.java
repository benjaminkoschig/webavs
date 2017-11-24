/**
 * 
 */
package globaz.pegasus.vb.droit;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.calculmoissuivant.CalculMoisSuivantBuilder;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.calcul.CalculMoisSuivant;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 */
public class PCCalculDroitViewBean extends BJadePersistentObjectViewBean {

    private CalculException cause = null;

    private String dFForVersionListHtml = null;
    /** liste des df retro warning, plus non prise en compte dans le calcul mois suivant */
    ArrayList<CalculMoisSuivant> dfRetroDfList = null;

    /** Liste des id dfHeader modifies ou crees pour la version */
    private List<String> donneesFinancieresForVersion = null;

    private Droit droit = null;

    private String idDemande = null;

    private String idsEntityGroup = null;

    private String idVersionDroit = null;

    private boolean isCalculBloqueDuAujourDappoint = false;
    /** Blocage calcul standard pas de données financières */
    private boolean isCalculStandardBloqueNonDFModif = false;
    /** Blocage calcul standard aucune df dans la plage de calcul */
    private boolean isCalculStandardBloqueNonDFPlageCalcul = false;
    /** blocage mois suivant demande close */
    private boolean isCMSBlockedDemandeClose = false;
    private boolean isCalculInterdit = false;

    /** Etat blocqué ou non du calcul moissuivant */
    private boolean isCMSBlockedDuToError = true;

    /** blocage mois suivant, toutes les donnees financeres ont une action clore periode mois courant */
    private boolean isCMSBloqueDFClorePeriode = false;

    private Boolean isDfModifForVersion = false;

    private boolean isEffetMoisSuivantBloque = false;

    /** Liste des données financières modifiés pour la version */
    private List<CalculMoisSuivant> listeDFModifieForVersion = null;
    private String typeDeCalcule = null;

    /**
	 * 
	 */
    public PCCalculDroitViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {

    }

    /**
     * Retourne true, si une condition amenant à un blocage quelqconque du calcul est remplie
     * 
     * @return boolééen qui définit si un blocage est défini
     */
    private boolean aucunTypeCalculBloque() {
        return !isCalculStandardBloqueNonDFModif && !isCalculStandardBloqueNonDFPlageCalcul
                && !isCMSBlockedDemandeClose && !isCMSBloqueDFClorePeriode && !isCMSBlockedDuToError
                && !isCalculBloqueDuAujourDappoint;

    }

    public void calculeDroit() throws Exception {

        List<String> idsEntityGroupList = new ArrayList<String>();

        if (idsEntityGroup.trim().length() != 0) {
            // Recréation d'une liste des idEntityGroup
            for (String idEntity : idsEntityGroup.split(",")) {
                idsEntityGroupList.add(idEntity);
            }

        }

        PegasusServiceLocator.getDroitService().calculerDroit(idVersionDroit,
                IPCDroits.CS_DROIT_TYPE_DE_CALCUL_STANDARD.equals(typeDeCalcule), idsEntityGroupList);

    }

    /**
     * Va définir si le calcul mois suivant doit être bloqué selon les condition suivantes:<br/>
     * - la donnees financieres doit etre dans un etat periodeClose<br/>
     * - la date de fin de la df doit être egal à la date du prochain paiement (cloture mois courant)<br/>
     * 
     * @throws PmtMensuelException
     *             exception lancée du à l'appel du service de paiement mensuel (récupération date dernier paiement)
     * @throws JadeApplicationServiceNotAvailableException
     *             exception lancée par le fw lors de l'appels aux services
     */
    private void checkIsDFOnlyClorePriode() throws PmtMensuelException, JadeApplicationServiceNotAvailableException {

        String moisCourant = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();

        isCMSBloqueDFClorePeriode = true;

        for (CalculMoisSuivant cms : listeDFModifieForVersion) {
            // si la df n'est pas en clore periode, ou que la date de fin n'est pas egal au mois courant arret du test
            // on break
            if (!cms.getIsPeriodeClose() || !cms.getDateFinDonneeFinanciere().equals(moisCourant)) {
                isCMSBloqueDFClorePeriode = false;
                break;
            }

        }

    }

    private void checkWichTypeCalculAllowed() throws Exception {
        String dateMoisSuivant = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();

        // Blocage mois suivant si demande close
        if (!JadeStringUtil.isBlank(droit.getDemande().getSimpleDemande().getDateFin())) {
            isCMSBlockedDemandeClose = true;
        } else {
            // ************************* calcul standard ********************
            // Si la liste des df modifie est vide, calcul standard bloque pas de df
            if (listeDFModifieForVersion.size() == 0) {
                isCalculStandardBloqueNonDFModif = true;
            } else {
                // Si aucune date de début des données financières de la version est antérieur le calcul standard n'est
                // pas
                // autorisé
                boolean hasPeriodBefore = false;
                for (CalculMoisSuivant cms : listeDFModifieForVersion) {
                    if (!hasPeriodBefore) {
                        hasPeriodBefore = JadeDateUtil.isDateBefore("01." + cms.getDateDebutDonneeFinanciere(), "01."
                                + dateMoisSuivant);
                    }
                }
                isCalculStandardBloqueNonDFPlageCalcul = !hasPeriodBefore;
            }
        }

        if (IPCDemandes.CS_REOUVERT.equals(droit.getDemande().getSimpleDemande().getCsEtatDemande())) {
            String[] dates = PegasusServiceLocator.getDroitService().calculDroitPlageCalcul(getDroit(),
                    !isCalculStandardBloqueNonDFModif);

            String dateDebut = dates[0];
            if (dateDebut != null) {
                int annee = Integer.valueOf(dateDebut.substring(6));
                PCAccordee pca = PegasusServiceLocator.getPCAccordeeService().findLastestPca(
                        droit.getDemande().getSimpleDemande().getIdDemande());
                if (pca != null) {
                    int anneeDemande = Integer.valueOf(pca.getSimplePCAccordee().getDateDebut().substring(3));
                    if (annee > anneeDemande) {
                        isCalculInterdit = true;
                    }
                }
            }
        }

        String dateProchainPaiement = getDateProchainPaiement();
        // l'option effet mois suivant est bloqué quand :
        // - le prochain mois est le mois de janvier
        // - la validation des décisions de l'adaptation annuelle n'est pas pas validé
        // - la pca n'est pas retroactive
        isEffetMoisSuivantBloque = dateProchainPaiement.startsWith("01")
                && isNextValidationDecisionNotValidate(dateProchainPaiement) && isNotRetroactive();

        boolean hasDateDebutHomSameHasDateProchainPmt = false;

        if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()) {
            TaxeJournaliereHomeSearch search = new TaxeJournaliereHomeSearch();
            search.setForDateDebut(dateMoisSuivant);
            search.setForIdVersionDroit(droit.getSimpleVersionDroit().getIdVersionDroit());
            search.setForLessDateEntreeHome("01." + dateMoisSuivant);
            int nb = PegasusServiceLocator.getTaxeJournaliereHomeService().count(search);
            if (nb > 0) {
                hasDateDebutHomSameHasDateProchainPmt = true;
            }
        }
        isCalculBloqueDuAujourDappoint = hasDateDebutHomSameHasDateProchainPmt;

        // si aucun calcul n'est bloqué on check l'etat clore periode des donnes financiere
        if (aucunTypeCalculBloque()) {
            checkIsDFOnlyClorePriode();
        }

    }

    private boolean isNotRetroactive() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        PCAccordee pca = PegasusServiceLocator.getPCAccordeeService().findLastestPca(
                droit.getDemande().getSimpleDemande().getIdDemande());
        if (pca != null) {
            return pca.getSimplePCAccordee().getDateFin() == null || pca.getSimplePCAccordee().getDateFin().isEmpty();
        }
        return true;
    }

    public String getMotifDroitAdapatation() {
        return IPCDroits.CS_MOTIF_DROIT_ADAPTATION;
    }

    @Override
    public void delete() throws Exception {

    }

    public CalculException getCause() {
        return cause;
    }

    public String getDateDebutPlageCalcul() throws PmtMensuelException, JadeApplicationServiceNotAvailableException {
        return PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
    }

    public String getDateProchainPaiement() throws Exception {
        return PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
    }

    private boolean isNextValidationDecisionNotValidate(String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PegasusServiceLocator.getDecisionService().isAdaptationAnnuelleNotValidate(date);
    }

    public String getdFForVersionListHtml() {
        return dFForVersionListHtml;
    }

    public String getDFForVersionListHtml() {
        return dFForVersionListHtml;
    }

    public ArrayList<CalculMoisSuivant> getDfRetroDfList() {
        return dfRetroDfList;
    }

    /**
     * Retourne la liste sous forme de chaine des id des EntityGroup des données financièresc crées ou modifiées Utilisé
     * pour setter le champ dans l'écran
     * 
     * @return
     */
    public String getDonneesFinancieresForVersionAsString() {
        return StringUtils.join(donneesFinancieresForVersion, ",");
    }

    public Droit getDroit() {
        return droit;
    }

    @Override
    public String getId() {
        return idVersionDroit;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsDfModifForVersion() {
        return isDfModifForVersion;
    }

    public List<CalculMoisSuivant> getListeDFModifieForVersion() {
        return listeDFModifieForVersion;
    }

    public String getMessageConfirmCalculMoisSuivants() throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {
        String mess = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_CMS_CONFIRM_CONTENT");
        return PRStringUtils.replaceString(mess, "$mois", PegasusServiceLocator.getPmtMensuelService()
                .getDateProchainPmt());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getTypeDeCalcule() {
        return typeDeCalcule;
    }

    public boolean isCalculBloqueDuAujourDappoint() {
        return isCalculBloqueDuAujourDappoint;
    }

    public boolean isCalculStandardBloqueNonDFModif() {
        return isCalculStandardBloqueNonDFModif;
    }

    public boolean isCalculStandardBloqueNonDFPlageCalcul() {
        return isCalculStandardBloqueNonDFPlageCalcul;
    }

    public boolean isCMSBlockedDemandeClose() {
        return isCMSBlockedDemandeClose;
    }

    public Boolean isCMSBlockedDuToError() {
        return isCMSBlockedDuToError;
    }

    public boolean isCMSBloqueDFClorePeriode() {
        return isCMSBloqueDFClorePeriode;
    }

    @Override
    public void retrieve() throws Exception {
        // load droit
        DroitSearch droitSearch = new DroitSearch();

        droitSearch.setForIdVersionDroit(idVersionDroit);
        droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        if (droitSearch.getSize() == 1) {
            setDroit((Droit) droitSearch.getSearchResults()[0]);
        } else {
            throw new DroitException("can't retrieve droit. " + droitSearch.getSize()
                    + " element(s) was found for the same no version droit and id droit");
        }

        PegasusServiceLocator.getCalculMoisSuivantService().getCalculMoisSuivantBuilder();
        // Construction liste df
        CalculMoisSuivantBuilder.build(this);

        // Check etat blocage calculs
        checkWichTypeCalculAllowed();

    }

    public void setCalculStandardBloqueNonDFModif(boolean isCalculStandardBloqueNonDFModif) {
        this.isCalculStandardBloqueNonDFModif = isCalculStandardBloqueNonDFModif;
    }

    public void setCalculStandardBloqueNonDFPlageCalcul(boolean isCalculStandardBloqueNonDFPlageCalcul) {
        this.isCalculStandardBloqueNonDFPlageCalcul = isCalculStandardBloqueNonDFPlageCalcul;
    }

    public void setCause(CalculException cause) {
        this.cause = cause;
    }

    public void setCMSBlocedkDuToError(boolean isCalculMoisSuivantAllowed) {
        isCMSBlockedDuToError = isCalculMoisSuivantAllowed;
    }

    public void setCMSBlockedDemandeClose(boolean isCMSBlockedDemandeClose) {
        this.isCMSBlockedDemandeClose = isCMSBlockedDemandeClose;
    }

    public void setdFForVersionListHtml(String dFForVersionListHtml) {
        this.dFForVersionListHtml = dFForVersionListHtml;
    }

    public void setDfRetroDfList(ArrayList<CalculMoisSuivant> dfRetroDfList) {
        this.dfRetroDfList = dfRetroDfList;
    }

    public void setDonneesFinancieresForVersion(List<String> donneesFinancieresForVersion) {
        this.donneesFinancieresForVersion = donneesFinancieresForVersion;
    }

    public void setDroit(Droit droit) {
        this.droit = droit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        idVersionDroit = newId;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * ON set les idsEntityGroup, provenant de l'écran
     * 
     * @param idsEntityGroup
     */
    public void setIdsEntityGroup(String idsEntityGroup) {
        this.idsEntityGroup = idsEntityGroup;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsDfModifForVersion(Boolean isDfModifForVersion) {
        this.isDfModifForVersion = isDfModifForVersion;
    }

    public void setListeDFModifieForVersion(List<CalculMoisSuivant> listeDFModifieForVersion) {
        this.listeDFModifieForVersion = listeDFModifieForVersion;
    }

    public void setTypeDeCalcule(String typeDeCalcule) {
        this.typeDeCalcule = typeDeCalcule;
    }

    @Override
    public void update() throws Exception {

    }

    public boolean isCalculInterdit() {
        return isCalculInterdit;
    }

    public boolean isEffetMoisSuivantBloque() {
        return isEffetMoisSuivantBloque;
    }

    public void setEffetMoisSuivantBloque(boolean isEffetMoisSuivantBloque) {
        this.isEffetMoisSuivantBloque = isEffetMoisSuivantBloque;
    }

}
