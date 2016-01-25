package globaz.osiris.db.ventilation;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APIVPDetailMontant;
import globaz.osiris.api.APIVPPoste;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.comptes.CATauxRubriquesManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ald Créé le 15 nov. 05
 * @modified by sel le 10.12.2009 : possibiliter de ventiler par section séparemment ou sur la globalité des sections.
 */
public class CAVPVentilateur {
    public static final String KEY_POSTE_A_VENTILER = "XXXXXXXX";
    public static final String KEY_POSTE_TOTAL = "Total     ";

    /** Liste Excel : Stock la description et le numéro de la section */
    private Map<String, String> infoSectionMap;

    /** Liste Excel : Contient le détail des écritures pour chaque sections */
    private Map<String, ArrayList<CAVPDetailSection>> listDetailSectionMap;// key=dateSection+idSection

    /** Liste Excel : Contient le total du montant à ventiler pour chaque section. */
    private HashMap<String, BigDecimal> listMontantBaseVentilationSectionMap;

    /** Liste Excel : Contient la liste des rubriques à afficher pour chaque sections */
    private HashMap<String, List<CAVPDetailRubriqueSection>> listRubriqueDetailSectionMap;// key=dateSection+idSection

    /** Liste Excel : Montant de la part pénale */
    private BigDecimal partPenal;

    /** Pour sortir un récap aligné, on est obligé de connaître toutes les rubriques présentes */
    private HashMap<String, Boolean> rubriquesPresentesMap = null; // Key=idRubrique, true si montant simple
    private BSession session = null;
    private TreeMap<String, CAVPListePosteParSection> tableauFinal;// key=dateSection+idSection
    private String typeDeProcedure;

    /**
     * @param idRubrique
     * @param session
     * @param typeprocedure
     * @return
     */
    public static String getRubriqueOrdre(String idRubrique, BSession session, String typeprocedure) {
        String rubOrdre = "0";
        try {
            CAVPTypeDeProcedureOrdreManager ordreProceduresMgr = new CAVPTypeDeProcedureOrdreManager();
            ordreProceduresMgr.setSession(session);
            ordreProceduresMgr.setForTypeProcedure(typeprocedure);
            ordreProceduresMgr.setForIdRubrique(idRubrique);
            ordreProceduresMgr.changeManagerSize(1);
            ordreProceduresMgr.find();

            if (ordreProceduresMgr.size() > 0) {
                CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) ordreProceduresMgr.get(0);
                rubOrdre = ordre.getRubriqueOrdreColonne();
            }

        } catch (Exception e) {
            e.printStackTrace(); // TODO sel gérer Exception
        }

        return rubOrdre;
    }

    private static boolean isMontantNegatif(BigDecimal montant) {
        if (montant.signum() == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Constructeur par défaut initialisant les variables.
     */
    public CAVPVentilateur() {
        super();

        tableauFinal = new TreeMap<String, CAVPListePosteParSection>();
        rubriquesPresentesMap = new HashMap<String, Boolean>();
        infoSectionMap = new HashMap<String, String>();
        listDetailSectionMap = new HashMap<String, ArrayList<CAVPDetailSection>>();
        listRubriqueDetailSectionMap = new HashMap<String, List<CAVPDetailRubriqueSection>>();
        listMontantBaseVentilationSectionMap = new HashMap<String, BigDecimal>();
        partPenal = new BigDecimal("0");
        typeDeProcedure = APIVPPoste.CS_PROCEDURE_PLAINTE_PENALE;
    }

    /**
     * Crée et ajoute detailSection dans la liste tabDetailSection
     * 
     * @param tabDetailSection
     * @param ecritureCourante
     * @param rubrique
     * @param detailSection
     * @param partEmployeur
     *            ou montant pour montant simple
     * @param partSalarie
     */
    private void addDetailSection(ArrayList<CAVPDetailSection> tabDetailSection, CAEcriture ecritureCourante,
            BigDecimal partEmployeur, BigDecimal partSalarie, boolean montantSimple) {
        CAVPDetailSection detailSection = new CAVPDetailSection();
        // Collecte les écritures utilisées pour la ventilation afin de les afficher
        // dans des onglets de détails. Cas double Employeur/Salarié
        if (montantSimple) {
            detailSection.setMontant(partEmployeur);
        } else {
            detailSection.setMontantEmployeur(partEmployeur);
            detailSection.setMontantSalarie(partSalarie);
        }
        detailSection.setMontantSimple(montantSimple);
        detailSection.setIdRubrique(ecritureCourante.getCompte().getIdRubrique());
        detailSection.setDate(ecritureCourante.getDate());
        detailSection.setLibelle(ecritureCourante.getLibelleDescription());
        tabDetailSection.add(detailSection);
    }

    /**
     * @param listePosteParSec
     * @param totalParRubrique
     * @param keyRub
     * @return
     */
    private BigDecimal additionnerMontantSimple(CAVPListePosteParSection listePosteParSec, BigDecimal totalParRubrique,
            String keyRub) {
        BigDecimal montantSimple = new BigDecimal("0");
        montantSimple = listePosteParSec
                .getPoste(keyRub)
                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE)
                .getMontantBase()
                .subtract(
                        listePosteParSec.getPoste(keyRub).getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE)
                                .getMontantVentile());
        // TODO : A CORRIGER
        // Le total pour la ventilation n'est pas correct, on met à zero
        // En attente de vérification. Pour l'instant le total est calculé via
        // une formule excel
        if (!CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(keyRub)) {
            totalParRubrique = totalParRubrique.add(montantSimple);
        }
        return totalParRubrique;
    }

    /**
     * Ajoute la liste d'écriture courante dans la section.
     * 
     * @param String
     *            key : clé (id rubrique ou KEY_POSTE_A_VENTILER)
     * @param CAVPPoste
     *            poste
     * @param CASection
     *            section : section actuelle
     * @param CAVPDetailMontant
     *            montant
     */
    public void addPoste(String key, CAVPPoste poste, CASection section) {
        CAVPListePosteParSection listePostesParSec = new CAVPListePosteParSection();
        try {
            if (tableauFinal.containsKey(getKey(section))) {
                listePostesParSec = tableauFinal.get(getKey(section));
                listePostesParSec.addPosteDansListeParSection(key, poste);
            } else {
                listePostesParSec.addPosteDansListeParSection(key, poste);
            }

            tableauFinal.put(getKey(section), listePostesParSec);
            if (!rubriquesPresentesMap.containsKey(key)) {
                rubriquesPresentesMap.put(key, new Boolean(poste.isMontantSimple()));
            }
        } catch (JAException e) {
            e.printStackTrace(); // TODO sel gérer Exception
        }
    }

    /**
     * Ajoute la liste d'écriture courante avec année de cotisation dans la section.
     * 
     * @param String
     *            key : clé (id rubrique ou KEY_POSTE_A_VENTILER)
     * @param CAVPPoste
     *            poste
     * @param CASection
     *            section : section actuelle
     * @param String
     *            annee
     */
    public void addPoste(String key, CAVPPoste poste, CASection section, String annee) {
        CAVPListePosteParSection listePostesParSec = new CAVPListePosteParSection();
        try {
            if (tableauFinal.containsKey(getKey(section))) {
                listePostesParSec = tableauFinal.get(getKey(section));
            }
            listePostesParSec.addPosteDansListeParSection(key, poste, annee);

            tableauFinal.put(getKey(section), listePostesParSec);
            if (!rubriquesPresentesMap.containsKey(key)) {
                rubriquesPresentesMap.put(key, new Boolean(poste.isMontantSimple()));
            }
        } catch (JAException e) {
            e.printStackTrace(); // TODO sel gérer Exception
        }
    }

    /**
     * Ajoute la liste d'écriture courante dans la liste à ventiler.
     * 
     * @param poste
     * @param section
     */
    public void addPosteAVentiler(CAVPPoste poste, CASection section) {
        CAVPListePosteParSection listePostesParSec = new CAVPListePosteParSection();
        try {
            if (tableauFinal.containsKey(getKey(section))) {
                listePostesParSec = tableauFinal.get(getKey(section));
            }
            listePostesParSec.addPosteDansListeParSection(CAVPVentilateur.KEY_POSTE_A_VENTILER, poste);

            tableauFinal.put(getKey(section), listePostesParSec);
        } catch (JAException e) {
            e.printStackTrace(); // TODO sel gérer Exception
        }
    }

    /**
     * Ajoute la liste d'écriture courante dans la liste des totaux.
     * 
     * @param String
     *            key : représente la clé (rubrique)
     * @param CAVPPoste
     *            poste
     * @param String
     *            section : type de section (dans ce cas : KEY_POSTE_TOTAL)
     * @param CAVPDetailMontant
     *            montant
     */
    public void addPosteTotal(String key, CAVPPoste poste, CAVPDetailMontant montant) {
        CAVPListePosteParSection listePostesParSec = new CAVPListePosteParSection();
        // vérifie si déjà présent dans la liste
        if (tableauFinal.containsKey(CAVPVentilateur.KEY_POSTE_TOTAL)) {
            listePostesParSec = tableauFinal.get(CAVPVentilateur.KEY_POSTE_TOTAL);
            listePostesParSec.addPosteDansListeParSection(key, poste);
        } else {
            listePostesParSec.addPosteDansListeParSection(key, poste);
        }
        // ajoute dans le tableau final
        tableauFinal.put(CAVPVentilateur.KEY_POSTE_TOTAL, listePostesParSec);
        if (!rubriquesPresentesMap.containsKey(key)) {
            rubriquesPresentesMap.put(key, new Boolean(poste.isMontantSimple()));
        }
    }

    /**
     * Crée et ajoute detailRubriqueSection dans la liste tabRubriqueDetailSection
     * 
     * @param tabRubriqueDetailSection
     * @param rubrique
     * @param detailRubriqueSection
     */
    private void addRubriqueDetailSection(ArrayList<CAVPDetailRubriqueSection> tabRubriqueDetailSection,
            CARubrique rubrique, boolean montantSimple) {
        CAVPDetailRubriqueSection detailRubriqueSection = new CAVPDetailRubriqueSection();
        // Collecte les rubriques employées pour la ventilation et vérifie si la rubrique n'existe pas déjà dans le
        // conteneur.
        if (!checkIfIdRubriqueIsInList(tabRubriqueDetailSection, rubrique.getIdRubrique())) {
            detailRubriqueSection.setIdRubrique(rubrique.getIdRubrique());
            detailRubriqueSection.setOrdre(CAVPVentilateur.getRubriqueOrdre(rubrique.getIdRubrique(), getSession(),
                    getTypeDeProcedure()));
            detailRubriqueSection.setMontantSimple(montantSimple);
            tabRubriqueDetailSection.add(detailRubriqueSection);
        }
    }

    /**
     * Vérifie si la rubrique est déjà présente dans la structure ou non
     * 
     * @param list
     * @param idRubrique
     * @return
     */
    private boolean checkIfIdRubriqueIsInList(ArrayList<CAVPDetailRubriqueSection> list, String idRubrique) {
        for (CAVPDetailRubriqueSection detailRubriqueSection : list) {
            if (detailRubriqueSection.getIdRubrique().equals(idRubrique)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crée un poste vide pour la section et l'ajoute à la liste.
     * 
     * @param section
     * @param tabDetailSection
     * @param tabRubriqueDetailSection
     * @param ecritureCourante
     * @param rubrique
     * @param detailSection
     * @param detailRubriqueSection
     */
    private void createPosteAVentiler(CASection section) {
        // pas de taux donc montant simple
        CAVPPoste posteSimple = new CAVPPoste();
        CAVPDetailMontant detailMontant = new CAVPDetailMontant();
        detailMontant.addMontant(new BigDecimal("0"), "0");
        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);
        posteSimple.addDetailMontant(detailMontant);
        posteSimple.setMontantSimple(true);
        posteSimple.setIdSection(section.getIdSection());

        this.addPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER, posteSimple, section, "0");
    }

    /**
     * gère la part pénal
     * 
     * @param totalParRubrique
     * @param totalParRubriqueEmpl
     * @param totalParRubriqueSal
     * @param keyRub
     */
    private void determinerPartPenale(BigDecimal totalParRubrique, BigDecimal totalParRubriqueEmpl,
            BigDecimal totalParRubriqueSal, String keyRub) {
        if (rubriquesPresentesMap.get(keyRub).booleanValue()) {
            CAVPPoste posteSimple = new CAVPPoste();
            CAVPDetailMontant detailMontant = new CAVPDetailMontant();
            detailMontant.addMontant(totalParRubrique);
            detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);
            posteSimple.addDetailMontant(detailMontant);
            posteSimple.setMontantSimple(true);

            addPosteTotal(keyRub, posteSimple, detailMontant);

            if (isPenal(keyRub, APIVPDetailMontant.CS_VP_MONTANT_SIMPLE)) {
                partPenal = partPenal.add(totalParRubrique);
            }
        } else {
            CAVPPoste poste = new CAVPPoste();
            CAVPDetailMontant detailMontant = new CAVPDetailMontant();
            detailMontant.addMontant(totalParRubriqueEmpl);
            detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);
            poste.addDetailMontant(detailMontant);

            if (isPenal(keyRub, APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)) {
                partPenal = partPenal.add(totalParRubriqueEmpl);
            }

            CAVPDetailMontant detailMontantSal = new CAVPDetailMontant();
            detailMontantSal.addMontant(totalParRubriqueSal);
            detailMontantSal.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);
            poste.addDetailMontant(detailMontantSal);
            poste.setMontantSimple(false);

            addPosteTotal(keyRub, poste, detailMontant);
            if (isPenal(keyRub, APIVPDetailMontant.CS_VP_MONTANT_SALARIE)) {
                partPenal = partPenal.add(totalParRubriqueSal);
            }
        }
    }

    /**
     * Initialise le manager et execute le find.
     * 
     * @param section
     * @return CAEcritureManager les écritures (E%) comptabilisées de la section.
     * @throws Exception
     */
    private CAEcritureManager findEcrituresOfSection(CASection section) throws Exception {
        CAEcritureManager listeEcritures = new CAEcritureManager();
        listeEcritures.setSession(section.getSession());
        listeEcritures.setForIdSection(section.getIdSection());
        listeEcritures.setVueOperationCaSe(new Boolean(true)); // jointure entre CASECTP et CAOPERP
        listeEcritures.setForEtat(APIOperation.ETAT_COMPTABILISE);
        listeEcritures.find(BManager.SIZE_NOLIMIT);
        return listeEcritures;
    }

    /**
     * Execute la requete et retourne les ordres correspondant au numéro.
     * 
     * @param ordre
     *            numéro d'ordre
     * @return le manager pour le numéro d'ordre
     * @throws Exception
     */
    private CAVPTypeDeProcedureOrdreManager findOrdreProcedures(int ordre) throws Exception {
        CAVPTypeDeProcedureOrdreManager ordreProceduresMgr = new CAVPTypeDeProcedureOrdreManager();
        ordreProceduresMgr.setSession(getSession());
        ordreProceduresMgr.setForOrdre(String.valueOf(ordre));
        ordreProceduresMgr.setForTypeProcedure(getTypeDeProcedure());
        ordreProceduresMgr.find();
        return ordreProceduresMgr;
    }

    /**
     * @return the infoSection
     */
    public Map<String, String> getInfoSectionMap() {
        return infoSectionMap;
    }

    /**
     * @param section
     * @return key au format YYYYMMDDidSection afin de trier les sections par date de création.
     * @throws JAException
     */
    private String getKey(CASection section) throws JAException {
        return JACalendar.format(new JADate(section.getDateSection()), JACalendar.FORMAT_YYYYMMDD)
                + section.getIdSection();
    }

    /**
     * @return the listDetailSection
     */
    public Map<String, ArrayList<CAVPDetailSection>> getListDetailSectionMap() {
        return listDetailSectionMap;
    }

    /**
     * @return the listMontantBaseVentilationSection
     */
    public HashMap<String, BigDecimal> getListMontantBaseVentilationSectionMap() {
        return listMontantBaseVentilationSectionMap;
    }

    /**
     * @return the listRubriqueDetailSection
     */
    public HashMap<String, List<CAVPDetailRubriqueSection>> getListRubriqueDetailSectionMap() {
        return listRubriqueDetailSectionMap;
    }

    /**
     * @param montant
     * @param taux
     * @param tauxTotal
     * @return
     */
    private BigDecimal getMontant(String montant, String taux, BigDecimal tauxTotal) {
        BigDecimal montantTot = new BigDecimal(montant);
        montantTot = montantTot.setScale(5);
        BigDecimal tauxBig = new BigDecimal(taux);
        tauxBig = tauxBig.setScale(5);
        return JANumberFormatter.round(montantTot.multiply(tauxBig.divide(tauxTotal, 5)), 0.05, 2,
                JANumberFormatter.NEAR);
    }

    /**
     * @param ecritureCourante
     * @param taux
     * @param tauxTotal
     * @param partSalarie
     * @return
     */
    private BigDecimal getPartEmployeur(CAEcriture ecritureCourante, CATauxRubriques taux, BigDecimal tauxTotal,
            BigDecimal partSalarie) {
        BigDecimal partEmployeur = getMontant(ecritureCourante.getMontant(), taux.getTauxEmployeur(), tauxTotal);
        BigDecimal montantEcriture = new BigDecimal(ecritureCourante.getMontant());
        montantEcriture = montantEcriture.subtract(partSalarie);
        if (montantEcriture.compareTo(partEmployeur) != 0) {
            partEmployeur = montantEcriture;
        }

        if (partEmployeur.signum() != 0) {
            return partEmployeur;
        } else {
            return new BigDecimal("0");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPListePostes#getPartPenal()
     */
    public BigDecimal getPartPenal() {
        return partPenal;
    }

    /**
     * @param ecritureCourante
     * @param detailSection
     * @param taux
     * @param tauxTotal
     * @return
     */
    private BigDecimal getPartSalarie(CAEcriture ecritureCourante, CATauxRubriques taux, BigDecimal tauxTotal) {
        BigDecimal partSalarie = getMontant(ecritureCourante.getMontant(), taux.getTauxSalarie(), tauxTotal);
        if (partSalarie.signum() != 0) {
            return partSalarie;
        } else {
            return new BigDecimal("0");
        }
    }

    /**
     * contient la liste des RUBRIQUES presentes
     * 
     * @return la liste des RUBRIQUES presentes
     */
    public HashMap<String, Boolean> getRubriquesPresentesMap() {
        return rubriquesPresentesMap;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPListePostes#getTableauFinal()
     */
    public TreeMap<String, CAVPListePosteParSection> getTableauFinal() {
        return tableauFinal;
    }

    /**
     * @param ecritureCourante
     * @param rubrique
     * @return
     */
    private CATauxRubriques getTaux(CAEcriture ecritureCourante, CARubrique rubrique) {
        CATauxRubriques taux = new CATauxRubriques();
        if (!JadeStringUtil.isIntegerEmpty(ecritureCourante.getAnneeCotisation())) {
            taux = rubrique.getTauxRubriques("31.12." + ecritureCourante.getAnneeCotisation(), ecritureCourante
                    .getSection().getIdCaisseProfessionnelle(),
                    CATauxRubriquesManager.ORDER_BY_CAISSE_PROF_ASC_DATE_DESC);
        } else {
            taux = null;
        }
        return taux;
    }

    /**
     * @param tauxSalarie
     * @param tauxEmployeur
     * @return
     */
    private BigDecimal getTauxTotal(BigDecimal tauxSalarie, BigDecimal tauxEmployeur) {
        BigDecimal tauxTotal = new BigDecimal("0.00000");
        tauxTotal = tauxTotal.setScale(5);
        // on additionne les 2 taux pour trouver la part de chacun (par ex 50/50 si 5.05 chacun)
        tauxTotal = tauxTotal.add(tauxSalarie);
        tauxTotal = tauxTotal.add(tauxEmployeur);
        return tauxTotal;
    }

    /**
     * @return
     */
    public String getTypeDeProcedure() {
        return typeDeProcedure;
    }

    /**
     * Liste Excel : Recherche les sections du compte annexe puis prépare les données pour la ventilation.
     * 
     * @param idCompteAnnexe
     * @param idSections
     * @throws Exception
     */
    public void initTableauFinal(String idCompteAnnexe, List<String> idSections) throws Exception {
        // On recherche toutes les sections du compte annexe
        CASectionManager sectManager = new CASectionManager();
        sectManager.setSession(getSession());
        sectManager.setForIdCompteAnnexe(idCompteAnnexe);
        sectManager.setForIdSectionIn(idSections);
        sectManager.setOrderBy("DATE");
        sectManager.find(BManager.SIZE_NOLIMIT);
        for (int i = 0; i < sectManager.size(); i++) {
            CASection section = (CASection) sectManager.getEntity(i);
            createPosteAVentiler(section);
            // Liste Excel : utilisé pour stocker la description de la section
            // en fonction de la clé.
            getInfoSectionMap().put(
                    getKey(section),
                    section.getIdExterne() + "/" + section.getDescription() + "/" + section.getDateDebutPeriode() + "/"
                            + section.getDateFinPeriode());
            traiteSection(section);
            reParcoursTableauPourPostesNegatifs(section);
        }
    }

    /**
     * @param idrubrique
     * @param typeMontant
     * @return
     */
    private boolean isPenal(String idrubrique, String typeMontant) {

        if (!CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(idrubrique)) {
            try {
                CAVPTypeDeProcedureOrdreManager mgr = new CAVPTypeDeProcedureOrdreManager();
                mgr.setForIdRubrique(idrubrique);
                mgr.setForTypeProcedure(getTypeDeProcedure());
                mgr.setForTypeOrdre(typeMontant);
                mgr.setSession(getSession());
                mgr.find();
                if (0 == mgr.size()) {
                    return false;
                } else {
                    CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) mgr.getFirstEntity();
                    if (ordre.isPenal().booleanValue()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * @param natureRubrique
     * @return
     */
    private boolean isRubriqueSpecial(String natureRubrique) {
        if (APIRubrique.COMPTE_COURANT_DEBITEUR.equals(natureRubrique)) {
            return true;
        } else if (APIRubrique.COMPTE_COURANT_CREANCIER.equals(natureRubrique)) {
            return true;
        } else if (APIRubrique.COMPTE_FINANCIER.equals(natureRubrique)) {
            return true;
        } else if (APIRubrique.COMPTE_COMPENSATION.equals(natureRubrique)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Méthode qui parcours la section en cours pour voir s'il y a des négatifs, <br />
     * le cas échéant on met le poste à 0 et on ajoute le montant dans le poste à ventiler
     * 
     * @param dateSection
     * @throws JAException
     */
    public void reParcoursTableauPourPostesNegatifs(CASection section) throws JAException {
        String dateSection = JACalendar.format(new JADate(section.getDateSection()), JACalendar.FORMAT_YYYYMMDD);
        CAVPListePosteParSection listePosteParSection = tableauFinal.get(dateSection + section.getIdSection());
        if (listePosteParSection == null) {
            return;
        }

        boolean isMontantAVentiler = false;

        for (Map.Entry<String, Boolean> rubriquesPresentesEntry : rubriquesPresentesMap.entrySet()) {
            String key = rubriquesPresentesEntry.getKey();
            if (listePosteParSection.containsKey(key)) {
                // si il s'agit d'un montant simple
                if (rubriquesPresentesEntry.getValue()) {
                    BigDecimal montantAControler = listePosteParSection.getPoste(key)
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantBase();
                    if (CAVPVentilateur.isMontantNegatif(montantAControler)) {
                        CAVPPoste poste = new CAVPPoste();
                        CAVPDetailMontant detailMontant = new CAVPDetailMontant();
                        detailMontant.addMontant(montantAControler.abs());
                        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);
                        poste.addDetailMontant(detailMontant);
                        poste.setMontantSimple(true);
                        poste.setIdSection(section.getIdSection());
                        isMontantAVentiler = true;
                        addPosteAVentiler(poste, section);

                        CAVPPoste posteSimple = new CAVPPoste();
                        CAVPDetailMontant detailMontantSimple = new CAVPDetailMontant();
                        detailMontantSimple.addMontant(montantAControler.abs());
                        detailMontantSimple.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);
                        posteSimple.addDetailMontant(detailMontantSimple);
                        posteSimple.setMontantSimple(true);
                        posteSimple.setIdSection(section.getIdSection());
                        this.addPoste(key, posteSimple, section);
                    }
                }
                // si il s'agit d'un montant salarié/employeur
                else {
                    BigDecimal montantAControlerSalarie = listePosteParSection.getPoste(key)
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE).getMontantBase();
                    BigDecimal montantAControlerEmployeur = listePosteParSection.getPoste(key)
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR).getMontantBase();
                    if (CAVPVentilateur.isMontantNegatif(montantAControlerSalarie)) {
                        CAVPPoste poste = new CAVPPoste();
                        CAVPDetailMontant detailMontant = new CAVPDetailMontant();
                        detailMontant.addMontant(montantAControlerSalarie.abs());
                        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);
                        poste.addDetailMontant(detailMontant);
                        poste.setMontantSimple(false);
                        poste.setIdSection(section.getIdSection());
                        isMontantAVentiler = true;
                        addPosteAVentiler(poste, section);

                        detailMontant.addMontant(montantAControlerSalarie.abs());
                        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);
                        poste.addDetailMontant(detailMontant);
                    }
                    if (CAVPVentilateur.isMontantNegatif(montantAControlerEmployeur)) {
                        CAVPPoste poste = new CAVPPoste();
                        CAVPDetailMontant detailMontant = new CAVPDetailMontant();
                        detailMontant.addMontant(montantAControlerSalarie.abs());
                        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);
                        poste.addDetailMontant(detailMontant);
                        poste.setMontantSimple(false);
                        poste.setIdSection(section.getIdSection());
                        isMontantAVentiler = true;
                        addPosteAVentiler(poste, section);

                        detailMontant.addMontant(montantAControlerSalarie.abs());
                        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);
                        poste.addDetailMontant(detailMontant);
                    }
                }
            }
        }

        if (isMontantAVentiler) {
            rubriquesPresentesMap.put(CAVPVentilateur.KEY_POSTE_A_VENTILER, true);
        }
    }

    /**
     * @param infoSection
     *            the infoSection to set
     */
    public void setInfoSectionMap(Map<String, String> infoSectionMap) {
        this.infoSectionMap = infoSectionMap;
    }

    /**
     * @param listDetailSection
     *            the listDetailSection to set
     */
    public void setListDetailSectionMap(Map<String, ArrayList<CAVPDetailSection>> listDetailSectionMap) {
        this.listDetailSectionMap = listDetailSectionMap;
    }

    /**
     * @param listMontantBaseVentilationSection
     *            the listMontantBaseVentilationSection to set
     */
    public void setListMontantBaseVentilationSectionMap(HashMap<String, BigDecimal> listMontantBaseVentilationSectionMap) {
        this.listMontantBaseVentilationSectionMap = listMontantBaseVentilationSectionMap;
    }

    /**
     * @param listRubriqueDetailSection
     *            the listRubriqueDetailSection to set
     */
    public void setListRubriqueDetailSectionMap(
            HashMap<String, List<CAVPDetailRubriqueSection>> listRubriqueDetailSectionMap) {
        this.listRubriqueDetailSectionMap = listRubriqueDetailSectionMap;
    }

    /**
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param string
     */
    public void setTypeDeProcedure(String string) {
        typeDeProcedure = string;
    }

    /**
     * @param section
     * @param tabDetailSection
     * @param tabRubriqueDetailSection
     * @param ecritureCourante
     * @param rubrique
     * @param detailSection
     * @param detailRubriqueSection
     * @param taux
     * @throws Exception
     */
    private void traiteMontantDouble(CASection section, ArrayList<CAVPDetailSection> tabDetailSection,
            ArrayList<CAVPDetailRubriqueSection> tabRubriqueDetailSection, CAEcriture ecritureCourante,
            CATauxRubriques taux) throws Exception {
        CAVPPoste poste = new CAVPPoste();
        BigDecimal tauxSalarie = new BigDecimal(taux.getTauxEmployeur());
        BigDecimal tauxEmployeur = new BigDecimal(taux.getTauxSalarie());
        BigDecimal tauxTotal = getTauxTotal(tauxSalarie, tauxEmployeur);

        // il y a un taux, on split le montant de l'écriture
        BigDecimal partSalarie = getPartSalarie(ecritureCourante, taux, tauxTotal);
        BigDecimal partEmployeur = getPartEmployeur(ecritureCourante, taux, tauxTotal, partSalarie);

        // Force le montant le plus élevé sur la part employeur
        if (partSalarie.compareTo(partEmployeur) > 0) {
            BigDecimal temp = partSalarie;
            partSalarie = partEmployeur;
            partEmployeur = temp;
        }

        addDetailSection(tabDetailSection, ecritureCourante, partEmployeur, partSalarie, false);
        addRubriqueDetailSection(tabRubriqueDetailSection, ecritureCourante.getCompte(), false);

        // Check le total des deux (employeur, salarié par rapport au montant
        // total)
        BigDecimal montantTotal = new BigDecimal(ecritureCourante.getMontant());
        if (montantTotal.compareTo(partEmployeur.add(partSalarie)) != 0) {
            throw new Exception("La somme de la part employeur et de la part salarié est différente du montant total.");
        }

        // DetailMontant Employeur
        CAVPDetailMontant detailMontant = new CAVPDetailMontant();
        detailMontant.addMontant(partEmployeur, ecritureCourante.getAnneeCotisation());
        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);
        poste.addDetailMontant(detailMontant);

        // DetailMontant Salarie
        CAVPDetailMontant detailMontantSal = new CAVPDetailMontant();
        detailMontantSal.addMontant(partSalarie, ecritureCourante.getAnneeCotisation());
        detailMontantSal.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);
        poste.addDetailMontant(detailMontantSal);

        poste.setIdSection(section.getIdSection());
        poste.setMontantSimple(false);
        this.addPoste(ecritureCourante.getCompte().getIdRubrique(), poste, section,
                ecritureCourante.getAnneeCotisation());
    }

    /**
     * @param section
     * @param tabDetailSection
     * @param tabRubriqueDetailSection
     * @param ecritureCourante
     * @param rubrique
     * @param detailSection
     * @param detailRubriqueSection
     */
    private void traiteMontantSimple(CASection section, ArrayList<CAVPDetailSection> tabDetailSection,
            ArrayList<CAVPDetailRubriqueSection> tabRubriqueDetailSection, CAEcriture ecritureCourante) {
        // pas de taux donc montant simple
        String key = CAVPVentilateur.KEY_POSTE_A_VENTILER;
        CAVPPoste posteSimple = new CAVPPoste();
        CAVPDetailMontant detailMontant = new CAVPDetailMontant();
        BigDecimal montant = null;

        if (isRubriqueSpecial(ecritureCourante.getCompte().getNatureRubrique())) {
            montant = new BigDecimal(ecritureCourante.getMontant()).multiply(new BigDecimal("-1"));
        } else {
            key = ecritureCourante.getCompte().getIdRubrique();
            montant = new BigDecimal(ecritureCourante.getMontant());
            // Collecte les écritures utilisées pour la ventilation afin de les affichers dans des onglets de détails.
            if (montant.signum() != 0) {
                addDetailSection(tabDetailSection, ecritureCourante, montant, null, true);
                addRubriqueDetailSection(tabRubriqueDetailSection, ecritureCourante.getCompte(), true);
            }
        }
        detailMontant.addMontant(montant, ecritureCourante.getAnneeCotisation());
        detailMontant.setTypeMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);
        posteSimple.addDetailMontant(detailMontant);
        posteSimple.setMontantSimple(true);
        posteSimple.setIdSection(section.getIdSection());
        this.addPoste(key, posteSimple, section, ecritureCourante.getAnneeCotisation());
    }

    /**
     * Prépare les postes de la section qui seront employés pour la ventilation
     * 
     * @param section
     * @throws Exception
     */
    public void traiteSection(CASection section) throws Exception {
        // déclaration des différents conteneurs employés pour stocker le détail
        // des écritures.
        ArrayList<CAVPDetailSection> tabDetailSection = new ArrayList<CAVPDetailSection>();
        ArrayList<CAVPDetailRubriqueSection> tabRubriqueDetailSection = new ArrayList<CAVPDetailRubriqueSection>();

        CAEcritureManager listeEcritures = findEcrituresOfSection(section);

        // Vérifie les écritures pour les ajouter aux postes à ventiler.
        for (int i = 0; i < listeEcritures.size(); i++) {
            CAEcriture ecritureCourante = (CAEcriture) listeEcritures.getEntity(i);

            if (isRubriqueSpecial(ecritureCourante.getCompte().getNatureRubrique())) {
                traiteMontantSimple(section, tabDetailSection, tabRubriqueDetailSection, ecritureCourante);
            } else {
                CATauxRubriques taux = getTaux(ecritureCourante, ecritureCourante.getCompte());
                // TODO sch 12 mai 2011 : Ajouter un contrôle pour voir si la rubrique pour la procédure concernée est
                // bug 5400
                // montant simple ou pas
                if (taux != null) {
                    traiteMontantDouble(section, tabDetailSection, tabRubriqueDetailSection, ecritureCourante, taux);
                } else {
                    traiteMontantSimple(section, tabDetailSection, tabRubriqueDetailSection, ecritureCourante);
                }
            }
        }

        // Liste Excel : classe les clés dans l'ordre croissant => l'ordre des
        // postes toujours pareil.
        Collections.sort(tabRubriqueDetailSection, new CAVPComparator());
        // Liste Excel : contient la liste des rubriques à afficher pour chaque sections
        listRubriqueDetailSectionMap.put(getKey(section), tabRubriqueDetailSection);
        // Liste Excel : contient le détail des écritures pour chaque sections
        listDetailSectionMap.put(getKey(section), tabDetailSection);
    }

    /**
     * Appelé par le contentieux pour le traitement des irrécouvrables
     */
    public void ventiler() {
        this.ventiler(true);
    }

    /**
	 */
    public void ventiler(boolean isVentilationGlobale) {
        CAVPListePosteParSection listePosteParSection = new CAVPListePosteParSection();
        tableauFinal.put(CAVPVentilateur.KEY_POSTE_TOTAL, listePosteParSection);

        // Détermine si la ventilation se fait par section ou globalement pour
        // toutes les sections par rubrique (en pot commun).
        if (isVentilationGlobale) {
            // initialisation de listePosteParRubrique et remplissage
            CAVPListePosteParRubrique listePosteParRubrique = new CAVPListePosteParRubrique();
            listePosteParRubrique.remplirListePosteParRubrique(tableauFinal, getListMontantBaseVentilationSectionMap());
            // listePosteParRubrique.displayListePosteParRubrique();
            this.ventilerSection(listePosteParRubrique);
        } else {
            for (Map.Entry<String, CAVPListePosteParSection> tableauFinalEntry : tableauFinal.entrySet()) {
                this.ventilerSection(tableauFinalEntry.getValue(), tableauFinalEntry.getKey());
            }
        }

        // En se basant sur les ruriques présentes, il faut trouver le total de
        // chaque rubrique et determiner la part pénal
        for (Map.Entry<String, Boolean> rubriquesPresentesEntry : rubriquesPresentesMap.entrySet()) {
            BigDecimal totalParRubrique = new BigDecimal("0");
            BigDecimal totalParRubriqueEmpl = new BigDecimal("0");
            BigDecimal totalParRubriqueSal = new BigDecimal("0");
            String keyIdRubrique = rubriquesPresentesEntry.getKey();

            for (Map.Entry<String, CAVPListePosteParSection> tableauFinalEntry : tableauFinal.entrySet()) {
                listePosteParSection = tableauFinalEntry.getValue();
                if (listePosteParSection.getPoste(keyIdRubrique) != null) {
                    // montant simple, on récupère le montant simple
                    if (rubriquesPresentesEntry.getValue()) {
                        totalParRubrique = additionnerMontantSimple(listePosteParSection, totalParRubrique,
                                keyIdRubrique);
                    } else {
                        BigDecimal montantEmployeur = new BigDecimal("0");
                        montantEmployeur = listePosteParSection
                                .getPoste(keyIdRubrique)
                                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)
                                .getMontantBase()
                                .subtract(
                                        listePosteParSection.getPoste(keyIdRubrique)
                                                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)
                                                .getMontantVentile());
                        totalParRubriqueEmpl = totalParRubriqueEmpl.add(montantEmployeur);
                        BigDecimal montantSalarie = new BigDecimal("0");
                        montantSalarie = listePosteParSection
                                .getPoste(keyIdRubrique)
                                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE)
                                .getMontantBase()
                                .subtract(
                                        listePosteParSection.getPoste(keyIdRubrique)
                                                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE)
                                                .getMontantVentile());
                        totalParRubriqueSal = totalParRubriqueSal.add(montantSalarie);
                    }
                }

            }
            // traiter différement le montant simples des autres
            determinerPartPenale(totalParRubrique, totalParRubriqueEmpl, totalParRubriqueSal, keyIdRubrique);
        }
    }

    /**
     * Ventile les montants en fonction des taux
     * 
     * @param listePosteParRubrique
     *            : contient les postes regroupés par idRubrique
     */
    public void ventilerSection(CAVPListePosteParRubrique listePosteParRubrique) {
        // type d'ordre on va de 1 à 10, on admet maximum 10 ordres (priorités) de couverture
        for (int i = 0; i < 10; i++) {
            try {
                // Montant total à ventiler ne doit pas changer tant que tous
                // les postes pour cette ordre de priorité n'ont pas été
                // ventilé.
                BigDecimal montantTotalAVentile = listePosteParRubrique.getMontantTotalAVentiler();
                // Montant dû pour la priorité de l'ordre = i
                BigDecimal montantDuPourOrdreCourant = new BigDecimal("0"); // Montant à couvrir
                CAVPTypeDeProcedureOrdreManager ordreProceduresMgr = findOrdreProcedures(i);

                // Parcours les rubriques de même priorité/ordre
                for (int j = 0; j < ordreProceduresMgr.size(); j++) {
                    CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) ordreProceduresMgr.get(j);
                    // On ajoute la somme de chaque Poste du même ordre pour
                    // trouver le total
                    montantDuPourOrdreCourant = montantDuPourOrdreCourant.add(listePosteParRubrique.getMontantTotal(
                            ordre.getIdRubrique(), ordre.getTypeOrdre()));
                }

                if (montantDuPourOrdreCourant.compareTo(montantTotalAVentile) <= 0) {
                    // Le montant à ventiler est suffisant pour couvrir
                    for (int l = 0; l < ordreProceduresMgr.size(); l++) {
                        CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) ordreProceduresMgr.get(l);
                        listePosteParRubrique.ventilePostes(ordre.getIdRubrique(), ordre.getTypeOrdre());
                    }
                } else {
                    // On ventile les cas complexes au prorata arrondi au -.05
                    for (int k = 0; k < ordreProceduresMgr.size(); k++) {
                        CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) ordreProceduresMgr.get(k);
                        listePosteParRubrique.ventilePostesParProrata(ordre.getIdRubrique(), ordre.getTypeOrdre(),
                                montantDuPourOrdreCourant, montantTotalAVentile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // TODO sel gérer Exception
            }
        }
    }

    /**
     * Ventile les montants en fonction des taux
     * 
     * @param sectionAventiler
     * @param key
     */
    public void ventilerSection(CAVPListePosteParSection sectionAventiler, String key) {
        if (sectionAventiler.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER) == null) {
            return;
        }

        // type d'ordre on va de 1 à 10( on admet maximum 10 ordre
        BigDecimal montantAVentile = sectionAventiler.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER)
                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantBase();
        BigDecimal montantVentile = sectionAventiler.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER)
                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantVentile();

        if (montantAVentile.compareTo(new BigDecimal("0")) <= 0) {
            return;
        }

        // Ajoute le total du montant à ventiler pour cette section.
        listMontantBaseVentilationSectionMap.put(key, montantAVentile);

        for (int i = 0; i < 10; i++) {
            try {
                // contient le total à soustraire pour ventiler le tout.
                BigDecimal totalSoustrait = new BigDecimal("0");
                CAVPTypeDeProcedureOrdreManager ordreProceduresMgr = new CAVPTypeDeProcedureOrdreManager();
                ordreProceduresMgr.setSession(session);
                ordreProceduresMgr.setSession(getSession());
                ordreProceduresMgr.setForOrdre(String.valueOf(i));
                ordreProceduresMgr.setForTypeProcedure(getTypeDeProcedure());
                ordreProceduresMgr.find();
                // On récupère le montant à ventiler
                BigDecimal montantACouvrir = new BigDecimal("0");
                for (int j = 0; j < ordreProceduresMgr.size(); j++) {
                    // On ajoute la somme de chaque Poste du même ordre pour
                    // trouver le total
                    CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) ordreProceduresMgr.get(j);
                    if (sectionAventiler.containsKey(ordre.getIdRubrique())) {
                        montantACouvrir = montantACouvrir.add(sectionAventiler.getPoste(ordre.getIdRubrique())
                                .getDetailMontant(ordre.getTypeOrdre()).getMontantBase());
                    }
                    // montantACouvrir =
                    // montantACouvrir.add(sectionAventiler.getPoste(KEY_POSTE_A_VENTILER));
                }

                if (montantACouvrir.compareTo(montantAVentile) <= 0) {
                    // Le montant à ventiler est suffisant pour couvrir
                    for (int l = 0; l < ordreProceduresMgr.size(); l++) {
                        CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) ordreProceduresMgr.get(l);
                        if (sectionAventiler.getPoste(ordre.getIdRubrique()) != null) {
                            sectionAventiler
                                    .getPoste(ordre.getIdRubrique())
                                    .getDetailMontant(ordre.getTypeOrdre())
                                    .addMontantVentile(
                                            sectionAventiler.getPoste(ordre.getIdRubrique())
                                                    .getDetailMontant(ordre.getTypeOrdre()).getMontantBase());
                            totalSoustrait = totalSoustrait.add(sectionAventiler.getPoste(ordre.getIdRubrique())
                                    .getDetailMontant(ordre.getTypeOrdre()).getMontantBase());
                        }
                    }
                } else {
                    // On ventile les cas complexes au prorata arrondi au -.05
                    for (int k = 0; k < ordreProceduresMgr.size(); k++) {
                        BigDecimal montantADivide = new BigDecimal("0");
                        CAVPTypeDeProcedureOrdre ordre = (CAVPTypeDeProcedureOrdre) ordreProceduresMgr.get(k);
                        if (sectionAventiler.getPoste(ordre.getIdRubrique()) == null) {
                            continue;
                        }
                        montantADivide = sectionAventiler.getPoste(ordre.getIdRubrique())
                                .getDetailMontant(ordre.getTypeOrdre()).getMontantBase();
                        if (montantADivide.compareTo(new BigDecimal("0")) <= 0) {
                            continue;
                        }

                        BigDecimal taux = montantADivide.divide(montantACouvrir, 10, BigDecimal.ROUND_DOWN);
                        BigDecimal montantASoustraire = JANumberFormatter.round(montantAVentile.multiply(taux), 0.05,
                                2, JANumberFormatter.NEAR);
                        sectionAventiler.getPoste(ordre.getIdRubrique()).getDetailMontant(ordre.getTypeOrdre())
                                .addMontantVentile(montantASoustraire);

                        totalSoustrait = totalSoustrait.add(montantASoustraire);
                        // Si le montant à répartir est entre -0.05 et 0.05 =>
                        // on l'affecte à la dernière rubrique touchée de
                        // manière à avoir un solde de 0
                        BigDecimal montantAComparer = new BigDecimal(CAVPListePosteParSection.MARGE_MONTANT_A_REPARTIR);

                        if (montantAVentile.subtract(totalSoustrait).compareTo(montantAComparer.abs()) <= 0) {
                            sectionAventiler.getPoste(ordre.getIdRubrique()).getDetailMontant(ordre.getTypeOrdre())
                                    .addMontantVentile(montantAVentile.subtract(totalSoustrait));
                            totalSoustrait = totalSoustrait.add(montantAVentile.subtract(totalSoustrait));
                        }
                    }
                }
                montantAVentile = montantAVentile.subtract(totalSoustrait);
                montantVentile = montantVentile.add(totalSoustrait);
            } catch (Exception e) {
                e.printStackTrace(); // TODO sel gérer Exception
            }
        }
        BigDecimal montantRestant = sectionAventiler.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER)
                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantBase();
        montantRestant = montantRestant.subtract(montantAVentile);
        sectionAventiler.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER)
                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).addMontantVentile(montantVentile);
    }
}
