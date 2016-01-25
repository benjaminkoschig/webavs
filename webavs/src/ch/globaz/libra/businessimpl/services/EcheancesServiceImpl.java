package ch.globaz.libra.businessimpl.services;

import globaz.globall.db.BManager;
import globaz.libra.db.journalisations.LIEcheancesJointDossiersManager;
import globaz.libra.db.journalisations.LIJournalisationsJointDossiers;
import globaz.libra.interfaces.LIEcheancesInterfaces;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.model.Echeance;
import ch.globaz.libra.business.model.EcheanceSearch;
import ch.globaz.libra.business.services.EcheancesService;
import ch.globaz.pyxis.common.Messages;

public class EcheancesServiceImpl implements EcheancesService {
    LIEcheancesInterfaces echeance = new LIEcheancesInterfaces();

    private Echeance _adapt(LIJournalisationsJointDossiers entity) {
        Echeance echeance = new Echeance();
        echeance.setCsDomaine(entity.getCsDomaine());
        echeance.setCsEtat(entity.getCsEtat());

        echeance.setCsNationalite(entity.getCsNationalite());

        echeance.setCsSexe(entity.getCsSexe());

        echeance.setDateDeces(entity.getDateDeces());
        echeance.setDateNaissance(entity.getDateNaissance());
        echeance.setDateRappel(entity.getDateRappel());
        echeance.setDateReception(entity.getDateReception());
        echeance.setIdDossier(entity.getIdDossier());
        echeance.setIdGestionnaire(entity.getIdGestionnaire());
        echeance.setIdJournalisation(entity.getIdJournalisation());
        echeance.setIdTiers(entity.getIdTiers());
        echeance.setLibelle(entity.getLibelle());
        echeance.setLibelleAffichage(entity.getLibelleAffichage());
        echeance.setLibelleGroupe(entity.getLibelleGroupe());
        echeance.setNoAVS(entity.getNoAVS());
        echeance.setNom(entity.getNom());
        echeance.setPrenom(entity.getPrenom());
        echeance.setVisaGestionnaire(entity.getVisaGestionnaire());
        echeance.setIdExterne(entity.getIdExterne());

        return echeance;
    }

    private LIEcheancesJointDossiersManager _adaptSearch(EcheanceSearch search) {
        LIEcheancesJointDossiersManager manager = new LIEcheancesJointDossiersManager();
        manager.setForCsType(search.getForCsType());

        manager.setForDateDebut(search.getForDateDebut());
        manager.setForDateFin(search.getForDateFin());
        manager.setForIdDomaine(search.getForIdDomaine());
        manager.setForIdDossier(search.getForIdDossier());
        manager.setForIdGroupe(search.getForIdGroupe());
        manager.setForIdExtern(search.getForIdExtern());
        manager.setForIdUtilisateur(search.getForIdUtilisateur());
        return manager;
    }

    public int count(EcheanceSearch search) throws LibraException {
        int count = 0;
        try {
            count = _adaptSearch(search).getCount();
        } catch (Exception e) {
            throw new LibraException("Unable to count LIService!", e);
        }
        return count;
    }

    @Override
    public String createManuellWithTestDossier(String dateRappel, String idExterne, String libelle, String remarque,
            String idTiers, String csDomaine, boolean isDossier) throws Exception {

        echeance.createManuelWithTestDossier(LibraUtil.getCurrentTransaction(), dateRappel, idExterne, libelle,
                remarque, idTiers, csDomaine, isDossier);
        return csDomaine;

    }

    @Override
    public void createRappel(String dateRappel, String idExterne, String libelle, boolean isDossier)
            throws LibraException {
        try {
            echeance.createRappel(LibraUtil.getCurrentTransaction(), dateRappel, idExterne, libelle, isDossier);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }

    }

    @Override
    public void createRappelWithTestDossier(String dateRappel, String idExterne, String libelle, String idTiers,
            String csDomaine, boolean isDossier) throws LibraException {
        try {
            echeance.createRappelWithTestDossier(LibraUtil.getCurrentTransaction(), dateRappel, idExterne, libelle,
                    idTiers, csDomaine, isDossier);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

    @Override
    public List<Echeance> search(EcheanceSearch search) throws LibraException {
        return this.search(search, BManager.SIZE_USEDEFAULT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.libra.business.services.EcheancesService#search(ch.globaz.libra.business.model.EcheanceSearch,
     * int)
     */
    @Override
    public List<Echeance> search(EcheanceSearch search, int mgr_size) throws LibraException {
        if (search == null) {
            throw new LibraException(Messages.MODEL_IS_NULL + "EcheanceSearch ");
        }
        List<Echeance> list = new ArrayList<Echeance>();
        LIEcheancesJointDossiersManager manager = _adaptSearch(search);
        try {
            manager.find(mgr_size);
            for (int i = 0; i < manager.size(); i++) {
                LIJournalisationsJointDossiers entity = (LIJournalisationsJointDossiers) manager.getEntity(i);
                list.add(_adapt(entity));
            }
        } catch (Exception e) {
            throw new LibraException("Unable to search LIService!", e);
        }
        return list;
    }
}
