package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.util.CommonNSSFormater;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    public enum TypesOfLossEnum {
        ACTE_DE_DEFAUT_BIEN("1", "42003821"),
        ACTE_DE_DEFAUT_BIEN_FAILLITE("2", "42003822"),
        TITRE_EQUIVALENT("3", "42003823");

        private String value;
        private String cs;

        public String getValue() {
            return value;
        }

        public String getCs() {
            return cs;
        }

        private TypesOfLossEnum(String value, String cs) {
            this.value = value;
            this.cs = cs;
        }
    }

    public enum PaymentCategoryEnum {
        PAIEMENT_DEBITEUR("1"),
        RP_RETROACTIVE("2"),
        ANNULATION("3");

        private String value;

        public String getValue() {
            return value;
        }

        private PaymentCategoryEnum(String value) {
            this.value = value;
        }

        public static boolean isPaiementDebiteur(String value) {
            return PAIEMENT_DEBITEUR.getValue().equals(value);
        }

        public static boolean isRPRetro(String value) {
            return RP_RETROACTIVE.getValue().equals(value);
        }

        public static boolean isAnnulation(String value) {
            return ANNULATION.getValue().equals(value);
        }
    }

    protected String passSedex = "";
    protected String userSedex = "";
    protected JadeContext context;
    protected JAXBServices jaxbs;
    protected BSession session;
    protected Unmarshaller unmarshaller;
    protected Marshaller marshaller;
    private List<String> errors = null;
    protected List<String> personnesNotFound = null;

    public AnnoncesCODefault() {
        personnesNotFound = new ArrayList<String>();
        errors = new ArrayList<String>();
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
                // Si on en trouve plusieurs, on relance la requête en ne spécifiant qu'on ne veut que les actifs
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
        exception.printStackTrace();
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

    protected void sendMail(File file) throws Exception {
        String subject = getSubjectMail();
        StringBuilder body = new StringBuilder();
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

        JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                body.toString(), files);
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
