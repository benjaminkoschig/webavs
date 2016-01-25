/*
 * Créé le 8 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPReceptionReaderViewBean extends CPReceptionReader implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static Vector<String[]> getListeRechercheTiers() {
        Vector<String[]> vList = new Vector<String[]>();
        String[] element = null;
        try {
            vList = new Vector<String[]>(3);
            element = new String[2];
            element[0] = "numAffilie";
            element[1] = "numAffilie";
            vList.add(element);
            element = new String[2];
            element[0] = "numAvs";
            element[1] = "numAvs";
            vList.add(element);
            element = new String[2];
            element[0] = "numContribuable";
            element[1] = "numContribuable";
            vList.add(element);
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        return vList;
    }

    public String getCantonDefaut() {
        try {
            return ((CPApplication) getSession().getApplication()).getCantonDefaut();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne une liste: noms du canton suivi de classe d'implementation Le premier "couple" est le reader par defaut
     * 
     * @return
     */

    public LinkedList<String> getReaders() {
        CPReceptionReaderManager cantonList = new CPReceptionReaderManager();
        cantonList.setSession(getSession());
        // On except Sedex
        LinkedList<String> resultList = new LinkedList<String>();
        try {
            cantonList.find();

            // Canton dans lequel se trouve la caisse
            String isoCanton = ((CPApplication) getSession().getApplication()).getCantonCaisse();

            Iterator<?> it = cantonList.iterator();
            while (it.hasNext()) {
                CPReceptionReader reader = (CPReceptionReader) it.next();
                // Si le reader est celui du canton dans lequel se trouve la
                // caisse,
                // on le met à la tête de la liste.
                if (!reader.getIsoCanton().equals("SE")) {
                    if (reader.getIsoCanton().equals(isoCanton)) {
                        resultList.addFirst(reader.getNomCanton());
                        // resultList.addFirst((reader.isFormatXml()?"xml:":"txt:")
                        // + reader.getNomClass());
                        resultList.addFirst(reader.getIdCanton());
                    } else {
                        // resultList.addLast((reader.isFormatXml()?"xml:":"txt:")
                        // + reader.getNomClass());
                        resultList.addLast(reader.getIdCanton());
                        resultList.addLast(reader.getNomCanton());
                    }
                }
            }
            if (resultList.isEmpty()) {
                _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("CP_MSG_0000"));
            }
            return resultList;
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("CP_MSG_0000"));
            return resultList;
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // REcherche si réception déjà encodé pour ce canton
        if (!JadeStringUtil.isBlank(getIdCanton())) {
            CPReceptionReaderManager mng = new CPReceptionReaderManager();
            mng.setSession(getSession());
            mng.setForIdCanton(getIdCanton());
            mng.find();
            if (mng.size() > 0) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_RCP_EXISTANT"));
            }
        }
    }
}
