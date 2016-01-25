package globaz.hercule.mappingXmlml;

import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEEmployeurChangementMasseSalariale;
import globaz.hercule.db.controleEmployeur.CEEmployeurChangementMasseSalarialeManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.CEEmployeurChangementMasseSalarialeProcess;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @revision SCO 9 déc. 2010
 */
public class CEXmlmlMappingEmployeurChangementMasseSalariale {

    /**
     * Methode permettant de creer le corps du document
     * 
     * @param i
     * @param container
     * @param entity
     * @param process
     * @throws HerculeException
     * @throws Exception
     */
    private static void loadDetail(final HerculeContainer container, final CEEmployeurChangementMasseSalariale entity,
            final CEEmployeurChangementMasseSalarialeProcess process) throws HerculeException, Exception {
        float masseSalariale = Float.valueOf(entity.getMasseSalariale()).floatValue();
        float ancienneMasseSalariale = Float.valueOf(entity.getAncienneMasseSalariale()).floatValue();

        if (!CEControleEmployeurService.findCategorie(masseSalariale).equalsIgnoreCase(
                CEControleEmployeurService.findCategorie(ancienneMasseSalariale))) {
            container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
            container.put(ICEListeColumns.NOM, entity.getNom());
            container.put(ICEListeColumns.PERIODE_AFFILIATION,
                    entity.getDateDebutAffiliation() + " - " + entity.getDateFinAffiliation());

            // Renseigne l'adresse
            CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container,
                    entity.getIdTiers(), entity.getNumAffilie());

            container.put(ICEListeColumns.NOM_GROUPE, entity.getLibelleGroupe());
            container.put(ICEListeColumns.ANCIENNE_CATEGORIE,
                    CEControleEmployeurService.findCategorie(ancienneMasseSalariale));
            container.put(ICEListeColumns.ANCIENNE_MASSE, entity.getAncienneMasseSalariale());
            container.put(ICEListeColumns.CATEGORIE, CEControleEmployeurService.findCategorie(masseSalariale));
            container.put(ICEListeColumns.MASSE, entity.getMasseSalariale());

            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");
        }
    }

    // TODO SCO : A Passer en multilangue
    /**
     * Methode permettant de remplir le header
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(final HerculeContainer container,
            final CEEmployeurChangementMasseSalarialeProcess process) {
        String headerNumAffilie = "";
        if (!JadeStringUtil.isEmpty(process.getFromNumAffilie()) || !JadeStringUtil.isEmpty(process.getToNumAffilie())) {
            headerNumAffilie = process.getFromNumAffilie() + " à " + process.getToNumAffilie();
        }

        container.put(ICEListeColumns.HEADER_NUM_AFFILIE, headerNumAffilie);
        container.put(ICEListeColumns.HEADER_ANNEE, process.getForAnnee());
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_BLANK_2, "");
        container.put(ICEListeColumns.HEADER_BLANK_3, "");

        String annee = CEUtils.getAnneePrecedente(1, process.getForAnnee());
        String annee2 = CEUtils.getAnneePrecedente(2, process.getForAnnee());

        container.put(ICEListeColumns.HEADER_ANCIENNE_CATEGORIE, "Catégorie " + annee2);
        container.put(ICEListeColumns.HEADER_ANCIENNE_MASSE, "Masse " + annee2);
        container.put(ICEListeColumns.HEADER_CATEGORIE, "Catégorie " + annee);
        container.put(ICEListeColumns.HEADER_MASSE, "Masse " + annee);
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));
    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return
     * @throws Exception
     */
    public static HerculeContainer loadResults(final CEEmployeurChangementMasseSalarialeManager manager,
            final CEEmployeurChangementMasseSalarialeProcess process) throws Exception {
        HerculeContainer container = new HerculeContainer();

        CEXmlmlMappingEmployeurChangementMasseSalariale.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CEEmployeurChangementMasseSalariale entity = (CEEmployeurChangementMasseSalariale) manager.getEntity(i);
            process.incProgressCounter();

            CEXmlmlMappingEmployeurChangementMasseSalariale.loadDetail(container, entity, process);
        }
        return container;
    }
}
