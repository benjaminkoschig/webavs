package ch.globaz.naos.ree.domain.converter;

import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ree.ch.admin.bfs.xmlns.bfs_5054_000101._2.DeliverySalariesIKType;
import ch.globaz.naos.ree.domain.pojo.Pojo5054_101;
import ch.globaz.naos.ree.tools.InfoCaisse;

public class Converter5054_101 implements Converter<Pojo5054_101, DeliverySalariesIKType> {
    private static final Logger LOG = LoggerFactory.getLogger(Converter5054_101.class);
    private InfoCaisse currentCaisse;

    public Converter5054_101(InfoCaisse infoCaisse) {
        currentCaisse = infoCaisse;
    }

    @Override
    public DeliverySalariesIKType convert(Pojo5054_101 businessMessage) throws REEBusinessException {
        return convertPojos(businessMessage);
    }

    private DeliverySalariesIKType convertPojos(Pojo5054_101 pojo) throws REEBusinessException {

        DeliverySalariesIKType salaire = new DeliverySalariesIKType();

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
            LOG.debug("[505400101 : setDateOfBirth] Erreur de conversion pour l'affilié " + pojo.getNumeroAffilie(), e);
        }

        // Sexe selon header CI
        salaire.setSex(ConverterUtils.translateSex(pojo.getSexe()));

        // Nationalité
        salaire.setNationality(ConverterUtils.translateNationality(pojo.getPays()));

        // Mois de début de période
        salaire.setStart(pojo.getDateDebutPeriode());

        // Mois de fin de période
        salaire.setEnd(pojo.getDateFinPeriode());

        // Revenu positif ou revenu négatif
        salaire.setValidityValue(ConverterUtils.defineValidityValue(pojo.getCodeExtourne()));

        // Type de revenu salarié, basé sur le genre de cotisation
        salaire.setAssuredPersonType(ConverterUtils.translateTypeRevenuSalarie(pojo.getGenreEcriture(),
                pojo.getGenreCodeSpeciale()));

        // Valeur par défaut = 9
        salaire.setActivity(BigInteger.valueOf(9));

        // Revenu déterminant
        salaire.setSalary(ConverterUtils.formatRevenu(pojo.getMontant(), pojo.getCodeExtourne()));

        // Date de la décision = date de l'inscription CI
        try {
            salaire.setDecisionDate(ConverterUtils.convertToXMLGregorianCalendar(pojo.getDateCreationSpy()));
        } catch (DatatypeConfigurationException e) {
            LOG.debug("[505400101 : setDecisionDate] Erreur de conversion pour l'affilié " + pojo.getNumeroAffilie(), e);
        }

        return salaire;
    }

}
