package dataProvider;

import java.util.ArrayList;
import java.util.List;

import entity.Courses;

public class CourseDataProvider {

    public static List<Courses> getCourses() {
        List<Courses> addCourses = new ArrayList<>();

        addCourses.add(new Courses(1, 1, 1,
                1, "Software II - Advanced Java Concepts – C195",
                "In Progress", "In this course you will learn about and put into action lambda expressions, " +
                "collections, input/output, advanced error handling, and " +
                "the newest features of Java 8 to develop software that meets business requirements. " +
                "This course requires intermediate expertise in " +
                "object-oriented programming and the Java language.","Susan Walker", "(555) 923-8920",
                "swalker@wgu.edu", "11-02-2019", "04-27-2020"));
        addCourses.add(new Courses(2, 2, 2, 0,
                "Data Management - Foundations - C175", "Plan to Take",
                "This course introduces students to the concepts and terminology used in the field of data management. " +
                        "They will be introduced to Structured Query Language (SQL) and will learn how to use Data Definition " +
                        "Language (DDL) and Data Manipulation Language (DML) commands to define, retrieve, and manipulate data. ",
                "Christian Bale", "(555) 650-4483", "cbale@wgu.edu",
                "06-04-2020", "09-19-2020"));
        addCourses.add(new Courses(3, 3, 2, 0,
                "Network and Security - Foundations - C172", "Plan to Take",
                "This course introduces students to the components of a computer network and the concept and role of communication protocols. " +
                        "The course covers widely used categorical classifications of networks (e.g., LAN, MAN, WAN, WLAN, PAN, SAN, CAN, and VPN) " +
                        "as well as network topologies, physical devices, and layered abstraction.",
                "Terrance Ford", "(555) 722-0554", "tford@wgu.edu",
                "11-03-2020", "03-31-2021"));
        addCourses.add(new Courses(4, 4, 3, 1, "Mobile Application Development - C196",
                "Dropped","This course will help you get a jump start by teaching you the fundamental principles and programming " +
                "techniques you'll need to develop applications for the Android operating system. ", "Felicia Snow", "(555) 472-8873",
                "fsnow@wgu.edu", "07-05-2021", "10-12-2021"));
        addCourses.add(new Courses(5, 1, 1, 1, "Telecommunications - D321",
                "In Progress","This is a required class, one of the first taken by students enrolled in telecom programs. With lectures and lab-based work on " +
                "telecommunications theory and on the application of technical and practical skills, students get a solid understanding of telecom systems and architecture. Students " +
                "discuss network technology like fiber optics, Voice over Internet Protocol (VoIP), switching, routing and benchmark analysis of network performance.",
                "Al Pacino", "(555) 334-5677",
                "apacino@wgu.edu", "01-06-2020", "04-17-2020"));
        addCourses.add(new Courses(6, 2, 2,
                0, "Introduction to Engineering and Computer Science - C220",
                "Plan to Take", "This course is an overview of ECS curricula, connections among ECS fields and to the basics of sciences and other fields. " +
                "Basic study, problem solving and other skills needed to succeed as an ECS major. Engineering design and quantitative methods using MATLAB. " +
                "Multi-disciplinary team projects designed to replicate decision processes in real-world situations.","Susan Walker", "(555) 923-8920",
                "swalker@wgu.edu", "05-04-2020", "09-26-2020"));
        addCourses.add(new Courses(7, 3, 2, 0,
                "Computer Architecture - C412", "Plan to Take","This course introduces the concepts of computer architecture " +
                "by going through multiple levels of abstraction, and the numbering systems and their basic computations. It focuses on the instruction-set architecture " +
                "of the MIPS machine, including MIPS assembly programming, translation between MIPS and C, and between MIPS and machine code.",
                "Christian Bale", "(555) 650-4483", "cbale@wgu.edu",
                "12-04-2020", "02-19-2021"));
        addCourses.add(new Courses(8, 1, 2, 0,
                "Mathematical Foundations of Software Engineering - D300", "Plan to Take","Boolean logic, first-order logic, models " +
                "of first-order logic. Introduction to program verification, applications in Software Engineering. Completeness Theorem. Regular expressions, regular sets, " +
                "finite-state machines, and applications in Software Engineering. Graph Theory, graph algorithms. Statecharts, Petri Nets and their role in Software Engineering.",
                "Terrance Ford", "(555) 722-0554", "tford@wgu.edu",
                "11-15-2019", "03-27-2020"));
        addCourses.add(new Courses(9, 4, 2, 0,
                "Data Structures and Introduction to Algorithmic Analysis - D431",
                "Plan to Take", "Analysis of algorithms including time complexity and Big-O notation. Analysis of stacks, " +
                "queues, and trees, including B-trees. Heaps, hashing, and advanced sorting techniques. Disjoint sets and graphs. Course emphasizes design and implementation.",
                "Felicia Snow", "(555) 472-8873",
                "fsnow@wgu.edu", "06-05-2021", "10-17-2021"));
        addCourses.add(new Courses(10, 3, 3, 1,
                "C/C++ Programming in a UNIX Environment - D954",
                "Dropped", "Advanced programming techniques utilizing procedural and object oriented programming in a UNIX environment. " +
                "Topics include file input and output, implementation of strings, stacks, queues, lists, and trees, and dynamic memory allocation/management.",
                "Al Pacino", "(555) 334-5677",
                "apacino@wgu.edu", "01-06-2021", "04-18-2021"));

        return addCourses;
    }

}
