package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.util.CommonNSSFormater;
import java.util.Properties;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import ch.gdk_cds.xmlns.da_64a_common._1.AddressType;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendue;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendueSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.common.domaine.Date;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AnnoncesCODefault {
    protected String passSedex = "";
    protected String userSedex = "";
    protected JadeContext context;
    protected JAXBServices jaxbs;
    protected BSession session;
    protected Unmarshaller unmarshaller;
    protected Marshaller marshaller;

    /**
     * Retourne un contexte. Si n�cessaire il est initialis�
     * 
     * @return le contexte
     * 
     * @throws Exception
     *             Exception lev�e si le contexte ne peut �tre initialis�
     */
    protected JadeContext getContext() throws Exception {
        if (context == null) {
            context = initContext(getSession()).getContext();
        }
        return context;
    }

    /**
     * Initialise un contexte
     * 
     * @param session
     *            session
     * @return le contexte initialis�
     * @throws Exception
     *             Exception lev�e si le contexte ne peut �tre initialis�
     */
    protected JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(AMApplication.DEFAULT_APPLICATION_AMAL);
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    /**
     * Retourne une session. Si n�cessaire elle est initialis�e
     * 
     * @return la session
     * 
     * @throws Exception
     *             Exception lev�e si la session ne peut �tre initialis�e
     */
    public BSession getSession() throws Exception {
        if (session == null) {
            session = (BSession) GlobazSystem.getApplication(AMApplication.DEFAULT_APPLICATION_AMAL).newSession(
                    userSedex, passSedex);
        }

        return session;
    }

    protected void getUserAndPassSedex(Properties properties) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "R�ception message RP AMAL: user sedex non renseign�. ");
            throw new IllegalStateException("R�ception message RP AMAL: user sedex non renseign�. ");
        }
        userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "R�ception message RP AMAL: pass sedex non renseign�. ");
            throw new IllegalStateException("R�ception message RP AMAL: pass sedex non renseign�. ");
        }
        passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);
    }

    protected FamillePersonneEtendue getPersonneEtendue(String nss) {

        try {
            if (nss == null || nss.isEmpty()) {
                return null;
            }

            CommonNSSFormater nssFormater = new CommonNSSFormater();
            String nssFormate = nssFormater.format(nss);

            FamillePersonneEtendueSearch famillePersonneEtendueSearch = new FamillePersonneEtendueSearch();
            famillePersonneEtendueSearch.setLikeNss(nssFormate);
            famillePersonneEtendueSearch = AmalServiceLocator.getFamilleContribuableService().search(
                    famillePersonneEtendueSearch);
            int nbFamilleContribuable = famillePersonneEtendueSearch.getNbOfResultMatchingQuery();
            // Si on trouve un seul membre, on retourne celui la
            if (nbFamilleContribuable == 1) {
                return (FamillePersonneEtendue) famillePersonneEtendueSearch.getSearchResults()[0];
            } else if (nbFamilleContribuable > 1) {
                // Si on en trouve plusieurs, on relance la requ�te en ne sp�cifiant qu'on ne veut que les actifs
                famillePersonneEtendueSearch.setForMembreActifFromToday(Date.now().getSwissMonthValue());
                nbFamilleContribuable = AmalServiceLocator.getFamilleContribuableService().count(
                        famillePersonneEtendueSearch);
                if (nbFamilleContribuable == 1) {
                    return (FamillePersonneEtendue) famillePersonneEtendueSearch.getSearchResults()[0];
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return null;
    }

    protected String formatNSS(long nssNonFormate) throws Exception {
        String strNss = String.valueOf(nssNonFormate);
        return formatNSS(strNss);
    }

    protected String formatNSS(String nssNonFormate) throws Exception {
        CommonNSSFormater commonNSSFormater = new CommonNSSFormater();
        return commonNSSFormater.format(nssNonFormate);
    }

    protected String getNomCaisseMaladie(String noCaisseMaladie) {
        try {
            AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService()
                    .read(noCaisseMaladie);
            if (!admin.isNew()) {
                return admin.getTiers().getDesignation1();
            } else {
                return "Caisse inconnue";
            }
        } catch (Exception ex) {
            return "Caisse inconnue";
        }
    }

    protected String getNPALocalite(AddressType address) {
        String npa_localite = "";
        if (address.getCountry() == 8100) {
            npa_localite = address.getSwissZipCode().toString();
        } else {
            if (address.getForeignZipCode() != null) {
                npa_localite = address.getForeignZipCode().toString();
            }
        }
        npa_localite += " " + address.getMunicipalityName();
        return npa_localite;
    }

    protected String getRueNumero(AddressType address) {
        String rueNumero = address.getStreet() + " " + address.getHouseNumber();
        return rueNumero;
    }

    public String getPassSedex() {
        return passSedex;
    }

    public void setPassSedex(String passSedex) {
        this.passSedex = passSedex;
    }

    public String getUserSedex() {
        return userSedex;
    }

    public void setUserSedex(String userSedex) {
        this.userSedex = userSedex;
    }

    // /**
    // * R�gles :
    // * Si le d�biteur existe avec le nss pass�, on utilise celui ci
    // * Sinon, on prend le premier contribuable actif trouv� d'une personne assur�e
    // * Et enfin, on prend un contribuable qui a une fin de droit la plus r�cente et on met un message
    // *
    // * @param nss
    // * @param personnesAssurees
    // * @return
    // * @throws JadePersistenceException
    // */
    // protected FamillePersonneEtendue searchPersonne(String nss, List<InsuredPersonWithClaimType> personnesAssurees)
    // throws JadePersistenceException {
    //
    // try {
    // FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(nss);
    //
    // if (famillePersonneEtendue != null) {
    // return famillePersonneEtendue;
    // }
    //
    // // Ici, c'est qu'on a trouv� aucun membre avec ce nss, on tente de r�cup�rer le 1er contribuable actif qu'on
    // // trouve sur une des personnes assur�es...
    // // On en profite �galement pour conserver le contribuable avec la fin de droit la plus r�cente, au cas o� on
    // // devrait aller � la prochaine �tape...
    // FamillePersonneEtendue famillePersonneEtendueMostRecent = null;
    // for (InsuredPersonWithClaimType insuredPerson : personnesAssurees) {
    // String nssPersonneAssureeFormate = "";
    // try {
    // CommonNSSFormater nssFormateur = new CommonNSSFormater();
    // nssPersonneAssureeFormate = nssFormateur.format(String.valueOf(insuredPerson.getInsuredPerson()
    // .getVn()));
    // } catch (Exception e) {
    // nssPersonneAssureeFormate = "N/A";
    // }
    // FamillePersonneEtendueSearch famillePersonneEtendueSearch = new FamillePersonneEtendueSearch();
    // famillePersonneEtendueSearch.setLikeNss(nssPersonneAssureeFormate);
    // famillePersonneEtendueSearch.setOrderKey("orderByFinDroitDesc");
    // famillePersonneEtendueSearch = AmalServiceLocator.getFamilleContribuableService().search(
    // famillePersonneEtendueSearch);
    // for (JadeAbstractModel abstractFamilleContribuable : famillePersonneEtendueSearch.getSearchResults()) {
    // famillePersonneEtendue = (FamillePersonneEtendue) abstractFamilleContribuable;
    //
    // if (JadeStringUtil.isBlankOrZero(famillePersonneEtendue.getSimpleFamille().getFinDefinitive())) {
    // // On retourne le 1er cas sans date de fin qu'on trouve.
    // return famillePersonneEtendue;
    // } else {
    // // Sinon on prend le 1er, qui est le plus r�cent
    // Date dateFinMostRecent = new Date(famillePersonneEtendueMostRecent.getSimpleFamille()
    // .getFinDefinitive());
    // Date dateFinCurrent = new Date(famillePersonneEtendue.getSimpleFamille().getFinDefinitive());
    // if (dateFinCurrent.after(dateFinMostRecent)) {
    // famillePersonneEtendueMostRecent = famillePersonneEtendue;
    // }
    // }
    // }
    // }
    //
    // // Si on arrive la, c'est qu'on a trouv� aucun contribuable actif sur une des personnes assur�es, on
    // // retourne donc celui qui a la date de fin la plus r�cente.
    //
    // return famillePersonneEtendueMostRecent;
    // } catch (Exception ex) {
    // throw new JadePersistenceException("Erreur pendant la recherche de la personne " + nss, ex);
    // }
    // }

}
