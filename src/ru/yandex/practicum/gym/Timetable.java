package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, TrainingSession>> timetableMap = new HashMap<>();

    public void addNewTrainingSession(TrainingSession newTrainingSession) {
        if (newTrainingSession != null) {
            TreeMap<TimeOfDay, TrainingSession> sessions =
                    timetableMap.computeIfAbsent(newTrainingSession.getDayOfWeek(), k -> new TreeMap<>());

            if (!sessions.containsKey(newTrainingSession.getTimeOfDay())
                    && !isOverlapNewSessionByTime(sessions, newTrainingSession)) {
                sessions.put(newTrainingSession.getTimeOfDay(), newTrainingSession);
            }
        }
    }

    private boolean isOverlapNewSessionByTime(NavigableMap<TimeOfDay, TrainingSession> sessions,
                                              TrainingSession newTrainingSession) {
        if (sessions.isEmpty()) return false;

        boolean isOverlapPrevSession = false;
        boolean isOverlapNextSession = false;

        Map.Entry<TimeOfDay, TrainingSession> prevSessionEntry =
                sessions.floorEntry(newTrainingSession.getTimeOfDay());
        Map.Entry<TimeOfDay, TrainingSession> nextSessionEntry =
                sessions.ceilingEntry(newTrainingSession.getTimeOfDay());

        int startNewSession = newTrainingSession.getTimeOfDay().convertToMinutes();

        if (prevSessionEntry != null) {
            TrainingSession prevSession = prevSessionEntry.getValue();
            int finishPrevSession =
                    prevSession.getTimeOfDay().convertToMinutes() + prevSession.getGroup().getDuration();

            isOverlapPrevSession = finishPrevSession > startNewSession;
        }
        if (nextSessionEntry != null) {
            TrainingSession nextSession = nextSessionEntry.getValue();
            int startNextSession = nextSession.getTimeOfDay().convertToMinutes();
            int durationNewSession = newTrainingSession.getGroup().getDuration();

            isOverlapNextSession = startNewSession + durationNewSession > startNextSession;
        }

        return isOverlapPrevSession || isOverlapNextSession;
    }

    public Map<TimeOfDay, TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return Collections.unmodifiableMap(timetableMap.getOrDefault(dayOfWeek, new TreeMap<>()));
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