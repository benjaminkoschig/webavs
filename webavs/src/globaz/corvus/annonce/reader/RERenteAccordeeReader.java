package globaz.corvus.annonce.reader;

import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

public class RERenteAccordeeReader extends REAbstractBEntityValueReader {

    /**
     * Format numérique en DB, pas de formatage particulier dans RERenteAccordee
     * 
     * @param montantRenteOrdinaireRemplace
     * @return
     */
    public Integer readMontantRenteOrdinaireRemplace(String montantRenteOrdinaireRemplace) {
        Integer result = null;
        if (montantRenteOrdinaireRemplace != null) {
            if (montantRenteOrdinaireRemplace.contains(".")) {
                String[] values = montantRenteOrdinaireRemplace.split("\\.");
                if (values.length == 2) {
                    result = convertToInteger(values[0]);
                }
            } else {
                result = convertToInteger(montantRenteOrdinaireRemplace);
            }
        }
        return result;
    }

    /**
     * Un id numérique
     * 
     * @param idTiersBeneficiaire
     * @return
     */
    public long convertIdTiers(String idTiersBeneficiaire) {
        return convertToInteger(idTiersBeneficiaire);
    }

    /**
     * Format nombre entier
     * 
     * @param codeEtatCivil
     * @return
     */
    public Integer convertEtatCivil(String codeEtatCivil) {
        return convertToInteger(codeEtatCivil);
    }

    /**
     * Format booléen
     * Cas particulier, s'il n'y a pas de valeur dans la rente accordée, on par du postulat que ce n'est pas un réfugié
     * 
     * @param codeRefugie
     * @return
     */
    public Boolean convertIsRefugie(String codeRefugie) {
        Boolean result = Boolean.FALSE;
        if (!JadeStringUtil.isBlank(codeRefugie)) {
            if ("1".equals(codeRefugie)) {
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    /**
     * Valeur entière
     * 
     * @param codePrestation
     * @return
     */
    public Integer convertGenrePrestation(String codePrestation) {
        return convertToInteger(codePrestation);
    }

    /**
     * Valeur entière
     * 
     * @param codeCasSpecial
     * @return
     */
    public Integer convertCasSpecial(String codeCasSpecial) {
        return convertToInteger(codeCasSpecial);
    }

    /**
     * REREACC.YLDRAJ
     * Format en DB AAAAMM
     * 
     * @param dateRevocationAjournement
     * @return
     */
    public JADate convertDateRevocationAjournement(String dateRevocationAjournement) {
        JADate result = null;
        if (JadeDateUtil.isGlobazDateMonthYear(dateRevocationAjournement)) {
            result = convertToJADate(dateRevocationAjournement);
        }
        return result;
    }

    /**
     * REREACC.YLNRFG
     * Format sur 2 positions, réduction en %
     * 
     * @param reductionFauteGrave
     * @return
     */
    public Integer convertReduction(String reductionFauteGrave) {
        Integer result = null;
        if (JadeNumericUtil.isInteger(reductionFauteGrave)) {
            if (reductionFauteGrave.length() < 3) {
                result = convertToInteger(reductionFauteGrave);
            }
        }
        return result;
    }

    /**
     * Format attendu booléen
     * 
     * @param isSurivant
     * @return
     */
    public Boolean convertIsSurvivant(String isSurivant) {
        return convertToBoolean(isSurivant);
    }

    /**
     * REREACC.YLNDAJ
     * La dureeAjournement doit être au format x.xx (x est un nombre)
     * 
     * @param dureeAjournement
     * @return
     */
    public Integer convertDureeAjournementValeurEntiere(String dureeAjournement) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(dureeAjournement)) {
            String[] valeurs = dureeAjournement.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[0])) {
                result = convertToInteger(valeurs[0]);
            }
        }
        return result;
    }

    /**
     * REREACC.YLNDAJ
     * La dureeAjournement doit être au format x.xx (x est un nombre)
     * 
     * @param dureeAjournement
     * @return
     */
    public Integer convertDureeAjournementValeurDecimal(String dureeAjournement) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(dureeAjournement)) {
            String[] valeurs = dureeAjournement.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[1])) {
                result = convertToInteger(valeurs[1]);
            }
        }
        return result;
    }

    /**
     * REREACC.YLDDAN
     * Format AAAAMM
     * Format BEntity MM.AAAA
     * 
     * @param dateDebutAnticipation
     * @return
     * @throws IllegalArgumentException
     */
    public JADate convertDateDebutAnticipation(String dateDebutAnticipation) throws IllegalArgumentException {
        JADate result = null;
        if (JadeDateUtil.isGlobazDateMonthYear(dateDebutAnticipation)) {
            result = convertToJADate(dateDebutAnticipation);
        }
        return result;
    }

    /**
     * REREACC.YLLAAN
     * Format numérique sur 1 position
     * 
     * @param anneeAnticipation
     * @return
     */
    public Integer convertNbreAnneeAnticipation(String anneeAnticipation) {
        return convertToInteger(anneeAnticipation);
    }

    /**
     * REREACC.YLMRAN
     * Simple montant entier (ex : 212)
     * 
     * @param montantReducationAnticipation
     * @return
     */
    public Integer convertReductionAnticipation(String montantReducationAnticipation) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(montantReducationAnticipation)) {
            if (montantReducationAnticipation.contains(".")) {
                String[] valeurs = montantReducationAnticipation.split("\\.");
                if (valeurs.length == 2) {
                    result = convertToInteger(valeurs[0]);
                }
            } else {
                result = convertToInteger(montantReducationAnticipation);
            }
        }
        return result;
    }

    /**
     * REREACC.YLMSAJ
     * Simple montant entier (ex : 212)
     * 
     * @param supplementAjournement
     * @return
     */
    public Integer convertSupplementAjournement(String supplementAjournement) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(supplementAjournement)) {
            if (supplementAjournement.contains(".")) {
                String[] valeurs = supplementAjournement.split("\\.");
                if (valeurs.length == 2) {
                    result = convertToInteger(valeurs[0]);
                }
            } else {
                result = convertToInteger(supplementAjournement);
            }
        }
        return result;
    }
}
