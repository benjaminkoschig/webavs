/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.process;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFTypesDemandeJointDossierJointTiers;
import globaz.cygnus.db.demandes.RFTypesDemandeJointDossierJointTiersManager;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.preparerDecision.RFPreparerDecisionsService;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Création des décisions par regroupement des demandes. Calcul de celles-ci et imputations sur les Qds concernées puis
 * persistance des résultats et des décisions en BD.
 * 
 * @author JJE
 */
public class RFPreparerDecisionsProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDernierPaiementMensuelRente = "";
    private String dateSurDocument = "";
    private String idGestionnaire = "";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            RFPreparerDecisionsService rfPreDecSer = new RFPreparerDecisionsService(dateDernierPaiementMensuelRente,
                    dateSurDocument, retrieveDemandesATraiter(), idGestionnaire, "", getMemoryLog(), getSession(),
                    getTransaction());

            return rfPreDecSer.preparerDecisions();

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();

            if (getTransaction() != null) {
                getTransaction().setRollbackOnly();
            }

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    getSession().getLabel("PROCESS_PREPARER_DECISIONS"));

            return false;
        } finally {
            if (getTransaction() != null) {
                try {
                    if (getTransaction().hasErrors() || getTransaction().isRollbackOnly()) {
                        getTransaction().rollback();
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {

                    getTransaction().closeTransaction();

                    try {
                        RFSetEtatProcessService.setEtatProcessPreparerDecision(false, getSession());
                    } catch (Exception e) {
                        e.printStackTrace();
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                                getSession().getLabel("PROCESS_PREPARER_DECISIONS"));
                        throw e;
                    }
                }
            }
        }

    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {

        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_PREPARER_DECISIONS_FAILED");
        } else {
            return getSession().getLabel("PROCESS_PREPARER_DECISIONS_SUCCESS");
        }

    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private List<RFTypesDemandeJointDossierJointTiers> retrieveDemandesATraiter() throws Exception {

        RFTypesDemandeJointDossierJointTiersManager rfTypDemJointDosJointTieMgr = new RFTypesDemandeJointDossierJointTiersManager();
        rfTypDemJointDosJointTieMgr.setSession(getSession());
        rfTypDemJointDosJointTieMgr.setForCsEtatDemande(IRFDemande.ENREGISTRE);

        if (RFPropertiesUtils.utiliserGestionnaireRechercheDemandesAImputer()) {
            rfTypDemJointDosJointTieMgr.setForIdGestionnaire(idGestionnaire);
        }

        Set<String> csSousTypesToIgnore = new HashSet<String>();
        csSousTypesToIgnore.add(IRFTypesDeSoins.st_19_DEVIS_DENTAIRE);
        rfTypDemJointDosJointTieMgr.setCssSousTypeDeSoinToIgnore(csSousTypesToIgnore);
        rfTypDemJointDosJointTieMgr.changeManagerSize(0);
        rfTypDemJointDosJointTieMgr.find(getTransaction());

        Iterator<RFTypesDemandeJointDossierJointTiers> rfTypDemJointDosJointTieItr = rfTypDemJointDosJointTieMgr
                .iterator();

        List<RFTypesDemandeJointDossierJointTiers> rfTypDemJointDosJointTieList = new LinkedList<RFTypesDemandeJointDossierJointTiers>();

        while (rfTypDemJointDosJointTieItr.hasNext()) {

            RFTypesDemandeJointDossierJointTiers rfTypDemJoiDosJoiTie = rfTypDemJointDosJointTieItr.next();

            if (rfTypDemJoiDosJoiTie != null) {
                rfTypDemJointDosJointTieList.add(rfTypDemJoiDosJoiTie);
            } else {
                throw new Exception("Demande null [RFPreparerDecisionsProcess.retrieveDemandesATraiter()]");
            }
        }

        return rfTypDemJointDosJointTieList;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

}
