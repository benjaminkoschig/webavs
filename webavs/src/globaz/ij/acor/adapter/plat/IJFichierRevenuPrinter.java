package globaz.ij.acor.adapter.plat;

import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJGrandeIJ;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJRevenu;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.module.IJSalaireFilter;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * un printer qui inscrit soit les situations professionnelles pour une grande ij soit le dernier revenu déterminant
 * pour une petite ij.
 * </p>
 * 
 * @author vre
 */
public class IJFichierRevenuPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private HashMap comptes;
    private boolean fini;
    private HashMap indices;
    private Iterator sitPros;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFichierRevenuPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public IJFichierRevenuPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private IJACORPrononceAdapter adapter() {
        return (IJACORPrononceAdapter) parent;
    }

    private String cle(IJEmployeur employeur) {
        return employeur.getIdAffilie() + "_" + employeur.getIdTiers();
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
        if (adapter().getPrononce() instanceof IJGrandeIJ) {
            return situationsProfessionnelles().hasNext();
        } else {
            try {
                return !fini && (adapter().getPrononce().loadRevenuReadaptation(null) != null);
            } catch (Exception e) {
                throw new PRACORException("impossible de charger le revenu", e);
            }
        }
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public void printLigne(PrintWriter writer) throws PRACORException {
        if (adapter().getPrononce() instanceof IJGrandeIJ) {
            printLigneGrandeIJ(writer);
        } else {
            printLignePetiteIJ(writer);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.lang.StringBuffer )
     */
    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        if (adapter().getPrononce() instanceof IJGrandeIJ) {
            printLigneGrandeIJ(cmd);
        } else {
            printLignePetiteIJ(cmd);
        }
    }

    /**
     * inscrit une ligne d'une situation professionnelle d'une grande ij
     * 
     * @param writer
     * 
     * @throws PRACORException
     * @deprecated
     * 
     */
    @Deprecated
    private void printLigneGrandeIJ(PrintWriter writer) throws PRACORException {
        IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) situationsProfessionnelles().next();

        try {
            IJSalaireFilter salaire = new IJSalaireFilter(getSession(), sitPro);

            // pour forcer un salaire mensuel (ACOR n'aime pas quand c'est pas
            // mensuel)
            // desactive car erreur arrondi:
            // salaire.promouvoirSalaire(IIJSituationProfessionnelle.CS_MENSUEL);

            // 1. le revenu
            writeReel(writer, salaire.getMontantSalaireArrondi());

            // 2. l'année correspondante
            writeEntier(writer, sitPro.getAnneeCorrespondante());

            // 3. le type de salaire
            writeEntier(writer,
                    PRACORConst.csPeriodiciteSalaireIJToAcor(getSession(), salaire.getCsPeriodiciteSalaire()));

            // 4. le nombre d'heure par semaine
            writeEntier(writer, salaire.getNombreHeuresSemaines());

            // 5. numero d'affilie ou le no avs de l'assure si independant
            writeNumAffilie(writer, sitPro.loadEmployeur().loadNumero());

            // 6. le nom de l'employeur
            writeChaineSansFinDeChamp(writer, sitPro.loadEmployeur().loadNom());

            // suffixer avec un indice s'il y a plusieurs contrats avec cet
            // employeur.
            String cle = cle(sitPro.loadEmployeur());
            Integer compte = (Integer) comptes.get(cle);

            if ((compte != null) && (compte.intValue() > 1)) {
                if (indices == null) {
                    indices = new HashMap();
                }

                Integer indice = (Integer) indices.get(cle);

                if (indice == null) {
                    indice = new Integer(1);
                } else {
                    indice = new Integer(indice.intValue() + 1);
                }

                indices.put(cle, indice);

                writeChaine(writer, " (" + indice.intValue() + ")");
            }
        } catch (PRACORException e) {
            throw e;
        } catch (Exception e) {
            throw new PRACORException("impossible de charger les situations professionnelles", e);
        }
    }

    /**
     * inscrit une ligne d'une situation professionnelle d'une grande ij
     * 
     * @param cmd
     * @throws PRACORException
     */
    private void printLigneGrandeIJ(StringBuffer cmd) throws PRACORException {
        IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) situationsProfessionnelles().next();

        try {
            IJSalaireFilter salaire = new IJSalaireFilter(getSession(), sitPro);

            // pour forcer un salaire mensuel (ACOR n'aime pas quand c'est pas
            // mensuel)
            // desactive car erreur arrondi:
            // salaire.promouvoirSalaire(IIJSituationProfessionnelle.CS_MENSUEL);

            // 1. le revenu
            writeReel(cmd, salaire.getMontantSalaireArrondi());

            // 2. l'année correspondante
            writeEntier(cmd, sitPro.getAnneeCorrespondante());

            // 3. le type de salaire
            writeEntier(cmd, PRACORConst.csPeriodiciteSalaireIJToAcor(getSession(), salaire.getCsPeriodiciteSalaire()));

            // 4. le nombre d'heure par semaine
            writeEntier(cmd, salaire.getNombreHeuresSemaines());

            // 5. numero d'affilie ou le no avs de l'assure si independant
            writeNumAffilie(cmd, sitPro.loadEmployeur().loadNumero());

            // 6. le nom de l'employeur
            writeChaineSansFinDeChamp(cmd, sitPro.loadEmployeur().loadNom());

            // suffixer avec un indice s'il y a plusieurs contrats avec cet
            // employeur.
            String cle = cle(sitPro.loadEmployeur());
            Integer compte = (Integer) comptes.get(cle);

            if ((compte != null) && (compte.intValue() > 1)) {
                if (indices == null) {
                    indices = new HashMap();
                }

                Integer indice = (Integer) indices.get(cle);

                if (indice == null) {
                    indice = new Integer(1);
                } else {
                    indice = new Integer(indice.intValue() + 1);
                }

                indices.put(cle, indice);

                writeChaine(cmd, " (" + indice.intValue() + ")");
            }
        } catch (PRACORException e) {
            throw e;
        } catch (Exception e) {
            throw new PRACORException("impossible de charger les situations professionnelles", e);
        }
    }

    /**
     * Inscrit le dernier revenu determinant d'une petite ij, il n'y a qu'un revenu et donc qu'une ligne, le revenu est
     * transforme pour qu'il ait la meme periodicite que le revenu durant la readaptation (contrainte ACOR).
     * 
     * @param writer
     * 
     * @throws PRACORException
     * @deprecated
     */
    @Deprecated
    private void printLignePetiteIJ(PrintWriter writer) throws PRACORException {
        try {
            IJRevenu revenu = ((IJPetiteIJ) adapter().getPrononce()).loadRevenu();

            // Dans le cas ou aucun revenu n'a été saisi, on instancie un revenu
            // vide,
            // pour éviter des null pointer exception.
            if (revenu == null) {
                revenu = new IJRevenu();
            }

            IJSalaireFilter salaire = new IJSalaireFilter(getSession(), revenu);

            // tester la compatibilite des periodicites du revenu actuel et
            // dernier revenu.
            IJRevenu revenuReadaptation = adapter().getPrononce().loadRevenuReadaptation(null);

            // point ouvert 00509
            // On ne fait plus de calcul pour avoir des periodes commune entre
            // le revenu actuel et le dernier revenu
            // ACOR donne une erreur et l'utilisateur doit accorder les periodes
            // dans le prononce. Comme ca plus d'erreurs de calcul
            // ni de fautes d'arrondi.
            // if (revenuReadaptation != null) {
            // salaire.promouvoirSalaire(revenuReadaptation.getCsPeriodiciteRevenu());
            // }

            // 1. le revenu
            writeReel(writer, salaire.getMontantSalaireArrondi());

            // 2. l'année correspondante
            writeEntier(writer, revenu.getAnnee());

            // 3. le type de salaire
            writeEntier(writer,
                    PRACORConst.csPeriodiciteSalaireIJToAcor(getSession(), salaire.getCsPeriodiciteSalaire()));

            // 4. le nombre d'heure par semaine
            writeEntier(writer, salaire.getNombreHeuresSemaines());

            // 5. le numero d'affilie ou le numero de l'assure si independant
            writeChaine(writer, PRACORConst.CA_CHAINE_VIDE);

            // 6. le nom de l'employeur
            writeChaine(writer, PRACORConst.CA_CHAINE_VIDE);

            fini = true;
        } catch (Exception e) {
            throw new PRACORException("impossible de charger le revenu", e);
        }
    }

    /**
     * Inscrit le dernier revenu determinant d'une petite ij, il n'y a qu'un revenu et donc qu'une ligne, le revenu est
     * transforme pour qu'il ait la meme periodicite que le revenu durant la readaptation (contrainte ACOR).
     * 
     * @param writer
     * 
     * @throws PRACORException
     * 
     */
    private void printLignePetiteIJ(StringBuffer cmd) throws PRACORException {
        try {
            IJRevenu revenu = ((IJPetiteIJ) adapter().getPrononce()).loadRevenu();

            // Dans le cas ou aucun revenu n'a été saisi, on instancie un revenu
            // vide,
            // pour éviter des null pointer exception.
            if (revenu == null) {
                revenu = new IJRevenu();
            }

            IJSalaireFilter salaire = new IJSalaireFilter(getSession(), revenu);

            // tester la compatibilite des periodicites du revenu actuel et
            // dernier revenu.
            IJRevenu revenuReadaptation = adapter().getPrononce().loadRevenuReadaptation(null);

            // point ouvert 00509
            // On ne fait plus de calcul pour avoir des periodes commune entre
            // le revenu actuel et le dernier revenu
            // ACOR donne une erreur et l'utilisateur doit accorder les periodes
            // dans le prononce. Comme ca plus d'erreurs de calcul
            // ni de fautes d'arrondi.
            // if (revenuReadaptation != null) {
            // salaire.promouvoirSalaire(revenuReadaptation.getCsPeriodiciteRevenu());
            // }

            // 1. le revenu
            writeReel(cmd, salaire.getMontantSalaireArrondi());

            // 2. l'année correspondante
            writeEntier(cmd, revenu.getAnnee());

            // 3. le type de salaire
            writeEntier(cmd, PRACORConst.csPeriodiciteSalaireIJToAcor(getSession(), salaire.getCsPeriodiciteSalaire()));

            // 4. le nombre d'heure par semaine
            writeEntier(cmd, salaire.getNombreHeuresSemaines());

            // 5. le numero d'affilie ou le numero de l'assure si independant
            writeChaine(cmd, PRACORConst.CA_CHAINE_VIDE);

            // 6. le nom de l'employeur
            writeChaine(cmd, PRACORConst.CA_CHAINE_VIDE);

            fini = true;
        } catch (Exception e) {
            throw new PRACORException("impossible de charger le revenu", e);
        }
    }

    private Iterator situationsProfessionnelles() throws PRACORException {
        if (comptes == null) {
            comptes = new HashMap();

            try {
                IJSituationProfessionnelleManager mgr = new IJSituationProfessionnelleManager();

                mgr.setISession(getSession());
                mgr.setForIdPrononce(adapter().getPrononce().getIdPrononce());
                mgr.find();

                // reperer les contrats multiples
                for (Iterator sitPros = mgr.iterator(); sitPros.hasNext();) {
                    IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) sitPros.next();
                    String cle = cle(sitPro.loadEmployeur());
                    Integer compte = (Integer) comptes.get(cle);

                    if (compte != null) {
                        compte = new Integer(compte.intValue() + 1);
                    } else {
                        compte = new Integer(1);
                    }

                    comptes.put(cle, compte);
                }

                sitPros = mgr.iterator();
            } catch (Exception e) {
                throw new PRACORException("Impossible de charger les situations professionnelles", e);
            }
        }

        return sitPros;
    }
}
