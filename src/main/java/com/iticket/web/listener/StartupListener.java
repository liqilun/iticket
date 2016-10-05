package com.iticket.web.listener;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iticket.Config;
import com.iticket.util.IpConfig;
import com.iticket.util.GewaLogger;
import com.iticket.util.LoggerUtils;
import com.iticket.web.support.ServletContextWrapper;

public class StartupListener extends ContextLoaderListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		GewaLogger logger = LoggerUtils.getLogger(StartupListener.class, Config.getServerIp(), Config.SYSTEMID);
		ServletContextWrapper context = new ServletContextWrapper(event.getServletContext());
		// 根据环境加载配置文件
		String SPRING_CONFIG_KEY = "contextConfigLocation";
		
		//本地环境
		String[] localConfig = new String[] {
				"classpath:spring/config.local.xml",
				"classpath:spring/appContext-*.xml",
				"classpath:spring/hibernate.local.xml",
				"classpath:spring/task.xml"
		};
		//预发环境
		String[] preConfig = new String[] {
				"classpath:spring/config.pre.xml",
				"classpath:spring/appContext-*.xml",
				"classpath:spring/hibernate.pre.xml",
				"classpath:spring/task.xml"
		};
		//生产环境
		String[] remoteConfig = new String[] {
				"classpath:spring/config.remote.xml",
				"classpath:spring/appContext-*.xml",
				"classpath:spring/hibernate.remote.xml",
				"classpath:spring/task.xml"
		};
		String SPRING_CONFIG_VALUE = "";
		String ip = Config.getServerIp();
		logger.warn("server ip:" + ip);
		
		if (IpConfig.isPreEnvIp(ip)) {
			System.setProperty("GEWACONFIG", "REMOTE");
			SPRING_CONFIG_VALUE += StringUtils.join(preConfig, ",");
		}else if (IpConfig.isInnerIp(ip)) {
			System.setProperty("GEWACONFIG", "REMOTE");
			SPRING_CONFIG_VALUE += StringUtils.join(remoteConfig, ",");
		} else {
			System.setProperty("GEWACONFIG", "LOCAL");
			SPRING_CONFIG_VALUE += StringUtils.join(localConfig, ",");
		}
		logger.warn("Config Using REMOTE:" + SPRING_CONFIG_VALUE);
		context.setInitParams(SPRING_CONFIG_KEY, SPRING_CONFIG_VALUE);
		ServletContextEvent wrapperEvent = new ServletContextEvent(context);
		super.contextInitialized(wrapperEvent);
		
		WebApplicationContextUtils.getRequiredWebApplicationContext(context);
	}
}