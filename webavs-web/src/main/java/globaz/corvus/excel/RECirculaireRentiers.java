package globaz.corvus.excel;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiersManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Impression dans un fichier xls de : -> NSS -> Nom, prénom de l'assuré principal -> Adresse courrier
 * 
 * Pour tous les rentiers en cours dans le but de faire un publipostage
 * 
 * @author HPE
 */
public class RECirculaireRentiers extends REAbstractListExcel {

    public RECirculaireRentiers(BSession session) {
        super(session, "RECirculaireRentiers", "Adaptation - Circulaire aux rentiers");
    }

    private void ajouterLigne(REPrestAccJointInfoComptaJointTiers ra, String keyIdAdrPmt, String keyIdTiersBC,
            REPrestationAccordeeManager prestationAccordeeManager) throws Exception {

        createRow();
        this.createCell(ra.getNss(), getStyleListLeft()); // NSS
        this.createCell(ra.getNom() + " " + ra.getPrenom(), getStyleListLeft()); // NOM, PRENOM

        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(ra.getIdTiersBeneficiaire());
        tiers.retrieve();

        TIAdresseDataSource adresse = null;
        for (TIAdresseDataSource uneAdresse : getAdresseCourrier(tiers, ra)) {
            if (adresse == null) {
                adresse = uneAdresse;
            }
        }

        if (adresse == null) {
            this.createCell("Aucune adresse trouvée !! idRA = " + ra.getIdPrestationAccordee() + " | idTiersBenef = "
                    + ra.getIdTiersBeneficiaire());
        } else {
            this.createCell(
                    getSession()
                            .getCodeLibelle(adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CS_TITRE)),
                    getStyleListLeft());
            // ADRESSE
            this.createCell(adresse.fullLigne1, getStyleListLeft());
            this.createCell(adresse.fullLigne2, getStyleListLeft());
            this.createCell(adresse.fullLigne3, getStyleListLeft());
            this.createCell(adresse.fullLigne4, getStyleListLeft());
            this.createCell(adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE),
                    getStyleListLeft());
            this.createCell(adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_ATTENTION),
                    getStyleListLeft());
            this.createCell(adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE), getStyleListLeft());
            this.createCell(adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO), getStyleListLeft());
            this.createCell(adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA), getStyleListLeft());
            this.createCell(adresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE), getStyleListLeft());
            this.createCell(getSession().getCodeLibelle(tiers.getLangue()), getStyleListLeft());

            String beneficiairePC = "";
            prestationAccordeeManager.setForIdTiersBeneficiaire(tiers.getIdTiers());
            if (prestationAccordeeManager.getCount() >= 1) {
                beneficiairePC = "X";
            }
            this.createCell(beneficiairePC, getStyleListCenter());

        }

    }

    private Collection<TIAdresseDataSource> getAdresseCourrier(TITiers tiers, REPrestAccJointInfoComptaJointTiers ra)
            throws Exception {

        TIAdresseDataSource adresseDataSource = null;
        String idTiers = ra.getIdTiersBeneficiaire();

        Collection<TIAdresseDataSource> list = new ArrayList<TIAdresseDataSource>();

        if (tiers.isNew()) {
            throw new Exception(getSession().getLabel("ERREUR_ADAPTATION_AUC_TIERS_ID") + " " + tiers.getIdTiers());
        } else {

            adresseDataSource = tiers.getAdresseAsDataSource(PRTiersHelper.CS_ADRESSE_COURRIER,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);

            if (adresseDataSource != null) {
                list.add(adresseDataSource);
                return list;
            } else {

                RERenteAccJoinTblTiersJoinDemRenteManager renteAccManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
                renteAccManager.setSession(getSession());

                if (ra.getNss().length() > 14) {
                    renteAccManager.setLikeNumeroAVSNNSS("true");
                } else {
                    renteAccManager.setLikeNumeroAVSNNSS("false");
                }
                // ce manager cherche dans l'idTiers parent en boucle !
                renteAccManager.setLikeNumeroAVS(ra.getNss());
                renteAccManager.wantCallMethodBeforeFind(true);
                renteAccManager.setRechercheFamille(true);
                renteAccManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE);
                renteAccManager.find();

                // Liste Pour les rentes accordées non principale
                Collection<RERenteAccordee> listeMemeAdPmtNonPrincipale = new LinkedList<RERenteAccordee>();

                for (RERenteAccJoinTblTiersJoinDemandeRente elm : renteAccManager.getContainerAsList()) {

                    REInformationsComptabilite ic = new REInformationsComptabilite();
                    ic.setSession(getSession());
                    ic.setIdInfoCompta(ra.getIdInfoCompta());
                    ic.retrieve();

                    // Uniquement les rentes accorédes dont l'adresse de paiement est identique à l'écheance courante et
                    // sans date de fin de droit
                    if (elm.getIdTierAdressePmt().equals(ic.getIdTiersAdressePmt())
                            && JadeStringUtil.isEmpty(elm.getDateFinDroit())) {

                        RERenteAccordee rac = new RERenteAccordee();
                        rac.setSession(getSession());
                        rac.setIdPrestationAccordee(elm.getIdPrestationAccordee());
                        rac.retrieve();

                        // Si la rente est principale je l'utilise, sinon je l'insere dans la liste pour les rentes
                        // accordées non principale
                        if (rac.getCodePrestation().equals("10") || rac.getCodePrestation().equals("20")
                                || rac.getCodePrestation().equals("13") || rac.getCodePrestation().equals("23")
                                || rac.getCodePrestation().equals("50") || rac.getCodePrestation().equals("70")
                                || rac.getCodePrestation().equals("72")) {

                            REInformationsComptabilite infoCompt = ra.loadInformationsComptabilite();
                            idTiers = infoCompt.getIdTiersAdressePmt();

                            tiers = new TITiers();
                            tiers.setSession(getSession());
                            tiers.setIdTiers(idTiers);
                            tiers.retrieve();

                            if (tiers.isNew()) {
                                throw new Exception(getSession().getLabel("ERREUR_ADAPTATION_AUC_TIERS_ID") + " "
                                        + tiers.getIdTiers());
                            } else {

                                adresseDataSource = tiers.getAdresseAsDataSource(PRTiersHelper.CS_ADRESSE_COURRIER,
                                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                                        JACalendar.todayJJsMMsAAAA(), true);
                            }

                        } else {
                            listeMemeAdPmtNonPrincipale.add(ra);
                        }
                    }
                }

                if (adresseDataSource != null) {
                    list.add(adresseDataSource);
                    return list;
                } else {

                    // Si aucune adresse n'est trouvée, je recherche dans la liste des rentes accrodées non principale
                    if (!listeMemeAdPmtNonPrincipale.isEmpty()) {
                        for (RERenteAccordee ra2 : listeMemeAdPmtNonPrincipale) {
                            REInformationsComptabilite infoCompt1 = ra2.loadInformationsComptabilite();

                            idTiers = infoCompt1.getIdTiersAdressePmt();

                            tiers = new TITiers();
                            tiers.setSession(getSession());
                            tiers.setIdTiers(idTiers);
                            tiers.retrieve();

                            if (tiers.isNew()) {
                                throw new Exception(getSession().getLabel("ERREUR_ADAPTATION_AUC_TIERS_ID") + " "
                                        + tiers.getIdTiers());
                            } else {

                                adresseDataSource = tiers.getAdresseAsDataSource(PRTiersHelper.CS_ADRESSE_COURRIER,
                                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                                        JACalendar.todayJJsMMsAAAA(), true);
                            }

                            // Attention si null, retourné aussi, mais signifie qu'il n'y aucune adresse trouvée !
                            list.add(adresseDataSource);
                            return list;

                        }
                    }
                }

            }
            list.add(adresseDataSource);
            return list;
        }
    }

    private void initListe() {

        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<String>();
        colTitles.add(getSession().getLabel("PROCESS_CIRC_NSS"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_NOM_PRENOM"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_TITRE"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_DES_1"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_DES_2"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_DES_3"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_DES_4"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_CASE_POST"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_A_L_ATTENTION"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_RUE"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_NO"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_NPA"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_LOCALITE"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_LANGUE"));
        colTitles.add(getSession().getLabel("PROCESS_CIRC_BENEFICIAIRE_PC"));

        createSheet(getSession().getLabel("PROCESS_CIRC_MAIL"));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter();

        // définition de la taille des cellules
        int numCol = 0;

        currentSheet.setColumnWidth((short) numCol++, (short) 4500); // NSS
        currentSheet.setColumnWidth((short) numCol++, (short) 7000); // Nom prénom
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // Titre
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Désignation 1
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Désignation 2
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Désignation 3
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Désignation 4
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Case Postale
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // A l'attention de
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Rue
        currentSheet.setColumnWidth((short) numCol++, (short) 2000); // N°
        currentSheet.setColumnWidth((short) numCol++, (short) 2000); // NPA
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // Localité
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // Bénéficiare PC
    }

    /**
     * Initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(REPrestAccJointInfoComptaJointTiersManager manager,
            REPrestationAccordeeManager prestationAccordeeManager, BTransaction transaction) throws Exception {

        manager.find(BManager.SIZE_NOLIMIT);

        // Création de la page
        initListe();

        /*
         * Règles : --------
         * 
         * 1) On parcourt toutes les rentes accordées en cours 2) idTiersBC = déjà présent ? => NON = (A) => OUI = (B)
         * 
         * (A) = Ajouter (B) = idAdrPmt = déjà présent ? => NON = Ajouter => OUI = Ne pas ajouter, mais stocker la rente
         * principale si existante 10/20/50/70/13/23
         */

        Map<String, Map<String, REPrestAccJointInfoComptaJointTiers>> adressesPaiementParIdTiersBaseCalcul = new HashMap<String, Map<String, REPrestAccJointInfoComptaJointTiers>>();

        // Parcours du manager et remplissage de la map
        for (int i = 0; i < manager.size(); i++) {
            REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) manager.get(i);

            Map<String, REPrestAccJointInfoComptaJointTiers> prestationsAccordeesParIdAdressePaiment = new HashMap<String, REPrestAccJointInfoComptaJointTiers>();

            // idTiersBC = déjà présent ?
            if (!adressesPaiementParIdTiersBaseCalcul.containsKey(ra.getIdTiersBaseCalcul())) {
                // Ajouter
                prestationsAccordeesParIdAdressePaiment.put(ra.getIdTiersAdressePaiement(), ra);
                adressesPaiementParIdTiersBaseCalcul.put(ra.getIdTiersBaseCalcul(),
                        prestationsAccordeesParIdAdressePaiment);
            } else {
                // idAdrPmt = déjà présent ?
                if (!adressesPaiementParIdTiersBaseCalcul.get(ra.getIdTiersBaseCalcul()).containsKey(
                        ra.getIdTiersAdressePaiement())) {
                    // Ajouter
                    prestationsAccordeesParIdAdressePaiment.put(ra.getIdTiersAdressePaiement(), ra);
                    adressesPaiementParIdTiersBaseCalcul.put(ra.getIdTiersBaseCalcul(),
                            prestationsAccordeesParIdAdressePaiment);
                } else {
                    // Si rente principale, remplacer
                    if (CodePrestation.getCodePrestation(Integer.parseInt(ra.getCodePrestation())).isRentePrincipale()) {
                        prestationsAccordeesParIdAdressePaiment.put(ra.getIdTiersAdressePaiement(), ra);
                        adressesPaiementParIdTiersBaseCalcul.put(ra.getIdTiersBaseCalcul(),
                                prestationsAccordeesParIdAdressePaiment);
                    }
                }
            }
        }

        // Parcours de la MAP et remplissage des cellules
        for (String unIdTiersBaseCalcul : adressesPaiementParIdTiersBaseCalcul.keySet()) {

            Map<String, REPrestAccJointInfoComptaJointTiers> prestationsAccordeesParIdAdressePaiment = adressesPaiementParIdTiersBaseCalcul
                    .get(unIdTiersBaseCalcul);

            for (String unIdAdressePaiment : prestationsAccordeesParIdAdressePaiment.keySet()) {
                ajouterLigne(prestationsAccordeesParIdAdressePaiment.get(unIdAdressePaiment), unIdAdressePaiment,
                        unIdTiersBaseCalcul, prestationAccordeeManager);
            }
        }

        return currentSheet;
    }

}
