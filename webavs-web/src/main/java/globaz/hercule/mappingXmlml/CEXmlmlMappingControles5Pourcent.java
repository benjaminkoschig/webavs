package globaz.hercule.mappingXmlml;

import globaz.globall.db.BSession;
import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEControles5PourCent;
import globaz.hercule.db.controleEmployeur.CEControles5PourCentManager;
import globaz.hercule.db.controleEmployeur.CEControlesExtraOrdinairesEffectuesManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.controleEmployeur.CEControles5PourCentProcess;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @author JPA
 * @revision SCO 9 déc. 2010
 */
public class CEXmlmlMappingControles5Pourcent {

    /**
     * Methode permettant de remplir le corps du document avec les docnnées des controles 5%
     * 
     * @param container
     * @param manager
     * @param process
     * @throws HerculeException
     * @throws Exception
     */
    private static void loadDetailControles5PourCent(final CommonExcelmlContainer container,
            final CEControles5PourCentManager manager, final CEControles5PourCentProcess process)
            throws HerculeException, Exception {

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            process.incProgressCounter();

            CEControles5PourCent entity = (CEControles5PourCent) manager.getEntity(i);

            container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
            container.put(ICEListeColumns.NOM, entity.getNom());

            // On renseigne l'adresse
            CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container,
                    entity.getIdTiers(), entity.getNumAffilie());

            container.put(ICEListeColumns.NOM_GROUPE, entity.getLibelleGroupe());

            // Période d'affiliation
            String periodeAffiliation = entity.getDateDebutAffiliation();
            if (!JadeStringUtil.isEmpty(entity.getDateFinAffiliation())) {
                periodeAffiliation = entity.getDateDebutAffiliation() + "-" + entity.getDateFinAffiliation();
            }

            container.put(ICEListeColumns.PERIODE_AFFILIATION, periodeAffiliation);

            CEExcelmlUtils.remplirColumnByLibelle(container, process.getSession(), ICEListeColumns.TYPE_CONTROLE,
                    entity.getTypeControle(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DATE_EFFECTIVE, entity.getDateEffective(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DATE_PREVUE, entity.getDatePrevue(), "");

            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");
        }
    }

    /**
     * Methode permettant de remplir le corps du document avec les docnnées des controles extras
     * 
     * @param container
     * @param manager
     * @param process
     * @throws HerculeException
     * @throws Exception
     */
    private static void loadDetailControlesExtras(final CommonExcelmlContainer container,
            final CEControlesExtraOrdinairesEffectuesManager manager, final CEControles5PourCentProcess process)
            throws HerculeException, Exception {

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            process.incProgressCounter();

            CEControles5PourCent entity = (CEControles5PourCent) manager.getEntity(i);

            container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());
            container.put(ICEListeColumns.NOM, entity.getNom());

            // On renseigne l'adresse
            CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container,
                    entity.getIdTiers(), entity.getNumAffilie());

            container.put(ICEListeColumns.NOM_GROUPE, entity.getLibelleGroupe());

            // Période d'affiliation
            String periodeAffiliation = entity.getDateDebutAffiliation();
            if (!JadeStringUtil.isEmpty(entity.getDateFinAffiliation())) {
                periodeAffiliation = entity.getDateDebutAffiliation() + "-" + entity.getDateFinAffiliation();
            }

            container.put(ICEListeColumns.PERIODE_AFFILIATION, periodeAffiliation);
            CEExcelmlUtils.remplirColumnByLibelle(container, process.getSession(), ICEListeColumns.TYPE_CONTROLE,
                    entity.getTypeControle(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DATE_EFFECTIVE, entity.getDateEffective(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DATE_PREVUE, entity.getDatePrevue(), "");

            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");
        }
    }

    /**
     * Methode permettant de remplir le header
     * 
     * @param container
     * @param process
     * @param nombre5PourCent
     * @param nombreControles5PourCent
     * @param nombreControlesExtraordinaires
     */
    private static void loadHeader(final CommonExcelmlContainer container, final CEControles5PourCentProcess process,
            final int nombre5PourCent, final int nombreControles5PourCent, final int nombreControlesExtraordinaires) {

        // On set le header
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_ANNEE, process.getAnnee(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_NB_MINI, String.valueOf(nombre5PourCent), "0");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_CONT_EXTRA,
                String.valueOf(nombreControlesExtraordinaires), "0");
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_CONT_5_POURCENT,
                String.valueOf(nombreControles5PourCent), "0");

        int nombreRestant = nombre5PourCent - (nombreControles5PourCent + nombreControlesExtraordinaires);
        CEExcelmlUtils
                .remplirColumn(container, ICEListeColumns.HEADER_CONT_RESTANT, String.valueOf(nombreRestant), "0");

        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_BLANK_2, "");
        container.put(ICEListeColumns.HEADER_BLANK_3, "");
    }

    /**
     * Methode permettant de charger les libellés de la liste
     * 
     * @param container
     * @param session
     */
    public static void loadLabel(final CommonExcelmlContainer container, final BSession session) {

        container.put(ICEListeColumns.HEADER_LABEL_NOM_LISTE,
                session.getLabel("LISTE_CONTROLE_5_POURCENT_NOM_DOCUMENT"));
        container.put(ICEListeColumns.HEADER_LABEL_ANNEE, session.getLabel("LISTE_CONTROLE_5_POURCENT_ANNEE"));

        container.put(ICEListeColumns.HEADER_LABEL_NB_MINI_CONTROLER,
                session.getLabel("LISTE_CONTROLE_5_POURCENT_NB_MINI_CONTROLER"));
        container.put(ICEListeColumns.HEADER_LABEL_CONTROLE_EXTRA,
                session.getLabel("LISTE_CONTROLE_5_POURCENT_CONTROLE_EXTRA"));
        container.put(ICEListeColumns.HEADER_LABEL_CONTROLE_5, session.getLabel("LISTE_CONTROLE_5_POURCENT_CONTROLE"));
        container.put(ICEListeColumns.HEADER_LABEL_CONTROLE_RESTANT,
                session.getLabel("LISTE_CONTROLE_5_POURCENT_CONTROLE_RESTANT"));

        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_NUM_AFFILIE,
                session.getLabel("LISTE_CONTROLE_HEADER_NUM_AFFILIE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_NOM, session.getLabel("LISTE_CONTROLE_HEADER_NOM"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_RUE, session.getLabel("LISTE_CONTROLE_HEADER_RUE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CASE_POSTALE,
                session.getLabel("LISTE_CONTROLE_HEADER_CASE_POSTALE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_NPA, session.getLabel("LISTE_CONTROLE_HEADER_NPA"));
        container
                .put(ICEListeColumns.HEADER_COLONNE_LABEL_LOCALITE, session.getLabel("LISTE_CONTROLE_HEADER_LOCALITE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_GROUPE, session.getLabel("LISTE_CONTROLE_HEADER_GROUPE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_PERIODE_AFFILIATION,
                session.getLabel("LISTE_CONTROLE_HEADER_PERIODE_AFFILIATION"));

        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_TYPE_CONTROLE,
                session.getLabel("LISTE_CONTROLE_HEADER_TYPE_CONTROLE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_DATE_EFFECTIVE,
                session.getLabel("LISTE_CONTROLE_HEADER_DATE_EFFECTIVE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_DATE_PREVU,
                session.getLabel("LISTE_CONTROLE_HEADER_DATE_PREVUE"));

        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                session.getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                session.getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));
    }

    /**
     * @param manager5PourCent
     * @param managerExtraOrdinaires
     * @param nombre5PourCent
     * @param nombreControles5PourCent
     * @param nombreControlesExtraordinaires
     * @param process
     * @return
     * @throws Exception
     */
    public static CommonExcelmlContainer loadResults(final CEControles5PourCentManager manager5PourCent,
            final CEControlesExtraOrdinairesEffectuesManager managerExtraOrdinaires, final int nombre5PourCent,
            final int nombreControles5PourCent, final int nombreControlesExtraordinaires,
            final CEControles5PourCentProcess process) throws Exception {

        CommonExcelmlContainer container = new CommonExcelmlContainer();

        // On s'occupe des labels du documents
        loadLabel(container, process.getSession());

        // On remplit le header
        loadHeader(container, process, nombre5PourCent, nombreControles5PourCent, nombreControlesExtraordinaires);

        // On remplit d'abord les contrôles extraordinaires déjà effectués
        loadDetailControlesExtras(container, managerExtraOrdinaires, process);

        // On remplit avec les controles 5%
        loadDetailControles5PourCent(container, manager5PourCent, process);

        return container;
    }
}
