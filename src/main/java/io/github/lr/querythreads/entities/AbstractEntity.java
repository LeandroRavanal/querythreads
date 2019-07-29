package io.github.lr.querythreads.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Entidad abstracta que define atributos comunes como ID. 
 * Tambien define equals y hashcode.
 * 
 * @author lravanal
 *
 */
@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@Column(name="ID")
	protected Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity e = (AbstractEntity) o;
        return id != null && id.equals(e.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    
}
