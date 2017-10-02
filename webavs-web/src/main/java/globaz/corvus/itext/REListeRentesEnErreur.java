package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRenteVieillesseJoinTiersManager;
import globaz.corvus.db.rentesaccordees.REListeRentesActivesJoinMembresFamilleManager;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteActiveJoinMembresFamille;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REGenererListesVerificationProcess;
import globaz.corvus.utils.retenues.RERetenuesUtil;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFEnfantManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFMembreFamilleListeRecapCorvus;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.lowagie.text.DocumentException;

/**
 * Génère un tableau avec les rentes en erreur pour la liste de vérification du paiement mensuel
 */
public class REListeRentesEnErreur extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class InfoBeneficiaireContainer {
        public String idTiersBeneficiaire = "";
        public Boolean isTiersInactif = null;
        private Map<String, RelationInfoContainer> mapRelationsInfoContainer = null;
        public String raCodeNationalite = "";
        public String raCodePrest = "";
        public String raCS1 = "";
        public String raCS2 = "";
        public String raCS3 = "";
        public String raCS4 = "";
        public String raCS5 = "";
        public String raCsEtat = "";
        public String raCsSexe = "";
        public String raDateDeces = "";
        public String raDateNaissance = "";
        public String raDegreInv = "";
        public String raFractionRente = "";
        public String raIdPrstAccordee = "";
        public String raIdTiers = "";
        public String raIdTiersBaseCalcul = "";

        public String raNom = "";
        public String raNss = "";
        public String raPrenom = "";
        public String idInfoComptable = "";

        public void addRelation(final String csDomaine, final String idConjoint, final RelationContainer relation) {

            if (mapRelationsInfoContainer == null) {
                mapRelationsInfoContainer = new HashMap<String, REListeRentesEnErreur.RelationInfoContainer>();
            }

            if (mapRelationsInfoContainer.containsKey(csDomaine + "-" + idConjoint)) {
                RelationInfoContainer ric = mapRelationsInfoContainer.get(csDomaine + "-" + idConjoint);
                ric.addRelation(relation);
            } else {
                RelationInfoContainer ric = new RelationInfoContainer();
                ric.csDomaine = csDomaine;
                ric.idConjont = idConjoint;
                ric.addRelation(relation);
                mapRelationsInfoContainer.put(csDomaine + "-" + idConjoint, ric);
            }
        }

        public boolean contientCodeCasSpecial(final String codeCasSpecial) {
            return codeCasSpecial.equals(raCS1) || codeCasSpecial.equals(raCS2) || codeCasSpecial.equals(raCS3)
                    || codeCasSpecial.equals(raCS4) || codeCasSpecial.equals(raCS5);
        }

        public InfoBeneficiaireContainer remplirWrapper(final RERenteActiveJoinMembresFamille raMF) {
            idTiersBeneficiaire = raMF.getIdTiers();

            RelationContainer rc = new RelationContainer();
            rc.csTypeRelation = raMF.getCsTypeRelation();
            rc.dateDebut = raMF.getDateDebutRelation();
            rc.dateFin = raMF.getDateFinRelation();
            rc.idMFBeneficiaire = raMF.getIdMFBeneficiaire();
            rc.idConjoint = raMF.getIdConjoint();
            rc.csDomaine = raMF.getCsDomaine();
            if (raMF.getIdMFBeneficiaire().equals(raMF.getIdMFConjoint1())) {
                rc.idMFConjoint = raMF.getIdMFConjoint2();
            } else {
                rc.idMFConjoint = raMF.getIdMFConjoint1();
            }
            addRelation(raMF.getCsDomaine(), raMF.getIdConjoint(), rc);
            raCodeNationalite = raMF.getCodeNationalite();
            raCodePrest = raMF.getCodePrestation();
            raCS1 = raMF.getCs1();
            raCS2 = raMF.getCs2();
            raCS3 = raMF.getCs3();
            raCS4 = raMF.getCs4();
            raCS5 = raMF.getCs5();
            raCsEtat = raMF.getCsEtatPrestationAccordee();
            raCsSexe = raMF.getCsSexe();
            raDateNaissance = raMF.getDateNaissance();
            raDegreInv = raMF.getDegreInvalidite();
            raFractionRente = raMF.getFractionRente();
            raIdTiers = raMF.getIdTiers();
            raNom = raMF.getNom();
            raPrenom = raMF.getPrenom();
            raNss = raMF.getNss();
            raIdPrstAccordee = raMF.getIdPrestationAccordee();
            raDateDeces = raMF.getDateDeces();
            raIdTiersBaseCalcul = raMF.getIdTiersBaseCalcul();
            isTiersInactif = raMF.getIsTiersInactif();
            idInfoComptable = raMF.getIdInformationComptable();

            return this;
        }
    }

    class RelationContainer {
        public String csDomaine = "";
        public String csTypeRelation = "";
        public String dateDebut = "";
        public String dateFin = "";
        public String idConjoint = "";
        public String idMFBeneficiaire = "";
        public String idMFConjoint = "";

    }

    class RelationInfoContainer {
        public String csDomaine = null;
        public String idConjont = null;
        public List<RelationContainer> relations = null;

        public void addRelation(final RelationContainer rel) {
            if (relations == null) {
                relations = new ArrayList<REListeRentesEnErreur.RelationContainer>();
            }
            relations.add(rel);
        }
    }

    private String annee = "";
    private final Calendar cal = Calendar.getInstance();
    public int count = 0;
    private String forMoisAnnee = "";
    private InfoBeneficiaireContainer infoBC = null;
    private String mois = "";
    private int nbEntity = 0;
    private int nbEntityCount = 0;
    private int nbErreur = 0;

    public REListeRentesEnErreur() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, null, "", "liste des rentes en erreur", new REListeRentesActivesJoinMembresFamilleManager(),
                REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    public REListeRentesEnErreur(final BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, null, "", session.getLabel("LISTE_DES_RENTES_EN_ERREUR"),
                new REListeRentesActivesJoinMembresFamilleManager(), REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    /**
     * Transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {

        try {
            // Création du manager
            REListeRentesActivesJoinMembresFamilleManager manager = (REListeRentesActivesJoinMembresFamilleManager) _getManager();
            manager.setSession(getSession());
            manager.setForDatePaiement(getForMoisAnnee());
            int count = manager.getCount();

            if (count == 0) {
                nbEntity = 0;
                abort();
                getMemoryLog().logMessage(getSession().getLabel("LISTE_ERR_ERREUR"), FWServlet.ERROR,
                        getSession().getLabel("LISTE_ERR_TITRE"));
            } else {
                nbEntity = count;
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }

        getDocumentInfo().setDocumentProperty(REGenererListesVerificationProcess.PROPERTY_DOCUMENT_ORDER,
                REGenererListesVerificationProcess.LISTE_RENTES_ERREUR_ORDER);

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_RENTES_EN_ERREUR);

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("LISTE_DES_RENTES_EN_ERREUR") + " " + getForMoisAnnee()
                + getSession().getLabel("LISTE_DES_RENTES_EN_ERREUR_02"));

        mois = getForMoisAnnee().substring(0, 2);
        annee = getForMoisAnnee().substring(3, 7);
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    private void addCell(final InfoBeneficiaireContainer infoBC, final String messageErreur) throws FWIException,
            DocumentException {
        nbErreur++;
        if (!JadeStringUtil.isEmpty(infoBC.raIdTiers)) {
            _addCell(infoBC.raNss + " / " + infoBC.raNom + " " + infoBC.raPrenom + " / "
                    + PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(infoBC.raDateNaissance) + " / "
                    + getLibelleCourtSexe(infoBC.raCsSexe) + " / " + getLibellePays(infoBC.raCodeNationalite));
        } else {
            _addCell("Tiers non-défini : idTiers = " + infoBC.raIdTiers);
        }
        _addCell(infoBC.raIdPrstAccordee);
        _addCell(messageErreur);

        this._addDataTableRow();
    }

    @Override
    protected void addRow(final BEntity entity) throws FWIException {

        count++;
        nbEntityCount++;

        RERenteActiveJoinMembresFamille raMF = (RERenteActiveJoinMembresFamille) entity;

        try {
            if ((infoBC == null)
                    || ((infoBC != null) && infoBC.raIdPrstAccordee.equals(raMF.getIdPrestationAccordee()))) {
                if (infoBC == null) {
                    infoBC = new InfoBeneficiaireContainer();
                }
            } else {
                doTests(infoBC);
                infoBC = new InfoBeneficiaireContainer();
            }
            infoBC = infoBC.remplirWrapper(raMF);

            // Le dernier element...
            if (nbEntity == nbEntityCount) {
                doTests(infoBC);
            }
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }

        if ((nbErreur == 0) && (nbEntity == nbEntityCount)) {

            _addCell(getSession().getLabel("LISTE_ERR_AUCUNE_ERREUR") + getForMoisAnnee());
            _addCell("");
            _addCell("");
        }

    }

    protected void addRowErrorSumRetenues(final RERenteActiveJoinMembresFamille renteActJointMbFam,
            final String montantPrestationAccordee, final String sommeRetenues) throws Exception {
        // Récupération du message, et surcharge des montants dans le texte
        String messageErreur = PRStringUtils.replaceString(getSession().getLabel("LISTE_ERR_ERREUR_SOMME_RETENUES"),
                "{sommeRetenues}", sommeRetenues);
        messageErreur = PRStringUtils.replaceString(messageErreur, "{montantRente}", montantPrestationAccordee);

        // Insertion de la ligne
        _addCell(renteActJointMbFam.getNss() + " / " + renteActJointMbFam.getNom() + " "
                + renteActJointMbFam.getPrenom() + " / "
                + PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(renteActJointMbFam.getDateNaissance()) + " / "
                + getLibelleCourtSexe(renteActJointMbFam.getCsSexe()) + " / "
                + getLibellePays(renteActJointMbFam.getCodeNationalite()));
        _addCell(renteActJointMbFam.getIdPrestationAccordee());
        _addCell(messageErreur);
        this._addDataTableRow();
    }

    private void ajouterRentesInferieuresAuxRetenues(final REListeRentesActivesJoinMembresFamilleManager manager)
            throws Exception {

        // Iteration sur chaque prestation accordée issue du manager
        for (RERenteActiveJoinMembresFamille renteActJointMbFam : manager.getContainerAsList()) {

            // Récupération du montant de la rente accordée
            BigDecimal montantPrestationAccordee = montantRenteAccordee(renteActJointMbFam.getIdPrestationAccordee());
            // Récupération de la somme des retenues liées à la rente
            BigDecimal sommeRetenues = RERetenuesUtil.getSommesRetenuesByRenteAccordee(getSession(),
                    renteActJointMbFam.getIdPrestationAccordee());

            // Si somme retenues supérieure que montant prestation, ajout de la ligne dans la liste.
            if (montantPrestationAccordee.compareTo(sommeRetenues) < 0) {
                addRowErrorSumRetenues(renteActJointMbFam, montantPrestationAccordee.toString(),
                        sommeRetenues.toString());
            }
        }
    }

    protected void doTests(final InfoBeneficiaireContainer infoBC) throws Exception {
        // Test initial :
        // On contrôle que le tiers soit actif....
        if ((infoBC.isTiersInactif != null) && infoBC.isTiersInactif.booleanValue()) {
            addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_10"));
        }

        // Test 1 :
        // Si une RO AI 50.1 est versée à un homme veuf dont le degré d'invalidité est plus petit que 70% et sans code
        // cas spécial 38,
        // il faut qu'il ait au moins un enfant de moins de 18 ans dont une prestation est versée, sinon impression de
        // l'erreur :
        // " Reprendre une décision pour cet assuré dont le degré d'invalidité et le genre de prestation ne correspondent pas"

        // 1a. si c'est une RO AI 50.1
        test_1: if (infoBC.raCodePrest.equals("50") && infoBC.raFractionRente.equals("1")) {

            // 1b. degré d'invalidité plus petit que 70%
            if (Integer.parseInt(infoBC.raDegreInv) < 70) {

                // 1c. sans code cas spécial 38
                if (!hasRenteAccordeeCs38(infoBC)) {
                    boolean isConjointDecede = false;
                    int nbEnfantMoins18ansDontPrestationVersee = 0;
                    boolean isHomme = false;

                    if (PRACORConst.CS_HOMME.equals(infoBC.raCsSexe)) {
                        isHomme = true;
                    } else {
                        isHomme = false;
                        // si c'est une femme on peut arreter le test_1 ici
                        break test_1;
                    }

                    RelationContainer lastRelation = getLastRelation(infoBC);

                    // Est considéré comme veuf si son dernier conjoint est décédé, mais s'ils sont divorcé.
                    if (isVeuf(lastRelation)) {
                        isConjointDecede = true;
                    }

                    if (isConjointDecede) {

                        // On récupère tous les enfants du bénéficiaire.
                        SFEnfantManager mgr = new SFEnfantManager();
                        mgr.setSession(getSession());
                        mgr.setForIdConjointsIn(getIdsConjoints(infoBC));
                        mgr.find(getTransaction());
                        for (int i = 0; i < mgr.size(); i++) {
                            SFEnfant e = (SFEnfant) mgr.get(i);
                            SFMembreFamille enfant = new SFMembreFamille();
                            enfant.setIdMembreFamille(e.getIdMembreFamille());
                            enfant.setSession(getSession());
                            enfant.retrieve(getTransaction());

                            // Si l'enfant à plus de 18 ans sans période d'études...
                            String dateNaissance = enfant.getDateNaissance();

                            cal.set(Calendar.DAY_OF_MONTH, 1);
                            cal.set(Calendar.MONTH, Integer.parseInt(mois) - 1);
                            cal.set(Calendar.YEAR, Integer.parseInt(annee));
                            cal.add(Calendar.YEAR, -18);

                            String dateDebut = getDateFormatted(cal);

                            // si - de 18 ans
                            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateNaissance)) {
                                // si idTiers
                                if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                                    // 1f. si une prestation est versée
                                    RERenteAccordeeManager raMan = new RERenteAccordeeManager();
                                    raMan.setSession(getSession());
                                    raMan.setForIdTiersBeneficiaire(enfant.getIdTiers());
                                    raMan.setForEnCoursAtMois(getForMoisAnnee());
                                    raMan.find(BManager.SIZE_NOLIMIT);

                                    if (raMan.size() > 0) {
                                        nbEnfantMoins18ansDontPrestationVersee++;
                                        // si il y a un enfant de mois de 18 ans avec une prestation versee
                                        // on peut arreter le test_1 ici
                                        break test_1;
                                    }
                                }
                            }
                        }
                    } else {
                        addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_07"));
                    }

                    // donc, si homme veuf et pas d'enfant de moins de 18 ans dont prestation versée
                    if (isHomme && isConjointDecede && (nbEnfantMoins18ansDontPrestationVersee == 0)) {
                        addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_01"));
                    }
                }
            }
        }

        // Test 2 :
        // Si une RO AI 50.1 est versée à une femme non-veuve dont le degré d'invalidité est plus petit que 70% et sans
        // code spécial 38,
        // impression de l'erreur :
        // " Reprendre une décision pour cette assurée dont le degré d'invalidité et le genre de prestation ne correspondent pas"

        // 2a. si c'est une RO AI 50.1
        test_2: if (infoBC.raCodePrest.equals("50") && infoBC.raFractionRente.equals("1")) {

            // 2b. degré d'invalidité plus petit que 70%
            if (Integer.parseInt(infoBC.raDegreInv) < 70) {

                // 2c. sans code cas spécial 38
                if (!hasRenteAccordeeCs38(infoBC)) {
                    boolean isConjointDecede = false;
                    boolean isHomme = false;

                    if (infoBC.raCsSexe.equals(PRACORConst.CS_HOMME)) {
                        isHomme = true;
                        // si c'est un homme on peut arreter le test_2 ici
                        break test_2;
                    } else {
                        isHomme = false;
                    }

                    RelationContainer lastRelation = getLastRelation(infoBC);

                    // Est considéré comme veuf si son dernier conjoint est décédé, mais s'ils sont divorcé.
                    if (isVeuf(lastRelation)) {
                        isConjointDecede = true;
                        break test_2;
                    }

                    // donc, si femme non-veuve
                    if (!isHomme && !isConjointDecede) {
                        addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_02"));
                    }
                }
            }
        }

        // Test 3 :
        // Si une RO AVS 13.0 est versée à un homme veuf, il faut qu'il ait au moins un enfant de moins de 18 ans dont
        // une prestation est
        // versée, sinon, impression de l'erreur :
        // " Suppression de la rente de veuf pour cet assuré, car il n'est plus au bénéfice de rentes pour enfants"

        // 3a. si c'est une RO AVS 13.0
        test_3: if (infoBC.raCodePrest.equals("13") && infoBC.raFractionRente.equals("0")) {
            boolean isConjointDecede = false;
            int nbEnfantMoins18ansDontPrestationVersee = 0;
            boolean isHomme = false;

            if (PRACORConst.CS_HOMME.equals(infoBC.raCsSexe)) {
                isHomme = true;
            } else {
                isHomme = false;
                // si pas un homme on peut arreter le test 3 ici
                break test_3;
            }

            RelationContainer lastRelation = getLastRelation(infoBC);

            // Est considéré comme veuf si son dernier conjoint est décédé, mais s'ils sont divorcé.
            if (isVeuf(lastRelation)) {
                isConjointDecede = true;
            }

            // Pour chaque membre
            // uniquement si veuf

            if (isConjointDecede) {
                // On récupère tous les enfants du bénéficiaire.
                SFEnfantManager mgr = new SFEnfantManager();
                mgr.setSession(getSession());
                mgr.setForIdConjointsIn(getIdsConjoints(infoBC));
                mgr.find(getTransaction());
                for (int i = 0; i < mgr.size(); i++) {

                    SFEnfant e = (SFEnfant) mgr.get(i);
                    SFMembreFamille enfant = new SFMembreFamille();
                    enfant.setIdMembreFamille(e.getIdMembreFamille());
                    enfant.setSession(getSession());
                    enfant.retrieve(getTransaction());

                    // Si l'enfant à plus de 18 ans sans période d'études...
                    String dateNaissance = enfant.getDateNaissance();

                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.MONTH, Integer.parseInt(mois) - 1);
                    cal.set(Calendar.YEAR, Integer.parseInt(annee));
                    cal.add(Calendar.YEAR, -18);

                    String dateDebut = getDateFormatted(cal);

                    // si - de 18 ans

                    if (enfant.getDateDeces().isEmpty()
                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateNaissance)) {

                        // si idTiers
                        if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                            // 3e. si une prestation est versée
                            RERenteAccordeeManager raMan = new RERenteAccordeeManager();
                            raMan.setSession(getSession());
                            raMan.setForIdTiersBeneficiaire(enfant.getIdTiers());
                            raMan.setForEnCoursAtMois(getForMoisAnnee());
                            raMan.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_DIMINUE + ", "
                                    + IREPrestationAccordee.CS_ETAT_PARTIEL + ", "
                                    + IREPrestationAccordee.CS_ETAT_VALIDE);
                            raMan.find(BManager.SIZE_NOLIMIT);

                            if (raMan.size() > 0) {
                                nbEnfantMoins18ansDontPrestationVersee++;
                                // si un enfant de moins de 18 ans touche une prestation
                                // on peut arreter le test ici
                                break test_3;
                            }
                        }
                    }

                }
            }

            // donc, si homme veuf et pas d'enfant de moins de 18 ans dont prestation versée
            if (isHomme && isConjointDecede && (nbEnfantMoins18ansDontPrestationVersee == 0)) {
                addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_03"));
            } else if (!isConjointDecede) {
                addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_08"));
            }
        }

        // Test 4 :
        // Si une prestation 33.0 ou 53.x ou 73.x est versée, que le bénéf. de la RA est l'ex-conjoint du requérant
        // (divorcé ou séparé judiciairement) et
        // qu'il (le bénéf. de la RA) n'a plus d'enfant au bénéfice de rentes pour enfants, imprimer l'erreur :
        // " Suppression de la rente complémentaire car cet ex-conjoint n'a plus d'enfant au bénéfice de rentes pour enfants"
        // (Faire attention, un ex-conjoint peut également être un conjoint en même temp, dans ce cas-là, pas d'erreur,
        // c'est un remariage
        // avec la même personne)

        // Si une prestation 33.0 ou 53.x ou 73.x est versée, que le bénéf. de la RA a une relation (dernière) avec une
        // personne autre que le
        // requérant, imprimer le message d'erreur.
        // " Suppression de la rente complémentaire car cet ex-conjoint a été remarié"

        // 4a. si c'est une prestation 33.0 ou 53x ou 73.x
        test_4: if ((infoBC.raCodePrest.equals("33") && infoBC.raFractionRente.equals("0"))
                || infoBC.raCodePrest.equals("53") || infoBC.raCodePrest.equals("73")) {
            // on retrouve le requérant avec idTiersBaseCalcul
            String idTiersRequerant = infoBC.raIdTiersBaseCalcul;
            RelationContainer relationActuelle = getLastRelation(infoBC);

            if (relationActuelle == null) {
                break test_4;
            }
            String idMFConjoint = relationActuelle.idMFConjoint;
            SFMembreFamille conjointActuel = new SFMembreFamille();
            conjointActuel.setSession(getSession());
            conjointActuel.setIdMembreFamille(idMFConjoint);
            conjointActuel.retrieve(getTransaction());

            // Le conjoint actuel est le requérant
            if (!JadeStringUtil.isBlankOrZero(idTiersRequerant) && idTiersRequerant.equals(conjointActuel.getIdTiers())) {

                // On contrôle s'ils sont séparé/divorcé
                if (globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(relationActuelle.csTypeRelation)
                        || globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT
                                .equals(relationActuelle.csTypeRelation)) {

                    // On récupère tous les enfants du bénéficiaire de la RA et de son conjoint actuel, le requérant
                    // en l'occurence.
                    SFEnfantManager mgr = new SFEnfantManager();
                    mgr.setSession(getSession());
                    mgr.setForIdConjoint(relationActuelle.idConjoint);
                    mgr.find(getTransaction());

                    for (int i = 0; i < mgr.size(); i++) {
                        SFEnfant e = (SFEnfant) mgr.get(i);
                        SFMembreFamille enfant = new SFMembreFamille();
                        enfant.setIdMembreFamille(e.getIdMembreFamille());
                        enfant.setSession(getSession());
                        enfant.retrieve(getTransaction());

                        if (!JadeStringUtil.isBlankOrZero(enfant.getIdTiers())) {
                            RERenteAccordeeManager raMan = new RERenteAccordeeManager();
                            raMan.setSession(getSession());
                            raMan.setForEnCoursAtMois(getForMoisAnnee());
                            raMan.setForCodesPrestationsIn("'14', '15', '16', '24', '25', '26', '34', '35', '45', '55', '74', '75'");
                            raMan.setForIdTiersBeneficiaire(enfant.getIdTiers());
                            raMan.find(1);
                            // Des enfants sont au bénéfice d'une RA
                            if (!raMan.isEmpty()) {
                                break test_4;
                            }
                        }
                    }

                    addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_04"));
                }

            }
            // Le conjoint actuel n'est pas le requérant.
            else {
                addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_09"));
            }
        }

        // TEST 6 : voir si bénéficiaire avec une rente en cours et décédé
        if (!JadeStringUtil.isBlankOrZero(infoBC.raDateDeces)) {
            addCell(infoBC,
                    getSession().getLabel("LISTE_ERR_ERREUR_05") + " "
                            + PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(infoBC.raDateDeces));
        }

        // TEST 7 : Si la RA du Bénéficaire est de genre (10, 20, 50, 70, 72) et a un CS à 05
        // et que le dernier conjoint du bénéf. de la RA a une relation relation MARIE
        // et possède une rente en cours : OK, sinon : erreur
        if ("10".equals(infoBC.raCodePrest) || "20".equals(infoBC.raCodePrest) || "50".equals(infoBC.raCodePrest)
                || "72".equals(infoBC.raCodePrest) || "70".equals(infoBC.raCodePrest)) {

            if ("05".equals(infoBC.raCS1) || "05".equals(infoBC.raCS2) || "05".equals(infoBC.raCS3)
                    || "05".equals(infoBC.raCS4) || "05".equals(infoBC.raCS5)) {

                RelationContainer lastRelation = getLastRelation(infoBC);

                if ((lastRelation != null)
                        && (globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(lastRelation.csTypeRelation) || globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT
                                .equals(lastRelation.csTypeRelation))) {

                    // On recherche la rente en cours du conjoint...
                    SFMembreFamilleListeRecapCorvus mfConjoint = new SFMembreFamilleListeRecapCorvus();

                    mfConjoint.setIdMembreFamille(lastRelation.idMFConjoint);
                    mfConjoint.setSession(getSession());
                    mfConjoint.retrieve(getTransaction());

                    REPrestationAccordeeManager raMgr = null;

                    if (!JadeStringUtil.isBlankOrZero(mfConjoint.getIdTiers())) {
                        raMgr = new REPrestationAccordeeManager();
                        raMgr.setSession(getSession());
                        raMgr.setForEnCoursAtMois(getForMoisAnnee());
                        raMgr.setForIdTiersBeneficiaire(mfConjoint.getIdTiers());
                        raMgr.find();
                    }

                    if ((raMgr == null) || raMgr.isEmpty()) {
                        REDemandeRenteVieillesseJoinTiersManager manager = new REDemandeRenteVieillesseJoinTiersManager();
                        manager.setSession(getSession());
                        manager.setForIdTiersRequerant(mfConjoint.getIdTiers());
                        manager.setForCsEtatDemandeRente(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                        manager.setAvecAjournement(true);
                        manager.find();

                        if (manager.getSize() > 0) {
                            addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_06_BIS"));
                        } else {
                            addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_06"));
                        }
                    }
                }
            }
        }

        // TEST 8 : voir si le bénéficiaire a un NSS actif
        if (JadeStringUtil.isBlankOrZero(infoBC.raNss)) {
            addCell(infoBC,
                    getSession().getLabel("LISTE_ERR_ERREUR_11") + " "
                            + PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(infoBC.raDateDeces));
        }

        // test 9 : si code cas spécial 07 ou 08 (incarcération ou ajournement) et que la rente n'est pas validée
        if (infoBC.raCsEtat.equals(IREPrestationAccordee.CS_ETAT_CALCULE)) {
            if (infoBC.contientCodeCasSpecial("07")) {
                addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_12"));
            } else if (infoBC.contientCodeCasSpecial("08")) {
                addCell(infoBC, getSession().getLabel("LISTE_ERR_ERREUR_13"));
            }
        }

    }

    private String getDateFormatted(final Calendar cal) {
        DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), "dd.MM.yyyy");
        return df.format(cal.getTime());

    }

    /*
     * Titre de l'email
     */
    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_ERR_TITRE");
    }

    public String getForMoisAnnee() {
        return forMoisAnnee;
    }

    protected String getIdsConjoints(final InfoBeneficiaireContainer ibc) {
        String idsStd = "";
        String idsRent = "";

        Set<String> keys = ibc.mapRelationsInfoContainer.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            String key = iterator.next();

            RelationInfoContainer ric = ibc.mapRelationsInfoContainer.get(key);
            if (globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_RENTES.equals(ric.csDomaine)) {
                idsRent += "," + ric.idConjont;
            } else if (globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(ric.csDomaine)) {
                idsStd += "," + ric.idConjont;
            }

        }

        if (!JadeStringUtil.isBlankOrZero(idsRent)) {
            return idsRent.substring(1, idsRent.length());
        } else {
            if (!JadeStringUtil.isBlankOrZero(idsStd)) {
                return idsStd.substring(1, idsStd.length());
            } else {
                return "-1";
            }
        }

    }

    // Retourne la relation la plus récente.
    // La priorité est mise sur le domaine des rentes du requérant.
    protected RelationContainer getLastRelation(final InfoBeneficiaireContainer ibc) {

        if ((ibc.mapRelationsInfoContainer == null) || ibc.mapRelationsInfoContainer.isEmpty()) {
            return null;
        }

        Set<String> keys = ibc.mapRelationsInfoContainer.keySet();
        // parcours des relations par domaine

        // On recherce la relation la plus récente. (cad avec la plus grande date de début.)
        // On exclu les relations enfant commun et indéfinie.
        // On ne prend en compte que les domaines rentes et standard, avec priorité au domaine rente.

        try {
            JADate ddRelationStandard = new JADate("01.01.1900");
            JADate ddRelationRente = new JADate("01.01.1900");
            JACalendar cal = new JACalendarGregorian();

            RelationContainer theMostRecentRelationStd = null;
            RelationContainer theMostRecentRelationRent = null;
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
                String key = iterator.next();
                RelationInfoContainer ric = ibc.mapRelationsInfoContainer.get(key);

                if (globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_RENTES.equals(ric.csDomaine)
                        || globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(ric.csDomaine)) {

                    List<RelationContainer> relations = ric.relations;
                    for (Iterator<RelationContainer> iterator2 = relations.iterator(); iterator2.hasNext();) {
                        RelationContainer rc = iterator2.next();

                        if (globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(rc.csTypeRelation)
                                || globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE
                                        .equals(rc.csTypeRelation)) {
                            continue;
                        }

                        if (!JadeStringUtil.isBlankOrZero(rc.dateDebut)) {
                            JADate currentDate = new JADate(
                                    PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(rc.dateDebut));

                            if (globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(ric.csDomaine)) {
                                if (cal.compare(ddRelationStandard, currentDate) == JACalendar.COMPARE_SECONDUPPER) {
                                    theMostRecentRelationStd = rc;
                                    ddRelationStandard = new JADate(currentDate.toAMJ());
                                }
                            } else {
                                if (cal.compare(ddRelationRente, currentDate) == JACalendar.COMPARE_SECONDUPPER) {
                                    theMostRecentRelationRent = rc;
                                    ddRelationRente = new JADate(currentDate.toAMJ());
                                }
                            }
                        }
                    }
                }
            }

            // Priorité au domaine des rentes...
            if (theMostRecentRelationRent != null) {
                return theMostRecentRelationRent;
            } else {
                return theMostRecentRelationStd;
            }

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est passé en paramètre
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(final String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est passé en paramètre
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(final String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }
    }

    /**
     * Return true si la rente accordee contient un code cas speciaux 38
     * 
     * @param pRa
     * @return
     */
    private boolean hasRenteAccordeeCs38(final InfoBeneficiaireContainer ic) {

        if ("38".equals(ic.raCS1)) {
            return true;
        }

        if ("38".equals(ic.raCS2)) {
            return true;
        }

        if ("38".equals(ic.raCS3)) {
            return true;
        }

        if ("38".equals(ic.raCS4)) {
            return true;
        }

        if ("38".equals(ic.raCS5)) {
            return true;
        }

        return false;
    }

    /*
     * Initialisation des colonnes et des groupes
     */
    /**
	 */
    @Override
    protected void initializeTable() {
        // colonnes
        this._addColumnLeft(getSession().getLabel("LISTE_ERR_DETAIL_BENEFICIAIRE"), 35);
        this._addColumnRight(getSession().getLabel("LISTE_ERR_NO_RA"), 10);
        this._addColumnRight(getSession().getLabel("LISTE_ERR_GENRE_ERREUR"), 55);

    }

    protected boolean isVeuf(final RelationContainer rc) throws Exception {
        if (rc == null) {
            return false;
        }

        // On récupére le conjoint et s'il est décédé
        // indépendemment du type de relation, le bénéficiaire de la rente sera considéré comme veuf).
        SFMembreFamille mf = new SFMembreFamille();
        mf.setSession(getSession());
        mf.setIdMembreFamille(rc.idMFConjoint);
        mf.retrieve(getTransaction());

        if (!mf.isNew()) {
            if (JadeStringUtil.isBlankOrZero(mf.getDateDeces())) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Set la jobQueue
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private BigDecimal montantRenteAccordee(final String idRenteAccordee) throws Exception {
        RERenteAccordee rente = new RERenteAccordee();
        rente.setSession(getSession());
        rente.setIdPrestationAccordee(idRenteAccordee);
        rente.retrieve();

        return new BigDecimal(rente.getMontantPrestation());

    }

    public void setForMoisAnnee(final String forMoisAnnee) {
        this.forMoisAnnee = forMoisAnnee;
    }

    @Override
    public void summary() {
        try {
            // bz8646 : Ajouter les rentes qui ont une somme de retenues supérieur à la rente
            ajouterRentesInferieuresAuxRetenues((REListeRentesActivesJoinMembresFamilleManager) getManager());
        } catch (Exception e) {
            new Exception(e);
        }
    }

}
