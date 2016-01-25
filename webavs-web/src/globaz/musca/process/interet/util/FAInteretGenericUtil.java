package globaz.musca.process.interet.util;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.utils.CAUtil;
import java.util.Calendar;

public class FAInteretGenericUtil {

    /**
     * Effacer toutes les décisions d'intérêts, si déjà calculées dans ce passage.
     * 
     * @param session
     * @param transaction
     * @param passage
     * @throws Exception
     */
    public static void effacerInteretsPrecedents(BSession session, BTransaction transaction, IFAPassage passage)
            throws Exception {
        BSession osirisSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                .newSession(session);

        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
        manager.setSession(osirisSession);
        manager.setForIdJournalFacturation(passage.getIdPassage());

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            CAInteretMoratoire im = (CAInteretMoratoire) manager.get(i);

            if (!im.isTardif() && !im.isManuel()) {
                // Supprimer l'intérêt si non tardif et pas manuel
                im.setSession(osirisSession);
                im.setIdJournalFacturation("0");
                im.delete(transaction);

                if (transaction.hasErrors()) {
                    throw new Exception(session.getLabel("INTERET_SUPPRESION_IMPOSSIBLE") + " ("
                            + im.getIdInteretMoratoire() + ")");
                }
            }
        }

        if (!transaction.hasErrors()) {
            transaction.commit();
        } else {
            throw new Exception(transaction.getErrors().toString());
        }
    }

    /**
     * Facturer les intérêts. Créer un nouvel AFAct et si nécessaire l'insérer sur une nouvelle facture.
     * 
     * @param muscaSession
     * @param muscaTransaction
     * @param osirisSession
     * @param passage
     * @param idEnteteFacture
     * @param im
     * @throws Exception
     */
    public static void facturer(BSession muscaSession, BTransaction muscaTransaction, BSession osirisSession,
            FAPassage passage, String idEnteteFacture, CAInteretMoratoire im, FWCurrency montantInteret)
            throws Exception {
        FAAfact afact = new FAAfact();
        afact.setSession(muscaSession);
        if (!CAApplication.getApplicationOsiris().getCAParametres().isInteretSurCotArrSurSectSeparee()) {
            afact.setIdEnteteFacture(idEnteteFacture);
        } else {
            FAEnteteFacture entete = FAInteretGenericUtil.getEnteteFacture(muscaSession, muscaTransaction,
                    osirisSession, passage, idEnteteFacture, im);
            afact.setIdEnteteFacture(entete.getIdEntete());
        }

        afact.setIdPassage(passage.getIdPassage());
        afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        afact.setIdRubrique(im.getIdRubrique());
        // Bug 5678
        if ((montantInteret == null) || montantInteret.isZero()) {
            afact.setMontantFacture(im.calculeTotalInteret().toString());
        } else {
            afact.setMontantFacture(montantInteret.toString());
        }

        // reference la décison d'interet d'où provient le calcul
        afact.setReferenceExterne(im.getIdInteretMoratoire());

        // Bug 3172 - Renseigner la caisse prof. lors de la création des sections de type "50" et "900".
        if (JadeStringUtil.isBlankOrZero(afact.getNumCaisse())) {
            FAAfactManager afactManager = new FAAfactManager();
            afactManager.setSession(muscaSession);
            afactManager.setForIdEnteteFacture(im.getIdSection());
            afactManager.find();

            String numCaisse = null;
            for (int i = 0; (i < afactManager.size()) && (numCaisse == null); i++) {
                FAAfact afactSource = (FAAfact) afactManager.getEntity(i);
                if (!JadeStringUtil.isBlankOrZero(afactSource.getNumCaisse())) {
                    numCaisse = afactSource.getNumCaisse();
                }
            }

            afact.setNumCaisse(numCaisse);
        }

        // Choix du module
        afact.setIdModuleFacturation(ServicesFacturation.getIdModFacturationByType(muscaSession, muscaTransaction,
                FAModuleFacturation.CS_MODULE_IM_COTARR));

        // Ajouter l'afact
        afact.add(muscaTransaction);
    }

    /**
     * Contrôler que la facture n'existe pas dans le passage, si elle existe on la récupère, sinon on crée une nouvelle
     * facture. Création de l'entête de facture.
     * 
     * @param muscaSection
     * @param muscaTransaction
     * @param osirisSession
     * @param passage
     * @param idEnteteFacture
     * @param im
     * @return
     * @throws Exception
     */
    private static FAEnteteFacture getEnteteFacture(BSession muscaSection, BTransaction muscaTransaction,
            BSession osirisSession, FAPassage passage, String idEnteteFacture, CAInteretMoratoire im) throws Exception {
        FAEnteteFacture entete = new FAEnteteFacture();
        entete.setSession(muscaSection);

        entete.setIdEntete(idEnteteFacture);

        entete.retrieve(muscaTransaction);

        if (entete.hasErrors() || entete.isNew()) {
            throw new Exception(muscaSection.getLabel("ENTETE_FACTURE_NON_TROUVEE"));
        }

        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(muscaSection);

        entFactureManager.setForIdRole(entete.getIdRole());
        entFactureManager.setForIdTiers(entete.getIdTiers());
        entFactureManager.setForIdExterneRole(entete.getIdExterneRole());
        entFactureManager.setForIdPassage(passage.getIdPassage());
        entFactureManager.setForIdTypeFacture(entete.getIdTypeFacture());
        entFactureManager
                .setForIdSousType(APISection.ID_CATEGORIE_SECTION_INTERET_SUR_COTISATIONS_ARRIEREES_SUR_SECTION_SEPAREE);

        entFactureManager.find(muscaTransaction);

        if (entFactureManager.isEmpty()) {
            FAEnteteFacture nouveauEntFacture = new FAEnteteFacture();
            nouveauEntFacture.setSession(muscaSection);
            nouveauEntFacture.setIdPassage(passage.getIdPassage());
            nouveauEntFacture.setIdTiers(entete.getIdTiers());
            nouveauEntFacture.setIdExterneRole(entete.getIdExterneRole());
            nouveauEntFacture.setIdRole(entete.getIdRole());

            nouveauEntFacture.setIdDomaineCourrier(entete.getIdDomaineCourrier());
            nouveauEntFacture.setIdDomaineRemboursement(entete.getIdDomaineRemboursement());
            nouveauEntFacture.setIdDomaineLSV(entete.getIdDomaineLSV());
            nouveauEntFacture.setIdTypeCourrier(entete.getIdTypeCourrier());

            nouveauEntFacture.setIdTypeFacture(entete.getIdTypeFacture());
            nouveauEntFacture
                    .setIdSousType(APISection.ID_CATEGORIE_SECTION_INTERET_SUR_COTISATIONS_ARRIEREES_SUR_SECTION_SEPAREE);

            Calendar c = Calendar.getInstance();
            String numFacture = CAUtil.creerNumeroSectionUnique(osirisSession, muscaTransaction, entete.getIdRole(),
                    entete.getIdExterneRole(), entete.getIdTypeFacture(), Integer.toString(c.get(Calendar.YEAR)),
                    APISection.ID_CATEGORIE_SECTION_INTERET_SUR_COTISATIONS_ARRIEREES_SUR_SECTION_SEPAREE);
            nouveauEntFacture.setIdExterneFacture(numFacture);

            nouveauEntFacture.setIdSoumisInteretsMoratoires(CAInteretMoratoire.CS_AUTOMATIQUE);
            nouveauEntFacture.setNonImprimable(entete.isNonImprimable());
            nouveauEntFacture.setIdCSModeImpression(entete.getIdCSModeImpression());

            nouveauEntFacture.setIdModeRecouvrement(entete.getIdModeRecouvrement());
            nouveauEntFacture.setIdAdressePaiement(entete.getIdAdressePaiement());
            nouveauEntFacture.setReferenceFacture(entete.getReferenceFacture());

            nouveauEntFacture.add(muscaTransaction);

            return nouveauEntFacture;
        } else {
            return (FAEnteteFacture) entFactureManager.getFirstEntity();
        }
    }
}
