package ch.globaz.pegasus.businessimpl.utils.calcul;

public enum ProxyCalculDates {

    FORTUNE_IMMOBILIERE_SWITCH_STRATEGY_DATE(1293836400000L),

    // 1er janvier 2011
    DEPENSE_TOTAL_RECONNUES_SWITCH_STRATEGY_DATE(1420066800000L);// 1er janvier 2015

    public long timestamp;

    private ProxyCalculDates(long timestamp) {
        this.timestamp = timestamp;
    }

}
