package globaz.pavo.print.list;

import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.comparaison.CIAnomalieCI;
import globaz.pavo.db.comparaison.CIAnomalieCIManager;

public class CIComparaisonAnomaliesImprimer extends FWIAbstractManagerDocumentList {

    private static final long serialVersionUID = -864022396898596301L;

    private String forIdTypeAnomalie = "";

    private String likeNumeroAvs = "";

    public CIComparaisonAnomaliesImprimer() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO), "Ci", "GLOBAZ", "Liste des anomalies",
                new CIAnomalieCIManager(), "PAVO");
    }

    public CIComparaisonAnomaliesImprimer(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "Ci", "GLOBAZ", "Liste des anomalies", new CIAnomalieCIManager(), "LEO");
    }

    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber("0064CCI");
        CIAnomalieCIManager manager = (CIAnomalieCIManager) _getManager();
        manager.setSession(getSession());
        if (!JAUtil.isStringEmpty(likeNumeroAvs)) {
            manager.setLikeNumeroAvs(likeNumeroAvs);
        }
        if (!JAUtil.isStringEmpty(forIdTypeAnomalie)) {
            manager.setForIdTypeAnomalie(forIdTypeAnomalie);
        }
        manager.setOrder("KTNAVS");

    }

    @Override
    protected void addRow(BEntity arg0) throws FWIException {
        CIAnomalieCI anomalie = (CIAnomalieCI) arg0;
        _addCell(NSUtil.formatAVSUnknown(anomalie.getNumeroAvs().trim()));
        _addCell(getSession().getCodeLibelle(anomalie.getTypeAnomalie()));
        _addCell("");
        _addCell(anomalie.getNomPrenom());
        _addCell("");
        _addCell("");
        _addCell(anomalie.getCaisseAgenceFormatee());
        _addCell(!JAUtil.isIntegerEmpty(anomalie.getMotifOuverture()) ? anomalie.getMotifOuverture() : "");
        _addCell(!JAUtil.isIntegerEmpty(anomalie.getAnneeOuverture()) ? anomalie.getAnneeOuverture() : "");
        _addCell(NSUtil.formatAVSUnknown(anomalie.getNumeroAvsPrecedent()));

    }

    public String getForIdTypeAnomalie() {
        return forIdTypeAnomalie;
    }

    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    @Override
    protected void initializeTable() {
        // colonnes
        _addColumnLeft(getSession().getLabel("MSG_DOSSIER_EMAIL_NO_AVS"));
        _addColumnLeft(getSession().getLabel("MSG_COMPARAISON_TYPE_ANOMLIE"));
        _addColumnLeft("");
        _addColumnLeft(getSession().getLabel("MSG_ANNONCE_EMAIL_NOM"));
        _addColumnLeft("");
        _addColumnLeft("");
        _addColumnLeft(getSession().getLabel("MSG_COMPARAISON_DERNIERE_CLOTURE"));
        _addColumnLeft(getSession().getLabel("MSG_COMPARAISON_DERNIER_MOTIF_OUVERTURE"));
        _addColumnLeft(getSession().getLabel("MSG_COMPARAISON_ANNEE_OUVERTURE"));
        _addColumnLeft(getSession().getLabel("MSG_COMPARAISON_NUMERO_AVS_ANCIEN"));
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param string
     */
    public void setForIdTypeAnomalie(String string) {
        forIdTypeAnomalie = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = string;
    }

}
