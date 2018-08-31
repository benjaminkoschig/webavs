/**
 *
 */
package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pyxis.business.model.AdressePaiementComplexModel;
import ch.globaz.pyxis.business.model.AdressePaiementSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.AvoirPaiementSimpleModel;
import ch.globaz.pyxis.business.model.LocaliteSearchSimpleModel;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.exception.PyxisException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.AvoirAdressePaiement;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;
import ch.globaz.vulpecula.external.repositories.tiers.AdresseRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdresseConverter;
import ch.globaz.vulpecula.util.I18NUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Implémentation Jade de {@link AdresseRepositoryJade}
 */
public class AdresseRepositoryJade implements AdresseRepository {
    private static final String ADRESSE_ALLEMAND = "68906001";

    private static final String WHERE_CLAUSE = "widgetSearch";

    private final Logger LOGGER = LoggerFactory.getLogger(AdresseRepositoryJade.class);

    @Override
    public Adresse findAdressePrioriteCourrierByIdTiers(final String idTiers) {
        try {
            ArrayList<String> orderTypeAdresse = new ArrayList<String>();
            orderTypeAdresse.add(AdresseService.CS_TYPE_COURRIER);
            orderTypeAdresse.add(AdresseService.CS_TYPE_DOMICILE);
            AdresseTiersDetail adresseTiersDetails = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade(String.valueOf(idTiers), new Date().getSwissValue(),
                            AdresseService.CS_DOMAINE_DEFAUT, orderTypeAdresse,
                            AdresseService.MODE_CASC_PRIORITY_DOMAINE);
            Tiers tiers = VulpeculaRepositoryLocator.getTiersRepository().findById(idTiers);
            return AdresseConverter.convertToDomain(adresseTiersDetails,
                    I18NUtil.getLanguesOf(tiers.getLangue()).getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Adresse findAdresseCourrierByIdTiers(final String idTiers, CodeLangue langue) {
        try {
            ArrayList<String> orderTypeAdresse = new ArrayList<String>();

            if (CodeLangue.DE.equals(langue)) {
                orderTypeAdresse.add(AdresseRepositoryJade.ADRESSE_ALLEMAND);
            }
            orderTypeAdresse.add(AdresseService.CS_TYPE_COURRIER);
            orderTypeAdresse.add(AdresseService.CS_TYPE_DOMICILE);

            new Date();
            AdresseTiersDetail adresseTiersDetails = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade(String.valueOf(idTiers), new Date().getSwissValue(),
                            AdresseService.CS_DOMAINE_DEFAUT, orderTypeAdresse,
                            AdresseService.MODE_CASC_PRIORITY_DOMAINE);

            return AdresseConverter.convertToDomain(adresseTiersDetails, I18NUtil.getLanguesOf(langue).getCodeIso());
        } catch (PyxisException ex) {
            return null;
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AvoirAdressePaiement findByIdTiers(String idTiers, Date date, List<String> applications) {
        AvoirAdressePaiement adressePaiement = new AvoirAdressePaiement();
        List<AdressePaiementComplexModel> adressesPaiementComplexModel = getAdressesPaiements(idTiers, date);
        for (String applicationToFind : applications) {
            for (AdressePaiementComplexModel adressePaiementComplexModel : adressesPaiementComplexModel) {
                AvoirPaiementSimpleModel avoirPaiementSimpleModel = adressePaiementComplexModel.getAvoirPaiement();
                String application = adressePaiementComplexModel.getAvoirPaiement().getIdApplication();
                String dateDebutRelation = avoirPaiementSimpleModel.getDateDebutRelation();
                if (JadeNumericUtil.isEmptyOrZero(dateDebutRelation)) {
                    dateDebutRelation = Date.UNDEFINED_DATE_DEBUT;
                }
                Periode periode = new Periode(dateDebutRelation, avoirPaiementSimpleModel.getDateFinRelation());
                if (applicationToFind.equals(application) && periode.contains(date)) {
                    adressePaiement.setId(adressePaiementComplexModel.getAvoirPaiement().getId());
                    return adressePaiement;
                }
            }
        }
        return adressePaiement;
    }

    @Override
    public String findAdressePaiementByIdTiers(final String idTiers, Date date) {
        AdresseTiersDetail adresseDetail = null;
        try {
            adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, true,
                    AvoirAdressePaiement.CS_DOMAINE_STANDARD, date.getSwissValue(), null);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        return adresseDetail.getAdresseFormate();
    }

    @Override
    public AvoirAdressePaiement findForPrestations(String idTiers, Date date) {
        return findByIdTiers(idTiers, date, Arrays.asList(AvoirAdressePaiement.CS_DOMAINE_PRESTATIONS_CONVENTIONNELLES,
                AvoirAdressePaiement.CS_DOMAINE_STANDARD));
    }

    protected List<AdressePaiementComplexModel> getAdressesPaiements(String idTiers, Date date) {
        List<AdressePaiementComplexModel> adressesPaiements = new ArrayList<AdressePaiementComplexModel>();

        AdressePaiementSearchComplexModel searchModel = new AdressePaiementSearchComplexModel();
        searchModel.setForIdTiers(idTiers);
        searchModel.setForDate(date.getSwissValue());
        searchModel.setWhereKey(WHERE_CLAUSE);
        try {
            JadePersistenceManager.search(searchModel);
            for (int i = 0; i < searchModel.getSize(); i++) {
                AdressePaiementComplexModel adressePaiementComplexModel = (AdressePaiementComplexModel) searchModel
                        .getSearchResults()[i];
                adressesPaiements.add(adressePaiementComplexModel);
            }
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        return adressesPaiements;
    }

    @Override
    public Adresse findAdresseDomicileByIdTiers(String idTiers) {
        try {
            ArrayList<String> orderTypeAdresse = new ArrayList<String>();
            orderTypeAdresse.add(AdresseService.CS_TYPE_DOMICILE);
            AdresseTiersDetail adresseTiersDetails = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade(String.valueOf(idTiers), new Date().getSwissValue(),
                            AdresseService.CS_DOMAINE_DEFAUT, orderTypeAdresse,
                            AdresseService.MODE_CASC_PRIORITY_DOMAINE);
            Tiers tiers = VulpeculaRepositoryLocator.getTiersRepository().findById(idTiers);
            return AdresseConverter.convertToDomain(adresseTiersDetails,
                    I18NUtil.getLanguesOf(tiers.getLangue()).getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Adresse findAdresseDomicileByIdTiersForCT(String idTiers) {
        try {
            ArrayList<String> orderTypeAdresse = new ArrayList<String>();
            orderTypeAdresse.add(AdresseService.CS_TYPE_DOMICILE);
            AdresseTiersDetail adresseTiersDetails = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade(String.valueOf(idTiers), new Date().getSwissValue(),
                            AdresseService.CS_DOMAINE_DEFAUT, orderTypeAdresse,
                            AdresseService.MODE_CASC_PRIORITY_DOMAINE);
            Tiers tiers = VulpeculaRepositoryLocator.getTiersRepository().findById(idTiers);
            return AdresseConverter.convertToDomain(adresseTiersDetails,
                    I18NUtil.getLanguesOf(tiers.getLangue()).getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Adresse findAdresseCourrierByIdTiersForCT(String idTiers) {
        try {
            ArrayList<String> orderTypeAdresse = new ArrayList<String>();
            orderTypeAdresse.add(AdresseService.CS_TYPE_COURRIER);
            AdresseTiersDetail adresseTiersDetails = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade(String.valueOf(idTiers), new Date().getSwissValue(),
                            AdresseService.CS_DOMAINE_DEFAUT, orderTypeAdresse,
                            AdresseService.MODE_CASC_PRIORITY_DOMAINE);
            Tiers tiers = VulpeculaRepositoryLocator.getTiersRepository().findById(idTiers);
            return AdresseConverter.convertToDomain(adresseTiersDetails,
                    I18NUtil.getLanguesOf(tiers.getLangue()).getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Adresse findAdresseDomicileByIdTiersAndDate(String idTiers, Date date) {
        try {
            ArrayList<String> orderTypeAdresse = new ArrayList<String>();
            orderTypeAdresse.add(AdresseService.CS_TYPE_DOMICILE);
            AdresseTiersDetail adresseTiersDetails = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade(String.valueOf(idTiers), date.getSwissValue(),
                            AdresseService.CS_DOMAINE_DEFAUT, orderTypeAdresse,
                            AdresseService.MODE_CASC_PRIORITY_DOMAINE);
            Tiers tiers = VulpeculaRepositoryLocator.getTiersRepository().findById(idTiers);
            return AdresseConverter.convertToDomain(adresseTiersDetails,
                    I18NUtil.getLanguesOf(tiers.getLangue()).getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Adresse findAdresseCourrierByIdTiersAndDate(String idTiers, Date date) {
        try {
            ArrayList<String> orderTypeAdresse = new ArrayList<String>();
            orderTypeAdresse.add(AdresseService.CS_TYPE_COURRIER);
            AdresseTiersDetail adresseTiersDetails = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade(String.valueOf(idTiers), date.getSwissValue(),
                            AdresseService.CS_DOMAINE_DEFAUT, orderTypeAdresse,
                            AdresseService.MODE_CASC_PRIORITY_DOMAINE);
            Tiers tiers = VulpeculaRepositoryLocator.getTiersRepository().findById(idTiers);
            return AdresseConverter.convertToDomain(adresseTiersDetails,
                    I18NUtil.getLanguesOf(tiers.getLangue()).getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public String findIbanByIdTiers(String idTiers, Date date) {
        AdresseTiersDetail adresseDetail = null;
        try {
            adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, true,
                    AvoirAdressePaiement.CS_DOMAINE_STANDARD, date.getSwissValue(), null);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
        }

        if (adresseDetail.getFields() != null) {
            HashMap<String, String> map = adresseDetail.getFields();
            return map.get("compte");
        } else {
            return "";
        }

    }

    @Override
    public String findNomBanqueByIdTiers(String idTiers, Date date) {

        AdresseTiersDetail adresseDetail = null;
        try {
            adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, true,
                    AvoirAdressePaiement.CS_DOMAINE_STANDARD, date.getSwissValue(), null);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
        }

        if (adresseDetail.getFields() != null) {
            HashMap<String, String> map = adresseDetail.getFields();
            return map.get("banque_d1");
        } else {
            return "";
        }
    }

    @Override
    public String findLocaliteBanqueByIdTiers(String idTiers, Date date) {
        AdresseTiersDetail adresseDetail = null;
        try {
            adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, true,
                    AvoirAdressePaiement.CS_DOMAINE_STANDARD, date.getSwissValue(), null);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
        }

        if (adresseDetail.getFields() != null) {
            HashMap<String, String> map = adresseDetail.getFields();
            return map.get("banque_localite");
        } else {
            return "";
        }
    }

    @Override
    public String findIdLocaliteBanqueByIdTiers(String idTiers, Date date) {
        AdresseTiersDetail adresseDetail = null;
        HashMap<String, String> map = new HashMap<String, String>();

        try {
            adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, true,
                    AvoirAdressePaiement.CS_DOMAINE_STANDARD, date.getSwissValue(), null);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
        }

        if (adresseDetail.getFields() != null) {
            map = adresseDetail.getFields();
        }

        LocaliteSearchSimpleModel localiteSearchModel = new LocaliteSearchSimpleModel();
        localiteSearchModel.setForNpaLike(map.get("banque_npa"));
        localiteSearchModel.setForLocaliteLike(map.get("banque_localite"));

        try {
            localiteSearchModel = TIBusinessServiceLocator.getAdresseService().findLocalite(localiteSearchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
        }

        if (localiteSearchModel.getSearchResults()[0] != null) {
            LocaliteSimpleModel localite = (LocaliteSimpleModel) localiteSearchModel.getSearchResults()[0];
            return localite.getId();
        }

        return "";

    }
}
