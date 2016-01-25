package globaz.aquila.db.rdp.cashin;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.db.rdp.cashin.model.Creance;
import globaz.aquila.db.rdp.cashin.model.Litige;
import globaz.aquila.db.rdp.cashin.model.Personne;
import globaz.aquila.db.rdp.cashin.model.RequisitionPoursuite;
import globaz.aquila.db.rdp.cashin.persistence.EcritureRDP;
import globaz.aquila.db.rdp.cashin.persistence.EcritureRDPManager;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.naos.api.IAFAssurance;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CASection;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Montant;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;
import com.sun.star.lang.NullPointerException;

public class RequisitionPoursuiteCashinBuilder {
    private BTransaction transaction;

    public RequisitionPoursuiteCashinBuilder(BTransaction transaction) {
        this.transaction = transaction;
    }

    private static String CASHIN_CODE_AUTRES_TACHES = "cashin.code.autrestaches";

    private static String MOTIF_FIN_LIQUIDATION = "803030";

    /**
     * Mode définissant de séparer uniquement l'AVS et de fusionner toutes les opérations dans "autres tâches".
     * On retrouvera donc deux litiges avec une créance associée à chacune d'elle :
     */
    private static final int MODE_AUTRES_TACHES = 1;

    /**
     * Mode définissant de séparer l'AVS et de séparer toutes les opérations dans "autres tâches".
     * On retrouvera donc deux litiges avec plusieurs créances dans "autres tâches".
     */
    private static final int MODE_SEPARE = 2;

    private static enum TypeDossier {
        TYPE_AVS("AVS"),
        TYPE_AUTRES_TACHES("Autres tâches");

        private String value;

        private TypeDossier(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Construit la réquisition de poursuite destinée à être exportée dans un fichier Cash-In
     */
    public RequisitionPoursuite build(CASection section, String idTiers, String numAffilie, String user, String date,
            String motif, BTransaction transaction) throws Exception {

        Personne personne = buildPersonne(idTiers, numAffilie, date, transaction);
        List<Litige> litiges = buildLitiges(section, idTiers, numAffilie, user, date, motif, transaction);

        RequisitionPoursuite requisitionPoursuite = new RequisitionPoursuite(personne, litiges);

        return requisitionPoursuite;

    }

    private Personne buildPersonne(String idTiers, String numAffilie, String date, BTransaction transaction)
            throws Exception {

        TITiersViewBean tiers = retrievePersonneAvs(idTiers, transaction);
        TIAvoirAdresse avoirAdresse = TITiers.getAvoirAdresse(null, IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                IConstantes.CS_APPLICATION_DEFAUT, date, idTiers, transaction.getSession());
        if (avoirAdresse == null) {
            throw new NullPointerException("Aucune adresse de domicile trouvée !");
        }
        TIAdressePaiementData adressePaiement = TITiers.getAdressePaiementData(IConstantes.CS_APPLICATION_DEFAUT, date,
                idTiers, null, transaction.getSession());
        if (adressePaiement == null) {
            throw new NullPointerException("Aucune adresse de paiement trouvée !");
        }
        TILocalite localite = retrieveLocalite(transaction, avoirAdresse.getIdLocalite());
        TIPays pays = retrievePays(transaction, localite.getIdPays());
        AFAffiliation affiliation = retrieveAffilie(numAffilie);

        Personne personne = new Personne();

        personne.setNumExterne(numAffilie);
        personne.setNom((tiers.getDesignation1()));
        personne.setPrenom(tiers.getDesignation2());
        personne.setAutreNom("");

        FWParametersUserCode titreCS = getCodeSysteme(tiers.getTitreTiers(), transaction.getSession());
        String libelleTitre = titreCS.isNew() ? tiers.getTitreTiers() : titreCS.getLibelle();
        personne.setTitre(libelleTitre);

        FWParametersUserCode langueCS = getCodeSysteme(tiers.getLangue(), transaction.getSession());
        String codeLangue = langueCS.isNew() ? "???" : langueCS.getCodeUtilisateur();
        personne.setLangue(codeLangue);

        personne.setProfession("");
        personne.setCritereRechercheLibre("");
        personne.setNumeroCompte(adressePaiement.getCompte());
        personne.setNaissance(tiers.getDateNaissance());
        personne.setRue(avoirAdresse.getRue() + " " + avoirAdresse.getNumeroRue());
        personne.setChez("");

        if ("CH".equals(pays.getCodeIso())) {
            personne.setNpa(localite.getNumPostal().substring(0, 4));
        } else {
            personne.setNpa(localite.getNumPostal());
        }

        personne.setLocalite(localite.getLocalite());
        personne.setLieuDit("");
        personne.setDescTelephone("");
        personne.setNumTelephone("");
        personne.setPays(pays.getCodeIso());
        personne.setOrigMutAdresse("");
        personne.setStatut("");

        FWParametersUserCode sexeCS = getCodeSysteme(tiers.getSexe(), transaction.getSession());
        String codeSexe = sexeCS.isNew() ? "" : sexeCS.getCodeUtilisateur();
        personne.setSexe(codeSexe);
        personne.setStatutLiquidation(getCodemotifFin(affiliation.getMotifFin()));
        personne.setNss(tiers.getNumAvsActuel());
        personne.setPersonneMorale(tiers.getPersonneMorale() ? "1" : "0");
        personne.setNumeroEnregistrementRc("");
        personne.setSiegeSocial("");
        personne.setNotesPersonne("");
        personne.setDateFinRelation(affiliation.getDateFin());

        return personne;

    }

    private List<Litige> buildLitiges(CASection section, String idTiers, String numAffilie, String user, String date,
            String motif, BTransaction transaction) throws Exception {
        int mode = Integer.valueOf(transaction.getSession().getApplication().getProperty("cashin.mode"));

        List<Litige> litiges;

        Map<TypeDossier, Collection<EcritureRDP>> ecrituresBySecteur = findAndGroupEcrituresByTypeDossier(section
                .getIdSection());
        if (MODE_AUTRES_TACHES == mode) {
            litiges = createCreancesForModeAutresTaches(numAffilie, motif, section, user, date, ecrituresBySecteur);
        } else if (MODE_SEPARE == mode) {
            litiges = createCreancesForModeSepare(numAffilie, motif, section, user, date, ecrituresBySecteur);
        } else {
            throw new IllegalStateException("Le mode " + mode + " n'est pas valide. Veuillez vérifier vos propriétés");
        }
        return litiges;
    }

    private Litige createLitige(CASection section, String numAffilie, String codeAdministrationCaisse, String user,
            String date) {
        Litige litige = new Litige();
        litige.setNumExterneDossier(removeDotAndDash(numAffilie) + "-" + codeAdministrationCaisse);
        litige.setNumExterneDebiteur(numAffilie);
        litige.setNumeroExterneLitige(section.getIdExterne() + "-" + section.getTypeSection().getId());
        litige.setCodeGestionnaire(user);
        litige.setDateCreationLitige(date);
        return litige;
    }

    private String removeDotAndDash(String value) {
        return value.replaceAll("[\\-\\.]", "");
    }

    private List<Litige> createCreancesForModeAutresTaches(String numAffilie, String motif, CASection section,
            String user, String date, Map<TypeDossier, Collection<EcritureRDP>> ecrituresBySecteur) throws Exception {
        List<Litige> litiges = new ArrayList<Litige>();
        int increment = 1;
        for (Map.Entry<TypeDossier, Collection<EcritureRDP>> entry : ecrituresBySecteur.entrySet()) {
            TypeDossier typeDossier = entry.getKey();
            Montant montant = Montant.ZERO;
            String noSection = null;
            for (EcritureRDP ecriture : entry.getValue()) {
                montant = montant.add(new Montant(ecriture.getMontant()));
                noSection = ecriture.getNoSection();
            }
            String codeAdministrationCaisse = getCodeAdministrationForTypeDossier(numAffilie, typeDossier);
            if (codeAdministrationCaisse == null) {
                codeAdministrationCaisse = getCodeAdministrationAutresTaches();
            }
            Creance creance = createCreance(codeAdministrationCaisse, motif, typeDossier.getValue(), montant,
                    noSection, increment);

            Litige litige = createLitige(section, numAffilie, codeAdministrationCaisse, user, date);
            litige.addCreance(creance);
            litiges.add(litige);
            increment++;
        }
        return litiges;
    }

    private List<Litige> createCreancesForModeSepare(String numAffilie, String motif, CASection section, String user,
            String date, Map<TypeDossier, Collection<EcritureRDP>> ecrituresBySecteur) throws Exception {
        List<Litige> litiges = new ArrayList<Litige>();
        for (Map.Entry<TypeDossier, Collection<EcritureRDP>> entry : ecrituresBySecteur.entrySet()) {
            TypeDossier typeDossier = entry.getKey();
            int increment = 1;
            for (EcritureRDP ecriture : entry.getValue()) {
                String codeAdministrationCaisse = getCodeAdministrationForTypeDossier(numAffilie, typeDossier);
                if (codeAdministrationCaisse == null) {
                    codeAdministrationCaisse = getCodeAdministrationAutresTaches();
                }
                Creance creance = createCreance(codeAdministrationCaisse, motif, typeDossier.getValue(), new Montant(
                        ecriture.getMontant()), ecriture.getNoSection(), increment);
                Litige litige = createLitige(section, numAffilie, codeAdministrationCaisse, user, date);
                litige.addCreance(creance);
                increment++;
                litiges.add(litige);
            }
        }
        return litiges;
    }

    /**
     * Retourne le code administration correspondant aux autres tâches si la caisse AF n'existe pas.
     * 
     * @return String représentant un code administration
     * @throws IllegalStateException Si la propriété n'est pas existante en base de données
     */
    private String getCodeAdministrationAutresTaches() {
        try {
            String code = GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA).getProperty(
                    CASHIN_CODE_AUTRES_TACHES);
            if (code == null) {
                throw new IllegalStateException("La propriété " + CASHIN_CODE_AUTRES_TACHES + " n'est pas définie");
            }
            return code;
        } catch (Exception e) {
            throw new IllegalStateException("La propriété " + CASHIN_CODE_AUTRES_TACHES + " n'est pas définie");
        }
    }

    private String getCodeAdministrationCaisseAF(String numAffilie) throws Exception {
        return getCodeAdministrationCaisse(numAffilie, IAFAssurance.TYPE_ASS_COTISATION_AF);
    }

    private String getCodeAdministrationCaisseAVS(String numAffilie) throws Exception {
        return getCodeAdministrationCaisse(numAffilie, IAFAssurance.TYPE_ASS_COTISATION_AVS_AI);
    }

    /**
     * Retourne le code de l'administration du plan caisse pour l'affilié et un type de dossier (AVS, Autres tâches).
     * 
     * @param numAffilie Numéro d'affilié pour lequel rechercher le code administration correspondant au plan caisse
     * @param typeDossier Type de dossier (AVS ou Autres tâches)
     * @return Code administration du plan caisse
     * @throws Exception inconnue
     */
    private String getCodeAdministrationForTypeDossier(String numAffilie, TypeDossier typeDossier) throws Exception {
        if (TypeDossier.TYPE_AVS.equals(typeDossier)) {
            return getCodeAdministrationCaisseAVS(numAffilie);
        } else {
            return getCodeAdministrationCaisseAF(numAffilie);
        }
    }

    /**
     * Recherche du code de l'administration du plan caisse pour un affilié et un type de cotisation.
     * 
     * @param numAffilie Numéro d'affilié
     * @param typeCotisation Type de cotisation à rechercher
     * @return Code de l'administration correspondant au plan caisse
     * @throws IllegalStateException Si l'affilié ne dispose pas de ce type de cotisation
     * @throws Exception inconnu
     */
    private String getCodeAdministrationCaisse(String numAffilie, String typeCotisation) throws Exception {
        AFAffiliation affiliation = retrieveAffilie(numAffilie);
        AFCotisationManager manager = new AFCotisationManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(affiliation.getAffiliationId());
        manager.find();

        for (Object o : manager) {
            AFCotisation cotisation = (AFCotisation) o;
            if (typeCotisation.equals(cotisation.getAssurance().getTypeAssurance())) {
                return cotisation.getAdhesion().getAdministrationCaisseCode();
            }
        }
        return null;
    }

    private static Creance createCreance(String codeAdministration, String motif, String titreCreance, Montant montant,
            String noSection, int increment) {
        Creance creance = new Creance();
        creance.setFraisSommation("0.00");
        creance.setTitreCreance(titreCreance);
        creance.setNumeroExterneCreance(noSection + "-" + increment);
        creance.setMontant(montant.getValue());
        creance.setTauxInteret("0.00");
        creance.setDateDebutInteret("");
        creance.setCommentaire(motif);
        creance.setNumeroExterneMandataire(codeAdministration);
        creance.setNumeroExterneAgent("");
        creance.setNumeroExterneSociete("");
        creance.setNumerosAutresCreanciers("");
        creance.setCreancePartagee("");
        creance.setCodeUtilisateur("");
        creance.setModeImportation("1");
        creance.setOption1ConcDebiteur("");
        creance.setOption2ConcDebiteur("");
        creance.setOption3ConcDebiteur("");
        creance.setOption4ConcDebiteur("");
        creance.setOption5ConcDebiteur("");
        creance.setCodePremiereEtape("");
        creance.setVentilationComptable("");
        creance.setRepartitionCreance("");
        creance.setDatePremiereEtape("");
        return creance;
    }

    private static String getCodemotifFin(String codeMotifFin) {

        if (CodeSystem.MOTIF_FIN_DECES.equals(codeMotifFin)) {
            return "1";
        } else if (MOTIF_FIN_LIQUIDATION.equals(codeMotifFin)) {
            return "0";
        } else {
            return "";
        }
    }

    /**
     * Charge les écritures pour la section dont l'id est passé en paramètre.
     * 
     * @param idSection Id de la section pour laquelle rechercher les écritures
     * @return Map contenant comme clé le type dossier (AVS ou Autres tâches) et les écrtures sous forme de collection
     * @throws Exception inconnu
     */
    private Map<TypeDossier, Collection<EcritureRDP>> findAndGroupEcrituresByTypeDossier(String idSection)
            throws Exception {
        Collection<EcritureRDP> ecritures = loadEcritures(idSection);
        return groupEcrituresByTypeDossier(ecritures);
    }

    /**
     * Groupe les écritures selon son type (AVS ou Autres tâches).
     * 
     * @param ecritures Ecritures à grouper
     * @return Map contenant comme clé le type dossier (AVS ou Autres tâches) et les écritures sous forme de collection
     */
    private static Map<TypeDossier, Collection<EcritureRDP>> groupEcrituresByTypeDossier(
            Collection<EcritureRDP> ecritures) {
        return Multimaps.index(ecritures, new Function<EcritureRDP, TypeDossier>() {
            @Override
            public TypeDossier apply(EcritureRDP ecriture) {
                String secteur = ecriture.getNoRubriqueCompteCourant().substring(0, 1);
                if (EcritureRDP.SECTEURS_AVS.contains(secteur)) {
                    return TypeDossier.TYPE_AVS;
                } else {
                    return TypeDossier.TYPE_AUTRES_TACHES;
                }
            }
        }).asMap();
    }

    /**
     * Recherche les écritures comptables à exporter selon l'id section passé en paramètre.
     * 
     * @param idSection L'id de la section sur laquelle rechercher les écritures
     * @return Manager Classe possédant les résultats de la recherche
     * @throws Exception générique inconnue
     */
    private Collection<EcritureRDP> loadEcritures(String idSection) throws Exception {
        List<EcritureRDP> ecritures = new ArrayList<EcritureRDP>();
        EcritureRDPManager manager = new EcritureRDPManager();
        manager.setSession(transaction.getSession());
        manager.setForIdSection(idSection);
        manager.find(transaction);

        for (Object o : manager) {
            ecritures.add((EcritureRDP) o);
        }

        return ecritures;
    }

    private static TITiersViewBean retrievePersonneAvs(String idTiers, BTransaction transaction) throws Exception {
        TIPersonneAvsManager personneManager = new TIPersonneAvsManager();
        personneManager.setSession(transaction.getSession());
        personneManager.setForIdTiers(idTiers);
        personneManager.find(transaction);

        if (personneManager.size() == 1) {

            TITiersViewBean tiers = (TITiersViewBean) personneManager.get(0);

            return tiers;
        }

        throw new Exception("Le tiers correspondant à l'id " + idTiers + " n'a pas été trouvé");
    }

    private AFAffiliation retrieveAffilie(String numAffilie) throws Exception {
        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(getSession());
        affiliationManager.setForAffilieNumero(numAffilie);
        affiliationManager.find();

        if (affiliationManager.size() == 1) {
            return (AFAffiliation) affiliationManager.get(0);
        }

        throw new Exception("L'affiliation correspondant au numéro " + numAffilie + " n'a pas été trouvée");
    }

    private static TIPays retrievePays(BTransaction transaction, String idPays) throws Exception {
        TIPays pays = new TIPays();
        pays.setSession(transaction.getSession());
        pays.setIdPays(idPays);
        pays.retrieve(transaction);
        return pays;
    }

    private static TILocalite retrieveLocalite(BTransaction transaction, String idLocalite) throws Exception {
        TILocalite localite = new TILocalite();
        localite.setSession(transaction.getSession());
        localite.setIdLocalite(idLocalite);
        localite.retrieve(transaction);
        return localite;
    }

    private static FWParametersUserCode getCodeSysteme(String codeSys, BSession session) throws Exception {

        FWParametersUserCode cs = new FWParametersUserCode();
        cs.setSession(session);
        cs.setIdCodeSysteme(codeSys);
        cs.setIdLangue("F");
        cs.retrieve();

        return cs;
    }

    private BSession getSession() {
        return transaction.getSession();
    }
}
