package globaz.corvus.process;

import globaz.corvus.api.adaptation.IREAdaptationRente;
import globaz.corvus.db.adaptation.REPmtFictif;
import globaz.corvus.db.adaptation.REPmtFictifManager;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiersManager;
import globaz.corvus.itext.REListePrestationsAugmentees;
import globaz.corvus.itext.REListeRecapitulativePrestationsAugmentees;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author HPE
 * 
 */
public class REListePrstAugmenteesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean isLstPrestAugManuellement = false;

    private Boolean isLstPrestNonAdapte = false;
    private Boolean isLstPrestProgrammeCentrale = false;
    private Boolean isLstPrestTraitementAutomatique = false;
    private Boolean isLstRecapAdaptation = false;
    Map<String, RERentesAdapteesJointRATiers> mapAutomatique = new TreeMap<String, RERentesAdapteesJointRATiers>();

    Map<String, RERentesAdapteesJointRATiers> mapJavaCentrale = new TreeMap<String, RERentesAdapteesJointRATiers>();
    Map<String, RERentesAdapteesJointRATiers> mapManuellement = new TreeMap<String, RERentesAdapteesJointRATiers>();
    Map<String, RERentesAdapteesJointRATiers> mapNonAdapte = new TreeMap<String, RERentesAdapteesJointRATiers>();
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REListePrstAugmenteesProcess() {
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

            if (!isLstPrestTraitementAutomatique && !isLstPrestProgrammeCentrale && !isLstPrestAugManuellement
                    && !isLstRecapAdaptation && !isLstPrestNonAdapte) {

                getMemoryLog().logMessage("Aucune liste n'a été sélectionnée", FWMessage.INFORMATION,
                        "REListePrstAugmenteesProcess");
                return true;

            } else {

                JADate date = new JADate(getMoisAnnee());

                RERentesAdapteesJointRATiersManager rentesAdapMgr = new RERentesAdapteesJointRATiersManager();
                rentesAdapMgr.setSession(getSession());
                rentesAdapMgr.setForAnneeAdaptation(String.valueOf(date.getYear()));
                rentesAdapMgr.find(BManager.SIZE_NOLIMIT);

                // Tout ranger dans les MAP
                for (Iterator iterator = rentesAdapMgr.iterator(); iterator.hasNext();) {
                    RERentesAdapteesJointRATiers rentesAdap = (RERentesAdapteesJointRATiers) iterator.next();

                    String cleMap = rentesAdap.getNomTri() + rentesAdap.getPrenomTri() + rentesAdap.getCodePrestation()
                            + rentesAdap.getIdRenteAdaptee();

                    if (rentesAdap.getCsTypeAdaptation().equals(IREAdaptationRente.CS_TYPE_AUG_ADAPTATION_AUTO)) {
                        mapAutomatique.put(cleMap, rentesAdap);
                    } else if (rentesAdap.getCsTypeAdaptation().equals(
                            IREAdaptationRente.CS_TYPE_AUG_DECISIONS_DECEMBRE)) {
                        mapJavaCentrale.put(cleMap, rentesAdap);
                    } else if (rentesAdap.getCsTypeAdaptation()
                            .equals(IREAdaptationRente.CS_TYPE_AUG_TRAITEMENT_MANUEL)) {
                        mapManuellement.put(cleMap, rentesAdap);
                    } else if (rentesAdap.getCsTypeAdaptation().equals(IREAdaptationRente.CS_TYPE_NON_AUGMENTEES)) {
                        mapNonAdapte.put(cleMap, rentesAdap);
                    } else {
                        throw new Exception(getSession().getLabel("ERREUR_ADAPTATION_TYPE_INCONNU"));
                    }
                }

            }

            if (isLstPrestTraitementAutomatique || isLstPrestProgrammeCentrale || isLstPrestAugManuellement
                    || isLstPrestNonAdapte) {

                // Impression des 3 listes d'augmentations
                REListePrestationsAugmentees listPrestationsAugmentees = new REListePrestationsAugmentees();
                listPrestationsAugmentees.setParentWithCopy(this);
                listPrestationsAugmentees.setTransaction(getTransaction());
                listPrestationsAugmentees.setControleTransaction(true);
                listPrestationsAugmentees.setMoisAnnee(getMoisAnnee());
                listPrestationsAugmentees.setMapAutomatique(getMapAutomatique());
                listPrestationsAugmentees.setMapJavaCentrale(getMapJavaCentrale());
                listPrestationsAugmentees.setMapManuellement(getMapManuellement());
                listPrestationsAugmentees.setMapNonAdapte(getMapNonAdapte());
                listPrestationsAugmentees.setIsLstPrestAugManuellement(getIsLstPrestAugManuellement());
                listPrestationsAugmentees.setIsLstPrestProgrammeCentrale(getIsLstPrestProgrammeCentrale());
                listPrestationsAugmentees.setIsLstPrestTraitementAutomatique(getIsLstPrestTraitementAutomatique());
                listPrestationsAugmentees.setIsLstPrestNonAdapte(getIsLstPrestNonAdapte());
                listPrestationsAugmentees.executeProcess();

            }

            // Impression de la liste de récap
            if (getIsLstRecapAdaptation()) {
                REListeRecapitulativePrestationsAugmentees listRecap = new REListeRecapitulativePrestationsAugmentees();
                listRecap.setMapAutomatique(getMapAutomatique());
                listRecap.setMapJavaCentrale(getMapJavaCentrale());
                listRecap.setMapManuellement(getMapManuellement());
                listRecap.setForMoisAnnee(getMoisAnnee());
                listRecap.setTransaction(getTransaction());
                listRecap.setParentWithCopy(this);
                listRecap.executeProcess();

                // Delete des éventuelles anciennes données
                REPmtFictifManager pmtFicMgr = new REPmtFictifManager();
                pmtFicMgr.setSession(getSession());
                pmtFicMgr.setForCsTypeDonnee(IREAdaptationRente.CS_TYPE_RECAP_AUGMENTATION);
                pmtFicMgr.setForMoisAnnee(getMoisAnnee());
                pmtFicMgr.find(transaction);

                if (pmtFicMgr.size() == 1) {
                    pmtFicMgr.delete(transaction);
                }

                // Stockage des montants
                REPmtFictif pmtFic = new REPmtFictif();
                pmtFic.setSession(getSession());

                // AVS
                pmtFic.setNbAVSOrdinaires(String.valueOf(listRecap.getNbRAROAVS()));
                pmtFic.setMontantAVSOrdinaires(listRecap.getMontantROAVS().toString());
                pmtFic.setNbAVSExtraordinaires(String.valueOf(listRecap.getNbRAREOAVS()));
                pmtFic.setMontantAVSExtraordinaires(listRecap.getMontantREOAVS().toString());
                pmtFic.setNbAPIAVS(String.valueOf(listRecap.getNbRAAPIAVS()));
                pmtFic.setMontantAPIAVS(listRecap.getMontantAPIAVS().toString());
                pmtFic.setNbTotalAVS(String.valueOf(listRecap.getNbTotalAVS()));
                pmtFic.setMontantTotalAVS(listRecap.getMontantTotalAVS().toString());

                // AI
                pmtFic.setNbAIOrdinaires(String.valueOf(listRecap.getNbRAROAI()));
                pmtFic.setMontantAIOrdinaires(listRecap.getMontantROAI().toString());
                pmtFic.setNbAIExtraordinaires(String.valueOf(listRecap.getNbRAREOAI()));
                pmtFic.setMontantAIExtraordinaires(listRecap.getMontantREOAI().toString());
                pmtFic.setNbAPIAI(String.valueOf(listRecap.getNbRAAPIAI()));
                pmtFic.setMontantAPIAI(listRecap.getMontantAPIAI().toString());
                pmtFic.setNbTotalAI(String.valueOf(listRecap.getNbTotalAI()));
                pmtFic.setMontantTotalAI(listRecap.getMontantTotalAI().toString());

                // Totaux
                String nbTotalOrdinaire = String.valueOf(listRecap.getNbRAROAI() + listRecap.getNbRAROAVS());
                FWCurrency montantTotalOrdinaire = listRecap.getMontantROAI();
                montantTotalOrdinaire.add(listRecap.getMontantROAVS());
                pmtFic.setNbTotalOrdinaires(nbTotalOrdinaire);
                pmtFic.setMontantTotalOrdinaires(montantTotalOrdinaire.toString());

                String nbTotalExtraordinaire = String.valueOf(listRecap.getNbRAREOAI() + listRecap.getNbRAREOAVS());
                FWCurrency montantTotalExtraOrdinaire = listRecap.getMontantREOAI();
                montantTotalExtraOrdinaire.add(listRecap.getMontantREOAVS());
                pmtFic.setNbTotaleExtraordinaires(nbTotalExtraordinaire);
                pmtFic.setMontantTotalExtraordinaires(montantTotalExtraOrdinaire.toString());

                String nbTotalAPI = String.valueOf(listRecap.getNbRAAPIAI() + listRecap.getNbRAAPIAVS());
                FWCurrency montantTotalAPI = listRecap.getMontantAPIAI();
                montantTotalAPI.add(listRecap.getMontantAPIAVS());
                pmtFic.setNbTotalAPI(nbTotalAPI);
                pmtFic.setMontantTotalAPI(montantTotalAPI.toString());

                // Total général
                pmtFic.setNbTotalGeneral(String.valueOf(listRecap.getNbTotalGeneral()));
                pmtFic.setMontantTotalGeneral(listRecap.getMontantTotalGeneral().toString());

                // Définition du mois de rapport & type de données
                pmtFic.setMoisRapport(getMoisAnnee());
                pmtFic.setTypeDonnes(IREAdaptationRente.CS_TYPE_RECAP_AUGMENTATION);

                pmtFic.add(transaction);
            }

            JadePublishDocumentInfo info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);

            mergePDF(info, true, 500, false, null);

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REListePrstAugmenteesProcess");
            }
            throw e;
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

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_LISTE_PRST_AUG_OBJET_MAIL");
    }

    public Boolean getIsLstPrestAugManuellement() {
        return isLstPrestAugManuellement;
    }

    public Boolean getIsLstPrestNonAdapte() {
        return isLstPrestNonAdapte;
    }

    public Boolean getIsLstPrestProgrammeCentrale() {
        return isLstPrestProgrammeCentrale;
    }

    public Boolean getIsLstPrestTraitementAutomatique() {
        return isLstPrestTraitementAutomatique;
    }

    public Boolean getIsLstRecapAdaptation() {
        return isLstRecapAdaptation;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapAutomatique() {
        return mapAutomatique;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapJavaCentrale() {
        return mapJavaCentrale;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapManuellement() {
        return mapManuellement;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapNonAdapte() {
        return mapNonAdapte;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setIsLstPrestAugManuellement(Boolean isLstPrestAugManuellement) {
        this.isLstPrestAugManuellement = isLstPrestAugManuellement;
    }

    public void setIsLstPrestNonAdapte(Boolean isLstPrestNonAdapte) {
        this.isLstPrestNonAdapte = isLstPrestNonAdapte;
    }

    public void setIsLstPrestProgrammeCentrale(Boolean isLstPrestProgrammeCentrale) {
        this.isLstPrestProgrammeCentrale = isLstPrestProgrammeCentrale;
    }

    public void setIsLstPrestTraitementAutomatique(Boolean isLstPrestTraitementAutomatique) {
        this.isLstPrestTraitementAutomatique = isLstPrestTraitementAutomatique;
    }

    public void setIsLstRecapAdaptation(Boolean isLstRecapAdaptation) {
        this.isLstRecapAdaptation = isLstRecapAdaptation;
    }

    public void setMapAutomatique(Map<String, RERentesAdapteesJointRATiers> mapAutomatique) {
        this.mapAutomatique = mapAutomatique;
    }

    public void setMapJavaCentrale(Map<String, RERentesAdapteesJointRATiers> mapJavaCentrale) {
        this.mapJavaCentrale = mapJavaCentrale;
    }

    public void setMapManuellement(Map<String, RERentesAdapteesJointRATiers> mapManuellement) {
        this.mapManuellement = mapManuellement;
    }

    public void setMapNonAdapte(Map<String, RERentesAdapteesJointRATiers> mapNonAdapte) {
        this.mapNonAdapte = mapNonAdapte;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }
}
