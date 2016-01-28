/**
 * 
 */
package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pyxis.business.model.AdressePaiementComplexModel;
import ch.globaz.pyxis.business.model.AdressePaiementSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.AvoirPaiementSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.AvoirAdressePaiement;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;
import ch.globaz.vulpecula.external.repositories.tiers.AdresseRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdresseConverter;
import ch.globaz.vulpecula.util.I18NUtil;

/**
 * Implémentation Jade de {@link AdresseRepositoryJade}
 */
public class AdresseRepositoryJade implements AdresseRepository {
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
            return AdresseConverter.convertToDomain(adresseTiersDetails, I18NUtil.getLanguesOf(tiers.getLangue())
                    .getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
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
            return AdresseConverter.convertToDomain(adresseTiersDetails, I18NUtil.getLanguesOf(tiers.getLangue())
                    .getCodeIso());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }
}
