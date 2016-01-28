/*
 * Créé le 8 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.interfaces.af;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JADate;
import globaz.hera.application.SFApplication;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.tools.SFSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFAssurance;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class SFAffiliationHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * <H1>Description</H1>
     * 
     * @author vre
     */
    public static class GenreAssurance {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String id = null;
        private String propertyBase;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private GenreAssurance(String propertyBase) {
            this.propertyBase = propertyBase;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if ((obj != null) && (obj instanceof GenreAssurance)) {
                return propertyBase.equals(((GenreAssurance) obj).propertyBase);
            }

            return false;
        }

        /**
         * getter pour l'attribut id assurance
         * 
         * @param forType
         *            DOCUMENT ME!
         * 
         * @return la valeur courante de l'attribut id assurance ou null si forType est null
         */
        public String getIdAssurance(String applicationName, TypeAssurance forType) {
            try {
                if (forType != null) {
                    id = GlobazSystem.getApplication(applicationName).getProperty(
                            propertyBase + forType.getPropertyEnd());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return id;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return propertyBase.hashCode();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "genre assurance: " + propertyBase;
        }
    }

    /**
     * <H1>Description</H1>
     * 
     * @author vre
     */
    public static class TypeAssurance {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String propertyEnd;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private TypeAssurance(String propertyEnd) {
            this.propertyEnd = propertyEnd;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if ((obj != null) && (obj instanceof TypeAssurance)) {
                return propertyEnd == ((TypeAssurance) obj).propertyEnd;
            }

            return false;
        }

        private String getPropertyEnd() {
            return "." + propertyEnd + ".id";
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return propertyEnd.hashCode();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "type assurance: " + propertyEnd;
        }
    }

    /**
     */
    public static final GenreAssurance GENRE_AC = new GenreAssurance("assurance.ac");

    /**
     */
    public static final GenreAssurance GENRE_AVS_AI = new GenreAssurance("assurance.avsai");

    /**
     */
    public static final GenreAssurance GENRE_FRAIS_ADM = new GenreAssurance("assurance.fad");

    /**
     */
    public static final GenreAssurance GENRE_LFA = new GenreAssurance("assurance.lfa");

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     */
    public static final TypeAssurance TYPE_PARITAIRE = new TypeAssurance("paritaire");

    /**
     */
    public static final TypeAssurance TYPE_PERSONNEL = new TypeAssurance("personnelle");

    /**
     * recherche toutes les affiliations pour un tiers et les retourne sous la forme d'une liste de String[2] {
     * idAffilie, numAffilie}.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return une liste de String[2] {idAffilie, numAffilie} ou null si pas d'affiliation ou idTiers null.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final Vector /* String[] */getAffiliationsTiers(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
        employeur.setISession(SFSession.connectSession(session, SFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            // on a trouve des affilitations que l'on retourne
            Vector retValue = new Vector();

            for (int idAffiliation = 0; idAffiliation < result.length; ++idAffiliation) {
                GlobazValueObject vo = (GlobazValueObject) result[idAffiliation];

                retValue.add(new String[] { (String) vo.getProperty(ISFAffilieImpl.PROPERTY_ID_AFFILIE),
                        (String) vo.getProperty(ISFAffilieImpl.PROPERTY_AFFILIE_NUMERO) });
            }

            return retValue;
        }
    }

    /**
     * getter pour l'attribut employeur par num affilie
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param idAffilie
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut employeur par num affilie
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final ISFAffilie getEmployeurParIdAffilie(BISession session, BITransaction transaction,
            String idAffilie, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isIntegerEmpty(idAffilie)) {
            return null;
        }

        ISFAffilie retValue = null;
        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);

        employeur.setISession(SFSession.connectSession(session, SFApplication.DEFAULT_APPLICATION_NAOS));
        employeur.setAffiliationId(idAffilie);
        employeur.setIdTiers(idTiers);
        employeur.retrieve(transaction);

        if (!employeur.isNew()) {
            GlobazValueObject tiers = SFTiersHelper.getTiersAdresseVOParId(session, employeur.getIdTiers());

            if (tiers != null) {
                // le tiers correspondant existe
                retValue = new ISFAffilieImpl(tiers, idAffilie, employeur.getAffilieNumero(),
                        employeur.getBrancheEconomique(), employeur.getTypeAffiliation());
            }
        }

        return retValue;
    }

    /**
     * recherche un employeur par son numéro d'affilié
     * 
     * @param session
     *            DOCUMENT ME!
     * @param numAffilie
     *            le numéro d'affilié de l'employeur à rechercher
     * 
     * @return l'employeur (un tiers) si il est trouvé, sinon null
     * 
     * @throws Exception
     *             si numéro d'affilié non unique
     */
    public static final ISFAffilie getEmployeurParNumAffilie(BISession session, String numAffilie) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(numAffilie)) {
            return null;
        }

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, numAffilie);
        employeur.setISession(SFSession.connectSession(session, SFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else if (result.length != 1) {
            throw new Exception("numéro d'affilié : " + numAffilie + " n'est pas unique");
        } else {
            // on a trouvé un enregistrement avec ce numéro d'affilie, il reste
            // à charger le tiers correspondant
            GlobazValueObject affilie = (GlobazValueObject) result[0];
            Object value = affilie.getProperty(ISFAffilieImpl.PROPERTY_TIERS_AF);

            if (value != null) {
                GlobazValueObject tiers = SFTiersHelper.getTiersAdresseVOParId(session, value.toString());

                if (tiers != null) {
                    // le tiers correspondant existe
                    return new ISFAffilieImpl(tiers, (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_ID_AFFILIE),
                            numAffilie, (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                            (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                            (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_DEBUT),
                            (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_FIN));
                }
            }

            // le tiers correspondant n'existe pas
            return null;
        }
    }

    /**
     * recherche un employeur par son numéro d'affilié PAR RAPPORT A UNE DATE DONNEE !!!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param numAffilie
     *            le numéro d'affilié de l'employeur à rechercher
     * @param date
     *            la date à laquelle l'affilié doit être actif
     * 
     * @return l'employeur (un tiers) si il est trouvé, sinon null
     * 
     * @throws Exception
     *             si numéro d'affilié non unique
     */
    public static final ISFAffilie getEmployeurParNumAffilie(BISession session, String numAffilie, String date)
            throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(numAffilie)) {
            return null;
        }

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, numAffilie);
        employeur.setISession(SFSession.connectSession(session, SFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else if (result.length != 1) {

            // Comparer les dates de début et de fin de l'affiliation par
            // rapport à la date de début du droit
            // pour reprendre uniquement les affiliations actives

            int nbAffiliationActive = 0;
            int noAffiliation = 0;
            String rangAffiliation = "";

            for (int i = 0; i < result.length; i++) {
                GlobazValueObject affilie = (GlobazValueObject) result[i];
                Object value = affilie.getProperty(ISFAffilieImpl.PROPERTY_TIERS_AF);

                JADate dateParametre = new JADate(date);
                JADate dateAffiliationDebut = new JADate(
                        (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_DEBUT));
                JADate dateAffiliationFin = new JADate((String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_FIN));

                int dateParametre2 = Integer.parseInt(dateParametre.toStrAMJ());
                int dateAffiliationDebut2 = Integer.parseInt(dateAffiliationDebut.toStrAMJ());
                int dateAffiliationFin2 = Integer.parseInt(dateAffiliationFin.toStrAMJ());

                if ((dateParametre2 > dateAffiliationDebut2 && dateParametre2 < dateAffiliationFin2)
                        || dateParametre2 > dateAffiliationDebut2
                        && JadeStringUtil.isEmpty((String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_FIN))) {
                    nbAffiliationActive++;
                    noAffiliation = i;

                }

            }

            // si aucune affiliation n'est active, on prend la plus récente
            if (nbAffiliationActive < 1) {

                int dateLastAffiliation = 0;
                int nbLastAffiliation = 0;

                for (int i = 0; i < result.length; i++) {
                    GlobazValueObject affilie = (GlobazValueObject) result[i];
                    Object value = affilie.getProperty(ISFAffilieImpl.PROPERTY_TIERS_AF);

                    JADate dateFinAffiliation = new JADate(
                            (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_FIN));
                    int dateFinAffiliation2 = Integer.parseInt(dateFinAffiliation.toStrAMJ());

                    if (dateFinAffiliation2 >= dateLastAffiliation) {
                        dateLastAffiliation = dateFinAffiliation2;
                        nbLastAffiliation = i;
                    }

                }

                GlobazValueObject affilie = (GlobazValueObject) result[nbLastAffiliation];
                Object value = affilie.getProperty(ISFAffilieImpl.PROPERTY_TIERS_AF);

                if (value != null) {
                    GlobazValueObject tiers = SFTiersHelper.getTiersAdresseVOParId(session, value.toString());

                    if (tiers != null) {
                        // le tiers correspondant existe
                        return new ISFAffilieImpl(tiers,
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_ID_AFFILIE), numAffilie,
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_DEBUT),
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_FIN));
                    }
                }

                // le tiers correspondant n'existe pas
                return null;

                // si une seule affiliation est active, on prend celle-là
            } else if (nbAffiliationActive == 1) {

                GlobazValueObject affilie = (GlobazValueObject) result[noAffiliation];
                Object value = affilie.getProperty(ISFAffilieImpl.PROPERTY_TIERS_AF);

                if (value != null) {
                    GlobazValueObject tiers = SFTiersHelper.getTiersAdresseVOParId(session, value.toString());

                    if (tiers != null) {
                        // le tiers correspondant existe
                        return new ISFAffilieImpl(tiers,
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_ID_AFFILIE), numAffilie,
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_DEBUT),
                                (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_FIN));
                    }
                }

                // le tiers correspondant n'existe pas
                return null;

                // Sinon (Cela ne devrait pas être possible)
            } else {
                throw new Exception(((BSession) session).getLabel("ERROR_PLUSIEURS_AFFILIATION") + "(numAffilie = "
                        + numAffilie + ")");
            }

        } else {
            // on a trouvé un enregistrement avec ce numéro d'affilie, il reste
            // à charger le tiers correspondant
            GlobazValueObject affilie = (GlobazValueObject) result[0];
            Object value = affilie.getProperty(ISFAffilieImpl.PROPERTY_TIERS_AF);

            if (value != null) {
                GlobazValueObject tiers = SFTiersHelper.getTiersAdresseVOParId(session, value.toString());

                if (tiers != null) {
                    // le tiers correspondant existe
                    return new ISFAffilieImpl(tiers, (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_ID_AFFILIE),
                            numAffilie, (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                            (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                            (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_DEBUT),
                            (String) affilie.getProperty(ISFAffilieImpl.PROPERTY_DATE_FIN));
                }
            }

            // le tiers correspondant n'existe pas
            return null;
        }

    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * Retourne la liste (d'instances de AFTauxAssurance) des taux triés par date croissante pour une assurance.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idAssurance
     *            DOCUMENT ME!
     * @param sexe
     *            DOCUMENT ME!
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * 
     * @return une liste (jamais null) d'instances de AFTauxAssurance triées par date croissante.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final List getTauxAssurance(BISession session, String idAssurance, String sexe, String dateDebut,
            String dateFin) throws Exception {
        IAFAssurance assurance = (IAFAssurance) session.getAPIFor(IAFAssurance.class);

        assurance.setISession(SFSession.connectSession(session, SFApplication.DEFAULT_APPLICATION_NAOS));

        return assurance.getTauxList(idAssurance, sexe, dateDebut, dateFin);
    }

    /**
     * True si un tiers possede un affiliation en tant qu'independant.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isTiersIndependant(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return false;
        }

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
        employeur.setISession(SFSession.connectSession(session, SFApplication.DEFAULT_APPLICATION_NAOS));

        IAFAffiliation[] result = employeur.findAffiliation(criteres);

        if ((result == null) || (result.length == 0)) {
            return false;
        } else {
            // on a trouve des affilitations
            for (int i = 0; i < result.length; i++) {
                // un independant
                if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(result[i].getTypeAffiliation())
                        || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(result[i].getTypeAffiliation())) {
                    return true;
                }
            }

            return false;
        }
    }
}
