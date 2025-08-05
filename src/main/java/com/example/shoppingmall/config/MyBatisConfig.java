//package com.example.shoppingmall.config;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//@PropertySource({"classpath:db.properties", "classpath:mail.properties"})
//@MapperScan(basePackages = {
//        "com.example.shoppingmall.user.dao",
//        "com.example.shoppingmall.review.dao",
//        "com.example.shoppingmall.order.dao",
//        "com.example.shoppingmall.notice.dao",
//        "com.example.shoppingmall.itemquestion.dao",
//        "com.example.shoppingmall.item.dao",
//        "com.example.shoppingmall.cart.dao",
//        "com.example.shoppingmall.admin.dao"
//})
//public class MyBatisConfig {
//
//    @Value("${db.driver}")
//    private String driverClassName;
//
//    @Value("${db.url}")
//    private String url;
//
//    @Value("${db.username}")
//    private String username;
//
//    @Value("${db.password}")
//    private String password;
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName(driverClassName);
//        ds.setUrl(url);
//        ds.setUsername(username);
//        ds.setPassword(password);
//        return ds;
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
//        factory.setDataSource(dataSource);
//        factory.setMapperLocations(
//                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml")
//        );
//        factory.setTypeAliasesPackage("com.example.shoppingmall");
//
//        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
//        config.setMapUnderscoreToCamelCase(true);
//        factory.setConfiguration(config);
//
//        return factory.getObject();
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory factory) {
//        return new SqlSessionTemplate(factory);
//    }
//
//    @Bean
//    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}