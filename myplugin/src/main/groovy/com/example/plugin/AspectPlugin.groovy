//import org.gradle.api.Plugin
//import org.gradle.api.Project
//import org.gradle.api.tasks.compile.JavaCompile
//
//class AspectPlugin implements Plugin<Project> {
//
//    @Override
//    void apply(Project project) {
//        final def log = project.logger
//        final def variants = project.android.applicationVariants
//
//        variants.all { variant ->
//            if (!variant.buildType.isDebuggable()) {
//                log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
//                return;
//            }
//
//            JavaCompile javaCompile = variant.javaCompile
//            javaCompile.doLast {
//                String[] args = ["-showWeaveInfo",
//                                 "-1.8",
//                                 "-inpath", javaCompile.destinationDir.toString(),
//                                 "-aspectpath", javaCompile.classpath.asPath,
//                                 "-d", javaCompile.destinationDir.toString(),
//                                 "-classpath", javaCompile.classpath.asPath,
//                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
//                log.debug "ajc args: " + Arrays.toString(args)
//
//                MessageHandler handler = new MessageHandler(true);
//                new Main().run(args, handler);
//                for (IMessage message : handler.getMessages(null, true)) {
//                    switch (message.getKind()) {
//                        case IMessage.ABORT:
//                        case IMessage.ERROR:
//                        case IMessage.FAIL:
//                            log.error message.message, message.thrown
//                            break;
//                        case IMessage.WARNING:
//                            log.warn message.message, message.thrown
//                            break;
//                        case IMessage.INFO:
//                            log.info message.message, message.thrown
//                            break;
//                        case IMessage.DEBUG:
//                            log.debug message.message, message.thrown
//                            break;
//                    }
//                }
//            }
//        }
//
//    }
//}