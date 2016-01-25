package globaz.aquila.service.taxes;

import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COEtapeInfoManager;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.access.poursuite.COHistoriqueManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Stack;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COTaxeService {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne une instance unique de {@link ICOTaxeProducer} capable de créer la liste des taxes pour l'étape donnée.
     * 
     * @param etape
     *            étape pour laquelle on veut créer la liste des taxes.
     * @return DOCUMENT ME!
     */
    public ICOTaxeProducer getTaxeProducer(COEtape etape) {
        // TODO à gérer différement
        // if
        // (etape.getLibEtape().equals(ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE))
        // {
        // return new COTaxeAmende();
        // }

        return new COTaxeDefaut();
    }

    /**
     * Calcule la somme des frais, taxes et intérêts en se basant sur l'historique d'un contentieux.
     * 
     * @param session
     * @param contentieux
     * @throws Exception
     */
    public void initMontantsTaxes(BSession session, COContentieux contentieux) throws Exception {
        if (!JadeStringUtil.isEmpty(contentieux.getMontantTotalFrais())
                && !JadeStringUtil.isEmpty(contentieux.getMontantTotalInterets())
                && !JadeStringUtil.isEmpty(contentieux.getMontantTotalTaxes())) {
            // on a déjà calculé, on le refait pas deux fois
            return;
        }

        // calculer les montants totaux en se basant sur l'historique
        Stack taxes = new Stack();
        Stack frais = new Stack();
        Stack interets = new Stack();

        COHistoriqueManager historiqueManager = new COHistoriqueManager();

        historiqueManager.setSession(session);
        historiqueManager.setForIdContentieux(contentieux.getIdContentieux());
        historiqueManager.setForIdSequence(contentieux.getIdSequence());
        historiqueManager.setOrderBy(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE);
        historiqueManager.find(BManager.SIZE_NOLIMIT);

        // HACK pour avoir les taxes on donne l'idEtapeInfoConfig 0 et
        // l'idHistorique
        COEtapeInfoManager etapeInfoManager = new COEtapeInfoManager();

        etapeInfoManager.setForIdEtapeInfoConfig("0");
        etapeInfoManager.setLeftJoin(true);

        for (int i = 0; i < historiqueManager.size(); i++) {
            COHistorique historique = (COHistorique) historiqueManager.getEntity(i);
            etapeInfoManager.setSession(session);
            etapeInfoManager.setForIdHistorique(historique.getIdHistorique());
            etapeInfoManager.find(BManager.SIZE_NOLIMIT);

            BigDecimal totalTaxesPourCetHistorique = new BigDecimal("0");

            for (int j = 0; j < etapeInfoManager.size(); j++) {
                totalTaxesPourCetHistorique = totalTaxesPourCetHistorique.add(new BigDecimal(
                        ((COEtapeInfo) etapeInfoManager.getEntity(j)).getValeur()));
            }

            taxes.push(totalTaxesPourCetHistorique.toString());

            // TODO sel plus utilisé
            COEtapeInfo etapeInfoFrais = historique.loadEtapeInfo(COEtapeInfoConfig.CS_FRAIS_VARIABLES);
            if ((etapeInfoFrais != null) && "true".equalsIgnoreCase(etapeInfoFrais.getComplement2())) {
                frais.push(etapeInfoFrais.getValeur());
            } else {
                frais.push("0");
            }
            // TODO sel plus utilisé
            COEtapeInfo etapeInfoInterets = historique.loadEtapeInfo(COEtapeInfoConfig.CS_INTERETS);
            if ((etapeInfoInterets != null) && "true".equalsIgnoreCase(etapeInfoInterets.getComplement2())) {
                interets.push(etapeInfoInterets.getValeur());
            } else {
                interets.push("0");
            }
        }

        // on affecte aux champs
        contentieux.setMontantTotalFrais(somme(frais));
        contentieux.setMontantTotalInterets(somme(interets));
        contentieux.setMontantTotalTaxes(somme(taxes));
    }

    private String somme(Stack elements) {
        BigDecimal total = new BigDecimal("0");

        Iterator iteratorTaxes = elements.iterator();

        while (iteratorTaxes.hasNext()) {
            try {
                total = total.add(new BigDecimal((String) iteratorTaxes.next()));
            } catch (NumberFormatException e) {
                // e.printStackTrace();
            }
        }

        return total.toString();
    }
}
