/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.qd;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.Hashtable;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.QDAnnuelle;
import ch.globaz.perseus.business.models.qd.QDSearchModel;
import ch.globaz.perseus.business.models.qd.SimpleQD;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.services.models.qd.QDService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class QDServiceImpl extends PerseusAbstractServiceImpl implements QDService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDService#annulerMontantUtilise(ch.globaz.perseus.business.models
     * .qd.QD, java.lang.Float)
     */
    @Override
    public QD annulerMontantUtilise(QD qd, String montantUtilise) throws JadePersistenceException, QDException {

        BigDecimal newMontantUtilise = new BigDecimal(qd.getSimpleQD().getMontantUtilise());
        BigDecimal montant = new BigDecimal(montantUtilise);
        newMontantUtilise = newMontantUtilise.subtract(montant);
        qd.getSimpleQD().setMontantUtilise(newMontantUtilise.toString());
        qd = update(qd);
        // Mise à jour de la qd supérieure
        if (!JadeStringUtil.isBlankOrZero(qd.getQdParente().getId())) {
            qd.setQdParente(annulerMontantUtilise(read(qd.getQdParente().getId()), montantUtilise));
        }

        return qd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDService#count(ch.globaz.perseus.business.models.qd.QDSearchModel)
     */
    @Override
    public int count(QDSearchModel searchModel) throws JadePersistenceException, QDException {
        if (searchModel == null) {
            throw new QDException("Unable to count QD, the model passed is null");
        }

        return JadePersistenceManager.count(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.qd.QDService#create(ch.globaz.perseus.business.models.qd.QD)
     */
    @Override
    public QD create(QD qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to create QD, the model passed is null !");
        }

        SimpleQD simpleQD = qd.getSimpleQD();
        simpleQD.setIdMembreFamille(qd.getMembreFamille().getId());
        simpleQD.setIdQDAnnuelle(qd.getQdAnnuelle().getId());
        simpleQD.setIdQDParente(qd.getQdParente().getId());

        try {
            simpleQD = PerseusImplServiceLocator.getSimpleQDService().create(simpleQD);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new QDException("Service not avaiable : " + e.getMessage(), e);
        }

        qd.setSimpleQD(simpleQD);

        return qd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDService#create(ch.globaz.perseus.business.models.qd.QDAnnuelle,
     * ch.globaz.perseus.business.models.situationfamille.MembreFamille, ch.globaz.perseus.business.models.qd.CSTypeQD)
     */
    @Override
    public QD create(QDAnnuelle qdAnnuelle, MembreFamille membreFamille, CSTypeQD typeQD,
            Hashtable<CSVariableMetier, VariableMetier> variablesMetier, Boolean ouvertureManuelle)
            throws JadePersistenceException, QDException {
        return this.create(qdAnnuelle, membreFamille, typeQD, null, variablesMetier, ouvertureManuelle);
    }

    private QD create(QDAnnuelle qdAnnuelle, MembreFamille membreFamille, CSTypeQD typeQD, QD qdParente,
            Hashtable<CSVariableMetier, VariableMetier> variablesMetier, Boolean ouvertureManuelle)
            throws JadePersistenceException, QDException {
        if (qdAnnuelle == null) {
            throw new QDException("Unable to create QD, qdA      nnuelle is null");
        }
        if (membreFamille == null) {
            throw new QDException("Unable to create QD, membreFamille is null");
        }
        if (typeQD == null) {
            throw new QDException("Unable to create QD, typeQD is null");
        }
        // Création de la QD
        QD qd = new QD();
        qd.setMembreFamille(membreFamille);
        qd.setQdAnnuelle(qdAnnuelle);
        qd.setTypeQD(typeQD);
        qd.getSimpleQD().setMontantUtilise("0");
        qd.getSimpleQD().setOuvertureManuelle(ouvertureManuelle);
        if (qdParente != null) {
            qd.setQdParente(qdParente);
        }
        // Chargement du montant maximal de la QD dans les variables métiers
        if (typeQD.getVariableMetier() != null) {
            if (variablesMetier.get(typeQD.getVariableMetier()) == null) {
                JadeThread.logError(QDServiceImpl.class.getName(), "perseus.calcul.variablemetier.missing");
            } else {
                qd.getSimpleQD().setMontantLimite(
                        variablesMetier.get(typeQD.getVariableMetier()).getMontant().toString());
            }
        }

        // enregistrement de la qd
        qd = this.create(qd);

        // Création des QD enfants
        for (CSTypeQD typeEnfant : typeQD.getListChild()) {
            this.create(qdAnnuelle, membreFamille, typeEnfant, qd, variablesMetier, ouvertureManuelle);
        }

        return qd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.qd.QDService#getMontantMaximalRemboursable(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Float getMontantMaximalRemboursable(QD qd) throws JadePersistenceException, QDException,
            JadeApplicationServiceNotAvailableException {

        // TODO : Mettre en place la récusivité si il y'a plus de 2 niveaux de qd
        Float qdSuperieure = Float.MAX_VALUE;
        // Si il y'a une qd parente
        if (!JadeStringUtil.isEmpty(qd.getQdParente().getId())) {
            Float qdSupLimite = Float.parseFloat(qd.getQdParente().getSimpleQD().getMontantLimite());
            Float qdSupUtilise = Float.parseFloat(qd.getQdParente().getSimpleQD().getMontantUtilise());
            qdSuperieure = qdSupLimite - qdSupUtilise;
        }

        Float qdLimite = Float.parseFloat(qd.getSimpleQD().getMontantLimite());
        Float qdUtilise = Float.parseFloat(qd.getSimpleQD().getMontantUtilise());
        Float qdCourante = qdLimite - qdUtilise;

        Float montantRemboursable;
        if ((qdSuperieure < qdCourante) || (qdLimite == 0)) {
            montantRemboursable = qdSuperieure;
        } else {
            montantRemboursable = qdCourante;
        }

        return montantRemboursable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.qd.QDService#read(java.lang.String)
     */
    @Override
    public QD read(String idQD) throws JadePersistenceException, QDException {
        if (JadeStringUtil.isEmpty(idQD)) {
            throw new QDException("Unable to read QD, the id passed is empty !");
        }
        QD qd = new QD();
        qd.setId(idQD);

        return (QD) JadePersistenceManager.read(qd);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDService#search(ch.globaz.perseus.business.models.qd.QDSearchModel
     * )
     */
    @Override
    public QDSearchModel search(QDSearchModel searchModel) throws JadePersistenceException, QDException {
        if (searchModel == null) {
            throw new QDException("Unable to search QD, the model passed is null !");
        }

        return (QDSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.qd.QDService#update(ch.globaz.perseus.business.models.qd.QD)
     */
    @Override
    public QD update(QD qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to update QD, the model passed is null");
        }
        try {
            qd.setSimpleQD(PerseusImplServiceLocator.getSimpleQDService().update(qd.getSimpleQD()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new QDException("Service not available : " + e.getMessage(), e);
        }

        return qd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.QDService#utiliserMontant(ch.globaz.perseus.business.models.qd.QD,
     * java.lang.Float)
     */
    @Override
    public QD utiliserMontant(QD qd, String montantRembourse) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to use QD, the model passed is null");
        }
        BigDecimal newMontantUtilise = new BigDecimal(qd.getSimpleQD().getMontantUtilise());
        BigDecimal montant = new BigDecimal(montantRembourse);
        newMontantUtilise = newMontantUtilise.add(montant);
        qd.getSimpleQD().setMontantUtilise(newMontantUtilise.toString());
        qd = update(qd);
        // Mise à jour de la qd supérieure
        if (!JadeStringUtil.isBlankOrZero(qd.getQdParente().getId())) {
            qd.setQdParente(utiliserMontant(read(qd.getQdParente().getId()), montantRembourse));
        }

        return qd;
    }

}
