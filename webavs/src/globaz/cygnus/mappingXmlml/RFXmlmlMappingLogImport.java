package globaz.cygnus.mappingXmlml;

import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author JJE
 * @author MBO revision 10.01.2014
 * 
 */
public class RFXmlmlMappingLogImport {

    public static final String NSS_A_ZERO_SI_VIDE = "000.0000.0000.00";

    /**
     * Methode permettant de creer le corps du document (log ->new String[] { typeDeMessage, idAdaptationJournaliere,
     * idTiersBeneficiaire, nss, msgErreur, idDecisionPc, numDecisionPc })
     * 
     * @param container
     * @param entity
     * @param process
     * @throws RFXmlmlException
     * @throws Exception
     */
    private static void loadDetail(RFXmlmlContainer container, ArrayList<String> ligne) throws RFXmlmlException,
            Exception {

        if (!JadeStringUtil.isEmpty(ligne.get(0))) {
            container.put("numHome", ligne.get(0));
        } else {
            container.put("numHome", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(1))) {
            container.put("nomHome", ligne.get(1));
        } else {
            container.put("nomHome", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(2)) && !ligne.get(2).equals(RFXmlmlMappingLogImport.NSS_A_ZERO_SI_VIDE)) {
            container.put("numAvs", ligne.get(2));
        } else {
            container.put("numAvs", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(3))) {
            container.put("nomPrenom", ligne.get(3));
        } else {
            container.put("nomPrenom", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(4))) {
            container.put("fraisJournalier", ligne.get(4));
        } else {
            container.put("fraisJournalier", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(5))) {
            container.put("dateDebut", ligne.get(5));
        } else {
            container.put("dateDebut", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(6))) {
            container.put("dateFin", ligne.get(6));
        } else {
            container.put("dateFin", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(7))) {
            container.put("nbJours", ligne.get(7));
        } else {
            container.put("nbJours", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(8))) {
            container.put("montantTotal", ligne.get(8));
        } else {
            container.put("montantTotal", "null");
        }
        if (!JadeStringUtil.isEmpty(ligne.get(9))) {
            container.put("dateDecompte", ligne.get(9));
        } else {
            container.put("dateDecompte", "null");
        }
    }

    /**
     * Chargement des résultats de chaque ligne
     * 
     * @param manager
     * @param process
     * @return
     * @throws RFXmlmlException
     * @throws Exception
     */
    public static RFXmlmlContainer loadResults(List<ArrayList<String>> listeLignesARetourner) throws RFXmlmlException,
            Exception {

        RFXmlmlContainer container = new RFXmlmlContainer();

        for (ArrayList<String> ligne : listeLignesARetourner) {

            RFXmlmlMappingLogImport.loadDetail(container, ligne);
        }

        return container;
    }
}
