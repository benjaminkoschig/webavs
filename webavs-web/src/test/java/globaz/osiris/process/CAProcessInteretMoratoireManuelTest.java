package globaz.osiris.process;

import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.tests.util.Init;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThreadActivator;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.process.interetmanuel.CAProcessInteretMoratoireManuel;
import globaz.prestation.tools.PRSession;
import org.apache.bsf.BSFEngine;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class CAProcessInteretMoratoireManuelTest {
    List<Periode> listPeriodeMotifsSurcis ;
    Map<String,Double> mapPeriodeVoulu;
    List<String> listPeriodes ;
    JADate dateCalculDebut;
    JADate dateCalculFin;
    CAInteretMoratoire interet;
    BSession session;
    BTransaction transaction;
    @Test
    @Ignore
    public void checkInteretMoratoirePeriodesSurcisProgation_FullTauxSurcisProro() {
        mapPeriodeVoulu = new LinkedHashMap<>();
        mapPeriodeVoulu.put("01.03.2020-20.03.2020",5.0);
        mapPeriodeVoulu.put("21.03.2020-20.09.2020",0.0);
        mapPeriodeVoulu.put("21.09.2020-30.09.2020",5.0);
        try{
            dateCalculDebut = new JADate("01.03.2020");
            dateCalculFin = new JADate("30.09.2020");
            listPeriodeMotifsSurcis = new ArrayList<>();
            Periode motif1 = new Periode("30.03.2020","30.09.2020");
            listPeriodeMotifsSurcis.add(motif1);
            lanceTest(mapPeriodeVoulu,listPeriodeMotifsSurcis,dateCalculDebut,dateCalculFin);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }


    }
    @Test
    @Ignore
    public void checkInteretMoratoirePeriodesSurcisProgation_MixedTaux() {
        CAProcessInteretMoratoireManuel test = new CAProcessInteretMoratoireManuel();
        mapPeriodeVoulu = new LinkedHashMap<>();
        mapPeriodeVoulu.put("01.03.2020-20.03.2020",5.0);
        mapPeriodeVoulu.put("21.03.2020-30.06.2020",0.0);
        mapPeriodeVoulu.put("01.07.2020-14.07.2020",5.0);
        mapPeriodeVoulu.put("15.07.2020-10.08.2020",0.0);
        mapPeriodeVoulu.put("11.08.2020-14.09.2020",5.0);
        mapPeriodeVoulu.put("15.09.2020-20.09.2020",0.0);
        mapPeriodeVoulu.put("21.09.2020-25.09.2020",5.0);
        try {
            dateCalculDebut = new JADate("01.03.2020");
            dateCalculFin = new JADate("25.09.2020");
            listPeriodeMotifsSurcis = new ArrayList<>();
            Periode motif1 = new Periode("23.03.2020","15.04.2020");
            Periode motif2 = new Periode("15.07.2020","10.08.2020");
            Periode motif3 = new Periode("15.09.2020","25.09.2020");
            listPeriodeMotifsSurcis.add(motif1);
            listPeriodeMotifsSurcis.add(motif2);
            listPeriodeMotifsSurcis.add(motif3);
            lanceTest(mapPeriodeVoulu,listPeriodeMotifsSurcis,dateCalculDebut,dateCalculFin);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }
    @Test
    @Ignore
    public void checkInteretMoratoirePeriodesSurcisProgation_MixedTaux_1JourDiff() {
        CAProcessInteretMoratoireManuel test = new CAProcessInteretMoratoireManuel();
        mapPeriodeVoulu = new LinkedHashMap<>();
        mapPeriodeVoulu.put("01.03.2020-20.03.2020",5.0);
        mapPeriodeVoulu.put("21.03.2020-20.09.2020",0.0);
        mapPeriodeVoulu.put("21.09.2020-25.09.2020",5.0);
        try {
            dateCalculDebut = new JADate("01.03.2020");
            dateCalculFin = new JADate("25.09.2020");
            listPeriodeMotifsSurcis = new ArrayList<>();
            Periode motif1 = new Periode("25.03.2020","15.07.2020");
            Periode motif2 = new Periode("15.07.2020","15.08.2020");
            listPeriodeMotifsSurcis.add(motif1);
            listPeriodeMotifsSurcis.add(motif2);
            lanceTest(mapPeriodeVoulu,listPeriodeMotifsSurcis,dateCalculDebut,dateCalculFin);
            listPeriodeMotifsSurcis.clear();
            motif1 = new Periode("25.03.2020","15.07.2020");
            motif2 = new Periode("16.07.2020","15.05.2020");
            listPeriodeMotifsSurcis.add(motif1);
            listPeriodeMotifsSurcis.add(motif2);
            lanceTest(mapPeriodeVoulu,listPeriodeMotifsSurcis,dateCalculDebut,dateCalculFin);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }
    private void lanceTest(Map<String, Double> mapPeriodeVoulu, List<Periode> listPeriodeMotifsSurcis, JADate dateCalculDebut, JADate dateCalculFin) throws Exception{

             CAProcessInteretMoratoireManuel test = new CAProcessInteretMoratoireManuel();
            session = BSessionUtil.createSession("OSIRIS","globazTEST");
            JadeThreadActivator.startUsingJdbcContext(this, Init.initContext(session).getContext());
            transaction = session.getCurrentThreadTransaction();
            test.setSession(session);
            test.setTransaction(transaction);
            test.setSimulationMode(true);
            interet = new CAInteretMoratoire();
            interet.setIdJournalCalcul("0");
            test.setMontantSoumisSurcisCalcul(new FWCurrency(1));
            test.setMontantCumuleSurcisCalcul(new FWCurrency());
            test.creerInteretForSurcisProro(dateCalculDebut, dateCalculFin, listPeriodeMotifsSurcis,interet,new FWCurrency("1"));
            List<CADetailInteretMoratoire> list = test.getVisualComponent().getDetailInteretMoratoire();
            listPeriodes = new LinkedList<>();
            listPeriodes.addAll(mapPeriodeVoulu.keySet());
            if(list.size() == mapPeriodeVoulu.size()){
                for(int i=0;i<list.size();i++){
                    Periode periode = new Periode(list.get(i).getDateDebut(), list.get(i).getDateFin());
                    String periodeTexteVoulu = listPeriodes.get(i);
                    if(!periode.equals(new Periode(periodeTexteVoulu.split("-")[0],periodeTexteVoulu.split("-")[1]))){
                        Assert.fail("Problème de période : Résultat "+periode.getDateDebut()+"-"+periode.getDateFin()+" Attendu : "+periodeTexteVoulu);
                    }
                    if(!(mapPeriodeVoulu.get(periodeTexteVoulu).equals(new Double(list.get(i).getTaux())))){
                        Assert.fail("Problème de taux : Résultat "+list.get(i).getTaux()+" Attendu : "+ mapPeriodeVoulu.get(periodeTexteVoulu));
                    }
                }
            }else{
                Assert.fail("Manque des périodes : Résultat  "+ list.size()+" Voulu "+  mapPeriodeVoulu.size());
            }


    }

}
