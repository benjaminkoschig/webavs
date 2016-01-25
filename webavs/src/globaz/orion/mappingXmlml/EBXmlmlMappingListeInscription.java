package globaz.orion.mappingXmlml;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.process.EBImprimerListeInscription;
import globaz.orion.utils.OrionContainer;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;

public class EBXmlmlMappingListeInscription {

    private static final String CELL_E_MAIL = "eMail";
    private static final String CELL_MODE_DEC_SALAIRE = "modeDecSalaire";
    private static final String CELL_NOM_PRENOM = "nomPrenom";
    private static final String CELL_NUM_TEL = "numTel";
    private static final String CELL_NUMERO_AFFILIE = "numeroAffilie";
    private static final String CELL_RAISON_SOCIALE = "raisonSociale";
    private static final String CELL_REMARQUE = "remarque";
    private static final String CELL_RESULTAT = "resultat";

    /**
     * @param container
     * @param inscription
     * @param session
     * @throws Exception
     */
    private static void loadDetail(OrionContainer container, InscriptionEbusiness inscription, BSession session)
            throws Exception {
        container.put(EBXmlmlMappingListeInscription.CELL_NUMERO_AFFILIE, inscription.getNumAffilie());
        container.put(EBXmlmlMappingListeInscription.CELL_RAISON_SOCIALE, inscription.getRaisonSociale());
        container.put(EBXmlmlMappingListeInscription.CELL_NOM_PRENOM,
                inscription.getNom() + " " + inscription.getPrenom());
        container.put(EBXmlmlMappingListeInscription.CELL_E_MAIL, inscription.getMail());
        container.put(EBXmlmlMappingListeInscription.CELL_NUM_TEL, inscription.getTel());
        container.put(EBXmlmlMappingListeInscription.CELL_MODE_DEC_SALAIRE,
                EBXmlmlMappingListeInscription.modeDec(session, inscription.getModeDeclSalaire()));
        container.put(EBXmlmlMappingListeInscription.CELL_RESULTAT,
                EBXmlmlMappingListeInscription.statut(session, inscription.getStatut()));
        container.put(EBXmlmlMappingListeInscription.CELL_REMARQUE, inscription.getRemarque());
    }

    /**
     * @param container
     * @param session
     */
    private static void loadHeader(OrionContainer container, BSession session) {
        container.put(IEBListeAcl.HEADER_TITRE,
                session.getLabel("LISTE_INSCRIPTION_TITRE") + " : " + JACalendar.todayJJsMMsAAAA());

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IEBListeAcl.HEADER_BLANK_1, "");
        container.put(IEBListeAcl.HEADER_BLANK_2, "");
        container.put(IEBListeAcl.HEADER_BLANK_3, "");
    }

    /**
     * @param inscriptions
     *            tableau de Inscription
     * @param process
     * @return
     * @throws Exception
     */
    public static OrionContainer loadResults(InscriptionEbusiness[] inscriptions, EBImprimerListeInscription process)
            throws Exception {
        OrionContainer container = new OrionContainer();

        EBXmlmlMappingListeInscription.loadHeader(container, process.getSession());

        for (int i = 0; (i < inscriptions.length) && !process.isAborted(); i++) {
            EBXmlmlMappingListeInscription.loadDetail(container, inscriptions[i], process.getSession());
            process.incProgressCounter();
        }

        return container;
    }

    /**
     * @param idModeDec
     * @return
     */
    private static String modeDec(BSession session, String idModeDec) {
        if (JadeStringUtil.isBlankOrZero(idModeDec)) {
            return "";
        }

        String modeDec = "";

        switch (Integer.parseInt(idModeDec)) {
            case 1:
                modeDec = session.getLabel("PUCS_TYPE_DAN");
                break;
            case 2:
                modeDec = session.getLabel("PUCS_TYPE_PUCS");
                break;
        }

        return modeDec;
    }

    /**
     * @param idStatut
     * @return
     */
    private static String statut(BSession session, String idStatut) {
        if (JadeStringUtil.isBlankOrZero(idStatut)) {
            return "";
        }

        String statut = "";

        switch (Integer.parseInt(idStatut)) {
            case 1:
                statut = session.getLabel("STATUT_NOUVELLE");
                break;
            case 2:
                statut = session.getLabel("STATUT_TERMINEE");
                break;
            case 3:
                statut = session.getLabel("STATUT_ERREUR");
                break;
        }

        return statut;
    }
}
