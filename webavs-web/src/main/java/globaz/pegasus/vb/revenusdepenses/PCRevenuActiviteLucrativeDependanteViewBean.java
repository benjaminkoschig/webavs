package globaz.pegasus.vb.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuActiviteDependante;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepensesSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRevenuActiviteLucrativeDependanteViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CS_REQUERANT = "64004001";
    private String employeur = null;

    private List listeFrais = new ArrayList();

    private SimpleTypeFraisObtentionRevenu[] listeSimpleTypeFraisObtentionRevenu = null;

    // création d'un vecteur pour la liste de checkbox
    public Vector getCsFraisObtentionRevenu() throws Exception {

        Vector v = new Vector();
        v.add(new String[] { IPCRevenuActiviteDependante.CS_LIBELLE_TRANSPORT,
                getISession().getCodeLibelle(IPCRevenuActiviteDependante.CS_LIBELLE_TRANSPORT) });
        v.add(new String[] { IPCRevenuActiviteDependante.CS_LIBELLE_REPAS,
                getISession().getCodeLibelle(IPCRevenuActiviteDependante.CS_LIBELLE_REPAS) });
        v.add(new String[] { IPCRevenuActiviteDependante.CS_LIBELLE_VETEMENTS,
                getISession().getCodeLibelle(IPCRevenuActiviteDependante.CS_LIBELLE_VETEMENTS) });
        v.add(new String[] { IPCRevenuActiviteDependante.CS_LIBELLE_LOGEMENT,
                getISession().getCodeLibelle(IPCRevenuActiviteDependante.CS_LIBELLE_LOGEMENT) });
        v.add(new String[] { IPCRevenuActiviteDependante.CS_LIBELLE_AUTRES,
                getISession().getCodeLibelle(IPCRevenuActiviteDependante.CS_LIBELLE_AUTRES) });
        return v;
    }

    public String getTypeEcheance() {
        return EcheanceType.REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE.toString();
    }

    public String getEmployeur() throws Exception {
        return employeur;
    }

    public String getFraisObtentionAssociationInnerJavascript() throws Exception {

        StringBuffer fraisObtentionAssociationInnerJavascript = new StringBuffer();
        Vector fraisObtention = getCsFraisObtentionRevenu();

        for (int i = 0; i < fraisObtention.size(); i++) {
            fraisObtentionAssociationInnerJavascript.append("currentFraisObtention[\"");
            fraisObtentionAssociationInnerJavascript.append(((String[]) fraisObtention.get(i))[0] + "\"]=\"");
            fraisObtentionAssociationInnerJavascript.append(((String[]) fraisObtention.get(i))[1] + "\";");
        }

        return fraisObtentionAssociationInnerJavascript.toString();
    }

    public String getFraisObtentionInnerJavascript() throws Exception {

        StringBuffer fraisObtentionInnerJavascript = new StringBuffer();
        fraisObtentionInnerJavascript.append("var currentFraisObtention=[");
        Vector fraisObtention = getCsFraisObtentionRevenu();

        for (int i = 0; i < fraisObtention.size(); i++) {
            fraisObtentionInnerJavascript.append("\"" + ((String[]) fraisObtention.get(i))[0] + "\"");

            if (i != (fraisObtention.size() - 1)) {
                fraisObtentionInnerJavascript.append(",");
            }
        }

        fraisObtentionInnerJavascript.append("];");

        return fraisObtentionInnerJavascript.toString();
    }

    public List getListeFrais() {
        return listeFrais;
    }

    public String getListFraisObtentionInput() {
        return "";
    }

    public boolean isChecked(String csMotifRefus) {

        if (!JadeStringUtil.isEmpty(getListFraisObtentionInput())) {
            String[] listFraisObtentionCheckedTab = getListFraisObtentionInput().split(",");

            int i = 0;
            while (i < listFraisObtentionCheckedTab.length) {

                if (listFraisObtentionCheckedTab[i].equals(csMotifRefus)) {
                    return true;
                }
                i++;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les données financières
        RevenusDepensesSearch search = new RevenusDepensesSearch();
        search.setForNumeroVersion(getNoVersion());
        search.setForIdDroit(getId());
        search.setWhereKey("forVersionedRevenuActiviteLucrativeDependante");
        search = PegasusServiceLocator.getDroitService().searchRevenusDepenses(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            RevenusDepenses donnee = (RevenusDepenses) it.next();

            // récupération des frais
            SimpleTypeFraisObtentionRevenuSearch searchFrais = new SimpleTypeFraisObtentionRevenuSearch();
            searchFrais.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchFrais.setForIdRevenuActiviteLucrativeDependante(donnee.getRevenuActiviteLucrativeDependante()
                    .getSimpleRevenuActiviteLucrativeDependante().getId());
            searchFrais = PegasusServiceLocator.getDroitService().searchSimpleTypeFraisObtentionRevenu(searchFrais);

            int n = searchFrais.getSearchResults().length;
            listeSimpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu[n];
            String[] checked = new String[n];
            for (int i = 0; i < n; i++) {

                listeSimpleTypeFraisObtentionRevenu[i] = (SimpleTypeFraisObtentionRevenu) searchFrais
                        .getSearchResults()[i];
                checked[i] = listeSimpleTypeFraisObtentionRevenu[i].getCsFraisObtentionRevenu();

            }
            listeFrais.add(checked);
            // fin récup. frais

            if (donnee.getDonneeFinanciere() instanceof RevenuActiviteLucrativeDependante) {
                DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

                List donneesMembre = (List) donnees.get(f.getId());
                if (donneesMembre == null) {
                    donneesMembre = new ArrayList();
                    donnees.put(f.getId(), donneesMembre);
                }
                donneesMembre.add(donnee);
            }
        }

    }

    public void setEmployeur(String employeur) throws Exception {
        this.employeur = employeur;
    }

    public void setListeFrais(List listeFrais) {
        this.listeFrais = listeFrais;
    }

}
