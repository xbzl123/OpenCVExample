import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
//        MyExtension myExtension = project.getExtensions().create("pluginSrc",MyExtension.class)
//        project.task('123456').doLast(
//                println(myExtension.message)
//            )
        AppExtension appExtension = project.extensions.getByType(AppExtension.class)
        project.afterEvaluate(
                new Action<Project>() {
            @Override
            void execute(Project p) {
                appExtension.getApplicationVariants().all(new Action<ApplicationVariant>() {
                    @Override
                    void execute(ApplicationVariant applicationVariant) {
                        File apk = applicationVariant.outputs.all(new Action<BaseVariantOutput>() {
                            @Override
                            void execute(BaseVariantOutput baseVariantOutput) {
                                File apk = baseVariantOutput.getOutputFile()
                                String name = applicationVariant.name
                                project.tasks.create("jiagu"+name,JiaguTask.class,apk)
                            }
                        })
                    }
                })
            }
        }
        )
    }
}