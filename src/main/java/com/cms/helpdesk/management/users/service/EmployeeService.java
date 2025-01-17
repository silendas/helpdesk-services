package com.cms.helpdesk.management.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.exception.UserFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;
import com.cms.helpdesk.common.reuse.PatchField;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.branch.repository.BranchRepository;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.departments.repository.DepartmentRepository;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.regions.repository.RegionRepository;
import com.cms.helpdesk.management.users.dto.request.ReqEmployeeDTO;
import com.cms.helpdesk.management.users.filter.EmployeeFilter;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.repository.EmployeeRepository;
import com.cms.helpdesk.management.users.repository.PaginationEmployee;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private PaginationEmployee paginate;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RegionRepository regionRepository;

    public ResponseEntity<Object> getEmployees(boolean pageable, int page, int size) {
        Specification<Employee> spec = Specification
                .where(new Filter<Employee>().orderByIdDesc())
                .and(new EmployeeFilter().notIncludeSuperadmin())
                .and(new Filter<Employee>().isNotDeleted());
        if (pageable) {
            Page<Employee> res = paginate.findAll(spec, PageRequest.of(page, size));
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
        } else {
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), null, repo.findAll(spec), null), 1);
        }
    }

    public ResponseEntity<Object> getEmployeeById(String nip) {
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, repo.findById(nip), null), 1);
    }

    public ResponseEntity<Object> getEmployeeByNIP(String nip) {
        Employee employee = getEmployeeByNip(nip);
        if (employee.isRegistered()) {
            throw new UserFoundException("Karyawan sudah mendaftar sebelumnya");
        }
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, employee, null), 1);
    }

    public ResponseEntity<Object> saveEmployee(ReqEmployeeDTO dto) {
        if (repo.findByNip(dto.getNip()).isPresent()) {
            return Response.buildResponse(new GlobalDto(302, null,
                    "Karyawan dengan nip : " + dto.getNip() + " sudah terdaftar", null, null, null), 0);
        }
        Employee employee = buildReqToEmployee(dto, 0L);
        employee.setRegistered(false);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, repo.save(employee), null), 0);
    }

    public ResponseEntity<Object> updateEmployee(ReqEmployeeDTO dto, String nip) {
        Employee employee = getEmployee(nip);
        Employee request = buildReqToEmployee(dto, 0L);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null,
                repo.save(new PatchField<Employee>().fusion(employee, request)), null), 0);
    }

    public ResponseEntity<Object> deleteEmployee(String nip) {
        Employee employee = getEmployee(nip);
        employee.setDeleted(true);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, repo.save(employee), null), 0);
    }

    // action == 1 dengan validasi, action == 0 tanpa dengan validasi
    public Employee buildReqToEmployee(ReqEmployeeDTO dto, Long action) {
        Employee request = new Employee();
        request.setNip(dto.getNip());
        request.setName(dto.getName());
        request.setPhone(dto.getPhone());
        if (dto.getDepartmentId() == null && dto.getBranchId() == null && dto.getRegionId() == null && action == 1) {
            throw new ResourceNotFoundException("Department, Branch dan Region harus dipilih salah satu");
        }
        request.setBranch(getBranch(dto.getBranchId()));
        request.setDepartment(getDepartment(dto.getDepartmentId()));
        request.setRegion(getRegion(dto.getRegionId()));
        return request;
    }

    public Employee getEmployee(String nip) {
        return repo.findById(nip)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id : " + nip + " not found"));
    }

    public Employee getEmployeeByNip(String nip) {
        return repo.findByNip(nip)
                .orElseThrow(() -> new ResourceNotFoundException("Karyawan dengan NIP : " + nip + " tidak ditemukan"));
    }

    public Region getRegion(Long id) {
        if (id == null)
            return null;
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region with id : " + id + " not found"));
    }

    public Branch getBranch(Long id) {
        if (id == null)
            return null;
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch with id : " + id + " not found"));
    }

    public Department getDepartment(Long id) {
        if (id == null)
            return null;
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id : " + id + " not found"));
    }

}
