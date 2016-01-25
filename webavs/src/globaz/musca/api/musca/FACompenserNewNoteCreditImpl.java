package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.process.FAPassageCompenserNewNoteCreditProcess;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;

public class FACompenserNewNoteCreditImpl implements IntModuleFacturation {

    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public FACompenserNewNoteCreditImpl() {
        super();
    }

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Avant regénérer, il faut effacer les afacts
     * 
     * @return true s'il faut effacer les afacts avant de regénérer, false sinon
     */
    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        FAPassageCompenserNewNoteCreditProcess cptProc = new FAPassageCompenserNewNoteCreditProcess();
        // copier le process parent
        cptProc.setParentWithCopy(context);
        //
        cptProc.setIdModuleFacturation(idModuleFacturation);
        cptProc.setIdPassage(passage.getIdPassage());
        cptProc.setEMailAddress(context.getEMailAddress());
        return cptProc._executeCompenserProcess(passage);
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
        CASection section = new CASection();
        CASectionManager sectionMana = new CASectionManager();
        sectionMana.setSession(context.getSession());
        sectionMana.setForModeCompensationEquals(APISection.MODE_REPORT);
        sectionMana.setForIdPassageComp(passage.getIdPassage());
        sectionMana.find();
        if (sectionMana.size() > 0) {
            for (int i = 0; i < sectionMana.size(); i++) {
                section = (CASection) sectionMana.getEntity(i);
                section.setIdPassageComp("");
                section.update();
            }
        }
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
