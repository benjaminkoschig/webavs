/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.globall.db.BSession;
import java.util.List;
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
            throw new IllegalArgumentException("Session must be not null");
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

        if (ligneDeblocage == null) {
            throw new IllegalArgumentException("To update ligneDeblocage, ligneDeblocage must be not null");
        }

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

        if (ligneDeblocage == null) {
            throw new IllegalArgumentException("To delete ligneDeblocage, ligneDeblocage must be not null");
        }

        if (ligneDeblocage.isNew()) {
            throw new IllegalArgumentException("To delete ligneDeblocage, ligneDeblocage must be not new");
        }

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

        if (ligneDeblocage == null) {
            throw new IllegalArgumentException("To update ligneDeblocage, ligneDeblocage must be not null");
        }

        if (ligneDeblocage.isNew()) {
            throw new IllegalArgumentException("To update ligneDeblocage, ligneDeblocage must be not new");
        }

        ligneDeblocage.setSession(session);
        try {
            ligneDeblocage.update();
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to add ligne de déblocage", e);
        }

        return ligneDeblocage;
    }

    /**
     * Recherche de toutes les lignes de déblocage de l'id de la prestation rente.
     * 
     * @param idRentePrestation Un id rente prestation
     * @return
     */
    public RELigneDeblocages searchByIdRente(Integer forIdRentePrestation) {

        if (forIdRentePrestation == null) {
            throw new IllegalArgumentException("To perform a search by id rente, idRentePrestation must be not null");
        }

        RELigneDeblocageManager manager = new RELigneDeblocageManager();
        manager.setSession(session);
        manager.setForIdRentePrestation(forIdRentePrestation);

        List<RELigneDeblocage> entities = manager.search();

        RELigneDeblocages lignesDeblocages = new RELigneDeblocages();
        lignesDeblocages.addAll(entities);

        return lignesDeblocages;
    }

    /**
     * Recherche de toutes les lignes de déblocage par l'état.
     * 
     * @param forEtat Un état de déblocage
     * @return
     */
    public RELigneDeblocages searchByEtat(RELigneDeblocageEtat forEtat) {

        if (forEtat == null) {
            throw new IllegalArgumentException("To perform a search by etat, etat must be not null");
        }

        RELigneDeblocageManager manager = new RELigneDeblocageManager();
        manager.setSession(session);
        manager.setForEtat(forEtat);

        List<RELigneDeblocage> entities = manager.search();

        RELigneDeblocages lignesDeblocages = new RELigneDeblocages();
        lignesDeblocages.addAll(entities);

        return lignesDeblocages;
    }

    /**
     * Recherche de toutes les lignes de déblocage par le type
     * 
     * @param fortype Un type de déblocage
     * @return
     */
    public RELigneDeblocages searchByType(RELigneDeblocageType fortype) {

        if (fortype == null) {
            throw new IllegalArgumentException("To perform a search by type, type must be not null");
        }

        RELigneDeblocageManager manager = new RELigneDeblocageManager();
        manager.setSession(session);
        manager.setForType(fortype);

        List<RELigneDeblocage> entities = manager.search();

        RELigneDeblocages lignesDeblocages = new RELigneDeblocages();
        lignesDeblocages.addAll(entities);

        return lignesDeblocages;
    }

}
