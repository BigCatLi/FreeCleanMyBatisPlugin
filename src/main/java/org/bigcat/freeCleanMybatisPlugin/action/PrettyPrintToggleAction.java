package org.bigcat.freeCleanMybatisPlugin.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.bigcat.freeCleanMybatisPlugin.Icons;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * <p>美化sql</p>
 *
 * @author: bigcatlee
 * @date: 2022/5/04 18:11
 * @description:
 */
public class PrettyPrintToggleAction extends ToggleAction {

    public PrettyPrintToggleAction() {
        super("Pretty Print", "Pretty print", Icons.PRETTY_PRINT);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        if (Objects.isNull(e.getProject())) {
            return false;
        }
        return PropertiesComponent.getInstance(e.getProject()).getBoolean(PrettyPrintToggleAction.class.getName());
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        if (Objects.isNull(e.getProject())) {
            return;
        }

        PropertiesComponent.getInstance(e.getProject()).setValue(PrettyPrintToggleAction.class.getName(), state);

    }

}