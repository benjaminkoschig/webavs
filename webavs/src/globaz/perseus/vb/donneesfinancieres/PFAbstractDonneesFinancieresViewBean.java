/**
 * 
 */
package globaz.perseus.vb.donneesfinancieres;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public abstract class PFAbstractDonneesFinancieresViewBean extends BJadePersistentObjectViewBean {

    protected Demande demande = null;
    protected List<DepenseReconnue> depensesReconnues = null;
    protected List<Dette> dettes = null;
    protected List<Fortune> fortunes = null;
    protected MembreFamille membreFamille = null;
    protected List<Revenu> revenus = null;

    public PFAbstractDonneesFinancieresViewBean() {
        super();
        revenus = new ArrayList<Revenu>();
        dettes = new ArrayList<Dette>();
        fortunes = new ArrayList<Fortune>();
        depensesReconnues = new ArrayList<DepenseReconnue>();

    }

    @Override
    public void add() throws Exception {
        // Le formulaire ne s'affiche qu'en affichage ou modification
    }

    @Override
    public void delete() throws Exception {
        // Le formulaire ne s'affiche qu'en affichage ou modification
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    private void setIdsDonneesFinancieres(DonneeFinanciere df) {
        df.setDemande(demande);
        df.setMembreFamille(membreFamille);
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    @Override
    public void update() throws Exception {
        // Parcours de tous les revenus pour les mettre à jour dans la BD
        for (Revenu revenu : revenus) {
            if (revenu.isNew()) {
                if (!JadeStringUtil.isEmpty(revenu.getSimpleDonneeFinanciere().getValeur())) {
                    setIdsDonneesFinancieres(revenu);
                    PerseusServiceLocator.getRevenuService().create(revenu);
                }
            } else {
                if (JadeStringUtil.isEmpty(revenu.getSimpleDonneeFinanciere().getValeur())) {
                    PerseusServiceLocator.getRevenuService().delete(revenu);
                } else {
                    PerseusServiceLocator.getRevenuService().update(revenu);
                }
            }
        }
        // Parcours de toutes les dettes pour les mettre à jour dans la BD
        for (Dette dette : dettes) {
            if (dette.isNew()) {
                if (!JadeStringUtil.isEmpty(dette.getSimpleDonneeFinanciere().getValeur())) {
                    setIdsDonneesFinancieres(dette);
                    PerseusServiceLocator.getDetteService().create(dette);
                }
            } else {
                if (JadeStringUtil.isEmpty(dette.getSimpleDonneeFinanciere().getValeur())) {
                    PerseusServiceLocator.getDetteService().delete(dette);
                } else {
                    PerseusServiceLocator.getDetteService().update(dette);
                }
            }
        }
        // Parcours de toutes les fortunes pour les mettre à jour dans la BD
        for (Fortune fortune : fortunes) {
            if (fortune.isNew()) {
                if (!JadeStringUtil.isEmpty(fortune.getSimpleDonneeFinanciere().getValeur())) {
                    setIdsDonneesFinancieres(fortune);
                    PerseusServiceLocator.getFortuneService().create(fortune);
                }
            } else {
                if (JadeStringUtil.isEmpty(fortune.getSimpleDonneeFinanciere().getValeur())) {
                    PerseusServiceLocator.getFortuneService().delete(fortune);
                } else {
                    PerseusServiceLocator.getFortuneService().update(fortune);
                }
            }
        }
        // Parcours de toutes les depenseReconnues pour les mettre à jour dans la BD
        for (DepenseReconnue depenseReconnue : depensesReconnues) {
            if (depenseReconnue.isNew()) {
                if (!JadeStringUtil.isEmpty(depenseReconnue.getSimpleDonneeFinanciere().getValeur())) {
                    setIdsDonneesFinancieres(depenseReconnue);
                    PerseusServiceLocator.getDepenseReconnueService().create(depenseReconnue);
                }
            } else {
                if (JadeStringUtil.isEmpty(depenseReconnue.getSimpleDonneeFinanciere().getValeur())) {
                    PerseusServiceLocator.getDepenseReconnueService().delete(depenseReconnue);
                } else {
                    PerseusServiceLocator.getDepenseReconnueService().update(depenseReconnue);
                }
            }
        }

        // Faire un clean de la demande
        demande = PerseusServiceLocator.getDemandeService().updateAndClean(demande, false);
    }

}
