package globaz.corvus.acor.parser.xml.rev10;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import globaz.corvus.acor.parser.xml.rev10.fcalcul.REBaseCalculXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.fcalcul.REDecisionXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.fcalcul.REPrestationXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.fcalcul.RERenteXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.fcalcul.REXmlDataContainer;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.acor.PRACORException;

/**
 * <H1>Description</H1>
 * <p>
 * Un parser capable de parser le fichier xml f_calcul.xml retourn� par ACOR. Les donn�es remont�es de la feuille de
 * calcul sont les suivantes : - ann�e bonification pour tache �ducative - Taux r�duction pour anticipation - D�cision
 * (utilis� pour d�terminer a qui s'applique les info ci dessus.)
 * </p>
 *
 * @author scr
 */
public class REImportFCalculACOR extends REACORAbstractXMLFileParser {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Parse le fichier f_calcul.xml (Reader).
     *
     * @return RXmlDataContainer
     * @throws Exception
     */

    public static final REXmlDataContainer importFCalcul(BSession session, BTransaction transaction, Reader reader)
            throws Exception, PRACORException {

        Document document = REACORAbstractXMLFileParser.loadDocument(reader);

        NodeList nl = document.getElementsByTagName("f_calcul");
        if (nl.getLength() == 0) {
            throw new PRACORException("format de fichier xml invalide: pas d'element <f_calcul>");
        }

        Node nFeuilleCalcul = nl.item(0);

        // Parsing de la feuille de calcul...
        REXmlDataContainer dc = new REXmlDataContainer();
        // dc.setDecisions(REImportFCalculACOR.parseDecisions(nFeuilleCalcul));
        dc.setBasesCalcul(REImportFCalculACOR.parseBasesCalcul(nFeuilleCalcul, dc));

        return dc;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    protected static REBaseCalculXmlDataStructure[] parseBasesCalcul(Node nFC, REXmlDataContainer dc) throws Exception {

        List lst = new ArrayList();
        List lstDecisions = new ArrayList();

        NodeList nl = nFC.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if ("bases_calcul".equals(n.getNodeName())) {
                Map mapBaseCalcul = REACORAbstractXMLFileParser.childrenMap(n);

                REBaseCalculXmlDataStructure bc = new REBaseCalculXmlDataStructure();
                bc.setId(REACORAbstractXMLFileParser.getNodeValue(mapBaseCalcul, "id", true));
                bc.setGenerateur(REACORAbstractXMLFileParser.getNodeValue(mapBaseCalcul, "generateur", true));

                Node nBR = REACORAbstractXMLFileParser.getNode(mapBaseCalcul, "base_ram", false);
                if (nBR != null) {
                    Map mapBaseRam = REACORAbstractXMLFileParser.childrenMap(nBR);

                    Node nBTE = REACORAbstractXMLFileParser.getNode(mapBaseRam, "bte", false);
                    if (nBTE != null) {
                        Map mapBTE = REACORAbstractXMLFileParser.childrenMap(nBTE);
                        bc.setAnneeBte1(REACORAbstractXMLFileParser.getNodeValue(mapBTE, "an1", false));
                        bc.setAnneeBte2(REACORAbstractXMLFileParser.getNodeValue(mapBTE, "an2", false));
                        bc.setAnneeBte4(REACORAbstractXMLFileParser.getNodeValue(mapBTE, "an4", false));
                    }

                }
                Node nAnticipation = REACORAbstractXMLFileParser.getNode(mapBaseCalcul, "anticipation", false);
                if (nAnticipation != null) {
                    Map mapAnticipation = REACORAbstractXMLFileParser.childrenMap(nAnticipation);

                    Node nTranche = REACORAbstractXMLFileParser.getNode(mapAnticipation, "tranche", false);
                    if (nTranche != null) {
                        Map mapTranche = REACORAbstractXMLFileParser.childrenMap(nTranche);
                        bc.setTauxReductionAnticipation(REACORAbstractXMLFileParser.getNodeValue(mapTranche,
                                "taux_reduction_anticipation", false));
                    }

                }

                Node nDecision = REACORAbstractXMLFileParser.getNode(mapBaseCalcul, "decision", false);
                if (nDecision != null) {
                    Map mapDecision = REACORAbstractXMLFileParser.childrenMap(nDecision);
                    /*
                     * Prestations
                     */
                    Node nPrestation = REACORAbstractXMLFileParser.getNode(mapDecision, "prestation", true);
                    Map mapPrestation = REACORAbstractXMLFileParser.childrenMap(nPrestation);

                    REPrestationXmlDataStructure prestation = new REPrestationXmlDataStructure();
                    prestation.setNssBeneficiaire(
                            REACORAbstractXMLFileParser.getNodeValue(mapPrestation, "beneficiaire", true));
                    /*
                     *
                     * Rente
                     */
                    Node nRente = REACORAbstractXMLFileParser.getNode(mapPrestation, "rente", true);
                    Map mapRente = REACORAbstractXMLFileParser.childrenMap(nRente);

                    RERenteXmlDataStructure rente = new RERenteXmlDataStructure();
                    rente.setDateDebutDroit(REACORAbstractXMLFileParser.getNodeValue(mapRente, "debut_droit", false));
                    rente.setIdBaseCalcul(bc.getId());
                    rente.setCodePrestation(REACORAbstractXMLFileParser.getNodeValue(mapRente, "genre", true));
                    // On ne r�cup�re plus les remarques de cette mani�re
                    // rente.setRemarques(REACORAbstractXMLFileParser.getNodeValue(mapRente, "remarques", false));

                    prestation.setRente(rente);
                    REDecisionXmlDataStructure decision = new REDecisionXmlDataStructure();
                    decision.setPrestation(prestation);
                    bc.setDecision(decision);
                    lstDecisions.add(decision);
                }

                lst.add(bc);
            }
        }
        if (lstDecisions.isEmpty()) {
            dc.setDecisions(null);
        } else {
            dc.setDecisions((REDecisionXmlDataStructure[]) lstDecisions
                    .toArray(new REDecisionXmlDataStructure[lstDecisions.size()]));
        }
        if (lst.isEmpty()) {
            return null;
        }

        return (REBaseCalculXmlDataStructure[]) lst.toArray(new REBaseCalculXmlDataStructure[lst.size()]);
    }

    protected static REDecisionXmlDataStructure[] parseDecisions(Node nFC) throws Exception {

        List lst = new ArrayList();

        NodeList nl = nFC.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nDecision = nl.item(i);
            if ("decision".equals(nDecision.getNodeName())) {
                Map mapDecision = REACORAbstractXMLFileParser.childrenMap(nDecision);
                REDecisionXmlDataStructure decision = new REDecisionXmlDataStructure();

                /*
                 * Prestations
                 */
                Node nPrestation = REACORAbstractXMLFileParser.getNode(mapDecision, "prestation", true);
                Map mapPrestation = REACORAbstractXMLFileParser.childrenMap(nPrestation);

                REPrestationXmlDataStructure prestation = new REPrestationXmlDataStructure();
                prestation.setNssBeneficiaire(
                        REACORAbstractXMLFileParser.getNodeValue(mapPrestation, "beneficiaire", true));
                /*
                 *
                 * Rente
                 */
                Node nRente = REACORAbstractXMLFileParser.getNode(mapPrestation, "rente", true);
                Map mapRente = REACORAbstractXMLFileParser.childrenMap(nRente);

                RERenteXmlDataStructure rente = new RERenteXmlDataStructure();
                rente.setDateDebutDroit(REACORAbstractXMLFileParser.getNodeValue(mapRente, "debut_droit", false));
                rente.setIdBaseCalcul(REACORAbstractXMLFileParser.getNodeValue(mapRente, "bases", false));
                rente.setCodePrestation(REACORAbstractXMLFileParser.getNodeValue(mapRente, "genre", true));
                // On ne r�cup�re plus les remarques de cette mani�re
                // rente.setRemarques(REACORAbstractXMLFileParser.getNodeValue(mapRente, "remarques", false));

                prestation.setRente(rente);
                decision.setPrestation(prestation);
                lst.add(decision);
            }
        }
        if (lst.isEmpty()) {
            return null;
        }

        return (REDecisionXmlDataStructure[]) lst.toArray(new REDecisionXmlDataStructure[lst.size()]);
    }

    /**
     * Cr�e une nouvelle instance de la classe REACORPrestationsParser.
     */
    private REImportFCalculACOR() {
    }

}
