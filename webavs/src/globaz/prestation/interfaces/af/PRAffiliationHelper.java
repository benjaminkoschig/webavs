/*
 * Cr�� le 8 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.interfaces.af;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.format.IFormatData;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFAssurance;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRSession;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class PRAffiliationHelper {

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
     * @return une liste de String[2] {idAffilie, numAffilie} ou null si pas d'affiliation ou idTiers null.
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
        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            // on a trouve des affilitations que l'on retourne
            Vector retValue = new Vector();

            for (int idAffiliation = 0; idAffiliation < result.length; ++idAffiliation) {
                GlobazValueObject vo = (GlobazValueObject) result[idAffiliation];

                retValue.add(new String[] { (String) vo.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE),
                        (String) vo.getProperty(IPRAffilieImpl.PROPERTY_AFFILIE_NUMERO) });
            }

            return retValue;
        }
    }

    public static final Vector /* String[] */getAffiliationsTiersExt(BISession session, String idTiers)
            throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else {
            // on a trouve des affilitations que l'on retourne
            Vector retValue = new Vector();

            for (int idAffiliation = 0; idAffiliation < result.length; ++idAffiliation) {
                GlobazValueObject vo = (GlobazValueObject) result[idAffiliation];

                String s = (String) vo.getProperty(IPRAffilieImpl.PROPERTY_AFFILIE_NUMERO);

                // InfoRom 531
                try {
                    String id = (String) vo.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE);
                    AFAffiliation af = new AFAffiliation();
                    af.setSession((BSession) PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                    af.setAffiliationId(id);
                    af.retrieve();

                    if (!af.isNew()) {
                        s = s + " " + session.getCode(af.getTypeAffiliation());
                    }
                } catch (Exception e) {
                    ;// pas d'indication du type d'affiliation
                }

                retValue.add(new String[] { (String) vo.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE), s });
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
     * @return la valeur courante de l'attribut employeur par num affilie
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IPRAffilie getEmployeurParIdAffilie(BISession session, BITransaction transaction,
            String idAffilie, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isIntegerEmpty(idAffilie)) {
            return null;
        }

        IPRAffilie retValue = null;
        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);

        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
        employeur.setAffiliationId(idAffilie);
        employeur.setIdTiers(idTiers);
        employeur.retrieve(transaction);

        if (!employeur.isNew()) {
            GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, employeur.getIdTiers());

            if (tiers != null) {
                // le tiers correspondant existe
                retValue = new IPRAffilieImpl(tiers, idAffilie, employeur.getAffilieNumero(),
                        employeur.getBrancheEconomique(), employeur.getTypeAffiliation(), employeur.getNumeroIDE(),
                        employeur.getIdeStatut());
            }
        }

        return retValue;
    }

    /**
     * recherche un employeur par son num�ro d'affili�
     * 
     * @param session
     *            DOCUMENT ME!
     * @param numAffilie
     *            le num�ro d'affili� de l'employeur � rechercher
     * @return l'employeur (un tiers) si il est trouv�, sinon null
     * @throws Exception
     *             si num�ro d'affili� non unique
     */
    public static final IPRAffilie getEmployeurParNumAffilie(BISession session, String numAffilie) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(numAffilie)) {
            return null;
        }

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, numAffilie);

        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else if (result.length > 1) {
            // on prend le plus r�cent

            GlobazValueObject mostRecentAffiliation = null;
            JACalendar cal = new JACalendarGregorian();
            for (int i = 0; i < result.length; i++) {
                GlobazValueObject affilie = (GlobazValueObject) result[i];
                if (mostRecentAffiliation == null) {
                    mostRecentAffiliation = affilie;
                } else {
                    JADate ddRef = new JADate(
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT));
                    JADate dd = new JADate((String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT));
                    if (cal.compare(ddRef, dd) == JACalendar.COMPARE_FIRSTLOWER) {
                        mostRecentAffiliation = affilie;
                    }
                }
            }
            Object value = mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);
            if (value != null) {
                GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                if (tiers != null) {
                    // le tiers correspondant existe
                    return new IPRAffilieImpl(tiers,
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE), numAffilie,
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_NUMERO_IDE),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_STATUT_IDE));
                }
            }

            // le tiers correspondant n'existe pas
            return null;

        } else {
            // on a trouv� un enregistrement avec ce num�ro d'affilie, il reste
            // � charger le tiers correspondant
            GlobazValueObject affilie = (GlobazValueObject) result[0];
            Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

            if (value != null) {
                GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                if (tiers != null) {
                    // le tiers correspondant existe
                    return new IPRAffilieImpl(tiers, (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE),
                            numAffilie, (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_NUMERO_IDE),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_STATUT_IDE));
                }
            }

            // le tiers correspondant n'existe pas
            return null;
        }
    }

    /**
     * Recherche un employeur par son num�ro d'affili� PAR RAPPORT A UNE DATE DONNEE !!!
     * 
     * @param session
     * @param numAffilie
     *            le num�ro d'affili� de l'employeur � rechercher
     * @param date
     *            la date � laquelle l'affili� doit �tre actif
     * @return l'employeur (un tiers) si il est trouv�, sinon null
     * @throws Exception
     *             si le num�ro d'affili� non unique
     */
    public static final IPRAffilie getEmployeurParNumAffilie(BISession session, String numAffilie, String date)
            throws Exception {
        return PRAffiliationHelper.getEmployeurParNumAffilie(session, numAffilie, date, true);
    }

    /**
     * Recherche un employeur par son num�ro d'affili� PAR RAPPORT A UNE DATE DONNEE !!!
     * 
     * @param session
     * @param numAffilie
     *            le num�ro d'affili� de l'employeur � rechercher
     * @param date
     *            la date � laquelle l'affili� doit �tre actif
     * @param failIfEmployeurNotUnique
     *            Lance une exception si d�finit � <code>true</code> et que plusieurs num�ro d'affili� sont identique
     * @return l'employeur (un tiers) si il est trouv�, sinon null
     * @throws Exception
     *             si num�ro d'affili� non unique
     */
    public static final IPRAffilie getEmployeurParNumAffilie(BISession session, String numAffilie, String date,
            boolean failIfEmployeurNotUnique) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(numAffilie)) {
            return null;
        }

        // BZ_6560 : Erreur dans le formattage automatique du no d'affili�
        IFormatData numAffilieFormater = PRAbstractApplication.getAffileFormater();
        numAffilie = numAffilieFormater.format(numAffilie);

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, numAffilie);
        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else if (result.length != 1) {

            // Comparer les dates de d�but et de fin de l'affiliation par
            // rapport � la date de d�but du droit
            // pour reprendre uniquement les affiliations actives

            int nbAffiliationActive = 0;
            int noAffiliation = 0;
            String rangAffiliation = "";

            for (int i = 0; i < result.length; i++) {
                GlobazValueObject affilie = (GlobazValueObject) result[i];
                Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

                JADate dateParametre = new JADate(date);
                JADate dateAffiliationDebut = new JADate(
                        (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT));
                JADate dateAffiliationFin = new JADate((String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN));

                int dateParametre2 = Integer.parseInt(dateParametre.toStrAMJ());
                int dateAffiliationDebut2 = Integer.parseInt(dateAffiliationDebut.toStrAMJ());
                int dateAffiliationFin2 = Integer.parseInt(dateAffiliationFin.toStrAMJ());

                if (((dateParametre2 > dateAffiliationDebut2) && (dateParametre2 < dateAffiliationFin2))
                        || ((dateParametre2 > dateAffiliationDebut2) && JadeStringUtil.isEmpty((String) affilie
                                .getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN)))) {
                    nbAffiliationActive++;
                    noAffiliation = i;

                }

            }

            // si aucune affiliation n'est active, on prend la plus r�cente
            if (nbAffiliationActive < 1) {

                int dateLastAffiliation = 0;
                int nbLastAffiliation = 0;

                for (int i = 0; i < result.length; i++) {
                    GlobazValueObject affilie = (GlobazValueObject) result[i];
                    Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

                    JADate dateFinAffiliation = new JADate(
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN));
                    int dateFinAffiliation2 = Integer.parseInt(dateFinAffiliation.toStrAMJ());

                    if (dateFinAffiliation2 >= dateLastAffiliation) {
                        dateLastAffiliation = dateFinAffiliation2;
                        nbLastAffiliation = i;
                    }

                }

                GlobazValueObject affilie = (GlobazValueObject) result[nbLastAffiliation];
                Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

                if (value != null) {
                    GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                    if (tiers != null) {
                        // le tiers correspondant existe
                        return new IPRAffilieImpl(tiers,
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE), numAffilie,
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_NUMERO_IDE),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_STATUT_IDE));
                    }
                }

                // le tiers correspondant n'existe pas
                return null;

                // si une seule affiliation est active, on prend celle-l�
                // 1.11.0
            } else if (nbAffiliationActive == 1) {

                GlobazValueObject affilie = (GlobazValueObject) result[noAffiliation];
                Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

                if (value != null) {
                    GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                    if (tiers != null) {
                        // le tiers correspondant existe
                        return new IPRAffilieImpl(tiers,
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE), numAffilie,
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                                (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN));
                    }
                }

                // le tiers correspondant n'existe pas
                return null;

                // Sinon (Cela ne devrait pas �tre possible)
            }
            // plusieurs affliliation active
            else {
                if (failIfEmployeurNotUnique) {
                    throw new Exception(
                            "Plusieurs affiliations ont �t� trouv�s pour la date de d�but du droit (numAffilie = "
                                    + numAffilie + ")");
                } else {
                    GlobazValueObject affilie = (GlobazValueObject) result[noAffiliation];
                    Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

                    if (value != null) {
                        GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                        if (tiers != null) {
                            // le tiers correspondant existe
                            return new IPRAffilieImpl(tiers,
                                    (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE), numAffilie,
                                    (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                                    (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                                    (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                                    (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN));
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }

            }

        } else {
            // on a trouv� un enregistrement avec ce num�ro d'affilie, il reste
            // � charger le tiers correspondant
            GlobazValueObject affilie = (GlobazValueObject) result[0];
            Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

            if (value != null) {
                GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                if (tiers != null) {
                    // le tiers correspondant existe
                    return new IPRAffilieImpl(tiers, (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE),
                            numAffilie, (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN));
                }
            }

            // le tiers correspondant n'existe pas
            return null;
        }

    }

    public static final IPRAffilie getEmployeurParNumAffilieEtTypeAffiliation(BISession session, String numAffilie,
            String csTypeAffiliation) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(numAffilie)) {
            return null;
        }

        IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        Hashtable criteres = new Hashtable();

        criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, numAffilie);
        criteres.put(IAFAffiliation.FIND_FOR_LISTTYPEAFFILIATION, csTypeAffiliation);

        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));

        Object[] result = employeur.find(criteres);

        if ((result == null) || (result.length == 0)) {
            return null;
        } else if (result.length > 1) {
            // on prend le plus r�cent

            GlobazValueObject mostRecentAffiliation = null;
            JACalendar cal = new JACalendarGregorian();
            for (int i = 0; i < result.length; i++) {
                GlobazValueObject affilie = (GlobazValueObject) result[i];
                if (mostRecentAffiliation == null) {
                    mostRecentAffiliation = affilie;
                } else {
                    JADate ddRef = new JADate(
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT));
                    JADate dd = new JADate((String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT));
                    if (cal.compare(ddRef, dd) == JACalendar.COMPARE_FIRSTLOWER) {
                        mostRecentAffiliation = affilie;
                    }
                }
            }
            Object value = mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);
            if (value != null) {
                GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                if (tiers != null) {
                    // le tiers correspondant existe
                    return new IPRAffilieImpl(tiers,
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE), numAffilie,
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_NUMERO_IDE),
                            (String) mostRecentAffiliation.getProperty(IPRAffilieImpl.PROPERTY_STATUT_IDE));
                }
            }

            // le tiers correspondant n'existe pas
            return null;

        } else {
            // on a trouv� un enregistrement avec ce num�ro d'affilie, il reste
            // � charger le tiers correspondant
            GlobazValueObject affilie = (GlobazValueObject) result[0];
            Object value = affilie.getProperty(IPRAffilieImpl.PROPERTY_TIERS_AF);

            if (value != null) {
                GlobazValueObject tiers = PRTiersHelper.getTiersAdresseVOParId(session, value.toString());

                if (tiers != null) {
                    // le tiers correspondant existe
                    return new IPRAffilieImpl(tiers, (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_ID_AFFILIE),
                            numAffilie, (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_BRANCHE_ECONOMIQUE),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_TYPE_AFFILIATION),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_DEBUT),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_DATE_FIN),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_NUMERO_IDE),
                            (String) affilie.getProperty(IPRAffilieImpl.PROPERTY_STATUT_IDE));
                }
            }

            // le tiers correspondant n'existe pas
            return null;
        }
    }

    /**
     * Retourne la liste (d'instances de AFTauxAssurance) des taux tri�s par date croissante pour une assurance.
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
     * @return une liste (jamais null) d'instances de AFTauxAssurance tri�es par date croissante.
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final List getTauxAssurance(BISession session, String idAssurance, String sexe, String dateDebut,
            String dateFin) throws Exception {
        IAFAssurance assurance = (IAFAssurance) session.getAPIFor(IAFAssurance.class);

        assurance.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));

        return assurance.getTauxList(idAssurance, sexe, dateDebut, dateFin);
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * True si un tiers possede un affiliation en tant qu'independant.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * @return
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
        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));

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
