package ch.globaz.common.sql;

import java.math.BigDecimal;

public class TestBean {

    public static final String CONST = "const";

    private String s1;
    private String s2;
    private BigDecimal bigDecimal = new BigDecimal(0);
    private String pasDeSetter;

    public TestBean() {

    }

    public TestBean(String s1, String s2, BigDecimal bigDecimal) {
        super();
        this.s1 = s1;
        this.s2 = s2;
        this.bigDecimal = bigDecimal;
    }

    public TestBean(String s1, String s2) {
        super();
        this.s1 = s1;
        this.s2 = s2;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    @Override
    public String toString() {
        return "TestBean [s1=" + s1 + ", s2=" + s2 + ", bigDecimal=" + bigDecimal + ", pasDeSetter=" + pasDeSetter
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bigDecimal == null) ? 0 : bigDecimal.hashCode());
        result = prime * result + ((pasDeSetter == null) ? 0 : pasDeSetter.hashCode());
        result = prime * result + ((s1 == null) ? 0 : s1.hashCode());
        result = prime * result + ((s2 == null) ? 0 : s2.hashCode());
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
        TestBean other = (TestBean) obj;
        if (bigDecimal == null) {
            if (other.bigDecimal != null) {
                return false;
            }
        } else if (!bigDecimal.equals(other.bigDecimal)) {
            return false;
        }
        if (pasDeSetter == null) {
            if (other.pasDeSetter != null) {
                return false;
            }
        } else if (!pasDeSetter.equals(other.pasDeSetter)) {
            return false;
        }
        if (s1 == null) {
            if (other.s1 != null) {
                return false;
            }
        } else if (!s1.equals(other.s1)) {
            return false;
        }
        if (s2 == null) {
            if (other.s2 != null) {
                return false;
            }
        } else if (!s2.equals(other.s2)) {
            return false;
        }
        return true;
    }

}
