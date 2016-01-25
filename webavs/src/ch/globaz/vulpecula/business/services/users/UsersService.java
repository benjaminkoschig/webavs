package ch.globaz.vulpecula.business.services.users;

import globaz.globall.db.BSession;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 14 mars 2014
 * 
 */
public interface UsersService {
    /**
     * indique si un user � un niveau de droit (compl�ment codeSecure) suffisant par rapport au niveau de s�curit� sur
     * l'affiliation
     * 
     * @return vrai si le user � le droit >= � la s�curit� de l'affiliation
     */
    public boolean hasRightAccesSecurity(String valeurSecuriteEmployeur);

    public boolean hasRightAccesSecurityForTravailleur(String valeurSecuriteEmployeur);

    boolean hasRightForPrinting(BSession session);
}
