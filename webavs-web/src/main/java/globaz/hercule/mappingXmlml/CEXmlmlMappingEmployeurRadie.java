package globaz.hercule.mappingXmlml;

import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEEmployeurRadie;
import globaz.hercule.db.controleEmployeur.CEEmployeurRadieManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.CEEmployeurRadieProcess;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;

public class CEXmlmlMappingEmployeurRadie {

    private static void loadDetail(final int i, final HerculeContainer container, final CEEmployeurRadie entity,
            final CEEmployeurRadieProcess process) throws HerculeException, Exception {
        process.incProgressCounter();

        container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        container.put(ICEListeColumns.NOM, entity.getNom());

        /**
         * recherche l'adresse dans le tiers put dans le container
         */
        CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container, entity.getIdTiers(),
                entity.getNumAffilie());

        container.put(ICEListeColumns.NOM_GROUPE, entity.getNomGroupe());
        container.put(ICEListeColumns.DATE_AFFILIATION, entity.getDateDebutAffiliation());
        container.put(ICEListeColumns.DATE_RADIATION, entity.getDateFinAffiliation());
        container.put(ICEListeColumns.MOTIF_RADIATION, process.getSession().getCodeLibelle(entity.getMotifRadiation()));
        container.put(ICEListeColumns.MASSE_SALARIALE, entity.getMasseSalariale());

        /**
         * recherche le dernier contrôle put dans le container
         */
        CEXmlmlMappingEmployeurRadie.renseigneLastControle(container, entity.getIdAffiliation(), process.getSession());

        container.put(ICEListeColumns.ANNEE_PREVUE,
                String.valueOf(new JADate(process.getFromDateRadiation()).getYear()));

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");
    }

    private static void loadHeader(final HerculeContainer container, final CEEmployeurRadieProcess process)
            throws JAException {

        container.put(ICEListeColumns.HEADER_DATE_RADIATION,
                process.getFromDateRadiation() + " - " + process.getToDateRadiation());
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());

        if (JadeStringUtil.isEmpty(process.getFromMasseSalariale())
                && JadeStringUtil.isEmpty(process.getToMasseSalariale())) {
            container.put(ICEListeColumns.HEADER_MASSE_SALARIALE, "");
        } else {
            container.put(ICEListeColumns.HEADER_MASSE_SALARIALE,
                    process.getFromMasseSalariale() + " - " + process.getToMasseSalariale());
        }

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_BLANK_2, "");
        container.put(ICEListeColumns.HEADER_BLANK_3, "");
        // TODO SCO : A Passer en multilangue
        container.put(ICEListeColumns.HEADER_COLONNE_MASSE_SALARIALE,
                "Masse salariale " + String.valueOf(new JADate(process.getFromDateRadiation()).getYear() - 1));

        // Numéro inforom
        container.put(ICEListeColumns.HEADER_NUM_INFOROM, CEEmployeurRadieProcess.NUMERO_INFOROM);

        // Libelle
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));
    }

    public static HerculeContainer loadResults(final CEEmployeurRadieManager manager,
            final CEEmployeurRadieProcess process) throws HerculeException, Exception {
        HerculeContainer container = new HerculeContainer();

        CEXmlmlMappingEmployeurRadie.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CEEmployeurRadie entity = (CEEmployeurRadie) manager.getEntity(i);
            CEXmlmlMappingEmployeurRadie.loadDetail(i, container, entity, process);
        }

        return container;
    }

    private static void renseigneLastControle(final HerculeContainer container, final String idAffiliation,
            final BSession session) throws Exception {
        CEControleEmployeur entity = CEUtils.rechercheDernierControle(idAffiliation, session);
        if (entity != null) {
            container.put(ICEListeColumns.DATE_DERNIER_CONTROLE, entity.getDateEffective());
            container.put(ICEListeColumns.PERIODE_CONTROLEE,
                    entity.getDateDebutControle() + " - " + entity.getDateFinControle());
        } else {
            container.put(ICEListeColumns.DATE_DERNIER_CONTROLE, "");
            container.put(ICEListeColumns.PERIODE_CONTROLEE, "");
        }
    }
}
