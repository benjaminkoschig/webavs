package ch.globaz.common.util;

import java.util.function.Function;

/**
 * Le but de cette class est de facilité la gestion de if et du cast lors de l'utilisation de instanceof
 */
public class Instances {

    private Object instanceToTest;
    private Object result;
    private Object instanceCasted;

    /**
     * Constructeur static de la class qui reçois l'instance à tester.
     *
     * @param instanceToTest l'objet que l'on veut tester.
     * @param <O>            Le type de l'instance.
     *
     * @return La nouvelle instance de l'utilistaire.
     */
    public static <O> Instances of(O instanceToTest) {
        Instances instances = new Instances();
        instances.instanceToTest = instanceToTest;
        return instances;
    }

    /**
     * Test de l'instance et qui va exécuter une fonction si il s'agit de la bonne instance.
     * Une seule fonction peut être exécuté, c'est le premier "is" qui contera.
     *
     * @param clazz    Class qui permet est utilié pour tester l'instance.
     * @param function Fonction que l'on veut exécuter si il s'agit de la bonne instance
     * @param <T>      Le type de la class que l'on veut tester.
     * @param <R>      Le retour de la fonction
     *
     * @return Soit même.
     */
    public <T, R> Instances is(Class<T> clazz, Function<T, R> function) {
        if (clazz.isInstance(this.instanceToTest) && this.instanceCasted == null) {
            T castInstanceTyped = clazz.cast(this.instanceToTest);
            this.instanceCasted = castInstanceTyped;
            this.result = function.apply(castInstanceTyped);
        }
        return this;
    }

    /**
     * Le résutat de la fonction qui à matché un {@link #is(Class, Function)}
     *
     * @param <R> Le type du résultat.
     *
     * @return Le résultat.
     */
    @SuppressWarnings("unchecked")
    public <R> R result() {
        return (R) result;
    }
}
