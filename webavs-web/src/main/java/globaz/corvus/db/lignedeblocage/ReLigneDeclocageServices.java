/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.AdressePaiement;
import ch.globaz.common.jadedb.exception.JadeDataBaseException;
import ch.globaz.common.services.AdressePaiementLoader;

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
    public RELigneDeblocages searchByIdRente(Long forIdRentePrestation) {

        if (forIdRentePrestation == null) {
            throw new IllegalArgumentException("To perform a search by id rente, idRentePrestation must be not null");
        }

        RELigneDeblocageManager manager = new RELigneDeblocageManager();
        manager.setSession(session);
        manager.setForIdRentePrestation(forIdRentePrestation);

        return search(manager);
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

        return search(manager);
    }

    /**
     * Recherche de toutes les lignes de déblocage par lot
     * 
     * @param idLot L'id du lot
     * @return
     */
    public RELigneDeblocages searchByIdLot(Long idLot) {

        if (idLot == null) {
            throw new IllegalArgumentException("To perform a search by idLot, type must be not null");
        }

        RELigneDeblocageManager manager = new RELigneDeblocageManager();
        manager.setSession(session);
        manager.setForIdLot(idLot);

        return search(manager);
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

        return search(manager);
    }

    public RELigneDeblocage read(String idLigneDeblocage) {

        if (idLigneDeblocage == null) {
            throw new IllegalArgumentException("To perform a read, idLigneDeblocage must be not null");
        }

        RELigneDeblocage ligneDeblocage = new RELigneDeblocage();
        ligneDeblocage.setSession(session);
        ligneDeblocage.setIdEntity(idLigneDeblocage);
        ligneDeblocage.setId(idLigneDeblocage);
        try {
            ligneDeblocage.retrieve();
            return ligneDeblocage;
        } catch (Exception e) {
            throw new JadeDataBaseException("Unabled read the déblocage", e);
        }
    }

    /**
     * Recherche de toutes les lignes de déblocage de l'id de la prestation rente.
     * 
     * @param idRentePrestation Un id rente prestation
     * @return
     */
    public RELigneDeblocages searchByIdRenteAndCompleteInfo(Long forIdRentePrestation, Set<String> idTiers,
            String descriptionTiersBeneficiaire) {
        RELigneDeblocages lignesDeblocages = searchByIdRente(forIdRentePrestation);
        RELigneDeblocages lignesDeblocagesCompleted = new RELigneDeblocages();
        lignesDeblocagesCompleted.addAll(toCreancier(lignesDeblocages.getLigneDeblocageCreancier()));

        RELigneDeblocageDetteHandler deblocageDetteHandler = new RELigneDeblocageDetteHandler(session);
        List<RELigneDeblocageDette> dettes = deblocageDetteHandler.toDette(
                lignesDeblocages.getLigneDeblocageDetteEnCompta(), idTiers);
        lignesDeblocagesCompleted.addAll(dettes);

        RELigneDeblocages versements = toVersement(lignesDeblocages.getLigneDeblocageVersementBeneficiaire(),
                descriptionTiersBeneficiaire);

        lignesDeblocagesCompleted.addAll(versements);

        lignesDeblocagesCompleted.addAll(lignesDeblocages.getLigneDeblocageImpotsSource().toList());
        return lignesDeblocagesCompleted;
    }

    private RELigneDeblocages toVersement(RELigneDeblocages ligneDeblocageVersementBeneficaire,
            String descriptionTiersBeneficiaire) {
        RELigneDeblocages lignes = new RELigneDeblocages();
        for (RELigneDeblocage dbLigne : ligneDeblocageVersementBeneficaire) {
            RELigneDeblocageVersement versement = new RELigneDeblocageVersement();
            versement.setDescriptionTiers(descriptionTiersBeneficiaire);
            addInfoCommun(versement, dbLigne);
            lignes.add(versement);
        }
        return lignes;
    }

    private List<RELigneDeblocageCreancier> toCreancier(RELigneDeblocages lignesDeblocages) {
        List<RELigneDeblocageCreancier> list = new ArrayList<RELigneDeblocageCreancier>();

        Map<Long, Long> map = new HashMap<Long, Long>();
        for (RELigneDeblocage l : lignesDeblocages) {
            map.put(l.getIdTiersAdressePaiement(), l.getIdApplicationAdressePaiement());
        }
        AdressePaiementLoader loader = new AdressePaiementLoader(session);

        Map<Long, AdressePaiement> mapAdresses = loader.searchAdressePaiement(map);

        for (RELigneDeblocage reLigneDeblocage : lignesDeblocages) {
            RELigneDeblocageCreancier ligne = new RELigneDeblocageCreancier();
            ligne.setAdressePaiement(mapAdresses.get(reLigneDeblocage.getIdTiersAdressePaiement().toString()));
            TITiers tiers = loadTiers(reLigneDeblocage.getIdTiersCreancier());
            ligne.setDesignationTiers1(tiers.getDesignation1());
            ligne.setDesignationTiers2(tiers.getDesignation2());
            ligne.setIdTiersAdressePaiement(reLigneDeblocage.getIdTiersAdressePaiement());
            ligne.setIdTiersCreancier(reLigneDeblocage.getIdTiersCreancier());
            addInfoCommun(ligne, reLigneDeblocage);
            ligne.setSpy(reLigneDeblocage.getSpy());
            if (ligne.getIdTiersCreancier() != null && ligne.getIdTiersCreancier() != 0) {
                ligne.setDesignationTiers1(ligne.getDesignationTiers1());
                ligne.setDesignationTiers2(ligne.getDesignationTiers2());
            }
            list.add(ligne);
        }
        return list;
    }

    private void addInfoCommun(RELigneDeblocage newLigne, RELigneDeblocage dbLigne) {
        newLigne.setEtat(dbLigne.getEtat());
        newLigne.setType(dbLigne.getType());
        newLigne.setMontant(dbLigne.getMontant());
        newLigne.setIdRenteAccordee(dbLigne.getIdRenteAccordee());
        newLigne.setIdEntity(dbLigne.getIdEntity());
        newLigne.setIdLot(dbLigne.getIdLot());
        newLigne.setSpy(dbLigne.getSpy());
    }

    private TITiers loadTiers(Long idTiers) {
        TITiersManager manager = new TITiersManager();
        manager.setForIdTiers(String.valueOf(idTiers));
        manager.setSession(session);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
            return (TITiers) manager.getFirstEntity();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load the tier with this id: " + idTiers, e);
        }
    }

    private RELigneDeblocages search(RELigneDeblocageManager manager) {
        List<RELigneDeblocage> entities = manager.search();

        RELigneDeblocages lignesDeblocages = new RELigneDeblocages();
        lignesDeblocages.addAll(entities);

        return lignesDeblocages;
    }

}
