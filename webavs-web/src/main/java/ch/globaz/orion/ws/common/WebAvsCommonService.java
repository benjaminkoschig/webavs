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
}
