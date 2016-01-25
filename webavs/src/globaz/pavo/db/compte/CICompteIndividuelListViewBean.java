package globaz.pavo.db.compte;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (20.11.2002 15:10:43)
 * 
 * @author: David Girardin
 */
public class CICompteIndividuelListViewBean extends CICompteIndividuelManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        String normalField = "CIINDIP.KAIIND" + ",CIINDIP.KANAVS" + ",CIINDIP.KALNOM" + ",CIINDIP.KAIPAY"
                + ",CIINDIP.KADNAI" + ",CIINDIP.KAIEMP" + ",CIINDIP.KABOUV" + ",CIINDIP.KADCLO" + ",CIINDIP.KAIARC"
                + ",CIINDIP.KAIINR" + ",CIINDIP.KAICAI" + ",CIINDIP.KATSEX" + ",CIINDIP.KAIPAY" + ",CIINDIP.KABNNS";
        // + ",AFAFFIP.MALNAF";
        return normalField;
    }
}
