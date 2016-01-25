package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Hashtable;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.QDAnnuelle;
import ch.globaz.perseus.business.models.qd.QDSearchModel;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;

/**
 * Interface exposant les services disponibles pour les QD.
 * 
 * @author JSI
 * 
 */
/**
 * @author DDE
 * 
 */
public interface QDService extends JadeApplicationService {

    /**
     * Permet d'annuler l'utilisation d'un montant pour une facture dans une QD (utilisé lors de la suppression d'une
     * facture)
     * 
     * @param qd
     * @param montant
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public QD annulerMontantUtilise(QD qd, String montant) throws JadePersistenceException, QDException;

    /**
     * Permet de compter le nombre de résultats d'une recherche
     * 
     * @param searchModel
     * @return nombre de résultats
     * @throws JadePersistenceException
     * @throws QDException
     */
    public int count(QDSearchModel searchModel) throws JadePersistenceException, QDException;

    /**
     * Crée une nouvelle QD avec toutes les sous QD nécessaires et liées.
     * 
     * @param qd
     * @return la qd créée
     * @throws JadePersistenceException
     * @throws QDException
     */
    public QD create(QD qd) throws JadePersistenceException, QDException;

    /**
     * Création d'une QD avec les sous QD nécessaires
     * 
     * @return qd
     * @throws JadePersistenceException
     * @throws QDException
     */
    public QD create(QDAnnuelle qdAnnuelle, MembreFamille membreFamille, CSTypeQD typeQD,
            Hashtable<CSVariableMetier, VariableMetier> variablesMetier, Boolean ouvertureManuelle)
            throws JadePersistenceException, QDException;

    /**
     * Retourne le montant maximale remboursable dans une qd en prenant en compte les qd parentes
     * 
     * @param qd
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Float getMontantMaximalRemboursable(QD qd) throws JadePersistenceException, QDException,
            JadeApplicationServiceNotAvailableException;

    /**
     * TODO Javadoc
     * 
     * @param idQD
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public QD read(String idQD) throws JadePersistenceException, QDException;

    /**
     * TODO Javadoc
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public QDSearchModel search(QDSearchModel searchModel) throws JadePersistenceException, QDException;

    /**
     * Modification d'un qd, en général on modifie seuelement le montant utilisé d'une QD
     * 
     * @param qd
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public QD update(QD qd) throws JadePersistenceException, QDException;

    /**
     * Permet d'utilise un montant pour une facture dans une QD
     * 
     * @param qd
     * @param montant
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public QD utiliserMontant(QD qd, String montant) throws JadePersistenceException, QDException;

}
