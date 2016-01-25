package globaz.apg.acor.adapter.plat;

import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Sous-classe permettant la création du fichier PERIODES_APG pour ACOR.
 * </p>
 * 
 * @author vre
 */
public class APFichierPeriodesAPGPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private int idPeriode;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe FichierEnfantsAPGPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    protected APFichierPeriodesAPGPrinter(APACORDroitAPGAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected APAbstractACORDroitAdapter adapter() {
        return (APAbstractACORDroitAdapter) parent;
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
        return idPeriode < adapter().basesCalcul().size();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * 
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        APBaseCalcul baseCalcul = adapter().baseCalcul(idPeriode++);

        // 1. date de début de la période
        writeDate(writer, baseCalcul.getDateDebut().toStr(""));

        // 2. date de fin de la période
        writeDate(writer, baseCalcul.getDateFin().toStr(""));

        // 3. nombre de jours soldés pour cette période
        writeEntier(writer, String.valueOf(baseCalcul.getNombreJoursSoldes()));

        // 4. nombre d'enfants durant la période
        writeEntier(writer, String.valueOf(baseCalcul.getNombreEnfants()));

        // 5. champ vide
        writeChampVide(writer);

        // // 6. statut
        // if (baseCalcul.isSansActiviteLucrative()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_NON_ACTIF);
        // } else {
        // if (baseCalcul.isIndependant() && baseCalcul.isSalarie()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_SALARIE_ET_INDEPENDANT);
        // } else if (baseCalcul.isIndependant()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_INDEPENDANT);
        // } else if (baseCalcul.isSalarie()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_SALARIE);
        // }
        // }

        writeChampVide(writer);

        // 7. indemnité d'exploitation
        // il faut considerer toutes les situation prof.
        Iterator iter = baseCalcul.getBasesCalculSituationProfessionnel().iterator();
        boolean hasAllocationExploitation = false;
        while (iter.hasNext() && !hasAllocationExploitation) {
            APBaseCalculSituationProfessionnel bcsp = (APBaseCalculSituationProfessionnel) iter.next();

            APSituationProfessionnelle sp = new APSituationProfessionnelle();
            sp.setSession(getSession());
            sp.setIdSituationProf(bcsp.getIdSituationProfessionnelle());
            try {
                sp.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sp.getIsAllocationExploitation().booleanValue()) {
                hasAllocationExploitation = true;
            }
        }
        writeOuiNonSansFinDeChamp(writer, hasAllocationExploitation);
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {
        APBaseCalcul baseCalcul = adapter().baseCalcul(idPeriode++);

        // 1. date de début de la période
        writeDate(writer, baseCalcul.getDateDebut().toStr(""));

        // 2. date de fin de la période
        writeDate(writer, baseCalcul.getDateFin().toStr(""));

        // 3. nombre de jours soldés pour cette période
        writeEntier(writer, String.valueOf(baseCalcul.getNombreJoursSoldes()));

        // 4. nombre d'enfants durant la période
        writeEntier(writer, String.valueOf(baseCalcul.getNombreEnfants()));

        // 5. champ vide
        writeChampVide(writer);

        // // 6. statut
        // if (baseCalcul.isSansActiviteLucrative()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_NON_ACTIF);
        // } else {
        // if (baseCalcul.isIndependant() && baseCalcul.isSalarie()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_SALARIE_ET_INDEPENDANT);
        // } else if (baseCalcul.isIndependant()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_INDEPENDANT);
        // } else if (baseCalcul.isSalarie()) {
        // writeEntier(writer, PRACORConst.CA_STATUT_SALARIE);
        // }
        // }

        writeChampVide(writer);

        // 7. indemnité d'exploitation
        // il faut considerer toutes les situation prof.
        Iterator iter = baseCalcul.getBasesCalculSituationProfessionnel().iterator();
        boolean hasAllocationExploitation = false;
        while (iter.hasNext() && !hasAllocationExploitation) {
            APBaseCalculSituationProfessionnel bcsp = (APBaseCalculSituationProfessionnel) iter.next();

            APSituationProfessionnelle sp = new APSituationProfessionnelle();
            sp.setSession(getSession());
            sp.setIdSituationProf(bcsp.getIdSituationProfessionnelle());
            try {
                sp.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sp.getIsAllocationExploitation().booleanValue()) {
                hasAllocationExploitation = true;
            }
        }
        writeOuiNonSansFinDeChamp(writer, hasAllocationExploitation);
    }
}
