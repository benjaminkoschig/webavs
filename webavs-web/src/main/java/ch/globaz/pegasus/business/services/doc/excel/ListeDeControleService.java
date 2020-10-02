package ch.globaz.pegasus.business.services.doc.excel;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import ch.globaz.pegasus.business.exceptions.doc.DocException;

public interface ListeDeControleService extends JadeApplicationService {

    String createListeAllocationsFamiliales(String date) throws Exception;

    String createListeControleAllocationNoel(String idExecutiontProcess) throws Exception;

    String createListeDecisionsValidees(String dateDebut, String dateFin) throws Exception;

    String createListeDemandesPC(String dateDebut, String dateFin, String idGestionnaire) throws Exception;

    String createListeMutationMontantPCA(String date) throws Exception;

    ExcelmlWorkbook createListeOrdreDeVersement(String idLot) throws DocException;

    String createListeOrdreDeVersementAndSave(String idLot) throws Exception;

    String createListeRecap(String date) throws Exception;

    /**
     * Cree la liste de revision pour une periode donnee (un mois ou toute une annee).
     * 
     * @param annee
     *            l'annee de la revision
     * @param moisAnnee
     *            le mois de la revision
     * @return
     * @throws Exception
     */
    String createListeRevisions(String annee, String moisAnnee) throws Exception;

    String createListeControleProcessAdaptation(String idExecutiontProcess) throws Exception;

    String createListeEnfants11Ans(String dateDebut, String dateFin) throws Exception;


}
