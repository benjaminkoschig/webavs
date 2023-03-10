package ch.globaz.pegasus.businessimpl.services.doc.excel;

import ch.globaz.pegasus.businessimpl.services.doc.excel.impl.*;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService;

public class ListeDeControleServiceImpl implements ListeDeControleService {

    @Override
    public String createListeAllocationsFamiliales(String date) throws Exception {
        try {
            ListeAllocationsFamiliales liste = new ListeAllocationsFamiliales();
            return liste.createDocAndSave(date);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String createListeControleAllocationNoel(String idExecutiontProcess) throws Exception {
        ListeControleAllocationNoel list = new ListeControleAllocationNoel();
        return list.createDocAndSave(idExecutiontProcess);
    }

    @Override
    public String createListeControleProcessAdaptation(String idExecutiontProcess) throws Exception {
        ListeControleProcessAdaptation list = new ListeControleProcessAdaptation();
        return list.createDocAndSave(idExecutiontProcess);
    }


    @Override
    public String createListeDecisionsValidees(String dateDebut, String dateFin) throws Exception {
        try {
            ListeDecisionsValidees liste = new ListeDecisionsValidees();
            return liste.createDocAndSave(dateDebut, dateFin);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String createListeDemandesPC(String dateDebut, String dateFin, String idGestionnaire) throws Exception {
        try {
            ListeDemandesPC liste = new ListeDemandesPC();
            return liste.createDocAndSave(dateDebut, dateFin, idGestionnaire);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String createListeMutationMontantPCA(String date) throws Exception {
        ListeMutationMontantPCA liste = new ListeMutationMontantPCA();
        return liste.createDocAndSave(date);
    }

    @Override
    public ExcelmlWorkbook createListeOrdreDeVersement(String idLot) throws DocException {
        ListeOrdresVersement listeOrdresVersement = new ListeOrdresVersement();
        return listeOrdresVersement.createDoc(idLot);
    }

    @Override
    public String createListeOrdreDeVersementAndSave(String idLot) throws Exception {
        ListeOrdresVersement listeOrdresVersement = new ListeOrdresVersement();
        return listeOrdresVersement.createDocAndSave(idLot);
    }

    @Override
    public String createListeRecap(String date) throws Exception {
        ListeMutationPca listeMutationPca = new ListeMutationPca();
        return listeMutationPca.createDocAndSave(date);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService#createListeRevisions(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String createListeRevisions(String annee, String moisAnnee) throws Exception {
        ListeExcelRevisions listeRevisions = new ListeExcelRevisions();
        return listeRevisions.createDocAndSave(annee, moisAnnee);
    }

    @Override
    public String createListeEnfants11Ans(String dateDebut, String dateFin) throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        ListeEnfants11Ans listeEnfants11Ans = new ListeEnfants11Ans();
        listeEnfants11Ans.setSession(session);
        return listeEnfants11Ans.createDocAndSave(dateDebut, dateFin);
    }

}
