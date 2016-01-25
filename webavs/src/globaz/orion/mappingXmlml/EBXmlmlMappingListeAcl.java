package globaz.orion.mappingXmlml;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.orion.process.EBImprimerListeAcl;
import globaz.orion.utils.OrionContainer;
import ch.globaz.orion.business.models.acl.Acl;

public class EBXmlmlMappingListeAcl {

    // constantes
    public static final String PREFIXE_CODE_PAYS = "315";

    private static void loadDetail(OrionContainer container, Acl acl, BSession session) throws Exception {

        container.put(IEBListeAcl.NUMERO_ASSURE, acl.getNumeroAssure());
        container.put(IEBListeAcl.DATE_ENGAGEMENT, acl.getDateEngagement());
        container.put(IEBListeAcl.TYPE_ARC, acl.getTypeArc());
        container.put(IEBListeAcl.STATUT, acl.getStatut());
        container.put(IEBListeAcl.NOM_PRENOM, acl.getNomPrenom());
        container.put(IEBListeAcl.NATIONNALITE, acl.getNationnalite());
        container.put(IEBListeAcl.DATE_NAISSANCE, acl.getDateNaissance());
        container.put(IEBListeAcl.NUMERO_AFFILIE, acl.getNumeroAffilie());
    }

    private static void loadHeader(OrionContainer container, BSession session) {

        container.put(IEBListeAcl.HEADER_TITRE,
                session.getLabel("LISTE_ACL_TITRE") + " : " + JACalendar.todayJJsMMsAAAA());

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IEBListeAcl.HEADER_BLANK_1, "");
        container.put(IEBListeAcl.HEADER_BLANK_2, "");
        container.put(IEBListeAcl.HEADER_BLANK_3, "");
    }

    public static OrionContainer loadResults(Acl[] acl, EBImprimerListeAcl process) throws Exception {
        OrionContainer container = new OrionContainer();

        EBXmlmlMappingListeAcl.loadHeader(container, process.getSession());

        for (int i = 0; (i < acl.length) && !process.isAborted(); i++) {
            EBXmlmlMappingListeAcl.loadDetail(container, acl[i], process.getSession());
            process.incProgressCounter();

        }

        return container;
    }
}
