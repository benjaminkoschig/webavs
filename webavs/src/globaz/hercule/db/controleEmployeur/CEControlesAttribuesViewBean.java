package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BManager;
import globaz.hercule.db.reviseur.CEReviseurManager;
import globaz.hercule.process.CEListeControlesAttribuesProcess;
import globaz.jade.log.JadeLogger;

/**
 * Ce ViewBean va lancer un process pour imprimer les contrôles attribués (contrôle employeurs)
 * 
 * @author: hpe
 */

public class CEControlesAttribuesViewBean extends CEListeControlesAttribuesProcess implements FWViewBeanInterface,
        BIPersistentObject {

    private static final long serialVersionUID = -2811349971865578056L;
    private String annee = "";
    private String genreControle = "";
    private String selectionGroupe = "";
    private String typeAdresse = "";
    private String visaReviseur = "";

    public CEControlesAttribuesViewBean() throws java.lang.Exception {
    }

    public CEReviseurManager _getReviseursList() {

        CEReviseurManager reviseurMan = new CEReviseurManager();
        reviseurMan.setSession(getSession());

        try {

            reviseurMan.find(getTransaction(), BManager.SIZE_NOLIMIT);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return reviseurMan;

    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    @Override
    public String getAnnee() {
        return annee;
    }

    @Override
    public String getGenreControle() {
        return genreControle;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSelectionGroupe() {
        return selectionGroupe;
    }

    @Override
    public String getTypeAdresse() {
        return typeAdresse;
    }

    @Override
    public String getVisaReviseur() {
        return visaReviseur;
    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void setAnnee(String string) {
        annee = string;
    }

    @Override
    public void setGenreControle(String string) {
        genreControle = string;
    }

    @Override
    public void setId(String newId) {

    }

    @Override
    public void setSelectionGroupe(String string) {
        selectionGroupe = string;
    }

    @Override
    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    @Override
    public void setVisaReviseur(String string) {
        visaReviseur = string;
    }

    @Override
    public void update() throws Exception {

    }

}
