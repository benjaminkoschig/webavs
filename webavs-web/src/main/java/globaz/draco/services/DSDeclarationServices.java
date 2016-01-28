package globaz.draco.services;

import globaz.draco.db.inscriptions.DSCountNbrEmployesByCantonForDeclarationManager;
import globaz.draco.exceptions.DSTechnicalException;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.exceptions.AFTechnicalException;

/**
 * Class de services sur les déclarations de salaire
 */
public class DSDeclarationServices {

    private DSDeclarationServices() {
        throw new UnsupportedOperationException();
    }

    /**
     * Permet le comptage des assurés par canton pour une déclaration donnée
     * 
     * @param session Une session
     * @param codeCanton Le code system du canton
     * @param idDeclaration L'id d'une déclarartion de salaire
     * @return le nombre d'assuré
     * @exception NullPointerException Si la session passée est null<BR>
     * @exception IllegalArgumentException Si les paramètres sont vides ou null
     * @exception AFTechnicalException Si le comptage des assuré a lancé une exception
     */
    public static int countNbrAssuresByCantonForDeclararion(BSession session, String codeCanton, String idDeclaration) {

        int nbrAssures = 0;

        if (session == null) {
            throw new NullPointerException("La session doit être non null");
        }

        if (JadeStringUtil.isBlankOrZero(codeCanton)) {
            throw new IllegalArgumentException("Le code du canton doit être précisé" + codeCanton);
        }

        if (JadeStringUtil.isBlankOrZero(idDeclaration)) {
            throw new IllegalArgumentException("L'id d'un déclaration doit être précisé : " + idDeclaration);
        }

        DSCountNbrEmployesByCantonForDeclarationManager manager = new DSCountNbrEmployesByCantonForDeclarationManager();
        manager.setForCodeCanton(codeCanton);
        manager.setForIdDeclaration(idDeclaration);
        manager.setSession(session);

        try {
            nbrAssures = manager.getCount();
        } catch (Exception e) {
            throw new DSTechnicalException(
                    "Impossible de compter le nombre d'assuré pour la déclaration (idDeclaration = " + idDeclaration
                            + " / codeCanton = " + codeCanton, e);
        }

        return nbrAssures;
    }
}
