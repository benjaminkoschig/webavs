package ch.globaz.libra.businessimpl.services;

import globaz.libra.db.journalisations.LIJournalisationsJointDossiers;
import globaz.libra.db.journalisations.LIJournalisationsSearchDossiers;
import globaz.libra.interfaces.LIJournalisationsInterfaces;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.model.Journalisation;
import ch.globaz.libra.business.model.JournalisationSearch;
import ch.globaz.libra.business.services.JournalisationService;
import ch.globaz.pyxis.common.Messages;

public class JournalisationServiceImpl implements JournalisationService {
    LIJournalisationsInterfaces journalisation = new LIJournalisationsInterfaces();

    private Journalisation _adapt(LIJournalisationsJointDossiers entity) {
        Journalisation journalisation = new Journalisation();
        journalisation.setCsDomaine(entity.getCsDomaine());
        journalisation.setCsEtat(entity.getCsEtat());

        journalisation.setCsNationalite(entity.getCsNationalite());

        journalisation.setCsSexe(entity.getCsSexe());

        journalisation.setDateDeces(entity.getDateDeces());
        journalisation.setDateNaissance(entity.getDateNaissance());
        journalisation.setDateRappel(entity.getDateRappel());
        journalisation.setDateReception(entity.getDateReception());
        journalisation.setIdDossier(entity.getIdDossier());
        journalisation.setIdGestionnaire(entity.getIdGestionnaire());
        journalisation.setIdJournalisation(entity.getIdJournalisation());
        journalisation.setIdTiers(entity.getIdTiers());
        journalisation.setLibelle(entity.getLibelle());
        journalisation.setLibelleAffichage(entity.getLibelleAffichage());
        journalisation.setLibelleGroupe(entity.getLibelleGroupe());
        journalisation.setNoAVS(entity.getNoAVS());
        journalisation.setNom(entity.getNom());
        journalisation.setPrenom(entity.getPrenom());
        journalisation.setVisaGestionnaire(entity.getVisaGestionnaire());
        journalisation.setIdExterne(entity.getIdExterne());

        return journalisation;
    }

    private LIJournalisationsSearchDossiers _adaptSearch(JournalisationSearch search) {
        LIJournalisationsSearchDossiers manager = new LIJournalisationsSearchDossiers();
        manager.setForCsType(search.getForCsType());
        manager.setForDateDebut(search.getForDateReceptionInitial());
        manager.setForDateFin(search.getForDateReceptionFinal());
        manager.setForIdDomaine(search.getForIdDomaine());
        manager.setForIdDossier(search.getForIdDossier());
        manager.setForIdGroupe(search.getForIdGroupe());
        manager.setForIdExtern(search.getForIdExtern());
        manager.setForIdUtilisateur(search.getForIdUtilisateur());
        manager.setForLibelle(search.getForLibelle());
        return manager;
    }

    @Override
    public void createJournalisation(String idExterne, String libelle, boolean isDossier) throws LibraException {
        try {
            journalisation.createJournalisation(LibraUtil.getCurrentTransaction(), idExterne, libelle, isDossier);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

    @Override
    public void createJournalisationAvecRemarque(String idExterne, String libelle, String remarque, boolean isDossier)
            throws LibraException {
        try {
            journalisation.createJournalisationAvecRemarque(LibraUtil.getCurrentTransaction(), idExterne, libelle,
                    remarque, isDossier);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

    @Override
    public void createJournalisationAvecRemarqueWithTestDossier(String idExterne, String libelle, String remarque,
            String idTiers, String csDomaine, boolean isDossier) throws LibraException {
        try {
            journalisation.createJournalisationAvecRemarqueWithTestDossier(LibraUtil.getCurrentTransaction(),
                    idExterne, libelle, remarque, idTiers, csDomaine, isDossier);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

    @Override
    public void createJournalisationWithTestDossier(String idExterne, String libelle, String idTiers, String csDomaine,
            boolean isDossier) throws LibraException {
        try {
            journalisation.createJournalisationWithTestDossier(LibraUtil.getCurrentTransaction(), idExterne, libelle,
                    idTiers, csDomaine, isDossier);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

    @Override
    public List<Journalisation> search(JournalisationSearch search, int mgr_size) throws LibraException {
        List<Journalisation> list = new ArrayList<Journalisation>();

        if (search == null) {
            throw new LibraException(Messages.MODEL_IS_NULL + "JournalisationSearch ");
        }

        LIJournalisationsSearchDossiers manager = _adaptSearch(search);
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