package ch.globaz.ij.businessimpl.services;

import globaz.globall.db.BSession;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.utils.PRDateUtils;
import ch.globaz.ij.business.models.IJPrononceJointDemande;
import ch.globaz.ij.business.models.IJPrononceJointDemandeSearchModel;
import ch.globaz.ij.business.services.IJPrononceService;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;
import ch.globaz.jade.JadeBusinessServiceLocator;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

import java.util.List;

public class IJPrononceServiceImpl implements IJPrononceService {

    private IJPrononceJointDemande calculerJoursSiBesoin(IJPrononceJointDemande prononce) {

        if (IIJPrononce.CS_ATTENTE_READAPTATION.equals(prononce.getCsGenreReadaptation())
                && !JadeStringUtil.isBlankOrZero(prononce.getDateFinPrononce())
                && !JadeStringUtil.isBlankOrZero(prononce.getDateDebutPrononce())) {
            prononce.setJours(PRDateUtils.getNbDayBetween(prononce.getDateDebutPrononce(),
                    prononce.getDateFinPrononce()) + 1);
        }

        return prononce;
    }

    private IJPrononceJointDemande chargerCodeSystemeGenreReabilitation(IJPrononceJointDemande prononce) {
        if ((prononce != null) && (prononce.getGenreReabilitation() == null)
                && !JadeStringUtil.isBlankOrZero(prononce.getCsGenreReadaptation())) {
            try {
                prononce.setGenreReabilitation(JadeBusinessServiceLocator.getCodeSystemeService().getCodeSysteme(
                        prononce.getCsGenreReadaptation()));
            } catch (Exception ex) {
                JadeLogger.error(this, ex.toString());
            }
        }
        return prononce;
    }

    @Override
    public int count(IJPrononceJointDemandeSearchModel search) throws ServiceBusinessException,
            ServiceTechnicalException {
        if (search == null) {
            throw new ServiceTechnicalException("Service : " + this.getClass().getName()
                    + " : unable to count values because searchModel is null");
        }
        try {
            return JadePersistenceManager.count(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException("Service : " + this.getClass().getName() + " : unable to count values");
        }
    }

    /**
     * <strong>Ce service n'est qu'un service de consultation.</strong><br>
     * Cette partie du service n'est pas implémenté car l'ajout et la suppression d'élément dans la base de données doit
     * géré l'incrémentation de l'id, ce service n'en est pas capable. <strong>L'appel de cette méthode lève une
     * Exception de type <code>IJPrononceServiceException</code></strong></br>
     * 
     * @see globaz.ij.db.prononces.IJPrononce
     */
    @Override
    public IJPrononceJointDemande create(IJPrononceJointDemande entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        throw new UnsupportedOperationException("Service : " + this.getClass().getName()
                + " can not be used to create an entity in database");
    }

    /**
     * <strong>Ce service n'est qu'un service de consultation.</strong><br>
     * Cette partie du service n'est pas implémenté car l'ajout et la suppression d'élément dans la base de données doit
     * géré l'incrémentation de l'id, ce service n'en est pas capable.<br>
     * <strong>L'appel de cette méthode lève une Exception de type <code>IJPrononceServiceException</code></strong></br>
     * 
     * @see globaz.ij.db.prononces.IJPrononce
     */
    @Override
    public IJPrononceJointDemande delete(IJPrononceJointDemande entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        throw new UnsupportedOperationException("Service : " + this.getClass().getName()
                + " can not be used to delete an entity in database");
    }

    @Override
    public IJPrononceJointDemande read(String idEntity) throws ServiceBusinessException, ServiceTechnicalException {
        IJPrononceJointDemande prononce = new IJPrononceJointDemande();
        prononce.setId(idEntity);
        try {
            return calculerJoursSiBesoin(chargerCodeSystemeGenreReabilitation((IJPrononceJointDemande) JadePersistenceManager
                    .read(prononce)));
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
    }

    @Override
    public IJPrononceJointDemandeSearchModel search(IJPrononceJointDemandeSearchModel search)
            throws JadeApplicationException, JadePersistenceException {
        search.setOrderKey("orderByDateDeFinDesc");
        IJPrononceJointDemandeSearchModel returnedModel = (IJPrononceJointDemandeSearchModel) JadePersistenceManager
                .search(search);

        for (JadeAbstractModel unResultat : returnedModel.getSearchResults()) {
            chargerCodeSystemeGenreReabilitation((IJPrononceJointDemande) unResultat);
            calculerJoursSiBesoin((IJPrononceJointDemande) unResultat);
        }

        return returnedModel;
    }

    /**
     * <strong>Ce service n'est qu'un service de consultation.</strong><br>
     * Cette partie du service n'est pas implémenté car l'ajout et la suppression d'élément dans la base de données doit
     * géré l'incrémentation de l'id, ce service n'en est pas capable. <strong>L'appel de cette méthode lève une
     * Exception de type <code>IJPrononceServiceException</code></strong></br>
     * 
     * @see globaz.ij.db.prononces.IJPrononce
     */
    @Override
    public IJPrononceJointDemande update(IJPrononceJointDemande entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        throw new UnsupportedOperationException("Service : " + this.getClass().getName()
                + " can not be used to delete an entity in database");
    }

    /**
     * recherche le canton dans les situations professionnelles
     * @param domaine
     * @param situationsProf
     * @return
     * @throws Exception
     */
    public String rechercheCantonAdressePaiementSitProf(BSession session, String domaine, List<IJSituationProfessionnelle> situationsProf, String dateDebut) throws Exception {
        String canton = "";
        // vérification du canton de la situation professionnelle
         for (IJSituationProfessionnelle sit : situationsProf) {
            if (!JadeStringUtil.isEmpty(sit.getIdEmployeur())) {
                IJEmployeur employeur = sit.loadEmployeur();
                TIAdressePaiementData data = PRTiersHelper.getAdressePaiementData(session, session.getCurrentThreadTransaction(), employeur.getIdTiers(),
                        domaine, employeur.getIdAffilie(), dateDebut);

                if (!data.isNew()) {
                    String cantonComparaison = PRTiersHelper.getCanton(session, data.getNpa());
                    if (cantonComparaison == null) {
                        // canton de l'adresse de paiement de la banque (indépendant étranger ?)
                        cantonComparaison = PRTiersHelper.getCanton(session, data.getNpa_banque());
                    }
                    // toutes les situations professionnelles du droit doivent avoir le même canton sinon impossible de déterminer
                    if (!canton.isEmpty() && !canton.equals(cantonComparaison)) {
                        throw new Exception("impossible de déterminer le canton d'imposition : plusieurs cantons différents pour plusieurs employeurs : ");
                    } else {
                        canton = cantonComparaison;
                    }
                }

            }
        }
        return canton;
    }

}
