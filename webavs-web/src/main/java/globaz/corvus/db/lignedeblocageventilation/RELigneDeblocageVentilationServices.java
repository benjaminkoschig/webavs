/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocageventilation;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import java.util.List;
import java.util.Set;
import ch.globaz.common.jadedb.exception.JadeDataBaseException;
import com.google.common.base.Joiner;

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

    /**
     * Permet de rechercher les lignes de ventilation avec les ids des lignes de d�blocage
     * 
     * @param ids
     * @return List<RELigneDeblocageVentilation>
     */
    public List<RELigneDeblocageVentilation> searchByIdsLigneDeblocage(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException(
                    "To searchByIdsLigneDeblocage ligneDeblocageVentilation, ids must be not null or empty");
        }

        RELigneDeblocageVentilationManager manager = new RELigneDeblocageVentilationManager();
        manager.setForIdsLigneDeblocage(ids);
        manager.setSession(session);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new JadeDataBaseException(
                    "Unabled to search ventilation ligne de d�blocage with this idsLigneDeblocage: "
                            + Joiner.on(",").join(ids), e);

        }
        return manager.toList();
    }

    /**
     * Permet de rechercher les lignes de ventilation avec l id section
     * 
     * @param id
     * @return List<RELigneDeblocageVentilation>
     */
    public List<RELigneDeblocageVentilation> searchByIdSection(String idSection) {
        if (idSection == null || idSection.isEmpty()) {
            throw new IllegalArgumentException(
                    "To searchByIdSection ligneDeblocageVentilation, idSection must be not null or empty");
        }

        RELigneDeblocageVentilationManager manager = new RELigneDeblocageVentilationManager();
        manager.setForIdSection(idSection);
        manager.setSession(session);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled to search ventilation ligne de d�blocage with this idSection: "
                    + idSection, e);

        }
        return manager.toList();
    }

    public void save(List<RELigneDeblocageVentilation> ventilations) {
        for (RELigneDeblocageVentilation ventilation : ventilations) {
            add(ventilation);
        }
    }

    public void delete(List<RELigneDeblocageVentilation> ventilations) {
        for (RELigneDeblocageVentilation ventilation : ventilations) {
            delete(ventilation);
        }
    }
}
