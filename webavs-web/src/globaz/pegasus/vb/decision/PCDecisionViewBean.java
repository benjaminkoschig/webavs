/**
 * 
 */
package globaz.pegasus.vb.decision;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.decision.ListDecisions;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author SCE
 * 
 *         14 juil. 2010
 */
public class PCDecisionViewBean extends BJadePersistentObjectViewBean {

    // instance de la classe métier
    private ListDecisions decision = null;
    private String idDecision = null;
    private String idDemande = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private String noVersion = null;

    /**
     * Constructeur simple
     */
    public PCDecisionViewBean() {
        super();
        decision = new ListDecisions();

    }

    /**
     * Constructeur avec moddèle en paramètre
     * 
     * @param monnaieEtrangere
     */
    public PCDecisionViewBean(ListDecisions decision) {
        super();
        this.decision = decision;

        // Si decision de refus
        if (isDecisionRefus(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())) {

            idDemande = this.decision.getIdDemandePc();
        }

        // Si decision de suppression
        if (isDecisionSuppression(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())) {

            idDroit = this.decision.getIdDroitSup();
            idVersionDroit = this.decision.getIdVersionDroitSup();
            noVersion = this.decision.getNoVersionDroitSup();
            idDemande = this.decision.getSimpleDroit().getIdDemandePC();
        }

        // Si decision apres calcul
        if (this.isDecisionAC(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())) {
            idDroit = this.decision.getIdDroitApc();
            idVersionDroit = this.decision.getIdVersionDroitApc();
            noVersion = this.decision.getNoVersionDroitApc();
            idDemande = this.decision.getSimpleDroit().getIdDemandePC();
        }
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
    }

    public String getDateDecision() {
        return decision.getDecisionHeader().getSimpleDecisionHeader().getDateDecision();
    }

    /**
     * @return the decision
     */
    public ListDecisions getDecision() {
        return decision;
    }

    public String getDecisionAction() throws JadePersistenceException {
        String type = getDecision().getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision();

        // Decision de refus
        if (isDecisionRefus(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())) {

            return IPCActions.ACTION_DECISION_DETAIL_REFUS;
        }
        // Decision de suppression
        if (isDecisionSuppression(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())) {
            return IPCActions.ACTION_DECISION_DETAIL_SUPPRESSION;
        }
        // Decision apres calcul
        if (this.isDecisionAC(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())) {
            return IPCActions.ACTION_DECISION_DETAIL_AP_CALCUL;
        }

        return null;
    }

    // public String getDecLie() {
    // // Si la decision contient un id de decision lié(conjoint)
    // if (!JadeStringUtil
    // .isBlank(this.decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionConjoint())) {
    // return this.decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionConjoint();
    // } else {
    // return "0";
    // }
    // }

    public String getDecisionCSType() {
        return decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision();
    }

    /**
     * Retourne l'id de l'entité
     * 
     * @return id
     */
    @Override
    public String getId() {
        return decision.getId();
    }

    /**
     * @return the idDecision
     */
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
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

    public String getMontantPC() {

        if (!JadeStringUtil.isBlankOrZero(decision.getMontantPC())) {
            // Si tiers egal, c'est le requérant, on retourne le montant de la pc
            if (decision.getSimplePrestation().getIdTiersBeneficiaire()
                    .equals(decision.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire())) {
                return decision.getSimplePrestation().getMontantPrestation();
            } else {
                Float montantConjoint = Float.parseFloat(decision.getMontantPC())
                        - Float.parseFloat(decision.getSimplePrestation().getMontantPrestation());
                return montantConjoint.toString();
            }
        } else {
            return "";
        }

    }

    /**
     * @return the noVersion
     */
    public String getNoVersion() {
        return noVersion;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * Retourne l'objet BSpy
     * 
     * @return objet BSpy
     */
    @Override
    public BSpy getSpy() {
        return (decision != null) && !decision.isNew() ? new BSpy(decision.getSpy()) : new BSpy(getSession());
    }

    /**
     * Formate la ligne vide (&nbsp) si chaine vide, pour tableau jsp
     * 
     * @param valueForTab
     * @return valueForTab si pas null, sinon &nbsp
     */
    public String getStringForTabValue(String valueForTab) {
        return JadeStringUtil.isEmpty(valueForTab) ? "&nbsp;" : valueForTab;
    }

    public Boolean hasConjoint() {
        String idCon = decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionConjoint();

        return !JadeStringUtil.isBlankOrZero(decision.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionConjoint());
    }

    public boolean hasDecompte() {
        if (this.isDecisionAC(getDecisionCSType())) {
            return (!IPCDecision.CS_PREP_COURANT.equals(decision.getCsTypePreparation()))
                    && decision.getSimplePcAccordee().getIsCalculRetro();
        } else {
            return false;
        }
    }

    public String isDacComingFromRepriseClassCss() {
        // si dac
        if (getDecisionCSType().equals(IPCDecision.CS_TYPE_REFUS_AC)
                || getDecisionCSType().equals(IPCDecision.CS_TYPE_OCTROI_AC)
                || getDecisionCSType().equals(IPCDecision.CS_TYPE_PARTIEL_AC)) {

            if (JadeStringUtil.isBlankOrZero(decision.getIdDecisionApresCalcul())) {
                return "reprise";
            }
        }
        return "";
    }

    public Boolean isDecisionAC() {
        return this.isDecisionAC(getDecisionCSType());
    }

    private Boolean isDecisionAC(String cs) {
        return (IPCDecision.CS_TYPE_OCTROI_AC.equals(cs) || IPCDecision.CS_TYPE_REFUS_AC.equals(cs) || IPCDecision.CS_TYPE_PARTIEL_AC
                .equals(cs));
    }

    /**
     * Retourne true si la decision est prete pour la validation Condition: doit etre une decision apres calcul, et
     * pré-validé
     * 
     * @return
     */
    public Boolean isDecisionReadyForValidation() {
        // Si le type est ApresCalcul
        if (decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision()
                .equals(IPCDecision.CS_TYPE_OCTROI_AC)) {
            // Si elle est dans l'etat prévalidé
            if (decision.getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision()
                    .equals(IPCDecision.CS_ETAT_PRE_VALIDE)) {
                return true;
            }
        }
        return false;
    }

    private Boolean isDecisionRefus(String cs) {

        return (cs.equals(IPCDecision.CS_TYPE_REFUS_SC));

    }

    private Boolean isDecisionSuppression(String cs) {
        return (cs.equals(IPCDecision.CS_TYPE_SUPPRESSION_SC));
    }

    /**
     * Lit l'instance
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void retrieve() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        decision = PegasusServiceLocator.getDecisionService().readDecision(decision.getId());

    }

    /**
     * @param monnaieEtrangere
     *            the monnaieEtrangere to set
     */
    public void setDecision(ListDecisions decision) {
        this.decision = decision;
    }

    /**
     * Set l'id de l'entité
     * 
     */
    @Override
    public void setId(String newId) {
        decision.setId(newId);

    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
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

    /**
     * @param noVersion
     *            the noVersion to set
     */
    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
