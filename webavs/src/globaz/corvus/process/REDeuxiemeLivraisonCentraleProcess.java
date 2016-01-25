package globaz.corvus.process;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.adaptation.IREAdaptationRente;
import globaz.corvus.api.arc.downloader.REReaderAnnonces51_53;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiersManager;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.itext.REListeComparaisonLivraison2Centrale;
import globaz.corvus.process.REComparaisonCentraleProcess.KeyRAAnnComparaison;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author HPE
 */
public class REDeuxiemeLivraisonCentraleProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class KeyRAAnnoncesAdap implements Comparable<KeyRAAnnoncesAdap> {

        private String codePrestation = "";
        private String fractionRente = "";
        private String montantAvant = "";
        private String nss = "";

        public KeyRAAnnoncesAdap(String codePrestation, String nss, String montantAvant, String fractionRente) {
            this.nss = nss;
            this.codePrestation = codePrestation;
            this.montantAvant = montantAvant;
            this.fractionRente = fractionRente;
        }

        @Override
        public int compareTo(KeyRAAnnoncesAdap keyRaAnnAdapt) {
            if (nss.compareTo(keyRaAnnAdapt.nss) != 0) {
                return nss.compareTo(keyRaAnnAdapt.nss);
            } else if (codePrestation.compareTo(keyRaAnnAdapt.codePrestation) != 0) {
                return codePrestation.compareTo(keyRaAnnAdapt.codePrestation);
            } else if (montantAvant.compareTo(keyRaAnnAdapt.montantAvant) != 0) {
                return montantAvant.compareTo(keyRaAnnAdapt.montantAvant);
            } else if (fractionRente.compareTo(keyRaAnnAdapt.fractionRente) != 0) {
                return fractionRente.compareTo(keyRaAnnAdapt.fractionRente);
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof KeyRAAnnoncesAdap)) {
                return false;
            }

            KeyRAAnnoncesAdap keyRaAnnAdapt = (KeyRAAnnoncesAdap) obj;

            return ((keyRaAnnAdapt.nss.equals(nss)) && (keyRaAnnAdapt.codePrestation.equals(codePrestation))
                    && (keyRaAnnAdapt.montantAvant.equals(montantAvant)) && (keyRaAnnAdapt.fractionRente
                        .equals(fractionRente)));
        }

        @Override
        public int hashCode() {
            return (nss + codePrestation + montantAvant + fractionRente).hashCode();
        }

    }

    private String idLot = "";
    private REListeComparaisonLivraison2Centrale listeComparaisonLivraison2Centrale = null;
    private Map<KeyRAAnnoncesAdap, Object> mapAnnonces = new TreeMap<KeyRAAnnoncesAdap, Object>();
    private Map<String, List<Object>> mapCorrespondances = new TreeMap<String, List<Object>>();
    private Map<KeyRAAnnoncesAdap, RERentesAdapteesJointRATiers> mapRenteAdaptees = new TreeMap<KeyRAAnnoncesAdap, RERentesAdapteesJointRATiers>();
    private String moisAnnee = "";

    public REDeuxiemeLivraisonCentraleProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();

            setSendMailOnError(true);

            // 1) Recherche de la deuxième livraison de la centrale pour les annonces
            // 51 et 53 dans Hermes, les mettre dans une map
            REReaderAnnonces51_53 da51_53 = new REReaderAnnonces51_53();
            da51_53.setSession(getSession());
            da51_53.setParentProcess(this);
            da51_53.setLog(getMemoryLog());
            da51_53.setIdLot(getIdLot());

            da51_53.read((BTransaction) transaction);

            // 2) Comparer ces annonces avec les rentesAdaptées manuellement ou par
            // le programme JAVA de la centrale
            if (da51_53.getAnnoncesCrees().isEmpty()) {
                throw new Exception("Aucune annonce 51/53 n'a été importée");
            }

            doComparaisonAnnoncesRenteAdaptees(da51_53.getAnnoncesCrees(), transaction);

            // 3) Créer des listes pour les résultats
            // => Liste des correspondances et différences entre centrale et renteAdaptees
            createListes(mapCorrespondances);

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REDeuxiemeLivraisonCentraleProcess");
            }
            return false;
        } finally {

            try {
                JadePublishDocumentInfo info = createDocumentInfo();
                info.setPublishDocument(true);
                info.setArchiveDocument(false);

                this.mergePDF(info, true, 500, false, null);
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
    }

    private void createListes(Map<String, List<Object>> mapCorrespondances2) throws Exception {

        listeComparaisonLivraison2Centrale = new REListeComparaisonLivraison2Centrale();
        listeComparaisonLivraison2Centrale.setParentWithCopy(this);
        listeComparaisonLivraison2Centrale.setTransaction(getTransaction());
        listeComparaisonLivraison2Centrale.setControleTransaction(true);
        listeComparaisonLivraison2Centrale.setMoisAnnee(getMoisAnnee());
        listeComparaisonLivraison2Centrale.setMapCorrespondances(mapCorrespondances2);
        listeComparaisonLivraison2Centrale.executeProcess();

    }

    private void doComparaisonAnnoncesRenteAdaptees(Map<KeyRAAnnComparaison, ArrayList<Object>> annoncesCrees,
            BITransaction transaction) throws Exception {

        // charger toutes les rentes adaptées nécessaires
        KeyRAAnnoncesAdap key = null;

        JADate date = new JADate(getMoisAnnee());

        RERentesAdapteesJointRATiersManager raMgr = new RERentesAdapteesJointRATiersManager();
        raMgr.setSession(getSession());
        raMgr.setForAnneeAdaptation(String.valueOf(date.getYear()));
        raMgr.setForCsTypeAdaptationIn(IREAdaptationRente.CS_TYPE_AUG_DECISIONS_DECEMBRE + ","
                + IREAdaptationRente.CS_TYPE_AUG_TRAITEMENT_MANUEL);
        raMgr.setOrderBy(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        raMgr.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < raMgr.size(); i++) {
            RERentesAdapteesJointRATiers ra = (RERentesAdapteesJointRATiers) raMgr.get(i);

            String fraction;
            if (JadeStringUtil.isBlankOrZero(ra.getFractionRente())) {
                fraction = "1";
            } else {
                fraction = ra.getFractionRente();
            }

            key = new KeyRAAnnoncesAdap(ra.getCodePrestation(), ra.getNssRA(),
            // "","");
                    ra.getAncienMontantPrestation(), fraction);

            mapRenteAdaptees.put(key, ra);
        }

        // Parcourir toutes les annonces 51 et 53 et ajouter dans une map avec la clé pour comparer ensuite
        for (KeyRAAnnComparaison keyIdAnnonce : annoncesCrees.keySet()) {

            ArrayList<Object> listAnn = annoncesCrees.get(keyIdAnnonce);

            KeyRAAnnoncesAdap keyAnn;

            if (listAnn.get(0) instanceof REAnnonce51) {

                REAnnonce51 ann51_01 = (REAnnonce51) listAnn.get(0);
                REAnnonce51 ann51_03 = (REAnnonce51) listAnn.get(2);

                keyAnn = new KeyRAAnnoncesAdap(ann51_01.getGenrePrestation(), NSUtil.formatAVSUnknown(ann51_01
                        .getNoAssAyantDroit()), (new FWCurrency(ann51_03.getAncienMontantMensuel())).toString(),
                        ann51_03.getFractionRente());

                if (!mapAnnonces.containsKey(keyAnn)) {
                    mapAnnonces.put(keyAnn, listAnn);
                }

            } else if (listAnn.get(0) instanceof REAnnonce53) {

                REAnnonce53 ann53_01 = (REAnnonce53) listAnn.get(0);
                REAnnonce53 ann53_03 = (REAnnonce53) listAnn.get(2);

                keyAnn = new KeyRAAnnoncesAdap(ann53_01.getGenrePrestation(), NSUtil.formatAVSUnknown(ann53_01
                        .getNoAssAyantDroit()), (new FWCurrency(ann53_03.getAncienMontantMensuel())).toString(),
                        ann53_03.getFractionRente());

                if (!mapAnnonces.containsKey(keyAnn)) {
                    mapAnnonces.put(keyAnn, listAnn);
                }
            }
        }

        // Chercher les correspondances et les non
        ArrayList<Object> listRaAnn = null;
        String keyMap = "";

        // Parcourir toutes les rentesAdaptees 51 et 53 et rechercher les correspondants dans les annonces
        for (KeyRAAnnoncesAdap keyRentesAdap : mapRenteAdaptees.keySet()) {

            RERentesAdapteesJointRATiers ra = mapRenteAdaptees.get(keyRentesAdap);
            keyMap = ra.getNomTri() + ra.getPrenomTri() + ra.getCodePrestation() + ra.getIdPrestationAccordee();

            // => Si correspondance dans les annonces, mettre dans la mapCorrespondances
            if (mapAnnonces.containsKey(keyRentesAdap)) {

                listRaAnn = new ArrayList<Object>();
                listRaAnn.add(ra);
                listRaAnn.add(mapAnnonces.get(keyRentesAdap));

                mapCorrespondances.put(keyMap, listRaAnn);
            }
        }

    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_LST_COMP_CENTRALE_OBJET_MAIL");
    }

    public String getIdLot() {
        return idLot;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
