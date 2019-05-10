package ch.globaz.orion.businessimpl.services.acl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.orion.business.models.acl.Acl;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.acl.ACLService;
import ch.globaz.xmlns.eb.acl.AclFindResultBeanForWebAvs;
import ch.globaz.xmlns.eb.acl.AclStatutEnum;
import ch.globaz.xmlns.eb.acl.EBACLException_Exception;
import globaz.globall.db.BSession;

public class AclServiceImpl {

    public static void changeStatus(int idAcl, AclStatutEnum status, BSession session) throws EBACLException_Exception {
        ACLService serviceAcl = null;
        serviceAcl = ServicesProviders.aclServiceProvide(session);
        serviceAcl.changeStatus(idAcl, status);
    }

    public static Acl[] listACl(AclStatutEnum status, BSession session) throws EBACLException_Exception {

        List<AclFindResultBeanForWebAvs> listeAclBack = ServicesProviders.aclServiceProvide(session)
                .searchACLForWebAvs(status);
        List<Acl> listeAcl = new ArrayList<Acl>();
        for (AclFindResultBeanForWebAvs aclBack : listeAclBack) {
            Acl aclRetour = AclServiceImpl.parseAnnonceCollaborateurToAcl(aclBack);
            listeAcl.add(aclRetour);
        }
        Acl[] aclArray = listeAcl.toArray(new Acl[listeAcl.size()]);
        return aclArray;
    }

    private static String mapLength(int dayOrMont) {
        String retour = String.valueOf(dayOrMont);
        if (retour.length() == 1) {
            return "0" + retour;
        } else {
            return retour;
        }

    }

    public static Acl parseAnnonceCollaborateurToAcl(AclFindResultBeanForWebAvs annonceCollaborateur) {
        Acl acl = new Acl();
        acl.setIdAnnonceCollaborateur(String.valueOf(annonceCollaborateur.getIdAnnonceCollaborateur()));
        acl.setDateEngagement(AclServiceImpl.parseDate(annonceCollaborateur.getDateEngagement()));
        acl.setDateSaisie(AclServiceImpl.parseDate(annonceCollaborateur.getDateSaisie()));
        acl.setDuplicata(annonceCollaborateur.isDuplicata());
        acl.setNumeroAffilie(annonceCollaborateur.getNoAffilie());
        acl.setNumeroAssure(annonceCollaborateur.getNumeroAssure());
        acl.setStatut(AclServiceImpl.parseStatusToString((annonceCollaborateur.getStatut())));
        acl.setNouveauNumero(annonceCollaborateur.getNouveauNumeroAvs());
        acl.setNoEmploye(annonceCollaborateur.getNoEmploye());
        acl.setNoSuccursale(annonceCollaborateur.getNoSuccursale());
        return acl;
    }

    private static String parseDate(XMLGregorianCalendar date) {
        return AclServiceImpl.mapLength(date.getDay()) + "." + AclServiceImpl.mapLength(date.getMonth()) + "."
                + String.valueOf(date.getYear());

    }

    private static String parseStatusToString(AclStatutEnum status) {
        if (AclStatutEnum.SAISIE.equals(status)) {
            return "0";
        } else if (AclStatutEnum.EN_COURS.equals(status)) {
            return "1";
        } else if (AclStatutEnum.TERMINE.equals(status)) {
            return "2";
        } else if (AclStatutEnum.PROBLEME.equals(status)) {
            return "3";
        } else {
            return "";
        }
    }

    public static void updateNouveauNumeroAvs(int idAcl, String nouveauNumero, BSession session)
            throws EBACLException_Exception {
        ACLService serviceAcl = null;
        try {
            serviceAcl = ServicesProviders.aclServiceProvide(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        serviceAcl.updateNouveauNumeroAvs(idAcl, nouveauNumero);
    }

    private AclStatutEnum parseStatusToEnum(String status) {
        if ("0".equals(status)) {
            return AclStatutEnum.SAISIE;
        } else if ("1".equals(status)) {
            return AclStatutEnum.EN_COURS;
        } else if ("2".equals(status)) {
            return AclStatutEnum.TERMINE;
        } else {
            return AclStatutEnum.PROBLEME;
        }
    }

}
