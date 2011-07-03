package name.samolisov.jta.xa.demo.crm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_ACCOUNT")
public class Account {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCSEQ")
    @SequenceGenerator(name = "ACCSEQ", sequenceName = "account_id_seq")
	private Long id;

	@Column
	private String name;

	@Column(name = "product_name")
	private String productName;

	@Column
	private Integer count;

	@Column
	private Double price;

	@Column
	private Double total;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
