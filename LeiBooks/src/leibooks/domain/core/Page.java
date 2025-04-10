package leibooks.domain.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a page in a document within the LEIBooks system.
 *
 * This class encapsulates the properties and operations related to a document page,
 * including its page number, bookmark status, and annotations. Pages can be bookmarked
 * for quick access and can contain multiple text annotations.
 *
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class Page {
    private final int pageNum;

    private boolean bookmarked;

    private List<Annotation> annotations;

    /**
     * Constructs a new Page with the specified page number.
     * The page is initially not bookmarked and has no annotations.
     *
     * @param pageNum the page number within the document
     * @ensures this.pageNum == pageNum && !this.bookmarked && this.annotations.isEmpty()
     */
    public Page(int pageNum) {
        this.pageNum = pageNum;
        this.bookmarked = false;
        this.annotations = new ArrayList<>();
    }

    /**
     * Adds a new annotation to this page.
     *
     * @param text the text of the annotation to add
     * @requires text != null
     * @ensures getAnnotationCount() == \old(getAnnotationCount()) + 1
     */
    public void addAnnotation(String text) {
        annotations.add(new Annotation(text));
    }

    /**
     * Gets the number of annotations on this page.
     *
     * @return the number of annotations
     * @ensures \result >= 0
     */
    public int getAnnotationCount() {
        return annotations.size();
    }

    /**
     * Gets all annotations on this page.
     *
     * @return an Iterable of all annotations
     * @ensures \result != null
     */
    public Iterable<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Gets the text of a specific annotation by its ID.
     *
     * @param annotationId the ID of the annotation
     * @return the text of the annotation, or null if the ID is invalid
     * @ensures (annotationId >= 0 && annotationId < getAnnotationCount()) ==> \result != null
     * @ensures (annotationId < 0 || annotationId >= getAnnotationCount()) ==> \result == null
     */
    public String getAnnotationText(int annotationId) {
        return (annotationId >= 0 && annotationId < annotations.size()) ?
               annotations.get(annotationId).getAnnotationText() : null;
    }

    /**
     * Gets the page number of this page.
     *
     * @return the page number
     * @ensures \result == this.pageNum
     */
    public int getPageNum() {
        return pageNum+1;
    }

    /**
     * Checks if this page has any annotations.
     *
     * @return true if the page has at least one annotation, false otherwise
     * @ensures \result == (getAnnotationCount() > 0)
     */
    public boolean hasAnnotations() {
        return !annotations.isEmpty();
    }

    /**
     * Checks if this page is bookmarked.
     *
     * @return true if the page is bookmarked, false otherwise
     * @ensures \result == this.bookmarked
     */
    public boolean isBookmarked() {
        return bookmarked;
    }

    /**
     * Removes an annotation from this page by its ID.
     * If the ID is invalid (negative or beyond the bounds of the annotation list),
     * no action is taken.
     *
     * @param annotationId the ID of the annotation to remove
     * @ensures (annotationId >= 0 && annotationId < \old(getAnnotationCount())) ==> getAnnotationCount() == \old(getAnnotationCount()) - 1
     * @ensures (annotationId < 0 || annotationId >= \old(getAnnotationCount())) ==> getAnnotationCount() == \old(getAnnotationCount())
     */
    public void removeAnnotation(int annotationId) {
        if (annotationId < 0 || annotationId >= annotations.size()) {
            return;
        }
        annotations.remove(annotationId);
    }

    /**
     * Toggles the bookmark status of this page.
     * If the page is currently bookmarked, it will be unbookmarked, and vice versa.
     *
     * @ensures this.bookmarked == !\old(this.bookmarked)
     */
    public void toggleBookmark() {
        bookmarked = !bookmarked;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Annotation annotation : annotations) {
            sb.append("Annotation [text=").append(annotation.getAnnotationText()).append("], ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");

        return "Page{" +
                "bookmark=" + bookmarked +
                ", annotations=" + sb.toString() +
                ", pageNum=" + getPageNum() +
                '}';
    }
}
