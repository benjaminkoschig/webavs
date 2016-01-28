package ch.globaz.corvus.process.echeances.analyseur;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleAnalyseEcheance;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;

/**
 * Analyseur d'�ch�ance standard.<br/>
 * Lance tous les modules contenu dans {@link #getModulesAnalyse()} et retourne toutes les r�ponses positives de ces
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
     * Parcours les modules d'analyse (accessible via {@link #getModulesAnalyse()}) et traite leurs retours de mani�re �
     * construire une liste de r�ponse pour le processus d'impression des �ch�ances
     * 
     * @param echeance
     *            une �ch�ance de rente
     * @return La liste des r�ponses (post-trait�e ou non) des modules d'analyse pour cette �ch�ance
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
     * Retourne la liste des modules � utiliser pour analyser une �ch�ance
     * 
     * @return la liste des modules � utiliser pour analyser une �ch�ance
     */
    public List<REModuleAnalyseEcheance> getModulesAnalyse() {
        return modulesAnalyse;
    }

    /**
     * Permet de d�finir la liste des modules d'analyse.<br/>
     * La visibilit� de la m�thode est volontairement r�duite pour ne permettre qu'aux enfants et � l'usine (
     * {@link REAnalyseurEcheancesFactory}) de pouvoir s'en servir.
     * 
     * @param modulesAnalyse
     *            la liste des modules pour cette analyseur
     */
    protected void setModulesAnalyse(List<REModuleAnalyseEcheance> modulesAnalyse) {
        this.modulesAnalyse = modulesAnalyse;
    }
}
