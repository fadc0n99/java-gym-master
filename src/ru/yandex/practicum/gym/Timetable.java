package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, TrainingSession>> timetableMap = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (trainingSession != null) {
            Map<TimeOfDay, TrainingSession> sessions =
                    timetableMap.computeIfAbsent(trainingSession.getDayOfWeek(), k -> new TreeMap<>());

            if (!sessions.containsKey(trainingSession.getTimeOfDay())) {
                sessions.put(trainingSession.getTimeOfDay(), trainingSession);
            }
        }
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (dayOfWeek != null) {
            return timetableMap.getOrDefault(dayOfWeek, new TreeMap<>())
                    .values()
                    .stream()
                    .toList();
        }

        return Collections.emptyList();
    }

    public TrainingSession getTrainingSessionForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetableMap.getOrDefault(dayOfWeek, new TreeMap<>()).get(timeOfDay);
    }

    public List<CounterOfCoachTrainings> getCountByCoaches() {
        Map<Coach, Integer> countByCoaches = calculateCountByCoachesToMap();

        if (!countByCoaches.isEmpty()) {
            List<CounterOfCoachTrainings> counterOfCoachTrainings = new ArrayList<>();

            for (Map.Entry<Coach, Integer> coachIntegerEntry : countByCoaches.entrySet()) {
                counterOfCoachTrainings.add(
                        new CounterOfCoachTrainings(coachIntegerEntry.getKey(), coachIntegerEntry.getValue())
                );
            }

            counterOfCoachTrainings.sort(Comparator.comparing(CounterOfCoachTrainings::counter).reversed());

            return counterOfCoachTrainings;
        }

        return Collections.emptyList();
    }

    private Map<Coach, Integer> calculateCountByCoachesToMap() {
        Map<Coach, Integer> coachIntegerMap = new HashMap<>();

        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, TrainingSession>> sessionsOfDayEntry : timetableMap.entrySet()) {
            for (TrainingSession session : sessionsOfDayEntry.getValue().values()) {
                Coach coachFromSession = session.getCoach();

                coachIntegerMap.put(
                        coachFromSession,
                        coachIntegerMap.getOrDefault(coachFromSession, 0) + 1
                );
            }
        }
        return coachIntegerMap;
    }
}