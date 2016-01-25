package globaz.osiris.db.export;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CACompteCourantManager;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CARoleManager;
import globaz.osiris.db.comptes.CASoldeCompteAnnexeAtDate;
import globaz.osiris.db.comptes.CASoldeCompteAnnexeCC;
import globaz.osiris.external.IntRole;
import globaz.osiris.print.itext.list.CAIListSoldeCompteAnnexeAtDate_DS;
import globaz.osiris.print.itext.list.CAIListSoldeCompteAnnexeCC_DS;
import globaz.osiris.print.itext.list.CAIListSoldeCompteAnnexe_DS;
import globaz.osiris.print.list.CAAbstractListExcel;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author: ado
 */
public class CAListSoldeCompteAnnexe extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0013GCA";
    private String CAS;
    private String[] MANAGERINFO = new String[5];
    private BProcess process = null;
    private BTransaction transaction = null;

    /**
     * Methode CAListSoldeCCDocument.
     * 
     * @param sheetTitle
     * @param session
     * @param exportListSoldeCC
     */
    public CAListSoldeCompteAnnexe(String sheetTitle, BSession session, BTransaction transaction, BProcess process) {
        super(session, "CAListSoldeCC", session.getLabel("6003"));
        this.transaction = transaction;
        this.process = process;
        // Titre de ligne de totalisation
        CAS = session.getLabel("TOTAL_DE_CAS");

        createSheet(sheetTitle);
    }

    @Override
    public String getNumeroInforom() {
        return CAListSoldeCompteAnnexe.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * Créé le : 25 août 06
     * 
     * @param sheet
     * @param MANAGERINFO
     * @return HSSFSheet
     */
    private HSSFSheet initCritere() {
        // Critères de recherche
        String[] critereTitres = new String[4];
        critereTitres[0] = getSession().getLabel("ROLE");
        critereTitres[1] = getSession().getLabel("TRI");
        critereTitres[2] = getSession().getLabel("SIGNE");
        critereTitres[3] = getSession().getLabel("COMPTECOURANT");

        // Affichage de la sélection du rôle
        CARole tempRole;
        CARoleManager manRole = new CARoleManager();
        manRole.setSession(getSession());

        createRow();
        this.createCell(critereTitres[0], getStyleCritereTitle());
        try {
            manRole.find(transaction);
            if (!MANAGERINFO[0].equalsIgnoreCase("1000")) {
                String roles = "";
                for (int i = 0; i < manRole.size(); i++) {
                    tempRole = (CARole) manRole.getEntity(i);
                    if (MANAGERINFO[0].indexOf(tempRole.getIdRole()) != -1) {
                        roles += tempRole.getDescription() + " ; ";
                    }
                }
                this.createCell(roles, getStyleCritere());
            } else {
                this.createCell(getSession().getLabel("TOUS"), getStyleCritere());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Affichage de la sélection de tri
        createRow();
        this.createCell(critereTitres[1], getStyleCritereTitle());
        if (MANAGERINFO[1].equalsIgnoreCase("1")) {
            this.createCell(getSession().getLabel("NUMERO"), getStyleCritere());
        } else {
            this.createCell(getSession().getLabel("NOM"), getStyleCritere());
        }
        // Affichage de la sélection de choix des signes
        createRow();
        this.createCell(critereTitres[2], getStyleCritereTitle());
        if (MANAGERINFO[2].equalsIgnoreCase("1")) {
            this.createCell(getSession().getLabel("POSITIF_ET_NEGATIF"), getStyleCritere());
        } else if (MANAGERINFO[2].equalsIgnoreCase("2")) {
            this.createCell(getSession().getLabel("POSITIF"), getStyleCritere());
        } else {
            this.createCell(getSession().getLabel("NEGATIF"), getStyleCritere());
        }
        // Affichage de la sélection du compte courant
        createRow();
        this.createCell(critereTitres[3], getStyleCritereTitle());

        CACompteCourant tempCC;
        CACompteCourantManager manCC = new CACompteCourantManager();
        manCC.setSession(getSession());
        try {
            manCC.find(transaction);
            if (!MANAGERINFO[3].equalsIgnoreCase("1000") && !"".equals(MANAGERINFO[3])) {
                String cc = "";
                for (int i = 0; i < manCC.size(); i++) {
                    tempCC = (CACompteCourant) manCC.getEntity(i);
                    if (tempCC.getIdCompteCourant().equalsIgnoreCase(MANAGERINFO[3])) {
                        cc += tempCC.getIdExterne() + " ";
                        break;
                    }
                }
                this.createCell(cc, getStyleCritere());
            } else {
                this.createCell(getSession().getLabel("TOUS"), getStyleCritere());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentSheet;
    }

    /**
     * Création de la page ("Liste des décisions d'intérêts")
     * 
     * @return
     */
    private void initListe() {
        ArrayList colTitles = new ArrayList();
        // Titre des colonnes
        colTitles.add(new ParamTitle(getSession().getLabel("ROLE")));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("DESCRIPTION")));
        colTitles.add(new ParamTitle(getSession().getLabel("DATE_RADIATION")));
        colTitles.add(new ParamTitle(getSession().getLabel("MOTIF_FIN_AFFILIATION")));
        colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));

        initCritere();

        // Création de la ligne avec les entêtes de colonnes
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListSoldeCompteAnnexe.NUMERO_REFERENCE_INFOROM);

        // Largeur des colonnes (nombre de caractères * 256)
        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_3500);
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_COMPTEANNEXE);
        currentSheet.setColumnWidth((short) 2, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 3, CAAbstractListExcel.COLUMN_WIDTH_DATE);
        currentSheet.setColumnWidth((short) 4, CAAbstractListExcel.COLUMN_WIDTH_5500); // 6400
        currentSheet.setColumnWidth((short) 5, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * Method setTotalRow. Permet de définir la ligne de totalisation
     * 
     * @param wb
     * @param sheet
     * @param nbCas
     * @param montantTotal
     * @return HSSFSheet
     */
    private HSSFSheet initTotalRow(int nbCas, double montantTotal) {
        // create Title Row
        createRow();
        this.createCell(CAS, getStyleListTitleLeft());
        this.createCell(nbCas, getStyleListTitleLeft());
        this.createCell(" ", getStyleListTitleLeft());
        this.createCell(" ", getStyleListTitleLeft());
        this.createCell(" ", getStyleListTitleLeft());
        // Colonne Solde
        this.createCell(montantTotal, true);

        return currentSheet;
    }

    /**
     * @param manager
     * @return
     */
    public HSSFSheet populateSheet(BManager manager) {
        if (manager instanceof CAIListSoldeCompteAnnexe_DS) {
            return this.populateSheet((CAIListSoldeCompteAnnexe_DS) manager);
        } else if (manager instanceof CAIListSoldeCompteAnnexeCC_DS) {
            return this.populateSheet((CAIListSoldeCompteAnnexeCC_DS) manager);
        } else if (manager instanceof CAIListSoldeCompteAnnexeAtDate_DS) {
            return this.populateSheet((CAIListSoldeCompteAnnexeAtDate_DS) manager);
        }
        return null;
    }

    /**
     * @param CAIListSoldeCompteAnnexe_DS
     *            manager
     * @return HSSFSheet
     */
    public HSSFSheet populateSheet(CAIListSoldeCompteAnnexe_DS manager) {
        FWCurrency fTotal = new FWCurrency(0.0);
        // Création de l'entête de page
        MANAGERINFO[0] = manager.getForSelectionRole();
        MANAGERINFO[1] = manager.getForSelectionTri();
        MANAGERINFO[2] = manager.getForSelectionSigne();
        MANAGERINFO[3] = "";
        MANAGERINFO[4] = "";

        initListe();
        process.setProgressScaleValue(manager.size());

        int i = 0;
        for (; (i < manager.size()) && !process.isAborted(); i++) {
            // Parcourir les comptes annexes
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.getEntity(i);
            try {
                createRow();
                // Role
                this.createCell(compteAnnexe.getCARole().getDescription(), getStyleListLeft());
                // Description
                this.createCell(compteAnnexe.getIdExterneRole(), getStyleListLeft());
                // Description du compte annexe
                this.createCell(compteAnnexe.getDescription(), getStyleListLeft());
                // Date de radiation
                if ((compteAnnexe.getRole() != null) && (!JAUtil.isDateEmpty(compteAnnexe.getRole().getDateFin()))) {
                    this.createCell(JACalendar.format(compteAnnexe.getRole().getDateFin()), getStyleListLeft());
                } else {
                    this.createCell("", getStyleListLeft());
                }
                // Motif de fin d'affiliation
                if ((compteAnnexe.getRole() != null) && (!JAUtil.isDateEmpty(compteAnnexe.getRole().getDateFin()))) {
                    this.createCell(CACodeSystem.getLibelle(getSession(), compteAnnexe.getRole().getMotifFin()),
                            getStyleListLeft());
                } else {
                    this.createCell("", getStyleListLeft());
                }
                // Solde
                Double dMontant = new Double(compteAnnexe.getSolde());
                this.createCell(dMontant.doubleValue(), false);
                fTotal.add(compteAnnexe.getSolde());
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            process.incProgressCounter();
        }
        // Création de la lgine de totalisation
        initTotalRow(i, new Double(fTotal.toString()).doubleValue());

        return currentSheet;
    }

    /**
     * Method populateSheet.
     * 
     * @param CAIListSoldeCompteAnnexeAtDate_DS
     *            manager
     * @return HSSFSheet
     */
    public HSSFSheet populateSheet(CAIListSoldeCompteAnnexeAtDate_DS manager) {
        FWCurrency fTotal = new FWCurrency(0.0);
        // Création de l'entête de page
        MANAGERINFO[0] = manager.getForSelectionRole();
        MANAGERINFO[1] = manager.getForSelectionTri();
        MANAGERINFO[2] = manager.getForSelectionSigne();
        MANAGERINFO[3] = manager.getForSelectionCC();

        initListe();

        process.setProgressScaleValue(manager.size());
        int nbCas = 0;
        Iterator it = manager.iterator();
        while (it.hasNext() && !process.isAborted()) {
            process.incProgressCounter();

            // Parcourir les comptes annexes
            CASoldeCompteAnnexeAtDate compteAnnexe = (CASoldeCompteAnnexeAtDate) it.next();

            try {
                createRow();
                // Role
                this.createCell(compteAnnexe.getCARole().getDescription(), getStyleListLeft());
                // Description
                this.createCell(compteAnnexe.getIdExterneRole(), getStyleListLeft());
                // Description du compte annexe
                this.createCell(compteAnnexe.getDescription(), getStyleListLeft());
                // Date de radiation
                if ((compteAnnexe.getRole() != null) && (!JAUtil.isDateEmpty(compteAnnexe.getRole().getDateFin()))) {
                    this.createCell(JACalendar.format(compteAnnexe.getRole().getDateFin()), getStyleListLeft());
                } else {
                    this.createCell("", getStyleListLeft());
                }

                // Motif de fin d'affiliation
                if ((compteAnnexe.getRole() != null) && (!JAUtil.isDateEmpty(compteAnnexe.getRole().getDateFin()))) {
                    this.createCell(CACodeSystem.getLibelle(getSession(), compteAnnexe.getRole().getMotifFin()),
                            getStyleListLeft());
                } else {
                    this.createCell("", getStyleListLeft());
                }

                // Solde
                Double dMontant = new Double(compteAnnexe.getSolde());
                this.createCell(dMontant.doubleValue(), false);
                fTotal.add(compteAnnexe.getSolde());
                nbCas++;

            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        // Création de la lgine de totalisation
        initTotalRow(nbCas, new Double(fTotal.toString()).doubleValue());

        return currentSheet;
    }

    /**
     * Method populateSheet.
     * 
     * @param CAIListSoldeCompteAnnexeAtDate_DS
     *            manager
     * @return HSSFSheet
     */
    public HSSFSheet populateSheet(CAIListSoldeCompteAnnexeCC_DS manager) {
        FWCurrency fTotal = new FWCurrency(0.0);
        // Création de l'entête de page
        MANAGERINFO[0] = manager.getForSelectionRole();
        MANAGERINFO[1] = manager.getForSelectionTri();
        MANAGERINFO[2] = manager.getForSelectionSigne();
        MANAGERINFO[3] = manager.getForSelectionCC();

        initListe();
        process.setProgressScaleValue(manager.size());

        int nbCas = 0;
        Iterator it = manager.iterator();
        while (it.hasNext() && !process.isAborted()) {
            // Parcourir les comptes annexes
            CASoldeCompteAnnexeCC compteAnnexeCC = (CASoldeCompteAnnexeCC) it.next();

            try {
                createRow();
                // Role
                this.createCell(compteAnnexeCC.getCARole().getDescription(), getStyleListLeft());
                // Description
                this.createCell(compteAnnexeCC.getIdExterneRole(), getStyleListLeft());
                // Description du compte annexe
                this.createCell(compteAnnexeCC.getDescription(), getStyleListLeft());
                // Date de radiation
                if ((!JadeStringUtil.isIntegerEmpty(compteAnnexeCC.getIdRole()))
                        && (!JadeStringUtil.isBlank(compteAnnexeCC.getIdExterneRole()))) {
                    CAApplication currentApplication = CAApplication.getApplicationOsiris();
                    IntRole intRole = (IntRole) GlobazServer.getCurrentSystem()
                            .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                            .getImplementationFor(getSession(), IntRole.class);
                    intRole.retrieve(compteAnnexeCC.getIdRole(), compteAnnexeCC.getIdExterneRole());
                    if (!JAUtil.isDateEmpty(intRole.getDateFin())) {
                        this.createCell(JACalendar.format(intRole.getDateFin()), getStyleListLeft());
                    } else {
                        this.createCell("", getStyleListLeft());
                    }
                } else {
                    this.createCell("", getStyleListLeft());
                }

                // Motif de fin d'affiliation
                if ((!JadeStringUtil.isIntegerEmpty(compteAnnexeCC.getIdRole()))
                        && (!JadeStringUtil.isBlank(compteAnnexeCC.getIdExterneRole()))) {
                    CAApplication currentApplication = CAApplication.getApplicationOsiris();
                    IntRole intRole = (IntRole) GlobazServer.getCurrentSystem()
                            .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                            .getImplementationFor(getSession(), IntRole.class);
                    intRole.retrieve(compteAnnexeCC.getIdRole(), compteAnnexeCC.getIdExterneRole());
                    if (!JAUtil.isDateEmpty(intRole.getDateFin())) {
                        // cell.setCellValue(JACalendar.format(intRole.getDateFin()));
                        this.createCell(CACodeSystem.getLibelle(getSession(), intRole.getMotifFin()),
                                getStyleListLeft());
                    } else {
                        this.createCell("", getStyleListLeft());
                    }
                } else {
                    this.createCell("", getStyleListLeft());
                }

                // Solde
                Double dMontant = new Double(compteAnnexeCC.getSolde());
                this.createCell(dMontant.doubleValue(), false);
                fTotal.add(compteAnnexeCC.getSolde());
                nbCas++;

            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            process.incProgressCounter();
        }
        // Création de la lgine de totalisation
        initTotalRow(nbCas, new Double(fTotal.toString()).doubleValue());

        return currentSheet;
    }
}
