package globaz.ij.acor.adapter.plat;

import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierAssuresPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFichierAssuresPrinter extends PRAbstractFichierAssuresPrinter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final int STATE_DEBUT = -2;
    private static final int STATE_FAMILLE = 2;

    private static final int STATE_FIN = -1;
    private static final int STATE_REQUERANT = 1;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Iterator assures;
    private Object membre;
    private int state = IJFichierAssuresPrinter.STATE_DEBUT;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFichierAssuresPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public IJFichierAssuresPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private IJACORPrononceAdapter adapter() {
        return (IJACORPrononceAdapter) parent;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        switch (state) {
            case STATE_DEBUT:
                state = IJFichierAssuresPrinter.STATE_REQUERANT;

                break;
            case STATE_REQUERANT:
                state = IJFichierAssuresPrinter.STATE_FAMILLE;

                break;
        }

        switch (state) {
            case STATE_REQUERANT:
                membre = adapter().requerant(adapter().getDateDeterminante());

                return true;
            case STATE_FAMILLE:
                if (assures == null) {
                    assures = adapter().famille();
                }

                if (assures.hasNext()) {
                    membre = assures.next();

                    return true;
                } else {
                    state = IJFichierAssuresPrinter.STATE_FIN;
                }

                break;
        }

        return false;
    }

    /**
     * 
     * Inscrit les 9 premiers champs standards pour le fichier ACOR des assures.
     * 
     * @param cmd
     * @param membreO
     * @param officeAI
     * @throws PRACORException
     */

    protected void printDebutLigneAssure(StringBuffer cmd, Object membreO, String officeAI) throws PRACORException {

        if (state != IJFichierAssuresPrinter.STATE_REQUERANT) {

            ISFMembreFamilleRequerant membre = (ISFMembreFamilleRequerant) membreO;

            // Workaround, ACOR impose une date de naissance à tous les membres
            // de la famille.
            // Pour le conjoint inconnu, on force sa date de naissance au
            // 01.01.1970
            String dn = null;
            if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(membre.getIdMembreFamille())) {
                dn = "01.01.1970";
            } else {
                dn = membre.getDateNaissance();
            }

            this.printDebutLigneAssure(cmd, membre.getNss(), membre.getNom(), membre.getPrenom(), membre.getCsSexe(),
                    dn, membre.getDateDeces(), membre.getCsEtatCivil(), membre.getCsNationalite(),
                    membre.getCsCantonDomicile(), officeAI);
        } else {
            ISFMembreFamille membre = (ISFMembreFamille) membreO;

            this.printDebutLigneAssure(cmd, membre.getNss(), membre.getNom(), membre.getPrenom(), membre.getCsSexe(),
                    membre.getDateNaissance(), membre.getDateDeces(), membre.getCsEtatCivil(),
                    membre.getCsNationalite(), membre.getCsCantonDomicile(), officeAI);
        }
    }

    private void printDebutLigneAssure(StringBuffer cmd, String noAVS, String nom, String prenom, String csSexe,
            String dateNaissance, String dateDeces, String csEtatCivil, String csNationalite, String csCantonDomicile,
            String officeAI) {
        // 1. numéro AVS de l'assuré
        this.writeAVS(cmd, noAVS);

        String nomComplet = nom + "," + prenom;
        // Supression des caractères spéciaux dans le nom de l'assure, car si
        // existant
        // le fichier batch généré va s'interrompre, car non supporté par la
        // commande DOS : ECHO
        nomComplet = nomComplet.replace('&', ' ');
        nomComplet = nomComplet.replace('<', ' ');
        nomComplet = nomComplet.replace('>', ' ');
        nomComplet = nomComplet.replace('\'', ' ');
        nomComplet = nomComplet.replace('"', ' ');

        // 2. nom et prénom de l'assuré
        this.writeChaine(cmd, nomComplet);

        // 3. sexe de l'assuré
        this.writeChaine(cmd, PRACORConst.csSexeToAcor(csSexe));

        // 4. date de naissance de l'assuré
        this.writeDate(cmd, dateNaissance);

        // 5. date de décès de l'assuré
        this.writeDate(cmd, dateDeces);

        // 6. etat civil de l'assuré (code RR)
        this.writeEntier(cmd, PRACORConst.csEtatCivilHeraToAcor(getSession(), csEtatCivil));

        // 7. code du pays de nationalité de l'assuré (code OFAS)
        this.writeChaine(cmd, PRACORConst.csEtatToAcor(csNationalite));

        // 8. code pays ou canton de domicile de l'assuré. pour nous: toujours
        // le code canton (code OFAS)
        // TODO: inscrire le code de l'etat de domicile pour les assures
        // domicilies a l'etranger
        this.writeChaine(cmd, PRACORConst.csCantonToAcor(csCantonDomicile));

        // 9. code office AI
        this.writeChaine(cmd, officeAI);
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        this.printDebutLigneAssure(cmd, membre, adapter().getPrononce().getOfficeAI());

        for (int idChamp = 10; idChamp < 24; ++idChamp) {
            this.writeChampVide(cmd);
        }

    }

}
