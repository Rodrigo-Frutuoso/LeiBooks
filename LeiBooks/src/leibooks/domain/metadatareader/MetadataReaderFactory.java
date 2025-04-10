package leibooks.domain.metadatareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import leibooks.app.AppProperties;

/**
 * Factory for creating metadata readers based on document file MIME types.
 * Implemented as a singleton using Java's enum pattern.
 * 
 * This factory registers default readers and loads additional readers dynamically
 * from a configurable directory. Reader classes are mapped to MIME types using a
 * naming convention where a class named 'AbcXyzMetadataReader' corresponds to
 * the MIME type 'abc/xyz'.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public enum MetadataReaderFactory {
    INSTANCE;
    
    private final Map<String, Class<? extends IMetadataReader>> readersMap;
    
    /**
     * Initializes the factory by registering default readers and loading
     * additional readers dynamically.
     */
    MetadataReaderFactory() {
        readersMap = new HashMap<>();
        
        readersMap.put("application/pdf", PDFMetadataReaderAdapter.class);
        
        loadReaders();
    }
    
    /**
     * Creates a metadata reader for the specified document file.
     * 
     * @param pathToDocFile path to the document file
     * @return a metadata reader appropriate for the file's MIME type
     * @throws FileNotFoundException if the file doesn't exist
     * @requires pathToDocFile != null
     * @ensures \result != null
     */
    public IMetadataReader createMetadataReader(String pathToDocFile) throws FileNotFoundException {
        try { 
            String mimeType = Files.probeContentType(Path.of(pathToDocFile));

            if (mimeType == null) {
                return new GenericMetadataReader(pathToDocFile);
            }
            
            Class<? extends IMetadataReader> readerClass = readersMap.get(mimeType);
            if (readerClass != null) {
                return readerClass.getConstructor(String.class).newInstance(pathToDocFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GenericMetadataReader(pathToDocFile);
    }

    /**
     * Loads reader classes from the specified directory using URLClassLoader.
     * 
     * @param dir directory containing the reader classes
     */
    private void loadReaders() {
    //Inspired by SwingViewerFactory
    File folder = new File(AppProperties.INSTANCE.FOLDER_EXTRA_VIEWERS_AND_READERS); 
        File [] classes = folder.listFiles( (dir, name) -> {
                return name.endsWith(".class");
        });

        // try to load the classes that implement IViewer
        for (File classFile : classes) {
            try {
                String className = classFile.getName();
                String s = className.substring(0, className.lastIndexOf('.'));
                @SuppressWarnings("unchecked")
                Class<? extends IMetadataReader> readerClass = (Class<? extends IMetadataReader>) Class.forName(s);
                
                String mimeType = extractMimeTypeFromClassName(s);
                if (mimeType != null) {
                    readersMap.put(mimeType, readerClass);
                }
            } 
            catch (Exception e) {
                        System.err.println("Error loading class " + classFile.getName() + ": " + e.getMessage());
            }	
        } 
    }
    
    /**
     * Extracts a MIME type from a class name using the convention that
     * a class named 'AbcXyzMetadataReader' corresponds to MIME type 'abc/xyz'.
     * 
     * @param className the class name to extract the MIME type from
     * @return the extracted MIME type, or null if extraction fails
     * @requires className != null
     * @ensures \result == null || \result.contains("/")
     */
    private String extractMimeTypeFromClassName(String className) {
        if (className.endsWith("MetadataReader")||className.length()<2) {
            String prefix = className.substring(0, className.length() - "MetadataReader".length());
            for(int i= 1; i<prefix.length(); i++) {
                if(Character.isUpperCase(prefix.charAt(i))) {
                    return prefix.substring(0, i) + "/" + prefix.substring(i).toLowerCase();
                }
            }
        }
        return null;
    }
}