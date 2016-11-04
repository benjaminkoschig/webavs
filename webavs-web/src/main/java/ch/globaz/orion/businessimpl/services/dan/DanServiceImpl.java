package ch.globaz.orion.businessimpl.services.dan;

import globaz.globall.db.BSession;
import java.util.List;
import ch.globaz.orion.business.exceptions.OrionDanException;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.dan.DANService;
import ch.globaz.xmlns.eb.dan.Dan;
import ch.globaz.xmlns.eb.dan.DanStatutEnum;
import ch.globaz.xmlns.eb.dan.EBDanException_Exception;
import ch.globaz.xmlns.eb.dan.LienInstitution;
import ch.globaz.xmlns.eb.partnerweb.User;

/**
 * @author sco
 * @since 12 avr. 2011
 */
public class DanServiceImpl {

    public static byte[] downloadFile(String id, String type, String loginName, String userEmail, String langueIso)
            throws Exception {
        DANService serviceDan = null;
        User usr = null;

        usr = ServicesProviders.partnerWebServiceProvide(loginName, userEmail, langueIso)
                .readActivUserOrAdminByLoginName(loginName);
        serviceDan = ServicesProviders.danServiceProvide(loginName, userEmail, langueIso);
        return serviceDan.getPucsFileForWebavs(Integer.parseInt(id), usr.getUserId());
    }

    public static Dan getDanFile(String id, BSession session) throws EBDanException_Exception {
        DANService serviceDan = null;
        try {
            serviceDan = ServicesProviders.danServiceProvide(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceDan.readDan(Integer.parseInt(id));

    }

    public static Integer getInstitution(int type, int idDan, BSession session) throws EBDanException_Exception {
        DANService serviceDan = null;

        serviceDan = ServicesProviders.danServiceProvide(session);
        LienInstitution institution = serviceDan.findDanInstitutionForUtilisation(idDan, type);
        if (institution != null) {
            return institution.getIdInstitution();
        }
        return null;

    }

    public static void updateStatusDan(String idDan, DanStatutEnum danStatutEnum, BSession session) {
        DANService serviceDan = ServicesProviders.danServiceProvide(session);
        try {
            serviceDan.updateStatusDan(Integer.valueOf(idDan), Integer.valueOf(session.getUserId()), danStatutEnum);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (EBDanException_Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Dan> listDanFile(String likeNumAffilie, Integer forDateValidation, BSession session)
            throws EBDanException_Exception {
        DANService serviceDan = null;
        try {
            serviceDan = ServicesProviders.danServiceProvide(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceDan.searchDanForWebAvs(likeNumAffilie, forDateValidation);

    }

    public static void notifyFinished(String id, BSession session) throws OrionDanException {

        try {
            User usr = ServicesProviders.partnerWebServiceProvide(session).readActivUserOrAdminByLoginName(
                    session.getUserId());
            ServicesProviders.danServiceProvide(session).updateStatusDan(Integer.parseInt(id), usr.getUserId(),
                    DanStatutEnum.TRAITEE);
        } catch (Exception e) {
            throw new OrionDanException("Impossible de mettre à jour le statut", e);
        }

    }

    public static void preRempliDan(Dan dan, String noAffilie, List<ch.globaz.xmlns.eb.dan.Salaire> salaires,
            int annee, String idInstLaa, String idInstLpp, String login, boolean override, BSession session)
            throws EBDanException_Exception {
        DANService serviceDan = null;
        serviceDan = ServicesProviders.danServiceProvide(session);
        serviceDan.preRempliDan(dan, noAffilie, salaires, annee, idInstLaa, idInstLpp, login, override);

    }

    public static void preRempliDan(Dan dan, String noAffilie, List<ch.globaz.xmlns.eb.dan.Salaire> salaires,
            int annee, String idInstLaa, String idInstLpp, String login, boolean override, String userEmail)
            throws EBDanException_Exception {
        DANService serviceDan = null;
        // on passe "fr" comme langue car n'a pas d'impact pour le pré-remplissage
        serviceDan = ServicesProviders.danServiceProvide(login, userEmail, "fr");
        serviceDan.preRempliDan(dan, noAffilie, salaires, annee, idInstLaa, idInstLpp, login, override);
    }

}
