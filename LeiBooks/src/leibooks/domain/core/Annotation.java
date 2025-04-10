package leibooks.domain.core;

/**
 * Represents a text annotation on a document page in the LEIBooks system.
 * 
 * This class encapsulates a text annotation that users can add to pages of documents.
 * Annotations allow users to add notes, comments, or other textual information to
 * specific pages in a document, enhancing the reading experience by providing
 * a way to record thoughts and insights.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class Annotation {

    private String annotationText;

    /**
     * Constructs a new Annotation with the specified text.
     *
     * @param annotationText the text of the annotation
     * @requires annotationText != null
     * @ensures this.getAnnotationText().equals(annotationText)
     */
    public Annotation(String annotationText) {
        this.annotationText = annotationText;
    }

    /**
     * Gets the text content of this annotation.
     *
     * @return the annotation text
     * @ensures \result != null
     */
    public String getAnnotationText() {
        return annotationText;
    }

    /**
     * Sets new text content for this annotation.
     *
     * @param annotationText the new text for the annotation
     * @requires annotationText != null
     * @ensures this.getAnnotationText().equals(annotationText)
     */
    public void setAnnotationText(String annotationText) {
        this.annotationText = annotationText;
    }
    
    @Override
    public String toString() {
        return "Annotation [text=" + annotationText + "]";
    }
}