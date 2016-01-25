package globaz.corvus.process;

import globaz.corvus.api.adaptation.IREAdaptationRente;
import globaz.corvus.db.adaptation.REPmtFictif;
import globaz.corvus.db.adaptation.REPmtFictifManager;
import globaz.corvus.itext.REListeRecapitulativePaiement;
import globaz.corvus.itext.REListeRetenuesBlocages;
import globaz.corvus.itext.RERecapitulationPaiementAdapter;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import ch.globaz.common.properties.CommonProperties;

/**
 * 
 * @author HPE
 * 
 */
public class REPmtFictifProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String LISTE_RECAP_PAIEM_ORDER = "02";
    public static final String LISTE_RETENUES_ORDER = "01";
    // pour l'ordonnancement des documents de verification
    public static final String PROPERTY_DOCUMENT_ORDER = "DOCUMENT_ORDER";

    private RERecapitulationPaiementAdapter adapter;

    private REListeRecapitulativePaiement listeRecapitulativePaiement = null;
    private REListeRetenuesBlocages listeRetenues = null;
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REPmtFictifProcess() {
        super();
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();

            // Exécution des listes
            doListeRetenues(transaction);
            adapter = doListeRecapitulativePaiement(transaction);

            // Vérifier s'il existe déjà ces données ?
            REPmtFictifManager pmtFicMgr = new REPmtFictifManager();
            pmtFicMgr.setSession(getSession());
            pmtFicMgr.setForMoisAnnee(getMoisAnnee());
            pmtFicMgr.setForCsTypeDonnee(IREAdaptationRente.CS_TYPE_PAIEMENT_FICTIF);
            pmtFicMgr.find();

            if (pmtFicMgr.size() > 0) {
                pmtFicMgr.delete(transaction);
            }

            // Stockage des montants
            REPmtFictif pmtFic = new REPmtFictif();
            pmtFic.setSession(getSession());

            // AVS
            pmtFic.setNbAVSOrdinaires(String.valueOf(adapter.getNbRAROAVS()));
            pmtFic.setMontantAVSOrdinaires(adapter.getMontantROAVS().toString());
            pmtFic.setNbAVSExtraordinaires(String.valueOf(adapter.getNbRAREOAVS()));
            pmtFic.setMontantAVSExtraordinaires(adapter.getMontantREOAVS().toString());
            pmtFic.setNbAPIAVS(String.valueOf(adapter.getNbRAAPIAVS()));
            pmtFic.setMontantAPIAVS(adapter.getMontantAPIAVS().toString());
            pmtFic.setNbTotalAVS(String.valueOf(adapter.getNbTotalAVS()));
            pmtFic.setMontantTotalAVS(adapter.getMontantTotalAVS().toString());

            // AI
            pmtFic.setNbAIOrdinaires(String.valueOf(adapter.getNbRAROAI()));
            pmtFic.setMontantAIOrdinaires(adapter.getMontantROAI().toString());
            pmtFic.setNbAIExtraordinaires(String.valueOf(adapter.getNbRAREOAI()));
            pmtFic.setMontantAIExtraordinaires(adapter.getMontantREOAI().toString());
            pmtFic.setNbAPIAI(String.valueOf(adapter.getNbRAAPIAI()));
            pmtFic.setMontantAPIAI(adapter.getMontantAPIAI().toString());
            pmtFic.setNbTotalAI(String.valueOf(adapter.getNbTotalAI()));
            pmtFic.setMontantTotalAI(adapter.getMontantTotalAI().toString());

            // Totaux
            String nbTotalOrdinaire = String.valueOf(adapter.getNbRAROAI() + adapter.getNbRAROAVS());
            FWCurrency montantTotalOrdinaire = adapter.getMontantROAI();
            montantTotalOrdinaire.add(adapter.getMontantROAVS());
            pmtFic.setNbTotalOrdinaires(nbTotalOrdinaire);
            pmtFic.setMontantTotalOrdinaires(montantTotalOrdinaire.toString());

            String nbTotalExtraordinaire = String.valueOf(adapter.getNbRAREOAI() + adapter.getNbRAREOAVS());
            FWCurrency montantTotalExtraOrdinaire = adapter.getMontantREOAI();
            montantTotalExtraOrdinaire.add(adapter.getMontantREOAVS());
            pmtFic.setNbTotaleExtraordinaires(nbTotalExtraordinaire);
            pmtFic.setMontantTotalExtraordinaires(montantTotalExtraOrdinaire.toString());

            String nbTotalAPI = String.valueOf(adapter.getNbRAAPIAI() + adapter.getNbRAAPIAVS());
            FWCurrency montantTotalAPI = adapter.getMontantAPIAI();
            montantTotalAPI.add(adapter.getMontantAPIAVS());
            pmtFic.setNbTotalAPI(nbTotalAPI);
            pmtFic.setMontantTotalAPI(montantTotalAPI.toString());

            // Total général
            pmtFic.setNbTotalGeneral(String.valueOf(adapter.getNbTotalGeneral()));
            pmtFic.setMontantTotalGeneral(adapter.getMontantTotalGeneral().toString());

            // Définition du mois de rapport & type de données
            pmtFic.setMoisRapport(getMoisAnnee());
            pmtFic.setTypeDonnes(IREAdaptationRente.CS_TYPE_PAIEMENT_FICTIF);

            pmtFic.add(transaction);

            // Fusion des documents
            JadePublishDocumentInfo info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);

            this.mergePDF(info, true, 500, false, REPmtFictifProcess.PROPERTY_DOCUMENT_ORDER);

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REPmtFictifProcess");
            }
            return false;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * 
     * @param transaction
     * @throws Exception
     */
    private RERecapitulationPaiementAdapter doListeRecapitulativePaiement(BITransaction transaction) throws Exception {
        listeRecapitulativePaiement = new REListeRecapitulativePaiement();

        listeRecapitulativePaiement.setForMoisAnnee(moisAnnee);
        listeRecapitulativePaiement.setTransaction(transaction);
        listeRecapitulativePaiement.setParentWithCopy(this);
        listeRecapitulativePaiement.executeProcess();

        return listeRecapitulativePaiement.getAdapter();
    }

    /**
     * 
     * @param transaction
     * @throws Exception
     */
    private void doListeRetenues(BITransaction transaction) throws Exception {
        listeRetenues = new REListeRetenuesBlocages(getSession());
        listeRetenues.setMois(moisAnnee);
        listeRetenues.setTransaction(transaction);
        listeRetenues.setParentWithCopy(this);
        listeRetenues.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        listeRetenues.executeProcess();
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_PMT_FICTIF_OBJET_MAIL");
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
