package ch.globaz.perseus.business.models.demande;

import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;

/**
 * Classe pour les demandes de prestations complémentaires familles
 * 
 * @author DDE
 * 
 */
public class Demande extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Dossier dossier = null;
    private SimpleDemande simpleDemande = null;
    private SituationFamiliale situationFamiliale = null;

    public Demande() {
        super();
        simpleDemande = new SimpleDemande();
        dossier = new Dossier();
        situationFamiliale = new SituationFamiliale();
    }

    public String getCreationDate() {
        return (new BSpy(simpleDemande.getCreationSpy())).getDate();
    }

    /**
     * @return the dossier
     */
    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return simpleDemande.getId();
    }

    /**
     * @return the listCsAutresDemandes
     */
    public List<String> getListCsAutresDemandes() {
        ArrayList<String> list = new ArrayList<String>();
        if (!JadeStringUtil.isEmpty(simpleDemande.getListCsAutresDemandes())) {
            StringTokenizer strToken = new StringTokenizer(simpleDemande.getListCsAutresDemandes(), ",");
            while (strToken.hasMoreElements()) {
                list.add(strToken.nextToken());
            }
        }
        return list;
    }

    /**
     * @return the listCsAutresPrestations
     */
    public List<String> getListCsAutresPrestations() {
        ArrayList<String> list = new ArrayList<String>();
        if (!JadeStringUtil.isEmpty(simpleDemande.getListCsAutresPrestations())) {
            StringTokenizer strToken = new StringTokenizer(simpleDemande.getListCsAutresPrestations(), ",");
            while (strToken.hasMoreElements()) {
                list.add(strToken.nextToken());
            }
        }
        return list;
    }

    /**
     * @return the demande
     */
    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    /**
     * @return the situationFamiliale
     */
    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    @Override
    public String getSpy() {
        return simpleDemande.getSpy();
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        simpleDemande.setId(id);

    }

    /**
     * @param listCsAutresDemandes
     *            the listCsAutresDemandes to set
     */
    public void setListCsAutresDemandes(List<String> listCsAutresDemandes) {
        boolean isFirst = true;
        String listeComma = "";
        for (String str : listCsAutresDemandes) {
            if (isFirst) {
                listeComma += str;
                isFirst = false;
            } else {
                listeComma += "," + str;
            }
        }

        simpleDemande.setListCsAutresDemandes(listeComma);
    }

    /**
     * @param listCsAutresPrestations
     *            the listCsAutresPrestations to set
     */
    public void setListCsAutresPrestations(List<String> listCsAutresPrestations) {
        boolean isFirst = true;
        String listeComma = "";
        for (String str : listCsAutresPrestations) {
            if (isFirst) {
                listeComma += str;
                isFirst = false;
            } else {
                listeComma += "," + str;
            }
        }

        simpleDemande.setListCsAutresPrestations(listeComma);
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setSimpleDemande(SimpleDemande demande) {
        simpleDemande = demande;
    }

    /**
     * @param situationFamiliale
     *            the situationFamiliale to set
     */
    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    @Override
    public void setSpy(String spy) {
        simpleDemande.setSpy(spy);
    }

}
