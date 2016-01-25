package globaz.osiris.external;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * Interface représentant un tiers au format définit par la comptabilité auxiliaire
 * 
 * @author auteur
 */
/**
 * @author sch
 */
public interface IntTiers extends BIEntity {
    public static final String PYXIS_DOMAINE_CONTENTIEUX = "519013";
    public static final String PYXIS_DOMAINE_DEFAUT = "519004";
    public static final String PYXIS_DOMAINE_SURSIS_PMT = "519014";
    public static final String TYPE_LIEN_MANDATAIRE = "507003";
    String OSIRIS_BANQUE = "3";
    String OSIRIS_CAISSE_COMPENSATION = "4";
    String OSIRIS_EMAIL = "4";
    String OSIRIS_FAX = "2";

    String OSIRIS_PERSONNE_MORALE = "2";
    String OSIRIS_PERSONNE_PHYSIQUE = "1";
    String OSIRIS_PORTABLE = "3";
    String OSIRIS_TELEPHONE = "1";

    /**
     * @param idType
     *            type d'adresse
     * @param idApplication
     *            code système du type d'application (TIApplication)
     * @param date
     *            date de validité de l'adresse
     */
    public String getAdresseAsString(JadePublishDocumentInfo docInfo, String idType, String idApplication,
            String idExterne, String date) throws java.lang.Exception;

    public String getAdresseAsString(JadePublishDocumentInfo docInfo, String idType, String idApplication,
            String idExterne, String date, boolean herite) throws java.lang.Exception;

    /**
     * Renvoie la valeur de la propriété adresseCourrier
     * 
     * @return la valeur de la propriété adresseCourrier
     * @param typeAdresse
     *            typeAdresse
     */
    public IntAdresseCourrier getAdresseCourrier(String typeAdresse);

    /**
     * Renvoie la valeur de la propriété adresseCourrier
     * 
     * @return la valeur de la propriété adresseCourrier
     * @param typeAdresse
     *            typeAdresse
     * @param domaine
     *            domaine
     */
    public IntAdresseCourrier getAdresseCourrier(String typeAdresse, String domaine);

    public String getAdresseLienAsString(String typeLien, String idType, String idApplication, String date,
            String langue) throws Exception;

    /**
     * Récupère le nom complémentaire
     */
    public String[] getComplementNom();

    /**
     * Renvoie la valeur le data source de l'adresse de courrier
     * 
     * @return TIAdresseDataSource
     */
    public TIAdresseDataSource getDataSourceAdresseCourrier();

    public String getDesignation1();

    public String getDesignation2();

    /**
     * Renvoie la valeur de la propriété idAdresseCourrier
     * 
     * @return la valeur de la propriété idAdresseCourrier
     * @param typeAdresse
     *            typeAdresse
     */
    public String getIdAdresseCourrier(String typeAdresse);

    /**
     * Renvoie la valeur de la propriété idAdresseCourrier
     * 
     * @return la valeur de la propriété idAdresseCourrier
     * @param typeAdresse
     *            typeAdresse
     * @param domaine
     *            domaine
     */
    public String getIdAdresseCourrier(String typeAdresse, String domaine);

    /**
     * Renvoie la valeur de la propriété idAdressePaiement
     * 
     * @return la valeur de la propriété idAdressePaiement
     * @param domaine
     *            domaine
     */
    public String getIdAdressePaiement(String domaine);

    /**
     * Renvoie la valeur de la propriété idAdressePaiement
     * 
     * @return la valeur de la propriété idAdressePaiement
     * @param domaine
     *            domaine
     * @param idExterne
     *            idExterne
     * @param date
     *            date
     */
    public String getIdAdressePaiement(String domaine, String idExterne, String date);

    /**
     * Renvoie la valeur de la propriété idTiers
     * 
     * @return la valeur de la propriété idTiers
     */
    public String getIdTiers();

    /**
     * Renvoie la valeur de la propriété langueISO
     * 
     * @return la valeur de la propriété langueISO
     */
    public String getLangueISO();

    /**
     * Renvoie la valeur de la propriété lieu
     * 
     * @return la valeur de la propriété lieu
     */
    public String getLieu();

    /**
     * Renvoie la valeur de la propriété listeAdressesCourrier
     * 
     * @return la valeur de la propriété listeAdressesCourrier
     */
    public IntAdresseCourrier[] getListeAdressesCourrier();

    /**
     * Renvoie la valeur de la propriété listeAdressesPaiement
     * 
     * @return la valeur de la propriété listeAdressesPaiement
     */
    public IntAdressePaiement[] getListeAdressesPaiement();

    /**
     * Renvoie la valeur de la propriété listeRoles
     * 
     * @return la valeur de la propriété listeRoles
     */
    public IntRole[] getListeRoles();

    /**
     * Renvoie la valeur de la propriété moyenCommunication
     * 
     * @return la valeur de la propriété moyenCommunication
     * @param typeMoyen
     *            typeMoyen
     */
    public String getMoyenCommunication(String typeMoyen);

    /**
     * Renvoie la valeur de la propriété nom
     * 
     * @return la valeur de la propriété nom
     */
    public String getNom();

    /**
     * Renvoie le nom et prénom
     * 
     * @return Nom et Prénom String
     */
    public String getNomPrenom();

    /**
     * Cette méthode retourne le numéro AVS actuel, en cas d'erreur, retourne ""
     * 
     * @return String numéro AVS
     */
    public String getNumAvsActuel();

    /**
     * Renvoie le tiers de l'office de poursuites Retourne null si tiers non trouvé
     * 
     * @return la valeur de la propriété officePoursuites
     */
    public IntTiers getOfficePoursuites();

    /**
     * Renvoie le tiers de l'office de poursuites en se basant d'abord sur un lien entre le tiers et l'OP puis sur un
     * lien entre la localité de l'adresse du tiers et l'OP Retourne null si tiers non trouvé
     * 
     * @param idLocalite
     *            l'identifiant de la localité du tiers
     * @return la valeur de la propriété officePoursuites
     */
    public IntTiers getOfficePoursuitesSelonLien(String idLocalite);

    /**
     * Récupère la politesse du tiers
     * 
     * @return String la politesse ou "" en cas d'erreur
     */
    public String getPolitesse();

    /**
     * Cette méthode retourne le prénom et nom du tiers
     * 
     * @return String prénom et nom du tiers
     */
    public String getPrenomNom();

    /**
     * Récupère la politesse du tiers en fonction de la langue
     * 
     * @param langue
     *            peut prendre comme valeur :
     *            <ul>
     *            <li>503001 = FRANCAIS
     *            <li>503002 = ALLEMAND
     *            <lI>503004 = ITALIEN
     *            </ul>
     * @return String la politesse ou "" en cas d'erreur
     */
    // public String getPolitesse(String langue);

    /**
     * Récupère le titre du tiers
     */
    public String getTitre();

    /**
     * Récupère le titre du tiers en fonction de la langue.
     * 
     * @param language
     * @return
     */
    public String getTitre(String language);

    public String getTitreTiers();

    /**
     * Renvoie la valeur de la propriété titulaireNomLieu
     * 
     * @return la valeur de la propriété titulaireNomLieu
     */
    public String getTitulaireNomLieu();

    /**
     * Renvoie la valeur de la propriété titulaireNomLocalite
     * 
     * @return
     */
    public String getTitulaireNomLocalite();

    /**
     * Renvoie le tiers de l'office de poursuites en se basant sur le canton du tier
     * 
     * @return la valeur de la propriété officePoursuites
     */
    public IntTiers getTribunalCanton();

    /**
     * Renvoie le tiers du Tribunal en se basant d'abord sur un lien entre le tiers et le Tribunal puis sur un lien
     * entre la localité de l'adresse du tiers et le Tribunal <br />
     * Retourne null si tiers non trouvé
     * 
     * @param idLocalite
     *            l'identifiant de la localité du tiers
     * @return la valeur de la propriété Tribunal
     */
    public IntTiers getTribunalSelonLien(String idLocalite);

    /**
     * Renvoie le tiers de l'office de poursuites en se basant d'abord sur un lien entre le tiers et l'OP puis sur un
     * lien entre la localité de l'adresse du tiers et l'OP Retourne null si tiers non trouvé
     * 
     * @param idType
     *            le type d'adresse (courrier, domicile, ...)
     * @param idApplication
     *            le domaine d'adresse (standard, contentieux, ...)
     * @return la valeur de la propriété officePoursuites
     */
    public IntTiers getTribunalSelonLien(String idType, String idApplication);

    /**
     * Renvoie la valeur de la propriété typeTiers
     * 
     * @return la valeur de la propriété typeTiers
     */
    public String getTypeTiers();

    /**
     * Indique si ???
     * 
     * @return true si ???, false sinon
     */
    public boolean isOnError();

    /**
     * ???
     * 
     * @param transaction
     *            transaction
     * @param idTiers
     *            idTiers
     * @exception java.lang.Exception
     *                si ???
     */
    public void retrieve(BITransaction transaction, String idTiers) throws java.lang.Exception;

    /**
     * ???
     * 
     * @param idTiers
     *            idTiers
     * @exception java.lang.Exception
     *                si ???
     */
    public void retrieve(String idTiers) throws java.lang.Exception;
}
