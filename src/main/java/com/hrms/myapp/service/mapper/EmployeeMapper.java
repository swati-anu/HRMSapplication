package com.hrms.myapp.service.mapper;

import com.hrms.myapp.domain.Branch;
import com.hrms.myapp.domain.Department;
import com.hrms.myapp.domain.Designation;
import com.hrms.myapp.domain.Employee;
import com.hrms.myapp.domain.Region;
import com.hrms.myapp.service.dto.BranchDTO;
import com.hrms.myapp.service.dto.DepartmentDTO;
import com.hrms.myapp.service.dto.DesignationDTO;
import com.hrms.myapp.service.dto.EmployeeDTO;
import com.hrms.myapp.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "designation", source = "designation", qualifiedByName = "designationName")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentName")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "branchBranchName")
    @Mapping(target = "region", source = "region", qualifiedByName = "regionRegionName")
    EmployeeDTO toDto(Employee s);

    @Named("designationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DesignationDTO toDtoDesignationName(Designation designation);

    @Named("departmentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoDepartmentName(Department department);

    @Named("branchBranchName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "branchName", source = "branchName")
    BranchDTO toDtoBranchBranchName(Branch branch);

    @Named("regionRegionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "regionName", source = "regionName")
    RegionDTO toDtoRegionRegionName(Region region);
}
