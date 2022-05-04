package org.bigcat.freeCleanMybatisPlugin.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.bigcat.freeCleanMybatisPlugin.executor.CustomExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * <p></p>
 *
 * @author: bigcatlee
 * @date: 2022/5/4 16:46
 * @description:
 */
public class StartUpAction extends AnAction {

    public StartUpAction() {
        super("StartUp", "StartUp", AllIcons.Actions.Execute);
    }
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        CustomExecutor.getInstance(e.getProject()).reStart();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!CustomExecutor.getInstance(e.getProject()).isRunning());
    }
}
