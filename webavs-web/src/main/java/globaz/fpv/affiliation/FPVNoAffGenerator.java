package globaz.fpv.affiliation;

import globaz.globall.db.BSession;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;

public class FPVNoAffGenerator implements INumberGenerator {

    public static String MAX_NUMERO_AUTRE = "999999.99";
    public static String MAX_NUMERO_CAF_SEUL = "079999.99";

    public static void main(String[] args) {
        try {
            BSession session = new BSession("NAOS");
            session.connect("adminuser", "adminuser");
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(session);
            affiliation.setIdTiers("1");
            affiliation.setAffilieNumero("7");
            FPVNoAffGenerator gen = new FPVNoAffGenerator();
            System.out.println("7->" + gen.generateBeforeAdd(affiliation));
            affiliation.setAffilieNumero("");
            System.out.println("vide->" + gen.generateBeforeAdd(affiliation));
            affiliation.setAffilieNumero("900221");
            affiliation.setTypeAssocie("1");
            System.out.println("Associé de 900221->" + gen.generateBeforeAdd(affiliation));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    @Override
    public String generateBeforeAdd(AFAffiliation affiliation) throws Exception {
        if ((affiliation == null) || JadeStringUtil.isEmpty(affiliation.getIdTiers())) {
            return "";
        }
        if (!affiliation.isNew()) {
            return affiliation.getAffilieNumero();
        }
        String result = "";
        if (!"7".equals(affiliation.getAffilieNumero())) {
            result = affiliation.getAffilieNumero();
        }
        IFormatData affilieFormater = ((AFApplication) affiliation.getSession().getApplication()).getAffileFormater();
        if (JadeStringUtil.isEmpty(result)) {
            // affilié standart ou associé
            if (!JadeStringUtil.isEmpty(affiliation.getTypeAssocie())) {
                // associé ou commenditaire. Un numéro doit avoir été saisi
                String noSaisi = affiliation.getAffilieNumero();
                if (!JadeStringUtil.isEmpty(noSaisi)) {
                    // formatage: si -00 saisi, supprimer extension
                    int pos = noSaisi.indexOf('.');
                    if (pos != -1) {
                        // supression extension
                        noSaisi = noSaisi.substring(0, pos);
                    }
                    noSaisi = affilieFormater.format(noSaisi);
                    if (noSaisi.length() == 6) {
                        AFAffiliationManager mgr = new AFAffiliationManager();
                        mgr.setSession(affiliation.getSession());
                        mgr.setLikeAffilieNumero(noSaisi);
                        mgr.find();
                        boolean parentFound = false;
                        int associe = 0;
                        for (int i = 0; i < mgr.size(); i++) {
                            String noAff = ((AFAffiliation) mgr.getEntity(i)).getAffilieNumero();
                            try {
                                if ((noAff.indexOf('.') == -1) && (noAff.length() == 6)) {
                                    parentFound = true;
                                } else {
                                    int extension = Integer
                                            .parseInt(noAff.substring(noAff.length() - 2, noAff.length()));
                                    if (extension > associe) {
                                        // maj dernier associé
                                        associe = extension;
                                    }
                                }
                            } catch (Exception ex) {
                                // impossible de lire l'extension -> ignore le
                                // cas
                                continue;
                            }
                        } // end for
                        if (parentFound) {
                            // création nouveau numéro
                            result = noSaisi + "." + JadeStringUtil.rightJustifyInteger(String.valueOf(associe + 1), 2);
                        }
                    }
                } // if noSaisi
            } else {
                String noSaisi = affiliation.getAffilieNumero();
                // affiliation normal
                // recherche du prochain numéro valable
                AFAffiliationManager mgr = new AFAffiliationManager();
                mgr.setSession(affiliation.getSession());
                if (!JadeStringUtil.isEmpty(noSaisi) && "7".equals(noSaisi)) {
                    mgr.setToAffilieNumero(FPVNoAffGenerator.MAX_NUMERO_CAF_SEUL);
                } else {
                    mgr.setToAffilieNumero(FPVNoAffGenerator.MAX_NUMERO_AUTRE);
                }
                mgr.forIsTraitement(false);
                mgr.setOrder("MALNAF DESC");
                mgr.find(affiliation.getSession().getCurrentThreadTransaction());
                if (mgr.size() != 0) {
                    String noAff = ((AFAffiliation) mgr.getFirstEntity()).getAffilieNumero();
                    if ((noAff != null) && (noAff.length() > 6)) {
                        noAff = noAff.substring(0, 6);
                    }
                    result = String.valueOf(Integer.parseInt(noAff) + 1);
                }
            }
        }
        if (!JadeStringUtil.isEmpty(result)) {
            result = affilieFormater.format(result);
        }
        return result;
    }

    @Override
    public String generateBeforeDisplay(AFAffiliation affiliation) throws Exception {
        return "";
    }

    @Override
    public boolean isEditable(AFAffiliation affiliation) throws Exception {
        return true;
    }
}
