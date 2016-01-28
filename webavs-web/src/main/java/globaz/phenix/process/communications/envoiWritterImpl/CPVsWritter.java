/*
 * Créé le 20 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.api.helper.ITIAdministrationHelper;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TIHistoriqueContribuableManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPVsWritter {

    private final static String BASE_FILENAME = "ENVOI_FISC.txt";
    private String date_impression = "";
    public String filename = null;
    private BProcess processAppelant = null;
    private BSession session;
    private String user = "";

    public CPVsWritter(BSession session) {
        String nom = "";
        this.session = session;
        setUser(getSession().getUserFullName());
        try {
            CPReceptionReaderManager manager = new CPReceptionReaderManager();
            manager.setSession(session);
            manager.setForIdCanton(IConstantes.CS_LOCALITE_CANTON_VALAIS);
            manager.find();
            if (manager.size() > 0) {
                CPReceptionReader reader = (CPReceptionReader) manager.getFirstEntity();
                if (reader.getNomFichier().length() > 0) {
                    nom = reader.getNomFichier();
                } else {
                    nom = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
                }
            } else {
                nom = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
            }
            nom += "-" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD) + ".txt";
            filename = nom;
        } catch (Exception e) {
            filename = CPVsWritter.BASE_FILENAME;
        }
    }

    /*
     * Création du fichier pour l'envoi des communication fiscales selon le format VS
     */
    public String create(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction) {
        CPCommunicationFiscaleAffichage entity;
        BStatement statement = null;
        PrintWriter fileOut = null;
        try {
            ITIAdministration admin = CaisseHelperFactory.getInstance().getAdministrationCaisse(getSession());
            statement = manager.cursorOpen(transaction);
            fileOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFilename(), false), "ISO8859_1"));
            getProcessAppelant().setProgressScaleValue(manager.getCount(transaction));
            int nbEnreg = 0;

            while (((entity = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew())) {
                // Zones obligatoires
                boolean casErrone = false;
                String idConjoint = "";
                CPDecision decision = null;
                String numContri = entity.getNumContri(entity.getAnneeDecision());
                if (JadeStringUtil.isBlankOrZero(numContri) || JadeStringUtil.isBlankOrZero(entity.getAnneeDecision())) {
                    casErrone = true;
                }
                if (!casErrone) {
                    // On recherche la décision qui a généré la demande
                    // Utile pour retrouver l'idConjoint
                    CPDecisionManager decM = new CPDecisionManager();
                    decM.setSession(getSession());
                    decM.setForIdAffiliation(entity.getIdAffiliation());
                    decM.setForAnneeDecision(entity.getAnneeDecision());
                    decM.setForIsActive(Boolean.TRUE);
                    decM.find();
                    if (decM.size() > 0) {
                        decision = (CPDecision) decM.getFirstEntity();
                        idConjoint = decision.getIdConjoint();
                    } else {
                        casErrone = true;
                    }
                }
                if (!casErrone) {
                    String idTiers = "";
                    nbEnreg++;

                    // init des zones
                    TITiersViewBean conjoint = null;
                    AFAffiliation affiliationConjoint = null;
                    AFAffiliation affiliation = null;
                    String ville = "";
                    String rue = "";
                    String numRue = "";
                    String npa = "";
                    String dateNaissance = "";
                    String nomPrenom = "";
                    String dateDebut = "";
                    String dateFin = "";
                    String designationRue = "";
                    String designation3 = "";
                    String designation4 = "";
                    String villeConjoint = "";
                    String rueConjoint = "";
                    String numRueConjoint = "";
                    String npaConjoint = "";
                    String designationRueConjoint = "";
                    String designation3Conjoint = "";
                    String designation4Conjoint = "";
                    String numCaisse = "000";
                    String numAgence = "000";
                    String caisseNumAffilie = "";
                    String caisseDebut = "";
                    String caisseFin = "";
                    String affMontantAvs = "";
                    // Le contribuable est toujours le mari ==> si une communication a un tiers
                    // de sexe féminin et qui a un conjoint, on inverse les rôles, le contribuable passe conjoint (même
                    // si l'homme est
                    // inconnu à la caisse)
                    if (entity.getSexe().equalsIgnoreCase(TITiersViewBean.CS_FEMME)
                            && (!JadeStringUtil.isBlankOrZero(idConjoint) || decision.getDivision2().equals(
                                    Boolean.TRUE))) {
                        idTiers = idConjoint;
                        idConjoint = entity.getIdTiers();
                        affiliationConjoint = new AFAffiliation();
                        affiliationConjoint.setSession(getSession());
                        affiliationConjoint.setAffiliationId(entity.getIdAffiliation());
                        affiliationConjoint.retrieve();
                    } else {
                        idTiers = entity.getIdTiers();
                        affiliation = new AFAffiliation();
                        affiliation.setSession(getSession());
                        affiliation.setAffiliationId(entity.getIdAffiliation());
                        affiliation.retrieve();
                        dateDebut = affiliation.getDateDebut();
                        dateFin = affiliation.getDateFin();
                    }
                    // Rechercher du tiers
                    String numAvs = "";
                    TITiersViewBean tiers = null;
                    if (!JadeStringUtil.isBlankOrZero(idTiers) && !"100".equalsIgnoreCase(idTiers)) {
                        tiers = new TITiersViewBean();
                        tiers.setSession(getSession());
                        tiers.setIdTiers(idTiers);
                        tiers.retrieve();
                        // Test validité numAVS
                        if (tiers.getNumAvsActuel() != null) {
                            numAvs = NSUtil.unFormatAVS(tiers.getNumAvsActuel());
                            if (numAvs.length() == 13) {
                                // Recherche de l'ancien n° avs
                                numAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numAvs, getSession()));
                            }
                        }
                        dateNaissance = tiers.getDateNaissance();
                        nomPrenom = tiers.getNomPrenom();
                    }

                    if (!JadeStringUtil.isBlankOrZero(idConjoint) && !"100".equalsIgnoreCase(idConjoint)) {
                        conjoint = new TITiersViewBean();
                        conjoint.setSession(getSession());
                        // conjoint.setIdTiers(cotisation.getIdConjoint());
                        conjoint.setIdTiers(idConjoint);
                        conjoint.retrieve();
                        // On va rechercher l'affiliation du conjoint si elle n'est pas renseignée (cas ou le tiers de
                        // la décision est une femme)
                        if (affiliation != null) {
                            affiliationConjoint = CPToolBox.returnAffiliation(getSession(), transaction,
                                    conjoint.getIdTiers(), entity.getAnneeDecision(), "", 1);
                        }
                    }
                    TIAdresseDataSource d = null;
                    if (tiers != null) {
                        d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005", "31.12."
                                + entity.getAnneeDecision(), true);
                        if (d == null) {
                            d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005", "01.01."
                                    + entity.getAnneeDecision(), true);
                        }
                    }
                    if (d != null) {
                        ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                        ville = JadeStringUtil.toUpperCase(ville);
                        rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                        numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                        npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
                        // titre = (String)
                        // d.getData().get(TIAdresseDataSource.ADRESSE_VAR_TITRE);
                        // designation1 = (String)
                        // d.getData().get(TIAdresseDataSource.ADRESSE_VAR_T1);
                        // designation2 = (String)
                        // d.getData().get(TIAdresseDataSource.ADRESSE_VAR_T2);
                        designation3 = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T3);
                        designation4 = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T4);
                        designationRue = rue + " " + numRue;
                        if (JadeStringUtil.isEmpty(designation4)) {
                            designation4 = designationRue;
                            designationRue = "";
                        }
                        if (JadeStringUtil.isEmpty(designation3)) {
                            designation3 = designation4;
                            designation4 = designationRue;
                            designationRue = "";
                        }
                    }
                    if ((conjoint != null) && (!conjoint.isNew())) {
                        TIAdresseDataSource dc = conjoint.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                "519005", "31.12." + entity.getAnneeDecision(), true);
                        if (dc == null) {
                            dc = conjoint.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                                    "01.01." + entity.getAnneeDecision(), true);
                        }
                        if (dc != null) {
                            villeConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                            villeConjoint = JadeStringUtil.toUpperCase(villeConjoint);
                            rueConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                            numRueConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                            npaConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
                            designationRueConjoint = rueConjoint + " " + numRueConjoint;
                            // titreConjoint = (String)
                            // d.getData().get(TIAdresseDataSource.ADRESSE_VAR_TITRE);
                            // designation1Conjoint = (String)
                            // d.getData().get(TIAdresseDataSource.ADRESSE_VAR_T1);
                            // designation2Conjoint = (String)
                            // d.getData().get(TIAdresseDataSource.ADRESSE_VAR_T2);
                            designation3Conjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T3);
                            designation4Conjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T4);
                            if (JadeStringUtil.isEmpty(designation3Conjoint)) {
                                designation3Conjoint = designation4Conjoint;
                                designation4Conjoint = rueConjoint + " " + numRueConjoint;
                                designationRueConjoint = "";
                            }
                            if (JadeStringUtil.isEmpty(designation4Conjoint)) {
                                designation4Conjoint = designationRueConjoint;
                                designationRueConjoint = "";
                            }
                        }
                    }
                    /**************************************** DEB PAGE 1 CoM. FiSc. **********************************************************/
                    fileOut.write(CPToolBox.formattedNbre(CPToolBox.unFormat(numContri), 11));
                    fileOut.write(CPToolBox.formattedString(entity.getAnneeDecision(), 4, '0'));
                    fileOut.write(CPToolBox.formattedString(
                            JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_YYYYMMDD), 8, '0'));
                    fileOut.write("00000000"); // date communication
                    fileOut.write("00000000"); // Date taxation
                    fileOut.write(" "); // Code taxation 1
                    fileOut.write(" "); // Code taxation 2
                    fileOut.write("  "); // Type de taxation
                    fileOut.write(CPToolBox.formattedString(CPToolBox.unFormat(entity.getNumAffilie()), 12, ' '));
                    if (numAvs.length() == 13) {
                        // NSS
                        fileOut.write(numAvs.substring(2));
                    } else {
                        // NAVS
                        fileOut.write(CPToolBox.formattedString(numAvs, 11, '0'));
                    }
                    fileOut.write(CPToolBox.formattedString(
                            JACalendar.format(dateNaissance, JACalendar.FORMAT_YYYYMMDD), 8, '0'));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(dateDebut, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(dateFin, JACalendar.FORMAT_YYYYMMDD), 8,
                            '0'));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(nomPrenom), 50, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(nomPrenom), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation3), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation4), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designationRue), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(npa + " " + ville, 40, ' '));
                    // Recherche caisse avs
                    AFSuiviCaisseAffiliation suivi = null;
                    if (affiliation != null) {
                        suivi = AFAffiliationUtil.getCaissseAVS(affiliation, entity.getAnneeDecision());
                        if ((suivi != null) && !suivi.isNew()) {
                            caisseNumAffilie = suivi.getNumeroAffileCaisse();
                            caisseDebut = suivi.getDateDebut();
                            caisseFin = suivi.getDateFin();
                            admin = new ITIAdministrationHelper(suivi.getAdministration().toValueObject());
                        }
                    }
                    if ((admin != null) && !admin.isNew()) {
                        int sep = admin.getCodeAdministration().indexOf('.');
                        if (sep != -1) {
                            numCaisse = admin.getCodeAdministration().substring(0, sep);
                            numAgence = admin.getCodeAdministration().substring(sep + 1);
                        } else {
                            numCaisse = admin.getCodeAdministration();
                        }
                    }
                    fileOut.write(CPToolBox.formattedNbre(numCaisse, 3) + CPToolBox.formattedNbre(numAgence, 3));
                    fileOut.write(CPToolBox.formattedString(CPToolBox.unFormat(entity.getNumAffilie()), 12, ' '));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseDebut, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseFin, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(caisseNumAffilie, 20, ' '));
                    fileOut.write(CPToolBox.formattedString(affMontantAvs, 12, ' '));
                    if (tiers != null) {
                        fileOut.write(getEtatCivilCode(tiers.getEtatCivil()));
                        fileOut.write(getSexeAffilieCode(tiers.getSexe()));
                    } else if (conjoint != null) {
                        fileOut.write(getEtatCivilCode(conjoint.getEtatCivil()));
                        fileOut.write(getSexeAffilieCode(conjoint.getSexe()));
                    } else {
                        fileOut.write(" ");
                        fileOut.write(" ");
                    }
                    /**************************************** FIN PAGE 2 CoM. FiSc. **********************************************************/
                    numCaisse = "";
                    numAgence = "";
                    caisseNumAffilie = "";
                    caisseDebut = "";
                    caisseFin = "";
                    String cjtNumAffilie = "";
                    String cjtNumAvs = "";
                    String cjtDateNaissance = "";
                    String cjtAffiliationDebut = "";
                    String cjtAffiliationFin = "";
                    String cjtNomPrenom = "";
                    String cjtMontantAvs = "";
                    if (affiliationConjoint != null) {
                        cjtNumAffilie = CPToolBox.unFormat(affiliationConjoint.getAffilieNumero());
                        cjtDateNaissance = JACalendar.format(conjoint.getDateNaissance(), JACalendar.FORMAT_YYYYMMDD);
                        cjtAffiliationDebut = JACalendar.format(affiliationConjoint.getDateDebut(),
                                JACalendar.FORMAT_YYYYMMDD);
                        cjtAffiliationFin = JACalendar.format(affiliationConjoint.getDateFin(),
                                JACalendar.FORMAT_YYYYMMDD);
                        // Recherche caisse avs
                        suivi = AFAffiliationUtil.getCaissseAVS(affiliationConjoint, entity.getAnneeDecision());
                        if ((suivi != null) && !suivi.isNew()) {
                            caisseNumAffilie = suivi.getNumeroAffileCaisse();
                            caisseDebut = suivi.getDateDebut();
                            caisseFin = suivi.getDateFin();
                            admin = new ITIAdministrationHelper(suivi.getAdministration().toValueObject());
                        }
                        if ((admin != null) && !admin.isNew()) {
                            int sep = admin.getCodeAdministration().indexOf('.');
                            if (sep != -1) {
                                numCaisse = admin.getCodeAdministration().substring(0, sep);
                                numAgence = admin.getCodeAdministration().substring(sep + 1);
                            } else {
                                numCaisse = admin.getCodeAdministration();
                            }
                        }
                        // Montant
                        // Recherche décision active pour l'année pour avoir le
                        // montant des cotisations
                        // cotisation=
                        // CPToolBox.rechercheCotisationActive(session,
                        // transaction, affiliationConjoint.getAffiliationId(),
                        // entity.getAnneeDecision(),
                        // CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                        // if(cotisation!=null){
                        // cjtMontantAvs = cotisation.getMontantAnnuel();
                        // if(!JadeStringUtil.isEmpty(cjtMontantAvs))
                        // cjtMontantAvs =
                        // JANumberFormatter.fmt(JANumberFormatter.deQuote(cjtMontantAvs),false,true,true,2);
                        // }
                    }
                    if (conjoint != null) {
                        cjtNomPrenom = conjoint.getNomPrenom();
                        if (conjoint.getNumAvsActuel() != null) {
                            cjtNumAvs = NSUtil.unFormatAVS(conjoint.getNumAvsActuel());
                            if (cjtNumAvs.length() == 13) {
                                // Recherche de l'ancien n° avs
                                cjtNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(cjtNumAvs, getSession()));
                            }
                        }
                    }
                    fileOut.write(CPToolBox.formattedString(cjtNumAffilie, 12, ' '));
                    if (cjtNumAvs.length() == 13) {
                        // NNS
                        fileOut.write(cjtNumAvs.substring(2));
                    } else {
                        // NAVS
                        fileOut.write(CPToolBox.formattedString(cjtNumAvs, 11, '0'));
                    }
                    fileOut.write(CPToolBox.formattedString(cjtDateNaissance, 8, '0'));
                    fileOut.write(CPToolBox.formattedString(cjtAffiliationDebut, 8, '0'));
                    fileOut.write(CPToolBox.formattedString(cjtAffiliationFin, 8, '0'));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(cjtNomPrenom), 50, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(cjtNomPrenom), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation3Conjoint), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation4Conjoint), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designationRueConjoint), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(npaConjoint + " " + villeConjoint, 40, ' '));
                    fileOut.write(CPToolBox.formattedNbre(numCaisse, 3) + CPToolBox.formattedNbre(numAgence, 3));
                    fileOut.write(CPToolBox.formattedString(cjtNumAffilie, 12, ' '));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseDebut, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseFin, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(caisseNumAffilie, 20, ' '));
                    fileOut.write(CPToolBox.formattedString(cjtMontantAvs, 12, ' '));
                    fileOut.write(CPToolBox.formattedString("", 632, ' '));
                    fileOut.write("\n");

                    // Mise à jour de la date d'envoi
                    CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                    comFis.setSession(getSession());
                    comFis.setIdCommunication(entity.getIdCommunication());
                    comFis.retrieve(transaction);
                    if (!comFis.isNew()) {
                        if (manager.getDemandeAnnulee().equals(Boolean.FALSE)) {
                            comFis.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
                        } else {
                            comFis.setDateEnvoiAnnulation(JACalendar.todayJJsMMsAAAA());
                        }
                        comFis.update(transaction);
                    }
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                    getProcessAppelant().incProgressCounter();
                }
            }
            // Enregistrement de fin
            fileOut.write(CPToolBox.formattedString("", 11, '9'));
            fileOut.write(CPToolBox.formattedString(Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())),
                    4, '0'));
            fileOut.write(CPToolBox.formattedString("", 24, '0'));
            fileOut.write(CPToolBox.formattedString("", 4, ' '));
            fileOut.write("000000001   ");
            fileOut.write(CPToolBox.formattedNbre(Integer.toString(nbEnreg), 11));
            fileOut.write(CPToolBox.formattedString("", 8, '0'));
            fileOut.write(CPToolBox.formattedString(
                    JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_YYYYMMDD), 8, '0'));
            fileOut.write(CPToolBox.formattedString("", 8, '0'));
            fileOut.write(CPToolBox.formattedString("", 268, ' '));
            fileOut.write(CPToolBox.formattedString("", 16, '0'));
            fileOut.write(CPToolBox.formattedString("", 349, ' '));
            fileOut.write(CPToolBox.formattedString("", 16, '0'));
            fileOut.write(CPToolBox.formattedString("", 664, ' '));
            fileOut.write("\n");
            fileOut.close();
        } catch (IOException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            try {
                if (statement != null) {
                    manager.cursorClose(statement);
                }
            } catch (Exception e1) {
                JadeLogger.error(this, e1);
            }

        }
        return getFilename();
    }

    /*
     * Création du fichier pour le type fchier central.
     */
    public String createFichierCentral(AFAffiliationManager manager, String annee, BTransaction transaction) {
        AFAffiliation entity;
        BStatement statement = null;
        PrintWriter fileOut = null;
        try {
            ITIAdministration admin = CaisseHelperFactory.getInstance().getAdministrationCaisse(getSession());
            statement = manager.cursorOpen(transaction);
            fileOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFilename(), false), "ISO8859_1"));
            getProcessAppelant().setProgressScaleValue(manager.getCount(transaction));
            int nbEnreg = 0;
            while (((entity = (AFAffiliation) manager.cursorReadNext(statement)) != null) && (!entity.isNew())) {
                // Zones obligatoires
                boolean casErrone = false;
                String numContri = "";
                TIHistoriqueContribuable hist = new TIHistoriqueContribuable();
                hist.setSession(getSession());
                numContri = hist.findPrevKnownNumContribuable(entity.getIdTiers(), "31.12." + annee);
                if (JadeStringUtil.isIntegerEmpty(numContri)) {
                    numContri = hist.findNextKnownNumContribuable(entity.getIdTiers(), "31.12." + annee);
                    if (JadeStringUtil.isIntegerEmpty(numContri)) {
                        numContri = "";
                    }
                }

                if (JadeStringUtil.isBlankOrZero(numContri) || JadeStringUtil.isBlankOrZero(annee)) {
                    casErrone = true;
                }
                TITiersViewBean tiers = new TITiersViewBean();
                if (!casErrone) {
                    // On va rechercher le tiers
                    tiers.setSession(getSession());
                    tiers.setIdTiers(entity.getIdTiers());
                    tiers.retrieve();
                    // Si femme et marié (ne pas prendre)
                    // Comme il n'y a pas de lien entre tiers pour le fichier central, il faut vérifier si il y a un
                    // tiers de sexe masculin avec
                    // le même numéro de contribuable
                    if (tiers.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME)) {
                        // Recherche si tiers avec même numéro de contribuale et affiliation active
                        TIHistoriqueContribuableManager hMng = new TIHistoriqueContribuableManager();
                        hMng.setSession(getSession());
                        hMng.setForNumContribuable(numContri);
                        hMng.find();
                        for (int i = 0; (i < hMng.size()) && !casErrone; i++) {
                            TIHistoriqueContribuable hCjt = (TIHistoriqueContribuable) hMng.getEntity(i);
                            if (!hCjt.getIdTiers().equalsIgnoreCase(entity.getIdTiers())) {
                                // Recherche affiliation
                                AFAffiliationManager affMng = new AFAffiliationManager();
                                affMng.setSession(getSession());
                                affMng.setForIdTiers(hCjt.getIdTiers());
                                affMng.setForAnnee(annee);
                                if (affMng.getCount() > 0) {
                                    casErrone = true;
                                }
                            }
                        }
                    }
                }
                if (!casErrone) {
                    nbEnreg++;
                    // init des zones
                    TITiersViewBean conjoint = null;
                    AFAffiliation affiliationConjoint = null;
                    String ville = "";
                    String rue = "";
                    String numRue = "";
                    String npa = "";
                    String designationRue = "";
                    String designation3 = "";
                    String designation4 = "";
                    String villeConjoint = "";
                    String rueConjoint = "";
                    String numRueConjoint = "";
                    String npaConjoint = "";
                    String designationRueConjoint = "";
                    String designation3Conjoint = "";
                    String designation4Conjoint = "";
                    String numCaisse = "000";
                    String numAgence = "000";
                    String caisseNumAffilie = "";
                    String caisseDebut = "";
                    String caisseFin = "";
                    String affMontantAvs = "";
                    // CPDecisionAffiliationCotisation cotisation = null
                    // Test validité numAVS
                    String numAvs = "";
                    if (tiers.getNumAvsActuel() != null) {
                        numAvs = NSUtil.unFormatAVS(tiers.getNumAvsActuel());
                        if (numAvs.length() == 13) {
                            // Recherche de l'ancien n° avs
                            numAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numAvs, getSession()));
                        }
                    }
                    // Recherche conjoint - Prendre même numéro de contribuable car le lien entre conjoint n'existe pas
                    // dans tiers
                    if (tiers.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME)) {
                        TIHistoriqueContribuableManager hMng = new TIHistoriqueContribuableManager();
                        hMng.setSession(getSession());
                        hMng.setForNumContribuable(numContri);
                        hMng.find();
                        for (int i = 0; (i < hMng.size()) && !casErrone; i++) {
                            TIHistoriqueContribuable hCjt = (TIHistoriqueContribuable) hMng.getEntity(i);
                            if (!hCjt.getIdTiers().equalsIgnoreCase(entity.getIdTiers())) {
                                conjoint = new TITiersViewBean();
                                conjoint.setSession(getSession());
                                conjoint.setIdTiers(hCjt.getIdTiers());
                                conjoint.retrieve();
                                // On va rechercher l'affiliation du conjoint
                                if (!conjoint.getNumAvsActuel().equalsIgnoreCase(tiers.getNumAvsActuel())) {
                                    affiliationConjoint = CPToolBox.returnAffiliation(getSession(), transaction,
                                            conjoint.getIdTiers(), annee, "", 1);
                                }
                            }
                        }
                    }
                    TIAdresseDataSource d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            "519005", "31.12." + annee, true);
                    if (d == null) {
                        d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005", "01.01."
                                + annee, true);
                    }
                    if (d != null) {
                        ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                        ville = JadeStringUtil.toUpperCase(ville);
                        rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                        numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                        npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
                        designation3 = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T3);
                        designation4 = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T4);
                        designationRue = rue + " " + numRue;
                        if (JadeStringUtil.isEmpty(designation4)) {
                            designation4 = designationRue;
                            designationRue = "";
                        }
                        if (JadeStringUtil.isEmpty(designation3)) {
                            designation3 = designation4;
                            designation4 = designationRue;
                            designationRue = "";
                        }
                    }
                    if ((conjoint != null) && (!conjoint.isNew())
                            && !conjoint.getNumAvsActuel().equalsIgnoreCase(tiers.getNumAvsActuel())) {
                        TIAdresseDataSource dc = conjoint.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                "519005", "31.12." + annee, true);
                        if (dc == null) {
                            dc = conjoint.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                                    "01.01." + annee, true);
                        }
                        if (dc != null) {
                            villeConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                            villeConjoint = JadeStringUtil.toUpperCase(villeConjoint);
                            rueConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                            numRueConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                            npaConjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
                            designationRueConjoint = rueConjoint + " " + numRueConjoint;
                            designation3Conjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T3);
                            designation4Conjoint = dc.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_T4);
                            if (JadeStringUtil.isEmpty(designation3Conjoint)) {
                                designation3Conjoint = designation4Conjoint;
                                designation4Conjoint = rueConjoint + " " + numRueConjoint;
                                designationRueConjoint = "";
                            }
                            if (JadeStringUtil.isEmpty(designation4Conjoint)) {
                                designation4Conjoint = designationRueConjoint;
                                designationRueConjoint = "";
                            }
                        }
                    }
                    /**************************************** DEB PAGE 1 CoM. FiSc. **********************************************************/
                    fileOut.write(CPToolBox.formattedNbre(CPToolBox.unFormat(numContri), 11));
                    fileOut.write(CPToolBox.formattedString(annee, 4, '0'));
                    fileOut.write(CPToolBox.formattedString(
                            JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_YYYYMMDD), 8, '0'));
                    fileOut.write(CPToolBox.formattedString("", 8, '0'));
                    fileOut.write(CPToolBox.formattedString("", 8, '0'));
                    fileOut.write(CPToolBox.formattedString("", 1, ' '));
                    fileOut.write(CPToolBox.formattedString("", 1, ' '));
                    fileOut.write(CPToolBox.formattedString("", 2, ' '));
                    fileOut.write(CPToolBox.formattedString(CPToolBox.unFormat(entity.getAffilieNumero()), 12, ' '));
                    if (numAvs.length() == 13) {
                        // NSS
                        fileOut.write(numAvs.substring(2));
                    } else {
                        // NAVS
                        fileOut.write(CPToolBox.formattedString(numAvs, 11, '0'));
                    }
                    fileOut.write(CPToolBox.formattedString(
                            JACalendar.format(tiers.getDateNaissance(), JACalendar.FORMAT_YYYYMMDD), 8, '0'));
                    fileOut.write(CPToolBox.formattedString(
                            JACalendar.format(entity.getDateDebut(), JACalendar.FORMAT_YYYYMMDD), 8, '0'));
                    fileOut.write(CPToolBox.formattedString(
                            JACalendar.format(entity.getDateFin(), JACalendar.FORMAT_YYYYMMDD), 8, '0'));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(tiers.getNomPrenom()), 50, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(tiers.getNomPrenom()), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation3), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation4), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designationRue), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(npa + " " + ville, 40, ' '));
                    // Recherche caisse avs
                    AFSuiviCaisseAffiliation suivi = AFAffiliationUtil.getCaissseAVS(entity, annee);
                    if ((suivi != null) && !suivi.isNew()) {
                        caisseNumAffilie = suivi.getNumeroAffileCaisse();
                        caisseDebut = suivi.getDateDebut();
                        caisseFin = suivi.getDateFin();
                        admin = new ITIAdministrationHelper(suivi.getAdministration().toValueObject());
                    }
                    if ((admin != null) && !admin.isNew()) {
                        int sep = admin.getCodeAdministration().indexOf('.');
                        if (sep != -1) {
                            numCaisse = admin.getCodeAdministration().substring(0, sep);
                            numAgence = admin.getCodeAdministration().substring(sep + 1);
                        } else {
                            numCaisse = admin.getCodeAdministration();
                        }
                    }
                    fileOut.write(CPToolBox.formattedNbre(numCaisse, 3) + CPToolBox.formattedNbre(numAgence, 3));
                    fileOut.write(CPToolBox.formattedString(CPToolBox.unFormat(entity.getAffilieNumero()), 12, ' '));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseDebut, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseFin, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(caisseNumAffilie, 20, ' '));
                    fileOut.write(CPToolBox.formattedString(affMontantAvs, 12, ' '));
                    fileOut.write(getEtatCivilCode(tiers.getEtatCivil()));
                    fileOut.write(getSexeAffilieCode(tiers.getSexe()));
                    /**************************************** FIN PAGE 2 CoM. FiSc. **********************************************************/
                    numCaisse = "";
                    numAgence = "";
                    caisseNumAffilie = "";
                    caisseDebut = "";
                    caisseFin = "";
                    String cjtNumAffilie = "";
                    String cjtNumAvs = "";
                    String cjtDateNaissance = "";
                    String cjtAffiliationDebut = "";
                    String cjtAffiliationFin = "";
                    String cjtNomPrenom = "";
                    String cjtMontantAvs = "";
                    if (affiliationConjoint != null) {
                        cjtNumAffilie = CPToolBox.unFormat(affiliationConjoint.getAffilieNumero());
                        cjtDateNaissance = JACalendar.format(conjoint.getDateNaissance(), JACalendar.FORMAT_YYYYMMDD);
                        cjtAffiliationDebut = JACalendar.format(affiliationConjoint.getDateDebut(),
                                JACalendar.FORMAT_YYYYMMDD);
                        cjtAffiliationFin = JACalendar.format(affiliationConjoint.getDateFin(),
                                JACalendar.FORMAT_YYYYMMDD);
                        // Recherche caisse avs
                        suivi = AFAffiliationUtil.getCaissseAVS(affiliationConjoint, annee);
                        if ((suivi != null) && !suivi.isNew()) {
                            caisseNumAffilie = suivi.getNumeroAffileCaisse();
                            caisseDebut = suivi.getDateDebut();
                            caisseFin = suivi.getDateFin();
                            admin = new ITIAdministrationHelper(suivi.getAdministration().toValueObject());
                        }
                        if ((admin != null) && !admin.isNew()) {
                            int sep = admin.getCodeAdministration().indexOf('.');
                            if (sep != -1) {
                                numCaisse = admin.getCodeAdministration().substring(0, sep);
                                numAgence = admin.getCodeAdministration().substring(sep + 1);
                            } else {
                                numCaisse = admin.getCodeAdministration();
                            }
                        }
                    }
                    if ((conjoint != null) && (!conjoint.getNumAvsActuel().equalsIgnoreCase(tiers.getNumAvsActuel()))) {
                        cjtNomPrenom = conjoint.getNomPrenom();
                        if (conjoint.getNumAvsActuel() != null) {
                            cjtNumAvs = NSUtil.unFormatAVS(conjoint.getNumAvsActuel());
                            if (cjtNumAvs.length() == 13) {
                                // Recherche de l'ancien n° avs
                                cjtNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(cjtNumAvs, getSession()));
                            }
                        }
                    }
                    fileOut.write(CPToolBox.formattedString(cjtNumAffilie, 12, ' '));
                    if (cjtNumAvs.length() == 13) {
                        fileOut.write(cjtNumAvs.substring(2));
                    } else {
                        fileOut.write(CPToolBox.formattedString(cjtNumAvs, 11, '0'));
                    }
                    fileOut.write(CPToolBox.formattedString(cjtDateNaissance, 8, '0'));
                    fileOut.write(CPToolBox.formattedString(cjtAffiliationDebut, 8, '0'));
                    fileOut.write(CPToolBox.formattedString(cjtAffiliationFin, 8, '0'));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(cjtNomPrenom), 50, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(cjtNomPrenom), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation3Conjoint), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designation4Conjoint), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(JadeStringUtil.toUpperCase(designationRueConjoint), 40, ' '));
                    fileOut.write(CPToolBox.formattedString(npaConjoint + " " + villeConjoint, 40, ' '));
                    fileOut.write(CPToolBox.formattedNbre(numCaisse, 3) + CPToolBox.formattedNbre(numAgence, 3));
                    fileOut.write(CPToolBox.formattedString(cjtNumAffilie, 12, ' '));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseDebut, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(JACalendar.format(caisseFin, JACalendar.FORMAT_YYYYMMDD),
                            8, '0'));
                    fileOut.write(CPToolBox.formattedString(caisseNumAffilie, 20, ' '));
                    fileOut.write(CPToolBox.formattedString(cjtMontantAvs, 12, ' '));
                    fileOut.write(CPToolBox.formattedString("", 632, ' '));
                    fileOut.write("\n");

                    getProcessAppelant().incProgressCounter();
                }
            }
            // Enregistrement de fin
            fileOut.write(CPToolBox.formattedString("", 11, '9'));
            fileOut.write(CPToolBox.formattedString(Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())),
                    4, '0'));
            fileOut.write(CPToolBox.formattedString("", 24, '0'));
            fileOut.write(CPToolBox.formattedString("", 4, ' '));
            fileOut.write("000000001   ");
            fileOut.write(CPToolBox.formattedNbre(Integer.toString(nbEnreg), 11));
            fileOut.write(CPToolBox.formattedString("", 8, '0'));
            fileOut.write(CPToolBox.formattedString(
                    JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_YYYYMMDD), 8, '0'));
            fileOut.write(CPToolBox.formattedString("", 8, '0'));
            fileOut.write(CPToolBox.formattedString("", 268, ' '));
            fileOut.write(CPToolBox.formattedString("", 16, '0'));
            fileOut.write(CPToolBox.formattedString("", 349, ' '));
            fileOut.write(CPToolBox.formattedString("", 16, '0'));
            fileOut.write(CPToolBox.formattedString("", 664, ' '));
            fileOut.write("\n");
            fileOut.close();
        } catch (IOException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            try {
                if (statement != null) {
                    manager.cursorClose(statement);
                }
            } catch (Exception e1) {
                JadeLogger.error(this, e1);
            }

        }
        return getFilename();
    }

    public String getCodeIfd(String idIfd) {
        String numIfd = null;
        String code = null;
        try {
            CPPeriodeFiscale periode = new CPPeriodeFiscale();
            periode.setSession(getSession());
            periode.setIdIfd(idIfd);
            periode.retrieve();
            numIfd = periode.getNumIfd();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if ((numIfd != null) || !JadeStringUtil.isEmpty(numIfd)) {
            code = "S" + numIfd;
        } else {
            code = "";
        }

        return code;
    }

    public String getDate_impression() {
        return date_impression;
    }

    private String getEtatCivilCode(String etatCivil) {
        // 1 = Célibataire
        if (etatCivil.equalsIgnoreCase(TITiersViewBean.CS_CELIBATAIRE)) {
            return "1";
        } else if (etatCivil.equalsIgnoreCase(TITiersViewBean.CS_MARIE)) {
            return "2";
        } else if (etatCivil.equalsIgnoreCase(TITiersViewBean.CS_VEUF)) {
            return "3";
        } else if (etatCivil.equalsIgnoreCase(TITiersViewBean.CS_DIVORCE)) {
            return "4";
        } else if ((etatCivil.equalsIgnoreCase(TITiersViewBean.CS_SEPARE) || (etatCivil
                .equalsIgnoreCase(TITiersViewBean.CS_SEPARE_DE_FAIT)))) {
            return "5";
        } else {
            return " ";
        }
    }

    public String getFilename() {
        return filename;
    }

    public String getNumIfd(String idIfd) {
        String numIfd = null;
        try {
            CPPeriodeFiscale periode = new CPPeriodeFiscale();
            periode.setSession(getSession());
            periode.setIdIfd(idIfd);
            periode.retrieve();
            numIfd = periode.getNumIfd();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return numIfd;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    private String getSexeAffilieCode(String sexe) {
        if (sexe.equalsIgnoreCase(TITiersViewBean.CS_HOMME)) {
            return "1";
        } else if (sexe.equalsIgnoreCase(TITiersViewBean.CS_FEMME)) {
            return "2";
        } else {
            return " ";
        }
    }

    public String getUser() {
        return user;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUser(String string) {
        user = string;
    }
}
