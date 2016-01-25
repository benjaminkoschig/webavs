package globaz.osiris.db.ordres;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAPaiementBVR;
import globaz.osiris.db.comptes.CARecouvrement;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.parser.IntBVRDDType2Parser;
import globaz.osiris.parser.IntBVRParser;
import globaz.osiris.parser.IntReferenceBVRParser;
import globaz.osiris.process.CAProcessBVR;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CA organe d'exécution Date de création : (13.12.2001 12:15:03)
 * 
 * @author: Brand
 */
public class CAOrganeExecution extends BEntity implements Serializable, APIOrganeExecution {
    private static final long serialVersionUID = -5829820867422831664L;
    private static final String FIELD_GENRE = "GENRE";
    private static final String FIELD_IDADRDEBTAX = "IDADRDEBTAX";
    private static final String FIELD_IDADRESSEPAIEMENT = "IDADRESSEPAIEMENT";
    private static final String FIELD_IDENTIFIANTDTA = "IDENTIFIANTDTA";
    private static final String FIELD_IDORGANEEXECUTION = "IDORGANEEXECUTION";
    private static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    private static final String FIELD_IDTYPETRAITEMENTBV = "IDTYPETRAITEMENTBV";
    private static final String FIELD_IDTYPETRAITEMENTLS = "IDTYPETRAITEMENTLS";
    private static final String FIELD_IDTYPETRAITEMENTOG = "IDTYPETRAITEMENTOG";
    private static final String FIELD_MODETRANSFERT = "MODETRANSFERT";
    private static final String FIELD_NOADHERENT = "NOADHERENT";
    private static final String FIELD_NOADHERENTBVR = "NOADHERENTBVR";
    private static final String FIELD_NOM = "NOM";
    private static final String FIELD_NOMCLASSEPARSERBVR = "NOMCLASSEPARSERBVR";
    private static final String FIELD_NOMCLASSEPARSERLSV = "NOMCLASSEPARSERLSV";
    private static final String FIELD_NUMINTERNELSV = "NUMINTERNELSV";

    public static final String TABLE_NAME = "CAOREXP";

    private IntAdressePaiement _adresseDebitTaxes = null;
    private IntAdressePaiement _adressePaiement = null;
    private FWParametersSystemCode csGenre = null;
    private FWParametersSystemCodeManager csGenres = null;
    private FWParametersSystemCodeManager csTypeTraitementBVs;
    private FWParametersSystemCodeManager csTypeTraitementLSs;
    private FWParametersSystemCodeManager csTypeTraitementOGs;
    private String genre = new String();
    private String idAdresseDebitTaxes = new String();
    private String idAdressePaiement = new String();

    private String identifiantDTA = new String();
    private String idOrganeExecution = new String();
    private String idRubrique = new String();

    private String idTypeTraitementBV = new String();
    private String idTypeTraitementLS = new String();
    private String idTypeTraitementOG = new String();
    private FWMemoryLog memoryLog = null;
    private String modeTransfert = new String();
    private String noAdherent = new String();
    private String noAdherentBVR = new String();
    private String nom = new String();

    private String nomClasseParserBvr = new String();
    private String nomClasseParserLSV = new String();
    private String numeroRubrique;
    private String numInterneLsv = new String();
    private boolean retrieveBvrFromDataBase = true;
    // création des variable de travail
    private globaz.osiris.db.comptes.CARubrique rubrique = null;
    private long totTransactionErreur = 0;
    private long totTransactionOk = 0;

    // création des codes systèmes
    private FWParametersUserCode ucGenre = null;

    private FWParametersUserCode ucTypeTraitementBV;
    private FWParametersUserCode ucTypeTraitementLS;
    private FWParametersUserCode ucTypeTraitementOG;

    // TODO : Modification en cours pour la gestion des paiements sur une
    // opération auxiliaire.

    /**
     * Commentaire relatif au constructeur CAOrganeExecution
     */
    public CAOrganeExecution() {
        super();
    }

    /**
     * Date de création : (29.01.2002 14:24:47)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdOrganeExecution(this._incCounter(transaction, idOrganeExecution));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAOrganeExecution.TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        genre = statement.dbReadNumeric(CAOrganeExecution.FIELD_GENRE);
        idAdresseDebitTaxes = statement.dbReadNumeric(CAOrganeExecution.FIELD_IDADRDEBTAX);
        idAdressePaiement = statement.dbReadNumeric(CAOrganeExecution.FIELD_IDADRESSEPAIEMENT);
        identifiantDTA = statement.dbReadString(CAOrganeExecution.FIELD_IDENTIFIANTDTA);
        idOrganeExecution = statement.dbReadNumeric(CAOrganeExecution.FIELD_IDORGANEEXECUTION);
        idRubrique = statement.dbReadNumeric(CAOrganeExecution.FIELD_IDRUBRIQUE);
        nom = statement.dbReadString(CAOrganeExecution.FIELD_NOM);
        idTypeTraitementBV = statement.dbReadNumeric(CAOrganeExecution.FIELD_IDTYPETRAITEMENTBV);
        idTypeTraitementLS = statement.dbReadNumeric(CAOrganeExecution.FIELD_IDTYPETRAITEMENTLS);
        idTypeTraitementOG = statement.dbReadNumeric(CAOrganeExecution.FIELD_IDTYPETRAITEMENTOG);
        nomClasseParserBvr = statement.dbReadString(CAOrganeExecution.FIELD_NOMCLASSEPARSERBVR);
        noAdherentBVR = statement.dbReadString(CAOrganeExecution.FIELD_NOADHERENTBVR);
        nomClasseParserLSV = statement.dbReadString(CAOrganeExecution.FIELD_NOMCLASSEPARSERLSV);
        noAdherent = statement.dbReadString(CAOrganeExecution.FIELD_NOADHERENT);
        numInterneLsv = statement.dbReadString(CAOrganeExecution.FIELD_NUMINTERNELSV);
        modeTransfert = statement.dbReadNumeric(CAOrganeExecution.FIELD_MODETRANSFERT);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Retrouver l'id de la rubrique
        if (!JadeStringUtil.isBlank(numeroRubrique)) {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForIdExterne(numeroRubrique);
            mgr.find();
            if (mgr.size() != 0) {
                idRubrique = ((CARubrique) mgr.getFirstEntity()).getIdRubrique();
            }
        }

        _propertyMandatory(statement.getTransaction(), getIdOrganeExecution(), getSession().getLabel("7205"));
        _propertyMandatory(statement.getTransaction(), getNom(), getSession().getLabel("7265"));
        _propertyMandatory(statement.getTransaction(), getRubrique().getIdExterne(), getSession().getLabel("7266"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CAOrganeExecution.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CAOrganeExecution.FIELD_GENRE,
                this._dbWriteNumeric(statement.getTransaction(), getGenre(), "genre"));
        statement.writeField(CAOrganeExecution.FIELD_IDADRDEBTAX,
                this._dbWriteNumeric(statement.getTransaction(), getIdAdresseDebitTaxes(), "idAdrDebTax"));
        statement.writeField(CAOrganeExecution.FIELD_IDADRESSEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdAdressePaiement(), "idAdressePaiement"));
        statement.writeField(CAOrganeExecution.FIELD_IDENTIFIANTDTA,
                this._dbWriteString(statement.getTransaction(), getIdentifiantDTA(), "identifiantDTA"));
        statement.writeField(CAOrganeExecution.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(CAOrganeExecution.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CAOrganeExecution.FIELD_NOM,
                this._dbWriteString(statement.getTransaction(), getNom(), "nom"));
        statement.writeField(CAOrganeExecution.FIELD_IDTYPETRAITEMENTBV,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeTraitementBV(), "idTypeTritementBV"));
        statement.writeField(CAOrganeExecution.FIELD_IDTYPETRAITEMENTLS,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeTraitementLS(), "idTypeTraitementLS"));
        statement.writeField(CAOrganeExecution.FIELD_IDTYPETRAITEMENTOG,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeTraitementOG(), "idTypeTraitementOG"));
        statement.writeField(CAOrganeExecution.FIELD_NOMCLASSEPARSERBVR,
                this._dbWriteString(statement.getTransaction(), getNomClasseParserBvr(), "nomClasseParserBvr"));
        statement.writeField(CAOrganeExecution.FIELD_NOADHERENTBVR,
                this._dbWriteString(statement.getTransaction(), getNoAdherentBVR(), "noAdherentBVR"));
        statement.writeField(CAOrganeExecution.FIELD_NOMCLASSEPARSERLSV,
                this._dbWriteString(statement.getTransaction(), getNomClasseParserLSV(), "nomClasseParserLSV"));
        statement.writeField(CAOrganeExecution.FIELD_NOADHERENT,
                this._dbWriteString(statement.getTransaction(), getNoAdherent(), "noAdherent"));
        statement.writeField(CAOrganeExecution.FIELD_NUMINTERNELSV,
                this._dbWriteString(statement.getTransaction(), getNumInterneLsv(), "numeroInterneLSV"));
        statement.writeField(CAOrganeExecution.FIELD_MODETRANSFERT,
                this._dbWriteNumeric(statement.getTransaction(), getModeTransfert(), "modeTransfert"));
    }

    /**
     * Contrôle que le compte annexe du plan corresponde à l'idExterneRole de la référence BVR
     * 
     * @param refBVR
     * @param planRecouvrement
     * @param oper
     * @throws Exception
     */
    private void checkCompteAnnexe(IntReferenceBVRParser refBVR, CAPlanRecouvrement planRecouvrement,
            CAPaiementBVR oper, IntBVRParser parser) throws Exception {
        // Contrôle que le compte annexe du plan corresponde à l'idExterneRole
        // de la référence BVR
        if ((planRecouvrement != null) && !planRecouvrement.getIdCompteAnnexe().equals(refBVR.getIdCompteAnnexe())) {
            parser.getMemoryLog().logMessage(
                    "7397",
                    "plan idCA=" + planRecouvrement.getIdCompteAnnexe() + "<> refBVR idCA="
                            + refBVR.getIdCompteAnnexe(), FWMessage.ERREUR, this.getClass().getName());
            oper.setMemoryLog(parser.getMemoryLog());
            oper.setEtat(APIOperation.ETAT_ERREUR);
        }
    }

    /**
     * Si part pénale, le paiement est mis en erreur
     * 
     * @author: sel Créé le : 13 févr. 07
     * @param planRecouvrement
     */
    private void checkPartPenale(CAPlanRecouvrement planRecouvrement, CAPaiementBVR paiement) {
        if (planRecouvrement.getPartPenale().booleanValue()) {
            getMemoryLog().logMessage("7394", null, FWMessage.ERREUR, this.getClass().getName());
            paiement.setMemoryLog(getMemoryLog());
            paiement.setEtat(APIOperation.ETAT_ERREUR);
        }
    }

    /**
     * Instancier un nouveau paiement BVR
     * 
     * @author: sel Créé le : 9 févr. 07
     * @param context
     * @param fTotal
     * @param parser
     * @param refBVR
     * @param jrn
     * @return
     * @throws Exception
     */
    private CAPaiementBVR createNewPaiementBvr(globaz.osiris.process.CAProcessBVR context, FWCurrency fTotal,
            IntBVRParser parser, IntReferenceBVRParser refBVR, CAJournal jrn) throws Exception {
        // Instancier un nouveau paiement BVR
        CAPaiementBVR oper = new CAPaiementBVR();
        oper.setSession(context.getSession());

        // Partager le log
        oper.setMemoryLog(parser.getMemoryLog());

        // Décomposer le numéro de référence
        oper.setIdCompteAnnexe(refBVR.getIdCompteAnnexe());

        if (!JadeStringUtil.isBlankOrZero(refBVR.getIdSection())) {
            oper.setIdSection(refBVR.getIdSection());
        } else if (!JadeStringUtil.isBlankOrZero(refBVR.getIdExterneSection())) {
            if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(refBVR.getIdTypeSection())
                    && refBVR.isModeCreditBulletinNeutre()) {
                // Pour paiement avant comptabilisation de la facture (WebMetier)
                oper.setSection(createSection(refBVR, jrn));
                oper.setIdExterneSectionEcran(refBVR.getIdExterneSection());
                oper.setNewSection(true);
            }
        }
        // Charger les attributs ATTENTION !!! pour les comptes annexes auxiliaires l'idCompteAnnexe et l'idSection sont
        // modifiés par la méthode initPaiementBvr
        oper = initPaiementBvr(oper, parser, jrn);
        oper.setMontant(parser.getMontant());

        // Demander une validation sur la base des informations du BVR
        oper.validerFromBVR(context.getTransaction(), false);

        // Cas du BMS pour un rectificatif. Un paiement arrive sur un bulletin neutre en partie payée
        if (oper.getMemoryLog().getMessagesToVector().size() == 1) {
            FWMessage msg = (FWMessage) oper.getMemoryLog().getMessagesToVector().firstElement();
            if ("7401".equals(msg.getMessageId())) {
                oper.getMemoryLog().clear();
                oper.setEtat(APIOperation.ETAT_OUVERT);
            }
        }
        // Incrémenter le montant total
        fTotal.sub(oper.getMontant());

        // Test pour un paiement provenant d'opérations auxiliaires
        // checkOperationAuxiliaire(oper); //Déplacé dans CAPaiement._valider()

        // Création du paiement BVR
        if (!context.getSimulation().booleanValue()) {
            oper.add(context.getTransaction());
            if (oper.hasErrors()) {
                _addError(context.getTransaction(), getSession().getLabel("5331"));
                throw new Exception(getSession().getLabel("5331"));
            }
        }

        return oper;
    }

    /**
     * Création des paiements BVR selon le sursis au paiement et Incrémenter le montant total
     * 
     * @author: sel Créé le : 9 févr. 07
     * @param context
     * @param fTotal
     * @param parser
     * @param jrn
     * @param planRecouvrement
     * @return
     * @throws Exception
     */
    private CAPaiementBVR[] createPaiementBvrPlan(CAProcessBVR context, FWCurrency fTotal, IntBVRParser parser,
            CAJournal jrn, CAPlanRecouvrement planRecouvrement, IntReferenceBVRParser refBVR) throws Exception {
        // Création du paiement BVR et Incrémenter le montant total
        CAPaiementBVR[] paiements = CAPlanRecouvrement.serviceComputePaiements(context.getSession(),
                planRecouvrement.getIdPlanRecouvrement(), new BigDecimal(parser.getMontant()), parser.getMemoryLog());

        for (int i = 0; i < paiements.length; i++) {
            initPaiementBvr(paiements[i], parser, jrn);
            // Demander une validation sur la base des informations du BVR
            paiements[i].validerFromBVR(context.getTransaction(), true);
            // Incrémenter le montant total
            fTotal.sub(paiements[i].getMontant());
            checkPartPenale(planRecouvrement, paiements[i]);

            // Contrôle que le compte annexe du plan corresponde à
            // l'idExterneRole de la référence BVR
            checkCompteAnnexe(refBVR, planRecouvrement, paiements[i], parser);

            // Création du paiement BVR
            if (!context.getSimulation().booleanValue()) {
                paiements[i].add(context.getTransaction());
                if (paiements[i].hasErrors()) {
                    throw new Exception(getSession().getLabel("5331") + " Réf: " + parser.getNumeroReference());
                }
            }

            updateStatistique(paiements[i]);
        }
        return paiements;
    }

    /**
     * @param refBVR parser référence BVR
     * @param jrn journal de création
     * @return section créée
     */
    private CASection createSection(IntReferenceBVRParser refBVR, CAJournal jrn) {
        CASection sec = new CASection();
        sec.setSession(getSession());
        sec.setIdCompteAnnexe(refBVR.getIdCompteAnnexe());
        sec.setIdTypeSection(refBVR.getIdTypeSection());
        sec.setIdExterne(refBVR.getIdExterneSection());
        if (jrn != null) {
            sec.setIdJournal(jrn.getIdJournal());
            sec.setDateSection(jrn.getDateValeurCG());
        }
        return sec;
    }

    /**
     * Date de création : (18.02.2002 10:55:57)
     * 
     * @param context
     *            globaz.osiris.process.CAProcessBVR
     */
    public String executeBVR(globaz.osiris.process.CAProcessBVR context) {
        // TODO modifier le traitement si Opération auxiliaire

        long totTransactionTraitee = 0;
        totTransactionOk = 0;
        totTransactionErreur = 0;

        try {
            // Partager le log du contexte fourni
            setMemoryLog(context.getMemoryLog());

            FWCurrency fTotal = new FWCurrency();

            // Instancier un parser en fonction du type
            IntBVRParser parser = initParser(context);
            // Instancier la classe qui permet de décomposer le numéro de
            // référence
            IntReferenceBVRParser refBVR = initRefBvr(context);
            // Créer un nouveau journal
            CAJournal jrn = initJournal(context);

            // Vérifier la condition de sortie
            if (context.isAborted() || (parser == null) || (refBVR == null) || (jrn == null)) {
                return null;
            }

            // Forcer un nouveau log
            parser.setMemoryLog(null);

            // Détermine le nombre de ligne dans le fichier
            int count = 0;
            while (parser.parseNextElement()) {
                count++;
            }
            context.setProgressScaleValue(count);
            parser.getInputReader().close();
            parser = initParser(context);
            // Forcer un nouveau log
            parser.setMemoryLog(null);

            // Tant qu'il y a des lignes à lire
            while (parser.parseNextElement()) {
                // Vérifier la condition de sortie
                if (context.isAborted()) {
                    context.setProgressDescription(parser.getNumeroReference());
                    return null;
                }

                // Vérifier le log
                if (parser.getMemoryLog().isOnFatalLevel()) {
                    _addError(context.getTransaction(), getMemoryLog().getMessage(getMemoryLog().size()).getMessage());
                    _addError(context.getTransaction(), getSession().getLabel("5330"));
                    return null;
                }

                // Vérifier le numéro d'adhérent existe et correspond
                if (parser.getNumeroAdherent().equalsIgnoreCase(getNoAdherentBVR())
                        || (Integer.parseInt(parser.getNumeroAdherent()) == CAApplication.getApplicationOsiris()
                                .getCAParametres().getAncienNoAdherentBVR())) {
                    // Vérifier s'il s'agit d'une ligne
                    if (parser.getTypeTransaction().equals(IntBVRParser.TRANSACTION)) {
                        try {
                            refBVR.setReference(parser.getNumeroReference(), getSession(), getNumInterneLsv());
                        } catch (Exception e) {
                            getMemoryLog().logMessage("5334", e.getMessage(), FWMessage.ERREUR,
                                    this.getClass().getName());
                        }

                        // Vérifier s'il s'agit d'un plan de paiement
                        if (refBVR.isPlanPaiement()) {
                            traitementPlanPaiement(context, fTotal, parser, refBVR, jrn);
                        } else { // Pas plan de paiement
                            CAPaiementBVR oper = createNewPaiementBvr(context, fTotal, parser, refBVR, jrn);

                            // Statistiques
                            if (oper.getMemoryLog().hasErrors()) {
                                totTransactionErreur++;
                            } else {
                                totTransactionOk++;
                            }
                        }
                        // Incrémenter le nombre de transactions
                        totTransactionTraitee++;
                    }

                    // S'il s'agit du footer
                    if (parser.getTypeTransaction().equals(IntBVRParser.FOOTER)) {
                        totalForFooter(totTransactionTraitee, fTotal, parser);
                    }
                } else { // Numéro d'adhérent n'existe pas ou correspond pas
                    parser.getMemoryLog().logMessage("5339", parser.getNumeroAdherent(), FWMessage.ERREUR,
                            this.getClass().getName());
                }

                // Charger le log d'erreur dans le log principal si simulation
                if (context.getSimulation().booleanValue()) {
                    if (parser.getMemoryLog().hasMessages()) {
                        getMemoryLog().logMessage("5338", parser.getNumeroTransaction(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getMemoryLog().logMessage(parser.getCurrentBuffer(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getMemoryLog().logMessage(parser.getMemoryLog());
                    }
                }

                // Forcer la création d'un nouveau log
                parser.setMemoryLog(null);

                context.incProgressCounter();
            }

            // Si aucune ligne lue
            if (totTransactionTraitee == 0) {
                _addError(context.getTransaction(), getSession().getLabel("5349"));
                return null;
            }

            // Exécuter le mise en compte
            miseEnCompte(context, totTransactionErreur, jrn);

            // Indiquer les statistiques
            getMemoryLog().logMessage("5340", String.valueOf(totTransactionTraitee), FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("5341", String.valueOf(totTransactionOk), FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("5342", String.valueOf(totTransactionErreur), FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog()
                    .logMessage("5347", fTotal.toStringFormat(), FWMessage.INFORMATION, this.getClass().getName());

            // Libérer le reader
            try {
                parser.getInputReader().close();
            } catch (Exception e) {
                e.getMessage();
            }

            return jrn.getIdJournal();
        } catch (Exception e) {
            _addError(context.getTransaction(), e.toString());
            return null;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:55:57)
     * 
     * @param context
     *            globaz.osiris.process.CAProcessLSV
     */
    public void executeLSV(globaz.osiris.process.CAProcessLSV context) {
        // Sous contrôle d'exceptions
        try {
            // Initialiser
            IntBVRDDType2Parser parser = null;
            IntReferenceBVRParser refLSV = null;

            // Partager le log du contexte fourni
            setMemoryLog(context.getMemoryLog());

            // Vérifier si fin de processus désirée
            if (context.isAborted()) {
                return;
            }

            // Instancier un parser en fonction du type
            if (getIdTypeTraitementLS().equals(APIOrganeExecution.LSV_POSTE)) {
                parser = new CABVRDD2Parser();
            } else {
                getMemoryLog().logMessage("5263", getIdTypeTraitementLS(), FWMessage.FATAL, this.getClass().getName());
                return;
            }

            // Passer les paramètres
            parser.setMemoryLog(getMemoryLog());
            parser.setEchoToConsole(context.getEchoToConsole());

            // Instancier un stream de lecture
            try {
                JadeFsFacade.copyFile(
                        "jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + context.getFileName(), Jade
                                .getInstance().getHomeDir() + "work/" + context.getFileName());

                parser.setInputReader(new BufferedReader(new FileReader(new File(Jade.getInstance().getHomeDir()
                        + "work/" + context.getFileName()))));
            } catch (FileNotFoundException ex) {
                _addError(context.getTransaction(), getSession().getLabel("5264") + " " + context.getFileName());
                return;
            }

            // Vérifier la condition de sortie
            if (context.isAborted()) {
                return;
            }

            // Instancier la classe qui permet de décomposer le numéro de
            // référence
            if (JadeStringUtil.isBlank(getNomClasseParserLSV())) {
                _addError(context.getTransaction(), getSession().getLabel("5332"));
                return;
            } else {
                try {
                    Class<?> cl = Class.forName(getNomClasseParserLSV());
                    refLSV = (IntReferenceBVRParser) cl.newInstance();
                    refLSV.setISession(context.getSession());
                } catch (Exception ex) {
                    _addError(context.getTransaction(), getSession().getLabel("5333"));
                    return;
                }
            }

            // Créer un nouveau journal
            CAJournal jrn = new CAJournal();
            if (!context.getSimulation().booleanValue()) {
                jrn.setSession(context.getSession());
                jrn.setLibelle(context.getLibelle());
                jrn.setDateValeurCG(context.getDateValeur());
                jrn.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);
                jrn.add(context.getTransaction());
                if (jrn.isNew() || jrn.hasErrors()) {
                    _addError(context.getTransaction(), getSession().getLabel("5225"));
                    return;
                }
            }

            // Forcer un nouveau log
            parser.setMemoryLog(null);

            long lNombreTr = 0;
            long lStatOK = 0;
            long lStatKO = 0;
            FWCurrency fTotalCredit = new FWCurrency();
            FWCurrency fTotalRefus = new FWCurrency();
            FWCurrency fTotalContrePassation = new FWCurrency();
            FWCurrency fTotalPrix = new FWCurrency();

            // Tant qu'il y a des lignes à lire
            while (parser.parseNextElement()) {

                // Vérifier la condition de sortie
                if (context.isAborted()) {
                    return;
                }

                // Vérifier le log
                if (parser.getMemoryLog().isOnFatalLevel()) {
                    _addError(context.getTransaction(), getMemoryLog().getMessage(getMemoryLog().size()).getMessage());
                    _addError(context.getTransaction(), getSession().getLabel("5266"));
                    return;
                }

                // Vérifier le numéro d'adhérent
                int _parserNumeroAdherent = Integer.parseInt(parser.getNumeroAdherent());
                int _numeroAdherent = Integer.parseInt(getNoAdherent());
                if (_parserNumeroAdherent != _numeroAdherent) {
                    parser.getMemoryLog().logMessage("5339", parser.getNumeroAdherent(), FWMessage.ERREUR,
                            this.getClass().getName());
                } else {
                    if (parser.getGenreTransaction().equals("081") || parser.getGenreTransaction().equals("084")) {
                        CARecouvrement oper = new CARecouvrement();
                        oper.setSession(context.getSession());

                        // Partager le log
                        oper.setMemoryLog(parser.getMemoryLog());

                        // Charger les attributs
                        oper.setIdJournal(jrn.getIdJournal());
                        oper.setIdOrganeExecution(getIdOrganeExecution());
                        oper.setGenreTransaction(parser.getGenreTransaction());
                        oper.setReference(parser.getNumeroReference());
                        oper.setDateEcheance(parser.getDateEcheance());
                        oper.setCodeRefus("2450" + parser.getCodeRefus());

                        if (parser.getGenreTransaction().equals("084") && parser.getCodeRefus().equals("02")) {
                            oper.setCodeDebitCredit(APIEcriture.DEBIT);
                            oper.setMontant(parser.getMontantContrePassation());
                        } else if (parser.getGenreTransaction().equals("084")) {
                            oper.setCodeDebitCredit(APIEcriture.DEBIT);
                            oper.setMontant(parser.getMontantRefus());
                        } else if (parser.getGenreTransaction().equals("081")) {
                            oper.setCodeDebitCredit(APIEcriture.CREDIT);
                            oper.setMontant(parser.getMontant());
                        }

                        fTotalContrePassation.add(parser.getMontantContrePassation());
                        fTotalRefus.add(parser.getMontantRefus());
                        fTotalCredit.add(parser.getMontant());
                        fTotalPrix.add(parser.getPrix());

                        // Décomposer le numéro de référence
                        try {
                            refLSV.setReference(parser.getNumeroReference(), getSession(), getNumInterneLsv());
                            oper.setIdCompteAnnexe(refLSV.getIdCompteAnnexe());
                            oper.setIdSection(refLSV.getIdSection());
                        } catch (Exception e) {
                            getMemoryLog()
                                    .logMessage("5334", e.toString(), FWMessage.ERREUR, this.getClass().getName());
                        }

                        // Demander une validation sur la base des informations
                        // du LSV
                        oper.validerFromLSV(context.getTransaction());

                        // Statistiques
                        if (oper.getMemoryLog().hasErrors()) {
                            lStatKO++;
                        } else {
                            lStatOK++;
                        }

                        // Incrémenter le nombre de transactions et le montant
                        // total
                        lNombreTr++;

                        // Création du paiement LSV
                        if (!context.getSimulation().booleanValue()) {
                            oper.add(context.getTransaction());
                            if (oper.hasErrors() || oper.isNew()) {
                                _addError(context.getTransaction(), getSession().getLabel("5331"));
                                return;
                            }
                        }
                    } else if (parser.getGenreTransaction().equals("097")) {

                        // Convertir total et nombre de transactions
                        FWCurrency fTotalControleCredit = new FWCurrency(parser.getMontant());
                        FWCurrency fTotalControleRefus = new FWCurrency(parser.getMontantRefus());
                        FWCurrency fTotalControleContrePassation = new FWCurrency(parser.getMontantContrePassation());
                        FWCurrency fTotalControlePrix = new FWCurrency(parser.getTotalPrix());

                        long lNombreTrControle = 0;
                        try {
                            lNombreTrControle = Long.parseLong(parser.getNbrTransactionsEcrituresCredit());
                            lNombreTrControle += Long.parseLong(parser.getNbrTransactionsRefus());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fTotalCredit.compareTo(fTotalControleCredit) != 0) {
                            getMemoryLog().logMessage("5336", fTotalControleCredit.toStringFormat(), FWMessage.ERREUR,
                                    this.getClass().getName());
                        }

                        if (fTotalControleRefus.compareTo(fTotalRefus) != 0) {
                            getMemoryLog().logMessage("5336", fTotalControleRefus.toStringFormat(), FWMessage.ERREUR,
                                    this.getClass().getName());
                        }

                        if (fTotalControleContrePassation.compareTo(fTotalContrePassation) != 0) {
                            getMemoryLog().logMessage("5336", fTotalControleContrePassation.toStringFormat(),
                                    FWMessage.ERREUR, this.getClass().getName());
                        }

                        if (fTotalControlePrix.compareTo(fTotalPrix) != 0) {
                            getMemoryLog().logMessage("5336", fTotalControlePrix.toStringFormat(), FWMessage.ERREUR,
                                    this.getClass().getName());
                        }

                        if (lNombreTr != lNombreTrControle) {
                            getMemoryLog().logMessage("5335", parser.getMontant(), FWMessage.ERREUR,
                                    this.getClass().getName());
                        }

                    }
                }

                // Charger le log d'erreur dans le log principal si simulation
                if (context.getSimulation().booleanValue()) {
                    if (parser.getMemoryLog().hasMessages()) {
                        getMemoryLog().logMessage("5338", parser.getNumCourantTransaction(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getMemoryLog().logMessage(parser.getCurrentBuffer(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getMemoryLog().logMessage(parser.getMemoryLog());
                    }
                }

                // Forcer la création d'un nouveau log
                parser.setMemoryLog(null);
            }

            // Si aucune ligne lue
            if (lNombreTr == 0) {
                _addError(context.getTransaction(), getSession().getLabel("5349"));
                return;
            }

            // Exécuter le mise en compte
            if (!context.getSimulation().booleanValue() && !getMemoryLog().isOnFatalLevel()) {

                // Comptabiliser s'il n'y a aucune erreur
                if (lStatKO == 0) {
                    new CAComptabiliserJournal().comptabiliser(context, jrn);
                } else {
                    jrn.setEtat(CAJournal.ERREUR);
                }

                // Mettre a jour
                jrn.update(context.getTransaction());
                if (jrn.hasErrors() || jrn.isNew()) {
                    _addError(context.getTransaction(), getSession().getLabel("5225"));
                    return;
                }

                // Vérifier les erreurs de comptabilisation
                if (jrn.getEtat().equals(CAJournal.ERREUR)) {
                    getMemoryLog().logMessage("5337", null, FWMessage.ERREUR, this.getClass().getName());
                }
            }

            // Indiquer les statistique
            getMemoryLog().logMessage("5340", String.valueOf(lNombreTr), FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog()
                    .logMessage("5341", String.valueOf(lStatOK), FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog()
                    .logMessage("5342", String.valueOf(lStatKO), FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage("5347", fTotalCredit.toStringFormat(), FWMessage.INFORMATION,
                    this.getClass().getName());

            // Libérer le reader
            try {
                parser.getInputReader().close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            _addError(context.getTransaction(), e.toString());
        }
    }

    /**
     * Date de création : (08.02.2002 11:01:56)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    @Override
    public IntAdressePaiement getAdresseDebitTaxes() throws Exception {
        // Si l'ordre groupé n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdAdresseDebitTaxes())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_adresseDebitTaxes == null) {
            // Instancier une nouvelle adresse de paiement
            try {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                _adresseDebitTaxes = (IntAdressePaiement) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntAdressePaiement.class);

                // Récupérer l'adresse
                _adresseDebitTaxes.retrieve(getIdAdresseDebitTaxes());
                if (_adresseDebitTaxes.isNew()) {
                    _adresseDebitTaxes = null;
                }
            } catch (Exception e) {
                _addError(null, e.toString());
                _adresseDebitTaxes = null;
            }
        }

        return _adresseDebitTaxes;
    }

    /**
     * Date de création : (08.02.2002 11:01:56)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    @Override
    public IntAdressePaiement getAdressePaiement() throws Exception {
        // Si l'ordre groupé n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_adressePaiement == null) {
            // Instancier une nouvelle adresse de paiement
            try {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                _adressePaiement = (IntAdressePaiement) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntAdressePaiement.class);

                // Récupérer l'adresse
                _adressePaiement.retrieve(getIdAdressePaiement());
                if (_adressePaiement.isNew()) {
                    _adressePaiement = null;
                }
            } catch (Exception e) {
                _addError(null, e.toString());
                _adressePaiement = null;
            }
        }

        if (_adressePaiement != null) {
            _adressePaiement.setISession(getSession());
        }

        return _adressePaiement;
    }

    /**
     * Date de création : (10.01.2002 16:37:11)
     * 
     * @return FWParametersSystemCode
     */
    public FWParametersSystemCode getCsGenre() {
        if (csGenre == null) {
            // liste pas encore chargee, on la charge
            csGenre = new FWParametersSystemCode();
            csGenre.getCode(getGenre());
        }
        return csGenre;
    }

    /**
     * Date de création : (10.01.2002 16:38:30)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsGenres() {
        // liste déjà chargée ?
        if (csGenres == null) {
            // liste pas encore chargée, on la charge
            csGenres = new FWParametersSystemCodeManager();
            csGenres.setSession(getSession());
            csGenres.getListeCodesSup(CAOrganeExecution.FIELD_GENRE, getSession().getIdLangue());
        }
        return csGenres;
    }

    /**
     * Date de création : (10.01.2002 16:38:30)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeTraitementBVs() {
        // liste déjà chargée ?
        if (csTypeTraitementBVs == null) {
            // liste pas encore chargée, on la charge
            csTypeTraitementBVs = new FWParametersSystemCodeManager();
            csTypeTraitementBVs.setSession(getSession());
            csTypeTraitementBVs.getListeCodes("OSIOGRBVR", getSession().getIdLangue());
        }
        return csTypeTraitementBVs;
    }

    /**
     * Date de création : (10.01.2002 16:38:30)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeTraitementLSs() {
        // liste déjà chargée ?
        if (csTypeTraitementLSs == null) {
            // liste pas encore chargée, on la charge
            csTypeTraitementLSs = new FWParametersSystemCodeManager();
            csTypeTraitementLSs.setSession(getSession());
            csTypeTraitementLSs.getListeCodes("OSIOGRBVR", getSession().getIdLangue());
        }
        return csTypeTraitementLSs;
    }

    public FWParametersSystemCodeManager getCsTypeTraitementOGs() {
        // liste déjà chargée ?
        if (csTypeTraitementOGs == null) {
            // liste pas encore chargée, on la charge
            csTypeTraitementOGs = new FWParametersSystemCodeManager();
            csTypeTraitementOGs.setSession(getSession());
            csTypeTraitementOGs.getListeCodes("OSIOGRBOG", getSession().getIdLangue());
        }
        return csTypeTraitementOGs;
    }

    /**
     * Getter
     */
    @Override
    public String getGenre() {
        return genre;
    }

    public String getIdAdresseDebitTaxes() {
        return idAdresseDebitTaxes;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    @Override
    public String getIdentifiantDTA() {
        return identifiantDTA;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    @Override
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Date de création : (18.02.2002 10:16:36)
     * 
     * @return String
     */
    public String getIdTypeTraitementBV() {
        return idTypeTraitementBV;
    }

    /**
     * Date de création : (18.02.2002 10:16:50)
     * 
     * @return String
     */
    public String getIdTypeTraitementLS() {
        return idTypeTraitementLS;
    }

    /**
     * Date de création : (18.02.2002 10:54:35)
     * 
     * @return FWMemoryLog
     */
    public FWMemoryLog getMemoryLog() {
        if (memoryLog == null) {
            memoryLog = new FWMemoryLog();
            memoryLog.setSession(getSession());
        }
        return memoryLog;
    }

    @Override
    public String getModeTransfert() {
        return modeTransfert;
    }

    /**
     * Date de création : (28.11.2002 11:49:32)
     * 
     * @return String
     */
    @Override
    public String getNoAdherent() {
        return noAdherent;
    }

    /**
     * Date de création : (21.02.2002 15:39:51)
     * 
     * @return String
     */
    @Override
    public String getNoAdherentBVR() {
        return noAdherentBVR;
    }

    @Override
    public String getNom() {
        return nom;
    }

    /**
     * Date de création : (21.02.2002 09:06:20)
     * 
     * @return String
     */
    public String getNomClasseParserBvr() {
        return nomClasseParserBvr;
    }

    /**
     * Date de création : (28.11.2002 11:48:12)
     * 
     * @return String
     */
    public String getNomClasseParserLSV() {
        return nomClasseParserLSV;
    }

    /**
     * @author: sel Créé le : 30 nov. 06
     * @return
     */
    public String getNumeroRubrique() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(getIdRubrique());
        try {
            rubrique.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubrique.isNew()) {
            return rubrique.getIdExterne();
        } else {
            return "";
        }
    }

    @Override
    public String getNumInterneLsv() {
        return numInterneLsv;
    }

    /**
     * Date de création : (10.01.2002 16:53:13)
     * 
     * @return globaz.osiris.db.utils.CALog
     */
    public CARubrique getRubrique() {
        // Si rubrique n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            return null;
        }

        // Si rubrique pas déjà chargé
        if (rubrique == null) {
            // Instancier une nouvelle rubrique
            rubrique = new CARubrique();
            rubrique.setSession(getSession());

            // Récupérer la rubrique en question
            rubrique.setIdRubrique(getIdRubrique());
            try {
                rubrique.retrieve();
            } catch (Exception e) {
                _addError(null, e.toString());
                return null;
            }
        }

        return rubrique;
    }

    /**
     * Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcGenre() {
        if (ucGenre == null) {
            // liste pas encore chargee, on la charge
            ucGenre = new FWParametersUserCode();
            ucGenre.setSession(getSession());

            // Récupérer le code système dans la langue de l'utilisateur
            ucGenre.setIdCodeSysteme(getGenre());
            ucGenre.setIdLangue(getSession().getIdLangue());
            try {
                ucGenre.retrieve();
                if (ucGenre.isNew() || ucGenre.hasErrors()) {
                    _addError(null, getSession().getLabel("7251"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7251"));
            }
        }
        return ucGenre;
    }

    /**
     * Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcTypeTraitementBV() {
        if (ucTypeTraitementBV == null) {
            // liste pas encore chargee, on la charge
            ucTypeTraitementBV = new FWParametersUserCode();
            ucTypeTraitementBV.setSession(getSession());

            // Récupérer le code système dans la langue de l'utilisateur
            ucTypeTraitementBV.setIdCodeSysteme(getIdTypeTraitementBV());
            ucTypeTraitementBV.setIdLangue(getSession().getIdLangue());
            try {
                ucTypeTraitementBV.retrieve();
                if (ucTypeTraitementBV.isNew() || ucTypeTraitementBV.hasErrors()) {
                    _addError(null, getSession().getLabel("7251"));
                }
            } catch (Exception e) {
                _addError(null, e.toString());
            }
        }

        return ucTypeTraitementBV;
    }

    /**
     * Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcTypeTraitementLS() {
        if (ucTypeTraitementLS == null) {
            // liste pas encore chargee, on la charge
            ucTypeTraitementLS = new FWParametersUserCode();
            ucTypeTraitementLS.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            ucTypeTraitementLS.setIdCodeSysteme(getIdTypeTraitementLS());
            ucTypeTraitementLS.setIdLangue(getSession().getIdLangue());
            try {
                ucTypeTraitementLS.retrieve();
                if (ucTypeTraitementLS.isNew() || ucTypeTraitementLS.hasErrors()) {
                    _addError(null, getSession().getLabel("7251"));
                }
            } catch (Exception e) {
                _addError(null, e.toString());
            }
        }

        return ucTypeTraitementLS;
    }

    public FWParametersUserCode getUcTypeTraitementOG() {
        if (ucTypeTraitementOG == null) {
            // liste pas encore chargee, on la charge
            ucTypeTraitementOG = new FWParametersUserCode();
            ucTypeTraitementOG.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            ucTypeTraitementOG.setIdCodeSysteme(getIdTypeTraitementOG());
            ucTypeTraitementOG.setIdLangue(getSession().getIdLangue());
            try {
                ucTypeTraitementOG.retrieve();
                if (ucTypeTraitementOG.isNew() || ucTypeTraitementOG.hasErrors()) {
                    _addError(null, getSession().getLabel("7251"));
                }
            } catch (Exception e) {
                _addError(null, e.toString());
            }
        }

        return ucTypeTraitementOG;
    }

    /**
     * @author: sel Créé le : 8 févr. 07
     * @param context
     * @param jrn
     * @throws Exception
     */
    private CAJournal initJournal(globaz.osiris.process.CAProcessBVR context) throws Exception {
        CAJournal jrn = new CAJournal();
        if (!context.getSimulation().booleanValue()) {
            jrn.setSession(context.getSession());
            jrn.setLibelle(context.getLibelle());
            jrn.setDateValeurCG(context.getDateValeur());
            jrn.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);
            jrn.add(context.getTransaction());
            if (jrn.hasErrors()) {
                throw new Exception(getSession().getLabel("5225"));
            }
        }
        return jrn;
    }

    /**
     * @author: sel Créé le : 8 févr. 07
     * @param paiement
     * @param parser
     * @param jrn
     * @return
     * @throws Exception
     */
    private CAPaiementBVR initPaiementBvr(CAPaiementBVR paiement, IntBVRParser parser, CAJournal jrn) throws Exception {
        // Charger les attributs
        paiement.setIdJournal(jrn.getIdJournal());
        paiement.setIdOrganeExecution(getIdOrganeExecution());
        paiement.setGenreTransaction(parser.getGenreTransaction()); // 012
        paiement.setReferenceBVR(parser.getNumeroReference());
        paiement.setDateDepot(parser.getDateDepot());
        paiement.setDateInscription(parser.getDateInscription());
        paiement.setDateTraitement(parser.getDateTraitement());
        paiement.setReferenceInterne(parser.getReferenceInterne());

        if (parser.getGenreEcriture().equals(IntBVRParser.GENRE_CREDIT)) {
            paiement.setCodeDebitCredit(APIEcriture.CREDIT);
        } else {
            paiement.setCodeDebitCredit(APIEcriture.DEBIT);
        }

        if (!(paiement.getCompteAnnexe() == null) && paiement.getCompteAnnexe().isCompteAuxiliaire()) {
            CASection secAux = new CASection();
            secAux.setSession(getSession());
            secAux.setIdSection(paiement.getIdSection());
            secAux.retrieve();

            CASection secPri = new CASection();
            secPri.setSession(getSession());
            secPri.setIdSection(secAux.getIdSectionPrincipal());
            secPri.retrieve();

            paiement.setIdSection(secAux.getIdSectionPrincipal());
            paiement.setIdSectionAux(secAux.getIdSection());
            paiement.setIdCompteAnnexe(secPri.getIdCompteAnnexe());
            paiement.getIdTypeOperation();
        }

        return paiement;
    }

    /**
     * Instancie un parser en fonction du type <br>
     * Instancie un stream de lecture
     * 
     * @author: sel Créé le : 8 févr. 07
     * @param context
     * @param parser
     * @return
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private IntBVRParser initParser(CAProcessBVR context) throws JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException, Exception {
        IntBVRParser parser = null;

        // Vérifier si fin de processus désirée
        if (context.isAborted()) {
            throw new Exception();
        }

        // Instancier un parser en fonction du type
        if (getIdTypeTraitementBV().equals(APIOrganeExecution.BVR_TYPE3)) {
            parser = new CABVR3Parser();
        } else {
            getMemoryLog().logMessage("5325", getIdTypeTraitementBV(), FWMessage.FATAL, this.getClass().getName());
            throw new Exception();
        }

        // Passer les paramètres
        parser.setMemoryLog(getMemoryLog());
        parser.setEchoToConsole(context.getEchoToConsole());

        // Instancier un stream de lecture
        try {
            if (retrieveBvrFromDataBase) {
                JadeFsFacade.copyFile(
                        "jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + context.getFileName(), Jade
                                .getInstance().getHomeDir() + "work/" + context.getFileName());
            }

            parser.setInputReader(new BufferedReader(new FileReader(new File(Jade.getInstance().getHomeDir() + "work/"
                    + context.getFileName()))));
        } catch (FileNotFoundException ex) {
            throw new Exception(getSession().getLabel("5326") + " " + Jade.getInstance().getDefaultJdbcSchema() + "/"
                    + context.getFileName());
        }
        return parser;
    }

    /**
     * Instancier la classe qui permet de décomposer le numéro de référence
     * 
     * @author: sel Créé le : 8 févr. 07
     * @param context
     * @param refBVR
     * @return
     * @throws CAOrganeExecutionException
     */
    private IntReferenceBVRParser initRefBvr(globaz.osiris.process.CAProcessBVR context) throws Exception {
        IntReferenceBVRParser refBVR = null;

        // Vérifier la condition de sortie
        if (context.isAborted()) {
            throw new Exception();
        }

        // Instancier la classe qui permet de décomposer le numéro de référence
        if (JadeStringUtil.isBlank(getNomClasseParserBvr())) {
            throw new Exception(getSession().getLabel("5332"));
        } else {
            try {
                Class<?> cl = Class.forName(getNomClasseParserBvr());
                refBVR = (IntReferenceBVRParser) cl.newInstance();
                refBVR.setISession(context.getSession());
            } catch (Exception ex) {
                throw new Exception(getSession().getLabel("5333") + " : " + ex.toString());
            }
        }
        return refBVR;
    }

    public boolean isRetrieveBvrFromDataBase() {
        return retrieveBvrFromDataBase;
    }

    /**
     * Exécuter le mise en compte
     * 
     * @author: sel Créé le : 8 févr. 07
     * @param context
     * @param lStatKO
     * @param jrn
     * @throws Exception
     */
    private void miseEnCompte(globaz.osiris.process.CAProcessBVR context, long lStatKO, CAJournal jrn) throws Exception {
        if (!context.getSimulation().booleanValue() && !getMemoryLog().isOnFatalLevel()) {
            // Comptabiliser s'il n'y a aucune erreur
            if (lStatKO == 0) {
                // seb--> ne pas comptabiliser le journal automatiquement
                // laisser le choix à l'utilisateur
                // jrn.comptabiliser(context);
                jrn.setEtat(CAJournal.OUVERT);
            } else {
                jrn.setEtat(CAJournal.ERREUR);
            }

            // Mettre a jour
            jrn.update(context.getTransaction());
            if (jrn.isNew() || jrn.hasErrors()) {
                throw new Exception(getSession().getLabel("5225"));
            }
            // Vérifier les erreurs de comptabilisation
            if (jrn.getEtat().equals(CAJournal.ERREUR)) {
                getMemoryLog().logMessage("5337", null, FWMessage.ERREUR, this.getClass().getName());
            }
        }
    }

    /**
     * Setter
     */
    public void setGenre(String newGenre) {
        genre = newGenre;
    }

    public void setIdAdresseDebitTaxes(String newIdAdresseDebitTaxes) {
        idAdresseDebitTaxes = newIdAdresseDebitTaxes;
    }

    public void setIdAdressePaiement(String newIdAdressePaiement) {
        idAdressePaiement = newIdAdressePaiement;
    }

    public void setIdentifiantDTA(String newIdentifiantDTA) {
        identifiantDTA = newIdentifiantDTA;
    }

    public void setIdOrganeExecution(String newIdOrganeExecution) {
        idOrganeExecution = newIdOrganeExecution;
    }

    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
        rubrique = null;
    }

    /**
     * Date de création : (18.02.2002 10:16:36)
     * 
     * @param newIdTypeTraitementBV
     *            String
     */
    public void setIdTypeTraitementBV(String newIdTypeTraitementBV) {
        idTypeTraitementBV = newIdTypeTraitementBV;
        ucTypeTraitementBV = null;
    }

    /**
     * Date de création : (18.02.2002 10:16:50)
     * 
     * @param newIdTypeTraitementLS
     *            String
     */
    public void setIdTypeTraitementLS(String newIdTypeTraitementLS) {
        idTypeTraitementLS = newIdTypeTraitementLS;
        ucTypeTraitementLS = null;
    }

    /**
     * Date de création : (18.02.2002 10:54:35)
     * 
     * @param newMemoryLog
     *            FWMemoryLog
     */
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    public void setModeTransfert(String modeTransfert) {
        this.modeTransfert = modeTransfert;
    }

    /**
     * Date de création : (28.11.2002 11:49:32)
     * 
     * @param newNoAdherent
     *            String
     */
    public void setNoAdherent(String newNoAdherent) {
        noAdherent = newNoAdherent;
    }

    /**
     * Date de création : (21.02.2002 15:39:51)
     * 
     * @param newNoAdherentBVR
     *            String
     */
    public void setNoAdherentBVR(String newNoAdherentBVR) {
        noAdherentBVR = newNoAdherentBVR;
    }

    public void setNom(String newNom) {
        nom = newNom;
    }

    /**
     * Date de création : (21.02.2002 09:06:20)
     * 
     * @param newNomClasseParserBvr
     *            String
     */
    public void setNomClasseParserBvr(String newNomClasseParserBvr) {
        nomClasseParserBvr = newNomClasseParserBvr;
    }

    /**
     * Date de création : (28.11.2002 11:48:12)
     * 
     * @param newNomClasseParserLSV
     *            String
     */
    public void setNomClasseParserLSV(String newNomClasseParserLSV) {
        nomClasseParserLSV = newNomClasseParserLSV;
    }

    /**
     * @author: sel Créé le : 30 nov. 06
     * @param string
     */
    public void setNumeroRubrique(String string) {
        numeroRubrique = string;
    }

    public void setNumInterneLsv(String numInterneLsv) {
        this.numInterneLsv = numInterneLsv;
    }

    public void setRetrieveBvrFromDataBase(boolean retrieveBvrFromDataBase) {
        this.retrieveBvrFromDataBase = retrieveBvrFromDataBase;
    }

    /**
     * Vérifie si c'est un paiement sur une section qui a des sections auxiliares. <br>
     * Si c'est une opération auxiliaire, met le paiement en erreur.
     * 
     * @author: sel Créé le : 12 févr. 07
     * @param oper
     * @return
     * @throws Exception
     */
    // private CASectionManager checkOperationAuxiliaire(CAPaiement oper) throws
    // Exception {
    // // Test pour un paiement provenant d'opérations auxiliaires
    // CASectionManager manager = new CASectionManager();
    // manager.setSession(getSession());
    // manager.setForIdSectionPrinc(oper.getIdSection());
    // manager.setLikeIdExterne(oper.getSection().getIdExterne());
    // manager.find();
    //
    // if (0 < manager.size()) {
    // getMemoryLog().logMessage("7395", null, FWMessage.ERREUR,
    // getClass().getName());
    // oper.setMemoryLog(getMemoryLog());
    // oper.setEtat(CAOperation.ETAT_ERREUR);
    // }
    // return manager;
    // }
    /**
     * Convertir total et nombre de transactions
     * 
     * @author: sel Créé le : 9 févr. 07
     * @param lNombreTr
     * @param fTotal
     * @param parser
     */
    private void totalForFooter(long lNombreTr, FWCurrency fTotal, IntBVRParser parser) {
        // Convertir total et nombre de transactions
        FWCurrency fTotalControle = new FWCurrency(parser.getMontant());
        long lNombreTrControle = 0;
        try {
            lNombreTrControle = Long.parseLong(parser.getNombreTransactions());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Vérifier
        if (!fTotal.equals(fTotalControle)) {
            getMemoryLog().logMessage("5336", fTotalControle.toStringFormat(), FWMessage.ERREUR,
                    this.getClass().getName());
        }
        if (lNombreTr != lNombreTrControle) {
            getMemoryLog().logMessage("5335", parser.getMontant(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * @param context
     * @param fTotal
     * @param parser
     * @param refBVR
     * @param jrn
     * @throws Exception
     */
    private void traitementPlanPaiement(globaz.osiris.process.CAProcessBVR context, FWCurrency fTotal,
            IntBVRParser parser, IntReferenceBVRParser refBVR, CAJournal jrn) throws Exception {
        CAPlanRecouvrement planRecouvrement = new CAPlanRecouvrement();
        planRecouvrement.setSession(context.getSession());
        planRecouvrement.setIdPlanRecouvrement(refBVR.getIdPlanPaiement());
        planRecouvrement.retrieve();

        if (planRecouvrement.isNew() || !planRecouvrement.getIdEtat().equals(CAPlanRecouvrement.CS_ACTIF)) {

            if ((planRecouvrement != null) && !planRecouvrement.getIdCompteAnnexe().equals(refBVR.getIdCompteAnnexe())) {
                parser.getMemoryLog().logMessage(
                        "7397",
                        "plan idCA=" + planRecouvrement.getIdCompteAnnexe() + "<> refBVR idCA="
                                + refBVR.getIdCompteAnnexe(), FWMessage.ERREUR, this.getClass().getName());
            }

            // Plan en erreur
            // Message d'erreur : Le plan n'existe pas ou est inactif
            parser.getMemoryLog().logMessage("7129", getMessage() + " Plan N°" + refBVR.getIdPlanPaiement(),
                    FWMessage.ERREUR, this.getClass().getName());
            CAPaiementBVR oper = createNewPaiementBvr(context, fTotal, parser, refBVR, jrn);

            updateStatistique(oper);
        } else {
            createPaiementBvrPlan(context, fTotal, parser, jrn, planRecouvrement, refBVR);
        }
    }

    /**
     * Met à jour le total de transactions ok et en erreur
     * 
     * @param paiements
     * @param i
     */
    private void updateStatistique(CAOperation op) {
        // Statistiques
        if (op.getEtat().equals(APIOperation.ETAT_ERREUR) && op.getMemoryLog().hasErrors()) {
            totTransactionErreur++;
        } else {
            totTransactionOk++;
        }
    }

    /**
     * @return the idTypeTraitementOG
     */
    public String getIdTypeTraitementOG() {
        return idTypeTraitementOG;
    }

    /**
     * @param idTypeTraitementOG the idTypeTraitementOG to set
     */
    public void setIdTypeTraitementOG(String idTypeTraitementOG) {
        this.idTypeTraitementOG = idTypeTraitementOG;
    }
}
