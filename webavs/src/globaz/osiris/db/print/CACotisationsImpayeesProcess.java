package globaz.osiris.db.print;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.comptecourant.CACotisationsImpayees;
import globaz.osiris.db.comptecourant.CACotisationsImpayeesManager;
import globaz.osiris.print.list.CACotisationsImpayeesExcelList;
import java.math.BigDecimal;

public class CACotisationsImpayeesProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValue;
    /**
     * Secteur : 4 premiers chiffres de la rubrique. <br>
     * Exemple : Secteur 2 = 2000.xxxx.xxxx
     */
    private double secteur2, secteur4_8, secteur9, total;
    private double[] tab_ante = new double[4];
    private double[] tab_revue = new double[4];

    private double[] tab_tot = new double[4];

    /**
     * Constructeur CICompteIndividuelProcess.
     */
    public CACotisationsImpayeesProcess() {
        super();
    }

    /**
     * Constructeur CICompteIndividuelProcess.
     */
    public CACotisationsImpayeesProcess(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        CACotisationsImpayees aff = null;
        CACotisationsImpayeesManager mgr_aff = new CACotisationsImpayeesManager();
        setProgressScaleValue(10);

        mgr_aff.setSession(getSession());
        mgr_aff.setForDateCG(getDateValue());
        mgr_aff.setForDateAnterieur(false);
        BStatement statement = mgr_aff.cursorOpen(getTransaction());

        try {
            // Sous revue
            while ((aff = (CACotisationsImpayees) mgr_aff.cursorReadNext(statement)) != null) {
                if (isAborted()) {
                    return false;
                }
                calculSecteur(aff.getIdexterne(), aff.getMontant());
            }

            incProgressCounter();

            secteur();
            tab_revue[0] = secteur2;
            tab_revue[1] = secteur4_8;
            tab_revue[2] = secteur9;
            tab_revue[3] = total;

            aff = null;
            mgr_aff.clear();
            clearSecteur();
            mgr_aff.cursorClose(statement);
            mgr_aff.setForDateAnterieur(true);
            mgr_aff.setForDateCG(getDateValue());
            statement = mgr_aff.cursorOpen(getTransaction());

            incProgressCounter();

            // Anterieur
            while ((aff = (CACotisationsImpayees) mgr_aff.cursorReadNext(statement)) != null) {
                if (isAborted()) {
                    return false;
                }
                calculSecteur(aff.getIdexterne(), aff.getMontant());
            }

            incProgressCounter();

            secteur();
            tab_ante[0] = secteur2;
            tab_ante[1] = secteur4_8;
            tab_ante[2] = secteur9;
            tab_ante[3] = total;

            for (int i = 0; (i <= 3) && !isAborted(); i++) {
                tab_tot[i] = round((tab_revue[i] + tab_ante[i]), 2);
            }

            incProgressCounter();
            if (isAborted()) {
                return false;
            }

            // arrondie à deux chiffres après la virugle
            for (int i = 0; (i <= 3) && !isAborted(); i++) {
                tab_revue[i] = round(tab_revue[i], 2);
            }

            incProgressCounter();
            if (isAborted()) {
                return false;
            }

            for (int i = 0; (i <= 3) && !isAborted(); i++) {
                tab_ante[i] = round(tab_ante[i], 2);
            }

            incProgressCounter();

            // affichage
            CACotisationsImpayeesExcelList list = new CACotisationsImpayeesExcelList(getSession());
            list.setDocumentInfo(createDocumentInfo());

            // Poursuite
            mgr_aff.clear();
            mgr_aff.cursorClose(statement);
            mgr_aff.setForDateCG(getDateValue());
            mgr_aff.setForPoursuite(true);
            mgr_aff.setForSursis(false);
            mgr_aff.find(getTransaction());
            double montantPoursuite = round(
                    div(Double.parseDouble(((CACotisationsImpayees) mgr_aff.getFirstEntity()).getMontant())), 2);
            list.setPoursuite(montantPoursuite);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }

            // Sursis
            mgr_aff.clear();
            mgr_aff.setForDateCG(getDateValue());
            mgr_aff.setForSursis(true);
            mgr_aff.setForPoursuite(false);
            mgr_aff.find(getTransaction());
            double montantSursis = round(
                    div(Double.parseDouble(((CACotisationsImpayees) mgr_aff.getFirstEntity()).getMontant())), 2);
            list.setSursis(montantSursis);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }

            list.populateSheetListe(tab_revue, tab_ante, tab_tot, getDateValue());

            incProgressCounter();

            if (!isAborted()) {
                this.registerAttachedDocument(list.getDocumentInfo(), list.getOutputFile());
            }

            incProgressCounter();

        } catch (Exception e) {
            e.printStackTrace();
            this._addError(getTransaction(), "Erreur");
        } finally {
            mgr_aff.cursorClose(statement);
        }
        return false;
    }

    /**
     * Total par secteur.
     * 
     * @param ID
     *            : rubrique pour déterminer le secteur.
     * @param Montant
     */
    public void calculSecteur(String ID, String Montant) {
        ID = ID.substring(0, 3); // Prend les premiers chiffres de la rubrique pour le secteur.

        int id = Integer.parseInt(ID);
        switch (id) {
            case 200:
                secteur2 += Double.parseDouble(Montant);
                break;
            case 900:
                secteur9 += Double.parseDouble(Montant);
                break;
            default:
                secteur4_8 += Double.parseDouble(Montant);
                break;
        }
    }

    /**
     * Réinitialise les membres (secteurs et total).
     */
    public void clearSecteur() {
        total = 0;
        secteur2 = 0;
        secteur4_8 = 0;
        secteur9 = 0;
    }

    /**
     * Divise par 1'000'000 le montant passé en paramètre et l'arrondi.
     * 
     * @param Montant
     *            à diviser et arrondir.
     * @return le montant divisé par 1'000'000 et arrondi à 8 chiffres après la virgule.
     */
    public double div(double num) {
        num /= Math.pow(10, 6);
        num = round(num, 8);
        return num;
    }

    /**
     * @return
     */
    public String getDateValue() {
        return dateValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (!isOnError() && !isAborted()) {
            return "Le process  s'est effectué avec succès!";
        }
        return "Le process a échoué!";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Arrondi un montant.
     * 
     * @param num
     *            : Montant à arrondir
     * @param scale
     *            : Nombre de chiffre après la virgule
     * @return Le montant arrondi.
     */
    public double round(double num, int scale) {
        BigDecimal bd = new BigDecimal(num);
        BigDecimal bd2 = bd.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        num = bd2.doubleValue();
        return num;
    }

    /**
     * Calcule le totale des secteurs et divise par 1'000'000
     */
    public void secteur() {
        total = secteur2 + secteur4_8 + secteur9;
        secteur2 = div(secteur2);
        secteur4_8 = div(secteur4_8);
        secteur9 = div(secteur9);
        total = div(total);
    }

    /**
     * @param dateValue
     */
    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }
}
