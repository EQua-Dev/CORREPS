package com.schoolprojects.corrreps.utils

import com.schoolprojects.corrreps.models.Course
import com.schoolprojects.corrreps.models.Lecturer


// Create a list of dummy lecturers
val lecturers = listOf(
    Lecturer(
        lecturerId = "L001",
        lecturerName = "Dr. John Smith",
        lecturerPhone = "555-1234",
        lecturerEmail = "john.smith@university.edu"
    ),
    Lecturer(
        lecturerId = "L002",
        lecturerName = "Prof. Emily Johnson",
        lecturerPhone = "555-5678",
        lecturerEmail = "emily.johnson@university.edu"
    ),
    Lecturer(
        lecturerId = "L003",
        lecturerName = "Dr. Michael Brown",
        lecturerPhone = "555-8765",
        lecturerEmail = "michael.brown@university.edu"
    ),
    Lecturer(
        lecturerId = "L004",
        lecturerName = "Dr. Laura Wilson",
        lecturerPhone = "555-4321",
        lecturerEmail = "laura.wilson@university.edu"
    ),
    Lecturer(
        lecturerId = "L005",
        lecturerName = "Dr. Sarah Martinez",
        lecturerPhone = "555-9987",
        lecturerEmail = "sarah.martinez@university.edu"
    )
)

// Create a list of dummy courses for various levels and semesters
val courses = listOf(
    // 100 Level Courses
    Course(
        courseCode = "CS101",
        courseTitle = "Introduction to Computer Science",
        courseBrief = "An overview of computer science concepts and programming.",
        lecturer = "L001",
        creditUnits = "3",
        courseLevel = "100 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS102",
        courseTitle = "Introduction to Programming",
        courseBrief = "Introduction to programming using Python.",
        lecturer = "L002",
        creditUnits = "4",
        courseLevel = "100 Level",
        courseSemester = "Second Semester"
    ),
    Course(
        courseCode = "CS103",
        courseTitle = "Discrete Mathematics",
        courseBrief = "Basic concepts of discrete mathematics in computer science.",
        lecturer = "L003",
        creditUnits = "3",
        courseLevel = "100 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS104",
        courseTitle = "Digital Logic Design",
        courseBrief = "Fundamentals of digital systems and logic design.",
        lecturer = "L004",
        creditUnits = "3",
        courseLevel = "100 Level",
        courseSemester = "Second Semester"
    ),

    // 200 Level Courses
    Course(
        courseCode = "CS201",
        courseTitle = "Data Structures and Algorithms",
        courseBrief = "Study of data structures and algorithmic techniques.",
        lecturer = "L001",
        creditUnits = "4",
        courseLevel = "200 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS202",
        courseTitle = "Computer Architecture",
        courseBrief = "Introduction to the architecture of computer systems.",
        lecturer = "L002",
        creditUnits = "3",
        courseLevel = "200 Level",
        courseSemester = "Second Semester"
    ),
    Course(
        courseCode = "CS203",
        courseTitle = "Object-Oriented Programming",
        courseBrief = "Object-oriented programming concepts using Java.",
        lecturer = "L003",
        creditUnits = "4",
        courseLevel = "200 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS204",
        courseTitle = "Operating Systems",
        courseBrief = "Study of operating system concepts and design.",
        lecturer = "L004",
        creditUnits = "4",
        courseLevel = "200 Level",
        courseSemester = "Second Semester"
    ),

    // 300 Level Courses
    Course(
        courseCode = "CS301",
        courseTitle = "Database Systems",
        courseBrief = "Introduction to database design and management systems.",
        lecturer = "L001",
        creditUnits = "4",
        courseLevel = "300 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS302",
        courseTitle = "Software Engineering",
        courseBrief = "Software development processes and methodologies.",
        lecturer = "L002",
        creditUnits = "3",
        courseLevel = "300 Level",
        courseSemester = "Second Semester"
    ),
    Course(
        courseCode = "CS303",
        courseTitle = "Computer Networks",
        courseBrief = "Study of network architectures and protocols.",
        lecturer = "L003",
        creditUnits = "3",
        courseLevel = "300 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS304",
        courseTitle = "Artificial Intelligence",
        courseBrief = "Introduction to AI concepts and applications.",
        lecturer = "L004",
        creditUnits = "4",
        courseLevel = "300 Level",
        courseSemester = "Second Semester"
    ),

    // 400 Level Courses
    Course(
        courseCode = "CS401",
        courseTitle = "Advanced Database Systems",
        courseBrief = "In-depth study of advanced database concepts.",
        lecturer = "L001",
        creditUnits = "4",
        courseLevel = "400 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS402",
        courseTitle = "Distributed Systems",
        courseBrief = "Study of distributed computing systems and applications.",
        lecturer = "L002",
        creditUnits = "4",
        courseLevel = "400 Level",
        courseSemester = "Second Semester"
    ),
    Course(
        courseCode = "CS403",
        courseTitle = "Machine Learning",
        courseBrief = "Introduction to machine learning techniques and models.",
        lecturer = "L003",
        creditUnits = "4",
        courseLevel = "400 Level",
        courseSemester = "First Semester"
    ),
    Course(
        courseCode = "CS404",
        courseTitle = "Computer Graphics",
        courseBrief = "Study of computer graphics principles and techniques.",
        lecturer = "L004",
        creditUnits = "3",
        courseLevel = "400 Level",
        courseSemester = "Second Semester"
    )
)