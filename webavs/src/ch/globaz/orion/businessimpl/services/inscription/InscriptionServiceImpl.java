package ch.globaz.orion.businessimpl.services.inscription;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.exceptions.OrionInscriptionException;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.partnerweb.EBPartnerWebException_Exception;
import ch.globaz.xmlns.eb.partnerweb.InscriptionBack;
import ch.globaz.xmlns.eb.partnerweb.InscriptionBackStatusEnum;
import ch.globaz.xmlns.eb.partnerweb.PartnerWebService;

public class InscriptionServiceImpl {

    public static void changeStatusForInsc(int inscId, InscriptionBackStatusEnum status, BSession session,
            String message) throws EBPartnerWebException_Exception {
        PartnerWebService pwService = null;
        String owner = null;
        try {
            pwService = ServicesProviders.partnerWebServiceProvide(session);

            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            owner = app.getProperty("ebusiness.owner");
        } catch (Exception e) {
            e.printStackTrace();
        }
        pwService.changeStatusForInscription(inscId, status, owner, message);
    }

    public static String createAffilieAndAdmin(InscriptionEbusiness insc, BSession session)
            throws OrionInscriptionException, EBPartnerWebException_Exception {

        PartnerWebService pwService = null;
        try {
            pwService = ServicesProviders.partnerWebServiceProvide(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pwService.createAffilieAndAdminForInsc(insc.getRaisonSociale(), insc.getNumAffilie(), "", insc.getNom(),
                insc.getPrenom(), insc.getMail(), insc.getTel());

    }

    public static InscriptionEbusiness[] listeInscriptionsNouvelle(BSession session) throws OrionInscriptionException,
            EBPartnerWebException_Exception, Exception {

        PartnerWebService pwService = null;
        String owner = null;
        try {
            pwService = ServicesProviders.partnerWebServiceProvide(session);

            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            owner = app.getProperty("ebusiness.owner");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return InscriptionServiceImpl.mapInscEbu(pwService.searchInscriptionsForWebAvs(owner));

    }

    public static InscriptionEbusiness[] mapInscEbu(List<InscriptionBack> listInscOr) {
        List<InscriptionEbusiness> listInscRet = new ArrayList<InscriptionEbusiness>();

        for (InscriptionBack insc : listInscOr) {
            InscriptionEbusiness inscRet = new InscriptionEbusiness();
            inscRet.setIdInscription(String.valueOf(insc.getIdInscription()));
            inscRet.setMail(insc.getMail());
            inscRet.setModeDeclSalaire(String.valueOf(insc.getModeDeclSalaire()));
            inscRet.setNom(insc.getNom());
            inscRet.setNumAffilie(insc.getNumAffilie());
            inscRet.setOwner(insc.getOwner());
            inscRet.setPrenom(insc.getPrenom());
            inscRet.setRaisonSociale(insc.getRaisonSociale());
            inscRet.setTel(insc.getTel());
            listInscRet.add(inscRet);
        }
        InscriptionEbusiness[] inscArray = listInscRet.toArray(new InscriptionEbusiness[listInscRet.size()]);
        return inscArray;
    }

}
