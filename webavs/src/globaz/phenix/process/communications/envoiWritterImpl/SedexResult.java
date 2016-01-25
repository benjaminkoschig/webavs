package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.phenix.process.communications.CPProcessXMLSedexWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente le résultat de l'envoi d'un lot de communication fiscales
 * 
 * @see CPProcessXMLSedexWriter
 * @author lga
 * 
 */
public class SedexResult {

    /**
     * Représente les infos qui doivent être remontées depuis le traitement du lot
     */
    private List<String> information = new ArrayList<String>();
    /**
     * Représente les warnings qui doivent être remontées depuis le traitement du lot
     */
    private List<String> warning = new ArrayList<String>();
    /**
     * Représente les erreurs qui doivent être remontées depuis le traitement du lot
     */
    private List<String> error = new ArrayList<String>();
    /**
     * Si la transaction qui gère le lot doit être rollbackée
     */
    boolean doRollback = false;

    public void setDoRollback(boolean doRollback) {
        this.doRollback = doRollback;
    }

    public boolean getDoRollback() {
        return doRollback;
    }

    public void addInformation(String info) {
        information.add(info);
    }

    public void addWarning(String warning) {
        this.warning.add(warning);
    }

    public void addWarning(List<String> warnings) {
        warning.addAll(warnings);
    }

    public void addError(String error) {
        this.error.add(error);
    }

    public void addError(List<String> errors) {
        error.addAll(errors);
    }

    public boolean hasWarnings() {
        return warning.size() > 0;
    }

    public boolean hasErrors() {
        return error.size() > 0;
    }

    public List<String> getInformation() {
        return information;
    }

    public List<String> getWarnings() {
        return warning;
    }

    public List<String> getErrors() {
        return error;
    }

}
