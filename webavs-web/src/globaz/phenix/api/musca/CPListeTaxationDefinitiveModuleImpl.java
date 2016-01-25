/*
 * Créé le 3 juin 05
 */
package globaz.phenix.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.phenix.itext.taxation.definitive.CPListeTaxationDefinitive;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class CPListeTaxationDefinitiveModuleImpl implements IntModuleFacturation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
    private static final long serialVersionUID = -5192240794451174398L;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * AUCUNE ACTION N'EST ACTION N'EST FAITE CONCERNANT LA COMPTABILISATION, L'IMPRESSION, LA SUPPRESSION
     */
    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRecomptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // suprime les affacts déjà générés
        return true;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrCom(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        // suprime les affacts déjà générés
        return true;
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        CPListeTaxationDefinitive lst = new CPListeTaxationDefinitive();
        lst.setParent(context);
        lst.setNoPassage(passage.getIdPassage());
        lst.setEMailAddress(context.getEMailAddress());

        lst.executeProcess();

        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // appelle la méthode générer
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // appelle la méthode générer
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
