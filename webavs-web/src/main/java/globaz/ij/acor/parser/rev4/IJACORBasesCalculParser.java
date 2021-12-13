package globaz.ij.acor.parser.rev4;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.api.prestations.IIJIJCalculee;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.Reader;
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
 * @author vre
 * @see globaz.ij.db.prestations.IJIJCalculee
 * @see globaz.ij.db.prestations.IJIndemniteJournaliere
 */
public class IJACORBasesCalculParser extends IJACORAbstractXMLFileParser {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private static IJIJCalculee majMontantGaranti3Revision(BSession session, IJIJCalculee ijCourante,
            IJIJCalculee ijSuivante) throws Exception {

        if (ijSuivante != null && ijCourante.getDateDebutDroit().equals(ijSuivante.getDateDebutDroit())) {
            return null;
        } else {
            // Mise � jour de l'element courant
            ijCourante.retrieve();
            ijCourante.setCsGarantieR3("");
            ijCourante.update();

            if (ijSuivante != null) {
                return ijSuivante;
            } else {
                return null;
            }
        }
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
     * 
     * @return
     * 
     * @throws PRACORException
     *             l'insertion dans la base est impssible
     */
    private static final IJIJCalculee nouvelleIJCalculee(BSession session, IJPrononce prononce, Map nodes, Node demande)
            throws PRACORException {
        IJIJCalculee retValue;
        Node node;

        // recuperation du type d'ij
        node = getNode(nodes, "genre", true);

        if (PRACORConst.CA_TYPE_IJ_GRANDE.equals(getNodeValue(node))
            || PRACORConst.CA_TYPE_IJ_AVEC_REVENU.equals(getNodeValue(node))) {
            IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();

            // champs specifiques a la grande ij
            grandeIJ.setMontantIndemniteEnfant(getNodeValue(nodes, "montant_enfants", false));
            grandeIJ.setNbEnfants(getNodeValue(nodes, "n_enfants", false));
            // ya pas pour 4eme rev: grandeIJ.setMontantIndemniteAssistance();
            // ya pas pour 4eme rev: grandeIJ.setMontantIndemniteExploitation();

            retValue = grandeIJ;
        } else {
            IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();

            /*
             * champs specifiques � la petite IJ il est probable que l'element formation soit celui qu'on cherche ici
             * mais dans le doute on reprend la valeur d'origine
             */
            petiteIJ.setCsModeCalcul(((IJPetiteIJ) prononce).getCsSituationAssure());

            retValue = petiteIJ;
        }

        // champs communs a petite et grande ij
        retValue.setNoAVS(getNodeValue(findChildNode(demande, "id_assure")));
        retValue.setOfficeAI(getNodeValue(nodes, "office_ai", true));
        retValue.setCsGenreReadaptation(PRACORConst.caGenreReadaptationToCS(session,
                getNodeValue(nodes, "genre_readaptation", true)));
        retValue.setDatePrononce(getNodeDateValue(nodes, "date_prononce", true));
        retValue.setDateDebutDroit(getNodeDateValue(nodes, "debut_droit", true));
        retValue.setDateFinDroit(getNodeDateValue(nodes, "fin_droit", true));

        // ya pas pour la 4eme rev: retValue.setCsTypeBase();
        retValue.setRevenuDeterminant(getNodeChildValue(nodes, "revenu_determinant", "revenu_journalier", false));
        retValue.setDateRevenu(getNodeChildDateValue(nodes, "revenu_determinant", "date", false));
        retValue.setMontantBase(getNodeValue(nodes, "montant_base", true));

        // ya pas pour la 4eme rev: retValue.setSupplementPersonneSeule();
        try {
            retValue.setRevenuJournalierReadaptation(getNodeChildValue(nodes, "revenu_readaptation",
                    "revenu_journalier", false));
        } catch (Exception e) {
            // Dans certains cas (demi_ij_ac, montant_garanti_aa...), le tag
            // revenu_journalier n'est pas pr�sent. On prend � la place
            // le revenu_tot_journalier
            retValue.setRevenuJournalierReadaptation(getNodeChildValue(nodes, "revenu_readaptation",
                    "revenu_tot_journalier", false));
        }
        retValue.setCsStatutProfessionnel(PRACORConst.caStatutProfessionnelToCS(session,
                getNodeValue(nodes, "statut", true)));
        retValue.setPourcentDegreIncapaciteTravail(getNodeValue(nodes, "incapacite", false));

        retValue.setDemiIJACBrut(getNodeChildValue(nodes, "revenu_readaptation", "AC_demi_brut", false));

        // detecter si cette ij calculee est un montant garanti 3eme revision
        if (JadeStringUtil.isBlank(getNodeValue(nodes, "mutation", false))) {
            // ij "normale" ni une ijrev4 defavorable ni un ijrev3 favorable
            retValue.setCsGarantieR3(IIJIJCalculee.CS_NON_APPLICABLE);
        } else {
            if (JadeStringUtil.isBlank(getNodeValue(nodes, "garantie_mut3", false))
                    && JadeStringUtil.isBlank(getNodeValue(nodes, "garantie_revision3", false))) {
                // ij rev4 defavorable... il s'agit des montants rev4
                retValue.setCsGarantieR3(IIJIJCalculee.CS_DEFAVORABLE);

            } else {
                // ij rev3 favorable... il s'agit des montants rev3 qui doivent
                // remplacer ceux rev4 si montant garanti 3
                // demande
                retValue.setCsGarantieR3(IIJIJCalculee.CS_FAVORABLE);

            }
        }

        // R�cup�ration de la diff�rence de revenu, si >=0 effet sur calcul IJ
        try {
            retValue.setDifferenceRevenu(getNodeValue(nodes, "difference_revenu", false));
        } catch (Exception e) {
            retValue.setDifferenceRevenu("0");
        }

        // insertion dans la base
        retValue.setIdPrononce(prononce.getIdPrononce());
        retValue.setCsTypeIJ(prononce.getCsTypeIJ());
        save(session, retValue);

        return retValue;
    }

    /**
     * Parser le fichier xml f_calcul.xml retourne par globaz.ij.acor dans le cas d'un ij de 4eme revision et extraire
     * les bases de calcul (IJIJCalculee) et les prestations (IJIndemniteJournaliere).
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
    public static final List parse(BSession session, IJPrononce prononce, Reader reader) throws PRACORException {
        LinkedList retValue = new LinkedList();

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

        // iterer sur toutes bases de calcul
        list = document.getElementsByTagName("cycle");

        for (int idItem = 0; idItem < list.getLength(); ++idItem) {
            Node cycle = list.item(idItem);

            // on ne prend en compte que les base de calcul de la 4eme revision
            if (cycle.getAttributes().getNamedItem("revision").getNodeValue().equals("4")) {
                Map basesCalculNodes = childrenMap(findChildNode(cycle, "bases_calcul"));
                IJIJCalculee ijCalculee = nouvelleIJCalculee(session, prononce, basesCalculNodes, demande);

                // Contr�le que les donn�es import�es correspondent bien au
                // prononc� courant.
                // Permet d'�viter que l'utilisateur click sur importer les
                // donn�es sans avoir pr�alablement calcul�e
                // les IJ. Dans ce cas pr�cis, l'importation des donn�es se fait
                // avec le dernier calcul effectu� sur le poste de
                // l'utilisateur.
                try {

                    // Ces validation sont possible uniquement pour les IJ
                    // nouvelle r�vision dont
                    // la date du prononc� d�but apr�s le 1er janvier 2004, et
                    // s'il n'y a qu'une seule r�vision dans la feuille de
                    // calcul.

                    // Si par exemple un nouvel enfant nait en cours de droit,
                    // cela va g�n�r� 2 cycle dans la feuille de calcul.
                    // 1 avant la naissance et 1 apr�s la naissance de l'enfant.
                    // Du coup, tous les tests effectu�s sur les dates ne sont
                    // plus valide.

                    if (list.getLength() == 1) {

                        if (!BSessionUtil.compareDateEqual(session, prononce.getDatePrononce(),
                                ijCalculee.getDatePrononce())) {

                            throw new PRACORException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + "(3)");

                        }

                        // Dans le cas d'une IJ � cheval entre 3�me et 4�me
                        // r�vision, la date de d�but du droit de l'IJ
                        // sera forc�ment diff�rente de la date de d�but du
                        // propnonc�. Donc il ne faut pas faire ce test.
                        // Si une nouvelle r�vision venait � arriver, il
                        // faudrait modifier ce test � nouveau.
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(session, prononce.getDatePrononce(),
                                "01.01.2004")) {

                            if (!BSessionUtil.compareDateEqual(session, prononce.getDateDebutPrononce(),
                                    ijCalculee.getDateDebutDroit())) {

                                throw new PRACORException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + "(4)");
                            }

                            // Des dates de fin sont renseign�es
                            if (!JadeStringUtil.isBlank(prononce.getDateFinPrononce())
                                    && !JadeStringUtil.isBlank(ijCalculee.getDateFinDroit())
                                    && prononce.getDateFinPrononce().length() > 1
                                    && ijCalculee.getDateFinDroit().length() > 1) {

                                if (!BSessionUtil.compareDateEqual(session, prononce.getDateFinPrononce(),
                                        ijCalculee.getDateFinDroit())) {

                                    throw new PRACORException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + "(5)");

                                }

                            }
                        }
                    }
                    String nss1 = prononce.loadDemande(null).loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    String nss2 = ijCalculee.getNoAVS();
                    if (nss1 != null && nss2 != null) {
                        nss1 = JadeStringUtil.removeChar(nss1, '.');
                        nss2 = JadeStringUtil.removeChar(nss2, '.');
                        // TODO Le test nss1 �gal nss2 a �t� remplac� par le
                        // test nss1 se termine par nss2.
                        // Lorsqu�ACOR supportera le format � 13 positions,
                        // l�ancien test pourra �tre restaur�.
                        if (!nss1.endsWith(nss2)) {
                            throw new PRACORException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + "(6)");
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

                // extraire les indemnites journalieres
                for (int idIJ = 0; idIJ < cycle.getChildNodes().getLength(); ++idIJ) {
                    Node ij = cycle.getChildNodes().item(idIJ);

                    if (ij.getNodeName().equals("ij")) {
                        Map ijNodes = childrenMap(ij);
                        IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();

                        if (PRACORConst.CA_TYPE_MESURE_INTERNE.equals(getNodeValue(ijNodes, "categorie", true))) {
                            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
                        } else {
                            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
                        }

                        // champs de prestation
                        indemniteJournaliere.setMontantSupplementaireReadaptation(getNodeValue(ijNodes, "deduction",
                                false));
                        indemniteJournaliere.setMontantGarantiAANonReduit(getNodeValue(basesCalculNodes,
                                "garantie_AA_non_reduite", false));
                        indemniteJournaliere.setIndemniteAvantReduction(getNodeValue(basesCalculNodes, "montant_base",
                                false));

                        indemniteJournaliere.setDeductionRenteAI(getNodeValue(basesCalculNodes, "reduction_AI", false));

                        // on a le montant ca suffit:
                        // indemniteJournaliere.setFractionReductionSiRevenuAvantReadaptation();
                        indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation(getNodeValue(
                                basesCalculNodes, "reduction_revenu", false));
                        indemniteJournaliere.setMontantJournalierIndemnite(getNodeValue(ijNodes, "montant_indemnite",
                                true));
                        indemniteJournaliere.setMontantGarantiAAReduit(getNodeValue(basesCalculNodes,
                                "garantie_AA_reduite", false));

                        // ya pas pour la 4eme rev:
                        // indemniteJournaliere.setMontantComplet();
                        // ya pas pour la 4eme rev:
                        // indemniteJournaliere.setMontantPlafonne();
                        // ya pas pour la 4eme rev:
                        // indemniteJournaliere.setMontantPlafonneMinimum();

                        // insertion dans la base
                        indemniteJournaliere.setIdIJCalculee(ijCalculee.getIdIJCalculee());
                        save(session, indemniteJournaliere);
                    }
                }
            }
        }

        // Cas standard, montant garanti 3�me r�vision :
        // IJ 1 IJ 2 favorable
        // �----------------------------�------------------------------|
        // IJ 3 d�favorable
        // �------------------------------|
        // 01.01.03 31.12.03

        // Cas sp�cial :
        // IJ1 IJ2 favorable
        // �--------------------------�-------------------------�
        // 01.01.03 31.12.03
        //
        // Dans le cas ou l'IJ2 (4�me) r�vision est plus favorable, ACOR ne va
        // pas g�n�r�
        // 2 IJ pour comparaison, mais va terminer l'IJ1 et d�buter l'IJ2 (4�me)
        // r�vision.
        // Le syst�me va flagger le montantGaranti3�meRevision de l'IJ2, alors
        // qu'il ne devrait pas le
        // faire �tant donn� que pour la m�me p�riode, il n'y a qu'une IJ
        // valide.
        // Ceci va g�n�r� des erreurs pour la suite. Il faut donc identifier ce
        // cas, et enlever le flag de l'IJ2

        IJIJCalculeeManager mgr = new IJIJCalculeeManager();
        mgr.setSession(session);
        mgr.setInGaranti3Revision(IIJIJCalculee.CS_DEFAVORABLE + ", " + IIJIJCalculee.CS_FAVORABLE);
        mgr.setOrderBy(IJIJCalculee.FIELDNAME_DATE_DEBUT_DROIT);

        try {
            mgr.find();

            IJIJCalculee ijCourante = null;
            IJIJCalculee ijSuivante = null;

            Iterator iter = mgr.iterator();
            while (iter.hasNext()) {
                if (ijCourante == null) {
                    ijCourante = (IJIJCalculee) iter.next();
                }

                if (iter.hasNext()) {
                    ijSuivante = (IJIJCalculee) iter.next();
                } else {
                    ijSuivante = null;
                }
                IJIJCalculee nextIJATraiter = majMontantGaranti3Revision(session, ijCourante, ijSuivante);

                if (nextIJATraiter != null) {
                    ijCourante = nextIJATraiter;
                } else {
                    ijCourante = null;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return retValue;
        }

        return retValue;
    }

    /**
     * Cr�e une nouvelle instance de la classe IJACORBasesCalculParser.
     */
    private IJACORBasesCalculParser() {
    }
}
