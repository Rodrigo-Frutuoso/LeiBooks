package leibooks.domain.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;
import leibooks.utils.Listener;

/**
 * Represents a library of documents in the LEIBooks system.
 * 
 * This class implements the ILibrary interface and manages a collection of documents.
 * It provides methods for adding, removing, and updating documents, as well as searching
 * for documents that match a specific pattern. The Library class also propagates events
 * to registered listeners when documents are modified.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class Library implements ILibrary {

    private List<IDocument> documents;

    /**
     * Constructs a new empty Library.
     * 
     * @ensures this.getNumberOfDocuments() == 0
     */
    public Library() {
        this.documents = new ArrayList<>();
    }


    @Override
    public Iterator<IDocument> iterator() {
        return documents.iterator();
    }

    @Override
    public void emitEvent(DocumentEvent e) {
        for(IDocument document : documents) {
            document.emitEvent(e);
        }
    }

    @Override
    public void registerListener(Listener<DocumentEvent> obs) {
        for(IDocument document : documents) {
            document.registerListener(obs);
        }
    }

    @Override
    public void unregisterListener(Listener<DocumentEvent> obs) {
        for(IDocument document : documents) {
            document.unregisterListener(obs);
        }
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public boolean addDocument(IDocument document) {
        if (!documents.contains(document)) {
            documents.add(document);
            emitEvent(new AddDocumentEvent(document));
            return true;
        }
        return false;
    }

    @Override
    public void removeDocument(IDocument document) {
        if (documents.contains(document)) {
            documents.remove(document);
            emitEvent(new RemoveDocumentEvent(document));
        }
    }

    @Override
    public void updateDocument(IDocument document, DocumentProperties documentProperties) {
        if (documents.contains(document)) {
            document.setTitle(documentProperties.title());
            document.setAuthor(documentProperties.author());
        }
    }

    @Override
    public List<IDocument> getMatches(String regex) {
        List<IDocument> matches = new ArrayList<>();
        for (IDocument document : documents) {
            if (document.matches(regex)) {
                matches.add(document);
            }
        }
        return matches;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Library other = (Library) obj;
        
        if (documents.size() != other.documents.size()) return false;
        
        return documents.containsAll(other.documents);
    }

    @Override
    public int hashCode() {
        return documents != null ? documents.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Library{");
        for(IDocument document : documents) {
            sb.append(document.toString()).append("\n");
        }
        return sb.toString();
    }

}
