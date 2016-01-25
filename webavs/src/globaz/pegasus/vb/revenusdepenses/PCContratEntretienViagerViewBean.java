package globaz.pegasus.vb.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCContratEntretienViager;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepensesSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCContratEntretienViagerViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // private static final String CS_REQUERANT = "64004001";
    private List listeLibelle = new ArrayList();

    private SimpleLibelleContratEntretienViager[] listeSimpleLibelleContratEntretienViagers = null;

    // création d'un vecteur pour la liste de checkbox
    public Vector getCsLibelle() throws Exception {

        Vector v = new Vector();
        v.add(new String[] { IPCContratEntretienViager.CS_LIBELLE_LOGEMENT,
                getISession().getCodeLibelle(IPCContratEntretienViager.CS_LIBELLE_LOGEMENT) });
        v.add(new String[] { IPCContratEntretienViager.CS_LIBELLE_NOURRITURE,
                getISession().getCodeLibelle(IPCContratEntretienViager.CS_LIBELLE_NOURRITURE) });
        v.add(new String[] { IPCContratEntretienViager.CS_LIBELLE_SOINS,
                getISession().getCodeLibelle(IPCContratEntretienViager.CS_LIBELLE_SOINS) });
        return v;
    }

    public String getLibelleAssociationInnerJavascript() throws Exception {

        StringBuffer libelleAssociationInnerJavascript = new StringBuffer();
        Vector libelle = getCsLibelle();

        for (int i = 0; i < libelle.size(); i++) {
            libelleAssociationInnerJavascript.append("currentLibelle[\"");
            libelleAssociationInnerJavascript.append(((String[]) libelle.get(i))[0] + "\"]=\"");
            libelleAssociationInnerJavascript.append(((String[]) libelle.get(i))[1] + "\";");
        }

        return libelleAssociationInnerJavascript.toString();
    }

    public String getLibelleInnerJavascript() throws Exception {

        StringBuffer libelleInnerJavascript = new StringBuffer();
        libelleInnerJavascript.append("var currentLibelle=[");
        Vector libelle = getCsLibelle();

        for (int i = 0; i < libelle.size(); i++) {
            libelleInnerJavascript.append("\"" + ((String[]) libelle.get(i))[0] + "\"");

            if (i != libelle.size() - 1) {
                libelleInnerJavascript.append(",");
            }
        }

        libelleInnerJavascript.append("];");

        return libelleInnerJavascript.toString();
    }

    public List getListeLibelle() {
        return listeLibelle;
    }

    public String getListLibelleInput() {
        return "";
    }

    public boolean isChecked(String csMotifRefus) {

        if (!JadeStringUtil.isEmpty(getListLibelleInput())) {
            String[] listLibelleCheckedTab = getListLibelleInput().split(",");

            int i = 0;
            while (i < listLibelleCheckedTab.length) {

                if (listLibelleCheckedTab[i].equals(csMotifRefus)) {
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
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedContratEntretienViager");
        search = PegasusServiceLocator.getDroitService().searchRevenusDepenses(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            RevenusDepenses donnee = (RevenusDepenses) it.next();

            // récupération des frais
            SimpleLibelleContratEntretienViagerSearch searchLibelle = new SimpleLibelleContratEntretienViagerSearch();
            searchLibelle.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchLibelle.setForIdContratEntretienViager(donnee.getSimpleContratEntretienViager().getId());
            searchLibelle = PegasusServiceLocator.getDroitService().searchSimpleLibelleContratEntretienViager(
                    searchLibelle);

            int n = searchLibelle.getSearchResults().length;
            listeSimpleLibelleContratEntretienViagers = new SimpleLibelleContratEntretienViager[n];
            String[] checked = new String[n];
            for (int i = 0; i < n; i++) {

                listeSimpleLibelleContratEntretienViagers[i] = (SimpleLibelleContratEntretienViager) searchLibelle
                        .getSearchResults()[i];
                checked[i] = listeSimpleLibelleContratEntretienViagers[i].getCsLibelleContratEntretienViager();

            }
            listeLibelle.add(checked);
            // fin récup. frais

            if (donnee.getDonneeFinanciere() instanceof SimpleContratEntretienViager) {
                MembreFamilleEtendu f = donnee.getMembreFamilleEtendu();

                List donneesMembre = (List) donnees.get(f.getId());
                if (donneesMembre == null) {
                    donneesMembre = new ArrayList();
                    donnees.put(f.getId(), donneesMembre);
                }
                donneesMembre.add(donnee);
            }
        }

    }

    public void setListeLibelle(List listeLibelle) {
        this.listeLibelle = listeLibelle;
    }

}
