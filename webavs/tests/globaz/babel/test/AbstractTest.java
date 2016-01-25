package globaz.babel.test;

import globaz.babel.application.CTApplication;
import globaz.babel.db.cat.CTDocument;
import globaz.babel.db.cat.CTElement;
import globaz.babel.db.cat.CTTexte;
import globaz.babel.vb.cat.CTTexteSaisieViewBean;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe de base pour les tests qui etend TestCase de Junit et qui permet de mettre en place un contexte de test
 * approprié.
 * </p>
 * 
 * @author vre
 */
public class AbstractTest {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    protected static BSession SESSION;

    protected static final String USERNAME = "vre";
    protected static final String PASSWORD = "vre";

    protected static final String ID_TYPE_DOCUMENT_TEST = "999999";
    protected static final String CS_DOMAINE_TEST = "12345678";
    protected static final String CS_TYPE_DOCUMENT_TEST = "87654321";
    protected static final String CS_DESTINATAIRE_TEST = "12121212";

    protected static final String NOM_DOCUMENT_TEST = "test";

    protected static final int NB_NIVEAUX = 2;
    protected static final int NB_ELEMENTS_NIVEAU_1 = 4;
    protected static final int NB_ELEMENTS_NIVEAU_2 = 1;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * cree un type de document bidon un document bidon contenant 5 elements (4
     * au niveau 1 et un au niveau 2) contenant chacun des textes dans les trois
     * langues nationales.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected CTDocument peuplementBase() throws Exception {
        // insererer un type de document de test
        CTInsertableTypeDocument type = new CTInsertableTypeDocument();

        type.setSession(SESSION);
        type.setIdTypeDocument(ID_TYPE_DOCUMENT_TEST);
        type.setCsDomaine(CS_DOMAINE_TEST);
        type.setCsTypeDocument(CS_TYPE_DOCUMENT_TEST);
        type.add();

        // inserer un document de test
        CTDocument document = new CTDocument();

        document.setSession(SESSION);
        document.setIdTypeDocument(ID_TYPE_DOCUMENT_TEST);
        document.setCsDestinataire(CS_DESTINATAIRE_TEST);
        document.setNom(NOM_DOCUMENT_TEST);
        document.setActif(Boolean.TRUE);
        document.setDefaut(Boolean.TRUE);
        document.add();

        // juste pour le test
        document.setCsDomaine(CS_DOMAINE_TEST);
        document.setCsTypeDocument(CS_TYPE_DOCUMENT_TEST);

        // inserer des elements avec du texte
        // niveau 1, 4 elements
        insererElement(document, 1, NB_ELEMENTS_NIVEAU_1);

        // niveau 2, 1 elements
        insererElement(document, 2, NB_ELEMENTS_NIVEAU_2);

        return document;
    }

    /**
     * setter pour l'attribut up.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */

    protected void setUp() throws Exception {
        if (SESSION == null) {
            SESSION = (BSession) ((CTApplication) GlobazSystem.getApplication(CTApplication.DEFAULT_APPLICATION_BABEL))
                    .newSession(USERNAME, PASSWORD);
        }
    }

    private void insererElement(CTDocument document, int niveau, int nbElements) throws Exception {
        CTElement element;
        CTTexte texte;

        for (int idElement = nbElements; --idElement >= 0;) {
            element = new CTElement();

            element.setSession(SESSION);
            element.setIdDocument(document.getIdDocument());
            element.setNiveau(String.valueOf(niveau));
            element.setPosition(String.valueOf(idElement + 1));
            element.add();

            // texte allemand
            texte = new CTTexte();
            texte.setSession(SESSION);
            texte.setIdElement(element.getIdElement());
            texte.setCodeIsoLangue(CTTexteSaisieViewBean.CODE_ISO_ALLEMAND);
            texte.setDescription("de");
            texte.add();

            // texte francais
            texte = new CTTexte();
            texte.setSession(SESSION);
            texte.setIdElement(element.getIdElement());
            texte.setCodeIsoLangue(CTTexteSaisieViewBean.CODE_ISO_FRANCAIS);
            texte.setDescription("fr");
            texte.add();

            // texte italien
            texte = new CTTexte();
            texte.setSession(SESSION);
            texte.setIdElement(element.getIdElement());
            texte.setCodeIsoLangue(CTTexteSaisieViewBean.CODE_ISO_ITALIEN);
            texte.setDescription("it");
            texte.add();
        }
    }
}
