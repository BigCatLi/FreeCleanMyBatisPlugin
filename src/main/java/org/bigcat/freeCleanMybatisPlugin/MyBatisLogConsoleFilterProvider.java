package org.bigcat.freeCleanMybatisPlugin;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;

/**
 * <p>实现ConsoleFilterProvider，配置过滤器</p>
 *
 * @author: bigcatlee
 * @date: 2022/4/10 15:41
 * @description:
 *
 */
public class MyBatisLogConsoleFilterProvider implements ConsoleFilterProvider {
    private final Key<SqlFilter> key = Key.create(SqlFilter.class.getName());

    public MyBatisLogConsoleFilterProvider() {

    }

    @Override
    public Filter @NotNull [] getDefaultFilters(@NotNull Project project) {
        SqlFilter filter = project.getUserData(key);
        if (Objects.isNull(filter)) {
            filter = new SqlFilter(project);
            project.putUserData(key, filter);
        }
        return new Filter[] { filter };
    }
}
