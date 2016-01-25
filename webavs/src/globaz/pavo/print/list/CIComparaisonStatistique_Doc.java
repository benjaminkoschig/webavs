package globaz.pavo.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.comparaison.CIAnomalieCI;
import globaz.pavo.db.comparaison.CIAnomalieCIManager;
import globaz.pavo.db.comparaison.CIComparaisonIteratorInput;
import globaz.pavo.db.comparaison.CIComparaisonIteratorInputEBC;
import globaz.pavo.db.comparaison.CICompteIndividuelComparaisonManager;
import globaz.pavo.db.comparaison.ICIComparaisonIteratorInput;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.util.CIUtil;

public class CIComparaisonStatistique_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean isFirst = true;
    private int nb63 = 0;
    private int nbCisAbsentsAvecInscriptions = 0;

    private String userVisa = "";

    public CIComparaisonStatistique_Doc() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));
    }

    public CIComparaisonStatistique_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    public CIComparaisonStatistique_Doc(FWProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public CIComparaisonStatistique_Doc(globaz.globall.db.BSession session) throws Exception {
        super(session, CIApplication.APPLICATION_PAVO_REP, "ImpressionStatistiques");

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber("0159CCI");
        getDocumentInfo().setDocumentType("0159CCI");
        setTemplateFile("PAVO_Comparaison");
        super.setParametres("P_TITRE", getSession().getLabel("MSG_COMPARAISON_IMPRESSION_STAT"));
        super.setParametres("P_LIB_NB_REC", getSession().getLabel("MSG_COMPARAISON_STAT_NB_REC"));
        super.setParametres("P_NB_REC", getNbRecords());
        super.setParametres("P_LIB_NB_CI", getSession().getLabel("MSG_COMPARAISON_IMPRESSION_NB_CI"));
        super.setParametres("P_NB_CI", String.valueOf(getNbCi()));
        // super.setParametres("P_LIB_NB_CI_ABSENTS_CAISSE",
        // getSession().getLabel("MSG_COMPARAISON_IMPRESSION_ABSENT_CAISSE"));
        // Pour trouver le nombre d'ouvertures à la caisse, on cherche le nb
        // d'absent à la caisse avec le flag traité
        int nbCIsAbsents = getNbCisAbsentsCaisse();
        super.setParametres("P_LIB_NB_CI_ABSENTS_CAISSE",
                getSession().getLabel("MSG_COMPARAISON_IMPRESSION_ABSENT_CAISSE"));
        super.setParametres("P_NB_CI_ABSENTS_CAISSE", String.valueOf(nbCIsAbsents));
        // Pour trouver le nb e 63 généré le nb de ci absent à ZAS non rentiers
        // avec le flag traité
        // chgt de nom => ano sur les données = nb de CIs absent à ZAS
        super.setParametres("P_LIB_NB_ANOMALIES_DONNEES", getSession().getLabel("MSG_COMPARAISON_NB_ANOM_DONNEES"));
        super.setParametres("P_NB_ANOMALIES_DONNEES", String.valueOf(retourneAnomaliesDonnees()));
        getNb63Generes();
        super.setParametres("P_LIB_NB_63", getSession().getLabel("MSG_COMPARAISON_IMPRESSION_63_GENERES"));
        super.setParametres("P_NB_63", String.valueOf(nb63));
        super.setParametres("P_LIB_NB_RENTIERS", getSession()
                .getLabel("MSG_COMPARAISON_IMPRESSION_ABSENT_ZAS_RENTIERS"));
        super.setParametres("P_NB_RENTIERS", String.valueOf(nbCisAbsentsAvecInscriptions));
        super.setParametres(
                "P_COMPANY",
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                        + getSession().getIdLangueISO().toUpperCase()));

        super.setParametres("P_LIB_NB_CIS_REOUVERT", getSession().getLabel("MSG_COMPARAISON_NB_CIS_REOUVERTS"));
        super.setParametres("P_NB_CIS_REOUVERT", String.valueOf(retourneCIsReouverts()));

    }

    @Override
    public void beforeExecuteReport() throws FWIException {
    }

    @Override
    public void createDataSource() throws Exception {
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError()) {
            return "l'impression des statistiques s'est effectuée avec succès";
        } else {
            return "l'impression des statistiques a échouée";
        }
    }

    public String getFileNameInput() {
        String file;
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            if (JAUtil.isStringEmpty(application.getLocalPath())) {
                file = "./pavoRoot/comparaison.txt";
            } else {
                file = application.getLocalPath() + "/pavoRoot/comparaison.txt";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return file;
    }

    private void getNb63Generes() {
        try {
            CIAnomalieCIManager mgr = new CIAnomalieCIManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeAnomalie(CIAnomalieCI.CS_CI_ABSENT_A_ZAS);
            // mgr.setForAtraiter(CIAnomalieCI.CS_TRAITE);
            mgr.find(BManager.SIZE_NOLIMIT);
            int size = mgr.size();
            CIAnomalieCI anom = new CIAnomalieCI();
            for (int i = 0; i < size; i++) {
                anom = (CIAnomalieCI) mgr.get(i);
                if (!hasEcriture(anom)) {
                    nb63++;
                } else {
                    nbCisAbsentsAvecInscriptions++;
                }
            }
        } catch (Exception e) {
        }
    }

    private int getNbCi() {
        try {
            CICompteIndividuelComparaisonManager mgr = new CICompteIndividuelComparaisonManager();
            mgr.setSession(getSession());
            return mgr.getCount();
        } catch (Exception e) {
            return 0;
        }

    }

    private int getNbCisAbsentsCaisse() {
        try {
            CIAnomalieCIManager mgr = new CIAnomalieCIManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeAnomalie(CIAnomalieCI.CS_CI_ABSENT_A_LA_CAISSE);
            // mgr.setForAtraiter(CIAnomalieCI.CS_TRAITE);
            return mgr.getCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getNbRecords() {
        try {
            ICIComparaisonIteratorInput iterator = null;
            if (!CIUtil.isComparaisonEBCDIC(getSession())) {
                iterator = new CIComparaisonIteratorInput();
            } else {
                iterator = new CIComparaisonIteratorInputEBC();
            }
            new CIComparaisonIteratorInput();
            iterator.setFileName(getFileNameInput());
            int size = iterator.size();
            return String.valueOf(size);
        } catch (Exception e) {
            return "n/a";
        }
    }

    private boolean hasEcriture(CIAnomalieCI anomalie) {
        try {
            CIEcritureManager mgr = new CIEcritureManager();
            mgr.setForCompteIndividuelId(anomalie.getCompteIndividuelId());
            mgr.setEtatEcritures("actives");
            mgr.setSession(getSession());
            if (mgr.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    private int retourneAnomaliesDonnees() {
        int retour = 0;
        try {
            CIAnomalieCIManager mgr = new CIAnomalieCIManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeAnomalie(CIAnomalieCI.CS_CI_ABSENT_A_ZAS);

            retour = mgr.getCount();
        } catch (Exception e) {
            retour = 0;

        }
        return retour;
    }

    private int retourneCIsReouverts() {
        int retour = 0;
        try {
            CIAnomalieCIManager mgr = new CIAnomalieCIManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeAnomalie(CIAnomalieCI.CS_CI_PRESENT_CLOTURE);
            retour = mgr.getCount();
        } catch (Exception e) {
            return 0;
        }
        return retour;
    }

}
