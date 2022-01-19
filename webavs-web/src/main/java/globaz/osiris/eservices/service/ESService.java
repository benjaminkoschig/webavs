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
            LOG.error("Une erreur s'est produite lors de la récupération du token : ", e);
            throw new RETechnicalException(e);
        }
    }

    public ESInfoFacturationDTO getExtraitCompte(ESInfoFacturationDTO dto, ESTokenImpl token) {
        try {
            return dto;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération de l'extrait de compte : ", e);
            throw new RETechnicalException(e);
        }
    }

    public ESInfoFacturationDTO getInfoFacturation(ESInfoFacturationDTO dto, ESTokenImpl token) {
        try {
            /*
            Si aucun paramètre de période n’est fourni, toutes les informations de section et d’extrait de compte de l’affilié seront remontées.

            Si seul le paramètre de début de période est fourni, toutes les informations postérieures à cette date seront remontées.

            Si seul le paramètre de fin de période est fourni, toutes les informations antérieures à cette date seront remontées.
            */
            return dto;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération des informations de facturation : ", e);
            throw new RETechnicalException(e);
        }
    }
}