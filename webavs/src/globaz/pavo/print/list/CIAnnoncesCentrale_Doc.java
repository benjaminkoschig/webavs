package globaz.pavo.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.utils.DateUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIAnnonceCentraleManager;
import globaz.pavo.process.CIAnnonceCentraleImpressionRapportProcess;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JRExporterParameter;

/**
 * @author jpa génère autant de documents que d'annonces dans ciancep affiche les montants et différentes infos de ces
 *         annonces
 */
public class CIAnnoncesCentrale_Doc extends FWIDocumentManager {

    private static final long serialVersionUID = 1L;
    public static final String NUMERO_REFERENCE_INFOROM = "0303CCI";

    private Iterator _docIterator = null;
    private String anneeCourante;
    // ** intervalle pour laquelle la dernière liste de rappels a eu lieu
    private CIAnnonceCentrale annonces = null;
    private Map column = null;
    private int compteur = 0;
    private int compteurInscriptions = 0;
    private long dernierM;
    private String forAnnee;
    private int index = 0;
    private List list = new ArrayList();
    private Vector listeAnnonce = new Vector();

    private String modeImpression = "";

    private int numeroAnnonce = 1;
    private String specifiqueAnnonceCentraleIdPrint = "";

    public CIAnnoncesCentrale_Doc() {
        super();
    }

    public CIAnnoncesCentrale_Doc(BSession session) throws FWIException {
        super(session, CIApplication.APPLICATION_PAVO_REP, session.getLabel("AnnoncesCentrale"));
    }

    /**
     * Constructor for HERappelDocument_Doc.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CIAnnoncesCentrale_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
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
    public CIAnnoncesCentrale_Doc(FWProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Method _setTemplate.
     */
    private void _setTemplate() {
        super.setTemplateFile("PAVO_Annonces_Centrale");
    }

    @Override
    public void afterCreateDataSource() {
        super.afterCreateDataSource();
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY1, getSession().getLabel("ANNONCESCENTRALE_ENTRY1"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY2, getSession().getLabel("ANNONCESCENTRALE_ENTRY2"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY4, getSession().getLabel("ANNONCESCENTRALE_ENTRY4"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY5, getSession().getLabel("ANNONCESCENTRALE_ENTRY5"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY6, getSession().getLabel("ANNONCESCENTRALE_ENTRY6"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY7, getSession().getLabel("ANNONCESCENTRALE_ENTRY7"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY8, getSession().getLabel("ANNONCESCENTRALE_ENTRY8"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.ENTRY9, getSession().getLabel("ANNONCESCENTRALE_ENTRY9"));
        getImporter().setParametre(CIAnnoncesCentrale_Param.SOMME, formatNumber(String.valueOf(compteurInscriptions)));
        getImporter().setParametre(
                CIAnnoncesCentrale_Param.COMPANY,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                        + getSession().getIdLangueISO().toUpperCase()));
        getImporter().setParametre(CIAnnoncesCentrale_Param.TITLE,
                getSession().getLabel("ANNONCESCENTRALE_TITLE") + anneeCourante);
        if (CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE.equals(annonces.getIdTypeAnnonce())) {
            getImporter().setParametre(CIAnnoncesCentrale_Param.TYPE,
                    getSession().getLabel("ANNONCESCENTRALE_LETZTE_MELDUNG"));
        } else {
            getImporter().setParametre(CIAnnoncesCentrale_Param.TYPE,
                    getSession().getLabel("ANNONCESCENTRALE_ZWISCHEN_MELDUNG"));
        }
        getImporter().setParametre(CIAnnoncesCentrale_Param.DATE, JACalendar.todayJJsMMsAAAA());
        getImporter().setParametre(CIAnnoncesCentrale_Param.NUM_PAGE,
                getSession().getLabel("ANNONCESCENTRALESUM_PAGE") + " " + index + "/" + listeAnnonce.size());
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
        loadManager();
        _docIterator = listeAnnonce.iterator();
        if (listeAnnonce.size() == 1) {
            super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
        }
        setEMailAddress(getEMailAddress());
        // setSendCompletionMail(false);
        if (listeAnnonce.size() == 0) {
            System.out.println("Aucune annonce trouvée ");
        }
    }

    private String calculDatenRecords() {
        int datenRecords = 0;
        int nbrInscriptions = JAUtil.parseInt(annonces.getNbrInscriptions(), 0);
        datenRecords = (nbrInscriptions / 2) + (nbrInscriptions % 2);
        datenRecords += 2;
        return String.valueOf(datenRecords);
    }

    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CIAnnoncesCentrale_Doc.NUMERO_REFERENCE_INFOROM);
        _setTemplate();
        dernierM = getMontantPrecedent(DateUtils.convertDate(annonces.getDateEnvoi(), DateUtils.JJMMAAAA_DOTS,
                DateUtils.AAAAMMJJ));
        long differenceM = JAUtil.parseLong(annonces.getMontantTotal(), 0);
        differenceM -= dernierM;
        compteurInscriptions += JAUtil.parseInt(annonces.getNbrInscriptions(), 0);
        column = new HashMap();

        // BZ 9386 : on prend la date du jour pour se rapprocher de la date d'envoi qui est prise lorsqu'on fait un
        // HEPUT
        Date date = new Date();
        String formattedDate = JadeDateUtil.getGlobazFormattedDate(date);

        column.put(CIAnnoncesCentrale_Param.DATE_ANNONCE, formattedDate);
        column.put(CIAnnoncesCentrale_Param.DATEN_RECORDS, formatNumber(calculDatenRecords()));
        column.put(CIAnnoncesCentrale_Param.EINTRAGUNGEN, formatNumber(annonces.getNbrInscriptions()));
        column.put(CIAnnoncesCentrale_Param.VOR_VERARBEITUNG, formatNumber(String.valueOf(dernierM))); // String.valueOf(dernierMontant));
        column.put(CIAnnoncesCentrale_Param.EINKOMMEN, formatNumber(String.valueOf(differenceM))); // String.valueOf(differenceMontant));
        column.put(CIAnnoncesCentrale_Param.NACH_VERARBEITUNG, formatNumber(annonces.getMontantTotal()));
        column.put(CIAnnoncesCentrale_Param.MELDUNG_NUMMER, String.valueOf(numeroAnnonce));
        list.clear();
        list.add(column);
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

    public String getModeImpression() {
        return modeImpression;
    }

    private long getMontantPrecedent(String dateEnvoi) {
        CIAnnonceCentraleManager manager = new CIAnnonceCentraleManager();
        manager.setSession(getSession());
        manager.setBeforeDateEnvoi(dateEnvoi);
        manager.setForAnneeEnvoi(dateEnvoi.substring(0, 4));

        if (isModeImpressionInforomD0064()) {
            manager.setInStatut(CIAnnonceCentrale.CS_ETAT_GENERE + "," + CIAnnonceCentrale.CS_ETAT_ENVOYE);
        }

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

    public String getSpecifiqueAnnonceCentraleIdPrint() {
        return specifiqueAnnonceCentraleIdPrint;
    }

    private boolean isModeImpressionInforomD0064() {
        return CIAnnonceCentraleImpressionRapportProcess.MODE_IMPRESSION_INFOROM_D0064.equalsIgnoreCase(modeImpression);
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
        if (isModeImpressionInforomD0064()) {
            manager.setForAnnonceCentraleId(specifiqueAnnonceCentraleIdPrint);
        }
        manager.setOrder("KRDENV");
        try {
            statement = manager.cursorOpen(getTransaction());
            CIAnnonceCentrale entity = null;
            while ((entity = (CIAnnonceCentrale) manager.cursorReadNext(statement)) != null) {
                listeAnnonce.add(entity);

            }
            manager.cursorClose(statement);
        } catch (Exception e) {
            super._addError("Les annonces n'ont pas pu être recherchés");
            super.setMsgType(FWViewBeanInterface.ERROR);
            super.setMessage("Les annonces n'ont pas pu être recherchés");
        } finally {
            try {
                if ((statement != null) && (manager != null)) {
                    manager.cursorClose(statement);
                }
                statement = null;
            } catch (Exception e) {
                e.printStackTrace();
                super.setMsgType(FWViewBeanInterface.ERROR);
                super.setMessage("La fermeture du curseur a générer une erreur : " + e.getMessage());
            }
        }
    }

    @Override
    public boolean next() throws FWIException {
        boolean hasNext;
        if (hasNext = _docIterator.hasNext()) {
            annonces = (CIAnnonceCentrale) _docIterator.next();
            super.setDocumentTitle(annonces.getDateCreation());
            index++;
        }

        compteur++;
        return hasNext;
    }

    @Override
    public void returnDocument() throws FWIException {
        super.returnDocument();
    }

    public void setForAnnee(String i) {
        forAnnee = i;
    }

    public void setModeImpression(String modeImpression) {
        this.modeImpression = modeImpression;
    }

    public void setSpecifiqueAnnonceCentraleIdPrint(String specifiqueAnnonceCentraleIdPrint) {
        this.specifiqueAnnonceCentraleIdPrint = specifiqueAnnonceCentraleIdPrint;
    }
}
