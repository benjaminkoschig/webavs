package ch.globaz.pegasus.business.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLaprams;

public interface SimpleAnnonceLapramsService extends JadeApplicationService {
    public SimpleAnnonceLaprams create(SimpleAnnonceLaprams simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, PrestationException;

    public SimpleAnnonceLaprams delete(SimpleAnnonceLaprams simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, PrestationException;

    public SimpleAnnonceLaprams read(String idSimpleAnnonceLaprams) throws JadePersistenceException,
            PrestationException;;

    public SimpleAnnonceLaprams update(SimpleAnnonceLaprams simpleAnnonceLaprams) throws JadePersistenceException,
            PrestationException;;

}