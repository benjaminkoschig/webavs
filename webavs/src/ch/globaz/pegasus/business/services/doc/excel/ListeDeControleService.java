package ch.globaz.pegasus.business.services.doc.excel;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import ch.globaz.pegasus.business.exceptions.doc.DocException;

public interface ListeDeControleService extends JadeApplicationService {

    public String createListeAllocationsFamiliales(String date) throws Exception;

    public String createListeControleAllocationNoel(String idExecutiontProcess) throws Exception;

    public String createListeDecisionsValidees(String dateDebut, String dateFin) throws Exception;

    public String createListeDemandesPC(String dateDebut, String dateFin, String idGestionnaire) throws Exception;

    public String createListeMutationMontantPCA(String date) throws Exception;

    public ExcelmlWorkbook createListeOrdreDeVersement(String idLot) throws DocException;

    public String createListeOrdreDeVersementAndSave(String idLot) throws Exception;

    public String createListeRecap(String date) throws Exception;

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
    public String createListeRevisions(String annee, String moisAnnee) throws Exception;

    String createListeControleProcessAdaptation(String idExecutiontProcess) throws Exception;
}
