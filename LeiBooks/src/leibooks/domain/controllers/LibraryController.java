package leibooks.domain.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import leibooks.domain.core.DocumentFactory;
import leibooks.domain.core.ILibrary;
import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.ILibraryController;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.LBEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

/**
 * Controller for managing library operations in the LEIBooks system.
 *
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class LibraryController extends AbsSubject<LBEvent> implements ILibraryController {
    private ILibrary library;
    /**
     * Constructs a new LibraryController for the specified library.
     *
     * @param library the library to be managed by this controller
     * @requires library != null
     * @ensures this.library == library
     */
    public LibraryController(ILibrary library) {
        this.library = library;
        this.library.registerListener(this);
    }

    @Override
	public void emitEvent(LBEvent e) {
		super.emitEvent(e);

	}

	@Override
	public void registerListener(Listener<LBEvent> obs) {
		super.registerListener(obs);

	}

	@Override
	public void unregisterListener(Listener<LBEvent> obs) {
		super.unregisterListener(obs);

	}

    @Override
    public void processEvent(DocumentEvent e) {
        super.emitEvent(e);
    }

    @Override
    public Iterable<IDocument> getDocuments() {
        return library;
    }

    @Override
    public Optional<IDocument> importDocument(String title, String pathTofile) {
        IDocument document = null;
            document = DocumentFactory.INSTANCE.createDocument(title, pathTofile);
            library.addDocument(document);
            return Optional.of(document);
    }

    @Override
    public void removeDocument(IDocument document) {
        library.removeDocument(document);
    }

    @Override
    public void updateDocument(IDocument document, DocumentProperties documentProperties) {
        library.updateDocument(document, documentProperties);
    }

    @Override
    public List<IDocument> getMatches(String regex) {
        List<IDocument> matches = new ArrayList<>();
        for (IDocument document : library) {
            if (document.matches(regex)) {
                matches.add(document);
            }
        }
        return matches;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LibraryController other = (LibraryController) obj;
        return library == other.library; // Reference equality for the library
    }

    @Override
    public int hashCode() {
        return library == null ? 0 : System.identityHashCode(library);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("LibraryController [");
        for (IDocument doc : library) {
            result.append(doc).append("\n");
        }
        return result+"\n";
    }
}
