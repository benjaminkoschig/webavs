package ch.globaz.al.businessimpl.processus;

/**
 * Classe contenant les différents critères nécessaire à la sélection des données pour les processus / traitements
 * 
 * @author GMO
 * 
 */
public class ProcessusDatasCriteria {

    /**
     * Critère de bonification (direct/indirect)
     */
    public String bonificationCriteria = null;
    /**
     * Critère de cotisation (paritaire / personnel)
     */
    public String cotisationCriteria = null;
    /**
     * Critère de sélection par date précise
     */
    public String fullDateCriteria = null;
    /**
     * Critère de période
     */
    public String periodeCriteria = null;

}
