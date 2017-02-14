package com.xinra.nucleus.apt;

import com.squareup.javapoet.AnnotationSpec;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Base class for annotation Processors
 * 
 * @author Erik Hofer
 */
public abstract class NucleusProcessor extends AbstractProcessor {
  
  /**
   * Emits an error message (will be displayed in the IDE).
   * 
   * @param element the element to which the error belongs
   * @param message the message (will be formatted with {@link String#format(String, Object...)})
   * @param args arguments for the formatting
   */
  public void error(Element element, String message, Object... args) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
        String.format(message, args), element);
  }
  
  /**
   * Returns {@link SourceVersion#latestSupported()}.
   */
  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
  
  /**
   * Returns a spec of @{@link Generated} with {@code value} set to the fully
   * qualified name of the annotation processor and {@code date} set to the
   * current date and time.
   * 
   * @param comments the value of {@link Generated#comments()} (omitted if {@code null})
   */
  public AnnotationSpec getGeneratedAnnotation(String comments) {
    AnnotationSpec.Builder builder = AnnotationSpec.builder(Generated.class)
        .addMember("value", "$S", this.getClass().getName())
        .addMember("date", "$S", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    if(comments != null) {
      builder.addMember("comments", "$S", comments);
    }
    return builder.build();
  }
  
  /**
   * Returns a spec of @{@link Generated} with {@code value} set to the fully
   * qualified name of the annotation processor, {@code date} set to the
   * current date and time and without {@code comments}.
   */
  public AnnotationSpec getGeneratedAnnotation() {
    return getGeneratedAnnotation(null);
  }

}
