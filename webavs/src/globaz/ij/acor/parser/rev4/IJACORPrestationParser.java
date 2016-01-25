package globaz.ij.acor.parser.rev4;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCalcul;
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
 * @author vre
 */
public class IJACORPrestationParser extends IJACORAbstractXMLFileParser {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * ajouter deux nombres codes en string.
     * 
     * @param op1
     * @param op2
     * 
     * @return
     */
    private static final String add(String op1, String op2) {
        FWCurrency currency = new FWCurrency(op1);

        currency.add(op2);

        return currency.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * multiplie deux nombres, arrondit le resultat a 2 decimales et 5 centimes et retourne cette valeur en tant que
     * chaine.
     * 
     * @param op1
     * @param op2
     * 
     * @return
     */
    private static final String multiply(String op1, String op2) {
        return JANumberFormatter.format(PRCalcul.multiply(op1, op2), 0.05, 2, JANumberFormatter.NEAR);
    }

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
    public static final List parse(BSession session, IJPrononce prononce, IJBaseIndemnisation baseIndemnisation,
            IJIJCalculee ijCalculee, Reader reader) throws PRACORException {
        LinkedList retValue = new LinkedList();
        Document document = loadDocument(reader);

        NodeList demande = document.getElementsByTagName("demande");

        String nssImporte = null;
        if (demande != null && demande.getLength() >= 1) {
            Map demandeChildren = childrenMap(demande.item(0));
            if (demandeChildren.containsKey("id_assure")) {
                nssImporte = getNodeValue(demandeChildren, "id_assure", true);
            }
        }

        // HACK: comme ACOR ne retourne que le montant global, on recupere les
        // indemnites int./ext. pour recalculer
        String indemniteExt = "0.0";
        String indemniteInt = "0.0";
        NodeList cycles = document.getElementsByTagName("cycle");

        for (int idCycle = 0; idCycle < cycles.getLength(); ++idCycle) {
            Map cycleChildren = childrenMap(cycles.item(idCycle));

            if (cycleChildren.containsKey("paiement")) {

                NodeList ijs = cycles.item(idCycle).getChildNodes();
                for (int idIj = 0; idIj < ijs.getLength(); ++idIj) {
                    Node node = ijs.item(idIj);

                    if ("ij".equals(node.getNodeName())) {
                        Map ijChildren = childrenMap(node);

                        if (PRACORConst.CA_TYPE_MESURE_EXTERNE.equals(getNodeValue(ijChildren, "categorie", true))) {
                            indemniteExt = getNodeValue(ijChildren, "montant_indemnite", true);
                        } else {
                            indemniteInt = getNodeValue(ijChildren, "montant_indemnite", true);
                        }
                    }
                }
            }

        }

        if (indemniteExt == null && indemniteInt == null) {
            throw new PRACORException(session.getLabel("AUCUNE_IJ_CALCULEE"));
        }

        // recuperation des prestations
        NodeList list = document.getElementsByTagName("paiement");
        Map paiementChildren;

        if (list.getLength() > 0) {
            paiementChildren = childrenMap(list.item(0));
        } else {
            throw new PRACORException("format de fichier invalide: il manque un element paiement");
        }

        // Contrôle que les données importées correspondent bien au prononcé
        // courant.
        // Permet d'éviter que l'utilisateur click sur importer les données sans
        // avoir préalablement calculée
        // les IJ. Dans ce cas précis, l'importation des données se fait avec le
        // dernier calcul effectué sur le poste de l'utilisateur.
        try {

            String nss1 = prononce.loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            String nss2 = nssImporte;

            if (nss1 != null && nss2 != null) {
                nss1 = JadeStringUtil.removeChar(nss1, '.');
                nss2 = JadeStringUtil.removeChar(nss2, '.');
                // TODO Le test nss1 égal nss2 a été remplacé par le test nss1
                // se termine par nss2.
                // Lorsqu’ACOR supportera le format à 13 positions, l’ancien
                // test pourra être restauré.
                if (!nss1.endsWith(nss2)) {
                    throw new PRACORException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + "(7)");
                }
            }

        } catch (PRACORException e) {
            throw e;
        } catch (Exception e2) {
            JadeLogger.error(e2, e2.getMessage());
            e2.printStackTrace();
        }

        // creation de la prestation, d'apres le schema, il y a au maximum UN
        // element paiement
        IJPrestation prestation = new IJPrestation();

        retValue.add(prestation);

        // on recopie les montants journalier
        prestation.setMontantJournalierExterne(indemniteExt);
        prestation.setMontantJournalierInterne(indemniteInt);

        prestation.setDateDebut(getNodeDateValue(paiementChildren, "date_debut", true));
        prestation.setDateFin(getNodeDateValue(paiementChildren, "date_fin", true));
        prestation.setIdIJCalculee(ijCalculee.getIdIJCalculee());
        prestation.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        prestation.setMontantBrutExterne(multiply(indemniteExt, getNodeValue(paiementChildren, "njours_ext", false)));
        prestation.setMontantBrutInterne(multiply(indemniteInt, getNodeValue(paiementChildren, "njours_int", false)));
        prestation.setNombreJoursExt(getNodeValue(paiementChildren, "njours_ext", false));
        prestation.setNombreJoursInt(getNodeValue(paiementChildren, "njours_int", false));
        prestation.setDateDecompte(JACalendar.todayJJsMMsAAAA());
        prestation.setMontantBrut(add(prestation.getMontantBrut(),
                getNodeValue(paiementChildren, "montant_global", true)));

        save(session, prestation);

        return retValue;
    }

    /**
     * Crée une nouvelle instance de la classe IJACORPrestationParser.
     */
    private IJACORPrestationParser() {
    }
}
