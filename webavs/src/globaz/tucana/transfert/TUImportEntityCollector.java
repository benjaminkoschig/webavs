package globaz.tucana.transfert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Représentation d'une entité avec ses enfants et son parent
 * 
 * @author fgo
 * 
 */
public class TUImportEntityCollector {
    private TUImportEntity entity = null;

    private Collection listEntities = null;

    private TUImportEntityCollector parentCollector = null;

    /**
     * Constructeur en passant l'entité, qui représente une entité sans père ou donc une entité du plus haut niveau
     * 
     * @param _entity
     */
    public TUImportEntityCollector(TUImportEntity _entity) {
        entity = _entity;
        listEntities = new ArrayList();
    }

    /**
     * Constructeur en passant en paramètre l'entité père et l'entité proprement-dite
     * 
     * @param _parentCollector
     * @param _entity
     */
    public TUImportEntityCollector(TUImportEntityCollector _parentCollector, TUImportEntity _entity) {
        entity = _entity;
        parentCollector = _parentCollector;
        listEntities = new ArrayList();
    }

    /**
     * Ajoute une entité à la liste des entités filles
     * 
     * @param _entity
     */
    public void addEntity(TUImportEntityCollector _entity) {
        listEntities.add(_entity);
    }

    /**
     * Récupère l'entité
     * 
     * @return
     */
    public TUImportEntity getEntity() {
        return entity;
    }

    /**
     * Récupère la liste des entités filles
     * 
     * @return
     */
    public Collection getListEntities() {
        return listEntities;
    }

    /**
     * Récupère le TUImportEntityCollector parent
     * 
     * @return
     */
    public TUImportEntityCollector getParentCollector() {
        return parentCollector;
    }

    /**
     * Retourne vrai s'il existe un TUImportEntityCollector parent
     * 
     * @return
     */
    public boolean hasParentCollector() {
        return parentCollector != null;
    }

}
