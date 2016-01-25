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
     *            la transaction � utiliser pour la requ�te
     * @exception java.lang.Exception
     *                si l'ajout a �chou�
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
     * Renvoie la date de r�ception.
     * 
     * @return la date de r�ception.
     */
    public String getDateReception();

    /**
     * Renvoie l'id de l'annonce dans HERMES.
     * 
     * @return l'id de l'annonce dans HERMES.
     */
    public String getIdAnnonce();

    /**
     * Renvoie l'id du CI associ� � l'annonce si celui-ci est au RA.
     * 
     * @return l'id du CI associ� � l'annonce si celui-ci est au RA.
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
     * Renvoie le no avs associ� � l'annonce.
     * 
     * @return le no avs associ� � l'annonce.
     */
    public String getNumeroAvs();

    /**
     * Renvoie le no de la caisse associ� � l'annonce.
     * 
     * @return le no de la caisse associ� � l'annonce.
     */
    public String getNumeroCaisse();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @param transaction
     *            la transaction � utiliser
     * @exception java.lang.Exception
     *                si le chargement a �chou�
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * D�finit si l'annonce est en suspens.
     * 
     * @param newCodeApplication
     *            true si l'annonce est en suspens.
     */
    public void setAnnonceSuspens(Boolean newAnnonceSuspens);

    /**
     * D�finit l'id de l'annonce.
     * 
     * @param newCodeApplication
     *            l'id de l'annonce.
     */
    public void setAnnonceSuspensId(String newAnnonceSuspensId);

    /**
     * D�finit le code application.
     * 
     * @param newCodeApplication
     *            le code application.
     */
    public void setCodeApplication(java.lang.String newCodeApplication);

    /**
     * D�finit la date de r�ception de l'annonce.
     * 
     * @param newCodeApplication
     *            la date de r�ception de l'annonce.
     */
    public void setDateReception(String newDateReception);

    /**
     * D�finit l'id de l'annonce dans HERMES.
     * 
     * @param newCodeApplication
     *            l'id de l'annonce dans HERMES.
     */
    public void setIdAnnonce(String newIdAnnonce);

    /**
     * D�finit le motif de l'annonce.
     * 
     * @param newCodeApplication
     *            le motif de l'annonce.
     */
    public void setIdMotifArc(String newIdMotifArc);

    /**
     * D�finit le no avs li� � l'annonce.
     * 
     * @param newCodeApplication
     *            le no avs li� � l'annonce.
     */
    public void setNumeroAvs(String newNumeroAvs);

    /**
     * D�finit le no de la caisse li� � l'annonce.
     * 
     * @param newCodeApplication
     *            le no de la caisse li� � l'annonce.
     */
    public void setNumeroCaisse(String newNumeroCaisse);
}
