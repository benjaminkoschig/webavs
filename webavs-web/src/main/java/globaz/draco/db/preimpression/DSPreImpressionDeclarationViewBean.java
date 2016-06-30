package globaz.draco.db.preimpression;

import globaz.babel.api.ICTDocument;
import globaz.draco.print.itext.Doc2_preImpressionDeclaration;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.translation.CodeSystem;
import java.util.Vector;

/**
 * Permet de controler les valeurs entrées par l'utilisateur
 * 
 */
public class DSPreImpressionDeclarationViewBean extends Doc2_preImpressionDeclaration implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String langueDoc;

    private String typeDefaut = CodeSystem.DECL_SAL_PRE_LISTE;

    public DSPreImpressionDeclarationViewBean() throws java.lang.Exception {
        // Nothing
    }

    /**
     * Retourne les documents possible pour l'impression des plans.
     * 
     * @author sel, 06.07.2007
     * @return le vecteur renseignant la liste déroulante des modèles de GCA60005.
     * @throws Exception
     */
    public Vector getDocumentsPossible() throws Exception {
        // Vector pour FWListSelectTag data
        Vector v = new Vector();

        ICTDocument[] candidats;
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        loader.setActif(Boolean.TRUE);
        loader.setCodeIsoLangue(getLangue());
        loader.setCsDomaine(Doc2_preImpressionDeclaration.CS_DOMAINE);
        loader.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_ATTESTATION);
        loader.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        loader.setDefault(new Boolean(true));
        // Charge les documents correspondant aux critères définis
        candidats = loader.load();
        if (candidats != null) {
            String line[];
            for (int i = 0; i < candidats.length; i++) {
                line = new String[2];
                line[0] = candidats[i].getIdDocument();
                line[1] = candidats[i].getNom();
                setIdDocumentDefaut(candidats[i].getIdDocument());
                v.add(line);
            }
        }

        ICTDocument loader2 = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        loader2.setActif(Boolean.TRUE);
        loader2.setCodeIsoLangue(getLangue());
        loader2.setCsDomaine(Doc2_preImpressionDeclaration.CS_DOMAINE);
        loader2.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_ATTESTATION);
        loader2.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        loader2.setDefault(new Boolean(false));
        // Charge les documents correspondant aux critères définis
        candidats = loader2.load();

        if (candidats != null) {
            String line2[];
            for (int i = 0; i < candidats.length; i++) {
                line2 = new String[2];
                line2[0] = candidats[i].getIdDocument();
                line2[1] = candidats[i].getNom();
                v.add(line2);
            }
        }
        return v;
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

    public String getTypeDefaut() {
        return typeDefaut;
    }

    public void setTypeDefaut(String typeDefaut) {
        this.typeDefaut = typeDefaut;
    }
}
