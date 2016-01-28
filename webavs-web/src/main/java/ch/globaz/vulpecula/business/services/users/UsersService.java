package ch.globaz.vulpecula.business.services.users;

import globaz.globall.db.BSession;

/**
 * @author Arnaud Geiser (AGE) | Créé le 14 mars 2014
 * 
 */
public interface UsersService {
    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public boolean hasRightAccesSecurity(String valeurSecuriteEmployeur);

    public boolean hasRightAccesSecurityForTravailleur(String valeurSecuriteEmployeur);

    boolean hasRightForPrinting(BSession session);
}
