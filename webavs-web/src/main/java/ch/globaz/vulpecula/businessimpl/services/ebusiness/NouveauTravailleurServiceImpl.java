package ch.globaz.vulpecula.businessimpl.services.ebusiness;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.vulpecula.business.services.ebusiness.NouveauTravailleurService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.domain.repositories.ebusiness.NouveauTravailleurRepository;
import ch.globaz.vulpecula.web.gson.AdresseInfoGSON;
import ch.globaz.vulpecula.ws.services.VulpeculaAbstractService;
import ch.globaz.vulpecula.ws.utils.UtilsService;

/**
 * Implémentation Jade du service <code>TravailleurService</code>.
 * 
 */
public class NouveauTravailleurServiceImpl extends VulpeculaAbstractService implements NouveauTravailleurService {
    private NouveauTravailleurRepository travailleurRepository;

    protected static final String CS_TYPE_DOMICILE = "508008";
    protected static final String CS_TYPE_COURRIER = "508001";
    protected static final String CS_DOMAINE_DEFAUT = "519004";

    public NouveauTravailleurServiceImpl(final NouveauTravailleurRepository travailleurRepository) {
        this.travailleurRepository = travailleurRepository;
    }

    @Override
    public TravailleurEbuDomain create(final TravailleurEbuDomain travailleur) throws GlobazBusinessException {
        return travailleurRepository.create(travailleur);
    }

    @Override
    public void delete(final TravailleurEbuDomain travailleur) throws GlobazBusinessException {
        travailleurRepository.delete(travailleur);
    }

    @Override
    public TravailleurEbuDomain update(TravailleurEbuDomain travailleur) throws GlobazBusinessException {
        return travailleurRepository.update(travailleur);
    }

    @Override
    public String findIdTiersBanqueByClearingAndIdLocalite(String clearing, String idLocalite) {
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        List<String> results = new ArrayList<String>();

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            results = QueryExecutor.execute(getQueryIdTiersBanque(clearing, idLocalite), String.class);
        } catch (SQLException e) {
            session.addError(e.getMessage());
        } catch (Exception e) {
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        if (results.size() > 0) {
            return results.get(0);
        } else {
            return "";
        }

    }

    private String getQueryIdTiersBanque(String clearing, String idLocalite) {

        return "SELECT banque.HTITIE from SCHEMA.TIBANQP banque"
                + " INNER JOIN SCHEMA.titierp tiers ON banque.HTITIE=tiers.htitie"
                + " INNER JOIN SCHEMA.TIAADRP aadr ON aadr.HTITIE=banque.HTITIE"
                + " INNER JOIN SCHEMA.TIADREP adresse ON aadr.HAIADR=adresse.HAIADR"
                + " WHERE tiers.HTINAC='2' and banque.HUCLEA=" + clearing + " and adresse.HJILOC=" + idLocalite;
    }

    @Override
    public AdresseComplexModel insertAdresse(PersonneEtendueComplexModel personneEtendueComplexModel,
            AdresseInfoGSON adresseInfoGSON) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        AdresseComplexModel adresseComplexModel = new AdresseComplexModel();
        adresseComplexModel.setTiers(personneEtendueComplexModel);
        adresseComplexModel.getAvoirAdresse().setDateDebutRelation(Date.now().getSwissValue());
        adresseComplexModel.getTiers().setId(personneEtendueComplexModel.getTiers().getId());
        adresseComplexModel.getLocalite().setNumPostal(adresseInfoGSON.getNpa());
        adresseComplexModel.getAdresse().setRue(adresseInfoGSON.getRue());
        adresseComplexModel.getAdresse().setNumeroRue(adresseInfoGSON.getRueNumero());
        adresseComplexModel.getAdresse().setCasePostale(adresseInfoGSON.getCasePostale());

        return TIBusinessServiceLocator.getAdresseService().addAdresse(adresseComplexModel, CS_DOMAINE_DEFAUT,
                CS_TYPE_DOMICILE, false);
    }

}
