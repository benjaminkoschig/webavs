package globaz.osiris.print.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAParametreEtape;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;
import globaz.osiris.utils.CATiersUtil;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Contentieux : Liste Excel des poursuites par office Date de création : (10.07.2002 14:07:18) Date de modification :
 * 14.08.2006
 * 
 * @author: Administrator
 */
public class CAListParOP extends CAAbstractListExcel {

    private class TotauxParOffice {
        FWCurrency totalFraisOffice = new FWCurrency();
        FWCurrency totalMontantOffice = new FWCurrency();
        int totalParOffice = 0;

        public TotauxParOffice() {
        }
    }

    private static final String NUMERO_REFERENCE_INFOROM = "0154GCA";
    private ArrayList colTitles = new ArrayList();
    private String currIdOffPours = "";
    private String date = new String();
    private String dateReference = new String();
    private String lastDebiteur = new String();
    private ArrayList listOffices = new ArrayList();

    private ArrayList listTotauxParOffice = new ArrayList();

    private boolean modePrevisionnel = false;
    private String selectionTriListeCA = new String();
    private String selectionTriListeSection = new String();

    private FWCurrency totalFrais = new FWCurrency();
    private int totalGeneral = 0;
    private FWCurrency totalMontant = new FWCurrency();

    /**
     * Date de création : (10.07.2002 14:33:17)
     * 
     * @throws Exception
     */
    public CAListParOP() throws Exception {
        this(new BSession(OsirisDef.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2002 14:43:27)
     * 
     * @param parent
     *            BProcess
     */
    public CAListParOP(BProcess parent) throws Exception {
        this(parent.getSession());
    }

    /**
     * @param sheetTitle
     * @param session
     */
    public CAListParOP(BSession session) {
        super(session, "ListPoursuiteParOffice", session.getLabel("CONT_POU_LISTE"));

        // Titre des colonnes
        colTitles.add(new ParamTitle(getSession().getLabel("CONT_POU_DEBITEUR")));
        colTitles.add(new ParamTitle(getSession().getLabel("CONT_POU_FACTURE")));
        colTitles.add(new ParamTitle(getSession().getLabel("CONT_POU_MONTANT")));
        colTitles.add(new ParamTitle(getSession().getLabel("CONT_POU_AVANCE_FRAIS")));
        colTitles.add(new ParamTitle(getSession().getLabel("CONT_POU_ETAPE")));
    }

    /**
     * Date de création : (11.07.2002 12:46:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDate() {
        return date;
    }

    /**
     * Date de création : (11.07.2002 12:45:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateReference() {
        return dateReference;
    }

    @Override
    public String getNumeroInforom() {
        return CAListParOP.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * Retourne le numéro de la commune OFS Créé le : 12 sept. 06
     * 
     * @param section
     * @return le numéro de la commune OFS
     */
    private String getNumOfficeDesPoursuite(CASection section) {
        IntAdresseCourrier adresse = section.getCompteAnnexe().getTiers()
                .getAdresseCourrier(IntAdresseCourrier.POURSUITE);
        return adresse.getNumCommuneOfs();
    }

    /**
     * Date de création : (22.07.2002 14:21:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSelectionTriListeCA() { // InsertROW()
        return selectionTriListeCA;
    }

    /**
     * Date de création : (22.07.2002 14:23:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSelectionTriListeSection() { // InsertROW()
        return selectionTriListeSection;
    }

    /**
     * Initialise une feuille Excel
     * 
     * @author: sel Créé le : 17 août 06
     * @param sheet
     */
    protected void initSheet() {
        createHeader();
        createFooter(CAListParOP.NUMERO_REFERENCE_INFOROM);
        // Mise en page en landscape
        initPage(true);

        // Largeur des colonnes (nombre de caractères * 256)
        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION); // Compte
        // annexxe
        // Débiteur
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION); // Section
        // Facture
        currentSheet.setColumnWidth((short) 2, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Montant
        currentSheet.setColumnWidth((short) 3, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Avance
        // de
        // frais
        currentSheet.setColumnWidth((short) 4, CAAbstractListExcel.COLUMN_WIDTH_4500); // Etape
    }

    /**
     * Créé le : 27.06.2002 Modifié le : 23 août 06
     * 
     * @param section
     *            globaz.osiris.db.comptes.CASection
     * @param date
     *            String
     * @param montant
     *            String
     * @param frais
     *            String
     */
    public void insertRow(CASection section, CAParametreEtape etape, String date, String montant, String frais) {
        HSSFCellStyle styleTexte = getStyleListLeft();

        // Compte annexe
        CACompteAnnexe _compte = (CACompteAnnexe) section.getCompteAnnexe();
        String debiteur = _compte.getIdExterneRole() + " " + _compte.getDescription() + ", "
                + _compte.getTiers().getLieu();

        // Office de poursuites
        String idOffPours;

        if (getNumOfficeDesPoursuite(section) != null) {
            try {
                idOffPours = CATiersUtil.getLibelleOfficeDesPoursuitesCourt(getSession(), section.getCompteAnnexe()
                        .getTiers(), section.getCompteAnnexe().getIdExterneRole());
            } catch (Exception e) {
                idOffPours = "";
                e.printStackTrace();
            }
        } else {
            idOffPours = "";
        }

        // Section
        String facture = section.getIdExterne() + " " + section.getDescription();
        // Frais
        FWCurrency totalFrais = new FWCurrency(frais);
        // Montant
        FWCurrency totalMontant = new FWCurrency(montant);
        // Etape
        String sEtape = etape.getEtape().getDescription();

        // Mise à jour des totaux généraux
        this.totalFrais.add(totalFrais);
        this.totalMontant.add(totalMontant);

        // Mise à jour de l'Id courant
        currIdOffPours = idOffPours;

        selectSheet(section); // Selectionne la bonne feuille ou la crée
        createRow();
        // Débiteur
        if (!lastDebiteur.equalsIgnoreCase(debiteur)) {
            this.createCell(debiteur, styleTexte);
        } else {
            this.createCell("", styleTexte);
        }
        // Facture
        this.createCell(facture, styleTexte);
        // Montant
        this.createCell(totalMontant.doubleValue(), false);
        // Frais
        this.createCell(totalFrais.doubleValue(), false);
        // Etape
        this.createCell(sEtape, styleTexte);

        // Mise à jour des totaux par office
        ((TotauxParOffice) listTotauxParOffice.get(listOffices.indexOf(currIdOffPours))).totalParOffice++;
        ((TotauxParOffice) listTotauxParOffice.get(listOffices.indexOf(currIdOffPours))).totalFraisOffice
                .add(totalFrais);
        ((TotauxParOffice) listTotauxParOffice.get(listOffices.indexOf(currIdOffPours))).totalMontantOffice
                .add(totalMontant);
        totalGeneral++;
        lastDebiteur = debiteur;
    }

    /**
     * Date de création : (27.06.2002 10:53:43)
     * 
     * @return boolean
     */
    public boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * Test si la feuille à déjà été créée pour cette office
     * 
     * @author: sel Créé le : 12 sept. 06
     * @param section
     * @return Sheet
     */
    private HSSFSheet selectSheet(CASection section) {
        try {
            // Instancier la classe tiers
            CAApplication currentApplication = CAApplication.getApplicationOsiris();
            IntTiers tiers = (IntTiers) GlobazServer.getCurrentSystem()
                    .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                    .getImplementationFor(getSession(), IntTiers.class);
            // Récupérer un tiers
            tiers.retrieve("");

            // Test si la feuille à déjà été créée pour cette office
            if (listOffices.contains(currIdOffPours)) {
                // On utilise la feuille existante
                setCurrentSheet(getWorkbook().getSheetAt(listOffices.indexOf(currIdOffPours)));
            } else {
                // On créée une nouvelle feuille correspondante
                listTotauxParOffice.add(new TotauxParOffice());

                String libelleOffice = CATiersUtil.getLibelleOfficeDesPoursuitesCourt(getSession(), section
                        .getCompteAnnexe().getTiers(), section.getCompteAnnexe().getIdExterneRole());
                createSheet(libelleOffice);
                initSheet();

                // Ajoute à la liste
                listOffices.add(currIdOffPours);

                createRow();
                this.createCell(CATiersUtil.getLibelleOfficeDesPoursuitesLong(getSession(), section.getCompteAnnexe()
                        .getTiers(), section.getCompteAnnexe().getIdExterneRole()));
                createRow();

                // Création de la ligne avec les entêtes de colonnes
                initTitleRow(colTitles);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return currentSheet;
    }

    /**
     * Date de création : (11.07.2002 12:46:40)
     * 
     * @param newDate
     *            java.lang.String
     */
    public void setDate(java.lang.String newDate) {
        date = newDate;
    }

    /**
     * Date de création : (11.07.2002 12:45:34)
     * 
     * @param newDateReference
     *            java.lang.String
     */
    public void setDateReference(java.lang.String newDateReference) {
        dateReference = newDateReference;
    }

    /**
     * Génère les totaux.
     * 
     * @author: sel Créé le : 23 août 06
     */
    public void setFinalizeSheet() {
        for (int i = 0; i < listOffices.size(); i++) {
            setCurrentSheet(getWorkbook().getSheetAt(i));
            TotauxParOffice totOffice = (TotauxParOffice) listTotauxParOffice.get(i);
            // Totale dernière feuille
            createRow();

            // Champs de total par Office
            this.createCell("* " + getSession().getLabel("CONT_POU_TOT_OFF"), getStyleListTitleLeft());
            this.createCell(String.valueOf(totOffice.totalParOffice), getStyleListTitleLeft());
            this.createCell(totOffice.totalMontantOffice.doubleValue(), true);
            this.createCell(totOffice.totalFraisOffice.doubleValue(), true);
            // Initialisation des totaux par office
            totOffice = null;

            // Champs de total
            createRow();
            this.createCell("* " + getSession().getLabel("CONT_POU_TOT_GEN"), getStyleListTitleLeft());
            this.createCell(String.valueOf(totalGeneral), getStyleListTitleLeft());
            this.createCell(totalMontant.doubleValue(), true);
            this.createCell(totalFrais.doubleValue(), true);
        }
    }

    /**
     * Date de création : (27.06.2002 10:53:43)
     * 
     * @param newModePrevisionnel
     *            boolean
     */
    public void setModePrevisionnel(boolean newModePrevisionnel) {
        modePrevisionnel = newModePrevisionnel;
    }

    /**
     * Date de création : (22.07.2002 14:21:49)
     * 
     * @param newSelectionTriListeCA
     *            java.lang.String
     */
    public void setSelectionTriListeCA(String newSelectionTriListeCA) {
        selectionTriListeCA = newSelectionTriListeCA;
    }

    /**
     * Date de création : (22.07.2002 14:23:18)
     * 
     * @param newSelectionTriListeSection
     *            java.lang.String
     */
    public void setSelectionTriListeSection(String newSelectionTriListeSection) {
        selectionTriListeSection = newSelectionTriListeSection;
    }
}
