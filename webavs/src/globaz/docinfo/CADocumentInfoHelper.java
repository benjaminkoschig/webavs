/*
 * Créé le 28 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.docinfo;

import globaz.globall.db.BSession;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;

/**
 * Classe implémentant la structure jadePublishInfo avec les infos provenant de OSIRIS
 * 
 * @author sda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CADocumentInfoHelper {
    /* Constantes */
    public static final String SECTION_ID = "osiris.section.id";
    public static final String SECTION_ID_EXTERNE = "osiris.section.idExterne";
    public static final String SECTION_TYPE = "osiris.section.type";

    // Permet de remplir la structure en passant un APISection en paramètres
    public static void fill(JadePublishDocumentInfo info, APISection section) throws Exception {

        info.setDocumentProperty(SECTION_ID_EXTERNE, section.getIdExterne());
        info.setDocumentProperty(SECTION_TYPE, section.getIdTypeSection());
    }

    // Permet de remplir la structure en passant un CASection en paramètres
    public static void fill(JadePublishDocumentInfo info, CASection section) throws Exception {
        info.setDocumentProperty(SECTION_ID_EXTERNE, section.getIdExterne());
        info.setDocumentProperty(SECTION_TYPE, section.getIdTypeSection());
    }

    // Permet de remplir la structure en passant l'idSection en paramètres
    public static void fill(JadePublishDocumentInfo info, String idSection, BSession session) throws Exception {
        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idSection);
        section.retrieve();
        fill(info, section);
    }

    // Permet de remplir la structure en passant une entête de facture
    public static void fill(JadePublishDocumentInfo info, String idExterne, String idType, String date)
            throws Exception {

        info.setDocumentProperty(SECTION_ID_EXTERNE, idExterne);
        info.setDocumentProperty(SECTION_TYPE, idType);
        info.setDocumentDate(date);
    }

    private CADocumentInfoHelper() {
    }
}
