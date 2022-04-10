package org.bigcat.freeCleanMybatisPlugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.bigcat.freeCleanMybatisPlugin.executor.CustomExecutor;

/**
 * <p></p>
 *
 * @author: bigcatlee
 * @date: 2022/4/10 15:11
 * @description:
 */
public class MyBatisConsoleViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        CustomExecutor executor = new CustomExecutor(e.getProject());
        executor.run();
    }
}
