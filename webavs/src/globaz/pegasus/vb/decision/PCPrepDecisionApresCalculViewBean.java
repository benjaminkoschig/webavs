/**
 * 
 */
package globaz.pegasus.vb.decision;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.AmalUtilsForDecisionsPC;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.services.AmalInterApplicationServiceLocator;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeIdMembresRetenus;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeIdMembresRetenusSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author SCE
 * 
 *         27 juil. 2010
 */
public class PCPrepDecisionApresCalculViewBean extends PCPrepDecisionAbstractViewBean {

    private String dateDernierPaiement = null;

    private DecisionApresCalcul decisionApresCalcul = null;

    private String idDroit = null;

    private String idVersionDroit = null;

    private Boolean isAmalIncoherent = null;

    private Boolean isCalculRetro = false;

    private Boolean isCourantReady = false;

    private Boolean isRetroReady = false;

    private String listeGenrePrep = IPCDecision.CS_PREP_STANDARD; // On force le type de préparation à standard porvisoirement, les autres possibilité n'on jamais été testées.
    private String noVersion = null;

    private PCAccordeeSearch pcAccordeeSearchDac = null;

    /**
     * Constructeur simple
     */
    /**
     * @throws Exception
     * 
     */
    public PCPrepDecisionApresCalculViewBean() throws Exception {
        super();
        setDecisionApresCalcul(new DecisionApresCalcul());
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    /**
     * Retourne le message amal en fomnction de l'état, vide si pas de soucis
     * 
     * @return
     * @throws PropertiesException
     */
    public String getAmalWarning() throws PropertiesException {

        if (PCproperties.getBoolean(EPCProperties.CHECK_AMAL_FOR_DECISION_ENABLE)) {
            // messahe amal
            if (AmalUtilsForDecisionsPC.getAmalIncoherenceDisplayMessage() != null) {
                return AmalUtilsForDecisionsPC.getAmalIncoherenceDisplayMessage();
            } else if (AmalUtilsForDecisionsPC.getAmalTechnicalErrorDisplayMessage() != null) {
                return AmalUtilsForDecisionsPC.getAmalTechnicalErrorDisplayMessage();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Retourne la date courant au format Globaz
     * 
     * @return
     */
    public String getDateNow() {
        String globazFormattedDate = JadeDateUtil.getGlobazFormattedDate(new Date());
        if (JadeDateUtil.isDateBefore(globazFormattedDate, dateDernierPaiement)) {
            globazFormattedDate = dateDernierPaiement;
        }
        return globazFormattedDate;
    }

    /**
     * @return the decisionApresCalcul
     */
    public DecisionApresCalcul getDecisionApresCalcul() {
        return decisionApresCalcul;
    }

    @Override
    public String getId() {
        return decisionApresCalcul.getId();
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsAmalIncoherent() throws Exception {
        if ((isAmalIncoherent != null)
                && Boolean.parseBoolean(getSession().getApplication().getProperty(
                        "pegasus.amal.check.blockifincomplete"))) {
            return isAmalIncoherent;
        } else {
            return false;
        }
    }

    public Boolean getIsAmalOnError() {
        return AmalUtilsForDecisionsPC.getIsAmalOnError();
    }

    public Boolean getIsCalculRetro() {
        return isCalculRetro;
    }

    private Map<String, Map<String, List<SimpleDetailFamille>>> getListeAmal() throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PropertiesException {

        // si amal check pas actif on retourne null
        if (!PCproperties.getBoolean(EPCProperties.CHECK_AMAL_FOR_DECISION_ENABLE)) {
            return null;
        }

        // Recherche des pca avec les infos des membres familles
        PCAccordeeIdMembresRetenusSearch pcaMembreRetenusSearch = new PCAccordeeIdMembresRetenusSearch();
        pcaMembreRetenusSearch.setForIdVersionDroit(idVersionDroit);
        pcaMembreRetenusSearch = PegasusServiceLocator.getPCAccordeeService().search(pcaMembreRetenusSearch);

        // Map group by
        List<PCAccordeeIdMembresRetenus> list = PersistenceUtil.typeSearch(pcaMembreRetenusSearch,
                PCAccordeeIdMembresRetenus.class);
        // group-by periode
        Map<String, List<PCAccordeeIdMembresRetenus>> periodForTiers = JadeListUtil.groupBy(list,
                new Key<PCAccordeeIdMembresRetenus>() {
                    @Override
                    public String exec(PCAccordeeIdMembresRetenus e) {
                        return e.getSimplePCAccordee().getDateDebut();
                    }
                });

        // map pour amal
        Map<String, List<String>> mapAmal = new HashMap<String, List<String>>();

        // iteration sur les périodes pour construction map amal
        for (String dateDebut : periodForTiers.keySet()) {
            List<String> idTiers = new ArrayList<String>();
            // (Iteration sur les idTiers
            for (PCAccordeeIdMembresRetenus membreRetenus : periodForTiers.get(dateDebut)) {

                idTiers.add(membreRetenus.getMembreFamille().getMembreFamille().getPersonneEtendue().getTiers()
                        .getIdTiers());
            }
            mapAmal.put(dateDebut, idTiers);
        }

        Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal = null;
        // Gestion exception amal
        try {
            // Récupération de la liste amal
            listeAmal = AmalInterApplicationServiceLocator.getPCCustomerService().getAmalSubsidesByPeriodes(mapAmal);
        } catch (Exception ex) {
            JadeLogger.error(null, "Problem during checking coherrence with subside amal");

        }
        return listeAmal;
    }

    public String getListeGenrePrep() {
        return listeGenrePrep;
    }

    public String getMailGestionnaire(BSession session) {
        return session.getUserEMail();
    }

    /**
     * @return the noVersionDroit
     */
    public String getNoVersion() {
        return noVersion;
    }

    /**
     * Retourne le modele complex de la personne
     * 
     * @return personneComplexModel
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return decisionApresCalcul.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                .getPersonneEtendue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(getDecisionApresCalcul().getSimpleDecisionApresCalcul().getSpy());
    }

    /**
     * Retourne le Tiers
     * 
     * @return
     */
    public TiersSimpleModel getTiers() {
        return decisionApresCalcul.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                .getPersonneEtendue().getTiers();
    }

    public Boolean isDecisionComplete() {
        return isRetroReady && isCourantReady;
    }

    public Boolean isVersionInitial() throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        return "1".equals(getNoVersion())
                && PegasusImplServiceLocator.getSimpleDemandeService().isDemandeInitial(
                        decisionApresCalcul.getVersionDroit().getDemande().getSimpleDemande(),
                        decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getDateDebut());
    }

    public void preparer() throws PCAccordeeException, JadeApplicationServiceNotAvailableException, DecisionException,
            JadePersistenceException, Exception {

        // Recheche des PCAccordées pour la version du droit sans idParent setter
        pcAccordeeSearchDac = new PCAccordeeSearch();
        pcAccordeeSearchDac.setForVersionDroit(decisionApresCalcul.getVersionDroit().getSimpleVersionDroit()
                .getIdVersionDroit());
        pcAccordeeSearchDac.setOrderKey("byDateDebut");
        pcAccordeeSearchDac.setWhereKey("forCreateDAC");
        pcAccordeeSearchDac = PegasusServiceLocator.getPCAccordeeService().search(pcAccordeeSearchDac);

        decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().setPreparationPar(getCurrentUserId());

        // this.decisionApresCalcul.getSimpleDecisionApresCalcul().setDateDecisionAmal(this.dateDecisionAmal);
        // Set date amal

        // Action selon choix type prep
        if (listeGenrePrep.equals(IPCDecision.CS_PREP_STANDARD)) {
            // Création
            PegasusServiceLocator.getDecisionApresCalculService().createStandardDecision(decisionApresCalcul,
                    pcAccordeeSearchDac, getListeAmal());
        }
        if (listeGenrePrep.equals(IPCDecision.CS_PREP_COURANT)) {
            // Création
            PegasusServiceLocator.getDecisionApresCalculService().createCourantDecision(decisionApresCalcul,
                    pcAccordeeSearchDac, getListeAmal());
        }
        if (listeGenrePrep.equals(IPCDecision.CS_PREP_RETRO)) {
            // Création
            PegasusServiceLocator.getDecisionApresCalculService().createRetroDecision(decisionApresCalcul,
                    pcAccordeeSearchDac, getListeAmal());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        // recherche date dernier paiement
        dateDernierPaiement = "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();

        // Recherche drooit pour info tiers
        decisionApresCalcul.setVersionDroit(PegasusServiceLocator.getDroitService().readVersionDroit(
                getIdVersionDroit()));
        idDroit = decisionApresCalcul.getVersionDroit().getSimpleDroit().getId();
        noVersion = decisionApresCalcul.getVersionDroit().getSimpleVersionDroit().getNoVersion();
        // Recherche si decision apres calcul existante pour cette version du droit en courant --> pour retro
        DecisionApresCalculSearch decisionSearch = new DecisionApresCalculSearch();
        decisionSearch.setForIdVersionDroit(decisionApresCalcul.getVersionDroit().getSimpleVersionDroit()
                .getIdVersionDroit());

        decisionSearch.setForCsTypePreparation(IPCDecision.CS_PREP_COURANT);
        decisionSearch.setForCsEtatDecision(IPCDecision.CS_ETAT_DECISION_VALIDE);
        decisionSearch = PegasusServiceLocator.getDecisionApresCalculService().searchForDecisionCourant(decisionSearch);

        // si aucune decision liés, ok standrad et courant
        if (decisionSearch.getSearchResults().length == 0) {
            // Recherche si calcul retro
            SimplePCAccordeeSearch pcaSearch = new SimplePCAccordeeSearch();
            pcaSearch.setForIsCalculRetro(Boolean.FALSE);
            pcaSearch.setForIdVersionDroit(getIdVersionDroit());
            int result = PegasusServiceLocator.getSimplePcaccordeeService().count(pcaSearch);

            if (result == 0) {
                isCalculRetro = true;
                isRetroReady = true;
                isCourantReady = true;
            }
        } else {
            // iteration sur les décision
            for (JadeAbstractModel decision : decisionSearch.getSearchResults()) {
                isCalculRetro = true;
                // si decision courante ok, set etat
                if (((DecisionApresCalcul) decision).getSimpleDecisionApresCalcul().getCsTypePreparation()
                        .equals(IPCDecision.CS_PREP_COURANT)) {
                    isCourantReady = true;
                }
                // Si decision retro, ok set etat
                if (((DecisionApresCalcul) decision).getSimpleDecisionApresCalcul().getCsTypePreparation()
                        .equals(IPCDecision.CS_PREP_RETRO)) {
                    isRetroReady = true;
                }
                // Si decision etat standard set etat ok
                if (((DecisionApresCalcul) decision).getSimpleDecisionApresCalcul().getCsTypePreparation()
                        .equals(IPCDecision.CS_PREP_STANDARD)) {
                    isRetroReady = true;
                    isCourantReady = true;
                }
            }
        }

        // Si check amal
        if (Boolean.parseBoolean(getSession().getApplication().getProperty("pegasus.amal.check.enable"))) {
            // Check Amaml
            AmalUtilsForDecisionsPC.checkAndGenerateWarningCoherenceWithAmal(decisionApresCalcul.getVersionDroit()
                    .getSimpleVersionDroit().getIdVersionDroit(), getSession());
        }

    }

    /**
     * @param decisionApresCalcul
     *            the decisionApresCalcul to set
     */
    public void setDecisionApresCalcul(DecisionApresCalcul decisionApresCalcul) {
        this.decisionApresCalcul = decisionApresCalcul;
    }

    @Override
    public void setId(String newId) {
        getDecisionApresCalcul().getSimpleDecisionApresCalcul().setId(newId);
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsCalculRetro(Boolean isCalculRetro) {
        this.isCalculRetro = isCalculRetro;
    }

//    public void setListeGenrePrep(String listGenrePrep) {
//        listeGenrePrep = listGenrePrep;
//    }

    /**
     * @param noVersionDroit
     *            the noVersionDroit to set
     */
    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    @Override
    public void update() throws Exception {
    }

}
