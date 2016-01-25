package globaz.musca.module.interet;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;

public abstract class FAInteretModuleFacturation implements IntModuleFacturation {

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRecomptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRegenerer(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
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

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrGen(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#comptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return true;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#generer(globaz.musca.api.IFAPassage, globaz.globall.db.BProcess)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#imprimer(globaz.musca.api.IFAPassage, globaz.globall.db.BProcess)
     */
    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#recomptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#repriseOnErrorCompta(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#repriseOnErrorGen(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#supprimer(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

}
