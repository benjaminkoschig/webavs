package globaz.aquila.service.tiers;

import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TIPersonne;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.HashMap;

/**
 * <H1>Description</H1>
 * <p>
 * Cette instance conserve un cache des tiers chargés, elle n'est donc pas pratique dans un contexte statique.
 * </p>
 * 
 * @author vre
 */
public class COTiersService {

    public final static String CS_COMPLEMENT_REQUISITION = "508022";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private HashMap idTiersToTiers = new HashMap();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut adresse data source.
     * 
     * @param session
     * @param tiers
     *            DOCUMENT ME!
     * @param date
     * @return la valeur courante de l'attribut adresse data source
     * @throws Exception
     *             DOCUMENT ME!
     */
    public TIAdresseDataSource getAdresseDataSource(BSession session, IntTiers tiers, String idExterne, String date)
            throws Exception {
        if (tiers == null) {
            return getAdresseDataSourceForType(session, tiers, idExterne, date, IConstantes.CS_APPLICATION_CONTENTIEUX,
                    null, true);
        } else {
            TIAdresseDataSource tmp = getAdresseDataSourceForType(session, tiers, idExterne, date,
                    IConstantes.CS_APPLICATION_CONTENTIEUX, IConstantes.CS_AVOIR_ADRESSE_COURRIER, false);

            if (tmp == null) {
                TIAdresseDataSource tmpDomicile = getAdresseDataSourceDomicileStandard(session, tiers, idExterne, date);

                if (tmpDomicile == null) {
                    return getAdresseDataSourceForType(session, tiers, idExterne, date,
                            IConstantes.CS_APPLICATION_DEFAUT, IConstantes.CS_AVOIR_ADRESSE_COURRIER, false);
                } else {
                    return tmpDomicile;
                }
            } else {
                return tmp;
            }
        }
    }

    /**
     * Return l'adresse pour le complément de la requisition de poursuite par bande.
     * 
     * @param session
     * @param tiers
     * @param date
     * @return l'adresse pour le complément de la requisition de poursuite par bande.
     * @throws Exception
     */
    public TIAdresseDataSource getAdresseDataSourceComplementRDP(BSession session, IntTiers tiers, String idExterne,
            String date) throws Exception {
        return getAdresseDataSourceForType(session, tiers, idExterne, date, IConstantes.CS_APPLICATION_CONTENTIEUX,
                COTiersService.CS_COMPLEMENT_REQUISITION, false);
    }

    /**
     * Return l'adresse de domicile standard du tiers. Utilisé pour les indépendants ou pour déterminer si le tiers est
     * étranger.
     * 
     * @param session
     * @param tiers
     * @param idExterne
     * @param date
     * @return l'adresse de domicile standard du tiers.
     * @throws Exception
     */
    public TIAdresseDataSource getAdresseDataSourceDomicileStandard(BSession session, IntTiers tiers, String idExterne,
            String date) throws Exception {
        return getAdresseDataSourceForType(session, tiers, idExterne, date, IConstantes.CS_APPLICATION_DEFAUT,
                IConstantes.CS_AVOIR_ADRESSE_DOMICILE, false);
    }

    /**
     * Return l'adresse de l'office de poursuite passé en paramètre.
     * 
     * @param session
     * @param tiers
     * @param date
     * @return l'adresse de l'office de poursuite passé en paramètre.
     * @throws Exception
     */
    public TIAdresseDataSource getAdresseDataSourceForOfficePoursuite(BSession session, IntTiers tiers, String date)
            throws Exception {
        TIAdresseDataSource adresseDataSource = null;

        if ((tiers != null) && !JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            // retrouver le tiers et son adresse de contentieux
            TITiers pyTiers = loadTiers(session, tiers);

            adresseDataSource = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_CONTENTIEUX, date, true);
        }

        if (adresseDataSource == null) {
            adresseDataSource = new TIAdresseDataSource();
        }

        return adresseDataSource;

    }

    /**
     * Return le data source pour le type passé en paramètre.
     * 
     * @param session
     * @param tiers
     * @param date
     * @param idType
     * @param herite
     *            : Adresse cascadée ?
     * @return
     * @throws Exception
     */
    private TIAdresseDataSource getAdresseDataSourceForType(BSession session, IntTiers tiers, String idExterne,
            String date, String idDomaine, String idType, boolean herite) throws Exception {
        TIAdresseDataSource adresseDataSource = null;

        if ((tiers != null) && !JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            // retrouver le tiers et son adresse de contentieux
            TITiers pyTiers = loadTiers(session, tiers);

            adresseDataSource = pyTiers.getAdresseAsDataSource(idType, idDomaine, idExterne, date, herite, null);

            if ((adresseDataSource == null) && !herite) {
                adresseDataSource = pyTiers.getAdresseAsDataSource(idType, idDomaine, date, false);
            }
        }

        if ((adresseDataSource == null) && herite) {
            adresseDataSource = new TIAdresseDataSource();
        }

        return adresseDataSource;

    }

    /**
     * getter pour l'attribut adresse paiement string.
     * 
     * @param session
     * @param tiers
     *            DOCUMENT ME!
     * @param date
     * @return la valeur courante de l'attribut adresse paiement string
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String getAdressePaiementString(BSession session, IntTiers tiers, String date) throws Exception {
        String adresse = null;

        if ((tiers != null) && !JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            // retrouver le tiers et son adresse de contentieux
            TITiers pyTiers = loadTiers(session, tiers);

            adresse = pyTiers.getAdressePaiementAsString(IConstantes.CS_APPLICATION_CONTENTIEUX, date);
        }

        if (adresse == null) {
            adresse = "";
        }

        return adresse;
    }

    /**
     * @param tiers
     * @param idExterne
     * @return TIAvoirAdresse selon la cascade pour le contentieux : Adresse contentieux, adresse domicile, adresse
     *         courrier.
     * @throws Exception
     */
    private TIAvoirAdresse getAvoirAdresseCascadeContentieux(BSession session, IntTiers tiers, String idExterne)
            throws Exception {
        TIAvoirAdresse avoirAdresse = null;

        avoirAdresse = TITiers.getAvoirAdresse(idExterne, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                IConstantes.CS_APPLICATION_CONTENTIEUX, JACalendar.today().toStr("."), tiers.getIdTiers(), session);

        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_CONTENTIEUX, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(idExterne, IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(idExterne, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        return avoirAdresse;
    }

    /**
     * @param session
     * @param idCanton
     * @return le canton dans la langue passée en paramètre
     * @throws Exception
     */
    public String getCanton(BSession session, String idCanton, String codeISOLangue) throws Exception {
        if (JadeStringUtil.isBlank(idCanton)) {
            return "";
        }

        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(session);
        userCode.setIdCodeSysteme(idCanton);
        if ("FR".equals(codeISOLangue)) {
            userCode.setIdLangue("F");
        } else if ("DE".equals(codeISOLangue)) {
            userCode.setIdLangue("D");
        } else if ("IT".equals(codeISOLangue)) {
            userCode.setIdLangue("I");
        } else {
            userCode.setIdLangue("F");
        }
        try {
            userCode.retrieve();
            if (!userCode.isNew()) {
                return userCode.getLibelle();
            } else {
                return "";
            }
        } catch (Exception E) {
            return "";
        }
    }

    /**
     * @param session
     * @param tiers
     * @return la date de naissance du tier
     * @throws Exception
     */
    public String getDateNaissance(BSession session, IntTiers tiers) throws Exception {
        String dateNaissance = "";

        if ((tiers != null) && !JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            // retrouver le tier
            TIPersonne pyTiers = loadPersonne(session, tiers);
            if (isPersonnePhysique(session, tiers)) {
                dateNaissance = pyTiers.getDateNaissance();
            }
        }
        return dateNaissance;
    }

    /**
     * Retourne la formule de politesse pour ce tiers.
     * 
     * @param session
     * @param tiers
     *            un tiers osiris
     * @param langueISO
     *            DE ; FR ; IT ...
     * @return une formule de politesse
     * @throws Exception
     * @see globaz.pyxis.db.tiers.TITiers#getFormulePolitesse(String)
     */
    public String getFormulePolitesse(BSession session, IntTiers tiers, String langueISO) throws Exception {
        if ((tiers == null) || JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            return "";
        }

        TITiers pyTiers = loadTiers(session, tiers);

        return pyTiers.getFormulePolitesse(TITiers.langueISOtoCodeSystem(langueISO));
    }

    /**
     * Retourne l'office des poursuites pour le tiers donné en se basant sur les liens.
     * 
     * @param tiers
     *            le tiers dont on veut trouver l'OP.
     * @return l'OP lié directement au tiers s'il existe ou l'OP lié à la localité de l'adresse du tiers sinon.
     * @throws Exception
     */
    public IntTiers getOfficePoursuite(BSession session, IntTiers tiers, String idExterne) throws Exception {
        TIAvoirAdresse avoirAdresse = getAvoirAdresseCascadeContentieux(session, tiers, idExterne);
        return tiers.getOfficePoursuitesSelonLien(avoirAdresse.getIdLocalite());
    }

    /**
     * Retourne le tribunal pour le tiers donné.
     * 
     * @param tiers
     *            le tiers dont on veut trouver le tribunal.
     * @return le tribunal lié directement au tiers s'il existe ou le tribunal lié à la localité de l'adresse du tiers
     *         sinon.
     * @throws Exception
     */
    public IntTiers getTribunal(BSession session, IntTiers tiers, String idExterne) throws Exception {
        IntTiers t = null;
        if (getAvoirAdresseCascadeContentieux(session, tiers, idExterne) != null) {
            t = tiers
                    .getTribunalSelonLien(getAvoirAdresseCascadeContentieux(session, tiers, idExterne).getIdLocalite());
        }
        if (t == null) {
            return tiers.getTribunalCanton();
        } else {
            return t;
        }
    }

    /**
     * @param session
     * @param tiers
     * @return true si peronne physique
     * @throws Exception
     */
    public boolean isPersonnePhysique(BSession session, IntTiers tiers) throws Exception {
        boolean isPersonnePhysique = false;

        if ((tiers != null) && !JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            // retrouver le tier
            TITiers pyTiers = loadTiers(session, tiers);

            isPersonnePhysique = pyTiers.getPersonnePhysique().booleanValue();
        }
        return isPersonnePhysique;
    }

    /**
     * charge une personne et le met en cache.
     * 
     * @param session
     * @param tiers
     * @return une personne
     * @throws Exception
     */
    public TIPersonne loadPersonne(BSession session, IntTiers tiers) throws Exception {
        TIPersonne pyTiers = (TIPersonne) idTiersToTiers.get(tiers.getIdTiers());

        if (pyTiers == null) {
            pyTiers = new TITiersViewBean();
            pyTiers.setIdTiers(tiers.getIdTiers());
            pyTiers.setSession(session);
            pyTiers.retrieve();

            idTiersToTiers.put(tiers.getIdTiers(), pyTiers);
        }

        return pyTiers;
    }

    /**
     * charge un tiers et le met en cache.
     * 
     * @param session
     * @param tiers
     * @return un tiers
     * @throws Exception
     */
    public TITiers loadTiers(BSession session, IntTiers tiers) throws Exception {
        TITiers pyTiers = (TITiers) idTiersToTiers.get(tiers.getIdTiers());

        if (pyTiers == null) {
            pyTiers = new TITiersViewBean();
            pyTiers.setIdTiers(tiers.getIdTiers());
            pyTiers.setSession(session);
            pyTiers.retrieve();

            idTiersToTiers.put(tiers.getIdTiers(), pyTiers);
        }

        return pyTiers;
    }

}
