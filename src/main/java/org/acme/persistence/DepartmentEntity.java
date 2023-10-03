package org.acme.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "departments")
public class DepartmentEntity extends BaseEntity {
    public String name;
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "department",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    public List<EmployeeEntity> employees;
}
