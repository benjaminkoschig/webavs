package ch.globaz.common.businessimpl.services;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import ch.ech.xmlns.ech_0085._1.GetInfoPersonResponseType;
import ch.globaz.common.business.exceptions.UPIException;
import ch.globaz.common.business.models.InfosPersonResponseType;
import ch.globaz.common.business.services.UPIService;
import ch.globaz.orion.EBApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

public class UPIServiceImpl implements UPIService {

    private static final short CODE_ERREUR_FORMELLE_DONNES_TRANSMISE = 1;
    private static final short CODE_ERREUR_AVS_NON_TROUVE = 3;
    private static final short CODE_ERREUR_AVS_ANNULE = 4;
    private static final short CODE_ERREUR_SERVICE_NON_DISPONIBLE = 10;
    private static final short UPI_ERREUR_ACCES_SERVICES_UPI = 99;

    @Override
    public InfosPersonResponseType getPerson(String nss) throws Exception {
        if (JadeStringUtil.isBlank(nss)) {
            throw new IllegalArgumentException("Empty NSS received [" + nss + "]");
        }

        BSession session = null;
        InfosPersonResponseType personResponseType = new InfosPersonResponseType();
        try {
            session = new BSession(EBApplication.APPLICATION_ID);
            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            GetInfoPersonResponseType personUpi = ch.globaz.common.business.services.upi.UPIService.getInstance()
                    .getInfoPersonByCommonWebAVSUPI(session.getUserId(), nss);

            personResponseType = buildPersonResponse(personUpi, nss, session);
        } catch (WebServiceException wsException) {
            throw new Exception("Impossible d'accéder aux WebService : \n" + wsException.getMessage());
        } catch (MalformedURLException malformeURLException) {
            throw new Exception("Exception levée lors d'un accès au service UPI - URL mal formé (erreur technique)");
        } catch (UPIException upiException) {
            throw new Exception("Exception levée lors d'un accès au service UPI (Unique Person Identifier)"
                    + upiException.getMessage());
        } catch (Exception exception) {
            String message = session.getLabel("UPI_ERREUR_ACCES_SERVICES_UPI");
            message += " : " + exception.getMessage();
            throw new Exception(message);
        }

        return personResponseType;
    }

    private InfosPersonResponseType buildPersonResponse(GetInfoPersonResponseType personUpi, String nss,
            BSession session) {
        InfosPersonResponseType personResponse = new InfosPersonResponseType();
        personResponse.setNss(nss);
        if (personUpi.getRefused() != null) {
            personResponse = buildPersonResponseError(personResponse, personUpi, session);

        } else if (personUpi.getAccepted() != null) {
            personResponse.setNom(personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getOfficialName());
            personResponse.setPrenom(personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getFirstNames());
            XMLGregorianCalendar dateXml = personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson()
                    .getDateOfBirth().getYearMonthDay();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, dateXml.getDay());
            calendar.set(Calendar.MONTH, dateXml.getMonth() - 1);
            calendar.set(Calendar.YEAR, dateXml.getYear());
            personResponse.setDateNaissance(JadeDateUtil.getGlobazFormattedDate(calendar.getTime()));
            personResponse.setNationalite(transfomCountryIDToWebAVSCode(
                    personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getNationality().getCountryId()));
            String sexe = personUpi.getAccepted().getValuesStoredUnderAhvvn().getPerson().getSex();
            if ("2".equals(sexe)) {
                personResponse.setSexe("F");
            } else {
                personResponse.setSexe("H");
            }
        }
        return personResponse;
    }

    private String transfomCountryIDToWebAVSCode(Integer countryId) {
        Integer codePays = 0;
        if (countryId != null && countryId > 8000) {
            codePays = countryId - 8000;
            return codePays.toString();
        }
        return codePays.toString();
    }

    private InfosPersonResponseType buildPersonResponseError(InfosPersonResponseType personResponse,
            GetInfoPersonResponseType personUpi, BSession session) {
        switch (personUpi.getRefused().getReason()) {
            case CODE_ERREUR_FORMELLE_DONNES_TRANSMISE:
                personResponse.setCodeErreur(String.valueOf(CODE_ERREUR_FORMELLE_DONNES_TRANSMISE));
                personResponse.setMessageErreur(session.getLabel("UPI_ERREUR_1_ERREUR_DONNEES_TRANSMISES"));
                break;
            case CODE_ERREUR_AVS_NON_TROUVE:
                personResponse.setCodeErreur(String.valueOf(CODE_ERREUR_AVS_NON_TROUVE));
                personResponse.setMessageErreur(session.getLabel("UPI_ERREUR_3_NAVS_NON_TROUVE"));
                break;
            case CODE_ERREUR_AVS_ANNULE:
                personResponse.setCodeErreur(String.valueOf(CODE_ERREUR_AVS_ANNULE));
                personResponse.setMessageErreur(session.getLabel("UPI_ERREUR_4_NAVS_ANNULE"));
                break;
            case CODE_ERREUR_SERVICE_NON_DISPONIBLE:
                personResponse.setCodeErreur(String.valueOf(CODE_ERREUR_SERVICE_NON_DISPONIBLE));
                personResponse.setMessageErreur(session.getLabel("UPI_ERREUR_10_SERVICE_INDISPONIBLE"));
                break;
            default:
                personResponse.setCodeErreur(String.valueOf(UPI_ERREUR_ACCES_SERVICES_UPI));
                personResponse.setMessageErreur(session.getLabel("UPI_ERREUR_ACCES_SERVICES_UPI"));
        }
        return personResponse;
    }
}
