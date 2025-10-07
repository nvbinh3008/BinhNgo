namespace SchoolMvc.Models;

/// <summary>
/// Join entity that stores enrollment information and the mark for a student in a course.
/// </summary>
public class StudentCourse
{
    /// <summary>
    /// Composite key part referencing the student.
    /// </summary>
    public int StudentId { get; set; }

    /// <summary>
    /// Composite key part referencing the course.
    /// </summary>
    public int CourseId { get; set; }

    /// <summary>
    /// Optional mark obtained by the student.
    /// </summary>
    public double? Mark { get; set; }

    /// <summary>
    /// Navigation property to the student.
    /// </summary>
    public Student? Student { get; set; }

    /// <summary>
    /// Navigation property to the course.
    /// </summary>
    public Course? Course { get; set; }
}
