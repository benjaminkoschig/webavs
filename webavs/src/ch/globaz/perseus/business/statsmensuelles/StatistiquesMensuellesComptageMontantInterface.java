package ch.globaz.perseus.business.statsmensuelles;

import ch.globaz.perseus.business.models.demande.SimpleDemande;

/**
 * Interface proposant un contrat entre les classes contenant des montant à prendre
 * en compte pour les statistiques et celles créées pour les compter.
 * 
 * @author rco
 * 
 */
public interface StatistiquesMensuellesComptageMontantInterface {
    String getMontant();

    void setMontant(String montant);

    SimpleDemande getDemande();

    void setDemande(SimpleDemande demande);
}
