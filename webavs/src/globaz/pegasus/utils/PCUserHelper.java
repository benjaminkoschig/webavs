/**
 * 
 */
package globaz.pegasus.utils;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.nnss.PRNSSUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author ECO
 */
public class PCUserHelper {

    public static String getDetailAssure(BSession session, PersonneEtendueComplexModel personne) {

        if (!JadeStringUtil.isEmpty(personne.getPersonne().getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(personne.getPersonneEtendue().getNumAvsActuel(), personne
                    .getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2(), personne.getPersonne()
                    .getDateNaissance(), PCUserHelper.getLibelleCourtSexe(personne.getPersonne()), PCUserHelper
                    .getLibellePays(personne.getTiers()), personne.getPersonne().getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(personne.getPersonneEtendue().getNumAvsActuel(), personne
                    .getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2(), personne.getPersonne()
                    .getDateNaissance(), PCUserHelper.getLibelleCourtSexe(personne.getPersonne()), PCUserHelper
                    .getLibellePays(personne.getTiers()));
        }
    }

    public static String getDetailAssure(BSession session, TiersSimpleModel tiers, PersonneSimpleModel personne,
            PersonneEtendueSimpleModel PersonneEtendue) {

        if (!JadeStringUtil.isEmpty(personne.getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(PersonneEtendue.getNumAvsActuel(),
                    tiers.getDesignation1() + " " + tiers.getDesignation2(), personne.getDateNaissance(),
                    PCUserHelper.getLibelleCourtSexe(personne), PCUserHelper.getLibellePays(tiers),
                    personne.getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(PersonneEtendue.getNumAvsActuel(), tiers.getDesignation1()
                    + " " + tiers.getDesignation2(), personne.getDateNaissance(),
                    PCUserHelper.getLibelleCourtSexe(personne), PCUserHelper.getLibellePays(tiers));
        }
    }

    public static String getDetailAssure(PersonneEtendueComplexModel personne) {
        return PCUserHelper.getDetailAssure(BSessionUtil.getSessionFromThreadContext(), personne);
    }

    public static String getDetailAssure(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        return PCUserHelper.getDetailAssure(BSessionUtil.getSessionFromThreadContext(), TIBusinessServiceLocator
                .getPersonneEtendueService().read(idTiers));

    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public static String getLibelleCourtSexe(String sexe) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (PRACORConst.CS_HOMME.equals(sexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(sexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    public static String getLibelleCourtSexe(PersonneSimpleModel personne) {
        return PCUserHelper.getLibelleCourtSexe(personne.getSexe());
    }

    public static String getLibelleCourtSexe(PersonneEtendueComplexModel personne) {
        return PCUserHelper.getLibelleCourtSexe(personne.getPersonne().getSexe());
    }

    public static String getLibellePays(PersonneEtendueComplexModel personne) {
        return PCUserHelper.getLibellePays(personne.getTiers());

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public static String getLibellePays(TiersSimpleModel tiers) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", tiers.getIdPays())))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", tiers.getIdPays()));
        }

    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public static String getNumeroAvsFormateSansPrefixe(PersonneEtendueSimpleModel personneEtendue) {
        String nss = personneEtendue.getNumAvsActuel();
        if (JadeStringUtil.isEmpty(nss)) {
            nss = "";
        }
        return NSUtil.formatWithoutPrefixe(nss, nss.length() > 14 ? true : false);
    }

}
