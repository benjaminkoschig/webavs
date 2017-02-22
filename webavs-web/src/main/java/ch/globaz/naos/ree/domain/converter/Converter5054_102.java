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

        // Numéro de l'affilii avec ponctuation
        salaire.setAccountNumberArG(pojo.getNumeroAffilie());

        // Numéro de la caisse AVS
        salaire.setCompensationOffice(ConverterUtils.formatCodeCaisse(currentCaisse.getNumeroCaisse() + "."
                + currentCaisse.getNumeroAgenceFormate()));

        // Nss au format Long
        salaire.setVn(ConverterUtils.formatNssInLong(pojo.getNss()));
        // Date de naissance
        try {
            salaire.setDateOfBirth(ConverterUtils.defineDatePartiel(pojo.getDateNaissance()));
        } catch (DatatypeConfigurationException e) {
            LOG.debug("[505400102 : setDateOfBirth] Erreur de conversion pour l'affilié " + pojo.getNumeroAffilie(), e);
        }
        // Sexe de l'indépendant
        salaire.setSex(ConverterUtils.translateSex(pojo.getSexe()));

        // Nationalité
        salaire.setNationality(ConverterUtils.translateNationality(pojo.getPays()));

        // Mois de début de période
        salaire.setStart(getMonth(pojo.getDateDebutPeriode(), pojo.getNumeroAffilie()));

        // Mois de fin de période
        salaire.setEnd(getMonth(pojo.getDateFinPeriode(), pojo.getNumeroAffilie()));

        // Revenu positif ou revenu négatif
        salaire.setValidityValue(ConverterUtils.defineValidityValue(new BigDecimal(pojo.getMontant())));

        // Type d'indépendant, basé sur le genre d'affilié
        salaire.setAssuredPersonType(convertGenreAffilie(pojo.getGenreAffilie()));

        // Activité accessoire
        salaire.setActivity(defineActivity(pojo.getActivity()));

        // Revenu déterminant
        salaire.setSalary(ConverterUtils.formatRevenu(pojo.getMontant()));

        // Type de décision (Définitive ou provisoire)
        salaire.setKindOfDecision(defineTypeDecision(pojo.getGenreDecision()));

        // Date de la décision
        try {
            salaire.setDecisionDate(ConverterUtils.convertToXMLGregorianCalendar(pojo.getDateDecision()));
        } catch (DatatypeConfigurationException e) {
            LOG.debug("[505400102 : setDecisionDate] Erreur de conversion pour l'affilié " + pojo.getNumeroAffilie(), e);
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
                    + "] pour l'affilié [" + idAffilie + "] : " + e.toString());
        }
    }

    /**
     * Defini l'activité accessoire.
     * 2 si activité (Donc présence d'id != 0)
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
     * Permet de définir le type de décision
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
     * Retourne le type d'affilié
     * 
     * @param typeAffilie le code système du genre d'affilié
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
