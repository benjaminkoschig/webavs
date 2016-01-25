package ch.globaz.utils;

public class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public R getRight() {
        return right;
    }

    public L getLeft() {
        return left;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair<?, ?> other = (Pair<?, ?>) obj;
            if (left == null || right == null) {
                return false;
            }
            return (getLeft().equals(other.getLeft()) && getRight().equals(other.getRight()));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pair [left=" + left + ", right=" + right + "]";
    }

    @Override
    public int hashCode() {
        int leftCode = (left == null) ? 0 : left.hashCode();
        int rightCode = (right == null) ? 0 : right.hashCode();
        return leftCode + rightCode;
    }
}
