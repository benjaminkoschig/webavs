/**
 * 
 */
package ch.globaz.naos.businessimpl.service;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.log.JadeLogger;
import globaz.naos.properties.AFProperties;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.business.data.DomainteTypeAdresseIde;
import ch.globaz.naos.business.service.CascadeAdresseIdeService;

/**
 * @author est
 * 
 */
public class CascadeAdresseIdeServiceImpl implements CascadeAdresseIdeService {

    static final String ID_PAYS = "100"; // 100 = Suisse

    private DomainteTypeAdresseIde domaineTypeAdresseIde;
    private TIAdresseDataManager mgr;

    private void init() {
        BSession bSession = initSession();

        domaineTypeAdresseIde = new DomainteTypeAdresseIde();

        mgr = new TIAdresseDataManager();
        mgr.setSession(bSession);
    }

    /**
     * Initialise une session anonyme
     * 
     * @return session
     */
    private static BSession initSession() {
        BSession session = null;
        try {
            session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK").newSession();
            session._connectAnonymous();
        } catch (Exception e) {
            JadeLogger.error("An error happened while getting a new session!", e);
        }
        return session;
    }

    @Override
    public TIAdresseDataSource getAdresseFromCascadeIde(Boolean isPersonneMorale, String affilieNumero, String idTiers)
            throws PropertiesException {
        TIAdresseDataSource adresseRetrouvee;

        init();

        if (isPersonneMorale) {
            adresseRetrouvee = getAdresseForAnnoncePersonneMorale(affilieNumero, idTiers);
        } else {
            adresseRetrouvee = getAdresseForAnnoncePersonnePhysique(affilieNumero, idTiers);
        }

        return adresseRetrouvee;
    }

    @Override
    public String retrievePropretieCascadeIdeMorale() throws PropertiesException {
        if (!AFProperties.IDE_CASCADE_ADRESSE_MORALE.getValue().isEmpty()
                && AFProperties.IDE_CASCADE_ADRESSE_MORALE.getValue() != null) {

            String propertyMorale = AFProperties.IDE_CASCADE_ADRESSE_MORALE.getValue();
            if (propertyMorale.matches("(\\d*,\\d*\\/?)*")) {
                // On enlève le dernier caractère si c'est un slash /
                if (propertyMorale.substring(propertyMorale.length() - 1) == "//") {
                    propertyMorale = propertyMorale.substring(0, propertyMorale.length() - 1);
                }
            } else {
                throw new PropertiesException("The property "
                        + AFProperties.IDE_CASCADE_ADRESSE_MORALE.getDescription()
                        + " is not matching the pattern {xxx},{xxx}/{xxx},{xxx}(...)");
            }
            return propertyMorale;
        } else {
            return "";
        }
    }

    @Override
    public String retrievePropretieCascadeIdePhysique() throws PropertiesException {
        if (!AFProperties.IDE_CASCADE_ADRESSE_PHYSIQUE.getValue().isEmpty()
                && AFProperties.IDE_CASCADE_ADRESSE_PHYSIQUE.getValue() != null) {

            String propertyPhysique = AFProperties.IDE_CASCADE_ADRESSE_PHYSIQUE.getValue();
            if (propertyPhysique.matches("(\\d*,\\d*\\/?)*")) {
                // On enlève le dernier caractère si c'est un slash /
                if (propertyPhysique.substring(propertyPhysique.length() - 1) == "//") {
                    propertyPhysique = propertyPhysique.substring(0, propertyPhysique.length() - 1);
                }
            } else {
                throw new PropertiesException("The property "
                        + AFProperties.IDE_CASCADE_ADRESSE_PHYSIQUE.getDescription()
                        + " is not matching the pattern {xxx},{xxx}/{xxx},{xxx}(...)");
            }
            return propertyPhysique;
        } else {
            return "";
        }
    }

    public TIAdresseDataSource getAdresseForAnnoncePersonneMorale(String affilieNumero, String idTiers)
            throws PropertiesException {

        String propertyMorale = retrievePropretieCascadeIdeMorale();

        // Si la propriété est pas defini
        if (propertyMorale.isEmpty()) {
            // défini une adresse vide (mais pas null), les contrôles se font en aval
            domaineTypeAdresseIde.setAdresse(new TIAdresseDataSource());
        } else {

            for (String pairDomaineType : propertyMorale.split("/")) {
                String[] tabDomaineType = pairDomaineType.split(",");
                // Si on a pas d'adresse déjà définie on continue à chercher
                if (!domaineTypeAdresseIde.getAdresseTrouvee()) {

                    domaineTypeAdresseIde.setDomaine(tabDomaineType[0]);
                    domaineTypeAdresseIde.setType(tabDomaineType[1]);

                    domaineTypeAdresseIde.setAdresse(retrieveAdresse(affilieNumero, idTiers));
                } else {
                    // Si adresse trouvée on stop la recherche cascade
                    break;
                }
            }
        }
        return domaineTypeAdresseIde.getAdresse();
    }

    public TIAdresseDataSource getAdresseForAnnoncePersonnePhysique(String affilieNumero, String idTiers)
            throws PropertiesException {

        String propertyPhysique = retrievePropretieCascadeIdePhysique();

        // Si la propriété est pas defini
        if (propertyPhysique.isEmpty()) {
            // défini une adresse vide (mais pas null), les contrôles se font en aval
            domaineTypeAdresseIde.setAdresse(new TIAdresseDataSource());
        } else {

            for (String pairDomaineType : propertyPhysique.split("/")) {
                String[] tabDomaineType = pairDomaineType.split(",");
                // Si on a pas d'adresse déjà définie on continue à chercher
                if (!domaineTypeAdresseIde.getAdresseTrouvee()) {

                    domaineTypeAdresseIde.setDomaine(tabDomaineType[0]);
                    domaineTypeAdresseIde.setType(tabDomaineType[1]);

                    domaineTypeAdresseIde.setAdresse(retrieveAdresse(affilieNumero, idTiers));
                } else {
                    // Si adresse trouvée on stop la recherche cascade
                    break;
                }
            }
        }
        return domaineTypeAdresseIde.getAdresse();
    }

    /**
     * Méthode qui retrouve une adresse via le numéro d'affilie et l'idtiers
     * 
     * @param affilieNumero
     * @param idTiers
     */
    private TIAdresseDataSource retrieveAdresse(String affilieNumero, String idTiers) {
        TIAbstractAdresseData adresseData = null;
        try {
            mgr.setForIdTiers(idTiers);
            mgr.setForIdApplication(domaineTypeAdresseIde.getDomaine());
            mgr.setForTypeAdresse(domaineTypeAdresseIde.getType());
            mgr.setForIdPays(ID_PAYS);
            mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
            mgr.find(BManager.SIZE_NOLIMIT);

            if (mgr.size() == 1) {
                adresseData = (TIAbstractAdresseData) mgr.getFirstEntity();
            }
            // Dans le cas où l'on a plusieurs adresses de retournées, on doit prendre celle qui est lié à
            // l'affiliation
            if (mgr.size() > 1) {
                adresseData = retrieveAdresseAmongMany(affilieNumero);
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e.getMessage());
        }

        TIAdresseDataSource dataSource = new TIAdresseDataSource();
        dataSource.load(adresseData, "");

        // Adresse trouvée
        if (adresseData != null) {
            domaineTypeAdresseIde.setAdresseTrouvee(true);
        }

        return dataSource;
    }

    /***
     * Retrouve l'adresse lorsque que le tiers en a plusieurs.
     * On retourne celle avec l'affiliation correspondante à l'affiliation source de l'annonce IDE
     * 
     * @param affilieNumero
     * @param adresseData
     * @return l'adresse si trouvé parmi les affiliations, null sinon
     */
    private TIAbstractAdresseData retrieveAdresseAmongMany(String affilieNumero) {
        for (int i = 0; i < mgr.size(); i++) {
            TIAbstractAdresseData adresse = (TIAbstractAdresseData) mgr.getEntity(i);
            if (adresse.getIdExterne().equals(affilieNumero)) {
                return adresse;
            }
        }
        return null;
    }
}