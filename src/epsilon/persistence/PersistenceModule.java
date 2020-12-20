package epsilon.persistence;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariDataSource;

import common.Core;
import omega.annotation.Transactional;
import omega.persistence.PersistenceService;
import omega.persistence.TransactionInterceptor;

public class PersistenceModule extends AbstractModule {

	public PersistenceModule() {
	}

	@Override
	protected final void configure() {

		HikariDataSource mainDataSource = new HikariDataSource();

		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("application.configuration");
			if (url != null) {
				Properties propertyRepository = new Properties();
				propertyRepository.load(url.openStream());

				mainDataSource.setDriverClassName(propertyRepository.getProperty("driver.class.name"));
				mainDataSource.setJdbcUrl(propertyRepository.getProperty("main.database.url"));
				mainDataSource.setUsername(propertyRepository.getProperty("main.database.username"));
				mainDataSource.setPassword(propertyRepository.getProperty("main.database.password"));

				String poolSize = propertyRepository.getProperty("main.database.pool.size");
				if (poolSize != null) {
					mainDataSource.setMaximumPoolSize(Integer.parseInt(poolSize));
				} else {
					mainDataSource.setMaximumPoolSize(10);
				}

				Core.setPropertyRepository(propertyRepository);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		bind(new TypeLiteral<Map<Class, Class>>() {
		}).annotatedWith(Names.named("DefaultColumnClassMap")).toInstance(classTypeMapping);

		bind(PersistenceService.class).toInstance(new PersistenceService(mainDataSource));
		MethodInterceptor transactionInterceptor = new TransactionInterceptor();
		requestInjection(transactionInterceptor);

		bindInterceptor(any(), annotatedWith(Transactional.class), transactionInterceptor);

	}

	public static final Map<Class, Class> classTypeMapping = new HashMap<>() {
		{
			put(java.sql.Timestamp.class, java.time.LocalDateTime.class);
		}
	};
}
