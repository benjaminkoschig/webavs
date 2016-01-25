package ch.globaz.pegasus.business.services.annonce.annoncelaprams;

import globaz.globall.db.BProcess;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.annonce.AnnonceLaprams;

public interface AnnonceLapramsBuilder {
    public void build(List<AnnonceLaprams> annonces, String mailGest, String dateRapport, BProcess process)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception;

}
