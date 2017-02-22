package ch.globaz.naos.ree.domain.converter;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ree.ch.admin.bfs.xmlns.bfs_5054_000102._2.DeliverySalariesSEType;
import ch.globaz.naos.ree.domain.pojo.Pojo5054_102;
import ch.globaz.naos.ree.tools.InfoCaisse;

public class Converter5054_102 implements Converter<Pojo5054_102, DeliverySalariesSEType> {
    private static final Logger LOG = LoggerFactory.getLogger(Converter5054_102.class);
    private InfoCaisse currentCaisse;

    public Converter5054_102(InfoCaisse infoCaisse) {
        currentCaisse = infoCaisse;
    }

    @Override
    public DeliverySalariesSEType convert(Pojo5054_102 businessMessage) throws REEBusinessException {
        return convertPojos(businessMessage);
    }

    private DeliverySalariesSEType convertPojos(Pojo5054_102 pojo) throws REEBusinessException {
        DeliverySalariesSEType salaire = new DeliverySalariesSEType();

        // Num�ro de l'affilii avec ponctuation
        salaire.setAccountNumberArG(pojo.getNumeroAffilie());

        // Num�ro de la caisse AVS
        salaire.setCompensationOffice(ConverterUtils.formatCodeCaisse(currentCaisse.getNumeroCaisse() + "."
                + currentCaisse.getNumeroAgenceFormate()));

        // Nss au format Long
        salaire.setVn(ConverterUtils.formatNssInLong(pojo.getNss()));
        // Date de naissance
        try {
            salaire.setDateOfBirth(ConverterUtils.defineDatePartiel(pojo.getDateNaissance()));
        } catch (DatatypeConfigurationException e) {
            LOG.debug("[505400102 : setDateOfBirth] Erreur de conversion pour l'affili� " + pojo.getNumeroAffilie(), e);
        }
        // Sexe de l'ind�pendant
        salaire.setSex(ConverterUtils.translateSex(pojo.getSexe()));

        // Nationalit�
        salaire.setNationality(ConverterUtils.translateNationality(pojo.getPays()));

        // Mois de d�but de p�riode
        salaire.setStart(getMonth(pojo.getDateDebutPeriode(), pojo.getNumeroAffilie()));

        // Mois de fin de p�riode
        salaire.setEnd(getMonth(pojo.getDateFinPeriode(), pojo.getNumeroAffilie()));

        // Revenu positif ou revenu n�gatif
        salaire.setValidityValue(ConverterUtils.defineValidityValue(new BigDecimal(pojo.getMontant())));

        // Type d'ind�pendant, bas� sur le genre d'affili�
        salaire.setAssuredPersonType(convertGenreAffilie(pojo.getGenreAffilie()));

        // Activit� accessoire
        salaire.setActivity(defineActivity(pojo.getActivity()));

        // Revenu d�terminant
        salaire.setSalary(ConverterUtils.formatRevenu(pojo.getMontant()));

        // Type de d�cision (D�finitive ou provisoire)
        salaire.setKindOfDecision(defineTypeDecision(pojo.getGenreDecision()));

        // Date de la d�cision
        try {
            salaire.setDecisionDate(ConverterUtils.convertToXMLGregorianCalendar(pojo.getDateDecision()));
        } catch (DatatypeConfigurationException e) {
            LOG.debug("[505400102 : setDecisionDate] Erreur de conversion pour l'affili� " + pojo.getNumeroAffilie(), e);
        }

        return salaire;
    }

    private String getMonth(String date, String idAffilie) throws REEBusinessException {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }
        try {
            String mois = new SimpleDateFormat("MM").format(new SimpleDateFormat("yyyyMMdd").parse(date));
            return String.valueOf(Integer.valueOf(mois));
        } catch (Exception e) {
            throw new REEBusinessException("Erreur lors de la recherche du mois pour la date [" + date
                    + "] pour l'affili� [" + idAffilie + "] : " + e.toString());
        }
    }

    /**
     * Defini l'activit� accessoire.
     * 2 si activit� (Donc pr�sence d'id != 0)
     * 1 sinon.
     * 
     * @param activity
     * @return
     */
    static BigInteger defineActivity(String activity) {
        // TODO a tester
        if (JadeStringUtil.isBlankOrZero(activity)) {
            return new BigInteger("1");
        } else {
            return new BigInteger("2");
        }
    }

    /**
     * Permet de d�finir le type de d�cision
     * 
     * @param decision
     * @return
     */
    static String defineTypeDecision(String decision) {

        List<String> decisionDefinitives = Arrays.asList("605002", "605004", "605008", "605009");

        if (decisionDefinitives.contains(decision)) {
            return "D";
        } else {
            return "P";
        }

    }

    /**
     * Retourne le type d'affili�
     * 
     * @param typeAffilie le code syst�me du genre d'affili�
     * @return
     */
    private static BigInteger convertGenreAffilie(String typeAffilie) throws REEBusinessException {

        if (typeAffilie == null) {
            return BigInteger.ZERO;
        } else if ("602003".equals(typeAffilie)) {
            return new BigInteger("2");
        } else if ("602001".equals(typeAffilie)) {
            return new BigInteger("3");
        } else if ("602004".equals(typeAffilie)) {
            return new BigInteger("7");
        } else if ("602006".equals(typeAffilie)) {
            return new BigInteger("9");
        }
        return BigInteger.ZERO;

    }
}
