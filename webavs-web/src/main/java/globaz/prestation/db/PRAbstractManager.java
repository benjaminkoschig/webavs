/*
 * Créé le 21 sept. 05
 */
package globaz.prestation.db;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manager abstrait qui devrait être étendu par tous les managers afin d'avoir un orderBy par défaut.
 * 
 * @author DVH
 */
public abstract class PRAbstractManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String orderBy = "";

    public PRAbstractManager() {
        super();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isBlank(orderBy)) {
            return getOrderByDefaut();
        } else {
            return orderBy;
        }
    }

    public String getOrderBy() {
        return orderBy;
    }

    /**
     * A implémenter pour qu'elle retourne le nom de la colonne à utiliser pour l'orderBy par défaut
     * 
     * @return le nom de la colonne à utiliser par défaut pour l'orderBy
     */
    public abstract String getOrderByDefaut();

    public void setOrderBy(String string) {
        orderBy = string;
    }

    public <T> List<T> getContainerAsList() {

        if (getContainer() == null) {
            throw new NullPointerException("Container is null. Try to execute find before.");
        }

        Iterator<T> ite = getContainer().iterator();
        List<T> mgrList = new ArrayList<T>(getContainer().size());

        while (ite.hasNext()) {
            mgrList.add(ite.next());
        }

        return mgrList;
    }
}
