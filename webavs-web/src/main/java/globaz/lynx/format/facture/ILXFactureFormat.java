package globaz.lynx.format.facture;

import globaz.globall.api.BISession;

public interface ILXFactureFormat {

    /**
     * Contrôle du format de l'id externe (n° de facture interne) d'une facture.
     * 
     * @param session
     * @param value
     * @throws Exception
     *             Si non valide
     */
    public void checkIdExterne(BISession session, String value) throws Exception;
}
