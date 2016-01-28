package globaz.lynx.format.fournisseur;

import globaz.globall.api.BISession;

public interface ILXFournisseurFormat {

    /**
     * Contr�le du format de l'id externe d'un fournisseur.
     * 
     * @param session
     * @param value
     * @throws Exception
     *             Si non valide
     */
    public void checkIdExterne(BISession session, String value) throws Exception;
}
