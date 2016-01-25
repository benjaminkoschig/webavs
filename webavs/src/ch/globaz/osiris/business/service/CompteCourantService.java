package ch.globaz.osiris.business.service;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.osiris.business.model.SoldeCompteCourant;

public interface CompteCourantService extends JadeApplicationService {

    List<SoldeCompteCourant> searchSoldeCompteCourant(String dateValeurSection, String idcompteAnnexe,
            String idRubriqueCompteCourant);
}
