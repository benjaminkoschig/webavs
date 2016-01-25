package ch.globaz.cygnus.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Map;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.Regimes;

public interface RegimeLoader extends JadeApplicationService {

    public Map<String, Regimes> loadRegimes(Map<String, MembresFamilles> mapMembreFamille);

}
