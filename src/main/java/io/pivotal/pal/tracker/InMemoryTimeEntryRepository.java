package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
     Map<Long,TimeEntry> timeEntryMap=new HashMap<Long, TimeEntry>();
     Long id=0L;



    public TimeEntry create(TimeEntry timeEntry){
        TimeEntry createdTimeEntry = new TimeEntry(++id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        timeEntryMap.put(createdTimeEntry.getId() ,createdTimeEntry);

        return createdTimeEntry;
    };

    public TimeEntry find(long timeEntryId) {
        return timeEntryMap.get(timeEntryId);
    }

    public List<TimeEntry> list(){

     return new ArrayList<TimeEntry>(timeEntryMap.values());

    };

    public TimeEntry update(long eq, TimeEntry timeEntry){
        TimeEntry updatedTimeEntry = new TimeEntry(eq, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        timeEntryMap.put(updatedTimeEntry.getId() ,updatedTimeEntry);

        return updatedTimeEntry;
    };

    public void delete(long timeEntryId){

        timeEntryMap.remove(timeEntryId);

    };


}
