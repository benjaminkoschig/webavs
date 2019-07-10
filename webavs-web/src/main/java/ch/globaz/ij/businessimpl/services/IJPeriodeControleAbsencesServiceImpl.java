package ch.globaz.ij.businessimpl.services;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;
import java.util.Map.Entry;
import ch.globaz.ij.business.models.IJAbsence;
import ch.globaz.ij.business.models.IJAbsenceSearchModel;
import ch.globaz.ij.business.models.IJPeriodeControleAbsences;
import ch.globaz.ij.business.models.IJPeriodeControleAbsencesSearchModel;
import ch.globaz.ij.business.models.IJSimpleAbsence;
import ch.globaz.ij.business.models.IJSimpleAbsenceSearchModel;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsences;
import ch.globaz.ij.business.models.IJSimplePeriodeControleAbsence;
import ch.globaz.ij.business.services.IJPeriodeControleAbsencesService;
import ch.globaz.ij.business.services.IJServiceLocator;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;

public class IJPeriodeControleAbsencesServiceImpl implements IJPeriodeControleAbsencesService {

    @Override
    @SuppressWarnings("incomplete-switch")
    public void calculerJoursPayeEtSoldePourPeriode(IJPeriodeControleAbsences unePeriode)
            throws ServiceTechnicalException, ServiceBusinessException {
        JadePeriodWrapper periodeCouverte = new JadePeriodWrapper(unePeriode.getDateDeDebut(),
                unePeriode.getDateDeFin());

        // Calcul des jours d'interruption cumulé des absences
        int nombreJoursInterruption = 0;
        IJAbsenceSearchModel sm = new IJAbsenceSearchModel();
        sm.setForIdDossier(unePeriode.getDossier().getIdDossierControleAbsences());

        try {
            sm = IJServiceLocator.getAbsenceService().search(sm);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        } catch (JadeApplicationException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }

        for (JadeAbstractModel a : sm.getSearchResults()) {
            IJAbsence absence = (IJAbsence) a;
            JadePeriodWrapper dureeAbsence = new JadePeriodWrapper(absence.getDateDeDebut(), absence.getDateDeFin());
            switch (periodeCouverte.comparerChevauchement(dureeAbsence)) {
                case LesPeriodesSeChevauchent:
                    nombreJoursInterruption += Integer.valueOf(absence.getJoursInterruption());
                    break;
            }
        }

        int joursPayesSolde = 0;
        if (unePeriode.getJoursPayesSolde() != null) {
            joursPayesSolde = Integer.parseInt(unePeriode.getJoursPayesSolde());
        }
        unePeriode.setJoursPayes(String.valueOf(joursPayesSolde + nombreJoursInterruption));
        int solde = Integer.valueOf(unePeriode.getDroitIj()) - (joursPayesSolde + nombreJoursInterruption);
        unePeriode.setSolde(String.valueOf(solde));
    }

    private List<IJSimpleAbsence> chargerAbsencesPourDossier(IJSimpleDossierControleAbsences dossier)
            throws ServiceTechnicalException {
        IJSimpleAbsenceSearchModel searchModel = new IJSimpleAbsenceSearchModel();
        searchModel.setForIdDossierControleAbsence(dossier.getIdDossierControleAbsences());

        try {
            searchModel = (IJSimpleAbsenceSearchModel) JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        List<IJSimpleAbsence> absencesDuDossier = new ArrayList<IJSimpleAbsence>();
        for (JadeAbstractModel unResultat : searchModel.getSearchResults()) {
            absencesDuDossier.add((IJSimpleAbsence) unResultat);
        }
        return absencesDuDossier;
    }

    private List<IJPeriodeControleAbsences> chargerAbsencesPourLesPeriodes(List<IJPeriodeControleAbsences> periodes)
            throws ServiceTechnicalException {
        Map<IJSimpleDossierControleAbsences, List<IJPeriodeControleAbsences>> periodesParDossiers = new HashMap<>();
        for (IJPeriodeControleAbsences unePeriode : periodes) {

            if (periodesParDossiers.containsKey(unePeriode.getDossier())) {
                periodesParDossiers.get(unePeriode.getDossier()).add(unePeriode);
            } else {
                List<IJPeriodeControleAbsences> periodesDuDossier = new ArrayList<IJPeriodeControleAbsences>();
                periodesDuDossier.add(unePeriode);
                periodesParDossiers.put(unePeriode.getDossier(), periodesDuDossier);
            }
        }

        for (Entry<IJSimpleDossierControleAbsences, List<IJPeriodeControleAbsences>> uneEntree : periodesParDossiers
                .entrySet()) {
            List<IJSimpleAbsence> absencesDuDossier = chargerAbsencesPourDossier(uneEntree.getKey());
            for (IJPeriodeControleAbsences unePeriode : uneEntree.getValue()) {
                unePeriode.setAbsences(absencesDuDossier);
            }
        }

        List<IJPeriodeControleAbsences> listePeriodes = new ArrayList<>();
        for (List<IJPeriodeControleAbsences> uneListeDePeriodes : periodesParDossiers.values()) {
            listePeriodes.addAll(uneListeDePeriodes);
        }

        Collections.sort(listePeriodes, new Comparator<IJPeriodeControleAbsences>() {
            public int compare(IJPeriodeControleAbsences o1, IJPeriodeControleAbsences o2) {
                return o1.getPeriode().getOrdre().compareTo(o2.getPeriode().getOrdre());
            }
        });
        return listePeriodes;
    }

    @Override
    public int count(IJPeriodeControleAbsencesSearchModel search) throws ServiceBusinessException,
            ServiceTechnicalException {
        if (search == null) {
            throw new ServiceTechnicalException(
                    "IJPeriodeControleAbsencesServiceImpl : unable to count values because searchModel is null");
        }
        try {
            return JadePersistenceManager.count(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException("IJPeriodeControleAbsencesServiceImpl : unable to count values");
        }
    }

    @Override
    public IJPeriodeControleAbsences create(IJPeriodeControleAbsences entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        validateEntity(entity);
        IJSimplePeriodeControleAbsence returnedEntity = null;
        try {
            returnedEntity = (IJSimplePeriodeControleAbsence) JadePersistenceManager.add(entity.getPeriode());
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        if (returnedEntity != null) {
            entity.setPeriode(returnedEntity);
        }
        return entity;
    }

    @Override
    public IJPeriodeControleAbsences create(String idDossier) throws ServiceBusinessException,
            ServiceTechnicalException {
        IJPeriodeControleAbsences periode = new IJPeriodeControleAbsences();
        periode.getPeriode().setIdDossierControleAbsence(idDossier);

        IJPeriodeControleAbsencesSearchModel searchModel = new IJPeriodeControleAbsencesSearchModel();
        searchModel.setForIdDossier(idDossier);
        searchModel.setOrderKey("orderByOrdreAsc");
        searchModel = search(searchModel);

        // La liste est retourné dans l'ordre inverse (plus petit au plus grand

        String dateDebutNouvellePeriode = null;
        String dateDebutPeriode = null;
        if (searchModel.getSize() == 0) {
            IJSimpleDossierControleAbsences dossier;
            try {
                dossier = IJServiceLocator.getDossierControleAbsenceService().read(idDossier);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new ServiceTechnicalException(e.getMessage(), e);
            } catch (JadeApplicationException e) {
                throw new ServiceTechnicalException(e.getMessage(), e);
            } catch (JadePersistenceException e) {
                throw new ServiceTechnicalException(e.getMessage(), e);
            }

            dateDebutNouvellePeriode = dossier.getDateDebutFPI();
            if (JadeStringUtil.isBlankOrZero(dateDebutNouvellePeriode)) {
                dateDebutNouvellePeriode = dossier.getDateDebutIJAI();
            }

            // si aucune des dates n'est définie, on envoie une exception à l'utilisateur
            if (!JadeDateUtil.isGlobazDate(dateDebutNouvellePeriode)) {
                throw new ServiceBusinessException(translate("ERROR_AT_LEAST_ONE_OF_FPI_OR_IJAI_DATE_MUST_BE_SET"));
            }

            // On supprime 1 jour lorsqu'on récupère la date depuis le dossier de ctrl des absences
            Calendar calendar = JadeDateUtil.getGlobazCalendar(dateDebutNouvellePeriode);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            dateDebutPeriode = JadeDateUtil.getGlobazFormattedDate(calendar.getTime());

            periode.getPeriode().setOrdre("1");
            periode.getPeriode().setDroitIj("30");
        } else {
            IJPeriodeControleAbsences dernierePeriode = (IJPeriodeControleAbsences) searchModel.getSearchResults()[searchModel
                    .getSize() - 1];

            int ordreNouvellePeriode = Integer.parseInt(dernierePeriode.getOrdre()) + 1;
            periode.getPeriode().setOrdre(Integer.toString(ordreNouvellePeriode));
            dateDebutPeriode = dernierePeriode.getDateDeFin();
            dateDebutNouvellePeriode = JadeDateUtil.addDays(dernierePeriode.getDateDeFin(), 1);

            int droitIJ = Integer.parseInt(dernierePeriode.getDroitIj());
            if (droitIJ != 90) {
                droitIJ += 30;
            }
            periode.getPeriode().setDroitIj(Integer.toString(droitIJ));
        }
        periode.getPeriode().setDateDeDebut(dateDebutNouvellePeriode);
        periode.getPeriode().setDateDeFin(JadeDateUtil.addYears(dateDebutPeriode, 1));

        return this.create(periode);
    }

    @Override
    public IJPeriodeControleAbsences delete(IJPeriodeControleAbsences entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        validateEntity(entity);
        IJSimplePeriodeControleAbsence returnedEntity = null;
        try {
            returnedEntity = (IJSimplePeriodeControleAbsence) JadePersistenceManager.delete(entity.getPeriode());
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        if (returnedEntity != null) {
            entity.setPeriode(returnedEntity);
        }
        return entity;
    }

    @Override
    public boolean hasChevauchementDePeriode(String idDossierControle) throws ServiceTechnicalException,
            ServiceBusinessException {

        boolean result = false;
        IJPeriodeControleAbsencesSearchModel searchModel = new IJPeriodeControleAbsencesSearchModel();
        searchModel.setForIdDossier(idDossierControle);
        searchModel = search(searchModel);

        ArrayList<IJPeriodeControleAbsences> periodes = new ArrayList<IJPeriodeControleAbsences>();
        for (JadeAbstractModel x : searchModel.getSearchResults()) {
            periodes.add((IJPeriodeControleAbsences) x);
        }

        for (int ctr = 0; ctr < periodes.size(); ctr++) {

            JadePeriodWrapper periodeCouverte = new JadePeriodWrapper(periodes.get(ctr).getDateDeDebut(), periodes.get(
                    ctr).getDateDeFin());

            for (int ctx = (ctr + 1); ctx < periodes.size(); ctx++) {

                JadePeriodWrapper periodeATester = new JadePeriodWrapper(periodes.get(ctx).getDateDeDebut(), periodes
                        .get(ctx).getDateDeFin());

                switch (periodeCouverte.comparerChevauchement(periodeATester)) {
                    case LesPeriodesSeChevauchent:
                        result = true;
                        break;
                    default:
                        break;
                }
            }
        }
        return result;
    }

    @Override
    public IJPeriodeControleAbsences read(String idEntity) throws ServiceBusinessException, ServiceTechnicalException {
        IJPeriodeControleAbsences entity = new IJPeriodeControleAbsences();
        IJPeriodeControleAbsences returnedEntity = null;
        entity.setId(idEntity);
        entity.setAbsences(chargerAbsencesPourDossier(entity.getDossier()));
        try {
            returnedEntity = (IJPeriodeControleAbsences) JadePersistenceManager.read(entity);
            calculerJoursPayeEtSoldePourPeriode(returnedEntity);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnedEntity;
    }

    @Override
    public IJPeriodeControleAbsencesSearchModel search(IJPeriodeControleAbsencesSearchModel search)
            throws ServiceBusinessException, ServiceTechnicalException {
        try {
            search.setOrderKey("orderByOrdreDesc");
            search = (IJPeriodeControleAbsencesSearchModel) JadePersistenceManager.search(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }

        // On liste les périodes
        List<IJPeriodeControleAbsences> periodes = new ArrayList<IJPeriodeControleAbsences>();
        for (JadeAbstractModel unResultat : search.getSearchResults()) {
            periodes.add((IJPeriodeControleAbsences) unResultat);
        }
        periodes = chargerAbsencesPourLesPeriodes(periodes);

        for (IJPeriodeControleAbsences unePeriode : periodes) {
            calculerJoursPayeEtSoldePourPeriode(unePeriode);
        }
        String messageWarningSoldeNegatif = translate("WARNING_SOLDE_NEGATIF");
        for (JadeAbstractModel unResultat : search.getSearchResults()) {
            ((IJPeriodeControleAbsences) unResultat).setMessageWarningSoldeNegatif(messageWarningSoldeNegatif);
        }

        search.setSearchResults(periodes.toArray(new IJPeriodeControleAbsences[periodes.size()]));
        return search;
    }

    private String translate(String messageKey) {
        return BSessionUtil.getSessionFromThreadContext().getLabel(messageKey);
    }

    @Override
    public IJPeriodeControleAbsences update(IJPeriodeControleAbsences entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        IJSimplePeriodeControleAbsence simplePeriode = null;
        IJPeriodeControleAbsences tmp = null;

        try {

            tmp = read(entity.getIdPeriodeControleAbsence());

            if (tmp == null) {
                throw new ServiceTechnicalException("Unable to retreive entity with id : "
                        + entity.getIdPeriodeControleAbsence());
            }

            // Si le délais d'attente est différent, il faut recalculer la durée de la période
            int newDelaisAttente = 0;
            if (!JadeStringUtil.isEmpty(entity.getDelaisAttente())) {
                newDelaisAttente = Integer.valueOf(entity.getDelaisAttente());
            }
            int oldDelaisAttente = 0;
            if (!JadeStringUtil.isEmpty(tmp.getDelaisAttente())) {
                oldDelaisAttente = Integer.valueOf(tmp.getDelaisAttente());
            }
            if (newDelaisAttente != oldDelaisAttente) {
                int diff = newDelaisAttente - oldDelaisAttente;
                String dateDeFin = entity.getDateDeFin();
                String newDateDeFin = JadeDateUtil.addDays(dateDeFin, diff);
                entity.getPeriode().setDateDeFin(newDateDeFin);
            }

            simplePeriode = (IJSimplePeriodeControleAbsence) JadePersistenceManager.update(entity.getPeriode());
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        if (entity != null) {
            entity.setPeriode(simplePeriode);
        }
        return entity;
    }

    private void validateEntity(IJPeriodeControleAbsences entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        // Date de début
        if (!JadeDateUtil.isGlobazDate(entity.getDateDeDebut())) {
            throw new ServiceBusinessException(translate("ERROR_START_DATE_IS_EMPTY"));
        }
        // Date de fin
        if (!JadeDateUtil.isGlobazDate(entity.getDateDeFin())) {
            throw new ServiceBusinessException(translate("ERROR_END_DATE_IS_EMPTY"));
        }
        // Précédence de la date de début
        if (!JadeDateUtil.isDateAfter(entity.getDateDeFin(), entity.getDateDeDebut())) {
            throw new ServiceBusinessException(translate("ERROR_START_DATE_IS_NOT_BEFORE_END_DATE"));
        }
        if (JadeStringUtil.isEmpty(entity.getDroitIj())) {
            throw new ServiceBusinessException(translate("ERROR_FIELD_DROIT_IJ_NOT_FILLED"));
        }
    }
}
