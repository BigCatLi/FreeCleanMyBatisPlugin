package org.bigcat.freeCleanMybatisPlugin.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.bigcat.freeCleanMybatisPlugin.executor.CustomExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * StopAction
 * @author huangxingguang
 */
public class StopAction extends AnAction {

    public StopAction() {
        super("Stop", "Stop", AllIcons.Actions.Suspend);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        CustomExecutor.getInstance(e.getProject()).stop();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(CustomExecutor.getInstance(e.getProject()).isRunning());
    }

}