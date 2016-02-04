package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.perseus.process.facture.FactureWrapper;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.exceptions.models.facture.FactureException;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.qd.FactureSearchModel;

/**
 * Interface exposant les services disponibles pour une facture.
 * 
 * @author JSI
 * 
 */
public interface FactureService extends JadeApplicationService {

    /**
     * Compte le nombre de factures par rapport � un mod�le de recherche.
     * 
     * @param search
     * @return nombre de factures
     * @throws FactureException
     * @throws JadePersistenceException
     */
    public int count(FactureSearchModel search) throws FactureException, JadePersistenceException;

    /**
     * Cr�e une facture
     * 
     * @param facture
     * @return la facture cr��e
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public Facture create(Facture facture) throws JadePersistenceException, FactureException;

    /**
     * Cr�e une facture, avec le boolean pour forcer l'acceptation et �viter tous les contr�les.
     * 
     * @param facture
     * @param forcerAcceptation
     * @return la facture accept�e
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public Facture create(Facture facture, boolean forcerAcceptation) throws JadePersistenceException, FactureException;

    /**
     * Efface une facture
     * 
     * @param facture
     * @return la facture effac�e
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public Facture delete(Facture facture) throws JadePersistenceException, FactureException;

    /**
     * Cherche les informations n�cessaire afin d'indiquer � l'utilisateur le nombre de facture et le nombres de minutes
     * totaux de ceux ci qui ont �t� de type hygieniste dentaire pour le membre de la famille pour l'ann�e de la
     * nouvelle facture (etat : enregistr� et valid�).
     * 
     * @param dateFacture
     *            Date de la facture
     * @param idMembreFamille
     *            L'id du membre de la famille
     * @return Nb de factures et Nb de minutes totaux
     */
    public Map<String, String> getInformationsAboutHygienisteDentaire(String datePriseEnCharge, String dateFacture,
            String idMembreFamille) throws JadePersistenceException;

    /**
     * Lit une facture, si elle est en base de donn�e et la retourne
     * 
     * @param idFacture
     * @return la facture � lire
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public Facture read(String idFacture) throws JadePersistenceException, FactureException;

    /**
     * Permet de demander la restitution d'une facture lorsque celle-ci a �t� comptabilis�e
     * 
     * @param facture
     * @return
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public Facture restituer(Facture facture) throws JadePersistenceException, FactureException;

    /**
     * TODO JavaDoc
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public FactureSearchModel search(FactureSearchModel searchModel) throws JadePersistenceException, FactureException;

    /**
     * Met � jour une facture
     * 
     * @param facture
     * @return la facture mise � jour
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public Facture update(Facture facture) throws JadePersistenceException, FactureException;

    /**
     * Permet de valider la facture, cette action comptabilise la facture dans un lot de type facture ouvert (si aucun
     * n'est ouvert un lot est cr��)
     * 
     * @param facture
     * @return
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public Facture valider(Facture facture) throws JadePersistenceException, FactureException;

    /**
     * Permet de valider plusieurs factures d'un seul clic, fait appel a la validation individuelle pour comptabiliser
     * dans un lot
     * 
     * @param factures
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public List<FactureWrapper> validerMultiple(List<String> idFactures) throws JadePersistenceException,
            FactureException;

}
