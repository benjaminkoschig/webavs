/**
 *
 */
package ch.globaz.vulpecula.process.decompte.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.utils.BasicSerializer;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.businessimpl.services.decompte.GenererDecompteProcessor;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteInfo;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteProperty;

/**
 * Seconde étape de la génération des décomptes
 * 
 * @author sel
 * 
 */
public class PTProcessDecompteEntityHandlerGenererVides implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PTProcessDecompteProperty> {

    private String idEmployeur;
    private PTProcessDecompteInfo decompteInfo;

    private GenererDecompteProcessor genererDecompteProcess;

    protected Map<PTProcessDecompteProperty, String> dataToSave = new HashMap<PTProcessDecompteProperty, String>();

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(idEmployeur);

        // Génération des décomptes vides pour l'employeur selon les
        // informations contenues dans la classe decompteInfo.
        decomptes = genererDecompteProcess.genererDecompte(employeur, decompteInfo);

        // Création d'une liste contenant les différents ids des décomptes
        // générés dans le but de pouvoir les retrouver dans une étape
        // ultérieure.
        List<String> ids = new ArrayList<String>();
        for (Decompte decompte : decomptes) {
            ids.add(String.valueOf(decompte.getId()));
        }

        // Sauvegarde des ids des décomptes dans la table des propriétées.
        dataToSave.put(PTProcessDecompteProperty.ID_DECOMPTE, BasicSerializer.serialize(ids));
    }

    @Override
    public void setCurrentEntity(final JadeProcessEntity entity) {
        idEmployeur = entity.getIdRef();
        decompteInfo = PTProcessDecompteInfo.createFromPersistence(entity.getValue1());
    }

    @Override
    public Map<PTProcessDecompteProperty, String> getValueToSave() {
        return dataToSave;
    }

    public void setGenererDecompteProcess(GenererDecompteProcessor genererDecompteProcess) {
        this.genererDecompteProcess = genererDecompteProcess;
    }
}
