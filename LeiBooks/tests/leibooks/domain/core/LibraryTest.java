package leibooks.domain.core;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;

class LibraryTest {

	private Library library;

	@BeforeEach
	void setUp() {
		library = new Library();
	}

	@Test
	void testAddNewDocument() {
		MockDocument doc = new MockDocument("Test.jpg");
		assertTrue(library.addDocument(doc));
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertTrue(s.anyMatch(d -> d.equals(doc)));
		assertEquals(1, library.getNumberOfDocuments());
	}

	@Test
	void testAddDuplicatedDocument() {
		MockDocument doc = new MockDocument("Test.jpg");

		assertTrue(library.addDocument(doc));
		assertFalse(library.addDocument(doc));
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertTrue(s.anyMatch(d -> d.equals(doc)));
		assertEquals(1, library.getNumberOfDocuments());
	}


	@Test
	void testDeleteExistingDocument() {
		MockDocument doc = new MockDocument("Test.jpg");
		library.addDocument(doc);

		library.removeDocument(doc);
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertFalse(s.anyMatch(d -> d.equals(doc)));
		assertEquals(0, library.getNumberOfDocuments());
	}

	@Test
	void testDeleteNotExistingDocument() {
		MockDocument doc1 = new MockDocument("One.jpg");
		MockDocument doc2 = new MockDocument("Two.jpg");

		library.addDocument(doc1);
		library.removeDocument(doc2);
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertFalse(s.anyMatch(d -> d.equals(doc2)));
		assertEquals(1, library.getNumberOfDocuments());
	}


	@Test
	void testUpdateDocument() {
		MockDocument doc = new MockDocument("Test.jpg");
		DocumentProperties props = new DocumentProperties(doc);
		props.setTitle("New Title");
		props.setAuthor("New Author");
		
		library.addDocument(doc);
		library.updateDocument(doc, props);
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertTrue(s.anyMatch(d -> d.equals(doc)));
		assertEquals(1, library.getNumberOfDocuments());
	}

	@Test
	void testGetMatchesEmpty() {
		Collection<IDocument> matches = library.getMatches(".*");
		assertNotNull(matches);
		assertTrue(matches.isEmpty());
	}

	@Test
	void testGetMatchesNotEmpty() {
		MockDocument docY = new MockDocument("Y.jpg",true);
		MockDocument docN = new MockDocument("N.jpg",false);

		library.addDocument(docY);
		library.addDocument(docN);
		Collection<IDocument> matches = library.getMatches(".*");

		assertNotNull(matches);
		assertEquals(1, matches.size());
	}

    @Test
    void testIterator() {
        MockDocument doc1 = new MockDocument("Test1.jpg");
        MockDocument doc2 = new MockDocument("Test2.jpg");
        MockDocument doc3 = new MockDocument("Test3.jpg");
        
        library.addDocument(doc1);
        library.addDocument(doc2);
        library.addDocument(doc3);
        
        int count = 0;
        for (IDocument doc : library) {
            assertTrue(doc.equals(doc1) || doc.equals(doc2) || doc.equals(doc3));
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    void testUpdateNonExistingDocument() {
        MockDocument doc = new MockDocument("Test.jpg");
        DocumentProperties props = new DocumentProperties(doc);
        props.setTitle("New Title");
        props.setAuthor("New Author");
        
        library.updateDocument(doc, props);
        
        assertEquals("", doc.getTitle());
        assertEquals("", doc.getAuthor());
    }
    
    @Test
    void testMultipleDocumentsManagement() {
        MockDocument doc1 = new MockDocument("One.jpg");
        MockDocument doc2 = new MockDocument("Two.jpg");
        MockDocument doc3 = new MockDocument("Three.jpg");
        
        assertTrue(library.addDocument(doc1));
        assertTrue(library.addDocument(doc2));
        assertTrue(library.addDocument(doc3));
        assertEquals(3, library.getNumberOfDocuments());
        
        library.removeDocument(doc2);
        assertEquals(2, library.getNumberOfDocuments());
        
        Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
        assertTrue(s.anyMatch(d -> d.equals(doc1)));
        
        s = StreamSupport.stream(library.spliterator(), false);
        assertFalse(s.anyMatch(d -> d.equals(doc2)));
        
        s = StreamSupport.stream(library.spliterator(), false);
        assertTrue(s.anyMatch(d -> d.equals(doc3)));
    }
    
    @Test
    void testGetMatchesWithSpecificPattern() {
        MockDocument docA = new MockDocument("A.jpg", true);
        MockDocument docB = new MockDocument("B.jpg", true);
        MockDocument docC = new MockDocument("C.jpg", false);
        
        library.addDocument(docA);
        library.addDocument(docB);
        library.addDocument(docC);
        
        Collection<IDocument> matches = library.getMatches("specific-pattern");
        
        assertEquals(2, matches.size());
        assertTrue(matches.contains(docA));
        assertTrue(matches.contains(docB));
        assertFalse(matches.contains(docC));
    }
    
    @Test
    void testEqualsAndHashCode() {
        Library library1 = new Library();
        Library library2 = new Library();
        
        assertTrue(library1.equals(library2));
        assertEquals(library1.hashCode(), library2.hashCode());
        
        MockDocument doc = new MockDocument("Test.jpg");
        library1.addDocument(doc);
        library2.addDocument(doc);
        
        assertTrue(library1.equals(library2));
        assertEquals(library1.hashCode(), library2.hashCode());
        
        MockDocument doc2 = new MockDocument("Different.jpg");
        library2.addDocument(doc2);
        
        assertFalse(library1.equals(library2));
        
        library1.addDocument(doc2);
        
        assertTrue(library1.equals(library2));
        assertEquals(library1.hashCode(), library2.hashCode());
    }
}