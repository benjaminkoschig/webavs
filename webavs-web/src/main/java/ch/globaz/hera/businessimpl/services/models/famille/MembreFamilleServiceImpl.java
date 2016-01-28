package ch.globaz.hera.businessimpl.services.models.famille;

import globaz.globall.db.BSession;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFRequerant;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.exceptions.models.RelationConjointException;
import ch.globaz.hera.business.models.famille.DateNaissanceConjoint;
import ch.globaz.hera.business.models.famille.MembreFamille;
import ch.globaz.hera.business.models.famille.MembreFamilleSearch;
import ch.globaz.hera.business.models.famille.RelationConjoint;
import ch.globaz.hera.business.models.famille.RelationConjointSearch;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.hera.business.services.models.famille.MembreFamilleService;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.hera.businessimpl.services.HeraAbstractServiceImpl;
import ch.globaz.hera.common.SessionProvider;

public class MembreFamilleServiceImpl extends HeraAbstractServiceImpl implements MembreFamilleService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.MembreFamilleService#
     * count(ch.globaz.hera.business.models.famille.MembreFamilleSearch)
     */
    @Override
    public int count(MembreFamilleSearch search) throws MembreFamilleException, JadePersistenceException {
        if (search == null) {
            throw new MembreFamilleException("Unable to count membreFamille, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public DateNaissanceConjoint readDateNaissanceConjoint(String idMbrFamConjoint) throws JadePersistenceException,
            MembreFamilleException {
        if (idMbrFamConjoint == null) {
            throw new MembreFamilleException("Unable to count membreFamille, the search model passed is null!");
        }
        DateNaissanceConjoint mbrFamille = new DateNaissanceConjoint();
        mbrFamille.getSimpleMembreFamille().setIdMembreFamille(idMbrFamConjoint);
        mbrFamille = (DateNaissanceConjoint) JadePersistenceManager.read(mbrFamille);
        return mbrFamille;
    }

    /*
	 * 
	 */
    @Override
    public void createRequerant(String idTiersRequerant) throws MembreFamilleException {
        boolean isMFFoundDomaineStd = false;

        BSession bSession = getSession();
        // BTransaction transaction = null;

        try {
            // transaction = (BTransaction) bSession.newTransaction();
            // transaction.openTransaction();
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(bSession);
            membre.setIdTiers(idTiersRequerant);
            membre.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
            membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            membre.retrieve();
            if (membre.isNew()) {
                // On tente de récupérer le membre de famille pour le domaine
                // standard !!!
                membre.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                membre.retrieve();
                if (!membre.isNew()) {
                    isMFFoundDomaineStd = true;
                }
            } else {
                if (ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(membre.getCsDomaineApplication())) {
                    isMFFoundDomaineStd = true;
                }
            }

            // On créé le membre de famille pour le domaine standard si
            // inexistant.
            String idMembreFamille = null;
            if (isMFFoundDomaineStd) {
                idMembreFamille = membre.getIdMembreFamille();
            }
            // On créé le membre de famille pour le domaine standard
            else {
                // Crée le membre
                membre = new SFMembreFamille();
                membre.setSession(bSession);
                membre.setIdTiers(idTiersRequerant);
                membre.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                membre.add();
                // SFRequerantHelper.throwExceptionIfError(transaction, membre);
                idMembreFamille = membre.getIdMembreFamille();
            }

            // On crée le requérant
            SFRequerant req = new SFRequerant();
            req.setISession(bSession);
            req.setIdDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
            req.setIdMembreFamille(idMembreFamille);
            req.add();
            // SFRequerantHelper.throwExceptionIfError(transaction, req);
        } catch (Exception e) {
            throw new MembreFamilleException("Impossiblie de créer le requérant", e);
        }

    }

    /**
     * Filtre les membre de famille valable(mariée, en vie) pour la date données en paramétre
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws MembreFamilleException
     * 
     */
    @Override
    public MembreFamilleVO[] filtreMembreFamilleWithDate(MembreFamilleVO[] arrayMembreFamilleVO, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, MembreFamilleException {
        // MembreFamilleVO[] mfDisponibles = new MembreFamilleVO[arrayMembreFamilleVO.length];

        if (arrayMembreFamilleVO == null) {
            throw new MembreFamilleException(
                    "Unable to filtreMembreFamilleWithDate arrayMembreFamilleVO, the model passed is null!");
        }

        if (date == null) {
            throw new MembreFamilleException("Unable to filtreMembreFamilleWithDate  date, the model passed is null!");
        }

        ArrayList<MembreFamilleVO> mfDisponibles = new ArrayList<MembreFamilleVO>();

        String idMembreFamilleConjointRequerant = null;
        List<MembreFamilleVO> listeMembreFamilleConjoints = new ArrayList<MembreFamilleVO>();

        int i = 0;
        for (MembreFamilleVO membreFamDispo : arrayMembreFamilleVO) {

            if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(membreFamDispo.getRelationAuRequerant())) {
                listeMembreFamilleConjoints.add(membreFamDispo);
            } else if (ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(membreFamDispo.getRelationAuRequerant())) {
                if (isAlive(membreFamDispo, date)) {
                    mfDisponibles.add(membreFamDispo);
                }
            } else if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(membreFamDispo.getRelationAuRequerant())) {
                mfDisponibles.add(membreFamDispo);
                idMembreFamilleConjointRequerant = membreFamDispo.getIdMembreFamille();
            }
        }

        try {
            for (MembreFamilleVO mbrFamConjoint : listeMembreFamilleConjoints) {
                if (isAlive(mbrFamConjoint, date)) {
                    RelationConjointSearch search = new RelationConjointSearch();
                    search.setForDateValable(date);
                    search.setForIdConjoint1(idMembreFamilleConjointRequerant);
                    search.setForIdConjoint2(mbrFamConjoint.getIdMembreFamille());
                    search.setWhereKey(RelationConjointSearch.WITH_DATE_VALABLE);
                    search = HeraServiceLocator.getRelationConjointService().search(search);
                    // cherche si la personne est marie et pas un ex-conjoint
                    boolean isDivorce = false;
                    boolean isMarie = false;
                    if (search.getSize() == 1) {
                        for (JadeAbstractModel absDonnee : search.getSearchResults()) {
                            RelationConjoint conjoint = (RelationConjoint) absDonnee;
                            isDivorce |= ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(conjoint
                                    .getSimpleRelationConjoint().getTypeRelation());
                            isMarie |= ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(conjoint
                                    .getSimpleRelationConjoint().getTypeRelation());
                            if (isDivorce) {
                                break;
                            }
                        }
                        if (isMarie && !isDivorce) {
                            mfDisponibles.add(mbrFamConjoint);
                        }
                    }
                }
            }
        } catch (RelationConjointException e) {
            throw new MembreFamilleException("Unable to find the relationConjoint", e);
        }
        return mfDisponibles.toArray(new MembreFamilleVO[mfDisponibles.size()]);
    }

    @Override
    public ArrayList<MembreFamilleVO> getFamilleByIDEnfant(String idEnfant, Boolean withoutParents)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return this.getFamilleByIDEnfant(idEnfant, null, withoutParents);
    }

    /**
     * Retourne la famille complète dâprès l'id tiers d'un enfant passé en paramètre, pour le domaine des rentes
     * 
     * @param idEnfant
     *            , l'id tiers de l'enfant
     * @param withoutParents
     *            , retourne les parents avec, ou pas. Si null, considérer comme false
     * @return, tableau de MembreFamilleVO
     * @throws MembreFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public ArrayList<MembreFamilleVO> getFamilleByIDEnfant(String idEnfant, String date, Boolean withoutParents)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (idEnfant == null) {
            throw new MembreFamilleException("Impossible de rechercher la famille, l'id de l'enfant est null!");
        }

        try {
            // recup de l'api siutationFamilliale
            ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idEnfant);

            // recherche des parents avec idTiers de l'enfant
            globaz.hera.api.ISFMembreFamille[] parents = situationFamiliale.getParents(idEnfant);

            // instanciation de la famille
            globaz.hera.api.ISFMembreFamilleRequerant[] famille = null;

            // Liste de retour pour la fratrie
            ArrayList<MembreFamilleVO> fratrie = new ArrayList<MembreFamilleVO>(0);

            // Si pas de parents retour liste vide
            if (parents.length != 0) {
                // on itere sur les parents
                for (globaz.hera.api.ISFMembreFamille parent : parents) {
                    // on s'assure que l'idTiers passé pour la recherche est bien celui d'unrequérant
                    if (situationFamiliale.getMembresFamilleRequerant(parent.getIdTiers()) != null) {
                        if (date != null) {
                            famille = situationFamiliale.getMembresFamilleRequerant(parent.getIdTiers(), date);
                        } else {
                            famille = situationFamiliale.getMembresFamilleRequerant(parent.getIdTiers());
                        }
                    }
                }

                // iteration sur le membres fanilles, d'apres id parents
                for (globaz.hera.api.ISFMembreFamilleRequerant membre : famille) {
                    // Instanciation et creation de l'objet VO
                    MembreFamilleVO membreVO = new MembreFamilleVO(membre.getCsCantonDomicile(),
                            membre.getCsEtatCivil(), membre.getCsNationalite(), membre.getCsSexe(),
                            membre.getDateDeces(), membre.getDateNaissance(), membre.getIdMembreFamille(),
                            membre.getIdTiers(), membre.getNom(), membre.getNss(), membre.getPrenom(),
                            (membre).getRelationAuRequerant());
                    // Si parents inclus, param pas null et setter
                    if ((withoutParents != null) && !withoutParents) {
                        fratrie.add(membreVO);
                    } else {
                        // Si le membre est un enfant ajout dans la liste, et n'est pas l'enfant requérant
                        if ((membre).getRelationAuRequerant().equals(ISFMembreFamille.CS_TYPE_RELATION_ENFANT)) {
                            fratrie.add(membreVO);
                        }
                    }
                }
            }

            return fratrie;
        } catch (Exception e) {
            throw new MembreFamilleException("Impossible de rechercher la famille", e);
        }

    }

    private BSession getSession() throws MembreFamilleException {
        BSession session = SessionProvider.findSession();
        if (session == null) {
            throw new MembreFamilleException("Unable to find session in context!");
        }
        return session;
    }

    private boolean isAlive(MembreFamilleVO membreFamille, String dateToCompare) {
        String dateNaiss = membreFamille.getDateNaissance();
        String dateDeces = membreFamille.getDateDeces();

        return ((JadeDateUtil.isDateBefore(dateNaiss, dateToCompare) || JadeDateUtil.areDatesEquals(dateNaiss,
                dateToCompare)) && (JadeStringUtil.isEmpty(dateDeces) || JadeDateUtil.isDateAfter(dateDeces,
                dateToCompare)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.MembreFamilleService# read(java.lang.String)
     */
    @Override
    public MembreFamille read(String idMembreFamille) throws JadePersistenceException, MembreFamilleException {
        if (JadeStringUtil.isEmpty(idMembreFamille)) {
            throw new MembreFamilleException("Unable to read membreFamille, the id passed is null!");
        }
        MembreFamille membreFamille = new MembreFamille();
        membreFamille.setId(idMembreFamille);
        return (MembreFamille) JadePersistenceManager.read(membreFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.MembreFamilleService#
     * search(ch.globaz.hera.business.models.famille.MembreFamilleSearch)
     */
    @Override
    public MembreFamilleSearch search(MembreFamilleSearch search) throws JadePersistenceException,
            MembreFamilleException {
        if (search == null) {
            throw new MembreFamilleException("Unable to search membreFamille, the search model passed is null!");
        }
        return (MembreFamilleSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.MembreFamilleService#
     * searchMembresFamilleRequerant(java.lang.String, java.lang.String)
     */
    @Override
    public MembreFamilleVO[] searchMembresFamilleRequerant(String csDomaine, String idTiersRequerant, String date)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        BSession session = getSession();

        // utilisation de l'API Hera pour trouver la famille du requerant pour
        // un domaine

        ISFMembreFamilleRequerant[] mf = null;
        try {
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, csDomaine,
                    idTiersRequerant);

            if (JadeStringUtil.isBlankOrZero(date)) {
                mf = sf.getMembresFamilleRequerant(idTiersRequerant);
            } else {
                mf = sf.getMembresFamilleRequerant(idTiersRequerant, date);
            }
        } catch (Exception e) {
            throw new MembreFamilleException(e.toString());
        }

        // construction des MembreFamilleVO
        ArrayList result = new ArrayList();

        if (mf != null) {
            for (int i = 0; i < mf.length; i++) {

                ISFMembreFamilleRequerant mfr = mf[i];

                result.add(new MembreFamilleVO(mfr.getCsCantonDomicile(), mfr.getCsEtatCivil(), mfr.getCsNationalite(),
                        mfr.getCsSexe(), mfr.getDateDeces(), mfr.getDateNaissance(), mfr.getIdMembreFamille(), mfr
                                .getIdTiers(), mfr.getNom(), mfr.getNss(), mfr.getPrenom(), mfr
                                .getRelationAuRequerant()));
            }
        }

        return (MembreFamilleVO[]) result.toArray(new MembreFamilleVO[] {});
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.MembreFamilleService#
     * searchMembresFamilleRequerantDomaineRentes(java.lang.String)
     */
    @Override
    public MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes(String idTiersRequerant)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        return searchMembresFamilleRequerant(ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant, "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.MembreFamilleService#
     * searchMembresFamilleRequerantDomaineRentes(java.lang.String,java.lang.String)
     */
    @Override
    public MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes(String idTiersRequerant, String date)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        return searchMembresFamilleRequerant(ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant, date);
    }
}
