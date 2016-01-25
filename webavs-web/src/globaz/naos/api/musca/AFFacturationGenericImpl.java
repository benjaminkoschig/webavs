package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class AFFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public AFFacturationGenericImpl() {
        super();
    }

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

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRegenerer(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
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

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrGen(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        // suprime les affacts déjà générés
        return true;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#comptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#generer(globaz.musca.api.IFAPassage, globaz.globall.db.BProcess)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // CETTE METHODE DOIT IMPERATIVEMENT ETRE IMPLEMENTEE PAR LES
        // SOUS-CLASSES !!!
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
     * @see globaz.musca.external.IntModuleFacturation#regenerer(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // appelle la méthode générer
        return generer(passage, context, idModuleFacturation);
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
        // appelle la méthode générer
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
