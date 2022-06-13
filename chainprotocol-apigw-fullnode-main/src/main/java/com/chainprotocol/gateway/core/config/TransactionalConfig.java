//package com.chainprotocol.gateway.core.config;
//
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.transaction.ChainedTransactionManager;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//public class TransactionalConfig {
//
//    @Autowired
//    @Qualifier("gwDataSource")
//    DataSource gwDataSource;
//
//    @Primary
//    @Bean
//    public PlatformTransactionManager platformTransactionManager() {
//        PlatformTransactionManager[] transactionManagers = new PlatformTransactionManager[]{
//                new DataSourceTransactionManager(gwDataSource),
//        };
//        return new ChainedTransactionManager(transactionManagers);
//    }
//
//}
