package globaz.pegasus.vb.avance;

import globaz.corvus.db.avances.REAvance;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleDroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCAvanceViewBean extends BJadePersistentObjectViewBean {

    /** Avance considéré */
    private REAvance avance = null;
    /** Modele des infos pour le conjoint */
    private PersonneEtendueComplexModel conjointAsTiers = null;

    private Demande demande = null;

    private String ERROR_ADRESS_MESSAGE = null;
    private String idAvance = null;

    private String idDemande = null;
    private String idTiersConjoint = null;

    private String idTiersDemande = null;
    private String idTiersRequerant = null;

    /** Determine si une adresse de paiement valide à été trouvé au chargement de la page */
    private boolean isAdressePaiementFindOnLoad = true;

    /** Modele des infos pour le tiers, utilisé seulemetn si new */
    private PersonneEtendueComplexModel requerantAsTiers = null;

    public PCAvanceViewBean() {
        ERROR_ADRESS_MESSAGE = ((BSession) getISession()).getLabel("JSP_PC_ADRESSE_INTROUVABLE");
    }

    @Override
    public void add() throws Exception {
        PegasusServiceLocator.getAvanceService().createReAvance(avance);
    }

    /**
     * Construiit les infos de la personne pour la description dans le box de choix du bénéficiaire
     * 
     * @param pers
     * @return
     */
    private String buildInfoTiers(PersonneEtendueComplexModel pers) {

        String descriptionPersonne = null;

        if (pers != null) {
            descriptionPersonne = pers.getTiers().getDesignation1() + " " + pers.getTiers().getDesignation2() + " / "
                    + pers.getPersonneEtendue().getNumAvsActuel();
        }

        return descriptionPersonne;
    }

    /**
     * Formattage du bloc ad'adresse ajouté vi js (choix req/con)
     * 
     * @param adresse
     * @return
     */
    public String decodeForJs(String adresse) {
        String ret = PRStringUtils.replaceString(adresse, "\n", "<br />");
        return ret;
    }

    /**
     * Supression d'une entité
     */

    @Override
    public void delete() throws Exception {
        PegasusServiceLocator.getAvanceService().deleteReAvance(avance);
    }

    /**
     * Retourne l'adresse de paiement du bénéficiaire de l'avance, 2 cas de figure:
     * 
     * - new, retourne l'adresse de paiement du requérant de la demande
     * - exsistante, retourne l'adresse de paiement du beneficiare de l'avance
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiementBeneficiaire() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        String idTiers = null;
        String domaineAdresse = null;

        // Si avance on utilise l'id tiers et le domaine de l'avance,
        if (idAvance != null) {
            idTiers = avance.getIdTiersAdrPmt();
            domaineAdresse = avance.getCsDomaine();
        }
        // Sinon on prend l'id tiers passé en paramètre, idTiers de la demande et le domaine par defaut
        else {
            idTiers = idTiersDemande;
            domaineAdresse = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;
        }

        return getDetailTiersAdressePaiement(idTiers, domaineAdresse);

    }

    /**
     * Retourne l'adresse de paiement du conjoint, si existant, afin de stcoker dans variable js
     * Pour afficheg dans initialisation de la page
     * Avec domaine rente par défaut
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiementConjoint() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        // Si pas de conjoint, retourne chaine vide, sans importance, utilisé uniquement pour affichageinitiale de
        // l'adresse, et switch entre conjoitn et requerant (bouton radio)
        if (conjointAsTiers != null) {
            return getDetailTiersAdressePaiement(conjointAsTiers.getTiers().getIdTiers(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        } else {
            return "[NO CONJOINT]";
        }
    }

    /**
     * Retourne l'adresse de paiement du requerant, afin de stcoker dans variable js
     * Pour afficheg dans initialisation de la page
     * Avec domaine rente par défaut
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiementRequerant() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        return getDetailTiersAdressePaiement(requerantAsTiers.getTiers().getIdTiers(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
    }

    public REAvance getAvance() {
        return avance;
    }

    public PersonneEtendueComplexModel getConjointAsTiers() {
        return conjointAsTiers;
    }

    /**
     * Retourne le domaine de l' adresse paiement, deux cas de figures:
     * - new, on retourne, le cd domaine rebtes
     * - existante, le domaine de l'avance
     * 
     * @return
     */
    public String getCsDomaineAdressePaiement() {
        if (idAvance != null) {
            return avance.getCsDomaine();
        } else {
            return IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;
        }
    }

    /**
     * Retourne le detail d'une adresse de paiement d'un tiers sur la base de l'idTiers passé en paramètre, et du
     * domaine d'adresse passé en paramètre
     * 
     * @param idTiers
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private String getDetailTiersAdressePaiement(String idTiers, String domaineAdresse)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        // modèle
        AdresseTiersDetail detailTiers = null;
        // appel du service
        detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, Boolean.TRUE,
                domaineAdresse, JACalendar.todayJJsMMsAAAA(), null);
        // construction des détails
        String adresse = "";

        // Si adresse trouvé ok
        if ((detailTiers != null) && (detailTiers.getAdresseFormate() != null)) {
            adresse = detailTiers.getAdresseFormate();
            adresse = adresse.replace("\"", "&#34;").replace("'", "&#39;");
        } else {
            isAdressePaiementFindOnLoad = false;
            adresse = ERROR_ADRESS_MESSAGE;
        }

        return adresse;
    }

    /**
     * retourne le titre du détail
     * 
     * @return
     */
    public String getDetailTitle() {

        String csDomaineAvance = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_AVANCE_D_TITRE");

        if (!JadeStringUtil.isBlank(avance.getCsDomaineAvance())) {
            csDomaineAvance += (" - " + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                    avance.getCsDomaineAvance()));
        }

        return csDomaineAvance;
    }

    @Override
    public String getId() {
        return idAvance;
    }

    public String getIdAvance() {
        return idAvance;
    }

    public String getIdDemande() {
        return idDemande;
    }

    /**
     * Retourne l'id tiers adresse paiement, deux casde figures:
     * - new, on retourne, l'idTiers adresse paiement du requérant
     * - existante, l'idTiers adresse paiement de la demande
     * 
     * @return
     */
    public String getIdTiersAdressePaiement() {
        if (idAvance != null) {
            return avance.getIdTiersAdrPmt();
        } else {
            return idTiersDemande;
        }
    }

    public String getIdTiersBeneficiaire() {
        if (idAvance != null) {
            return avance.getIdTiersBeneficiaire();
        } else {
            return idTiersDemande;
        }
    }

    public String getIdTiersConjoint() {
        if (idTiersConjoint != null) {

            return idTiersConjoint;
        } else {
            return "";
        }
    }

    public String getIdTiersDemande() {
        return idTiersDemande;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    /**
     * Retourn la description du conjoint si il y a
     * 
     * @return
     */
    public String getInfoConjointAsBeneficiaire() {
        if (conjointAsTiers != null) {
            return buildInfoTiers(conjointAsTiers);
        }
        return null;
    }

    /**
     * retourne la ligne de description du bénéficiare requerant, par défaut
     * 
     * @return
     */
    public String getInfoRequerantAsBeneficiaire() {
        String infoRequerant = null;

        infoRequerant = buildInfoTiers(requerantAsTiers);

        return infoRequerant;
    }

    public String getLibelle(String idCode) {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(idCode);
    }

    public PersonneEtendueComplexModel getRequerantAsTiers() {
        return requerantAsTiers;
    }

    @Override
    public BSpy getSpy() {
        return avance.getSpy();
    }

    /**
     * Initialisation du viewbean dans la servlet, pour le new
     * 
     * @throws DemandeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public void initForNew() {

        // on charge la demande dans tous les cas
        try {
            loadDemande();
            // on set l'id tiers reçu en paramètre (reuérent de la demande)
            idTiersRequerant = idTiersDemande;
            // on recherche un éventuel conjoint
            searchConjoint();
            // on set pour les infos du tiers
            requerantAsTiers = demande.getDossier().getDemandePrestation().getPersonneEtendue();
            // on instancie le modele
            avance = new REAvance();
        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(toString(), e.getMessage());
            JadeLogger.error(this, e.getMessage());
        }

    }

    public boolean isAdressePaiementFindOnLoad() {
        return isAdressePaiementFindOnLoad;
    }

    /**
     * Detremine pour qui est l'avance, 2 cas de figure
     * - new, pour le requérant par défaut
     * - existante, pour le bànàficiaire de l'avance
     * 
     * @return
     */
    public boolean isForRequerant() {

        boolean isForRequerant = true;

        // Si pas new, onn test
        if (idAvance != null) {
            isForRequerant = (idTiersDemande.equals(avance.getIdTiersBeneficiaire()));
        }

        return isForRequerant;
    }

    /**
     * Chargement de la demande PC
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    private void loadDemande() throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        demande = PegasusServiceLocator.getDemandeService().read(idDemande);
    }

    public void PersonneEtendueComplexModel(PersonneEtendueComplexModel conjointAsTiers) {
        this.conjointAsTiers = conjointAsTiers;
    }

    @Override
    public void retrieve() throws Exception {

        // on charge la demande dans tous les cas
        loadDemande();

        // Id avance pas null, donc avance existante
        if (idAvance != null) {
            // chargement de l'avance
            avance = (REAvance) PegasusServiceLocator.getAvanceService().readAvance(idAvance);
            // on set le tiers
            // this.idTiersRequerant = this.avance.getIdTiersBeneficiaire();
            // this.id
            // on set pour les infos du tiers
            requerantAsTiers = demande.getDossier().getDemandePrestation().getPersonneEtendue();
            // on recherche le conjoint
            searchConjoint();

        }
    }

    /**
     * Recherche si un conjoitn existe pour cette demande
     * Si il n'y pas de droit, il n'existe pas
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     */
    public void searchConjoint() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        // chargement du droit
        SimpleDroitSearch droitSearch = new SimpleDroitSearch();
        droitSearch.setForIdDemandePC(idDemande);
        droitSearch = PegasusImplServiceLocator.getSimpleDroitService().search(droitSearch);

        // Si on a des résultats
        if (droitSearch.getSearchResults().length > 0) {

            // on recupère le 1er id droit
            String idDroit = ((SimpleDroit) droitSearch.getSearchResults()[0]).getIdDroit();

            // on recherche si conjoint il y a
            ArrayList<String> csConjoint = new ArrayList<String>();
            csConjoint.add(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);

            // load personnes
            DroitMembreFamilleSearch membreSearch = new DroitMembreFamilleSearch();
            membreSearch.setForCsRoletMembreFamilleIn(csConjoint);
            membreSearch.setForIdDroit(idDroit);
            membreSearch = PegasusServiceLocator.getDroitService().searchDroitMembreFamille(membreSearch);

            for (Iterator it = Arrays.asList(membreSearch.getSearchResults()).iterator(); it.hasNext();) {
                // on a bien un conjoint
                DroitMembreFamille membre = (DroitMembreFamille) it.next();
                // on set le tiers conjoint
                conjointAsTiers = membre.getMembreFamille().getPersonneEtendue();
                idTiersConjoint = membre.getMembreFamille().getPersonneEtendue().getTiers().getIdTiers();
            }
        }

    }

    public void setAvance(REAvance avance) {
        this.avance = avance;
    }

    @Override
    public void setId(String newId) {
        if (newId != null) {
            avance.setId(newId);
        }

    }

    public void setIdAvance(String idAvance) {
        this.idAvance = idAvance;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setIdTiersDemande(String idTiersDemande) {

        this.idTiersDemande = idTiersDemande;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setRequerantAsTiers(PersonneEtendueComplexModel requerantAsTiers) {
        this.requerantAsTiers = requerantAsTiers;
    }

    @Override
    public void update() throws Exception {
        PegasusServiceLocator.getAvanceService().updateReAvance(avance);
    }

}
