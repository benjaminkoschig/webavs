package ch.globaz.eform.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;

public interface GFEFormFileService extends JadeApplicationService {
    String getZipFormulaire(String id) throws Exception;
}
