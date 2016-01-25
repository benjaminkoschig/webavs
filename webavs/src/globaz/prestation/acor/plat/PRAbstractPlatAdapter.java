/*
 * Cr�� le 19 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.acor.plat;

import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORAdapter;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRFichierACORPrinter;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cette classe d�finit des m�thodes et des constantes de base pour la cr�ation des fichiers n�cessaires au traitement
 * d'une demande APG/IJ/RENTE/AMAT avec ACOR.
 * </p>
 * 
 * <p>
 * La classe fournit �galement un classe interne de base pour l'impl�mentation de l'interface PRFichierACORPrinter pour
 * des fichiers ACOR. Cette classe fournit de nombreuses m�thodes indispensables � la transformation et prend e�galement
 * garde � �crire correctement les donn�es sp�ciales (par exemple vides) dans le fichier.
 * </p>
 * 
 * <p>
 * Enfin, la classe fournit aussi des impl�mentations pour les fichiers des demandes (commun � toutes les demandes de
 * prestation) et le fichier DEM_GEDO. Il s'agit d'un fichier sp�cifique � la CSC mais il est n�anmoins obligatoire pour
 * le calcul avec aCOR. L'impl�mentation fournie par cette classe renvoie simplement un fichier vide.:
 * </p>
 * 
 * @author vre
 */
public abstract class PRAbstractPlatAdapter implements PRACORAdapter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String delimiteurChamps = "\t";
    private PRFichierACORPrinter fichierDemandeDefautPrinter = null;
    private PRFichierACORPrinter fichierDemGEDOPrinter = null;

    protected BSession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe PRAbstractPlatAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    protected PRAbstractPlatAdapter(BSession session) {
        this.session = session;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected PRFichierACORPrinter fichierDemandeDefautPrinter() {
        if (fichierDemandeDefautPrinter == null) {
            fichierDemandeDefautPrinter = new PRFichierDemandeDefautPrinter(this, PRACORConst.NF_DEMANDE);
        }

        return fichierDemandeDefautPrinter;
    }

    /**
     * Retourne le printer par defaut (fichier vide) pour le fichier DEM_GDO:.
     * 
     * <p>
     * Ce fichier est sp�cifique � la CSC, n�anmoins, il est n�cessaire pour le calcul avec ACOR.
     * </p>
     * 
     * @return DOCUMENT ME!
     */
    protected PRFichierACORPrinter fichierDemGEDOPrinter() {
        if (fichierDemGEDOPrinter == null) {
            fichierDemGEDOPrinter = new PRFichierVidePrinter(this, PRACORConst.NF_DEM_GDO);
        }

        return fichierDemGEDOPrinter;
    }

    /**
     * Retourne la date de d�c�s d'un tiers.
     * 
     * <p>
     * Red�finir cette m�thode si n�cessaire.
     * </p>
     * 
     * @param tiers
     *            le tiers dont on veut r�cup�rer la date de d�c�s.
     * 
     * @return la date de d�c�s du tiers ou cha�ne vide si non renseign�es.
     */
    protected String getDateDeces(PRTiersWrapper tiers) {
        return tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);
    }

    /**
     * retourne la date de d�p�t
     * 
     * @return la valeur courante de l'attribut dateDepot
     */
    public abstract String getDateDepot();

    /**
     * retourne la date de traitement
     * 
     * @return la valeur courante de l'attribut dateTraitement
     */
    public abstract String getDateTraitement();

    /**
     * getter pour l'attribut delimiteur champs.
     * 
     * @return la valeur courante de l'attribut delimiteur champs (par d�faut "\t")
     */
    public String getDelimiteurChamps() {
        return delimiteurChamps;
    }

    /**
     * retourne le code sur 3 positions de l'office AI du tiers mentionn�.
     * 
     * <p>
     * Par d�faut, cette m�thode retourne "000", la red�finir si l'office AI est n�cessaire.
     * </p>
     * 
     * @param tiers
     *            le tiers dont on veut savoir le code de l'office AI.
     * 
     * @return "000".
     */
    protected String getOfficeAI(PRTiersWrapper tiers) {
        return PRACORConst.CA_CODE_3_VIDE;
    }

    /**
     * getter pour l'attribut session.
     * 
     * @return la valeur courante de l'attribut session
     */
    @Override
    public BSession getSession() {
        return session;
    }

    /**
     * getter pour l'attribut type calcul.
     * 
     * @return la valeur courante de l'attribut type calcul
     */
    public abstract String getTypeCalcul();

    /**
     * retourne le type de la demande (CA_TYPE_DEMANDE...).
     * 
     * @return la valeur courante de l'attribut type demande
     */
    public abstract String getTypeDemande();

    /**
     * setter pour l'attribut delimiteur champs.
     * 
     * @param delimiteurChamps
     *            une nouvelle valeur pour cet attribut
     */
    public void setDelimiteurChamps(String delimiteurChamps) {
        this.delimiteurChamps = delimiteurChamps;
    }

    /**
     * setter pour l'attribut session.
     * 
     * @param session
     *            une nouvelle valeur pour cet attribut
     */
    public void setSession(BSession session) {
        this.session = session;
    }

}
