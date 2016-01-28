package globaz.draco.services;

import globaz.draco.db.inscriptions.DSCountNbrEmployesByCantonForDeclarationManager;
import globaz.draco.exceptions.DSTechnicalException;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.exceptions.AFTechnicalException;

/**
 * Class de services sur les d�clarations de salaire
 */
public class DSDeclarationServices {

    private DSDeclarationServices() {
        throw new UnsupportedOperationException();
    }

    /**
     * Permet le comptage des assur�s par canton pour une d�claration donn�e
     * 
     * @param session Une session
     * @param codeCanton Le code system du canton
     * @param idDeclaration L'id d'une d�clarartion de salaire
     * @return le nombre d'assur�
     * @exception NullPointerException Si la session pass�e est null<BR>
     * @exception IllegalArgumentException Si les param�tres sont vides ou null
     * @exception AFTechnicalException Si le comptage des assur� a lanc� une exception
     */
    public static int countNbrAssuresByCantonForDeclararion(BSession session, String codeCanton, String idDeclaration) {

        int nbrAssures = 0;

        if (session == null) {
            throw new NullPointerException("La session doit �tre non null");
        }

        if (JadeStringUtil.isBlankOrZero(codeCanton)) {
            throw new IllegalArgumentException("Le code du canton doit �tre pr�cis�" + codeCanton);
        }

        if (JadeStringUtil.isBlankOrZero(idDeclaration)) {
            throw new IllegalArgumentException("L'id d'un d�claration doit �tre pr�cis� : " + idDeclaration);
        }

        DSCountNbrEmployesByCantonForDeclarationManager manager = new DSCountNbrEmployesByCantonForDeclarationManager();
        manager.setForCodeCanton(codeCanton);
        manager.setForIdDeclaration(idDeclaration);
        manager.setSession(session);

        try {
            nbrAssures = manager.getCount();
        } catch (Exception e) {
            throw new DSTechnicalException(
                    "Impossible de compter le nombre d'assur� pour la d�claration (idDeclaration = " + idDeclaration
                            + " / codeCanton = " + codeCanton, e);
        }

        return nbrAssures;
    }
}
