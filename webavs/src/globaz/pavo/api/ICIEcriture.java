package globaz.pavo.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'une inscription.
 * 
 * @author David Girardin
 */
public interface ICIEcriture extends BIEntity {
    /** Part de bonif. pour tâches d'assistance */
    public final static String CS_BONIFICATION_COMPLETE = "319001";
    /** Type de compte */
    public final static String CS_CI = "303001";
    /** Code */
    public final static String CS_CODE_AMORTISSEMENT = "313001";
    /** Code */
    public final static String CS_CODE_EXEMPTION = "313002";
    /** Code */
    public final static String CS_CODE_PROVISOIRE = "313004";
    /** Code */
    public final static String CS_CODE_SURSIS = "313003";
    /** Catégorie de personnel */
    public final static String CS_COLLABORATEUR_AGRICOLE = "320001";
    /** Type de compte */
    public final static String CS_CORRECTION = "303005";
    /** Part de bonif. pour tâches d'assistance */
    public final static String CS_DEMI_BONIFICATION = "319002";
    /** Type de compte */
    public final static String CS_GENRE_6 = "303004";
    /** Type de compte */
    public final static String CS_GENRE_7 = "303007";
    /** Branche économique */
    public final static String CS_HORLOGERIE = "314033";
    /** Catégorie de personnel */
    public final static String CS_PERSONNEL_MAISON = "320002";
    /** Type de compte */
    public final static String CS_PROVISOIRE = "303006";
    /** Type de compte */
    public final static String CS_SUSPENS = "303002";
    /** Type de compte */
    public final static String CS_SUSPENS_SUPPRIMES = "303003";
    /** Part de bonif. pour tâches d'assistance */
    public final static String CS_TIERS_BONIFICATION = "319003";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @param transaction
     *            la transaction à utiliser pour la requête
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Passe l'inscription au CI.
     * 
     * @param transaction
     *            la transaction à utiliser pour l'opération.
     * @exception java.lang.Exception
     *                si l'appel distant de la fonction n'a pas pu être effectué.
     */
    public void comptabiliser(BITransaction transaction) throws Exception;

    /**
     * Supprime le journal de la BD
     * 
     * @param transaction
     *            la transaction à utiliser pour la requête
     * @exception java.lang.Exception
     *                si la suppression a échouée
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie l'année de l'écriture.
     * 
     * @return l'année de l'écriture. Format <tt>AAAA</tt>
     */
    public String getAnnee();

    /**
     * Renvoie le numéro AVS du CI associé à l'écriture.
     * 
     * @return le numéro AVS au format à 11 chiffres
     */
    public java.lang.String getAvs();

    /**
     * Renvoie la branche économique.
     * 
     * @return la branche économique ou vide si non spécifié. Valeurs possibles:
     *         <ul>
     *         <li><tt>CS_HORLOGERIE</tt></li>
     *         <li>... (à définir selon besoin)</li>
     *         </ul>
     */
    public String getBrancheEconomique();

    /**
     * Renvoie la caisse chômage, uniquement pour les écritures de l'assurance chômage.
     * 
     * @return la caisse chômage ou vide si non spécifié.
     */
    public String getCaisseChomage();

    /**
     * Renvoie la catégorie du personnel.
     * 
     * @return la catégorie du personnel ou vide si non spécifié. Valeurs possibles:
     *         <ul>
     *         <li><tt>CS_COLLABORATEUR_AGRICOLE</tt></li>
     *         <li><tt>CS_PERSONNEL_MAISON</tt></li>
     *         </ul>
     */
    public String getCategoriePersonnel();

    /**
     * Renvoie le code qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_CODE_AMORTISSEMENT</tt></li>
     * <li><tt>CS_CODE_EXEMPTION</tt></li>
     * <li><tt>CS_CODE_SURSIS</tt></li>
     * <li><tt>CS_CODE_PROVISOIRE</tt></li>
     * </ul>
     * 
     * @return le code ou vide si non spécifié.
     */
    public String getCode();

    /**
     * Renvoie le code spécial qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_COTISATION_MINIMALE</tt></li>
     * <li><tt>CS_NONFORMATTEUR_INDEPENDANT</tt></li>
     * <li><tt>CS_NONFORMATTEUR_SALARIE</tt></li>
     * </ul>
     * 
     * @return le code ou vide si non spécifié.
     */
    public String getCodeSpecial();

    /**
     * Renvoie la date de l'annonce de cette écriture à la centrale.
     * 
     * @return la date de l'annonce de cette écriture à la centrale ou vide si pas encore envoyée. Format
     *         <tt>jj.mm.aaaa</tt>
     */
    public String getDateAnnonceCentrale();

    /**
     * Renvoie la date de l'envoi du CI additionnel.
     * 
     * @return la date de l'envoi du CI additionnel ou vide si non applicable. Format <tt>jj.mm.aaaa</tt>
     */
    public String getDateCiAdditionnel();

    /**
     * Renvoie la date d'inscription.
     * 
     * @return la date d'inscription. Format <tt>jj.mm.aaaa</tt>
     */
    public String getDateInscription();

    /**
     * Renvoie l'id de l'écriture.
     * 
     * @return l'id de l'écriture.
     */
    public String getEcritureId();

    /**
     * Renvoie le genre de l'écriture composé du code extourne, du genre et, éventuellement, du chiffre clé particulier.
     * Exemple 01, 185, etc
     * 
     * @return le code extourne, le genre et, éventuellement, le chiffre clé particulier.
     */
    public String getGre();

    /**
     * Renvoie l'id du journal associé.
     * 
     * @return l'id du journal associé.
     */
    public String getIdJournal();

    /**
     * Renvoie le type de compte de l'inscription. Types définis:
     * <ul>
     * <li><tt>CS_CI</tt></li>
     * <li><tt>CS_SUSPENS</tt></li>
     * <li><tt>CS_GENRE_6</tt></li>
     * <li><tt>CS_GENRE_7</tt></li>
     * </ul>
     * 
     * @return le type de compte de l'inscription.
     */
    public String getIdTypeCompte();

    /**
     * Renvoie le mois de début de l'inscription.
     * 
     * @return le mois de début de l'inscription.
     */
    public String getMoisDebut();

    /**
     * Renvoie le mois de fin de l'inscription.
     * 
     * @return le mois de fin de l'inscription.
     */
    public String getMoisFin();

    /**
     * Renvoie le montant non signé de l'inscription.
     * 
     * @return le montant non signé de l'inscription. Format <tt>1234.45</tt>
     */
    public String getMontant();

    /**
     * Retourne le montant signé de l'inscription.<br>
     * Le montant est positif si la propriété <tt>extourne</tt> est 0, 6 ou 8, et négative dans les autres cas.
     * 
     * @return le montant signé.
     */
    public String getMontantSigne();

    /**
     * Renvoie le BTA qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_BONIFICATION_COMPLETE</tt></li>
     * <li><tt>CS_DEMI_BONIFICATION</tt></li>
     * <li><tt>CS_TIERS_BONIFICATION</tt></li>
     * </ul>
     * 
     * @return le BTA ou vide si non spécifié.
     */
    public String getPartBta();

    /**
     * Renvoie la remarque associée à l'inscription.
     * 
     * @return la remarque associée à l'inscription ou vide si non renseigné.
     */
    public String getRemarque();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @param transaction
     *            la transaction à utiliser pour la requête.
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Définit l'année de l'écriture. Peut être spécifiée par défaut pour le journal associé.
     * 
     * @param inAnnee
     *            l'année de l'écriture. Format <tt>AAAA</tt>
     */
    public void setAnnee(String inAnnee);

    /**
     * Définit le numéro AVS du CI associé à l'écriture.
     * 
     * @param inAvs
     *            le numéro AVS au format à 11 chiffres
     */
    public void setAvs(String inAvs);

    /**
     * Définit la branche économique.
     * 
     * @param inBrancheEconomique
     *            la branche économique. Valeurs possibles:
     *            <ul>
     *            <li><tt>CS_HORLOGERIE</tt>
     *            <li>... (à définir selon besoin)</li>
     *            </ul>
     */
    public void setBrancheEconomique(String inBrancheEconomique);

    /**
     * Définit la caisse chômage, uniquement pour les écritures de l'assurance chômage.
     * 
     * @param inCaisseChomage
     *            la caisse chômage, doit comprendre au moins 3 chiffres
     */
    public void setCaisseChomage(String inCaisseChomage);

    /**
     * Définit la catégorie du personnel.
     * 
     * @param inCategoriePersonnel
     *            la catégorie du personnel. Valeurs possibles:
     *            <ul>
     *            <li><tt>CS_COLLABORATEUR_AGRICOLE</tt>
     *            <li>CS_PERSONNEL_MAISON</li>
     *            </ul>
     */
    public void setCategoriePersonnel(String inCategoriePersonnel);

    /**
     * Définit le code qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_CODE_AMORTISSEMENT</tt></li>
     * <li><tt>CS_CODE_EXEMPTION</tt></li>
     * <li><tt>CS_CODE_SURSIS</tt></li>
     * <li><tt>CS_CODE_PROVISOIRE</tt></li>
     * </ul>
     * 
     * @param inCode
     *            le code.
     */
    public void setCode(String inCode);

    /**
     * Définit le code spécial qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_COTISATION_MINIMALE</tt></li>
     * <li><tt>CS_NONFORMATTEUR_INDEPENDANT</tt></li>
     * <li><tt>CS_NONFORMATTEUR_SALARIE</tt></li>
     * </ul>
     * 
     * @param inCodeSpecial
     *            le code spécial.
     */
    public void setCodeSpecial(String inCodeSpecial);

    /**
     * Définit l'id de l'écriture. Ignoré lors d'un ajout (incrémentation automatique)
     * 
     * @param inEcritureId
     *            l'id de l'écriture
     */
    public void setEcritureId(String inEcritureId);

    /**
     * Définit le montant non signé de l'inscription.
     * 
     * @param inMontant
     *            le montant non signé de l'inscription. Format <tt>1234.45</tt> ou <tt>1'234.45</tt>
     */
    public void setEmployeurPartenaire(String emplyoeur);

    /**
     * Définit le genre de l'écriture composé du code extourne, du genre et, éventuellement, du chiffre clé particulier.
     * Exemple 01, 185, etc
     * 
     * @param inGre
     *            le code extourne, le genre et, éventuellement, le chiffre clé particulier.
     */
    public void setGre(String inGre);

    /**
     * Définit l'id du journal associé.
     * 
     * @param inIdJournal
     *            l'id du journal associé.
     */
    public void setIdJournal(String inIdJournal);

    /**
     * Défini le type de compte de l'inscription. Types définis:
     * <ul>
     * <li><tt>CS_CI</tt>: l'écriture passe directement au CI lors d'un ajout.</li>
     * <li><tt>CS_SUSPENS</tt>: en suspens, n'est pas comptabilisé.</li>
     * <li><tt>CS_GENRE_6</tt>: pour compte genre 6</li>
     * <li><tt>CS_GENRE_7</tt>: pour compte genre 7</li>
     * </ul>
     * 
     * @param inIdTypeCompte
     *            le type de compte de l'inscription. Peut-être vide (= non inscrit, par défaut)
     */
    public void setIdTypeCompte(String inIdTypeCompte);

    /**
     * Définit le mois de début de l'inscription.
     * 
     * @param inMoisDebut
     *            le mois de début de l'inscription.
     */
    public void setMoisDebut(String inMoisDebut);

    /**
     * Définit le mois de fin de l'inscription.
     * 
     * @param inMoisFin
     *            le mois de fin de l'inscription.
     */
    public void setMoisFin(String newMoisFin);

    public void setMontant(String inMontant);

    /**
     * Définit le BTA qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_BONIFICATION_COMPLETE</tt></li>
     * <li><tt>CS_DEMI_BONIFICATION</tt></li>
     * <li><tt>CS_TIERS_BONIFICATION</tt></li>
     * </ul>
     * 
     * @param inPartBta
     *            le BTA.
     */
    public void setPartBta(String inPartBta);

    /**
     * Définit la remarque associée au journal.
     * 
     * @param inRemTexte
     *            la remarque associée au journal (max. 255 char.).
     */
    public void setRemarque(String inRemTexte);

    /**
     * Met à jour l'inscription dans la BD
     * 
     * @param transaction
     *            la transaction à utiliser pour la modifiacation
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
