package io.github.lr.querythreads.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entidad de Bebe. 
 * 
 * @author lravanal
 *
 */
@Entity
@Table(name="BABY")
public class Baby extends AbstractEntity {

	@Column(name="NAME")
	private String name;
	
	@Column(name="BIRTHDAY")
	private Date birthday;

	@Override
	public String toString() {
		return "Baby [id=" + id + ", name=" + name + ", birthday=" + birthday + "]";
	}
	
}
