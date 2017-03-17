package ch.globaz.osiris.businessimpl.service;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptecourant.CASoldesCompteCourant;
import globaz.osiris.db.comptecourant.CASoldesCompteCourantManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.osiris.business.model.SoldeCompteCourant;
import ch.globaz.osiris.business.service.CompteCourantService;

public class CompteCourantServiceImpl implements CompteCourantService {

    private static SoldeCompteCourant parse(CASoldesCompteCourant compte) {
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

        return searchSoldeCompteCourant(dateValeurSection, idcompteAnnexe, idRubriqueCompteCourant, null);
    }

    public static List<SoldeCompteCourant> searchSoldeCompteCourant(String dateValeurSection, String idcompteAnnexe,
            String idRubriqueCompteCourant, BSession session) {
        CASoldesCompteCourantManager manager = new CASoldesCompteCourantManager();
        manager.setForDateValeurSection(dateValeurSection);
        manager.setForIdCompteAnnexe(idcompteAnnexe);
        manager.setForIdRubriqueCompteCourant(idRubriqueCompteCourant);
        manager.setSession(session);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
