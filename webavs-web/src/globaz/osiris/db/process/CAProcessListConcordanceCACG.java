/**
 *
 */
package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.osiris.db.comptecourant.CAConcordanceCACGManager;
import globaz.osiris.print.list.CAListConcordanceCACGExcel;

/**
 * @author SEL
 * 
 */
public class CAProcessListConcordanceCACG extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateFin = "";
    private String forIdExterne = "";

    private Boolean printOnlyDiff = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        CAConcordanceCACGManager manager = new CAConcordanceCACGManager();
        manager.setSession(getSession());
        manager.setForIdExterne(getForIdExterne());
        manager.setDateDebut((new JADate("01." + getDateDebut()).toStrAMJ()));
        manager.setDateFin((new JADate("31." + getDateFin()).toStrAMJ()));
        if (isPrintOnlyDiff() != null) {
            manager.setPrintOnlyDiff(isPrintOnlyDiff());
        }

        manager.find(getTransaction());

        if (isAborted()) {
            return false;
        }

        setProgressScaleValue(manager.size());
        CAListConcordanceCACGExcel listeExcel = new CAListConcordanceCACGExcel(getSession());
        listeExcel.setProcess(this);
        listeExcel.setDocumentInfo(createDocumentInfo());
        listeExcel.setDateDebut(getDateDebut());
        listeExcel.setDateFin(getDateFin());
        listeExcel.setPrintOnlyDiff(getPrintOnlyDiff());
        listeExcel.setForIdExterne(getForIdExterne());
        listeExcel.populateSheetListe(manager, getTransaction());

        if (!isAborted()) {
            this.registerAttachedDocument(listeExcel.getDocumentInfo(), listeExcel.getOutputFile());
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("COMPTECOURANT_LISTE_CONCORDANCE_CACG");
    }

    /**
     * @return the forIdExterne
     */
    public String getForIdExterne() {
        return forIdExterne;
    }

    public Boolean getPrintOnlyDiff() {
        return printOnlyDiff;
    }

    /**
     * @return the printOnlyDiff
     */
    public Boolean isPrintOnlyDiff() {
        return printOnlyDiff;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param forIdExterne
     *            the forIdExterne to set
     */
    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    /**
     * @param printOnlyDiff
     *            the printOnlyDiff to set
     */
    public void setPrintOnlyDiff(Boolean printOnlyDiff) {
        this.printOnlyDiff = printOnlyDiff;
    }

}
