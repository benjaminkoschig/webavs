package globaz.perseus.vb.retenue;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFRetenueViewBean extends BJadePersistentObjectViewBean {

    private Map<String, String> listSections = null;
    private PCFAccordee pcfAccordee = null;

    public PFRetenueViewBean() {
        super();
        pcfAccordee = new PCFAccordee();
        listSections = new HashMap<String, String>();
    }

    @Override
    public void add() throws Exception {
        throw new NotImplementedException();

    }

    @Override
    public void delete() throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public String getId() {
        return pcfAccordee.getId();
    }

    /**
     * @return the listSections
     */
    public Map<String, String> getListSections() {
        return listSections;
    }

    /**
     * @return the pcfAccordee
     */
    public PCFAccordee getPcfAccordee() {
        return pcfAccordee;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().read(pcfAccordee.getId());

        // Trouver les idTiers de la famille
        List<String> listIdTiers = new ArrayList<String>();
        List<MembreFamille> membresFamilles = PerseusServiceLocator.getDossierService().getListAllMembresFamille(
                pcfAccordee.getDemande().getDossier().getId());
        for (MembreFamille mf : membresFamilles) {
            listIdTiers.add(mf.getSimpleMembreFamille().getIdTiers());
        }

        // TODO: Créer un service d'intefacage
        CASectionJoinCompteAnnexeJoinTiersManager mgr = new CASectionJoinCompteAnnexeJoinTiersManager();
        mgr.setForIdTiersIn(listIdTiers);
        mgr.setForSoldePositif(true);
        mgr.find();

        for (Iterator<BEntity> it = mgr.iterator(); it.hasNext();) {
            CASectionJoinCompteAnnexeJoinTiers e = (CASectionJoinCompteAnnexeJoinTiers) it.next();
            // Clé : 156348,201126000
            listSections.put(
                    e.getIdCompteAnnexe() + "," + e.getIdExterne() + "," + e.getIdTypeSection(),
                    "<b>" + e.getIdExterne() + "</b> : " + e.getSolde() + " CHF " + e.getTypeSection() + " ("
                            + e.getCategorieSection() + ")");
        }

    }

    @Override
    public void setId(String newId) {
        pcfAccordee.setId(newId);
    }

    /**
     * @param listSections
     *            the listSections to set
     */
    public void setListSections(Map<String, String> listSections) {
        this.listSections = listSections;
    }

    /**
     * @param pcfAccordee
     *            the pcfAccordee to set
     */
    public void setPcfAccordee(PCFAccordee pcfAccordee) {
        this.pcfAccordee = pcfAccordee;
    }

    @Override
    public void update() throws Exception {
        throw new NotImplementedException();
    }

}
