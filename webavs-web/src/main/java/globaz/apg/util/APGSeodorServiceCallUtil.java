package globaz.apg.util;

import ch.admin.zas.seodor.ws.service_periods._1.ServicePeriodsPort10;
import ch.admin.zas.seodor.ws.service_periods._1.ServicePeriodsService10;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import com.sun.xml.internal.ws.client.ClientTransportException;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.properties.APProperties;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.prestation.beans.PRPeriode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seodor.ws.GetServicePeriodsRequestType;
import seodor.ws.GetServicePeriodsResponseType;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class APGSeodorServiceCallUtil {
    private static final String SSL_SOCKET_FACTORY_ORACLE_JDK = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_JAX_WS_RI = "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";
    private static final Logger LOG = LoggerFactory.getLogger(APGSeodorServiceCallUtil.class);

        public static final List<APGSeodorDataBean> getPeriode(BSession session, APGSeodorDataBean apgSeodorDataBean) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            List<APGSeodorDataBean> apgSeodorDataBeans = new ArrayList<>();

            try {
                String webServiceNameSpace = CommonProperties.SEODOR_WEBSERVICE_NAMESPACE.getValue();
                String webServiceName = CommonProperties.SEODOR_WEBSERVICE_NAME.getValue();

                String keyStorePath = CommonProperties.SEODOR_KEYSTORE_PATH.getValue();
                String keyStoreType = CommonProperties.SEODOR_KEYSTORE_TYPE.getValue();
                String keyStorePass = CommonProperties.SEODOR_KEYSTORE_PASSWORD.getValue();
                String contextType = CommonProperties.SEODOR_SSL_CONTEXT_TYPE.getValue();

                URL wsdlLocation = classloader.getResource(CommonProperties.SEODOR_SEODOR_WSDL_PATH.getValue());

                // Création du ServicePort
                ServicePeriodsPort10 port = createServicePeriodsPort(wsdlLocation, webServiceNameSpace, webServiceName);

                // Condition pour générer la config SSL et le Binding
                if (!JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_PATH.getValue())
                        && !JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_TYPE.getValue())
                        && !JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_PASSWORD.getValue())) {

                    // Config SSL
                    configureSSLOnTheClient(port, keyStorePath, keyStorePass, keyStoreType);

                    BindingProvider bindingProvider = (BindingProvider) port;

                    // Si la propriété ide.webservice.url.endpoint existe on surcharge l'adresse du endpoint
                    String endpoint = CommonProperties.SEODOR_ENDPOINT_ADDRESS.getValue();
                    if (StringUtils.isNotEmpty(endpoint)) {
                        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,  endpoint);
                    }
                }

            GetServicePeriodsRequestType requestDelivery = APGSeodorServiceMappingUtil.convertSeodorDataBeanToRequestDelivery(apgSeodorDataBean);

            GetServicePeriodsResponseType responseDelivery = port.getServicePeriods(requestDelivery);

            apgSeodorDataBeans = APGSeodorServiceMappingUtil.putResponseDeliveryResultInApgSeodorDataBean(session, responseDelivery, apgSeodorDataBean);

            } catch (ConnectException e) {
                if (apgSeodorDataBean == null) {
                    apgSeodorDataBean = new APGSeodorDataBean();
                }
                apgSeodorDataBean.setHasTechnicalError(true);
                String message = session.getLabel("WEBSERVICE_SEODOR_DELAIS_DEPASSE");
                apgSeodorDataBean.setMessageTechnicalError(message);
                LOG.error(message, e);
            } catch (SSLHandshakeException | ClientTransportException e) {
                if (apgSeodorDataBean == null) {
                    apgSeodorDataBean = new APGSeodorDataBean();
                }
                apgSeodorDataBean.setHasTechnicalError(true);
                String message = session.getLabel("WEBSERVICE_SEODOR_AUTHENTICATION_INVALID");
                apgSeodorDataBean.setMessageTechnicalError(message);
                LOG.error(message, e);
            } catch (Exception e) {
                if (apgSeodorDataBean == null) {
                    apgSeodorDataBean = new APGSeodorDataBean();
                }
                apgSeodorDataBean.setHasTechnicalError(true);
                String message = session.getLabel("SEODOR_ERREUR_TECHNIQUE_PARAMETRE");
                apgSeodorDataBean.setMessageTechnicalError(message);
                //apgSeodorDataBean.setMessageForUser(message);
                LOG.error(message, e);
            } finally {

                if (Objects.isNull(apgSeodorDataBeans)) {
                    apgSeodorDataBean = new APGSeodorDataBean();
                }

                if (apgSeodorDataBean.isHasTechnicalError()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TechnicalError as occurr : errorReason = [");
                    //sb.append(apgSeodorDataBean.getErrorReason());
                    sb.append("] ");
                    sb.append("errorDetailedReason = [");
                    //sb.append(apgSeodorDataBean.getErrorDetailedReason());
                    sb.append("] ");
                    sb.append("errorComment = [");
                    //sb.append(apgSeodorDataBean.getErrorComment());
                    sb.append("]");
                    LOG.error(sb.toString());
                    apgSeodorDataBeans.add(apgSeodorDataBean);
                }

                return apgSeodorDataBeans;
            }
        }

    /**
     * Méthode qui créé le ServicePeriodsPort
     *
     * @param wsdlLocation
     * @param webServiceNameSpace
     * @param webServiceName
     * @return
     */
        private static ServicePeriodsPort10 createServicePeriodsPort(URL wsdlLocation, String webServiceNameSpace, String webServiceName) {
            ServicePeriodsService10 servicePeriodsService10 = new ServicePeriodsService10(wsdlLocation,
                    new QName(webServiceNameSpace, webServiceName));
            return servicePeriodsService10.getPort(ServicePeriodsPort10.class);
        }

    /**
     * Configure SSL On the client
     * @param proxy webservice proxy
     * @param keyStorePath keyStorePath certificat filename with full path
     * @param keyStorePass certificat password
     * @param keyStoreType keyStoreType key types
     */
    private static void configureSSLOnTheClient(final ServicePeriodsPort10 proxy, final String keyStorePath, final String keyStorePass, String keyStoreType) throws Exception {

        SSLContext sc = null;
        KeyStore ks = null;

        try {
            sc = SSLContext.getInstance(CommonProperties.SEODOR_SSL_CONTEXT_TYPE.getValue());

            // Read the certificate
            FileInputStream filePkcs12;
            ks = KeyStore.getInstance(keyStoreType);
            filePkcs12 = new FileInputStream(keyStorePath);

            // For a better security you can encode your password and decode it here
            String certPasswd = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(
                    keyStorePass);
            ks.load(filePkcs12, certPasswd.toCharArray());

            try {
                filePkcs12.close();
            } catch (final IOException e) {
                System.err.println("Error on close " + keyStorePath + " file");
                throw e;
            }

            // Add certificate to the conduit
            final KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(ks, certPasswd.toCharArray());
            final KeyManager[] km = keyFactory.getKeyManagers();
            sc.init(keyFactory.getKeyManagers(), null, null);

            final Map<String, Object> ctxt = ((BindingProvider) proxy).getRequestContext();
            ctxt.put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
            ctxt.put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());

        } catch (final FileNotFoundException e) {
            System.err.println("File " + keyStorePath + " doesn't exist");
            throw e;
        } catch (final IOException ioe) {
            System.err.println("File " + keyStorePath + " doesn't exist. Cause by " + ioe.getCause());
            throw ioe;
        } catch (final KeyStoreException kse) {
            System.out.println("Security configuration failed with the following: " + kse.getCause());
            throw kse;
        } catch (final NoSuchAlgorithmException nsa) {
            System.out.println("Security configuration failed with the following: " + nsa.getCause());
            throw nsa;
        } catch (final GeneralSecurityException gse) {
            System.out.println("Security configuration failed with the following: " + gse.getCause());
            throw gse;
        } catch (Exception e) {
            System.out.println("Security configuration failed with the following: " + e.getCause());
            throw e;
        }
    }

    public static void callWSSeodor(APDroitAPGPViewBean viewBean, FWDispatcher mainDispatcher) {
        APGSeodorErreurListEntities messagesError = callWSSeodor(APGenreServiceAPG.resoudreGenreParCodeSystem(viewBean.getGenreService()).getCodePourAnnonce(), viewBean.getNss(), viewBean.getPeriodes(), viewBean.getNbrJourSoldes(), (BSession) mainDispatcher.getSession());

        // On ajoute les erreurs à la ViewBean et on la tag pour afficher les erreurs lors du rechargement de la page.
        if (!messagesError.getMessageErreur().isEmpty()) {
            viewBean.setMessagePropError(true);
            messagesError.setSession(((BSession) mainDispatcher.getSession()));
            viewBean.setMessagesError(messagesError);
        }
    }

    public static APGSeodorErreurListEntities callWSSeodor(String genreServiceParam, String nssParam, List<PRPeriode> periodes, String nombreJoursSoldes, BSession session) {

        List<APGSeodorDataBean> apgSeodorDataBeans = new ArrayList<>();
        APGSeodorErreurListEntities messagesError = new APGSeodorErreurListEntities();
        APGSeodorDataBean apgSeodorDataBean = new APGSeodorDataBean();

        try{
            if (StringUtils.isNotEmpty(APProperties.SEODOR_TYPE_SERVICE.getValue()) && Objects.nonNull(genreServiceParam)) {
                if (APProperties.SEODOR_TYPE_SERVICE.getValue().contains(genreServiceParam)) {
                    String nss = nssParam.replaceAll("\\.","");
                    PRPeriode periode1er = periodes.get(0);
                    Date dateDebut = new Date(periode1er.getDateDeFin());
                    apgSeodorDataBean.setNss(nss);
                    XMLGregorianCalendar dateDebutGregorian = dateDebut.toXMLGregorianCalendar();
                    dateDebutGregorian.setDay(1);
                    dateDebutGregorian.setMonth(1);
                    apgSeodorDataBean.setStartDate(dateDebutGregorian);
                    apgSeodorDataBeans = APGSeodorServiceCallUtil.getPeriode(session, apgSeodorDataBean);
                    if (apgSeodorDataBeans.size() > 0 && apgSeodorDataBeans.get(0).isHasTechnicalError()) {
                        messagesError.setMessageErreur(apgSeodorDataBeans.get(0).getMessageTechnicalError());
                    } else {
                        int genreService = Integer.valueOf(genreServiceParam);

                        // On va regarder si l'on trouve des périodes pour le code service renseigné
                        int nombrePeriodeGenreService = APGSeodorServiceMappingUtil.calcNombrePeriodeGenreService(apgSeodorDataBeans, genreService);

                        if (!apgSeodorDataBeans.isEmpty() && nombrePeriodeGenreService != 0) {
                            if (apgSeodorDataBeans.get(0).isHasTechnicalError()) {
                                messagesError.setMessageErreur(apgSeodorDataBeans.get(0).getMessageTechnicalError());
                            } else {
                                List<PRPeriode> periodesAControler = periodes;
                                messagesError = APGSeodorServiceMappingUtil.controlePeriodesSeodor(apgSeodorDataBeans, periodesAControler
                                        ,Long.valueOf(nombreJoursSoldes) , session.getLabel("DIFFERENCE_PERIODES_ANNONCEES")
                                        , genreService, nombrePeriodeGenreService);
                            }
                        } else {
                            messagesError.setMessageErreur(session.getLabel("WEBSERVICE_SEODOR_PAS_DE_DONNEES"));
                        }
                    }
                }
            } else {
                messagesError.setMessageErreur(session.getLabel("WEBSERVICE_SEODOR_PROP_MANQUANTE"));
            }
        } catch (PropertiesException e) {
            // La propriété n'existe pas
            LOG.error("La propriété apg.rapg.genre.service.seodor n'a pas été trouvé : ", e);
            messagesError.setMessageErreur(session.getLabel("WEBSERVICE_SEODOR_PROP_MANQUANTE"));
        } catch (DatatypeConfigurationException e) {
            LOG.error("Erreur de données reçu par le WebService : ", e);
            messagesError.setMessageErreur(session.getLabel("SEODOR_ERREURS_DONNEES_WS"));
        } catch (ParseException e) {
            LOG.error("Structure des données non conforme reçu par le WS : ", e);
            messagesError.setMessageErreur(session.getLabel("SEODOR_ERREURS_STRUCTURE_CENTRALE"));
        }

        return messagesError;
    }
}
