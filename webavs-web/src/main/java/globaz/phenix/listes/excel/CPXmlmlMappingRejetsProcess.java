package globaz.phenix.listes.excel;

import globaz.globall.util.JAException;
import globaz.helios.tools.TimeHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPRejets;
import globaz.phenix.db.communications.CPRejetsListViewBean;

/**
 * @revision SCO 15 déc. 2010
 */
public class CPXmlmlMappingRejetsProcess {

    private static void loadDetail(int i, PhenixContainer container, CPRejets entity, CPListeRejetsProcess process)
            throws Exception {
        process.incProgressCounter();
        container.put(ICPListeColumns.YOUR_BUSINESS, entity.getYourBusinessReferenceId());
        container.put(ICPListeColumns.NOM, entity.getNom() + " " + entity.getPrenom());
        container.put(ICPListeColumns.ANNEE, entity.getAnnee());
        container.put(ICPListeColumns.CANTON, entity.getCodeCantonFisc());
        container.put(ICPListeColumns.ETAT_CIVIL, entity.getEtatCivilLibelle());
        container.put(ICPListeColumns.REMARK, entity.getRemark());
        container.put(ICPListeColumns.REJET, entity.getRejetVisible());
        container.put(ICPListeColumns.ETAT, process.getSession().getCodeLibelle(entity.getEtat()));
    }

    private static void loadHeader(PhenixContainer container, CPListeRejetsProcess process) throws JAException {
        String nomCaisse = "";
        try {
            nomCaisse = process.getSession().getApplication()
                    .getProperty("COMPANYNAME_" + process.getSession().getIdLangueISO().toUpperCase());
        } catch (Exception e) {
            nomCaisse = "";
        }
        container.put("caisse", nomCaisse);
        container.put("headerBlank1", " ");
        container.put(ICPListeColumns.HEADER_TITRE, process.getSession().getLabel("TITRE_LISTE_COMMUNICATION_REJETS"));
        container.put(ICPListeColumns.HEADER_NUM_INFOROM, "0292CCP");
        if (!JadeStringUtil.isEmpty(process.getManager().getForCanton())) {
            container.put(ICPListeColumns.HEADER_CANTON,
                    process.getSession().getCodeLibelle(process.getManager().getForCanton()));
        } else {
            container.put(ICPListeColumns.HEADER_CANTON, " ");
        }
        if (!JadeStringUtil.isEmpty(process.getManager().getForEtat())) {
            container.put(ICPListeColumns.HEADER_ETAT,
                    process.getSession().getCodeLibelle(process.getManager().getForEtat()));
        } else {
            container.put(ICPListeColumns.HEADER_ETAT, " ");
        }
        if (!JadeStringUtil.isEmpty(process.getManager().getForReasonOfRejection())) {
            container.put(ICPListeColumns.HEADER_RAISON, process.getManager().getRejetVisible());
        } else {
            container.put(ICPListeColumns.HEADER_RAISON, " ");
        }
        container.put(ICPListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());
    }

    public static PhenixContainer loadResults(CPRejetsListViewBean manager, CPListeRejetsProcess process)
            throws Exception, Exception {
        PhenixContainer container = new PhenixContainer();

        CPXmlmlMappingRejetsProcess.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CPRejets entity = (CPRejets) manager.getEntity(i);
            CPXmlmlMappingRejetsProcess.loadDetail(i, container, entity, process);
        }

        return container;
    }
}
