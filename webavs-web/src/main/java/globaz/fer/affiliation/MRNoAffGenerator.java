/*
 * Cr�� le 15 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.fer.affiliation;

import globaz.globall.db.BSession;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.util.List;

/**
 * G�n�rateur de num�ro d'affiliation
 * 
 * @author dgi
 */
public class MRNoAffGenerator implements INumberGenerator {

    public static String MAX_NUMERO_AUTRE = "999999.99";
    public static String MAX_NUMERO_CAF_SEUL = "079999.99";

    public static void main(String[] args) {
        try {
            BSession session = new BSession("NAOS");
            session.connect("oca", "oca");
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(session);
            affiliation.setIdTiers("1");
            affiliation.setAffilieNumero("");
            MRNoAffGenerator gen = new MRNoAffGenerator();

            System.out.println(gen.generateBeforeAdd(affiliation));

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    @Override
    public String generateBeforeAdd(AFAffiliation affiliation) throws Exception {
        String numAff = null;
        if (affiliation == null) {
            throw new IllegalStateException("L'affiliation ne doit pas �tre 'null' � ce stade");
        }

        IFormatData affilieFormater = ((AFApplication) affiliation.getSession().getApplication()).getAffileFormater();

        /*
         * Si le num�ro pass� est vide, on g�n�re un nouveau num�ro
         */
        if (JadeStringUtil.isBlank(affiliation.getAffilieNumero())) {
            List<String> affs = TISQL.querySingleField(affiliation.getSession(), "max(MALNAF) MALNAF", "from "
                    + TIToolBox.getCollection() + "AFAFFIP");
            if (affs == null || affs.size() == 0) {
                // Il n'y a pas encore de num�ro d'affili�
                numAff = "MR000.001";
            } else {

                int current = Integer.parseInt(affilieFormater.unformat(affs.get(0)));
                current++;
                numAff = affilieFormater.format(current + "");
            }
        } else {
            /*
             * Si un num�ro valide est pass�, on r�utilise ce num�ro
             */
            String msg = affilieFormater.check(affiliation.getAffilieNumero());
            if ("".equals(msg)) {
                numAff = affiliation.getAffilieNumero();
            } else {
                /*
                 * Le num�ro pass� n'est pas valable � voir comment remonter ce message
                 */
                throw new Exception(msg);

            }
        }
        return numAff;
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
