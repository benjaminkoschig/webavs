package globaz.osiris.print.itext.list;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.util.JAUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRCloneableDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

public class CAIListSoldeCompteAnnexe_DS extends CACompteAnnexeManager implements JRCloneableDataSource,
        JRRewindableDataSource {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Iterator container = null;
    private CACompteAnnexe entity = null;
    private BProcess m_process = null;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#getContext()
     */
    public BProcess getContext() {
        return m_process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(JRField)
     */
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        if (jrField.getName().equals("COL_1")) {
            return entity.getCARole().getDescription() + " " + entity.getIdExterneRole() + " "
                    + entity.getDescription();
        } else if (jrField.getName().equals("COL_2")) {
            return new Double(entity.getSolde());
        } else if (jrField.getName().equals("COL_3")) {
            if ((entity.getRole() != null) && (!JAUtil.isDateEmpty(entity.getRole().getDateFin()))) {
                return entity.getRole().getDateFin();
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRRewindableDataSource#moveFirst()
     */
    @Override
    public void moveFirst() throws JRException {
        container = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() throws JRException {
        entity = null;
        try {
            if (container == null) {
                this.find(BManager.SIZE_NOLIMIT);
                container = getContainer().iterator();
            }
            if (container.hasNext()) {
                entity = (CACompteAnnexe) container.next();
            }
        } catch (Exception e) {
        }
        if (getContext() != null) {
            if (getContext().isAborted()) {
                return false;
            }
            getContext().incProgressCounter();
        }
        return (entity != null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#setContext(BProcess)
     */
    public void setContext(BProcess process) {
        m_process = process;
    }

}
