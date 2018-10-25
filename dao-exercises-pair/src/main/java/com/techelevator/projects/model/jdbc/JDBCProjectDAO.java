package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Project> getAllActiveProjects() {

		List<Project> activeProjects = new ArrayList<Project>();
		String sqlGetAllActiveProjects = "SELECT name, project_id FROM project WHERE to_date IS NULL OR from_date IS NULL ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllActiveProjects);
		while(results.next()) {
			Project theProject = new Project();
			String projectName = results.getString("name");
			Long projectId = results.getLong("project_id");
			theProject.setName(projectName);
			theProject.setId(projectId);
			activeProjects.add(theProject);
		}
		return activeProjects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sql = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ? ";
		this.jdbcTemplate.update(sql, projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sql = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?) ";
		this.jdbcTemplate.update(sql, projectId, employeeId);
	}
	
}
