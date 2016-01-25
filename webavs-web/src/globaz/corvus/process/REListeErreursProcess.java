package globaz.corvus.process;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiersManager;
import globaz.corvus.db.annonces.IREAnnonceAdaptation;
import globaz.corvus.db.annonces.REAnnonce51Adaptation;
import globaz.corvus.db.annonces.REAnnonce51AdaptationManager;
import globaz.corvus.db.annonces.REAnnonce53Adaptation;
import globaz.corvus.db.annonces.REAnnonce53AdaptationManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.itext.REListeDifferencesCentraleCaisse;
import globaz.corvus.itext.REListeDifferencesCentraleCaissePourcent;
import globaz.corvus.itext.REListeDifferencesDroitApplique;
import globaz.corvus.itext.REPrestationsNonTrouveesCentrale;
import globaz.corvus.itext.REPrestationsNonTrouveesFichierCaisse;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @author HPE
 * 
 */
public class REListeErreursProcess extends BProcess {

    private static final long serialVersionUID = 1L;

    public class KeyRAAnnoncesAdaptation implements Comparable<KeyRAAnnoncesAdaptation> {

        private String codePrestation = "";
        private String fractionRente = "";
        private String montantAvant = "";
        private String nss = "";

        /**
         * Crée une nouvelle instance de la classe KeyRAAnnoncesAdaptation.
         */
        public KeyRAAnnoncesAdaptation(String codePrestation, String nss, String montantAvant, String fractionRente) {
            this.nss = nss;
            this.codePrestation = codePrestation;
            this.montantAvant = montantAvant;
            this.fractionRente = fractionRente;
        }

        @Override
        public int compareTo(KeyRAAnnoncesAdaptation keyRaAnnAdapt) {
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
            if (!(obj instanceof KeyRAAnnoncesAdaptation)) {
                return false;
            }

            KeyRAAnnoncesAdaptation keyRaAnnAdapt = (KeyRAAnnoncesAdaptation) obj;

            return ((keyRaAnnAdapt.nss.equals(nss)) && (keyRaAnnAdapt.codePrestation.equals(codePrestation))
                    && (keyRaAnnAdapt.montantAvant.equals(montantAvant)) && (keyRaAnnAdapt.fractionRente
                        .equals(fractionRente)));
        }

        @Override
        public int hashCode() {
            return (nss + codePrestation + montantAvant + fractionRente).hashCode();
        }

    }

    private REListeDifferencesCentraleCaisse listeDiffencesCentraleCaisse = null;
    private REListeDifferencesCentraleCaissePourcent listeDiffencesCentraleCaissePourcent = null;
    private REListeDifferencesDroitApplique listeDiffencesDroitApplique = null;
    private REPrestationsNonTrouveesCentrale listePrestationsNonTrouveesCentrale = null;
    private REPrestationsNonTrouveesFichierCaisse listePrestationsNonTrouveesFichierCaisse = null;
    private Map<String, ArrayList<Object>> mapDifferencesCentraleCaisse = new TreeMap<String, ArrayList<Object>>();
    private Map<String, ArrayList<Object>> mapDifferencesDroitApplique = new TreeMap<String, ArrayList<Object>>();
    private Map<String, REPrestAccJointInfoComptaJointTiers> mapPrestationsNonTrouveesCentrale = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();
    private Map<String, IREAnnonceAdaptation> mapPrestationsNonTrouveesFichierCaisse = new TreeMap<String, IREAnnonceAdaptation>();
    private String moisAnnee = "";
    private String pourcentA = "";
    private String pourcentDe = "";
    private boolean wantListePourcent = false;

    public REListeErreursProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            JADate date = new JADate(getMoisAnnee());
            String dateAdaptation = date.toStrAMJ();

            // 1) Vérifier si les annonces 51/53 ont déjà été importées
            boolean isAnnonces5153 = true;

            REAnnonce51AdaptationManager mgr51 = new REAnnonce51AdaptationManager();
            mgr51.setSession(getSession());
            mgr51.setForMoisAdaptation(dateAdaptation);
            mgr51.setForCodeEnregistrement("01");
            mgr51.find(BManager.SIZE_NOLIMIT);

            if (mgr51.isEmpty()) {
                isAnnonces5153 = false;
                getMemoryLog().logMessage("Aucune annonce 51 dans le système pour la date  : " + dateAdaptation,
                        FWMessage.ERREUR, "REListeErreursProcess");
            } else {
                isAnnonces5153 = true;
            }

            REAnnonce53AdaptationManager mgr53 = new REAnnonce53AdaptationManager();
            mgr53.setSession(getSession());
            mgr53.setForMoisAdaptation(dateAdaptation);
            mgr53.setForCodeEnregistrement("01");
            mgr53.find(BManager.SIZE_NOLIMIT);

            if (mgr53.isEmpty()) {
                isAnnonces5153 = false;
                getMemoryLog().logMessage("Aucune annonce 53 dans le système pour la date  : " + dateAdaptation,
                        FWMessage.ERREUR, "REListeErreursProcess");
            } else {
                isAnnonces5153 = true;
            }

            if (isAnnonces5153) {

                // Création du process des listes d'erreurs de l'adaptation
                getMemoryLog().logMessage("Listes d'erreurs de l'adaptation", FWMessage.INFORMATION,
                        "REListeErreursProcess");

                try {

                    // 2) Process de contrôles des données
                    doControleRentesAnnonces5153(mgr51, mgr53);

                    // 3) Génération des listes d'erreurs
                    doListePrestationsNonTrouveesFichierCaisse();
                    doListePrestationsNonTrouveesCentrale();
                    doListeDifferencesCentraleEtCaisse();
                    if (wantListePourcent) {
                        doListeDifferencesCentraleEtCaissePourcent();
                    }
                    doListeDifferencesDroitApplique();

                } catch (Exception e) {
                    getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListeErreursProcess");
                    return false;

                } finally {
                    // 4) Fusion des documents
                    JadePublishDocumentInfo info = createDocumentInfo();
                    info.setPublishDocument(true);
                    info.setArchiveDocument(false);

                    this.mergePDF(info, true, 500, false, null);
                }

                return true;

            } else {

                getMemoryLog().logMessage(
                        "Aucune annonce 51 ou 53 pour le mois donné. La liste d'erreur ne sera pas imprimée.",
                        FWMessage.INFORMATION, "REListeErreursProcess");

                return false;
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListeErreursProcess");
            return false;
        }
    }

    private void doControleRentesAnnonces5153(REAnnonce51AdaptationManager mgr51, REAnnonce53AdaptationManager mgr53)
            throws Exception {

        Map<KeyRAAnnoncesAdaptation, IREAnnonceAdaptation> mapAnnonces = new TreeMap<KeyRAAnnoncesAdaptation, IREAnnonceAdaptation>();
        Map<KeyRAAnnoncesAdaptation, REPrestAccJointInfoComptaJointTiers> mapRenteAcc = new TreeMap<KeyRAAnnoncesAdaptation, REPrestAccJointInfoComptaJointTiers>();

        // Chargement des map avec les manager
        KeyRAAnnoncesAdaptation key = null;

        for (Iterator iterator = mgr51.iterator(); iterator.hasNext();) {
            REAnnonce51Adaptation ann51 = (REAnnonce51Adaptation) iterator.next();

            FWCurrency montant = new FWCurrency(ann51.getAncienMontantMensuel());

            key = new KeyRAAnnoncesAdaptation(ann51.getGenrePrestation(), NSUtil.formatAVSUnknown(ann51.getNss()),
            // "","");
                    montant.toString(), ann51.getFractionRente());

            mapAnnonces.put(key, ann51);

        }

        for (Iterator iterator = mgr53.iterator(); iterator.hasNext();) {
            REAnnonce53Adaptation ann53 = (REAnnonce53Adaptation) iterator.next();

            FWCurrency montant = new FWCurrency(ann53.getAncienMontantMensuel());

            key = new KeyRAAnnoncesAdaptation(ann53.getGenrePrestation(), NSUtil.formatAVSUnknown(ann53.getNss()),
            // "","");
                    montant.toString(), ann53.getFractionRente());

            mapAnnonces.put(key, ann53);

        }

        REPrestAccJointInfoComptaJointTiersManager raMgr = new REPrestAccJointInfoComptaJointTiersManager();
        raMgr.setSession(getSession());
        raMgr.setForEnCoursAtMois(getMoisAnnee());
        raMgr.setForDateDebutBefore(getMoisAnnee());
        raMgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + "," + IREPrestationAccordee.CS_ETAT_PARTIEL);
        raMgr.setOrderBy(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        raMgr.find(BManager.SIZE_NOLIMIT);

        for (Iterator iterator = raMgr.iterator(); iterator.hasNext();) {
            REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) iterator.next();

            String fraction;
            if (JadeStringUtil.isBlankOrZero(ra.getFractionRente())) {
                fraction = "1";
            } else {
                fraction = ra.getFractionRente();
            }

            key = new KeyRAAnnoncesAdaptation(ra.getCodePrestation(), ra.getNss(),
            // "","");
                    ra.getMontantPrestation(), fraction);

            mapRenteAcc.put(key, ra);

        }

        ArrayList<Object> listRaAnn = null;
        String keyMap = "";

        // Parcourir toutes les annonces 51 et 53 et rechercher les
        // correspondants dans les RA
        for (KeyRAAnnoncesAdaptation keyAnnonce : mapAnnonces.keySet()) {

            // => Si correspondance dans les RA, mettre dans la
            // mapDifferencesCentraleCaisse
            if (mapRenteAcc.containsKey(keyAnnonce)) {
                listRaAnn = new ArrayList<Object>();
                listRaAnn.add(mapAnnonces.get(keyAnnonce));
                listRaAnn.add(mapRenteAcc.get(keyAnnonce));

                REPrestAccJointInfoComptaJointTiers ra = mapRenteAcc.get(keyAnnonce);

                keyMap = ra.getNom() + ra.getPrenom() + ra.getCodePrestation() + ra.getIdPrestationAccordee();

                if (((ra.getDroitApplique().equals("10") && (mapAnnonces.get(keyAnnonce) instanceof REAnnonce51Adaptation)))
                        || ((ra.getDroitApplique().equals("9") && (mapAnnonces.get(keyAnnonce) instanceof REAnnonce53Adaptation)))) {

                    mapDifferencesDroitApplique.put(keyMap, listRaAnn);
                } else {

                    mapDifferencesCentraleCaisse.put(keyMap, listRaAnn);
                }

                // => Si pas correspondance, mettre dans la
                // mapPrestationsNonTrouveesFichierCaisse
            } else {
                if (mapAnnonces.get(keyAnnonce) instanceof REAnnonce51Adaptation) {

                    PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                            NSUtil.formatAVSUnknown(((REAnnonce51Adaptation) mapAnnonces.get(keyAnnonce)).getNss()));

                    if (tier != null) {
                        keyMap = tier.getProperty(PRTiersWrapper.PROPERTY_NOM)
                                + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM)
                                + ((REAnnonce51Adaptation) mapAnnonces.get(keyAnnonce)).getGenrePrestation()
                                + ((REAnnonce51Adaptation) mapAnnonces.get(keyAnnonce)).getIdAnnonce01();
                    } else {
                        keyMap = ((REAnnonce51Adaptation) mapAnnonces.get(keyAnnonce)).getGenrePrestation()
                                + ((REAnnonce51Adaptation) mapAnnonces.get(keyAnnonce)).getIdAnnonce01();
                    }

                    mapPrestationsNonTrouveesFichierCaisse.put(keyMap, mapAnnonces.get(keyAnnonce));
                } else {

                    PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                            NSUtil.formatAVSUnknown(((REAnnonce53Adaptation) mapAnnonces.get(keyAnnonce)).getNss()));

                    if (tier != null) {
                        keyMap = tier.getProperty(PRTiersWrapper.PROPERTY_NOM)
                                + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM)
                                + ((REAnnonce53Adaptation) mapAnnonces.get(keyAnnonce)).getGenrePrestation()
                                + ((REAnnonce53Adaptation) mapAnnonces.get(keyAnnonce)).getIdAnnonce01();
                    } else {
                        keyMap = ((REAnnonce53Adaptation) mapAnnonces.get(keyAnnonce)).getGenrePrestation()
                                + ((REAnnonce53Adaptation) mapAnnonces.get(keyAnnonce)).getIdAnnonce01();
                    }

                    mapPrestationsNonTrouveesFichierCaisse.put(keyMap, mapAnnonces.get(keyAnnonce));
                }
            }
        }

        // Parcourir toutes les rentes accordées en cours et rechercher les
        // correspondances dans les annonces
        Set setKeyMapRA = mapRenteAcc.keySet();
        Iterator iterKeyMapRA = setKeyMapRA.iterator();

        int nbCorrespondance = 0;
        while (iterKeyMapRA.hasNext()) {
            KeyRAAnnoncesAdaptation keyRA = (KeyRAAnnoncesAdaptation) iterKeyMapRA.next();

            // => Si correspondance, mise à jour d'un compteur pour contrôle
            if (mapAnnonces.containsKey(keyRA)) {
                nbCorrespondance++;

                // => Si pas correspondance, mettre dans la
                // mapPrestationsNonTrouveesCentrale
            } else {

                keyMap = (mapRenteAcc.get(keyRA)).getNom() + (mapRenteAcc.get(keyRA)).getPrenom()
                        + (mapRenteAcc.get(keyRA)).getCodePrestation()
                        + (mapRenteAcc.get(keyRA)).getIdPrestationAccordee();

                mapPrestationsNonTrouveesCentrale.put(keyMap, mapRenteAcc.get(keyRA));
            }

        }

        getMemoryLog().logMessage("Total des prestations trouvées : " + mapDifferencesCentraleCaisse.size(),
                FWMessage.INFORMATION, "REListeErreursProcess");
        getMemoryLog().logMessage(
                "Total des prestations non trouvées à la centrale : " + mapPrestationsNonTrouveesCentrale.size(),
                FWMessage.INFORMATION, "REListeErreursProcess");
        getMemoryLog()
                .logMessage(
                        "Total des prestations non trouvées dans le fichier de la caisse : "
                                + mapPrestationsNonTrouveesFichierCaisse.size(), FWMessage.INFORMATION,
                        "REListeErreursProcess");
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

    private void doListeDifferencesCentraleEtCaissePourcent() throws Exception {
        listeDiffencesCentraleCaissePourcent = new REListeDifferencesCentraleCaissePourcent();
        listeDiffencesCentraleCaissePourcent.setParentWithCopy(this);
        listeDiffencesCentraleCaissePourcent.setTransaction(getTransaction());
        listeDiffencesCentraleCaissePourcent.setControleTransaction(true);
        listeDiffencesCentraleCaissePourcent.setMoisAnnee(getMoisAnnee());
        listeDiffencesCentraleCaissePourcent.setMapDifferencesCentraleCaisse(mapDifferencesCentraleCaisse);
        listeDiffencesCentraleCaissePourcent.setPourcentDe(getPourcentDe());
        listeDiffencesCentraleCaissePourcent.setPourcentA(getPourcentA());
        listeDiffencesCentraleCaissePourcent.executeProcess();
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
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_LISTE_ERR_OBJET_MAIL") + " - " + getMoisAnnee();
    }

    public Map<String, ArrayList<Object>> getMapDifferencesCentraleCaisse() {
        return mapDifferencesCentraleCaisse;
    }

    public Map<String, ArrayList<Object>> getMapDifferencesDroitApplique() {
        return mapDifferencesDroitApplique;
    }

    public Map<String, REPrestAccJointInfoComptaJointTiers> getMapPrestationsNonTrouveesCentrale() {
        return mapPrestationsNonTrouveesCentrale;
    }

    public Map<String, IREAnnonceAdaptation> getMapPrestationsNonTrouveesFichierCaisse() {
        return mapPrestationsNonTrouveesFichierCaisse;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getPourcentA() {
        return pourcentA;
    }

    public String getPourcentDe() {
        return pourcentDe;
    }

    public boolean getWantListePourcent() {
        return wantListePourcent;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setMapDifferencesCentraleCaisse(Map<String, ArrayList<Object>> mapDifferencesCentraleCaisse) {
        this.mapDifferencesCentraleCaisse = mapDifferencesCentraleCaisse;
    }

    public void setMapDifferencesDroitApplique(Map<String, ArrayList<Object>> mapDifferencesDroitApplique) {
        this.mapDifferencesDroitApplique = mapDifferencesDroitApplique;
    }

    public void setMapPrestationsNonTrouveesCentrale(
            Map<String, REPrestAccJointInfoComptaJointTiers> mapPrestationsNonTrouveesCentrale) {
        this.mapPrestationsNonTrouveesCentrale = mapPrestationsNonTrouveesCentrale;
    }

    public void setMapPrestationsNonTrouveesFichierCaisse(
            Map<String, IREAnnonceAdaptation> mapPrestationsNonTrouveesFichierCaisse) {
        this.mapPrestationsNonTrouveesFichierCaisse = mapPrestationsNonTrouveesFichierCaisse;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setPourcentA(String pourcentA) {
        this.pourcentA = pourcentA;
    }

    public void setPourcentDe(String pourcentDe) {
        this.pourcentDe = pourcentDe;
    }

    public void setWantListePourcent(boolean wantListePourcent) {
        this.wantListePourcent = wantListePourcent;
    }

}
