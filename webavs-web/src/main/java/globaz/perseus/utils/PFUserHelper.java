/**
 * 
 */
package globaz.perseus.utils;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.constantes.IConstantes;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.web.application.PFApplication;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author ECO
 * 
 */
public class PFUserHelper {

    /**
     * 
     * @param personne
     * @param date
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static AdresseTiersDetail getAdresseAssure(PersonneEtendueComplexModel personne, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        return PFUserHelper.getAdresseAssure(personne.getPersonneEtendue().getIdTiers(), date);
    }

    /**
     * A utiliser si on veut faire une "cascade de type d adresse", pour chaque type faire une cascade de domaine si
     * true.
     * 
     * @param idTiers
     * @param date
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static AdresseTiersDetail getAdresseAssure(String idTiers, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        // Retrouver la localité du tiers
        AdresseTiersDetail adresseTiersDetail = null;
        adresseTiersDetail = PFUserHelper.getAdresseAssure(idTiers, IConstantes.CS_AVOIR_ADRESSE_DOMICILE, date);

        if (adresseTiersDetail.getFields() == null) {
            adresseTiersDetail = PFUserHelper.getAdresseAssure(idTiers, IConstantes.CS_AVOIR_ADRESSE_COURRIER, date);

            if (adresseTiersDetail.getFields() == null) {
                throw new PerseusException(JadeThread.getMessage("PFUSERHELPER_EXCEPTION_ADRESSE_ASSURE"));
            }
        }

        return adresseTiersDetail;
    }

    /**
     * 
     * @param idTiers
     * @param typeAdresse
     * @param date
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static AdresseTiersDetail getAdresseAssure(String idTiers, String typeAdresse, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        return PFUserHelper.getAdresseAssure(idTiers, typeAdresse, IPFConstantes.CS_DOMAINE_ADRESSE, date);
    }

    /**
     * 
     * @param idTiers
     * @param typeAdresse
     * @param idApplication
     * @param date
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static AdresseTiersDetail getAdresseAssure(String idTiers, String typeAdresse, String idApplication,
            String date) throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        return TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers, true, date, idApplication,
                typeAdresse, null);
    }

    /**
     * 
     * @param personne
     * @param date
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static AdresseTiersDetail getAdressePaiementAssure(PersonneEtendueComplexModel personne, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        return PFUserHelper.getAdressePaiementAssure(personne.getPersonneEtendue().getIdTiers(), date);
    }

    /**
     * 
     * @param idTiers
     * @param date
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static AdresseTiersDetail getAdressePaiementAssure(String idTiers, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        return PFUserHelper.getAdressePaiementAssure(idTiers, IPFConstantes.CS_DOMAINE_ADRESSE, date);
    }

    /**
     * 
     * @param idTiers
     * @param idApplication
     * @param date
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static AdresseTiersDetail getAdressePaiementAssure(String idTiers, String idApplication, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail adresseTiersDetail = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                idTiers, true, idApplication, date, null);

        return adresseTiersDetail;
    }

    /**
     * 
     * @param session
     * @param personne
     * @return
     */
    public static String getDetailAssure(BSession session, PersonneEtendueComplexModel personne) {

        if (!JadeStringUtil.isEmpty(personne.getPersonne().getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(personne.getPersonneEtendue().getNumAvsActuel(), personne
                    .getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2(), personne.getPersonne()
                    .getDateNaissance(), PFUserHelper.getLibelleCourtSexe(session, personne), PFUserHelper
                    .getLibellePays(session, personne), personne.getPersonne().getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(personne.getPersonneEtendue().getNumAvsActuel(), personne
                    .getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2(), personne.getPersonne()
                    .getDateNaissance(), PFUserHelper.getLibelleCourtSexe(session, personne), PFUserHelper
                    .getLibellePays(session, personne));
        }

    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public static String getLibelleCourtSexe(BSession session, PersonneEtendueComplexModel personne) {

        if (PRACORConst.CS_HOMME.equals(personne.getPersonne().getSexe())) {
            return session.getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(personne.getPersonne().getSexe())) {
            return session.getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public static String getLibellePays(BSession session, PersonneEtendueComplexModel personne) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", personne.getTiers().getIdPays())))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", personne.getTiers().getIdPays()));
        }

    }

    public static String getLocaliteAssure(PersonneEtendueComplexModel personne, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail adresse = PFUserHelper.getAdresseAssure(personne, date);
        if (adresse.getFields() != null) {
            return adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
        } else {
            return JadeThread.getMessage("PFUSERHELPER_LOCALITE_ASSURE");
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

    /**
     * 
     * @param application
     *            PFApplication.DEFAULT_APPLICATION_PERSEUS
     * @param property
     *            PFApplication.PROPERTY_...
     * @return
     *         Type de retour : Map<String, String>, Key = userID, value = userName + userLastName
     *         C'est un MAP entre le user id et son prénom nom
     */
    public static Map<String, String> getUsermap(String application, String property) {

        String propertiesNomGroupeAgence = new String();
        Map<String, String> listeAgence = new HashMap<String, String>();

        try {
            propertiesNomGroupeAgence = ((PFApplication) GlobazSystem.getApplication(application))
                    .getProperty(property);
        } catch (Exception e) {
            JadeThread.logError(PFUserHelper.class.getName(), "perseus.utils.pfuserhelper.getUsermap.application");
            JadeLogger.error(PFUserHelper.class, e.getMessage());
        }

        // -----------------------------------------------------------------------
        // Voir comment faire pour isoler les lignes et leur messages d'erreur
        if (!JadeStringUtil.isBlank(propertiesNomGroupeAgence)) {
            String[] users;
            try {
                users = PRGestionnaireHelper.getGestionnairesId(propertiesNomGroupeAgence);

                if (users != null) {
                    for (String userId : users) {
                        JadeUser j = PRGestionnaireHelper.getGestionnaire(userId);
                        listeAgence.put(userId, j.getFirstname() + " " + j.getLastname());
                    }
                }
            } catch (Exception e) {
                JadeThread.logError(PFUserHelper.class.getName(),
                        "perseus.utils.pfuserhelper.getUsermap.gestionnairesid");
                JadeLogger.error(PFUserHelper.class, e.getMessage());
            }
        }
        return listeAgence;
    }
}
