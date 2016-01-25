/**
 * 
 */
package globaz.pegasus.vb.decision;

import globaz.globall.db.BSpy;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author SCE
 * 
 *         20 juil. 2010
 */
public class PCPrepDecisionRefusViewBean extends PCPrepDecisionAbstractViewBean {

    // Code systeme motifs
    public static String CS_MOTIF = "PCMOTIFDER";
    // Liste motifs et sous motifs
    // private static String csCodesListe = null;

    private DecisionRefus decisionRefus = null;

    // id de la demande lié
    private String idDemande = null;

    /**
     * Constructeur simple
     * 
     * @throws Exception
     */
    public PCPrepDecisionRefusViewBean() throws Exception {
        super();
        setDecisionRefus(new DecisionRefus());
        // PCPrepDecisionRefusViewBean.csCodesListe = new PCDecisionHandler()
        // .createMotifsWithSousMotifs(PCPrepDecisionRefusViewBean.CS_MOTIF);

    }

    /**
     * Constructeur avec modèle en paramètre
     * 
     * @param monnaieEtrangere
     */
    public PCPrepDecisionRefusViewBean(DecisionRefus decisionRefus) {
        super();
        setDecisionRefus(decisionRefus);
        // PCPrepDecisionRefusViewBean.csCodesListe = new PCDecisionHandler()
        // .createMotifsWithSousMotifs(PCPrepDecisionRefusViewBean.CS_MOTIF);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the decision
     */
    public DecisionRefus getDecisionRefus() {
        return decisionRefus;
    }

    // public String getCsList() {
    // return PCPrepDecisionRefusViewBean.csCodesListe;
    // }

    /**
     * @return the dossier
     */
    public Demande getDemande() {
        return decisionRefus.getDemande();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return decisionRefus.getId();
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * Retourne le modele complex de la personne
     * 
     * @return personneComplexModel
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return decisionRefus.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(getDecisionRefus().getSimpleDecisionRefus().getSpy());
    }

    /**
     * Retourne le Tiers
     * 
     * @return
     */
    public TiersSimpleModel getTiers() {
        return decisionRefus.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers();
    }

    public String motifsHasSousMotifs() {
        return decisionRefus.getSimpleDecisionRefus().getCsSousMotif();
    }

    public void preparer() throws PCAccordeeException, JadeApplicationServiceNotAvailableException, DecisionException,
            JadePersistenceException, Exception {
        // Set preparation par
        decisionRefus.getDecisionHeader().getSimpleDecisionHeader().setPreparationPar(getCurrentUserId());
        // Création
        decisionRefus = PegasusServiceLocator.getDecisionRefusService().create(decisionRefus);

    }

    public String getCsCodeRenonciation() {
        return IPCDecision.CS_MOIF_REFUS_RENONCIATION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        decisionRefus.setDemande(PegasusServiceLocator.getDemandeService().read(getIdDemande()));
    }

    /**
     * @param decision
     *            the decision to set
     */
    public void setDecisionRefus(DecisionRefus decisionRefus) {
        this.decisionRefus = decisionRefus;
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDemande(Demande demande) {
        decisionRefus.setDemande(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        decisionRefus.setId(newId);
    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
