import com.android.build.gradle.AppExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

public class TestPlugin implements Plugin<Project> {
    @Override
    void apply(Project o) {
        MyExtension myExtension = o.extensions.create("pluginSrc",MyExtension)
//        AppExtension android = o.extensions.getByType(AppExtension)
//        o.task('testPlugin').doLast {
//        System.out.println("Hello aoaoyi gradle plugin in pluginSrc")
//    }
        o.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project project) {
                println("==================>"+myExtension.message)

            }
        })
//        def transform = new TransformTest()
//        android.registerTransform(new CustomTransform(project))

    }
}