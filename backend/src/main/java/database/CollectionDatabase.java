package database;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
public abstract class CollectionDatabase<T, K> implements Database<T, K> {
    private final Collection<T> collection;

    protected CollectionDatabase(Collection<T> collection) {
        this.collection = collection;
    }

    public Comparator<T> getComparatorByPrimaryKey() {
        return (el1, el2) -> getPrimaryKeyComparator()
                .compare(getPrimaryKey().apply(el1),
                        getPrimaryKey().apply(el2));
    }

    @Override
    public List<T> removeGreaterOrLowerThanPrimaryKey(K id, boolean greater) {
        List<T> deletedElements = new ArrayList<>();

        getCollection()
                .removeIf(element -> {

                    int compared = getPrimaryKeyComparator().compare(getPrimaryKey().apply(element), id);

                    boolean willBeDeleted;

                    if (greater) {
                        willBeDeleted = compared > 0;
                    } else {
                        willBeDeleted = compared < 0;
                    }

                    if (willBeDeleted) {
                        deletedElements.add(element);
                    }

                    return willBeDeleted;
                });

        return deletedElements;
    }

    @Override
    public String getElementsDescendingByPrimaryKey() {
        return getCollection()
                .stream()
                .sorted(getComparatorByPrimaryKey().reversed())
                .map(Objects::toString)
                .map(line -> line + "\n")
                .collect(Collectors.joining());
    }

    @Override
    public String getInfo() {
        return "Type: " +
                collection.getClass().getSimpleName() +
                "\nNumber of entries: " +
                collection.size() +
                "\n";
    }
    @Override
    public String serializeAllElements() {
        if (getCollection().isEmpty()) {
            return "The database is currently empty.";
        }

        return getCollection()
                .stream()
                .sorted(getComparatorByPrimaryKey())
                .map(Objects::toString)
                .map(line -> line + "\n")
                .collect(Collectors.joining());
    }

    /**
     *
     * @param id new element id
     * @param new_element new element
     * @return deleted element
     * @throws IllegalArgumentException
     */
    @Override
    public T updateElementByPrimaryKey(K id, T new_element) throws IllegalArgumentException {
        T deletedElement = removeElementByPrimaryKey(id);

        addElement(new_element);

        setPrimaryKeyTo().accept(new_element, id);

        return deletedElement;
    }

    @Override
    public T removeElementByPrimaryKey(K id) throws IllegalArgumentException {
         Optional<T> res = getCollection()
                 .stream()
                 .filter(element -> getPrimaryKeyComparator().compare(getPrimaryKey().apply(element), id) == 0)
                 .findFirst();

         res.orElseThrow(() -> new IllegalArgumentException("Element with id: " + id + " is not found!"));

         return res.get();

    }

    @Override
    public void addElement(T element) {
        collection.add(element);
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public boolean existsById(K id) {
        return getCollection().stream().anyMatch(elem -> getPrimaryKey().apply(elem).equals(id));
    }
}
