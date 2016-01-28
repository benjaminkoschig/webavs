package globaz.prestation.acor;

import globaz.globall.db.BSession;
import java.util.List;

/**
 * Adapteur permettant de transformer une demande de prestation APG/AMAT/Rente/IJ en liste de fichiers ACOR.
 * 
 * <p>
 * voir la doc du package pour de plus amples informations.
 * </p>
 * 
 * @author vre
 */
public interface PRACORAdapter {

    /**
     * retourne la date déterminante pour la création de la situation familiale (entre autres).
     * 
     * @return la valeur courante de l'attribut date determinante
     * 
     * @throws PRACORException
     *             si une erreur survient.
     */
    public String getDateDeterminante() throws PRACORException;

    /**
     * Retourne la liste des fichiers ACOR pour cette demande de prestation.
     * 
     * @return une liste (jamais nulle, peut-etre vide) d'instances de PRFichierACORPrinter.
     */
    public List<PRFichierACORPrinter> getFichiersACOR();

    /**
     * getter pour l'attribut session.
     * 
     * @return la valeur courante de l'attribut session
     */
    public BSession getSession();

    /**
     * retourne l'id du tiers de l'assure.
     * 
     * @throws PRACORException
     *             si une erreur survient.
     */
    public String idTiersAssure() throws PRACORException;

    /**
     * Le numéro AVS de l'assuré ayant fait la demande.
     * 
     * @return une no AVS formaté.
     * 
     * @throws PRACORException
     *             si une erreur survient.
     */
    public String numeroAVSAssure() throws PRACORException;
}
