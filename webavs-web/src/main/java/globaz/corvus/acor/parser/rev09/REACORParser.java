package globaz.corvus.acor.parser.rev09;

import globaz.commons.nss.NSUtil;
import globaz.corvus.acor.parser.REFeuilleCalculVO;
import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REAddRenteAccordee;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.regles.REDemandeRegles;
import globaz.corvus.tools.REArcFrenchValidator;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.file.parser.PRTextField;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;

/**
 * un parser qui parse le fichier annonce.pay de globaz.re.acor et en extrait les prestations
 * 
 * @author scr
 */
public class REACORParser extends REACORAbstractFlatFileParser {

    private static class IdDemandeRente {
        private Long id;

        /**
         * @return the id
         */
        public final Long getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public final void setId(Long id) {
            if (this.id == null) {
                this.id = id;
            }
        }
    }

    /**
     * ACK
     * Historiquement cette méthode retournais une liste d'ids de rentes accordées.
     * Mais cette méthode ne fait pas que parser, elle fait 50 autres choses dont la copie de la demande initiale si
     * besoin.
     * Bien évidement, en amont nous avons besoin de connaître l'id de la copie de la demande d'ou cet objet qui permet
     * de retourner les 2 informations
     * 
     * @author lga
     * 
     */
    public static class ReturnedValue {
        private List<Long> idRenteAccordees = new LinkedList<Long>();
        private Long idCopieDemande;

        /**
         * @return the idRenteAccordees
         */
        public final List<Long> getIdRenteAccordees() {
            return idRenteAccordees;
        }

        /**
         * @param idRenteAccordees the idRenteAccordees to set
         */
        public final void setIdRenteAccordees(List<Long> idRenteAccordees) {
            this.idRenteAccordees = idRenteAccordees;
        }

        /**
         * @return the idCopieDemande
         */
        public final Long getIdCopieDemande() {
            return idCopieDemande;
        }

        /**
         * @param idCopieDemande the idCopieDemande to set
         */
        public final void setIdCopieDemande(Long idCopieDemande) {
            if (this.idCopieDemande == null) {
                this.idCopieDemande = idCopieDemande;
            }
        }
    }

    private static final int CAS_NOUVEAU_CALCUL = 1;
    private static final int CAS_RECALCUL_DEMANDE_NON_VALIDEE = 3;
    private static final int CAS_RECALCUL_DEMANDE_VALIDEE = 2;

    public static void anakinValidation(final BSession session, final BTransaction transaction,
            final Map<String, String> idsArc41, final Map<String, String> idsArc44) throws Exception {

        if (idsArc41 != null) {
            Set<String> keys = idsArc41.keySet();
            Iterator<String> iter = keys.iterator();

            while (iter.hasNext()) {
                String idArc = iter.next();
                String moisRapport = idsArc41.get(idArc);
                REAnnoncesAugmentationModification9Eme arc01 = new REAnnoncesAugmentationModification9Eme();
                arc01.setSession(session);
                arc01.setIdAnnonce(idArc);
                arc01.retrieve(transaction);
                PRAssert.notIsNew(arc01, null);

                REAnnoncesAugmentationModification9Eme arc02 = null;
                if (!JadeStringUtil.isBlankOrZero(arc01.getIdLienAnnonce())) {
                    arc02 = new REAnnoncesAugmentationModification9Eme();
                    arc02.setSession(session);
                    arc02.setIdAnnonce(arc01.getIdLienAnnonce());
                    arc02.retrieve(transaction);
                    PRAssert.notIsNew(arc02, null);
                } else {
                    arc02 = null;
                }
                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, arc01, arc02,
                        moisRapport);
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }
                if (buff.length() > 0) {
                    throw new Exception(buff.toString());
                }
            }
        }

        if (idsArc44 != null) {
            Set<String> keys = idsArc44.keySet();
            Iterator<String> iter = keys.iterator();

            while (iter.hasNext()) {
                String idArc = iter.next();
                String moisRapport = idsArc44.get(idArc);

                REAnnoncesAugmentationModification10Eme arc01 = new REAnnoncesAugmentationModification10Eme();
                arc01.setSession(session);
                arc01.setIdAnnonce(idArc);
                arc01.retrieve(transaction);
                PRAssert.notIsNew(arc01, null);

                REAnnoncesAugmentationModification10Eme arc02 = null;
                if (!JadeStringUtil.isBlankOrZero(arc01.getIdLienAnnonce())) {
                    arc02 = new REAnnoncesAugmentationModification10Eme();
                    arc02.setSession(session);
                    arc02.setIdAnnonce(arc01.getIdLienAnnonce());
                    arc02.retrieve(transaction);
                    PRAssert.notIsNew(arc02, null);
                } else {
                    arc02 = null;
                }
                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, arc01, arc02,
                        moisRapport);
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }
                if (buff.length() > 0) {
                    throw new Exception("idRA : " + arc01.getIdRenteAccordee() + " - " + arc01.getNoAssAyantDroit()
                            + "\n" + buff.toString());
                }
            }
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Met à jours le champ anneeMontantRam selon le cas identifié ci-dessus. Rentes en cours : [ ] = RA (date début et
     * date de fin) x = date courante Cas 1) rentes en cours [ x ] Année montant du RAM = année courant Cas 2) rentes
     * dans le futur x [ ] Année montant du RAM = année début de la RA Cas 3) rentes rétréoactives [ ] x Année montant
     * du RAM = année(date de fin)
     */

    public static String computeAnneeMontantRAM(final RERenteAccordee ra, final BSession session) throws JAException {

        // date du dernier paiement mensuel comme ref.
        JADate dateDernierPaiement = new JADate(REPmtMensuel.getDateDernierPmt(session));

        JADate dateDebut = null;
        JADate dateFin = null;

        if (!JadeStringUtil.isEmpty(ra.getDateFinDroit())) {
            dateFin = new JADate(ra.getDateFinDroit());
        }

        if (!JadeStringUtil.isEmpty(ra.getDateDebutDroit())) {
            dateDebut = new JADate(ra.getDateDebutDroit());
        }

        int identificationCas = 1; // par défaut

        JACalendarGregorian cal = new JACalendarGregorian();

        int resultComparaison1 = cal.compare(dateDernierPaiement, dateFin);
        int resultComparaison2 = cal.compare(dateDernierPaiement, dateDebut);
        // Cas 1 : rentes en cours
        // Si pas de date de fin, ou que date de fin après date courante...
        if (

        ((dateFin == null) || (JACalendar.COMPARE_FIRSTLOWER == resultComparaison1) || (JACalendar.COMPARE_EQUALS == resultComparaison1))

                &&

                ((dateDebut != null) && ((JACalendar.COMPARE_FIRSTUPPER == resultComparaison2) || (JACalendar.COMPARE_EQUALS == resultComparaison2)))) {

            identificationCas = 1;
        }
        // Cas 2 : rentes dans le futur
        else if ((dateDebut != null) && (JACalendar.COMPARE_FIRSTLOWER == resultComparaison2)) {

            identificationCas = 2;
        } else if ((dateFin != null) && (JACalendar.COMPARE_FIRSTUPPER == resultComparaison1)) {
            identificationCas = 3;
        } else {
            throw new PRACORException("ACOR Parser : Année montant RAM :Impossible d'identifier le cas");
        }

        switch (identificationCas) {

        // Cas 1) rentes en cours
        // Année montant du RAM = année courant
            case 1:

                // mm.aaaa
                JADate dateDerPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));
                return String.valueOf(dateDerPmt.getYear());

                // Cas 2) rentes dans le futur
                // Année montant du RAM = année début de la RA
            case 2:
                JADate date = new JADate(ra.getDateDebutDroit());
                return String.valueOf(date.getYear());

                // Cas 3) rentes rétréoactives
                // Année montant du RAM = année(date de fin)
            case 3:
                date = new JADate(ra.getDateFinDroit());
                return String.valueOf(date.getYear());
            default:
                return null;
        }
    }

    /**
     * 
     * @param session
     * @param transaction
     * @param demandeSource
     * @param bc
     * @param noCasATraiter
     * @param idCopieDemande ATTENTION, C'EST UN ARGUMENT DE SORTIE
     * @return
     * @throws Exception
     */
    private static REBasesCalcul doTraiterBaseCalcul(final BSession session, final BTransaction transaction,
            final REDemandeRente demandeSource, REBasesCalcul bc, final int noCasATraiter, IdDemandeRente idCopieDemande)
            throws Exception {

        switch (noCasATraiter) {

            case CAS_NOUVEAU_CALCUL:
                RERenteCalculee rc = new RERenteCalculee();
                rc.setSession(session);
                rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
                rc.retrieve(transaction);

                if (rc.isNew()) {
                    // Avant de le remplacer, on s'assure qu'il n'y a plus aucune
                    // base de calcul liée à cet ancien ID !!!
                    if (!JadeStringUtil.isBlankOrZero(demandeSource.getIdRenteCalculee())) {
                        REBasesCalculManager mgr = new REBasesCalculManager();
                        mgr.setSession(session);
                        mgr.setForIdRenteCalculee(demandeSource.getIdRenteCalculee());
                        mgr.find(transaction, 2);
                        if (!mgr.isEmpty()) {
                            throw new Exception(
                                    "Incohérance dans les données, des bases de calculs existe pour idRenteCalculee/idDemandeRente = "
                                            + demandeSource.getIdRenteCalculee() + "/"
                                            + demandeSource.getIdDemandeRente());
                        }
                    }

                    // Rente calculée
                    rc.setIdRenteCalculee("");
                    rc.setSession(session);
                    rc.add(transaction);
                    demandeSource.setIdRenteCalculee(rc.getIdRenteCalculee());
                } else {
                    demandeSource.setIdRenteCalculee(rc.getIdRenteCalculee());
                }

                // on passe la demande dans l'etat...
                demandeSource.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                demandeSource.setDateTraitement(REACORParser.retrieveDateTraitement(demandeSource));
                demandeSource.update(transaction);

                bc.setIdRenteCalculee(rc.getIdRenteCalculee());
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                bc = REBasesCalcul.isBCExisteDeja(bc, session);
                if (bc.isNew()) {
                    bc.setReferenceDecision("0");
                    bc.add(transaction);
                } else {
                    bc.update(transaction);
                }

                break;

            case CAS_RECALCUL_DEMANDE_NON_VALIDEE:
                // Rente calculee
                rc = new RERenteCalculee();
                rc.setSession(session);
                rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
                rc.retrieve(transaction);
                if (rc.isNew()) {
                    throw new PRACORException("!!! RC not found. idRC/idDemande = "
                            + demandeSource.getIdRenteCalculee() + "/" + demandeSource.getIdDemandeRente());
                }

                // on passe la demande dans l'etat...
                demandeSource.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                demandeSource.setDateTraitement(REACORParser.retrieveDateTraitement(demandeSource));
                demandeSource.update(transaction);

                bc.setIdRenteCalculee(rc.getIdRenteCalculee());
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                bc = REBasesCalcul.isBCExisteDeja(bc, session);

                if (bc.isNew()) {
                    bc.setReferenceDecision("0");
                    bc.add(transaction);
                } else {
                    bc.update(transaction);
                }
                break;

            case CAS_RECALCUL_DEMANDE_VALIDEE:

                // On crée un clone de la demande source, comme demande 'enfant'
                REDemandeRente copieDemandeEnfant = REDemandeRegles.corrigerDemandeRente(session, transaction,
                        demandeSource);
                idCopieDemande.setId(Long.valueOf(copieDemandeEnfant.getIdDemandeRente()));
                // Rente calculee
                rc = new RERenteCalculee();
                rc.setSession(session);
                rc.add(transaction);

                copieDemandeEnfant.setIdRenteCalculee(rc.getIdRenteCalculee());
                copieDemandeEnfant.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                copieDemandeEnfant.setDateTraitement(REACORParser.retrieveDateTraitement(copieDemandeEnfant));
                copieDemandeEnfant.update(transaction);

                bc.setIdRenteCalculee(copieDemandeEnfant.getIdRenteCalculee());
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                bc.setReferenceDecision("0");
                bc.add(transaction);

                // On recharge la demandeSource comme étant celle de l'enfant.
                // Nécessaire, car la suite du traitement (ajout des RA à la
                // demande) se fait sur la demande source.
                // On veut donc les rajouter à la nouvelle demande crée.
                demandeSource.setIdDemandeRente(copieDemandeEnfant.getIdDemandeRente());
                demandeSource.retrieve(transaction);

                break;
            default:
                throw new PRACORException("Il n'est pas permis de recalculer ce cas.");
        }

        return bc;
    }

    /**
     * Importe les données de ACOR dans une annonce 41 --> Enregistrement 01.
     * 
     * @return
     */
    private static REAnnoncesAugmentationModification9Eme importAnnonce41_01(final BSession session,
            final BTransaction transaction, final String line, final Map<String, PRTextField> fields) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
        annonce41.setSession(session);

        annonce41.setCodeApplication(REACORAbstractFlatFileParser.getField(line, fields, "CODE_APPLICATION"));
        annonce41.setCodeEnregistrement01(REACORAbstractFlatFileParser.getField(line, fields, "CODE_ENREGISTREMENT"));
        annonce41.setNumeroCaisse(session.getApplication().getProperty("noCaisse"));
        annonce41.setNumeroAgence(session.getApplication().getProperty("noAgence"));
        annonce41.setNumeroAnnonce(REACORAbstractFlatFileParser.getField(line, fields, "NO_ANNONCE"));
        annonce41.setReferenceCaisseInterne(REACORAbstractFlatFileParser.getField(line, fields, "REF_INTERNE_CAISSE")
                .toUpperCase());
        annonce41.setNoAssAyantDroit(REACORAbstractFlatFileParser.getField(line, fields,
                "NO_ASS_AYANT_DROIT_PRESTATION").replaceAll("-", "756"));
        annonce41.setPremierNoAssComplementaire(REACORAbstractFlatFileParser.getField(line, fields,
                "PREMIER_NO_ASSURE_COMP").replaceAll("-", "756"));
        annonce41.setSecondNoAssComplementaire(REACORAbstractFlatFileParser.getField(line, fields,
                "SECOND_NO_ASSURE_COMP").replaceAll("-", "756"));
        annonce41.setNouveauNoAssureAyantDroit(REACORAbstractFlatFileParser.getField(line, fields,
                "NOUVEAU_NO_ASS_AYANT_DROIT").replaceAll("-", "756"));
        annonce41.setEtatCivil(REACORAbstractFlatFileParser.getField(line, fields, "ETAT_CIVIL"));
        annonce41.setIsRefugie(REACORAbstractFlatFileParser.getField(line, fields, "REFUGIE"));
        annonce41.setCantonEtatDomicile(REACORAbstractFlatFileParser.getField(line, fields, "CANTON_ETAT_DOMICILE"));
        annonce41.setGenrePrestation(REACORAbstractFlatFileParser.getField(line, fields, "GENRE_PRESTATIONS"));
        annonce41.setDebutDroit(REACORAbstractFlatFileParser.getField(line, fields, "DEBUT_DROIT"));
        annonce41.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(
                REACORAbstractFlatFileParser.getField(line, fields, "MENSUALITE_PRESTATIONS_FRANCS"), 5));
        annonce41.setMensualiteRenteOrdRemp(REACORAbstractFlatFileParser.getField(line, fields,
                "MENSUALITE_RENTE_ORDINAIRE"));
        annonce41.setFinDroit(REACORAbstractFlatFileParser.getField(line, fields, "FIN_DROIT"));
        annonce41.setCodeMutation(REACORAbstractFlatFileParser.getField(line, fields, "CODE_MUTATION"));
        // Ce champ de doit pas être stocké à la création de l'annonce. Il est
        // uniquement récupéré pour la validation ANAKIN. Il doit être supprimé avant l'insertion en DB.
        annonce41.setMoisRapport(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_RAPPORT"));

        return annonce41;
    }

    /**
     * Importe les données de ACOR dans une annonce 41 --> Enregistrement 02.
     * 
     * @return
     */
    private static REAnnoncesAugmentationModification9Eme importAnnonce41_02(final BSession session,
            final BTransaction transaction, final String line, final Map<String, PRTextField> fields) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
        annonce41.setSession(session);

        annonce41.setCodeApplication(REACORAbstractFlatFileParser.getField(line, fields, "CODE_APPLICATION"));
        annonce41.setCodeEnregistrement01(REACORAbstractFlatFileParser.getField(line, fields, "CODE_ENREGISTREMENT"));
        annonce41.setRamDeterminant(REACORAbstractFlatFileParser.getField(line, fields, "RAM_DETERMINANT"));
        annonce41.setDureeCotPourDetRAM(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COT_POUR_DET_RAM"));
        annonce41.setAnneeNiveau(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_NIVEAU"));
        annonce41.setRevenuPrisEnCompte(REACORAbstractFlatFileParser.getField(line, fields, "REVENU_PRIS_EM_COMPTE"));
        annonce41.setEchelleRente(REACORAbstractFlatFileParser.getField(line, fields, "ECHELLE_RENTE"));
        annonce41.setDureeCoEchelleRenteAv73(REACORAbstractFlatFileParser.getField(line, fields,
                "DUREE_COT_PRISE_EN_COMPTE_AVANT_1973"));
        annonce41.setDureeCoEchelleRenteDes73(REACORAbstractFlatFileParser.getField(line, fields,
                "DUREE_COT_PRISE_EN_COMPTE_DES_1973"));
        annonce41.setDureeCotManquante48_72(REACORAbstractFlatFileParser.getField(line, fields,
                "PRISE_EN_COMPTE_COT_MANQUANTE_48_72"));
        annonce41.setAnneeCotClasseAge(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_COT_CLASSE_AGE"));
        annonce41.setDureeAjournement(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_AJOURNEMENT"));
        annonce41.setSupplementAjournement(REACORAbstractFlatFileParser
                .getField(line, fields, "SUPPLEMENT_AJOURNEMENT"));
        annonce41.setDateRevocationAjournement(REACORAbstractFlatFileParser.getField(line, fields,
                "DATE_REVOCATION_AJOURNEMENT"));
        annonce41.setIsLimiteRevenu(REACORAbstractFlatFileParser.getField(line, fields, "LIMITE_REVENU"));
        annonce41.setIsMinimumGaranti(REACORAbstractFlatFileParser.getField(line, fields, "MINIMUM_GARANTI"));
        annonce41.setOfficeAICompetent(REACORAbstractFlatFileParser.getField(line, fields, "OFFICE_AI_AYANT_DROIT"));
        annonce41.setOfficeAiCompEpouse(REACORAbstractFlatFileParser.getField(line, fields, "OFFICE_AI_EPOUSE"));
        annonce41.setDegreInvalidite(REACORAbstractFlatFileParser
                .getField(line, fields, "DEGRE_INVALIDITE_AYANT_DROIT"));
        annonce41.setDegreInvaliditeEpouse(REACORAbstractFlatFileParser.getField(line, fields,
                "DEGRE_INVALIDITE_EPOUSE"));
        annonce41.setCodeInfirmite(REACORAbstractFlatFileParser.getField(line, fields, "CODE_INFIRMITE_AYANT_DROIT"));
        annonce41.setCodeInfirmiteEpouse(REACORAbstractFlatFileParser.getField(line, fields, "CODE_INFIRMITE_EPOUSE"));
        annonce41.setSurvenanceEvenAssure(REACORAbstractFlatFileParser.getField(line, fields,
                "SURVENANCE_EVT_ASS_AYANT_DROIT"));
        annonce41.setSurvenanceEvtAssureEpouse(REACORAbstractFlatFileParser.getField(line, fields,
                "SURVENANCE_EVT_ASS_EPOUSE"));
        annonce41.setAgeDebutInvalidite(REACORAbstractFlatFileParser.getField(line, fields,
                "AGE_DEBUT_INVALIDITE_AYANT_DROIT"));
        annonce41.setAgeDebutInvaliditeEpouse(REACORAbstractFlatFileParser.getField(line, fields,
                "AGE_DEBUT_INVALIDITE_EPOUSE"));
        annonce41.setGenreDroitAPI(REACORAbstractFlatFileParser.getField(line, fields, "GENRE_DROIT_API"));
        annonce41.setReduction(REACORAbstractFlatFileParser.getField(line, fields, "REDUCTION"));
        annonce41.setCasSpecial1(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_1"));
        annonce41.setCasSpecial2(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_2"));
        annonce41.setCasSpecial3(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_3"));
        annonce41.setCasSpecial4(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_4"));
        annonce41.setCasSpecial5(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_5"));
        annonce41.setDureeCotManquante73_78(REACORAbstractFlatFileParser.getField(line, fields,
                "PRISE_EN_COMPTE_DUREES_COT_MANQUANTES_73_78"));
        annonce41.setRevenuAnnuelMoyenSansBTE(REACORAbstractFlatFileParser.getField(line, fields, "RAM_SANS_BTE"));
        annonce41.setBteMoyennePrisEnCompte(REACORAbstractFlatFileParser.getField(line, fields, "BTE_MOYENNES"));
        annonce41.setNombreAnneeBTE(REACORAbstractFlatFileParser.getField(line, fields, "NBRE_ANNEE_BTE"));

        return annonce41;
    }

    /**
     * Importe les données de ACOR dans une annonce 44 --> Enregistrement 01.
     * 
     * @return
     */
    private static REAnnoncesAugmentationModification10Eme importAnnonce44_01(final BSession session,
            final BTransaction transaction, final String line, final Map<String, PRTextField> fields) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setSession(session);
        annonce44.setCodeApplication(REACORAbstractFlatFileParser.getField(line, fields, "CODE_APPLICATION"));
        annonce44.setCodeEnregistrement01(REACORAbstractFlatFileParser.getField(line, fields, "CODE_ENREGISTREMENT"));
        annonce44.setNumeroCaisse(session.getApplication().getProperty("noCaisse"));
        annonce44.setNumeroAgence(session.getApplication().getProperty("noAgence"));
        annonce44.setNumeroAnnonce(REACORAbstractFlatFileParser.getField(line, fields, "NO_ANNONCE"));
        annonce44.setReferenceCaisseInterne(REACORAbstractFlatFileParser.getField(line, fields, "REF_INTERNE_CAISSE")
                .toUpperCase());
        annonce44.setNoAssAyantDroit(REACORAbstractFlatFileParser.getField(line, fields, "NO_ASS_AYANT_DROIT")
                .replaceAll("-", "756"));

        if (!JadeStringUtil.isBlankOrZero(REACORAbstractFlatFileParser.getField(line, fields, "PREMIER_NO_ASS_COMP")
                .replaceAll("-", "756"))) {
            PRTiersWrapper tier1 = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser
                    .getField(line, fields, "PREMIER_NO_ASS_COMP").replaceAll("-", "756")));

            if (tier1 == null) {
                annonce44.setPremierNoAssComplementaire("00000000000");
            } else {
                annonce44.setPremierNoAssComplementaire(REACORAbstractFlatFileParser.getField(line, fields,
                        "PREMIER_NO_ASS_COMP").replaceAll("-", "756"));
            }
        }

        if (!JadeStringUtil.isBlankOrZero(REACORAbstractFlatFileParser.getField(line, fields, "SECOND_NO_ASS_COMP")
                .replaceAll("-", "756"))) {
            PRTiersWrapper tier2 = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser
                    .getField(line, fields, "SECOND_NO_ASS_COMP").replaceAll("-", "756")));

            if (tier2 == null) {
                annonce44.setSecondNoAssComplementaire("00000000000");
            } else {
                annonce44.setSecondNoAssComplementaire(REACORAbstractFlatFileParser.getField(line, fields,
                        "SECOND_NO_ASS_COMP").replaceAll("-", "756"));
            }
        }

        annonce44.setNouveauNoAssureAyantDroit(REACORAbstractFlatFileParser.getField(line, fields,
                "NOUVEAU_NO_ASS_AYANT_DROIT").replaceAll("-", "756"));
        annonce44.setEtatCivil(REACORAbstractFlatFileParser.getField(line, fields, "ETAT_CIVIL"));
        annonce44.setIsRefugie(REACORAbstractFlatFileParser.getField(line, fields, "REFUGIE"));
        annonce44.setCantonEtatDomicile(REACORAbstractFlatFileParser.getField(line, fields, "CANTON_ETAT_DOMICILE"));
        annonce44.setGenrePrestation(REACORAbstractFlatFileParser.getField(line, fields, "GENRE_PRESTATION"));
        annonce44.setDebutDroit(REACORAbstractFlatFileParser.getField(line, fields, "DEBUT_DROIT"));
        annonce44.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(
                REACORAbstractFlatFileParser.getField(line, fields, "MENSUALITE_PRESTATION_FRANCS"), 5));
        annonce44.setFinDroit(REACORAbstractFlatFileParser.getField(line, fields, "FIN_DROIT"));
        annonce44.setCodeMutation(REACORAbstractFlatFileParser.getField(line, fields, "CODE_MUTATION"));

        // Ce champ de doit pas être stocké à la création de l'annonce. Il est
        // uniquement récupéré pour la validation ANAKIN. Il doit être supprimé avant l'insertion en DB.
        annonce44.setMoisRapport(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_RAPPORT"));
        return annonce44;
    }

    /**
     * Importe les données de ACOR dans une annonce 44 --> Enregistrement 02.
     * 
     * @return
     */
    private static REAnnoncesAugmentationModification10Eme importAnnonce44_02(final BSession session,
            final BTransaction transaction, final String line, final Map<String, PRTextField> fields) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setSession(session);

        annonce44.setCodeApplication(REACORAbstractFlatFileParser.getField(line, fields, "CODE_APPLICATION"));
        annonce44.setCodeEnregistrement01(REACORAbstractFlatFileParser.getField(line, fields, "CODE_ENREGISTREMENT"));
        annonce44.setEchelleRente(REACORAbstractFlatFileParser.getField(line, fields, "ECHELLE_RENTE"));
        annonce44.setDureeCoEchelleRenteAv73(REACORAbstractFlatFileParser.getField(line, fields,
                "DUREE_COT_PRISE_EN_COMPTE_AVANT_1973"));
        annonce44.setDureeCoEchelleRenteDes73(REACORAbstractFlatFileParser.getField(line, fields,
                "DUREE_COT_PRISE_EN_COMPTE_DES_1973"));
        annonce44.setDureeCotManquante48_72(REACORAbstractFlatFileParser.getField(line, fields,
                "PRISE_EN_COMPTE_COT_MANQUANTES_48_72"));
        annonce44.setDureeCotManquante73_78(REACORAbstractFlatFileParser.getField(line, fields,
                "PRISE_EN_COMPTE_COT_MANQUANTES_73_78"));
        annonce44.setAnneeCotClasseAge(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_COT_CLASSE_AGE"));
        annonce44.setRamDeterminant(REACORAbstractFlatFileParser.getField(line, fields, "RAM_DETERMINANT"));
        annonce44.setDureeCotPourDetRAM(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COT_POUR_DET_RAM"));
        annonce44.setNombreAnneeBTE(REACORAbstractFlatFileParser.getField(line, fields, "NBRE_ANNEE_BTE"));

        annonce44.setCodeRevenuSplitte(REACORAbstractFlatFileParser.getField(line, fields, "CODE_REVENUS_SPLITTES"));
        annonce44.setAnneeNiveau(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_NIVEAU"));

        annonce44.setNbreAnneeBTA(REACORAbstractFlatFileParser.getField(line, fields, "NBRE_ANNEE_BTA"));
        annonce44.setNbreAnneeBonifTrans(REACORAbstractFlatFileParser.getField(line, fields,
                "NBRE_ANNEE_BONIF_TRANSITOIRES"));
        annonce44.setOfficeAICompetent(REACORAbstractFlatFileParser.getField(line, fields, "OFFICE_AI"));
        annonce44.setDegreInvalidite(REACORAbstractFlatFileParser.getField(line, fields, "DEGRE_INVALIDITE"));
        annonce44.setCodeInfirmite(REACORAbstractFlatFileParser.getField(line, fields, "CODE_INFIRMITE"));
        annonce44.setSurvenanceEvenAssure(REACORAbstractFlatFileParser.getField(line, fields, "SURVENANCE_EVT_ASSURE"));
        annonce44.setAgeDebutInvalidite(REACORAbstractFlatFileParser.getField(line, fields, "AGE_DEBUT_INVALIDITE"));
        annonce44.setGenreDroitAPI(REACORAbstractFlatFileParser.getField(line, fields, "GENRE_DROIT_API"));
        annonce44.setReduction(REACORAbstractFlatFileParser.getField(line, fields, "REDUCTION"));
        annonce44.setCasSpecial1(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_1"));
        annonce44.setCasSpecial2(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_2"));
        annonce44.setCasSpecial3(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_3"));
        annonce44.setCasSpecial4(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_4"));
        annonce44.setCasSpecial5(REACORAbstractFlatFileParser.getField(line, fields, "CAS_SPECIAL_5"));
        annonce44.setNbreAnneeAnticipation(REACORAbstractFlatFileParser.getField(line, fields,
                "NBRE_ANNEES_ANTICIPATION"));
        annonce44.setReductionAnticipation(REACORAbstractFlatFileParser
                .getField(line, fields, "REDUCTION_ANTICIPATION"));
        annonce44.setDateDebutAnticipation(REACORAbstractFlatFileParser.getField(line, fields,
                "DATE_DEBUT_ANTICIPATION"));
        annonce44.setDureeAjournement(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_AJOURNEMENT"));
        annonce44.setSupplementAjournement(REACORAbstractFlatFileParser
                .getField(line, fields, "SUPPLEMENT_AJOURNEMENT"));
        annonce44.setDateRevocationAjournement(REACORAbstractFlatFileParser.getField(line, fields,
                "DATE_REVOCATION_AJOURNEMENT"));
        annonce44.setIsSurvivant(REACORAbstractFlatFileParser.getField(line, fields, "CODE_SURVIVANT_INVALIDE"));

        return annonce44;
    }

    /**
     * Importe les données de ACOR dans une base de calcul.
     * 
     * @return
     */
    private static REBasesCalcul importBaseCalcul(final BSession session, final String line,
            final Map<String, PRTextField> fields) {
        String droitApplique = REACORAbstractFlatFileParser.getField(line, fields, "DROIT_APPLIQUE");
        REBasesCalcul bc = null;
        if (session.getCode(IREDemandeRente.CS_REVISION_10EME_REVISION).equals(droitApplique)) {
            bc = new REBasesCalculDixiemeRevision();
        } else {
            bc = new REBasesCalculNeuviemeRevision();
        }
        bc.setSession(session);

        String nssTiersBaseCalcul = REACORAbstractFlatFileParser.getField(line, fields, "NSS");

        try {
            PRTiersWrapper tiersBaseCalcul = PRTiersHelper.getTiers(session,
                    NSUtil.formatAVSUnknown(nssTiersBaseCalcul));

            if (tiersBaseCalcul != null) {
                bc.setIdTiersBaseCalcul(tiersBaseCalcul.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            }

        } catch (Exception e) {
        }

        bc.setAnneeBonifTacheAssistance(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_BONIF_TACHE_ASSIST"));
        bc.setAnneeBonifTacheEduc(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_BONIF_TACHE_EDUC"));
        bc.setAnneeBonifTransitoire(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_BONIF_TRANSITOIRE"));
        bc.setAnneeCotiClasseAge(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_COTI_CLASSE_AGE"));
        bc.setAnneeDeNiveau(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_NIVEAU"));
        bc.setAnneeTraitement(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_TRAITEMENT"));
        bc.setCleInfirmiteAyantDroit(REACORAbstractFlatFileParser.getField(line, fields, "CLE_INFIRM_AYANT_DROIT"));
        bc.setDroitApplique(REACORAbstractFlatFileParser.getField(line, fields, "DROIT_APPLIQUE"));
        bc.setCodeOfficeAi(REACORAbstractFlatFileParser.getField(line, fields, "OFFICE_AI_COMPETANT_AYANT_DROIT"));
        bc.setResultatComparatif(REACORAbstractFlatFileParser.getField(line, fields, "RESULTAT_COMPARAISON"));
        bc.setTypeCalculComparatif(REACORAbstractFlatFileParser.getField(line, fields, "TYPE_CALCUL_COMPARATIF"));
        bc.setDegreInvalidite(REACORAbstractFlatFileParser.getField(line, fields, "DEGRE_INVALIDITE_AYANT_DROIT"));
        bc.setDureeCotiAvant73(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COTI_AV_73"));
        bc.setDureeCotiDes73(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COTI_DES_73"));
        bc.setEchelleRente(REACORAbstractFlatFileParser.getField(line, fields, "ECHELLE_RENTE"));
        bc.setFacteurRevalorisation(REACORAbstractFlatFileParser.getField(line, fields, "FACTEUR_REVALORISATION"));
        bc.setInvaliditePrecoce(PRStringUtils.getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line,
                fields, "INVALIDITE_PRECOCE_AYANT_DROIT")));
        bc.setLimiteRevenu(PRStringUtils.getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line, fields,
                "LIMITE_REVENU")));
        bc.setMinimuGaranti(PRStringUtils.getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line, fields,
                "MINIMUM_GARANTI")));
        bc.setMoisAppointsAvant73(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_APPOINT_AV_73"));
        bc.setMoisAppointsDes73(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_APPOINT_DES_73"));
        bc.setMoisCotiAnneeOuvertDroit(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_COTI_ANNEE_OUVERTURE"));
        bc.setMontantMaxR10Ech44(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT_MAX_R10_E44"));
        bc.setIsPartageRevenuActuel(PRStringUtils.getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line,
                fields, "PARTAGE_REVENU")));
        bc.setPeriodeAssEtrangerAv73(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_ASS_ETR_AV_73"));
        bc.setPeriodeAssEtrangerDes73(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_ASS_ETR_DES73"));
        bc.setPeriodeJeunesse(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_JEUNESSE"));
        bc.setPeriodeMariage(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_MARIAGE"));
        bc.setRevenuAnnuelMoyen(REACORAbstractFlatFileParser.getField(line, fields, "RAM"));
        bc.setRevenuJeunesse(REACORAbstractFlatFileParser.getField(line, fields, "REVENU_JEUNESSE"));
        bc.setRevenuPrisEnCompte(REACORAbstractFlatFileParser.getField(line, fields, "REVENU_PRIS_COMPTE"));
        bc.setRevenuSplitte(PRStringUtils.getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line, fields,
                "REVENU_SPLITTE")));
        bc.setSupplementCarriere(REACORAbstractFlatFileParser.getField(line, fields, "POURCENT_SUPP_CARRIERE"));
        bc.setSurvenanceEvtAssAyantDroit(JadeStringUtil.removeChar(PRDateFormater
                .convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line, fields,
                        "SURVENANCE_EVEN_ASSURE_AYANT_DROIT")), '.'));
        bc.setDureeRevenuAnnuelMoyen(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COTI_RAM"));
        bc.setReferenceDecision("0");

        if (bc instanceof REBasesCalculNeuviemeRevision) {

            ((REBasesCalculNeuviemeRevision) bc).setBonificationTacheEducative(REACORAbstractFlatFileParser.getField(
                    line, fields, "BONIF_TACHE_EDUCATIVE"));
            ((REBasesCalculNeuviemeRevision) bc).setNbrAnneeEducation(REACORAbstractFlatFileParser.getField(line,
                    fields, "NOMBRE_ANNEE_EDUCATION"));
            ((REBasesCalculNeuviemeRevision) bc).setCodeOfficeAiEpouse(REACORAbstractFlatFileParser.getField(line,
                    fields, "OFFICE_AI_COMPETANT_EPOUSE"));
            ((REBasesCalculNeuviemeRevision) bc).setDegreInvaliditeEpouse(REACORAbstractFlatFileParser.getField(line,
                    fields, "DEGRE_INVALIDITE_EPOUSE"));
            ((REBasesCalculNeuviemeRevision) bc).setCleInfirmiteEpouse(REACORAbstractFlatFileParser.getField(line,
                    fields, "CLE_INFIRM_EPOUSE"));
            ((REBasesCalculNeuviemeRevision) bc).setSurvenanceEvenementAssureEpouse(PRDateFormater
                    .convertDate_MMAA_to_AAAAMM(REACORAbstractFlatFileParser.getField(line, fields,
                            "SURVENANCE_EVEN_ASSURE_EPOUSE")));
            ((REBasesCalculNeuviemeRevision) bc).setInvaliditePrecoceEpouse(PRStringUtils
                    .getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line, fields,
                            "INVALIDITE_PRECOCE_EPOUSE")));

        }
        return bc;
    }

    /**
     * Importe les données de ACOR dans une rente accordée.
     * 
     * @return
     */
    private static REPrestationDue importPrestationsDues(final BSession session, final BTransaction transaction,
            final String line, final Map<String, PRTextField> fields, final RERenteAccordee ra) throws Exception {

        REPrestationDue pd = new REPrestationDue();
        pd.setSession(session);
        if (line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL)) {
            pd.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
            pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
            if (line.length() > 57) {
                pd.setMontantReductionAnticipation(REACORAbstractFlatFileParser.getField(line, fields,
                        "MONTANT_REDUC_ANTICI"));
                if (line.length() > 60) {
                    pd.setMontantSupplementAjournement(REACORAbstractFlatFileParser.getField(line, fields,
                            "MONTANT_SUPPL_AJOURN"));
                } else {
                    pd.setMontantSupplementAjournement("0");
                }
            } else {
                pd.setMontantReductionAnticipation("0");
                pd.setMontantSupplementAjournement("0");
            }

        } else if (line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF)) {
            pd.setCsType(IREPrestationDue.CS_TYPE_MNT_TOT);

            String dateDernierPmt = REPmtMensuel.getDateDernierPmt(session);
            String dateDebutPmt = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(REACORAbstractFlatFileParser.getField(
                    line, fields, "DEBUT_PAIEMENT"));
            JADate jDateDateDernierPmt = new JADate(dateDernierPmt);
            JADate jDateDateDebutPmt = new JADate(dateDebutPmt);

            JACalendar cal = new JACalendarGregorian();
            if (cal.compare(jDateDateDebutPmt, jDateDateDernierPmt) != JACalendar.COMPARE_EQUALS) {

                throw new Exception("Le calcul dans ACOR doit se faire avec une date de traitement du mois courant.");
            }

        }
        pd.setCsTypePaiement(null);
        pd.setDateDebutPaiement(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(REACORAbstractFlatFileParser.getField(
                line, fields, "DEBUT_PAIEMENT")));
        pd.setMontant(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT"));
        pd.setRam(REACORAbstractFlatFileParser.getField(line, fields, "RAM"));

        return pd;
    }

    /**
     * Importe les données de ACOR dans une rente accordée.
     * 
     * @return
     */
    private static RERenteAccordee importRenteAccordee(final BSession session, final BTransaction transaction,
            final REDemandeRente demande, final String line, final Map<String, PRTextField> fields) throws Exception {
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setAnneeAnticipation(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_ANTICIPATION"));
        ra.setCodeAuxilliaire(REACORAbstractFlatFileParser.getField(line, fields, "CODE_AUXILIAIRE"));

        // code cas speciaux maximum 5 de 2 position cadre a droite et complete
        // par des blancs
        String casSpeciaux = REACORAbstractFlatFileParser.getField(line, fields, "CODE_CAS_SPECIAUX");
        // suppression des espaces
        casSpeciaux = casSpeciaux.trim();
        // tan qu'il y a des paires de caracteres
        int pos = 1;
        while ((casSpeciaux.length() >= 2) && (pos <= 5)) {
            String cs = casSpeciaux.substring(0, 2);
            casSpeciaux = casSpeciaux.substring(2, casSpeciaux.length());

            // appel dynamique de la methode
            Class<?>[] PARAMS = new Class[] { String.class };
            Method method = ra.getClass().getMethod("setCodeCasSpeciaux" + pos, PARAMS);
            method.invoke(ra, new Object[] { cs });

            pos++;
        }

        String codeMutation = REACORAbstractFlatFileParser.getField(line, fields, "CODE_MUTATION");

        if (!JadeStringUtil.isBlankOrZero(codeMutation)) {
            ra.setCodeMutation(codeMutation);
        }

        String fractionRente = REACORAbstractFlatFileParser.getField(line, fields, "FRACTION_RENTE_AI");

        // ra.setFractionRente(PRACORConst.caFractionRenteToCS(session,
        // fractionRente));
        ra.setFractionRente(fractionRente);

        ra.setCodePrestation(REACORAbstractFlatFileParser.getField(line, fields, "CODE_PRESTATION"));

        ra.setCodeSurvivantInvalide(REACORAbstractFlatFileParser.getField(line, fields, "CODE_SURVIVANT"));
        ra.setCsEtatCivil(PRACORConst.caEtatCivilToCS(session,
                REACORAbstractFlatFileParser.getField(line, fields, "CODE_ETAT_CIVIL")));

        ra.setPrescriptionAppliquee(REACORAbstractFlatFileParser.getField(line, fields, "PRESCRIPTION_APPLIQUEE"));

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, demande.loadDemandePrestation(transaction).getIdTiers());

        ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(demande.loadDemandePrestation(transaction)
                .getIdTiers());

        String nssTiersBaseCalcul = REACORAbstractFlatFileParser.getField(line, fields, "NSS_BASE_CALCUL");

        PRTiersWrapper tiersBaseCalcul = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(nssTiersBaseCalcul));
        if (tiersBaseCalcul != null) {
            ra.setIdTiersBaseCalcul(tiersBaseCalcul.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        String nssBeneficiaire = REACORAbstractFlatFileParser.getField(line, fields, "NSS_BENEFICIAIRE");

        for (int i = 0; i < mf.length; i++) {
            ISFMembreFamilleRequerant membre = mf[i];
            String nssNF = membre.getNss();
            // On supprime les '.'
            nssNF = JadeStringUtil.change(nssNF, ".", "");

            if ((nssBeneficiaire != null) && nssBeneficiaire.equals(nssNF)) {
                ra.setCsRelationAuRequerant(membre.getRelationAuRequerant());
            }
        }

        ra.setSupplementVeuvage(REACORAbstractFlatFileParser.getField(line, fields, "SUPPL_VEUVAGE"));
        ra.setDateDebutAnticipation(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(PRDateFormater
                .convertDate_MMAA_to_AAAAMM(REACORAbstractFlatFileParser.getField(line, fields,
                        "DATE_DEBUT_ANTICIPATION"))));
        ra.setDateDebutDroit(PRDateFormater.convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line,
                fields, "DEBUT_DROIT")));
        ra.setDateFinDroit(PRDateFormater.convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line,
                fields, "FIN_DROIT")));

        String d = REACORAbstractFlatFileParser.getField(line, fields, "FIN_DROIT_ECHEANCE");

        if (!JadeStringUtil.isBlankOrZero(d)) {
            // Date echéance focrément dans le future...
            if ((d != null) && (d.length() == 4)) {
                String AA = d.substring(2, 4);
                String MM = d.substring(0, 2);

                d = MM + ".20" + AA;
            }

            // On rajoute 1 mois à la date d'echeance par rapport à celle
            // remontée de ACOR
            if (!JadeStringUtil.isBlankOrZero(d)) {
                ra.setDateFinDroitPrevueEcheance(d);
                ra.setDateEcheance(d);

                // JADate jad = new JADate(d);
                // JACalendar cal = new JACalendarGregorian();
                // jad = cal.addMonths(jad, 1);
                // ra.setDateEcheance(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(jad.toStrAMJ()));

            }
        }
        ra.setDateRevocationAjournement(PRDateFormater.convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser
                .getField(line, fields, "DATE_REVOCATION_AJOURNEMENT")));
        ra.setDureeAjournement(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_AJOURNEMENT"));

        PRTiersWrapper wrapper = PRTiersHelper.getTiers(session,
                NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line, fields, "NSS_BENEFICIAIRE")));
        if (wrapper == null) {
            throw new Exception("Aucun tiers trouvé pour le NSS : "
                    + NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line, fields, "NSS_BENEFICIAIRE")));
        }

        ra.setIdTiersBeneficiaire(wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

        wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line,
                fields, "PREMIER_NSS_COMPLEMENTAIRE")));
        if (wrapper != null) {
            ra.setIdTiersComplementaire1(wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line,
                fields, "SECOND_NSS_COMPLEMENTAIRE")));
        if (wrapper != null) {
            ra.setIdTiersComplementaire2(wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        ra.setMontantPrestation(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT_PRESTATION"));
        ra.setMontantReducationAnticipation(REACORAbstractFlatFileParser.getField(line, fields,
                "MONTANT_REDUCT_ANTICIPATION"));
        ra.setMontantRenteOrdiRemplacee(REACORAbstractFlatFileParser.getField(line, fields,
                "MONTANT_RENTE_ORDINAIRE_REMPL"));
        ra.setReductionFauteGrave(REACORAbstractFlatFileParser.getField(line, fields, "REDUCTION_FAUTE_GRAVE"));
        ra.setCodeRefugie(REACORAbstractFlatFileParser.getField(line, fields, "CODE_REFUGIE"));
        ra.setSupplementAjournement(REACORAbstractFlatFileParser.getField(line, fields, "SUPPLEMENT_AJOURNEMENT"));
        ra.setIsTraitementManuel(Boolean.FALSE);

        ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        ra.setAnneeMontantRAM(REACORParser.computeAnneeMontantRAM(ra, session));
        return ra;
    }

    /**
     * Parse le fichier annonce.pay (reader) et insere dans la base les prestations qui s'y trouvent.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param ijCalculee
     *            DOCUMENT ME!
     * @param reader
     *            DOCUMENT ME!
     * @return une liste jamais nulle, peut-etre vide des ids des rentes accordées qui ont ete importees.
     * @throws Exception
     */
    public static final ReturnedValue parse(final BSession session, final BITransaction transaction,
            final REDemandeRente demandeSource, final Reader reader, final int noCasATraiter) throws PRACORException {
        ReturnedValue returnedValue = new ReturnedValue();
        BufferedReader bufferedReader = new BufferedReader(reader);
        Map<String, PRTextField> fields;

        try {
            // changer de parser si NNSS ou NAVS
            PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(demandeSource.getIdDemandePrestation());
            demande.retrieve();

            if (demande.isNew()) {
                throw new PRACORException("Demande prestation non trouvée !!");
            } else {
                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, demande.getIdTiers());
                if (null != tiers) {
                    // si navs
                    if (JadeStringUtil.removeChar(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), '.')
                            .length() == 11) {
                        REACORAbstractFlatFileParser.loadConfigurations_NAVS();
                        // si nnss
                    } else {
                        REACORAbstractFlatFileParser.loadConfigurations_NSS();
                    }
                } else {
                    throw new PRACORException("Tiers de la demande prestation non trouvé !!");
                }
            }

            // --------------------------------------------------------------------------------------------------------------------
            // Cas de figure :
            //
            // 1) CAS_NOUVEAU_CALCUL (Standard)
            // ====================================================================================================================
            // ====================================================================================================================
            // Demande source (ENREGISTRE) --> Calculer
            //
            // Résutalt :
            // =============================
            //
            // Demande source
            // |__________ Base Calcul
            // |__________ RA1
            // |__________ RA2
            //
            //
            //
            // 2) CAS_RECALCUL_DEMANDE_VALIDEE (Recalcul à partir d'une demande
            // existante, VALIDE ou PAYE)
            // ====================================================================================================================
            // ====================================================================================================================
            //
            // Demande source#1 (PAYE ou VALIDE) --> Calculer
            // |__________ Base Calcul
            // |__________ RA1
            // |__________ RA2
            //
            // Résutalt :
            // =============================
            // A l'importation des données de ACOR, on va créer une nouvelle
            // demande.
            //
            // Demande source#1
            // |__________ Base Calcul
            // |__________ RA1
            // |__________ RA2
            //
            // Demande #2 (idParent==Demande source#1)
            // |__________ Base Calcul
            // |__________ RA3
            // |__________ RA4
            //
            //
            // Les rentes accordées RA3 et RA4 vont annuler RA1 et/ou RA2. Ce
            // traitement n'est pas automatisé
            // pour le moment.
            //
            //
            //
            //
            //
            // 3) CAS_RECALCUL_DEMANDE_NON_VALIDEE (Recalcul à partir d'une
            // demande existante, non encore validée. ENREGISTRE, AU CALCUL)
            // ====================================================================================================================
            // ====================================================================================================================
            //
            // Demande source#1 (CALCULE) --> Calculer
            // |__________ Base Calcul 1
            // |__________ RA1
            // |__________ RA2
            //
            // Résutalt :
            // =============================
            // A l'importation des données de ACOR, on va créer les BC, RA... et
            // supprimer les anciennes, si existantent.
            //
            // Demande source#1
            // |__________ Base Calcul 2
            // |__________ RA3
            // |__________ RA4
            //
            //
            //
            // --------------------------------------------------------------------------------------------------------------------

            REBasesCalcul bc = null;
            RERenteAccordee ra = null;
            String line = "be there or be square";
            List<Long> ids = new LinkedList<Long>();
            int level = 0;
            boolean isAnnoncePayForDemandeRente = false;

            /*
             * Toutes les demande non-validées de la famille sont réinitialisées au début de la remontée du calcul ACOR
             * Il faut s'assurer à ce stade que l'on à bien une rente calculée
             */
            RERenteCalculee rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
            rc.retrieve(transaction);
            if (rc.isNew()) {
                rc.save();
                demandeSource.setIdRenteCalculee(rc.getIdRenteCalculee());
            }

            switch (noCasATraiter) {
                case CAS_NOUVEAU_CALCUL:
                    break;
                case CAS_RECALCUL_DEMANDE_NON_VALIDEE:
                    break;
                case CAS_RECALCUL_DEMANDE_VALIDEE:
                    break;
                default:
                    throw new PRACORException("Il n'est pas permis de recalculer ce cas.");
            }

            boolean isDemandeCloneCreated = false;
            while (line != null) {

                /*
                 * Niveau 0..50
                 */
                if (level <= 50) {
                    line = REACORParser.readRelevantLine(bufferedReader);
                    if (line == null) {
                        break;
                    }
                }

                /*
                 * Niveau 51..100
                 */
                if (level <= 100) {
                    if (line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_CALCUL)) {
                        fields = REACORAbstractFlatFileParser
                                .getConfiguration(REACORAbstractFlatFileParser.CODE_BASE_CALCUL);
                        bc = REACORParser.importBaseCalcul(session, line, fields);

                        // Si la demande à déjà été clonée et que l'on passe une
                        // 2ème fois dans le traitement de la BC,
                        // on la traite comme s'il s'agissait d'un cas de
                        // nouveau calcul pour éviter de créer un 2ème clone de
                        // la demande.
                        /**
                         * ACK, nous avons besoin de connaître l'id de la copie de la demande qui est fait dans la
                         * méthode doTraiterBaseCalcul(..)
                         * Et oui doTraiterBaseCalcul(..) fait également plein de chose comme son nom ne l'indique
                         * pas....
                         */
                        IdDemandeRente idCopieDemande = new IdDemandeRente();
                        if ((noCasATraiter == REACORParser.CAS_RECALCUL_DEMANDE_VALIDEE) && isDemandeCloneCreated) {
                            bc = REACORParser.doTraiterBaseCalcul(session, (BTransaction) transaction, demandeSource,
                                    bc, REACORParser.CAS_NOUVEAU_CALCUL, idCopieDemande);
                        } else {
                            bc = REACORParser.doTraiterBaseCalcul(session, (BTransaction) transaction, demandeSource,
                                    bc, noCasATraiter, idCopieDemande);
                        }
                        returnedValue.setIdCopieDemande(idCopieDemande.getId());
                        if (noCasATraiter == REACORParser.CAS_RECALCUL_DEMANDE_VALIDEE) {
                            isDemandeCloneCreated = true;
                        }
                    }
                }

                /**
                 * Niveau 101..150
                 */
                if (level <= 150) {
                    line = REACORParser.readRelevantLine(bufferedReader);
                }

                /**
                 * Niveau 151..200
                 */
                if (level <= 200) {
                    if ((line != null)
                            && (line.startsWith(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE) || line
                                    .startsWith(REACORAbstractFlatFileParser.CODE_AJOURNEMENT))) {
                        // importer les rentes accordées

                        fields = REACORAbstractFlatFileParser
                                .getConfiguration(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE);
                        boolean isAjournement = false;

                        if (line.startsWith(REACORAbstractFlatFileParser.CODE_AJOURNEMENT)) {
                            isAjournement = true;
                        }

                        ra = REACORParser.importRenteAccordee(session, (BTransaction) transaction, demandeSource, line,
                                fields);
                        // si il y a une relation au requerant => la rente
                        // accordee est pour un des membres de la famille
                        isAnnoncePayForDemandeRente = (isAnnoncePayForDemandeRente || !JadeStringUtil.isIntegerEmpty(ra
                                .getCsRelationAuRequerant()));

                        ra.setIdBaseCalcul(bc.getIdBasesCalcul());

                        // BZ 4427
                        boolean isCreerRA = true;
                        if (isAjournement) {
                            ra.setCsEtat(IREPrestationAccordee.CS_ETAT_AJOURNE);

                            // si les 3 champs sont vide, on remonte
                            if (JadeStringUtil.isBlankOrZero(ra.getDureeAjournement())
                                    && JadeStringUtil.isBlankOrZero(ra.getSupplementAjournement())
                                    && JadeStringUtil.isBlankOrZero(ra.getDateRevocationAjournement())) {

                                isCreerRA = true;

                            } else // si un des 3 champs vide, on créé pas la RA
                            if (JadeStringUtil.isBlankOrZero(ra.getDureeAjournement())
                                    || JadeStringUtil.isBlankOrZero(ra.getSupplementAjournement())
                                    || JadeStringUtil.isBlankOrZero(ra.getDateRevocationAjournement())) {
                                isCreerRA = false;

                                REBasesCalcul bcToDel = new REBasesCalcul();
                                bcToDel.setSession(session);
                                bcToDel.setIdBasesCalcul(ra.getIdBaseCalcul());
                                bcToDel.retrieve();
                                bcToDel.delete();
                            }
                        }
                        if (isCreerRA) {
                            Long idRA = Long.parseLong(REAddRenteAccordee.addRenteAccordeeCascade_noCommit(session,
                                    transaction, ra, IREValidationLevel.VALIDATION_LEVEL_NONE));
                            ids.add(idRA);
                        }
                    }
                }

                // Traitement des prestations dues...
                String plusPetiteDateDeDebutDesPrestationsDues = null;

                /**
                 * Level 201..300
                 */

                if (level <= 300) {
                    while ((line != null) && !line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_FIN_CALCUL)) {
                        line = REACORParser.readRelevantLine(bufferedReader);
                        if (line == null) {
                            break;
                        }

                        if (!line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF)
                                && !line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL)) {

                            if (line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_CALCUL)) {
                                level = 100;
                                break;
                            } else if (line.startsWith(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE)) {
                                level = 200;
                                break;
                            } else if (line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_FIN_CALCUL)) {
                                level = 0;
                                break;
                            } else {
                                throw new Exception("Parsing error, unknown start code : "); // +
                                // line.substring(0,5));
                            }
                        } else {
                            level = 0;
                        }

                        // Traitement des prestations dues....
                        if (((line != null) && line
                                .startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF))
                                || line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL)) {

                            String dateFin = null;
                            if (line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL)) {
                                fields = REACORAbstractFlatFileParser
                                        .getConfiguration(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL);
                                // Les $p sont ordonnés chronologiquement, donc
                                // le 1er trouvé correspond à la plus petite des
                                // dates.
                                if (plusPetiteDateDeDebutDesPrestationsDues == null) {
                                    plusPetiteDateDeDebutDesPrestationsDues = REACORAbstractFlatFileParser.getField(
                                            line, fields, "DEBUT_PAIEMENT");
                                }
                            } else {
                                fields = REACORAbstractFlatFileParser
                                        .getConfiguration(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF);
                                dateFin = REPmtMensuel.getDateDernierPmt(session);
                            }

                            REPrestationDue pd = REACORParser.importPrestationsDues(session,
                                    (BTransaction) transaction, line, fields, ra);
                            if (!JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())
                                    && BSessionUtil.compareDateFirstLower(session, ra.getDateFinDroit(), dateFin)) {

                                dateFin = ra.getDateFinDroit();
                            }

                            // Hack, la date de début de paiement du rétro n'est
                            // pas correctement par ACOR.
                            // On prend donc la date de début des $p la plus
                            // ancienne.
                            if (line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF)) {
                                pd.setDateDebutPaiement(PRDateFormater
                                        .convertDate_AAAAMM_to_MMxAAAA(plusPetiteDateDeDebutDesPrestationsDues));
                            }

                            pd.setDateFinPaiement(dateFin);
                            pd.setIdRenteAccordee(ra.getIdPrestationAccordee());
                            pd.add(transaction);
                        }
                    }
                }
            }

            if (!isAnnoncePayForDemandeRente) {
                throw new PRACORException("Le fichier annonce.pay ne correspond pas à la demande de rente.");
            }

            // Settage de tous les idRA
            returnedValue.getIdRenteAccordees().addAll(ids);

            // Mise à jour dateDebut et dateFin de la demande
            // --> La date de début représente la date la plus petite des ra
            // --> La date de fin représente la date la plus grande des ra, sauf
            // si un des ra n'a pas de fin, dans ce cas, on laisse vide

            String firstDateDebutRA = "";
            String lastDateFinRA = "";
            boolean isRAWithoutDateFin = false;

            for (Long idRA : ids) {

                // Retrieve de la ra
                RERenteAccordee renteAcc = new RERenteAccordee();
                renteAcc.setSession(session);
                renteAcc.setIdPrestationAccordee(idRA.toString());
                renteAcc.retrieve(transaction);

                // Date de début
                if (!JadeStringUtil.isBlankOrZero(renteAcc.getDateDebutDroit())) {
                    if (firstDateDebutRA.length() == 0) {
                        firstDateDebutRA = renteAcc.getDateDebutDroit();
                    } else {
                        if (Integer
                                .parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(renteAcc.getDateDebutDroit())) < Integer
                                .parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(firstDateDebutRA))) {
                            firstDateDebutRA = renteAcc.getDateDebutDroit();
                        }
                    }
                }

                // Date de fin
                if (!JadeStringUtil.isBlankOrZero(renteAcc.getDateFinDroit())) {
                    if (lastDateFinRA.length() == 0) {
                        lastDateFinRA = renteAcc.getDateFinDroit();
                    } else {
                        if (Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(renteAcc.getDateFinDroit())) > Integer
                                .parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(lastDateFinRA))) {
                            lastDateFinRA = renteAcc.getDateFinDroit();
                        }
                    }
                }

                // période infinie ?
                if (JadeStringUtil.isBlankOrZero(renteAcc.getDateFinDroit())) {
                    isRAWithoutDateFin = true;
                }

            }

            demandeSource.setDateDebut(firstDateDebutRA);

            if (isRAWithoutDateFin) {
                demandeSource.setDateFin("");
            } else {
                // on cherche le dernier jour du mois de fin
                JACalendar cal = new JACalendarGregorian();
                JADate dateFin = cal.addMonths(new JADate(lastDateFinRA), 1);
                dateFin = cal.addDays(dateFin, -1);
                demandeSource.setDateFin(dateFin.toStr("."));
            }

            demandeSource.update(transaction);

            // Mise à jours des PrestationsDues Date de fin et l'état.
            // Parcours des RA importées
            for (Long idRa : ids) {

                REPrestationsDuesManager mgr = new REPrestationsDuesManager();
                mgr.setSession(session);
                mgr.setForIdRenteAccordes(idRa.toString());
                mgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                mgr.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + " ASC ");
                mgr.find(transaction, BManager.SIZE_NOLIMIT);
                Iterator<REPrestationDue> iterPD = mgr.iterator();

                // charger la ra en question
                RERenteAccordee ra1 = new RERenteAccordee();
                ra1.setSession(session);
                ra1.setIdPrestationAccordee(idRa.toString());
                ra1.retrieve(transaction);

                REPrestationDue courant = null;
                REPrestationDue suivant = null;

                // Cas spécial, workaround !!!
                // Dans le cas ou aucun $p n'a été retourné par ACOR, il faut le créer
                // Ce cas arrive lors d'anticipation d'une demande de rente.
                // Lors du 2ème cas d'assurance ACOR ne retourne pas les $p.
                // Également dans le cas ou l'on saisi à l'avance une demande
                // vieillesse par exemple.

                JACalendar cal = new JACalendarGregorian();
                if (!iterPD.hasNext()) {

                    // On créé manuellement le $p.
                    REPrestationDue pd = new REPrestationDue();
                    pd.setSession(session);

                    pd.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                    pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                    pd.setCsTypePaiement(null);

                    pd.setDateDebutPaiement(ra1.getDateDebutDroit());
                    pd.setMontant(ra1.getMontantPrestation());
                    pd.setMontantReductionAnticipation(ra1.getMontantReducationAnticipation());
                    pd.setMontantSupplementAjournement(ra1.getSupplementAjournement());

                    REBasesCalcul baseCal = new REBasesCalcul();
                    baseCal.setSession(session);
                    baseCal.setIdBasesCalcul(ra1.getIdBaseCalcul());
                    baseCal.retrieve(transaction);
                    PRAssert.notIsNew(baseCal, "Impossible de récupérer la base de calcul (101).");

                    pd.setRam(baseCal.getRevenuAnnuelMoyen());
                    pd.setIdRenteAccordee(ra1.getIdPrestationAccordee());
                    pd.add(transaction);

                    // On recharge l'itérator... pour la suite du traitement
                    mgr.find(transaction, BManager.SIZE_NOLIMIT);
                    iterPD = mgr.iterator();

                }

                // Parcours des prestations dues pour chacune des RA
                while (iterPD.hasNext()) {
                    if (courant == null) {
                        courant = iterPD.next();
                    }

                    suivant = null;
                    if (iterPD.hasNext()) {
                        suivant = iterPD.next();
                    }

                    // On set la date de fin de la prestation dues courant, avec
                    // la date début de
                    // la prestation suivante, moins 1 mois.
                    if ((suivant != null) && JadeStringUtil.isBlankOrZero(courant.getDateFinPaiement())) {
                        JADate df = new JADate(suivant.getDateDebutPaiement());

                        cal = new JACalendarGregorian();
                        JADate dfCourant = cal.addMonths(df, -1);
                        JADate theDf = new JADate(dfCourant.toStr("."));

                        if (BSessionUtil.compareDateFirstLower(session, ra1.getDateFinDroit(),
                                PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(theDf.toStrAMJ()))
                                && !JadeStringUtil.isBlankOrZero(ra1.getDateFinDroit())) {

                            theDf = new JADate(ra1.getDateFinDroit());
                        }

                        courant.setDateFinPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(theDf.toStrAMJ()));

                        // courant.setCsEtat(IREPrestationDue.CS_ETAT_TRAITE);
                        courant.update(transaction);
                    } else if ((suivant == null) && !JadeStringUtil.isBlankOrZero(ra1.getDateFinDroit())) {
                        courant.setDateFinPaiement(ra1.getDateFinDroit());
                        courant.update(transaction);
                    }

                    courant = suivant;
                    if ((null != suivant) && !iterPD.hasNext()) {
                        if (JadeStringUtil.isBlankOrZero(courant.getDateFinPaiement())
                                && !JadeStringUtil.isBlankOrZero(ra1.getDateFinDroit())) {
                            courant.setDateFinPaiement(ra1.getDateFinDroit());
                            courant.update(transaction);
                        }
                    }
                }
            }

            // Dernière étape : MAJ des adresse de pmt des rentes accordées
            // créées
            REBeneficiairePrincipal.doMajAdressePmtDesRentesAccordees(session, transaction, ids);
        } catch (Exception e) {
            throw new PRACORException("impossible de parser : " + e.getMessage(), e);
        }

        return returnedValue;
    }

    /**
     * Parse le fichier annonce.RR (reader) et insière dans la base les annonces qui s'y trouvent.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param REDemandeRente
     *            DOCUMENT ME!
     * @param Reader
     *            DOCUMENT ME!
     * @param LinkedList
     *            DOCUMENT ME!
     * @return une liste jamais nulle, peut-etre vide des prestations qui ont ete importees.
     * @throws Exception
     */
    public static final void parseAnnonceRR(final BSession session, final BITransaction transaction,
            final Reader reader, final List<Long> listIdsRA) throws PRACORException {

        BufferedReader bufferedReader = new BufferedReader(reader);
        Map<String, PRTextField> fields;

        try {

            String _idAnnonce41_01 = "";
            String _idAnnonce41_02 = "";
            String _idAnnonce44_01 = "";
            String _idAnnonce44_02 = "";
            Long _idRA = null;
            String _noAvsAnnonce41_01 = "";
            String _noAvsAnnonce44_01 = "";

            REACORAbstractFlatFileParser.loadConfigurationsAnnonceRR();

            String line = "be there or be square";

            String idsRA = "";

            for (Iterator<Long> iterator = listIdsRA.iterator(); iterator.hasNext();) {
                _idRA = iterator.next();

                if (iterator.hasNext()) {
                    idsRA += _idRA + ", ";
                } else {
                    idsRA += _idRA;
                }

            }

            REAnnoncesAugmentationModification10Eme annonce44;
            REAnnoncesAugmentationModification9Eme annonce41;

            Map<String, String> idsArc41 = new HashMap<String, String>();
            Map<String, String> idsArc44 = new HashMap<String, String>();

            while (line != null) {
                line = REACORParser.readRelevantLine(bufferedReader);
                if (line == null) {
                    break;
                }
                if (line.startsWith(REACORAbstractFlatFileParser.CODE_41_01)) {
                    fields = REACORAbstractFlatFileParser.getConfiguration(REACORAbstractFlatFileParser.CODE_41_01);
                    annonce41 = REACORParser.importAnnonce41_01(session, (BTransaction) transaction, line, fields);
                    annonce41.setSession(session);

                    annonce41.setEtat(IREAnnonces.CS_ETAT_OUVERT);
                    annonce41.setNoAssAyantDroit(NSUtil.unFormatAVS(annonce41.getNoAssAyantDroit()));

                    PRTiersWrapper tier = PRTiersHelper.getTiers(session,
                            NSUtil.formatAVSUnknown(annonce41.getNoAssAyantDroit()));
                    if (tier != null) {
                        annonce41.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    }

                    _noAvsAnnonce41_01 = NSUtil.formatAVSUnknown(annonce41.getNoAssAyantDroit());

                    String mr = annonce41.getMoisRapport();
                    // Doit être maj lors de la validation de la décision !!!
                    annonce41.setMoisRapport("");
                    annonce41.add(transaction);
                    _idAnnonce41_01 = annonce41.getId();
                    idsArc41.put(_idAnnonce41_01, mr);

                    // Retrouver la rente accordée et setter REAnnonceRente
                    RERenteAccordeeManager raMan = new RERenteAccordeeManager();
                    raMan.setSession(session);
                    raMan.setForIdsRentesAccordees(idsRA);
                    raMan.find(transaction);
                    // On parcourt les rentes accordées dont les ids sont passés
                    // en paramètres dans la liste des ra crées précédemment
                    String idRA = "";
                    if (raMan.size() == 1) {
                        RERenteAccordee ra = (RERenteAccordee) raMan.getEntity(0);
                        idRA = ra.getIdPrestationAccordee();
                    } else {
                        // si il y a plusieurs rentes accordee on prend celle
                        // qui a les mêmes:
                        // - NSS (idTiersBeneficiaire)
                        // - Genre de rente (genrePrestation)
                        // - Date de debut
                        for (RERenteAccordee ra : raMan.getContainerAsList()) {

                            // Tests pour être sûr que c'est la bonne rente
                            // accordée
                            if (ra.getCodePrestation().equals(annonce41.getGenrePrestation())) {
                                if (ra.getIdTiersBeneficiaire().equals(annonce41.getIdTiers())) {
                                    if (BSessionUtil.compareDateEqual(session, ra.getDateDebutDroit(),
                                            PRDateFormater.convertDate_MMAA_to_MMxAAAA(annonce41.getDebutDroit()))) {
                                        idRA = ra.getIdPrestationAccordee();
                                    }
                                }
                            }
                        }
                    }

                    if (JadeStringUtil.isBlankOrZero(idRA)) {
                        throw new Exception("RA not found for id : " + idRA);
                    }

                    REAnnonceRente annonceRente = new REAnnonceRente();
                    annonceRente.setSession(session);
                    annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                    annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                    annonceRente.setIdAnnonceHeader(annonce41.getIdAnnonce());
                    annonceRente.setIdRenteAccordee(idRA);
                    annonceRente.add(transaction);

                    annonce41 = new REAnnoncesAugmentationModification9Eme();

                    // line = readRelevantLine(bufferedReader);
                }

                if (line.startsWith(REACORAbstractFlatFileParser.CODE_41_02)) {
                    fields = REACORAbstractFlatFileParser.getConfiguration(REACORAbstractFlatFileParser.CODE_41_02);
                    annonce41 = REACORParser.importAnnonce41_02(session, (BTransaction) transaction, line, fields);

                    annonce41.setSession(session);

                    annonce41.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                    PRTiersWrapper tier = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(_noAvsAnnonce41_01));
                    if (tier != null) {
                        annonce41.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    }

                    REArcFrenchValidator frenchValidator = new REArcFrenchValidator();
                    annonce41 = frenchValidator.validateARC_41_02(annonce41);

                    annonce41.add(transaction);
                    _idAnnonce41_02 = annonce41.getId();

                    annonce41 = new REAnnoncesAugmentationModification9Eme();

                    // line = readRelevantLine(bufferedReader);
                }

                if (line.startsWith(REACORAbstractFlatFileParser.CODE_44_01)) {
                    fields = REACORAbstractFlatFileParser.getConfiguration(REACORAbstractFlatFileParser.CODE_44_01);
                    annonce44 = REACORParser.importAnnonce44_01(session, (BTransaction) transaction, line, fields);

                    annonce44.setSession(session);

                    annonce44.setEtat(IREAnnonces.CS_ETAT_OUVERT);
                    annonce44.setNoAssAyantDroit(NSUtil.unFormatAVS(annonce44.getNoAssAyantDroit()));

                    PRTiersWrapper tier = PRTiersHelper.getTiers(session,
                            NSUtil.formatAVSUnknown(annonce44.getNoAssAyantDroit()));
                    if (tier != null) {
                        annonce44.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    }

                    _noAvsAnnonce44_01 = NSUtil.formatAVSUnknown(annonce44.getNoAssAyantDroit());

                    String mr = annonce44.getMoisRapport();
                    // Doit être maj lors de la validation de la décision !!!
                    annonce44.setMoisRapport("");
                    annonce44.add(transaction);
                    _idAnnonce44_01 = annonce44.getId();
                    idsArc44.put(_idAnnonce44_01, mr);

                    // Retrouver la rente accordée et setter REAnnonceRente
                    RERenteAccordeeManager raMan = new RERenteAccordeeManager();
                    raMan.setSession(session);
                    raMan.setForIdsRentesAccordees(idsRA);
                    raMan.find(BManager.SIZE_NOLIMIT);

                    // On parcourt les rentes accordées dont les ids sont passés
                    // en paramètres dans la liste des ra crées précédemment
                    String idRA = "";

                    if (raMan.size() == 1) {
                        RERenteAccordee ra = (RERenteAccordee) raMan.getEntity(0);
                        idRA = ra.getIdPrestationAccordee();
                    } else {
                        // si il y a plusieurs rentes accordee on prend celle
                        // qui a les mêmes:
                        // - NSS (idTiersBeneficiaire)
                        // - Genre de rente (genrePrestation)
                        // - Date de debut
                        for (RERenteAccordee ra : raMan.getContainerAsList()) {

                            // Tests pour être sûr que c'est la bonne rente
                            // accordée
                            if (ra.getCodePrestation().equals(annonce44.getGenrePrestation())) {
                                if (ra.getIdTiersBeneficiaire().equals(annonce44.getIdTiers())) {
                                    if (BSessionUtil.compareDateEqual(session, ra.getDateDebutDroit(),
                                            PRDateFormater.convertDate_MMAA_to_MMxAAAA(annonce44.getDebutDroit()))) {
                                        idRA = ra.getIdPrestationAccordee();
                                    }
                                }
                            }
                        }
                    }

                    if (JadeStringUtil.isBlankOrZero(idRA)) {
                        throw new Exception("RA not found for id : " + idRA);
                    }

                    REAnnonceRente annonceRente = new REAnnonceRente();
                    annonceRente.setSession(session);
                    annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                    annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                    annonceRente.setIdAnnonceHeader(annonce44.getIdAnnonce());
                    annonceRente.setIdRenteAccordee(idRA);
                    annonceRente.add(transaction);

                    annonce44 = new REAnnoncesAugmentationModification10Eme();
                }

                if (line.startsWith(REACORAbstractFlatFileParser.CODE_44_02)) {
                    fields = REACORAbstractFlatFileParser.getConfiguration(REACORAbstractFlatFileParser.CODE_44_02);
                    annonce44 = REACORParser.importAnnonce44_02(session, (BTransaction) transaction, line, fields);

                    annonce44.setSession(session);

                    annonce44.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                    PRTiersWrapper tier = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(_noAvsAnnonce44_01));
                    if (tier != null) {
                        annonce44.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    }

                    annonce44.add(transaction);

                    _idAnnonce44_02 = annonce44.getId();

                    annonce44 = new REAnnoncesAugmentationModification10Eme();

                    // line = readRelevantLine(bufferedReader);
                }

                if (!JadeStringUtil.isEmpty(_idAnnonce41_01) && !JadeStringUtil.isEmpty(_idAnnonce41_02)) {
                    // mise à jour de l'annonce 41 01
                    REAnnonceHeader annonceHeader = new REAnnonceHeader();
                    annonceHeader.setSession(session);
                    annonceHeader.setIdAnnonce(_idAnnonce41_01);
                    annonceHeader.retrieve(transaction);

                    annonceHeader.setIdLienAnnonce(_idAnnonce41_02);
                    annonceHeader.update(transaction);

                    _idAnnonce41_01 = "";
                    _idAnnonce41_02 = "";

                }

                if (!JadeStringUtil.isEmpty(_idAnnonce44_01) && !JadeStringUtil.isEmpty(_idAnnonce44_02)) {
                    // mise à jour de l'annonce 44 01
                    REAnnonceHeader annonceHeader = new REAnnonceHeader();
                    annonceHeader.setSession(session);
                    annonceHeader.setIdAnnonce(_idAnnonce44_01);
                    annonceHeader.retrieve(transaction);

                    annonceHeader.setIdLienAnnonce(_idAnnonce44_02);
                    annonceHeader.update(transaction);
                    _idAnnonce44_01 = "";
                    _idAnnonce44_02 = "";
                }
            }

            REACORParser.anakinValidation(session, (BTransaction) transaction, idsArc41, idsArc44);

        } catch (Exception e) {
            throw new PRACORException("impossible de parser annonce.RR\n" + e.getMessage(), e);
        }
    }

    public static final List<REFeuilleCalculVO> parseCIAdd(final BSession session, final BITransaction transaction,
            final REDemandeRente demandeSource, final Reader reader) throws PRACORException {

        List<REFeuilleCalculVO> retValue = new LinkedList<REFeuilleCalculVO>();

        BufferedReader bufferedReader = new BufferedReader(reader);
        Map<String, PRTextField> fields;

        REFeuilleCalculVO fcParBaseCalculVO = null;
        REFeuilleCalculVO.ElementVO elmFCVO = null;

        try {
            // changer de parser si NNSS ou NAVS
            PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(demandeSource.getIdDemandePrestation());
            demande.retrieve();

            if (demande.isNew()) {
                throw new PRACORException("Demande prestation non trouvée !!");
            } else {

                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, demande.getIdTiers());

                if (null != tiers) {
                    // si navs
                    if (JadeStringUtil.removeChar(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), '.')
                            .length() == 11) {
                        REACORAbstractFlatFileParser.loadConfigurations_NAVS();
                        // si nnss
                    } else {
                        REACORAbstractFlatFileParser.loadConfigurations_NSS();
                    }
                } else {
                    throw new PRACORException("Tiers de la demande prestation non trouvé !!");
                }

            }
            REBasesCalcul bc = null;
            RERenteAccordee ra = null;
            String line = "be there or be square";

            int level = 0;

            while (line != null) {

                /*
                 * Niveau 0..50
                 */

                if (level <= 50) {
                    line = REACORParser.readRelevantLine(bufferedReader);
                    if (line == null) {
                        break;
                    }
                }

                /*
                 * Niveau 51..100
                 */
                if (level <= 100) {
                    if (line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_CALCUL)) {
                        fields = REACORAbstractFlatFileParser
                                .getConfiguration(REACORAbstractFlatFileParser.CODE_BASE_CALCUL);
                        bc = REACORParser.importBaseCalcul(session, line, fields);

                        if (fcParBaseCalculVO != null) {
                            retValue.add(fcParBaseCalculVO);
                        }
                        fcParBaseCalculVO = new REFeuilleCalculVO();
                    }
                }

                /**
                 * Niveau 101..150
                 */

                if (level <= 150) {
                    line = REACORParser.readRelevantLine(bufferedReader);
                }

                /**
                 * Niveau 151..200
                 */

                if (level <= 200) {
                    if ((line != null)
                            && (line.startsWith(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE) || line
                                    .startsWith(REACORAbstractFlatFileParser.CODE_AJOURNEMENT))) {
                        // importer les rentes accordées

                        fields = REACORAbstractFlatFileParser
                                .getConfiguration(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE);
                        ra = REACORParser.importRenteAccordee(session, (BTransaction) transaction, demandeSource, line,
                                fields);

                        elmFCVO = fcParBaseCalculVO.new ElementVO();

                        elmFCVO.setRAM(bc.getRevenuAnnuelMoyen());
                        elmFCVO.setIdTiers(ra.getIdTiersBeneficiaire());
                        elmFCVO.setMontantRente(ra.getMontantPrestation());
                        elmFCVO.setGenreRente(ra.getCodePrestation());
                        fcParBaseCalculVO.addElementVO(elmFCVO);
                    }

                }

                /**
                 * Level 201..300
                 */

                if (level <= 300) {
                    while ((line != null) && !line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_FIN_CALCUL)) {
                        line = REACORParser.readRelevantLine(bufferedReader);
                        if (line == null) {
                            break;
                        }

                        if (!line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF)
                                && !line.startsWith(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL)) {

                            if (line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_CALCUL)) {
                                level = 100;
                                break;
                            } else if (line.startsWith(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE)) {
                                level = 200;
                                break;
                            } else if (line.startsWith(REACORAbstractFlatFileParser.CODE_BASE_FIN_CALCUL)) {
                                level = 0;
                                break;
                            } else {
                                throw new Exception("Parsing error, unknown start code : "); // +
                                // line.substring(0,5));
                            }
                        } else {
                            level = 0;
                        }
                    }
                }
            }
            // On ajoute les derniers éléments liés à la base de calcul dans la
            // liste
            retValue.add(fcParBaseCalculVO);

        } catch (Exception e) {
            throw new PRACORException("impossible de parser : " + e.getMessage(), e);
        }

        return retValue;
    }

    /**
     * return all the relevant line. skip unused information.
     * 
     * @param reader
     * @return
     * @throws Exception
     */
    private static String readRelevantLine(final BufferedReader reader) throws Exception {

        String line = reader.readLine();
        if (line != null) {
            // skip line
            if (line.startsWith(REACORAbstractFlatFileParser.CODE_ALLOC_RENCH_1991)
                    || line.startsWith(REACORAbstractFlatFileParser.CODE_COTISATION)
                    || line.startsWith(REACORAbstractFlatFileParser.CODE_RENTE_INCHANGEE)
                    || line.startsWith(REACORAbstractFlatFileParser.CODE_MONTANT_MENS_RENTE_AJOURNEES)) {
                line = REACORParser.readRelevantLine(reader);
            }
        }

        return line;
    }

    /**
     * Retourne la date de traitement La date de traitement envoyée à acor est la plus grande entre 'date traitement
     * saisie dans la demande' et la date du jours. Il n'est possible de saisir une date de traitement dans la demande,
     * que pour les cas de vieillesse. Elle est utile pour traiter la demande en avance, uniquement. Donc si elle est
     * saisie, elle sera obligatoirement plus grande que la date du jours.
     * 
     * @param demande
     * @return max (date jours, date traitement de la demande) format : jj.mm.aaaa
     */
    public static String retrieveDateTraitement(final REDemandeRente demande) {

        JADate dateTraitement = null;
        JADate dPmtMens = null;
        try {
            dateTraitement = new JADate(demande.getDateTraitement());
            dPmtMens = new JADate(REPmtMensuel.getDateDernierPmt(demande.getSession()));
        } catch (Exception e) {
            try {
                dateTraitement = new JADate(REPmtMensuel.getDateDernierPmt(demande.getSession()));
                dPmtMens = new JADate(REPmtMensuel.getDateDernierPmt(demande.getSession()));
            } catch (Exception e2) {
                ;
            }
        }

        JACalendarGregorian cal = new JACalendarGregorian();
        if (cal.compare(dPmtMens, dateTraitement) == JACalendar.COMPARE_FIRSTUPPER) {
            return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dPmtMens.toStrAMJ());
        } else {
            return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateTraitement.toStrAMJ());
        }

    }

    /**
     * Crée une nouvelle instance de la classe REACORParser.
     */
    private REACORParser() {
    }

    // /**
    // * ajouter deux nombres codes en string.
    // *
    // * @param op1
    // * @param op2
    // *
    // * @return
    // */
    // private static final String add(String op1,
    // String op2) {
    // FWCurrency currency = new FWCurrency(op1);
    //
    // currency.add(op2);
    //
    // return currency.toString();
    // }
    //
    // /**
    // * multiplie deux nombres, arrondit le resultat a 2 decimales et 5
    // centimes et retourne cette valeur en tant que
    // * chaine.
    // *
    // * @param op1
    // * @param op2
    // *
    // * @return
    // */
    // private static final String multiply(String op1,
    // String op2) {
    //
    // if (JadeStringUtil.isEmpty(op1))
    // op1="0.0";
    // if (JadeStringUtil.isEmpty(op2))
    // op2="0.0";
    //
    // return JANumberFormatter.format(PRCalcul.multiply(op1, op2), 0.05, 2,
    // JANumberFormatter.NEAR);
    // }
}
