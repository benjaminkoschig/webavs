package ch.globaz.corvus.process.echeances.analyseur;

import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleAnalyseEcheance;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;

/**
 * Analyseur d'�ch�ance prenant en charge le module d'�ch�ance forc�e.<br/>
 * Le fonctionnement diff�re de celui de {@link REAnalyseurEcheances} par le fait qu'il faut tester tous les
 * modules, m�me ceux qui n'ont pas �t� choisi par l'utilisateur, avant de lancer celui d'�ch�ance forc�e. Dans le cas
 * contraire, on peut se retrouver avec une �ch�ance sortant comme "�ch�ance forc�e" dans un cas, et si l'utilisateur
 * active un module dans lequel l'�ch�ance est reconnue, alors elle sortira avec le bon motif.<br/>
 * <br/>
 * Pour �viter ce cas de figure, cet analyseur a besoin de recevoir tous les modules qui n'ont pas �t� choisi par
 * l'utilisateur. Il lancera l'analyse de ces modules si ceux choisi par l'utilisateur n'ont rien trouv� sur l'�ch�ance
 * en cours. Et si, et seulement si, aucun des modules non choisi par l'utilisateur ne "match", l'analyse du module des
 * �ch�ances forc�es sera lanc�e.
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
                    // si un des modules que l'utilisateur n'a pas s�lectionn� reconna�t cette �ch�ance comme valide, ce
                    // n'est pas une �ch�ance forc�e mais une "autre �ch�ance", on ignore donc cette �ch�ance dans la
                    // liste � imprimer
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
