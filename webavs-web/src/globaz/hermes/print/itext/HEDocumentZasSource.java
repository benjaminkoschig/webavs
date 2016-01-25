package globaz.hermes.print.itext;

import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.gestion.HEOutputAnnonceJointHEInfos;
import globaz.hermes.db.gestion.HEOutputAnnonceJointHEInfosManager;
import globaz.hermes.utils.DateUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 *
 *
 *
 */
public class HEDocumentZasSource extends HEOutputAnnonceJointHEInfosManager {

    private static final long serialVersionUID = -2202539817238574908L;

    boolean arrivedAtEnd = false;
    private HEOutputAnnonceJointHEInfos entity = null;
    private String forService;
    private List<String> listDesUser = new ArrayList<String>();
    // private HELotViewBean lot = null;
    private Iterator<?> recipient = null;

    /**
     * Commentaire relatif au constructeur HEDocumentZasSource.
     */
    public HEDocumentZasSource() {
        super();
    }

    /**
	 *
	 */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
	 *
	 */
    public Map<String, String> getFieldValue() throws JRException {
        Map<String, String> map = new HashMap<String, String>();
        try {
            map.put("COL_1", entity.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE));
            map.put("COL_2", entity.getRefUnique());
            map.put("COL_3", globaz.commons.nss.NSUtil.formatAVSUnknown(entity.getField(
                    IHEAnnoncesViewBean.NUMERO_ASSURE).trim()));

            map.put("COL_4", entity.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
            map.put("COL_5", DateUtils.convertDate(entity.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA),
                    DateUtils.JJMMAA, DateUtils.JJMMAAAA_DOTS));
            map.put("COL_6", entity.getField(IHEAnnoncesViewBean.ETAT_ORIGINE));
            map.put("COL_7", entity.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            map.put("COL_8", globaz.commons.nss.NSUtil.formatAVSUnknown(entity.getField(
                    IHEAnnoncesViewBean.NUMERO_ASSURE_1).trim()));

            String value = "";
            try {
                String execute = "OK";
                String nonExecute = "";
                String enCours = "...";

                if ("0".equals(entity.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT))
                        || JadeStringUtil.isEmpty(entity.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT))) {
                    value = execute;
                } else if ("2".equals(entity.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT))) {
                    value = enCours;
                } else {
                    StringBuffer s = new StringBuffer(nonExecute);
                    // s.append("(");
                    s.append(entity.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT) == null ? "" : entity
                            .getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT));
                    s.append("-");
                    s.append(entity.getField(IHEAnnoncesViewBean.NUMERO_CHAMP_1_INCORRECT) == null ? "" : entity
                            .getField(IHEAnnoncesViewBean.NUMERO_CHAMP_1_INCORRECT));
                    s.append("-");
                    s.append(entity.getField(IHEAnnoncesViewBean.NUMERO_CHAMP_2_INCORRECT) == null ? "" : entity
                            .getField(IHEAnnoncesViewBean.NUMERO_CHAMP_2_INCORRECT));
                    // s.append(")");
                    value = s.toString();
                }
            } catch (Exception e) {
                value = entity.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT);
            }
            map.put("COL_9", value);
            map.put("COL_10", entity.getUtilisateur().toLowerCase());
            // setter dans la map le numéro d'affilié
            if (HEInfos.CS_NUMERO_AFFILIE.equals(entity.getTypeInfo())) {
                map.put("COL_11", entity.getLibInfo());
            }
            listDesUser.add(entity.getUtilisateur().toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public String getForService() {
        return forService;
    }

    public Map<String, String> getLastTotal() {
        int taille = listDesUser.size() - 1;
        String user = listDesUser.get(taille);
        int counter = 0;
        for (Iterator<String> iter = listDesUser.iterator(); iter.hasNext();) {
            String toCompare = iter.next();
            if (user.equals(toCompare)) {
                counter++;
            } else {
                continue;
            }
        }
        Map<String, String> totalLast = new HashMap<String, String>();
        totalLast.put("COL_1", "Total " + user + " : " + counter);
        totalLast.put("COL_10", user);

        return totalLast;
    }

    public String getLastUser() {
        return listDesUser.get(listDesUser.size() - 1);
    }

    public Map<String, String> getTotalRetour(int NbreTotal) {
        String dernierUser = listDesUser.get(listDesUser.size() - 1);

        Map<String, String> totalGen = new HashMap<String, String>();
        if (getSession().getIdLangue().equals("F")) {
            totalGen.put("COL_1", "Total global : " + NbreTotal);
        } else if (getSession().getIdLangue().equals("D")) {
            totalGen.put("COL_1", "Gesamtes Total : " + NbreTotal);
        } else {
            totalGen.put("COL_1", "Total global : " + NbreTotal);
        }
        // ajout du dernier user afin que iTexte mette le totalGlobal avec le groupe du dernier utilisateur
        totalGen.put("COL_10", dernierUser);
        return totalGen;
    }

    /**
	 *
	 */
    public Map<String, String> next() throws JRException {
        entity = null;
        try {
            // System.out.println("Thread=" +
            // Thread.currentThread().toString());
            if (container == null) { // container not loaded
                this.find(0);
                recipient = getContainer().iterator();
            }
            if (recipient.hasNext()) {
                entity = (HEOutputAnnonceJointHEInfos) recipient.next();
                return getFieldValue();
            } else {
                if (!listDesUser.isEmpty() && !arrivedAtEnd) {
                    arrivedAtEnd = true;
                    return getLastTotal();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setForService(String forService) {
        this.forService = forService;
    }
}
