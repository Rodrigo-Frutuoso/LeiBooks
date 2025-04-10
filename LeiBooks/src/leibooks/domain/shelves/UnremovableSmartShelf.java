package leibooks.domain.shelves;

import java.util.function.Predicate;

import leibooks.domain.core.ILibrary;
import leibooks.domain.facade.IDocument;
import leibooks.utils.UnRemovable;

/**
 * An UnremovableSmartShelf represents a smart shelf that cannot be removed.
 * It extends SmartShelf and implements the UnRemovable marker interface.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class UnremovableSmartShelf extends SmartShelf implements UnRemovable {
    /**
     * Constructs an UnremovableSmartShelf with the specified name and criteria.
     * 
     * @param name     the unique name of the shelf
     * @param criteria the predicate to determine which documents belong to the shelf
     */

    public UnremovableSmartShelf(String name, ILibrary lib, Predicate<IDocument> criteria) {
        super(name, lib, criteria);
    }
}
