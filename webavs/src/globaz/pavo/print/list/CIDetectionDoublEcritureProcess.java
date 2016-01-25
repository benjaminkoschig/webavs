package globaz.pavo.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.inscriptions.CIDetectionDoubleEcriture;
import globaz.pavo.db.inscriptions.CIDetectionDoubleEctritureManager;

public class CIDetectionDoublEcritureProcess extends CIDetectionDoublesEntetes {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CIDetectionDoublEcritureProcess() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO), "Ci", "GLOBAZ", "Double écritures",
                new CIDetectionDoubleEctritureManager(), "PAVO");
    }

    public CIDetectionDoublEcritureProcess(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "Ci", "GLOBAZ", "Double écritures", new CIDetectionDoubleEctritureManager(), "PAVO");
    }

    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber("0179CCI");
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("MSG_DOUBLE_ECRITURES_TITRE"));
        CIDetectionDoubleEctritureManager manager = (CIDetectionDoubleEctritureManager) _getManager();
        manager.setSession(getSession());
        manager.setForAnnee(getForAnnee());

    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        CIDetectionDoubleEcriture doubleEcriture = (CIDetectionDoubleEcriture) entity;
        _addCell(NSUtil.formatAVSUnknown(doubleEcriture.getNss()));
        _addCell(doubleEcriture.getNomPrenom());
        _addCell(doubleEcriture.getDateNaissance());
        _addCell(getSession().getCodeLibelle(doubleEcriture.getSexe()));
        _addCell(getForAnnee());

    }

    @Override
    protected void initializeTable() {
        _addColumnLeft(getSession().getLabel("LISTE_NSS"));
        _addColumnLeft(getSession().getLabel("LISTE_NOMPRENOM"));
        _addColumnLeft(getSession().getLabel("LISTE_DATENAISSANCE"));
        _addColumnLeft(getSession().getLabel("LISTE_SEXE"));
        _addColumnLeft(getSession().getLabel("DEC_ANNEE"));

    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_LONG;
    }

}
