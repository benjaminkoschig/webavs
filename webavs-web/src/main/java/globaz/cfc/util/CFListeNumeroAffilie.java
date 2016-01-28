package globaz.cfc.util;

import globaz.cfc.format.CFNumAffilie;
import globaz.pyxis.process.listenumaffilie.ITIListNumbers;
import java.util.ArrayList;
import java.util.List;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CFListeNumeroAffilie implements ITIListNumbers {

    public static void main(String[] args) {

        ITIListNumbers num = new CFListeNumeroAffilie();
        try {
            num.buildList("41712", "420.61");
            List list = num.getList();

            /* affichage horizontal */
            /*
             * ListIterator it = list.listIterator(); int nbx = 5; int x = 0; while(it.hasNext()) { x++;
             * System.out.print ((String)it.next()); if (it.hasNext()) { if (x >= nbx) { System.out.print ("\n"); x = 0;
             * } else { System.out.print("\t\t"); } } }
             */

            /* affichage vertical */
            int nby = 50;
            int nbx = list.size() / nby;
            int reste = list.size() % nby;

            for (int y = 0; y < nby; y++) {
                for (int x = 0; x <= nbx; x++) {

                    if ((y >= reste) && ((x == nbx))) {
                        // la dernière colonne n'est pas toujours remplie
                        break;
                    }
                    System.out.print((String) list.get(y + x * nby));
                    if (x != nbx) {
                        System.out.print("\t\t");
                    }
                }
                System.out.println("");
            }

        } catch (Exception e) {
            System.out.println("Les paramètres ne sont pas valides : " + e.getMessage());
        }
    }

    private List liste = new ArrayList();

    @Override
    public void buildList(String from, String to) throws Exception {

        // declaration
        int iFrom = 0;
        int iTo = 0;
        CFNumAffilie util = new CFNumAffilie();

        // supprime le formatage
        from = util.unformat(from);
        to = util.unformat(to);

        // construction de la liste des numéros avec leurs modulos
        iFrom = Integer.parseInt(from);
        iTo = Integer.parseInt(to);
        for (int i = iFrom; i <= iTo; i++) {
            liste.add(util.getValidNumber(i + ""));
        }

    }

    @Override
    public List getList() {
        return liste;
    }
}
