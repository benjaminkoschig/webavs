package globaz.corvus.process;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.arc.downloader.REReaderAnnonces51_53;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiersManager;
import globaz.corvus.db.annonces.IREAnnonceAdaptation;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.corvus.db.annonces.REAnnonce51Adaptation;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.corvus.db.annonces.REAnnonce53Adaptation;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.itext.REListeDifferencesCentraleCaisse;
import globaz.corvus.itext.REListeDifferencesDroitApplique;
import globaz.corvus.itext.REPrestationsNonTrouveesCentrale;
import globaz.corvus.itext.REPrestationsNonTrouveesFichierCaisse;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @author HPE
 * 
 */

public class REComparaisonCentraleProcess extends BProcess {

    private static final long serialVersionUID = 1L;

    public class KeyRAAnnComparaison implements Comparable<KeyRAAnnComparaison> {

        private String codePrestation = "";
        private String montant = "";
        private String nss = "";

        /**
         * Crée une nouvelle instance de la classe KeyRAAnnComparaison.
         */
        public KeyRAAnnComparaison(String codePrestation, String nss, String montant) {
            this.nss = nss;
            this.codePrestation = codePrestation;
            this.montant = montant;
        }

        @Override
        public int compareTo(KeyRAAnnComparaison keyRaAnnComp) {
            if (nss.compareTo(keyRaAnnComp.nss) != 0) {
                return nss.compareTo(keyRaAnnComp.nss);
            } else if (codePrestation.compareTo(keyRaAnnComp.codePrestation) != 0) {
                return codePrestation.compareTo(keyRaAnnComp.codePrestation);
            } else if (montant.compareTo(keyRaAnnComp.montant) != 0) {
                return montant.compareTo(keyRaAnnComp.montant);
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof KeyRAAnnComparaison)) {
                return false;
            }

            KeyRAAnnComparaison keyRaAnnComp = (KeyRAAnnComparaison) obj;

            return ((keyRaAnnComp.nss.equals(nss)) && (keyRaAnnComp.codePrestation.equals(codePrestation)) && (keyRaAnnComp.montant
                    .equals(montant)));
        }

        @Override
        public int hashCode() {
            return (nss + codePrestation + montant).hashCode();
        }

    }

    private String idLot = "";
    private REListeDifferencesCentraleCaisse listeDiffencesCentraleCaisse = null;
    private REListeDifferencesDroitApplique listeDiffencesDroitApplique = null;
    private REPrestationsNonTrouveesCentrale listePrestationsNonTrouveesCentrale = null;
    private REPrestationsNonTrouveesFichierCaisse listePrestationsNonTrouveesFichierCaisse = null;
    private Map<String, ArrayList<Object>> mapDifferencesCentraleCaisse = new TreeMap<String, ArrayList<Object>>();
    private Map<String, ArrayList<Object>> mapDifferencesDroitApplique = new TreeMap<String, ArrayList<Object>>();
    private Map<String, REPrestAccJointInfoComptaJointTiers> mapPrestationsNonTrouveesCentrale = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();
    private Map<String, IREAnnonceAdaptation> mapPrestationsNonTrouveesFichierCaisse = new TreeMap<String, IREAnnonceAdaptation>();

    private String moisAnnee = "";

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();

            // 1) Import des annonces 51/ 53 (Pas de stockage)
            REReaderAnnonces51_53 read51_53 = new REReaderAnnonces51_53();
            read51_53.setSession(getSession());
            read51_53.setParentProcess(this);
            read51_53.setIdLot(getIdLot());
            read51_53.setLog(getMemoryLog());

            if (JadeStringUtil.isBlankOrZero(getIdLot())) {
                throw new Exception("Aucun lot sélectionné ! Aucune annonce à comparer...");
            }

            read51_53.read((BTransaction) transaction);

            Map<KeyRAAnnComparaison, ArrayList<Object>> mapAnnoncesLues = new HashMap<KeyRAAnnComparaison, ArrayList<Object>>();
            mapAnnoncesLues = read51_53.getAnnoncesCrees();

            // 2) Ajout des rentes accordées dans une map (clé =
            // NSS/CodePrestation/Montant)
            Map<KeyRAAnnComparaison, REPrestAccJointInfoComptaJointTiers> mapRentesAcc = new HashMap<KeyRAAnnComparaison, REPrestAccJointInfoComptaJointTiers>();

            REPrestAccJointInfoComptaJointTiersManager raMgr = new REPrestAccJointInfoComptaJointTiersManager();
            raMgr.setSession(getSession());
            raMgr.setForEnCoursAtMois(getMoisAnnee());
            raMgr.setOrderBy(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
            raMgr.find(BManager.SIZE_NOLIMIT);

            KeyRAAnnComparaison key = null;

            for (Iterator iterator = raMgr.iterator(); iterator.hasNext();) {
                REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) iterator.next();

                key = new KeyRAAnnComparaison(ra.getCodePrestation(), ra.getNss(), ra.getMontantPrestation());

                mapRentesAcc.put(key, ra);

            }

            // 4) Création des listes
            ArrayList<Object> listRaAnn = null;
            String keyMap = "";

            // Parcourir toutes les annonces 51 et 53 et rechercher les
            // correspondants dans les RA
            for (KeyRAAnnComparaison keyAnnonce : mapAnnoncesLues.keySet()) {

                ArrayList<Object> listAnnonces = mapAnnoncesLues.get(keyAnnonce);

                REAnnonce51Adaptation ann51_01Ad = new REAnnonce51Adaptation();
                REAnnonce53Adaptation ann53_01Ad = new REAnnonce53Adaptation();
                boolean is51 = false;

                if (listAnnonces.get(0) instanceof REAnnonce51) {

                    is51 = true;

                    REAnnonce51 ann51_01 = (REAnnonce51) listAnnonces.get(0);
                    REAnnonce51 ann51_02 = (REAnnonce51) listAnnonces.get(1);
                    REAnnonce51 ann51_03 = (REAnnonce51) listAnnonces.get(2);

                    ann51_01Ad.setMontantPrestation(ann51_01.getMensualitePrestationsFrancs());
                    ann51_01Ad.setNss(ann51_01.getNoAssAyantDroit());
                    ann51_01Ad.setGenrePrestation(ann51_01.getGenrePrestation());
                    ann51_01Ad.setCodeCasSpecial1(ann51_02.getCasSpecial1());
                    ann51_01Ad.setCodeCasSpecial2(ann51_02.getCasSpecial2());
                    ann51_01Ad.setCodeCasSpecial3(ann51_02.getCasSpecial3());
                    ann51_01Ad.setCodeCasSpecial4(ann51_02.getCasSpecial4());
                    ann51_01Ad.setCodeCasSpecial5(ann51_02.getCasSpecial5());
                    ann51_01Ad.setRAM(ann51_02.getRamDeterminant());
                    ann51_01Ad.setMntSuppAjournement(ann51_02.getSupplementAjournement());
                    ann51_01Ad.setAncienMontantBTE(ann51_02.getBteMoyennePrisEnCompte());
                    ann51_01Ad.setMntRenteOrdRemplacee(ann51_02.getMntRenteOrdinaireRempl());
                    ann51_01Ad.setAncienMontantMensuel(ann51_03.getAncienMontantMensuel());
                    ann51_01Ad.setFractionRente(ann51_03.getFractionRente());

                    listAnnonces.clear();
                    listAnnonces.add(ann51_01Ad);

                } else {

                    REAnnonce53 ann53_01 = (REAnnonce53) listAnnonces.get(0);
                    REAnnonce53 ann53_02 = (REAnnonce53) listAnnonces.get(1);
                    REAnnonce53 ann53_03 = (REAnnonce53) listAnnonces.get(2);

                    ann53_01Ad.setMontantPrestation(ann53_01.getMensualitePrestationsFrancs());
                    ann53_01Ad.setNss(ann53_01.getNoAssAyantDroit());
                    ann53_01Ad.setGenrePrestation(ann53_01.getGenrePrestation());
                    ann53_01Ad.setCodeCasSpecial1(ann53_02.getCasSpecial1());
                    ann53_01Ad.setCodeCasSpecial2(ann53_02.getCasSpecial2());
                    ann53_01Ad.setCodeCasSpecial3(ann53_02.getCasSpecial3());
                    ann53_01Ad.setCodeCasSpecial4(ann53_02.getCasSpecial4());
                    ann53_01Ad.setCodeCasSpecial5(ann53_02.getCasSpecial5());
                    ann53_01Ad.setRAM(ann53_02.getRamDeterminant());
                    ann53_01Ad.setMntSuppAjournement(ann53_02.getSupplementAjournement());
                    ann53_01Ad.setMntReducAnticipation(ann53_02.getReductionAnticipation());
                    ann53_01Ad.setAncienMontantMensuel(ann53_03.getAncienMontantMensuel());
                    ann53_01Ad.setFractionRente(ann53_03.getFractionRente());

                    listAnnonces.clear();
                    listAnnonces.add(ann53_01Ad);

                }

                // => Si correspondance dans les RA, mettre dans la
                // mapDifferencesCentraleCaisse
                if (mapRentesAcc.containsKey(keyAnnonce)) {
                    listRaAnn = new ArrayList<Object>();
                    if (is51) {
                        listRaAnn.add(ann51_01Ad);
                    } else {
                        listRaAnn.add(ann53_01Ad);
                    }

                    listRaAnn.add(mapRentesAcc.get(keyAnnonce));

                    REPrestAccJointInfoComptaJointTiers ra = mapRentesAcc.get(keyAnnonce);

                    keyMap = ra.getNom() + ra.getPrenom() + ra.getCodePrestation() + ra.getIdPrestationAccordee();

                    if (((ra.getDroitApplique().equals("10") && (listAnnonces.get(0) instanceof REAnnonce51Adaptation)))
                            || ((ra.getDroitApplique().equals("9") && (listAnnonces.get(0) instanceof REAnnonce53Adaptation)))) {

                        mapDifferencesDroitApplique.put(keyMap, listRaAnn);
                    } else {

                        mapDifferencesCentraleCaisse.put(keyMap, listRaAnn);
                    }

                    // => Si pas correspondance, mettre dans la
                    // mapPrestationsNonTrouveesFichierCaisse
                } else {

                    if (listAnnonces.get(0) instanceof REAnnonce51Adaptation) {

                        String nomPrenom = "";
                        PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                                NSUtil.formatAVSUnknown(((REAnnonce51Adaptation) listAnnonces.get(0)).getNss()));
                        if (tier != null) {
                            nomPrenom = tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                    + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                        } else {
                            nomPrenom = "";
                        }

                        keyMap = nomPrenom + ((REAnnonce51Adaptation) listAnnonces.get(0)).getGenrePrestation()
                                + ((REAnnonce51Adaptation) listAnnonces.get(0)).getNss();

                        mapPrestationsNonTrouveesFichierCaisse.put(keyMap, (IREAnnonceAdaptation) listAnnonces.get(0));
                    } else {

                        String nomPrenom = "";
                        PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                                NSUtil.formatAVSUnknown(((REAnnonce53Adaptation) listAnnonces.get(0)).getNss()));
                        if (tier != null) {
                            nomPrenom = tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                    + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                        } else {
                            nomPrenom = "";
                        }

                        keyMap = nomPrenom + ((REAnnonce53Adaptation) listAnnonces.get(0)).getGenrePrestation()
                                + ((REAnnonce53Adaptation) listAnnonces.get(0)).getNss();

                        mapPrestationsNonTrouveesFichierCaisse.put(keyMap, (IREAnnonceAdaptation) listAnnonces.get(0));
                    }
                }
            }

            // Parcourir toutes les rentes accordées en cours et rechercher les
            // correspondances dans les annonces
            Set setKeyMapRA = mapRentesAcc.keySet();
            Iterator iterKeyMapRA = setKeyMapRA.iterator();

            int nbCorrespondance = 0;
            while (iterKeyMapRA.hasNext()) {
                KeyRAAnnComparaison keyRA = (KeyRAAnnComparaison) iterKeyMapRA.next();

                // => Si correspondance, mise à jour d'un compteur pour contrôle
                if (mapAnnoncesLues.containsKey(keyRA)) {
                    nbCorrespondance++;

                    // => Si pas correspondance, mettre dans la
                    // mapPrestationsNonTrouveesCentrale
                } else {
                    keyMap = mapRentesAcc.get(keyRA).getNom() + mapRentesAcc.get(keyRA).getPrenom()
                            + mapRentesAcc.get(keyRA).getCodePrestation()
                            + mapRentesAcc.get(keyRA).getIdPrestationAccordee();

                    mapPrestationsNonTrouveesCentrale.put(keyMap, mapRentesAcc.get(keyRA));

                }
            }

            getMemoryLog().logMessage("Total des prestations trouvées : " + mapDifferencesCentraleCaisse.size(),
                    FWMessage.INFORMATION, "REListeErreursProcess");
            getMemoryLog().logMessage(
                    "Total des prestations non trouvées à la centrale : " + mapPrestationsNonTrouveesCentrale.size(),
                    FWMessage.INFORMATION, "REListeErreursProcess");
            getMemoryLog().logMessage(
                    "Total des prestations non trouvées dans le fichier de la caisse : "
                            + mapPrestationsNonTrouveesFichierCaisse.size(), FWMessage.INFORMATION,
                    "REListeErreursProcess");

            // -> Liste des rentes non trouvées à la centrale
            doListePrestationsNonTrouveesCentrale();

            // -> Liste des rentes non trouvées à la caisse
            doListePrestationsNonTrouveesFichierCaisse();

            // -> Liste des différences / erreurs des correspondances
            doListeDifferencesCentraleEtCaisse();
            doListeDifferencesDroitApplique();

            // 5) Impression pdf et publication
            JadePublishDocumentInfo info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);

            this.mergePDF(info, true, 500, false, null);

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REComparaisonCentraleProcess");
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

    private void doListeDifferencesCentraleEtCaisse() throws Exception {
        listeDiffencesCentraleCaisse = new REListeDifferencesCentraleCaisse();
        listeDiffencesCentraleCaisse.setParentWithCopy(this);
        listeDiffencesCentraleCaisse.setTransaction(getTransaction());
        listeDiffencesCentraleCaisse.setControleTransaction(true);
        listeDiffencesCentraleCaisse.setMoisAnnee(getMoisAnnee());
        listeDiffencesCentraleCaisse.setMapDifferencesCentraleCaisse(mapDifferencesCentraleCaisse);
        listeDiffencesCentraleCaisse.executeProcess();
    }

    private void doListeDifferencesDroitApplique() throws Exception {
        listeDiffencesDroitApplique = new REListeDifferencesDroitApplique();
        listeDiffencesDroitApplique.setParentWithCopy(this);
        listeDiffencesDroitApplique.setTransaction(getTransaction());
        listeDiffencesDroitApplique.setControleTransaction(true);
        listeDiffencesDroitApplique.setMoisAnnee(getMoisAnnee());
        listeDiffencesDroitApplique.setMapDifferencesDroitApplique(mapDifferencesDroitApplique);
        listeDiffencesDroitApplique.executeProcess();
    }

    private void doListePrestationsNonTrouveesCentrale() throws Exception {
        listePrestationsNonTrouveesCentrale = new REPrestationsNonTrouveesCentrale();
        listePrestationsNonTrouveesCentrale.setParentWithCopy(this);
        listePrestationsNonTrouveesCentrale.setTransaction(getTransaction());
        listePrestationsNonTrouveesCentrale.setControleTransaction(true);
        listePrestationsNonTrouveesCentrale.setMoisAnnee(getMoisAnnee());
        listePrestationsNonTrouveesCentrale.setMapPrestationsNonTrouveesCentrale(mapPrestationsNonTrouveesCentrale);
        listePrestationsNonTrouveesCentrale.executeProcess();
    }

    private void doListePrestationsNonTrouveesFichierCaisse() throws Exception {
        listePrestationsNonTrouveesFichierCaisse = new REPrestationsNonTrouveesFichierCaisse();
        listePrestationsNonTrouveesFichierCaisse.setParentWithCopy(this);
        listePrestationsNonTrouveesFichierCaisse.setTransaction(getTransaction());
        listePrestationsNonTrouveesFichierCaisse.setControleTransaction(true);
        listePrestationsNonTrouveesFichierCaisse.setMoisAnnee(getMoisAnnee());
        listePrestationsNonTrouveesFichierCaisse
                .setMapPrestationsNonTrouveesFichierCaisse(mapPrestationsNonTrouveesFichierCaisse);
        listePrestationsNonTrouveesFichierCaisse.executeProcess();
    }

    @Override
    protected String getEMailObject() {
        return "Listes de comparaison avec la centrale (" + getMoisAnnee() + ")";
    }

    public String getIdLot() {
        return idLot;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
