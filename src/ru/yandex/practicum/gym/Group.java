package ru.yandex.practicum.gym;

public class Group {
    //название группы
    private final String title;
    //тип (взрослая или детская)
    private final Age age;
    //длительность (в минутах)
    private final int duration;

    public Group(String title, Age age, int duration) {
        this.title = title;
        this.age = age;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public Age getAge() {
        return age;
    }

    public int getDuration() {
        return duration;
    }
}