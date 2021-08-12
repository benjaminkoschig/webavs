package globaz.hercule.service;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hercule.db.controleEmployeur.CEAttributionPts;
import globaz.hercule.db.controleEmployeur.CEAttributionPtsManager;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPts;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsManager;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsViewBean;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

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
        //attrPts.setMasseAvs(viewBean._getMasseSalariale()); // ESVE CONTROLE EMPLOYEUR MASSE SALARIALE SETTER NON UTILISE
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
     * Cherche la déclaration de salaire de type employeur liée au contrôle.
     *
     * @param idControle
     * @param session
     * @return la déclaration de salaire
     */
    public static DSDeclarationViewBean chercheDeclarationDeSalaireControleEmployeurAvecIdControle(String idControle, BSession session) throws HerculeException {
        try {
            DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
            declarationManager.setSession(session);
            declarationManager.setForTypeDeclaration(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR);
            declarationManager.setForIdControlEmployeur(idControle);
            declarationManager.find(BManager.SIZE_NOLIMIT);

            // Si on trouve plus qu'une déclaration de salaires liées au contrôle on garde celle avec l'id le plus récent
            if (declarationManager.size() > 0) {
                String newestIdDeclaration = ((DSDeclarationViewBean) declarationManager.getFirstEntity()).getIdDeclaration();
                List<DSDeclarationViewBean> allDeclarations = declarationManager.toList();
                for (DSDeclarationViewBean currentDeclaration : allDeclarations) {
                    if (Integer.parseInt(currentDeclaration.getIdDeclaration()) > Integer.parseInt(newestIdDeclaration)) {
                        newestIdDeclaration = currentDeclaration.getIdDeclaration();
                    }
                }
                DSDeclarationViewBean declaration = new DSDeclarationViewBean();
                declaration.setSession(session);
                declaration.setIdDeclaration(newestIdDeclaration);
                declaration.retrieve();
                return declaration;
            }

        } catch (Exception exception) {
            throw new HerculeException("Technical exception, unabled to retrieve declaration point (idControle="
                    + idControle, exception);
        }

        return null;
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
     * @param numAffilie
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws HerculeException
     */
    public static String findIdAttributionPtsActifForControle(final BSession session, final String numAffilie,
                                                                      final String dateDebut, final String dateFin) throws HerculeException {

        CEAttributionPts attributionPtsActif = findAttributionPtsActifForControle(session, numAffilie, dateDebut, dateFin);

        if (attributionPtsActif != null) {
            return attributionPtsActif.getIdAttributionPts();
        } else {
            return null;
        }
    }

    /**
     * Permet de récupérer l'évaluation des points d'un controle
     *
     * @param session
     * @param numAffilie
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws HerculeException
     */
    public static CEAttributionPts findAttributionPtsActifForControle(final BSession session, final String numAffilie,
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

        CEAttributionPts attributionPts = null;

        CEAttributionPtsManager manager = new CEAttributionPtsManager();
        manager.setSession(session);
        manager.setForActif(true);
        manager.setForDateDebut(dateDebut);
        manager.setForDateFin(dateFin);
        manager.setForNumAffilie(numAffilie);

        try {
            manager.find();

            if (!manager.isEmpty()) {
                attributionPts = (CEAttributionPts) manager.getFirstEntity();
            }

        } catch (Exception e) {
            throw new HerculeException("Technical exception, to find id attribution point (dateDebut=" + dateDebut
                    + "/dateFin=" + dateFin + "/numAffilie=" + numAffilie + ")", e);
        }

        return attributionPts;
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
