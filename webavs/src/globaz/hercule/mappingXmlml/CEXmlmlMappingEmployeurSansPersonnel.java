package globaz.hercule.mappingXmlml;

import globaz.globall.db.BSession;
import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEEmployeurSansPersonnel;
import globaz.hercule.db.controleEmployeur.CEEmployeurSansPersonnelManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.CEEmployeurSansPersonnelProcess;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;

public class CEXmlmlMappingEmployeurSansPersonnel {

    private static void loadDetail(final int i, final HerculeContainer container,
            final CEEmployeurSansPersonnel entity, final CEEmployeurSansPersonnelProcess process)
            throws HerculeException, Exception {
        process.incProgressCounter();

        container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        container.put(ICEListeColumns.NOM, entity.getNom());
        container.put(ICEListeColumns.PERIODE_AFFILIATION,
                entity.getDateDebutAffiliation() + " - " + entity.getDateFinAffiliation());

        /**
         * recherche l'adresse dans le tiers put dans le container
         */
        CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container, entity.getIdTiers(),
                entity.getNumAffilie());

        container.put(ICEListeColumns.NOM_GROUPE, entity.getNomGroupe());

        /**
         * recherche le dernier contrôle put dans le container
         */
        CEXmlmlMappingEmployeurSansPersonnel.renseigneLastControle(container, entity.getIdAffiliation(),
                process.getSession());

        container.put(ICEListeColumns.SANS_PERSONNEL_DEPUIS, entity.getDateDebutParticularite());
        container.put(ICEListeColumns.ANNEE_PREVUE,
                String.valueOf(Integer.valueOf(process.getForAnnee()).intValue() + 1));
        container.put(ICEListeColumns.REVISEUR, "");

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");

    }

    private static void loadHeader(final HerculeContainer container, final CEEmployeurSansPersonnelProcess process) {

        container.put(ICEListeColumns.HEADER_ANNEE, process.getForAnnee());
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());
        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_BLANK_2, "");
        container.put(ICEListeColumns.HEADER_BLANK_3, "");

        // Numéro inforom
        container.put(ICEListeColumns.HEADER_NUM_INFOROM, CEEmployeurSansPersonnelProcess.NUMERO_INFOROM);

        // Libellé
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));

    }

    public static HerculeContainer loadResults(final CEEmployeurSansPersonnelManager manager,
            final CEEmployeurSansPersonnelProcess process) throws HerculeException, Exception {
        HerculeContainer container = new HerculeContainer();

        CEXmlmlMappingEmployeurSansPersonnel.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CEEmployeurSansPersonnel entity = (CEEmployeurSansPersonnel) manager.getEntity(i);
            CEXmlmlMappingEmployeurSansPersonnel.loadDetail(i, container, entity, process);
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
