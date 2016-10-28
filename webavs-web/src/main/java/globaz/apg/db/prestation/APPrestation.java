package globaz.apg.db.prestation;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.lots.APCompensation;
import globaz.apg.db.lots.APLot;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.pojo.wrapper.APPrestationWrapper;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.tools.PRCalcul;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Description Prestation calculée par le système pour une date de début et date de fin donnée.Prestation calculée par
 * le système pour une date de début et date de fin donnée.
 * 
 * @author
 */
public class APPrestation extends BEntity implements IAPPrestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Champ ajouté pour le RAPG, permet de stocké le montant journalier de base
     */
    public static final String FIELDNAME_BASICDAILYAMOUNT = "VHBDAM";
    public static final String FIELDNAME_CONTENUANNONCE = "VHTCOA";
    public static final String FIELDNAME_DATECALCUL = "VHDCAL";
    public static final String FIELDNAME_DATECONTROLE = "VHDCTR";
    public static final String FIELDNAME_DATEDEBUT = "VHDDEB";
    public static final String FIELDNAME_DATEFIN = "VHDFIN";
    public static final String FIELDNAME_DATEPAIEMENT = "VHDPMT";
    public static final String FIELDNAME_DROITACQUIS = "VHMDRA";
    public static final String FIELDNAME_ETAT = "VHTETA";
    public static final String FIELDNAME_FRAISGARDE = "VHMFRG";
    public static final String FIELDNAME_GENRE_PRESTATION = "VHTGEN";
    public static final String FIELDNAME_IDANNONCE = "VHIANN";
    public static final String FIELDNAME_IDDROIT = "VHIDRO";
    public static final String FIELDNAME_IDLOT = "VHILOT";
    public static final String FIELDNAME_IDPRESTATIONAPG = "VHIPRS";
    public static final String FIELDNAME_IDRESTITUTION = "VHIRST";
    public static final String FIELDNAME_IS_MODIFIED_BY_USER = "VHBSMA";
    public static final String FIELDNAME_MONTANT_JOURNALIER = "VHMTJR";
    public static final String FIELDNAME_MONTANTALLOCEXPLOITATION = "VHMALE";
    public static final String FIELDNAME_MONTANTBRUT = "VHDMOB";
    public static final String FIELDNAME_NOMBREJOURSSOLDES = "VHNNJS";
    public static final String FIELDNAME_NOREVISION = "VHTREV";
    public static final String FIELDNAME_REMARQUE = "VHLREM";
    public static final String FIELDNAME_REVENUMOYENDETERMINANT = "VHMRMD";
    public static final String FIELDNAME_TYPE = "VHTTYP";
    public static final String TABLE_NAME = "APPRESP";

    private String basicDailyAmount = "";
    private String contenuAnnonce = "";
    private String dateCalcul = "";
    private String dateControle = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String datePaiement = "";
    private String droitAcquis = "";
    protected String etat = "";
    private String fraisGarde = "";
    protected String genre = APTypeDePrestation.STANDARD.getCodesystemString();
    private String idAnnonce = "";
    protected String idDroit = "";
    private String idLot = "";
    private String idPrestationApg = "";
    private String idRestitution = "";
    private Boolean isModifiedByUser = Boolean.FALSE;
    private boolean miseAJourRepartitions = false;
    /**
     * montant journalier de l'allocation d'exploitation
     */
    private String montantAllocationExploitation = "";
    private String montantBrut = "";
    private String montantJournalier = "";
    private String nombreJoursSoldes = "";
    private String noRevision = "";
    private String remarque = "";
    private String revenuMoyenDeterminant = "";
    protected String type = "";

    public APPrestation() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APPrestation en copiant toutes les valeurs d'une autre prestation.
     * 
     * @param prestation
     *            la prestation depuis laquelle copier les propriétés.
     * @throws Exception
     *             si la copie ne fonctionne pas.
     */
    public APPrestation(APPrestation prestation) throws Exception {
        copyDataFromEntity(prestation);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // suppression des compensations liées a cette prestation si elle est
        // dans un lot et que ce lot est en état
        // compensé. remise de ce lot en etat ouvert
        if (IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT.equals(etat)) {
            APLot lot = new APLot();
            lot.setSession(getSession());
            lot.setIdLot(idLot);
            lot.retrieve(transaction);

            if (IAPLot.CS_COMPENSE.equals(lot.getEtat())) {
                lot.setEtat(IAPLot.CS_OUVERT);
                lot.update(transaction);
            }

            lot = null;

            APRepartitionPaiementsManager repartitionPaiementsManager = new APRepartitionPaiementsManager();
            repartitionPaiementsManager.setSession(getSession());
            repartitionPaiementsManager.setForIdPrestation(idPrestationApg);

            Set idsCompensations = new HashSet();
            repartitionPaiementsManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < repartitionPaiementsManager.size(); i++) {
                APRepartitionPaiements repartitionPaiements = (APRepartitionPaiements) repartitionPaiementsManager
                        .getEntity(i);
                idsCompensations.add(repartitionPaiements.getIdCompensation());
            }

            repartitionPaiementsManager = null;

            Iterator iterator = idsCompensations.iterator();

            while (iterator.hasNext()) {
                String idCompensation = (String) iterator.next();

                if (!JadeStringUtil.isIntegerEmpty(idCompensation)) {
                    APCompensation compensation = new APCompensation();
                    compensation.setSession(getSession());
                    compensation.setIdCompensation(idCompensation);
                    compensation.retrieve(transaction);
                    if (!compensation.isNew()) {
                        compensation.delete(transaction);
                    }
                }
            }
        }

        // effacement des répartitions de paiements
        APRepartitionPaiementsManager mgr = new APRepartitionPaiementsManager();

        mgr.setSession(getSession());
        mgr.setForIdPrestation(idPrestationApg);
        mgr.setForIdParent("0");
        mgr.find(transaction);

        for (int idRep = 0; idRep < mgr.size(); ++idRep) {
            APRepartitionPaiements rep = (APRepartitionPaiements) mgr.get(idRep);

            rep.setSession(getSession());
            rep.delete(transaction);
        }

        // suppression de l'annonce liée si elle existe
        if (!JadeStringUtil.isIntegerEmpty(idAnnonce)) {
            APAnnonceAPG annonce = new APAnnonceAPG();
            annonce.setSession(getSession());
            annonce.setIdAnnonce(idAnnonce);
            annonce.retrieve(transaction);
            annonce.delete(transaction);
        }
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdPrestationApg(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        if (IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(etat)) {
            _addError(transaction, getSession().getLabel("SUPPRESSION_PRESTATION_DEFINITIVE_IMPOSSIBLE"));
        }
        // Si l'on supprime une prestation de type restitution,
        // il faut au préalable annuler l'id de restitution des prestations
        // qu'elle était censé
        // restituée.

        if (IAPAnnonce.CS_RESTITUTION.equals(getContenuAnnonce())) {
            APPrestationManager mgr = new APPrestationManager();
            mgr.setSession(getSession());
            mgr.setForIdRestitution(getIdPrestationApg());
            mgr.find(transaction);

            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                APPrestation element = (APPrestation) iter.next();
                element.setIdRestitution(null);
                element.update(transaction);
            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // si la prestation a un idLot et que son état est différent de mis en
        // lot ou définitif, c'est que son état a
        // rétrogradé. Il faut virer le idLot.
        if (!JadeStringUtil.isEmpty(idLot)) {
            if (!IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(etat)
                    && !IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT.equals(etat)) {
                idLot = "";
            }
        }

        // si la prestation a une date de contrôle et que son état est égal a
        // ouvert ou validé, c'est que son état a
        // été rétrogradé. Il faut donc virer la date de contrôle
        if (!JAUtil.isDateEmpty(dateControle)) {
            if (IAPPrestation.CS_ETAT_PRESTATION_OUVERT.equals(etat)
                    || IAPPrestation.CS_ETAT_PRESTATION_VALIDE.equals(etat)) {
                dateControle = "";
            }
        }

        // si elle est en lot et qu'elle a un idLot, c'est que une autre
        // modification a eu lieu (mise en lot, montant,
        // etc) Dans tous les cas, les compensations du lot ne sont probablement
        // plus juste, on le remet donc en ouvert
        if (!JadeStringUtil.isIntegerEmpty(idLot) && IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT.equals(etat)) {
            APLot lot = new APLot();
            lot.setSession(getSession());
            lot.setIdLot(idLot);
            lot.retrieve(transaction);

            if (IAPLot.CS_COMPENSE.equals(lot.getEtat())) {
                lot.setEtat(IAPLot.CS_OUVERT);
                lot.update(transaction);
            }
        }

        // si on veut annulé une prestation de type restitution, on enleve les liens des prestations sur la restitution
        // et on met la prestation à annuler.
        if (IAPAnnonce.CS_RESTITUTION.equals(getContenuAnnonce())
                && IAPPrestation.CS_ETAT_PRESTATION_ANNULE.equals(etat)) {

            APPrestationManager mgr = new APPrestationManager();
            mgr.setSession(getSession());
            mgr.setForIdRestitution(getIdPrestationApg());
            mgr.find(transaction);

            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                APPrestation element = (APPrestation) iter.next();
                element.setIdRestitution(null);
                element.update(transaction);
            }

            return;
        }

        // recalculer les repartitions de paiements pour cette prestation
        if (miseAJourRepartitions) {

            // pas de modification des repartitions pour une restitution
            if (IAPAnnonce.CS_RESTITUTION.endsWith(getContenuAnnonce())) {
                _addError(transaction, getSession().getLabel("MODIFICATION_PRESTATIONS_RESTITUTION_ERROR"));
            }

            APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();
            APRepartitionPaiementsManager aEffacer = new APRepartitionPaiementsManager();
            APCotisationManager cotisations = new APCotisationManager();

            aEffacer.setSession(getSession());
            cotisations.setSession(getSession());

            repartitions.setForIdPrestation(idPrestationApg);
            repartitions.setForTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
            repartitions.setParentOnly(true);
            repartitions.setSession(getSession());
            repartitions.find();

            // pour memoriser le montant verse
            FWCurrency montantVerse = new FWCurrency("0");

            // pour savoir si il y a une repartition en paiement direct a
            // l'assure
            // on ne limite le revenu verse uniquement si il existe deja une
            // repartition pour cela
            boolean hasRepartitionPourAssure = false;
            for (int idRepartion = 0; idRepartion < repartitions.size(); ++idRepartion) {
                APRepartitionPaiements repartition = (APRepartitionPaiements) repartitions.get(idRepartion);
                repartition.loadSituationProfessionnelle();

                if (IAPRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(repartition.getTypePaiement())
                        && (repartition.getSituatuionPro() == null)) {
                    hasRepartitionPourAssure = true;
                }
            }

            for (int idRepartion = 0; idRepartion < repartitions.size(); ++idRepartion) {
                APRepartitionPaiements repartition = (APRepartitionPaiements) repartitions.get(idRepartion);

                // effacement des montants ventiles de la repartition de
                // paiement
                aEffacer.setForIdParent(repartition.getIdRepartitionBeneficiairePaiement());
                aEffacer.setSession(getSession());
                aEffacer.find();

                for (int idVentilation = 0; idVentilation < aEffacer.size(); ++idVentilation) {
                    APRepartitionPaiements ventilation = (APRepartitionPaiements) aEffacer.get(idVentilation);

                    ventilation.delete(transaction);
                }

                // recalcul du montant brut de la repartition
                FWCurrency m = null;
                if (!JadeStringUtil.isIntegerEmpty(repartition.getTauxRJM())) {
                    m = new FWCurrency(JANumberFormatter.format(
                            PRCalcul.pourcentage100(getMontantJournalier(), repartition.getTauxRJM()), 0.05, 2,
                            JANumberFormatter.NEAR));
                    m = new FWCurrency(m.getBigDecimalValue().multiply(new BigDecimal(getNombreJoursSoldes()))
                            .doubleValue());

                    // si l'employeur ne vers pas tout le salaire, il faut
                    // verifier qu'on ne lui donne pas trop d'argent
                    // Mais on le fait uniquement si une repartition pour
                    // l'assure existe deja!
                    if ((repartition.getSituatuionPro() != null)
                            && !JadeStringUtil.isIntegerEmpty(repartition.getSituatuionPro().getMontantVerse())
                            && hasRepartitionPourAssure) {

                        // on cherche le montant journalier verse par
                        // l'employeur
                        FWCurrency montantJournalierMoyenVerseEmployeur = null;

                        if (repartition.getSituatuionPro().getIsPourcentMontantVerse().booleanValue()) {

                            // le revenu journalier moyen repratit
                            montantJournalierMoyenVerseEmployeur = new FWCurrency(PRCalcul.pourcentage100(
                                    getRevenuMoyenDeterminant(), repartition.getTauxRJM()));
                            // multiplie par le % du salaire verse
                            montantJournalierMoyenVerseEmployeur = new FWCurrency(JANumberFormatter.format(PRCalcul
                                    .pourcentage100(montantJournalierMoyenVerseEmployeur.toString(), repartition
                                            .getSituatuionPro().getMontantVerse()), 0.05, 2, JANumberFormatter.NEAR));
                        } else {

                            BigDecimal revenuIntermediaire = null;
                            BigDecimal mv = new BigDecimal(repartition.getSituatuionPro().getMontantVerse());

                            // si périodicité horaire
                            if (IPRSituationProfessionnelle.CS_PERIODICITE_HEURE.equals(repartition.getSituatuionPro()
                                    .getPeriodiciteMontantVerse())) {
                                BigDecimal nbHeuresSemaine = new BigDecimal(repartition.getSituatuionPro()
                                        .getHeuresSemaine());
                                revenuIntermediaire = mv.multiply(nbHeuresSemaine);
                                revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal("7"), 10,
                                        BigDecimal.ROUND_DOWN);
                            }

                            // si périodicité mois
                            else if (IPRSituationProfessionnelle.CS_PERIODICITE_MOIS.equals(repartition
                                    .getSituatuionPro().getPeriodiciteMontantVerse())) {
                                revenuIntermediaire = mv.divide(new BigDecimal("30"), 10, BigDecimal.ROUND_DOWN);
                            }

                            // si périodicité 4 semaines
                            else if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES.equals(repartition
                                    .getSituatuionPro().getPeriodiciteMontantVerse())) {
                                revenuIntermediaire = mv.divide(new BigDecimal("28"), 10, BigDecimal.ROUND_DOWN);
                            }

                            // si périodicité année
                            else if (IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE.equals(repartition
                                    .getSituatuionPro().getPeriodiciteMontantVerse())) {
                                revenuIntermediaire = mv.divide(new BigDecimal("360"), 10, BigDecimal.ROUND_DOWN);
                            }

                            // pour garantir que le montant de la repartition ne
                            // depasse pas le montant verse,
                            // on arrondit au 0.05 inf.
                            montantJournalierMoyenVerseEmployeur = new FWCurrency(JANumberFormatter.format(
                                    revenuIntermediaire.doubleValue(), 0.05, 2, JANumberFormatter.INF));
                        }

                        // on cherche le montant journalier repartit
                        FWCurrency montantJournalierRepartit = new FWCurrency(JANumberFormatter.format(
                                PRCalcul.pourcentage100(getMontantJournalier(), repartition.getTauxRJM()), 0.05, 2,
                                JANumberFormatter.NEAR));

                        // Si le montant journalier repartit est plus grand que
                        // le montant journalier verse par l'employeur
                        // on verse le montant verse par l'employeur * par le
                        // nombre de jours
                        if (montantJournalierRepartit.compareTo(montantJournalierMoyenVerseEmployeur) > 0) {
                            m = new FWCurrency(montantJournalierMoyenVerseEmployeur.getBigDecimalValue()
                                    .multiply(new BigDecimal(getNombreJoursSoldes())).doubleValue());
                        }
                    }

                    montantVerse.add(m);
                } else {
                    // normalement les repatitions qui n'ont pas de tauxRJM sont
                    // les repartitions
                    // crees pour le versement a l'assure lorsque l'employeur ne
                    // verse qu'une partie du salaire
                    // le montant de la prestation vaut (montantBrut -
                    // montantVerse)
                    // !!! normalement cette prestation est cree en dernier donc
                    // le montant verse devrait etre juste
                    if (montantVerse.compareTo(new FWCurrency(montantBrut)) < 0) {
                        FWCurrency mb = new FWCurrency(montantBrut);
                        mb.sub(montantVerse);
                        m = mb;
                    } else {
                        repartition.delete();
                        continue;
                    }
                }

                repartition.setMontantBrut(m.toString());

                // recalcul des montants des cotisations
                cotisations.setForIdRepartitionBeneficiairePaiement(repartition.getIdRepartitionBeneficiairePaiement());
                cotisations.find();

                for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                    APCotisation cotisation = (APCotisation) cotisations.get(idCotisation);
                    String idExterne = cotisation.getIdExterne();

                    if (idExterne.equals(getSession().getApplication().getProperty("assurance.fad.personnelle.id"))
                            || idExterne
                                    .equals(getSession().getApplication().getProperty("assurance.fad.paritaire.id"))) {
                        ;
                    } else {
                        String nc = JANumberFormatter.format(
                                PRCalcul.pourcentage100(repartition.getMontantBrut(), cotisation.getTaux()), 0.05, 2,
                                JANumberFormatter.NEAR);

                        cotisation.setMontantBrut(repartition.getMontantBrut());

                        if (cotisation.getMontant().startsWith("-")) {
                            // si la cotisation etait soustraite, le nouvelle
                            // cotisation doit l'etre aussi
                            cotisation.setMontant("-" + nc);
                            m.sub(nc);
                        } else {
                            cotisation.setMontant(nc);
                            m.add(nc);
                        }

                        cotisation.wantCallMethodAfter(false);
                        cotisation.update(transaction);
                    }

                }

                FWCurrency mntCotisation = new FWCurrency(0);

                for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                    APCotisation cotisation = (APCotisation) cotisations.get(idCotisation);
                    String idExterne = cotisation.getIdExterne();

                    if (idExterne.equals(getSession().getApplication().getProperty("assurance.avsai.personnelle.id"))
                            || idExterne.equals(getSession().getApplication().getProperty(
                                    "assurance.avsai.paritaire.id"))) {

                        mntCotisation.add(cotisation.getMontant().toString());
                        break;

                    }
                }

                for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                    APCotisation cotisation = (APCotisation) cotisations.get(idCotisation);
                    String idExterne = cotisation.getIdExterne();

                    if (idExterne.equals(getSession().getApplication().getProperty("assurance.fad.personnelle.id"))
                            || idExterne
                                    .equals(getSession().getApplication().getProperty("assurance.fad.paritaire.id"))) {

                        // Inversion du montant
                        BigDecimal montantCotiInverse = new BigDecimal(mntCotisation.toString());
                        montantCotiInverse = montantCotiInverse.multiply(new BigDecimal(-1));

                        String nc = JANumberFormatter.format(
                                PRCalcul.pourcentage100(montantCotiInverse.toString(), cotisation.getTaux()), 0.05, 2,
                                JANumberFormatter.NEAR);

                        cotisation.setMontantBrut(montantCotiInverse.toString());

                        if (cotisation.getMontant().startsWith("-")) {
                            // si la cotisation etait soustraite, le nouvelle
                            // cotisation doit l'etre aussi
                            cotisation.setMontant("-" + nc);
                            m.sub(nc);
                        } else {
                            cotisation.setMontant(nc);
                            m.add(nc);
                        }

                        cotisation.wantCallMethodAfter(false);
                        cotisation.update(transaction);
                    }

                }
                // sauvegarder la nouvelle repartition
                repartition.setMontantNet(m.toString());
                repartition.wantCallValidate(false);
                repartition.update(transaction);
            }
        }

        super._beforeUpdate(transaction);
    }

    @Override
    protected String _getTableName() {
        return APPrestation.TABLE_NAME;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        datePaiement = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEPAIEMENT);
        montantBrut = statement.dbReadNumeric(APPrestation.FIELDNAME_MONTANTBRUT);
        type = statement.dbReadNumeric(APPrestation.FIELDNAME_TYPE);
        idDroit = statement.dbReadNumeric(APPrestation.FIELDNAME_IDDROIT);
        idPrestationApg = statement.dbReadNumeric(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        idRestitution = statement.dbReadNumeric(APPrestation.FIELDNAME_IDRESTITUTION);
        dateDebut = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATEFIN);
        nombreJoursSoldes = statement.dbReadNumeric(APPrestation.FIELDNAME_NOMBREJOURSSOLDES);
        etat = statement.dbReadNumeric(APPrestation.FIELDNAME_ETAT);
        genre = statement.dbReadNumeric(APPrestation.FIELDNAME_GENRE_PRESTATION);
        contenuAnnonce = statement.dbReadNumeric(APPrestation.FIELDNAME_CONTENUANNONCE);
        idAnnonce = statement.dbReadNumeric(APPrestation.FIELDNAME_IDANNONCE);
        montantJournalier = statement.dbReadNumeric(APPrestation.FIELDNAME_MONTANT_JOURNALIER);
        basicDailyAmount = statement.dbReadNumeric(APPrestation.FIELDNAME_BASICDAILYAMOUNT);
        montantAllocationExploitation = statement.dbReadNumeric(APPrestation.FIELDNAME_MONTANTALLOCEXPLOITATION);
        droitAcquis = statement.dbReadNumeric(APPrestation.FIELDNAME_DROITACQUIS);
        idLot = statement.dbReadNumeric(APPrestation.FIELDNAME_IDLOT);
        dateCalcul = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATECALCUL);
        dateControle = statement.dbReadDateAMJ(APPrestation.FIELDNAME_DATECONTROLE);
        revenuMoyenDeterminant = statement.dbReadNumeric(APPrestation.FIELDNAME_REVENUMOYENDETERMINANT);
        fraisGarde = statement.dbReadNumeric(APPrestation.FIELDNAME_FRAISGARDE);
        noRevision = statement.dbReadNumeric(APPrestation.FIELDNAME_NOREVISION);
        remarque = statement.dbReadString(APPrestation.FIELDNAME_REMARQUE);
        isModifiedByUser = statement.dbReadBoolean(APPrestation.FIELDNAME_IS_MODIFIED_BY_USER);
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(APPrestation.FIELDNAME_IDPRESTATIONAPG,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationApg, "idPrestationApg"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(APPrestation.FIELDNAME_DATEPAIEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), datePaiement, "datePaiement"));
        statement.writeField(APPrestation.FIELDNAME_MONTANTBRUT,
                this._dbWriteNumeric(statement.getTransaction(), montantBrut, "montantBrut"));
        statement.writeField(APPrestation.FIELDNAME_TYPE,
                this._dbWriteNumeric(statement.getTransaction(), type, "type"));
        statement.writeField(APPrestation.FIELDNAME_IDDROIT,
                this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
        statement.writeField(APPrestation.FIELDNAME_IDPRESTATIONAPG,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationApg, "idPrestationApg"));
        statement.writeField(APPrestation.FIELDNAME_IDRESTITUTION,
                this._dbWriteNumeric(statement.getTransaction(), idRestitution, "idRestitutionApg"));
        statement.writeField(APPrestation.FIELDNAME_DATEDEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(APPrestation.FIELDNAME_DATEFIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(APPrestation.FIELDNAME_NOMBREJOURSSOLDES,
                this._dbWriteNumeric(statement.getTransaction(), nombreJoursSoldes, "nombreJoursSoldes"));
        statement.writeField(APPrestation.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etat, "etat"));

        statement.writeField(APPrestation.FIELDNAME_GENRE_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), genre, "genre"));

        statement.writeField(APPrestation.FIELDNAME_CONTENUANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), contenuAnnonce, "contenuAnnonce"));
        statement.writeField(APPrestation.FIELDNAME_IDANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
        statement.writeField(APPrestation.FIELDNAME_MONTANT_JOURNALIER,
                this._dbWriteNumeric(statement.getTransaction(), montantJournalier, "montantJournalier"));
        statement.writeField(APPrestation.FIELDNAME_BASICDAILYAMOUNT,
                this._dbWriteNumeric(statement.getTransaction(), basicDailyAmount, "basicDailyAmount"));
        statement.writeField(APPrestation.FIELDNAME_MONTANTALLOCEXPLOITATION, this._dbWriteNumeric(
                statement.getTransaction(), montantAllocationExploitation, "montantAllocationExploitation"));
        statement.writeField(APPrestation.FIELDNAME_DROITACQUIS,
                this._dbWriteNumeric(statement.getTransaction(), droitAcquis, "droitAcquis"));
        statement.writeField(APPrestation.FIELDNAME_IDLOT,
                this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(APPrestation.FIELDNAME_DATECALCUL,
                this._dbWriteDateAMJ(statement.getTransaction(), dateCalcul, "dateCalcul"));
        statement.writeField(APPrestation.FIELDNAME_DATECONTROLE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateControle, "dateControle"));
        statement.writeField(APPrestation.FIELDNAME_REVENUMOYENDETERMINANT,
                this._dbWriteNumeric(statement.getTransaction(), revenuMoyenDeterminant, "revenuMoyenDeterminant"));
        statement.writeField(APPrestation.FIELDNAME_FRAISGARDE,
                this._dbWriteNumeric(statement.getTransaction(), fraisGarde, "fraisGarde"));
        statement.writeField(APPrestation.FIELDNAME_NOREVISION,
                this._dbWriteNumeric(statement.getTransaction(), noRevision, "noRevision"));
        statement.writeField(APPrestation.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(APPrestation.FIELDNAME_IS_MODIFIED_BY_USER, this._dbWriteBoolean(
                statement.getTransaction(), isModifiedByUser, BConstants.DB_TYPE_BOOLEAN_CHAR, "isModifiedByUser"));
    }

    /**
     * @return the basicDailyAmount
     */
    public String getBasicDailyAmount() {
        return basicDailyAmount;
    }

    /**
     * getter pour l'attribut contenu annonce
     * 
     * @return la valeur courante de l'attribut contenu annonce
     */
    public String getContenuAnnonce() {
        return contenuAnnonce;
    }

    /**
     * getter pour l'attribut date calcul
     * 
     * @return la valeur courante de l'attribut date calcul
     */
    public String getDateCalcul() {
        return dateCalcul;
    }

    /**
     * getter pour l'attribut date controle
     * 
     * @return la valeur courante de l'attribut date controle
     */
    public String getDateControle() {
        return dateControle;
    }

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    @Override
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut date paiement
     * 
     * @return la valeur courante de l'attribut date paiement
     */
    public String getDatePaiement() {
        return datePaiement;
    }

    /**
     * getter pour l'attribut droit acquis
     * 
     * @return la valeur courante de l'attribut droit acquis
     */
    public String getDroitAcquis() {
        return droitAcquis;
    }

    /**
     * getter pour l'attribut etat
     * 
     * @return la valeur courante de l'attribut etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * getter pour l'attribut frais garde
     * 
     * @return la valeur courante de l'attribut frais garde
     */
    public String getFraisGarde() {
        return fraisGarde;
    }

    /**
     * @return
     */
    public String getGenre() {
        return genre;
    }

    /**
     * getter pour l'attribut id annonce
     * 
     * @return la valeur courante de l'attribut id annonce
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.prestation.IAPPrestation#idDroit()
     */
    @Override
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut id lot
     * 
     * @return la valeur courante de l'attribut id lot
     */
    public String getIdLot() {
        return idLot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.prestation.IAPPrestation#idPrestation()
     */
    @Override
    public String getIdPrestation() {
        return idPrestationApg;
    }

    /**
     * getter pour l'attribut id prestation apg
     * 
     * @return la valeur courante de l'attribut id prestation apg
     */
    public String getIdPrestationApg() {
        return idPrestationApg;
    }

    /**
     * getter pour l'attribut id restitution
     * 
     * @return la valeur courante de l'attribut id restitution
     */
    public String getIdRestitution() {
        return idRestitution;
    }

    /**
     * @return
     */
    public Boolean getIsModifiedByUser() {
        return isModifiedByUser;
    }

    /**
     * getter pour l'attribut libelle contenu annonce
     * 
     * @return la valeur courante de l'attribut libelle contenu annonce
     */
    public String getLibelleContenuAnnonce() {
        return getSession().getCodeLibelle(getContenuAnnonce());
    }

    /**
     * getter pour l'attribut montant allocation exploitation
     * 
     * @return la valeur courante de l'attribut montant allocation exploitation, montant journalier
     */
    public String getMontantAllocationExploitation() {
        return montantAllocationExploitation;
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * getter pour l'attribut taux journalier
     * 
     * @return la valeur courante de l'attribut taux journalier
     */
    public String getMontantJournalier() {
        return montantJournalier;
    }

    /**
     * getter pour l'attribut nombre jours soldes
     * 
     * @return la valeur courante de l'attribut nombre jours soldes
     */
    public String getNombreJoursSoldes() {
        return nombreJoursSoldes;
    }

    /**
     * getter pour l'attribut no revision
     * 
     * @return la valeur courante de l'attribut no revision
     */
    public String getNoRevision() {
        return noRevision;
    }

    /**
     * getter pour l'attribut remarque
     * 
     * @return la valeur courante de l'attribut remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * getter pour l'attribut revenu moyen determinant
     * 
     * @return la valeur courante de l'attribut revenu moyen determinant
     */
    public String getRevenuMoyenDeterminant() {
        return revenuMoyenDeterminant;
    }

    /**
     * getter pour l'attribut type
     * 
     * @return la valeur courante de l'attribut type
     */
    public String getType() {
        return type;
    }

    /**
     * getter pour l'attribut mise AJour repartitions
     * 
     * @return la valeur courante de l'attribut mise AJour repartitions
     */
    public boolean isMiseAJourRepartitions() {
        return miseAJourRepartitions;
    }

    /**
     * getter pour l'attribut restitution.
     * 
     * @return la valeur courante de l'attribut restitution
     */
    public boolean isRestitution() {
        return IAPAnnonce.CS_RESTITUTION.equals(getContenuAnnonce());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.prestation.IAPPrestation#load(java.lang.String, java.lang.String, java.lang.String)
     */
    public IAPPrestation[] load(String idDroit, String genrePrestation, String orderBy) throws Exception {
        return load(idDroit, new String[] { genrePrestation }, orderBy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.prestation.IAPPrestation#load(java.lang.String, java.lang.String, java.lang.String)
     */
    public IAPPrestation[] load(String idDroit, String[] genrePrestations, String orderBy) throws Exception {
        // On récupère toutes les prestations de ce droit.
        APPrestationManager mgr = new APPrestationManager();
        mgr.setSession(getSession());
        mgr.setForIdDroit(idDroit);
        mgr.setForInGenre(java.util.Arrays.asList(genrePrestations));
        mgr.setOrderBy(orderBy);
        mgr.find(BManager.SIZE_NOLIMIT);

        if (mgr.isEmpty()) {
            return new APPrestation[0];
        } else {
            APPrestationWrapper[] result = new APPrestationWrapper[mgr.size()];
            IAPPrestation[] prestations = (IAPPrestation[]) mgr.getContainer().toArray(new IAPPrestation[mgr.size()]);

            for (int i = 0; i < mgr.size(); i++) {
                result[i] = new APPrestationWrapper();
                result[i].setDateDebut(prestations[i].getDateDebut());
                result[i].setIdPrestation(prestations[i].getIdPrestation());
                result[i].setIdDroit(prestations[i].getIdDroit());
            }

            return result;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.prestation.IAPPrestation#orderBy()
     */
    public String orderBy() {
        return null;
    }

    /**
     * @param basicDailyAmount
     *            the basicDailyAmount to set
     */
    public void setBasicDailyAmount(String basicDailyAmount) {
        this.basicDailyAmount = basicDailyAmount;
    }

    /**
     * setter pour l'attribut contenu annonce
     * 
     * @param contenuAnnonce
     *            une nouvelle valeur pour cet attribut
     */
    public void setContenuAnnonce(String contenuAnnonce) {
        this.contenuAnnonce = contenuAnnonce;
    }

    /**
     * setter pour l'attribut date calcul
     * 
     * @param dateCalcul
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateCalcul(String dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    /**
     * setter pour l'attribut date controle
     * 
     * @param dateControle
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateControle(String dateControle) {
        this.dateControle = dateControle;
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * setter pour l'attribut date paiement
     * 
     * @param datePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    /**
     * setter pour l'attribut droit acquis
     * 
     * @param droitAcquis
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitAcquis(String droitAcquis) {
        this.droitAcquis = droitAcquis;
    }

    /**
     * setter pour l'attribut etat
     * 
     * @param etat
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * setter pour l'attribut frais garde
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFraisGarde(String string) {
        fraisGarde = string;
    }

    /**
     * @param string
     */
    public void setGenre(String string) {
        genre = string;
    }

    /**
     * setter pour l'attribut id annonce
     * 
     * @param idAnnonce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param idDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * setter pour l'attribut id lot
     * 
     * @param idLot
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * setter pour l'attribut id prestation apg
     * 
     * @param idPrestationApg
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestationApg(String idPrestationApg) {
        this.idPrestationApg = idPrestationApg;
    }

    /**
     * setter pour l'attribut id restitution
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRestitution(String string) {
        idRestitution = string;
    }

    /**
     * @param boolean1
     */
    public void setIsModifiedByUser(Boolean boolean1) {
        isModifiedByUser = boolean1;
    }

    /**
     * setter pour l'attribut montant allocation exploitation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantAllocationExploitation(String string) {
        montantAllocationExploitation = string;
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    /**
     * setter pour l'attribut taux journalier
     * 
     * @param tauxJournalier
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantJournalier(String tauxJournalier) {
        montantJournalier = tauxJournalier;
    }

    /**
     * setter pour l'attribut nombre jours soldes
     * 
     * @param nombreJoursSoldes
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursSoldes(String nombreJoursSoldes) {
        this.nombreJoursSoldes = nombreJoursSoldes;
    }

    /**
     * setter pour l'attribut no revision
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoRevision(String string) {
        noRevision = string;
    }

    /**
     * setter pour l'attribut remarque
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setRemarque(String string) {
        remarque = string;
    }

    /**
     * setter pour l'attribut revenu moyen determinant
     * 
     * @param revenuMoyenDeterminant
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenuMoyenDeterminant(String revenuMoyenDeterminant) {
        this.revenuMoyenDeterminant = revenuMoyenDeterminant;
    }

    /**
     * setter pour l'attribut type
     * 
     * @param type
     *            une nouvelle valeur pour cet attribut
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * renseigner a vrai pour que les montants des repartitions de paiements soient recalcules en fonction du montant de
     * cette prestation.
     * 
     * @param miseAJourRepartitions
     *            DOCUMENT ME!
     */
    public void wantMiseAJourRepartitions(boolean miseAJourRepartitions) {
        this.miseAJourRepartitions = miseAJourRepartitions;
    }

}
