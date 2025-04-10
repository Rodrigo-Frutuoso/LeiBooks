package leibooks.domain.shelves;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import javax.naming.OperationNotSupportedException;

import leibooks.domain.core.ILibrary;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddShelfEvent;
import leibooks.domain.facade.events.RemoveDocumentShelfEvent;
import leibooks.domain.facade.events.RemoveShelfEvent;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

/**
 * The Shelves class represents a collection of shelves in the library system.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class Shelves extends AbsSubject<ShelfEvent> implements IShelves {
    private ILibrary library;
    private List<IShelf> shelfList;

    /**
     * Constructs a new Shelves object with the specified library.
     * 
     * @param library the library associated with this collection of shelves
     */
    public Shelves(ILibrary library) {
        this.library = library;
        this.shelfList = new ArrayList<>();
    }

    @Override
    public Iterator<IShelf> iterator() {
        return shelfList.iterator();
    }

    @Override
    public void emitEvent(ShelfEvent e) {
        super.emitEvent(e);
    }

    @Override
    public void registerListener(Listener<ShelfEvent> obs) {
        super.registerListener(obs);
    }

    @Override
    public void unregisterListener(Listener<ShelfEvent> obs) {
        super.unregisterListener(obs);
    }

    @Override
    public boolean addNormalShelf(String shelfName) {
        if(getShelf(shelfName) != null) {
            return false;
        }
        IShelf shelf = new NormalShelf(shelfName);
        boolean added = shelfList.add(shelf);
        if (added) {
            emitEvent(new AddShelfEvent(shelfName));
        }
        return added;
    }

    @Override
    public boolean addSmartShelf(String shelfName, Predicate<IDocument> criteria) {
        if(getShelf(shelfName) != null) {
            return false;
        }
        IShelf shelf = new SmartShelf(shelfName, library, criteria);
        boolean added = shelfList.add(shelf);
        if (added) {
            emitEvent(new AddShelfEvent(shelfName));
        }
        return added;
    }

    @Override
    public void removeShelf(String shelfName) throws OperationNotSupportedException {
        IShelf shelf = getShelf(shelfName);
        if (shelf != null) {
            boolean removed = shelfList.remove(shelf);
            if (removed) {
                emitEvent(new RemoveShelfEvent(shelfName));
            }
        } else {
            throw new OperationNotSupportedException("Shelf not found: " + shelfName);
        }
    }

    @Override
    public void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
        IShelf shelf = getShelf(shelfName);
        if (shelf != null) {
            shelf.removeDocument(document);
            emitEvent(new RemoveDocumentShelfEvent(shelfName, document));
        } else {
            throw new OperationNotSupportedException("Shelf not found: " + shelfName);
        }
    }

    @Override
    public boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
        IShelf shelf = getShelf(shelfName);
        if (shelf != null) {
            boolean added = shelf.addDocument(document);
            if (added) {
                //emitEvent(new AddDocumentShelfEvent(shelfName, document));
                //deveria ter este evento, mas não tem
            }
            return added;
        } else {
            throw new OperationNotSupportedException("Shelf not found: " + shelfName);
        }
    }

    @Override
    public Iterable<IDocument> getDocuments(String shelfName) {
        IShelf shelf = getShelf(shelfName);
        if (shelf != null) {
            return shelf; // IShelf é um Iterable<IDocument>
        }
        return new ArrayList<>();
    }

    private IShelf getShelf(String shelfName) {
        for (IShelf shelf : shelfList) {
            if (shelf.getName().equals(shelfName)) {
                return shelf;
            }
        }
        return null;
    }
}
