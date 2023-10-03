package org.acme.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class CustomerEntity extends BaseEntity {
    public String firstname;
    public String lastName;
}
