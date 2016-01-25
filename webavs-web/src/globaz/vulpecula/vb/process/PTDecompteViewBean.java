/**
 *
 */
package globaz.vulpecula.vb.process;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import ch.globaz.jade.process.business.handler.JadeProcessAbstractViewBean;
import ch.globaz.jade.process.business.models.property.SimplePropriete;
import ch.globaz.jade.process.business.models.property.SimpleProprieteSearch;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteProperty;
import ch.globaz.vulpecula.process.decompte.population.PTProcessDecomptePopulation;
import ch.globaz.vulpecula.web.util.FormUtil;

/**
 * ViewBean utilisé dans la page JSP "decompte_de.jsp" des process
 * 
 * @since Web@BMS 0.00.01
 */
public class PTDecompteViewBean extends JadeProcessAbstractViewBean {
    private List<Convention> conventions;

    @Override
    public String getKeyProcess() {
        return "vulpecula.decompte2";
    }

    @Override
    public HashMap<String, String> getProperties() {
        String id = getSimpleExecutionProcess().getId();
        if (id == null) {
            return new HashMap<String, String>();
        }
        HashMap<String, String> properties = new HashMap<String, String>();

        SimpleProprieteSearch search = new SimpleProprieteSearch();
        search.setForIdExecutionProcess(id);
        try {
            JadePersistenceManager.search(search);
        } catch (JadePersistenceException jade) {
            JadeLogger.error(this, jade.getMessage());
        }

        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
            SimplePropriete simplePropriete = (SimplePropriete) abstractModel;
            properties.put(simplePropriete.getKey(), simplePropriete.getValue());
        }

        return properties;
    }

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    public Vector<String[]> getConventions() {
        Vector<String[]> listConventions = FormUtil.getEmptyList();
        if (conventions == null) {
            return listConventions;
        }

        for (Convention convention : conventions) {
            String[] conventionEntry = new String[2];
            conventionEntry[0] = String.valueOf(convention.getId());
            conventionEntry[1] = convention.getDesignation();
            listConventions.add(conventionEntry);
        }

        return listConventions;
    }

    /**
     * HashSet pour définir les codes systèmes qui ne doivent pas venir dans la
     * liste FWCodeSelectTag
     * 
     * @return liste d'exception des codes systèmes d'état à afficher dans la
     *         liste ct:FWCodeSelectTag
     */
    public Set<String> getListExceptEtats() {
        Set<String> except = new HashSet<String>();
        except.add(TypeDecompte.CONTROLE_EMPLOYEUR.getValue());
        except.add(TypeDecompte.SPECIAL.getValue());
        return except;
    }

    public String getCurrentYear() {
        return String.valueOf(Date.getCurrentYear());
    }

    public boolean isAnnuelle() {
        return PTProcessDecomptePopulation.PERIODICITE_ANNUELLE.equals(getPeriodicite());
    }

    public boolean isMensuelleTrimestrielle() {
        return PTProcessDecomptePopulation.PERIODICITE_MENSUELLE_TRIMESTRIELLE.equals(getPeriodicite());
    }

    private String getPeriodicite() {
        return getProperties().get(String.valueOf(PTProcessDecompteProperty.PERIODICITE));
    }

    public String getCsComplementaire() {
        return TypeDecompte.COMPLEMENTAIRE.getValue();
    }
}
