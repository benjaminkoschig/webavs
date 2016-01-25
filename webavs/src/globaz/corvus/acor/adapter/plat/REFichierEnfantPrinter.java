package globaz.corvus.acor.adapter.plat;

import globaz.hera.api.ISFEnfant;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REFichierEnfantPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Iterator enfants;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REFichierEnfantPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public REFichierEnfantPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private REACORDemandeAdapter adapter() {
        return (REACORDemandeAdapter) parent;
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
        if (enfants == null) {
            enfants = adapter().enfants();
        }

        return enfants.hasNext();
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public void printLigne(PrintWriter writer) throws PRACORException {
        ISFMembreFamilleRequerant enfant = (ISFMembreFamilleRequerant) enfants.next();
        ISFEnfant detail;

        try {
            detail = adapter().situationFamiliale().getEnfant(enfant.getIdMembreFamille());
        } catch (Exception e) {
            throw new PRACORException(getSession().getLabel("ERREUR_DETAILS_ENFANTS"), e);
        }

        // 1. le no AVS de l'enfant
        this.writeAVS(writer, enfant.getNss());

        if (adapter().requerant().getNss().equals(detail.getNoAvsMere())) {
            // 2. le no AVS du pere
            this.writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsPere(), PRACORConst.CS_HOMME,
                            detail.getNomPere() + detail.getPrenomPere(), true));

            // 3. le no AVS de la mere
            this.writeAVS(writer, detail.getNoAvsMere());
        } else {
            // 2. le no AVS du pere
            this.writeAVS(writer, detail.getNoAvsPere());

            // 3. le no AVS de la mere
            this.writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsMere(), PRACORConst.CS_FEMME,
                            detail.getNomMere() + detail.getPrenomMere(), true));
        }

        // 4. enfant recueilli
        this.writeBoolean(writer, detail.isRecueilli());

        // 5. date d'adoption
        this.writeDate(writer, detail.getDateAdoption());

        // 6. no AVS de la personne pour qui l'enfant est a charge
        // (l'utilisateur devra le saisir dans ACOR)
        this.writeChaineSansFinDeChamp(writer, "0");
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {
        ISFMembreFamilleRequerant enfant = (ISFMembreFamilleRequerant) enfants.next();
        ISFEnfant detail;

        try {
            detail = adapter().situationFamiliale().getEnfant(enfant.getIdMembreFamille());
        } catch (Exception e) {
            throw new PRACORException(getSession().getLabel("ERREUR_DETAILS_ENFANTS"), e);
        }

        // 1. le no AVS de l'enfant
        this.writeAVS(writer, enfant.getNss());

        if (adapter().requerant().getNss().equals(detail.getNoAvsMere())) {
            // 2. le no AVS du pere
            this.writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsPere(), PRACORConst.CS_HOMME,
                            detail.getNomPere() + detail.getPrenomPere(), true));

            // 3. le no AVS de la mere
            // bz-5454
            // writeAVS(writer, detail.getNoAvsMere());
            this.writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsMere(), PRACORConst.CS_FEMME,
                            detail.getNomMere() + detail.getPrenomMere(), true));

        } else {
            // 2. le no AVS du pere
            // bz-5454
            // this.writeAVS(writer, detail.getNoAvsPere());
            this.writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsPere(), PRACORConst.CS_HOMME,
                            detail.getNomPere() + detail.getPrenomPere(), true));

            // 3. le no AVS de la mere
            this.writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsMere(), PRACORConst.CS_FEMME,
                            detail.getNomMere() + detail.getPrenomMere(), true));
        }

        // 4. enfant recueilli
        this.writeBoolean(writer, detail.isRecueilli());

        // 5. date d'adoption
        this.writeDate(writer, detail.getDateAdoption());

        // 6. no AVS de la personne pour qui l'enfant est a charge
        // (l'utilisateur devra le saisir dans ACOR)
        this.writeChaineSansFinDeChamp(writer, "0");

    }
}
