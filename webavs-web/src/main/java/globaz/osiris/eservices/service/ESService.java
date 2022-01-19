package globaz.osiris.eservices.service;

import globaz.osiris.eservices.dto.ESInfoFacturationDTO;
import globaz.osiris.eservices.dto.ESLoginDTO;
import globaz.osiris.eservices.token.ESTokenImpl;
import globaz.osiris.eservices.token.ESTokenServiceImpl;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ESService {

    public String getToken(ESLoginDTO req) {
        try {
            BSession session = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            session.connect(req.getUsername(), req.getPassword());

            return ESTokenServiceImpl.createTokenES(session);
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la r�cup�ration du token : ", e);
            throw new RETechnicalException(e);
        }
    }

    public ESInfoFacturationDTO getExtraitCompte(ESInfoFacturationDTO dto, ESTokenImpl token) {
        try {
            return dto;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la r�cup�ration de l'extrait de compte : ", e);
            throw new RETechnicalException(e);
        }
    }

    public ESInfoFacturationDTO getInfoFacturation(ESInfoFacturationDTO dto, ESTokenImpl token) {
        try {
            /*
            Si aucun param�tre de p�riode n�est fourni, toutes les informations de section et d�extrait de compte de l�affili� seront remont�es.

            Si seul le param�tre de d�but de p�riode est fourni, toutes les informations post�rieures � cette date seront remont�es.

            Si seul le param�tre de fin de p�riode est fourni, toutes les informations ant�rieures � cette date seront remont�es.
            */
            return dto;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la r�cup�ration des informations de facturation : ", e);
            throw new RETechnicalException(e);
        }
    }
}