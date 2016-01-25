package ch.globaz.vulpecula.facturation;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.external.IntRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.hercule.InteretsMoratoires;

/**
 * BProcess devant être implémenté lorsque des étapes de facturation sont implémentées (génération, comptabilisation...)
 * 
 */
public abstract class PTProcessFacturation extends BProcessWithContext {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PTProcessFacturation.class);

    private String idModuleFacturation;
    private FAPassage passage;

    public PTProcessFacturation() {
        super();
    }

    public PTProcessFacturation(final BProcess parent) {
        super(parent);
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public FAPassage getPassage() {
        return passage;
    }

    public String getIdPassage() {
        return passage.getId();
    }

    /**
     * Nettoyage du processus après exécution ou en cas d'échec.
     */
    protected abstract void clean();

    /**
     * Implémentation du corps du processus.
     * 
     * @return boolean définit si le processus s'est exécuté avec succès
     */
    protected abstract boolean launch();

    /**
     * Retourne l'id de l'adresse de paiement à utiliser dans les afacts.
     * Peut être overridé afin de changer l'adresse de paiement
     */
    protected String getIdAdressePaiement(String idTiers) {
        return null;
    }

    /**
     * Nettoyage de BProcess, soit la fermeture du JadeContext.
     * Si cette méthode est overridé, ne pas oublier d'appeler le super._executeCleanUp()
     */
    @Override
    protected final void _executeCleanUp() {
        clean();
        super._executeCleanUp();
    }

    /**
     * CETTE METHODE NE DOIT PAS ETRE APPELE DIRECTEMENT
     */
    @Override
    protected final boolean _executeProcess() throws Exception {
        super._executeProcess();
        return launch();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    protected FAEnteteFacture createEnteteFacture(String idTiers, String idExterneRole, String numeroSection,
            String idTypeFacture) throws Exception {
        return createEnteteFactureWithInteret(idTiers, idExterneRole, numeroSection, idTypeFacture, null);
    }

    protected FAEnteteFacture createEnteteFactureWithInteret(String idTiers, String idExterneRole,
            String numeroSection, String idTypeFacture, InteretsMoratoires interet) throws Exception {
        try {
            FAEnteteFactureManager enteteFactureManager = rechercheEntete(idTiers, idExterneRole, numeroSection,
                    idTypeFacture);

            // si aucune entete trouvée -> création de l'entete
            if (enteteFactureManager.size() == 0) {
                FAEnteteFacture nouvelleEnteteFacture = creationNouvelleEntete(idTiers, idExterneRole, numeroSection,
                        idTypeFacture, interet);

                // Ajout de l'entête
                nouvelleEnteteFacture.add(getTransaction());

                return nouvelleEnteteFacture;
            } else {
                return (FAEnteteFacture) enteteFactureManager.getFirstEntity();
            }
        } catch (Exception e) {
            throw new Exception("unable to read or create entete facture");
        }
    }

    protected FAEnteteFacture createEnteteFactureWithoutPrinting(String idTiers, String idExterneRole,
            String numeroSection, String idTypeFacture) throws Exception {
        try {
            FAEnteteFactureManager enteteFactureManager = rechercheEntete(idTiers, idExterneRole, numeroSection,
                    idTypeFacture);

            // si aucune entete trouvée -> création de l'entete
            if (enteteFactureManager.size() == 0) {
                FAEnteteFacture nouvelleEnteteFacture = creationNouvelleEntete(idTiers, idExterneRole, numeroSection,
                        idTypeFacture, null);

                nouvelleEnteteFacture.setIdCSModeImpression(FAEnteteFacture.CS_MODE_IMP_NOT_IMPRIMABLE);

                // Ajout de l'entête
                nouvelleEnteteFacture.add(getTransaction());

                return nouvelleEnteteFacture;
            } else {
                return (FAEnteteFacture) enteteFactureManager.getFirstEntity();
            }
        } catch (Exception e) {
            throw new Exception("unable to read or create entete facture");
        }
    }

    private FAEnteteFacture creationNouvelleEntete(String idTiers, String idExterneRole, String numeroSection,
            String idTypeFacture, InteretsMoratoires interet) throws Exception {
        NumeroDecompte numeroDecompte = new NumeroDecompte(numeroSection);
        FAEnteteFacture nouvelleEnteteFacture = new FAEnteteFacture();
        nouvelleEnteteFacture.setSession(getSession());
        nouvelleEnteteFacture.setIdPassage(getPassage().getId());
        nouvelleEnteteFacture.setIdTiers(idTiers);
        nouvelleEnteteFacture.setIdRole(IntRole.ROLE_AFFILIE_PARITAIRE);
        nouvelleEnteteFacture.setIdExterneRole(idExterneRole);
        nouvelleEnteteFacture.setIdTypeFacture(idTypeFacture);
        nouvelleEnteteFacture.setNonImprimable(false);
        nouvelleEnteteFacture.setIdAdressePaiement(getIdAdressePaiement(idTiers));
        if (NumeroDecompte.SPECIAL.equals(numeroDecompte.getCode())
                || NumeroDecompte.CONTROLE_EMPLOYEUR.equals(numeroDecompte.getCode())) {
            nouvelleEnteteFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU);
            nouvelleEnteteFacture.setIdAdressePaiement("");
        }
        nouvelleEnteteFacture.initDefaultPlanValue(IntRole.ROLE_AFFILIE_PARITAIRE);
        if (interet != null) {
            nouvelleEnteteFacture.setIdSoumisInteretsMoratoires(interet.getValue());
        }
        // création du numéro de section unique
        nouvelleEnteteFacture.setIdExterneFacture(numeroSection);
        return nouvelleEnteteFacture;
    }

    private FAEnteteFactureManager rechercheEntete(String idTiers, String idExterneRole, String numeroSection,
            String idTypeFacture) throws Exception {
        // Recherche de l'entête facture
        FAEnteteFactureManager enteteFactureManager = new FAEnteteFactureManager();
        enteteFactureManager.setSession(getSession());
        enteteFactureManager.setForIdPassage(getPassage().getId());
        enteteFactureManager.setForIdTiers(idTiers);
        enteteFactureManager.setForIdExterneRole(idExterneRole);
        enteteFactureManager.setForIdTypeFacture(idTypeFacture);
        enteteFactureManager.setForIdExterneFacture(numeroSection);
        enteteFactureManager.find(getTransaction());
        return enteteFactureManager;
    }

    /**
     * @param enteteFacture
     * @param idModuleFacturation
     * @return
     */
    protected FAAfact initAfact(final String idEnteteFacture, String module) {
        if (idModuleFacturation == null || idModuleFacturation.length() == 0) {
            // Recherche du module de facturation des décomptes
            idModuleFacturation = ServicesFacturation.getIdModFacturationByType(getSession(), getTransaction(), module);
        }

        FAAfact afact = new FAAfact();
        afact.setISession(getSession());
        afact.setIdEnteteFacture(idEnteteFacture);
        afact.setIdPassage(getPassage().getId());
        afact.setIdModuleFacturation(idModuleFacturation);
        afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        afact.setNonImprimable(Boolean.FALSE);
        afact.setNonComptabilisable(Boolean.FALSE);
        afact.setAQuittancer(Boolean.FALSE);
        afact.setAffichtaux(true);
        return afact;
    }

    /**
     * Retourne si l'Afact peut être ajouté.
     * Dans le cas d'une cotisation avec masse, il faut qu'une masse et un taux soit présents.
     * Dans tous les cas le montant facturé doit être différent de zéro.
     * 
     * @param montant masse
     * @param rubrique
     * @param montantFacture
     * @return boolean est-ce qu'il faut ajouter ou non l'afact
     */
    protected boolean isAfactAAjouter(Montant masse, final CARubrique rubrique, final Montant montantFacture) {
        if (rubrique.getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE) && masse.isZero()) {
            return false;
        }

        if (montantFacture == null || montantFacture.isZero()) {
            return false;
        }

        return true;
    }

    /**
     * Effacer les afacts générés par le module
     * 
     * @return boolean
     */
    public boolean deleteEnteteEtAfactForIdPassage(String idPassage) {
        if (idPassage == null || idPassage.length() == 0 || idPassage.equals("0")) {
            return false;
        }

        // rapatrier les afacts
        FAAfactManager afactManager = new FAAfactManager();
        afactManager.setSession(getSession());
        afactManager.setForIdPassage(idPassage);

        // Recherche de l'entête facture
        FAEnteteFactureManager enteteFactureManager = new FAEnteteFactureManager();
        enteteFactureManager.setSession(getSession());
        enteteFactureManager.setForIdPassage(idPassage);

        BStatement statement = null;
        try {
            statement = afactManager.cursorOpen(getTransaction());
            FAAfact afact = null;

            // parcourir les afacts à supprimer
            while ((afact = (FAAfact) afactManager.cursorReadNext(statement)) != null) {
                afact.delete(getTransaction());
                if (afact.hasErrors()) {
                    getMemoryLog().logMessage("Impossible de supprimer l'afact: " + afact.getIdAfact(),
                            FWMessage.AVERTISSEMENT, this.getClass().getName());
                }
            }

            statement = enteteFactureManager.cursorOpen(getTransaction());
            FAEnteteFacture entete = null;
            // parcourir les entete à supprimer
            while ((entete = (FAEnteteFacture) enteteFactureManager.cursorReadNext(statement)) != null) {
                entete.delete(getTransaction());
                if (entete.hasErrors()) {
                    getMemoryLog().logMessage("Impossible de supprimer l'entete: " + entete.getIdEntete(),
                            FWMessage.AVERTISSEMENT, this.getClass().getName());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        } finally {
            try {
                // dans tous les cas fermer le cursor
                afactManager.cursorClose(statement);
                enteteFactureManager.cursorClose(statement);
                statement = null;
            } catch (Exception e) {
                LOGGER.error(e.toString());
            }
        }
        return !isAborted();
    }
}
