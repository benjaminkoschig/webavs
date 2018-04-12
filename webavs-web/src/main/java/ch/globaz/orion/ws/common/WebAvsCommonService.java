package ch.globaz.orion.ws.common;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
@HandlerChain(file = "handlers.xml")
public interface WebAvsCommonService {
    /**
     * Permet de télécharger (tableau de byte) un fichier en fonction du path
     * 
     * @param filepath
     * @return
     */
    @WebMethod
    @WebResult(name = "file")
    public abstract byte[] downloadFile(@WebParam(name = "filepath") String filepath);

    /**
     * Permet de retourner la date de la retraite en fonction du sexe et de la date de naissance
     * 
     * @param sexe
     * @param dateNaissance
     * @return
     */
    @WebMethod
    @WebResult(name = "dateRetraite")
    public abstract String findDateRetraite(@WebParam(name = "sexe") String sexe,
            @WebParam(name = "dateNaissance") String dateNaissance);
}
