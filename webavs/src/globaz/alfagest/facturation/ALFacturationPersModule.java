package globaz.alfagest.facturation;

import globaz.alfagest.process.ALFacturationProcess;
import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;

/**
 * Créé le 18 nov. 2008
 * 
 * @author gmo
 * 
 *         Module de facturation ALFAGEST pour cot.pers., appelé par MUSCA
 */
public class ALFacturationPersModule extends ALFacturationModule {

    /**
     * Méthode principale appelée pour la génération. (Devrait changer... les méthodes regénérer et reprises devraient
     * être appelées)
     * Cette méthode est toujours appelée (lors le la première génération, lors de la regénération, lors de la reprise
     * sur erreur)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // process de facturation
        ALFacturationProcess myProcess = new ALFacturationProcess();
        // paramétrage du process
        myProcess.setTypeCotisation(ALFacturationModule.PERSO_COTISATIONS);
        myProcess.setTypeModule(FAModuleFacturation.CS_MODULE_PRESTATIONSAF_PERS);
        myProcess.setParentWithCopy(context);
        myProcess.setNumeroPassage(passage.getIdPassage());
        myProcess.setDateComptable(passage.getDateFacturation());
        myProcess.setSession(context.getSession());
        myProcess.setTransaction(context.getTransaction());

        // lancement du process
        return myProcess._executeProcessFacturation();
    }

}