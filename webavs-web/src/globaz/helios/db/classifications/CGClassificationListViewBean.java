package globaz.helios.db.classifications;

// exemple de code system pour une liste
//
// public String getType(int pos) {
// TIAvoirAdresse entity = (TIAvoirAdresse) getEntity(pos);
//
// String type = entity.getCsTypeAdresse().getCurrentCodeUtilisateur().getLibelle();
// return type;
//
// }

public class CGClassificationListViewBean extends CGClassificationManager implements
        globaz.framework.bean.FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGClassificationListViewBean.
     */
    public CGClassificationListViewBean() {
        super();
    }

    public String getIdClassification(int pos) {
        CGClassification entity = (CGClassification) getEntity(pos);
        return entity.getIdClassification();

    }

    public String getLibelle(int pos) {
        CGClassification entity = (CGClassification) getEntity(pos);
        return entity.getLibelle();

    }
}
