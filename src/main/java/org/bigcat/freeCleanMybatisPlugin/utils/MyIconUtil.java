package org.bigcat.freeCleanMybatisPlugin.utils;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class MyIconUtil {
    public static Icon getIcon(String path) {
        return IconLoader.getIcon(path);
    }
    public static final Icon ICON = getIcon("/icons/pluginIcon.svg");
    public static final Icon SMALLICON = getIcon("/icons/idea/other/smallIcon.png");

    public static final Icon LIGHTNING_DARK = getIcon("/icons/idea/bullet.png");

    // 参考: AllIcons.General.Tip
    public static final Icon TEXT = getIcon("/icons/idea/balloonInformation.png");

    public static final Icon BLOCKED = getIcon("/icons/degreed/blocked.svg");
    // 参考: AllIcons.General.Tip
    public static final Icon CRITICAL = getIcon("/icons/degreed/critical.svg");
    public static final Icon MARJOR = getIcon("/icons/degreed/major.svg");

    /**
     * 数据库相关图标
     */
    public static final class DataBase {
        // AllIcons.Providers.Mysql;
        public static final Icon MYSQL = getIcon("/icons/sqltype/mysql.svg");

        // AllIcons.Providers.Oracle;
        public static final Icon ORACLE = getIcon("/icons/sqltype/oracle.svg");

        // AllIcons.Providers.Postgresql;
        public static final Icon POSTGRESQL = getIcon("/icons/sqltype/postgresql.svg");

        // AllIcons.Providers.DB2;
        public static final Icon DB2 = getIcon("/icons/sqltype/DB2.svg");

        public static final Icon GAUSS = getIcon("/icons/sqltype/gauss.svg");
    }


}
