package globaz.pavo.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface des annonces en retour de la centrale qui sont en suspens
 * 
 * @author David Girardin
 */
public interface ICIAnnonceSuspens extends BIEntity {
    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @param transaction
     *            la transaction à utiliser pour la requête
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie l'id de l'annonce.
     * 
     * @return l'id de l'annonce.
     */
    public String getAnnonceSuspensId();

    /**
     * Renvoie le code application.
     * 
     * @return le code application.
     */
    public java.lang.String getCodeApplication();

    /**
     * Renvoie la date de réception.
     * 
     * @return la date de réception.
     */
    public String getDateReception();

    /**
     * Renvoie l'id de l'annonce dans HERMES.
     * 
     * @return l'id de l'annonce dans HERMES.
     */
    public String getIdAnnonce();

    /**
     * Renvoie l'id du CI associé à l'annonce si celui-ci est au RA.
     * 
     * @return l'id du CI associé à l'annonce si celui-ci est au RA.
     */
    public String getIdCIRA();

    /**
     * Renvoie l'id du motif.
     * 
     * @return l'id du motif.
     */
    public String getIdMotifArc();

    /**
     * Renvoie l'id du type de traitement.
     * 
     * @return l'id du type de traitement.
     */
    public String getIdTypeTraitement();

    /**
     * Renvoie le no avs associé à l'annonce.
     * 
     * @return le no avs associé à l'annonce.
     */
    public String getNumeroAvs();

    /**
     * Renvoie le no de la caisse associé à l'annonce.
     * 
     * @return le no de la caisse associé à l'annonce.
     */
    public String getNumeroCaisse();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @param transaction
     *            la transaction à utiliser
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Définit si l'annonce est en suspens.
     * 
     * @param newCodeApplication
     *            true si l'annonce est en suspens.
     */
    public void setAnnonceSuspens(Boolean newAnnonceSuspens);

    /**
     * Définit l'id de l'annonce.
     * 
     * @param newCodeApplication
     *            l'id de l'annonce.
     */
    public void setAnnonceSuspensId(String newAnnonceSuspensId);

    /**
     * Définit le code application.
     * 
     * @param newCodeApplication
     *            le code application.
     */
    public void setCodeApplication(java.lang.String newCodeApplication);

    /**
     * Définit la date de réception de l'annonce.
     * 
     * @param newCodeApplication
     *            la date de réception de l'annonce.
     */
    public void setDateReception(String newDateReception);

    /**
     * Définit l'id de l'annonce dans HERMES.
     * 
     * @param newCodeApplication
     *            l'id de l'annonce dans HERMES.
     */
    public void setIdAnnonce(String newIdAnnonce);

    /**
     * Définit le motif de l'annonce.
     * 
     * @param newCodeApplication
     *            le motif de l'annonce.
     */
    public void setIdMotifArc(String newIdMotifArc);

    /**
     * Définit le no avs lié à l'annonce.
     * 
     * @param newCodeApplication
     *            le no avs lié à l'annonce.
     */
    public void setNumeroAvs(String newNumeroAvs);

    /**
     * Définit le no de la caisse lié à l'annonce.
     * 
     * @param newCodeApplication
     *            le no de la caisse lié à l'annonce.
     */
    public void setNumeroCaisse(String newNumeroCaisse);
}
