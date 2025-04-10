package leibooks.domain.core;

import java.io.File;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddAnnotationEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveAnnotationEvent;
import leibooks.domain.facade.events.ToggleBookmarkEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

/**
 * Represents a document in the LEIBooks system.
 *
 * This class implements the IDocument interface and represents a document that can be
 * stored in the library. It maintains information about the document's metadata, content,
 * pages, bookmarks, and annotations. The Document class also implements the Observer pattern
 * through AbsSubject to notify listeners of changes to the document.
 *
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */

public class Document extends AbsSubject<DocumentEvent> implements IDocument {

    private String title;
    private String author;
    private LocalDate dateModified;
    private String mimeType;
    private final File file;
    private Optional<Integer> numberOfPages;
    private int lastPageVisited;
    private Map<Integer, Page> pages;

    /**
     * Constructs a new Document with the specified properties.

     * Initializes the document with the provided metadata and creates Page objects
     * for each page if the number of pages is specified.
     *
     * @param expectedTitle the title of the document
     * @param expectedAuthor the author of the document
     * @param expectedModifiedDate the last modification date of the document
     * @param expectedMimeType the MIME type of the document
     * @param expectedPath the file path to the document on disk
     * @param expectedNumPages an Optional containing the number of pages, or empty if not applicable
     */
    public Document(String expectedTitle, String expectedAuthor, LocalDate expectedModifiedDate,
                    String expectedMimeType, String expectedPath, Optional<Integer> expectedNumPages) {
        this.title = expectedTitle;
        this.author = expectedAuthor;
        this.dateModified = expectedModifiedDate;
        this.mimeType = expectedMimeType;
        this.file = new File(expectedPath);
        this.numberOfPages = expectedNumPages.or(() -> Optional.of(1));
        this.pages = new TreeMap<>();
        this.lastPageVisited = 0;

        this.numberOfPages.ifPresent(numPages -> {
            for (int i = 0; i < numPages; i++) {
                this.pages.put(i, new Page(i));
            }
        });
    }

    @Override
    public void emitEvent(DocumentEvent e) {
        super.emitEvent(e);
   }

    @Override
    public void registerListener(Listener<DocumentEvent> obs) {
        super.registerListener(obs);
    }

    @Override
    public void unregisterListener(Listener<DocumentEvent> obs) {
        super.unregisterListener(obs);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public LocalDate getLastModifiedDate() {
        return dateModified;
    }

    @Override
    public Optional<Integer> getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public int getLastPageVisited() {
        return lastPageVisited;
    }

    @Override
    public void setLastPageVisited(int lastPageVisited) {
        if (lastPageVisited < 1 || lastPageVisited > numberOfPages.orElse(0)) {
            return;
        }
        this.lastPageVisited = lastPageVisited;
    }

    @Override
    public List<Integer> getBookmarks() {
        List<Integer> bookmarks = new LinkedList<>();
        for (Map.Entry<Integer, Page> entry : pages.entrySet()) {
            if (entry.getValue().isBookmarked()) {
                bookmarks.add(entry.getKey() + 1);
            }
        }
        return bookmarks;
    }

    @Override
    public void toggleBookmark(int pageNum) {
        Page page = pages.get(pageNum - 1);
        if (page == null) {
            return;
        }
        page.toggleBookmark();
        emitEvent(new ToggleBookmarkEvent(this, pageNum, page.isBookmarked()));
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        this.dateModified = LocalDate.now();
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
        this.dateModified = LocalDate.now();
    }

    @Override
    public void addAnnotation(int pageNum, String text) {
        Page page = pages.get(pageNum - 1);
        if (page == null) {
            return;
        }
        page.addAnnotation(text);
        this.dateModified = LocalDate.now();
        emitEvent(new AddAnnotationEvent(this, pageNum, page.getAnnotationCount(), text, page.hasAnnotations()));
    }

    @Override
    public void removeAnnotation(int pageNum, int annotNum) {
        Page page = pages.get(pageNum - 1);
        if (page == null) {
            return;
        }
        if (annotNum < 0 || annotNum >= page.getAnnotationCount()) {
            return;
        }
        page.removeAnnotation(annotNum);
        this.dateModified = LocalDate.now();
        emitEvent(new RemoveAnnotationEvent(this, pageNum, annotNum, page.hasAnnotations()));
    }

    @Override
    public int numberOfAnnotations(int pageNum) {
        Page page = pages.get(pageNum - 1);
        if (page == null) {
            return 0;
        }
        return page.getAnnotationCount();
    }

    @Override
    public Iterable<String> getAnnotations(int pageNum) {
        Page page = pages.get(pageNum - 1);
        if (page == null) {
            return new LinkedList<>();
        }
        List<String> annotations = new LinkedList<>();
        for (Annotation annotation : page.getAnnotations()) {
            annotations.add(annotation.getAnnotationText());
        }
        return annotations;
    }

    @Override
    public String getAnnotationText(int pageNum, int annotNum) {
        Page page = pages.get(pageNum-1);
        if (page == null) {
            return null;
        }
        return page.getAnnotationText(annotNum);
    }

    @Override
    public boolean hasAnnotations(int pageNum) {
        Page page = pages.get(pageNum - 1);
        if (page == null) {
            return false;
        }
        return page.hasAnnotations();
    }

    @Override
    public boolean isBookmarked(int pageNum) {
        Page page = pages.get(pageNum - 1);
        if (page == null) {
            return false;
        }
        return page.isBookmarked();
    }

    @Override
    public boolean isBookmarked() {
        return !getBookmarks().isEmpty();
    }

    @Override
    public boolean matches(String regexp) {
        return (title != null && title.matches(regexp)) || (author != null && author.matches(regexp));
    }

    @Override
    public int compareTo(IDocument other) {
        return file.compareTo(other.getFile());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(file.getAbsolutePath(), document.file.getAbsolutePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(file.getAbsolutePath());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Page page : pages.values()) {
            if (page.isBookmarked() || page.hasAnnotations()) {
                sb.append(page.getPageNum()).append("=").append(page).append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");

        return "Document{" +
                "title=" + title +
                ", author=" + author +
                ", file=" + file.getName() +
                ", date=" + dateModified +
                ", mimeType=" + mimeType +
                ", numPages=" + numberOfPages +
                ", lastPageVisited=" + lastPageVisited +
                ", pages=" + sb.toString() +
                '}';
    }
}
