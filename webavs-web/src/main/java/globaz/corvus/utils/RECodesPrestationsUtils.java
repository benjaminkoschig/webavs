package globaz.corvus.utils;

import ch.globaz.corvus.process.attestationsfiscales.RERentePourAttestationsFiscales;
import globaz.corvus.db.attestationsFiscales.REDonneesPourAttestationsFiscales;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.jade.client.util.JadeStringUtil;

import java.text.DecimalFormat;
import java.util.Arrays;

public class RECodesPrestationsUtils {

    private static final String CODE_SYSTEM_ROOT = "52821";
    private static final String CODE_SYSTEM_RENTE_50 = "52821500";
    private static final String CODE_SYSTEM_RENTE_70 = "52821700";
    public static final String PATTERN_QUOTITE = " #.# %";

    public static String getRechercheCodeSystem(REDonneesPourAttestationsFiscales ra){
        return getRechercheCodeSystem(ra.getCodePrestation(), ra.getFractionRente(), ra.getQuotite());
    }
    public static String getRechercheCodeSystem(RERentePourAttestationsFiscales ra){
        return getRechercheCodeSystem(ra.getCodePrestation(), ra.getFractionRente(), ra.getQuotite());
    }

    /**
     * Prépare la chaîne pour retrouver le code système avec 0 ou 1 ou autres fractions selon les règles analysées
      * @param codePrestation
     * @param fractionRente
     * @param quotite
     * @return La chaîne permettant de chercher le code système
     */
    private static String getRechercheCodeSystem(String codePrestation, String fractionRente, String quotite){
        String pourRechercheCodeSysteme = CODE_SYSTEM_ROOT + codePrestation;
        if (Arrays.stream(REGenresPrestations.GENRE_PRESTATIONS_AI).anyMatch(genrePrestation -> genrePrestation.equals(codePrestation))) {
            if (!JadeStringUtil.isEmpty(fractionRente)) {
                return pourRechercheCodeSysteme + fractionRente;
            } else if (!JadeStringUtil.isEmpty(quotite)) {
                return buildCodeSystemQuotite(codePrestation, quotite, pourRechercheCodeSysteme);
            }
        }
        return  pourRechercheCodeSysteme + "0";
    }

    private static String buildCodeSystemQuotite(String codePrestation, String quotite, String pourRechercheCodeSysteme) {
        if (REGenresPrestations.GENRE_50.equals(codePrestation) || REGenresPrestations.GENRE_70.equals(codePrestation)) {
            if (Float.valueOf(quotite) >= 0.70) {
                return pourRechercheCodeSysteme + "1";
            }
            return pourRechercheCodeSysteme + "0";
        }
        return pourRechercheCodeSysteme + "1";
    }

    public static String concatQuotite(RERentePourAttestationsFiscales uneRenteDuBeneficiaire, String traductionCodePrestation, String pourRechercheCodeSysteme) {
        if ((CODE_SYSTEM_RENTE_50.equals(pourRechercheCodeSysteme) || CODE_SYSTEM_RENTE_70.equals(pourRechercheCodeSysteme))
                && !JadeStringUtil.isEmpty(uneRenteDuBeneficiaire.getQuotite())) {
            DecimalFormat dft = new DecimalFormat(PATTERN_QUOTITE);
            String quotite = dft.format(Double.valueOf(uneRenteDuBeneficiaire.getQuotite()));
            traductionCodePrestation += quotite;
        }
        return traductionCodePrestation;
    }
}
