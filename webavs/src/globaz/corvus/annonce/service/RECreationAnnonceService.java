package globaz.corvus.annonce.service;

import globaz.corvus.anakin.AnakinValidationException;
import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.annonce.REAnnoncesAPersister;
import globaz.corvus.annonce.REEtatAnnonce;
import globaz.corvus.annonce.controle.RECreationAnnonce10EmeRevisionControleLegaux;
import globaz.corvus.annonce.controle.RECreationAnnonce9EmeRevisionControleLegaux;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle10EmeRevision;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle9EmeRevision;
import globaz.corvus.annonce.formatter.RECreationAnnonceFormatterException;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.util.Enumeration;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;

public class RECreationAnnonceService {

    // public REAnnoncesAPersister creerAnnonce(REAnnonceAugmentation10EmeRevision annonceACreer)
    // throws IllegalArgumentException {
    //
    // if (annonceACreer == null) {
    // throw new IllegalArgumentException("annonceACreer is null");
    // }
    //
    // REAnnoncesAugmentationModification10Eme annonce01 = new REAnnoncesAugmentationModification10Eme();
    // REAnnoncesAugmentationModification10Eme annonce02 = new REAnnoncesAugmentationModification10Eme();
    // REAnnoncesAPersister annoncesAPersister = new REAnnoncesAPersister(annonce01, annonce02);
    //
    // // TODO
    //
    // return annoncesAPersister;
    // }
    //
    // public REAnnoncesAPersister creerAnnonce(final REAnnonceDiminution10EmeRevision annonceACreer)
    // throws IllegalArgumentException {
    //
    // if (annonceACreer == null) {
    // throw new IllegalArgumentException("annonceACreer is null");
    // }
    //
    // REAnnoncesDiminution10Eme annonce01 = new REAnnoncesDiminution10Eme();
    // REAnnoncesDiminution10Eme annonce02 = new REAnnoncesDiminution10Eme();
    // REAnnoncesAPersister annoncesAPersister = new REAnnoncesAPersister(annonce01, annonce02);
    //
    // // TODO
    //
    // return annoncesAPersister;
    // }

    public REAnnoncesAPersister creerAnnonce(REAnnoncePonctuelle10EmeRevision annonceACreer)
            throws RECreationAnnonceFormatterException {
        return creerAnnonce(annonceACreer, true);
    }

    public REAnnoncesAPersister creerAnnonce(REAnnoncePonctuelle10EmeRevision annonceACreer,
            boolean formatterLegalEnable) throws RECreationAnnonceFormatterException {

        if (annonceACreer == null) {
            throw new RECreationAnnonceFormatterException("annonce is null");
        }

        // Mise a blanc de certains champs avec le formatter légal
        if (formatterLegalEnable) {
            RECreationAnnonce10EmeRevisionControleLegaux formatterLegal = new RECreationAnnonce10EmeRevisionControleLegaux();
            try {
                formatterLegal.controlAnnonce(annonceACreer);
            } catch (Exception e) {
                throw new RECreationAnnonceFormatterException(e);
            }
        }

        // Création des BEntity
        RECreationAnnonceEntityGenerator entityGenerator = new RECreationAnnonceEntityGenerator();

        REAnnoncesAPersister annoncesAPersister = entityGenerator.create(annonceACreer);

        return annoncesAPersister;
    }

    public REAnnoncesAPersister creerAnnonce(REAnnoncePonctuelle9EmeRevision annonceACreer)
            throws RECreationAnnonceFormatterException {
        return creerAnnonce(annonceACreer, true);
    }

    public REAnnoncesAPersister creerAnnonce(REAnnoncePonctuelle9EmeRevision annonceACreer, boolean formatterLegalEnable)
            throws RECreationAnnonceFormatterException {

        if (annonceACreer == null) {
            throw new RECreationAnnonceFormatterException("annonce is null");
        }

        // Mise a blanc de certains champs avec le formatter légal
        if (formatterLegalEnable) {
            RECreationAnnonce9EmeRevisionControleLegaux formatterLegal = new RECreationAnnonce9EmeRevisionControleLegaux();
            try {
                formatterLegal.controlAnnonce(annonceACreer);
            } catch (Exception e) {
                throw new RECreationAnnonceFormatterException(e);
            }
        }
        // Création des BEntity
        RECreationAnnonceEntityGenerator entityGenerator = new RECreationAnnonceEntityGenerator();

        REAnnoncesAPersister annoncesAPersister = entityGenerator.create(annonceACreer);

        return annoncesAPersister;
    }

    // -----------------------------------------------------------------------//
    // PERSISTENCE DES ANNONCES //
    // -----------------------------------------------------------------------//

    public final void persisterAnnonce10EmeRevision(REAnnoncesAPersister annoncesAPersister, long idRenteAccordee,
            String dateDernierPaiement, /* List<Long> idsRA, */
            BSession session, BTransaction transaction) throws IllegalArgumentException, AnakinValidationException,
            Exception {

        // TODO améliorer cette histoire....
        // Contrôle d'usage
        if (annoncesAPersister == null) {
            throw new IllegalArgumentException("annonceACreer is null");
        }

        REAnnonceHeader annonce01 = annoncesAPersister.getAnnonce01();
        if (annonce01 == null) {
            throw new IllegalArgumentException("annonce01 is null");
        }
        if (!annonce01.getClass().isAssignableFrom(REAnnoncesAugmentationModification10Eme.class)
                && !annonce01.getClass().isAssignableFrom(REAnnoncesDiminution10Eme.class)) {
            throw new IllegalArgumentException("annonce01 n'est pas une instance de classe d'annonce 10ème révision");
        }

        REAnnonceHeader annonce02 = annoncesAPersister.getAnnonce02();
        if (annonce02 == null) {
            throw new IllegalArgumentException("annonce02 is null");
        }
        if (!annonce02.getClass().isAssignableFrom(REAnnoncesAugmentationModification10Eme.class)
                && !annonce02.getClass().isAssignableFrom(REAnnoncesDiminution10Eme.class)) {
            throw new IllegalArgumentException("annonce02 n'est pas une instance de classe d'annonce 10ème révision");
        }

        persister(idRenteAccordee, dateDernierPaiement, session, transaction, annonce01, annonce02);
    }

    public final void persisterAnnonce9EmeRevision(REAnnoncesAPersister annoncesAPersister, long idRenteAccordee,
            String dateDernierPaiement, BSession session, BTransaction transaction) throws IllegalArgumentException,
            AnakinValidationException, Exception {

        // TODO améliorer cette histoire....
        // Contrôle d'usage
        if (annoncesAPersister == null) {
            throw new IllegalArgumentException("annonceACreer is null");
        }

        REAnnonceHeader annonce01 = annoncesAPersister.getAnnonce01();
        if (annonce01 == null) {
            throw new IllegalArgumentException("annonce01 is null");
        }
        if (!annonce01.getClass().isAssignableFrom(REAnnoncesAugmentationModification9Eme.class)
                && !annonce01.getClass().isAssignableFrom(REAnnoncesDiminution9Eme.class)) {
            throw new IllegalArgumentException("annonce01 n'est pas une instance de classe d'annonce 10ème révision");
        }

        REAnnonceHeader annonce02 = annoncesAPersister.getAnnonce02();
        if (annonce02 == null) {
            throw new IllegalArgumentException("annonce02 is null");
        }
        if (!annonce02.getClass().isAssignableFrom(REAnnoncesAugmentationModification9Eme.class)
                && !annonce02.getClass().isAssignableFrom(REAnnoncesDiminution9Eme.class)) {
            throw new IllegalArgumentException("annonce02 n'est pas une instance de classe d'annonce 10ème révision");
        }

        persister(idRenteAccordee, dateDernierPaiement, session, transaction, annonce01, annonce02);
    }

    /**
     * Réalise la validation par Anakin et l'enregistrement des annonces en DB
     * 
     * @param idRenteAccordee
     * @param dateDernierPaiement
     * @param session
     * @param transaction
     * @param annonce01
     * @param annonce02
     * @throws Exception
     * @throws AnakinValidationException
     */
    private void persister(long idRenteAccordee, String dateDernierPaiement, BSession session,
            BTransaction transaction, REAnnonceHeader annonce01, REAnnonceHeader annonce02) throws Exception,
            AnakinValidationException {
        // TODO améliorer test
        if (JadeStringUtil.isBlank(dateDernierPaiement)) {
            throw new IllegalArgumentException("dateDernierPaiement is empty");
        }

        // Validation par Anakin et enregistrement des annonces

        // Création des 2 annonces 44, la 1 en premier (histoire d'avoir les ids dans l'ordre)
        annonce01.setSession(session);
        annonce01.add(transaction);
        annonce02.setSession(session);
        annonce02.add(transaction);

        // Mise à jour de l'annonce 44_01 avec l'id de la 44_02
        annonce01.setIdLienAnnonce(annonce02.getIdAnnonce());
        annonce01.update(transaction);

        // MMAA
        String moisRapport = ((REAnnoncesAbstractLevel1A) annonce01).getMoisRapport();

        // MM.AAAA
        moisRapport = PRDateFormater.convertDate_MMAA_to_MMxAAAA(moisRapport);
        Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, annonce01, annonce02,
                moisRapport);

        if (erreurs != null && erreurs.hasMoreElements()) {
            throw AnakinValidationException.create(erreurs);
        }

        // Création de l'annonce REANREN
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(session);
        annonceRente.setCsEtat(REEtatAnnonce.OUVERT.getCodeSystemEtatAnnonceAsString());
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS); // FIXME définir un enum
        annonceRente.setIdAnnonceHeader(annonce01.getIdAnnonce());
        annonceRente.setIdRenteAccordee(String.valueOf(idRenteAccordee));
        annonceRente.add(transaction);
    }

}
