package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.util.CommonNSSFormater;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import ch.gdk_cds.xmlns.da_64a_common._1.AddressType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.globaz.amal.business.constantes.TypesOfLossEnum;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur;
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
    private List<String> errors = null;
    protected List<String> personnesNotFound = null;
    protected List<String> listCriteres = null;

    public AnnoncesCODefault() {
        personnesNotFound = new ArrayList<String>();
        errors = new ArrayList<String>();
    }

    public void setListCriteres(List<String> listCriteres) {
        this.listCriteres = listCriteres;
    }

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

    protected FamillePersonneEtendue getPersonneEtendue(String nss, Boolean onlyPrincipal) throws Exception {

        // try {
        if (nss == null || nss.isEmpty()) {
            return null;
        }

        CommonNSSFormater nssFormater = new CommonNSSFormater();
        String nssFormate = nssFormater.format(nss);

        FamillePersonneEtendueSearch famillePersonneEtendueSearch = new FamillePersonneEtendueSearch();

        if (onlyPrincipal) {
            famillePersonneEtendueSearch.setIsContribuablePrincipal(Boolean.TRUE);
        }

        famillePersonneEtendueSearch.setLikeNss(nssFormate);
        famillePersonneEtendueSearch = AmalServiceLocator.getFamilleContribuableService().search(
                famillePersonneEtendueSearch);
        int nbFamilleContribuable = famillePersonneEtendueSearch.getNbOfResultMatchingQuery();
        // Si on trouve un seul membre, on retourne celui la
        if (nbFamilleContribuable == 1) {
            return (FamillePersonneEtendue) famillePersonneEtendueSearch.getSearchResults()[0];
        } else if (nbFamilleContribuable > 1) {
            // Si on en trouve plusieurs, on relance la requête en ne spécifiant qu'on ne veut que les actifs
            famillePersonneEtendueSearch.setForMembreActifFromToday(Date.now().getSwissMonthValue());
            nbFamilleContribuable = AmalServiceLocator.getFamilleContribuableService().count(
                    famillePersonneEtendueSearch);
            if (nbFamilleContribuable == 1) {
                return (FamillePersonneEtendue) famillePersonneEtendueSearch.getSearchResults()[0];
            }
        }
        // } catch (Exception ex) {
        // throw new AnnoncesedexCORuntimeException(ex.getMessage());
        // }

        return null;
    }

    /**
     * Règles :
     * Si le débiteur existe avec le nss passé, on utilise celui ci
     * Sinon, on prend le premier contribuable actif trouvé d'une personne assurée
     * Et enfin, on prend un contribuable qui a une fin de droit la plus récente et on met un message
     * 
     * @param nss
     * @param personnesAssurees
     * @param annonceSedexCODebiteur
     * @return
     * @throws JadePersistenceException
     */
    protected FamillePersonneEtendue searchPersonne(String nss, List<InsuredPersonWithClaimType> personnesAssurees,
            SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur) throws JadePersistenceException {

        try {
            FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(nss, Boolean.TRUE);

            if (famillePersonneEtendue != null) {
                return famillePersonneEtendue;
            }

            // Si on arrive jusqu'ici, c'est qu'on a trouvé aucun membre avec ce nss, on tente de récupérer le 1er
            // contribuable actif qu'on trouve sur une des personnes assurées...
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
                    throw new IllegalArgumentException(e.getMessage());
                }
                FamillePersonneEtendueSearch famillePersonneEtendueSearch = new FamillePersonneEtendueSearch();
                famillePersonneEtendueSearch.setLikeNss(nssPersonneAssureeFormate);
                famillePersonneEtendueSearch.setIsContribuablePrincipal(Boolean.TRUE);
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
            annonceSedexCODebiteur.addMessage("Contribuable non concordants !");
            return famillePersonneEtendueMostRecent;
        } catch (Exception ex) {
            throw new JadePersistenceException("Erreur pendant la recherche de la personne " + nss, ex);
        }
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

    protected TypesOfLossEnum getTypeActe(String typeOfLoss) {
        if (TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN.getValue().equals(typeOfLoss)) {
            return TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN;
        } else if (TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN_FAILLITE.getValue().equals(typeOfLoss)) {
            return TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN_FAILLITE;
        } else if (TypesOfLossEnum.TITRE_EQUIVALENT.getValue().equals(typeOfLoss)) {
            return TypesOfLossEnum.TITRE_EQUIVALENT;
        } else {
            return null;
        }
    }

    protected void logErrors(String source, String error, Throwable exception) {
        logErrors(source, exception.getMessage());
    }

    protected void logErrors(String source, String error) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }

        JadeThread.logError(source, error);
        errors.add(source + " : " + error);
    }

    private List<String> getErrors() {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        return errors;
    }

    private List<String> getPersonnesNotFoundList() {
        if (personnesNotFound == null) {
            personnesNotFound = new ArrayList<String>();
        }
        return personnesNotFound;
    }

    protected String getSubjectMail() {
        return "Contentieux Amal : réception des annonces contentieux effectuée avec succès !";
    }

    protected void sendMail(File file) {
        String subject = getSubjectMail();
        StringBuilder body = new StringBuilder();

        if (listCriteres != null && !listCriteres.isEmpty()) {
            body.append("Critères\n");
            for (String critere : listCriteres) {
                body.append("&nbsp;&nbsp;&nbsp;&nbsp;- " + critere + "\n");
            }
        }

        if (!getPersonnesNotFoundList().isEmpty()) {
            if (getPersonnesNotFoundList().size() > 1) {
                subject = getPersonnesNotFoundList().size() + " personnes non connues détectées !";
            } else {
                subject = "1 personne non connue détectée !";
            }

            body.append("Liste des personnes non trouvées :\n");
            for (String personneInexistante : getPersonnesNotFoundList()) {
                body.append("   -" + personneInexistante + "\n");
            }

        }

        if (!getErrors().isEmpty()) {
            body.append("Autres erreurs détectée(s)");
            for (String error : errors) {
                body.append(error);
            }
        }

        String[] files = new String[1];
        if (file != null) {
            files[0] = file.getPath();
        }

        try {
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    body.toString(), files);
        } catch (Exception e) {
            JadeThread.logError("Sendmail", "Erreur lors de l'envoi du mail : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi du mail : " + e.getMessage());
        }
    }

    protected List<String> getErrorsList() {
        return errors;
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
