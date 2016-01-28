package ch.globaz.osiris.businessimpl.service;

import globaz.osiris.db.comptecourant.CASoldesCompteCourant;
import globaz.osiris.db.comptecourant.CASoldesCompteCourantManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.osiris.business.model.SoldeCompteCourant;
import ch.globaz.osiris.business.service.CompteCourantService;

public class CompteCourantServiceImpl implements CompteCourantService {

    private SoldeCompteCourant parse(CASoldesCompteCourant compte) {
        SoldeCompteCourant soldeCompte = new SoldeCompteCourant();
        soldeCompte.setDescription(compte.getDescription());
        soldeCompte.setIdExterneCompteCourant(compte.getIdExterneCompteCourant());
        soldeCompte.setIdExterneRole(compte.getIdExterneRole());
        soldeCompte.setIdExterneSection(compte.getIdExterneSection());
        soldeCompte.setIdSection(compte.getIdSection());
        soldeCompte.setMontant(compte.getMontant());
        return soldeCompte;
    }

    @Override
    public List<SoldeCompteCourant> searchSoldeCompteCourant(String dateValeurSection, String idcompteAnnexe,
            String idRubriqueCompteCourant) {
        CASoldesCompteCourantManager manager = new CASoldesCompteCourantManager();
        manager.setForDateValeurSection(dateValeurSection);
        manager.setForIdCompteAnnexe(idcompteAnnexe);
        manager.setForIdRubriqueCompteCourant(idRubriqueCompteCourant);

        try {
            manager.find();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<CASoldesCompteCourant> listCa = new ArrayList(manager.getContainer());
        List<SoldeCompteCourant> list = new ArrayList<SoldeCompteCourant>();

        for (CASoldesCompteCourant compte : listCa) {
            SoldeCompteCourant soldeCompte = parse(compte);
            list.add(soldeCompte);
        }

        return list;
    }
}
