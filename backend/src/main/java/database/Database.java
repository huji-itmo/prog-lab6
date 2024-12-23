package database;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * interface for general-purpose database. Should be created with decorator.
 *
 * @param <T> - class that database handles
 * @param <K> - class of primary key
 */
public interface Database<T, K> {
    Comparator<K> getPrimaryKeyComparator();
    Function<T,K> getPrimaryKey();
    BiConsumer<T,K> setPrimaryKeyTo();
    Class<T> getElementClass();

    /**
     * @param id primary key
     * @return the list of deleted elements.
     */
    List<T> removeGreaterOrLowerThanPrimaryKey(K id, boolean greater);

    String getElementsDescendingByPrimaryKey();

    String getInfo();
    /**
     * Adds a new element and updates the last id.
     *
     * @param element element
     */
    void addElement(T element);
    String serializeAllElements();
    T updateElementByPrimaryKey(K id, T new_element) throws IllegalArgumentException;
    T removeElementByPrimaryKey(K id) throws IllegalArgumentException;
    /**
     * Clears the collection and resets the last id counter.
     */
    void clear();
    void serialize() throws Exception;
    void deserialize() throws Exception;

    void pushToUndoStack(UndoLog<T> log);

    boolean undo();

    boolean existsById(K id);

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Data
    class UndoLog<T> {
        List<ElementChange<T>> changesList;

        public static <T> UndoLog<T> addedElements(T ...elements) {
            List<ElementChange<T>> changes = new ArrayList<>();

            for (T element : elements) {
                changes.add(new ElementChange<>(true, element));
            }

            return new UndoLog<>(changes);
        }

        public static <T> UndoLog<T> deletedElements(T ...elements) {
            List<ElementChange<T>> changes = new ArrayList<>();

            for (T element : elements) {
                changes.add(new ElementChange<>(false, element));
            }

            return new UndoLog<>(changes);
        }

        public static <T> UndoLog<T> deletedElements(Iterable<T> elements) {
            List<ElementChange<T>> changes = new ArrayList<>();

            for (T element : elements) {
                changes.add(new ElementChange<>(false, element));
            }

            return new UndoLog<>(changes);
        }

        public static <T> UndoLog<T> changedElement(T newElement, T oldElement) {
            List<ElementChange<T>> changes = new ArrayList<>();

            changes.add(new ElementChange<>(true, newElement));
            changes.add(new ElementChange<>(false, oldElement));

            return new UndoLog<>(changes);
        }


            public boolean undo(Collection<T> collection) {
            for (ElementChange<T> change : changesList) {
                boolean res;

                if (change.isAdded) {
                    res = collection.remove(change.element);
                }
                else {
                    res = collection.add(change.element);
                }

                if (!res)
                    return false;
            }


            return true;
        }
    }

    @Data
    @AllArgsConstructor
    class ElementChange<T> {
        boolean isAdded;
        T element;
    }
}
