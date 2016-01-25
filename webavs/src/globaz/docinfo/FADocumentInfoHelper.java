package globaz.docinfo;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * Classe implémentant la structure jadePublishInfo avec les infos provenant de OSIRIS
 * 
 * @author sda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class FADocumentInfoHelper {
    public static String DATE_FACTURATION = "musca.date.facturation";
    /* Constantes */
    public static String ID_EXTERNE_FORMATE = "musca.idExterne.formate";
    public static String ID_PASSAGE = "musca.id.passage";

    // Permet de remplir la structure
    public static void fill(JadePublishDocumentInfo info, String idExterne) throws Exception {
        info.setDocumentProperty(ID_EXTERNE_FORMATE, getIdExterneFormate(idExterne));
    }

    // Permet de remplir la structure
    public static void fill(JadePublishDocumentInfo info, String idPassage, String dateFacturation) throws Exception {
        info.setDocumentProperty(ID_PASSAGE, idPassage);
        info.setDocumentProperty(DATE_FACTURATION, dateFacturation);
    }

    private static String getIdExterneFormate(String idExterne) {
        String idExterneFormate = "";
        int idExterneInt = JadeStringUtil.parseInt(idExterne, 0);
        int tmp1 = idExterneInt / 100000;
        idExterneFormate = JadeStringUtil.leftJustifyInteger("" + tmp1, 3) + ".";
        int tmp2 = (idExterneInt - tmp1 * 100000) / 1000;
        idExterneFormate += JadeStringUtil.rightJustifyInteger("" + tmp2, 2) + ".";
        int tmp3 = (idExterneInt - tmp1 * 100000 - tmp2 * 1000);
        idExterneFormate += JadeStringUtil.leftJustifyInteger("" + tmp3, 3);
        return idExterneFormate;
    }

    private FADocumentInfoHelper() {
    }
}
