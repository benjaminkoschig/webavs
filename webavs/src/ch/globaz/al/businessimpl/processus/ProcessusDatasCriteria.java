package ch.globaz.al.businessimpl.processus;

/**
 * Classe contenant les diff�rents crit�res n�cessaire � la s�lection des donn�es pour les processus / traitements
 * 
 * @author GMO
 * 
 */
public class ProcessusDatasCriteria {

    /**
     * Crit�re de bonification (direct/indirect)
     */
    public String bonificationCriteria = null;
    /**
     * Crit�re de cotisation (paritaire / personnel)
     */
    public String cotisationCriteria = null;
    /**
     * Crit�re de s�lection par date pr�cise
     */
    public String fullDateCriteria = null;
    /**
     * Crit�re de p�riode
     */
    public String periodeCriteria = null;

}
