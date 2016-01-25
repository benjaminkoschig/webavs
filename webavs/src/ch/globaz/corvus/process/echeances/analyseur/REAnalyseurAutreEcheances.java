package ch.globaz.corvus.process.echeances.analyseur;

import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleAnalyseEcheance;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;

/**
 * Analyseur d'échéance prenant en charge le module d'échéance forcée.<br/>
 * Le fonctionnement diffère de celui de {@link REAnalyseurEcheances} par le fait qu'il faut tester tous les
 * modules, même ceux qui n'ont pas été choisi par l'utilisateur, avant de lancer celui d'échéance forcée. Dans le cas
 * contraire, on peut se retrouver avec une échéance sortant comme "échéance forcée" dans un cas, et si l'utilisateur
 * active un module dans lequel l'échéance est reconnue, alors elle sortira avec le bon motif.<br/>
 * <br/>
 * Pour éviter ce cas de figure, cet analyseur a besoin de recevoir tous les modules qui n'ont pas été choisi par
 * l'utilisateur. Il lancera l'analyse de ces modules si ceux choisi par l'utilisateur n'ont rien trouvé sur l'échéance
 * en cours. Et si, et seulement si, aucun des modules non choisi par l'utilisateur ne "match", l'analyse du module des
 * échéances forcées sera lancée.
 * 
 * @author PBA
 */
public class REAnalyseurAutreEcheances extends REAnalyseurEcheances {

    private REModuleAnalyseEcheance moduleEchancesForcees;
    private List<REModuleAnalyseEcheance> tousLesModulesNonSelectionnesSaufAutreEcheance;

    public REAnalyseurAutreEcheances(REModuleAnalyseEcheance moduleEcheancesForcees,
            List<REModuleAnalyseEcheance> tousLesModulesNonSelectionnesSaufAutreEcheance) {
        super();
        moduleEchancesForcees = moduleEcheancesForcees;
        this.tousLesModulesNonSelectionnesSaufAutreEcheance = tousLesModulesNonSelectionnesSaufAutreEcheance;
    }

    @Override
    public List<REReponseModuleAnalyseEcheance> analyserEcheance(IREEcheances echeance) throws Exception {
        List<REReponseModuleAnalyseEcheance> reponsesPourLesModulesStandards = super.analyserEcheance(echeance);
        if (reponsesPourLesModulesStandards.size() == 0) {
            for (REModuleAnalyseEcheance unModuleNonSelectionne : tousLesModulesNonSelectionnesSaufAutreEcheance) {
                if (unModuleNonSelectionne.eval(echeance).isListerEcheance()) {
                    // si un des modules que l'utilisateur n'a pas sélectionné reconnaît cette échéance comme valide, ce
                    // n'est pas une échéance forcée mais une "autre échéance", on ignore donc cette échéance dans la
                    // liste à imprimer
                    return reponsesPourLesModulesStandards;
                }
            }
            REReponseModuleAnalyseEcheance reponseEcheanceForcee = moduleEchancesForcees.eval(echeance);
            if (reponseEcheanceForcee.isListerEcheance()) {
                reponsesPourLesModulesStandards.add(reponseEcheanceForcee);
            }
        }
        return reponsesPourLesModulesStandards;
    }
}
