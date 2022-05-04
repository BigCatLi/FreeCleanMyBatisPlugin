package org.bigcat.freeCleanMybatisPlugin.action;

import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;


/**
 * <p>格式化SQL命令行左侧按钮组：清除SQL内容</p>
 *
 * @author: bigcatlee
 * @date: 2022/5/04 15:11
 * @description:
 */
public class ClearAction extends DumbAwareAction {
    private final ConsoleView consoleView;

    public ClearAction(ConsoleView consoleView) {
        super("Clear", ExecutionBundle.message("clear.all.from.console.action.text"), AllIcons.Actions.GC);
        this.consoleView = consoleView;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        boolean enabled = consoleView.getContentSize() > 0;
        if (!enabled) {
            enabled = e.getData(LangDataKeys.CONSOLE_VIEW) != null;
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            if (editor != null && editor.getDocument().getTextLength() == 0) {
                enabled = false;
            }
        }
        e.getPresentation().setEnabled(enabled);
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        if (consoleView != null) {
            consoleView.clear();
        }
    }
}
