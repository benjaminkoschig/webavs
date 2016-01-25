package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.AFImpressionDecisionCapCgasProcess;

public class AFImpressionDecisionCapCgasImpl implements IntModuleFacturation {

    public AFImpressionDecisionCapCgasImpl() {
        super();
    }

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return false;
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {

        AFImpressionDecisionCapCgasProcess process = new AFImpressionDecisionCapCgasProcess();
        process.setParentWithCopy(context);
        process.setNumeroPassageFacturation(passage.getIdPassage());
        process.setEMailAddress(context.getEMailAddress());
        process.executeProcess();

        // contrôler si le process a fonctionné
        return !(context.isAborted() || context.isOnError() || context.getSession().hasErrors());

    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
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
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
