package ch.globaz.al.business.services.paiement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import java.util.HashMap;
import ch.globaz.al.business.paiement.PaiementBusinessModel;

public interface ProtocoleCSVPaiementDirect extends JadeApplicationService {

    /**
     * Génère une liste des paiement direct pour une liste d'allocataires
     * 
     * @param listePaiement
     *            liste des paiement à afficher dans la liste
     * @return contenu du protocole sous forme d'une chaîne CSV
     * @throws JadeApplicationException
     */
    public String getCSVListeAllocataire(Collection<PaiementBusinessModel> listePaiement, HashMap<String, String> params)
            throws JadeApplicationException;

    /**
     * Génère le contenu du protocole CSV des paiements direct. Seuls les comptes annexes dont le montant au crédit
     * dépasse la limite passée en paramètre sont listés.
     * 
     * @param listeRecap
     *            liste contenant des instances de
     *            {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel}
     * @param limite
     *            Limite au-delà de laquelles les prestations doivent être listées
     * 
     * @return Contenu du protocole CSV
     * @throws JadeApplicationException
     */
    public String getCSVMontantSuperieurALimite(Collection<PaiementBusinessModel> listeRecap, String limite,
            HashMap<String, String> params) throws JadeApplicationException;
}
