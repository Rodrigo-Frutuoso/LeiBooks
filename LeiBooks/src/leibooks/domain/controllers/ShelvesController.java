package leibooks.domain.controllers;

import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;
import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.IShelvesController;
import leibooks.domain.facade.events.AddShelfEvent;
import leibooks.domain.facade.events.LBEvent;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.domain.shelves.IShelves;
import leibooks.domain.shelves.IShelf;
import leibooks.utils.Listener;
/**
 * ShelvesController is responsible for managing shelves and their events.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class ShelvesController implements IShelvesController {
    private IShelves shelves;
    private List<Listener<LBEvent>> listeners = new ArrayList<>();

    /**
     * Constructs a new ShelvesController for the specified shelves.
     * @param shelves
     * @requires shelves != null
     */
    public ShelvesController(IShelves shelves) {
        this.shelves = shelves;
    }

    @Override
    public void emitEvent(LBEvent e) {
        for (Listener<LBEvent> listener : listeners) {
            listener.processEvent(e);
        }
    }

    @Override
    public void registerListener(Listener<LBEvent> obs) {
        listeners.add(obs);
    }

    @Override
    public void unregisterListener(Listener<LBEvent> obs) {
        listeners.remove(obs);
    }

    @Override
    public void processEvent(ShelfEvent e) {
        emitEvent(e);
    }

    @Override
    public boolean addNormalShelf(String name) {
        emitEvent(new AddShelfEvent(name));
        return shelves.addNormalShelf(name);
    }

    @Override
    public boolean addSmartShelf(String name, Predicate<IDocument> criteria) {
        emitEvent(new AddShelfEvent(name));
        return shelves.addSmartShelf(name, criteria);
    }

    @Override
    public Iterable<String> getShelves() {
        List<String> shelfNames = new ArrayList<>();
        for (IShelf shelf : shelves) {
            shelfNames.add(shelf.getName());
        }
        return shelfNames;
    }

    @Override
    public void remove(String name) throws OperationNotSupportedException {
        shelves.removeShelf(name);
    }

    @Override
    public void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
        shelves.removeDocument(shelfName, document);
    }

    @Override
    public boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
        return shelves.addDocument(shelfName, document);
    }

    @Override
    public Iterable<IDocument> getDocuments(String shelfName) {
        return shelves.getDocuments(shelfName);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Shelves=\n");
        for (IShelf shelf : shelves) {
            result.append(shelf.getName()).append(" = [");
            StringBuilder shelfInfo = new StringBuilder();
            for (IDocument document : shelf) { // Iterar diretamente sobre os documentos
                shelfInfo.append(document.getFile()).append(", ");
            }
            if (shelfInfo.length() == 0) {
                result.append("]\n");
            } else {
                result.append(shelfInfo.substring(0, shelfInfo.length() - 2)).append("]\n");
            }
        }
        return result.toString();
    }
}
