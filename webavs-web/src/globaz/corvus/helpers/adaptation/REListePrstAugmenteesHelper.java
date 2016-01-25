package globaz.corvus.helpers.adaptation;

import globaz.corvus.process.REListePrstAugmenteesProcess;
import globaz.corvus.vb.adaptation.REListePrstAugmenteesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REListePrstAugmenteesHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REListePrstAugmenteesViewBean vb = (REListePrstAugmenteesViewBean) viewBean;

        REListePrstAugmenteesProcess process = new REListePrstAugmenteesProcess();
        process.setSession(vb.getSession());
        process.setEMailAddress(vb.getEMailAddress());
        process.setMoisAnnee(vb.getMoisAnnee());
        process.setIsLstPrestAugManuellement(vb.isLstPrestAugManuellement());
        process.setIsLstPrestProgrammeCentrale(vb.isLstPrestProgrammeCentrale());
        process.setIsLstPrestTraitementAutomatique(vb.isLstPrestTraitementAutomatique());
        process.setIsLstRecapAdaptation(vb.isLstRecapAdaptation());
        process.setIsLstPrestNonAdapte(vb.isLstPrestNonAdapte());
        process.start();

    }
}