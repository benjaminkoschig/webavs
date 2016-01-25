package globaz.corvus.api.musca;

import globaz.corvus.process.REFacturationProcess;
import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;

/**
 * Créé le 21 mai 08
 * 
 * @author scr
 * 
 *         Module de facturation (restitution) des RENTES, appelé par MUSCA
 */
public class REFacturationModule implements IntModuleFacturation {

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

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
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
        return false;
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        // process de facturation
        REFacturationProcess proc = new REFacturationProcess();

        // paramétrisation du process
        proc.setParentWithCopy(context);
        proc.setNumeroPassage(passage.getIdPassage());
        proc.setDateComptable(passage.getDateFacturation());
        proc.setSession(context.getSession());
        proc.setTransaction(context.getTransaction());
        proc.setIdModuleFacturation(idModuleFacturation);

        // lancement du process
        return proc._executeProcessFacturation();
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
