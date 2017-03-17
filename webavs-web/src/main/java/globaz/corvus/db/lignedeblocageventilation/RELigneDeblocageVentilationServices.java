/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocageventilation;

import globaz.globall.db.BSession;
import ch.globaz.common.jadedb.exception.JadeDataBaseException;

/**
 * Service d'acces aux donn�es de ventilations des lignes de d�blocage
 * 
 * @author sco
 * 
 */
public class RELigneDeblocageVentilationServices {
    private BSession session;

    public RELigneDeblocageVentilationServices(BSession session) {

        if (session == null) {
            throw new IllegalArgumentException("Session must be not null");
        }

        this.session = session;
    }

    /**
     * Ajout d'une ventilation d'une ligne de d�blocage
     * 
     * @param ligneDeblocage Une ligne de d�blocage
     * @return La ligne de d�blocage modifi�e
     */
    public RELigneDeblocageVentilation add(RELigneDeblocageVentilation ligneDeblocageVentilation) {

        if (ligneDeblocageVentilation == null) {
            throw new IllegalArgumentException(
                    "To add ligneDeblocageVentilation, ligneDeblocageVentilation must be not null");
        }

        ligneDeblocageVentilation.setSession(session);
        try {
            ligneDeblocageVentilation.add();
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to add ventilation ligne de d�blocage", e);
        }

        return ligneDeblocageVentilation;
    }

    /**
     * Suppression d'une ventilation d'une ligne de d�blocage
     * 
     * @param ligneDeblocage Une ligne de d�blocage
     */
    public void delete(RELigneDeblocageVentilation ligneDeblocageVentilation) {

        if (ligneDeblocageVentilation == null) {
            throw new IllegalArgumentException(
                    "To delete ligneDeblocageVentilation, ligneDeblocageVentilation must be not null");
        }

        if (ligneDeblocageVentilation.isNew()) {
            throw new IllegalArgumentException(
                    "To delete ligneDeblocageVentilation, ligneDeblocageVentilation must be not new");
        }

        ligneDeblocageVentilation.setSession(session);

        try {
            ligneDeblocageVentilation.delete();
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to delete ventilation ligne de d�blocage", e);
        }
    }

    /**
     * Update d'une ventilation d'une ligne de d�blocage
     * 
     * @param ligneDeblocage Une ligne de d�blocage
     * @return La ligne de d�blocage modifi�e
     */
    public RELigneDeblocageVentilation update(RELigneDeblocageVentilation ligneDeblocageVentilation) {

        if (ligneDeblocageVentilation == null) {
            throw new IllegalArgumentException(
                    "To update ligneDeblocageVentilation, ligneDeblocageVentilation must be not null");
        }

        if (ligneDeblocageVentilation.isNew()) {
            throw new IllegalArgumentException(
                    "To update ligneDeblocageVentilation, ligneDeblocageVentilation must be not new");
        }

        ligneDeblocageVentilation.setSession(session);
        try {
            ligneDeblocageVentilation.update();
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to update ventilation ligne de d�blocage", e);
        }

        return ligneDeblocageVentilation;
    }
}
