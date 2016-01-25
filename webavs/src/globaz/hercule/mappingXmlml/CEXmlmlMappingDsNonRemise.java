package globaz.hercule.mappingXmlml;

import globaz.draco.db.declaration.DSSaisieMasseAutomatiqueViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.groupement.CEMembreGroupe;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.controleEmployeur.CEDsNonRemiseProcess;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;

/**
 * @author JPA
 * @revision SCO 10 déc. 2010
 */
public class CEXmlmlMappingDsNonRemise {

    private static AFAffiliation getAffiliation(final BSession session, final String numeroAffilie, final String annee)
            throws Exception {
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        AFAffiliation affiliation = application.getAffilieByNo(session, numeroAffilie, true, false, "", "", annee, "",
                "");
        if (affiliation == null) {
            throw new Exception(DSSaisieMasseAutomatiqueViewBean.class.getName() + "\n"
                    + (session).getLabel("AUCUNE_AFFILIATION_EXISTE"));
        }

        return affiliation;
    }

    /**
     * Methode permettant de remplir le corps du document
     * 
     * @param container
     * @param manager
     * @param process
     * @throws HerculeException
     */
    private static void loadDetail(final HerculeContainer container, final LUJournalViewBean entity,
            final CEDsNonRemiseProcess process) throws HerculeException {

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.NUM_AFFILIE, entity.getLibelle(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.NOM, entity.getDestinataire(), "");

        // On renseigne l'adresse
        CEExcelmlUtils.renseigneAdresse(process.getSession(), process.getTypeAdresse(), container,
                entity.getIdDestinataire(), entity.getLibelle());

        // On récupère l'affiliation
        AFAffiliation affiliation;
        try {
            affiliation = CEXmlmlMappingDsNonRemise.getAffiliation(process.getSession(), entity.getLibelle(),
                    process.getAnnee());
        } catch (Exception e) {
            throw new HerculeException("unable to retrieve the affiliation. (Num Affilie = " + entity.getLibelle()
                    + ")", e);
        }

        if ((affiliation != null) && !affiliation.isNew()) {
            // Récupération du groupe
            CEMembreGroupe groupe = CEControleEmployeurService.findGroupeForIdAffilie(process.getSession(),
                    process.getTransaction(), affiliation.getAffiliationId());
            if (groupe != null) {
                container.put(ICEListeColumns.NOM_GROUPE, groupe.getLibelleGroupe());
            } else {
                container.put(ICEListeColumns.NOM_GROUPE, "");
            }
            // Date de debut d'affiliation
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DEBUT_PERIODE_AFFILIATION,
                    affiliation.getDateDebut(), "");

            // Date de fin d'affiliation
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.FIN_PERIODE_AFFILIATION, affiliation.getDateFin(),
                    "");

            // Masse
            double masse = CEControleEmployeurService.retrieveMasse(process.getSession(),
                    CEUtils.getAnneePrecedente(1, process.getAnnee()), entity.getLibelle());
            container.put(ICEListeColumns.MASSE, String.valueOf(masse));

            loadInfosSuva(container, process, affiliation.getCodeSUVA());

        } else {
            container.put(ICEListeColumns.NOM_GROUPE, "");
            container.put(ICEListeColumns.DEBUT_PERIODE_AFFILIATION, "");
            container.put(ICEListeColumns.FIN_PERIODE_AFFILIATION, "");
            container.put(ICEListeColumns.MASSE, "0");
            container.put(ICEListeColumns.CODE_SUVA, "");
            container.put(ICEListeColumns.LIBELLE_SUVA, "");
        }

        container.put(ICEListeColumns.ANNEE_PREVUE, "");
        container.put(ICEListeColumns.REVISEUR, "");
    }

    private static void loadInfosSuva(final HerculeContainer container, final CEDsNonRemiseProcess process,
            final String codeSuva) {
        // Code suva
        if (!JadeStringUtil.isEmpty(codeSuva)) {

            FWParametersUserCode userCode = new FWParametersUserCode();
            userCode.setIdCodeSysteme(codeSuva);
            userCode.setIdLangue(process.getSession().getIdLangue());
            try {
                userCode.retrieve(process.getTransaction());
            } catch (Exception e) {
                JadeLogger.error(e, "Unabled to retrieve code systeme SUVA : " + codeSuva);
            }

            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, userCode.getCodeUtilisateur(), "");
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, userCode.getLibelle(), "");
        } else {
            container.put(ICEListeColumns.CODE_SUVA, "");
            container.put(ICEListeColumns.LIBELLE_SUVA, "");
        }
    }

    /**
     * Methode permettant de remplir le header
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(final HerculeContainer container, final CEDsNonRemiseProcess process) {

        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());

        // On set le header
        if (!JadeStringUtil.isEmpty(process.getAnnee())) {
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_ANNEE, process.getAnnee(), "");
            container.put(
                    ICEListeColumns.HEADER_MASSE_SALARIALE,
                    process.getSession().getLabel("LISTE_CONTROLE_HEADER_MASSE") + " "
                            + CEUtils.getAnneePrecedente(1, process.getAnnee()));
        }
        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_BLANK_2, "");
        container.put(ICEListeColumns.HEADER_BLANK_3, "");

        // Numéro inforom
        container.put(ICEListeColumns.HEADER_NUM_INFOROM, CEDsNonRemiseProcess.NUMERO_INFOROM);

        // Libellé
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                process.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));
    }

    /**
     * @param manager
     * @param process
     * @return
     * @throws HerculeException
     */
    public static HerculeContainer loadResults(final LUJournalListViewBean manager, final CEDsNonRemiseProcess process)
            throws HerculeException {
        HerculeContainer container = new HerculeContainer();

        // On remplit le header
        CEXmlmlMappingDsNonRemise.loadHeader(container, process);

        // Pour chaque occurence, on charge les détails
        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            LUJournalViewBean entity = (LUJournalViewBean) manager.getEntity(i);
            process.incProgressCounter();

            CEXmlmlMappingDsNonRemise.loadDetail(container, entity, process);
        }

        return container;
    }
}
