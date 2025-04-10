package leibooks.domain.shelves;

import static org.junit.jupiter.api.Assertions.*;
import java.util.function.Predicate;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leibooks.domain.core.MockDocument;
import leibooks.domain.facade.IDocument;
import leibooks.domain.core.Library;

class ShelvesTest {
    
    private Shelves shelves;
    private Library library; // Define the library field
    
    @BeforeEach
    void setUp() {
        library = new Library(); // Initialize the library
        shelves = new Shelves(library);
    }
    
    @Test
    void testAddNormalShelf() {
        boolean result = shelves.addNormalShelf("TestShelf");
        
        assertTrue(result);
        
        boolean found = false;
        for (IShelf shelf : shelves) {
            if ("TestShelf".equals(shelf.getName())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    
    @Test
    void testAddDuplicateNormalShelf() {
        shelves.addNormalShelf("TestShelf");
        
        boolean result = shelves.addNormalShelf("TestShelf");
        
        assertFalse(result);
    }
    
    @Test
    void testAddSmartShelf() {
        Predicate<IDocument> criteria = doc -> true;
        
        boolean result = shelves.addSmartShelf("SmartShelf", criteria);
        
        assertTrue(result);
        
        boolean found = false;
        for (IShelf shelf : shelves) {
            if ("SmartShelf".equals(shelf.getName())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    
    @Test
    void testAddDuplicateSmartShelf() {
        Predicate<IDocument> criteria = doc -> true;
        shelves.addSmartShelf("SmartShelf", criteria);
        
        boolean result = shelves.addSmartShelf("SmartShelf", criteria);
        
        assertFalse(result);
    }
    
    @Test
    void testRemoveShelf() throws OperationNotSupportedException {
        shelves.addNormalShelf("ShelfToRemove");
        
        shelves.removeShelf("ShelfToRemove");
        
        for (IShelf shelf : shelves) {
            assertNotEquals("ShelfToRemove", shelf.getName());
        }
    }
    
    @Test
    void testRemoveNonExistentShelf() {
        assertThrows(OperationNotSupportedException.class, () -> {
            shelves.removeShelf("NonExistentShelf");
        });
    }

    
    @Test
    void testAddDocumentToNonExistentShelf() {
        IDocument document = new MockDocument("TestDoc.pdf");
        
        assertThrows(OperationNotSupportedException.class, () -> {
            shelves.addDocument("NonExistentShelf", document);
        });
    }
    
    @Test
    void testRemoveDocumentFromNonExistentShelf() {
        IDocument document = new MockDocument("TestDoc.pdf");
        
        assertThrows(OperationNotSupportedException.class, () -> {
            shelves.removeDocument("NonExistentShelf", document);
        });
    }
    
    @Test
    void testGetDocumentsFromNonExistentShelf() {
        Iterable<IDocument> documents = shelves.getDocuments("NonExistentShelf");
        
        assertFalse(documents.iterator().hasNext());
    }
    
    @Test
    void testIterator() {
        shelves.addNormalShelf("Shelf1");
        shelves.addNormalShelf("Shelf2");
        
        int count = 0;
        for (IShelf shelf : shelves) {
            count++;
        }
        
        assertEquals(2, count);
    }

    @Test
    void testRemoveDocumentFromNormalShelf() throws OperationNotSupportedException {
        shelves.addNormalShelf("TestShelf");
        IDocument document = new MockDocument("TestDoc.pdf");
        shelves.addDocument("TestShelf", document);
        
        shelves.removeDocument("TestShelf", document);
        
        Iterable<IDocument> documents = shelves.getDocuments("TestShelf");
        for (IDocument doc : documents) {
            assertNotEquals(document, doc);
        }
    }

    @Test
    void testSmartShelfAutomaticallyContainsMatchingDocuments() throws OperationNotSupportedException {
        Library library = new Library();
        MockDocument document = new MockDocument("TestDoc.pdf");
        library.addDocument(document);
        
        Shelves smartShelves = new Shelves(library);
        
        Predicate<IDocument> allDocuments = doc -> true;
        smartShelves.addSmartShelf("SmartShelf", allDocuments);
        
        Iterable<IDocument> documents = smartShelves.getDocuments("SmartShelf");
        boolean found = false;
        for (IDocument doc : documents) {
            if (doc.equals(document)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testGetAllDocuments() {
        MockDocument doc1 = new MockDocument("Test1.jpg");
        MockDocument doc2 = new MockDocument("Test2.jpg");
        MockDocument doc3 = new MockDocument("Test3.jpg");
        
        library.addDocument(doc1);
        library.addDocument(doc2);
        library.addDocument(doc3);
        
        int count = 0;
        for (IDocument doc : library) {
            count++;
        }
        assertEquals(3, count);
        assertEquals(3, library.getNumberOfDocuments());
    }
}