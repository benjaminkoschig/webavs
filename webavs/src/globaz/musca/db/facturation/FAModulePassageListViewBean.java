package globaz.musca.db.facturation;

public class FAModulePassageListViewBean extends FAModulePassageManager implements
        globaz.framework.bean.FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Boolean getEstGenere(int pos) {
        return ((FAModulePassage) getEntity(pos)).isEstGenere();
    }

    public Boolean getGenere(int pos) {
        return ((FAModulePassage) getEntity(pos)).isEstGenere();
    }

    public String getIdAction(int pos) {
        return ((FAModulePassage) getEntity(pos)).getIdAction();
    }

    public String getIdModuleFacturation(int pos) {
        return ((FAModulePassage) getEntity(pos)).getIdModuleFacturation();
    }

    public String getIdPassage(int pos) {
        return ((FAModulePassage) getEntity(pos)).getIdPassage();
    }

    public String getLibelleAction(int pos) {
        return ((FAModulePassage) getEntity(pos)).getLibelleAction();
    }

    /*
     * Retourne le libellé du module de facturation dans la langue de l'utilisateur
     */
    public String getLibelleModule(int pos) throws Exception {
        return ((FAModulePassage) getEntity(pos)).getLibelle();
    }

    public String getNiveauAppel(int pos) {
        return ((FAModulePassage) getEntity(pos)).getNiveauAppel();
    }
}
