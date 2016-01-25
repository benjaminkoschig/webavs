package globaz.babel.test;


/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class TestAPI {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testAPIListeNoms() throws Exception {
        // BTransaction transaction = (BTransaction) SESSION.newTransaction();
        //
        // try {
        // transaction.openTransaction();
        //
        // // preparation
        // CTDocument document = peuplementBase();
        //
        // // test
        // ICTDocument helper = (ICTDocument) SESSION.getAPIFor(ICTDocument.class);
        //
        // helper.setActif(document.getActif());
        // helper.setDefault(document.getDefaut());
        // helper.setCsDomaine(document.getCsDomaine());
        // helper.setCsTypeDocument(document.getCsTypeDocument());
        // helper.setCsDestinataire(document.getCsDestinataire());
        // helper.setIdDocument(document.getIdDocument());
        // helper.setNom(document.getNom());
        // helper.setCodeIsoLangue(CTTexteSaisieViewBean.CODE_ISO_FRANCAIS);
        //
        // // resultat
        // Map resultat = helper.loadListeNoms();
        //
        // assertNotNull(resultat);
        // assertEquals(1, resultat.size()); // il n'y a qu'un document
        //
        // Map.Entry entry = (Map.Entry) resultat.entrySet().iterator().next();
        //
        // assertEquals(document.getIdDocument(), entry.getKey());
        // assertEquals(NOM_DOCUMENT_TEST, entry.getValue());
        // } finally {
        // try {
        // transaction.rollback();
        // } finally {
        // transaction.closeTransaction();
        // }
        // }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testAPILoad() throws Exception {
        // BTransaction transaction = (BTransaction) SESSION.newTransaction();
        //
        // try {
        // transaction.openTransaction();
        //
        // // preparation
        // CTDocument document = peuplementBase();
        //
        // // test
        // ICTDocument helper = (ICTDocument) SESSION.getAPIFor(ICTDocument.class);
        //
        // helper.setActif(document.getActif());
        // helper.setDefault(document.getDefaut());
        // helper.setCsDomaine(document.getCsDomaine());
        // helper.setCsTypeDocument(document.getCsTypeDocument());
        // helper.setCsDestinataire(document.getCsDestinataire());
        // helper.setIdDocument(document.getIdDocument());
        // helper.setNom(document.getNom());
        // helper.setCodeIsoLangue(CTTexteSaisieViewBean.CODE_ISO_FRANCAIS);
        //
        // // resultat
        // ICTDocument[] resultat = helper.load();
        //
        // assertNotNull(resultat);
        // assertEquals(1, resultat.length); // il n'y a qu'un document
        // assertEquals(CTTexteSaisieViewBean.CODE_ISO_FRANCAIS, resultat[0].getCodeIsoLangue());
        // assertEquals(document.getCsDomaine(), resultat[0].getCsDomaine());
        // assertEquals(document.getCsTypeDocument(), resultat[0].getCsTypeDocument());
        // assertEquals(document.getIdDocument(), resultat[0].getIdDocument());
        // assertEquals(document.getNom(), resultat[0].getNom());
        // assertEquals(document.getActif(), resultat[0].isActif());
        // assertEquals(NB_NIVEAUX, resultat[0].size());
        // assertEquals(NB_ELEMENTS_NIVEAU_1, resultat[0].getTextes(1).size());
        // assertEquals(NB_ELEMENTS_NIVEAU_2, resultat[0].getTextes(2).size());
        // } finally {
        // try {
        // transaction.rollback();
        // } finally {
        // transaction.closeTransaction();
        // }
        // }
    }
}
