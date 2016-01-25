package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.phenix.process.communications.CPProcessXMLSedexWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe repr�sente le r�sultat de l'envoi d'un lot de communication fiscales
 * 
 * @see CPProcessXMLSedexWriter
 * @author lga
 * 
 */
public class SedexResult {

    /**
     * Repr�sente les infos qui doivent �tre remont�es depuis le traitement du lot
     */
    private List<String> information = new ArrayList<String>();
    /**
     * Repr�sente les warnings qui doivent �tre remont�es depuis le traitement du lot
     */
    private List<String> warning = new ArrayList<String>();
    /**
     * Repr�sente les erreurs qui doivent �tre remont�es depuis le traitement du lot
     */
    private List<String> error = new ArrayList<String>();
    /**
     * Si la transaction qui g�re le lot doit �tre rollback�e
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
