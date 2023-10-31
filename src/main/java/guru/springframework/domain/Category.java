package guru.springframework.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long        id;

	@ManyToMany(mappedBy = "categories")
	private Set<Recipe> recipe;
}
