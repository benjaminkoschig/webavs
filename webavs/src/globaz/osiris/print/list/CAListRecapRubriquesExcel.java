package globaz.osiris.print.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.recaprubriques.CARecapRubriquesExcel;
import globaz.osiris.db.recaprubriques.CARecapRubriquesExcelManager;
import globaz.osiris.parser.CASelectBlockParser;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Crée la liste des dossiers par étape
 * 
 * @author SEL
 */
public class CAListRecapRubriquesExcel extends CAAbstractListExcel {

    private static final String NUMERO_REFERENCE_INFOROM = "0184GCA";
    private String categorie;
    private String dateValeurDebut;
    private String dateValeurFin;
    private String fromIdExterneRole;
    private String genre;
    private String idExterne;
    private BProcess process = null;
    private String role;
    private String toIdExterneRole;

    // créé la feuille xls
    public CAListRecapRubriquesExcel(BSession session, BProcess process) {
        super(session, session.getLabel("LISTE_RECAP_PARRUBRIQUE_EXCEL"), session
                .getLabel("LISTE_RECAP_PARRUBRIQUE_EXCEL"));
        this.process = process;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getDateValeurDebut() {
        return dateValeurDebut;
    }

    public String getDateValeurFin() {
        return dateValeurFin;
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    public String getGenre() {
        return genre;
    }

    public String getIdExterne() {
        return idExterne;
    }

    @Override
    public String getNumeroInforom() {
        return CAListRecapRubriquesExcel.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return the process
     */
    public BProcess getProcess() {
        return process;
    }

    public String getRole() {
        return role;
    }

    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    /**
     * Critères
     * 
     * @author: sel Créé le : 20 oct. 06
     * @param sheet
     */
    private void initCritere() {
        //

        if (!JadeStringUtil.isBlank(getRole())) {
            createRow();
            this.createCell(getSession().getLabel("ROLE"), getStyleCritereTitle());
            if ((getRole() != null) && (getRole().indexOf(',') == -1)) {
                try {
                    this.createCell(CACodeSystem.getLibelle(getSession(), getRole()), getStyleCritere());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                this.createCell(getSession().getLabel("TOUS"), getStyleCritere());
            }
        }

        if (!JadeStringUtil.isBlank(getGenre())) {
            createRow();
            this.createCell(getSession().getLabel("GENRE"), getStyleCritereTitle());
            if (getGenre().equals(CACompteAnnexe.GENRE_COMPTE_STANDARD)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_AUXILIAIRE_STANDARD),
                        getStyleCritere());
            } else {
                try {
                    FWParametersSystemCodeManager manager = CACodeSystem.getGenreComptes(getSession());
                    for (int i = 0; i < manager.size(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getGenre())) {
                            this.createCell(code.getCurrentCodeUtilisateur().getLibelle(), getStyleCritere());
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        if (!JadeStringUtil.isBlank(getCategorie())) {
            createRow();
            this.createCell(getSession().getLabel("CATEGORIE"), getStyleCritereTitle());
            if (getCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_TOUS), getStyleCritere());
            } else if (getCategorie().equals(CACompteAnnexe.CATEGORIE_COMPTE_STANDARD)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD),
                        getStyleCritere());
            } else {
                try {
                    FWParametersSystemCodeManager manager = CACodeSystem.getCategories(getSession());

                    for (int i = 0; i < manager.size(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getCategorie())) {
                            this.createCell(code.getCurrentCodeUtilisateur().getLibelle(), getStyleCritere());
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
        if (!JadeStringUtil.isBlank(getDateValeurDebut())) {
            createRow();
            this.createCell(getSession().getLabel("DATE_DEBUT"), getStyleCritereTitle());
            this.createCell(getDateValeurDebut(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(getDateValeurFin())) {
            createRow();
            this.createCell(getSession().getLabel("DATE_FIN"), getStyleCritereTitle());
            this.createCell(getDateValeurFin(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(getFromIdExterneRole())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTEANNEXE_DU"), getStyleCritereTitle());
            this.createCell(getFromIdExterneRole(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(getToIdExterneRole())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTEANNEXE_AU"), getStyleCritereTitle());
            this.createCell(getToIdExterneRole(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(getIdExterne())) {
            createRow();
            this.createCell(getSession().getLabel("RUBRIQUE"), getStyleCritereTitle());
            this.createCell(getIdExterne(), getStyleCritere());
        }
    }

    /**
     * Création de la page
     * 
     * @return
     */
    private void initListe() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("ADRESSE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("MONTANT"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("MASSE"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("LISTE_RECAP_PARRUBRIQUE_ANCIEN_NUMERO_AFFILIE"),
                getStyleListTitleLeft()));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListRecapRubriquesExcel.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // Compte
        // Annexe
        currentSheet.setColumnWidth((short) numCol++, (short) 8000); // Adresse
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Montant
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Masse
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // Ancien
        // numéro
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(CARecapRubriquesExcelManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        CARecapRubriquesExcel recap = null;

        // création de la page
        initListe();
        while (((recap = (CARecapRubriquesExcel) manager.cursorReadNext(statement)) != null) && (!recap.isNew())
                && !getProcess().isAborted()) {
            // parcours du manager et remplissage des cell
            if (recap != null) {
                // Recherche du compte annexe pour obtenir le motif de blocage
                // CACompteAnnexe compte = (CACompteAnnexe)
                // recap.getCompteAnnexe();

                createRow();
                this.createCell(recap.getRoleDescription() + " " + recap.getIdExterneRole(),
                        getStyleListVerticalAlignTopLeft()); // Compte Annexe
                this.createCell(recap.getAdresseTiers(), getStyleListLeft()); // Adresse
                this.createCell(JadeStringUtil.parseDouble(recap.getSumMontant(), 0), false); // Montant
                this.createCell(JadeStringUtil.parseDouble(recap.getSumMasse(), 0), false); // Masse
                this.createCell(recap.getAncienNumeroAffilie(), getStyleListLeft()); // Ancien
                // numéro
            }
            getProcess().incProgressCounter();
        }
        return currentSheet;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setDateValeurDebut(String dateValeurDebut) {
        this.dateValeurDebut = dateValeurDebut;
    }

    public void setDateValeurFin(String dateValeurFin) {
        this.dateValeurFin = dateValeurFin;
    }

    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * @param process
     *            the process to set
     */
    public void setProcess(BProcess process) {
        this.process = process;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToIdExterneRole(String toIdExterneRole) {
        this.toIdExterneRole = toIdExterneRole;
    }
}
