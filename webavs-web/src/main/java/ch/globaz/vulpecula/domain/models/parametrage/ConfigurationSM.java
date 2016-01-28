package ch.globaz.vulpecula.domain.models.parametrage;

public class ConfigurationSM {
    private final int minJours;
    private final int maxJours;
    private final int couvertureAPG;
    private final double tauxCP;
    private final double gratification;
    private final boolean CPForce0;

    public ConfigurationSM(int minJours, int maxJours, int couvertureAPG, double tauxCP, double gratification,
            boolean CPForce0) {
        this.minJours = minJours;
        this.maxJours = maxJours;
        this.couvertureAPG = couvertureAPG;
        this.tauxCP = tauxCP;
        this.gratification = gratification;
        this.CPForce0 = CPForce0;
    }

    public int getMinJours() {
        return minJours;
    }

    public int getMaxJours() {
        return maxJours;
    }

    public int getCouvertureAPG() {
        return couvertureAPG;
    }

    public double getTauxCP() {
        return tauxCP;
    }

    public double getGratification() {
        return gratification;
    }

    public boolean isCPForce0() {
        return CPForce0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (CPForce0 ? 1231 : 1237);
        result = prime * result + couvertureAPG;
        long temp;
        temp = Double.doubleToLongBits(gratification);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + maxJours;
        result = prime * result + minJours;
        temp = Double.doubleToLongBits(tauxCP);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConfigurationSM other = (ConfigurationSM) obj;
        if (CPForce0 != other.CPForce0) {
            return false;
        }
        if (couvertureAPG != other.couvertureAPG) {
            return false;
        }
        if (Double.doubleToLongBits(gratification) != Double.doubleToLongBits(other.gratification)) {
            return false;
        }
        if (maxJours != other.maxJours) {
            return false;
        }
        if (minJours != other.minJours) {
            return false;
        }
        if (Double.doubleToLongBits(tauxCP) != Double.doubleToLongBits(other.tauxCP)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConfigurationSM [minJours=" + minJours + ", maxJours=" + maxJours + ", couvertureAPG=" + couvertureAPG
                + ", tauxCP=" + tauxCP + ", gratification=" + gratification + ", CPForce0=" + CPForce0 + "]";
    }
}
