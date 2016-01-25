package globaz.corvus.itext;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class REDemandeCompensationAdapter {

    /**
     * Classe interne qui d�fini les �l�ments n�cessaires pour le groupeRente
     */
    public class REGroupeRente implements Comparable<REGroupeRente> {

        public String genreRente;
        public String idTiers;
        public String moisAnneeDebut;;
        public String moisAnneeFin;
        public FWCurrency montantAnnuel = new FWCurrency();
        public String nom;
        public String prenom;

        @Override
        public int compareTo(REGroupeRente grpRente) {
            if (genreRente.compareTo(grpRente.genreRente) != 0) {
                return genreRente.compareTo(grpRente.genreRente);
            } else if (nom.compareTo(grpRente.nom) != 0) {
                return nom.compareTo(grpRente.nom);
            } else if (prenom.compareTo(grpRente.prenom) != 0) {
                return prenom.compareTo(grpRente.prenom);
            } else {
                return 0;
            }
        }
    }

    /**
     * Classe interne qui d�fini les cl�s pour le tri par idTiersBeneficiaire
     */
    public class REKeyIdTiersBeneficiaire {

        public String idTiersBeneficiaire;

        public int compareTo(Object o) {
            REKeyIdTiersBeneficiaire key = (REKeyIdTiersBeneficiaire) o;

            if (idTiersBeneficiaire.compareTo(key.idTiersBeneficiaire) != 0) {
                return idTiersBeneficiaire.compareTo(key.idTiersBeneficiaire);
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof REKeyIdTiersBeneficiaire)) {
                return false;
            }
            REKeyIdTiersBeneficiaire key = (REKeyIdTiersBeneficiaire) obj;
            return ((key.idTiersBeneficiaire.equals(idTiersBeneficiaire)));
        }

        @Override
        public int hashCode() {
            return (idTiersBeneficiaire).hashCode();
        }
    }

    /**
     * Classe interne servant de clef pour regrouper par p�riode
     * 
     * @author HPE
     */
    public final class REKeyPeriode implements Comparable<REKeyPeriode> {

        public String dateDebut = "";
        public String dateFin = "";
        public String tauxInvalidite = "";

        public REKeyPeriode() {
        }

        public REKeyPeriode(REKeyPeriode k) {
            dateDebut = k.dateDebut;
            dateFin = k.dateFin;
        }

        public REKeyPeriode(String dateDebut, String dateFin) {
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        }

        @Override
        public int compareTo(REKeyPeriode key) {
            if (dateDebut.compareTo(key.dateDebut) != 0) {
                return dateDebut.compareTo(key.dateDebut);
            } else if (dateFin.compareTo(key.dateFin) != 0) {
                return dateFin.compareTo(key.dateFin);
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof REKeyPeriode)) {
                return false;
            }

            REKeyPeriode key = (REKeyPeriode) obj;

            return ((key.dateDebut.equals(dateDebut)) && (key.dateFin.equals(dateFin)));
        }

        @Override
        public int hashCode() {
            return (dateDebut + dateFin).hashCode();
        }
    }

    private RECreancier creancier;
    private String dateDebut = "31.12.9999";
    private String dateFin = "01.01.1000";
    private Map<REDemandeCompensationAdapter.REKeyPeriode, List<REDemandeCompensationAdapter.REGroupeRente>> groupeRenteHash = new TreeMap<REDemandeCompensationAdapter.REKeyPeriode, List<REDemandeCompensationAdapter.REGroupeRente>>();
    private String idTierRequerant = "";
    private REKeyIdTiersBeneficiaire keyIdTiers = new REKeyIdTiersBeneficiaire();
    private REKeyPeriode keyPeriode = new REKeyPeriode();
    private String moisAnnee;
    private FWCurrency montantTotal = new FWCurrency();
    private BSession session;

    public REDemandeCompensationAdapter(BSession session, RECreancier creancier, String moisAnnee) {
        this.session = session;
        this.creancier = creancier;
        this.moisAnnee = moisAnnee;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public Map<REDemandeCompensationAdapter.REKeyPeriode, List<REDemandeCompensationAdapter.REGroupeRente>> getGroupeRenteHash() {
        return groupeRenteHash;
    }

    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public REKeyIdTiersBeneficiaire getKeyIdTiersBeneficiaire() {
        return keyIdTiers;
    }

    public REKeyPeriode getKeyPeriode() {
        return keyPeriode;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public FWCurrency getMontantTotal() {
        return montantTotal;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setGroupeRenteHash(Map<REKeyPeriode, List<REGroupeRente>> groupeRenteHash) {
        this.groupeRenteHash = groupeRenteHash;
    }

    public void setIdTierRequerant(String idTierRequerant) {
        this.idTierRequerant = idTierRequerant;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setMontantTotal(FWCurrency montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void traitement() throws Exception {

        try {

            // Voir si pour mois dernier pmt ou mois prochain paiement
            boolean isDernierPmt = true;

            String dateDernierPmt = REPmtMensuel.getDateDernierPmt(session);
            String dateProchainPmt = REPmtMensuel.getDateProchainPmt(session);

            if (!JadeStringUtil.isBlankOrZero(getMoisAnnee())) {
                if (getMoisAnnee().equals(dateProchainPmt)) {
                    isDernierPmt = false;
                }
            }

            // 1. Voir si pas d'assur� s�lectionn� dans le cr�ancier
            // (idTiersRegroupement)
            //
            // Si c'est le cas, prendre toutes les prestations dues pour une
            // demande de rente et
            // regrouper selon la liste des assur�s
            if (JadeStringUtil.isIntegerEmpty(creancier.getIdTiersRegroupement())) {

                // Retrouver la demande de rente
                REDemandeRente demandeRente = new REDemandeRente();
                demandeRente.setSession(session);
                demandeRente.setIdDemandeRente(creancier.getIdDemandeRente());
                demandeRente.retrieve();

                // Retrouver la demande de prestation
                PRDemande demandePrestation = new PRDemande();
                demandePrestation.setSession(session);
                demandePrestation.setIdDemande(demandeRente.getIdDemandePrestation());
                demandePrestation.retrieve();

                setIdTierRequerant(demandePrestation.getIdTiers());

                // Retrouver toutes les prestations dues pour une demande de
                // rente
                REPrestationsDuesJointDemandeRenteManager prestDuesMan = new REPrestationsDuesJointDemandeRenteManager();
                prestDuesMan.setSession(session);
                prestDuesMan.setForNoDemandeRente(creancier.getIdDemandeRente());
                prestDuesMan.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT);
                prestDuesMan.find(BManager.SIZE_NOLIMIT);

                REPeriodeInvaliditeManager periodeInvMan = new REPeriodeInvaliditeManager();
                periodeInvMan.setSession(session);
                periodeInvMan.setForIdDemandeRente(demandeRente.getIdDemandeRente());
                periodeInvMan.find(BManager.SIZE_NOLIMIT);

                List<REPeriodeInvalidite> listTauxInv = new ArrayList<REPeriodeInvalidite>();

                for (int i = 0; i < periodeInvMan.size(); i++) {
                    REPeriodeInvalidite periodeInv = (REPeriodeInvalidite) periodeInvMan.get(i);
                    listTauxInv.add(periodeInv);
                }

                // Passer sur toutes les prestations dues pour regrouper par
                // p�riode
                for (int i = 0; i < prestDuesMan.size(); i++) {
                    REPrestationsDuesJointDemandeRente prestDues = (REPrestationsDuesJointDemandeRente) prestDuesMan
                            .get(i);

                    if (!JadeStringUtil.isIntegerEmpty(prestDues.getIdTiersBeneficiaire())) {

                        if (prestDues.getCsType().equals(IREPrestationDue.CS_TYPE_MNT_TOT)) {

                            // Premi�rement on g�re la date de d�but, date de
                            // fin et montant total des paiements
                            // r�troactifs g�n�ral avant de rentrer dans les
                            // d�tails

                            // 1. Date de d�but, on prend la plus ancienne, donc
                            // on compare et remplace si n�cessaire
                            if (BSessionUtil.compareDateFirstGreater(session, dateDebut,
                                    prestDues.getDateDebutPaiement())) {
                                dateDebut = prestDues.getDateDebutPaiement();
                            }

                            // 2. Date de fin, on prend la plus r�cente, donc on
                            // compare et remplace si n�cessaire
                            if (BSessionUtil.compareDateFirstLower(session, dateFin, prestDues.getDateFinPaiement())) {
                                dateFin = prestDues.getDateFinPaiement();

                                if (dateFin.equals(dateDernierPmt) && !isDernierPmt) {
                                    dateFin = dateProchainPmt;
                                    montantTotal.add(prestDues.getMontant());
                                }

                            }

                            // 3. Montant total, on additionne � chaque fois le
                            // montant
                            montantTotal.add(prestDues.getMontant());

                        } else if (prestDues.getCsType().equals(IREPrestationDue.CS_TYPE_PMT_MENS)) {

                            // Cr�ation de la cl� (dans ce cas, sur p�riode)
                            REKeyPeriode key = new REKeyPeriode();

                            key.dateDebut = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(prestDues
                                    .getDateDebutPaiement());
                            key.dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(prestDues.getDateFinPaiement());

                            key.tauxInvalidite = "";

                            JADate dateDebutPeriode = new JADate(key.dateDebut);

                            JACalendar cal = new JACalendarGregorian();

                            for (REPeriodeInvalidite periodeInv : listTauxInv) {
                                JADate dateDebInv = new JADate(
                                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(periodeInv
                                                .getDateDebutInvalidite()));
                                JADate dateFinInv = new JADate(
                                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(periodeInv
                                                .getDateFinInvalidite()));

                                if ((cal.compare(dateDebutPeriode, dateDebInv) != JACalendar.COMPARE_SECONDUPPER)
                                        && (cal.compare(dateDebutPeriode, dateFinInv) != JACalendar.COMPARE_SECONDLOWER)) {
                                    key.tauxInvalidite = periodeInv.getDegreInvalidite();
                                } else if ((cal.compare(dateDebutPeriode, dateDebInv) != JACalendar.COMPARE_SECONDUPPER)
                                        && JadeStringUtil.isBlankOrZero(periodeInv.getDateFinInvalidite())) {
                                    key.tauxInvalidite = periodeInv.getDegreInvalidite();
                                }

                            }

                            // si cl� encore inexistante
                            if (!groupeRenteHash.containsKey(key)) {

                                // On cr�e un objet
                                REGroupeRente grpRente = new REGroupeRente();

                                grpRente.moisAnneeDebut = prestDues.getDateDebutPaiement();
                                grpRente.moisAnneeFin = prestDues.getDateFinPaiement();
                                grpRente.montantAnnuel.add(prestDues.getMontant());
                                grpRente.idTiers = prestDues.getIdTiersBeneficiaire();

                                grpRente.genreRente = prestDues.getCodePrestation();

                                PRTiersWrapper tier = PRTiersHelper.getTiersParId(session, grpRente.idTiers);
                                String nom = "";
                                String prenom = "";

                                if (tier != null) {
                                    nom = tier.getProperty(PRTiersWrapper.PROPERTY_NOM);
                                    prenom = tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                }

                                grpRente.nom = nom;
                                grpRente.prenom = prenom;

                                // Comme la cl� est inexistante, on cr�e la
                                // liste d'objet
                                List<REGroupeRente> list = new ArrayList<REDemandeCompensationAdapter.REGroupeRente>();
                                list.add(grpRente);

                                // On ins�re la cl� et la liste de grpRente dans
                                // la map
                                groupeRenteHash.put(key, list);

                                // si la cl� existe d�j�
                            } else {

                                // On r�cup�re la liste
                                List<REGroupeRente> list = groupeRenteHash.get(key);

                                // On cr�e un objet
                                REGroupeRente grpRente = new REGroupeRente();

                                grpRente.moisAnneeDebut = prestDues.getDateDebutPaiement();
                                grpRente.moisAnneeFin = prestDues.getDateFinPaiement();
                                grpRente.montantAnnuel.add(prestDues.getMontant());
                                grpRente.idTiers = prestDues.getIdTiersBeneficiaire();

                                grpRente.genreRente = prestDues.getCodePrestation();

                                PRTiersWrapper tier = PRTiersHelper.getTiersParId(session, grpRente.idTiers);
                                String nom = "";
                                String prenom = "";

                                if (tier != null) {
                                    nom = tier.getProperty(PRTiersWrapper.PROPERTY_NOM);
                                    prenom = tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                }

                                grpRente.nom = nom;
                                grpRente.prenom = prenom;

                                // On ins�re l'objet dans la liste
                                list.add(grpRente);

                            }

                        }

                    } else {

                        throw new Exception(session.getLabel("ERREUR_IDTIERS_BENEFICIAIRE")
                                + prestDues.getIdPrestationDue());

                    }

                }

                // 2. Voir si un assur� a �t� s�lectionn� dans le cr�ancier
                // (idTiersRegroupement)
                //
                // Si c'est le cas, prendre toutes les prestations dues pour une
                // demande de rente dont
                // les rentes accord�es ont la m�me cl� (idTiersAdressePmt +
                // referencePmt)
            } else {

                // Retrouver la demande de rente
                REDemandeRente demandeRente = new REDemandeRente();
                demandeRente.setSession(session);
                demandeRente.setIdDemandeRente(creancier.getIdDemandeRente());
                demandeRente.retrieve();

                // Retrouver la demande de prestation
                PRDemande demandePrestation = new PRDemande();
                demandePrestation.setSession(session);
                demandePrestation.setIdDemande(demandeRente.getIdDemandePrestation());
                demandePrestation.retrieve();

                setIdTierRequerant(demandePrestation.getIdTiers());

                // Retrouver toutes les prestations dues pour une demande de
                // rente selon la cl� (idTiersAdressePmt + domaine +
                // referencePmt)
                REPrestationsDuesJointDemandeRenteManager prestDuesMan = new REPrestationsDuesJointDemandeRenteManager();
                prestDuesMan.setSession(session);
                prestDuesMan.setForNoDemandeRente(creancier.getIdDemandeRente());

                // Construction de la cl� � v�rifier
                String idTiersAdrPmt = "";
                String referencePmt = "";

                // Rechercher la rente accord�e de l'idTiersRegroupement
                RERenteAccJoinTblTiersJoinDemRenteManager raJTJDMan = new RERenteAccJoinTblTiersJoinDemRenteManager();
                raJTJDMan.setSession(session);
                raJTJDMan.setForNoDemandeRente(demandeRente.getIdDemandeRente());
                raJTJDMan.find(BManager.SIZE_NOLIMIT);

                for (int i = 0; i < raJTJDMan.size(); i++) {
                    RERenteAccJoinTblTiersJoinDemandeRente ra = (RERenteAccJoinTblTiersJoinDemandeRente) raJTJDMan
                            .get(i);

                    if (ra.getIdTiersBeneficiaire().equals(creancier.getIdTiersRegroupement())) {
                        REInformationsComptabilite infoCompta = new REInformationsComptabilite();
                        infoCompta.setSession(session);
                        infoCompta.setIdInfoCompta(ra.getIdInfoCompta());
                        infoCompta.retrieve();

                        idTiersAdrPmt = infoCompta.getIdTiersAdressePmt();
                        referencePmt = ra.getReferencePmt();

                    }

                }

                prestDuesMan.setForCleIdTiersAdrPmtReferencePmt(idTiersAdrPmt + "_" + referencePmt);
                prestDuesMan.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT);
                prestDuesMan.find(BManager.SIZE_NOLIMIT);

                REPeriodeInvaliditeManager periodeInvMan = new REPeriodeInvaliditeManager();
                periodeInvMan.setSession(session);
                periodeInvMan.setForIdDemandeRente(demandeRente.getIdDemandeRente());
                periodeInvMan.find(BManager.SIZE_NOLIMIT);

                List<REPeriodeInvalidite> listTauxInv = new ArrayList<REPeriodeInvalidite>();

                for (int i = 0; i < periodeInvMan.size(); i++) {
                    REPeriodeInvalidite periodeInv = (REPeriodeInvalidite) periodeInvMan.get(i);
                    listTauxInv.add(periodeInv);
                }

                // Passer sur toutes les prestations dues pour regrouper par
                // idTiersBeneficiaire
                for (int i = 0; i < prestDuesMan.size(); i++) {
                    REPrestationsDuesJointDemandeRente prestDues = (REPrestationsDuesJointDemandeRente) prestDuesMan
                            .get(i);

                    if (!JadeStringUtil.isIntegerEmpty(prestDues.getIdTiersBeneficiaire())) {

                        if (prestDues.getCsType().equals(IREPrestationDue.CS_TYPE_MNT_TOT)) {

                            // Premi�rement on g�re la date de d�but, date de
                            // fin et montant total des paiements
                            // r�troactifs g�n�ral avant de rentrer dans les
                            // d�tails

                            // 1. Date de d�but, on prend la plus ancienne, donc
                            // on compare et remplace si n�cessaire
                            if (BSessionUtil.compareDateFirstGreater(session, dateDebut,
                                    prestDues.getDateDebutPaiement())) {
                                dateDebut = prestDues.getDateDebutPaiement();
                            }

                            // 2. Date de fin, on prend la plus r�cente, donc on
                            // compare et remplace si n�cessaire
                            if (BSessionUtil.compareDateFirstLower(session, dateFin, prestDues.getDateFinPaiement())) {
                                dateFin = prestDues.getDateFinPaiement();

                                if (dateFin.equals(dateDernierPmt) && !isDernierPmt) {
                                    dateFin = dateProchainPmt;
                                    montantTotal.add(prestDues.getMontant());
                                }

                            }

                            // 3. Montant total, on additionne � chaque fois le
                            // montant
                            montantTotal.add(prestDues.getMontant());

                        } else if (prestDues.getCsType().equals(IREPrestationDue.CS_TYPE_PMT_MENS)) {

                            // Cr�ation de la cl� (dans ce cas, sur p�riode)
                            REKeyPeriode key = new REKeyPeriode();
                            key.dateDebut = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(prestDues
                                    .getDateDebutPaiement());
                            key.dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(prestDues.getDateFinPaiement());

                            key.tauxInvalidite = "";

                            JADate dateDebutPeriode = new JADate(key.dateDebut);

                            JACalendar cal = new JACalendarGregorian();

                            for (REPeriodeInvalidite periodeInv : listTauxInv) {
                                JADate dateDebInv = new JADate(
                                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(periodeInv
                                                .getDateDebutInvalidite()));
                                JADate dateFinInv = new JADate(
                                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(periodeInv
                                                .getDateFinInvalidite()));

                                if ((cal.compare(dateDebutPeriode, dateDebInv) != JACalendar.COMPARE_SECONDUPPER)
                                        && (cal.compare(dateDebutPeriode, dateFinInv) != JACalendar.COMPARE_SECONDLOWER)) {
                                    key.tauxInvalidite = periodeInv.getDegreInvalidite();
                                } else if ((cal.compare(dateDebutPeriode, dateDebInv) != JACalendar.COMPARE_SECONDUPPER)
                                        && JadeStringUtil.isBlankOrZero(periodeInv.getDateFinInvalidite())) {
                                    key.tauxInvalidite = periodeInv.getDegreInvalidite();
                                }

                            }

                            // 1. Traitement des groupesRente

                            // si la cl� est encore inexistante
                            if (!groupeRenteHash.containsKey(key)) {

                                // On cr�e un objet
                                REGroupeRente grpRente = new REGroupeRente();

                                grpRente.moisAnneeDebut = prestDues.getDateDebutPaiement();
                                grpRente.moisAnneeFin = prestDues.getDateFinPaiement();
                                grpRente.montantAnnuel.add(prestDues.getMontant());
                                grpRente.idTiers = prestDues.getIdTiersBeneficiaire();

                                grpRente.genreRente = prestDues.getCodePrestation();

                                PRTiersWrapper tier = PRTiersHelper.getTiersParId(session, grpRente.idTiers);
                                String nom = "";
                                String prenom = "";

                                if (tier != null) {
                                    nom = tier.getProperty(PRTiersWrapper.PROPERTY_NOM);
                                    prenom = tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                }

                                grpRente.nom = nom;
                                grpRente.prenom = prenom;

                                // Comme la cl� est inexistante, on cr�e la
                                // liste d'objet
                                List<REGroupeRente> list = new ArrayList<REGroupeRente>();
                                list.add(grpRente);

                                // On ins�re la cl� et la liste de grpRente dans
                                // la map
                                groupeRenteHash.put(key, list);

                                // si la cl� existe d�j�
                            } else {

                                // On r�cup�re la liste
                                List<REGroupeRente> list = groupeRenteHash.get(key);

                                // On cr�e un objet
                                REGroupeRente grpRente = new REGroupeRente();

                                grpRente.moisAnneeDebut = prestDues.getDateDebutPaiement();
                                grpRente.moisAnneeFin = prestDues.getDateFinPaiement();
                                grpRente.montantAnnuel.add(prestDues.getMontant());
                                grpRente.idTiers = prestDues.getIdTiersBeneficiaire();

                                grpRente.genreRente = prestDues.getCodePrestation();

                                PRTiersWrapper tier = PRTiersHelper.getTiersParId(session, grpRente.idTiers);
                                String nom = "";
                                String prenom = "";

                                if (tier != null) {
                                    nom = tier.getProperty(PRTiersWrapper.PROPERTY_NOM);
                                    prenom = tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                }

                                grpRente.nom = nom;
                                grpRente.prenom = prenom;

                                // On ins�re l'objet dans la liste
                                list.add(grpRente);

                            }

                        }
                    } else {
                        throw new Exception(session.getLabel("ERREUR_IDTIERS_BENEFICIAIRE")
                                + prestDues.getIdPrestationDue());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.addError(e.getMessage());
        }
    }
}
