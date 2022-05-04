package org.bigcat.freeCleanMybatisPlugin.executor;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.*;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.content.Content;
import org.apache.commons.lang3.StringUtils;
import org.bigcat.freeCleanMybatisPlugin.utils.BasicFormatter;
import org.bigcat.freeCleanMybatisPlugin.action.*;
import org.bigcat.freeCleanMybatisPlugin.utils.MyIconUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p>
 *
 * @author: bigcatlee
 * @date: 2022/4/10 15:41
 * @description:
 */
public class CustomExecutor implements Disposable {

    private final ConsoleView consoleView;
    private final Project project;
    //是否运行监听
    private volatile boolean running = false;

    private final BasicFormatter FORMATTER;
    private final AtomicInteger counter;
    //单例
    private static volatile CustomExecutor customExecutor;

    private CustomExecutor(@NotNull Project project){
        this.project = project;
        this.consoleView = createConsoleView(project);
        this.FORMATTER = new BasicFormatter();
        this.counter = new AtomicInteger();
    }

    public static CustomExecutor getInstance(Project project){
        if (customExecutor == null){
            synchronized (CustomExecutor.class){
                if (customExecutor == null){
                    customExecutor = new CustomExecutor(project);
                }
            }
        }
        return customExecutor;
    }



    private ConsoleViewImpl createConsoleView(Project project) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        ConsoleViewImpl console = (ConsoleViewImpl) consoleBuilder.getConsole();
        console.getComponent();
        Editor editor = console.getEditor();
        editor.getDocument().addDocumentListener(new RangeHighlighterDocumentListener(editor));
        console.print("欢迎使用FreeCleanMyBatis插件", ConsoleViewContentType.SYSTEM_OUTPUT);
        return console;
    }

    public void println(String logPrefix, String sql) {
        consoleView.print(String.format("-- %s -- %s\n", counter.incrementAndGet(), logPrefix), ConsoleViewContentType.USER_INPUT);
        consoleView.print(String.format("%s\n", isFormat() ? FORMATTER.format(sql) : StringUtils.removeEnd(sql, "\n")), ConsoleViewContentType.SYSTEM_OUTPUT);
    }

    private boolean isFormat() {
        return PropertiesComponent.getInstance(project).getBoolean(PrettyPrintToggleAction.class.getName());
    }

    public static JPanel createConsolePanel(ConsoleView view) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(view.getComponent(), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void dispose() {
        Disposer.dispose(this);
    }

    public void run() {
        if (project.isDisposed()) {
            return;
        }

        Executor executor = CustomRunExecutor.getRunExecutorInstance();
        if (executor == null) {
            return;
        }

        final RunnerLayoutUi.Factory factory = RunnerLayoutUi.Factory.getInstance(project);
        RunnerLayoutUi layoutUi = factory.create("runnerId", "runnerTitle", "sessionName", project);
        final JPanel consolePanel = createConsolePanel(consoleView);

        RunContentDescriptor descriptor = new RunContentDescriptor(new RunProfile() {
            @Nullable
            @Override
            public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
                return null;
            }

            @NotNull
            @Override
            public String getName() {
                //第一层名称显示
                return "Format SQL";
            }

            @Override
            public @NotNull Icon getIcon() {
                return MyIconUtil.SMALLICON;
            }
        }, new DefaultExecutionResult(), layoutUi);
        descriptor.setExecutionId(System.nanoTime());

        final Content content = layoutUi.createContent("contentId", consolePanel, "Format SQL", AllIcons.Debugger.Console, consolePanel);
        content.setCloseable(false);
        layoutUi.addContent(content);
        layoutUi.getOptions().setLeftToolbar(InitLeftToolBarGroup(),"RunnerToolbar");

        Disposer.register(descriptor,this);
        Disposer.register(content, consoleView);

        RunContentManager.getInstance(project).showRunContent(executor, descriptor);
        running = true;
    }

    public void reStart(){
        run();
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;

    }

    public boolean isRunning() {
        return running;
    }


    private ActionGroup InitLeftToolBarGroup() {
        final ConsoleView consoleView = this.consoleView;
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new StartUpAction());
        actionGroup.add(new StopAction());
        actionGroup.add(new PrettyPrintToggleAction());
        actionGroup.add(new ClearAction(consoleView));
        return actionGroup;
    }



    //文件监听
    private static final class RangeHighlighterDocumentListener implements DocumentListener {

        private final Editor editor;

        private RangeHighlighterDocumentListener(Editor editor) {
            this.editor = editor;
        }

        @Override
        public void documentChanged(@NotNull DocumentEvent event) {
            final Document document = event.getDocument();
            final int textLength = document.getTextLength();
            if (textLength < 1) {
                return;
            }

            for (int i = event.getOffset(); i < textLength; ) {
                final int endOffset = document.getLineEndOffset(document.getLineNumber(i));
                final String text = document.getText(TextRange.create(i, endOffset));
                System.out.println(text);
                if (text.matches("^-- [\\d]+ -- .*")) {
                    editor.getMarkupModel().addRangeHighlighter(i, i + 1, 506, TextAttributes.ERASE_MARKER, HighlighterTargetArea.EXACT_RANGE);
                }
                i = endOffset + 1;
            }
        }
    }

}
