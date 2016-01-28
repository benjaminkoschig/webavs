package globaz.osiris.print.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.comptes.CATypeSectionManager;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.db.contentieux.CASectionsBloqueesContentieuxManager;
import globaz.osiris.parser.CASelectBlockParser;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author sel Créé le 19 sept. 06
 */
public class CAListSectionsBloqueesContentieux extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0146GCA";
    private Boolean blocageInactif = new Boolean(false);
    private String forIdCategorie;

    private String forIdGenreCompte;
    private String forIdTypeSection = "";
    private String forSelectionCompte;
    private String forSelectionRole;
    private String forSelectionTriCA;
    private String forSelectionTriSection;
    private String idContMotifBloque;
    private String idExterneRole = "";
    private String idMotConSus;

    private boolean isAquila = false;
    private CAMotifContentieux motifContentieux = null; // Aquila
    private BProcess processAppelant = null;
    private BTransaction t; // Utilisé dans : setCriteres
    private String user = "";

    // créé la feuille xls
    public CAListSectionsBloqueesContentieux(BSession session) {
        super(session, session.getLabel("SECBC_TITRE"), session.getLabel("SECBC_TITRE"));
    }

    /**
     * @author: sel Créé le : 6 oct. 06
     * @return
     */
    public Boolean getBlocageInactif() {
        return blocageInactif;
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @author: sel Créé le : 21 sept. 06
     * @return
     */
    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * @return
     */
    public String getForSelectionCompte() {
        return forSelectionCompte;
    }

    /**
     * @author: sel Créé le : 27 sept. 06
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @return
     */
    public String getForSelectionTriCA() {
        return forSelectionTriCA;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @return
     */
    public String getForSelectionTriSection() {
        return forSelectionTriSection;
    }

    /**
     * @author: sel Créé le : 6 oct. 06
     * @return
     */
    public String getIdContMotifBloque() {
        return idContMotifBloque;
    }

    /**
     * @return
     */
    public String getIdMotConSus() {
        return idMotConSus;
    }

    @Override
    public String getNumeroInforom() {
        return CAListSectionsBloqueesContentieux.NUMERO_REFERENCE_INFOROM;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public String getUser() {
        return user;
    }

    /**
     * Critères
     * 
     * @author: sel Créé le : 20 oct. 06
     * @param sheet
     */
    private void initCritere() {
        FWParametersUserCode ucMotifContentieuxSuspendu;
        // Sections
        if (!JadeStringUtil.isBlank(getForIdTypeSection()) && !getForIdTypeSection().equals("0")) {
            createRow();
            this.createCell(getSession().getLabel("SECTION"), getStyleCritereTitle());
            CATypeSection tempTypeSection;
            CATypeSectionManager manTypeSection = new CATypeSectionManager();
            manTypeSection.setSession(getSession());
            try {
                manTypeSection.find(t);
                if (!getForIdTypeSection().equalsIgnoreCase("1000")) {
                    for (int i = 0; i < manTypeSection.size(); i++) {
                        tempTypeSection = (CATypeSection) manTypeSection.getEntity(i);
                        if (tempTypeSection.getIdTypeSection().equalsIgnoreCase(getForIdTypeSection())) {
                            this.createCell(tempTypeSection.getDescription(), getStyleCritere());
                            i = manTypeSection.size(); // Pour sortir de la
                            // boucle
                        }
                    }
                } else {
                    this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_TOUS), getStyleCritere());
                }
            } catch (Exception e) {
                JadeCodingUtil.catchException(this, "setCriteres", e);
            }
        }

        // Rôle
        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            createRow();
            this.createCell(getSession().getLabel("ROLE"), getStyleCritereTitle());
            try {
                StringBuffer ssTitle = new StringBuffer();
                if (getForSelectionRole().indexOf(',') != -1) {
                    String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);
                    for (int id = 0; id < roles.length; ++id) {
                        if (id > 0) {
                            ssTitle.append(',');
                        }
                        ssTitle.append(CACodeSystem.getLibelle(getSession(), roles[id]));
                    }
                } else {
                    ssTitle.append(CACodeSystem.getLibelle(getSession(), getForSelectionRole()));
                }
                this.createCell(ssTitle.toString(), getStyleCritere());
            } catch (Exception e) {
                JadeCodingUtil.catchException(this, "setCriteres", e);
            }
        }

        // Tri CA
        if (!JadeStringUtil.isBlank(getForSelectionTriCA()) && !getForSelectionTriCA().equals("0")) {
            createRow();
            this.createCell(getSession().getLabel("PREMIERTRI"), getStyleCritereTitle());
            if (getForSelectionTriCA().equalsIgnoreCase("1")) {
                this.createCell(getSession().getLabel("TRICOMPTEANNEXENUMERO"), getStyleCritere());
            } else {
                this.createCell(getSession().getLabel("TRICOMPTEANNEXENOM"), getStyleCritere());
            }
        }

        // Tri Section
        if (!JadeStringUtil.isBlank(getForSelectionTriSection()) && !getForSelectionTriSection().equals("0")) {
            createRow();
            this.createCell(getSession().getLabel("DEUXIEMETRI"), getStyleCritereTitle());
            if (getForSelectionTriSection().equalsIgnoreCase("1")) {
                this.createCell(getSession().getLabel("TRISECTIONNUMERO"), getStyleCritere());
            } else {
                this.createCell(getSession().getLabel("TRISECTIONDATE"), getStyleCritere());
            }
        }

        // Genre
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            createRow();
            this.createCell(getSession().getLabel("GENRE"), getStyleCritereTitle());
            if (!getForIdGenreCompte().equals("0")) {
                try {
                    this.createCell(CACodeSystem.getLibelle(getSession(), getForIdGenreCompte()), getStyleCritere());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.createCell(getSession().getLabel("COMPTE_ANNEXE_CATEGORIE_STANDARD"), getStyleCritere());
            }
        }

        // Catégories
        if (!JadeStringUtil.isBlank(getForIdCategorie())) {
            createRow();
            this.createCell(getSession().getLabel("CATEGORIE"), getStyleCritereTitle());
            if (getForIdCategorie().equalsIgnoreCase(CACompteAnnexeManager.ALL_CATEGORIE)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_TOUS), getStyleCritere());
            } else if (getForIdCategorie().equalsIgnoreCase(CACompteAnnexe.CATEGORIE_COMPTE_STANDARD)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD),
                        getStyleCritere());
            } else {
                FWParametersSystemCodeManager manager;
                try {
                    manager = CACodeSystem.getCategories(getSession());
                    for (int i = 0; i < manager.size(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getForIdCategorie())) {
                            this.createCell(code.getCurrentCodeUtilisateur().getLibelle(), getStyleCritere());
                            i = manager.size(); // Pour sortir de la boucle
                        }
                    }
                } catch (Exception e) {
                    JadeCodingUtil.catchException(this, "setCriteres", e);
                }
            }
        }

        // Motif Ancien contentieux
        if (!JadeStringUtil.isBlank(getIdMotConSus())) {
            createRow();
            this.createCell(getSession().getLabel("MOTIF"), getStyleCritereTitle());
            try {
                ucMotifContentieuxSuspendu = new FWParametersUserCode();
                ucMotifContentieuxSuspendu.setSession(getSession());
                ucMotifContentieuxSuspendu.setIdCodeSysteme(getIdMotConSus());
                ucMotifContentieuxSuspendu.setIdLangue(getSession().getIdLangue());
                ucMotifContentieuxSuspendu.retrieve();
                this.createCell(ucMotifContentieuxSuspendu.getCodeUtiLib(), getStyleCritere());
            } catch (Exception e) {
                JadeCodingUtil.catchException(this, "setCriteres", e);
            }
        }
        // Motif Nouveau contentieux (Aquila)
        if (!JadeStringUtil.isBlank(getIdContMotifBloque())) {
            createRow();
            this.createCell(getSession().getLabel("MOTIF"), getStyleCritereTitle());
            try {
                ucMotifContentieuxSuspendu = new FWParametersUserCode();
                ucMotifContentieuxSuspendu.setSession(getSession());
                ucMotifContentieuxSuspendu.setIdCodeSysteme(getIdContMotifBloque());
                ucMotifContentieuxSuspendu.setIdLangue(getSession().getIdLangue());
                ucMotifContentieuxSuspendu.retrieve();
                this.createCell(ucMotifContentieuxSuspendu.getCodeUtiLib(), getStyleCritere());
            } catch (Exception e) {
                JadeCodingUtil.catchException(this, "setCriteres", e);
            }
        }

        // avec blocage inactif
        createRow();
        this.createCell(getSession().getLabel("CAMBC_BLOCAGE"), getStyleCritereTitle());
        this.createCell(getBlocageInactif().booleanValue(), getStyleCritere());
    }

    /**
     * Création de la page ("Liste des décisions d'intérêts")
     * 
     * @return
     */
    private void initListe() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("SECTION")));
        colTitles.add(new ParamTitle(getSession().getLabel("MOTIF")));
        if (isAquila) {
            colTitles.add(new ParamTitle(getSession().getLabel("DATE_DEBUT")));
            colTitles.add(new ParamTitle(getSession().getLabel("DATE_FIN")));
            colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));
            colTitles.add(new ParamTitle(getSession().getLabel("COMMENTAIRE")));
        } else {
            colTitles.add(new ParamTitle(getSession().getLabel("DATE_FIN")));
            colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));
        }

        createSheet(getSession().getLabel("LISTE"));
        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListSectionsBloqueesContentieux.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION); // Compte
        // Annexe
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION); // Sections
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_REMARQUE); // Motif
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DATE); // Date
        // de
        // début
        // (Aquila)
        // //
        // Date
        // de
        // fin
        // sinon
        if (isAquila) {
            currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DATE); // Date
            // de
            // fin
            currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Solde
            currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_REMARQUE); // Commentaire
        } else {
            currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Solde
        }
    }

    /**
     * @author: sel Créé le : 20 oct. 06
     * @param numPremiereLigne
     */
    private void initTotal(int numPremiereLigne) {
        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListTitleLeft());
        // 3 Cellules vide
        this.createCell("", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());

        // solde total
        if (isAquila) {
            // Cellule vide
            this.createCell("", getStyleListTitleLeft());
            createCellFormula("SUM(F" + numPremiereLigne + ":F" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                    getStyleMontantTotal());
            // Cellule vide
            this.createCell("", getStyleListTitleLeft());
        } else {
            createCellFormula("SUM(E" + numPremiereLigne + ":E" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                    getStyleMontantTotal());
        }
    }

    /**
     * @return
     */
    public Boolean isBlocageInactif() {
        return blocageInactif;
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(CASectionsBloqueesContentieuxManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CASection section = null;
        int numPremiereLigne = 0;
        isAquila = false;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());

        // création de la page
        initListe();
        // récupération du numéro de la première ligne
        numPremiereLigne = currentSheet.getPhysicalNumberOfRows() + 1;

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((section = (CASection) manager.cursorReadNext(statement)) != null) && (!section.isNew())) {
            if (section != null) {
                createRow();
                // Compte annexe
                if (!idExterneRole.equalsIgnoreCase(section.getCompteAnnexe().getIdExterneRole())) {
                    idExterneRole = section.getCompteAnnexe().getIdExterneRole();
                    CACompteAnnexe compteAnnexe = (CACompteAnnexe) section.getCompteAnnexe();
                    String sCompteAnnexe = compteAnnexe.getCARole().getDescription() + " "
                            + compteAnnexe.getIdExterneRole() + " " + compteAnnexe.getDescription();
                    this.createCell(sCompteAnnexe, getStyleListLeft());
                } else {
                    this.createCell("", getStyleListLeft());
                }
                // Section
                this.createCell(section.getIdExterne() + " " + section.getDescription(), getStyleListLeft());
                // Motif
                this.createCell(section.getUcMotifContentieuxSuspendu().getCodeUtiLib(), getStyleListLeft());
                // Date fin
                this.createCell(section.getDateSuspendu(), getStyleListLeft());
                // Solde
                this.createCell(JadeStringUtil.parseDouble(section.getSolde(), 0), false);

                processAppelant.incProgressCounter();
            }
        }
        initTotal(numPremiereLigne);

        return currentSheet;
    }

    /**
     * Initialisation de la feuille xls Pour le nouveau contentieux (Aquila)
     */
    public HSSFSheet populateSheetListeAquila(CASectionsBloqueesContentieuxManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CASection section = null;
        int numPremiereLigne = 0;
        isAquila = true;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        t = transaction;
        // création de la page
        initListe();
        // récupération du numéro de la première ligne
        numPremiereLigne = currentSheet.getPhysicalNumberOfRows() + 1;

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((section = (CASection) manager.cursorReadNext(statement)) != null) && (!section.isNew())) {
            if (section != null) {
                CAMotifContentieuxManager motifMgr = new CAMotifContentieuxManager();
                motifMgr.setSession(getSession());
                motifMgr.setForIdSection(section.getIdSection());
                motifMgr.setForIdMotifBlocage(getIdContMotifBloque());
                motifMgr.changeManagerSize(0);
                if (!getBlocageInactif().booleanValue()) {
                    motifMgr.setFromDateBetweenDebutFin(JACalendar.todayJJsMMsAAAA());
                }
                motifMgr.find(transaction);
                if (!motifMgr.isEmpty()) {
                    for (int i = 0; i < motifMgr.size(); i++) {
                        motifContentieux = (CAMotifContentieux) motifMgr.getEntity(i);
                        createRow();

                        // Compte annexe
                        CACompteAnnexe compteAnnexe = (CACompteAnnexe) section.getCompteAnnexe();
                        String sCompteAnnexe = compteAnnexe.getCARole().getDescription() + " "
                                + compteAnnexe.getIdExterneRole() + " " + compteAnnexe.getDescription();
                        this.createCell(sCompteAnnexe, getStyleListLeft());
                        // Section
                        this.createCell(section.getIdExterne() + " " + section.getDescription(), getStyleListLeft());
                        // Motif
                        this.createCell(motifContentieux.getUcMotifContentieuxSuspendu().getCodeUtiLib(),
                                getStyleListLeft());
                        // Date début
                        this.createCell(motifContentieux.getDateDebut(), getStyleListLeft());
                        // Date Fin
                        this.createCell(motifContentieux.getDateFin(), getStyleListLeft());
                        if (i == motifMgr.size() - 1) {
                            // Solde
                            this.createCell(JadeStringUtil.parseDouble(section.getSolde(), 0), false);
                        } else {
                            this.createCell("", getStyleListLeft());
                        }
                        // Commentaire
                        this.createCell(motifContentieux.getCommentaire(), getStyleListLeft());
                    }
                }
                processAppelant.incProgressCounter();
            }
        }
        initTotal(numPremiereLigne);

        return currentSheet;
    }

    /**
     * @param boolean1
     */
    public void setBlocageInactif(Boolean b) {
        blocageInactif = b;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @author: sel Créé le : 21 sept. 06
     * @param string
     */
    public void setForIdTypeSection(String string) {
        forIdTypeSection = string;
    }

    /**
     * @param string
     */
    public void setForSelectionCompte(String string) {
        forSelectionCompte = string;
    }

    /**
     * @author: sel Créé le : 27 sept. 06
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @param string
     */
    public void setForSelectionTriCA(String string) {
        forSelectionTriCA = string;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @param string
     */
    public void setForSelectionTriSection(String string) {
        forSelectionTriSection = string;
    }

    /**
     * @author: sel Créé le : 6 oct. 06
     * @param string
     */
    public void setIdContMotifBloque(String string) {
        idContMotifBloque = string;
    }

    /**
     * @param string
     */
    public void setIdMotConSus(String string) {
        idMotConSus = string;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setUser(String string) {
        user = string;
    }
}
