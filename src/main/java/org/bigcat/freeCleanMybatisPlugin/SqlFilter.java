package org.bigcat.freeCleanMybatisPlugin;

import java.util.*;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.ide.util.PropertiesComponent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bigcat.freeCleanMybatisPlugin.constant.ProjectConstant;
import org.bigcat.freeCleanMybatisPlugin.executor.CustomExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;

/**
 * <P>控制台过滤器</P>
 *
 * @author: bigcatlee
 * @date: 2022/4/10 15:41
 * @description:
 */
public class SqlFilter implements Filter {

    private static final char MARK = '?';

    private static final Set<String> NEED_BRACKETS;

    private final Project project;

    private String sql = null;

    static {
        Set<String> types = new HashSet<>(8);
        types.add("String");
        types.add("Date");
        types.add("Time");
        types.add("LocalDate");
        types.add("LocalTime");
        types.add("LocalDateTime");
        types.add("BigDecimal");
        types.add("Timestamp");
        NEED_BRACKETS = Collections.unmodifiableSet(types);
    }

    SqlFilter(Project project) {
        this.project = project;
    }

    @Override
    public @Nullable Result applyFilter(@NotNull String line, int entireLength) {

        final CustomExecutor customExecutor = CustomExecutor.getInstance(project);
        if (Objects.isNull(customExecutor)) {
            return null;
        }

        if (!customExecutor.isRunning()) {
            return null;
        }

        if (line.contains(ProjectConstant.PREPARING)) {
            sql = line;
            return null;
        }

        if (StringUtils.isNotBlank(sql) && !line.contains(ProjectConstant.PARAMETERS)) {
            return null;
        }

        if (StringUtils.isBlank(sql)) {
            return null;
        }

        final String logPrefix = StringUtils.substringBefore(sql, ProjectConstant.PREPARING);
        final String wholeSql = parseSql(StringUtils.substringAfter(sql, ProjectConstant.PREPARING), parseParams(StringUtils.substringAfter(line, ProjectConstant.PARAMETERS))).toString();

        customExecutor.println(logPrefix, wholeSql);
        return null;
    }

    static StringBuilder parseSql(String sql, Queue<Map.Entry<String, String>> params) {

        final StringBuilder sb = new StringBuilder(sql);

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) != MARK) {
                continue;
            }

            final Map.Entry<String, String> entry = params.poll();
            if (Objects.isNull(entry)) {
                continue;
            }


            sb.deleteCharAt(i);

            if (NEED_BRACKETS.contains(entry.getValue())) {
                sb.insert(i, String.format("'%s'", entry.getKey()));
            } else {
                sb.insert(i, entry.getKey());
            }


        }

        return sb;
    }

    static Queue<Map.Entry<String, String>> parseParams(String line) {
        line = StringUtils.removeEnd(line, "\n");

        final String[] strings = StringUtils.splitByWholeSeparator(line, ", ");
        final Queue<Map.Entry<String, String>> queue = new ArrayDeque<>(strings.length);

        for (String s : strings) {
            String value = StringUtils.substringBeforeLast(s, "(");
            String type = StringUtils.substringBetween(s, "(", ")");
            if (StringUtils.isEmpty(type)) {
                queue.offer(new AbstractMap.SimpleEntry<>(value, null));
            } else {
                queue.offer(new AbstractMap.SimpleEntry<>(value, type));
            }
        }

        return queue;
    }

}
