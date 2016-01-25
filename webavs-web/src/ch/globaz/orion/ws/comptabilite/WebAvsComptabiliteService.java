package ch.globaz.orion.ws.comptabilite;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface WebAvsComptabiliteService {

    /**
     * retourne la liste des aperçus de compte annexes en fonction du numéro d'affilié
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    public abstract List<ApercuCompteAnnexe> listerApercuCompteAnnexe(
            @WebParam(name = "numeroAffilie") String numeroAffilie, @WebParam(name = "langue") String langue);

    /**
     * Génère le document Extrait de compte et retourne le path du fichier
     * 
     * @param idCompteAnnexe
     * @param dateDebut
     * @param dateFin
     * @param langue
     * @return
     */
    @WebMethod
    public abstract String genererExtraitCompteAnnexe(@WebParam(name = "idCompteAnnexe") String idCompteAnnexe,
            @WebParam(name = "dateDebut") String dateDebut, @WebParam(name = "dateFin") String dateFin,
            @WebParam(name = "langue") String langue);

    /**
     * Permet de télécharger (tableau de byte) un fichier en fonction du path
     * 
     * @param filepath
     * @return
     */
    @WebMethod
    public abstract byte[] downloadFile(@WebParam(name = "filepath") String filepath);
}
