package edu.utn.inspt.cinearchive.backend;

import edu.utn.inspt.cinearchive.backend.config.AppConfig;
import edu.utn.inspt.cinearchive.backend.config.DatabaseConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
public class DataSourceSmokeTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testConnectionAndSelectOne() throws Exception {
        try (Connection c = dataSource.getConnection()) {
            Assert.assertNotNull(c);
            Assert.assertTrue(c.isValid(2));
            System.out.println("[TEST-DB] URL=" + c.getMetaData().getURL());
        }
        Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        Assert.assertEquals(Integer.valueOf(1), one);
    }
}
