/*
 * Crée le 6 septembre 2006
 */
package globaz.ij.process;

import ch.globaz.common.util.Dates;
import ch.globaz.ij.business.services.IJPrononceService;
import ch.globaz.ij.business.services.IJServiceLocator;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.*;
import globaz.globall.util.JAException;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prestations.IIJRepartitionPaiements;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJCotisation;
import globaz.ij.db.prestations.IJCotisationManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestationJointLotManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.helpers.prononces.IJSituationProfessionnelleHelper;
import globaz.ij.itext.IJAttestations;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author hpe
 * 
 */
public class IJGenererAttestationsProcess extends BProcess {

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
        public String idBaseInd = "";
        public BigDecimal montantTotal = new BigDecimal(0);
        public BigDecimal montantVentilations = new BigDecimal(0);
        public BigDecimal totalMontantCotisations = new BigDecimal(0);
        public BigDecimal totalMontantIJ = new BigDecimal(0);
        public BigDecimal totalMontantImpotSource = new BigDecimal(0);
        private boolean isAddLettreEntete = false;
        private boolean isCopyFisc = false;
        private boolean isHasCopyFisc = false;
        private String canton = "";

        @Override
        public int compareTo(Object o) {
            if (o instanceof AttestationsInfos) {
                return Integer.parseInt(dateDebut) - Integer.parseInt(((AttestationsInfos) o).dateDebut);
            } else {
                return -1;
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
            if ((obj != null) && (obj instanceof Key)) {
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
    private String idBaseInd = "";
    private String idTiers = "";
    private IJPrononce prononce;
    private List<IJSituationProfessionnelle> situationsProf;
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

    private String totalMontantIJ = "";

    /**
     * Crée une nouvelle instance de la classe IJGenererAttestationsProcess.
     */
    public IJGenererAttestationsProcess() {
        super();
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererAttestationsProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJGenererAttestationsProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererAttestationsProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererAttestationsProcess(BSession session) {
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

        IJRepartitionJointPrestationJointLotManager mgr = new IJRepartitionJointPrestationJointLotManager();

        // Prendre les répartitions qui ont été comptabilisées durant l'année
        // passée en paramètre

        mgr.setSession(getSession());
        mgr.setForDateDebutComptaLot(dateDebut);
        mgr.setForDateFinComptaLot(dateFin);
        mgr.setForEtatPrestation(IIJPrestation.CS_DEFINITIF);
        mgr.setOrderBy(IJPrestation.FIELDNAME_DATEDEBUT + "," + IJPrestation.FIELDNAME_DATEFIN);

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

        IJRepartitionJointPrestation repPres = null;

        int nbRep = 0;

        while ((repPres = (IJRepartitionJointPrestation) (mgr.cursorReadNext(statement))) != null) {

            nbRep++;

            // Reprendre chaque répartition
            IJRepartitionPaiements rep = new IJRepartitionPaiements();
            rep.setIdRepartitionPaiement(repPres.getIdRepartitionPaiement());
            rep.setSession(getSession());
            rep.retrieve();

            // recherche le prononce
            IJPrononce prononceLocal = new IJPrononce();
            prononceLocal.setSession(getSession());
            prononceLocal.setIdPrononce(repPres.getIdPrononce());
            prononceLocal.retrieve();

            // Si pas une ventilation,
            // et (un payement direct ou un payement a l'employeur pour un
            // independant)
            if (JadeStringUtil.isIntegerEmpty(rep.getIdParent())
                    && (IIJRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(rep.getTypePaiement()) || IIJPrononce.CS_INDEPENDANT
                            .equals(prononceLocal.getCsStatutProfessionnel()))) {

                montantTotal = rep.getMontantRestant();
                totalMontantIJ = rep.getMontantBrut();
                idBaseInd = repPres.getIdBaseIndemnisation();
                idTiers = repPres.getIdTiers();
                tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);
                prononce = prononceLocal;

                IJSituationProfessionnelleManager spMgr = new IJSituationProfessionnelleManager();
                spMgr.setSession(getSession());
                spMgr.setForIdPrononce(prononce.getIdPrononce());
                spMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
                situationsProf = spMgr.getContainerAsList();

                IJPrestation prest = new IJPrestation();
                prest.setSession(getSession());
                prest.setIdPrestation(rep.getIdPrestation());
                prest.retrieve(getTransaction());

                /*
                 * IJBaseIndemnisation bi = new IJBaseIndemnisation(); bi.setSession(getSession());
                 * bi.setIdBaseIndemisation(prest.getIdBaseIndemnisation()); bi.retrieve(getTransaction());
                 * 
                 * boolean isVentilationATraiter = true; //On ne traite pas les ventilations pour des bases
                 * d'indemnistations annulées !!! if (IIJBaseIndemnisation.CS_ANNULE.equals(bi.getCsEtat())) { continue;
                 * //isVentilationATraiter = false; }
                 */
                Set idsVentilation = new TreeSet();
                FWCurrency montantVentilation = new FWCurrency(0);
                IJRepartitionPaiementsManager repartitions = new IJRepartitionPaiementsManager();
                repartitions.setForIdParent(rep.getIdRepartitionPaiement());
                repartitions.setNotForIdRepartitionPaiement(rep.getIdRepartitionPaiement());
                repartitions.setSession(getSession());

                repartitions.find(getTransaction());

                for (int i = 0; i < repartitions.size(); i++) {
                    IJRepartitionPaiements elm = (IJRepartitionPaiements) repartitions.get(i);

                    if (!idsVentilation.contains(elm.getIdRepartitionPaiement())) {
                        montantVentilation.add(elm.getMontantVentile());
                        idsVentilation.add(elm.getIdRepartitionPaiement());
                    }
                }

                // Reprendre les cotisations pour la répartition
                IJCotisationManager cotMan = new IJCotisationManager();
                cotMan.setForIdRepartitionPaiements(rep.getIdRepartitionPaiement());
                cotMan.setSession(getSession());
                cotMan.find();

                FWCurrency totalMontantCotisations = new FWCurrency();
                FWCurrency totalMontantImpotSource = new FWCurrency();

                for (Iterator iterator = cotMan.iterator(); iterator.hasNext();) {
                    IJCotisation cot = (IJCotisation) iterator.next();

                    if (cot.getIsImpotSource().booleanValue()) {
                        totalMontantImpotSource.add(cot.getMontant());
                    } else {
                        totalMontantCotisations.add(cot.getMontant());
                    }

                }

                boolean isRestitution = false;
                if (Double.parseDouble(totalMontantIJ) >= 0) {
                    isRestitution = false;
                } else {
                    isRestitution = true;
                }

                if (((Double.parseDouble(totalMontantIJ) != 0) && ((!totalMontantCotisations.isZero()) || (!totalMontantImpotSource
                        .isZero())))
                        && ((((!isRestitution) && (totalMontantCotisations.isNegative())) || ((isRestitution) && (totalMontantCotisations
                                .isPositive()))) || ((!isRestitution) && (totalMontantImpotSource.isNegative())) || ((isRestitution) && (totalMontantImpotSource
                                .isPositive())))) {

                    // Création de la clé
                    Key k = new Key();
                    k.idTiers = rep.getIdTiers();

                    // Si la clé est encore inexistante
                    if (!map.containsKey(k)) {

                        createAttestationInfoAndPutInMap(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k, map, false);

                    } else { // si la clé existe déjà

                        putAttestationInfoInList(prest, idsVentilation, montantVentilation, totalMontantCotisations, totalMontantImpotSource, k, map, false);

                    }

                    if (isPrestationIJ(prest)) {

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
        createAttestation(annee, dateDebut, dateFin, map, false);

        // si il y a des copies d'attestations à générer
        if (!mapFisc.isEmpty()) {

            // regroupe les copies d'attestation par canton
            LinkedHashMap<String, LinkedHashMap<Key, ArrayList<AttestationsInfos>>> mapFiscRegroupedByCanton = groupMapFiscByCanton(mapFisc);

            // génère les copies d'attestations regroupées par canton (cas avec impôt source)
            for (Map.Entry<String, LinkedHashMap<Key, ArrayList<AttestationsInfos>>> entry : mapFiscRegroupedByCanton.entrySet()) {
                createAttestation(annee, dateDebut, dateFin, entry.getValue(), true);
            }
        }

        return true;
    }

    private boolean findOneImpotSourceForTiers(Map<Key, ArrayList<AttestationsInfos>> mapFisc, String idTiers) {
        boolean foundOneImpotSource = false;
        for (Map.Entry<Key, ArrayList<AttestationsInfos>> entry : mapFisc.entrySet()) {
            for (AttestationsInfos attestationsInfos : entry.getValue()) {
                if (entry.getKey().idTiers.equals(idTiers) && attestationsInfos.isHasCopyFisc() && !attestationsInfos.totalMontantImpotSource.equals(new BigDecimal(0))) {
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

            // on prends le canton de l'attestation la plus récente ou la première de la liste
            AttestationsInfos newestAttestationInfos = attestationInfos.stream()
                    .filter(ai -> JadeStringUtil.isEmpty(ai.dateFin))
                    .filter(ai -> JadeStringUtil.isBlankOrZero(ai.getCanton()))
                    .max(Comparator.comparing(ai -> Dates.toDate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(ai.dateFin))))
                    .orElse(attestationInfos.get(0));

            String canton = newestAttestationInfos.getCanton();

            long cantonDifferentCount = attestationInfos.stream()
                    .filter(ai -> JadeStringUtil.isBlankOrZero(ai.getCanton()))
                    .filter(ai -> !ai.getCanton().equals(canton))
                    .count();

            if (cantonDifferentCount > 0) {
                getMemoryLog().logMessage("impossible de déterminer le canton d'imposition : plusieurs cantons différents trouvés pour le tiers : " + newestAttestationInfos.idTiers, FWMessage.AVERTISSEMENT,
                    "IJGenererAttestationsProcess");
            }

            LinkedHashMap<Key, ArrayList<AttestationsInfos>> linkedHashMap = mapFiscByCanton.get(canton);
            if (linkedHashMap == null) {
                linkedHashMap = new LinkedHashMap<>();
            }
            linkedHashMap.put(key, attestationInfos);
            mapFiscByCanton.put(canton, linkedHashMap);
        }
        return mapFiscByCanton;
    }

    private void createAttestationInfoAndPutInMap(IJPrestation prest, Set idsVentilation, FWCurrency montantVentilation, FWCurrency totalMontantCotisations, FWCurrency totalMontantImpotSource, Key k, Map map, boolean isCopyFisc) throws JAException {
        // On crée un objet
        AttestationsInfos ai = new AttestationsInfos();

        ai.idTiers = idTiers;
        ai.idBaseInd = idBaseInd;
        ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
        ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
        ai.totalMontantIJ = ai.totalMontantIJ.add(new BigDecimal(totalMontantIJ.toString()));
        ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                totalMontantCotisations.toString()));
        ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                totalMontantImpotSource.toString()));
        ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
        ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                .toString()));

        initCopyFisc(prest, totalMontantImpotSource, isCopyFisc, ai);

        ai.idsRPVentilations = idsVentilation;
        // Comme la clé est inexistante, on crée la liste
        // d'objet
        ArrayList list = new ArrayList();
        list.add(ai);

        // On insère la clé et la liste dans la map
        map.put(k, list);
    }

    private void putAttestationInfoInList(IJPrestation prest, Set idsVentilation, FWCurrency montantVentilation, FWCurrency totalMontantCotisations, FWCurrency totalMontantImpotSource, Key k, Map map, boolean isCopyFisc) throws JAException {
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

            // Donc si les dates sont identiques
            if (/* (ai.idsRPVentilations.size() == 0) && */dateDebutPrest.equals(dateDebutAi)
                    && dateFinPrest.equals(dateFinAi)) {

                // on ajoute simplement tous les montants à
                // l'objet ai
                ai.idTiers = idTiers;
                ai.idBaseInd = idBaseInd;
                ai.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateDebutPrest);
                ai.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinPrest);
                ai.totalMontantIJ = ai.totalMontantIJ.add(new BigDecimal(totalMontantIJ.toString()));
                ai.totalMontantCotisations = ai.totalMontantCotisations.add(new BigDecimal(
                        totalMontantCotisations.toString()));
                ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(new BigDecimal(
                        totalMontantImpotSource.toString()));
                ai.montantTotal = ai.montantTotal.add(new BigDecimal(montantTotal.toString()));
                ai.montantVentilations = ai.montantVentilations.add(new BigDecimal(montantVentilation
                        .toString()));

                initCopyFisc(prest, totalMontantImpotSource, isCopyFisc, ai);

                isFusion = true;

            }
        }

        if (!isFusion) {

            AttestationsInfos ai1 = new AttestationsInfos();

            ai1.idTiers = idTiers;
            ai1.idBaseInd = idBaseInd;
            ai1.dateDebut = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateDebut());
            ai1.dateFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prest.getDateFin());
            ai1.totalMontantIJ = ai1.totalMontantIJ.add(new BigDecimal(totalMontantIJ.toString()));
            ai1.totalMontantCotisations = ai1.totalMontantCotisations.add(new BigDecimal(
                    totalMontantCotisations.toString()));
            ai1.totalMontantImpotSource = ai1.totalMontantImpotSource.add(new BigDecimal(
                    totalMontantImpotSource.toString()));
            ai1.montantTotal = ai1.montantTotal.add(new BigDecimal(montantTotal.toString()));
            ai1.montantVentilations = ai1.montantVentilations.add(new BigDecimal(montantVentilation
                    .toString()));
            ai1.idsRPVentilations = idsVentilation;

            initCopyFisc(prest, totalMontantImpotSource, isCopyFisc, ai1);

            list.add(ai1);
        }
    }

    private void initCopyFisc(IJPrestation prest, FWCurrency totalMontantImpotSource, boolean isCopyFisc, AttestationsInfos ai) {
        try {
            if (isPrestationIJ(prest)) {

                if (prononce.getSoumisImpotSource()) {

                    // cherche le canton impôt source de l'attestation d'imposition
                    String canton = searchCantonImpotSourceCascade(prononce, prest);
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
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de l'initialisation de la copie au fisc : " + tiers.getNSS() + " " + e.toString(), FWMessage.AVERTISSEMENT,
                    "IJGenererAttestationsProcess");
        }
    }

    private String searchCantonImpotSourceCascade(IJPrononce prononce, IJPrestation prest) {
        String canton = "";

        try {
            // recherche du canton dans le prononce
            canton = prononce.getCsCantonImpositionSource();

            // si canton vide dans le droit ou si la valeur est set à ETRANGER
            if (JadeStringUtil.isBlankOrZero(canton) || PRACORConst.CODE_CANTON_ETRANGER.equals(canton)) {

                // recherche du canton dans l'adresse de domicile
                canton = PRTiersHelper.getTiersCanton(getSession(), idTiers);

                // si canton vide il n'y a pas d'adresse de domicile ou si l'adresse de domicile est à l'étranger alors on vas rechercher l'adresse de l'employeur
                if (JadeStringUtil.isBlankOrZero(canton) || PRACORConst.CODE_CANTON_ETRANGER.equals(canton)) {

                    // recherche du canton dans l'adresse de l'employeur
                    IJSituationProfessionnelleHelper ijSituationProfessionnelleHelper = new IJSituationProfessionnelleHelper();
                    canton = ijSituationProfessionnelleHelper.rechercheCantonAdressePaiementSitProf(getSession(), rechercheDomaine(), situationsProf, prest.getDateDebut());

                    // si canton vide il n'y a pas de sitProf ou si adresse sitProf est à l'étranger alors on génère une alerte
                    if (JadeStringUtil.isBlankOrZero(canton) || PRACORConst.CODE_CANTON_ETRANGER.equals(canton)) {
                        getMemoryLog().logMessage("Erreur lors de la recherche du canton d'imposition à l'impôt source : " + tiers.getNSS(), FWMessage.AVERTISSEMENT,
                                "IJGenererAttestationsProcess");
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la recherche du canton d'imposition à l'impôt source : " + tiers.getNSS() + " " + e.toString(), FWMessage.AVERTISSEMENT,
                    "IJGenererAttestationsProcess");
        }

        return canton;
    }

    private String rechercheDomaine() {
        return IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI;
    }

    private boolean isPrestationIJ(IJPrestation prest) {
        return (Double.parseDouble(totalMontantIJ) != 0) && (IIJPrestation.CS_NORMAL.equals(prest.getCsType()) || IIJPrestation.CS_RESTITUTION.equals(prest.getCsType()));
    }

    private void createAttestation(String annee, String dateDebut, String dateFin, Map map, boolean isAttestationCopy) throws Exception {
        IJAttestations attestations = new IJAttestations(getSession());
        attestations.setAttestationsMap(map);
        attestations.setDateDebut(dateDebut);
        attestations.setDateFin(dateFin);
        attestations.setAnnee(annee);
        attestations.setParent(this);
        attestations.setTailleLot(1);
        attestations.setIsSendToGED(getIsSendToGed());
        attestations.setIsGenerationUnique(isGenerationUnique);
        attestations.setAttestationCopy(isAttestationCopy);
        attestations.executeProcess();
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

    public List<IJSituationProfessionnelle> getSituationsProf() {
        return situationsProf;
    }

    public void setSituationsProf(List<IJSituationProfessionnelle> situationsProf) {
        this.situationsProf = situationsProf;
    }
}
