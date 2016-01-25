package globaz.phenix.db.communications;

public class CPAvancementJournauxCFListViewBean extends CPAvancementJournauxCFManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    public java.lang.String getAction() {
        return action;
    }

    public String getNbrAbandonne(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrAbandonne();
    }

    public String getNbrAControler(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrAControler();
    }

    public String getNbrAvertissement(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrAvertissement();
    }

    public String getNbrComptabilise(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrComptabilise();
    }

    public String getNbrEnquete(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrEnEnquete();
    }

    public String getNbrErreur(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrErreur();
    }

    public String getNbrReceptionne(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrReceptionne();
    }

    public String getNbrSansAnomalie(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrSansAnomalie();
    }

    public String getNbrTotal(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrTotal();
    }

    public String getNbrValide(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNbrValide();
    }

    public String getNumJournal(int pos) {
        return ((CPAvancementJournauxCF) getEntity(pos)).getNumJouranl();
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
