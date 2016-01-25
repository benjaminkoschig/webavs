/*
 * Créé le 20 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COEtapeInfoConfigManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO01AImputerVersement;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CO099ImputerVersementPartiel extends COAbstractEnvoyerDocument {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annexeTexteLibre;
    private String dateVersement;
    private String montantImputation;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      java.util.List, globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        try {
            CO01AImputerVersement rcp = new CO01AImputerVersement(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rcp.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rcp.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rcp.addContentieux(contentieux);
            rcp.setTaxes(taxes);
            rcp.setMontantImputation(montantImputation);
            rcp.setDateVersement(dateVersement);
            rcp.setAnnexe(annexeTexteLibre);
            this._envoyerDocument(contentieux, rcp, false);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_annuler(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.aquila.db.access.poursuite.COHistorique, globaz.globall.db.BTransaction)
     */
    // protected void _annuler(COContentieux contentieux, COHistorique
    // historique, BTransaction transaction) throws COTransitionException {
    // }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        // si le paramètrage est correct, le montant à imputer est requis, on
        // verifie quand meme.
        loadMontantImputation(transaction, contentieux);
        loadDateVersement(transaction, contentieux);
        if (JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(montantImputation))) {
            throw new COTransitionException("AQUILA_MONTANT_IMPUTATION_VIDE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_MONTANT_IMPUTATION_VIDE"));
        }
    }

    /**
     * redéfinie pour empêcher la modification de l'état du contentieux.
     * 
     * @see COTransitionAction#effectuerTransition(COContentieux, COTransition)
     */
    @Override
    protected void effectuerTransition(COContentieux contentieux, COTransition transition) throws COTransitionException {
        // HACK: UNE IMPUTATION SE COMPORTE COMME SI AUCUNE TRANSITION N'AVAIT
        // ETE EFFECTUEE !!!
        contentieux.setDateDeclenchement(contentieux.getProchaineDateDeclenchement());
        contentieux.setDateExecution(getDateExecution());
    }

    public String getAnnexeTexteLibre() {
        return annexeTexteLibre;
    }

    /**
     * La valeur courante de la propriété.
     * 
     * @return La valeur courante de la propriété
     */
    // @Override
    // public String getMotif() {
    // // Bug 6681
    // String motifSaisi = "";
    // if (!JadeStringUtil.isBlank(super.getMotif())) {
    // motifSaisi = " : " + super.getMotif();
    // }
    // return this.getTransition().getEtapeSuivante().getLibEtapeLibelle() + motifSaisi;
    // }

    private String loadDateVersement(BTransaction transaction, COContentieux contentieux) throws COTransitionException {
        if (dateVersement == null) {
            // charger la configuration
            COEtapeInfoConfigManager etapeInfoConfigManager = new COEtapeInfoConfigManager();

            etapeInfoConfigManager.setSession(transaction.getSession());
            etapeInfoConfigManager.setForCsLibelle(COEtapeInfoConfig.CS_DATE_VERSEMENT);
            etapeInfoConfigManager.setForLibSequence(contentieux.getSequence().getLibSequence());

            try {
                etapeInfoConfigManager.find();
            } catch (Exception e) {
                throw new COTransitionException("AQUILA_IMPOSSIBLE_TROUVER_INFOS_ETAPE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_IMPOSSIBLE_TROUVER_INFOS_ETAPE"));
            }

            if (etapeInfoConfigManager.isEmpty()) {
                throw new COTransitionException("AQUILA_INFO_IMPUTER_MAL_PARAMETREE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_INFO_IMPUTER_MAL_PARAMETREE"));
            }

            // retrouver l'info
            COEtapeInfoConfig etapeInfoConfig = (COEtapeInfoConfig) etapeInfoConfigManager.get(0);

            dateVersement = etapeInfos.get(etapeInfoConfig.getIdEtapeInfoConfig());
        }

        return dateVersement;
    }

    private String loadMontantImputation(BTransaction transaction, COContentieux contentieux)
            throws COTransitionException {
        if (montantImputation == null) {
            // charger la configuration
            COEtapeInfoConfigManager etapeInfoConfigManager = new COEtapeInfoConfigManager();

            etapeInfoConfigManager.setSession(transaction.getSession());
            etapeInfoConfigManager.setForCsLibelle(COEtapeInfoConfig.CS_MONTANT_IMPUTER);
            etapeInfoConfigManager.setForLibSequence(contentieux.getSequence().getLibSequence());

            try {
                etapeInfoConfigManager.find();
            } catch (Exception e) {
                throw new COTransitionException("AQUILA_IMPOSSIBLE_TROUVER_INFOS_ETAPE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_IMPOSSIBLE_TROUVER_INFOS_ETAPE"));
            }

            if (etapeInfoConfigManager.isEmpty()) {
                throw new COTransitionException("AQUILA_INFO_IMPUTER_MAL_PARAMETREE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_INFO_IMPUTER_MAL_PARAMETREE"));
            }

            // retrouver l'info
            COEtapeInfoConfig etapeInfoConfig = (COEtapeInfoConfig) etapeInfoConfigManager.get(0);

            montantImputation = etapeInfos.get(etapeInfoConfig.getIdEtapeInfoConfig());
        }

        return montantImputation;
    }

    public void setAnnexeTexteLibre(String annexeTexteLibre) {
        this.annexeTexteLibre = annexeTexteLibre;
    }

}
