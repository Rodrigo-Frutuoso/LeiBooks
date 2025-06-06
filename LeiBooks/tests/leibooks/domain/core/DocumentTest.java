package leibooks.domain.core;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class DocumentTest {

	@Test
	void testCreateDocumentWithMetaData() {
		String expectedTitle = "Test Title", expectedAuthor = "Test Author", expectedPath = "TestFile.pdf", expectedMimeType = "application/pdf";
		LocalDate expectedModifiedDate = LocalDate.of(2025, 1, 1);
		Optional<Integer> expectedNumPages = Optional.of(10);
		Document doc = new Document(expectedTitle, expectedAuthor, expectedModifiedDate, expectedMimeType, expectedPath, expectedNumPages);

		String actualTitle = doc.getTitle();
		assertEquals(expectedTitle, actualTitle);

		String actualAuthor = doc.getAuthor();
		assertEquals(expectedAuthor, actualAuthor);

		LocalDate actualModifiedDate = doc.getLastModifiedDate();
		assertEquals(expectedModifiedDate, actualModifiedDate);
		
		String actualMimeType = doc.getMimeType();
		assertEquals(expectedMimeType, actualMimeType);

		boolean isBookmarked = doc.isBookmarked();
		assertFalse(isBookmarked);     
		assertTrue(doc.getBookmarks().isEmpty());
		
		Optional<Integer> actualNumPages = doc.getNumberOfPages();
		assertEquals(expectedNumPages, actualNumPages);
		
		File actualFile = doc.getFile();
		assertEquals(new File(expectedPath), actualFile);
	
		assertEquals(0, doc.getLastPageVisited());
		assertFalse(doc.hasAnnotations(2));
		assertThrows(NoSuchElementException.class, () -> doc.getAnnotations(3).iterator().next());
	}

	@Test
	void testToggleBookmarkOnce() {
		String expectedTitle = "Test Title", expectedAuthor = "Test Author", expectedPath = "TestFile.pdf", expectedMimeType = "application/pdf";
		LocalDate expectedModifiedDate = LocalDate.of(2025, 1, 1);
		Optional<Integer> expectedNumPages = Optional.of(10);
		Document doc = new Document(expectedTitle, expectedAuthor, expectedModifiedDate, expectedMimeType, expectedPath, expectedNumPages);
		
		doc.toggleBookmark(1);
		assertTrue(doc.isBookmarked(1));
		assertTrue(doc.isBookmarked());
	}

	@Test
	void testToggleBookmarkTwice() {
		String expectedTitle = "Test Title", expectedAuthor = "Test Author", expectedPath = "TestFile.pdf", expectedMimeType = "application/pdf";
		LocalDate expectedModifiedDate = LocalDate.of(2025, 1, 1);
		Optional<Integer> expectedNumPages = Optional.of(10);
		Document doc = new Document(expectedTitle, expectedAuthor, expectedModifiedDate, expectedMimeType, expectedPath, expectedNumPages);

		doc.toggleBookmark(2);
		assertTrue(doc.isBookmarked(2));
		doc.toggleBookmark(2);
		assertFalse(doc.isBookmarked(2));
		assertFalse(doc.isBookmarked());
	}

	@Test
	void testToggleBookmarkDifferentPages() {
		String expectedTitle = "Test Title", expectedAuthor = "Test Author", expectedPath = "TestFile.pdf", expectedMimeType = "application/pdf";
		LocalDate expectedModifiedDate = LocalDate.of(2025, 1, 1);
		Optional<Integer> expectedNumPages = Optional.of(10);
		Document doc = new Document(expectedTitle, expectedAuthor, expectedModifiedDate, expectedMimeType, expectedPath, expectedNumPages);

		doc.toggleBookmark(1);
		doc.toggleBookmark(2);
		assertTrue(doc.isBookmarked(1));
		assertTrue(doc.isBookmarked(2));
	
		doc.toggleBookmark(2);
		assertTrue(doc.isBookmarked(1));
		assertFalse(doc.isBookmarked(2));
		assertTrue(doc.isBookmarked());
	}

	@Test
	void testNoMatches() {
		String expectedTitle = "Test Title", expectedAuthor = "Test Author", expectedPath = "TestFile.pdf", expectedMimeType = "application/pdf";
		LocalDate expectedModifiedDate = LocalDate.of(2025, 1, 1);
		Optional<Integer> expectedNumPages = Optional.of(10);
		Document doc = new Document(expectedTitle, expectedAuthor, expectedModifiedDate, expectedMimeType, expectedPath, expectedNumPages);
		String regexp = "Exp.*";

		boolean matches = doc.matches(regexp);
		assertFalse(matches);
	}

	@Test
	void testMatchesTitle() {
		String expectedTitle = "Test Title", expectedAuthor = "Test Author", expectedPath = "TestFile.pdf", expectedMimeType = "application/pdf";
		LocalDate expectedModifiedDate = LocalDate.of(2025, 1, 1);
		Optional<Integer> expectedNumPages = Optional.of(10);
		Document doc = new Document(expectedTitle, expectedAuthor, expectedModifiedDate, expectedMimeType, expectedPath, expectedNumPages);
		String regexp = ".*itl.*";

		boolean matches = doc.matches(regexp);
		assertTrue(matches);
	}

	@Test
	void testMatchesAuthor() {
		String expectedTitle = "Test Title", expectedAuthor = "Test Author", expectedPath = "TestFile.pdf", expectedMimeType = "application/pdf";
		LocalDate expectedModifiedDate = LocalDate.of(2025, 1, 1);
		Optional<Integer> expectedNumPages = Optional.of(10);
		Document doc = new Document(expectedTitle, expectedAuthor, expectedModifiedDate, expectedMimeType, expectedPath, expectedNumPages);
		String regexp = ".*utho.*";

		boolean matches = doc.matches(regexp);
		assertTrue(matches);
	}

	@Test
	void testEqualsSame() {
		Document doc1 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		Document doc2 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		assertTrue(doc1.equals(doc2));
	}

	@Test
	void testEqualsSameFile() {
		Document doc1 = new Document("Test Title1", "Test Author1", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		Document doc2 = new Document("Test Title2", "Test Author2", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.empty());
		assertTrue(doc1.equals(doc2));
	}

	@Test
	void testEqualsDifferentFiles() {
		Document doc1 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile1.pdf", Optional.empty());
		Document doc2 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile2.pdf", Optional.empty());
		assertFalse(doc1.equals(doc2));
	}

	@Test
	void testCompareToSame() {
		Document doc1 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		Document doc2 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		assertEquals(0, doc1.compareTo(doc2));
	}

	@Test
	void testCompareToSameFile() {
		Document doc1 = new Document("Test Title1", "Test Author1", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		Document doc2 = new Document("Test Title2", "Test Author2", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(0));
		assertEquals(0, doc1.compareTo(doc2));
	}

	@Test
	void testCompareToDifferentFile() {
		Document doc1 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile1.pdf", Optional.empty());
		Document doc2 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile2.pdf", Optional.empty());
		assertFalse(doc1.compareTo(doc2) == 0);
	}

	@Test
	void testHashCodeSame() {
		Document doc1 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		Document doc2 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
		assertEquals(doc1.hashCode(), doc2.hashCode());
	}

	@Test
	void testToStringNewDocument() {
		LocalDate date = LocalDate.now();
		Document doc = new Document("Test Title", "Test Author", date, "application/pdf", "TestFile.pdf", Optional.of(10));
		String expected = "Document{title=Test Title, author=Test Author, file=TestFile.pdf, date=" + 
				date + ", mimeType=application/pdf, numPages=Optional[10], lastPageVisited=0, pages={}}";
		assertEquals(expected, doc.toString());
	}
	
	@Test
	void testToStringDocumentWithBookmarks() {
		LocalDate date = LocalDate.now();
		Document doc = new Document("Test Title", "Test Author", date, "application/pdf", "TestFile.pdf", Optional.of(10));
		doc.toggleBookmark(2);
		String expected = "Document{title=Test Title, author=Test Author, file=TestFile.pdf, date=" + date +
				", mimeType=application/pdf, numPages=Optional[10], lastPageVisited=0, pages={2=Page{bookmark=true, annotations=[], pageNum=2}}}";
		assertEquals(expected, doc.toString());
	}
	
	@Test
	void testToStringDocumentWithAnnotations() {
		LocalDate date = LocalDate.now();
		Document doc = new Document("Test Title", "Test Author", date, "application/pdf", "TestFile.pdf", Optional.of(10));
		doc.addAnnotation(3, "test");
		String expected = "Document{title=Test Title, author=Test Author, file=TestFile.pdf, date=" + date +
			 ", mimeType=application/pdf, numPages=Optional[10], lastPageVisited=0, pages={3=Page{bookmark=false, annotations=[Annotation [text=test]], pageNum=3}}}";
		assertEquals(expected, doc.toString());
	}

	@Test
	void testAddAnnotation() {
	    Document doc = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
	    String annotationText = "This is an important section";
	    doc.addAnnotation(5, annotationText);
	    
	    assertTrue(doc.hasAnnotations(5));
	    assertEquals(1, doc.numberOfAnnotations(5));
	    assertEquals(annotationText, doc.getAnnotationText(5, 0));
	}

	@Test
	void testAddMultipleAnnotations() {
	    Document doc = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
	    doc.addAnnotation(4, "First annotation");
	    doc.addAnnotation(4, "Second annotation");
	    
	    assertEquals(2, doc.numberOfAnnotations(4));
	    assertEquals("First annotation", doc.getAnnotationText(4, 0));
	    assertEquals("Second annotation", doc.getAnnotationText(4, 1));
	}

	@Test
	void testRemoveAnnotation() {
	    Document doc = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
	    doc.addAnnotation(3, "Test annotation");
	    assertEquals(1, doc.numberOfAnnotations(3));
	    
	    doc.removeAnnotation(3, 0);
	    assertEquals(0, doc.numberOfAnnotations(3));
	    assertFalse(doc.hasAnnotations(3));
	}

	@Test
	void testGetAnnotations() {
	    Document doc = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
	    doc.addAnnotation(2, "Annotation 1");
	    doc.addAnnotation(2, "Annotation 2");
	    
	    Iterable<String> annotations = doc.getAnnotations(2);
	    Iterator<String> iterator = annotations.iterator();
	    assertEquals("Annotation 1", iterator.next());
	    assertEquals("Annotation 2", iterator.next());
	    assertFalse(iterator.hasNext());
	}

	@Test
	void testSetLastPageVisited() {
	    Document doc = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
	    assertEquals(0, doc.getLastPageVisited());
	    
	    doc.setLastPageVisited(5);
	    assertEquals(5, doc.getLastPageVisited());
	}

	@Test
	void testSetTitle() {
	    Document doc = new Document("Old Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
	    
	    doc.setTitle("New Title");
	    assertEquals("New Title", doc.getTitle());
	}

	@Test
	void testSetAuthor() {
	    Document doc = new Document("Test Title", "Old Author", LocalDate.now(), "application/pdf", "TestFile.pdf", Optional.of(10));
	    
	    doc.setAuthor("New Author");
	    assertEquals("New Author", doc.getAuthor());
	}

	@Test
	void testHashCodeDifferentFiles() {
	    Document doc1 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile1.pdf", Optional.of(10));
	    Document doc2 = new Document("Test Title", "Test Author", LocalDate.now(), "application/pdf", "TestFile2.pdf", Optional.of(10));
	    assertNotEquals(doc1.hashCode(), doc2.hashCode());
	}

	@Test
	void testToStringDocumentWithBookmarksAndAnnotations() {
	    LocalDate date = LocalDate.now();
	    Document doc = new Document("Test Title", "Test Author", date, "application/pdf", "TestFile.pdf", Optional.of(10));
	    doc.toggleBookmark(2);
	    doc.addAnnotation(2, "important note");
	    
	    String expected = "Document{title=Test Title, author=Test Author, file=TestFile.pdf, date=" + date +
	            ", mimeType=application/pdf, numPages=Optional[10], lastPageVisited=0, pages={2=Page{bookmark=true, annotations=[Annotation [text=important note]], pageNum=2}}}";
	    assertEquals(expected, doc.toString());
	}

}