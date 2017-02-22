package ch.globaz.naos.ree.domain.converter;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ree.ch.ech.xmlns.ech_0044._2.DatePartiallyKnownType;
import ch.globaz.naos.ree.domain.CodeSystem;

class ConverterUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ConverterUtils.class);

    /**
     * Permet de retranscrire les codes système Globaz pour le sexe dans le format REE
     * 
     * @param sexeCS Un code systeme
     * @return 1 si homme, 2 si femme, vide null
     */
    protected static String translateSex(String sexeCS) {
        int value = 0;
        try {
            value = Integer.valueOf(sexeCS);
        } catch (NumberFormatException e) {
            LOG.debug("Mapping [translateSex] Impossible de récupérer la valeur numérique du CS sexe du tiers {} : {}",
                    sexeCS, e.getMessage());
        }
        switch (value) {
            case 516001:// Homme
                return "1";
            case 316000:// Homme
                return "1";
            case 516002:// Femme
                return "2";
            case 316001:// Femme
                return "2";
            default:
                return null;
        }
    }

    /**
     * Retourne le nss sous un format Long.
     * Retire donc les ".".
     * Retourne 0 si aucun NSS n'est passé en paramètre.
     * 
     * @param nss String
     * @return null si pas de nss
     */
    protected static Long formatNssInLong(String nss) throws REEBusinessException {
        if (nss != null && !nss.isEmpty() && !nss.trim().isEmpty()) {
            if (nss.substring(0, 3).equals("756")) {
                Long nssL = Long.valueOf(nss.replaceAll("\\.", "").trim());
                if (nssL > 7569999999999L || nssL <= 7560000000000L) {
                    throw new REEBusinessException("Le NSS n'est pas valide [" + nss + "].");
                }
                return nssL;
            } else {
                throw new REEBusinessException("Le NSS n'est pas valide [" + nss + "].");
            }
        }

        LOG.debug("Impossible de formater le NSS en format long -> " + nss);
        return null;
    }

    /**
     * Retourne la nationalité
     * - Si 100 (Suisse), on retourne 1
     * - Autre code sauf 999, on retourne 2
     * - Sinon on retourne 9
     * 
     * @param nationality
     * @return
     */
    protected static BigInteger translateNationality(String nationality) {

        int value = 0;
        try {
            value = Integer.valueOf(nationality);
        } catch (NumberFormatException e) {
            value = 315999;
        }

        switch (value) {
            case 100:
            case 315100:
                return new BigInteger("1");
            case 999:
            case 315999:
                return new BigInteger("9");
            case 0:
                return new BigInteger("9");
            default:
                return new BigInteger("2");
        }
    }

    /**
     * Retourne le type de revenu salarie (assuredPersonCodeIK)
     * - Si 01 (salarié) = 1
     * - Si 06 (inconnu) = 6
     * - Si 07, code spéciale 03 = 7
     * - Sinon retourne 0
     * 
     * @param genreEcriture Le genre d'écriture
     * @param codeSpeciale Le code spéciale de l'écriture
     * @return Le type de revenu
     */
    protected static BigInteger translateTypeRevenuSalarie(String genreEcriture, String codeSpeciale)
            throws REEBusinessException {

        int value = 0;
        try {
            value = Integer.valueOf(genreEcriture);
        } catch (NumberFormatException e) {
            throw new REEBusinessException("Erreur de données, impossible de définir le type de revenu salarié "
                    + genreEcriture);
        }

        switch (value) {
            case 310001:
                return new BigInteger("1");
            case 310006:
                return new BigInteger("6");
            case 310007:
                int codeSpec = 0;
                try {
                    codeSpec = Integer.valueOf(codeSpeciale);
                } catch (NumberFormatException e) {
                    throw new REEBusinessException(
                            "Erreur de données, impossibl de définir le code spéciale pour le genre de codisation 7 "
                                    + codeSpeciale);
                }
                if (codeSpec == CodeSystem.CODE_SPECIAL_SALARIE) {
                    return new BigInteger("7");
                }
            default:
                return BigInteger.ZERO;
        }
    }

    /**
     * Retourne le type de revenu indep (assuredPersonCodeSE)
     * - Si 02 (TSE) = 2
     * - Si 03 (indépendant) = 3
     * - Si 07, code spéciale 02 = 7
     * - Si 09 (agriculteur) = 9
     * 
     * @param genreEcriture Le genre d'écriture
     * @param codeSpeciale Le code spéciale de l'écriture
     * @return Le type de revenu
     */
    protected static BigInteger translateTypeRevenuIndependant(String genreEcriture, String codeSpeciale)
            throws REEBusinessException {

        int value = 0;
        try {
            value = Integer.valueOf(genreEcriture);
        } catch (NumberFormatException e) {
            throw new REEBusinessException("Erreur de données, impossible de définir le type de revenu independant "
                    + genreEcriture);
        }

        switch (value) {
            case 310002:
                return new BigInteger("2");
            case 310003:
                return new BigInteger("3");
            case 310009:
                return new BigInteger("9");
            case CodeSystem.GENRE_ECRITURE_07:
                int codeSpec = 0;
                try {
                    codeSpec = Integer.valueOf(codeSpeciale);
                } catch (NumberFormatException e) {
                    throw new REEBusinessException(
                            "Erreur de données, impossibl de définir le code spéciale pour le genre de codisation 7 "
                                    + codeSpeciale);
                }
                if (codeSpec == CodeSystem.CODE_SPECIAL_INDEPENDANT) {
                    return new BigInteger("7");
                }
            default:
                return BigInteger.ZERO;
        }
    }

    /**
     * Permet de formater le revenu determinant.
     * - Revenu sans les centimes
     * - Si code extourne, revenu négatif.
     * 
     * @param montant Un montant
     * @param codeExtourne Un code extourne
     * @return Le revenu sans les centimes
     * @throws REEBusinessException
     */
    protected static BigDecimal formatRevenu(String montant, String codeExtourne) throws REEBusinessException {

        if (JadeStringUtil.isBlank(montant)) {
            throw new REEBusinessException("Erreur de données, impossible de définir le montant du revenu " + montant);
        }

        BigDecimal revenu = new BigDecimal(montant).abs().setScale(0, RoundingMode.DOWN);

        if ("311001".equals(codeExtourne)) {
            return revenu.negate();
        }

        return revenu;
    }

    /**
     * Permet deformater le revenu determinant.
     * - Revenu sans les centimes
     * 
     * @param montant Un montant
     * @return Le revenu sans les centimes
     * @throws REEBusinessException
     */
    protected static BigDecimal formatRevenu(String montant) throws REEBusinessException {
        // TODO a tester
        if (JadeStringUtil.isBlank(montant)) {
            throw new REEBusinessException("Erreur de données, impossible de définir le montant du revenu " + montant);
        }
        BigDecimal revenu = new BigDecimal(montant).abs().setScale(0, RoundingMode.DOWN);
        return revenu;
    }

    /**
     * Méthode permettant la conversion une date Globaz dans le format {@link XMLGregorianCalendar }
     * 
     * @param yyyyMMdd Une string représentant la date sous le format yyyyMMdd
     * @return Retourne une date sous le format {@link XMLGregorianCalendar }
     * @throws DatatypeConfigurationException
     */
    protected static XMLGregorianCalendar convertToXMLGregorianCalendar(String yyyyMMdd)
            throws DatatypeConfigurationException {
        XMLGregorianCalendar returnCalendar = null;
        try {
            GregorianCalendar c = new GregorianCalendar(Integer.parseInt(yyyyMMdd.substring(0, 4)),
                    (Integer.parseInt(yyyyMMdd.substring(4, 6)) - 1), Integer.parseInt(yyyyMMdd.substring(6)));
            returnCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            returnCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnCalendar;
    }

    /**
     * Transforme le numéro de caisse formaté comme présent dans la plupart de ses représentations en DB vers le format
     * attendu pour SEDEX CCCAAA
     * 
     * @param codeCaisse formaté xxx.xxx
     * @return
     */
    protected static String formatCodeCaisse(String codeCaisse) {
        String[] codePart = codeCaisse.split(Pattern.quote("."));
        return StringUtils.stripStart(codePart[0], "0")
                + (codePart.length > 1 ? String.format("%3s", codePart[1]).replace(' ', '0') : "000");
    }

    /**
     * Définit Si le revenu est positif ou négatif
     * 
     * @param codeExtourne Le code extourne
     * @return 1 si négatif, 0 sinon
     */
    protected static BigDecimal defineValidityValue(String codeExtourne) {

        if ("311001".equals(codeExtourne)) {
            return BigDecimal.ONE;
        }

        return BigDecimal.ZERO;
    }

    /**
     * Définit Si le revenu est positif ou négatif
     * 
     * @param montant le montant
     * @return 1 si négatif, sinon 0
     * @throws REEBusinessException
     */
    protected static BigDecimal defineValidityValue(BigDecimal montant) throws REEBusinessException {
        // TODO a tester
        if (montant == null) {
            throw new REEBusinessException(
                    "Erreur de données, impossible de définir ValidityValue car le montant est null");
        }
        if (montant.intValue() < 0) {
            return BigDecimal.ONE;
        }

        return BigDecimal.ZERO;
    }

    /**
     * Permet de définir une date partiellement
     * 
     * @param date Une date sous format String
     * @return la date partielle
     * @throws DatatypeConfigurationException
     */
    protected static DatePartiallyKnownType defineDatePartiel(String date) throws DatatypeConfigurationException {

        if (date == null || date.isEmpty()) {
            return null;
        }

        DatePartiallyKnownType datePartially = new DatePartiallyKnownType();
        datePartially.setYearMonthDay(ConverterUtils.convertToXMLGregorianCalendar(date));

        return datePartially;
    }

}
