package ch.globaz.pegasus.business.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLapramsDonneeFinanciereHeader;

public interface SimpleAnnonceLapramsDoFinHService extends JadeApplicationService {
    public SimpleAnnonceLapramsDonneeFinanciereHeader create(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException;

    public SimpleAnnonceLapramsDonneeFinanciereHeader delete(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException;

    public void deleteByIdAnnonce(List<String> listIdAnnonceDfh) throws AnnonceException, JadePersistenceException;;

    public SimpleAnnonceLapramsDonneeFinanciereHeader read(String idSimpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException;

    public SimpleAnnonceLapramsDonneeFinanciereHeader update(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException;;

}