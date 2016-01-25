package globaz.osiris.print.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public abstract class CAIListFactory extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean _hasNext = true;

    /**
     * Constructor for CAIListFactory.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListFactory(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Constructor for CAIListFactory.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListFactory(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    void _getRefParam() {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");
            StringBuffer refBuffer = new StringBuffer(getSession().getLabel("REFERENCE") + " ");
            refBuffer.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1));
            refBuffer.append(" (");
            refBuffer.append(formater.format(new Date()));
            refBuffer.append(")");
            refBuffer.append(" - ");
            refBuffer.append(getSession().getUserId());
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, refBuffer.toString());
        } catch (Exception e) {
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
        getDocumentInfo().setTemplateName("");
        this.setParametres(
                FWIImportParametre.PARAM_COMPANYNAME,
                FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                        ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    /**
     * Returns the hasNext.
     * 
     * @return boolean
     */
    public boolean isHasNext() {
        return _hasNext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#next()
     */
    @Override
    public boolean next() throws FWIException {
        try {
            return _hasNext;
        } finally {
            _hasNext = false;// setHasNext(false);
        }
    }

    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
    }

    // ALD, commentaire car le setBeanProperties appelait cette méthode
    // avec hasNext=false puisque hasNext n'existe n'est pas dans request
    // /**
    // * Sets the hasNext.
    // * @param hasNext The hasNext to set
    // */
    // public void setHasNext(boolean hasNext) {
    // this._hasNext = hasNext;
    // }

}
