package ch.globaz.common.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Collections {

    /**
     * Le but de cette fonction est de faire une boucle qui fournit les deux éléments qui se suivent.
     *
     * @param consumer   La lambda que l'on veut appliquer à la boucle.
     * @param collection La collection sur la quelle on veut itérer.
     * @param <T>        Le type de donné que contient la collection.
     */
    public static <T> void forEachPair(BiConsumer<T, Optional<T>> consumer, Collection<T> collection) {
        Iterator<T> iterator = collection.iterator();

        while (iterator.hasNext()) {
            T item1 = iterator.next();
            Optional<T> item2 = Optional.empty();
            if (iterator.hasNext()) {
                item2 = Optional.of(iterator.next());
            }
            consumer.accept(item1, item2);
        }
    }

    /**
     * Cela premet d'avoir l'indéxation quand on veut utiliser le foreach des stream.
     *
     * @param consumer La lambda que l'on veut appliquer à la boucle.
     * @param <T>      Le type de donné que contient la collection.
     *
     * @return Le consumer qui vas être utilisé par le foreach.
     */
    public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.incrementAndGet(), item);
    }
}
