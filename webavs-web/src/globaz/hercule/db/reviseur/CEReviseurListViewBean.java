package globaz.hercule.db.reviseur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

public class CEReviseurListViewBean extends CEReviseurManager implements FWViewBeanInterface {

    private static final long serialVersionUID = 6295249656019834667L;

    public String getIdReviseur(final int index) {
        return ((CEReviseur) getEntity(index)).getIdReviseur();
    }

    public String getNomReviseur(final int index) {
        return ((CEReviseur) getEntity(index)).getNomReviseur();
    }

    public String getTypeReviseur(final int index) {

        CEReviseur rev = (CEReviseur) getEntity(index);

        if (!JadeStringUtil.isBlank(rev.getTypeReviseur())) {
            return getSession().getCodeLibelle(rev.getTypeReviseur());
        }

        return "";
    }

    public String getVisa(final int index) {
        return ((CEReviseur) getEntity(index)).getVisa();
    }

    public Boolean isReviseurActif(final int index) {
        return ((CEReviseur) getEntity(index)).isReviseurActif();
    }

}
