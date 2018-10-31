package io.pivotal.pal.tracker;

import java.sql.*;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;


import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository{
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String sql="INSERT INTO time_entries ( project_id, user_id, date, hours) VALUES (?,?,?,?)";

        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, timeEntry.getProjectId());
                ps.setLong(2, timeEntry.getUserId());
                ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                ps.setInt(4, timeEntry.getHours());
                return ps;
            }
        }, generatedKeyHolder);

        return new TimeEntry(generatedKeyHolder.getKey().longValue(),timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours());
    }

    @Override
    public TimeEntry find(Long id) {
       return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{id},
                extractor);

    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", mapper);
    }

    @Override
    public TimeEntry update(Long eq, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                eq);

        return find(eq);
    }

    @Override
    public void delete(Long timeEntryId) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
