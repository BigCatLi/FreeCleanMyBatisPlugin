package org.bigcat.freeCleanMybatisPlugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.bigcat.freeCleanMybatisPlugin.executor.CustomExecutor;

/**
 * <p>插件的命令行，位于idea下方，输出的是mybatis的sql和param拼接后的结果</p>
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
