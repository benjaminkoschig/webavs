package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAFacturationExt;
import globaz.musca.db.facturation.FAFacturationExtManager;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch Process de facturation ALFAGEST, appelé par le module de facturation (ALFacturation)
 */
public class FAPassageAjouterDeclProcess extends FAPassageAjouterTableProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nettoyage après erreur ou exécution.
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Exécution du process
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            manager = new FAFacturationExtManager();
            manager.setSession(getSession());
            manager.setForTypeFactu(FAPassageAjouterTableProcess.TYPE_FACTU_DECL);
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            FAEnteteFacture enteteFacture = null;
            if (manager.size() > 0) {
                for (int i = 0; i < manager.getSize(); i++) {
                    if (!isAborted()) {
                        entity = (FAFacturationExt) manager.getEntity(i);
                        enteteFacture = getEnteteFacture(getNumPassage());
                        if (enteteFacture != null) {
                            // on rajoute l'affact dans l'en-tête de facture trouvée
                            super.createLigneFacture(enteteFacture);
                        } else {
                            continue;
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        }

        // tout s'est bien passé
        return true;
    }

    /**
     * Exécute le processus de facturation en appelant la méthode protected
     */
    @Override
    public boolean _executeProcessFacturation() throws Exception {
        return _executeProcess();
    }

    // @Override
    // protected FAEnteteFacture getEnteteFacture(String numAffilie, String numPassage) {
    // FAEnteteFactureManager entete = new FAEnteteFactureManager();
    // try {
    // entete.setSession(this.getSession());
    // entete.setForIdPassage(numPassage);
    // entete.setForIdExterneRole(numAffilie);
    // entete.setLikeIdExterneFacture(this.entity.getAnneeCotisation());
    // entete.find();
    // if (entete.size() > 0) {
    // return (FAEnteteFacture) entete.getFirstEntity();
    // }
    // } catch (Exception e) {
    // this.getMemoryLog().logMessage(
    // "Erreur lors de la recherche du décompte: " + numAffilie + "\n" + e.getMessage(),
    // FWViewBeanInterface.ERROR, this.getClass().getName());
    // }
    // return null;
    // }

}
