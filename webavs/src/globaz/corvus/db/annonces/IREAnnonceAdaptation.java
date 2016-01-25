package globaz.corvus.db.annonces;

/**
 * Fonctionnalités communes aux annonces 51 et 53
 */
public interface IREAnnonceAdaptation {

    public static final String CODE_APPLICATION_ANNONCE_51 = "51";
    public static final String CODE_APPLICATION_ANNONCE_53 = "53";

    /**
     * @return le code prestation ou genre de prestation de la rente liée à cette annonce
     */
    public String getGenrePrestation();

    /**
     * @return l'ID en base de donnée de l'annonce
     */
    public String getIdAnnonce01();

    /**
     * @return le NSS du titulaire de cette annonce
     */
    public String getNss();

    /**
     * @return le code application d'annonce, permettant de faire la différence entre une annonce 51 et 53. Contiendra
     *         la valeur "51" pour une annonce 51 ou la valeur "53" pour une annonce 53
     */
    public String getCodeApplicationAnnonce();

    /**
     * @return l'ancien montant de la prestation, avant augmentation
     */
    public String getAncienMontantMensuel();

    /**
     * @return le montant actuel de la prestation, après augmentation
     */
    public String getMontantPrestation();

    public String getCodeCasSpecial1();

    public String getCodeCasSpecial2();

    public String getCodeCasSpecial3();

    public String getCodeCasSpecial4();

    public String getCodeCasSpecial5();
}