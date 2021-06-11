package globaz.apg.module.calcul;

import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.module.calcul.salaire.APSalaire;
import globaz.apg.module.calcul.salaire.APSalaireAdapter;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Descpription
 * 
 * @author scr Date de création 31 mai 05
 */
public class APBaseCalculSituationProfessionnel {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List basesCalculSalaireJournalier = null;
    private String idAffilie = null;
    private String idSituationProfessionnelle = "";
    private String idTiers = null;

    private boolean isCollaborateurAgricole = false;

    private boolean isIndependant = false;
    private boolean isPaiementEmployeur = false;

    private boolean isSoumisCotisation = false;
    private boolean isTravailleurAgricole = false;
    private boolean isTravailleurSansEmployeur = false;
    private String noAffilie = "";
    private String nom = null;
    private APBaseCalculSalaireJournalier versementEmployeur = null;
    @Getter
    @Setter
    private Integer nbJourIndemnise;
    @Getter
    @Setter
    private Boolean isJoursIdentiques;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APBaseCalculSituationProfessionnel.
     */
    public APBaseCalculSituationProfessionnel() {
        basesCalculSalaireJournalier = new ArrayList();
    }

    /**
     * Crée une nouvelle instance de la classe APBaseCalculSituationProfessionnel.
     * <p>
     * Il est FORTEMENT RECOMMANDE d'utiliser ce constructeur.
     * </p>
     * 
     * @param sitPro
     *            DOCUMENT ME!
     * @param nbContrats
     *            DOCUMENT ME!
     * @param idContrat
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    /**
     * @param sitPro
     * @param nbContrats
     * @param idContrat
     * @throws Exception
     */
    public APBaseCalculSituationProfessionnel(APSituationProfessionnelle sitPro, int nbContrats, int idContrat)
            throws Exception {
        this();

        idAffilie = sitPro.loadEmployeur().getIdAffilie();
        idTiers = sitPro.loadEmployeur().getIdTiers();
        isPaiementEmployeur = sitPro.getIsVersementEmployeur().booleanValue();
        idSituationProfessionnelle = sitPro.getIdSituationProf();
        isSoumisCotisation = !sitPro.getIsNonSoumisCotisation().booleanValue();
        isCollaborateurAgricole = sitPro.getIsCollaborateurAgricole().booleanValue();
        isTravailleurAgricole = sitPro.getIsTravailleurAgricole().booleanValue();
        isIndependant = sitPro.getIsIndependant().booleanValue();
        nom = sitPro.loadEmployeur().loadNom();
        noAffilie = sitPro.loadEmployeur().loadNumero();
        nbJourIndemnise = sitPro.getNbJourIndemnise();
        isJoursIdentiques = sitPro.getIsJoursIdentiques();

        // indicer le nom s'il y a plus d'un contrat avec cet employeur
        if (nbContrats > 1) {
            // termine le nom d'employeur par '(n)' pour les cas ou l'on a
            // plusieurs fois le meme no d'affilie
            nom = PRStringUtils.indicer(nom, 25, idContrat);
        }

        // revenu indépendant
        if (sitPro.getIsIndependant().booleanValue()) {
            APBaseCalculSalaireJournalier bcj = new APBaseCalculSalaireJournalier();

            bcj.setSalaireAnnuel(new FWCurrency(sitPro.getRevenuIndependant()));
            this.addBaseCalculSalaireJournalier(bcj);
        }

        APSalaireAdapter adapter = new APSalaireAdapter(sitPro);

        this.addBaseCalculSalaireJournalier(adapter.salairePrincipal(), sitPro.getHeuresSemaine()); // salaire
        // principal
        this.addBaseCalculSalaireJournalier(adapter.remunerationAutre(), sitPro.getHeuresSemaine()); // autre
        // rémunération
        this.addBaseCalculSalaireJournalier(adapter.salaireNature(), sitPro.getHeuresSemaine()); // salaire
        // nature
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param bc
     *            DOCUMENT ME!
     */
    public void addBaseCalculSalaireJournalier(APBaseCalculSalaireJournalier bc) {
        basesCalculSalaireJournalier.add(bc);
    }

    private void addBaseCalculSalaireJournalier(APSalaire salaire, String nombreHeure) {
        if (salaire != null) {
            APBaseCalculSalaireJournalier bcj = new APBaseCalculSalaireJournalier();

            salaire.updateBaseCalculSalaire(bcj, nombreHeure);
            this.addBaseCalculSalaireJournalier(bcj);
        }
    }

    /**
     * getter pour l'attribut bases calcul salaire journalier
     * 
     * @return la valeur courante de l'attribut bases calcul salaire journalier
     */
    public List getBasesCalculSalaireJournalier() {
        return basesCalculSalaireJournalier;
    }

    /**
     * getter pour l'attribut id affilie
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id situation professionnelle
     * 
     * @return la valeur courante de l'attribut id situation professionnelle
     */
    public String getIdSituationProfessionnelle() {
        return idSituationProfessionnelle;
    }

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the noAffilie
     */
    public final String getNoAffilie() {
        return noAffilie;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut versement employeur
     * 
     * @return la valeur courante de l'attribut versement employeur
     */
    public APBaseCalculSalaireJournalier getVersementEmployeur() {
        return versementEmployeur;
    }

    /**
     * getter pour l'attribut collaborateur agricole
     * 
     * @return la valeur courante de l'attribut collaborateur agricole
     */
    public boolean isCollaborateurAgricole() {
        return isCollaborateurAgricole;
    }

    /**
     * getter pour l'attribut independant
     * 
     * @return la valeur courante de l'attribut independant
     */
    public boolean isIndependant() {
        return isIndependant;
    }

    /**
     * getter pour l'attribut paiement employeur
     * 
     * @return la valeur courante de l'attribut paiement employeur
     */
    public boolean isPaiementEmployeur() {
        return isPaiementEmployeur;
    }

    /**
     * Retourne vrai si cette base pour le calcul correspond à la situation professionnelle transmise en argument.
     * <p>
     * En interne, cette classe se base sur l'identifiant de la situation professionnelle pour tenter de résoudre
     * l'ambiguité qui survient si un employeur verse plusieurs salaires à une même personne. S'il n'est pas possible
     * d'utiliser cet identifant (par exemple si l'identifiant est perdu ou n'a jamais été initialisé), cette méthode
     * compare uniquement les identifiants affilie et tiers, IL Y A DONC UNE AMBIGUITE POSSIBLE. Il est donc fortement
     * recommandé de toujours utiliser le constructeur APBaseCalculSituationProfessionnel(APSituationProfessionnelle
     * sitPro) pour créer une instance de cette classe.
     * </p>
     * 
     * @param sitPro
     *            la situationn professionnelle à examiner.
     * @return vrai si cette base de calcul correspond à la situation professionnelle transmise en argument.
     * @throws Exception
     *             s'il est nécessaire de charger l'employeur lié à la situation professionnelle et que ce chargement
     *             échoue.
     */
    public boolean isPourSituationProfessionnelle(APSituationProfessionnelle sitPro) throws Exception {
        if (JadeStringUtil.isEmpty(idSituationProfessionnelle)) {
            return sitPro.loadEmployeur().getIdAffilie().equals(idAffilie)
                    && sitPro.loadEmployeur().getIdTiers().equals(idTiers);
        } else {
            return idSituationProfessionnelle.equals(sitPro.getIdSituationProf());
        }
    }

    /**
     * getter pour l'attribut soumis cotisation
     * 
     * @return la valeur courante de l'attribut soumis cotisation
     */
    public boolean isSoumisCotisation() {
        return isSoumisCotisation;
    }

    /**
     * @return
     */
    public boolean isTravailleurAgricole() {
        return isTravailleurAgricole;
    }

    /**
     * getter pour l'attribut travailleur sans employeur
     * 
     * @return la valeur courante de l'attribut travailleur sans employeur
     */
    public boolean isTravailleurSansEmployeur() {
        return isTravailleurSansEmployeur;
    }

    /**
     * setter pour l'attribut collaborateur agricole
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setCollaborateurAgricole(boolean b) {
        isCollaborateurAgricole = b;
    }

    /**
     * setter pour l'attribut id affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * setter pour l'attribut independant
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setIndependant(boolean b) {
        isIndependant = b;
    }

    /**
     * @param noAffilie
     *            the noAffilie to set
     */
    public final void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut paiement employeur
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setPaiementEmployeur(boolean b) {
        isPaiementEmployeur = b;
    }

    /**
     * setter pour l'attribut soumis cotisation
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisation(boolean b) {
        isSoumisCotisation = b;
    }

    /**
     * @param b
     */
    public void setTravailleurAgricole(boolean b) {
        isTravailleurAgricole = b;
    }

    /**
     * setter pour l'attribut travailleur sans employeur
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setTravailleurSansEmployeur(boolean b) {
        isTravailleurSansEmployeur = b;
    }

    /**
     * setter pour l'attribut versement employeur
     * 
     * @param journalier
     *            une nouvelle valeur pour cet attribut
     */
    public void setVersementEmployeur(APBaseCalculSalaireJournalier journalier) {
        versementEmployeur = journalier;
    }

}
