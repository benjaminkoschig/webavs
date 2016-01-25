/**
 * 
 */
package globaz.helios.itext.list.bilan;

import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRCloneableDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author sel
 * 
 *         Data source pour le bilan
 * 
 */
public class CGBilanDS implements JRCloneableDataSource {

    private CGCompteSoldeBean bean = null;
    private Iterator<CGCompteSoldeBean> it = null;
    private List<CGCompteSoldeBean> listBeanDocument;

    /**
     * @param listBeanDocument
     */
    public CGBilanDS(List<CGCompteSoldeBean> listBeanDocument) {
        this.listBeanDocument = listBeanDocument;
        if (listBeanDocument != null) {
            it = listBeanDocument.iterator();
        }
    }

    /**
     * @return Object
     * @exception CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {

        if (jrField.getName().equals("NIVEAU")) {
            return bean.getNiveau();
        }

        if (jrField.getName().equals("COL_1")) {
            return bean.getNoClasseLibelle();
        }

        // Infos de compte
        if (jrField.getName().equals("COL_3")) {
            return bean.getNoCompteLibelle();
        }

        // Récupération des actifs
        if (jrField.getName().equals("COL_4")) {
            return bean.computeSoldeActif();
        }

        // Récupération des passifs
        if (jrField.getName().equals("COL_5")) {
            return bean.computeSoldePassif();
        }

        if (jrField.getName().equals("COL_4_TOT")) {
            if (bean.isTotal()) {
                return bean.getTotalActif();
            }
        }
        if (jrField.getName().equals("COL_5_TOT")) {
            if (bean.isTotal()) {
                return bean.getTotalPassif();
            }
        }

        // Récupération des actifs pour les comptes en monnaie étrangère.
        if (jrField.getName().equals("COL_4_ME") && !JadeStringUtil.isBlankOrZero(bean.getSoldeDebitMonnaie())) {
            return bean.getSoldeDebitMonnaie();
        }
        // Récupération des passifs
        if (jrField.getName().equals("COL_5_ME") && !JadeStringUtil.isBlankOrZero(bean.getSoldeCreditMonnaie())) {
            return bean.getSoldeCreditMonnaie();
        }

        return null;
    }

    /**
     * @return the listBeanDocument
     */
    public List<CGCompteSoldeBean> getListBeanDocument() {
        return listBeanDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() throws JRException {
        if ((it != null) && it.hasNext()) {
            bean = it.next();
            return true;
        }

        return false;
    }
}
