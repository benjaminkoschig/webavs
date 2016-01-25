package ch.globaz.al.facturation;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.facturation.process.ALFacturationProcess;

public class ALFacturationPersModule extends ALFacturationModule {
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // process de facturation
        ALFacturationProcess factuAF = new ALFacturationProcess();

        // paramétrisation du process
        factuAF.setTypeCoti(ALConstPrestations.TYPE_COT_PERS);
        factuAF.setIdTypeFacturation(passage.getIdTypeFacturation());
        factuAF.setIdModuleFacturation(idModuleFacturation);
        factuAF.setParentWithCopy(context);
        factuAF.setNumeroPassage(passage.getIdPassage());
        factuAF.setDateComptable(passage.getDateFacturation());
        factuAF.setPeriodeComptable(passage.getDatePeriode());
        factuAF.setSession(context.getSession());
        factuAF.setTransaction(context.getTransaction());

        // lancement du process
        return factuAF._executeProcess();
    }
}
