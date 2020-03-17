package dataProvider;

import java.util.ArrayList;
import java.util.List;

import entity.ObjectiveAssessments;
import entity.PerformanceAssessments;

public class AssessmentDataProvider {

    public static List<PerformanceAssessments> getPerfAssessments() {

        List<PerformanceAssessments> addPerfAssessments = new ArrayList<>();

        addPerfAssessments.add(new PerformanceAssessments(1, 1, "Java w/SQL Assessment",
                "4-25-2020", 1));
        addPerfAssessments.add(new PerformanceAssessments(2, 1, "Calculator Widget Application",
                "3-29-2020", 1));
        addPerfAssessments.add(new PerformanceAssessments(3, 2, "SQL Commands Comprehension Exam",
                "7-07-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(4, 2, "Scripts and StoredProcedures Unit Exam",
                "9-08-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(5, 3, "Risk Mitigation Comprehension Exam",
                "3-21-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(6, 3, "CompTIA Security+ Certification Exam",
                "3-29-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(7, 4, "Inventory Management Mobile Application",
                "9-30-2021", 3));
        addPerfAssessments.add(new PerformanceAssessments(8, 5, "TCP/IP Fundamentals Test",
                "3-27-2020", 1));
        addPerfAssessments.add(new PerformanceAssessments(9, 5, "Telecommunications Systems Exam",
                "4-06-2020", 1));
        addPerfAssessments.add(new PerformanceAssessments(10, 6, "Differential Equations Assessment",
                "8-11-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(11, 6, "MATLAB Fundamentals Exam",
                "7-22-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(12, 7, "MIPS Codes/Functions Assessment",
                "4-15-2021", 2));
        addPerfAssessments.add(new PerformanceAssessments(13, 7, "Caching, Virtual Memory, & Pipelining Review",
                "4-03-2021", 2));
        addPerfAssessments.add(new PerformanceAssessments(14, 8, "Predicates, Quantifiers Injection Exam",
                "4-08-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(15, 8, "Algorithms, Complexity, & Recursiveness Assessment",
                "4-26-2020", 2));
        addPerfAssessments.add(new PerformanceAssessments(16, 9, "Algorithmic Analysis of List/Stacks, Heaps, & Queues",
                "9-25-2021", 2));
        addPerfAssessments.add(new PerformanceAssessments(17, 10, "Semaphores: Producer Consumer Problem",
                "3-28-2021", 3));
        addPerfAssessments.add(new PerformanceAssessments(18, 10, "C++ Grocery Checkout Application",
                "4-18-2021", 3));
        return addPerfAssessments;
    }

    public static List<ObjectiveAssessments> getObjAssessments() {
        List<ObjectiveAssessments> addObjAssessments = new ArrayList<>();

        addObjAssessments.add(new ObjectiveAssessments(1, 1, "Java SE 11 Programmer I Certification Exam",
                "4-19-2020", 1));
        addObjAssessments.add(new ObjectiveAssessments(2, 2, "MTA Database Fundamentals (98-364)",
                "7-07-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(3, 2, "MySQL 5.6 Developer | 1Z0-882",
                "9-08-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(4, 3, "Network Topologies Assessment",
                "4-16-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(5, 3, "Network/Configurations Protocol Exam",
                "4-21-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(6, 4, "Game Theory & Physics Model",
                "9-25-2021", 3));
        addObjAssessments.add(new ObjectiveAssessments(7, 5, "Wireless Telecommunications OBJ-Assessment",
                "4-09-2020", 1));
        addObjAssessments.add(new ObjectiveAssessments(8, 5, "IP Networks, IP Addresses, Packets, and Routers",
                "4-17-2020", 1));
        addObjAssessments.add(new ObjectiveAssessments(9, 6, "Dynamic Programming w/ Algorithms",
                "8-30-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(10, 6, "Computational Models Assessment",
                "8-22-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(11, 7, "Data Dependency and Hazards Exam",
                "4-19-2021", 2));
        addObjAssessments.add(new ObjectiveAssessments(12, 7, "Cache Mapping Protocols Assessment",
                "4-26-2021", 2));
        addObjAssessments.add(new ObjectiveAssessments(13, 8, "Proof Sets Definition Exam",
                "3-28-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(14, 8, "Quantifiers & Logic Translation Model Build",
                "4-24-2020", 2));
        addObjAssessments.add(new ObjectiveAssessments(15, 9, "Design and Analysis of Algorithms Test",
                "9-20-2021", 2));
        addObjAssessments.add(new ObjectiveAssessments(16, 10, "My Linux Application OBJ-331",
                "3-03-2021", 3));
        addObjAssessments.add(new ObjectiveAssessments(17, 10, "Template Builder w/ Processor",
                "4-18-2021", 3));
        return addObjAssessments;
    }

}
