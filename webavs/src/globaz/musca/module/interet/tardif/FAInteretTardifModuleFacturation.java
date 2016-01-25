package globaz.musca.module.interet.tardif;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.module.interet.FAInteretModuleFacturation;
import globaz.musca.process.interet.tardif.FAInteretTardifProcess;

/**
 * Module de facturation des intérêts moratoires tardifs.
 * 
 * @author DDA
 */
public class FAInteretTardifModuleFacturation extends FAInteretModuleFacturation {
    private FAInteretTardifProcess procFacturation = null;

    public FAInteretTardifModuleFacturation() {
        super();
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return true;
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        if (procFacturation == null) {
            procFacturation = new FAInteretTardifProcess();
            procFacturation.setIdModuleFacturation(idModuleFacturation);
            procFacturation.setParentWithCopy(context);
            procFacturation.setPassage((FAPassage) passage);
            procFacturation.setEMailAddress(context.getEMailAddress());
        }

        procFacturation.executeProcess();

        return (!context.isAborted() && ((context.getMemoryLog() == null) || !context.getMemoryLog().hasErrors()));
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

}
