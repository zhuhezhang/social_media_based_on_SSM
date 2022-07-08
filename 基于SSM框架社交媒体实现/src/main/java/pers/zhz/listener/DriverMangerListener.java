package pers.zhz.listener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * 解决项目名关闭时出现的问题，详情：https://blog.csdn.net/qq_43794633/article/details/123199393
 */
public class DriverMangerListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
    }

    public void contextDestroyed(ServletContextEvent sce) {
        AbandonedConnectionCleanupThread.uncheckedShutdown();
        Enumeration<Driver> enumeration = DriverManager.getDrivers();
        while (enumeration.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(enumeration.nextElement());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
