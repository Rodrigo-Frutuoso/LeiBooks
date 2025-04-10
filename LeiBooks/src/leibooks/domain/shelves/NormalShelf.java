package leibooks.domain.shelves;

/**
 * A NormalShelf represents a standard shelf in the library.
 * It allows explicit addition and removal of documents.
 *
 * This class extends AShelf and provides functionality to handle
 * events related to document removal from the library.
 *
 * When a document is removed from the library, it is also removed from this shelf.
 *
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class NormalShelf extends AShelf {

    /**
     * Constructs a NormalShelf with the specified name.
     *
     * @param name the unique name of the shelf
     */
    public NormalShelf(String name) {
        super(name);
    }
}
