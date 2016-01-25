package ch.globaz.vulpecula.process.annoncesalaries;

import globaz.globall.db.BSession;
import java.io.FileNotFoundException;
import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.api.csv.CSVList;

public class AnnonceSalarieExcel extends CSVList {
    private static final String CS_ALLEMAND = "503002";
    private static final String CS_FRANCAIS = "503001";
    private static final String CS_FEMME = "516002";
    private static final String CS_HOMME = "516001";
    private static final int MAX_LENGTH_NNSS = 13;
    private static final int MAX_LENGTH_LANGUE = 3;
    private static final int MAX_LENGTH_SEXE = 1;
    private static final int MAX_LENGTH_PRENOM_BENEFICIAIRE = 15;
    private static final int MAX_LENGTH_NOM_BENEFICIAIRE = 20;
    private static final int MAX_LENGTH_DATE = 8;
    private static final int MAX_LENGTH_DESCRIPTION_EMPLOYEUR = 30;
    private static final int MAX_LENGTH_NO_AFFILIE = 13;
    private static final int MAX_LENGTH_DESCRIPTION_CONVENTION = 30;
    private static final int MAX_LENGTH_NO_CONVENTION = 2;

    private static final String H_NO_CONVENTION = "UANOCV";
    private static final String H_CONVENTION = "ULDESI";
    private static final String H_NO_AFFILIE = "MALNAF";
    private static final String H_RAISON_SOCIALE = "RANOEM";
    private static final String H_DATE_ENTREE = "UKDTEN";
    private static final String H_NOM = "RANOBE";
    private static final String H_PRENOM = "RAPRBE";
    private static final String H_SEXE = "RASEBE";
    private static final String H_CODE_LANGUE = "UILANG";
    private static final String H_NSS = "UKANAF";
    private static final String H_DATE_NAISSANCE = "UKDTNA";

    private List<PosteTravail> postes;

    public AnnonceSalarieExcel(List<PosteTravail> postes, BSession session, String filenameRoot)
            throws FileNotFoundException {
        super(session, filenameRoot);
        this.postes = postes;
    }

    @Override
    public void createContent() {
        createCell(H_NO_CONVENTION);
        createCell(H_CONVENTION);
        createCell(H_NO_AFFILIE);
        createCell(H_RAISON_SOCIALE);
        createCell(H_DATE_ENTREE);
        createCell(H_NOM);
        createCell(H_PRENOM);
        createCell(H_SEXE);
        createCell(H_CODE_LANGUE);
        createCell(H_NSS);
        createCell(H_DATE_NAISSANCE);
        createRow();

        for (PosteTravail posteTravail : postes) {
            createCellFormat(posteTravail.getConventionNo(), MAX_LENGTH_NO_CONVENTION);
            createCellFormat(posteTravail.getDesignationConvention(), MAX_LENGTH_DESCRIPTION_CONVENTION);
            createCellFormat(posteTravail.getAffilieNumero(), MAX_LENGTH_NO_AFFILIE);
            createCellFormat(posteTravail.getRaisonSocialeEmployeur(), MAX_LENGTH_DESCRIPTION_EMPLOYEUR);
            createCellFormat(unformat(posteTravail.getPeriodeActivite().getDateDebutAsSwissValue()), MAX_LENGTH_DATE);
            createCellFormat(posteTravail.getTravailleur().getDesignation1(), MAX_LENGTH_NOM_BENEFICIAIRE);
            createCellFormat(posteTravail.getTravailleur().getDesignation2(), MAX_LENGTH_PRENOM_BENEFICIAIRE);
            createCellFormat(turnIntoSexe(posteTravail.getTravailleur().getSexe()), MAX_LENGTH_SEXE);
            createCellFormat(turnIntoLangue(posteTravail.getTravailleur().getLangue()), MAX_LENGTH_LANGUE);
            createCellFormat(prepareNumAVS(posteTravail.getTravailleur().getNumAvsActuel()), MAX_LENGTH_NNSS);
            createCellFormat(unformat(posteTravail.getTravailleur().getDateNaissance()), MAX_LENGTH_DATE);
            createRow();
        }
    }

    private String prepareNumAVS(String numAvsBrut) {
        return unformat(checkNumAvs(numAvsBrut));
    }

    private String checkNumAvs(String numAvsActuel) {
        if (numAvsActuel == null || numAvsActuel.length() == 0) {
            return "0";
        }
        return numAvsActuel;
    }

    private String turnIntoSexe(String codeSysteme) {
        if (CS_HOMME.equals(codeSysteme)) {
            return "H";
        } else if (CS_FEMME.equals(codeSysteme)) {
            return "F";
        } else {
            return "?";
        }
    }

    private String turnIntoLangue(String codeSysteme) {
        if (CS_FRANCAIS.equals(codeSysteme)) {
            return "F  ";
        } else if (CS_ALLEMAND.equals(codeSysteme)) {
            return "A  ";
        } else {
            return "?";
        }
    }
}
