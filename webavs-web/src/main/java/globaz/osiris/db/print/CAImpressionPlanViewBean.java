package globaz.osiris.db.print;

import globaz.babel.api.ICTDocument;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.log.JadeLogger;
import globaz.osiris.process.CAProcessImpressionPlan;
import globaz.osiris.translation.CACodeSystem;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * @author kurkus, 30 mai 05
 */
public class CAImpressionPlanViewBean extends CAProcessImpressionPlan implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Champs pour le catalogue de textes
    private String idDocument = "";
    private String langueDoc;

    /**
     * @throws Exception
     */
    public CAImpressionPlanViewBean() throws Exception {
        super();
    }

    /**
     * Retourne les documents possible pour l'impression des plans.
     * 
     * @author sel, 06.07.2007
     * @return le vecteur renseignant la liste déroulante des modèles de GCA60005.
     * @throws Exception
     * @throws Exception
     */
    public Vector getDocumentsPossible() {
        // Vector pour FWListSelectTag data
        Vector v = new Vector();

        ICTDocument[] candidats = null;
        if (candidats == null) {
            ICTDocument loader = null;
            try {
                loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            } catch (Exception e1) {
                JadeLogger.error(this, e1);
                this._addError(getSession().getCurrentThreadTransaction(), e1.toString());
                e1.printStackTrace();
                return v;
            }

            loader.setActif(Boolean.TRUE);
            loader.setCodeIsoLangue(getLangue());
            loader.setCsDomaine(CACodeSystem.CS_DOMAINE_CA);
            loader.setCsTypeDocument(CACodeSystem.CS_TYPE_SURSIS_DECISION);
            try {
                // Charge les documents correspondant aux critères définis
                candidats = loader.load();
            } catch (Exception e) {
                JadeLogger.error(this, e);
                this._addError(getSession().getCurrentThreadTransaction(), e.toString());
                return v;
            }

            // Si aucun document ne correspond, on retourne une liste vide pour
            // ne pas faire planter la page.
            if ((candidats == null) || (candidats.length <= 0)) {
                return v;
            }

            String line[];
            for (int i = 0; i < candidats.length; i++) {
                line = new String[2];
                line[0] = candidats[i].getIdDocument();
                line[1] = candidats[i].getNom();
                v.add(line);
            }
            // Permet de trier le vecteur en fonction du nom du document.
            Collections.sort(v, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ((String[]) o1)[1].compareTo(((String[]) o2)[1]);
                }
            });
        }
        return v;
    }

    /**
     * @return the idDocument
     */
    @Override
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * Retourne le code iso de la langue pour ce document.
     * 
     * @return la valeur courante de l'attribut langue
     */
    protected String getLangue() {
        if (langueDoc == null) {
            langueDoc = getSession().getIdLangueISO();
        }

        return langueDoc;
    }

    /**
     * @param idDocument
     *            the idDocument to set
     */
    @Override
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

}
