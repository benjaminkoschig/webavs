/*
 * Cr?? le 3 juin 05
 */
package globaz.phenix.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.phenix.itext.taxation.definitive.CPListeTaxationDefinitiveXlsPdf;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class CPListeTaxationDefinitiveModuleImpl implements IntModuleFacturation {

    private static final long serialVersionUID = -5192240794451174398L;

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // suprime les affacts d?j? g?n?r?s
        return true;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        // suprime les affacts d?j? g?n?r?s
        return true;
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        CPListeTaxationDefinitiveXlsPdf lst = new CPListeTaxationDefinitiveXlsPdf();
        lst.setParent(context);
        lst.setNoPassage(passage.getIdPassage());
        lst.setEMailAddress(context.getEMailAddress());
        lst.setFromFacturation(true);
        lst.executeProcess();

        // contr?ler si le process a fonctionn?
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
        // appelle la m?thode g?n?rer
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // appelle la m?thode g?n?rer
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
