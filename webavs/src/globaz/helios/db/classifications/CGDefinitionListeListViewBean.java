package globaz.helios.db.classifications;

public class CGDefinitionListeListViewBean extends CGDefinitionListeManager implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getIdDefinitionListe(int pos) {
        CGDefinitionListe entity = (CGDefinitionListe) getEntity(pos);
        return entity.getIdDefinitionListe();

    }

    public String getLibelle(int pos) {
        CGDefinitionListe entity = (CGDefinitionListe) getEntity(pos);
        return entity.getLibelle();

    }
}
