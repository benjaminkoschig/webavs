package globaz.amal.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import ch.globaz.amal.business.exceptions.models.CaisseMaladieException;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListe;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMCaisseMaladieHelper {

    /**
     * Recherche les groupes de caisse maladie
     * 
     * HashMap<value, libelle>
     * 
     * @param job
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws CaisseMaladieException
     */
    // public static CaisseMaladieGroupeRCListeSearch getGroupes() throws CaisseMaladieException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException {
    // HashMap<String, String> aReturn = new HashMap<String, String>();
    // BSession currentSession = BSessionUtil.getSessionFromThreadContext();
    //
    // CaisseMaladieGroupeRCListeSearch caisseMaladieGroupeRCListeSearch = new CaisseMaladieGroupeRCListeSearch();
    // caisseMaladieGroupeRCListeSearch.setForTypeLien("19150084");
    // caisseMaladieGroupeRCListeSearch = AmalServiceLocator.getCaisseMaladieService().searchGroupe(
    // caisseMaladieGroupeRCListeSearch);
    //
    // return caisseMaladieGroupeRCListeSearch;
    // }

    /**
     * Recherche les groupes de caisse maladie
     * 
     * HashMap<value, libelle>
     * 
     * @param job
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws CaisseMaladieException
     */
    public static HashMap<String, String> getGroupesCM() throws CaisseMaladieException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        HashMap<String, String> aReturn = new HashMap<String, String>();
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();

        CaisseMaladieGroupeRCListeSearch caisseMaladieGroupeRCListeSearch = new CaisseMaladieGroupeRCListeSearch();
        // 19150084 == Lien de type "Groupe de caisse maladie"
        caisseMaladieGroupeRCListeSearch.setForTypeLien("19150084");
        caisseMaladieGroupeRCListeSearch = AmalServiceLocator.getCaisseMaladieService().searchGroupe(
                caisseMaladieGroupeRCListeSearch);

        aReturn.put("", "Tous");
        aReturn.put("0", "Sans groupe");
        for (JadeAbstractModel model : caisseMaladieGroupeRCListeSearch.getSearchResults()) {
            CaisseMaladieGroupeRCListe caisseMaladieGroupeRCListe = (CaisseMaladieGroupeRCListe) model;
            if (!aReturn.containsKey(caisseMaladieGroupeRCListe.getId())) {
                aReturn.put(caisseMaladieGroupeRCListe.getId(), caisseMaladieGroupeRCListe.getNomGroupe());
            }
        }

        return aReturn;
    }
}
