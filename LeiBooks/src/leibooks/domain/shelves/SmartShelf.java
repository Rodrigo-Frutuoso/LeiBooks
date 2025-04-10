package leibooks.domain.shelves;

import java.util.function.Predicate;


import leibooks.domain.core.ILibrary;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;

/**
 * A SmartShelf represents a shelf that automatically categorizes documents
 * based on a given predicate.
 *
 * Smart shelves do not support explicit addition or removal of documents.
 *
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class SmartShelf extends AShelf {

    private Predicate<IDocument> criteria;
    /**
     * Constructs a SmartShelf with the specified name, library, and criteria.
     *
     * @param name     the unique name of the shelf
     * @param lib      the library associated with the shelf
     * @param criteria the predicate to determine which documents belong to the shelf
     */
    public SmartShelf(String name, ILibrary lib, Predicate<IDocument> criteria) {
        super(name);
        lib.registerListener(this);
        for(IDocument document : lib) {
            if(criteria.test(document)) {
                documents.add(document);
            }
        }
    }

    @Override
    public boolean addDocument(IDocument document) {
        throw new UnsupportedOperationException("SmartShelf does not support adding documents explicitly.");
    }

    @Override
    public boolean removeDocument(IDocument document) {
        throw new UnsupportedOperationException("SmartShelf does not support removing documents explicitly.");
    }

    @Override
    public void processEvent(DocumentEvent event) {
        if (event instanceof AddDocumentEvent) {
            if (criteria.test(event.getDocument())) {
                documents.add(event.getDocument());
            }
        } else if (event instanceof RemoveDocumentEvent removeEvent) {
            if (documents.contains(removeEvent.getDocument())) {
                documents.remove(removeEvent.getDocument());
            }
        }
    }
}
