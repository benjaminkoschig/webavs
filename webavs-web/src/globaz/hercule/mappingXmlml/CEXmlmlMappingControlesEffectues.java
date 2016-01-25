package globaz.hercule.mappingXmlml;

import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEControlesEffectues;
import globaz.hercule.db.controleEmployeur.CEControlesEffectuesManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.CEListeControlesEffectuesProcess;
import globaz.hercule.service.CEComptesIndividuelsService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 3 déc. 2010
 */
public class CEXmlmlMappingControlesEffectues {

    /**
     * Methode permettant de creer le corps du document
     * 
     * @param container
     * @param entity
     * @param process
     * @throws HerculeException
     * @throws Exception
     */
    private static void loadDetail(final HerculeContainer container, final CEControlesEffectues entity,
            final CEListeControlesEffectuesProcess process) throws HerculeException, Exception {

        // Numéro d'affilié
        container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        // Nom de l'affilié
        container.put(ICEListeColumns.NOM, CEUtils.formatNomTiers(entity.getDesignation1(), entity.getDesignation2()));
        // On renseigne l'adresse
        CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container, entity.getIdTiers(),
                entity.getNumAffilie());
        // Nom du groupe
        container.put(ICEListeColumns.NOM_GROUPE, entity.getLibelleGroupe());

        // Dates d'affiliation
        if (JadeStringUtil.isEmpty(entity.getDateFinAffiliation())) {
            container.put(ICEListeColumns.PERIODE_AFFILIATION, entity.getDateDebutAffiliation());
        } else {
            container.put(ICEListeColumns.PERIODE_AFFILIATION,
                    entity.getDateDebutAffiliation() + "-" + entity.getDateFinAffiliation());
        }

        // Dates de contrôle
        if (JadeStringUtil.isEmpty(entity.getDateFinControle())) {
            container.put(ICEListeColumns.PERIODE_CONTROLEE, entity.getDateDebutControle());
        } else {
            container.put(ICEListeColumns.PERIODE_CONTROLEE,
                    entity.getDateDebutControle() + "-" + entity.getDateFinControle());
        }

        // Date effective
        container.put(ICEListeColumns.DATE_EFFECTIVE, entity.getDateEffective());
        // Date effective
        container.put(ICEListeColumns.DATE_IMPRESSION, entity.getDateImpression());
        // Type de controle
        container.put(ICEListeColumns.TYPE_CONTROLE, process.getSession().getCodeLibelle(entity.getTypeControle()));
        // Temps jours
        container.put(ICEListeColumns.TEMPS_JOURS, entity.getTempsJour());
        // Visa du réviseur
        container.put(ICEListeColumns.REVISEUR, entity.getVisaReviseur());

        int anneePrecedente = CEUtils.stringDateToAnnee(entity.getDatePrevue()) - 1;
        // CI
        int nbCI = CEComptesIndividuelsService.retrieveNombreInscription(process.getSession(),
                process.getTransaction(), entity.getIdAffilie(), Integer.toString(anneePrecedente));
        container.put(ICEListeColumns.CI, Integer.toString(nbCI));
        // Masse
        double masse = CEControleEmployeurService.retrieveMasse(process.getSession(),
                Integer.toString(anneePrecedente), entity.getNumAffilie());
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE, Double.toString(masse), "0.00");

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");

    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(final HerculeContainer container, final CEListeControlesEffectuesProcess process) {

        // On set le header
        container.put(ICEListeColumns.HEADER_FROM_DATE_IMPRESSION, process.getFromDateImpression());
        container.put(ICEListeColumns.HEADER_TO_DATE_IMPRESSION, process.getToDateImpression());
        container.put(ICEListeColumns.HEADER_REVISEUR, process.getVisaReviseur());
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());

        // Libellé
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));

    }

    /**
     * Charge les parametres pour chaque controle
     * 
     * @param manager
     * @param process
     * @return
     * @throws Exception
     */
    public static HerculeContainer loadResults(final CEControlesEffectuesManager manager,
            final CEListeControlesEffectuesProcess process) throws Exception {
        HerculeContainer container = new HerculeContainer();

        loadHeader(container, process);

        // On remplit chaque ligne
        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            // On fait progresser la progress bar
            process.incProgressCounter();
            // On récupère le controle
            CEControlesEffectues entity = (CEControlesEffectues) manager.getEntity(i);

            loadDetail(container, entity, process);

        }

        return container;
    }
}
