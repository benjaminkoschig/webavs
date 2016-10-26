package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.print.CO01RequisitionPoursuite;
import globaz.aquila.process.COProcessContentieux;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;

import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 05-nov-2004
 * @TODO Amende de mauvais payeur en fonction du nombre de factures au contentieux ???
 */
public class CO005ExecuterRDP extends COAbstractEnvoyerDocument {
    private boolean traitementSpecifique = false;

    /**
     * @return the traitementSpecifique
     */
    public boolean isTraitementSpecifique() {
        return traitementSpecifique;
    }

    /**
     * @param traitementSpecifique the traitementSpecifique to set
     */
    public void setTraitementSpecifique(boolean traitementSpecifique) {
        this.traitementSpecifique = traitementSpecifique;
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        try {
            CO01RequisitionPoursuite rdp = new CO01RequisitionPoursuite(transaction.getSession());

            if ((getParent() == null) || !(getParent() instanceof COProcessContentieux)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rdp.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rdp.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            if (contentieux.isNew()) {
                rdp.setNouveauContentieux(Boolean.TRUE);
                rdp.addContentieuxPrevisionnel(contentieux);
            } else {
                rdp.addContentieux(contentieux);
            }
            rdp.setTaxes(taxes);

            if (!isTraitementSpecifique() && rdp.isNouveauRegime(transaction.getSession(), getDateExecution())) {
                // Redonne le canton de l office destinataire
                String canton = giveCantonDestinataire(transaction.getSession(), contentieux);
                if (CORequisitionPoursuiteUtil.isOfficeDontWantToUseNewRegime(transaction.getSession(), canton) == false) {
                    
                    List<CAInteretManuelVisualComponent> listInteret = giveDecisionIM(transaction, contentieux, getDateExecution());
                    setInteretCalcule(listInteret);
                    
                    if (listInteret.size() > 0) {
                        FWCurrency montantIM = new FWCurrency(0);
                        for (CAInteretManuelVisualComponent interet : listInteret) {
                            montantIM.add(interet.montantInteretTotalCalcule());
                        }
//                        contentieux.setMontantInteretsCalcule(montantIM.toStringFormat());
                        rdp.setMontantTotalIM(montantIM.toString());
                    }
                }
            }

            this._envoyerDocument(contentieux, rdp);
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
        _validerCasPourEnvoyerPoursuite(contentieux);
    }

}
