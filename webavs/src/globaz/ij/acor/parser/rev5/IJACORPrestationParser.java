package globaz.ij.acor.parser.rev5;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.Reader;
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
 * revision et qui en extrait les decomptes (IJPrestation).
 * </p>
 * 
 * @author scr
 */
public class IJACORPrestationParser extends IJACORAbstractXMLFileParser {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param ijCalculee
     *            DOCUMENT ME!
     * @param reader
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final List parse(BSession session, BTransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation, IJIJCalculee ijCalculee, Reader reader) throws PRACORException {
        LinkedList retValue = new LinkedList();
        Document document = IJACORAbstractXMLFileParser.loadDocument(reader);

        NodeList demande = document.getElementsByTagName("demande");

        // Recherche du nss du requérant...
        NodeList assures = document.getElementsByTagName("assure");
        String nssImporte = null;
        for (int i = 0; i < assures.getLength(); ++i) {
            Node assure = assures.item(i);
            Map mapAssure = IJACORAbstractXMLFileParser.childrenMap(assure);
            String fct = IJACORAbstractXMLFileParser.getNodeValue(mapAssure, "fonction", true);
            // On a le requérant
            if ("req".equals(fct)) {
                nssImporte = IJACORAbstractXMLFileParser.getNodeValue(mapAssure, "id", true);
                String nssTmp = nssImporte;
                nssTmp = JadeStringUtil.removeChar(nssTmp, '.');

                if ((nssTmp.trim().length() == 11) || (nssTmp.trim().length() == 13)) {
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
                                    nssImporte = IJACORAbstractXMLFileParser.getNodeValue(mapAssure, "navs", false);
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

        if (JadeStringUtil.isBlankOrZero(nssImporte)) {
            throw new PRACORException("format de fichier xml invalide : Impossible de retrouver le NSS du requérant. ");
        }

        if ((demande == null) || (demande.getLength() == 0)) {
            throw new PRACORException("format de fichier xml invalide: pas d'element demande");
        }

        // Contrôle que les données importées correspondent bien au prononcé
        // courant.
        // Permet d'éviter que l'utilisateur click sur importer les données sans
        // avoir préalablement calculée
        // les IJ. Dans ce cas précis, l'importation des données se fait avec le
        // dernier calcul effectué sur le poste de l'utilisateur.
        try {

            String nss1 = prononce.loadDemande(transaction).loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            String nss2 = nssImporte;

            if ((nss1 != null) && (nss2 != null)) {
                nss1 = JadeStringUtil.removeChar(nss1, '.');
                nss2 = JadeStringUtil.removeChar(nss2, '.');
                // TODO Le test nss1 égal nss2 a été remplacé par le test nss1
                // se termine par nss2.
                // Lorsqu’ACOR supportera le format à 13 positions, l’ancien
                // test pourra être restauré.
                if (!nss1.endsWith(nss2)) {
                    throw new PRACORException("Les données importées ne correspondent pas au prononcé courant (7)");
                }
            }

        } catch (PRACORException e) {
            throw e;
        } catch (Exception e2) {
            JadeLogger.error(e2, e2.getMessage());
            e2.printStackTrace();
        }

        // iterer sur toutes bases de calcul
        NodeList list = document.getElementsByTagName("paiement");

        Map paiementMap = null;
        if (list.getLength() > 0) {
            paiementMap = IJACORAbstractXMLFileParser.childrenMap(list.item(0));
        } else {
            // nouvelle version de ACOR, apparemment le tag <paiement> est renommé en <decompte> dans la f_calcul.xml
            // --> nouvelle tentative.
            list = document.getElementsByTagName("decompte");

            if (list.getLength() > 0) {
                paiementMap = IJACORAbstractXMLFileParser.childrenMap(list.item(0));
            } else {
                throw new PRACORException("format de fichier invalide: il manque l'element paiement");
            }
        }

        Node paiementNode = list.item(0);
        //
        String indemniteExt = "0.0";
        String indemniteInt = "0.0";
        String montantBrutExt = "0.0";
        String montantBrutInt = "0.0";

        for (int id = 0; id < paiementNode.getChildNodes().getLength(); ++id) {
            Node node = paiementNode.getChildNodes().item(id);
            // On récupère les décomptes
            if (node.getNodeName().equals("decompte_categorie")) {
                Map decompteMap = IJACORAbstractXMLFileParser.childrenMap(node);
                if (PRACORConst.CA_TYPE_MESURE_EXTERNE.equals(IJACORAbstractXMLFileParser.getNodeValue(decompteMap,
                        "categorie", true))) {
                    indemniteExt = IJACORAbstractXMLFileParser.getNodeValue(decompteMap, "montant_categorie_j", true);
                    montantBrutExt = IJACORAbstractXMLFileParser.getNodeValue(decompteMap, "montant_categorie_total",
                            true);
                } else {
                    indemniteInt = IJACORAbstractXMLFileParser.getNodeValue(decompteMap, "montant_categorie_j", true);
                    montantBrutInt = IJACORAbstractXMLFileParser.getNodeValue(decompteMap, "montant_categorie_total",
                            true);
                }
            }
        }

        // creation de la prestation, d'apres le schema, il y a au maximum UN
        // element paiement
        IJPrestation prestation = new IJPrestation();

        prestation.setDateDebut(IJACORAbstractXMLFileParser.getNodeDateAAAAMMJJValue(paiementMap, "date_debut", true));
        prestation.setDateFin(IJACORAbstractXMLFileParser.getNodeDateAAAAMMJJValue(paiementMap, "date_fin", true));
        prestation.setIdIJCalculee(ijCalculee.getIdIJCalculee());
        prestation.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());

        prestation.setMontantBrutExterne(montantBrutExt);
        prestation.setMontantBrutInterne(montantBrutInt);
        prestation.setNombreJoursExt(IJACORAbstractXMLFileParser.getNodeValue(paiementMap, "njours_ext", false));
        prestation.setNombreJoursInt(IJACORAbstractXMLFileParser.getNodeValue(paiementMap, "njours_int", false));
        prestation.setDateDecompte(JACalendar.todayJJsMMsAAAA());
        prestation.setMontantBrut(IJACORAbstractXMLFileParser.getNodeValue(paiementMap, "montant_global", true));
        retValue.add(prestation);

        // on recopie les montants journalier
        prestation.setMontantJournalierExterne(indemniteExt);
        prestation.setMontantJournalierInterne(indemniteInt);
        IJACORAbstractXMLFileParser.save(session, transaction, prestation);

        if ((indemniteExt == null) && (indemniteInt == null)) {
            throw new PRACORException(session.getLabel("AUCUNE_IJ_CALCULEE"));
        }

        return retValue;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJACORPrestationParser.
     */
    private IJACORPrestationParser() {
    }
}
