package globaz.hermes.db.parametrage;

import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.utils.CurrencyUtils;
import java.util.Arrays;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (08.05.2003 14:07:57)
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
                            return pavoSession.getLabel("PRTGENRE15"); // "Part de revenu destin�e au conjoint";
                        } else {
                            return pavoSession.getLabel("PRTGENRE14"); // "Part de revenu provenant du conjoint";
                        }
                    } else if ("95".equals(motif.trim())) {
                        return "";
                    }
                    switch (codeRevenu) {
                        case 0:
                            if (numeroAffilie.equals("11111111111")) {
                                return pavoSession.getLabel("PRTGENRE3"); // "Bonification pour t�ches d'assistance";
                            } else {
                                return pavoSession.getLabel("PRTGENRE2"); // "Assurance facultative des Suisses � l'�tranger";
                            }
                        case 1:
                            if (numeroAffilie.equals("88888888888")) {
                                return pavoSession.getLabel("PRTGENRE6"); // "Indemnit� journali�re AI";
                            } else if (numeroAffilie.equals("77777777777")) {
                                return pavoSession.getLabel("PRTGENRE7"); // "Allocation pour perte de gain";
                            } else if (numeroAffilie.equals("66666666666")) {
                                return pavoSession.getLabel("PRTGENRE8"); // "Indemnit� journali�re de l'assurance militaire";
                            } else if (numeroAffilie.equals("55555555555")) {
                                return pavoSession.getLabel("PRTGENRE17"); // "Allocation pour perte de gain - pand�mie";
                            } else if (numeroAffilie.startsWith("999999")) {
                                return pavoSession.getLabel("PRTGENRE5"); // "Indemnit� de ch�mage";
                            } else {
                                return information;
                            }
                        case 2:
                            return pavoSession.getLabel("PRTGENRE9"); // "Salari�/e dont l'employeur n'est pas soumis � cotisations";
                        case 3:
                            return pavoSession.getLabel("PRTGENRE10"); // "Personne de condition ind�pendante";
                        case 4:
                            if (getField(IHEAnnoncesViewBean.REVENU).equals("")) {
                                return pavoSession.getLabel("PRTGENRE1"); // "Conjoint non actif � l'�tranger";
                            } else {
                                return pavoSession.getLabel("PRTGENRE11"); // "Personne sans activit� lucrative";
                            }
                        case 5:
                            return pavoSession.getLabel("PRTGENRE12"); // "Timbres-cotisations";
                        case 7:
                            return pavoSession.getLabel("PRTGENRE13"); // "Revenu soumis � cotisations de personnes retrait�es";
                        case 8:
                            if (extourne == 1) {
                                return pavoSession.getLabel("PRTGENRE15"); // "Part de revenu destin�e au conjoint";
                            } else {
                                return pavoSession.getLabel("PRTGENRE14"); // "Part de revenu provenant du conjoint";
                            }
                        case 9:
                            return pavoSession.getLabel("PRTGENRE16"); // "Personne de condition ind�pendante dans l'agriture";
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
