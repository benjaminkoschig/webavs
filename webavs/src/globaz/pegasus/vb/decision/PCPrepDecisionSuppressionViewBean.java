/**
 * 
 */
package globaz.pegasus.vb.decision;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.DecisionUtils;
import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class PCPrepDecisionSuppressionViewBean extends PCPrepDecisionAbstractViewBean {

    // Code systeme motif
    public static String CS_MOTIF = "PCMOTIFDES";

    public static String getDateDernierPaiement() throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {
        return PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
    }

    private String csMotifTransfert = null;
    private String dateDernierPaiement = null;
    private DecisionSuppression decisionSuppression = null;
    private String idDroit = null;

    private String idNouvelleCaisse = null;

    private String idVersionDroit = null;

    /**
     * Constructeur sans paramètres
     */
    public PCPrepDecisionSuppressionViewBean() throws Exception {
        super();
        setDecisionSuppression(new DecisionSuppression());
    }

    public String getCsCodeTexteLibreMotif() {
        return IPCDecision.CS_MOTIF_SUPP_TEXTE_LIBRE;
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

    public String getCsMotifTransfert() {
        return csMotifTransfert;
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
     * @return the decisionSuppression
     */
    public DecisionSuppression getDecisionSuppression() {
        return decisionSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return decisionSuppression.getId();
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    public String getIdNouvelleCaisse() {
        return idNouvelleCaisse;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    /**
     * Retourne le modele complex de la personne
     * 
     * @return personneComplexModel
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return decisionSuppression.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                .getPersonneEtendue();

    }

    /**
     * Retourn la session
     * 
     * @return objet BSEssion
     */
    @Override
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(decisionSuppression.getSpy());
    }

    /**
     * Retourne le Tiers
     * 
     * @return
     */
    public TiersSimpleModel getTiers() {
        return decisionSuppression.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                .getPersonneEtendue().getTiers();
    }

    public String motifsHasSousMotifs() {
        return decisionSuppression.getSimpleDecisionSuppression().getCsSousMotif();
    }

    public void preparer() throws PCAccordeeException, JadeApplicationServiceNotAvailableException, DecisionException,
            JadePersistenceException, Exception {

        DecisionUtils.checkIfDateDecisionCompriseEntreDernierEtProchainPaiement(decisionSuppression.getDecisionHeader()
                .getSimpleDecisionHeader().getDateDecision());

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            // Set preparation par
            decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setPreparationPar(getCurrentUserId());

            // Suppression des décisions existantes pour la version
            PegasusImplServiceLocator.getCleanDecisionService().deleteDecisionsForVersion(
                    decisionSuppression.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());

            // Suppression des pca existantes pour la version
            PegasusImplServiceLocator.getPCAccordeeService().deleteByIdVersionDroit(
                    decisionSuppression.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());

            // Gestion si restitution
            if (decisionSuppression.getSimpleDecisionSuppression().getIsRestitution() == null) {
                decisionSuppression.getSimpleDecisionSuppression().setIsRestitution(false);
            }

            // Création des decision de suppressions
            decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().create(decisionSuppression);

            // création d'une demande de transfert sans paramètre
            if (IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER.equals(decisionSuppression
                    .getSimpleDecisionSuppression().getCsMotif())) {
                // génération du document de transfert
                PegasusServiceLocator.getTransfertDossierPCProviderService().createTransfertDossierSuppression(
                        decisionSuppression, idNouvelleCaisse, csMotifTransfert);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // Recherche drooit pour info tiers
        decisionSuppression.setVersionDroit(PegasusServiceLocator.getDroitService().readVersionDroit(
                getIdVersionDroit()));

        dateDernierPaiement = "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
    }

    public void setCsMotifTransfert(String csMotifTransfert) {
        this.csMotifTransfert = csMotifTransfert;
    }

    /**
     * @param decisionSuppression
     *            the decisionSuppression to set
     */
    public void setDecisionSuppression(DecisionSuppression decisionSuppression) {
        this.decisionSuppression = decisionSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        decisionSuppression.setId(newId);

    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdNouvelleCaisse(String idNouvelleCaisse) {
        this.idNouvelleCaisse = idNouvelleCaisse;
    }

    /**
     * @param parameter
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;

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
