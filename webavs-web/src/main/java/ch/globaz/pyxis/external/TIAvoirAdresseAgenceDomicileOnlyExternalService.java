/*
 * Créé le 12 déc. 05
 */
package ch.globaz.pyxis.external;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.*;
import globaz.pyxis.db.tiers.*;

public class TIAvoirAdresseAgenceDomicileOnlyExternalService extends BAbstractEntityExternalService {
    private static final String TYPE_EXPLOITATION = "508021";
    private static final String TYPE_GROUPE_AGENCE_COMMUNALE = "521002";
    private static final String TYPE_LIEN_AGENCE_COMMUNALE = "507007";
    private static final String TYPE_LIEN_AGENCE_COMMUNALE_PRIVEE = "507008";

    /**
     * Crée le lien entre le tiers et l'agence communale à partir de la relation d'adresse
     * 
     * @param avAdr
     * @param idTiersAgence
     */
    private void creeLien(TIAvoirAdresse avAdr, String idTiersAgence) throws Exception {
        TICompositionTiers lien = new TICompositionTiers();
        lien.setSession(avAdr.getSession());
        if (TYPE_EXPLOITATION.equalsIgnoreCase(avAdr.getTypeAdresse())) {
            lien.setTypeLien(TYPE_LIEN_AGENCE_COMMUNALE_PRIVEE);
        } else {
            lien.setTypeLien(TYPE_LIEN_AGENCE_COMMUNALE);
        }
        lien.setIdTiersParent(avAdr.getIdTiers());
        lien.setIdTiersEnfant(idTiersAgence);
        lien.setDebutRelation(avAdr.getDateDebutRelation());
        lien.setFinRelation(avAdr.getDateFinRelation());
        lien.add();
    }

    /**
     * @return
     */
    private TICompositionTiers findLienAgence(TIAvoirAdresse avAdr) throws Exception {
        TICompositionTiersManager lienMgr = new TICompositionTiersManager();
        lienMgr.setSession(avAdr.getSession());
        if (TYPE_EXPLOITATION.equalsIgnoreCase(avAdr.getTypeAdresse())) {
            lienMgr.setForTypeLien(TYPE_LIEN_AGENCE_COMMUNALE_PRIVEE);
        } else {
            lienMgr.setForTypeLien(TYPE_LIEN_AGENCE_COMMUNALE);
        }
        lienMgr.setForIdTiersParent(avAdr.getIdTiers());
        lienMgr.find(BManager.SIZE_NOLIMIT);
        if (lienMgr.size() > 0) {
            return (TICompositionTiers) lienMgr.getFirstEntity();
        }
        return null;
    }

    /**
     * Retrouve l'agence communale à partir de la loclite de l'adresse
     * 
     * @param avAdr
     * @return
     */
    private String getIdTiersAgenceCommunale(TIAvoirAdresse avAdr) throws Exception {

        // recherche le groupe qui contient la localité
        TILocaliteLieeAGroupeManager localiteeLieeMgr = new TILocaliteLieeAGroupeManager();
        localiteeLieeMgr.setSession(avAdr.getSession());

        TIAdresse adr = new TIAdresse();
        adr.setSession(avAdr.getSession());
        adr.setIdAdresseUnique(avAdr.getIdAdresse());
        adr.retrieve();

        localiteeLieeMgr.setForIdLinkedEntity(adr.getIdLocalite());
        localiteeLieeMgr.changeManagerSize(0);
        localiteeLieeMgr.find(BManager.SIZE_NOLIMIT);
        // Charge toutes les agences avant la boucle pour ne faire qu'une seul requête.
        TIAvoirGroupeLocaliteManager avGrMgr = new TIAvoirGroupeLocaliteManager();
        avGrMgr.setSession(avAdr.getSession());
        avGrMgr.setForCsLinkType(TYPE_GROUPE_AGENCE_COMMUNALE); // AGENCE_COMM
        avGrMgr.changeManagerSize(0);
        avGrMgr.find(BManager.SIZE_NOLIMIT);
        for(TILocaliteLieeAGroupe locGr : localiteeLieeMgr.<TILocaliteLieeAGroupe>toList()) {
            // Est-ce que ce groupe a un avoirGroupe avec un type AGENCE_COMM
            for(TIAvoirGroupeLocalite avGr : avGrMgr.<TIAvoirGroupeLocalite>toList()) {
                if (locGr.getIdGroupeLocation().equals(avGr.getIdGroupeLocation())) {
                    return avGr.getIdTiers();
                }
            }
        }
        // Aucune agence trouvée
        return null;
    }

    private void process(BEntity entity) throws Exception {
        TIAvoirAdresse avAdr = (TIAvoirAdresse) entity;
        boolean isBanqueOrAdmin = TITiers.isTiersBanque(avAdr.getSession(), avAdr.getIdTiers())
                || TITiers.isTiersAdministration(avAdr.getSession(), avAdr.getIdTiers());
        if (isBanqueOrAdmin) {
            return; // ce traitement ne concerne ni les banques ni administrations
        }
        if (condition(avAdr)) {
            String idTiersAgence = getIdTiersAgenceCommunale(avAdr);
            if (idTiersAgence == null) {
                // Cherche l'agence par defaut
                TIAdministrationManager admMgr = new TIAdministrationManager();
                admMgr.setSession(avAdr.getSession());
                admMgr.setForCodeAdministration("9999"); // Autre Agence
                admMgr.setForGenreAdministration("509031"); // Agence comm.
                admMgr.find(BManager.SIZE_NOLIMIT);
                if (admMgr.size() > 0) {
                    // Agence par défaut
                    idTiersAgence = ((TIAdministrationViewBean) admMgr.getFirstEntity()).getIdTiers();
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(idTiersAgence)) {
                // si il existe deja un lien pour notre tiers dans les dates données,
                TICompositionTiers lienAgence = findLienAgence(avAdr);
                if (lienAgence != null) {
                    // on le modifie
                    updateLien(avAdr, lienAgence, idTiersAgence);
                } else {
                    creeLien(avAdr, idTiersAgence);
                }
            }
        }
    }

    /**
     * @param avAdr
     * @param lienAgence
     * @param idTiersAgence
     */
    private void updateLien(TIAvoirAdresse avAdr, TICompositionTiers lienAgence, String idTiersAgence)
            throws Exception {
        lienAgence.setSession(avAdr.getSession());
        if (TYPE_EXPLOITATION.equalsIgnoreCase(avAdr.getTypeAdresse())) {
            lienAgence.setTypeLien(TYPE_LIEN_AGENCE_COMMUNALE_PRIVEE);
        } else {
            lienAgence.setTypeLien(TYPE_LIEN_AGENCE_COMMUNALE);
        }
        lienAgence.setIdTiersParent(avAdr.getIdTiers());
        lienAgence.setIdTiersEnfant(idTiersAgence);
        lienAgence.setDebutRelation("");
        lienAgence.setFinRelation("");

        lienAgence.update();
    }

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        process(entity);
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        // not used
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        // not used
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        process(entity);
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        // not used
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // not used
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        // not used
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        // not used
    }

    private boolean condition(TIAvoirAdresse avAdr) {
        boolean result = false;

        if (TYPE_EXPLOITATION.equals(avAdr.getTypeAdresse())
                || ((IConstantes.CS_APPLICATION_DEFAUT.equals(avAdr.getIdApplication()))
                && (IConstantes.CS_AVOIR_ADRESSE_DOMICILE.equals(avAdr.getTypeAdresse())))) {
            return true;
        }
        return result;
    }

    @Override
    public void init(BEntity entity) throws Throwable {
        // not used
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        // not used
    }

}
