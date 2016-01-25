package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.db.poursuite.COContentieuxViewBean;
import globaz.aquila.util.COStringUtils;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

/**
 * <H1>Description</H1>
 * <p>
 * Une factory qui a des allures de service pour créer ou charger des instances de contentieux corrects en fonction de
 * certains paramètres.
 * </p>
 * 
 * @author vre
 */
public class COContentieuxFactory {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée un nouveau contentieux pour la section et le compte annexe donné.
     * 
     * @param session
     * @param section
     * @param compteAnnexe
     * @return un nouveau contentieux pour la section et le compte annexe donné
     * @throws Exception
     */
    public static final COContentieux creerNouveauContentieux(BSession session, CASection section,
            CACompteAnnexe compteAnnexe) throws Exception {
        COSequence sequence = COSequence.loadSequence(session, section, compteAnnexe);

        // trouver la première étape de la séquence
        COEtapeManager etapeManager = new COEtapeManager();

        etapeManager.setForIdSequence(sequence.getIdSequence());
        etapeManager.setForLibEtape(ICOEtape.CS_AUCUNE);
        etapeManager.setSession(session);
        etapeManager.find();

        if (etapeManager.size() != 1) {
            throw new Exception(COStringUtils.formatMessage((session).getLabel("AQUILA_ERR_ETAPE_BAD"),
                    new Object[] { session.getCodeLibelle(sequence.getLibSequence()) }));
        }

        // créer le contentieux
        COContentieux contentieux = new COContentieux();

        contentieux.setSequence(sequence);
        contentieux.setEtape((COEtape) etapeManager.get(0));
        contentieux.setSection(section);
        contentieux.setCompteAnnexe(compteAnnexe);
        contentieux.setIdSequence(sequence.getIdSequence());
        contentieux.setIdEtape(contentieux.getEtape().getIdEtape());
        contentieux.setIdSection(section.getIdSection());
        contentieux.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        contentieux.setDateDeclenchement(section.getDateEcheance());
        contentieux.setProchaineDateDeclenchement(section.getDateEcheance());
        contentieux.setDateExecution(section.getDateEcheance());
        contentieux.setDateOuverture(JACalendar.todayJJsMMsAAAA());
        contentieux.setUser(session.getUserName());
        contentieux.setMontantInitial(section.getSolde());
        contentieux.setSession(session);

        return contentieux;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Charge le contentieux suivant son id.
     * 
     * @param session
     * @param idContentieux
     * @return le contentieux correspondant à l'idContentieux
     * @throws Exception
     */
    public static final COContentieux loadContentieux(BISession session, String idContentieux) throws Exception {
        COContentieux retValue = new COContentieux();

        retValue.setIdContentieux(idContentieux);
        retValue.setISession(session);
        retValue.retrieve();

        return retValue;
    }

    /**
     * Charge le contentieux suivant son id.
     * 
     * @param session
     * @param idContentieux
     * @return le contentieux suivant son id.
     * @throws Exception
     */
    public static final COContentieux loadContentieuxViewBean(BISession session, String idContentieux) throws Exception {
        // si oui on charge le contentieux
        COContentieux retValue = new COContentieuxViewBean();

        retValue.setIdContentieux(idContentieux);
        retValue.setISession(session);
        retValue.retrieve();

        return retValue;
    }

    private COContentieuxFactory() {
    }
}
