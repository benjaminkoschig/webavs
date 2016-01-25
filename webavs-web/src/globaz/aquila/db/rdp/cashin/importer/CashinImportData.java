package globaz.aquila.db.rdp.cashin.importer;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class CashinImportData {
    public static final Integer NOMBRE_DE_CHAMPS = 15;
    private String[] data_original;
    private Integer noLot;
    private String identification;
    private String codeProvenance;
    private Date dateExport;
    private Integer noLigne;
    private Date dateFrais;
    private String noExtCreancier;
    private String description;
    private Montant montantFrais;
    private Integer noTypeFrais;
    private String libelleTypeFrais;
    private String noExtSociete;
    private String noExtDebiteur;
    private String noExtLitige;
    private String noExtCreance;
    private String noExtLitigeSansType;
    private String noExtLitigeType;

    public CashinImportData(String[] data) {
        data_original = data;
        noLot = StringToInteger(data[0]);
        identification = normalizeString(data[1]);
        codeProvenance = normalizeString(data[2]);
        dateExport = StringToDate(data[3]);
        noLigne = StringToInteger(data[4]);
        dateFrais = StringToDate(data[5]);
        noExtCreancier = normalizeString(data[6]);
        description = normalizeString(data[7]);
        montantFrais = StringToMontant(data[8]);
        noTypeFrais = StringToInteger(data[9]);
        libelleTypeFrais = normalizeString(data[10]);
        noExtSociete = normalizeString(data[11]);
        noExtDebiteur = normalizeString(data[12]);
        noExtLitige = normalizeString(data[13]);
        String[] noExtSplitted = noExtLitige.split("-");
        noExtLitigeSansType = noExtSplitted[0];
        noExtLitigeType = noExtSplitted[1];
        noExtCreance = normalizeString(data[14]);
    }

    private String normalizeString(String s_data) {
        if (s_data == null || s_data.trim().length() == 0) {
            return null;
        }

        return s_data.trim();
    }

    private Montant StringToMontant(String s_data) {
        try {
            if (s_data == null || s_data.trim().length() == 0) {
                return null;
            }

            Montant montant = new Montant(s_data);
            return montant;
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    private Date StringToDate(String s_data) {
        try {
            if (s_data == null || s_data.trim().length() == 0) {
                return null;
            }

            Date date = new Date(s_data);
            return date;
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    private Integer StringToInteger(String s_data) {
        try {
            if (s_data == null || s_data.trim().length() == 0) {
                return 0;
            }

            Integer i_data = Integer.parseInt(s_data);
            return i_data;
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    @Override
    public String toString() {
        String toStr = "";

        for (int i = 0; i < data_original.length; i++) {
            String data = "";
            if (data_original[i] == null) {
                data = "[null]";
            } else if (data_original[i].trim().length() == 0) {
                data = "[vide]";
            } else {
                data = data_original[i];
            }

            toStr += i + 1 + " : " + data + "\n";
        }

        return toStr;
    }

    public String[] getData_original() {
        return data_original;
    }

    public Integer getNoLot() {
        return noLot;
    }

    public String getIdentification() {
        return identification;
    }

    public String getCodeProvenance() {
        return codeProvenance;
    }

    public Date getDateExport() {
        return dateExport;
    }

    public Integer getNoLigne() {
        return noLigne;
    }

    public Date getDateFrais() {
        return dateFrais;
    }

    public String getNoExtCreancier() {
        return noExtCreancier;
    }

    public String getDescription() {
        return description;
    }

    public Montant getMontantFrais() {
        return montantFrais;
    }

    public Integer getNoTypeFrais() {
        return noTypeFrais;
    }

    public String getLibelleTypeFrais() {
        return libelleTypeFrais;
    }

    public String getNoExtSociete() {
        return noExtSociete;
    }

    public String getNoExtDebiteur() {
        return noExtDebiteur;
    }

    public String getNoExtLitige() {
        return noExtLitige;
    }

    public String getNoExtCreance() {
        return noExtCreance;
    }

    public String getNoExtLitigeSansType() {
        return noExtLitigeSansType;
    }

    public String getNoExtLitigeType() {
        return noExtLitigeType;
    }
}
