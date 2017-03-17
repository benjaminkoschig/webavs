/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocageventilation;

import globaz.globall.db.BSession;
import ch.globaz.common.jadedb.exception.JadeDataBaseException;

/**
 * Service d'acces aux données de ventilations des lignes de déblocage
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
     * Ajout d'une ventilation d'une ligne de déblocage
     * 
     * @param ligneDeblocage Une ligne de déblocage
     * @return La ligne de déblocage modifiée
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
            throw new JadeDataBaseException("Unabled to add ventilation ligne de déblocage", e);
        }

        return ligneDeblocageVentilation;
    }

    /**
     * Suppression d'une ventilation d'une ligne de déblocage
     * 
     * @param ligneDeblocage Une ligne de déblocage
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
            throw new JadeDataBaseException("Unabled to delete ventilation ligne de déblocage", e);
        }
    }

    /**
     * Update d'une ventilation d'une ligne de déblocage
     * 
     * @param ligneDeblocage Une ligne de déblocage
     * @return La ligne de déblocage modifiée
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
            throw new JadeDataBaseException("Unabled to update ventilation ligne de déblocage", e);
        }

        return ligneDeblocageVentilation;
    }
}
