import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

import javax.inject.Inject

class JiaguTask extends DefaultTask{

    File apk;
    @Inject
     JiaguTask(File apk){
       setGroup("jiagu")
        this.apk = apk
    }

    @TaskAction
    void jiagu(){
        getProject().exec(new Action<ExecSpec>() {
            @Override
            void execute(ExecSpec execSpec) {
                System.out.println("======>"+apk.name)
                execSpec.commandLine("java","-version")

            }
        })
    }
}