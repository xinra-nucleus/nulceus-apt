package com.xinra.nucleus.apt.testing;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * Used for debugging annotation processors.
 */
//based on http://stackoverflow.com/a/27560295/5519485
public class ProcessorTesting {

  private ProcessorTesting() {}
  
  /**
   * Compiles sources in the given directory and applies annotation processors.
   * Include in a unit test:
   * <pre>
   * {@literal @Test}
   * public void runAnnoationProcessor() throws Exception {
   *   ProcessorTesting.compile("src/main/java", MyProcessor.class);
   * }
   * </pre>
   * This way the annotation processor(s) can be debugged when running the
   * test in debug mode.
   * 
   * @param sourcePath path of the source files to be compiled
   * @param processors annotation processors that should be run on the code
   */
  public static void compile(String sourcePath, Processor... processors)
      throws Exception {

    Iterable<JavaFileObject> files = getSourceFiles(sourcePath);

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    CompilationTask task = 
        compiler.getTask(new PrintWriter(System.out), null, null, null, null, files);
    task.setProcessors(Arrays.asList(processors));

    task.call();
  }

  private static Iterable<JavaFileObject> getSourceFiles(String path) throws Exception {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);

    files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(path)));

    Set<Kind> fileKinds = Collections.singleton(Kind.SOURCE);
    return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
  }
  
}
