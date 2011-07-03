package name.samolisov.jta.xa.demo.eshop.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import name.samolisov.jta.xa.demo.eshop.IProductDao;
import name.samolisov.jta.xa.demo.eshop.Product;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcProductDao extends SimpleJdbcDaoSupport implements IProductDao {

	private static final String PRODUCT_TABLE = "TBL_PRODUCT";

	private static final String GET_ALL_QUERY = "select * from " + PRODUCT_TABLE + " order by price";

	private static final String GET_PRICE_QUERY = "select price from " + PRODUCT_TABLE + " where id = ?";

	private static final String GET_COUNT_QUERY = "select count from  " + PRODUCT_TABLE + " where id = ?";

	private static final String GET_NAME_QUERY = "select name from " + PRODUCT_TABLE + " where id = ?";

	private static final String UPDATE_COUNT_QUERY = "update " + PRODUCT_TABLE + " set count = ? where id = ?";

	private static ProductRowMapper MAPPER = new ProductRowMapper();

	@Override
	public List<Product> getAllProducts() {
		return getSimpleJdbcTemplate().query(GET_ALL_QUERY, MAPPER);
	}

	@Override
	public double getPrice(Long productId) {
		return getSimpleJdbcTemplate().queryForObject(GET_PRICE_QUERY, Double.class, productId);
	}

	@Override
	public int getCount(Long productId) {
		return getSimpleJdbcTemplate().queryForInt(GET_COUNT_QUERY, productId);
	}

	@Override
	public String getProductName(Long productId) {
		return getSimpleJdbcTemplate().queryForObject(GET_NAME_QUERY, String.class, productId);
	}

	@Override
	public void updateCount(Long productId, Integer newCount) {
		getSimpleJdbcTemplate().update(UPDATE_COUNT_QUERY, newCount, productId);
	}

    public static class ProductRowMapper implements ParameterizedRowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        	Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setCount(rs.getInt("count"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            return product;
        }
    }
}
