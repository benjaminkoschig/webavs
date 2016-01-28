package ch.globaz.al.businessimpl.checker.model.dossier;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.exceptions.model.dossier.ALCommentaireModelException;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de v�rification des donn�es de <code>CommentaireModel</code>
 * 
 * @author jts
 * 
 */
public abstract class CommentaireModelChecker extends ALAbstractChecker {

    /** Longueur maximal du commentaire */
    private static final int COMMENT_MAX_LENGTH = 900;

    /**
     * Contr�le l'int�grit� m�tier du mod�le
     * 
     * @param commentaireModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkBusinessIntegrity(CommentaireModel commentaireModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // on v�rifie que l'idDossier fourni correspond bien � un dossier
        // existant en base de donn�es
        DossierFkSearchModel sd = new DossierFkSearchModel();
        sd.setForIdDossier(commentaireModel.getIdDossier());
        if (0 == ALImplServiceLocator.getDossierFkModelService().count(sd)) {
            JadeThread.logError(CommentaireModelChecker.class.getName(),
                    "al.dossier.commentaireModel.idDossier.businessIntegrity.ExistingId");
        }
    }

    /**
     * V�rifie que les codes syst�me appartiennent � la famille de code attendue
     * 
     * @param commentaireModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_COMMENTAIRE_TYPE,
                    commentaireModel.getType())) {
                JadeThread.logError(CommentaireModelChecker.class.getName(),
                        "al.dossier.commentaireModel.type.codesystemIntegrity");

            }
        } catch (Exception e) {
            throw new ALCommentaireModelException(
                    "CommentaireModelChecker problem during checking codes system integrity", e);
        }
    }

    /**
     * V�rifie que toutes les donn�es ont le format attendu par la base de donn�es
     * 
     * @param commentaireModel
     *            Mod�le � valider
     */
    private static void checkDatabaseIntegrity(CommentaireModel commentaireModel) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id du dossier
        if (!JadeNumericUtil.isIntegerPositif(commentaireModel.getIdDossier())) {
            JadeThread.logError(CommentaireModelChecker.class.getName(),
                    "al.dossier.commentaireModel.idDossier.databaseIntegrity.type");
        }

        // type de commentaire
        if (!JadeNumericUtil.isIntegerPositif(commentaireModel.getType())) {
            JadeThread.logError(CommentaireModelChecker.class.getName(),
                    "al.dossier.commentaireModel.type.databaseIntegrity.type");
        }

        // longueur du texte
        if (commentaireModel.getTexte().length() > COMMENT_MAX_LENGTH) {
            JadeThread.logError(CommentaireModelChecker.class.getName(),
                    "al.dossier.commentaireModel.texte.databaseIntegrity.length",
                    new String[] { String.valueOf(COMMENT_MAX_LENGTH) });
        }
    }

    /**
     * V�rifie si tous les param�tres requis ont �t� indiqu�s
     * 
     * @param commentaireModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException {

        // id du dossier
        if (JadeStringUtil.isEmpty(commentaireModel.getIdDossier())) {
            JadeThread.logError(CommentaireModelChecker.class.getName(),
                    "al.dossier.commentaireModel.idDossier.mandatory");
        }

        // type du commentaire
        if (JadeStringUtil.isEmpty(commentaireModel.getType())) {
            JadeThread.logError(CommentaireModelChecker.class.getName(), "al.dossier.commentaireModel.type.mandatory");
        }

        // texte
        if (JadeStringUtil.isEmpty(commentaireModel.getTexte())) {
            JadeThread.logError(CommentaireModelChecker.class.getName(), "al.dossier.commentaireModel.texte.mandatory");
        }
    }

    /**
     * Ex�cute les v�rifications n�cessaire avant l'enregistrement du mod�le
     * 
     * @param commentaireModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException {

        CommentaireModelChecker.checkMandatory(commentaireModel);
        CommentaireModelChecker.checkDatabaseIntegrity(commentaireModel);
        CommentaireModelChecker.checkCodesystemIntegrity(commentaireModel);
        CommentaireModelChecker.checkBusinessIntegrity(commentaireModel);
    }

}
