package leibooks.domain.shelves;

import javax.naming.OperationNotSupportedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;
/**
 * Represents a shelf in the LEIBooks system.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public abstract class AShelf implements IShelf {
    protected String name;
    protected List<IDocument> documents;

    /**
     * Constructs a new shelf with the specified name.
     * 
     * @param name the name of the shelf
     * @requires name != null
     */
    public AShelf(String name) {
        this.name = name;
        this.documents = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean addDocument(IDocument document) throws OperationNotSupportedException {
        if(documents.contains(document)) {
            return false;
        }
        return documents.add(document);
    }

    @Override
    public boolean removeDocument(IDocument document) throws OperationNotSupportedException{
        if(!documents.contains(document)) {
            return false;
        }
        return documents.remove(document);
    }

    @Override
    public void processEvent(DocumentEvent event) {
        if (event instanceof RemoveDocumentEvent removeEvent) {
            try {
                removeDocument(removeEvent.getDocument());
            } catch (OperationNotSupportedException e) {
                System.err.println("Error removing document: " + e.getMessage());
            }
        }else if(event instanceof AddDocumentEvent addEvent){
            try {
                    addDocument(addEvent.getDocument());         
            } catch (OperationNotSupportedException e) {
                System.err.println("Error adding document: " + e.getMessage());
            }
        }else {
            System.err.println("Unknown event type: " + event.getClass().getName());
        }
    }
    
    @Override
    public Iterator<IDocument> iterator() {
        return documents.iterator();
    }
}
