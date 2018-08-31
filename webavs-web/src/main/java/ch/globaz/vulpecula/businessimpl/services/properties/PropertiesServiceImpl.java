/**
 *
 */
package ch.globaz.vulpecula.businessimpl.services.properties;

import globaz.globall.db.BSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;

/**
 * @author JPA
 * 
 */
public class PropertiesServiceImpl implements PropertiesService {
    public final static String MODULENAME = "vulpecula";
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesServiceImpl.class);

    @Override
    public Montant getDifferenceAutoriseeControleDecompte() {
        return new Montant(findProperties(DIFFERENCE_AUTORISEE_CONTROLE_DECOMPTE));
    }

    @Override
    public int getRappelNombreJours() {
        return Integer.valueOf(findProperties(RAPPEL_NOMBRE_JOURS));
    }

    @Override
    public int getTaxationNombreJoursInferieureEtablissement() {
        return Integer.valueOf(findProperties(TAXATION_NOMBRE_JOURS_INFERIEURE_ETABLISSEMENT));
    }

    @Override
    public int getTaxationNombreJoursSuperieureEtablissement() {
        return Integer.valueOf(findProperties(TAXATION_NOMBRE_JOURS_SUPERIEURE_ETABLISSEMENT));
    }

    @Override
    public int getMontantBaseTOSansDecompte() {
        return Integer.valueOf(findProperties(MONTANT_BASE_TO_SANS_DECOMPTE));
    }

    @Override
    public Annee getAnneeProduction() {
        return new Annee(findProperties(ANNEE_PRODUCTION));
    }

    @Override
    public List<String> getCaissesMetiers() {
        String caissesMetiers = findProperties(CAISSES_METIERS);
        String[] caisses = caissesMetiers.split(",");
        return Arrays.asList(caisses);
    }

    @Override
    public List<String> getCaissesAF() {
        String caissesMetiers = findProperties(CAISSES_AF);
        String[] caisses = caissesMetiers.split(",");
        return Arrays.asList(caisses);
    }

    @Override
    public String getTexteRectificatifAllemand() {
        return findProperties(TEXTE_RECTIFICATIF_ALLEMAND);
    }

    @Override
    public String getTexteRectificatifAllemandElectricien() {
        return findProperties(TEXTE_RECTIFICATIF_ALLEMAND_ELECTRICIEN);
    }

    @Override
    public List<String> getTosAnnuleesEmails() {
        String emails = findProperties(TOS_ANNULEES_EMAILS);
        if (JadeStringUtil.isEmpty(emails)) {
            return Collections.emptyList();
        }
        String[] list = emails.split(",");
        return Arrays.asList(list);
    }

    @Override
    public String findProperties(String propertiesName) {
        try {
            String newName = new StringBuilder().append(MODULENAME).append(".").append(propertiesName).toString();

            String newValue = JadePropertiesService.getInstance().getProperty(newName);
            if (newValue != null) {
                return newValue;
            }
            String commonName = new StringBuilder().append(BSystem.COMMON_PREFIX).append(propertiesName).toString();
            String commonValue = JadePropertiesService.getInstance().getProperty(commonName);
            if (commonValue != null) {
                return commonValue;
            }
            LOGGER.error("Ni la propriété 'vulpecula.caissesAF' ni 'common.caissesAF' n'est renseigné !");
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> findPropertiesAsList(String propertiesName) {
        String properties = findProperties(propertiesName);
        String[] list = properties.split(",");
        return Arrays.asList(list);
    }

    @Override
    public List<String> getPrioritesLPP() {
        return findPropertiesAsList(PRIORITES_COTISATIONS_LPP);
    }

    @Override
    public String getMailAF() {
        return findProperties(MAIL_AF);
    }

    @Override
    public Boolean isGedMyProdis() {
        String value = findProperties(GED_MY_PRODIS);
        return "TRUE".equalsIgnoreCase(value);
    }

    @Override
    public Boolean mustImprimerFactureSpecialEbusiness() {
        String value = findProperties(IMPRIMER_FACTURE_SPECIAL_EBUSINESS);
        if (value == null) {
            LOGGER.error("La propriété 'vulpecula.imprimerFactureSpecialEbusiness' n'est pas renseigné !");
            return null;

        } else {
            return "TRUE".equalsIgnoreCase(value);
        }
    }
}
