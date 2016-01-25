package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliationManager;

public class DSContRappel_doc extends DSAbstractPrincipaleGenererEtape {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public DSContRappel_doc() throws Exception {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /**
     * @param session
     * @param nomDocument
     * @throws Exception
     */
    public DSContRappel_doc(BSession session) throws Exception {
        super(session, session.getLabel(DSPreImpressionContentieux_Param.L_NOMDOCCONTENTIEUX));
    }

    /**
     * @param session
     * @param nomDocument
     * @throws Exception
     */
    public DSContRappel_doc(BSession session, String nomDocument) throws Exception {
        super(session, nomDocument);
        // TODO Raccord de constructeur auto-généré
    }

    @Override
    public String getDocInforomNum() {
        return "0091CDS";
    }

    @Override
    protected ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "Error while api for document");
        }
        document.setISession(getSession());
        document.setCsDomaine(Doc2_preImpressionDeclaration.CS_DOMAINE);
        AFAffiliationManager affMgr = new AFAffiliationManager();
        affMgr.setSession(getSession());
        affMgr.setForAffilieNumero(getNumAff());
        try {
            affMgr.find();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "L'affilié n'a pu être identifié");
        }

        document.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_RAPPEL);
        document.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        document.setDefault(new Boolean(true));
        document.setActif(new Boolean(true));
        try {
            document.setCodeIsoLangue(getIsoLangueDestinataire());
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }

        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.draco.print.itext.DSAbstractDocument#getTemplate()
     */
    @Override
    protected String getTemplate() {
        return DSPreImpressionContentieux_Param.TEMPLATE_CONTENTIEUX;

    }

}
