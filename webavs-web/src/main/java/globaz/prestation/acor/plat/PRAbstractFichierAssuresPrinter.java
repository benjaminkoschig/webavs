package globaz.prestation.acor.plat;

import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe de base permettant l'écriture des 9 premiers champs des lignes des fichiers ACOR concernant les assurés. Ces
 * champs sont communs à tous les types de prestations (rentes, IJ, APG).
 * </p>
 * 
 * @author vre
 */
public abstract class PRAbstractFichierAssuresPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe FichierDemGdoWriter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    protected PRAbstractFichierAssuresPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param writer
     *            DOCUMENT ME!
     * @param tiers
     *            DOCUMENT ME!
     */
    // protected void printDebutLigneAssure(PrintWriter writer, PRTiersWrapper tiers) {
    // String caEtatDomicile = PRACORConst.csEtatToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));
    //
    // this.printDebutLigneAssure(
    // writer,
    // tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
    // tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
    // + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
    // tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE),
    // tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
    // this.parent.getDateDeces(tiers),
    // PRACORConst.csEtatCivilToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL)),
    // PRACORConst.csEtatToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS)),
    // PRACORConst.CA_SUISSE.equals(caEtatDomicile) ? PRACORConst.csCantonToAcor(tiers
    // .getProperty(PRTiersWrapper.PROPERTY_ID_CANTON)) : caEtatDomicile, this.parent
    // .getOfficeAI(tiers));
    // }

    /**
     * Inscrit les 9 premiers champs standards pour le fichier ACOR des assures.
     * 
     * @param writer
     *            le writer.
     * @param noAVS
     *            no
     * @param nomComplet
     *            DOCUMENT ME!
     * @param caSexe
     *            DOCUMENT ME!
     * @param dateNaissance
     *            DOCUMENT ME!
     * @param dateDeces
     *            DOCUMENT ME!
     * @param caEtatCivil
     *            DOCUMENT ME!
     * @param caNationalite
     *            DOCUMENT ME!
     * @param caCanton
     *            DOCUMENT ME!
     * @param officeAI
     *            DOCUMENT ME!
     */
    // private void printDebutLigneAssure(PrintWriter writer, String noAVS, String nomComplet, String caSexe,
    // String dateNaissance, String dateDeces, String caEtatCivil, String caNationalite, String caCanton,
    // String officeAI) {
    //
    // // Supression des caractères spéciaux dans le nom de l'assure, car si
    // // existant
    // // le fichier batch généré va s'interrompre, car non supporté par la
    // // commande DOS : ECHO
    // nomComplet = nomComplet.replace('&', ' ');
    // nomComplet = nomComplet.replace('<', ' ');
    // nomComplet = nomComplet.replace('>', ' ');
    // nomComplet = nomComplet.replace('\'', ' ');
    // nomComplet = nomComplet.replace('"', ' ');
    //
    // // 1. numéro AVS de l'assuré
    // this.writeAVS(writer, noAVS);
    //
    // // 2. nom et prénom de l'assuré
    // this.writeChaine(writer, nomComplet);
    //
    // // 3. sexe de l'assuré
    // // Si le sexe n'est pas renseigné, par défaut retourne HOMME. Non
    // // bloquant pour le calcul ACOR
    // this.writeChaine(writer, PRACORConst.csSexeToAcor(caSexe));
    //
    // // 4. date de naissance de l'assuré
    // this.writeDate(writer, dateNaissance);
    //
    // // 5. date de décès de l'assuré
    // this.writeDate(writer, dateDeces);
    //
    // // 6. etat civil de l'assuré (code RR)
    // this.writeEntier(writer, caEtatCivil);
    //
    // // 7. code du pays de nationalité de l'assuré (code OFAS)
    // this.writeChaine(writer, caNationalite);
    //
    // // 8. code pays ou canton de domicile de l'assuré. pour nous: toujours
    // // le code canton (code OFAS)
    // this.writeChaine(writer, caCanton);
    //
    // // 9. code office AI
    // this.writeChaine(writer, officeAI);
    // }

    protected void printDebutLigneAssure(StringBuffer cmd, PRTiersWrapper tiers) {
        String caEtatDomicile = PRACORConst.csEtatToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));

        this.printDebutLigneAssure(
                cmd,
                tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE),
                tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                parent.getDateDeces(tiers),
                PRACORConst.csEtatCivilToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL)),
                PRACORConst.csEtatToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS)),
                PRACORConst.CA_SUISSE.equals(caEtatDomicile) ? PRACORConst.csCantonToAcor(tiers
                        .getProperty(PRTiersWrapper.PROPERTY_ID_CANTON)) : caEtatDomicile, parent.getOfficeAI(tiers));
    }

    /**
     * Inscrit les 9 premiers champs standards pour le fichier ACOR des assures en utilisant le npa donné pour la
     * détermination du canton de domicile.
     * 
     * @param writer
     *            le writer.
     * @param tiers
     *            le tiers pour cette demande.
     * @param csEtatDomicile
     *            DOCUMENT ME!
     * @param npaDomicile
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected void printDebutLigneAssure(StringBuffer writer, PRTiersWrapper tiers, String csEtatDomicile,
            String npaDomicile) throws PRACORException {
        String caEtatDomicile = PRACORConst.csEtatToAcor(csEtatDomicile);

        try {
            this.printDebutLigneAssure(
                    writer,
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    parent.getDateDeces(tiers),
                    PRACORConst.csEtatCivilToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL)),
                    PRACORConst.csEtatToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS)),
                    PRACORConst.CA_SUISSE.equals(caEtatDomicile) ? PRACORConst.csCantonToAcor(PRTiersHelper.getCanton(
                            getSession(), npaDomicile)) : caEtatDomicile, parent.getOfficeAI(tiers));
        } catch (Exception e) {
            // le npa doit etre renseigne
            throw new PRACORException("impossible de trouver le canton", e);
        }
    }

    /**
     * Inscrit les 9 premiers champs standards pour le fichier ACOR des assures.
     * 
     * @param writer
     *            le writer.
     * @param noAVS
     *            le tiers pour cette demande.
     * @param dateNaissance
     *            DOCUMENT ME!
     * @param prenom
     *            DOCUMENT ME!
     * @param nom
     *            DOCUMENT ME!
     * @param nationaliteInconnue
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected void printDebutLigneAssure(StringBuffer writer, String noAVS, String dateNaissance, String prenom,
            String nom, boolean nationaliteInconnue) throws PRACORException {
        this.printDebutLigneAssure(writer, noAVS, nom + " " + prenom, PRACORConst.CA_HOMME, dateNaissance,
                PRACORConst.CA_DATE_VIDE, PRACORConst.CA_CELIBATAIRE,
                nationaliteInconnue ? PRACORConst.CA_ORIGINE_INCONNU : PRACORConst.CA_CODE_3_VIDE,
                nationaliteInconnue ? PRACORConst.CA_ORIGINE_INCONNU : PRACORConst.CA_CODE_3_VIDE,
                PRACORConst.CA_CODE_3_VIDE);
    }

    private void printDebutLigneAssure(StringBuffer cmd, String noAVS, String nomComplet, String caSexe,
            String dateNaissance, String dateDeces, String caEtatCivil, String caNationalite, String caCanton,
            String officeAI) {

        // Supression des caractères spéciaux dans le nom de l'assure, car si
        // existant
        // le fichier batch généré va s'interrompre, car non supporté par la
        // commande DOS : ECHO
        nomComplet = nomComplet.replace('&', ' ');
        nomComplet = nomComplet.replace('<', ' ');
        nomComplet = nomComplet.replace('>', ' ');
        nomComplet = nomComplet.replace('\'', ' ');
        nomComplet = nomComplet.replace('"', ' ');

        // 1. numéro AVS de l'assuré
        this.writeAVS(cmd, noAVS);

        // 2. nom et prénom de l'assuré
        this.writeChaine(cmd, nomComplet);

        // 3. sexe de l'assuré
        // Si le sexe n'est pas renseigné, par défaut retourne HOMME. Non
        // bloquant pour le calcul ACOR
        this.writeChaine(cmd, PRACORConst.csSexeToAcor(caSexe));

        // 4. date de naissance de l'assuré
        this.writeDate(cmd, dateNaissance);

        // 5. date de décès de l'assuré
        this.writeDate(cmd, dateDeces);

        // 6. etat civil de l'assuré (code RR)
        this.writeEntier(cmd, caEtatCivil);

        // 7. code du pays de nationalité de l'assuré (code OFAS)
        this.writeChaine(cmd, caNationalite);

        // 8. code pays ou canton de domicile de l'assuré. pour nous: toujours
        // le code canton (code OFAS)
        this.writeChaine(cmd, caCanton);

        // 9. code office AI
        this.writeChaine(cmd, officeAI);
    }

    /**
     * Inscrit les 9 premiers champs standards pour le fichier ACOR des assures en utilisant le npa donné pour la
     * détermination du canton de domicile.
     * 
     * Ici le sexe vaut tout le temps CS_FEMME. ACORD ne supporte pas les couples de même sexe et les requérants hommes.
     * Alors que pour le droit à l'adoption genevois (LaMat), un homme peut faire la demande lors d'une adoption.
     * 
     * @param writer
     *            le writer.
     * @param tiers
     *            le tiers pour cette demande.
     * @param csEtatDomicile
     *            DOCUMENT ME!
     * @param npaDomicile
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected void printDebutLigneAssureWithSexeF(StringBuffer writer, PRTiersWrapper tiers, String csEtatDomicile,
            String npaDomicile) throws PRACORException {
        String caEtatDomicile = PRACORConst.csEtatToAcor(csEtatDomicile);

        try {
            this.printDebutLigneAssure(
                    writer,
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    PRACORConst.CS_FEMME,
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    parent.getDateDeces(tiers),
                    PRACORConst.csEtatCivilToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL)),
                    PRACORConst.csEtatToAcor(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS)),
                    PRACORConst.CA_SUISSE.equals(caEtatDomicile) ? PRACORConst.csCantonToAcor(PRTiersHelper.getCanton(
                            getSession(), npaDomicile)) : caEtatDomicile, parent.getOfficeAI(tiers));
        } catch (Exception e) {
            // le npa doit etre renseigne
            throw new PRACORException("impossible de trouver le canton", e);
        }
    }

}
