/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.globall.db.BSession;
import ch.globaz.common.jadedb.exception.JadeDataBaseException;

/**
 * Service d'accés à la BDD aux ligne de déblocage des rentes.
 * 
 * @author sco
 */
public class ReLigneDeclocageServices {

    private BSession session;

    public ReLigneDeclocageServices(BSession session) {

        if (session == null) {
            throw new IllegalArgumentException();
        }

        this.session = session;
    }

    /**
     * Ajout d'une ligne de déblocage
     * 
     * @param ligneDeblocage Une ligne de déblocage
     * @return La ligne de déblocage modifiée
     */
    public RELigneDeblocage add(RELigneDeblocage ligneDeblocage) {

        ligneDeblocage.setSession(session);
        try {
            ligneDeblocage.add();
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to add ligne de déblocage", e);
        }

        return ligneDeblocage;
    }

    /**
     * Suppression d'une ligne de déblocage
     * 
     * @param ligneDeblocage Une ligne de déblocage
     */
    public void delete(RELigneDeblocage ligneDeblocage) {
        ligneDeblocage.setSession(session);

        try {
            ligneDeblocage.delete();
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to delete ligne de déblocage", e);
        }
    }

    /**
     * Update d'une ligne de déblocage
     * 
     * @param ligneDeblocage Une ligne de déblocage
     * @return La ligne de déblocage modifiée
     */
    public RELigneDeblocage update(RELigneDeblocage ligneDeblocage) {

        ligneDeblocage.setSession(session);
        try {
            ligneDeblocage.update();
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to add ligne de déblocage", e);
        }

        return ligneDeblocage;
    }

    public RELigneDeblocages searchByIdRente(String idRente) {
        RELigneDeblocages lignesDeblocages = new RELigneDeblocages();

        return lignesDeblocages;

    }

    public RELigneDeblocages searchByEtat() {

        RELigneDeblocages lignesDeblocages = new RELigneDeblocages();

        return null;
    }

    public RELigneDeblocages searchByEtatGroupByType(RELigneDeblocageEtat etat, RELigneDeblocageType type) {

        RELigneDeblocages lignesDeblocages = new RELigneDeblocages();

        return null;
    }

}
