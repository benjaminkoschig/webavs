package globaz.alfagest.facturation;

import globaz.alfagest.process.ALFacturationProcess;
import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;

/**
 * Cr�� le 18 nov. 2008
 * 
 * @author gmo
 * 
 *         Module de facturation ALFAGEST pour cot.pers., appel� par MUSCA
 */
public class ALFacturationPersModule extends ALFacturationModule {

    /**
     * M�thode principale appel�e pour la g�n�ration. (Devrait changer... les m�thodes reg�n�rer et reprises devraient
     * �tre appel�es)
     * Cette m�thode est toujours appel�e (lors le la premi�re g�n�ration, lors de la reg�n�ration, lors de la reprise
     * sur erreur)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // process de facturation
        ALFacturationProcess myProcess = new ALFacturationProcess();
        // param�trage du process
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