package globaz.helios.itext.list.pertesprofits;

import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRCloneableDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class CGPertesProfitsDS implements JRCloneableDataSource {

    private CGCompteSoldeBean bean = null;
    private Iterator<CGCompteSoldeBean> it = null;
    private List<CGCompteSoldeBean> listBeanDocument;

    /**
     * @param listBeanDocument
     */
    public CGPertesProfitsDS(List<CGCompteSoldeBean> listBeanDocument) {
        this.listBeanDocument = listBeanDocument;
        it = listBeanDocument.iterator();
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
            return bean.computeSoldeCharge();
        }

        // Récupération des passifs
        if (jrField.getName().equals("COL_5")) {
            return bean.computeSoldeProduit();
        }

        if (jrField.getName().equals("COL_4_TOT")) {
            if (bean.isTotal()) {
                return bean.getTotalCharges();
            }
        }
        if (jrField.getName().equals("COL_5_TOT")) {
            if (bean.isTotal()) {
                return bean.getTotalProduits();
            }
        }

        if (jrField.getName().equals("RESULTAT")) {
            if (bean.isTotal()) {
                return bean.getResultat();
            }
        }

        // Récupération des actifs pour les comptes en monnaie étrangère.
        if (jrField.getName().equals("COL_4_ME")) {
        }
        // Récupération des passifs
        if (jrField.getName().equals("COL_5_ME")) {
        }

        return null;
    }

    /**
     * @return the listBeanDocument
     */
    public List<CGCompteSoldeBean> getListBeanDocument() {
        return listBeanDocument;
    }

    @Override
    public boolean next() throws JRException {
        if ((listBeanDocument == null) || (it == null)) {
            return false;
        }

        if (it.hasNext()) {
            bean = it.next();
            return true;
        }

        return false;
    }

}
