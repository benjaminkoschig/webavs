package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hercule.db.controleEmployeur.CEAttributionPts;
import globaz.hercule.db.controleEmployeur.CEAttributionPtsManager;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPts;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsManager;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsViewBean;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 8 sept. 2010
 */
public class CEAttributionPtsService {

    /**
     * Permet de remplir les champs de l'entité avec ceux du viewBean
     * 
     * @param attrPts
     * @param viewBean
     * @return
     */
    public static void fillFields(final CEAttributionPts attrPts, final CEGestionAttributionPtsViewBean viewBean) {

        attrPts.setNumAffilie(viewBean.getNumAffilie());
        attrPts.setIdAttributionPts(viewBean.getIdAttributionPts());
        attrPts.setDerniereRevision(viewBean.getDerniereRevision());
        attrPts.setDerniereRevisionCom(viewBean.getDerniereRevisionCom());
        attrPts.setQualiteRH(viewBean.getQualiteRH());
        attrPts.setQualiteRHCom(viewBean.getQualiteRHCom());
        attrPts.setCollaboration(viewBean.getCollaboration());
        attrPts.setCollaborationCom(viewBean.getCollaborationCom());
        attrPts.setCriteresEntreprise(viewBean.getCriteresEntreprise());
        attrPts.setCriteresEntrepriseCom(viewBean.getCriteresEntrepriseCom());
        attrPts.setCommentaires(viewBean.getCommentaires());
        attrPts.setNbrePoints(viewBean.getNbrePoints());
        attrPts.setLastUser(viewBean.getLastUser());
        attrPts.setLastModification(viewBean.getLastModification());
        attrPts.setPeriodeDebut(viewBean.getPeriodeDebut());
        attrPts.setPeriodeFin(viewBean.getPeriodeFin());
        attrPts.setObservations(viewBean.getObservations());
        attrPts.setIsAttributionActive(viewBean.getIsAttributionActive());
        attrPts.setIsModificationUtilisateur(viewBean.getIsModificationUtilisateur());

    }

    /**
     * Permet de remplir les champs du viewBean avec ceux de l'entité
     * 
     * @param attrPts
     * @param viewBean
     * @return
     */
    public static void fillFields(final CEGestionAttributionPtsViewBean viewBean, final CEGestionAttributionPts attrPts) {

        viewBean.setNumAffilie(attrPts.getNumAffilie());
        viewBean.setIdAttributionPts(attrPts.getIdAttributionPts());
        viewBean.setDerniereRevision(attrPts.getDerniereRevision());
        viewBean.setDerniereRevisionCom(attrPts.getDerniereRevisionCom());
        viewBean.setQualiteRH(attrPts.getQualiteRH());
        viewBean.setQualiteRHCom(attrPts.getQualiteRHCom());
        viewBean.setCollaboration(attrPts.getCollaboration());
        viewBean.setCollaborationCom(attrPts.getCollaborationCom());
        viewBean.setCriteresEntreprise(attrPts.getCriteresEntreprise());
        viewBean.setCriteresEntrepriseCom(attrPts.getCriteresEntrepriseCom());
        viewBean.setCommentaires(attrPts.getCommentaires());
        viewBean.setNbrePoints(attrPts.getNbrePoints());
        viewBean.setLastUser(attrPts.getLastUser());
        viewBean.setLastModification(attrPts.getLastModification());
        viewBean.setPeriodeDebut(attrPts.getPeriodeDebut());
        viewBean.setPeriodeFin(attrPts.getPeriodeFin());
        viewBean.setObservations(attrPts.getObservations());
        viewBean.setIsAttributionActive(attrPts.getIsAttributionActive());
        viewBean.setIsModificationUtilisateur(attrPts.getIsModificationUtilisateur());

        // Table controle employeur CECONTP
        viewBean.setGenreControle(attrPts.getGenreControle());
        viewBean.setDateDebutControle(attrPts.getDateDebutControle());
        viewBean.setDateFinControle(attrPts.getDateFinControle());
        viewBean.setDateEffective(attrPts.getDateEffective());
        viewBean.setIdControle(attrPts.getIdControle());
        viewBean.setNumAffilieExterne(attrPts.getNumAffilieExterne());
        viewBean.setTempsJour(attrPts.getTempsJour());

        // Table affilitaiton AFAFFIP
        viewBean.setDateDebutAffiliation(attrPts.getDateDebutAffiliation());
        viewBean.setDateFinAffiliation(attrPts.getDateFinAffiliation());
        viewBean.setIdTiers(attrPts.getIdTiers());
        viewBean.setCodeNOGA(attrPts.getCodeNOGA());
        viewBean.setBrancheEconomique(attrPts.getBrancheEconomique());

        // Table de tiers TITIERP
        viewBean.setNom(attrPts.getNom());

        // Table des couvertures CECOUVP
        viewBean.setAnneeCouverture(attrPts.getAnneeCouverture());

        // Table des réviseurs
        viewBean.setNomReviseur(attrPts.getNomReviseur());
        viewBean.setTypeReviseur(attrPts.getTypeReviseur());

        viewBean.setCodeSuva(attrPts.getCodeSuva());
        viewBean.setLibelleSuva(attrPts.getLibelleSuva());

        // Spy
        viewBean.populateSpy(attrPts.getSpy().toString());
    }

    /**
     * Permet de récupérer l'id de l'évaluation des points d'un controle
     * 
     * @param session
     * @param idControle
     * @return
     * @throws HerculeException
     */
    public static String findIdAttributionPtsActifForControle(final BSession session, final String numAffilie,
            final String dateDebut, final String dateFin) throws HerculeException {

        if (session == null) {
            throw new HerculeException("Unabled to find id attribution point. session is null");
        }

        if (JadeStringUtil.isBlank(dateDebut)) {
            throw new HerculeException("Unabled to find id attribution point, dateDebut is null or empty ");
        }

        if (JadeStringUtil.isBlank(dateFin)) {
            throw new HerculeException("Unabled to find id attribution point, dateFin is null or empty ");
        }

        if (JadeStringUtil.isBlank(numAffilie)) {
            throw new HerculeException("Unabled to find id attribution point, numAffilie is null or empty ");
        }

        String idAttributionPts = null;

        CEAttributionPtsManager manager = new CEAttributionPtsManager();
        manager.setSession(session);
        manager.setForActif(true);
        manager.setForDateDebut(dateDebut);
        manager.setForDateFin(dateFin);
        manager.setForNumAffilie(numAffilie);

        try {
            manager.find();

            if (!manager.isEmpty()) {
                CEAttributionPts attrpts = (CEAttributionPts) manager.getFirstEntity();
                idAttributionPts = attrpts.getIdAttributionPts();
            }

        } catch (Exception e) {
            throw new HerculeException("Technical exception, to find id attribution point (dateDebut=" + dateDebut
                    + "/dateFin=" + dateFin + "/numAffilie=" + numAffilie + ")", e);
        }

        return idAttributionPts;
    }

    /**
     * Permet la récupération d'une entité de type CEAttributionPts<BR>
     * Les champs qui caratérisent une attribution de points sont : <br>
     * Un numéro d'affilié<br>
     * Une date de début<br>
     * Une date de fin<br>
     * 
     * @param session
     * @param numAffilie
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws HerculeException
     */
    public static CEAttributionPts retrieveAttributionPtsActif(final BSession session, final BTransaction transaction,
            final String idAttribution, final String numAffilie, final String dateDebut, final String dateFin)
            throws HerculeException {

        if (session == null) {
            throw new HerculeException("Unabled to retrieve attribution point. session is null");
        }

        if (JadeStringUtil.isBlank(numAffilie)) {
            throw new HerculeException("Unabled to retrieve attribution point, numAffilie is null or empty ");
        }

        if (JadeStringUtil.isBlankOrZero(dateDebut)) {
            throw new HerculeException("Unabled to retrieve attribution point, dateDebut is null or empty ");
        }

        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            throw new HerculeException("Unabled to retrieve attribution point, dateFin is null or empty ");
        }

        CEAttributionPts attrpts = null;

        CEAttributionPtsManager manager = new CEAttributionPtsManager();
        manager.setSession(session);
        manager.setForDateDebut(dateDebut);
        manager.setForDateFin(dateFin);
        manager.setForNumAffilie(numAffilie);
        manager.setForActif(true);
        manager.setForNotIdAttribution(idAttribution);

        try {
            manager.find(transaction);

            if (!manager.isEmpty()) {
                attrpts = (CEAttributionPts) manager.getFirstEntity();
            }

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve attribution point (numAffilie="
                    + numAffilie + "/dateDebut=" + dateDebut + "/dateFin=" + dateFin, e);
        }

        return attrpts;
    }

    /**
     * Permet de récupérer une entité CEGestionAttributionPts grace a son identifiant
     * 
     * @param session
     * @param idAttributionPts
     * @return
     * @throws HerculeException
     */
    public static CEGestionAttributionPts retrieveGestionAttributionPts(final BSession session,
            final String idAttributionPts) throws HerculeException {

        if (session == null) {
            throw new HerculeException("Unabled to retrieve attribution point. session is null");
        }

        if (JadeStringUtil.isBlankOrZero(idAttributionPts)) {
            throw new HerculeException("Unabled to retrieve attribution point, idAttributionPts is null or empty ");
        }

        CEGestionAttributionPts attrPts = null;

        CEGestionAttributionPtsManager manager = new CEGestionAttributionPtsManager();
        manager.setSession(session);
        manager.setForIdAttributionPts(idAttributionPts);

        try {
            manager.find();

            if (!manager.isEmpty()) {
                attrPts = (CEGestionAttributionPts) manager.getFirstEntity();
            }

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve attribution point (id="
                    + idAttributionPts, e);
        }

        return attrPts;
    }

    /**
     * Constructeur de CEAttributionPtsService
     */
    protected CEAttributionPtsService() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
