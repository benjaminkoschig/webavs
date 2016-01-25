package ch.globaz.vulpecula.external.services;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.naos.api.IAFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.naos.business.model.TauxAssuranceSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Age;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.AssuranceTauxComplexModel;
import ch.globaz.vulpecula.external.models.AssuranceTauxSearchComplexModel;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;
import ch.globaz.vulpecula.external.models.CotisationParametreComplexModel;
import ch.globaz.vulpecula.external.models.CotisationParametreSearchComplexModel;
import ch.globaz.vulpecula.external.models.CotisationSearchComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.CotisationConverter;

public class CotisationServiceImpl implements CotisationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CotisationServiceImpl.class);

    private static final String ORDER_BY_DATE_DEBUT_DESC = "dateDebutDesc";

    @Override
    public Taux findTaux(String idCotisation, Date date) {
        AFCotisation afCotisation = findAFCotisation(idCotisation, date);
        String taux = afCotisation.getTaux(JadeDateUtil.getDMYDate(new java.util.Date(date.getTime())), null);
        if (taux != null && taux.length() > 0) {
            return new Taux(taux);
        } else {
            return new Taux(0.0);
        }
    }

    @Override
    public AFCotisation findAFCotisation(String idCotisation, Date date) {
        AFCotisation afCotisation = new AFCotisation();
        afCotisation.setCotisationId(idCotisation);
        try {
            afCotisation.retrieve();
            return afCotisation;
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public Taux findTauxForEmployeurAndType(String idAffilie, TypeAssurance type, Date date) {
        Taux taux = new Taux(0);
        List<Cotisation> cotisations = findByIdAffilieForDate(idAffilie, date);
        for (Cotisation cotisation : cotisations) {
            if (type.equals(cotisation.getAssurance().getTypeAssurance())) {
                taux = taux.addTaux(findTaux(cotisation.getId(), date));
            }
        }
        return taux;
    }

    @Override
    public List<Cotisation> findByIdAffilieForDate(String id, Date date) {
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        CotisationSearchComplexModel searchModel = new CotisationSearchComplexModel();
        searchModel.setForDateDebutLessEquals(date.getSwissValue());
        searchModel.setForDateFinGreaterEquals(date.getSwissValue());
        searchModel.setForIdAffilie(String.valueOf(id));
        try {
            JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                CotisationComplexModel cotisationComplexModel = (CotisationComplexModel) model;
                Cotisation cotisation = CotisationConverter.convertToDomain(cotisationComplexModel);
                cotisations.add(cotisation);
            }
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
        return cotisations;
    }

    @Override
    public List<Cotisation> findByIdAffilie(String id) {
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        CotisationSearchComplexModel searchModel = new CotisationSearchComplexModel();
        searchModel.setForIdAffilie(String.valueOf(id));
        try {
            JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                CotisationComplexModel cotisationComplexModel = (CotisationComplexModel) model;
                Cotisation cotisation = CotisationConverter.convertToDomain(cotisationComplexModel);
                cotisations.add(cotisation);
            }
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
        return cotisations;
    }

    @Override
    public List<Cotisation> findByIdAffilieForDate(String id, Date dateDebut, Date dateFin) {
        List<Cotisation> cotisationsActives = new ArrayList<Cotisation>();
        CotisationSearchComplexModel searchModel = new CotisationSearchComplexModel();
        searchModel.setWhereKey(CotisationComplexModel.SEARCH_BY_ID);
        searchModel.setForIdAffilie(id);
        List<Cotisation> cotisations = searchAndFetch(searchModel);

        for (Cotisation cotisation : cotisations) {
            Date dateDebutCotisation = cotisation.getDateDebut();
            Date dateFinCotisation = cotisation.getDateFin();
            if (dateFin == null && dateFinCotisation == null) {
                cotisationsActives.add(cotisation);
            } else if (dateFin == null) {
                if (dateFinCotisation.afterOrEquals(dateDebut)) {
                    cotisationsActives.add(cotisation);
                }
            } else if (dateFinCotisation == null) {
                if (dateFin.afterOrEquals(dateDebutCotisation)) {
                    cotisationsActives.add(cotisation);
                }
            } else if (dateDebutCotisation.beforeOrEquals(dateFin) && dateFinCotisation.afterOrEquals(dateDebut)) {
                cotisationsActives.add(cotisation);
            }
        }
        return cotisationsActives;
    }

    protected List<Cotisation> searchAndFetch(CotisationSearchComplexModel searchModel) {
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        try {
            JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                CotisationComplexModel cotisationComplexModel = (CotisationComplexModel) model;
                Cotisation cotisation = CotisationConverter.convertToDomain(cotisationComplexModel);
                cotisations.add(cotisation);
            }
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
        return cotisations;
    }

    @Override
    public String findLibelleCotisation(String idCotisation, Locale userLocale) {
        AFCotisation afCotisation = new AFCotisation();
        afCotisation.setCotisationId(idCotisation);
        try {
            afCotisation.retrieve();
            if (Locale.GERMAN.equals(userLocale)) {
                return afCotisation.getAssurance().getAssuranceLibelleAl();
            } else if (Locale.ITALIAN.equals(userLocale)) {
                return afCotisation.getAssurance().getAssuranceLibelleIt();
            } else {
                return afCotisation.getAssurance().getAssuranceLibelleFr();
            }
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public boolean isEnAgeDeCotiser(Travailleur travailleur, String idCotisation, String anneeDebut) {
        return isEnAgeDeCotiser(travailleur, idCotisation, new Date("01.01." + anneeDebut));
    }

    @Override
    public boolean isEnAgeDeCotiser(Travailleur travailleur, String idCotisation, Date date) {
        CotisationParametreSearchComplexModel search = new CotisationParametreSearchComplexModel();
        search.setForIdCotisation(idCotisation);
        try {
            JadePersistenceManager.search(search);
            for (JadeAbstractModel cotisationParametre : search.getSearchResults()) {
                CotisationParametreComplexModel param = (CotisationParametreComplexModel) cotisationParametre;

                if (param.getParametreAssuranceSimpleModel().getGenre().equals(IAFAssurance.GEN_PARAM_ASS_AGE_MAX)) {
                    if (isDateParametreValide(date, param)) {
                        if (travailleur.getSexe() != null) {
                            if (!JadeNumericUtil.isEmptyOrZero(param.getParametreAssuranceSimpleModel().getSexe())) {
                                if (travailleur.getSexe().equals(param.getParametreAssuranceSimpleModel().getSexe())) {
                                    Age age = new Age(param.getParametreAssuranceSimpleModel().getValeurNum());
                                    if (travailleur.getAge(date) >= age.getValue()) {
                                        return false;
                                    }
                                }
                            } else {
                                Age age = new Age(param.getParametreAssuranceSimpleModel().getValeurNum());
                                if (travailleur.getAge(date) >= age.getValue()) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (param.getParametreAssuranceSimpleModel().getGenre().equals(IAFAssurance.GEN_PARAM_ASS_AGE_MIN)) {
                    if (isDateParametreValide(date, param)) {
                        if (travailleur.getSexe() != null) {
                            if (!JadeNumericUtil.isEmptyOrZero(param.getParametreAssuranceSimpleModel().getSexe())) {
                                if (travailleur.getSexe().equals(param.getParametreAssuranceSimpleModel().getSexe())) {
                                    Age age = new Age(param.getParametreAssuranceSimpleModel().getValeurNum());
                                    if (travailleur.getAge(date) < age.getValue()) {
                                        return false;
                                    }
                                }
                            } else {
                                Age age = new Age(param.getParametreAssuranceSimpleModel().getValeurNum());
                                if (travailleur.getAgeNonRevolu(date) < age.getValue()) {
                                    return false;
                                }
                            }

                        }
                    }
                }
            }
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }

        return true;
    }

    private boolean isDateParametreValide(Date datePrestations, CotisationParametreComplexModel param) {
        Date dateDebutParam = new Date(param.getParametreAssuranceSimpleModel().getDateDebut());
        if (param.getParametreAssuranceSimpleModel().getDateDebut() == null
                || param.getParametreAssuranceSimpleModel().getDateDebut().equals("")) {
            return true;
        }
        if (param.getParametreAssuranceSimpleModel().getDateFin() == null
                || param.getParametreAssuranceSimpleModel().getDateFin().equals("")) {
            if (datePrestations.after(dateDebutParam)) {
                return true;
            }
        } else {
            Date dateFinParam = new Date(param.getParametreAssuranceSimpleModel().getDateFin());
            if (datePrestations.after(dateDebutParam) && datePrestations.before(dateFinParam)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public TauxAssuranceSimpleModel findTauxParitaireAVS() {
        return findTauxParitaireAVS(Date.now());
    }

    @Override
    public TauxAssuranceSimpleModel findTauxParitaireAVS(Date date) {
        return findTauxParitaire(date, CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
    }

    @Override
    public TauxAssuranceSimpleModel findTauxParitaireAF(Date date) {
        return findTauxParitaire(date, CodeSystem.TYPE_ASS_COTISATION_AF);
    }

    @Override
    public TauxAssuranceSimpleModel findTauxParitaireAC(Date date) {
        return findTauxParitaire(date, CodeSystem.TYPE_ASS_COTISATION_AC);
    }

    @Override
    public TauxAssuranceSimpleModel findTauxParitaireAC() {
        return findTauxParitaireAC(Date.now());
    }

    private TauxAssuranceSimpleModel findTauxParitaire(Date date, String typeAssurance) {
        AssuranceTauxSearchComplexModel search = new AssuranceTauxSearchComplexModel();
        search.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        search.setForTypeAssurance(typeAssurance);
        search.setForTypeTaux(CodeSystem.TYPE_TAUX_DEFAUT);
        search.setForDate(date);
        search.setOrderKey(ORDER_BY_DATE_DEBUT_DESC);
        return searchAndFetchAssuranceTauxSimpleModel(search);
    }

    @Override
    public TauxAssuranceSimpleModel findTauxForAssurance(String idAssurance, Date date) {
        AssuranceTauxSearchComplexModel search = new AssuranceTauxSearchComplexModel();
        search.setForIdAssurance(idAssurance);
        search.setForDate(date);
        search.setOrderKey(ORDER_BY_DATE_DEBUT_DESC);
        search.setForTypeTaux(CodeSystem.TYPE_TAUX_DEFAUT);
        return searchAndFetchAssuranceTauxSimpleModel(search);
    }

    private TauxAssuranceSimpleModel searchAndFetchAssuranceTauxSimpleModel(AssuranceTauxSearchComplexModel search) {
        try {
            search = (AssuranceTauxSearchComplexModel) JadePersistenceManager.search(search);
            if (search.getSize() == 0) {
                return null;
            }
            AssuranceTauxComplexModel taux = (AssuranceTauxComplexModel) search.getSearchResults()[0];
            return taux.getTauxAssuranceSimpleModel();
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public AssuranceTauxComplexModel findAssuranceTauxParitaireAVS(Date date) {
        return findAssuranceTauxParitaire(date, CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
    }

    @Override
    public AssuranceTauxComplexModel findAssuranceTauxParitaireAC(Date date) {
        return findAssuranceTauxParitaire(date, CodeSystem.TYPE_ASS_COTISATION_AC);
    }

    @Override
    public AssuranceTauxComplexModel findAssuranceTauxParitaireAF(Date date) {
        return findAssuranceTauxParitaire(date, CodeSystem.TYPE_ASS_COTISATION_AF);
    }

    private AssuranceTauxComplexModel findAssuranceTauxParitaire(Date date, String typeAssurance) {
        AssuranceTauxSearchComplexModel search = new AssuranceTauxSearchComplexModel();
        search.setForTypeAssurance(typeAssurance);
        search.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        search.setOrderKey(ORDER_BY_DATE_DEBUT_DESC);
        search.setForTypeTaux(CodeSystem.TYPE_TAUX_DEFAUT);
        search.setForDate(date);
        return searchAndFetchAssuranceTauxComplexModel(search);
    }

    private AssuranceTauxComplexModel searchAndFetchAssuranceTauxComplexModel(AssuranceTauxSearchComplexModel search) {
        try {
            search = (AssuranceTauxSearchComplexModel) JadePersistenceManager.search(search);
            if (search.getSize() == 0) {
                return null;
            }
            AssuranceTauxComplexModel taux = (AssuranceTauxComplexModel) search.getSearchResults()[0];
            return taux;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public CotisationView findByIdCotisation(String idCotisation) {
        AFCotisation cotisationEntity = new AFCotisation();
        cotisationEntity.setId(idCotisation);
        try {
            cotisationEntity.retrieve();
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }

        CotisationView cotisation = new CotisationView(cotisationEntity.getId(), cotisationEntity.getDateDebut(),
                cotisationEntity.getDateFin());

        return cotisation;
    }
}
