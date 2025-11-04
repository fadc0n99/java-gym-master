package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.gym.*;

import java.util.List;

class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> trainingSessionsForMonday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> trainingSessionsForTuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, trainingSessionsForMonday.size());

        //Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, trainingSessionsForTuesday.size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> trainingSessionsForMonday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(1, trainingSessionsForMonday.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> trainingSessionsForThursday =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        Assertions.assertEquals(2, trainingSessionsForThursday.size());
        Assertions.assertEquals(new TimeOfDay(13, 0), trainingSessionsForThursday.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), trainingSessionsForThursday.get(1).getTimeOfDay());

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> trainingSessionsForTuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertEquals(0, trainingSessionsForTuesday.size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        TrainingSession trainingSessionAt13 =
                timetable.getTrainingSessionForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        Assertions.assertEquals(13, trainingSessionAt13.getTimeOfDay().getHours());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        TrainingSession trainingSessionAt14 =
                timetable.getTrainingSessionForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        Assertions.assertNull(trainingSessionAt14);
    }

    @Test
    void testGetCountByCoaches_ShouldReturnSortedByCountDescending() {
        // Arrange
        Timetable timetable = new Timetable();

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach3 = new Coach("Малышев", "Максим", "Владимирович");

        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));

        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0)));

        timetable.addNewTrainingSession(new TrainingSession(groupChild, coach3,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupChild, coach3,
                DayOfWeek.SATURDAY, new TimeOfDay(18, 0)));

        List<CounterOfCoachTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals("Николай", result.get(0).coach().getName());
        Assertions.assertEquals(3, result.get(0).counter());

        Assertions.assertEquals("Максим", result.get(1).coach().getName());
        Assertions.assertEquals(2, result.get(1).counter());

        Assertions.assertEquals("Иван", result.get(2).coach().getName());
        Assertions.assertEquals(1, result.get(2).counter());
    }

    @Test
    void testGetCountByCoaches_ShouldReturnEmptyListWhenNoSessions() {
        Timetable timetable = new Timetable();

        List<CounterOfCoachTrainings> result = timetable.getCountByCoaches();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetCountByCoaches_ShouldReturnSingleEntryWhenOneSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));

        List<CounterOfCoachTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Николай", result.getFirst().coach().getName());
        Assertions.assertEquals(1, result.getFirst().counter());
    }

    @Test
    void testAddNewTrainingSessionForWhenOverlapWithPrevious() {
        Timetable timetable = new Timetable();

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 60);

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 10)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 10)));

        List<TrainingSession> trainingSessionList = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(2, trainingSessionList.size());
    }

    @Test
    void testAddNewTrainingSessionForWhenOverlapWithNext() {
        Timetable timetable = new Timetable();

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 60);

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 30)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(11, 10)));

        List<TrainingSession> trainingSessionList = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(2, trainingSessionList.size());
    }

}
