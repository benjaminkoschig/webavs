package ch.globaz.vulpecula.process.comptabilite;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
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
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListRecapParRubriqueExcel extends AbstractListExcel {

    private List<String> fromIdExternes = new ArrayList<String>();
    private CARecapRubriquesExcelManager manager = new CARecapRubriquesExcelManager();

    public ListRecapParRubriqueExcel(BSession session, String filenameRoot, String documentTitle, List<String> list) {
        super(session, filenameRoot, documentTitle);
        fromIdExternes = list;
    }

    public CARecapRubriquesExcelManager getManager() {
        return manager;
    }

    public void setManager(CARecapRubriquesExcelManager manager) {
        this.manager = manager;
    }

    @Override
    public void createContent() {
        boolean isAuMoins1Sheet = false;
        for (String idExterne : fromIdExternes) {
            manager.setFromIdExterne(idExterne);
            try {
                manager.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                getSession().getCurrentThreadTransaction().addErrors(e.getMessage());
            }

            if (manager.size() > 0) {
                createASheet(manager, idExterne);
                isAuMoins1Sheet = true;
            }
        }
        if (!isAuMoins1Sheet) {
            createSheet("vide");
        }
    }

    private void initCritere(CARecapRubriquesExcelManager managerForCritere) {

        if (!JadeStringUtil.isBlank(managerForCritere.getForSelectionRole())) {
            createRow();
            this.createCell(getSession().getLabel("ROLE"), getStyleCritereTitle());
            if ((managerForCritere.getForSelectionRole() != null)
                    && (managerForCritere.getForSelectionRole().indexOf(',') == -1)) {
                try {
                    this.createCell(CACodeSystem.getLibelle(getSession(), managerForCritere.getForSelectionRole()),
                            getStyleCritere());
                } catch (Exception e) {
                    // Do nothing
                }
            } else {
                this.createCell(getSession().getLabel("TOUS"), getStyleCritere());
            }
        }

        if (!JadeStringUtil.isBlank(managerForCritere.getForIdGenreCompte())) {
            createRow();
            this.createCell(getSession().getLabel("GENRE"), getStyleCritereTitle());
            if (managerForCritere.getForIdGenreCompte().equals(CACompteAnnexe.GENRE_COMPTE_STANDARD)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_AUXILIAIRE_STANDARD),
                        getStyleCritere());
            } else {
                try {
                    FWParametersSystemCodeManager managerFinder = CACodeSystem.getGenreComptes(getSession());
                    for (int i = 0; i < managerFinder.size(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) managerFinder.getEntity(i);
                        if (code.getIdCode().equals(managerForCritere.getForIdGenreCompte())) {
                            this.createCell(code.getCurrentCodeUtilisateur().getLibelle(), getStyleCritere());
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        if (!JadeStringUtil.isBlank(managerForCritere.getForIdCategorie())) {
            createRow();
            this.createCell(getSession().getLabel("CATEGORIE"), getStyleCritereTitle());
            if (managerForCritere.getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_TOUS), getStyleCritere());
            } else if (managerForCritere.getForIdCategorie().equals(CACompteAnnexe.CATEGORIE_COMPTE_STANDARD)) {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD),
                        getStyleCritere());
            } else {
                try {
                    FWParametersSystemCodeManager managerFinder = CACodeSystem.getCategories(getSession());

                    for (int i = 0; i < managerFinder.size(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) managerFinder.getEntity(i);
                        if (code.getIdCode().equals(managerForCritere.getForIdCategorie())) {
                            this.createCell(code.getCurrentCodeUtilisateur().getLibelle(), getStyleCritere());
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
        if (!JadeStringUtil.isBlank(managerForCritere.getFromDateValeur())) {
            createRow();
            this.createCell(getSession().getLabel("DATE_DEBUT"), getStyleCritereTitle());
            this.createCell(new Date(managerForCritere.getFromDateValeur()).getSwissValue(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(managerForCritere.getToDateValeur())) {
            createRow();
            this.createCell(getSession().getLabel("DATE_FIN"), getStyleCritereTitle());
            this.createCell(new Date(managerForCritere.getToDateValeur()).getSwissValue(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(managerForCritere.getFromIdExterneRole())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTEANNEXE_DU"), getStyleCritereTitle());
            this.createCell(managerForCritere.getFromIdExterneRole(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(managerForCritere.getToIdExterneRole())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTEANNEXE_AU"), getStyleCritereTitle());
            this.createCell(managerForCritere.getToIdExterneRole(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(managerForCritere.getFromIdExterne())) {
            createRow();
            this.createCell(getSession().getLabel("RUBRIQUE"), getStyleCritereTitle());
            this.createCell(managerForCritere.getFromIdExterne(), getStyleCritere());
        }

    }

    private void createASheet(CARecapRubriquesExcelManager managerRecap, String titre) {
        if (titre == null || titre.isEmpty()) {
            titre = "Liste";
        }
        HSSFSheet sheet = createSheet(titre);

        sheet.setColumnWidth((short) 0, (short) 7500);
        sheet.setColumnWidth((short) 1, (short) 10000);
        sheet.setColumnWidth((short) 2, (short) 3000);
        sheet.setColumnWidth((short) 3, (short) 3000);
        sheet.setColumnWidth((short) 4, (short) 3000);

        initCritere(managerRecap);
        setTitle();

        for (int i = 0; i < managerRecap.size(); i++) {
            CARecapRubriquesExcel recap = (CARecapRubriquesExcel) managerRecap.get(i);
            createRow();
            createCell(recap.getRoleDescription() + " " + recap.getIdExterneRole(), getStyleListVerticalAlignTopLeft()); // Compte
            createCell(recap.getAdresseTiers(), getStyleListLeft()); // Adresse
            createCell(JadeStringUtil.parseDouble(recap.getSumMontant(), 0), false); // Montant
            createCell(JadeStringUtil.parseDouble(recap.getSumMasse(), 0), false); // Masse
            createCell(recap.getAncienNumeroAffilie(), getStyleListLeft()); // Ancien
        }

    }

    private void setTitle() {
        createRow();
        createCell("Compte Annexe", getStyleListTitleLeft());
        createCell("Adresse", getStyleListTitleLeft());
        createCell("Montant", getStyleListTitleRight());
        createCell("Masse", getStyleListTitleRight());
        createCell("Ancien n° d'affilié", getStyleListTitleLeft());
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_RECAP_PAR_RUBRIQUE_TYPE_NUMBER;
    }

}
