package dataProvider;

import java.util.ArrayList;
import java.util.List;

import entity.Notes;

public class NotesDataProvider {

    public static List<Notes> getNotes() {
        List<Notes> addNotes = new ArrayList<>();

        addNotes.add(new Notes(1, 1, "Finish reading course material, then start working on " +
                "Calculator Widget Application.", "2020-01-10 09:15:00", "2020-01-10 09:15:00"));
        addNotes.add(new Notes(2, 2, "Study SQL syntax and commands. Download MySQL to laptop to " +
                "practice creating tables w/ stored procedures.", "2020-01-12 14:00:00", "2020-01-12 14:00:00"));
        addNotes.add(new Notes(3, 2, "Study for Scripts and StoredProcedures Unit Exam",
                "2020-01-10 10:15:00", "2020-01-10 10:15:00"));
        addNotes.add(new Notes(4, 3, "Start reading about network routing and look over current IEEE " +
                "protocols and definitions.", "2020-01-13 16:30:00", "2020-01-13 16:30:00"));
        addNotes.add(new Notes(5, 3, "Schedule for CompTIA Security+ Certification Exam and enter " +
                "voucher information with comptia.org", "2020-01-08 08:45:00", "2020-01-08 08:45:00"));
        addNotes.add(new Notes(6, 4, "Create wireframe/prototype for Inventory Management Mobile " +
                "Application", "2019-11-10 09:15:00", "2019-11-10 09:15:00"));
        addNotes.add(new Notes(7, 4, "Read Java OOP reading material.",
                "2019-11-10 09:30:00", "2019-11-10 09:30:00"));
        addNotes.add(new Notes(8, 5, "Prepare for Telecommunications Systems Exam and " +
                "draw mock network diagram.", "2019-11-10 12:15:00", "2019-11-10 12:15:00"));
        addNotes.add(new Notes(9, 5, "Download ARC Ping Tester mobile app and run ping test on " +
                "home network.", "2019-11-22 11:00:00", "2019-11-22 11:00:00"));
        addNotes.add(new Notes(10, 6, "Download MATLAB from course materials section.",
                "2019-11-23 13:30:00", "2019-11-23 13:30:00"));
        addNotes.add(new Notes(11, 6, "Read MATLAB Fundamentals course study materials.",
                "2019-12-13 10:45:00", "2019-12-13 10:45:00"));
        addNotes.add(new Notes(12, 7, "Practice MIPS Assembly functions and apply source code.",
                "2019-12-16 15:15:00", "2019-12-16 15:15:00"));
        addNotes.add(new Notes(13, 7, "Study and know differences between virtual memory & " +
                "cache memory.", "2020-01-07 17:25:00", "2020-01-07 17:25:00"));
        addNotes.add(new Notes(14, 8, "Create predicate logic diagram and stem quantifiers " +
                "to the desired nodes.", "2019-11-05 11:45:00", "2019-11-05 11:45:00"));
        addNotes.add(new Notes(15, 9, "Finish reading course material on Algorithms, Complexity, & " +
                "Recursiveness by Dwyer.", "2019-12-09 10:55:00", "2019-12-09 10:55:00"));
        addNotes.add(new Notes(16, 9, "Study course notes on Dynamic Data Structures and apply heaps, " +
                "stacks, and queues to source code.", "2020-01-10 09:00:00", "2020-01-10 09:00:00"));
        addNotes.add(new Notes(17, 10, "Continue reading Real-Time Embedded Multithreading Using ThreadX.",
                "2019-11-17 14:15:00", "2019-11-17 14:15:00"));
        addNotes.add(new Notes(18, 10, "Build a modern and concurrent practice application for Unix. " +
                "Apply source logic to C++ Grocery Checkout Application.", "2019-11-18 09:15:00", "2019-11-18 09:15:00"));

        return addNotes;
    }

}
