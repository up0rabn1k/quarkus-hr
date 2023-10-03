package org.acme.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class EmployeeEntity extends BaseEntity {
    public String name;
    public String location;
    @ManyToOne(optional = false)
    public DepartmentEntity department;
}

