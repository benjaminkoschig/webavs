package globaz.musca.db.facturation;

public class FAOrdreAttribuerListViewBean extends FAOrdreAttribuerManager implements
        globaz.framework.bean.FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getIdExterneRubrique(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getIdExterneRubrique();
    }

    public String getIdOrdreAttribuer(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getIdOrdreAttribuer();
    }

    public String getIdOrdreRegroupement(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getIdOrdreRegroupement();
    }

    public String getIdRubrique(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getIdRubrique();
    }

    public String getLibelle(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getLibelle();
    }

    public String getNature(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getNature();
    }

    public String getNumCaisse(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getNumCaisse();
    }

    public String getNumOrdreRegroupement(int pos) {
        return ((FAOrdreAttribuer) getEntity(pos)).getNumOrdreRegroupement();
    }
}
