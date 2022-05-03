package ch.globaz.corvus.process.echeances.analyseur.modules;

import ch.globaz.corvus.business.models.echeances.IREEcheances;
import globaz.globall.db.BSession;

public class REModuleEchanceEnfantRecueilliGratuitement extends REModuleAnalyseEcheance{


    public REModuleEchanceEnfantRecueilliGratuitement(BSession session, String moisTraitement) {
        super(session, moisTraitement);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        // TODO implémenter les règles d'échéances


        return REReponseModuleAnalyseEcheance.Faux;
    }
}
