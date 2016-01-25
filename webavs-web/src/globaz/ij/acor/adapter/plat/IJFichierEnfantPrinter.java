package globaz.ij.acor.adapter.plat;

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
 * @author vre
 */
public class IJFichierEnfantPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Iterator enfants;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFichierEnfantPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public IJFichierEnfantPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
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
     * 
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        ISFMembreFamilleRequerant enfant = (ISFMembreFamilleRequerant) enfants.next();
        ISFEnfant detail;

        try {
            detail = adapter().situationFamiliale(adapter().idTiersAssure()).getEnfant(enfant.getIdMembreFamille());
        } catch (Exception e) {
            throw new PRACORException("impossible de trouver les details sur l'enfant", e);
        }

        // 1. le no AVS de l'enfant
        writeAVS(writer, enfant.getNss());

        if (adapter().requerant().getNss().equals(detail.getNoAvsMere())) {
            // 2. le no AVS du pere
            writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsPere(), PRACORConst.CS_HOMME,
                            detail.getNomPere() + detail.getPrenomPere(), true));

            // 3. le no AVS de la mere
            writeAVS(writer, detail.getNoAvsMere());
        } else {
            // 2. le no AVS du pere
            writeAVS(writer, detail.getNoAvsPere());

            // 3. le no AVS de la mere
            writeAVS(
                    writer,
                    adapter().nssBidon(detail.getNoAvsMere(), PRACORConst.CS_FEMME,
                            detail.getNomMere() + detail.getPrenomMere(), true));
        }

        // 4. enfant recueilli
        writeBoolean(writer, detail.isRecueilli());

        // 5. date d'adoption
        writeDate(writer, detail.getDateAdoption());

        // 6. no AVS de la personne pour qui l'enfant est a charge
        // (l'utilisateur devra le saisir dans ACOR)
        writeAVSSansFinDeChamp(writer, "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.lang.StringBuffer )
     */
    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        ISFMembreFamilleRequerant enfant = (ISFMembreFamilleRequerant) enfants.next();
        ISFEnfant detail;

        try {
            detail = adapter().situationFamiliale(adapter().idTiersAssure()).getEnfant(enfant.getIdMembreFamille());
        } catch (Exception e) {
            throw new PRACORException("impossible de trouver les details sur l'enfant", e);
        }

        // 1. le no AVS de l'enfant
        writeAVS(cmd, enfant.getNss());

        if (adapter().requerant().getNss().equals(detail.getNoAvsMere())) {
            // 2. le no AVS du pere
            writeAVS(
                    cmd,
                    adapter().nssBidon(detail.getNoAvsPere(), PRACORConst.CS_HOMME,
                            detail.getNomPere() + detail.getPrenomPere(), true));

            // 3. le no AVS de la mere
            writeAVS(cmd, detail.getNoAvsMere());
        } else {
            // 2. le no AVS du pere
            writeAVS(cmd, detail.getNoAvsPere());

            // 3. le no AVS de la mere
            writeAVS(
                    cmd,
                    adapter().nssBidon(detail.getNoAvsMere(), PRACORConst.CS_FEMME,
                            detail.getNomMere() + detail.getPrenomMere(), true));
        }

        // 4. enfant recueilli
        writeBoolean(cmd, detail.isRecueilli());

        // 5. date d'adoption
        writeDate(cmd, detail.getDateAdoption());

        // 6. no AVS de la personne pour qui l'enfant est a charge
        // (l'utilisateur devra le saisir dans ACOR)
        writeAVSSansFinDeChamp(cmd, "");
    }
}
