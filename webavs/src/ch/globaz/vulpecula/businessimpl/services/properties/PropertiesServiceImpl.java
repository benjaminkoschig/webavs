/**
 * 
 */
package ch.globaz.vulpecula.businessimpl.services.properties;

import globaz.globall.db.BSystem;
import globaz.jade.properties.JadePropertiesService;
import java.util.Arrays;
import java.util.List;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;

/**
 * @author JPA
 * 
 */
public class PropertiesServiceImpl implements PropertiesService {
    public final static String MODULENAME = "vulpecula";

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
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
