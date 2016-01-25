package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO04ReceptionPaiement;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.config.COConfigurationKey;
import globaz.aquila.service.config.COConfigurationService;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.List;

/**
 * <h1>Description</h1>
 * <p>
 * Envoye un document de type formule 44.
 * </p>
 * <p>
 * Le document est envoyée seulement si {@link #_validate(COContentieux, BTransaction) certains critères} sont vérifiés.
 * </p>
 * 
 * @author Pascal Lovy, 17-nov-2004
 */
public class CO008ExecuterFormule44 extends COAbstractEnvoyerDocument {

    /** Méthode d'arrondi. */
    private static final int ARRONDI = BigDecimal.ROUND_HALF_UP;

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final COConfigurationService CONFIG_SERVICE = COServiceLocator.getConfigService();

    private static final int NOMBRE_CHIFFRES_APRES_VIRGULE_POUR_TAUX = 6;

    /** Précision des calculs. */
    private static final int PRECISION = 4;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Génération et envoi du document
        try {
            CO04ReceptionPaiement rp = new CO04ReceptionPaiement(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rp.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rp.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rp.addContentieux(contentieux);
            rp.setTaxes(taxes);
            rp.setInteretCalcule(getInteretCalcule());
            this._envoyerDocument(contentieux, rp);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        super._validate(contentieux, transaction);
        // Test des préconditions
        _validerSolde(contentieux);
        _validerEcheance(contentieux);

        BigDecimal creance = new BigDecimal(contentieux.getMontantInitial());
        BigDecimal solde = new BigDecimal(contentieux.getSolde());

        // solde <= 25% de la créance
        BigDecimal rapport = getOption(contentieux.getSession(), COConfigurationService.TAUX_FORMULE_44);
        if (new BigDecimal(0).compareTo(rapport) == 0) {
        } else {
            if ((creance.signum() > 0)
                    && (solde.divide(creance, CO008ExecuterFormule44.PRECISION, CO008ExecuterFormule44.ARRONDI)
                            .compareTo(rapport) > 0)) {
                throw new COTransitionException("AQUILA_ERR_CO008_SOLDE_SUP_RAPPORT", COActionUtils.getMessage(
                        contentieux.getSession(),
                        "",
                        new Object[] { rapport
                                .multiply(new BigDecimal(100.0))
                                .setScale(CO008ExecuterFormule44.NOMBRE_CHIFFRES_APRES_VIRGULE_POUR_TAUX,
                                        BigDecimal.ROUND_HALF_DOWN).toString() }));
            }
        }
        // solde <= 200CHF
        BigDecimal seuil = getOption(contentieux.getSession(), COConfigurationService.MONTANT_FORMULE_44);
        if (new BigDecimal(0).compareTo(seuil) == 0) {
        } else {
            if (solde.compareTo(seuil) > 0) {
                throw new COTransitionException("AQUILA_ERR_CO008_SOLDE_SUP_SEUIL",
                        COActionUtils.getMessage(contentieux.getSession(), "AQUILA_ERR_CO008_SOLDE_SUP_SEUIL",
                                new Object[] { seuil.toString() }));
            }
        }
    }

    protected BigDecimal getOption(BSession session, COConfigurationKey key) throws COTransitionException {
        try {
            return new BigDecimal(CO008ExecuterFormule44.CONFIG_SERVICE.getOption(session, key).doubleValue());
        } catch (Exception e) {
            throw new COTransitionException("AQUILA_OPTION_CONFIG_ERREUR", COActionUtils.getMessage(session,
                    "AQUILA_OPTION_CONFIG_ERREUR"));
        }
    }
}
