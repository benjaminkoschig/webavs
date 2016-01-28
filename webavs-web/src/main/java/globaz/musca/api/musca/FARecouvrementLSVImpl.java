/*
 * Créé le 10 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.process.FAPassageRecouvrementProcess;

/**
 * @author mmu
 */
public class FARecouvrementLSVImpl implements IntModuleFacturation {

    /**
	 *   
	 */
    public FARecouvrementLSVImpl() {
        super();
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRecomptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRegenerer(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess) Il n'y a pas d'afacts du module à supprimer
     */
    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrCom(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrGen(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess) Il n'y a pas d'afacts du module à supprimer
     */
    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#comptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#generer(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // FAPassageRecouvrementLSVProcess cptProc = new
        // FAPassageRecouvrementLSVProcess();
        FAPassageRecouvrementProcess cptProc = new FAPassageRecouvrementProcess();
        // copier le process parent
        cptProc.setParentWithCopy(context);
        //
        cptProc.setIdPassage(passage.getIdPassage());
        cptProc.setEMailAddress(context.getEMailAddress());
        return cptProc._executeRecouvrementProcess(passage);
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#imprimer(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#recomptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    /*
     * TOUTES LES METHODES SUIVANTES NE SONT PAS IMPLEMENTEES ET RETOURNENT FALSE PAR DEFAUT
     */
    /**
     * @see globaz.musca.external.IntModuleFacturation#regenerer(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess) Il n'y a pas d'afacts du module à supprimer
     */
    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#repriseOnErrorCompta(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#repriseOnErrorGen(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    /**
     * @see globaz.musca.external.IntModuleFacturation#supprimer(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

}
