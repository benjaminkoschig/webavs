package globaz.pegasus.vb.demande;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.hera.business.models.famille.MembreFamille;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.demande.SimpleDemandeChecker;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import com.google.gson.Gson;

public class PCDemandeDetailViewBean extends BJadePersistentObjectViewBean {

    private Demande demande = null;
    private boolean actionRouvrirDemande = false;
    private boolean isRouvrirPossible = false;
    private boolean isOnlyRetro;
    private boolean actionRefermerDemande = false;
    private Boolean annule = false;
    private Boolean isDateReduc = false;
    private Boolean comptabilisationAuto = false;
    private Map<String, String> membresFamille = new HashMap<String, String>();
    private Boolean forcerAnnulerActif;

    public PCDemandeDetailViewBean() {
        super();
        demande = new Demande();

        // TODO faire plus proprement gestion des fratries lors de la création d'une demande sans fratrie
        demande.getSimpleDemande().setIsFratrie(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        demande = PegasusServiceLocator.getDemandeService().create(demande);
    }

    public Map<String, String> getMembresFamille() {
        return membresFamille;
    }

    public String getMembresFamilleJson() {
        Gson gson = new Gson();
        return gson.toJson(membresFamille).replace("\"", "'");

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        demande = PegasusServiceLocator.getDemandeService().delete(demande);
    }

    /**
     * @return the dateArrivee
     */
    public String getDateArrivee() {
        return demande.getSimpleDemande().getDateArrivee();
    }

    /**
     * @return the dateDepot
     */
    public String getDateDepot() {
        return demande.getSimpleDemande().getDateDepot();
    }

    public Demande getDemande() {
        return demande;
    }

    @Override
    public String getId() {
        return demande.getSimpleDemande().getIdDemande();
    }

    /**
     * Retourne l'id de la decision de refu liee ou 0 si pas de decision de refu
     * 
     * @return
     */
    public String getIdDecisionRefus() {
        if (getDemande().getSimpleDecisionRefus() != null) {
            return getDemande().getSimpleDecisionRefus().getIdDecisionHeader();
        } else {
            return "0";
        }
    }

    public String getPeriode() {
        String dDeb = demande.getSimpleDemande().getDateDebut() != null ? demande.getSimpleDemande().getDateDebut()
                : "";
        String dFin = demande.getSimpleDemande().getDateFin() != null ? demande.getSimpleDemande().getDateFin() : "";

        return dDeb + " - " + dFin;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(demande.getSimpleDemande().getSpy());
    }

    public String getDateDebut() {
        return demande.getSimpleDemande().getDateDebut();
    }

    /**
     * Return true si il existe une decision de refus pour cette demande (idDecisionRefus != 0)
     * 
     * @return
     */
    public boolean hasDecisionDerefus() {
        // if (IPCDemandes.CS_REFUSE.equals(this.demande.getSimpleDemande().getCsEtatDemande())) {
        // return true;
        // } else {
        // return false;
        // }

        return !JadeStringUtil.isBlankOrZero(getIdDecisionRefus());
    }

    /**
     * @return
     */
    public Boolean isFratrie() {
        if (demande.getSimpleDemande().isNew()) {
            return false;
        } else {
            return demande.getSimpleDemande().getIsFratrie();
        }

    }

    /**
     * Return true si la demande est transférable (demande dans les etats EN_ATTENTE_CALCUL ou EN_ATTENTE_JUSTIFICATIF)
     * 
     * @return
     */
    public boolean isTransferable() {
        return IPCDemandes.CS_EN_ATTENTE_CALCUL.equals(getDemande().getSimpleDemande().getCsEtatDemande())
                || IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS.equals(getDemande().getSimpleDemande().getCsEtatDemande());
    }

    /**
     * Retourne true si une décsion de refus peut être préparée (demande dans les etats EN_ATTENTE_CALCUL ou
     * EN_ATTENTE_JUSTIFICATIF) et pas de décisions de refus deja creee
     * 
     * @return
     */
    public boolean isValidForPrepDecisionRefus() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return (IPCDemandes.CS_EN_ATTENTE_CALCUL.equals(getDemande().getSimpleDemande().getCsEtatDemande()) || IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS
                .equals(getDemande().getSimpleDemande().getCsEtatDemande())) && !hasDecisionDerefus();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        demande = PegasusServiceLocator.getDemandeService().read(demande.getId());
        isOnlyRetro = SimpleDemandeChecker.isPossibleToCreateNewDeamande(demande.getDossier().getId());
        isRouvrirPossible = PegasusServiceLocator.getDemandeService().isDemandeReouvrable(demande);

        DroitMembreFamilleEtenduSearch membreSearch = new DroitMembreFamilleEtenduSearch();
        membreSearch.setForIdDemande(demande.getId());
        membreSearch = PegasusServiceLocator.getDroitService().searchDroitMemebreFamilleEtendu(membreSearch);

        for (JadeAbstractModel model : membreSearch.getSearchResults()) {
            DroitMembreFamilleEtendu membreFamille = (DroitMembreFamilleEtendu) model;
            MembreFamille tiers = membreFamille.getDroitMembreFamille().getMembreFamille();
            membresFamille.put(tiers.getPersonneEtendue().getPersonne().getIdTiers(),
                    tiers.getNom() + " - " + tiers.getPrenom());
        }
        forcerAnnulerActif = PCproperties.getBoolean(EPCProperties.AFFICHAGE_FORCER_ANNULER);

    }

    public boolean isRouvrirPossible() {
        return isRouvrirPossible;
    }

    /**
     * @param dateArrivee
     *            the dateArrivee to set
     */
    public void setDateArrivee(String dateArrivee) {
        demande.getSimpleDemande().setDateArrivee(dateArrivee);
    }

    /**
     * @param dateDepot
     *            the dateDepot to set
     */
    public void setDateDepot(String dateDepot) {
        demande.getSimpleDemande().setDateDepot(dateDepot);
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String newId) {
        demande.getSimpleDemande().setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (actionRouvrirDemande) {
            demande = PegasusServiceLocator.getDemandeService().rouvrir(demande);
        } else if (actionRefermerDemande) {
            demande = PegasusServiceLocator.getDemandeService().reFermer(demande);
        } else if (annule && !IPCDemandes.CS_ANNULE.equals(demande.getSimpleDemande().getCsEtatDemande())) {
            demande = PegasusServiceLocator.getDemandeService().annuler(demande, comptabilisationAuto);
        } else if (!annule && IPCDemandes.CS_ANNULE.equals(demande.getSimpleDemande().getCsEtatDemande())) {
            demande = PegasusServiceLocator.getDemandeService().retourArriereAnnuler(demande);
        } else if (!JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFinInitial())) {
            demande = PegasusServiceLocator.getDemandeService().dateReduction(demande, comptabilisationAuto);
        } else {
            demande = PegasusServiceLocator.getDemandeService().update(demande);
        }
    }

    public Boolean getForcerAnnulerActif() {
        return forcerAnnulerActif;
    }

    public boolean getActionRouvrirDemande() {
        return actionRouvrirDemande;
    }

    public boolean getActionRefermerDemande() {
        return actionRefermerDemande;
    }

    public void setActionRefermerDemande(boolean actionRefermerDemande) {
        this.actionRefermerDemande = actionRefermerDemande;
    }

    public void setActionRouvrirDemande(boolean actionRouvrirDemande) {
        this.actionRouvrirDemande = actionRouvrirDemande;
    }

    public boolean getIsOnlyRetro() {
        return isOnlyRetro;
    }

    public void setIsOnlyRetro(boolean only) {
        isOnlyRetro = only;
    }

    public String getDomainePegasus() {
        return EcheanceDomaine.PEGASUS.toString();
    }

    public String getTypeEcheance() {
        return EcheanceType.DEMANDE.toString();
    }

    public boolean getAnnule() {
        return annule;
    }

    public void setAnnule(boolean annule) {
        this.annule = annule;
    }

    public Boolean getComptabilisationAuto() {
        return comptabilisationAuto;
    }

    public void setComptabilisationAuto(Boolean comptabilisationAuto) {
        this.comptabilisationAuto = comptabilisationAuto;
    }

    public void setDateReduc(String dateReduc) {
        if (JadeStringUtil.isBlankOrZero(demande.getSimpleDemande().getDateFinInitial())) {
            demande.getSimpleDemande().setDateFinInitial(dateReduc);
        }
    }

    public String getDateReduc() {
        return demande.getSimpleDemande().getDateFinInitial();
    }

    public Boolean getIsDateReduc() {
        return isDateReduc;
    }

    public void setIsDateReduc(Boolean isDateReduc) {
        this.isDateReduc = isDateReduc;
    }
}
