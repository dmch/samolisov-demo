package name.samolisov.spring.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQuery(name = "getPersonByName",
			query = "from Person p where p.name = :name")
public class Person {

    @Id
    @Column(name = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Auto> autos;

    public Person() {}

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Set<Auto> getAutos() {
		return autos;
	}

	@SuppressWarnings("unused")
	private void setAutos(Set<Auto> autos) {
		this.autos = autos;
	}

	public void addAuto(Auto auto) {
		if (autos == null)
			autos = new HashSet<Auto>();
		auto.setPerson(this);
		autos.add(auto);
	}

	public void removeAuto(Auto auto) {
		if (autos != null) {
			autos.remove(auto);
			auto.setPerson(null);
		}
	}
}
