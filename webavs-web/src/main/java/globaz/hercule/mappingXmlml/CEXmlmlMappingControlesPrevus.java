package globaz.hercule.mappingXmlml;

import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEControlesPrevus;
import globaz.hercule.db.controleEmployeur.CEControlesPrevusManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.CEListeControlesPrevusProcess;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @author SCO
 * @since 11 oct. 2010
 */
public class CEXmlmlMappingControlesPrevus {

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(final CommonExcelmlContainer container, final CEListeControlesPrevusProcess process) {

        // On set le header
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_ANNEE, process.getAnnee(), "");
        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());

        // Numéro inforom
        container.put(ICEListeColumns.HEADER_NUM_INFOROM, CEListeControlesPrevusProcess.NUMERO_INFOROM);

        // Libellé
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));
    }

    /**
     * Methode permettant de creer le corps du document
     * 
     * @param container
     * @param entity
     * @param process
     * @throws HerculeException
     * @throws Exception
     */
    private static void loadDetail(final CommonExcelmlContainer container, final CEControlesPrevus entity,
            final CEListeControlesPrevusProcess process) throws HerculeException {

        // Numéro de l'affilié
        container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        // Nom de l'affilié
        container.put(ICEListeColumns.NOM, CEUtils.formatNomTiers(entity.getDesignation1(), entity.getDesignation2()));
        // Adressse de l'affilié
        CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container, entity.getIdTiers(),
                entity.getNumAffilie());

        // Nom du groupe
        container.put(ICEListeColumns.NOM_GROUPE, entity.getLibelleGroupe());
        // Période d'affiliation
        if (JadeStringUtil.isEmpty(entity.getDateFinAffiliation())) {
            container.put(ICEListeColumns.PERIODE_AFFILIATION, entity.getDateDebutAffiliation());
        } else {
            container.put(ICEListeColumns.PERIODE_AFFILIATION,
                    entity.getDateDebutAffiliation() + "-" + entity.getDateFinAffiliation());
        }
        // Prédiode de controle
        if (JadeStringUtil.isEmpty(entity.getDateFinControle())) {
            container.put(ICEListeColumns.PERIODE_CONTROLEE, entity.getDateDebutControle());
        } else {
            container.put(ICEListeColumns.PERIODE_CONTROLEE,
                    entity.getDateDebutControle() + "-" + entity.getDateFinControle());
        }

        // Type de controle
        container.put(ICEListeColumns.TYPE_CONTROLE, process.getSession().getCodeLibelle(entity.getTypeControle()));
        // Temps en jours
        container.put(ICEListeColumns.TEMPS_JOURS, entity.getTempsJour());
        // Nbr CI
        container.put(ICEListeColumns.CI, entity.getNbInscCI());
        // date du précédent controle
        container.put(ICEListeColumns.DATE_DERNIER_CONTROLE, entity.getDatePrecControle());
        // Masse
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE, entity.getMontantMasse(), "0.00");
        // Visa Reviseur
        container.put(ICEListeColumns.REVISEUR, entity.getVisaReviseur());

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");
    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return
     * @throws HerculeException
     */
    public static CommonExcelmlContainer loadResults(final CEControlesPrevusManager manager,
            final CEListeControlesPrevusProcess process) throws HerculeException {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        loadHeader(container, process);

        // On remplit chaque ligne
        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            process.incProgressCounter();
            CEControlesPrevus entity = (CEControlesPrevus) manager.getEntity(i);

            loadDetail(container, entity, process);
        }

        return container;
    }
}
