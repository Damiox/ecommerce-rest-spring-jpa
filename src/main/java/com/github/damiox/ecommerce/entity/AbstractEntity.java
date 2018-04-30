package com.github.damiox.ecommerce.entity;

import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Defines an abstract entity for the system.
 * All entities in the system must have an 'id' field.
 * This field is being used to check equality.
 *
 * @author dnardelli
 */
@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, AbstractEntity.class.cast(obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
