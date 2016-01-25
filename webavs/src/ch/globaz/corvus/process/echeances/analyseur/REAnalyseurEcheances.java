package ch.globaz.corvus.process.echeances.analyseur;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleAnalyseEcheance;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;

/**
 * Analyseur d'échéance standard.<br/>
 * Lance tous les modules contenu dans {@link #getModulesAnalyse()} et retourne toutes les réponses positives de ces
 * modules.
 * 
 * @author PBA
 * @see REModuleAnalyseEcheance
 * @see REAnalyseurEcheancesFactory
 */
public class REAnalyseurEcheances {

    private List<REModuleAnalyseEcheance> modulesAnalyse;

    public REAnalyseurEcheances() {
        super();

        modulesAnalyse = new ArrayList<REModuleAnalyseEcheance>();
    }

    /**
     * Parcours les modules d'analyse (accessible via {@link #getModulesAnalyse()}) et traite leurs retours de manière à
     * construire une liste de réponse pour le processus d'impression des échéances
     * 
     * @param echeance
     *            une échéance de rente
     * @return La liste des réponses (post-traitée ou non) des modules d'analyse pour cette échéance
     * @throws Exception
     *             dans le cas d'erreur dans un module d'analyse
     */
    public List<REReponseModuleAnalyseEcheance> analyserEcheance(IREEcheances echeance) throws Exception {
        List<REReponseModuleAnalyseEcheance> reponses = new ArrayList<REReponseModuleAnalyseEcheance>();

        for (REModuleAnalyseEcheance unModuleAnalyse : getModulesAnalyse()) {
            REReponseModuleAnalyseEcheance reponseDuModule = unModuleAnalyse.eval(echeance);
            if (reponseDuModule.isListerEcheance()) {
                reponses.add(reponseDuModule);
            }
        }

        return reponses;
    }

    /**
     * Retourne la liste des modules à utiliser pour analyser une échéance
     * 
     * @return la liste des modules à utiliser pour analyser une échéance
     */
    public List<REModuleAnalyseEcheance> getModulesAnalyse() {
        return modulesAnalyse;
    }

    /**
     * Permet de définir la liste des modules d'analyse.<br/>
     * La visibilité de la méthode est volontairement réduite pour ne permettre qu'aux enfants et à l'usine (
     * {@link REAnalyseurEcheancesFactory}) de pouvoir s'en servir.
     * 
     * @param modulesAnalyse
     *            la liste des modules pour cette analyseur
     */
    protected void setModulesAnalyse(List<REModuleAnalyseEcheance> modulesAnalyse) {
        this.modulesAnalyse = modulesAnalyse;
    }
}
