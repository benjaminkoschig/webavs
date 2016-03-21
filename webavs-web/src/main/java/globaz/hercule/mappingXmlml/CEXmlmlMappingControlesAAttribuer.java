package globaz.hercule.mappingXmlml;

import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEControlesAttribues;
import globaz.hercule.db.controleEmployeur.CEControlesAttribuesManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.CEListeControlesAttribuesProcess;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.webavs.common.CommonExcelmlContainer;

public class CEXmlmlMappingControlesAAttribuer {

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(final CommonExcelmlContainer container,
            final CEListeControlesAttribuesProcess process) {

        // On set le header
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_ANNEE, process.getAnnee(), "");
        CEExcelmlUtils.remplirColumnByLibelle(container, process.getSession(), ICEListeColumns.HEADER_ANNEE,
                process.getGenreControle(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_VISA_REVISEUR, process.getVisaReviseur(), "");
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());

        if (process.getAEffectuer().booleanValue() && process.getDejaEffectuer().booleanValue()) {
            container.put(ICEListeColumns.HEADER_A_EFFECTUER,
                    process.getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_CONTROLE_A_EFFECTUER_NON"));
        } else if (!process.getAEffectuer().booleanValue() && !process.getDejaEffectuer().booleanValue()) {
            container.put(ICEListeColumns.HEADER_A_EFFECTUER,
                    process.getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_CONTROLE_A_EFFECTUER_NON"));
        } else {
            if (process.getAEffectuer().booleanValue()) {
                container.put(ICEListeColumns.HEADER_A_EFFECTUER,
                        process.getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_CONTROLE_A_EFFECTUER"));
            } else {
                container.put(ICEListeColumns.HEADER_A_EFFECTUER,
                        process.getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_CONTROLE_DEJA_EFFECTUER"));
            }
        }

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
     */
    private static void loadDetail(final CommonExcelmlContainer container, final CEControlesAttribues entity,
            final CEListeControlesAttribuesProcess process) throws HerculeException {

        container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        container.put(ICEListeColumns.NOM, entity.getNom());

        // On renseigne l'adresse
        CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container, entity.getIdTiers(),
                entity.getNumAffilie());
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.NOM_GROUPE, entity.getLibelleGroupe(), "");

        if (JadeStringUtil.isEmpty(entity.getDateFinAffiliation())) {
            container.put(ICEListeColumns.PERIODE_AFFILIATION, entity.getDateDebutAffiliation());
        } else {
            container.put(ICEListeColumns.PERIODE_AFFILIATION,
                    entity.getDateDebutAffiliation() + "-" + entity.getDateFinAffiliation());
        }

        if (JadeStringUtil.isEmpty(entity.getDateFinControle())) {
            container.put(ICEListeColumns.PERIODE_CONTROLEE, entity.getDateDebutControle());
        } else {
            container.put(ICEListeColumns.PERIODE_CONTROLEE,
                    entity.getDateDebutControle() + "-" + entity.getDateFinControle());
        }

        container.put(ICEListeColumns.TYPE_CONTROLE, process.getSession().getCodeLibelle(entity.getTypeControle()));
        container.put(ICEListeColumns.TEMPS_JOURS, entity.getTempsJour());
        container.put(ICEListeColumns.CI, entity.getNbInscCI());
        container.put(ICEListeColumns.DATE_DERNIER_CONTROLE, entity.getDatePrecControle());

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE, entity.getMontantMasse_1(), "0.00");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.REVISEUR, entity.getVisaReviseur(), "");

        // Date effective
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DATE_EFFECTIVE, entity.getDateEffective(), "");

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");

    }

    /**
     * Chargement de la liste des contrôles a attribuer
     * 
     * @param manager
     * @param process
     * @return
     * @throws HerculeException
     */
    public static CommonExcelmlContainer loadResults(final CEControlesAttribuesManager manager,
            final CEListeControlesAttribuesProcess process) throws HerculeException {

        CommonExcelmlContainer container = new CommonExcelmlContainer();

        CEXmlmlMappingControlesAAttribuer.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            process.incProgressCounter();

            CEControlesAttribues entity = (CEControlesAttribues) manager.getEntity(i);
            CEXmlmlMappingControlesAAttribuer.loadDetail(container, entity, process);
        }

        return container;
    }
}
