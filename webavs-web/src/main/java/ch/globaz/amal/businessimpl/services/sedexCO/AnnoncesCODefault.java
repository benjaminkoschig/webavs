package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.pyxis.util.CommonNSSFormater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendue;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendueSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.common.domaine.Date;

public class AnnoncesCODefault {
    protected String passSedex = "";
    protected String userSedex = "";
    protected JadeContext context;
    protected JAXBServices jaxbs;
    protected BSession session;
    protected Unmarshaller unmarshaller;
    protected Marshaller marshaller;
    protected Map<String, List<String>> mapMessageParNSS = new HashMap<String, List<String>>();

    /**
     * Retourne un contexte. Si nécessaire il est initialisé
     * 
     * @return le contexte
     * 
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
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
     * @return le contexte initialisé
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
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
     * Retourne une session. Si nécessaire elle est initialisée
     * 
     * @return la session
     * 
     * @throws Exception
     *             Exception levée si la session ne peut être initialisée
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
            JadeLogger.error(this, "Réception message RP AMAL: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: user sedex non renseigné. ");
        }
        userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "Réception message RP AMAL: pass sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: pass sedex non renseigné. ");
        }
        passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);
    }

    /**
     * Règles :
     * Si le débiteur existe avec le nss passé, on utilise celui ci
     * Sinon, on prend le premier contribuable actif trouvé d'une personne assurée
     * Et enfin, on prend un contribuable qui a une fin de droit la plus récente et on met un message
     * 
     * @param nss
     * @param personnesAssurees
     * @return
     * @throws JadePersistenceException
     */
    protected FamillePersonneEtendue searchPersonne(String nss, List<InsuredPersonWithClaimType> personnesAssurees)
            throws JadePersistenceException {

        try {
            FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(nss);

            if (famillePersonneEtendue != null) {
                return famillePersonneEtendue;
            }

            // Ici, c'est qu'on a trouvé aucun membre avec ce nss, on tente de récupérer le 1er contribuable actif qu'on
            // trouve sur une des personnes assurées...
            // On en profite également pour conserver le contribuable avec la fin de droit la plus récente, au cas où on
            // devrait aller à la prochaine étape...
            FamillePersonneEtendue famillePersonneEtendueMostRecent = null;
            for (InsuredPersonWithClaimType insuredPerson : personnesAssurees) {
                String nssPersonneAssureeFormate = "";
                try {
                    CommonNSSFormater nssFormateur = new CommonNSSFormater();
                    nssPersonneAssureeFormate = nssFormateur.format(String.valueOf(insuredPerson.getInsuredPerson()
                            .getVn()));
                } catch (Exception e) {
                    nssPersonneAssureeFormate = "N/A";
                }
                FamillePersonneEtendueSearch famillePersonneEtendueSearch = new FamillePersonneEtendueSearch();
                famillePersonneEtendueSearch.setLikeNss(nssPersonneAssureeFormate);
                famillePersonneEtendueSearch.setOrderKey("orderByFinDroitDesc");
                famillePersonneEtendueSearch = AmalServiceLocator.getFamilleContribuableService().search(
                        famillePersonneEtendueSearch);
                for (JadeAbstractModel abstractFamilleContribuable : famillePersonneEtendueSearch.getSearchResults()) {
                    famillePersonneEtendue = (FamillePersonneEtendue) abstractFamilleContribuable;

                    if (JadeStringUtil.isBlankOrZero(famillePersonneEtendue.getSimpleFamille().getFinDefinitive())) {
                        // On retourne le 1er cas sans date de fin qu'on trouve.
                        return famillePersonneEtendue;
                    } else {
                        // Sinon on prend le 1er, qui est le plus récent
                        Date dateFinMostRecent = new Date(famillePersonneEtendueMostRecent.getSimpleFamille()
                                .getFinDefinitive());
                        Date dateFinCurrent = new Date(famillePersonneEtendue.getSimpleFamille().getFinDefinitive());
                        if (dateFinCurrent.after(dateFinMostRecent)) {
                            famillePersonneEtendueMostRecent = famillePersonneEtendue;
                        }
                    }
                }
            }

            // Si on arrive la, c'est qu'on a trouvé aucun contribuable actif sur une des personnes assurées, on
            // retourne donc celui qui a la date de fin la plus récente.

            return famillePersonneEtendueMostRecent;
        } catch (Exception ex) {
            throw new JadePersistenceException("Erreur pendant la recherche de la personne " + nss, ex);
        }
    }

    protected FamillePersonneEtendue getPersonneEtendue(String nss) throws Exception {

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
            new Date();
            // Si on en trouve plusieurs, on relance la requête en ne spécifiant qu'on ne veut que les actifs
            famillePersonneEtendueSearch.setForMembreActifFromToday(Date.now().getSwissMonthValue());
            nbFamilleContribuable = AmalServiceLocator.getFamilleContribuableService().count(
                    famillePersonneEtendueSearch);
            if (nbFamilleContribuable == 1) {
                return (FamillePersonneEtendue) famillePersonneEtendueSearch.getSearchResults()[0];
            }
        }

        return null;
    }

    private void insertMessageForNss(String nss, String message) {
        if (!mapMessageParNSS.containsKey(nss)) {
            mapMessageParNSS.put(nss, new ArrayList<String>());
        }
        List<String> listErreurs = mapMessageParNSS.get(nss);
        listErreurs.add(message);
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

}
