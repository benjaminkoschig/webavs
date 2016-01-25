/*
 * Créé le 10 mai 05
 * 
 * Description :
 */
package globaz.hera.interfaces.tiers;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.hera.interfaces.af.ISFAffilie;
import globaz.hera.interfaces.af.SFAffiliationHelper;
import globaz.hera.tools.SFAVSUtils;
import globaz.hera.tools.SFSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.api.ITIAbstractAdresseData;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.api.ITIApplication;
import globaz.pyxis.api.ITIAvoirAdresse;
import globaz.pyxis.api.ITILocalite;
import globaz.pyxis.api.ITIPays;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.api.ITIPersonneAvs;
import globaz.pyxis.api.ITIPersonneAvsAdresse;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

/**
 * DOCUMENT ME!
 * 
 * @author scr Descpription
 */
public class SFTiersHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CS_ADRESSE_COURRIER = "508001";

    public static final String CS_ADRESSE_DOMICILE = "508008";
    /** Code système de Tiers pour la modification */
    public static final String CS_TIERS_MODIFICATION_INCONNUE = "506007";

    /** Cet idPays spécifie que le tiers est un étranger sans spécifier le pays */
    public static final String ID_PAYS_BIDON = "999";

    private static final HashMap LISTE_PAYS = new HashMap();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private static void addRole(BISession session, BITransaction transaction, String idTiers) throws Exception {
        ITIRole role = (ITIRole) session.getAPIFor(ITIRole.class);
        role.setIdTiers(idTiers);
        role.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        if ("IJ".equals(session.getApplicationId())) {
            role.setRole(IntRole.ROLE_IJAI);
        } else if ("APG".equals(session.getApplicationId())) {
            role.setRole(IntRole.ROLE_APG);
        } else if ("CORVUS".equals(session.getApplicationId())) {
            role.setRole(IntRole.ROLE_RENTIER);
        } else {
            role.setRole(IntRole.ROLE_ASSURE);
        }

        role.add(transaction);

    }

    /**
     * Crée une nouvelle instance de la classe PRTiersHelper.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param tiers
     *            DOCUMENT ME!
     * @param truc
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String addTiers(BISession session, BITransaction transaction, ISFTiers tiers, int truc)
            throws Exception {
        ITIPersonneAvs personneAvs = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);

        personneAvs.setTypeTiers(ITITiers.CS_TIERS);
        personneAvs.setNumAvsActuel(tiers.getNoAVS());
        if (!JadeStringUtil.isBlankOrZero(tiers.getNoAVS())) {
            personneAvs.setPersonnePhysique(Boolean.TRUE);
        }
        personneAvs.setDesignation1(tiers.getNom());
        personneAvs.setDesignation2(tiers.getPrenom());
        personneAvs.setLangue(ITITiers.CS_FRANCAIS);

        if (!JadeStringUtil.isEmpty(tiers.getSexe())) {
            personneAvs.setSexe(tiers.getSexe());

            if (ITIPersonne.CS_FEMME.equals(tiers.getSexe())) {
                personneAvs.setTitreTiers(ITITiers.CS_MADAME);
            } else {
                personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
            }
        } else {
            personneAvs.setSexe(ITIPersonne.CS_HOMME);
            personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
        }

        personneAvs.setDateNaissance(tiers.getDateNaissance());
        personneAvs.setDateDeces(tiers.getDateDeces());
        personneAvs.setIdPays(tiers.getIdPays());

        personneAvs.setLangue(tiers.getLangue());

        personneAvs.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));
        personneAvs.add(transaction);

        SFTiersHelper.addRole(session, transaction, personneAvs.getIdTiers());

        return personneAvs.getIdTiers();
    }

    public static final String addTiers(BISession session, BITransaction transaction, String noAVS, String nom,
            String prenom, String sexe, String dateNaissance, String dateDeces, String pays, String canton,
            String langue, String etatCivil) throws Exception {
        ITIPersonneAvs personneAvs = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);

        personneAvs.setTypeTiers(ITITiers.CS_TIERS);
        personneAvs.setNumAvsActuel(noAVS);
        if (!JadeStringUtil.isBlankOrZero(noAVS)) {
            personneAvs.setPersonnePhysique(Boolean.TRUE);
        }
        personneAvs.setDesignation1(nom);
        personneAvs.setDesignation2(prenom);
        personneAvs.setLangue(ITITiers.CS_FRANCAIS);
        if (!JadeStringUtil.isEmpty(sexe)) {
            personneAvs.setSexe(sexe);

            if (ITIPersonne.CS_FEMME.equals(sexe)) {
                personneAvs.setTitreTiers(ITITiers.CS_MADAME);
            } else {
                personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
            }
        } else {
            personneAvs.setSexe(ITIPersonne.CS_HOMME);
            personneAvs.setTitreTiers(ITITiers.CS_MONSIEUR);
        }

        personneAvs.setDateNaissance(dateNaissance);
        personneAvs.setDateDeces(dateDeces);
        personneAvs.setIdPays(pays);

        // personneAvs.setIdCanton(canton);
        personneAvs.setLangue(langue);

        personneAvs.setEtatCivil(etatCivil);

        personneAvs.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        if (transaction != null) {
            personneAvs.add(transaction);
        } else if (((BSession) session).getCurrentThreadTransaction() != null) {
            personneAvs.add(((BSession) session).getCurrentThreadTransaction());
        } else {
            // HACK: création d'une transaction pour insérer un tiers
            BITransaction newTransaction = ((BSession) session).newTransaction();

            try {
                personneAvs.add(newTransaction);
                SFTiersHelper.addRole(session, transaction, personneAvs.getIdTiers());

            } catch (Exception e) {
                newTransaction.setRollbackOnly();
            } finally {
                if (newTransaction.isRollbackOnly()) {
                    newTransaction.closeTransaction();
                } else {
                    newTransaction.commit();
                }
            }
        }

        return personneAvs.getIdTiers();
    }

    public static final String addTiers(BISession session, String noAVS, String nom, String prenom, String sexe,
            String dateNaissance, String dateDeces, String pays, String canton, String langue, String etatCivil)
            throws Exception {
        return SFTiersHelper.addTiers(session, null, noAVS, nom, prenom, sexe, dateNaissance, dateDeces, pays, canton,
                langue, etatCivil);
    }

    /**
     * retourne l'adresse de courrier courante et formatee d'un tiers si elle existe.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut adresse courrier formatee ou chaine vide si le tiers n'a pas d'adresse
     *         de courrier.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */

    // public static final String getAdresseCourrierFormatee(BISession session,
    // String idTiers) throws Exception {
    // TIAdresseDataManager adresses = new TIAdresseDataManager();
    //
    // adresses.setForIdApplication(CS_ADRESSE_COURRIER);
    // adresses.setForIdTiers(idTiers);
    // adresses.setISession(PRSession.connectSession(session,
    // TIApplication.DEFAULT_APPLICATION_PYXIS));
    // adresses.find();
    //
    // if (adresses.isEmpty()) {
    // return "";
    // }
    //
    // TIAdresseDataSource source = new TIAdresseDataSource();
    //
    // source.load((TIAdresseData) adresses.get(0),
    // JACalendar.todayJJsMMsAAAA());
    //
    // return new TIAdresseFormater().format(source);
    // }

    /**
     * getter pour les administrations d genre
     * 
     * @param session
     *            DOCUMENT ME!
     * @param genre
     *            DOCUMENT ME!
     * 
     * @return
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final SFTiersWrapper[] getAdministrationForGenre(BISession session, String genre) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(genre)) {
            return null;
        }

        ITIAdministration administaration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable criteres = new Hashtable();
        criteres.put(ITIAdministration.FIND_FOR_GENRE_ADMINISTRATION, genre);

        administaration.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] obj = administaration.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            SFTiersWrapper[] result = new SFTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new SFTiersWrapper((GlobazValueObject) obj[i], SFTiersWrapper.TI_PERSONNE_AVS);
            }

            return result;
        }
    }

    /**
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut tiers par id
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final SFTiersWrapper getAdministrationParId(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIAdministration administaration = (ITIAdministration) session.getAPIFor(ITIAdministration.class);

        Hashtable criteres = new Hashtable();
        criteres.put(ITITiers.FIND_FOR_IDTIERS, idTiers);

        administaration.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = administaration.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new SFTiersWrapper((GlobazValueObject) result[0], SFTiersWrapper.TI_PERSONNE_AVS);
        }
    }

    /**
     * retourne une adresse de courrier valide pour un tiers.
     * 
     * <p>
     * Cette methode utilise le systeme de cascade mis en place par pyxis, elle recherche une adresse d'abord pour le
     * domaine apg, puis pour le domaine standard, etc.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * @param idApplication
     *            DOCUMENT ME!
     * 
     * @return une adresse ou chaine vide s'il n'y a pas d'adresse pour ce tiers.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String getAdresseCourrierFormatee(BISession session, String idTiers, String idAffilie,
            String idApplication) throws Exception {
        ITITiers helper = (ITITiers) session.getAPIFor(ITITiers.class);
        Hashtable params = new Hashtable();

        params.put(ITITiers.FIND_FOR_IDTIERS, idTiers);
        helper.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        ITITiers[] result = helper.findTiers(params);

        if ((result == null) || (result.length == 0)) {
            return "";
        } else {
            ITITiers tiers = result[0];

            tiers.setISession(helper.getISession());

            ISFAffilie aff = SFAffiliationHelper.getEmployeurParIdAffilie(session, null, idAffilie, idTiers);

            String nAff = "";

            if (aff != null) {
                nAff = aff.getNumAffilie();
            } else {
                nAff = "";
            }

            return tiers.getAdresseAsString(TIAvoirAdresse.CS_COURRIER, idApplication, JACalendar.todayJJsMMsAAAA(),
                    nAff);
        }
    }

    /**
     * recherche le canton dans lequel se trouve la ville dont le no postal est transmis en argument.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param npa
     *            DOCUMENT ME!
     * 
     * @return le code système du canton on null si non trouvé
     * 
     * @throws Exception
     *             si erreur dans pyxis.
     */
    public static final String getCanton(BISession session, String npa) throws Exception {
        if (JadeStringUtil.isEmpty(npa)) {
            return null;
        }

        ITILocalite localite = (ITILocalite) session.getAPIFor(ITILocalite.class);
        Hashtable criteres = new Hashtable();

        criteres.put(ITILocalite.FIND_FOR_NPA_LIKE, JadeStringUtil.leftJustify(npa, 6, '0'));

        Object[] result = localite.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            // sinon on retourne le premier qui ressemble.
            // les suivants sont généralement des sous-localites ou des numéros
            // synonymes.
            return (String) ((GlobazValueObject) result[0]).getProperty("idCanton");
        }
    }

    /**
     * recherche le (code OFAS du) canton dans lequel se trouve la ville dont le no postal est transmis en argument.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param npa
     *            DOCUMENT ME!
     * 
     * @return le code système du canton on null si non trouvé
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String getCodeOFASCanton(BISession session, String npa) throws Exception {
        if (JadeStringUtil.isEmpty(npa)) {
            return null;
        }

        ITILocalite localite = (ITILocalite) session.getAPIFor(ITILocalite.class);
        Hashtable criteres = new Hashtable();

        criteres.put(ITILocalite.FIND_FOR_NPA_LIKE, JadeStringUtil.leftJustify(npa, 6, '0'));

        Object[] result = localite.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            // sinon on retourne le premier qui ressemble.
            // les suivants sont généralement des sous-localites ou des numéros
            // synonymes.
            return (String) ((GlobazValueObject) result[0]).getProperty("codeOFASCanton");
        }
    }

    /**
     * retourne un vecteur de tableaux String[2]{codePays, NomPays}. La langue utilisée est celle transmise dans la
     * session.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut pays
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final Vector getPays(BISession session) throws Exception {
        String idLangue = session.getIdLangue();
        Vector pays = (Vector) SFTiersHelper.LISTE_PAYS.get(idLangue);

        if (pays == null) {
            ITIPays tiPays = (ITIPays) session.getAPIFor(ITIPays.class);

            pays = new Vector();
            tiPays.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

            Object[] result = tiPays.find(new Hashtable());

            for (int idPays = 0; idPays < result.length; ++idPays) {
                pays.add(new String[] { (String) ((GlobazValueObject) result[idPays]).getProperty("idPays"),
                        (String) ((GlobazValueObject) result[idPays]).getProperty("libelle") });
            }

            SFTiersHelper.LISTE_PAYS.put(idLangue, pays);
        }

        return pays;
    }

    /**
     * cherche une "personneAVS" par son id
     * 
     * @param session
     * @param idTiers
     *            l'id du tiers à chercher
     * 
     * @return un VO de la personneAVS
     * 
     * @throws Exception
     */
    public static final SFTiersWrapper getPersonneAVS(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvs personneAVS = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);
        Hashtable criteres = new Hashtable();

        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);
        personneAVS.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new SFTiersWrapper((GlobazValueObject) result[0], SFTiersWrapper.TI_PERSONNE_AVS);
        }
    }

    /**
     * Recherche un "tiers" (sans adresse) à partir de son numéro AVS.
     * 
     * @param session
     * @param noAvs
     *            Le noAVS du tiers à rechercher
     * 
     * @return Le tiers, si trouvé ou null si non trouvé.
     * 
     * @throws Exception
     *             si probleme avec TI
     */
    public static final SFTiersWrapper getTiers(BISession session, String noAvs) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(noAvs)) {
            return null;
        }

        ITIPersonneAvs personneAVS = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);
        Hashtable criteres = new Hashtable();
        criteres.put(ITIPersonneAvs.FIND_FOR_NUM_AVS_ACTUEL, noAvs);
        personneAVS.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new SFTiersWrapper((GlobazValueObject) result[0], SFTiersWrapper.TI_PERSONNE_AVS);
        }
    }

    /**
     * Recherche un "tiers" à partir de son numéro AVS.
     * 
     * @param session
     * @param noAvs
     *            Le noAVS du tiers à rechercher
     * 
     * @return Le tiers, si trouvé ou null si non trouvé.
     * 
     * @throws Exception
     *             si noAVS non unique
     * 
     * @deprecated ne plus utiliser, peut retourner plusieurs tiers identiques avec juste adresses différentes...
     */
    @Deprecated
    public static final SFTiersWrapper getTiersAdresse(BISession session, String noAvs) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(noAvs)) {
            return null;
        }

        ITIPersonneAvsAdresse personneAVS = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Hashtable criteres = new Hashtable();
        criteres.put(ITIPersonneAvsAdresse.FIND_FOR_NUM_AVS_ACTUEL_LIKE, noAvs);
        personneAVS.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0) || (result.length > 1)) {
            return null;
        } else if (result.length != 1) {
            throw new Exception("No AVS : " + noAvs + "n'est pas unique");
        } else {
            return new SFTiersWrapper((GlobazValueObject) result[0], SFTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
        }
    }

    /**
     * Recherche un "tiers" et son adresse de domicile à partir de son identifiant.
     * 
     * @param session
     *            la session
     * @param idTiers
     *            l'identifiant du tiers à rechercher
     * 
     * @return Le tiers, si trouvé ou null si non trouvé.
     * 
     * @throws Exception
     *             si la recherche échoue.
     */
    public static final SFTiersWrapper getTiersAdresseDomicileParId(BISession session, String idTiers) throws Exception {
        return SFTiersHelper.getTiersAdresseDomicileParId(session, idTiers, null);
    }

    public static final SFTiersWrapper getTiersAdresseDomicileParId(BISession session, String idTiers, String date)
            throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvsAdresse personneAVS = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Hashtable<String, Object> criteres = new Hashtable<String, Object>();

        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);

        // BZ 5124 SPECIFIQUE FER CIAM, ON NE DOIT PAS TENIR COMPTE DE L'ADRESSE DE DOMICILE, MAIS DE COURRIER DANS LE
        // DOMAINE RENTES
        String prop = null;

        try {
            BSession sessionCorvus = new BSession("CORVUS");
            prop = sessionCorvus.getApplication().getProperty("isWantAdresseCourrier");
        } catch (Exception e) {
            prop = null;
        }

        if ((prop != null) && prop.equals("true")) {
            criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, PRTiersHelper.CS_ADRESSE_COURRIER);
            criteres.put(ITIAbstractAdresseData.FIND_FOR_IDAPPLICATION,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        } else {
            criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, PRTiersHelper.CS_ADRESSE_DOMICILE);
        }
        if (!JadeStringUtil.isEmpty(date) && JadeDateUtil.isGlobazDate(date)) {
            criteres.put(ITIAbstractAdresseData.FIND_FOR_DATE_ENTRE_DEBUT_ET_FIN, date);
        }

        personneAVS.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {

            criteres = new Hashtable<String, Object>();
            criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);

            criteres.put(ITIPersonneAvsAdresse.FIND_FOR_SINGLE_ADRESSE_MODE, new Boolean(true));
            // seul. l'adr de
            // domicile
            // courante de chaque tiers
            result = personneAVS.find(criteres);
        }

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new SFTiersWrapper((GlobazValueObject) result[0], SFTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
        }
    }

    /**
     * getter pour l'attribut tiers adresse like no AVS
     * 
     * @param session
     *            DOCUMENT ME!
     * @param likeNoAVS
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut tiers adresse like no AVS
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final SFTiersWrapper[] getTiersAdresseLikeNoAVS(BISession session, String likeNoAVS) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(likeNoAVS)) {
            return null;
        }

        Hashtable cr = new Hashtable();
        cr.put(ITIPersonneAvsAdresse.FIND_FOR_NUM_AVS_ACTUEL_LIKE, likeNoAVS);
        cr.put(ITIPersonneAvsAdresse.FIND_FOR_SINGLE_ADRESSE_MODE, new Boolean(true)); // seul.
        // l'adr
        // de
        // domicile
        // courante de chaque tiers
        cr.put("changeManagerSize", "600"); // retourne max. de 600 occurance

        ITIPersonneAvsAdresse mgr = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Object[] obj = mgr.find(cr);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            SFTiersWrapper[] result = new SFTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new SFTiersWrapper((GlobazValueObject) obj[i], SFTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
            }

            return result;
        }
    }

    /**
     * getter pour l'attribut tiers adresse like no AVS
     * 
     * @param session
     *            DOCUMENT ME!
     * @param likeNoAVS
     *            DOCUMENT ME!
     * @param date
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut tiers adresse like no AVS
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final SFTiersWrapper[] getTiersAdresseLikeNoAVS(BISession session, String likeNoAVS, String date)
            throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(likeNoAVS)) {
            return null;
        }

        Hashtable criteres = new Hashtable();
        ITIPersonneAvsAdresse tiers = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);

        criteres.put(ITIPersonneAvsAdresse.FIND_FOR_NUM_AVS_LIKE, likeNoAVS);
        criteres.put(ITIAbstractAdresseData.FIND_FOR_TYPE_ADRESSE, ITIAvoirAdresse.CS_COURRIER);
        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDAPPLICATION, ITIApplication.CS_DEFAUT);
        criteres.put(ITIAbstractAdresseData.FIND_FOR_DATE_ENTRE_DEBUT_ET_FIN, date);

        Object[] obj = tiers.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            SFTiersWrapper[] result = new SFTiersWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new SFTiersWrapper((GlobazValueObject) obj[i], SFTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
            }

            return result;
        }
    }

    /**
     * Recherche un "tiers" à partir de son identifiant.
     * 
     * @param session
     *            la session
     * @param idTiers
     *            l'identifiant du tiers à rechercher
     * 
     * @return Le tiers, si trouvé ou null si non trouvé.
     * 
     * @throws Exception
     *             si la recherche échoue.
     */
    public static final SFTiersWrapper getTiersAdresseParId(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        SFTiersWrapper retValue = null;
        GlobazValueObject vo = SFTiersHelper.getTiersAdresseVOParId(session, idTiers);

        if (vo != null) {
            retValue = new SFTiersWrapper(vo, SFTiersWrapper.TI_PERSONNE_AVS_ADRESSE);
        }

        return retValue;
    }

    /**
     * cherche une "personneAVS" par son identifiant
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut tiers adresse VOPar id
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final GlobazValueObject getTiersAdresseVOParId(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvsAdresse personneAVS = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Hashtable criteres = new Hashtable();

        criteres.put(ITIAbstractAdresseData.FIND_FOR_IDTIERS, idTiers);
        personneAVS.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return (GlobazValueObject) result[0];
        }
    }

    /**
     * getter pour l'attribut tiers par id
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut tiers par id
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final SFTiersWrapper getTiersParId(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        ITIPersonneAvs personneAVS = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);
        Hashtable criteres = new Hashtable();

        criteres.put(ITITiers.FIND_FOR_IDTIERS, idTiers);
        personneAVS.setISession(SFSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        Object[] result = personneAVS.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            return new SFTiersWrapper((GlobazValueObject) result[0], SFTiersWrapper.TI_PERSONNE_AVS);
        }
    }

    /**
     * retourne vrai si l'assuré portant l'identifiant transmis est en age AVS à une date donnée.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            l'identifiant de l'assuré dont on veut savoir s'il est rentier AVS
     * @param date
     *            la date à laquelle on veut savoir si l'assuré était, est ou sera rentier AVS.
     * 
     * @return la valeur courante de l'attribut rentier
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isRentier(BISession session, String idTiers, String date) throws Exception {
        SFTiersWrapper tiers = SFTiersHelper.getTiersParId(session, idTiers);
        String dateNaissance = tiers.getProperty(SFTiersWrapper.PROPERTY_DATE_NAISSANCE);

        if (JAUtil.isDateEmpty(dateNaissance)) {
            return false; // on part du principe que si la date n'est pas
            // renseignée, ce n'est pas un retraité.
        }

        int anneeNaissance = JACalendar.getYear(dateNaissance);
        int ageAvs = 0;
        String csSexe = tiers.getProperty(SFTiersWrapper.PROPERTY_SEXE);

        if (JadeStringUtil.isEmpty(csSexe)) {
            // si le sexe n'est pas renseigné, on tente de le deviner au moyen
            // du no AVS.
            String noAVS = tiers.getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

            csSexe = SFAVSUtils.getInstance(noAVS).getSexe(noAVS);
        }

        if (tiers.getProperty(SFTiersWrapper.PROPERTY_SEXE).equalsIgnoreCase(ITIPersonne.CS_FEMME)) {
            // si c'est une femme
            if (anneeNaissance <= 1938) {
                ageAvs = anneeNaissance + 62;
            } else {
                if (anneeNaissance <= 1941) {
                    ageAvs = anneeNaissance + 63;
                } else {
                    ageAvs = anneeNaissance + 64;
                }
            }
        } else {
            // sinon, ou par défaut, c'est un homme...
            ageAvs = anneeNaissance + 65;
        }

        // on construit la date d'age avs
        JADate dateAVS = new JADate(JACalendar.getDay(dateNaissance), JACalendar.getMonth(dateNaissance), ageAvs);

        return ((BSession) session).getApplication().getCalendar().compare(new JADate(date), dateAVS) != JACalendar.COMPARE_FIRSTLOWER;
    }

    // /**
    // * @param session
    // * @param idTiers DOCUMENT ME!
    // * @param noAVS
    // * @param nom
    // * @param prenom
    // * @param sexe
    // * @param dateNaissance
    // * @param pays
    // * @param canton
    // * @param langue
    // * @param etatCivil
    // *
    // * @return
    // *
    // * @throws Exception
    // */
    // public static final String updateTiers(BISession session,
    // String idTiers,
    // String noAVS,
    // String nom,
    // String prenom,
    // String sexe,
    // String dateNaissance,
    // String pays,
    // String canton,
    // String langue,
    // String etatCivil) throws Exception {
    // // HACK: création d'une transaction pour accéder aux tiers
    // BITransaction transaction = ((BSession) session).newTransaction();
    //
    // ITIPersonneAvs personneAvs = (ITIPersonneAvs)
    // session.getAPIFor(ITIPersonneAvs.class);
    // personneAvs.setIdTiers(idTiers);
    // personneAvs.retrieve(transaction);
    // //
    //
    // if (!personneAvs.getDesignation1().equals(nom)) {
    // personneAvs.setDateModifDesignation1(JACalendar.todayJJsMMsAAAA());
    // personneAvs.setMotifModifDesignation1(CS_TIERS_MODIFICATION_INCONNUE);
    // }
    //
    // if (!personneAvs.getDesignation2().equals(prenom)) {
    // personneAvs.setDateModifDesignation2(JACalendar.todayJJsMMsAAAA());
    // personneAvs.setMotifModifDesignation2(CS_TIERS_MODIFICATION_INCONNUE);
    // }
    //
    // personneAvs.setTypeTiers(ITIPersonneAvs.CS_TIERS);
    // noAVS = JAUtil.replaceString(noAVS, ".", "");
    //
    // if (!noAVS.equals(personneAvs.getNumAvsActuel())) {
    // personneAvs.setNumAvsActuel(noAVS);
    // personneAvs.setDateModifAvs(JACalendar.todayJJsMMsAAAA());
    // personneAvs.setMotifModifAvs(CS_TIERS_MODIFICATION_INCONNUE);
    // }
    //
    // //
    // personneAvs.setDesignation1(nom);
    // personneAvs.setDesignation2(prenom);
    //
    // if (!JAUtil.isStringEmpty(sexe)) {
    // personneAvs.setSexe(sexe);
    // personneAvs.setDateModifTitre(JACalendar.todayJJsMMsAAAA());
    // personneAvs.setMotifModifTitre(CS_TIERS_MODIFICATION_INCONNUE);
    //
    // if (ITIPersonneAvs.CS_FEMME.equals(sexe)) {
    // personneAvs.setTitreTiers(ITIPersonneAvs.CS_MADAME);
    // } else {
    // personneAvs.setTitreTiers(ITIPersonneAvs.CS_MONSIEUR);
    // }
    // } else {
    // personneAvs.setSexe(ITIPersonneAvs.CS_HOMME);
    // personneAvs.setTitreTiers(ITIPersonneAvs.CS_MONSIEUR);
    // }
    //
    // personneAvs.setDateNaissance(dateNaissance);
    //
    // personneAvs.setIdPays(pays);
    // personneAvs.setDateModifPays(JACalendar.todayJJsMMsAAAA());
    // personneAvs.setMotifModifPays(CS_TIERS_MODIFICATION_INCONNUE);
    //
    // personneAvs.setLangue(langue);
    //
    // personneAvs.setEtatCivil(etatCivil);
    //
    // personneAvs.setISession(PRSession.connectSession(session,
    // TIApplication.DEFAULT_APPLICATION_PYXIS));
    //
    // if (((BSession) session).getCurrentThreadTransaction() != null) {
    // personneAvs.update(((BSession) session).getCurrentThreadTransaction());
    // } else {
    // try {
    // personneAvs.update(transaction);
    // } catch (Exception e) {
    // transaction.setRollbackOnly();
    // } finally {
    // if (transaction.isRollbackOnly()) {
    // transaction.closeTransaction();
    // } else {
    // transaction.commit();
    // }
    // }
    // }
    //
    // // CANTON
    // // ITIPersonneAvsAdresse personneAdresse = (ITIPersonneAvsAdresse)
    // // session.getAPIFor(ITIPersonneAvsAdresse.class);
    // // personneAdresse.setIdTiers(idTiers);
    // // personneAdresse.retrieve(transaction);
    // //
    // // if (!canton.equals(personneAdresse.getIdCanton())) {
    // // personneAdresse.setIdCanton(canton);
    // // personneAdresse.u
    // // }
    //
    // return personneAvs.getIdTiers();
    // }
}
