package globaz.musca.module.interet.generic;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.module.interet.FAInteretModuleFacturation;
import globaz.musca.process.interet.cotipercuentrop.FACotiPercuEnTropProcess;
import globaz.musca.process.interet.generic.FAInteretAffiliationRetroactiveProcess;
import globaz.musca.process.interet.generic.FAInteretArriereProcess;
import globaz.musca.process.interet.generic.FAInteretDecompteFinalProcess;
import globaz.musca.process.interet.generic.FAInteretDifference25PourCentProcess;
import globaz.musca.process.interet.generic.FAInteretGenericProcess;
import globaz.musca.process.interet.generic.FAInteretRenumeratoireProcess;
import globaz.musca.process.interet.util.FAInteretGenericUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;

/**
 * Module de facturation des intérêts moratoires :<br/>
 * - Coti. arrierree.<br/>
 * - Affiliation rétroactive.<br/>
 * - Decompte final.<br/>
 * - Cot. pers. 25%.<br/>
 * - Renumeratoire.<br/>
 * - Renumeratoire pour cotisations percu en trop.<br/>
 * 
 * @author DDA
 */
public class FAInteretGenericModuleFacturation extends FAInteretModuleFacturation {

    public FAInteretGenericModuleFacturation() {
        super();
    }

    /**
     * La comptabilisation ne doit pas être effectuée si des intérêts moratoires sont encore à contrôler.
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
        manager.setSession(context.getSession());
        manager.setForIdJournalFacturation(passage.getIdPassage());
        manager.setForMotifCalcul(CAInteretMoratoire.CS_A_CONTROLER);

        if ((manager.getCount(context.getTransaction()) > 0) && !passage.getIsAuto()) {
            context.getMemoryLog().logMessage(context.getSession().getLabel("IM_RESTANT_A_CONTROLER"),
                    FWMessage.ERREUR, this.getClass().getName());
            return false;
        } else {
            return true;
        }
    }

    private void executeProcessInteretGeneric(BProcess context, IFAPassage passage,
            FAInteretGenericProcess procFacturation) throws Exception {
        procFacturation.setParentWithCopy(context);
        procFacturation.setPassage((FAPassage) passage);
        procFacturation.setEMailAddress(context.getEMailAddress());

        procFacturation.executeProcess();
    }

    private void executeProcessInteretPourCotisationPercuEnTrop(BProcess context, IFAPassage passage) throws Exception {
        FACotiPercuEnTropProcess procFacturation = new FACotiPercuEnTropProcess();

        procFacturation.setParentWithCopy(context);
        procFacturation.setPassage((FAPassage) passage);
        procFacturation.setEMailAddress(context.getEMailAddress());

        procFacturation.executeProcess();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        FAInteretGenericUtil.effacerInteretsPrecedents(context.getSession(), context.getTransaction(), passage);

        executeProcessInteretGeneric(context, passage, new FAInteretDecompteFinalProcess());
        executeProcessInteretGeneric(context, passage, new FAInteretRenumeratoireProcess());
        executeProcessInteretGeneric(context, passage, new FAInteretArriereProcess());
        executeProcessInteretGeneric(context, passage, new FAInteretAffiliationRetroactiveProcess());
        executeProcessInteretGeneric(context, passage, new FAInteretDifference25PourCentProcess());

        if (CAApplication.getApplicationOsiris().getCAParametres().isInteretRemuneratoireActif()) {
            executeProcessInteretPourCotisationPercuEnTrop(context, passage);
        }

        return (!context.isAborted() && ((context.getMemoryLog() == null) || !context.getMemoryLog().hasErrors()));
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

}
