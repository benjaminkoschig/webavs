package globaz.lynx.format.societe;

import globaz.globall.api.BISession;

public interface ILXSocieteDebitriceFormat {

    /**
     * Contrôle du format de l'id externe d'une société.
     * 
     * @param session
     * @param value
     * @throws Exception
     *             Si non valide
     */
    public void checkIdExterne(BISession session, String value) throws Exception;
}
