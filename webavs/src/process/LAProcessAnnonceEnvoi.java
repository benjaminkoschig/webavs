package process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

/**
 * Insérez la description du type ici. Date de création : (18.11.2004 14:00:00)
 * 
 * @author: acr
 */
public class LAProcessAnnonceEnvoi extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean creationFichier = new Boolean(true);
    private String dateAnnonce = "";
    private Boolean impression = new Boolean(true);

    public LAProcessAnnonceEnvoi() {
        super();
    }

    public LAProcessAnnonceEnvoi(BProcess parent) {
        super(parent);
    }

    public LAProcessAnnonceEnvoi(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Traitement de facturation pour les décisions qui ont été prises pour l'année suivante et qui sont déjà à l'état
     * comptabilisé mais qui n'ont pas encore été facturées. Ce traitement utilise un paramêtre "année limite" qui doit
     * être modifié avant son exécution. Date de création : (19.11.2004 08:00:00)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        return true;
    }

    public Boolean getCreationFichier() {
        return creationFichier;
    }

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    @Override
    protected java.lang.String getEMailObject() {
        return "";
    }

    public Boolean getImpression() {
        return impression;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setCreationFichier(Boolean creationFichier) {
        this.creationFichier = creationFichier;
    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public void setImpression(Boolean impression) {
        this.impression = impression;
    }
}
