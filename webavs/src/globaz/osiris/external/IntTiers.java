package globaz.osiris.external;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * Interface repr�sentant un tiers au format d�finit par la comptabilit� auxiliaire
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
     *            code syst�me du type d'application (TIApplication)
     * @param date
     *            date de validit� de l'adresse
     */
    public String getAdresseAsString(JadePublishDocumentInfo docInfo, String idType, String idApplication,
            String idExterne, String date) throws java.lang.Exception;

    public String getAdresseAsString(JadePublishDocumentInfo docInfo, String idType, String idApplication,
            String idExterne, String date, boolean herite) throws java.lang.Exception;

    /**
     * Renvoie la valeur de la propri�t� adresseCourrier
     * 
     * @return la valeur de la propri�t� adresseCourrier
     * @param typeAdresse
     *            typeAdresse
     */
    public IntAdresseCourrier getAdresseCourrier(String typeAdresse);

    /**
     * Renvoie la valeur de la propri�t� adresseCourrier
     * 
     * @return la valeur de la propri�t� adresseCourrier
     * @param typeAdresse
     *            typeAdresse
     * @param domaine
     *            domaine
     */
    public IntAdresseCourrier getAdresseCourrier(String typeAdresse, String domaine);

    public String getAdresseLienAsString(String typeLien, String idType, String idApplication, String date,
            String langue) throws Exception;

    /**
     * R�cup�re le nom compl�mentaire
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
     * Renvoie la valeur de la propri�t� idAdresseCourrier
     * 
     * @return la valeur de la propri�t� idAdresseCourrier
     * @param typeAdresse
     *            typeAdresse
     */
    public String getIdAdresseCourrier(String typeAdresse);

    /**
     * Renvoie la valeur de la propri�t� idAdresseCourrier
     * 
     * @return la valeur de la propri�t� idAdresseCourrier
     * @param typeAdresse
     *            typeAdresse
     * @param domaine
     *            domaine
     */
    public String getIdAdresseCourrier(String typeAdresse, String domaine);

    /**
     * Renvoie la valeur de la propri�t� idAdressePaiement
     * 
     * @return la valeur de la propri�t� idAdressePaiement
     * @param domaine
     *            domaine
     */
    public String getIdAdressePaiement(String domaine);

    /**
     * Renvoie la valeur de la propri�t� idAdressePaiement
     * 
     * @return la valeur de la propri�t� idAdressePaiement
     * @param domaine
     *            domaine
     * @param idExterne
     *            idExterne
     * @param date
     *            date
     */
    public String getIdAdressePaiement(String domaine, String idExterne, String date);

    /**
     * Renvoie la valeur de la propri�t� idTiers
     * 
     * @return la valeur de la propri�t� idTiers
     */
    public String getIdTiers();

    /**
     * Renvoie la valeur de la propri�t� langueISO
     * 
     * @return la valeur de la propri�t� langueISO
     */
    public String getLangueISO();

    /**
     * Renvoie la valeur de la propri�t� lieu
     * 
     * @return la valeur de la propri�t� lieu
     */
    public String getLieu();

    /**
     * Renvoie la valeur de la propri�t� listeAdressesCourrier
     * 
     * @return la valeur de la propri�t� listeAdressesCourrier
     */
    public IntAdresseCourrier[] getListeAdressesCourrier();

    /**
     * Renvoie la valeur de la propri�t� listeAdressesPaiement
     * 
     * @return la valeur de la propri�t� listeAdressesPaiement
     */
    public IntAdressePaiement[] getListeAdressesPaiement();

    /**
     * Renvoie la valeur de la propri�t� listeRoles
     * 
     * @return la valeur de la propri�t� listeRoles
     */
    public IntRole[] getListeRoles();

    /**
     * Renvoie la valeur de la propri�t� moyenCommunication
     * 
     * @return la valeur de la propri�t� moyenCommunication
     * @param typeMoyen
     *            typeMoyen
     */
    public String getMoyenCommunication(String typeMoyen);

    /**
     * Renvoie la valeur de la propri�t� nom
     * 
     * @return la valeur de la propri�t� nom
     */
    public String getNom();

    /**
     * Renvoie le nom et pr�nom
     * 
     * @return Nom et Pr�nom String
     */
    public String getNomPrenom();

    /**
     * Cette m�thode retourne le num�ro AVS actuel, en cas d'erreur, retourne ""
     * 
     * @return String num�ro AVS
     */
    public String getNumAvsActuel();

    /**
     * Renvoie le tiers de l'office de poursuites Retourne null si tiers non trouv�
     * 
     * @return la valeur de la propri�t� officePoursuites
     */
    public IntTiers getOfficePoursuites();

    /**
     * Renvoie le tiers de l'office de poursuites en se basant d'abord sur un lien entre le tiers et l'OP puis sur un
     * lien entre la localit� de l'adresse du tiers et l'OP Retourne null si tiers non trouv�
     * 
     * @param idLocalite
     *            l'identifiant de la localit� du tiers
     * @return la valeur de la propri�t� officePoursuites
     */
    public IntTiers getOfficePoursuitesSelonLien(String idLocalite);

    /**
     * R�cup�re la politesse du tiers
     * 
     * @return String la politesse ou "" en cas d'erreur
     */
    public String getPolitesse();

    /**
     * Cette m�thode retourne le pr�nom et nom du tiers
     * 
     * @return String pr�nom et nom du tiers
     */
    public String getPrenomNom();

    /**
     * R�cup�re la politesse du tiers en fonction de la langue
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
     * R�cup�re le titre du tiers
     */
    public String getTitre();

    /**
     * R�cup�re le titre du tiers en fonction de la langue.
     * 
     * @param language
     * @return
     */
    public String getTitre(String language);

    public String getTitreTiers();

    /**
     * Renvoie la valeur de la propri�t� titulaireNomLieu
     * 
     * @return la valeur de la propri�t� titulaireNomLieu
     */
    public String getTitulaireNomLieu();

    /**
     * Renvoie la valeur de la propri�t� titulaireNomLocalite
     * 
     * @return
     */
    public String getTitulaireNomLocalite();

    /**
     * Renvoie le tiers de l'office de poursuites en se basant sur le canton du tier
     * 
     * @return la valeur de la propri�t� officePoursuites
     */
    public IntTiers getTribunalCanton();

    /**
     * Renvoie le tiers du Tribunal en se basant d'abord sur un lien entre le tiers et le Tribunal puis sur un lien
     * entre la localit� de l'adresse du tiers et le Tribunal <br />
     * Retourne null si tiers non trouv�
     * 
     * @param idLocalite
     *            l'identifiant de la localit� du tiers
     * @return la valeur de la propri�t� Tribunal
     */
    public IntTiers getTribunalSelonLien(String idLocalite);

    /**
     * Renvoie le tiers de l'office de poursuites en se basant d'abord sur un lien entre le tiers et l'OP puis sur un
     * lien entre la localit� de l'adresse du tiers et l'OP Retourne null si tiers non trouv�
     * 
     * @param idType
     *            le type d'adresse (courrier, domicile, ...)
     * @param idApplication
     *            le domaine d'adresse (standard, contentieux, ...)
     * @return la valeur de la propri�t� officePoursuites
     */
    public IntTiers getTribunalSelonLien(String idType, String idApplication);

    /**
     * Renvoie la valeur de la propri�t� typeTiers
     * 
     * @return la valeur de la propri�t� typeTiers
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
