package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;
import com.techelevator.projects.model.Employee;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departments = new ArrayList<Department>();
		String sqlGetAllDepartments = "SELECT name, department_id FROM department ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetAllDepartments);
		while(results.next()) {
			String name = results.getString("name");
			long id = results.getLong("department_id");
			Department department = new Department();
			department.setName(name);
			department.setId(id);
			departments.add(department);
		}
		return departments;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> departments = new ArrayList<Department>();
		String sqlSearchDepartmentsByName = "SELECT name FROM department WHERE name ILIKE ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlSearchDepartmentsByName, nameSearch);
		while(results.next()) {
			String name = results.getString("name");
			Department department = new Department();
			department.setName(name);
			departments.add(department);
		}
		return departments;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String sqlSaveDepartment = "UPDATE department " +
				"SET name = ? WHERE department_id = ? ";
		this.jdbcTemplate.update(sqlSaveDepartment, updatedDepartment.getName(), updatedDepartment.getId());
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		Department department = new Department();
		String sqlCreateDepartment = "INSERT INTO department (name) VALUES ( ? ) ";
		this.jdbcTemplate.update(sqlCreateDepartment, newDepartment.getName());
		return newDepartment;
	}

	@Override
	public Department getDepartmentById(Long id) {
		Department department = new Department();
		String sqlGetDepartmentById = "SELECT name FROM department WHERE department_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDepartmentById, id);
		while(results.next()) {
			long departmentId = results.getLong("department_id");
			department.setId(departmentId);
		}
		return department;
	}

}


