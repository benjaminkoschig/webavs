package globaz.musca.db.facturation;

public class FAAfactListViewBean extends FAAfactManager implements globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    public java.lang.String getAction() {
        return action;
    }

    public String getAnneeFacture(int pos) {
        return ((FAAfact) getEntity(pos)).getAnneeCotisation();
    }

    public String getIdAfact(int pos) {
        return ((FAAfact) getEntity(pos)).getIdAfact();
    }

    /*
     * Retourne le détail de la rubrique (Cf OSIRIS)
     */
    public String getIdExterneRubrique(int pos) {
        return ((FAAfact) getEntity(pos)).getIdExterneRubrique();
    }

    public String getLibelleSurFacture(int pos) {
        return ((FAAfact) getEntity(pos)).getLibelleSurFacture(getSession().getIdLangueISO());
    }

    public String getMasseFacture(int pos) {
        return ((FAAfact) getEntity(pos)).getMasseFacture();

    }

    public String getMontantFacture(int pos) {
        return ((FAAfact) getEntity(pos)).getMontantFacture();

    }

    public String getTauxFacture(int pos) {
        return ((FAAfact) getEntity(pos)).getTauxFacture();

    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
