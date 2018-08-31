package globaz.aquila.db.rdp.cashin.importer;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class CashinImportDataPaiement {
    public static final Integer NOMBRE_DE_CHAMPS = 15;
    private String[] data_original;
    private Integer noLot;
    private String identification;
    private Date dateExport;
    private Integer noLigne;
    private Date dateEcriture;
    private String noExtMandataire;
    private String description;
    private Montant montant;
    private Integer typeEcriture;
    private String noSociete;
    private String noExtSociete;
    private String noExtDebiteur;
    private String noExtLitige;
    private String noCreance;
    private String noExtLitigeSansType;
    private String noExtLitigeType;
    private String modeSaisie;
    private CashinTypePaiement typePaiement;

    public CashinImportDataPaiement(String[] data) {
        data_original = data;
        noLot = StringToInteger(data[0]);
        identification = normalizeString(data[1]);
        dateExport = StringToDate(data[2]);
        noLigne = StringToInteger(data[3]);
        dateEcriture = StringToDate(data[4]);
        noExtMandataire = normalizeString(data[5]);
        description = normalizeString(data[6]);
        montant = StringToMontant(data[7]);
        typeEcriture = StringToInteger(data[8]);
        noSociete = normalizeString(data[9]);
        noExtDebiteur = normalizeString(data[10]);
        noExtLitige = normalizeString(data[11]);
        String[] noExtSplitted = noExtLitige.split("-");
        noExtLitigeSansType = noExtSplitted[0];
        noExtLitigeType = noExtSplitted[1];
        noCreance = normalizeString(data[12]);
        modeSaisie = normalizeString(data[13]);
        typePaiement = CashinTypePaiement.fromValue(normalizeString(data[14]));
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

            Montant montant = new Montant(s_data).negate();
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

    public Date getDateExport() {
        return dateExport;
    }

    public Integer getNoLigne() {
        return noLigne;
    }

    public Date getDateEcriture() {
        return dateEcriture;
    }

    public String getNoExtMandataire() {
        return noExtMandataire;
    }

    public String getDescription() {
        return description;
    }

    public Montant getMontant() {
        return montant;
    }

    public Integer getTypeEcriture() {
        return typeEcriture;
    }

    public String getNoSociete() {
        return noSociete;
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

    public String getNoCreance() {
        return noCreance;
    }

    public String getNoExtLitigeSansType() {
        return noExtLitigeSansType;
    }

    public String getNoExtLitigeType() {
        return noExtLitigeType;
    }

    public String getModeSaisie() {
        return modeSaisie;
    }

    public CashinTypePaiement getTypePaiement() {
        return typePaiement;
    }
}
