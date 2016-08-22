package globaz.ij.acor.parser.rev5;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.ij.api.prestations.IIJIJCalculee;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceJointDemande;
import globaz.ij.db.prononces.IJPrononceJointDemandeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un parser capable de parser le fichier xml f_calcul.xml retourne par globaz.ij.acor dans le cas d'un ij de 4eme
 * revision et qui en extrait les bases de calcul (IJIJCalculee) et les prestations (IJIndemniteJournaliere).
 * </p>
 * 
 * @author scr
 * @see globaz.ij.db.prestations.IJIJCalculee
 * @see globaz.ij.db.prestations.IJIndemniteJournaliere
 */
public class IJACORBasesCalculParser extends IJACORAbstractXMLFileParser {

    private static final String ZERO = "0";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private static IJIJCalculee copierIJCalculeePourDate(BSession session, BTransaction transaction,
            IJPrononce prononce, IJIJCalculee source, String dateDebut, String dateFin) throws Exception {

        IJIJCalculee result = null;

        if (IIJIJCalculee.CS_TYPE_PETITE_IJ.equals(source.getCsTypeIJ())) {
            result = new IJPetiteIJCalculee();
        } else {
            result = new IJGrandeIJCalculee();
        }
        result.setSession(session);

        // Partie commune
        result.setCsGenreReadaptation(source.getCsGenreReadaptation());
        result.setCsStatutProfessionnel(source.getCsStatutProfessionnel());
        result.setCsTypeBase(source.getCsTypeBase());
        result.setCsTypeIJ(source.getCsTypeIJ());
        result.setDateDebutDroit(dateDebut);
        result.setDateFinDroit(dateFin);
        result.setDatePrononce(prononce.getDatePrononce());
        result.setDateRevenu(source.getDateRevenu());
        result.setDemiIJACBrut(source.getDemiIJACBrut());
        result.setDifferenceRevenu(source.getDifferenceRevenu());
        result.setIdPrononce(prononce.getIdPrononce());
        result.setMontantBase(source.getMontantBase());
        result.setNoAVS(source.getNoAVS());
        result.setNoRevision(source.getNoRevision());
        result.setPourcentDegreIncapaciteTravail(source.getPourcentDegreIncapaciteTravail());
        result.setRevenuDeterminant(source.getRevenuDeterminant());
        result.setRevenuJournalierReadaptation(source.getRevenuJournalierReadaptation());
        result.setSupplementPersonneSeule(source.getSupplementPersonneSeule());

        if (IIJIJCalculee.CS_TYPE_PETITE_IJ.equals(source.getCsTypeIJ())) {
            ((IJPetiteIJCalculee) result).setCsModeCalcul(((IJPetiteIJCalculee) source).getCsModeCalcul());
        } else {
            ((IJGrandeIJCalculee) result).setMontantIndemniteEnfant(((IJGrandeIJCalculee) source)
                    .getMontantIndemniteEnfant());
            ((IJGrandeIJCalculee) result).setNbEnfants(((IJGrandeIJCalculee) source).getNbEnfants());
            ((IJGrandeIJCalculee) result).setMontantIndemniteAssistance(((IJGrandeIJCalculee) source)
                    .getMontantIndemniteAssistance());
            ((IJGrandeIJCalculee) result).setMontantIndemniteExploitation(((IJGrandeIJCalculee) source)
                    .getMontantIndemniteExploitation());
        }

        // Récupération des indemnités journalieres
        result.add(transaction);

        IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
        mgr.setForIdIJCalculee(source.getIdIJCalculee());
        mgr.setSession(session);
        mgr.find();
        for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
            IJIndemniteJournaliere old = (IJIndemniteJournaliere) iterator.next();

            IJIndemniteJournaliere newIndemnite = new IJIndemniteJournaliere();
            newIndemnite.setSession(session);
            newIndemnite.setCsTypeIndemnisation(old.getCsTypeIndemnisation());
            newIndemnite.setDeductionRenteAI(old.getDeductionRenteAI());
            newIndemnite.setFractionReductionSiRevenuAvantReadaptation(old
                    .getFractionReductionSiRevenuAvantReadaptation());
            newIndemnite.setIdIJCalculee(result.getIdIJCalculee());
            newIndemnite.setIndemniteAvantReduction(old.getIndemniteAvantReduction());
            newIndemnite.setMontantComplet(old.getMontantComplet());
            newIndemnite.setMontantGarantiAANonReduit(old.getMontantGarantiAANonReduit());
            newIndemnite.setMontantGarantiAAReduit(old.getMontantGarantiAAReduit());
            newIndemnite.setMontantJournalierIndemnite(old.getMontantJournalierIndemnite());
            newIndemnite.setMontantPlafonne(old.getMontantPlafonne());
            newIndemnite.setMontantPlafonneMinimum(old.getMontantPlafonneMinimum());
            newIndemnite.setMontantReductionSiRevenuAvantReadaptation(old
                    .getMontantReductionSiRevenuAvantReadaptation());
            newIndemnite.setMontantSupplementaireReadaptation(old.getMontantSupplementaireReadaptation());
            newIndemnite.add(transaction);
        }

        return result;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * cree une nouvelle instance de IJGrandeIJCalculee ou IJPetiteIJCalculee avec les valeurs correctes et l'insere
     * dans la base.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param nodes
     * @param demande
     * @param nss
     *            NSS du requérant
     * 
     * @return
     * 
     * @throws PRACORException
     *             l'insertion dans la base est impssible
     */
    private static final IJIJCalculee nouvelleIJCalculee(BSession session, BTransaction transaction,
            IJPrononce prononce, Map nodes, Node demande, String nss, String noRevision) throws PRACORException {
        IJIJCalculee retValue;
        Node node;

        // recuperation du type d'ij
        node = getNode(nodes, "genre", true);

        if (PRACORConst.CA_TYPE_IJ_GRANDE.equals(getNodeValue(node))) {
            IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();

            // champs specifiques a la grande ij
            grandeIJ.setMontantIndemniteEnfant(getNodeValue(nodes, "montant_enfants", false));

            grandeIJ.setNbEnfants(getNodeValue(nodes, "n_enfants", false));
            retValue = grandeIJ;
        } else {
            IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();

            /*
             * champs specifiques à la petite IJ il est probable que l'element formation soit celui qu'on cherche ici
             * mais dans le doute on reprend la valeur d'origine
             */
            petiteIJ.setCsModeCalcul(((IJPetiteIJ) prononce).getCsSituationAssure());

            retValue = petiteIJ;
        }

        String value = getNodeValue(nodes, "droit_prestation_enfant", false);
        if (JadeStringUtil.isBlank(value)) {
            retValue.setIsDroitPrestationPourEnfant(null);
        } else if ("1".equals(value)) {
            retValue.setIsDroitPrestationPourEnfant(Boolean.TRUE);
        } else {
            retValue.setIsDroitPrestationPourEnfant(Boolean.FALSE);
        }

        // champs communs a petite et grande ij
        retValue.setNoAVS(nss);

        retValue.setOfficeAI(getNodeValue(nodes, "office_ai", true));
        retValue.setCsGenreReadaptation(PRACORConst.caGenreReadaptationToCS(session,
                getNodeValue(nodes, "genre_readaptation", true)));
        String dPrononce = getNodeDateAAAAMMJJValue(nodes, "date_prononce", false);
        if (JadeStringUtil.isBlankOrZero(dPrononce)) {
            try {
                dPrononce = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prononce.getDatePrononce());
            } catch (Exception e) {
                throw new PRACORException(e.toString());
            }
        }
        retValue.setDatePrononce(dPrononce);

        retValue.setDateDebutDroit(getNodeDateAAAAMMJJValue(nodes, "debut_droit", true));
        retValue.setDateFinDroit(getNodeDateAAAAMMJJValue(nodes, "fin_droit", false));

        // ya pas pour la 4eme rev: retValue.setCsTypeBase();
        retValue.setRevenuDeterminant(getNodeChildValue(nodes, "revenu_determinant", "revenu_journalier", false));
        retValue.setDateRevenu(getNodeChildDateAAAAMMJJValue(nodes, "revenu_determinant", "date", false));
        retValue.setMontantBase(getNodeValue(nodes, "montant_base", true));

        // ya pas pour la 4eme rev: retValue.setSupplementPersonneSeule();
        try {
            retValue.setRevenuJournalierReadaptation(getNodeChildValue(nodes, "revenu_readaptation",
                    "revenu_journalier", false));
        } catch (Exception e) {
            // Dans certains cas (demi_ij_ac, montant_garanti_aa...), le tag
            // revenu_journalier n'est pas présent. On prend à la place
            // le revenu_tot_journalier
            retValue.setRevenuJournalierReadaptation(getNodeChildValue(nodes, "revenu_readaptation",
                    "revenu_tot_journalier", false));
        }
        retValue.setCsStatutProfessionnel(PRACORConst.caStatutProfessionnelToCS(session,
                getNodeValue(nodes, "statut", true)));
        retValue.setPourcentDegreIncapaciteTravail(getNodeValue(nodes, "incapacite", false));

        retValue.setDemiIJACBrut(getNodeChildValue(nodes, "revenu_readaptation", "AC_demi_brut", false));

        // Récupération de la différence de revenu, si >=0 effet sur calcul IJ
        try {
            retValue.setDifferenceRevenu(getNodeValue(nodes, "difference_revenu", false));
        } catch (Exception e) {
            retValue.setDifferenceRevenu("0");
        }

        // insertion dans la base
        retValue.setIdPrononce(prononce.getIdPrononce());
        retValue.setCsTypeIJ(prononce.getCsTypeIJ());
        retValue.setNoRevision(noRevision);
        save(session, transaction, retValue);
        return retValue;
    }

    /**
     * Parser le fichier xml f_calcul.xml retourne par globaz.ij.acor dans le cas d'un ij de 4eme ou 5ème revision et
     * extraire les bases de calcul (IJIJCalculee) et les prestations (IJIndemniteJournaliere).
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param reader
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final List<IJIJCalculee> parse(BSession session, BTransaction transaction, IJPrononce prononce,
            Reader reader) throws PRACORException {
        LinkedList<IJIJCalculee> retValue = new LinkedList<IJIJCalculee>();

        // les fichiers de retour en xml ne sont jamais bien gros donc on les
        // charge en memoire
        Document document = loadDocument(reader);
        Node demande;

        // charger l'element demande qui sera utilise pour les grandes et
        // petites ij calculees
        NodeList list = document.getElementsByTagName("demande");

        if (list.getLength() > 0) {
            demande = list.item(0);
        } else {
            throw new PRACORException("format de fichier xml invalide: pas d'element demande");
        }

        // Recherche du nss du requérant...
        NodeList assures = document.getElementsByTagName("assure");

        String nss = "";
        for (int i = 0; i < assures.getLength(); ++i) {
            Node assure = assures.item(i);
            Map mapAssure = childrenMap(assure);
            String fct = getNodeValue(mapAssure, "fonction", true);
            // On a le requérant
            if ("req".equals(fct)) {
                nss = getNodeValue(mapAssure, "id", true);
                String nssTmp = nss;
                nssTmp = JadeStringUtil.removeChar(nssTmp, '.');

                if (nssTmp.trim().length() == 11 || nssTmp.trim().length() == 13) {
                    break;
                }
                // Ne devrait pas arrivé, mais bon... dernière tentative !!!
                else {
                    try {
                        // extraire les info sur le type ID
                        for (int id = 0; id < assure.getChildNodes().getLength(); ++id) {
                            Node nId = assure.getChildNodes().item(id);

                            if (nId.getNodeName().equals("id")) {
                                String type = nId.getAttributes().getNamedItem("type").getNodeValue();
                                if (!type.equals("navs") || !type.equals("nnss")) {
                                    // On lit le nss depuis le tag <navs>, mais
                                    // ne devrait jamais arriver!!!
                                    nss = getNodeValue(mapAssure, "navs", false);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Ne devrait jamais arriver
                        e.printStackTrace();
                    }
                }
            }
        }
        if (JadeStringUtil.isBlankOrZero(nss)) {
            throw new PRACORException("format de fichier xml invalide : Impossible de retrouver le NSS du requérant. ");
        }

        // iterer sur toutes bases de calcul
        list = document.getElementsByTagName("cycle");
        Node baseDeCalculNode = null;
        for (int idItem = 0; idItem < list.getLength(); ++idItem) {
            Node cycle = list.item(idItem);

            //
            for (int idBC = 0; idBC < cycle.getChildNodes().getLength(); ++idBC) {
                Node bc = cycle.getChildNodes().item(idBC);
                // On récupère toutes les bases de calculs
                if (bc.getNodeName().equals("bases_calcul")) {
                    baseDeCalculNode = bc;
                    String noRevision = bc.getAttributes().getNamedItem("revision").getNodeValue();
                    Map<String, Node> bcNodes = childrenMap(bc);
                    IJIJCalculee ijCalculee = nouvelleIJCalculee(session, transaction, prononce, bcNodes, demande, nss,
                            noRevision);

                    // Contrôle que les données importées correspondent bien au
                    // prononcé courant.
                    // Permet d'éviter que l'utilisateur click sur importer les
                    // données sans avoir préalablement calculée
                    // les IJ. Dans ce cas précis, l'importation des données se
                    // fait avec le dernier calcul effectué sur le poste de
                    // l'utilisateur.
                    try {
                        String nss1 = prononce.loadDemande(null).loadTiers()
                                .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                        String nss2 = ijCalculee.getNoAVS();
                        if (nss1 != null && nss2 != null) {
                            nss1 = JadeStringUtil.removeChar(nss1, '.');
                            nss2 = JadeStringUtil.removeChar(nss2, '.');
                            // TODO Le test nss1 égal nss2 a été remplacé par le
                            // test nss1 se termine par nss2.
                            // Lorsqu’ACOR supportera le format à 13 positions,
                            // l’ancien test pourra être restauré.
                            if (!nss1.endsWith(nss2)) {
                                throw new PRACORException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
                            }
                        }
                    } catch (PRACORException e) {
                        throw e;
                    } catch (Exception e2) {
                        JadeLogger.error(e2, e2.getMessage());
                        e2.printStackTrace();
                    }

                    // ajouter aux resultats
                    retValue.add(ijCalculee);
                    boolean indemniteJournaliereTrouve = false;
                    // extraire les indemnites journalieres
                    for (int idIJ = 0; idIJ < bc.getChildNodes().getLength(); ++idIJ) {
                        Node ij = bc.getChildNodes().item(idIJ);

                        if (ij.getNodeName().equals("ij")) {
                            indemniteJournaliereTrouve = true;
                            Map ijNodes = childrenMap(ij);
                            IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();

                            if (PRACORConst.CA_TYPE_MESURE_INTERNE.equals(getNodeValue(ijNodes, "categorie", true))) {
                                indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
                            } else {
                                indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
                            }

                            // champs de prestation
                            indemniteJournaliere.setMontantSupplementaireReadaptation(getNodeValue(ijNodes,
                                    "deduction", false));

                            indemniteJournaliere.setMontantGarantiAANonReduit(getNodeValue(bcNodes,
                                    "garantie_AA_non_reduite", false));
                            indemniteJournaliere
                                    .setIndemniteAvantReduction(getNodeValue(bcNodes, "montant_base", false));

                            indemniteJournaliere.setDeductionRenteAI(getNodeValue(bcNodes, "reduction_AI", false));

                            // on a le montant ca suffit:
                            // indemniteJournaliere.setFractionReductionSiRevenuAvantReadaptation();
                            indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation(getNodeValue(bcNodes,
                                    "reduction_revenu", false));
                            indemniteJournaliere.setMontantJournalierIndemnite(getNodeValue(ijNodes,
                                    "montant_indemnite", true));
                            indemniteJournaliere.setMontantGarantiAAReduit(getNodeValue(bcNodes, "garantie_AA_reduite",
                                    false));
                            // insertion dans la base
                            indemniteJournaliere.setIdIJCalculee(ijCalculee.getIdIJCalculee());
                            save(session, transaction, indemniteJournaliere);
                        }
                    }

                    /*
                     * Si aucune indemnisation, c'edst que le balise <ij> n'est pas présente dans la feuille de calcul
                     * ACOR
                     * Dans ce cas on créé des indemnités journalières (interne et externe) à 0.-
                     */
                    if (!indemniteJournaliereTrouve) {
                        String reduction_AI = null;
                        try {
                            if (bc != null) {
                                Node n = null;
                                for (int ctr = 0; ctr < baseDeCalculNode.getChildNodes().getLength(); ctr++) {
                                    n = baseDeCalculNode.getChildNodes().item(ctr);
                                    if (n.getNodeName().equals("rentes_ai")) {
                                        reduction_AI = rechercherMontantJournalier(n);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            JadeLogger.error(IJACORBasesCalculParser.class, e);
                        }
                        try {
                            creerIJIndemniteJournaliere(ijCalculee.getIdIJCalculee(), session, reduction_AI);
                        } catch (Exception e) {
                            throw new PRACORException(e.toString(), e);
                        }
                    }
                }
            }
        }

        /*
         * Exemple de cas :
         * 
         * Cas 1)
         * 
         * [ (a) 4ème rév ][ (b) 5ème rév. ] [ (c) 4ème rév ] --------------------
         * -------|------------------------------------------ ---------------------------> 01.2008
         * 
         * Voici ce que retourne ACOR pour un calcul effectué avec la nouvelle version (ACOR 5ème rév)
         * 
         * 
         * 
         * 
         * Cas 2)
         * 
         * [ (a) 4ème rév ] ---------------------------|--------------------------
         * -------------------------------------------> 01.2008
         * 
         * Voici ce que retourne ACOR pour un calcul effectué avec la version (ACOR 4ème rév) Ce cas correspond au cas
         * actuel, étant donnée que pour les cas ayant commncé avant le 1er janv. 2008, il faut garantir les droit 4ème
         * révision, cela ne pose pas de problème.
         * 
         * 
         * Cas 3a)
         * 
         * [ (a) 4ème rév ][ (b) 5ème rév. ] ---------------------------|--------
         * -------------------------------------------------------------> 01.2008
         * 
         * Dans ce cas, l'IJ (a) a été calculée avec ACOR (4ème révision) En cas de chamgement, mutation... Acor 5ème
         * rév. va calculer l'IJ (b) uniquement, car il s'agit d'un nouveau prononcé. Et dans ce cas, on est supposé
         * garantir le montant 4ème révision si plus élévé.
         * 
         * Il faudrait obtenir le cas suivant :
         * 
         * 
         * Cas 3b)
         * 
         * [ (a) 4ème rév ][ (b) 5ème rév. ] [ (c) 4ème rév. ] ------------------
         * ---------|------------------------------------------ ---------------------------> 01.2008
         * 
         * Etant donné que ACOR ne traite pas cette problématique, le système va généré l'IJ (c).
         * 
         * Règles : Pour chaque nouveau prononcé, lors du calcul de l'IJ, s'il y a une IJ calculee en cours à la date du
         * jour précédent le début du prononcé, il faut recopier cette IJ pour la même période que la nouvelle, mais
         * avec les montants de l'ancienne.
         */

        try {
            for (Iterator iterator = retValue.iterator(); iterator.hasNext();) {
                IJIJCalculee ij = (IJIJCalculee) iterator.next();

                JADate ddPrononce = new JADate(prononce.getDateDebutPrononce());
                JADate ddIJ = new JADate(ij.getDateDebutDroit());
                JACalendar cal = new JACalendarGregorian();

                if (cal.compare(ddPrononce, ddIJ) == JACalendar.COMPARE_EQUALS) {
                    JADate jourPrecedent = new JADate(ddPrononce.toAMJ());
                    jourPrecedent = cal.addDays(jourPrecedent, -1);

                    String idTiers = prononce.loadDemande(null).getIdTiers();
                    if (idTiers != null) {
                        IJPrononceJointDemandeManager mgr = new IJPrononceJointDemandeManager();
                        mgr.setSession(session);
                        mgr.setForIdTiers(idTiers);
                        mgr.setForCsEtatPrononce(IIJPrononce.CS_COMMUNIQUE);
                        mgr.setForCsTypeIJ(prononce.getCsTypeIJ());
                        mgr.setOrderBy(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE + " DESC, "
                                + IJPrononce.FIELDNAME_ID_PRONONCE + " DESC ");
                        mgr.find();

                        for (int i = 0; i < mgr.size(); i++) {
                            // On prend le premier prononcé, qui est le plus
                            // récent
                            IJPrononceJointDemande ancienPrononce = (IJPrononceJointDemande) mgr.getEntity(i);
                            // Dans le cas d'une correction,
                            // on s'assure que l'on ne traite pas le prononce
                            // parent ou prononce que l'on veut corriger...
                            // car dans ce cas, les ij calcules 4ème révision ne
                            // seront pas récupérée, car les dates
                            // ne corresponderont pas.

                            if (!JadeStringUtil.isBlankOrZero(prononce.getIdCorrection())) {
                                if (ancienPrononce.getIdPrononce().equals(prononce.getIdCorrection())
                                        || ancienPrononce.getIdPrononce().equals(prononce.getIdParent())) {

                                    continue;
                                }
                            }

                            // On récupère toute ces ijcalculées
                            IJIJCalculeeManager ijcMgr = new IJIJCalculeeManager();
                            ijcMgr.setSession(session);
                            ijcMgr.setForIdPrononce(ancienPrononce.getIdPrononce());
                            // prendre le no de révision précédente
                            // Révision précédente
                            BigDecimal noRev = new BigDecimal(ij.getNoRevision());
                            noRev = noRev.subtract(new BigDecimal(1));
                            ijcMgr.setForNoRevision(noRev.toString());

                            ijcMgr.find();
                            IJIJCalculee ancienneIJ = null;
                            for (Iterator iterator2 = ijcMgr.iterator(); iterator2.hasNext();) {
                                IJIJCalculee dummy = (IJIJCalculee) iterator2.next();
                                if (IIJIJCalculee.CS_TYPE_GRANDE_IJ.equals(dummy.getCsTypeIJ())) {
                                    ancienneIJ = new IJGrandeIJCalculee();
                                    ancienneIJ.setSession(dummy.getSession());
                                    ancienneIJ.setIdIJCalculee(dummy.getIdIJCalculee());
                                    ancienneIJ.retrieve();
                                    PRAssert.notIsNew(ancienneIJ, null);
                                } else if (IIJIJCalculee.CS_TYPE_PETITE_IJ.equals(dummy.getCsTypeIJ())) {
                                    ancienneIJ = new IJPetiteIJCalculee();
                                    ancienneIJ.setSession(dummy.getSession());
                                    ancienneIJ.setIdIJCalculee(dummy.getIdIJCalculee());
                                    ancienneIJ.retrieve();
                                    PRAssert.notIsNew(ancienneIJ, null);

                                } else {
                                    throw new Exception(
                                            "Error : Incohérence dans le données : L'IJ Calculée doit être du type Grande/Petite. idTypeIJ = "
                                                    + dummy.getCsTypeIJ());
                                }

                                JADate dateFin = null;
                                if (!JadeStringUtil.isBlankOrZero(ancienneIJ.getDateFinDroit())) {
                                    dateFin = new JADate(ancienneIJ.getDateFinDroit());
                                }

                                JADate dfPrononce = null;
                                if (!JadeStringUtil.isBlankOrZero(prononce.getDateFinPrononce())) {
                                    dfPrononce = new JADate(prononce.getDateFinPrononce());
                                }

                                // Aucune date de fin
                                if (dateFin == null && dfPrononce == null) {
                                    copierIJCalculeePourDate(session, transaction, prononce, ancienneIJ,
                                            ij.getDateDebutDroit(), ij.getDateFinDroit());
                                    return retValue;
                                }

                                // A ce niveau là, 1 seul des 2 dates au max.
                                // peut être null.
                                if (dateFin == null) {
                                    dateFin = new JADate("31.12.2999");
                                }

                                if (dfPrononce == null) {
                                    dfPrononce = new JADate("31.12.2999");
                                }

                                // On prend la valeu min.
                                JACalendar cal2 = new JACalendarGregorian();

                                if (cal2.compare(dateFin, dfPrononce) == 1) {
                                    dateFin = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dfPrononce
                                            .toStrAMJ()));
                                }

                                JADate ddAncienIJCalculee = new JADate(ancienneIJ.getDateDebutDroit());
                                if ((cal.compare(jourPrecedent, dateFin) == JACalendar.COMPARE_FIRSTLOWER || cal
                                        .compare(jourPrecedent, dateFin) == JACalendar.COMPARE_EQUALS)

                                        &&

                                        (cal.compare(jourPrecedent, ddAncienIJCalculee) == JACalendar.COMPARE_FIRSTUPPER || cal
                                                .compare(jourPrecedent, ddAncienIJCalculee) == JACalendar.COMPARE_EQUALS))

                                {
                                    copierIJCalculeePourDate(session, transaction, prononce, ancienneIJ,
                                            ij.getDateDebutDroit(), ij.getDateFinDroit());
                                    return retValue;
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(e, "Erreur de création de l'IJ pour garantie ancienne révision");
        }

        return retValue;
    }

    /**
     * @param n
     */
    private static String rechercherMontantJournalier(Node n) {
        NodeList rentes_ai = n.getChildNodes();
        for (int ctr2 = 0; ctr2 < rentes_ai.getLength(); ctr2++) {
            Node n2 = rentes_ai.item(ctr2);
            if (n2.getNodeName().equals("montant_journalier")) {
                return getNodeValue(n2);
            }
        }
        return null;
    }


    /**
     * @param session
     * @param ijCalculee
     * @throws Exception
     */
    private static void creerIJIndemniteJournaliere(String idIJCalculee, BSession session, String reductionAi)
            throws Exception {
        IJIndemniteJournaliere indemniteJournaliere = null;
        // ------------------------------------------//

        // 1ère IJ de type interne
        indemniteJournaliere = creerIndemniteJournaliereSansType();
        indemniteJournaliere.setSession(session);
        indemniteJournaliere.setIdIJCalculee(idIJCalculee);
        indemniteJournaliere.setDeductionRenteAI(reductionAi);
        // IJINDJRN.XWTTIN
        indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
        indemniteJournaliere.add();
        // ------------------------------------------//

        // 2ème IJ de type externe
        indemniteJournaliere = creerIndemniteJournaliereSansType();
        indemniteJournaliere.setSession(session);
        indemniteJournaliere.setIdIJCalculee(idIJCalculee);
        indemniteJournaliere.setDeductionRenteAI(reductionAi);
        // IJINDJRN.XWTTIN
        indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
        indemniteJournaliere.add();
    }

    /**
     * @param indemniteJournaliere
     */
    private static IJIndemniteJournaliere creerIndemniteJournaliereSansType() {
        IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();
        // IJINDJRN.XWMDRA
        indemniteJournaliere.setDeductionRenteAI(ZERO);
        // IJINDJRN.XWMFRR
        indemniteJournaliere.setFractionReductionSiRevenuAvantReadaptation(ZERO);
        // IJINDJRN.XWMIAR
        indemniteJournaliere.setIndemniteAvantReduction(ZERO);
        // IJINDJRN.XWMMCO
        indemniteJournaliere.setMontantComplet(ZERO);
        // IJINDJRN.XWMGNR
        indemniteJournaliere.setMontantGarantiAANonReduit(ZERO);
        // IJINDJRN.XWMGAR
        indemniteJournaliere.setMontantGarantiAAReduit(ZERO);
        // IJINDJRN.XWMMJI
        indemniteJournaliere.setMontantJournalierIndemnite(ZERO);
        // IJINDJRN.XWMMPL
        indemniteJournaliere.setMontantPlafonne(ZERO);
        // IJINDJRN.XWMMPM
        indemniteJournaliere.setMontantPlafonneMinimum(ZERO);
        // IJINDJRN.XWMMRR
        indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation(ZERO);
        // IJINDJRN.XWMMSR
        indemniteJournaliere.setMontantSupplementaireReadaptation(ZERO);
        return indemniteJournaliere;
    }

    /**
     * Crée une nouvelle instance de la classe IJACORBasesCalculParser.
     */
    private IJACORBasesCalculParser() {
    }
}
