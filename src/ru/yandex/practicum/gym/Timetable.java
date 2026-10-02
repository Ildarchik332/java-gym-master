package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        Coach coach = trainingSession.getCoach();
        Group group = trainingSession.getGroup();

        if (dayOfWeek == null || timeOfDay == null || coach == null || group == null) {
            return;
        }

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            daySchedule = new TreeMap<>();
            timetable.put(dayOfWeek, daySchedule);
        }

        List<TrainingSession> sessionsAtTime = daySchedule.get(timeOfDay);
        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            daySchedule.put(timeOfDay, sessionsAtTime);
        }

        sessionsAtTime.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        if (daySchedule == null) {
            return Collections.emptyList();
        }

        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            result.addAll(daySchedule.get(time));
        }

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> ofDayListTreeMap = timetable.get(dayOfWeek);
        if (ofDayListTreeMap == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> trainingSessions = ofDayListTreeMap.get(timeOfDay);
        return trainingSessions != null ? trainingSessions : Collections.emptyList();
    }


    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachTrainingCount = new HashMap<>();
        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> dayEntry : timetable.entrySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> valueTrainingMap = dayEntry.getValue();
            for (Map.Entry<TimeOfDay, List<TrainingSession>> timeEntry : valueTrainingMap.entrySet()) {
                List<TrainingSession> trainingSessionList = timeEntry.getValue();
                for (TrainingSession trainingSession : trainingSessionList) {
                    Coach coach = trainingSession.getCoach();
                    Integer currentCount = coachTrainingCount.get(coach);
                    if (currentCount == null) {
                        coachTrainingCount.put(coach, 1);
                    } else {
                        coachTrainingCount.put(coach, currentCount + 1);
                    }
                }
            }
        }
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachTrainingCount.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(Comparator.comparingInt(CounterOfTrainings::getCount).reversed());

        return result;
    }
}
