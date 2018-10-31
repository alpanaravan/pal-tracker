package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {

        this.timeEntryRepository = timeEntryRepository;
    }
    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry){
        TimeEntry entry=timeEntryRepository.create(timeEntry);

        return new ResponseEntity<TimeEntry>(entry, null, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry entry=timeEntryRepository.find(id);
        if(entry==null){
            return new ResponseEntity<TimeEntry>(entry, null, HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<TimeEntry>(entry, null, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> entryList=timeEntryRepository.list();
        return new ResponseEntity<List<TimeEntry>>(entryList, null, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry expected) {
        TimeEntry entry=timeEntryRepository.update(id,expected);
        if(entry==null){
            return new ResponseEntity<TimeEntry>(entry, null, HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<TimeEntry>(entry, null, HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        timeEntryRepository.delete(id);


        return new ResponseEntity<TimeEntry>(null, null, HttpStatus.NO_CONTENT);
    }
}

