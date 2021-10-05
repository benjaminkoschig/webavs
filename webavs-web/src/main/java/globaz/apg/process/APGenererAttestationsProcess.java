/*
 * Crée le 21 septembre 2006
 */
package globaz.apg.process;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.*;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.itext.APAttestations;
import globaz.apg.utils.APGUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.*;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author hpe
 * 
 */
public class APGenererAttestationsProcess extends BProcess {

    // ~ Instance fiels
    // -------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Set cantonsLettreEntete = new HashSet();

    // Classe interne pour les infos repris des requêtes pour le tri par tiers
    public class AttestationsInfos implements Comparable {
        public String dateDebut = "";
        public String dateFin = "";
        public Set idsRPVentilations = new TreeSet();
        public String idTiers = "";
        public boolean isMaternite = false;
        public BigDecimal montantTotal = new BigDecimal(0);
        public BigDecimal montantVentilations = new BigDecimal(0);
        public BigDecimal totalMontantAPG = new BigDecimal(0);
        public BigDecimal totalMontantCotisations = new BigDecimal(0);
        public BigDecimal totalMontantImpotSource = new BigDecimal(0);
        private boolean isAddLettreEntete = false;
        private boolean isCopyFisc = false;
        private boolean isHasCopyFisc = false;
        private String canton = "";

        @Override
        public int compareTo(Object o) {
            AttestationsInfos ai = (AttestationsInfos) o;

            if (dateDebut.compareTo(ai.dateDebut) != 0) {
                return dateDebut.compareTo(ai.dateDebut);
            } else if (dateFin.compareTo(ai.dateFin) != 0) {
                return dateFin.compareTo(ai.dateFin);
            } else {
                return 0;
            }
        }

        public boolean isAddLettreEntete() {
            return isAddLettreEntete;
        }

        public void setIsAddLettreEntete(boolean addLettreEntete) {
            isAddLettreEntete = addLettreEntete;
        }

        public boolean isCopyFisc() {
            return isCopyFisc;
        }

        public void setIsCopyFisc(boolean copy) {
            isCopyFisc = copy;
        }

        public boolean isHasCopyFisc() {
            return isHasCopyFisc;
        }

        public void setIsHasCopyFisc(boolean hasCopy) {
            isHasCopyFisc = hasCopy;
        }

        public String getCanton() {
            return canton;
        }

        public void setCanton(String canton) {
            this.canton = canton;
        }
    }

    // Classe interne qui défini les clés pour le tri par idTiers
    public class Key implements Comparable {
        public String idTiers;

        @Override
        public int compareTo(Object o) {
            if (o instanceof Key) {
                return Integer.parseInt(idTiers) - Integer.parseInt(((Key) o).idTiers);
            } else {
                return -1;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof Key) {
                return (Integer.parseInt(idTiers) - Integer.parseInt(((Key) obj).idTiers) == 0);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Integer.parseInt(idTiers);
        }
    }

    private static final String ORDER_PRINTING_BY = "orderPrintingBy";
    private String annee = "";
    private String eMailObject = "";
    // private String montantVentilation = "";
    private String idTiers = "";
    private List<APSitProJointEmployeur> situationsProf;
    private Boolean isGenerationUnique = Boolean.TRUE;
    private Boolean isSendToGed = Boolean.FALSE;

    Map<Key, ArrayList<AttestationsInfos>> map = new TreeMap();
    Map<Key, ArrayList<AttestationsInfos>> mapFisc = new LinkedHashMap<>();

    private JadePublishDocumentInfo mergedDocInfo = null;
    private String montantTotal = "";
    private String NSS = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private PRTiersWrapper tiers;

    private String totalMontantAPG = "";

    private String typePrestation = "";

    /**
     * Crée une nouvelle instance de la classe APGenererAttestationsProcess.
     */
    public APGenererAttestationsProcess() {
        super();
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererAttestationsProcess.
     *
     * @param parent DOCUMENT ME!
     */
    public APGenererAttestationsProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererAttestationsProcess.
     *
     * @param session DOCUMENT ME!
     */
    public APGenererAttestationsProcess(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        // Reprendre l'année setter par l'utilisateur et transformer en :
        // --> 1.1.XXXX pour dateDebut
        // --> 31.12.XXXX pour dateFin
        String annee = getAnnee();
        String dateDebut = annee + "0101";
        String dateFin = annee + "1231";
        boolean isAttestationPat = false;
        boolean isAttestationPai = false;

        APRepartitionJointPrestationJointLotDemandeManager mgr = new APRepartitionJointPrestationJointLotDemandeManager();

        // Prendre les répartitions qui ont été comptabilisées durant l'année
        // passée en paramètre

        mgr.setSession(getSession());
        mgr.setForDateDebutComptaLot(dateDebut);
        mgr.setForDateFinComptaLot(dateFin);
        mgr.setForEtatPrestation(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        mgr.setForTypeDemande(typePrestation);
        mgr.setOrderBy(APPrestation.FIELDNAME_DATEDEBUT + "," + APPrestation.FIELDNAME_DATEFIN);

        List<String> typeDePrestations = new ArrayList<>();

        if (getIsGenerationUnique().booleanValue()) {
            tiers = PRTiersHelper.getTiers(getSession(), getNSS());
            if (null == tiers) {
                getMemoryLog().logMessage(getSession().getLabel("ERREUR_TIERS_PAS_TROUVE") + " " + getNSS(),
                        FWViewBeanInterface.ERROR, "APGenererAttestationsProcess");
                setEMailObject(getSession().getLabel("EMAIL_OBJECT_ATT_FISCALES_1"));
                abort();
                return false;
            }
            mgr.setForIdTiers(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        BStatement statement = mgr.cursorOpen(getTransaction());

        APRepartitionJointPrestation repPres = null;

        int nbRep = 0;

        while ((repPres = (APRepartitionJointPrestation) (mgr.cursorReadNext(statement))) != null) {

            nbRep++;

            // Reprendre chaque répartition
            APRepartitionPaiements rep = new APRepartitionPaiements();
            rep.setIdRepartitionBeneficiairePaiement(repPres.getIdRepartitionBeneficiairePaiement());
            rep.setSession(getSession());
            rep.retrieve();

            // recherche la sit. pro.
            APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
            sitPro.setSession(getSession());
            sitPro.setIdSituationProf(rep.getIdSituationProfessionnelle());
            sitPro.retrieve();

            APSitProJointEmployeurManager sitProJointEmployeurManager = new APSitProJointEmployeurManager();
            sitProJointEmployeurManager.setSession(getSession());
            sitProJointEmployeurManager.setForIdDroit(repPres.getIdDroit());
            sitProJointEmployeurManager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);

            // Si pas une ventilation,
            // et (un payement direct ou un payement a l'employeur pour un
            // independant)
            if (JadeStringUtil.isIntegerEmpty(rep.getIdParent())
                    && (IAPRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(rep.getTypePaiement()) || sitPro
                    .getIsIndependant().booleanValue())) {

                montantTotal = rep.getMontantRestant();
                totalMontantAPG = rep.getMontantBrut();
                idTiers = repPres.getIdTiers();
                tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);
                situationsProf = sitProJointEmployeurManager.getContainerAsList();

                // Rechercher les ventilations pour cette répartition
                APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();

                repartitions.setForIdParent(rep.getIdRepartitionBeneficiairePaiement());
                repartitions.setNotForIdRepartition(rep.getIdRepartitionBeneficiairePaiement());
                repartitions.setSession(getSession());
                // montantVentilation = (repartitions.getSum("VIMMOV",
                // getTransaction())).toString();

                APPrestation prest = new APPrestation();
                prest.setSession(getSession());
                prest.setIdPrestationApg(rep.getIdPrestationApg());
                prest.retrieve(getTransaction());

                Set idsVentilation = new TreeSet();
                FWCurrency montantVentilation = new FWCurrency(0);
                repartitions.find(getTransaction());
                for (int i = 0; i < repartitions.size(); i++) {
                    APRepartitionPaiements elm = (APRepartitionPaiements) repartitions.get(i);

                    if (!idsVentilation.contains(elm.getIdRepartitionBeneficiairePaiement())) {
                        montantVentilation.add(elm.getMontantVentile());
                        idsVentilation.add(elm.getIdRepartitionBeneficiairePaiement());
                    }
                }

                // Reprendre les cotisations pour la répartition
                APCotisationManager cotMan = new APCotisationManager();
                cotMan.setForIdRepartitionBeneficiairePaiement(rep.getIdRepartitionBeneficiairePaiement());
                cotMan.setSession(getSession());
                cotMan.find();

                FWCurrency totalMontantCotisations = new FWCurrency();
                FWCurrency totalMontantImpotSource = new FWCurrency();

                for (Iterator iterator = cotMan.iterator(); iterator.hasNext(); ) {
                    APCotisation cot = (APCotisation) iterator.next();

                    if (cot.getType().equals(APCotisation.TYPE_IMPOT)) {
                        totalMontantImpotSource.add(cot.getMontant());
                    } else {
                        totalMontantCotisations.add(cot.getMontant());
                    }

                }

                boolean isRestitution = false;
                if (Double.parseDouble(totalMontantAPG) >= 0) {
                    isRestitution = false;
                } else {
                    isRestitution = true;
                }
                isAttestationPat = isPrestationLapat(prest);
                isAttestationPai = isPrestationLapai(prest);
                if (isAttestationPat || isAttestationPai || isPrestationLamat(prest) ||
                        (((Double.parseDouble(totalMontantAPG) != 0) && ((!totalMontantCotisations.isZero()) || (!totalMontantImpotSource
                                .isZero())))
                                && ((((!isRestitution) && (totalMontantCotisations.isNegative())) || ((isRestitution) && (totalMontantCotisations
                                .isPositive()))) || ((!isRestitution) && (totalMontantImpotSource.isNegative())) || ((isRestitution) && (totalMontantImpotSource
                                .isPositive()))))) {

                    // Création de la clé
                    Key k = new Key();
                    k.idTiers = rep.getIdTiers();

                    // Si la clé est encore inexistante
                    if (!map.containsKey(k)) {

                        // crée une attestation dans la map pour les attestations
                        createAttestationInfoAndPutInMap(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k, map, false);

                    } else { // si la clé existe déjà

                        // ajoute une attestation dans la map pour les attestations
                        putAttestationInfoInList(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k, map, false);

                    }

                    if (isPrestationLapat(prest) || (isPrestationAPG(prest) || isPrestationPandemie(prest) || isPrestationAmat(prest) || isPrestationLapai(prest))) {

                        // Si la clé est encore inexistante
                        if (!mapFisc.containsKey(k)) {

                            // crée une copie d'attestation dans la map pour les copies d'attestations au fisc
                            createAttestationInfoAndPutInMap(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k, mapFisc, true);

                        } else { // si la clé existe déjà

                            // ajoute une copie d'attestation dans la map pour les copies d'attestations au fisc
                            putAttestationInfoInList(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k, mapFisc, true);

                        }
                    }
                }
            }
        }

        if (nbRep == 0) {
            if (getIsGenerationUnique().booleanValue()) {
                getMemoryLog().logMessage(getSession().getLabel("ERREUR_AUCUNE_ATT_POUR_TIERS"),
                        FWViewBeanInterface.WARNING, "APGenererAttestationsFiscales");
                setEMailObject(getSession().getLabel("EMAIL_OBJECT_ATT_FISCALES_2") + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            } else {
                getMemoryLog().logMessage(getSession().getLabel("ERREUR_AUCUNE_ATT_POUR_ANNEE"),
                        FWViewBeanInterface.WARNING, "APGenererAttestationsFiscales");
                setEMailObject(getSession().getLabel("EMAIL_OBJECT_ATT_FISCALES_3") + " " + getAnnee());

            }
        }

        // enlève de la map des copies au fisc les attestations qui ne possède aucun impôts source sur leurs prestations
        mapFisc.entrySet().removeIf(entry -> (!findOneImpotSourceForTiers(mapFisc, entry.getKey().idTiers)));

        // génère les attestations originales
        createAttestation(annee, dateDebut, dateFin, isAttestationPat, isAttestationPai, map, false);

        // si il y a des copies d'attestations à générer
        if (!mapFisc.isEmpty()) {

            // regroupe les copies d'attestation par canton
            LinkedHashMap<String, LinkedHashMap<Key, ArrayList<AttestationsInfos>>> mapFiscRegroupedByCanton = groupMapFiscByCanton(mapFisc);

            // génère les copies d'attestations regroupées par canton (cas avec impôt source)
            for (Map.Entry<String, LinkedHashMap<Key, ArrayList<AttestationsInfos>>> entry : mapFiscRegroupedByCanton.entrySet()) {
                createAttestation(annee, dateDebut, dateFin, isAttestationPat, isAttestationPai, entry.getValue(), true);
            }
        }

        return true;
    }

    private boolean findOneImpotSourceForTiers(Map<Key, ArrayList<AttestationsInfos>> mapFisc, String idTiers) {
        boolean foundOneImpotSource = false;
        for (Map.Entry<Key, ArrayList<AttestationsInfos>> entry : mapFisc.entrySet()) {
            for (AttestationsInfos attestationsInfos : entry.getValue()) {
                if (entry.getKey().idTiers.equals(idTiers) && !attestationsInfos.totalMontantImpotSource.equals(new BigDecimal(0))) {
                    foundOneImpotSource = true;
                    break;
                }
            }
            if (foundOneImpotSource) {
                break;
            }
        }
        return foundOneImpotSource;
    }

    private LinkedHashMap<String, LinkedHashMap<Key, ArrayList<AttestationsInfos>>> groupMapFiscByCanton(Map<Key, ArrayList<AttestationsInfos>> mapFisc) {
        LinkedHashMap<String, LinkedHashMap<Key, ArrayList<AttestationsInfos>>> mapFiscByCanton = new LinkedHashMap<>();
        for (Map.Entry<Key, ArrayList<AttestationsInfos>> mapFiscEntry : mapFisc.entrySet()) {
            ArrayList<AttestationsInfos> attestationInfos = mapFiscEntry.getValue();
            Key key = mapFiscEntry.getKey();

            // on prends le premier car toutes les prestations d'un tiers devrait possèder le même canton
            String canton = mapFiscEntry.getValue().get(0).getCanton();

            LinkedHashMap<Key, ArrayList<AttestationsInfos>> linkedHashMap = mapFiscByCanton.get(canton);
            if (linkedHashMap == null) {
                linkedHashMap = new LinkedHashMap<>();
            }
            linkedHashMap.put(key, attestationInfos);
            mapFiscByCanton.put(canton, linkedHashMap);
        }
        return mapFiscByCanton;
    }

    private void createAttestationInfoAndPutInMap(APPrestation prest, Set idsVentilation, FWCurrency montantVentilation, FWCurrency totalMontantCotisations, FWCurrency totalMontantImpotSource, Key k, Map map, boolean isCopyFisc) throws JAException {
        // On crée un objet
        AttestationsInfos ai = new AttestationsInfos();

        ai.idTiers = idTiers;
        ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
        ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
        ai.totalMontantAPG = ai.totalMontantAPG.add(new BigDecimal(totalMontantAPG.toString()));
        ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                totalMontantCotisations.toString()));
        ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                totalMontantImpotSource.toString()));
        ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
        ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                .toString()));
        ai.isMaternite = IPRDemande.CS_TYPE_MATERNITE.equals(typePrestation);

        initCopyFisc(prest, totalMontantImpotSource, isCopyFisc, ai);

        ai.idsRPVentilations = idsVentilation;
        // Comme la clé est inexistante, on crée la liste
        // d'objet
        ArrayList list = new ArrayList();
        list.add(ai);

        // On insère la clé et la liste dans la map
        map.put(k, list);
    }

    private void putAttestationInfoInList(APPrestation prest, Set idsVentilation, FWCurrency montantVentilation, FWCurrency totalMontantCotisations, FWCurrency totalMontantImpotSource, Key k, Map map, boolean isCopyFisc) throws JAException {
        // On récupère la liste
        ArrayList list = (ArrayList) map.get(k);

        ArrayList listCopy = new ArrayList();
        listCopy.addAll(list);

        boolean isFusion = false;

        // On itère sur les objets dans la liste
        for (Iterator iterator = listCopy.iterator(); iterator.hasNext();) {
            AttestationsInfos ai = (AttestationsInfos) iterator.next();

            ai.idsRPVentilations.addAll(idsVentilation);
            // Dans un premier temps, on regroupe uniquement les
            // périodes identiques

            // date de l'objet en cours d'itération
            String dateDebutAi = PRDateFormater.formatDateFrom(ai.dateDebut);
            String dateFinAi = PRDateFormater.formatDateFrom(ai.dateFin);

            // date de l'objet à fusionner ou ajouter
            String dateDebutPrest = prest.getDateDebut();
            String dateFinPrest = prest.getDateFin();

            // Donc si les dates sont identiques...
            if (/* ai.idsRPVentilations.size()>0 && */dateDebutPrest.equals(dateDebutAi)
                    && dateFinPrest.equals(dateFinAi)) {

                // on ajoute simplement tous les montants à
                // l'objet ai
                ai.idTiers = idTiers;
                ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateDebutPrest);
                ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinPrest);
                ai.totalMontantAPG = ai.totalMontantAPG.add(new BigDecimal(totalMontantAPG.toString()));
                ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                        totalMontantCotisations.toString()));
                ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                        totalMontantImpotSource.toString()));
                ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
                ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                        .toString()));
                ai.isMaternite = IPRDemande.CS_TYPE_MATERNITE.equals(typePrestation);

                initCopyFisc(prest, totalMontantImpotSource, isCopyFisc, ai);

                // Pas besoin de l'ajouter dans la liste, car
                // modifié en temps réel !

                // Sinon on crée un nouvel objet

                isFusion = true;

            }
        }

        if (!isFusion) {

            AttestationsInfos ai1 = new AttestationsInfos();

            ai1.idTiers = idTiers;
            ai1.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
            ai1.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
            ai1.totalMontantAPG = ai1.totalMontantAPG.add(new BigDecimal(totalMontantAPG.toString()));
            ai1.totalMontantCotisations = ai1.totalMontantCotisations.add(new BigDecimal(
                    totalMontantCotisations.toString()));
            ai1.totalMontantImpotSource = ai1.totalMontantImpotSource.add(new BigDecimal(
                    totalMontantImpotSource.toString()));
            ai1.montantTotal = ai1.montantTotal.add(new BigDecimal(montantTotal.toString()));
            ai1.montantVentilations = ai1.montantVentilations.add(new BigDecimal(montantVentilation
                    .toString()));
            ai1.isMaternite = IPRDemande.CS_TYPE_MATERNITE.equals(typePrestation);
            ai1.idsRPVentilations = idsVentilation;

            initCopyFisc(prest, totalMontantImpotSource, isCopyFisc, ai1);

            list.add(ai1);
        }
    }

    private void initCopyFisc(APPrestation prest, FWCurrency totalMontantImpotSource, boolean isCopyFisc, AttestationsInfos ai) {
        try {
            if (isPrestationLapat(prest) || (isPrestationAPG(prest) || isPrestationPandemie(prest) || isPrestationAmat(prest) || isPrestationLapai(prest))) {

                // cherche le canton impôt source de l'attestation d'imposition
                String canton = searchCantonImpotSourceCascade(prest);
                ai.setCanton(canton);

                if (isCopyFisc) {
                    // set le flag isCopyFisc qui définit si le document est une copie au fisc
                    ai.setIsCopyFisc(isCopyFisc);

                    if (!totalMontantImpotSource.isZero()) {
                        // évite l'envoi de lettre entete en doublon pour le même canton
                        if (!cantonsLettreEntete.contains(canton)) {
                            cantonsLettreEntete.add(canton);
                            // set le flag isAddLettreEntete qui déclanche la création d'une lettre d'entête
                            ai.setIsAddLettreEntete(true);
                        }
                    }
                }

                // set le flag isHasCopyFisc pour les documents original et pour la copie
                if (!totalMontantImpotSource.isZero()) {
                    ai.setIsHasCopyFisc(true);
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de l'initialisation de la copie au fisc : " + e.toString(), FWMessage.ERREUR,
                    "APGenererAttestationsProcess");
        }
    }

    private String searchCantonImpotSourceCascade(APPrestation prest) {
        String canton = "";

        try {
            APDroitLAPG droit = APGUtils.loadDroit(getSession(), getTransaction(), prest.getIdDroit(), rechercheTypeDroit(prest));

            if (droit.getIsSoumisImpotSource()) {

                // recherche du canton dans le droit
                canton = droit.getCsCantonDomicile();

                // si canton vide dans le droit ou si la valeur est set à ETRANGER
                if (JadeStringUtil.isBlankOrZero(canton) || PRACORConst.CODE_CANTON_ETRANGER.equals(canton)) {

                    // recherche du canton dans l'adresse de domicile
                    canton = PRTiersHelper.getTiersCanton(getSession(), idTiers);

                    // si canton vide il n'y a pas d'adresse de domicile ou si l'adresse de domicile est à ETRANGER alors on vas rechercher l'adresse de l'employeur
                    if (JadeStringUtil.isBlankOrZero(canton) || PRACORConst.CODE_CANTON_ETRANGER.equals(canton)) {

                        // recherche du canton dans l'adresse de l'employeur
                        canton = rechercheCantonAdressePaiementSitProf(rechercheDomaine(prest), situationsProf, prest.getDateDebut());

                        if (JadeStringUtil.isBlankOrZero(canton)) {
                            getMemoryLog().logMessage("impossible de déterminer le canton d'imposition", FWMessage.ERREUR,
                                    "APGenererAttestationsProcess");
                        }
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la recherche du canton d'imposition à l'impôt source : " + e.toString(), FWMessage.ERREUR,
                    "APGenererAttestationsProcess");
        }

        return canton;
    }

    private String rechercheTypeDroit(APPrestation prest) throws Exception {
        String typeDroit = "ANY";
        if (isPrestationAPG(prest)) {
            typeDroit = "ANY";
        } else if (isPrestationAmat(prest)) {
            typeDroit = IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE;
        } else if (isPrestationLapat(prest)) {
            typeDroit = IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE;
        } else if (isPrestationLapai(prest)) {
            typeDroit = IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT;
        } else if (isPrestationPandemie(prest)) {
            typeDroit = "ANY";
        }
        return typeDroit;
    }

    private String rechercheDomaine(APPrestation prest) throws Exception {
        String domaine = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
        if (isPrestationAPG(prest)) {
            domaine = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
        } else if (isPrestationAmat(prest)) {
            domaine = IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE;
        } else if (isPrestationLapat(prest)) {
            domaine = IPRConstantesExternes.TIERS_CS_DOMAINE_PATERNITE;
        } else if (isPrestationLapai(prest)) {
            domaine = IPRConstantesExternes.TIERS_CS_DOMAINE_PROCHE_AIDANT;
        } else if (isPrestationPandemie(prest)) {
            domaine = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
        }
        return domaine;
    }

    /**
     * recherche le canton dans les situations professionnelles
     * @param situationsProf
     * @return
     * @throws Exception
     */
    private String rechercheCantonAdressePaiementSitProf(String domaine, List<APSitProJointEmployeur> situationsProf, String dateDebut) throws Exception {
        String canton = "";
        // vérification du canton de la situation professionnelle
        for (APSitProJointEmployeur sit : situationsProf) {
            TIAdressePaiementData data = PRTiersHelper.getAdressePaiementData(getSession(), getSession().getCurrentThreadTransaction(), sit.getIdTiers(),
                    domaine, sit.getIdAffilie(), dateDebut);

            if (!data.isNew()) {
                String cantonComparaison = PRTiersHelper.getCanton(getSession(), data.getNpa());
                if(cantonComparaison == null) {
                    // canton de l'adresse de paiement de la banque (indépendant étranger ?)
                    cantonComparaison = PRTiersHelper.getCanton(getSession(), data.getNpa_banque());
                }
                // toutes les situations professionnelles du droit doivent avoir le même canton sinon impossible de déterminer
                if (!canton.isEmpty() && !canton.equals(cantonComparaison)) {
                    getMemoryLog().logMessage("impossible de déterminer le canton d'imposition : plusieurs cantons différents pour plusieurs employeurs", FWMessage.ERREUR,
                            "APGenererAttestationsProcess");
                } else {
                    canton = cantonComparaison;
                }
            }

        }
        return canton;
    }

    private void createAttestation(String annee, String dateDebut, String dateFin, boolean isAttestationPat, boolean isAttestationPai, Map map, boolean isAttestationCopy) throws Exception {
        APAttestations attestations = new APAttestations(getSession());
        attestations.setAttestationsMap(map);
        attestations.setDateDebut(dateDebut);
        attestations.setDateFin(dateFin);
        attestations.setAnnee(annee);
        attestations.setParent(this);
        attestations.setTailleLot(1);
        attestations.setIsSendToGED(getIsSendToGed());
        attestations.setType(typePrestation);
        attestations.setAttestationPat(isAttestationPat);
        attestations.setAttestationPai(isAttestationPai);
        attestations.setAttestationCopy(isAttestationCopy);
        attestations.executeProcess();
    }

    private boolean isPrestationLamat(APPrestation prest) {
        return (Double.parseDouble(totalMontantAPG) != 0) && APTypeDePrestation.LAMAT.isCodeSystemEqual(prest.getGenre());
    }
    private boolean isPrestationAmat(APPrestation prest) throws Exception {
        return (Double.parseDouble(totalMontantAPG) != 0) && APGUtils.isTypePrestation(prest.getIdPrestation(),getSession(), IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
    }
    private boolean isPrestationLapat(APPrestation prest) throws Exception {
        return (Double.parseDouble(totalMontantAPG) != 0) && APGUtils.isTypePrestation(prest.getIdPrestation(),getSession(), IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE);
    }
    private boolean isPrestationAPG(APPrestation prest) throws Exception {
        return (Double.parseDouble(totalMontantAPG) != 0)
                && (IPRDemande.CS_TYPE_APG.equals(typePrestation)
                || IAPPrestation.CS_TYPE_NORMAL.equals(prest.getType())
                || IAPPrestation.CS_TYPE_ANNULATION.equals(prest.getType()))
                && !isPrestationLamat(prest) && !isPrestationAmat(prest) && !isPrestationLapat(prest) && !isPrestationPandemie(prest) && !isPrestationLapai(prest);
    }
    private boolean isPrestationPandemie(APPrestation prest) {
        return (Double.parseDouble(totalMontantAPG) != 0)
                && (IPRDemande.CS_TYPE_PANDEMIE.equals(typePrestation)
                || APGUtils.isTypeAllocationPandemie(prest.getType()));
    }
    private boolean isPrestationLapai(APPrestation prest) throws Exception {
        return (Double.parseDouble(totalMontantAPG) != 0) && APGUtils.isTypePrestation(prest.getIdPrestation(),getSession(), IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT);
    }

    public String getAnnee() {
        return annee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return eMailObject;
    }

    /**
     * @return
     */
    public Boolean getIsGenerationUnique() {
        return isGenerationUnique;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * @return
     */
    public String getNSS() {
        return NSS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEMailObject(String mailObject) {
        eMailObject = mailObject;
    }

    /**
     * @param boolean1
     */
    public void setIsGenerationUnique(Boolean boolean1) {
        isGenerationUnique = boolean1;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /**
     * @param string
     */
    public void setNSS(String string) {
        NSS = string;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public List<APSitProJointEmployeur> getSituationsProf() {
        return situationsProf;
    }

    public void setSituationsProf(List<APSitProJointEmployeur> situationsProf) {
        this.situationsProf = situationsProf;
    }
}
