package ch.globaz.al.businessimpl.services.rubriques.comptables;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesBMSService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCCJUService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCCVDService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCICIService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCVCIService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesFPVService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesFVEService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesHorlogeresService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesService;

/**
 * Factory permettant de déterminer le service Rubrique comptable à utiliser
 * 
 * @author jts, pta (22.09.2016)
 */
public abstract class RubriqueComptableServiceFactory {

    /**
     * Liste des dispatchers spécifiques
     */
    private static HashMap<String, Class<?>> caissesMap;

    static {
        RubriqueComptableServiceFactory.caissesMap = new HashMap<String, Class<?>>();
        RubriqueComptableServiceFactory.caissesMap.put("BMS", RubriquesComptablesBMSService.class);
        RubriqueComptableServiceFactory.caissesMap.put("CCJU", RubriquesComptablesCCJUService.class);
        RubriqueComptableServiceFactory.caissesMap.put("CCVD", RubriquesComptablesCCVDService.class);
        RubriqueComptableServiceFactory.caissesMap.put("CVCI", RubriquesComptablesCVCIService.class);
        RubriqueComptableServiceFactory.caissesMap.put("FPV", RubriquesComptablesFPVService.class);
        RubriqueComptableServiceFactory.caissesMap.put("CICI", RubriquesComptablesCICIService.class);
        RubriqueComptableServiceFactory.caissesMap.put("FVE", RubriquesComptablesFVEService.class);

        RubriqueComptableServiceFactory.caissesMap.put("H510", RubriquesComptablesHorlogeresService.class);
        RubriqueComptableServiceFactory.caissesMap.put("H513", RubriquesComptablesHorlogeresService.class);
        RubriqueComptableServiceFactory.caissesMap.put("H514", RubriquesComptablesHorlogeresService.class);
        RubriqueComptableServiceFactory.caissesMap.put("H515", RubriquesComptablesHorlogeresService.class);
        RubriqueComptableServiceFactory.caissesMap.put("H517", RubriquesComptablesHorlogeresService.class);
        RubriqueComptableServiceFactory.caissesMap.put("H51X", RubriquesComptablesHorlogeresService.class);

        RubriqueComptableServiceFactory.caissesMap.put("NODE", RubriquesComptablesHorlogeresService.class);

    }

    /**
     * Récupère le service de rubrique comptable en fonction de la <code>date</code> et du nom de la caisse récupéré
     * dans les paramètres
     * 
     * @param date
     *            Date pour laquelle récupérer le service
     * @return Classe à instancier
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static Class getServiceRubriqueComptable(String date) throws JadeApplicationException,
            JadePersistenceException {

        String caisse = ALServiceLocator.getParametersServices().getNomCaisse(date);

        if (RubriqueComptableServiceFactory.caissesMap.containsKey(caisse)) {
            return RubriqueComptableServiceFactory.caissesMap.get(caisse);
        } else {
            return RubriquesComptablesService.class;
        }
    }
}
