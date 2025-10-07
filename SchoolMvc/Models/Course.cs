using System.ComponentModel.DataAnnotations;

namespace SchoolMvc.Models;

/// <summary>
/// Represents a course instance that students can enroll in.
/// </summary>
public class Course
{
    /// <summary>
    /// Primary key.
    /// </summary>
    public int Id { get; set; }

    /// <summary>
    /// Title of the course (e.g., "Spring 2024 Algebra").
    /// </summary>
    [Required]
    [StringLength(100)]
    public string Title { get; set; } = string.Empty;

    /// <summary>
    /// Start date of the course.
    /// </summary>
    [DataType(DataType.Date)]
    public DateOnly StartDate { get; set; }

    /// <summary>
    /// End date of the course.
    /// </summary>
    [DataType(DataType.Date)]
    public DateOnly EndDate { get; set; }

    /// <summary>
    /// Foreign key pointing to the subject.
    /// </summary>
    [Display(Name = "Subject")]
    public int SubjectId { get; set; }

    /// <summary>
    /// Navigation property for the subject.
    /// </summary>
    public Subject? Subject { get; set; }

    /// <summary>
    /// Many-to-many relationship with students through StudentCourse.
    /// </summary>
    public ICollection<StudentCourse> StudentCourses { get; set; } = new List<StudentCourse>();
}
