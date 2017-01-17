package ch.globaz.orion.ws.common;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface WebAvsCommonService {
    /**
     * Permet de télécharger (tableau de byte) un fichier en fonction du path
     * 
     * @param filepath
     * @return
     */
    @WebMethod
    public abstract byte[] downloadFile(@WebParam(name = "filepath") String filepath);

    /**
     * Permet de retourner l'âge de la retraite en fonction du sexe et de la date de naissance
     * 
     * @param sexe
     * @param dateNaissance
     * @return
     */
    @WebMethod
    public abstract String findAgeRetraite(@WebParam(name = "sexe") String sexe,
            @WebParam(name = "dateNaissance") String dateNaissance);
}
