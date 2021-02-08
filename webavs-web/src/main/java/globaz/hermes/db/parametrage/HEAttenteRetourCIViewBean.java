package globaz.hermes.db.parametrage;

import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.utils.CurrencyUtils;
import java.util.Arrays;

/**
 * Insérez la description du type ici. Date de création : (08.05.2003 14:07:57)
 * 
 * @author: ado
 */
public class HEAttenteRetourCIViewBean extends globaz.hermes.db.gestion.HEOutputAnnonceViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Commentaire relatif au constructeur HEAttenteRetourCIViewBean.
     */
    private BSession pavoSession = null;

    public HEAttenteRetourCIViewBean() {
        super();
    }

    public String getRevenuFormatte(String cleExtourne) throws Exception {
        if (!isRevenuCache(getSession().getUserId())) {
            int[] values = { Integer.MIN_VALUE, 0, 2, 6, 8 };
            try {
                Double revenu = new Double(getRevenu());
                if (Arrays.binarySearch(values, Integer.parseInt(cleExtourne)) >= 0) {
                    return CurrencyUtils.formatCurrency(revenu.toString(), true, false, true, 0);
                } else {
                    return CurrencyUtils.formatCurrency(new Double(revenu.doubleValue() * -1.0).toString(), true,
                            false, true, 0);
                }
            } catch (Exception e) {
                return "";
            }
        } else {
            return getSession().getLabel("HERMES_10001");
        }
    }

    public String getTextRevenu(String numeroAffilie, String cleExtourne, String cleCodeRevenu) throws Exception {
        if (pavoSession == null) {
            pavoSession = (BSession) ((HEApplication) getSession().getApplication()).getSessionCI(getSession());
        }
        if (!isRevenuCache(getSession().getUserId())) {
            String information = getField(IHEAnnoncesViewBean.PARTIE_INFORMATION);
            if (cleCodeRevenu.equals("")) {
                return information;
            } else {
                int codeRevenu = JAUtil.isStringEmpty(cleCodeRevenu) ? 0 : Integer.parseInt(cleCodeRevenu);
                int extourne = JAUtil.isStringEmpty(cleExtourne) ? 0 : Integer.parseInt(cleExtourne);
                try {
                    String motif = getField(IHEAnnoncesViewBean.MOTIF_ANNONCE);
                    if ("95".equals(motif.trim()) && codeRevenu == 8) {
                        if (extourne == 1) {
                            return pavoSession.getLabel("PRTGENRE15"); // "Part de revenu destinée au conjoint";
                        } else {
                            return pavoSession.getLabel("PRTGENRE14"); // "Part de revenu provenant du conjoint";
                        }
                    } else if ("95".equals(motif.trim())) {
                        return "";
                    }
                    switch (codeRevenu) {
                        case 0:
                            if (numeroAffilie.equals("11111111111")) {
                                return pavoSession.getLabel("PRTGENRE3"); // "Bonification pour tâches d'assistance";
                            } else {
                                return pavoSession.getLabel("PRTGENRE2"); // "Assurance facultative des Suisses à l'étranger";
                            }
                        case 1:
                            if (numeroAffilie.equals("88888888888")) {
                                return pavoSession.getLabel("PRTGENRE6"); // "Indemnité journalière AI";
                            } else if (numeroAffilie.equals("77777777777")) {
                                return pavoSession.getLabel("PRTGENRE7"); // "Allocation pour perte de gain";
                            } else if (numeroAffilie.equals("66666666666")) {
                                return pavoSession.getLabel("PRTGENRE8"); // "Indemnité journalière de l'assurance militaire";
                            } else if (numeroAffilie.equals("55555555555")) {
                                return pavoSession.getLabel("PRTGENRE17"); // "Allocation pour perte de gain - pandémie";
                            } else if (numeroAffilie.startsWith("999999")) {
                                return pavoSession.getLabel("PRTGENRE5"); // "Indemnité de chômage";
                            } else {
                                return information;
                            }
                        case 2:
                            return pavoSession.getLabel("PRTGENRE9"); // "Salarié/e dont l'employeur n'est pas soumis à cotisations";
                        case 3:
                            return pavoSession.getLabel("PRTGENRE10"); // "Personne de condition indépendante";
                        case 4:
                            if (getField(IHEAnnoncesViewBean.REVENU).equals("")) {
                                return pavoSession.getLabel("PRTGENRE1"); // "Conjoint non actif à l'étranger";
                            } else {
                                return pavoSession.getLabel("PRTGENRE11"); // "Personne sans activité lucrative";
                            }
                        case 5:
                            return pavoSession.getLabel("PRTGENRE12"); // "Timbres-cotisations";
                        case 7:
                            return pavoSession.getLabel("PRTGENRE13"); // "Revenu soumis à cotisations de personnes retraitées";
                        case 8:
                            if (extourne == 1) {
                                return pavoSession.getLabel("PRTGENRE15"); // "Part de revenu destinée au conjoint";
                            } else {
                                return pavoSession.getLabel("PRTGENRE14"); // "Part de revenu provenant du conjoint";
                            }
                        case 9:
                            return pavoSession.getLabel("PRTGENRE16"); // "Personne de condition indépendante dans l'agriture";
                        default:
                            return "";
                    }
                } catch (Exception e) { // e.printStackTrace();
                    return information;
                }
            }
        } else {
            return getSession().getLabel("HERMES_10001");
        }
    }
}
