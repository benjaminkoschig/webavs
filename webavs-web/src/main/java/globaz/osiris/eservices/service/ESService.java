package globaz.osiris.eservices.service;

import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.orion.ws.comptabilite.WebAvsComptabiliteServiceImpl;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.osiris.eservices.dto.req.ESExtraitCompteREQ;
import globaz.osiris.eservices.dto.req.ESInfoFacturationREQ;
import globaz.osiris.eservices.dto.req.ESLoginREQ;
import globaz.osiris.eservices.dto.resp.ESExtraitCompteRESP;
import globaz.osiris.eservices.dto.resp.ESInfoFacturationRESP;
import globaz.osiris.eservices.exceptions.ESBadRequestException;
import globaz.osiris.eservices.exceptions.ESUnauthorizedException;
import globaz.osiris.eservices.token.ESTokenImpl;
import globaz.osiris.eservices.token.ESTokenServiceImpl;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntRole;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
public class ESService {

    private WebAvsComptabiliteServiceImpl webAvsComptabiliteService = new WebAvsComptabiliteServiceImpl();

    public boolean isValid(Field[] fields, Object req) throws ESBadRequestException {
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (f.get(req) == null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                return false;
            }
        }
        return true;
    }

    public String getToken(ESLoginREQ req) {
        if (!isValid(req.getClass().getDeclaredFields(), req)) {
            throw new ESBadRequestException("");
        }
        try {
            BSession session = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            session.connect(req.getUsername(), req.getPassword());

            return ESTokenServiceImpl.createTokenES(session);
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la r�cup�ration du token : ", e);
            throw new ESUnauthorizedException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * R�cup�ration du document et encode en base64.
     *
     * @return le document encod� en base 64
     */
    private String encodeBase64(String pathFile) throws IOException {
        if (!JadeStringUtil.isEmpty(pathFile)) {
            byte[] inFileBytes = Files.readAllBytes(Paths.get(pathFile));
            return Base64.getEncoder().encodeToString(inFileBytes);
        } else {
            return "";
        }
    }

    private AffiliationSimpleModel getAffiliation(String affiliateNumber) throws JadePersistenceException, JadeApplicationException {
        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
        searchModel.setForNumeroAffilie(affiliateNumber);
        searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
        if (searchModel.getSize() > 0) {
            for (int i = 0; i < searchModel.getSize(); i++) {
                return (AffiliationSimpleModel) searchModel.getSearchResults()[i];
            }
        }
        return null;
    }

    public ESExtraitCompteRESP getExtraitCompte(ESExtraitCompteREQ dto, ESTokenImpl token) {
        try {
            ESExtraitCompteRESP resp = new ESExtraitCompteRESP();

            // R�cup�ration de l'affiliation
            AffiliationSimpleModel aff = getAffiliation(dto.getAffiliateNumber());

            // R�cup�ration du compte annexe
            CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null, aff.getIdTiers(), IntRole.ROLE_AFFILIE, aff.getAffilieNumero(), false);

            // R�cup�ration de l'extrait de compte
            String pathFile = webAvsComptabiliteService.genererExtraitCompteAnnexe(dto.getOperation(), dto.getTri(), dto.getSection(), ca.getIdCompteAnnexe(), dto.getStartPeriod(), dto.getEndPeriod(), dto.getLangue());
            resp.setDocument(encodeBase64(pathFile));

            return resp;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la r�cup�ration de l'extrait de compte : ", e);
            throw new ESBadRequestException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    public ESInfoFacturationRESP getInfoFacturation(ESInfoFacturationREQ dto, ESTokenImpl token) {
        try {
            /*
            Si aucun param�tre de p�riode n�est fourni, toutes les informations de section et d�extrait de compte de l�affili� seront remont�es.

            Si seul le param�tre de d�but de p�riode est fourni, toutes les informations post�rieures � cette date seront remont�es.

            Si seul le param�tre de fin de p�riode est fourni, toutes les informations ant�rieures � cette date seront remont�es.
            */
            return new ESInfoFacturationRESP();
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la r�cup�ration des informations de facturation : ", e);
            throw new ESBadRequestException(e);
        }
    }
}