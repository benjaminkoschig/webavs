package globaz.hercule.mappingXmlml;

import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.controleEmployeur.CEEmployeurMasseSalariale;
import globaz.hercule.db.controleEmployeur.CEEmployeurMasseSalarialeManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.mappingXmlml.line.CEXmlmlLineEmployeurMasseSalariale;
import globaz.hercule.process.CEEmployeurMasseSalarialeProcess;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * Classe permettant de remplir la liste xml
 * 
 * @author Sullivann Corneille
 * @since 3 avr. 2014
 */
public class CEXmlmlMappingEmployeurMasseSalariale {

    /**
     * Methode permettant de creer le corps du document
     * 
     * @param container
     * @param entity
     * @param process
     * @throws HerculeException
     * @throws Exception
     */
    private static void loadDetail(final CommonExcelmlContainer container, final CEEmployeurMasseSalariale entity,
            final CEEmployeurMasseSalarialeProcess process) throws HerculeException, Exception {

        CEXmlmlLineEmployeurMasseSalariale xmlLine = new CEXmlmlLineEmployeurMasseSalariale();

        // Info sur l'affiliation
        xmlLine.setNumAffilie(entity.getNumAffilie());
        xmlLine.setNom(entity.getNom());
        xmlLine.setPeriodeAffiliation(entity.getDateDebutAffiliation() + " - " + entity.getDateFinAffiliation());

        // Infos sur l'adresse
        CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), xmlLine, entity.getIdTiers(),
                entity.getNumAffilie());

        xmlLine.setGroupe(entity.getLibelleGroupe());
        xmlLine.setMasseAvs1(entity.getMasseSalarialeAnneeMoins1());
        xmlLine.setMasseAvs2(entity.getMasseSalarialeAnneeMoins2());
        xmlLine.setMasseAvs3(entity.getMasseSalarialeAnneeMoins3());
        xmlLine.setMasseAvs4(entity.getMasseSalarialeAnneeMoins4());
        xmlLine.setMasseAvs5(entity.getMasseSalarialeAnneeMoins5());

        xmlLine.setCi1(entity.getNbCI1());
        xmlLine.setCi2(entity.getNbCI2());
        xmlLine.setCi3(entity.getNbCI3());
        xmlLine.setCi4(entity.getNbCI4());
        xmlLine.setCi5(entity.getNbCI5());

        xmlLine.setMasseAf(entity.getMasseSalarialeAF());

        xmlLine.setGroupe(entity.getLibelleGroupe());
        if (JadeStringUtil.isIntegerEmpty(entity.getCaisseAVS())) {
            xmlLine.setCaisseAvs(entity.getCaisseAVS());
        }
        xmlLine.setCategorie(CEControleEmployeurService.findCategorie(Float.valueOf(
                entity.getMasseSalarialeAnneeMoins1()).floatValue()));

        xmlLine.setCodeSuva(entity.getCodeSuva());
        xmlLine.setLibelle(entity.getLibelleSuva());

        // Remplissage du container
        xmlLine.remplirContainerWithLineData(container);
    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(final CommonExcelmlContainer container,
            final CEEmployeurMasseSalarialeProcess process) {

        // -----------------------
        // Ajout des labels des infos de processus
        // -----------------------
        container.put(ICEListeColumns.HEADER_LABEL_NOM_LISTE,
                process.getSession().getLabel("LISTE_EMPLOYEUR_MASSE_SALARIALE_NOM_DOCUMENT"));
        container.put(ICEListeColumns.HEADER_LABEL_NUM_AFFILIES,
                process.getSession().getLabel("LISTE_EMPLOYEUR_MASSE_SALARIALE_NUM_AFFILIES"));
        container.put(ICEListeColumns.HEADER_LABEL_ANNEE,
                process.getSession().getLabel("LISTE_EMPLOYEUR_MASSE_SALARIALE_ANNEE"));

        // -----------------------
        // Ajout des labels de colonne
        // -----------------------
        int annee = Integer.parseInt(process.getForAnnee());

        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_NUM_AFFILIE,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NUM_AFFILIE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_NOM,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NOM"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_RUE,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_RUE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CASE_POSTALE,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CASE_POSTALE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_NPA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NPA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LOCALITE,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LOCALITE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_GROUPE,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_GROUPE"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_PERIODE_AFFILIATION,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_PERIODE_AFFILIATION"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CAISSE_AVS,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CAISSE_AVS"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CATEGORIE,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CATEGORIE") + " " + (annee - 1));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_MASSE_AVS_1,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_MASSE_AVS") + " " + (annee - 1));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_MASSE_AVS_2,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_MASSE_AVS") + " " + (annee - 2));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_MASSE_AVS_3,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_MASSE_AVS") + " " + (annee - 3));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_MASSE_AVS_4,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_MASSE_AVS") + " " + (annee - 4));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_MASSE_AVS_5,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_MASSE_AVS") + " " + (annee - 5));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CI_1,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NB_CI") + " " + (annee - 1));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CI_2,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NB_CI") + " " + (annee - 2));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CI_3,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NB_CI") + " " + (annee - 3));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CI_4,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NB_CI") + " " + (annee - 4));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CI_5,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_NB_CI") + " " + (annee - 5));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_MASSE_AF,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_MASSE_AF") + " " + (annee - 1));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));

        // -----------------------
        // Ajout des infos du processus
        // -----------------------
        String headerNumAffilie = "";
        if (!JadeStringUtil.isEmpty(process.getFromNumAffilie()) || !JadeStringUtil.isEmpty(process.getToNumAffilie())) {
            headerNumAffilie = process.getFromNumAffilie() + " - " + process.getToNumAffilie();
        }
        container.put(ICEListeColumns.HEADER_NUM_AFFILIE, headerNumAffilie);
        container.put(ICEListeColumns.HEADER_ANNEE, process.getForAnnee());
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());
        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_BLANK_2, "");
        container.put(ICEListeColumns.HEADER_BLANK_3, "");
    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return
     * @throws HerculeException
     * @throws Exception
     */
    public static CommonExcelmlContainer loadResults(final CEEmployeurMasseSalarialeManager manager,
            final CEEmployeurMasseSalarialeProcess process) throws HerculeException, Exception {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        CEXmlmlMappingEmployeurMasseSalariale.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CEEmployeurMasseSalariale entity = (CEEmployeurMasseSalariale) manager.getEntity(i);
            process.incProgressCounter();

            CEXmlmlMappingEmployeurMasseSalariale.loadDetail(container, entity, process);
        }

        return container;
    }
}
