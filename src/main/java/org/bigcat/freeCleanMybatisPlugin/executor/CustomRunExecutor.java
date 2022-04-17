package org.bigcat.freeCleanMybatisPlugin.executor;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import org.bigcat.freeCleanMybatisPlugin.constant.ProjectConstant;
import org.bigcat.freeCleanMybatisPlugin.utils.MyIconUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <p></p>
 *
 * @author: bigcatlee
 * @date: 2022/4/10 15:13
 * @description:
 */
public class CustomRunExecutor extends Executor {

    public static final String TOOL_WINDOW_ID = "tool window plugin";

    @Override
    public @NotNull String getToolWindowId() {
        return TOOL_WINDOW_ID;
    }

    @Override
    public @NotNull Icon getToolWindowIcon() {
        return MyIconUtil.SMALLICON;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return MyIconUtil.SMALLICON;
    }

    @Override
    public Icon getDisabledIcon() {
        return MyIconUtil.SMALLICON;
    }

    @Override
    public String getDescription() {
        return TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getActionName() {
        return TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getId() {
        return ProjectConstant.PLUGIN_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return TOOL_WINDOW_ID;
    }

    @Override
    public String getContextActionId() {
        return "custom context action id";
    }

    @Override
    public String getHelpId() {
        return TOOL_WINDOW_ID;
    }

    public static Executor getRunExecutorInstance() {
        return ExecutorRegistry.getInstance().getExecutorById(ProjectConstant.PLUGIN_ID);
    }
}
