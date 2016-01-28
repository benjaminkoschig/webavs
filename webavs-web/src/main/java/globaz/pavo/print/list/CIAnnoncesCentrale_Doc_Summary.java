package globaz.pavo.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIAnnonceCentraleManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JRExporterParameter;

/**
 * @author jpa génère un résumé des annonces a la centrale de la table ciancep sous forme de tableau
 */
public class CIAnnoncesCentrale_Doc_Summary extends FWIDocumentManager {

    private static final long serialVersionUID = -717030255289388893L;
    private String anneeCourante;
    // ** intervalle pour laquelle la dernière liste de rappels a eu lieu
    private Vector annonces = new Vector();
    private Map column = null;
    private int compteurInscriptions = 0;
    private String forAnnee;
    private boolean hasNext = true;
    private List list = new ArrayList();

    private int numeroAnnonce = 1;

    public CIAnnoncesCentrale_Doc_Summary(BSession session) throws FWIException {
        super(session, CIApplication.APPLICATION_PAVO_REP, session.getLabel("ANNONCESCENTRALESUM_TITLE"));
    }

    /**
     * Constructor for HERappelDocument_Doc.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CIAnnoncesCentrale_Doc_Summary(BSession session, String rootApplication, String fileName)
            throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * Constructor for HERappelDocument_Doc.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CIAnnoncesCentrale_Doc_Summary(FWProcess parent, String rootApplication, String fileName)
            throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Method _setTemplate.
     */
    private void _setTemplate() {
        super.setTemplateFile("PAVO_Annonces_Centrale_Summary");
    }

    @Override
    public void afterCreateDataSource() {
        super.afterCreateDataSource();
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.SOMME,
                formatNumber(String.valueOf(compteurInscriptions)));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_ANZAHL,
                getSession().getLabel("ANNONCESCENTRALESUM_ANZAHL"));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_NUMMER,
                getSession().getLabel("ANNONCESCENTRALESUM_NUMMER"));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_ENTRY1,
                getSession().getLabel("ANNONCESCENTRALESUM_ENTRY1"));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_ENTRY2,
                getSession().getLabel("ANNONCESCENTRALESUM_ENTRY2"));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_ENTRY3,
                getSession().getLabel("ANNONCESCENTRALESUM_ENTRY3"));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_ENTRY4,
                getSession().getLabel("ANNONCESCENTRALESUM_ENTRY4"));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_ENTRY5,
                getSession().getLabel("ANNONCESCENTRALESUM_ENTRY5"));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.P_ENTRY6,
                getSession().getLabel("ANNONCESCENTRALESUM_ENTRY6"));
        getImporter().setParametre(
                CIAnnoncesCentrale_Param_Summary.COMPANY,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                        + getSession().getIdLangueISO().toUpperCase()));
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.TITLE,
                getSession().getLabel("ANNONCESCENTRALESUM_TITLE") + " " + anneeCourante);
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.DATE, JACalendar.todayJJsMMsAAAA());
        getImporter().setParametre(CIAnnoncesCentrale_Param_Summary.PAGE,
                getSession().getLabel("ANNONCESCENTRALESUM_PAGE"));
    }

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            anneeCourante = JadeStringUtil.isEmpty(getForAnnee()) ? String.valueOf(JACalendar.getYear(JACalendar
                    .todayJJsMMsAAAA())) : getForAnnee();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
        super.getExporter().setExportFileName(getSession().getLabel("ANNONCESCENTRALESUM"));
        loadManager();

        setEMailAddress(getEMailAddress());
        if (annonces.size() == 0) {
            // aucun rappel a envoyer donc pas besoin d'envoyer un email
            setSendCompletionMail(false);
            System.out.println("Aucune annonce trouvée ");
        }
    }

    private String calculDatenRecords(CIAnnonceCentrale annonce) {
        int datenRecords = 0;
        int nbrInscriptions = JAUtil.parseInt(annonce.getNbrInscriptions(), 0);
        datenRecords = (nbrInscriptions / 2) + (nbrInscriptions % 2);
        datenRecords += 2;
        return String.valueOf(datenRecords);
    }

    @Override
    public void createDataSource() throws Exception {
        _setTemplate();
        CIAnnonceCentraleManager manager = new CIAnnonceCentraleManager();
        manager.setSession(getSession());
        manager.setOrder("KRDENV");
        if (!JAUtil.isStringEmpty(getForAnnee())) {
            manager.setForAnnee(String.valueOf(getForAnnee()));
        }
        manager.find();
        CIAnnonceCentrale annonce = null;
        long dernierM = 0;
        for (int i = 0; i < manager.size(); i++) {
            annonce = (CIAnnonceCentrale) manager.getEntity(i);
            dernierM = getMontantPrecedent(annonce.getDateEnvoi());
            long differenceM = JAUtil.parseLong(annonce.getMontantTotal(), 0);
            differenceM -= dernierM;
            compteurInscriptions += JAUtil.parseInt(annonce.getNbrInscriptions(), 0);
            annonce.setLoadedFromManager(false);
            annonce.retrieve(getTransaction());
            column = new HashMap();
            // On formate la date envoi JJ.MM.YYYY
            // String dateEnvoi = annonce.getDateEnvoi();
            String dateCreation = annonce.getDateCreation();
            String formattedDateEnvoi = dateCreation.substring(6, 8) + "." + dateCreation.substring(4, 6) + "."
                    + dateCreation.substring(0, 4);
            column.put(CIAnnoncesCentrale_Param_Summary.DATE_ANNONCE, formattedDateEnvoi);
            column.put(CIAnnoncesCentrale_Param_Summary.DATEN_RECORDS, formatNumber(calculDatenRecords(annonce)));
            column.put(CIAnnoncesCentrale_Param_Summary.EINTRAGUNGEN, formatNumber(annonce.getNbrInscriptions()));
            column.put(CIAnnoncesCentrale_Param_Summary.VOR_VERARBEITUNG, formatNumber(String.valueOf(dernierM))); // String.valueOf(dernierMontant));
            column.put(CIAnnoncesCentrale_Param_Summary.EINKOMMEN, formatNumber(String.valueOf(differenceM))); // String.valueOf(differenceMontant));
            column.put(CIAnnoncesCentrale_Param_Summary.NACH_VERARBEITUNG, formatNumber(annonce.getMontantTotal()));
            column.put(CIAnnoncesCentrale_Param_Summary.NUMMER, String.valueOf(numeroAnnonce));
            list.add(column);
        }
        super.setDataSource(list);
    }

    private String formatNumber(String string) {
        String nombreFormate = new String();
        boolean isNombreNeg = false;
        if (string.charAt(0) == '-') {
            isNombreNeg = true;
            string = string.substring(1, string.length());
        }
        StringBuffer nombre = new StringBuffer(string);
        // calcul
        int longueur = nombre.length();
        int reste = nombre.length() % 3;
        int nombreGroupe = nombre.length() / 3;
        if (nombreGroupe > 0) {
            // placement de la première apostrophe
            if (reste != 0) {
                nombreFormate = nombre.substring(0, reste) + "'";
            }
            // placement des autres
            for (int i = 0; i < nombreGroupe; i++) {
                if ((3 * (i + 1)) + reste != longueur) {
                    nombreFormate += nombre.substring(reste + (3 * i), reste + (3 * i) + 3) + "'";
                } else {
                    // c'est le dernier groupe pas d'apostrophes!
                    nombreFormate += nombre.substring(reste + (3 * i), reste + (3 * i) + 3);
                }
            }
            if (!isNombreNeg) {
                return nombreFormate;
            } else {
                return "-" + nombreFormate;
            }

        } else {
            if (!isNombreNeg) {
                return nombre.toString();
            } else {
                return "-" + nombre.toString();
            }
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("AnnoncesCentrale");
    }

    /**
     * @Annee d'exécution
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @param string
     * @return
     */
    private long getMontantPrecedent(String dateEnvoi) {
        CIAnnonceCentraleManager manager = new CIAnnonceCentraleManager();
        manager.setSession(getSession());
        manager.setBeforeDateEnvoi(dateEnvoi);
        manager.setForAnneeEnvoi(dateEnvoi.substring(0, 4));
        manager.setOrder("KRDENV DESC");
        try {
            numeroAnnonce = manager.getCount() + 1;
            manager.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (manager.size() > 0) {
            CIAnnonceCentrale entity = (CIAnnonceCentrale) manager.getFirstEntity();
            return JAUtil.parseLong(entity.getMontantTotal(), 0);
        } else {
            return 0;
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void loadManager() {
        BStatement statement = null;
        CIAnnonceCentraleManager manager = new CIAnnonceCentraleManager();
        manager.setSession(getSession());
        if (!JAUtil.isStringEmpty(getForAnnee())) {
            manager.setForAnnee(String.valueOf(getForAnnee()));
        }
        manager.setOrder("KRDENV");
        try {
            statement = manager.cursorOpen(getTransaction());
            CIAnnonceCentrale entity = null;
            while ((entity = (CIAnnonceCentrale) manager.cursorReadNext(statement)) != null) {
                annonces.add(entity);
            }
            manager.cursorClose(statement);
        } catch (Exception e) {
            super._addError("Les annonces n'ont pas pu être recherchés");
            super.setMsgType(super.ERROR);
            super.setMessage("Les annonces n'ont pas pu être recherchés");
        } finally {
            try {
                if (statement != null && manager != null) {
                    manager.cursorClose(statement);
                }
                statement = null;
            } catch (Exception e) {
                e.printStackTrace();
                super.setMsgType(super.ERROR);
                super.setMessage("La fermeture du curseur a générer une erreur : " + e.getMessage());
            }
        }
    }

    @Override
    public boolean next() throws FWIException {
        boolean res = hasNext;
        hasNext = false;
        if (hasNext) {
            super.setDocumentTitle(getSession().getLabel("MSG_ANNNONCES_CENTRALE_DOC"));
        }
        return res;
    }

    @Override
    public void returnDocument() throws FWIException {
        super.returnDocument();
    }

    public void setForAnnee(String i) {
        forAnnee = i;
    }
}
