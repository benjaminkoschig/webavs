package ch.globaz.apg.businessimpl.services.upi;

import ch.globaz.common.business.services.upi.configuration.UPIProperties;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.pojo.Person;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.constantes.IConstantes;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.ech.xmlns.ech_0085._1.GetInfoPersonResponseType;
import ch.globaz.apg.business.services.upi.UPIService;

public class UPIServiceImpl implements UPIService {

    @Override
    public Person getPerson(String nss) throws Exception {
        if (JadeStringUtil.isBlank(nss)) {
            throw new IllegalArgumentException("Empty NSS received [" + nss + "]");
        }

        String checkUpi = GlobazServer.getCurrentSystem().getApplication("APG").getProperty("rapg.checkUpi");
        if (!JadeStringUtil.isEmpty(checkUpi) && "true".equals(checkUpi)) {
            BSession session = null;
            Person person = null;
            try {
                person = new Person();
                person.setNss(nss);
                session = BSessionUtil.getSessionFromThreadContext();

                initializeUpiProperties();

                GetInfoPersonResponseType personUpi = ch.globaz.common.business.services.upi.UPIService.getInstance()
                        .getInfoPerson(session.getUserId(), nss);
                person.setNom(personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getOfficialName());
                person.setPrenom(personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getFirstNames());
                XMLGregorianCalendar dateXml = personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson()
                        .getDateOfBirth().getYearMonthDay();
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.set(Calendar.DAY_OF_MONTH, dateXml.getDay());
                calendar.set(Calendar.MONTH, dateXml.getMonth() - 1);
                calendar.set(Calendar.YEAR, dateXml.getYear());
                person.setDateNaissance(JadeDateUtil.getGlobazFormattedDate(calendar.getTime()));
                person.setNationalite(personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getNationality()
                        .getCountryId().toString());
                String sexe = personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getSex();
                if ("2".equals(sexe)) {
                    person.setSexe(IConstantes.CS_PERSONNE_SEXE_FEMME);
                } else {
                    person.setSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);
                }
            } catch (Exception exception) {
                String message = session.getLabel("UPI_ERREUR_ACCES_SERVICES_UPI");
                message += " : " + exception.toString();
                throw new Exception(message);
            }

            return person;
        } else {
            return null;
        }

    }

    private void initializeUpiProperties() throws PropertiesException {
        UPIProperties.UPI_WEBSERVICE_NAMESPACE.setValue(CommonProperties.UPI_WEBSERVICE_NAMESPACE.getValue());
        UPIProperties.UPI_WEBSERVICE_NAME.setValue(CommonProperties.UPI_WEBSERVICE_NAME.getValue());
        UPIProperties.UPI_WEBSERVICE_SEDEX_SENDER_ID.setValue(CommonProperties.UPI_WEBSERVICE_SEDEX_SENDER_ID.getValue());
        UPIProperties.UPI_KEYSTORE_PATH.setValue(CommonProperties.UPI_KEYSTORE_PATH.getValue());
        UPIProperties.UPI_KEYSTORE_TYPE.setValue(CommonProperties.UPI_KEYSTORE_TYPE.getValue());
        UPIProperties.UPI_KEYSTORE_PASSWORD.setValue(CommonProperties.UPI_KEYSTORE_PASSWORD.getValue());
        UPIProperties.UPI_SSL_CONTEXT_TYPE.setValue(CommonProperties.UPI_SSL_CONTEXT_TYPE.getValue());
        UPIProperties.UPI_WSDL_RESOURCE_PATH.setValue(CommonProperties.UPI_WSDL_RESOURCE_PATH.getValue());
    }
}
