package globaz.lynx.format.societe;

import globaz.globall.api.BISession;

public interface ILXSocieteDebitriceFormat {

    /**
     * Contr�le du format de l'id externe d'une soci�t�.
     * 
     * @param session
     * @param value
     * @throws Exception
     *             Si non valide
     */
    public void checkIdExterne(BISession session, String value) throws Exception;
}
