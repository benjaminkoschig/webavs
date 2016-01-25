package globaz.pavo.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'une inscription.
 * 
 * @author David Girardin
 */
public interface ICIEcriture extends BIEntity {
    /** Part de bonif. pour t�ches d'assistance */
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
    /** Cat�gorie de personnel */
    public final static String CS_COLLABORATEUR_AGRICOLE = "320001";
    /** Type de compte */
    public final static String CS_CORRECTION = "303005";
    /** Part de bonif. pour t�ches d'assistance */
    public final static String CS_DEMI_BONIFICATION = "319002";
    /** Type de compte */
    public final static String CS_GENRE_6 = "303004";
    /** Type de compte */
    public final static String CS_GENRE_7 = "303007";
    /** Branche �conomique */
    public final static String CS_HORLOGERIE = "314033";
    /** Cat�gorie de personnel */
    public final static String CS_PERSONNEL_MAISON = "320002";
    /** Type de compte */
    public final static String CS_PROVISOIRE = "303006";
    /** Type de compte */
    public final static String CS_SUSPENS = "303002";
    /** Type de compte */
    public final static String CS_SUSPENS_SUPPRIMES = "303003";
    /** Part de bonif. pour t�ches d'assistance */
    public final static String CS_TIERS_BONIFICATION = "319003";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @param transaction
     *            la transaction � utiliser pour la requ�te
     * @exception java.lang.Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Passe l'inscription au CI.
     * 
     * @param transaction
     *            la transaction � utiliser pour l'op�ration.
     * @exception java.lang.Exception
     *                si l'appel distant de la fonction n'a pas pu �tre effectu�.
     */
    public void comptabiliser(BITransaction transaction) throws Exception;

    /**
     * Supprime le journal de la BD
     * 
     * @param transaction
     *            la transaction � utiliser pour la requ�te
     * @exception java.lang.Exception
     *                si la suppression a �chou�e
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie l'ann�e de l'�criture.
     * 
     * @return l'ann�e de l'�criture. Format <tt>AAAA</tt>
     */
    public String getAnnee();

    /**
     * Renvoie le num�ro AVS du CI associ� � l'�criture.
     * 
     * @return le num�ro AVS au format � 11 chiffres
     */
    public java.lang.String getAvs();

    /**
     * Renvoie la branche �conomique.
     * 
     * @return la branche �conomique ou vide si non sp�cifi�. Valeurs possibles:
     *         <ul>
     *         <li><tt>CS_HORLOGERIE</tt></li>
     *         <li>... (� d�finir selon besoin)</li>
     *         </ul>
     */
    public String getBrancheEconomique();

    /**
     * Renvoie la caisse ch�mage, uniquement pour les �critures de l'assurance ch�mage.
     * 
     * @return la caisse ch�mage ou vide si non sp�cifi�.
     */
    public String getCaisseChomage();

    /**
     * Renvoie la cat�gorie du personnel.
     * 
     * @return la cat�gorie du personnel ou vide si non sp�cifi�. Valeurs possibles:
     *         <ul>
     *         <li><tt>CS_COLLABORATEUR_AGRICOLE</tt></li>
     *         <li><tt>CS_PERSONNEL_MAISON</tt></li>
     *         </ul>
     */
    public String getCategoriePersonnel();

    /**
     * Renvoie le code qui peut �tre du type suivant:
     * <ul>
     * <li><tt>CS_CODE_AMORTISSEMENT</tt></li>
     * <li><tt>CS_CODE_EXEMPTION</tt></li>
     * <li><tt>CS_CODE_SURSIS</tt></li>
     * <li><tt>CS_CODE_PROVISOIRE</tt></li>
     * </ul>
     * 
     * @return le code ou vide si non sp�cifi�.
     */
    public String getCode();

    /**
     * Renvoie le code sp�cial qui peut �tre du type suivant:
     * <ul>
     * <li><tt>CS_COTISATION_MINIMALE</tt></li>
     * <li><tt>CS_NONFORMATTEUR_INDEPENDANT</tt></li>
     * <li><tt>CS_NONFORMATTEUR_SALARIE</tt></li>
     * </ul>
     * 
     * @return le code ou vide si non sp�cifi�.
     */
    public String getCodeSpecial();

    /**
     * Renvoie la date de l'annonce de cette �criture � la centrale.
     * 
     * @return la date de l'annonce de cette �criture � la centrale ou vide si pas encore envoy�e. Format
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
     * Renvoie l'id de l'�criture.
     * 
     * @return l'id de l'�criture.
     */
    public String getEcritureId();

    /**
     * Renvoie le genre de l'�criture compos� du code extourne, du genre et, �ventuellement, du chiffre cl� particulier.
     * Exemple 01, 185, etc
     * 
     * @return le code extourne, le genre et, �ventuellement, le chiffre cl� particulier.
     */
    public String getGre();

    /**
     * Renvoie l'id du journal associ�.
     * 
     * @return l'id du journal associ�.
     */
    public String getIdJournal();

    /**
     * Renvoie le type de compte de l'inscription. Types d�finis:
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
     * Renvoie le mois de d�but de l'inscription.
     * 
     * @return le mois de d�but de l'inscription.
     */
    public String getMoisDebut();

    /**
     * Renvoie le mois de fin de l'inscription.
     * 
     * @return le mois de fin de l'inscription.
     */
    public String getMoisFin();

    /**
     * Renvoie le montant non sign� de l'inscription.
     * 
     * @return le montant non sign� de l'inscription. Format <tt>1234.45</tt>
     */
    public String getMontant();

    /**
     * Retourne le montant sign� de l'inscription.<br>
     * Le montant est positif si la propri�t� <tt>extourne</tt> est 0, 6 ou 8, et n�gative dans les autres cas.
     * 
     * @return le montant sign�.
     */
    public String getMontantSigne();

    /**
     * Renvoie le BTA qui peut �tre du type suivant:
     * <ul>
     * <li><tt>CS_BONIFICATION_COMPLETE</tt></li>
     * <li><tt>CS_DEMI_BONIFICATION</tt></li>
     * <li><tt>CS_TIERS_BONIFICATION</tt></li>
     * </ul>
     * 
     * @return le BTA ou vide si non sp�cifi�.
     */
    public String getPartBta();

    /**
     * Renvoie la remarque associ�e � l'inscription.
     * 
     * @return la remarque associ�e � l'inscription ou vide si non renseign�.
     */
    public String getRemarque();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @param transaction
     *            la transaction � utiliser pour la requ�te.
     * @exception java.lang.Exception
     *                si le chargement a �chou�
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * D�finit l'ann�e de l'�criture. Peut �tre sp�cifi�e par d�faut pour le journal associ�.
     * 
     * @param inAnnee
     *            l'ann�e de l'�criture. Format <tt>AAAA</tt>
     */
    public void setAnnee(String inAnnee);

    /**
     * D�finit le num�ro AVS du CI associ� � l'�criture.
     * 
     * @param inAvs
     *            le num�ro AVS au format � 11 chiffres
     */
    public void setAvs(String inAvs);

    /**
     * D�finit la branche �conomique.
     * 
     * @param inBrancheEconomique
     *            la branche �conomique. Valeurs possibles:
     *            <ul>
     *            <li><tt>CS_HORLOGERIE</tt>
     *            <li>... (� d�finir selon besoin)</li>
     *            </ul>
     */
    public void setBrancheEconomique(String inBrancheEconomique);

    /**
     * D�finit la caisse ch�mage, uniquement pour les �critures de l'assurance ch�mage.
     * 
     * @param inCaisseChomage
     *            la caisse ch�mage, doit comprendre au moins 3 chiffres
     */
    public void setCaisseChomage(String inCaisseChomage);

    /**
     * D�finit la cat�gorie du personnel.
     * 
     * @param inCategoriePersonnel
     *            la cat�gorie du personnel. Valeurs possibles:
     *            <ul>
     *            <li><tt>CS_COLLABORATEUR_AGRICOLE</tt>
     *            <li>CS_PERSONNEL_MAISON</li>
     *            </ul>
     */
    public void setCategoriePersonnel(String inCategoriePersonnel);

    /**
     * D�finit le code qui peut �tre du type suivant:
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
     * D�finit le code sp�cial qui peut �tre du type suivant:
     * <ul>
     * <li><tt>CS_COTISATION_MINIMALE</tt></li>
     * <li><tt>CS_NONFORMATTEUR_INDEPENDANT</tt></li>
     * <li><tt>CS_NONFORMATTEUR_SALARIE</tt></li>
     * </ul>
     * 
     * @param inCodeSpecial
     *            le code sp�cial.
     */
    public void setCodeSpecial(String inCodeSpecial);

    /**
     * D�finit l'id de l'�criture. Ignor� lors d'un ajout (incr�mentation automatique)
     * 
     * @param inEcritureId
     *            l'id de l'�criture
     */
    public void setEcritureId(String inEcritureId);

    /**
     * D�finit le montant non sign� de l'inscription.
     * 
     * @param inMontant
     *            le montant non sign� de l'inscription. Format <tt>1234.45</tt> ou <tt>1'234.45</tt>
     */
    public void setEmployeurPartenaire(String emplyoeur);

    /**
     * D�finit le genre de l'�criture compos� du code extourne, du genre et, �ventuellement, du chiffre cl� particulier.
     * Exemple 01, 185, etc
     * 
     * @param inGre
     *            le code extourne, le genre et, �ventuellement, le chiffre cl� particulier.
     */
    public void setGre(String inGre);

    /**
     * D�finit l'id du journal associ�.
     * 
     * @param inIdJournal
     *            l'id du journal associ�.
     */
    public void setIdJournal(String inIdJournal);

    /**
     * D�fini le type de compte de l'inscription. Types d�finis:
     * <ul>
     * <li><tt>CS_CI</tt>: l'�criture passe directement au CI lors d'un ajout.</li>
     * <li><tt>CS_SUSPENS</tt>: en suspens, n'est pas comptabilis�.</li>
     * <li><tt>CS_GENRE_6</tt>: pour compte genre 6</li>
     * <li><tt>CS_GENRE_7</tt>: pour compte genre 7</li>
     * </ul>
     * 
     * @param inIdTypeCompte
     *            le type de compte de l'inscription. Peut-�tre vide (= non inscrit, par d�faut)
     */
    public void setIdTypeCompte(String inIdTypeCompte);

    /**
     * D�finit le mois de d�but de l'inscription.
     * 
     * @param inMoisDebut
     *            le mois de d�but de l'inscription.
     */
    public void setMoisDebut(String inMoisDebut);

    /**
     * D�finit le mois de fin de l'inscription.
     * 
     * @param inMoisFin
     *            le mois de fin de l'inscription.
     */
    public void setMoisFin(String newMoisFin);

    public void setMontant(String inMontant);

    /**
     * D�finit le BTA qui peut �tre du type suivant:
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
     * D�finit la remarque associ�e au journal.
     * 
     * @param inRemTexte
     *            la remarque associ�e au journal (max. 255 char.).
     */
    public void setRemarque(String inRemTexte);

    /**
     * Met � jour l'inscription dans la BD
     * 
     * @param transaction
     *            la transaction � utiliser pour la modifiacation
     * @exception java.lang.Exception
     *                si la mise � jour a �chou�e
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
